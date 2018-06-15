'use strict';

function HttpPost(str,url,path) {//将json发送到服务器，str为json内容，url为历史消息页面地址，path是接收程序的路径和文件名
    var http = require('http');
    var data = {
        str: encodeURIComponent(str),
        url: encodeURIComponent(url)
    };
    // console.log(data);
    var content = require('querystring').stringify(data);
    var options = {
        method: "POST",
        host: "localhost",//注意没有http://，这是服务器的域名。
        port: 8887,
        path: path,//接收程序的路径和文件名
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
            "Content-Length": content.length
        },
    };
    var requestDetail = http.request(options, function (res) {
        res.setEncoding('utf8');
        res.on('data', function (chunk) {
            console.log('BODY: success');
        });
    });
    requestDetail.on('error', function (e) {
        console.log('problem with request: ' + e.message);
    });
    requestDetail.write(content);
    requestDetail.end();
}

function HttpGet(path) {//将json发送到服务器，str为json内容，url为历史消息页面地址，path是接收程序的路径和文件名
    var http = require('http');
    var options = {
        method: "GET",
        host: "localhost",//注意没有http://，这是服务器的域名。
        port: 8887,
        path: path,//接收程序的路径和文件名
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        },
    };
    var requestDetail = http.request(options, function (res) {
        res.setEncoding('utf8');
        res.on('data', function (chunk) {
            console.log(chunk);
            return chunk;
        });
    });
    requestDetail.on('error', function (e) {
        console.log('problem with request: ' + e.message);
    });
    requestDetail.end();
}

function sleep(milliseconds) {
    var start = new Date().getTime();
    console.log("start------------" + start);
    for (var i = 0; i < 1e7; i++) {
        if ((new Date().getTime() - start) > milliseconds){
            console.log("sleep.....")
            break;
        }
    }
}

module.exports = {

        summary: 'the default rule for AnyProxy',

    /**
     *
     *
     * @param {object} requestDetail
     * @param {string} requestDetail.protocol
     * @param {object} requestDetail.requestOptions
     * @param {object} requestDetail.requestData
     * @param {object} requestDetail.response
     * @param {number} requestDetail.response.statusCode
     * @param {object} requestDetail.response.header
     * @param {buffer} requestDetail.response.body
     * @returns
     */
    *beforeSendRequest(requestDetail) {
        return null;
    },


    /**
     *
     *
     * @param {object} requestDetail
     * @param {object} responseDetail
     */
    *beforeSendResponse(requestDetail, responseDetail) {
        const newResponse = responseDetail.response;
        if(/mp\/getmasssendmsg/i.test(requestDetail.url)){//当链接地址为公众号历史消息页面时(第一种页面形式)
            if(responseDetail.response.body.toString() !== ""){
                try {//防止报错退出程序
                    var reg = /msgList = (.*?);/;//定义历史消息正则匹配规则
                    var ret = reg.exec(responseDetail.response.body.toString());//转换变量为string
                    HttpPost(ret[1],requestDetail.url,"/getMsgJson");//这个函数是后文定义的，将匹配到的历史消息json发送到自己的服务器
                    var http = require('http');
                    console.log("------------get send before");
                    http.get('http://localhost:8887/getWxHis?flag=1&isGetHistory=0', function (res) {
                        console.log("------------get send ing");
                        res.setEncoding('utf8');
                        res.on('data', function (chunk) {
                            console.log("------------get send after");
                            newResponse.body = newResponse.body + chunk;
                        });
                    });
                    return new Promise((resolve, reject) => {
                        setTimeout(() => { // delay the response for 5s
                        console.log("------------before return");
                        resolve({ response: newResponse });
                    }, 50000);
                });
                }catch(e){//如果上面的正则没有匹配到，那么这个页面内容可能是公众号历史消息页面向下翻动的第二页，因为历史消息第一页是html格式的，第二页就是json格式的。
                    try {
                        var json = JSON.parse(responseDetail.response.body.toString());
                        if (json.general_msg_list != []) {
                            HttpPost(json.general_msg_list,requestDetail.url,"getMsgJson.php");//这个函数和上面的一样是后文定义的，将第二页历史消息的json发送到自己的服务器
                        }
                    }catch(e){
                        console.log(e);//错误捕
                        return null;
                    }
                }
            }
        }else if(/mp\/profile_ext\?action=home/i.test(requestDetail.url)){//当链接地址为公众号历史消息页面时(第二种页面形式)
            try {
                var reg = /var msgList = \'(.*?)\';/;//定义历史消息正则匹配规则（和第一种页面形式的正则不同）
                var ret = reg.exec(responseDetail.response.body.toString());//转换变量为string
                console.log(ret[1]);
                HttpPost(ret[1],requestDetail.url,"/getMsgJson");//这个函数是后文定义的，将匹配到的历史消息json发送到自己的服务器
                console.log('BODY: ' + requestDetail.url);
                var http = require('http');
                var flag = true;
                console.log("------------get send before");
                http.get('http://localhost:8887/getWxHis?flag=2&isGetHistory=1', function (res) {
                    console.log("------------get send ing");
                    res.setEncoding('utf8');
                    res.on('data', function (chunk) {
                        console.log("chunk" + chunk);
                        flag = false;
                        console.log("------------get send after");
                        newResponse.body = newResponse.body + chunk;
                    });
                });
                return new Promise((resolve, reject) => {
                    setTimeout(() => { // delay the response for 5s
                    console.log("------------before return");
                    resolve({ response: newResponse });
                }, 10000);
            });
            }catch(e){
                console.log(e);
                return null;
            }
        }else if(/mp\/profile_ext\?action=getmsg/i.test(requestDetail.url)){//第二种页面表现形式的向下翻页后的json
            try {
                var json = JSON.parse(responseDetail.response.body.toString());
                if (json.general_msg_list != []) {
                    HttpPost(json.general_msg_list,requestDetail.url,"/getMsgJson");//这个函数和上面的一样是后文定义的，将第二页历史消息的json发送到自己的服务器
                }
            }catch(e){
                console.log(e);
                return null;
            }
        }else if(/mp\/getappmsgext/i.test(requestDetail.url)){//当链接地址为公众号文章阅读量和点赞量时
            try {
                HttpPost(responseDetail.response.body,requestDetail.requestOptions.headers['Referer'],"/getMsgExt");//函数是后文定义的，功能是将文章阅读量点赞量的json发送到服务器
            }catch(e){
                console.log(e);
                return null;
            }
        }else if(/s\?__biz/i.test(requestDetail.url) || /mp\/rumor/i.test(requestDetail.url)){//当链接地址为公众号文章时（rumor这个地址是公众号文章被辟谣了）
            try {
                var http = require('http');
                var data = undefined;
                http.get('http://localhost:8887/getWxPost?flag=0', function (res) {
                    res.setEncoding('utf8');
                    res.on('data', function (chunk) {
                        newResponse.body = newResponse.body + chunk;
                    });
                });
                responseDetail.response.header['Content-Security-Policy'] = "";
                return new Promise((resolve, reject) => {
                    setTimeout(() => { // delay the response for 5s
                    console.log("------------before return");
                    resolve({ response: newResponse });
                }, 10000);
            });
            }catch(e){
                console.log(e);
                return null;
            }
        }
    },


    /**
     * default to return null
     * the user MUST return a boolean when they do implement the interface in rule
     *
     * @param {any} requestDetail
     * @returns
     */
    *beforeDealHttpsRequest(requestDetail) {
        return null;
    },

    /**
     *
     *
     * @param {any} requestDetail
     * @param {any} error
     * @returns
     */
    *onError(requestDetail, error) {
        return null;
    },


    /**
     *
     *
     * @param {any} requestDetail
     * @param {any} error
     * @returns
     */
    *onConnectError(requestDetail, error) {
        return null;
    },
};

