/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  WeixinDAO.java 2018-05-31 10:45:16 $
 */
package com.wxcrawler.dao;

import com.wxcrawler.service.IMyBaseDAO;
import com.wxcrawler.domain.Weixin;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IWeixinDAO extends IMyBaseDAO<Weixin>{

    List<Map<String, Object>> getWXList(@Param("offset") Integer offset, @Param("rows") Integer rows);
}
