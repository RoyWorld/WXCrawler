/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  Post.java 2018-06-07 15:42:16 $
 */





package com.wxcrawler.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.wxcrawler.service.MyBaseDomain;


import java.util.*;

public class Post extends MyBaseDomain<Integer>{

    /**  */
    private Integer id;


    /** 文章对应的公众号biz */
    private String biz;


    /** 微信定义的一个id，每条文章唯一 */
    private Integer fieldId;


    /** 文章标题 */
    private String title;


    /** 文章编码，防止文章出现emoji */
    private String titleEncode;


    /** 文章摘要 */
    private String digest;


    /** 文章地址 */
    private String contentUrl;


    /** 阅读原文地址 */
    private String sourceUrl;


    /** 封面图片 */
    private String cover;


    /** 是否多图文 */
    private Integer isMulti;


    /** 是否头条 */
    private Integer isTop;


    /** 文章时间戳 */
    private Integer datetime;


    /** 文章阅读量 */
    private Integer readNum;


    /** 文章点赞量 */
    private Integer likeNum;


    /** 是否已爬取 */
    private Integer isExsist;


	public Post(){
	}
	/**
	 *
	 * @param value
	 */
	public void setId(Integer value) {
        this.id = value;
    }
	/**
	 *
	 * @return
	 */
    public Integer getId() {
        return this.id;
    }

	/**
	 *文章对应的公众号biz
	 * @param value
	 */
	public void setBiz(String value) {
        this.biz = value;
    }
	/**
	 *文章对应的公众号biz
	 * @return
	 */
    public String getBiz() {
        return this.biz;
    }

	/**
	 *微信定义的一个id，每条文章唯一
	 * @param value
	 */
	public void setFieldId(Integer value) {
        this.fieldId = value;
    }
	/**
	 *微信定义的一个id，每条文章唯一
	 * @return
	 */
    public Integer getFieldId() {
        return this.fieldId;
    }

	/**
	 *文章标题
	 * @param value
	 */
	public void setTitle(String value) {
        this.title = value;
    }
	/**
	 *文章标题
	 * @return
	 */
    public String getTitle() {
        return this.title;
    }

	/**
	 *文章编码，防止文章出现emoji
	 * @param value
	 */
	public void setTitleEncode(String value) {
        this.titleEncode = value;
    }
	/**
	 *文章编码，防止文章出现emoji
	 * @return
	 */
    public String getTitleEncode() {
        return this.titleEncode;
    }

	/**
	 *文章摘要
	 * @param value
	 */
	public void setDigest(String value) {
        this.digest = value;
    }
	/**
	 *文章摘要
	 * @return
	 */
    public String getDigest() {
        return this.digest;
    }

	/**
	 *文章地址
	 * @param value
	 */
	public void setContentUrl(String value) {
        this.contentUrl = value;
    }
	/**
	 *文章地址
	 * @return
	 */
    public String getContentUrl() {
        return this.contentUrl;
    }

	/**
	 *阅读原文地址
	 * @param value
	 */
	public void setSourceUrl(String value) {
        this.sourceUrl = value;
    }
	/**
	 *阅读原文地址
	 * @return
	 */
    public String getSourceUrl() {
        return this.sourceUrl;
    }

	/**
	 *封面图片
	 * @param value
	 */
	public void setCover(String value) {
        this.cover = value;
    }
	/**
	 *封面图片
	 * @return
	 */
    public String getCover() {
        return this.cover;
    }

	/**
	 *是否多图文
	 * @param value
	 */
	public void setIsMulti(Integer value) {
        this.isMulti = value;
    }
	/**
	 *是否多图文
	 * @return
	 */
    public Integer getIsMulti() {
        return this.isMulti;
    }

	/**
	 *是否头条
	 * @param value
	 */
	public void setIsTop(Integer value) {
        this.isTop = value;
    }
	/**
	 *是否头条
	 * @return
	 */
    public Integer getIsTop() {
        return this.isTop;
    }

	/**
	 *文章时间戳
	 * @param value
	 */
	public void setDatetime(Integer value) {
        this.datetime = value;
    }
	/**
	 *文章时间戳
	 * @return
	 */
    public Integer getDatetime() {
        return this.datetime;
    }

	/**
	 *文章阅读量
	 * @param value
	 */
	public void setReadNum(Integer value) {
        this.readNum = value;
    }
	/**
	 *文章阅读量
	 * @return
	 */
    public Integer getReadNum() {
        return this.readNum;
    }

	/**
	 *文章点赞量
	 * @param value
	 */
	public void setLikeNum(Integer value) {
        this.likeNum = value;
    }
	/**
	 *文章点赞量
	 * @return
	 */
    public Integer getLikeNum() {
        return this.likeNum;
    }

	/**
	 *是否已爬取
	 * @param value
	 */
	public void setIsExsist(Integer value) {
        this.isExsist = value;
    }
	/**
	 *是否已爬取
	 * @return
	 */
    public Integer getIsExsist() {
        return this.isExsist;
    }


	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("Biz",getBiz())
			.append("FieldId",getFieldId())
			.append("Title",getTitle())
			.append("TitleEncode",getTitleEncode())
			.append("Digest",getDigest())
			.append("ContentUrl",getContentUrl())
			.append("SourceUrl",getSourceUrl())
			.append("Cover",getCover())
			.append("IsMulti",getIsMulti())
			.append("IsTop",getIsTop())
			.append("Datetime",getDatetime())
			.append("ReadNum",getReadNum())
			.append("LikeNum",getLikeNum())
			.append("IsExsist",getIsExsist())
			.toString();
	}

	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}

	public boolean equals(Object obj) {
		if(obj instanceof Post == false) return false;
		if(this == obj) return true;
		Post other = (Post)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

