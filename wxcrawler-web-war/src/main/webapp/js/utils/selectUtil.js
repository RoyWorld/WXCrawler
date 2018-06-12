/**
 * Created by CXinZhi on 8/22/16.
 *
 *
 * 常用辅助工具类
 */

var modelUrl = "/select/api/";

(function (window) {

    if (window.selectUtils) {
        return;
    }

    var my = {};

    /**
     *
     * 获得区域下拉列表
     *
     * @param $scope    : 请求下拉框所在的控制域
     * @param $http     : 请求下拉框所在的控制域的 xmlHtmlRequest
     * @param cityId    : 请求的城市Id
     * @param areaId    : 默认区县Id
     */
    my.getAreaList = function ($scope, $http, cityId, areaId) {

        $http({
            method: 'get',
            url: modelUrl + 'getAreaList/' + cityId,
            async: false,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data) {
            if (data.rtnCode == "0000000") {

                $scope.areaList = data.bizData;

                if (areaId)
                    $scope.areaId = areaId;
                else
                    $scope.s_areaId = "";
            }
        });
    };

    /**
     *
     * 获取学校下拉列表
     *
     * @param $scope    : 请求下拉框所在的控制域
     * @param $http     : 请求下拉框所在的控制域的 xmlHtmlRequest
     * @param areaId    : 请求的区县Id
     * @param schoolId  : 默认学校Id
     */
    my.getSchoolList = function ($scope, $http, areaId, schoolId) {

        $http({
            method: 'get',
            url: modelUrl + 'getSchoolList/' + areaId,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data) {
            if (data.rtnCode == "0000000") {
                $scope.schoolList = data.bizData;

                if (schoolId)
                    $scope.s_schoolId
                else
                    $scope.s_schoolId = "";
            }
        });
    };

    /**
     *
     * 获取年级下拉列表
     *
     * @param $scope    : 请求下拉框所在的控制域
     * @param $http     : 请求下拉框所在的控制域的 xmlHtmlRequest
     * @param areaId    : 请求的学校Id
     * @param schoolId  : 默认班级Id
     */
    my.getGradeList = function ($scope, $http, schoolId, gradeId) {

        $http({
            method: 'get',
            url: modelUrl + 'getGradeList/' + schoolId,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data) {
            if (data.rtnCode == "0000000") {
                $scope.gradeList = data.bizData;

                if (gradeId)
                    $scope.s_gradeId = gradeId;
                else
                    $scope.s_gradeId = "";
            }
        });
    };

    /**
     *
     * 获取班级下拉列表
     *
     * @param $scope    : 请求下拉框所在的控制域
     * @param $http     : 请求下拉框所在的控制域的 xmlHtmlRequest
     * @param schoolId  : 请求的学校Id
     * @param gradeId   : 请求的年级Id
     * @param gradeId   : 默认的年级Id
     */
    my.getClassList = function ($scope, $http, schoolId, gradeId, classId) {
        $http({
            method: 'get',
            url: modelUrl + 'getClassList/' + schoolId + "/" + gradeId,
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data) {
            if (data.rtnCode == "0000000") {
                $scope.classList = data.bizData;

                if (classId)
                    $scope.s_classId = classId;
                else
                    $scope.s_classId = "";
            }
        });
    };


    window.selectUtils = my;

})(window);