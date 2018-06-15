package com.wxcrawler.util;

import com.wxcrawler.annotation.LogAfter;
import com.wxcrawler.annotation.LogBefore;
import com.wxcrawler.domain.Post;
import com.wxcrawler.domain.SearchField;
import com.wxcrawler.domain.Tmplist;
import com.wxcrawler.service.impl.IPostServiceImpl;
import com.wxcrawler.service.impl.ITmplistServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by RoyChan on 2018/6/5.
 */
public class ScanTmplistJob {
    Logger logger = LoggerFactory.getLogger(ScanTmplistJob.class);

    @Autowired
    ITmplistServiceImpl iTmplistService;

    @Autowired
    IPostServiceImpl iPostService;

    @Autowired
    PostCrawler postCrawler;

    //爬虫队列
    LinkedBlockingQueue crawlerQueue = new LinkedBlockingQueue<>(1024);

    // 命名线程工厂
    static class CrawlerThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix = "CrawlerPool-thread-";

        CrawlerThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    //爬虫线程池
    ExecutorService threadPoolExecutor = new ThreadPoolExecutor(10, 20, 3, TimeUnit.SECONDS, crawlerQueue, new CrawlerThreadFactory(), new ThreadPoolExecutor.AbortPolicy());


    /**
     * 定时任务执行方法，每10分钟执行一次
     * 扫描任务表是否有任务需要执行
     * @throws InterruptedException
     */
    @LogBefore(clazz = ScanTmplistJob.class, msg = "------------ 开始扫描 ------------")
    @LogAfter(clazz = ScanTmplistJob.class, msg = "------------ 暂无任务 ------------")
    public void scan() throws InterruptedException {
        Map<String, Object> condition = new HashMap<>(16);
        condition.put("loading", new SearchField("loading", "=", 1));
        List<Tmplist> tmplists = iTmplistService.queryList(condition);

        //爬取文章数据
        executeCrawler(tmplists);
    }


    public void executeCrawler(List<Tmplist> tmplists) throws InterruptedException {
        if (tmplists.size() != 0){
            CountDownLatch countDownLatch = new CountDownLatch(tmplists.size());
            tmplists.forEach((tmplist)-> {
                threadPoolExecutor.execute(new PostCrawlerTask(tmplist, countDownLatch));
            });
            while (countDownLatch.getCount() != 0) {
                Thread.sleep(5000);
            }
        }
    }

    /**
     * Post爬虫任务
     */
    class PostCrawlerTask implements Runnable{

        Tmplist tmplist;
        CountDownLatch countDownLatch;

        public PostCrawlerTask(Tmplist tmplist, CountDownLatch countDownLatch) {
            this.tmplist = tmplist;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            logger.debug(String.format("------------ %s开始爬取 ------------", Thread.currentThread().getName()));

            //找到原文章
            Map<String, Object> postCondition = new HashMap<>();
            postCondition.put("content_url",  new SearchField("content_url", "=", tmplist.getContentUrl()));
            logger.debug(String.format("------------ tmplist.content_url ：%s ------------", tmplist.getContentUrl()));
            Post post = iPostService.queryOne(postCondition);

            //爬取文章
            postCrawler.crawlerContent(tmplist.getContentUrl(), post.getBiz(), post.getId());

            //更新tmplist为2
            tmplist.setLoading(2);
            iTmplistService.update(tmplist);

            post.setIsExsist(1);
            countDownLatch.countDown();
            logger.debug(String.format("------------ %s爬取结束 ------------", Thread.currentThread().getName()));
        }
    }
}
