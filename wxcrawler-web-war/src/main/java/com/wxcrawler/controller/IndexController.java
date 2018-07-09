package com.wxcrawler.controller;

import com.wxcrawler.domain.BizData4Page;
import com.wxcrawler.service.impl.IWeixinServiceImpl;
import com.wxcrawler.util.DecodeUtil;
import com.wxcrawler.util.PropertiesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by RoyChan on 2018/6/19.
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController {

    public static final String postFilePath = PropertiesHelper.getPropertiesFromResource().getProperty("postFilePath");

    @Autowired
    IWeixinServiceImpl iWeixinService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
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

    /**
     * 公众号列表
     * @param page
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "/getWXList", method = RequestMethod.GET)
    public BizData4Page wxList(Integer page) throws UnsupportedEncodingException {
        int pageSize = 5;
        int offset = (page - 1) * pageSize;
        int rows = pageSize;
        List<Map<String, Object>> wxList = iWeixinService.getWXList(offset, rows);
        int count = iWeixinService.count(new HashMap<>());
        for (int i = 0; i < wxList.size(); i++) {
            Map<String, Object> wx = wxList.get(i);
            wx.put("postTitle", DecodeUtil.decodeTitle((String) wx.get("postTitle")));
            wxList.set(i, wx);
        }
        BizData4Page bizData4Page = new BizData4Page();
        bizData4Page.setRows(wxList);
        int total = count % pageSize == 0 ? count/pageSize : count/pageSize + 1;
        bizData4Page.setTotal(total);
        return bizData4Page;
    }

}
