package com.wxcrawler.util;

import com.wxcrawler.domain.Log;
import com.wxcrawler.service.impl.ILogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by RoyChan on 2018/6/26.
 */
@Component
public class LogToDB {

    @Autowired
    ILogServiceImpl iLogService;

    public void insertLog(Exception e, String url){
        Log log = new Log(e.toString(), e.getStackTrace()[0].toString(), url);
        iLogService.insert(log);
    }

    public void insertLog(Exception e, String str, String url){
        Log log = new Log(e.toString(), str, url);
        iLogService.insert(log);
    }
}
