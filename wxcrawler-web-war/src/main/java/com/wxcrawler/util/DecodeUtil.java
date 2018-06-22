package com.wxcrawler.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

/**
 * Created by RoyChan on 2018/6/21.
 */
public class DecodeUtil {

    public static Date decodeTime(long time){
        return new Date(time * 1000);
    }

    public static String decodeTitle(String title_encode) throws UnsupportedEncodingException {
        return URLDecoder.decode(title_encode, "UTF-8");
    }

}
