/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  PostServiceImpl.java 2018-05-31 10:45:15 $
 */
package com.wxcrawler.service.impl;

import com.wxcrawler.dao.IPostDAO;
import com.wxcrawler.domain.Post;
import com.wxcrawler.facade.IPostFacade;
import com.wxcrawler.service.IMyBaseDAO;
import com.wxcrawler.service.MyAbstractPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("IPostServiceImpl")
public class IPostServiceImpl extends MyAbstractPageService<IMyBaseDAO, Post> implements IPostFacade{
    @Autowired
    private IPostDAO dao;

    @Override
    public IPostDAO getDao() {
        return dao;
    }
}
