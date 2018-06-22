package com.wxcrawler.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.wxcrawler.domain.Post;
import com.wxcrawler.domain.SearchField;
import com.wxcrawler.domain.Tmplist;
import com.wxcrawler.domain.Weixin;
import com.wxcrawler.service.impl.IPostServiceImpl;
import com.wxcrawler.service.impl.ITmplistServiceImpl;
import com.wxcrawler.service.impl.IWeixinServiceImpl;
import com.wxcrawler.util.PicUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RoyChan on 2018/5/31.
 */
@Controller
public class ReceiveController {

    Logger logger = LoggerFactory.getLogger(ReceiveController.class);

    @Autowired
    IPostServiceImpl iPostService;
    @Autowired
    ITmplistServiceImpl iTmplistService;
    @Autowired
    IWeixinServiceImpl iWeixinService;

    private String uin;
    private String key;
    private String pass_ticket;

    Pattern jsonErrorPattern = Pattern.compile("(?<=title\":\")[^\"]{0,10}\"[^\"]{0,10}\"");

    private String rootPath = "E:\\公众号";

    @ResponseBody
    @RequestMapping(value = "/getMsgJson", method = RequestMethod.POST)
    public Object getMsgJson(String str, String url) {
        try {
            //先针对url参数进行操作
            MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(URLDecoder.decode(url, "UTF-8")).build().getQueryParams();
            //得到公众号的biz
            String biz = parameters.get("__biz").get(0);

            //得到必要的参数
            uin = parameters.get("uin").get(0);
            key = parameters.get("key").get(0);
            pass_ticket = parameters.get("pass_ticket").get(0);

            //接下来进行以下操作
            //从数据库中查询biz是否已经存在，如果不存在则插入，这代表着我们新添加了一个采集目标公众号
            insertWeixin(biz);

            //再解析str变量
            //首先进行json_decode
            str = htmlSpecialCharsDecode(str);
//            logger.error(String.format("str: %s", str));
            JSONObject json;
            try{
                json = (JSONObject) JSONArray.parse(str);
            }catch (JSONException e){
                String jsonRex = "";
                Matcher jsonRexMatch = jsonErrorPattern.matcher(str);
                if (jsonRexMatch.find()){
                    jsonRex = jsonRexMatch.group();
                }
                jsonRex = jsonRex.substring(1, jsonRex.length()-1);

                str = str.replaceAll("(?<=title\":\")[^\"]{0,10}\"[^\"]{0,10}\"", jsonRex);
                json = (JSONObject) JSONArray.parse(str);
            }

            JSONArray list = (JSONArray) json.get("list");
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                JSONObject value = (JSONObject) iterator.next();
                int type = (int) getJsonProperty(value, "comm_msg_info", "type");

                if (type == 49) {//type=49代表是图文消息
                    //获得图文消息的链接地址
                    String content_url = htmlSpecialCharsDecode((String) getJsonProperty(value, "app_msg_ext_info", "content_url")).replace("\\", "");
                    //是否是多图文消息
                    int is_multi = (int) getJsonProperty(value, "app_msg_ext_info", "is_multi");
                    //图文消息发送时间
                    int datetime = (int) getJsonProperty(value, "comm_msg_info", "datetime");
                    //在这里将图文消息链接地址插入到采集队列库中（队列库将在后文介绍，主要目的是建立一个批量采集队列，另一个程序将根据队列安排下一个采集的公众号或者文章内容）


                    //在这里根据content_url从数据库中判断一下是否重复
                    //数据库中不存在相同的content_url
                    if (exsistContentUrl(biz, content_url)) {
                        //一个微信给的id
                        int fileid = (int) getJsonProperty(value, "app_msg_ext_info", "fileid");
                        //文章标题
                        String title = (String) getJsonProperty(value, "app_msg_ext_info", "title");
                        //建议将标题进行编码，这样就可以存储emoji特殊符号了
                        String title_encode = URLEncoder.encode(title.replace(" ", "&nbsp;"), "UTF-8");
                        //文章摘要
                        String digest = (String) getJsonProperty(value, "app_msg_ext_info", "digest");
                        //建议将标题进行编码，这样就可以存储emoji特殊符号了
                        String digest_encode = URLEncoder.encode(digest.replace(" ", "&nbsp;"), "UTF-8");
                        //阅读原文的链接
                        String source_url = htmlSpecialCharsDecode((String) getJsonProperty(value, "app_msg_ext_info", "source_url")).replace("\\", "");
                        //封面图片
                        String cover = htmlSpecialCharsDecode((String) getJsonProperty(value, "app_msg_ext_info", "cover")).replace("\\", "");
                        //标记一下是头条内容
                        int is_top = 1;

                        //现在存入数据库
                        savePost(biz, fileid, title_encode, digest_encode, content_url, source_url, cover, is_multi, is_top, datetime);
                        if (exsistContentUrlInTmplist(content_url)){
                            saveTmp(content_url);
                        }
                        logger.debug("头条标题：" + title);
                    }
                    //如果是多图文消息
                    if (is_multi == 1) {
                        JSONArray msg_item_list = (JSONArray) getJsonProperty(value, "app_msg_ext_info", "multi_app_msg_item_list");
                        Iterator msg_iterator = msg_item_list.iterator();
                        //循环后面的图文消息
                        while (msg_iterator.hasNext()) {
                            JSONObject msg_item_value = (JSONObject) msg_iterator.next();
                            //图文消息链接地址
                            String msg_item_content_url = htmlSpecialCharsDecode((String) msg_item_value.get("content_url")).replace("\\", "");
                            //这里再次根据content_url判断一下数据库中是否重复以免出错
                            if (exsistContentUrl(biz, msg_item_content_url)) {
                                //在这里将图文消息链接地址插入到采集队列库中（队列库将在后文介绍，主要目的是建立一个批量采集队列，另一个程序将根据队列安排下一个采集的公众号或者文章内容）
                                //文章标题
                                String title = (String) msg_item_value.get("title");
                                //一个微信给的id
                                int fileid = (int) msg_item_value.get("fileid");
                                //建议将标题进行编码，这样就可以存储emoji特殊符号了
                                String title_encode = URLEncoder.encode(title.replace(" ", "&nbsp;"), "UTF-8");
                                //文章摘要
                                String digest = (String) msg_item_value.get("digest");
                                //文章摘要
                                String digest_encode = URLEncoder.encode(digest.replace(" ", "&nbsp;"), "UTF-8");
                                //阅读原文的链接
                                String source_url = htmlSpecialCharsDecode((String) msg_item_value.get("source_url")).replace("\\", "");
                                //封面图片
                                String cover = htmlSpecialCharsDecode((String) msg_item_value.get("cover")).replace("\\", "");

                                //现在存入数据库
                                savePost(biz, fileid, title_encode, digest_encode, msg_item_content_url, source_url, cover, is_multi, 0, datetime);
                                if (exsistContentUrlInTmplist(msg_item_content_url)){
                                    saveTmp(msg_item_content_url);
                                }
                                logger.debug("标题：" + title);
                            }

                        }
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, Object> result = new HashMap<>(16);
        result.put("msg", "success");
        return result;
    }

    /**
     * 从数据库中查询biz是否已经存在，如果不存在则插入，这代表着我们新添加了一个采集目标公众号
     * @param biz
     */
    private void insertWeixin(String biz){
        Map<String, Object> condition = new HashMap<>(16);
        condition.put("biz", biz);
        Weixin weixin = iWeixinService.queryOne(condition);
        if (weixin == null){
            weixin = new Weixin();
            weixin.setBiz(biz);
            weixin.setCollect((int) (System.currentTimeMillis()/1000));
            iWeixinService.insert(weixin);
        }
    }

    /**
     * 对str中的html特殊字符进行解码
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    private String htmlSpecialCharsDecode(String str) throws UnsupportedEncodingException {
        return StringEscapeUtils.unescapeHtml(URLDecoder.decode(str, "UTF-8"));
    }

    /**
     * 根据content_url从数据库中判断一下是否重复
     * @param biz
     * @param content_url
     * @return
     */
    private boolean exsistContentUrl(String biz, String content_url) {
        Map<String, Object> condition = new HashMap<>(16);
        condition.put("biz", new SearchField("biz", "=", biz));
        condition.put("content_url", new SearchField("content_url", "=", content_url));
        Post post = iPostService.queryOne(condition);
        return post == null;
    }

    /**
     * 根据content_url从数据库中判断一下是否重复
     * @param content_url
     * @return
     */
    private boolean exsistContentUrlInTmplist(String content_url) {
        Map<String, Object> condition = new HashMap<>(16);
        condition.put("content_url", new SearchField("content_url", "=", content_url));
        Tmplist tmplist = iTmplistService.queryOne(condition);
        return tmplist == null;
    }

    /**
     * post存入数据库
     * @param biz
     * @param fileid
     * @param title_encode
     * @param digest_encode
     * @param content_url
     * @param source_url
     * @param cover
     * @param is_multi
     * @param is_top
     * @param datetime
     */
    private void savePost(String biz, int fileid, String title_encode, String digest_encode, String content_url, String source_url, String cover, int is_multi, int is_top, int datetime) {
        String[] coverPicInfo = PicUtil.getPicInfoFromUrl(cover);
        String coverPic = coverPicInfo[1] + "." + coverPicInfo[0];

        Post newPost = new Post();
        newPost.setBiz(biz);
        newPost.setFieldId(fileid);
        newPost.setTitle("");
        newPost.setTitleEncode(title_encode);
        newPost.setDigest("");
        newPost.setDigestEncode(digest_encode);
        newPost.setContentUrl(content_url);
        newPost.setSourceUrl(source_url);
        newPost.setCover(cover);
        newPost.setIsMulti(is_multi);
        newPost.setIsTop(is_top);
        newPost.setDatetime(datetime);
        newPost.setReadNum(1);//set default
        newPost.setLikeNum(0);//set default
        newPost.setIsExsist(0);//set default
        iPostService.insert(newPost);

        PicUtil.savePic(cover, rootPath + String.format("//%s//%s", biz, newPost.getId()), coverPic, coverPicInfo[1]);
    }

    /**
     * tmplist存入数据库
     * @param content_url
     */
    private void saveTmp(String content_url){
        Tmplist tmplist = new Tmplist();
        tmplist.setContentUrl(content_url);
        tmplist.setLoading(0);
        iTmplistService.insert(tmplist);
    }

    /**
     * 取出json中的数据
     * @param jsonObject
     * @param n1
     * @param n2
     * @return
     */
    private Object getJsonProperty(JSONObject jsonObject, String n1, String n2) {
        return ((JSONObject) jsonObject.get(n1)).get(n2);
    }



    /**
     * 获取文章阅读量和点赞量的程序
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMsgExt", method = RequestMethod.POST)
    public Object getMsgExt(String str, String url) throws UnsupportedEncodingException {
        //先针对url参数进行操作
        MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(URLDecoder.decode(url, "UTF-8")).build().getQueryParams();
        //得到公众号的biz
        String biz = parameters.get("__biz").get(0);
        String sn = parameters.get("sn").get(0);
        //再解析str变量
        //进行json_decode
        str = htmlSpecialCharsDecode(str);
        JSONObject json = (JSONObject) JSONArray.parse(str);

        //$sql = "select * from `文章表` where `biz`='".$biz."' and `content_url` like '%".$sn."%'" limit 0,1;
        //根据biz和sn找到对应的文章
        Map<String, Object> condition = new HashMap<>(16);
        condition.put("biz", new SearchField("biz", "=", biz));
        condition.put("content_url", new SearchField("content_url", "like", "%" + sn + "%"));
        List<Post> posts = iPostService.queryPage(condition, null, 0, 1);

        //阅读量
        int read_num = (int) getJsonProperty(json, "appmsgstat", "read_num");
        //点赞量
        int like_num = (int) getJsonProperty(json, "appmsgstat", "like_num");
        //在这里同样根据sn在采集队列表中删除对应的文章，代表这篇文章可以移出采集队列了
        //$sql = "delete from `队列表` where `content_url` like '%".$sn."%'"
        condition.clear();
        condition.put("content_url", new SearchField("content_url", "like", "%" + sn + "%"));
        iTmplistService.deleteByCondition(condition);

        //然后将阅读量和点赞量更新到文章表中。
        Post post = posts.get(0);
        post.setReadNum(read_num);
        post.setLikeNum(like_num);
        iPostService.update(post);

        Map<String, Object> result = new HashMap<>(16);
        result.put("msg", "success");
        return result;
    }


    /**
     * 当前页面为公众号历史消息时，读取这个程序
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getWxHis", method = RequestMethod.GET)
    public void getWxHis(Integer flag, Integer isGetHistory, HttpServletResponse response) throws InterruptedException, IOException {
        //在采集队列表中有一个loading字段，当值等于1时代表正在被读取
        //首先删除采集队列表中loading=1的行
        Map<String, Object> condition = new HashMap<>(16);
//        condition.put("loading", new SearchField("loading", "=", new Integer(1)));
//        iTmplistService.deleteByCondition(condition);

        //然后从队列表中任意select一行
        condition.put("loading", new SearchField("loading", "=", new Integer(0)));
        Tmplist tmplist = iTmplistService.queryOne(condition);
        //队列表为空
        String url = "";
        if(tmplist == null){
            //判断是否爬取最新的公众号
            if (isGetHistory == 0){
                //队列表如果空了，就从存储公众号biz的表中取得一个biz，这里我在公众号表中设置了一个采集时间的time字段，按照正序排列之后，就得到时间戳最小的一个公众号记录，并取得它的biz
                Weixin weixin = null;
                while (weixin == null) {
                    Thread.sleep(10 * 1000);
                    Map<String, Object> orderMap = new HashMap<>(16);
                    orderMap.put("collect", "desc");
                    weixin = iWeixinService.queryOne(new HashMap<>(), orderMap);
                }

                //拼接公众号历史消息url地址（第一种页面形式）
                url = "https://mp.weixin.qq.com/mp/getmasssendmsg?__biz=" + weixin.getBiz() + "#wechat_webview_type=1&wechat_redirect";
                if (flag != 1){
                    //拼接公众号历史消息url地址（第二种页面形式）
                    url = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=" + weixin.getBiz() + "&scene=124#wechat_redirect";
                }
                //更新刚才提到的公众号表中的采集时间time字段为当前时间戳。
                weixin.setCollect((int) (System.currentTimeMillis()/1000));
                iWeixinService.update(weixin);
            }else {
                //将下一个将要跳转的$url变成js脚本，由anyproxy注入到微信页面中。
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print("<script>setInterval(function(){window.scrollBy(0,50);},2000);</script>");
                out.close();
            }
        }else{
            //取得当前这一行的content_url字段
            url = tmplist.getContentUrl() + "&uin=" + uin + "&key=" + key + "&pass_ticket=" + pass_ticket;
            //将loading字段update为1
            tmplist.setLoading(1);
            iTmplistService.update(tmplist);
        }

        //将下一个将要跳转的$url变成js脚本，由anyproxy注入到微信页面中。
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<script>setTimeout(function(){window.location.href='" + url + "';},2000);</script>");
        out.close();
    }


    /**
     * 当前页面为公众号文章页面时，读取这个程序
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getWxPost", method = RequestMethod.GET)
    public void getWxPost(int flag, HttpServletResponse response) throws IOException {
        //首先删除采集队列表中loading=1的行
        Map<String, Object> condition = new HashMap<>(16);
//        condition.put("loading", new SearchField("loading", "=", 1));
//        iTmplistService.deleteByCondition(condition);

        //然后从队列表中按照“order by id asc”选择多行(注意这一行和上面的程序不一样)
        Map<String, Object> orderMap = new HashMap<>(16);
        orderMap.put("id", "asc");
        condition.put("loading", new SearchField("loading", "=", 0));
        List<Tmplist> tmplists = iTmplistService.queryList(condition, orderMap);
        //队列表为空
        String url = "";
        if(!tmplists.isEmpty() && tmplists.size() > 1){//(注意这一行和上面的程序不一样)
            //取得第0行的content_url字段
            Tmplist tmplist = tmplists.get(0);
            url = tmplists.get(0).getContentUrl() + "&uin=" + uin + "&key=" + key + "&pass_ticket=" + pass_ticket;
            //将第0行的loading字段update为1
            //将loading字段update为1
            tmplist.setLoading(1);
            iTmplistService.update(tmplist);
        }else{
            //队列表还剩下最后一条时，就从存储公众号biz的表中取得一个biz，这里我在公众号表中设置了一个采集时间的time字段，按照正序排列之后，就得到时间戳最小的一个公众号记录，并取得它的biz
            orderMap.clear();
            orderMap.put("collect", "desc");
            Weixin weixin = iWeixinService.queryOne(new HashMap<>(), orderMap);
            //拼接公众号历史消息url地址（第一种页面形式）
            url = "https://mp.weixin.qq.com/mp/getmasssendmsg?__biz=" + weixin.getBiz() + "#wechat_webview_type=1&wechat_redirect";
            if (flag != 1){
                //拼接公众号历史消息url地址（第二种页面形式）
                url = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=" + weixin.getBiz() + "&scene=124#wechat_redirect";
            }
            //更新刚才提到的公众号表中的采集时间time字段为当前时间戳。
            weixin.setCollect((int) (System.currentTimeMillis()/1000));
            iWeixinService.update(weixin);
        }
        //将下一个将要跳转的$url变成js脚本，由anyproxy注入到微信页面中。
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<script>setTimeout(function(){window.location.href='" + url + "';},2000);</script>");
        out.close();
    }

}
