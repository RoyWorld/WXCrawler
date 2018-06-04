/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  WeixinController.java 2018-05-31 10:45:16 $
 */

package com.wxcrawler.controller.api;

import com.wxcrawler.service.impl.IWeixinServiceImpl;
import com.wxcrawler.domain.SearchField;
import com.wxcrawler.domain.BizData4Page;
import com.wxcrawler.domain.RtnCodeEnum;
import com.wxcrawler.exception.BizException;
import com.wxcrawler.util.StringUtil;

import com.wxcrawler.domain.Weixin;

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
public class ApiWeixinController{
    private static final Logger logger = LoggerFactory.getLogger(ApiWeixinController.class);

    @Autowired
    private IWeixinServiceImpl weixinServiceImpl;

   /**
     * 新增 公众号表
     * @param weixin
     * @return  处理条数
     */
    @ResponseBody
    @RequestMapping(value = "/weixin", method = RequestMethod.POST)
    public Object addWeixin(@RequestBody Weixin weixin) {
        try {
            String msg = "";

            if(weixin==null) {
                msg = "添加公众号表参数不正确";
            }

            if(StringUtils.isNotBlank(msg)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), msg);
            }

            int row = weixinServiceImpl.insert(weixin);
            if(row > 0) {
                return new String( StringUtil.toString(weixin.getId()) );
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "新增公众号表失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "新增公众号表异常");
        }
    }


    /**
     * 删除 公众号表
     *
     * @param {weixinId} 公众号表ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/{weixinId}" ,method = RequestMethod.DELETE)
    public Object delWeixin(@PathVariable String weixinId) {
        try {
            if(StringUtils.isBlank(weixinId)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除公众号表失败，参数不正确");
            }
            Map<String, Object> condition = new HashMap();
            condition.put("id", weixinId);
            int row = weixinServiceImpl.deleteByCondition(condition);
            if(row > 0) {
                return new String( "删除公众号表成功" );
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除公众号表失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除公众号表异常");
        }
    }


    /**
     * 修改公众号表数据
     * @param weixin 提交上来的公众号表JSON数据
     * @param weixinId  公众号表ID
     * @return  修改条数
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/{weixinId}", method = RequestMethod.PATCH)
    public String editWeixin(@RequestBody Weixin weixin, @PathVariable String weixinId) {
        try {
            String msg = "";
            if(weixin==null) {
                msg = "添加公众号表参数不正确";

            }

            if(StringUtils.isNotBlank(msg)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), msg);
            }
            Map<String, Object> condition = new HashMap<>();
            condition.put("id", weixinId);
            Weixin weixin_old = (Weixin) weixinServiceImpl.queryOne(condition);
            if(weixin==null) {
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改失败，系统未找到相关数据");
            }

            if(weixin.getId() != null){
                weixin_old.setId(weixin.getId());
            }
            if(!StringUtils.isBlank(weixin.getBiz())){
                weixin_old.setBiz(weixin.getBiz());
            }
            if(weixin.getCollect() != null){
                weixin_old.setCollect(weixin.getCollect());
            }


            int row = weixinServiceImpl.update(weixin_old);
            if(row > 0) {
                return new String("修改公众号表成功");
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改公众号表失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改公众号表数据异常");
        }
    }


    /**
     * 获取单公众号表实体
     *
     * @param weixinId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/{weixinId}", method = RequestMethod.GET)
    public Weixin getWeixin(@PathVariable String weixinId) {
        try {
            if(StringUtils.isBlank(weixinId)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "参数不正确！");
            }
            Map<String,Object> whereParams = new HashMap<String, Object>();
            whereParams.put("id", weixinId);
            Weixin weixin= (Weixin) weixinServiceImpl.queryOne(whereParams);
            if(null == weixin){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "系统未找到公众号表相关数据！");
            }
            return weixin;
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "获取单公众号表实体异常");
        }
    }


    /**
     * 公众号表数据输出 带分页
     *
     * @param weixin 过滤条件
     * @param page      第几页
     * @return 业务数据列表实体，最终转换为json数据输出
     * @throws ServletException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/weixinlist", method = RequestMethod.POST)
    public BizData4Page weixinlist(Weixin weixin, Integer page) {
        try {
            Map<String, Object> whereParams = new HashMap<String, Object>();
            if(weixin.getId() != null){
                whereParams.put("id", new SearchField("id", "=", weixin.getId()));
            }
            if(!StringUtils.isBlank(weixin.getBiz())){
                whereParams.put("biz", new SearchField("biz", "like", "%" + weixin.getBiz() + "%"));
            }
            if(weixin.getCollect() != null){
                whereParams.put("collect", new SearchField("collect", "=", weixin.getCollect()));
            }


            //other props filter
            whereParams.put("groupOp", "and");

            BizData4Page bizData4Page = new BizData4Page();
            bizData4Page.setConditions(whereParams);
            if (page != null) {
                bizData4Page.setPage(page);
            }

            weixinServiceImpl.queryPageByDataPerm(bizData4Page);
            return bizData4Page;
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "查询公众号表数据异常");
        }
    }
}
