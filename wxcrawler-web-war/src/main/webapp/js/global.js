/*
 * show 把指定的target变为等待中状态，
 * hide 取消等待
 * 可以支持多个对象同时使用，使用方法：
 *      var tinyLoading1=new tinyLoading( $(event.target) );  		
 * 		var tinyLoading2=new tinyLoading( $("#btnCount2") );		
 * 		tinyLoading1.show();
 * 		tinyLoading2.show();
 *      //异步...
 *      tinyLoading1.hide();
 *      tinyLoading2.hide();
 * */
function tinyLoading(target) {
    this.target = target;//this作用域：当前对象
    this.oldInnerHtml = target.html();
    this.show = function () {
        this.target.attr("disabled", true);
        this.target.html('<div class="tiny-loading"><img class="img-load" alt="Loading..." src="/static/assets/images/tiny-loading.gif"/></div>');
    };
    this.hide = function () {
        this.target.attr("disabled", false).html(this.oldInnerHtml);
    }
}

//特殊的弹框提示
function alertSpec(content) {
    alert(content);
}

(function ($) {

//	这个方法同时只能一个实例在运行
//window.tinyLoading = {
//	'oldInnerHtml':'',
//	'$eSourceObj':'',
//    show: function( target ) {
//    	
//    	this.$eSourceObj=target.attr("disabled",true);//保存并隐藏事件的触发对象（按钮)	     
//    	this.oldInnerHtml=target.html();
//    	this.$eSourceObj.html('<div class="tiny-loading"><img class="img-load" alt="Loading..." src="/static/assets/images/tiny-loading.gif"/></div>');
//
//    },
//    hide: function(  ) {alert(this.$eSourceObj)
//    	this.$eSourceObj.attr("disabled",false).html(this.oldInnerHtml);
//    }
//};

    window.hugeLoading = {
        show: function (msg) {
            var el = $(".huge-loading"),
                msg = msg || "正在加载",
                header = $(".header"),
                top = Math.max(header.offset().top - $(window).scrollTop(), 0);

            if (el.length) {
                el.text(msg);
            } else {
                el = $('<div class="huge-loading"></div>')
                    .text(msg)
                    .appendTo("body");
            }

            el.css({
                marginLeft: 0 - el.outerWidth() / 2,
                top: top
            }).show();
        },
        hide: function () {
            var el = $(".huge-loading");
            if (el.length) {
                el.hide();
            }
        }
    };

    window.resultMsg = function (options) {
        var opts = $.extend({
                msg: "保存成功",
                type: "success",
                delay: 5000,
                css: {}
            }, options),
            $msg = opts.target.next("span.result-msg");

        if (!opts.target) return false
        if ($msg.length) $msg.remove();

        $("<span/>", {
            text: opts.msg,
            "class": "result-msg",
            css: opts.css
        }).addClass(opts.type)
            .insertAfter(opts.target)
            .delay(opts.delay)
            .fadeOut();
    };

    window.labelColors = [
        "#aaaaaa",
        "#ed9a51",
        "#e3886f",
        "#d96666",
        "#a47768",
        "#a6887d",
        "#e1c44f",
        "#99b869",
        "#90a342",
        "#5e966f",
        "#62b298",
        "#75b7cd",
        "#72abd7",
        "#808fda",
        "#6383cc",
        "#b48fd2",
        "#9073b8",
        "#ce78c0",
        "#d98aaa",
        "#777777",
        "#444444"
    ];

    window.RGBToHex = function (rgb) {
        var regexp = /^rgb\(([0-9]{0,3})\,\s([0-9]{0,3})\,\s([0-9]{0,3})\)/g;
        var re = rgb.replace(regexp, "$1 $2 $3").split(" ");//利用正则表达式去掉多余的部分

        if (re.length == 1) {
            return rgb
        }

        var hexColor = "#";
        var hex = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'];

        for (var i = 0; i < 3; i++) {
            var r = null;
            var c = re[i];
            var hexAr = [];

            while (c > 16) {
                r = c % 16;
                c = (c / 16) >> 0;
                hexAr.push(hex[r]);
            }

            hexAr.push(hex[c]);
            if (hexAr.length == 1) hexAr.push(0);
            hexColor += hexAr.reverse().join('');
        }

        return hexColor;
    }


})(jQuery);
