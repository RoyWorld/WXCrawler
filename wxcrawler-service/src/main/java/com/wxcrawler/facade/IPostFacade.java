/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  PostFacade.java 2018-05-31 10:45:15 $
 */
package com.wxcrawler.facade;

import com.wxcrawler.domain.Post;
import com.wxcrawler.service.IMyBaseDAO;
import com.wxcrawler.service.IMyPersistenceProvider;

public interface IPostFacade extends IMyPersistenceProvider<IMyBaseDAO, Post> {

}
