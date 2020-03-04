package com.wayn.framework.shiro.credentials;

import com.wayn.common.util.date.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.data.redis.cache.RedisCache;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义密码验证服务
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MyCredentialsMatcher extends HashedCredentialsMatcher {


    private RedisCache passwordRetryCache;

    private Integer retryCount;

    /**
     * 检查密码重试次数，防止暴力破解
     *
     * @param token
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String userName = (String) token.getPrincipal();
        //retry count + 1
        Object o = passwordRetryCache.get(userName) == null ? null : passwordRetryCache.get(userName).get();
        if (o == null) {
            passwordRetryCache.put(userName, new AtomicInteger(0));
        }
        AtomicInteger count = (AtomicInteger) o;
        if (count != null && count.incrementAndGet() > retryCount) {
            // 计算过期至今的时间
            Duration duration = passwordRetryCache.getCacheConfiguration().getTtl();
            String timeBefore = DateUtils.getTimeAfter(new Date(duration.toMillis()));
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException("该账号密码重试次数过多，请在" + timeBefore + "重试");
        }
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //clear retry count
            passwordRetryCache.evict(userName);
        }
        return matches;
    }
}
