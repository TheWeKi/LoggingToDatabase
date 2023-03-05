package com.weki.loggingrestapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggingController {


    @GetMapping("/")
    public String hello() {
        return "Hello, WeKi!";
    }
}
