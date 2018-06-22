'use strict';

var modelUrl = "/index/weixin";


/* App Module */
var modelApp = angular.module('modelApp',[]);

modelApp.controller('modelCtrl', ['$scope', '$rootScope', '$http', '$compile', '$window', function ($scope, $rootScope, $http, $compile, $window) {

    $scope.page = 1;

    $scope.biz = $('#biz').val();
    $scope.avatar = $('#avatar').val();
    $scope.weixiName = $('#name').val();

    $scope.postList = [];


    function init() {
        $http({
            method: 'get',
            url: modelUrl + "/getPostList?biz=" + $scope.biz + "&page=" + $scope.page,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data, status) {
            $scope.postList = $scope.postList.concat(data.bizData.rows);
            $scope.page++;
        });
    }


    // init();
}]);

