'use strict';

var modelUrl = "/index";


/* App Module */
var modelApp = angular.module('modelApp',[]);

modelApp.directive("scroll", function ($window, $http) {
    return {
        link: function(scope) {
            angular.element($window).bind("wheel", function() {
                if (scope.page % 4 != 0 && scope.page < scope.total && scope.canScroll) {
                    scope.page++;
                    $http({
                        method: 'get',
                        url: modelUrl + "/getWXList?page=" + scope.page,
                        headers: {
                            contentType: 'application/json;charset=UTF-8'
                        }
                    }).success(function (data, status) {
                        if (data.bizData.rows.length != []){
                            scope.weixinList = scope.weixinList.concat(data.bizData.rows);
                        }else {
                            scope.canScroll = false;
                        }
                    });
                }
            });
        }
    }
})

modelApp.controller('modelCtrl', ['$scope', '$rootScope', '$http', '$compile', '$window', function ($scope, $rootScope, $http, $compile, $window, $base64) {

    $scope.page = 1;
    $scope.weixinList = [];
    $scope.postFilePath = $('#postFilePath').val();
    $scope.canScroll = true;

    $scope.prePage = function () {
        $scope.page--;
        $scope.getWeixinList($scope.page);
    }

    $scope.nextPage = function () {
        $scope.page++;
        $scope.getWeixinList($scope.page);
    }

    $scope.isSelected = function (page) {
        return $scope.page == page ? true : false;
    }

    $scope.getWeixinList = function (page){
        $scope.page = page;
        $scope.canScroll = true;
        $http({
            method: 'get',
            url: modelUrl + "/getWXList?biz=" + $scope.biz + "&page=" + page,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data, status) {
            $scope.weixinList = data.bizData.rows;
        });
    }

    function init() {
        $http({
            method: 'get',
            url: modelUrl + "/getWXList?page=" + $scope.page,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data, status) {
            $scope.weixinList = data.bizData.rows;
            $scope.total = data.bizData.total;
        });
    }


    init();
}]);

