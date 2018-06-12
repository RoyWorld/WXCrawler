package com.wxcrawler.controller;

import com.wxcrawler.util.ScanTmplistJob;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by RoyChan on 2018/6/4.
 */
public class ReceiveControllerTest {

    @Test
    public void test() throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        ScanTmplistJob scanTmplistJob = (ScanTmplistJob) context.getBean("scanTmpListJob");
        Thread.sleep(10000000);
    }
}