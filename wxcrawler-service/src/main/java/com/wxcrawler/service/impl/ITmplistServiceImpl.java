/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  TmplistServiceImpl.java 2018-05-31 10:45:16 $
 */
package com.wxcrawler.service.impl;

import com.wxcrawler.dao.ITmplistDAO;
import com.wxcrawler.domain.Tmplist;
import com.wxcrawler.facade.ITmplistFacade;
import com.wxcrawler.service.IMyBaseDAO;
import com.wxcrawler.service.MyAbstractPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("ITmplistServiceImpl")
public class ITmplistServiceImpl extends MyAbstractPageService<IMyBaseDAO, Tmplist> implements ITmplistFacade{
    @Autowired
    private ITmplistDAO dao;

    @Override
    public ITmplistDAO getDao() {
        return dao;
    }
}
