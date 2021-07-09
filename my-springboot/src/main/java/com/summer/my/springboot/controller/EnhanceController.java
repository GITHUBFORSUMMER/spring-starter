package com.summer.my.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class EnhanceController {

    @Autowired
    Environment environment;

    //http://localhost:8080/getNowTime
    @GetMapping("/getNowTime")
    public String getNowTime() {
        return LocalDateTime.now().toString();
    }

    //http://localhost:8080/getTimeMillis
    @GetMapping("/getTimeMillis")
    public String getTimeMillis() {
        return String.valueOf(System.currentTimeMillis());
    }


}
