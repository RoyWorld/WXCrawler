/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: wxcrawler
 * $Id:  LogFacade.java 2018-06-25 18:32:37 $
 */
package com.wxcrawler.facade;

import com.wxcrawler.domain.Log;
import com.wxcrawler.service.IMyBaseDAO;
import com.wxcrawler.service.IMyPersistenceProvider;

public interface ILogFacade extends IMyPersistenceProvider<IMyBaseDAO, Log> {

}
