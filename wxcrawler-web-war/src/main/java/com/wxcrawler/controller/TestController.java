package com.wxcrawler.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * BIG
 */
@Controller
public class TestController {


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("query");
    }


    public static void main(String[] args) {
        System.out.println(1527807600);
        System.out.println(System.currentTimeMillis()/1000);
    }
}