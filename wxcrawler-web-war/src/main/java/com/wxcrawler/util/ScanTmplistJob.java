package com.wxcrawler.util;

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
    public void scan() throws InterruptedException {
        Map<String, Object> condition = new HashMap<>(16);
        condition.put("loading", new SearchField("loading", "=", 0));
        List<Tmplist> tmplists = iTmplistService.queryList(condition);
        if (tmplists.size() != 0){
            threadPoolExecutor.execute(()-> System.out.println(Thread.currentThread().getName()));
            threadPoolExecutor.shutdown();
            while (!threadPoolExecutor.isTerminated()){
                Thread.sleep(5000);
            }
        }else {
            logger.debug("------------ 暂无任务 ------------");
        }
    }

    /**
     * Post爬虫任务
     */
    class PostCrawlerTask implements Runnable{

        Tmplist tmplist;

        public PostCrawlerTask(Tmplist tmplist) {
            this.tmplist = tmplist;
        }

        @Override
        public void run() {
            logger.debug(String.format("------------ %s开始任务 ------------", Thread.currentThread().getName()));
            Map<String, Object> postCondition = new HashMap<>();
            postCondition.put("content_url",  new SearchField("content_url", "=", tmplist.getContentUrl()));
            Post post = iPostService.queryOne(postCondition);

            postCrawler.crawlerContent(tmplist.getContentUrl(), post.getBiz(), post.getId());
            logger.debug(String.format("------------ %s任务结束 ------------", Thread.currentThread().getName()));
        }
    }
}
