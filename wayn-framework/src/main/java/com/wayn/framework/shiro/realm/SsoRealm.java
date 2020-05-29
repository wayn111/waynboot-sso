package com.wayn.framework.shiro.realm;

import com.caucho.hessian.client.HessianProxyFactory;
import com.wayn.common.domain.User;
import com.wayn.common.service.RoleMenuService;
import com.wayn.common.service.UserRoleService;
import com.wayn.common.service.UserService;
import com.wayn.common.util.json.JsonUtil;
import com.wayn.framework.redis.RedisOpts;
import com.wayn.framework.shiro.token.SsoToken;
import com.wayn.ssocore.entity.SsoUser;
import com.wayn.ssocore.service.AuthcationRpcService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.util.Set;

public class SsoRealm extends AuthorizingRealm {

    /**
     * 用户服务
     */
    @Autowired
    private UserService userService;
    /**
     * 用户角色服务
     */
    @Autowired
    private UserRoleService userRoleService;
    /**
     * 角色菜单服务
     */
    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private RedisOpts redisOpts;

    private AuthcationRpcService authcationRpcService;

    private String ssoServerUrl;

    public String getSsoServerUrl() {
        return ssoServerUrl;
    }

    public SsoRealm setSsoServerUrl(String ssoServerUrl) {
        this.ssoServerUrl = ssoServerUrl;
        return this;
    }

    @Override
    protected void onInit() {
        super.onInit();
        if (authcationRpcService == null) {
            try {
                authcationRpcService = (AuthcationRpcService) new HessianProxyFactory().create(AuthcationRpcService.class,
                        ssoServerUrl + "/rpc/authcationRpcService");
            } catch (MalformedURLException e) {
                throw new RuntimeException("ssoRealm:authcationRpcService初始化失败！");
            }
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User sysUser = (User) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles = userRoleService.findRolesByUid(sysUser.getId());
        Set<String> permissions = roleMenuService.findMenusByUid(sysUser.getId());
        info.setRoles(roles);
        info.setStringPermissions(permissions);
        return info;
    }

    @SneakyThrows
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        SsoToken ssoToken = (SsoToken) authenticationToken;
        if (ssoToken == null) {
            return null;
        }

        String token = ssoToken.getToken();
        if (StringUtils.isBlank(token)) {
            return null;
        }
        boolean validate = authcationRpcService.validateAndRefresh(token);
        if (validate) {
            SsoUser ssoUser = JsonUtil.unmarshal(redisOpts.get(token.getBytes()), SsoUser.class);
            User dbUser = userService.getById(ssoUser.getId());
            ssoToken.setUsername(ssoUser.getUserName());
            ssoToken.setPassword(ssoUser.getPassword().toCharArray());
            // 盐值加密
            ByteSource byteSource = ByteSource.Util.bytes(dbUser.getUserName());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(dbUser, dbUser.getPassword(), byteSource, getName());
            return info;
        }
        return null;
    }

    public void clearCachedAuthorizationInfo() {
        doClearCache(SecurityUtils.getSubject().getPrincipals());
        clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
