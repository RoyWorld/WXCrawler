'use strict';

var modelUrl = "/index";


/* App Module */
var modelApp = angular.module('modelApp',[]);

modelApp.directive("scroll", function ($window) {
    return {
        link: function(scope) {
            angular.element($window).bind("wheel", function() {
                // var data = [
                //     {
                //         nickName: 'xx公众号',
                //         datetime: '2分钟前',
                //         avatar: 'https://img.alicdn.com/tfs/TB1L6tBXQyWBuNjy0FpXXassXXa-80-80.png',
                //         message: '刚刚完成了智能化搭建课程的学习',
                //     }
                // ]
                // if (scope.weixinList.length % 15 != 0) {
                //     scope.weixinList = scope.weixinList.concat(data);
                // }
                scope.$apply();
            });
        }
    }
})

modelApp.controller('modelCtrl', ['$scope', '$rootScope', '$http', '$compile', '$window', function ($scope, $rootScope, $http, $compile, $window, $base64) {

    $scope.page = 1;
    $scope.weixinList = [];
    $scope.postFilePath = $('#postFilePath').val();

    // $scope.openWeixin = function () {
    //     window.location.href = "/index/openWeixin";
    // }

    function init() {
        $http({
            method: 'get',
            url: modelUrl + "/getWXList?page=" + $scope.page,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data, status) {
            $scope.weixinList = $scope.weixinList.concat(data.bizData);
            $scope.page++;
        });
    }


    init();
}]);

