package com.xymall.baby.controller.mall;

import com.xymall.baby.Utils.Result;
import com.xymall.baby.Utils.ResultGenerator;
import com.xymall.baby.common.Constant;
import com.xymall.baby.common.ServiceResultEnum;
import com.xymall.baby.entity.User;
import com.xymall.baby.service.UserService;
//import com.xymall.baby.service.impl.MailService;
import com.xymall.baby.vo.UserVO;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/personal")
    public String personalPage(HttpServletRequest request,
                               HttpSession httpSession) {
        request.setAttribute("path", "personal");
        return "user/personal";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute(Constant.MALL_USER_SESSION_KEY);
//        return "user/login";
        return "redirect:/login" ;
    }

    @GetMapping({"/login", "login.html"})
    public String loginPage() {
        return "user/login";
    }

    @GetMapping({"/register", "register.html"})
    public String registerPage() {
        return "user/register";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestParam("loginName") String loginName,
                        @RequestParam("verifyCode") String verifyCode,
                        @RequestParam("password") String password,
                        HttpSession httpSession) {
        if (StringUtils.isEmpty(loginName)) {
            return ResultGenerator.getFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(password)) {
            return ResultGenerator.getFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
//        System.out.println(verifyCode);
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.getFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
        }
//        System.out.println(httpSession.getAttribute(Constant.MALL_VERIFY_CODE_KEY+""));
        String kaptchaCode = httpSession.getAttribute(Constant.MALL_VERIFY_CODE_KEY) + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.getFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
        }
        //todo 清verifyCode
        String loginResult = userService.login(loginName,password,httpSession);
        //登录成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(loginResult)) {
            return ResultGenerator.getSuccessResult();
        }
        //登录失败
        return ResultGenerator.getFailResult(loginResult);
    }

    @PostMapping("/register")
    @ResponseBody
    public Result register(@RequestParam("loginName") String loginName,
                           @RequestParam("verifyCode") String verifyCode,
                           @RequestParam("password") String password,
                           HttpSession httpSession) {
        if (StringUtils.isEmpty(loginName)) {
            return ResultGenerator.getFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(password)) {
            return ResultGenerator.getFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.getFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
        }
        String kaptchaCode = httpSession.getAttribute(Constant.MALL_VERIFY_CODE_KEY) + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.getFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
        }
        //todo 清verifyCode
        String registerResult = userService.register(loginName, password);
        //注册成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.getSuccessResult();
        }
        //注册失败
        return ResultGenerator.getFailResult(registerResult);
    }

//    @PostMapping("/personal/updateInfo")
//    @ResponseBody
//    public Result updateInfo(@RequestBody User user, HttpSession httpSession) {
//        System.out.println(user.getUserId());
//        System.out.println(user.getAddress());
//        System.out.println(user.getUserName());
//        UserVO mallUserTemp = userService.updateUserInfo(user,httpSession);
//        Result result;
//        if (mallUserTemp == null) {
//            result = ResultGenerator.getFailResult("修改失败");
//        } else {
//            //返回成功
//            result = ResultGenerator.getSuccessResult();
//        }
//        return result;
//    }

    @PostMapping("/personal/updateInfo")
    @ResponseBody
    public Result updateInfo(@RequestBody User user, HttpSession httpSession) throws JSONException {

        UserVO mallUserTemp = userService.updateUserInfo(user,httpSession);
        Result result;
        if (mallUserTemp == null) {
            result = ResultGenerator.getFailResult("修改失败");
        } else {
            //返回成功
            result = ResultGenerator.getSuccessResult();
        }
        return result;
    }
}
