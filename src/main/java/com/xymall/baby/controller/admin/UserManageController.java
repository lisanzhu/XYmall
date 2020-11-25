package com.xymall.baby.controller.admin;

import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.PageResult;
import com.xymall.baby.Utils.Result;
import com.xymall.baby.Utils.ResultGenerator;
import com.xymall.baby.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class UserManageController {
    @Autowired
    UserService userService;

    @GetMapping("/user")
    public String usersPage(HttpServletRequest httpServletRequest){
        httpServletRequest.setAttribute("path","user");
        return "admin/userManage";
    }

    @ResponseBody
    @GetMapping("/user/page")
    public Result getUsersPage(@RequestParam Map<String,Object> params){
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        PageResult userPage = userService.getUserPage(new PageQueryUtil(params));
        return ResultGenerator.getSuccessResult(userPage) ;
    }

    @ResponseBody
    @PostMapping("/user/lock/{lockStatus}")
    public Result lockUser(@RequestBody Integer[] ids, @PathVariable int lockStatus){
        if (ids.length < 1) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        if (lockStatus != 0 && lockStatus != 1) {
            return ResultGenerator.getFailResult("操作非法！");
        }
        if(userService.lockUser(ids,lockStatus)){
            return ResultGenerator.getSuccessResult("操作成功");
        }else{
            return ResultGenerator.getFailResult("操作失败");
        }

    }

//    @ResponseBody
//    @GetMapping("/user/lock")
//    public Result lockUser(@RequestParam )

}
