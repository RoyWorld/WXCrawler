// $("#headerApp").attr("ng-app","headerApp").attr("ng-controller","headerCtrl");

var headerApp = angular.module('headerApp', []);

// $("#headerApp").attr("ng-controller","headerCtrl");
headerApp.controller('headerCtrl', function ($scope, $http) {

    // 评卷状态
    var strs = "运营,扫描组长,教师,学生".split(',');
   $scope.convertUserType = function (val) {
       if (val < 1 || val > strs.length) {
           return "未知"
       }
       return strs[val - 1];
   };

    function getMenu() {
        $http({
            method: 'get',
            url: "/user/getMenu",
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data, status) {
            if (data.rtnCode == "0000000") {
                if (data && data.bizData) {
                    initMenu(data.bizData);
                }
            } else {
                utils.alert(data.msg, 'error');
            }
        });
    };
    function getUserData() {
        $http({
            method: 'get',
            url: "/user/getUserData",
            headers: {
                contentType: 'application/json;charset=UTF-8'
            }
        }).success(function (data, status) {
            if (data.rtnCode == "0000000") {
                $scope.userInfo = data.bizData;
                $(".user-info").removeClass("hidden");
            } else {
                utils.alert(data.msg, 'error');
            }
        });
    };

    function initMenu(menus) {
        var dt = '<dt><span class="glyphicon {class}"></span>{name}</dt>';
        var dt2 = '<dt><a href="{url}">{name}</a></dt>';

        var classes = ['glyphicon-compressed', 'glyphicon-tasks', 'glyphicon-check',
            'glyphicon-stats', 'glyphicon-comment'];
        var menuUrl = window.location.href;

        var menuList = [];
        menuList.push("<dl>");
        // 输出菜单
        for (var i = 0; i < menus.length; i++) {
            var menuItem = menus[i];
            // 输出菜单ico
            var className = i < classes.length ? classes[i] : classes[classes.length - 1];
            menuList.push(dt.replace("{name}", menuItem.name).replace("{class}", className));
            var sub_menuItems = menuItem.menuItemList;
            // 下级菜单
            if (sub_menuItems && sub_menuItems.length > 0) {
                menuList.push("<dd><dl>");
                for (var j = 0; j < sub_menuItems.length; j++) {
                    var subitem = sub_menuItems[j];
                    if (menuUrl.indexOf(subitem.url) > -1) {
                        menuUrl = subitem.url;
                    }
                    // 输出菜单项
                    menuList.push(dt2.replace("{url}", subitem.url).replace("{name}", subitem.name));
                }
                menuList.push("</dl></dd>")
            }
        }
        menuList.push("</dl>");

        $("#nav").append(menuList.join(''));

        $('a[href="' + menuUrl + '"]').addClass('cur').closest('dd').addClass('active');

        //菜单
        $(".nav dt").click(function () {
            $(this).toggleClass("active").siblings("dt").removeClass("active")
                .next("dd").slideUp("fast").removeClass("active");
            $(this).next("dd").slideToggle("fast").toggleClass("active");
        });

        $(".nav a").click(function () {
            $(".nav a").not(this).removeClass("cur");
            $(this).addClass("cur");
        });

    }

    function init() {
        // getMenu();
        getUserData();
    }

    init();
});

$('a[href="' + window.location.href + '"]').addClass('cur').closest('dd').addClass('active');

//菜单
$(".nav dt").click(function () {
    $(this).toggleClass("active").siblings("dt").removeClass("active")
        .next("dd").slideUp("fast").removeClass("active");
    $(this).next("dd").slideToggle("fast").toggleClass("active");
});

$(".nav a").click(function () {
    $(".nav a").not(this).removeClass("cur");
    $(this).addClass("cur");
}).each(function (i,n) {
    var href = $(n).attr("href");
    if (window.location.href.indexOf(href) > -1) {
        $(this).addClass('cur').closest('dd').addClass('active');
    }
});
angular.bootstrap(angular.element("#headerApp"), ['headerApp']);

// 获取菜单
// jQuery.getJSON("/user/getMenu", null, function (data) {
//     var dt = '<dt><span class="glyphicon {class}"></span>{name}</dt>';
//     var dt2 = '<dt><a href="{url}">{name}</a></dt>';
//
//     var classes = ['glyphicon-compressed', 'glyphicon-tasks', 'glyphicon-check',
//         'glyphicon-stats', 'glyphicon-comment'];
//     var menuUrl = window.location.href;
//
//     if (data && data.bizData) {
//         var menuList = [];
//         menuList.push("<dl>");
//         // 输出菜单
//         for (var i = 0; i < data.bizData.length; i++) {
//             var menuItem = data.bizData[i];
//             // 输出菜单ico
//             var className = i < classes.length ? classes[i] : classes[classes.length - 1];
//             menuList.push(dt.replace("{name}", menuItem.name).replace("{class}", className));
//             var sub_menuItems = menuItem.menuItemList;
//             // 下级菜单
//             if (sub_menuItems && sub_menuItems.length > 0) {
//                 menuList.push("<dd><dl>");
//                 for (var j = 0; j < sub_menuItems.length; j++) {
//                     var subitem = sub_menuItems[j];
//                     if (menuUrl.indexOf(subitem.url) > -1) {
//                         menuUrl = subitem.url;
//                     }
//                     // 输出菜单项
//                     menuList.push(dt2.replace("{url}", subitem.url).replace("{name}", subitem.name));
//                 }
//                 menuList.push("</dl></dd>")
//             }
//         }
//         menuList.push("</dl>");
//
//         $("#nav").append(menuList.join(''));
//
//         $('a[href="' + menuUrl + '"]').addClass('cur').closest('dd').addClass('active');
//
//         //菜单
//         $(".nav dt").click(function () {
//             $(this).toggleClass("active").siblings("dt").removeClass("active")
//                 .next("dd").slideUp("fast").removeClass("active");
//             $(this).next("dd").slideToggle("fast").toggleClass("active");
//         });
//
//         $(".nav a").click(function () {
//             $(".nav a").not(this).removeClass("cur");
//             $(this).addClass("cur");
//         });
//     }
// });