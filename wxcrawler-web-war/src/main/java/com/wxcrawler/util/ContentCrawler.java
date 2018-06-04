package com.wxcrawler.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RoyChan on 2018/6/4.
 */
public class ContentCrawler {

    Logger logger = LoggerFactory.getLogger(ContentCrawler.class);

    private Pattern nickNamePattern = Pattern.compile("var nickname = \"(.*?)\";/si");

    private Pattern headImgPattern = Pattern.compile("var round_head_img = \"(.*?)\";/si");

    private String rootPath = "E:\\公众号";

    public void crawlerContent(String url){
        try {
            Document doc = Jsoup.connect(url).get();
            Element content = doc.select("#js_content").get(0);

            String content_Str = StringEscapeUtils.unescapeHtml(content.toString());
            //$content变量的值是前面获取到的文章内容html
            content_Str.replace("data-src", "src");
            //$content变量的值是前面获取到的文章内容html
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

            System.out.println(content_Str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文章内容的html以数据库id为文件名保存成html文件，以biz字段为目录。
     * @param content
     * @param biz
     * @param id
     * @throws IOException
     */
    public void saveHtml(String content, String biz, String id) {
        try {
            File fileDir = new File(rootPath + "//" + biz);
            if (!fileDir.exists()){
                fileDir.mkdir();
            }
            File htmlFile = new File(fileDir.getAbsolutePath() + String.format("/%s.html", id));
            FileOutputStream fileOutputStream = new FileOutputStream(htmlFile);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (IOException e){
            e.printStackTrace();
            logger.error(String.format("保存失败，文件biz：%s, id：%s", biz, id));
        }
    }

    public static void main(String[] args) {
        ContentCrawler contentCrawler = new ContentCrawler();
        contentCrawler.crawlerContent("https://mp.weixin.qq.com/s?__biz=MzU2MTU0MzIxNg==&mid=2247483830&idx=1&sn=9bfe0e98be7938dbe8c55b16a116fc79&chksm=fc7660e9cb01e9ffca9990a6f4487ded4a53e471dd22c798d06c5548812b1a6f10f5e5774f15&scene=27#wechat_redirect");

        String str = "<html>1111</html>";
        contentCrawler.saveHtml(str, "1", "1111");
    }
}
