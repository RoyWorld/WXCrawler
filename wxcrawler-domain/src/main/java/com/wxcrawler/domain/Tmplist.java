/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  Tmplist.java 2018-05-31 10:45:16 $
 */





package com.wxcrawler.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.wxcrawler.service.MyBaseDomain;


import java.util.*;

public class Tmplist extends MyBaseDomain<Integer>{

    /**  */
    private Integer id;


    /** 文章地址 */
    private String contentUrl;


    /** 读取中标记 */
    private Integer load;


	public Tmplist(){
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
	 *读取中标记
	 * @param value
	 */
	public void setLoad(Integer value) {
        this.load = value;
    }
	/**
	 *读取中标记
	 * @return
	 */
    public Integer getLoad() {
        return this.load;
    }


	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("ContentUrl",getContentUrl())
			.append("Load",getLoad())
			.toString();
	}

	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}

	public boolean equals(Object obj) {
		if(obj instanceof Tmplist == false) return false;
		if(this == obj) return true;
		Tmplist other = (Tmplist)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

