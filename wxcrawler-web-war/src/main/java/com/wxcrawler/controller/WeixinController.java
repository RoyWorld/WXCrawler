package com.wxcrawler.controller;

import com.wxcrawler.util.PropertiesHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Created by RoyChan on 2018/6/20.
 */
@Controller
@RequestMapping(value = "/index/weixin")
public class WeixinController {

    public static final String postFilePath = (String) PropertiesHelper.getPropertiesFromResource().get("postFilePath");

    @RequestMapping(value = "/openWeixin", method = RequestMethod.GET)
    public ModelAndView openWeixin() throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView("weixin_page");
        return modelAndView;
    }

    /**
     * 获取公众号头像
     * @param biz
     * @param avatar
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/avatar/{biz}/{avatar}" , method = RequestMethod.GET)
    public void getAvatar(@PathVariable("biz") String biz, @PathVariable("avatar") String avatar, HttpServletResponse response) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(postFilePath + "\\" + biz + "\\" + avatar));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/*;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }

    @RequestMapping(value = "/cover/{biz}/{post}/{coverId}/", method = RequestMethod.GET)
    public void getCover(@PathVariable("biz") String biz, @PathVariable("post") String post, @PathVariable("coverId") String coverId, HttpServletResponse response) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(postFilePath + "\\" + biz + "\\" + post+ "\\" + coverId));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/*;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }
}
