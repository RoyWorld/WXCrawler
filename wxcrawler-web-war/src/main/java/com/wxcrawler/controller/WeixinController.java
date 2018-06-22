package com.wxcrawler.controller;

import com.wxcrawler.domain.BizData4Page;
import com.wxcrawler.domain.Post;
import com.wxcrawler.service.SqlOrderEnum;
import com.wxcrawler.service.impl.IPostServiceImpl;
import com.wxcrawler.util.DecodeUtil;
import com.wxcrawler.util.PicUtil;
import com.wxcrawler.util.PropertiesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by RoyChan on 2018/6/20.
 */
@Controller
@RequestMapping(value = "/index/weixin")
public class WeixinController {

    //正则匹配获取图片源
    private Pattern picsrcPattern = Pattern.compile("src=\"(.*?)\"");

    @Autowired
    IPostServiceImpl iPostService;

    public static final String postFilePath = (String) PropertiesHelper.getPropertiesFromResource().get("postFilePath");

    @RequestMapping(value = "/openWeixin", method = RequestMethod.GET)
    public ModelAndView openWeixin(String biz, String avatar, String name) throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView("weixin_page");
        modelAndView.addObject("biz", biz);
        modelAndView.addObject("avatar", avatar);
        modelAndView.addObject("name", name);
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
     * 获取封面图片
     * @param biz
     * @param post
     * @param coverId
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/cover/{biz}/{post}/{coverId}/", method = RequestMethod.GET)
    public void getCover(@PathVariable("biz") String biz, @PathVariable("post") String post, @PathVariable("coverId") String coverId, HttpServletResponse response) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(postFilePath + "\\" + biz + "\\" + post+ "\\" + coverId));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/*;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }

    /**
     * 文章列表
     * @param page
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "/getPostList", method = RequestMethod.GET)
    public BizData4Page postList(String biz, Integer page) throws UnsupportedEncodingException {
        int pageSize = 10;

        Map<String, Object> condition = new HashMap<>();
        condition.put("biz", biz);

        BizData4Page<Post> bizData = new BizData4Page();
        bizData.setPagesize(pageSize);
        bizData.setPage(page);
        bizData.setConditions(condition);
        iPostService.queryPageByDataPerm(bizData, "datetime", SqlOrderEnum.DESC);
        List<Post> postList = bizData.getRows();
        for (int i = 0; i < postList.size(); i++) {
            Post post = bizData.getRows().get(i);
            String cover = post.getCover();
            String[] coverInfo = PicUtil.getPicInfoFromUrl(cover);
            post.setCover(coverInfo[1] + "." + coverInfo[0]);
            post.setTitle(DecodeUtil.decodeTitle(post.getTitleEncode()));
            postList.set(i, post);
        }
        return bizData;
    }

    /**
     * 获取封面图片
     * @param biz
     * @param post
     * @param imgId
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/postImg/{biz}/{post}/{imgId}", method = RequestMethod.GET)
    public void getPostImg(@PathVariable("biz") String biz, @PathVariable("post") String post, @PathVariable("imgId") String imgId, HttpServletResponse response) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(postFilePath + "\\" + biz + "\\" + post+ "\\" + imgId));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/*;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }


    /**
     * 查看本地文章
     * @param biz
     * @param post
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/openPost/{biz}/{post}", method = RequestMethod.GET)
    public ModelAndView openPost(@PathVariable("biz") String biz, @PathVariable("post") String post, HttpServletRequest request) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(postFilePath + "\\" + biz + "\\" + post+ "\\" + post + ".html"));
        String content_Str = new String(bytes);
        content_Str = replaceTextOfMatchGroup(content_Str, picsrcPattern, 0, srcStr -> {
            int index = srcStr.indexOf("=");
            String picUrl = srcStr.substring(index + 2, srcStr.length() - 1);
            //将src中的图片链接替换成本地图片链接
            srcStr = srcStr.replaceAll("(?<=src\\=\\\").*(?=\\\")", String.format("/index/weixin/postImg/%s/%s/%s/", biz, post, picUrl));
            return srcStr;
        });

        HttpSession session = request.getSession();
        ServletContext sc = session.getServletContext();
        String path = sc.getRealPath("/");
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("file:" + path + "/WEB-INF/view/article.html");
        FileOutputStream outputStreamWriter = new FileOutputStream(resource.getFile());
        outputStreamWriter.write(content_Str.getBytes());
        ModelAndView modelAndView = new ModelAndView("post");
        return modelAndView;
    }

    /**
     * 替换匹配的字符串
     *
     * @param sourceString
     * @param pattern
     * @param groupToReplace
     * @param replaceStrategy
     * @return
     */
    private String replaceTextOfMatchGroup(String sourceString, Pattern pattern, int groupToReplace, Function<String, String> replaceStrategy) {
        Stack<Integer> startPositions = new Stack<>();
        Stack<Integer> endPositions = new Stack<>();
        Matcher matcher = pattern.matcher(sourceString);

        //find next match
        while (matcher.find()) {
            startPositions.push(matcher.start(groupToReplace));
            endPositions.push(matcher.end(groupToReplace));
        }
        StringBuilder sb = new StringBuilder(sourceString);
        while (!startPositions.isEmpty()) {
            int start = startPositions.pop();
            int end = endPositions.pop();
            if (start >= 0 && end >= 0) {
                sb.replace(start, end, replaceStrategy.apply(sourceString.substring(start, end)));
            }
        }
        return sb.toString();
    }
}
