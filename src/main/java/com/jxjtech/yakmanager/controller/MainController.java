package com.jxjtech.yakmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping(value = {"/index"})
    public String index() { return "index"; }

    @RequestMapping(value = "/policy")
    public String test() { return "policy"; }

    @RequestMapping(value = "/csvUpload")
    public String csvUpload() { return "csvUpload";}
}
