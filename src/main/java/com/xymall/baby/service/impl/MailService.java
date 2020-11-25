package com.xymall.baby.service.impl;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MailService {
//
//    // JavaMailSender 在Mail 自动配置类 MailSenderAutoConfiguration 中已经导入，这里直接注入使用即可
//    @Autowired
//    JavaMailSender javaMailSender;
//
//    //方法5个参数分别表示：邮件发送者、收件人、抄送人、邮件主题以及邮件内容
//    public void sendSimpleMail(String from, String to, String cc, String subject, String content) {
//        // 简单邮件直接构建一个 SimpleMailMessage 对象进行配置并发送即可
//        SimpleMailMessage simpMsg = new SimpleMailMessage();
//        simpMsg.setFrom(from);
//        simpMsg.setTo(to);
//        simpMsg.setCc(cc);
//        simpMsg.setSubject(subject);
//        simpMsg.setText(content);
//        javaMailSender.send(simpMsg);
//    }
//}
