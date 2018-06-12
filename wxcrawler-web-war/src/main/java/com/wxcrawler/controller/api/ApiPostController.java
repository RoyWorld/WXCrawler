/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  PostController.java 2018-06-12 16:45:24 $
 */

package com.wxcrawler.controller.api;

import com.wxcrawler.service.impl.IPostServiceImpl;
import com.wxcrawler.domain.SearchField;
import com.wxcrawler.domain.BizData4Page;
import com.wxcrawler.domain.RtnCodeEnum;
import com.wxcrawler.exception.BizException;
import com.wxcrawler.util.StringUtil;

import com.wxcrawler.domain.Post;

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
public class ApiPostController{
    private static final Logger logger = LoggerFactory.getLogger(ApiPostController.class);

    @Autowired
    private IPostServiceImpl postServiceImpl;

   /**
     * 新增 公众号文章表
     * @param post
     * @return  处理条数
     */
    @ResponseBody
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public Object addPost(@RequestBody Post post) {
        try {
            String msg = "";

            if(post==null) {
                msg = "添加公众号文章表参数不正确";
            }else if(StringUtils.isBlank(post.getBiz())){
                msg = "公众号文章表文章对应的公众号biz不能为空";
            }else if(post.getBiz().length() > 255){
                msg = "公众号文章表文章对应的公众号biz长度不可超过255";
            }else if(post.getFieldId() != null){
                msg = "公众号文章表微信定义的一个id，每条文章唯一不能为空";
            }else if(StringUtils.isBlank(post.getTitle())){
                msg = "公众号文章表文章标题不能为空";
            }else if(post.getTitle().length() > 255){
                msg = "公众号文章表文章标题长度不可超过255";
            }else if(StringUtils.isBlank(post.getTitleEncode())){
                msg = "公众号文章表文章标题编码，防止文章标题出现emoji不能为空";
            }else if(post.getTitleEncode().length() > 65535){
                msg = "公众号文章表文章标题编码，防止文章标题出现emoji长度不可超过65,535";
            }else if(StringUtils.isBlank(post.getDigest())){
                msg = "公众号文章表文章摘要不能为空";
            }else if(post.getDigest().length() > 500){
                msg = "公众号文章表文章摘要长度不可超过500";
            }else if(StringUtils.isBlank(post.getDigestEncode())){
                msg = "公众号文章表文章摘要编码，防止文章摘要出现emoji不能为空";
            }else if(post.getDigestEncode().length() > 65535){
                msg = "公众号文章表文章摘要编码，防止文章摘要出现emoji长度不可超过65,535";
            }else if(StringUtils.isBlank(post.getContentUrl())){
                msg = "公众号文章表文章地址不能为空";
            }else if(post.getContentUrl().length() > 500){
                msg = "公众号文章表文章地址长度不可超过500";
            }else if(StringUtils.isBlank(post.getSourceUrl())){
                msg = "公众号文章表阅读原文地址不能为空";
            }else if(post.getSourceUrl().length() > 500){
                msg = "公众号文章表阅读原文地址长度不可超过500";
            }else if(StringUtils.isBlank(post.getCover())){
                msg = "公众号文章表封面图片不能为空";
            }else if(post.getCover().length() > 500){
                msg = "公众号文章表封面图片长度不可超过500";
            }else if(post.getIsMulti() != null){
                msg = "公众号文章表是否多图文不能为空";
            }else if(post.getIsTop() != null){
                msg = "公众号文章表是否头条不能为空";
            }else if(post.getDatetime() != null){
                msg = "公众号文章表文章时间戳不能为空";
            }else if(post.getReadNum() != null){
                msg = "公众号文章表文章阅读量不能为空";
            }else if(post.getLikeNum() != null){
                msg = "公众号文章表文章点赞量不能为空";
            }else if(post.getIsExsist() != null){
                msg = "公众号文章表是否已爬取，0：未爬取，1：已爬取，2：文章不存在不能为空";
            }

            if(StringUtils.isNotBlank(msg)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), msg);
            }

            int row = postServiceImpl.insert(post);
            if(row > 0) {
                return new String( StringUtil.toString(post.getId()) );
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "新增公众号文章表失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "新增公众号文章表异常");
        }
    }


    /**
     * 删除 公众号文章表
     *
     * @param {postId} 公众号文章表ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/post/{postId}" ,method = RequestMethod.DELETE)
    public Object delPost(@PathVariable String postId) {
        try {
            if(StringUtils.isBlank(postId)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除公众号文章表失败，参数不正确");
            }
            Map<String, Object> condition = new HashMap();
            condition.put("id", postId);
            int row = postServiceImpl.deleteByCondition(condition);
            if(row > 0) {
                return new String( "删除公众号文章表成功" );
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除公众号文章表失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "删除公众号文章表异常");
        }
    }


    /**
     * 修改公众号文章表数据
     * @param post 提交上来的公众号文章表JSON数据
     * @param postId  公众号文章表ID
     * @return  修改条数
     */
    @ResponseBody
    @RequestMapping(value = "/post/{postId}", method = RequestMethod.PATCH)
    public String editPost(@RequestBody Post post, @PathVariable String postId) {
        try {
            String msg = "";
            if(post==null) {
                msg = "添加公众号文章表参数不正确";
            }else if(StringUtils.isBlank(post.getBiz())){
                msg = "公众号文章表文章对应的公众号biz不能为空";
            }else if(post.getBiz().length() > 255){
                msg = "公众号文章表文章对应的公众号biz长度不可超过255";
            }else if(post.getFieldId() != null){
                msg = "公众号文章表微信定义的一个id，每条文章唯一不能为空";
            }else if(StringUtils.isBlank(post.getTitle())){
                msg = "公众号文章表文章标题不能为空";
            }else if(post.getTitle().length() > 255){
                msg = "公众号文章表文章标题长度不可超过255";
            }else if(StringUtils.isBlank(post.getTitleEncode())){
                msg = "公众号文章表文章标题编码，防止文章标题出现emoji不能为空";
            }else if(post.getTitleEncode().length() > 65535){
                msg = "公众号文章表文章标题编码，防止文章标题出现emoji长度不可超过65,535";
            }else if(StringUtils.isBlank(post.getDigest())){
                msg = "公众号文章表文章摘要不能为空";
            }else if(post.getDigest().length() > 500){
                msg = "公众号文章表文章摘要长度不可超过500";
            }else if(StringUtils.isBlank(post.getDigestEncode())){
                msg = "公众号文章表文章摘要编码，防止文章摘要出现emoji不能为空";
            }else if(post.getDigestEncode().length() > 65535){
                msg = "公众号文章表文章摘要编码，防止文章摘要出现emoji长度不可超过65,535";
            }else if(StringUtils.isBlank(post.getContentUrl())){
                msg = "公众号文章表文章地址不能为空";
            }else if(post.getContentUrl().length() > 500){
                msg = "公众号文章表文章地址长度不可超过500";
            }else if(StringUtils.isBlank(post.getSourceUrl())){
                msg = "公众号文章表阅读原文地址不能为空";
            }else if(post.getSourceUrl().length() > 500){
                msg = "公众号文章表阅读原文地址长度不可超过500";
            }else if(StringUtils.isBlank(post.getCover())){
                msg = "公众号文章表封面图片不能为空";
            }else if(post.getCover().length() > 500){
                msg = "公众号文章表封面图片长度不可超过500";
            }else if(post.getIsMulti() != null){
                msg = "公众号文章表是否多图文不能为空";
            }else if(post.getIsTop() != null){
                msg = "公众号文章表是否头条不能为空";
            }else if(post.getDatetime() != null){
                msg = "公众号文章表文章时间戳不能为空";
            }else if(post.getReadNum() != null){
                msg = "公众号文章表文章阅读量不能为空";
            }else if(post.getLikeNum() != null){
                msg = "公众号文章表文章点赞量不能为空";
            }else if(post.getIsExsist() != null){
                msg = "公众号文章表是否已爬取，0：未爬取，1：已爬取，2：文章不存在不能为空";

            }

            if(StringUtils.isNotBlank(msg)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), msg);
            }
            Map<String, Object> condition = new HashMap<>();
            condition.put("id", postId);
            Post post_old = (Post) postServiceImpl.queryOne(condition);
            if(post==null) {
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改失败，系统未找到相关数据");
            }

            if(post.getId() != null){
                post_old.setId(post.getId());
            }
            if(!StringUtils.isBlank(post.getBiz())){
                post_old.setBiz(post.getBiz());
            }
            if(post.getFieldId() != null){
                post_old.setFieldId(post.getFieldId());
            }
            if(!StringUtils.isBlank(post.getTitle())){
                post_old.setTitle(post.getTitle());
            }
            if(!StringUtils.isBlank(post.getTitleEncode())){
                post_old.setTitleEncode(post.getTitleEncode());
            }
            if(!StringUtils.isBlank(post.getDigest())){
                post_old.setDigest(post.getDigest());
            }
            if(!StringUtils.isBlank(post.getDigestEncode())){
                post_old.setDigestEncode(post.getDigestEncode());
            }
            if(!StringUtils.isBlank(post.getContentUrl())){
                post_old.setContentUrl(post.getContentUrl());
            }
            if(!StringUtils.isBlank(post.getSourceUrl())){
                post_old.setSourceUrl(post.getSourceUrl());
            }
            if(!StringUtils.isBlank(post.getCover())){
                post_old.setCover(post.getCover());
            }
            if(post.getIsMulti() != null){
                post_old.setIsMulti(post.getIsMulti());
            }
            if(post.getIsTop() != null){
                post_old.setIsTop(post.getIsTop());
            }
            if(post.getDatetime() != null){
                post_old.setDatetime(post.getDatetime());
            }
            if(post.getReadNum() != null){
                post_old.setReadNum(post.getReadNum());
            }
            if(post.getLikeNum() != null){
                post_old.setLikeNum(post.getLikeNum());
            }
            if(post.getIsExsist() != null){
                post_old.setIsExsist(post.getIsExsist());
            }


            int row = postServiceImpl.update(post_old);
            if(row > 0) {
                return new String("修改公众号文章表成功");
            }else{
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改公众号文章表失败");
            }
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "修改公众号文章表数据异常");
        }
    }


    /**
     * 获取单公众号文章表实体
     *
     * @param postId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/post/{postId}", method = RequestMethod.GET)
    public Post getPost(@PathVariable String postId) {
        try {
            if(StringUtils.isBlank(postId)){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "参数不正确！");
            }
            Map<String,Object> whereParams = new HashMap<String, Object>();
            whereParams.put("id", postId);
            Post post= (Post) postServiceImpl.queryOne(whereParams);
            if(null == post){
                throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "系统未找到公众号文章表相关数据！");
            }
            return post;
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "获取单公众号文章表实体异常");
        }
    }


    /**
     * 公众号文章表数据输出 带分页
     *
     * @param post 过滤条件
     * @param page      第几页
     * @return 业务数据列表实体，最终转换为json数据输出
     * @throws ServletException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/postlist", method = RequestMethod.POST)
    public BizData4Page postlist(Post post, Integer page) {
        try {
            Map<String, Object> whereParams = new HashMap<String, Object>();
            if(post.getId() != null){
                whereParams.put("id", new SearchField("id", "=", post.getId()));
            }
            if(!StringUtils.isBlank(post.getBiz())){
                whereParams.put("biz", new SearchField("biz", "like", "%" + post.getBiz() + "%"));
            }
            if(post.getFieldId() != null){
                whereParams.put("field_id", new SearchField("field_id", "=", post.getFieldId()));
            }
            if(!StringUtils.isBlank(post.getTitle())){
                whereParams.put("title", new SearchField("title", "like", "%" + post.getTitle() + "%"));
            }
            if(!StringUtils.isBlank(post.getTitleEncode())){
                whereParams.put("title_encode", new SearchField("title_encode", "like", "%" + post.getTitleEncode() + "%"));
            }
            if(!StringUtils.isBlank(post.getDigest())){
                whereParams.put("digest", new SearchField("digest", "like", "%" + post.getDigest() + "%"));
            }
            if(!StringUtils.isBlank(post.getDigestEncode())){
                whereParams.put("digest_encode", new SearchField("digest_encode", "like", "%" + post.getDigestEncode() + "%"));
            }
            if(!StringUtils.isBlank(post.getContentUrl())){
                whereParams.put("content_url", new SearchField("content_url", "like", "%" + post.getContentUrl() + "%"));
            }
            if(!StringUtils.isBlank(post.getSourceUrl())){
                whereParams.put("source_url", new SearchField("source_url", "like", "%" + post.getSourceUrl() + "%"));
            }
            if(!StringUtils.isBlank(post.getCover())){
                whereParams.put("cover", new SearchField("cover", "like", "%" + post.getCover() + "%"));
            }
            if(post.getIsMulti() != null){
                whereParams.put("is_multi", new SearchField("is_multi", "=", post.getIsMulti()));
            }
            if(post.getIsTop() != null){
                whereParams.put("is_top", new SearchField("is_top", "=", post.getIsTop()));
            }
            if(post.getDatetime() != null){
                whereParams.put("datetime", new SearchField("datetime", "=", post.getDatetime()));
            }
            if(post.getReadNum() != null){
                whereParams.put("readNum", new SearchField("readNum", "=", post.getReadNum()));
            }
            if(post.getLikeNum() != null){
                whereParams.put("likeNum", new SearchField("likeNum", "=", post.getLikeNum()));
            }
            if(post.getIsExsist() != null){
                whereParams.put("is_exsist", new SearchField("is_exsist", "=", post.getIsExsist()));
            }


            //other props filter
            whereParams.put("groupOp", "and");

            BizData4Page bizData4Page = new BizData4Page();
            bizData4Page.setConditions(whereParams);
            if (page != null) {
                bizData4Page.setPage(page);
            }

            postServiceImpl.queryPageByDataPerm(bizData4Page);
            return bizData4Page;
        } catch (BizException bize) {
            throw bize;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizException(RtnCodeEnum.UNKNOW.getValue(), "查询公众号文章表数据异常");
        }
    }
}
