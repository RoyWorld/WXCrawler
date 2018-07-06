# Project Description
这个项目是一个公众号文章爬取项目，目的是提供一个本地的公众号文章收藏和浏览工具，可以将线上的文章本地化，有利于快速查找和浏览，同时也可以防止好文章被删除，往后无法查看的问题。

而项目的爬取方式是基于一个[知乎专栏][R2]的内容进行改写，该专栏使用的是`python`和`django`做后台服务，而这个项目是改用`Java web`的方式去做后台服务，而具体的实现方式和处理逻辑可以参照专栏，这里只给出这个项目的实现方式。

# About Project
### project structure
由于要做一个可供浏览阅读的界面，而浏览界面需要兼容多个系统，所以这个项目做成了BS架构，服务的技术架构如下：
* 后台框架：Spring+SpringMVC+Mybatis
* 数据库：MongoDB+Mysql
* 前端页面：bootstrap+velocity+angularjs
* 数据转发：anyproxy

### DataBase
项目里运用了两种不同的数据库，一个是关系型数据库Mysql，另一个是非关系型数据库MongoDB，用途如下：
* Mysql
Mysql的作用是保存公众号和文章信息，项目中的表结构如下：
```sql
CREATE TABLE `tb_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `error` text NOT NULL COMMENT '错误信息',
  `str` text COMMENT '参数str',
  `url` text COMMENT '参数url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

CREATE TABLE `tb_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `biz` varchar(255) NOT NULL COMMENT '文章对应的公众号biz',
  `field_id` int(11) NOT NULL COMMENT '微信定义的一个id，每条文章唯一',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '文章标题',
  `title_encode` text NOT NULL COMMENT '文章标题编码，防止文章标题出现emoji',
  `digest` varchar(500) NOT NULL DEFAULT '' COMMENT '文章摘要',
  `digest_encode` text NOT NULL COMMENT '文章摘要编码，防止文章摘要出现emoji',
  `content_url` varchar(500) NOT NULL COMMENT '文章地址',
  `source_url` varchar(500) NOT NULL COMMENT '阅读原文地址',
  `cover` varchar(500) NOT NULL COMMENT '封面图片',
  `is_multi` int(11) NOT NULL COMMENT '是否多图文',
  `is_top` int(11) NOT NULL COMMENT '是否头条',
  `datetime` int(11) NOT NULL COMMENT '文章时间戳',
  `readNum` int(11) NOT NULL DEFAULT '1' COMMENT '文章阅读量',
  `likeNum` int(11) NOT NULL DEFAULT '0' COMMENT '文章点赞量',
  `is_exsist` int(11) NOT NULL DEFAULT '0' COMMENT '是否已爬取，0：未爬取，1：已爬取，2：文章不存在',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 COMMENT='公众号文章表|CreateBaseDomain\r\n公众号文章表';

CREATE TABLE `tb_tmplist` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content_url` varchar(255) DEFAULT NULL COMMENT '文章地址',
  `loading` int(11) DEFAULT '0' COMMENT '读取中标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `content_url` (`content_url`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 COMMENT='采集队列表|CreateBaseDomain\r\n采集队列表';

CREATE TABLE `tb_weixin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `biz` varchar(255) DEFAULT '' COMMENT '公众号唯一标识biz',
  `nickName` varchar(255) DEFAULT '' COMMENT '公众号昵称',
  `avatar` varchar(500) DEFAULT '' COMMENT '公众号头像',
  `collect` int(11) DEFAULT '1' COMMENT '记录采集时间的时间戳',
  `status` int(2) DEFAULT '1' COMMENT '状态，0：软删除，1：启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='公众号表|CreateBaseDomain\r\n公众号表';
```

* MongoDB
MongoDB的作用是保存文章的标题和内容，方便做后台文章搜索，表结构如下：
```sql
{
    id: string,
    content: string
}
```

### Anyproxy
Anyproxy是一个抓包工具，用途是抓取公众号和文章的相关数据，并将数据转发到后台中。这实质上是利用了中间人攻击的原理，将微信客户端浏览公众号的页面信息转发到后台后，再返回到微信客户端中。

![Project Structure](https://raw.githubusercontent.com/RoyWorld/WXCrawler/master/wxcrawler-web-war/src/main/resources/images/proxy.png)

Anyproxy的相关信息和技术指南可查看这个链接：http://anyproxy.io

Anyproxy的转发功能是自行修改文件`rule_default.js`实现的，具体代码参考这个[文件][R1]

### how to use
本机web访问地址：**http://localhost:8887/index**
* 公众号列表查看

![pic1](https://raw.githubusercontent.com/RoyWorld/WXCrawler/master/wxcrawler-web-war/src/main/resources/images/pic1.png)

* 文章列表查看

![pic2](https://raw.githubusercontent.com/RoyWorld/WXCrawler/master/wxcrawler-web-war/src/main/resources/images/pic2.png)

* 文章查看

![pic3](https://raw.githubusercontent.com/RoyWorld/WXCrawler/master/wxcrawler-web-war/src/main/resources/images/pic3.png)

# technic-problem


# project version
## v1.0.1
目前已实现的功能
* 爬取历史公众号历史文章
* 爬取公众号最新文章
* 本地文章查看
* 按文章标题和内容搜索文章

待优化和增加的功能
* 优化历史文章爬取速度
* 优化更新新文章的流程
* 优化搜素结果
* 实现爬取可配置

[R1]: https://raw.githubusercontent.com/RoyWorld/WXCrawler/master/wxcrawler-web-war/src/main/webapp/js/rule_default.js
[R2]: https://zhuanlan.zhihu.com/c_65943221