/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  Weixin.java 2018-06-12 10:34:50 $
 */





package com.wxcrawler.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.wxcrawler.service.MyBaseDomain;


import java.util.*;

public class Weixin extends MyBaseDomain<Integer>{

    /**  */
    private Integer id;


    /** 公众号唯一标识biz */
    private String biz;


    /** 公众号昵称 */
    private String nickName;


    /** 公众号头像 */
    private String avatar;


    /** 记录采集时间的时间戳 */
    private Integer collect;


	public Weixin(){
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
	 *公众号唯一标识biz
	 * @param value
	 */
	public void setBiz(String value) {
        this.biz = value;
    }
	/**
	 *公众号唯一标识biz
	 * @return
	 */
    public String getBiz() {
        return this.biz;
    }

	/**
	 *公众号昵称
	 * @param value
	 */
	public void setNickName(String value) {
        this.nickName = value;
    }
	/**
	 *公众号昵称
	 * @return
	 */
    public String getNickName() {
        return this.nickName;
    }

	/**
	 *公众号头像
	 * @param value
	 */
	public void setAvatar(String value) {
        this.avatar = value;
    }
	/**
	 *公众号头像
	 * @return
	 */
    public String getAvatar() {
        return this.avatar;
    }

	/**
	 *记录采集时间的时间戳
	 * @param value
	 */
	public void setCollect(Integer value) {
        this.collect = value;
    }
	/**
	 *记录采集时间的时间戳
	 * @return
	 */
    public Integer getCollect() {
        return this.collect;
    }


	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("Biz",getBiz())
			.append("NickName",getNickName())
			.append("Avatar",getAvatar())
			.append("Collect",getCollect())
			.toString();
	}

	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}

	public boolean equals(Object obj) {
		if(obj instanceof Weixin == false) return false;
		if(this == obj) return true;
		Weixin other = (Weixin)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

