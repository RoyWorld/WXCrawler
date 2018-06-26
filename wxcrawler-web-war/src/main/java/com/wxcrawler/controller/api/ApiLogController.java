/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  LogController.java 2018-06-25 18:32:37 $
 */

package com.wxcrawler.controller.api;

import com.wxcrawler.service.impl.ILogServiceImpl;
import com.wxcrawler.domain.SearchField;
import com.wxcrawler.domain.BizData4Page;
import com.wxcrawler.domain.RtnCodeEnum;
import com.wxcrawler.exception.BizException;
import com.wxcrawler.util.StringUtil;

import com.wxcrawler.domain.Log;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value="/api")
public class ApiLogController{
    private static final Logger logger = LoggerFactory.getLogger(ApiLogController.class);

    @Autowired
    private ILogServiceImpl logServiceImpl;

   /**
     * 新增 
     * @param log
     * @return  处理条数
     */
    @ResponseBody
    @RequestMapping(value = "/log", method = RequestMethod.POST)
    public Object addLog(@RequestBody Log log) {
        try {
            String msg = "";

            if(log==null) {
                msg = "添加参数不正确";
            }else if(StringUtils.isBlank(log.getError())){
                msg = "错误信息不能为空";
            }else if(log.getError().length() > 65535){
                msg = "错误信息长度不可超过65,535";
            }

            if(StringUtils.isNotBlank(msg)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), msg);
            }

            int row = logServiceImpl.insert(log);
            if(row > 0) {
                return new String( StringUtil.toString(log.getId()) );
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "新增失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "新增异常");
        }
    }


    /**
     * 删除 
     *
     * @param {logId} ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/log/{logId}" ,method = RequestMethod.DELETE)
    public Object delLog(@PathVariable String logId) {
        try {
            if(StringUtils.isBlank(logId)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除失败，参数不正确");
            }
            Map<String, Object> condition = new HashMap();
            condition.put("id", logId);
            int row = logServiceImpl.deleteByCondition(condition);
            if(row > 0) {
                return new String( "删除成功" );
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除异常");
        }
    }


    /**
     * 修改数据
     * @param log 提交上来的JSON数据
     * @param logId  ID
     * @return  修改条数
     */
    @ResponseBody
    @RequestMapping(value = "/log/{logId}", method = RequestMethod.PATCH)
    public String editLog(@RequestBody Log log, @PathVariable String logId) {
        try {
            String msg = "";
            if(log==null) {
                msg = "添加参数不正确";
            }else if(StringUtils.isBlank(log.getError())){
                msg = "错误信息不能为空";
            }else if(log.getError().length() > 65535){
                msg = "错误信息长度不可超过65,535";

            }

            if(StringUtils.isNotBlank(msg)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), msg);
            }
            Map<String, Object> condition = new HashMap<>();
            condition.put("id", logId);
            Log log_old = (Log) logServiceImpl.queryOne(condition);
            if(log==null) {
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改失败，系统未找到相关数据");
            }

            if(log.getId() != null){
                log_old.setId(log.getId());
            }
            if(!StringUtils.isBlank(log.getError())){
                log_old.setError(log.getError());
            }
            if(!StringUtils.isBlank(log.getStr())){
                log_old.setStr(log.getStr());
            }
            if(!StringUtils.isBlank(log.getUrl())){
                log_old.setUrl(log.getUrl());
            }


            int row = logServiceImpl.update(log_old);
            if(row > 0) {
                return new String("修改成功");
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改数据异常");
        }
    }


    /**
     * 获取单实体
     *
     * @param logId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/log/{logId}", method = RequestMethod.GET)
    public Log getLog(@PathVariable String logId) {
        try {
            if(StringUtils.isBlank(logId)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "参数不正确！");
            }
            Map<String,Object> whereParams = new HashMap<String, Object>();
            whereParams.put("id", logId);
            Log log= (Log) logServiceImpl.queryOne(whereParams);
            if(null == log){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "系统未找到相关数据！");
            }
            return log;
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "获取单实体异常");
        }
    }


    /**
     * 数据输出 带分页
     *
     * @param log 过滤条件
     * @param page      第几页
     * @return 业务数据列表实体，最终转换为json数据输出
     * @throws ServletException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/loglist", method = RequestMethod.POST)
    public BizData4Page loglist(Log log, Integer page) {
        try {
            Map<String, Object> whereParams = new HashMap<String, Object>();
            if(log.getId() != null){
                whereParams.put("id", new SearchField("id", "=", log.getId()));
            }
            if(!StringUtils.isBlank(log.getError())){
                whereParams.put("error", new SearchField("error", "like", "%" + log.getError() + "%"));
            }
            if(!StringUtils.isBlank(log.getStr())){
                whereParams.put("str", new SearchField("str", "like", "%" + log.getStr() + "%"));
            }
            if(!StringUtils.isBlank(log.getUrl())){
                whereParams.put("url", new SearchField("url", "like", "%" + log.getUrl() + "%"));
            }


            //other props filter
            whereParams.put("groupOp", "and");

            BizData4Page bizData4Page = new BizData4Page();
            bizData4Page.setConditions(whereParams);
            if (page != null) {
                bizData4Page.setPage(page);
            }

            logServiceImpl.queryPageByDataPerm(bizData4Page);
            return bizData4Page;
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "查询数据异常");
        }
    }
}
