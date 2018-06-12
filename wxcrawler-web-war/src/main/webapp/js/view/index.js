'use strict';

var modelUrl = "/exercise/new";


/* App Module */
var modelApp = angular.module('modelApp', ['base64']);

modelApp.directive("scroll", function ($window) {
    return {
        link: function(scope) {
            angular.element($window).bind("wheel", function() {
                var data = [
                    {
                        nickName: 'xx公众号',
                        datetime: '2分钟前',
                        avatar: 'https://img.alicdn.com/tfs/TB1L6tBXQyWBuNjy0FpXXassXXa-80-80.png',
                        message: '刚刚完成了智能化搭建课程的学习',
                    }
                ]
                if (scope.weixinList.length % 15 != 0) {
                    scope.weixinList = scope.weixinList.concat(data);
                }
                scope.$apply();
            });
        }
    }
})

modelApp.controller('modelCtrl', ['$scope', '$rootScope', '$http', '$compile', '$window', '$base64', function ($scope, $rootScope, $http, $compile, $window, $base64) {

    var data = [
        {
            nickName: 'xx公众号',
            datetime: '2分钟前',
            avatar: 'https://img.alicdn.com/tfs/TB1L6tBXQyWBuNjy0FpXXassXXa-80-80.png',
            message: '刚刚完成了智能化搭建课程的学习',
        },
        {
            nickName: 'xx公众号',
            datetime: '3分钟前',
            avatar: 'https://img.alicdn.com/tfs/TB1L6tBXQyWBuNjy0FpXXassXXa-80-80.png',
            message: '刚刚完成了 JavaScript 模块化打包课程的学习',
        },
        {
            nickName: 'xx公众号',
            datetime: '5分钟前',
            avatar: 'https://img.alicdn.com/tfs/TB1L6tBXQyWBuNjy0FpXXassXXa-80-80.png',
            message: '刚刚完成了智能化搭建课程的学习',
        },
        {
            nickName: 'xx公众号',
            datetime: '1天前',
            avatar: 'https://img.alicdn.com/tfs/TB1L6tBXQyWBuNjy0FpXXassXXa-80-80.png',
            message: '刚刚完成了智能化搭建课程的学习',
        },
        {
            nickName: 'xx公众号',
            datetime: '2天前',
            avatar: 'https://img.alicdn.com/tfs/TB1L6tBXQyWBuNjy0FpXXassXXa-80-80.png',
            message: '刚刚完成了Sketch图形设计课程的学习，课程内容包括组件绘制，画布编辑等',
        },
    ];

    function getData(offset, size){
        $scope.weixinList.concat(data);
        var sum = 10;

        var result = '';

        /****业务逻辑块：实现拼接html内容并append到页面*********/

        //console.log(offset , size, sum);

        /*如果剩下的记录数不够分页，就让分页数取剩下的记录数
         * 例如分页数是5，只剩2条，则只取2条
         *
         * 实际MySQL查询时不写这个不会有问题
         */
        if(sum - offset < size ){
            size = sum - offset;
        }

        /*******************************************/

        /*隐藏more按钮*/
        if ( (offset + size) >= sum){
            $(".js-load-more").hide();
        }else{
            $(".js-load-more").show();
        }
    }

    $(function scrollMore() {

        /*初始化*/
        var counter = 0; /*计数器*/
        var pageStart = 0; /*offset*/
        var pageSize = 7; /*size*/
        var isEnd = false;/*结束标志*/

        /*首次加载*/
        getData(pageStart, pageSize);

        /*监听加载更多*/
        $(window).scroll(function(){
            if(isEnd == true){
                return;
            }

            // 当滚动到最底部以上100像素时， 加载新内容
            // 核心代码
            if ($(document).height() - $(this).scrollTop() - $(this).height()<100){
                counter ++;
                pageStart = counter * pageSize;

                getData(pageStart, pageSize);
            }
        });
    });

    function init() {

        $scope.weixinList = data;
    }


    init();
}]);

