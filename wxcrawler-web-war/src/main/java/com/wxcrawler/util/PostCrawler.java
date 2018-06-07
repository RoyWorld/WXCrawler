package com.wxcrawler.util;

import com.wxcrawler.domain.Weixin;
import com.wxcrawler.service.impl.IWeixinServiceImpl;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将文章爬取保存下来
 * Created by RoyChan on 2018/6/4.
 */
@Component
public class PostCrawler {

    Logger logger = LoggerFactory.getLogger(PostCrawler.class);

    //正则匹配获取公众号名
    private Pattern picsrcPattern = Pattern.compile("data-src=\"(.*?)\"");

    //正则匹配获取公众号名
    private Pattern nickNamePattern = Pattern.compile("var nickname = \"(.*?)\";/si");

    //正则匹配获取公众号头像
    private Pattern headImgPattern = Pattern.compile("var round_head_img = \"(.*?)\";/si");

    //正则匹配获取图片名
    private Pattern picNamePattern = Pattern.compile("(?<=mmbiz_png/).*(?=/)");

    private String rootPath = "E:\\公众号";

    @Autowired
    IWeixinServiceImpl iWeixinService;

    public void crawlerContent(String url, String biz, int id){
        try {
            Document doc = Jsoup.connect(url).get();
            Element content = doc.select("#js_content").get(0);

            String content_Str = StringEscapeUtils.unescapeHtml(content.toString());
            //$content变量的值是前面获取到的文章内容html
            content_Str = replaceTextOfMatchGroup(content_Str, picsrcPattern, 0, srcStr -> {
                int index = srcStr.indexOf("=");
                String picUrl = srcStr.substring(index+2, srcStr.length()-1);
                srcStr = srcStr.replace("data-src", "src");
                //从url中获取图片名
                String picName = getPicNameFromUrl(picUrl);

                //从url中获取图片类型
                String picType = "png";
                try {
                    picType = getPicTypeFromUrl(picUrl);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //图片文件完整名称
                String picFileName = picName + "." + picType;

                //将src中的图片链接替换成本地图片链接
                srcStr = srcStr.replaceAll("(?<=src\\=\\\").*(?=\\\")", picFileName);

                //保存图片
                savePic(picUrl, rootPath + String.format("//%s//%s", biz, id), picFileName, picType);

                return srcStr;
            });

            //$content变量的值是前面获取到的文章内容html，视频替换
            content_Str.replace("preview.html", "player.html");

            //公众号昵称
            String nickName = "";
            Matcher nickNameMatcher = nickNamePattern.matcher(doc.html());
            if (nickNameMatcher.find()){
                nickName = nickNameMatcher.group();
            }

            //公众号头像
            String headImg = "";
            Matcher headImgMatcher = headImgPattern.matcher(doc.html());
            if (headImgMatcher.find()){
                headImg = headImgMatcher.group();
            }

            Map<String, Object> condition = new HashMap<>(16);
            condition.put("biz", biz);
            Map<String, Object> updateMap = new HashMap<>(16);
            updateMap.put("nickName", nickName);
            updateMap.put("headImg", headImg);
            updateMap.put("collect", System.currentTimeMillis()/1000);
            iWeixinService.updateByCondition(condition, updateMap);

            //保存html
            saveHtml(content_Str, biz, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换匹配的字符串
     * @param sourceString
     * @param pattern
     * @param groupToReplace
     * @param replaceStrategy
     * @return
     */
    public String replaceTextOfMatchGroup(String sourceString, Pattern pattern, int groupToReplace, Function<String,String> replaceStrategy) {
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

    /**
     * 将文章内容的html以数据库id为文件名保存成html文件，以biz字段为目录。
     * @param content
     * @param biz
     * @param id
     * @throws IOException
     */
    public void saveHtml(String content, String biz, int id) {
        try {
            Path filePath = Paths.get(rootPath + "//" + biz + "//" + id);
            Files.createDirectories(Paths.get(rootPath + "//" + biz + "//" + id));
            File htmlFile = new File(filePath.toFile().getAbsolutePath() + String.format("/%s.html", id));
            FileOutputStream fileOutputStream = new FileOutputStream(htmlFile);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (IOException e){
            e.printStackTrace();
            logger.error(String.format("保存html失败，文件biz：%s, id：%s", biz, id));
        }
    }

    /**
     * 根据url保存图片
     * @param urlStr
     * @param path
     */
    public void savePic(String urlStr, String path, String picName, String picType){
        try {
            Files.createDirectories(Paths.get(path));
            //保存图片
            URL url = new URL(urlStr);
            BufferedImage img = ImageIO.read(url);
            File file = new File(path + "//" + picName);
            ImageIO.write(img, picType, file);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("保存图片失败，url：%s", urlStr));
        }
    }

    /**
     * 从url中的wx_fmt获取图片类型
     * @param url
     * @return
     */
    private String getPicTypeFromUrl(String url) throws UnsupportedEncodingException {
        MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(URLDecoder.decode(url, "UTF-8")).build().getQueryParams();
        return parameters.get("wx_fmt").get(0);
    }

    /**
     * 从url中获取图片名
     * @param url
     * @return
     */
    private String getPicNameFromUrl(String url){
        String fileName = "";
        Matcher fileNameMatch = picNamePattern.matcher(url);
        if (fileNameMatch.find()){
            fileName = fileNameMatch.group();
        }
        return fileName;
    }

    public static void main(String[] args) {
        PostCrawler contentCrawler = new PostCrawler();
        contentCrawler.crawlerContent("https://mp.weixin.qq.com/s?__biz=MzU2MTU0MzIxNg==&mid=2247483830&idx=1&sn=9bfe0e98be7938dbe8c55b16a116fc79&chksm=fc7660e9cb01e9ffca9990a6f4487ded4a53e471dd22c798d06c5548812b1a6f10f5e5774f15&scene=27#wechat_redirect", "MzU2MTU0MzIxNg==", 2);
    }


}
