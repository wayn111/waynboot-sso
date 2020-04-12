package com.wayn.admin.controller.profile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wayn.common.annotation.RepeatSubmit;
import com.wayn.common.base.BaseController;
import com.wayn.common.domain.Dept;
import com.wayn.common.domain.User;
import com.wayn.common.service.DeptService;
import com.wayn.common.service.UserService;
import com.wayn.common.util.R;
import com.wayn.common.util.file.FileUploadUtil;
import com.wayn.common.util.http.HttpUtil;
import com.wayn.common.util.shiro.util.ShiroUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人资料处理控制器
 */
@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {

    private static final String PREFIX = "profile";

    @Autowired
    private DeptService deptService;

    @Autowired
    private UserService userService;

    @Value("${wayn.uploadDir}")
    private String uploadDir;

    @GetMapping
    public String profile(Model model) {
        User curUser = getCurUser();
        model.addAttribute("user", curUser);
        List<String> deptNames = new ArrayList<>();
        Dept dept = deptService.getById(curUser.getDeptId());
        deptNames.add(dept.getDeptName());
        while (dept.getPid() != 0) {
            dept = deptService.getById(dept.getPid());
            deptNames.add(dept.getDeptName());
        }
        model.addAttribute("deptName", StringUtils.join(deptNames, " / "));
        return PREFIX + "/profile";
    }

    @GetMapping("/avatar")
    public String avatar(Model model) {
        model.addAttribute("imgSrc", getCurUser().getUserImg());
        return PREFIX + "/avatar";
    }

    /**
     * 更新个人资料
     *
     * @param request
     * @param user
     * @return
     */
    @RepeatSubmit
    @ResponseBody
    @PostMapping("updateUser")
    public R updateUser(HttpServletRequest request, User user) {
        userService.updateById(user);
        ShiroUtil.updatePrincipal(userService.getById(user.getId()));
        return R.success("修改用户信息成功！");
    }

    /**
     * 判断旧密码是否正确
     *
     * @param oldPassword
     * @return
     */
    @ResponseBody
    @PostMapping("judgeOldPasswordSuccess")
    public boolean judgeOldPasswordSuccess(String oldPassword) {
        // 获取加密后的密码
        String password = ShiroUtil.md5encrypt(oldPassword, userService.getById(getCurUserId()).getUserName());
        List<User> users = userService.list(new QueryWrapper<User>().eq("password", password));
        return users.size() > 0;
    }

    @ResponseBody
    @PostMapping("userResetPwd")
    public R userResetPwd(String newPassword) {
        String password = ShiroUtil.md5encrypt(newPassword, userService.getById(getCurUserId()).getUserName());
        userService.update().set("password", password).eq("id", getCurUserId()).update();
        return R.success("修改用户密码成功！");
    }


    /**
     * 更新头像照片
     *
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("updateAvatar")
    public R updateAvatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        // 上传文件路径
        String filePath = uploadDir + "/avatar";
        String fileName = FileUploadUtil.uploadFile(file, filePath);
        // Thumbnails.of(filePath + "/" + fileName).size(64, 64).toFile(new File(filePath, fileName));
        String requestUrl = HttpUtil.getRequestContext(request);
        String url = requestUrl + "/upload/avatar/" + fileName;
        userService.update().set("userImg", url).eq("id", getCurUserId()).update();
        ShiroUtil.updatePrincipal(userService.getById(getCurUserId()));
        return R.success("上传头像成功！").add("imgSrc", url);
    }
}
