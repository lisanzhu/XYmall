package com.xymall.baby.controller;

import com.xymall.baby.service.impl.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {


    @Autowired
    MailService mailService;

    @GetMapping("/mail")
    public void hello() {
        mailService.sendSimpleMail("1052935497@qq.com",
                "971659689@qq.com",
                "1052935497@qq.com",
                "宝宝是个",
                "铁憨憨" );
    }
}
