/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  WeixinFacade.java 2018-05-31 10:45:16 $
 */
package com.wxcrawler.facade;

import com.wxcrawler.domain.Weixin;
import com.wxcrawler.service.IMyBaseDAO;
import com.wxcrawler.service.IMyPersistenceProvider;

public interface IWeixinFacade extends IMyPersistenceProvider<IMyBaseDAO, Weixin> {

}
