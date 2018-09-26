package com.wxcrawler.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * BIG
 */
@Controller
public class TestController {


    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ModelAndView query() {
        return new ModelAndView("query");
    }

    @RequestMapping(value = "/kkk", method = RequestMethod.GET)
    public void kkk(HttpServletResponse response) throws IOException {
        String url = "https://www.baidu.com";
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<script>setTimeout(function(){window.location.href='" + url + "';},2000);</script>");
        out.close();
    }


    public static void main(String[] args) {
        System.out.println(1527807600);
        System.out.println(System.currentTimeMillis()/1000);
    }
}