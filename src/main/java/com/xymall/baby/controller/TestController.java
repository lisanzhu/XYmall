package com.xymall.baby.controller;

import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Array;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class TestController {
    @RequestMapping("/test")
    public String test(HttpRequest httpRequest, HttpResponse httpResponse){
        return "I love you";
    }

    public static void main(String[] args) {

//        String[] test=new String[]{"aaabb","aabc","erty"};
        int[] t=new int[]{1,2,3,4};

        List l=Arrays.asList(t);
        System.out.println(l.size());
        System.out.println(l.get(0));
//        list.add("123");
//        System.out.println(list.size());
//        System.out.println(list.get(1));

    }
}
