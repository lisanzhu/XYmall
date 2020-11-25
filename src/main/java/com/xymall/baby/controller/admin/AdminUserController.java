package com.xymall.baby.controller.admin;

import com.xymall.baby.common.ServiceResultEnum;
import com.xymall.baby.entity.AdminUser;
import com.xymall.baby.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminUserController {


    @Autowired
    AdminUserService adminUserService;


    @GetMapping({"/main"})
    public String test(){return "index";}

    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName, @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode, HttpSession session){
        if (StringUtils.isEmpty(verifyCode)) {
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            session.setAttribute("errorMsg", "验证码错误");
            System.out.println("验证码错误");
            return "admin/login";
        }
        AdminUser adminUser=adminUserService.login(userName, password);
        if(adminUser!=null){
            session.setAttribute("loginUser", adminUser.getUserName());
            session.setAttribute("loginUserId", adminUser.getUserId());
            //session过期时间设置为7200秒 即两小时
            session.setMaxInactiveInterval(60 * 60 * 2);
            return "admin/index";
        }else{
            session.setAttribute("errorMsg", "查询无此用户");
            return "admin/login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        request.setAttribute("categoryCount", 0);
        request.setAttribute("blogCount", 0);
        request.setAttribute("linkCount", 0);
        request.setAttribute("tagCount", 0);
        request.setAttribute("commentCount", 0);
        request.setAttribute("path", "index");
        return "admin/index";
    }
    /* 这里注意我们的用户名就是登陆的时候的用户名，不应该允许修改，因为如果很多用户同时进行修改用户名
    的时候，会导致我们同时进行全部数据库的查找，效率大大下降，同时会出现相同用户名的情况。
    */
    @GetMapping("/profile")
    public String profile(HttpServletRequest request){
        int userId=(int) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser=adminUserService.getAdminUserDetailById(userId);
        if(adminUser==null){
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", adminUser.getUserName());

        return "admin/profile";
    }
    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request,
                                 @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword){
        if(StringUtils.isEmpty(originalPassword)||StringUtils.isEmpty(newPassword)){
            return "密码不能为空";
        }
        int userId=(int) request.getSession().getAttribute("loginUserId");
        if(adminUserService.updatePassword(userId,originalPassword,newPassword)){
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return ServiceResultEnum.SUCCESS.getResult();
        }else{
            return "修改失败,请检查密码正确性并重试";
        }
    }
}
