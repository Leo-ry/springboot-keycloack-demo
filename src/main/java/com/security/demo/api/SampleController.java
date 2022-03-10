package com.security.demo.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    
    @RequestMapping("/hello")
    public String hello() {
        return "Hello KeyCloak!";
    }

    @RequestMapping("/app1")
    public String tracingTest() {
        return "This is Permit ALL!";
    }
}
