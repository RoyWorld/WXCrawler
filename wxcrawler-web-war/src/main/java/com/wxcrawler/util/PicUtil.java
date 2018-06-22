package com.wxcrawler.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RoyChan on 2018/6/21.
 */
public class PicUtil {

    public static final Logger logger = LoggerFactory.getLogger(PicUtil.class);

    //正则匹配获取图片名
    public static final  Pattern picNamePattern = Pattern.compile("(?<=mmbiz_).*(?=/)");

    /**
     * 根据url保存图片
     * @param urlStr
     * @param path
     */
    public static void savePic(String urlStr, String path, String picName, String picType){
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
     * 从url中获取图片信息
     * @param url
     * @return
     */
    public static String[] getPicInfoFromUrl(String url){
        String fileInfo = "";
        Matcher fileNameMatch = picNamePattern.matcher(url);
        if (fileNameMatch.find()){
            fileInfo = fileNameMatch.group();
        }
        String[] fileInfos = fileInfo.split("/");
        return fileInfos;
    }
}
