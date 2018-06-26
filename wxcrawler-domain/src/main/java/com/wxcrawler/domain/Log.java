/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  Log.java 2018-06-25 18:32:37 $
 */





package com.wxcrawler.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.wxcrawler.service.MyBaseDomain;


import java.util.*;

public class Log extends MyBaseDomain<Integer>{

    /**  */
    private Integer id;


    /** 错误信息 */
    private String error;


    /** 参数str */
    private String str;


    /** 参数url */
    private String url;


	public Log(){
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
	 *错误信息
	 * @param value
	 */
	public void setError(String value) {
        this.error = value;
    }
	/**
	 *错误信息
	 * @return
	 */
    public String getError() {
        return this.error;
    }

	/**
	 *参数str
	 * @param value
	 */
	public void setStr(String value) {
        this.str = value;
    }
	/**
	 *参数str
	 * @return
	 */
    public String getStr() {
        return this.str;
    }

	/**
	 *参数url
	 * @param value
	 */
	public void setUrl(String value) {
        this.url = value;
    }
	/**
	 *参数url
	 * @return
	 */
    public String getUrl() {
        return this.url;
    }


	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("Error",getError())
			.append("Str",getStr())
			.append("Url",getUrl())
			.toString();
	}

	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}

	public boolean equals(Object obj) {
		if(obj instanceof Log == false) return false;
		if(this == obj) return true;
		Log other = (Log)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

