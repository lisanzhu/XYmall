package com.xymall.baby.controller.User;

import com.xymall.baby.entity.User;
import com.xymall.baby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

//    @GetMapping("/login")
//    public String login(){
//        return "admin/login";
//    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam("userName") String userName, @RequestParam("password") String password, @RequestParam("verifyCode") String verifyCode, HttpSession session){
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
            return "admin/login";
        }
        User user=userService.login(userName, password);
        if(user!=null&&user.isActive()){
            return "admin/main";
        }else{
            session.setAttribute("errorMsg", "查询无此用户");
            return "admin/login";
        }


    }
}
