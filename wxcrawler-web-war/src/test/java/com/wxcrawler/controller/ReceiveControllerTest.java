package com.wxcrawler.controller;

import com.wxcrawler.util.PropertiesHelper;
import com.wxcrawler.util.ScanTmplistJob;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Test
    public void test1() throws InterruptedException {
        String str = "{\"title\":\"\"麻辣粉\"扩容大放水，债务爆雷，央妈要兜底？| 檀几条\",\"digest\":\"麻辣粉\"大放水，债务爆雷，央妈要兜底？\",\"content\":\"\",\"fileid\":508719873,\"content_url\":\"http:\\/\\/mp.weixin.qq.com\\/s?__biz=MzA3OTI2OTI5NA==&mid=2656203542&idx=4&sn=f7860c9d469fd0039966e43f29d0990a&chksm=8410cfd7b36746c14fa42a1f04351a26e3eb794b0a2ddee20629454c1ada5c5d4dde57590734&scene=27#wechat_redirect\",\"source_url\":\"\",\"cover\":\"http:\\/\\/mmbiz.qpic.cn\\/mmbiz_jpg\\/lv8zajRJMyl8JaicKYBGzwCxtPORPhDlfVkU9WdEpo3LBVMFGoPGxMdcRc7DCiclUVQiaP598nO8M4Sz7A40tGRjA\\/0?wx_fmt=jpeg\",\"author\":\"马兰\",\"copyright_stat\":11,\"del_flag\":1,\"item_show_type\":0,\"audio_fileid\":0,\"duration\":0,\"play_url\":\"\",\"malicious_title_reason_id\":0,\"malicious_content_type\":0},{\"title\":\"除甲醛，它比活性炭强1000倍！| 檀生活\",\"digest\":\"除醛除异味，一举两得\",\"content\":\"\",\"fileid\":508719856,\"content_url\":\"http:\\/\\/mp.weixin.qq.com\\/s?__biz=MzA3OTI2OTI5NA==&mid=2656203542&idx=5&sn=63e1083a7dd98c9cf6cac6e8ab6ac270&chksm=8410cfd7b36746c121fd630f87c1149a087ecb8cc8ffb98c3c87aa13e6e7dbb114c86a78a362&scene=27#wechat_redirect\",\"source_url\":\"https:\\/\\/h5.yit.com\\/product.html?product_id=65908&channel=862\",\"cover\":\"http:\\/\\/mmbiz.qpic.cn\\/mmbiz_jpg\\/lv8zajRJMyl8JaicKYBGzwCxtPORPhDlfNvyIjfKWYcVCTltg29ZEjRdCNO2jGoQEKDUEgJ2Hvpus2dp4rfN9Fw\\/0?wx_fmt=jpeg\",\"author\":\"\",\"copyright_stat\":101,\"del_flag\":1,\"item_show_type\":0,\"audio_fileid\":0,\"duration\":0,\"play_url\":\"\",\"malicious_title_reason_id\":0,\"malicious_content_type\":0}";


        Pattern picNamePattern = Pattern.compile("(?<=title\":\")[^\"]{0,30}\"[^\"]{0,10}");

        String fileInfo = "";
        Matcher fileNameMatch = picNamePattern.matcher(str);
        if (fileNameMatch.find()){
            fileInfo = fileNameMatch.group();
        }

        fileInfo = fileInfo.replaceAll("\"", "");
//        fileInfo = fileInfo.substring(1, fileInfo.length()-1);

        System.out.println(fileInfo);

        str = str.replaceAll("(?<=digest\":\")[^\"]{0,30}\"[^\"]{0,10}", fileInfo);

        System.out.println(str);
    }


    @Test
    public void test2() throws InterruptedException, IOException {
//        String indexDirectoryPath = "E:\\indexD";
//        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
//        IndexWriter writer = new IndexWriter(indexDirectory, new IndexWriterConfig());
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("E:\\book\\english\\The-Children-Act-Ian-Mcewan.epub")));
//
//        Document document = new Document();
//        document.add(new StringField("title", "The-Children-Act-Ian-Mcewan.epub", Field.Store.YES));
//        document.add(new TextField("body", reader));
//
//        writer.addDocument(document);
//        String searchQuery = "";
//        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
//        QueryParser queryParser = new QueryParser();
//        Query query = QueryParser.parse(searchQuery);
//        indexSearcher.search(query);

    }
}