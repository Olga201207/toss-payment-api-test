package com.ssafy.tosspay.interfaces.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/success")
    public String success() {
        return "forward:/success.html";
    }

    @GetMapping("/fail")
    public String fail() {
        return "forward:/fail.html";
    }
}