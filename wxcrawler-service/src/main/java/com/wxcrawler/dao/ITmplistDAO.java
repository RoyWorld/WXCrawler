/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  TmplistDAO.java 2018-05-31 10:45:16 $
 */
package com.wxcrawler.dao;

import com.wxcrawler.service.IMyBaseDAO;
import com.wxcrawler.domain.Tmplist;

public interface ITmplistDAO extends IMyBaseDAO<Tmplist>{
	
	Tmplist findByContentUrl(String contentUrl);

}
