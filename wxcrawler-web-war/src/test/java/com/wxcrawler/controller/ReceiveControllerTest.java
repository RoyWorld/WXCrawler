package com.wxcrawler.controller;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * Created by RoyChan on 2018/6/4.
 */
public class ReceiveControllerTest {

    @Test
    public void test() throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        Thread.sleep(10000000);
    }
}