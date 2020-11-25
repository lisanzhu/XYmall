package com.xymall.baby.controller;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Controller
public class TestController {

    public String test(HttpRequest httpRequest, HttpResponse httpResponse){
        return "I love you";
    }

    @GetMapping("/admin/test")
    public String test(){
        return "admin/test";
    }

    public static void main(String[] args) {
        System.out.println(8150776562029630058L/(8*1000000));
    }
}
