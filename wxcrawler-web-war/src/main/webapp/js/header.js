// $("#headerApp").attr("ng-app","headerApp").attr("ng-controller","headerCtrl");

var headerApp = angular.module('headerApp', []);
headerApp.controller('headerCtrl', function ($scope, $http) {

    var provinceList, cityList, areaList, schoolList;
    var initialized = false;

    function getProvince(v) {

        initialized = true;
        if (!v)
            $scope.s_city = "";

        $http({
            method: 'get',
            url: "/user/getUserProvince",
            cache: true,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data, status) {
            if (data.rtnCode == "0000000") {
                provinceList = data.bizData || [];

                $scope.provinces = angular.copy(provinceList);
                if ($scope.s_province) {
                    $scope.setCheck($scope.provinces, {id: $scope.s_province});
                }

                // utils.removeSel('s_province');
            } else {
                if (data.msg == undefined) {
                    document.write(data);//会话已过期，请重新登录,
                } else {
                    utils.alert(data.msg, 'error');
                }
            }
        });
    };

    $scope.getCity = function (id, v) {

        if (!v) {
            $scope.s_area = "";
            $scope.s_school = "";

            $scope.areas = [];
            $scope.schools = [];
            schoolList = [];
        }
        $http({
            method: 'get',
            url: "/user/getCity/" + id,
            cache: true,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data, status) {
            if (data.rtnCode == "0000000") {
                cityList = data.bizData;
                $scope.citys = angular.copy(cityList);
                if ($scope.s_city) {
                    $scope.setCheck($scope.citys, {id: $scope.s_city});
                }
            } else {
                if (data.msg == undefined) {
                    document.write(data);//会话已过期，请重新登录,
                } else {
                    utils.alert(data.msg, 'error');
                }
            }
        });
    };

    $scope.getArea = function (id, v) {
        // 请空选择
        if (!v) {
            $scope.s_school = "";
            $scope.s_area = "";
            schoolList = [];
            $scope.schools = [];
        }
        $http({
            method: 'get',
            url: "/user/getArea/" + id,
            cache: true,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data, status) {
            if (data.rtnCode == "0000000") {
                areaList = data.bizData;
                $scope.areas = angular.copy(areaList);
                if ($scope.s_area) {
                    $scope.setCheck($scope.areas, {id: $scope.s_area});
                }
            } else {
                if (data.msg == undefined) {
                    document.write(data);//会话已过期，请重新登录,
                } else {
                    utils.alert(data.msg, 'error');
                }
            }
        });
    };

    // 获取学校
    $scope.getSchoolList = function (areaId) {

        $http({
            method: 'get',
            url: "/user/getSchool/" + areaId,
            cache: true,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data) {
            if (data.rtnCode == "0000000") {
                schoolList = data.bizData;
                $scope.schools = angular.copy(schoolList);
                if ($scope.s_school) {
                    $scope.setCheck($scope.schools, {id: $scope.s_school});
                }
            } else {
                if (data.msg == undefined) {
                    document.write(data);//会话已过期，请重新登录,
                } else {
                    utils.alert(data.msg, 'error');
                }
            }
        });
    };


    $scope.shwoArea = function () {

        if (!initialized) {
            getProvince(true);
            if ($scope.s_province) {
                $scope.getCity($scope.s_province, !0);
            }
            if ($scope.s_city) {
                $scope.getArea($scope.s_city, !0);
            }
            if ($scope.s_area) {
                $scope.getSchoolList($scope.s_area, !0);
            }
        }
    };

    // 设置选中
    $scope.setCheck = function (list, item) {

        for (var i = 0; i < list.length; i++) {
            var obj = list[i];
            if (obj.id == item.id) {
                obj._checked = !item._checked;
            } else {
                obj._checked = false;
            }
        }
    };

    // 设置选中
    $scope.setCityCheck = function (list, item, selArea) {

        for (var i = 0; i < list.length; i++) {
            var obj = list[i];
            if (obj.id == item.id) {
                if (item._checked)
                    return;
                obj._checked = !item._checked;

                if (selArea)
                    $scope.getArea(item.id);

            }
            else {
                obj._checked = false;
            }
        }
    };


    // 搜索list
    function searchList(list, name) {
        list = angular.copy(list);
        if (name == null || name.length == 0) {
            return list;
        }
        var newList = [];
        for (var i = 0; i < list.length; i++) {
            var obj = list[i];
            if (obj.name.indexOf(name) > -1) {
                newList.push(obj);
            }
        }
        return newList;
    }

    function getSelect(list, id) {
        if (list && list.length > 0) {
            for (var i = 0; i < list.length; i++) {
                var obj = list[i];
                if (id) {
                    if (obj.id == id) {
                        return obj;
                    }
                } else if (obj._checked) {
                    return obj;
                }
            }
        }
        return {};
    }

    $scope.searchCity = function () {
        $scope.citys = searchList(cityList, $scope.c_name);
    };
    $scope.searchArea = function () {
        $scope.areas = searchList(areaList, $scope.a_name);
    };
    $scope.searchSchool = function () {
        $scope.schools = searchList(schoolList, $scope.s_name);
    };
    $scope.setArea = function () {
        var cityItem = getSelect($scope.citys);

        if (cityItem.id == undefined) {
            utils.alert('必须选择一个地市', 'warning');
            return;
        }
        $http({
            method: 'post',
            url: "/user/setArea",
            data: {
                province: getSelect($scope.provinces, $scope.s_province),
                city: cityItem,
                area: getSelect($scope.areas),
                school: getSelect($scope.schools)
            },
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data) {
            if (data.rtnCode == "0000000") {
                $('#areaModal').modal('hide');
                $("#g_cityName").html(cityItem.name);

                if (window.location.href.indexOf("/exam/paper/create") == -1) {
                    //假如url含有参数，跳转前，清除相关区域参数
                    if (window.location.href.indexOf('?') > -1) {
                        var reloadUrl = window.location.href;
                        reloadUrl = utils.removeURLParam("areaId", reloadUrl);
                        reloadUrl = utils.removeURLParam("cityId", reloadUrl);
                        reloadUrl = utils.removeURLParam("schoolId", reloadUrl);
                        window.location.href = reloadUrl;
                    } else {
                        window.location.reload();
                    }
                }
            } else {
                if (data.msg == undefined) {
                    document.write(data);//会话已过期，请重新登录,
                } else {
                    utils.alert(data.msg, 'error');
                }
            }
        });
    };
});
angular.bootstrap(angular.element("#header"), ['headerApp']);