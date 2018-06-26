/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  LogServiceImpl.java 2018-06-25 18:32:37 $
 */
package com.wxcrawler.service.impl;

import com.wxcrawler.dao.ILogDAO;
import com.wxcrawler.domain.Log;
import com.wxcrawler.facade.ILogFacade;
import com.wxcrawler.service.IMyBaseDAO;
import com.wxcrawler.service.MyAbstractPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("ILogServiceImpl")
public class ILogServiceImpl extends MyAbstractPageService<IMyBaseDAO, Log> implements ILogFacade{
    @Autowired
    private ILogDAO dao;

    @Override
    public ILogDAO getDao() {
        return dao;
    }
}
