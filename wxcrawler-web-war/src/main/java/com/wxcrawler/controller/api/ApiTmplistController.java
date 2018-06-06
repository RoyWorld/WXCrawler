/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  TmplistController.java 2018-06-06 16:05:36 $
 */

package com.wxcrawler.controller.api;

import com.wxcrawler.service.impl.ITmplistServiceImpl;
import com.wxcrawler.domain.SearchField;
import com.wxcrawler.domain.BizData4Page;
import com.wxcrawler.domain.RtnCodeEnum;
import com.wxcrawler.exception.BizException;
import com.wxcrawler.util.StringUtil;

import com.wxcrawler.domain.Tmplist;

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
public class ApiTmplistController{
    private static final Logger logger = LoggerFactory.getLogger(ApiTmplistController.class);

    @Autowired
    private ITmplistServiceImpl tmplistServiceImpl;

   /**
     * 新增 采集队列表
     * @param tmplist
     * @return  处理条数
     */
    @ResponseBody
    @RequestMapping(value = "/tmplist", method = RequestMethod.POST)
    public Object addTmplist(@RequestBody Tmplist tmplist) {
        try {
            String msg = "";

            if(tmplist==null) {
                msg = "添加采集队列表参数不正确";
            }

            if(StringUtils.isNotBlank(msg)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), msg);
            }

            int row = tmplistServiceImpl.insert(tmplist);
            if(row > 0) {
                return new String( StringUtil.toString(tmplist.getId()) );
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "新增采集队列表失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "新增采集队列表异常");
        }
    }


    /**
     * 删除 采集队列表
     *
     * @param {tmplistId} 采集队列表ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/tmplist/{tmplistId}" ,method = RequestMethod.DELETE)
    public Object delTmplist(@PathVariable String tmplistId) {
        try {
            if(StringUtils.isBlank(tmplistId)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除采集队列表失败，参数不正确");
            }
            Map<String, Object> condition = new HashMap();
            condition.put("id", tmplistId);
            int row = tmplistServiceImpl.deleteByCondition(condition);
            if(row > 0) {
                return new String( "删除采集队列表成功" );
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除采集队列表失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除采集队列表异常");
        }
    }


    /**
     * 修改采集队列表数据
     * @param tmplist 提交上来的采集队列表JSON数据
     * @param tmplistId  采集队列表ID
     * @return  修改条数
     */
    @ResponseBody
    @RequestMapping(value = "/tmplist/{tmplistId}", method = RequestMethod.PATCH)
    public String editTmplist(@RequestBody Tmplist tmplist, @PathVariable String tmplistId) {
        try {
            String msg = "";
            if(tmplist==null) {
                msg = "添加采集队列表参数不正确";

            }

            if(StringUtils.isNotBlank(msg)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), msg);
            }
            Map<String, Object> condition = new HashMap<>();
            condition.put("id", tmplistId);
            Tmplist tmplist_old = (Tmplist) tmplistServiceImpl.queryOne(condition);
            if(tmplist==null) {
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改失败，系统未找到相关数据");
            }

            if(tmplist.getId() != null){
                tmplist_old.setId(tmplist.getId());
            }
            if(!StringUtils.isBlank(tmplist.getContentUrl())){
                tmplist_old.setContentUrl(tmplist.getContentUrl());
            }
            if(tmplist.getLoading() != null){
                tmplist_old.setLoading(tmplist.getLoading());
            }


            int row = tmplistServiceImpl.update(tmplist_old);
            if(row > 0) {
                return new String("修改采集队列表成功");
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改采集队列表失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改采集队列表数据异常");
        }
    }


    /**
     * 获取单采集队列表实体
     *
     * @param tmplistId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/tmplist/{tmplistId}", method = RequestMethod.GET)
    public Tmplist getTmplist(@PathVariable String tmplistId) {
        try {
            if(StringUtils.isBlank(tmplistId)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "参数不正确！");
            }
            Map<String,Object> whereParams = new HashMap<String, Object>();
            whereParams.put("id", tmplistId);
            Tmplist tmplist= (Tmplist) tmplistServiceImpl.queryOne(whereParams);
            if(null == tmplist){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "系统未找到采集队列表相关数据！");
            }
            return tmplist;
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "获取单采集队列表实体异常");
        }
    }


    /**
     * 采集队列表数据输出 带分页
     *
     * @param tmplist 过滤条件
     * @param page      第几页
     * @return 业务数据列表实体，最终转换为json数据输出
     * @throws ServletException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/tmplistlist", method = RequestMethod.POST)
    public BizData4Page tmplistlist(Tmplist tmplist, Integer page) {
        try {
            Map<String, Object> whereParams = new HashMap<String, Object>();
            if(tmplist.getId() != null){
                whereParams.put("id", new SearchField("id", "=", tmplist.getId()));
            }
            if(!StringUtils.isBlank(tmplist.getContentUrl())){
                whereParams.put("content_url", new SearchField("content_url", "like", "%" + tmplist.getContentUrl() + "%"));
            }
            if(tmplist.getLoading() != null){
                whereParams.put("loading", new SearchField("loading", "=", tmplist.getLoading()));
            }


            //other props filter
            whereParams.put("groupOp", "and");

            BizData4Page bizData4Page = new BizData4Page();
            bizData4Page.setConditions(whereParams);
            if (page != null) {
                bizData4Page.setPage(page);
            }

            tmplistServiceImpl.queryPageByDataPerm(bizData4Page);
            return bizData4Page;
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "查询采集队列表数据异常");
        }
    }
}
