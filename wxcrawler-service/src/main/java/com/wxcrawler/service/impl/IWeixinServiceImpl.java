/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  WeixinServiceImpl.java 2018-05-31 10:45:16 $
 */
package com.wxcrawler.service.impl;

import com.wxcrawler.dao.IWeixinDAO;
import com.wxcrawler.domain.Weixin;
import com.wxcrawler.facade.IWeixinFacade;
import com.wxcrawler.service.IMyBaseDAO;
import com.wxcrawler.service.MyAbstractPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("IWeixinServiceImpl")
public class IWeixinServiceImpl extends MyAbstractPageService<IMyBaseDAO, Weixin> implements IWeixinFacade{
    @Autowired
    private IWeixinDAO dao;

    @Override
    public IWeixinDAO getDao() {
        return dao;
    }
}
