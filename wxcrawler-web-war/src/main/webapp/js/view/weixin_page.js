'use strict';

var modelUrl = "/index";


/* App Module */
var modelApp = angular.module('modelApp',[]);

modelApp.controller('modelCtrl', ['$scope', '$rootScope', '$http', '$compile', '$window', function ($scope, $rootScope, $http, $compile, $window) {

    $scope.page = 1;
    $scope.weixinList = [];
    $scope.postFilePath = $('#postFilePath').val();

    $scope.openWeixin = function () {
        window.location.href = "/index/openWeixin";
    }

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

