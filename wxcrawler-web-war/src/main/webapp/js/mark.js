//给分板效果脚本
function Drag() {
    //初始化
    this.initialize.apply(this, arguments)
}

Drag.prototype = {
    //初始化
    initialize: function (drag, options) {
        this.drag = this.$(drag);
        this._x = this._y = 0;
        this._moveDrag = this.bind(this, this.moveDrag);
        this._stopDrag = this.bind(this, this.stopDrag);

        this.setOptions(options);

        this.handle = this.$(this.options.handle);
        this.maxContainer = this.$(this.options.maxContainer);


        this.limit = this.options.limit;
        this.lockX = this.options.lockX;
        this.lockY = this.options.lockY;
        this.lock = this.options.lock;

        this.onStart = this.options.onStart;
        this.onMove = this.options.onMove;
        this.onStop = this.options.onStop;

        this.handle.style.cursor = "move";

        this.changeLayout();

        this.addHandler(this.handle, "mousedown", this.bind(this, this.startDrag))
    },
    changeLayout: function () {
        this.drag.style.top = this.drag.offsetTop + "px";
        this.drag.style.left = this.drag.offsetLeft + "px";
        this.drag.style.position = "absolute";
        this.drag.style.margin = "0"
    },
    startDrag: function (event) {
        var event = event || window.event;

        this._x = event.clientX - this.drag.offsetLeft;
        this._y = event.clientY - this.drag.offsetTop;

        this.addHandler(document, "mousemove", this._moveDrag);
        this.addHandler(document, "mouseup", this._stopDrag);

        event.preventDefault && event.preventDefault();
        this.handle.setCapture && this.handle.setCapture();

        this.onStart()
    },
    moveDrag: function (event) {
        var event = event || window.event;

        var iTop = event.clientY - this._y;
        var iLeft = event.clientX - this._x;

        if (this.lock) return;
        this.maxTop = Math.max(this.maxContainer.clientHeight, this.maxContainer.scrollHeight) - this.drag.offsetHeight + 2;
        this.maxLeft = Math.max(this.maxContainer.clientWidth, this.maxContainer.scrollWidth) - this.drag.offsetWidth + 2;

        this.limit && (
            iTop < 0 && (iTop = 0),
            iLeft < 0 && (iLeft = 0),
            iTop > this.maxTop && (iTop = this.maxTop),
            iLeft > this.maxLeft && (iLeft = this.maxLeft));

        this.lockY || (this.drag.style.top = iTop + "px");
        this.lockX || (this.drag.style.left = iLeft + "px");

        if (this.maxContainer.clientHeight < this.drag.clientHeight) {
            this.drag.style.top = "0px"
        }
        if (this.maxContainer.clientWidth < this.drag.clientWidth) {
            this.drag.style.left = "0px"
        }

        event.preventDefault && event.preventDefault();

        this.onMove()
    },
    stopDrag: function () {
        this.removeHandler(document, "mousemove", this._moveDrag);
        this.removeHandler(document, "mouseup", this._stopDrag);

        this.handle.releaseCapture && this.handle.releaseCapture();

        this.onStop()
    },
    //参数设置
    setOptions: function (options) {
        this.options = {
            handle: this.drag, //事件对象
            limit: true, //锁定范围
            lock: false, //锁定位置
            lockX: false, //锁定水平位置
            lockY: false, //锁定垂直位置
            maxContainer: document.documentElement || document.body, //指定限制容器
            onStart: function () {
            }, //开始时回调函数
            onMove: function () {
            }, //拖拽时回调函数
            onStop: function () {
            }  //停止时回调函数
        };
        for (var p in options) this.options[p] = options[p]
    },
    //获取id
    $: function (id) {
        return typeof id === "string" ? document.getElementById(id) : id
    },
    //添加绑定事件
    addHandler: function (oElement, sEventType, fnHandler) {
        return oElement.addEventListener ? oElement.addEventListener(sEventType, fnHandler, false) : oElement.attachEvent("on" + sEventType, fnHandler)
    },
    //删除绑定事件
    removeHandler: function (oElement, sEventType, fnHandler) {
        return oElement.removeEventListener ? oElement.removeEventListener(sEventType, fnHandler, false) : oElement.detachEvent("on" + sEventType, fnHandler)
    },
    //绑定事件到对象
    bind: function (object, fnHandler) {
        return function () {
            return fnHandler.apply(object, arguments)
        }
    }
};
var widthSetting = {};
var cname = "userMarkSetting"+$("#paperExamId").val();
function setCookie(cname,cvalue,exdays)
{
    var d = new Date();
    d.setTime(d.getTime()+(exdays*24*60*60*1000));
    var expires = "expires="+d.toGMTString();
    document.cookie = cname + "=" + cvalue + "; " + expires+";path=/";
    return true;
}

function getCookie(cname)
{
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++)
    {
        var c = ca[i].trim();
        if (c.indexOf(name)==0) return c.substring(name.length,c.length);
    }
    return "";
}

function saveUserMarkSetting(name,vale) {
    var userMarkSetting = {};
    var dataStr = getCookie(cname);
    if(dataStr!=""&&dataStr!="undefined"){
        userMarkSetting = $.parseJSON(dataStr);
    }
    userMarkSetting[name] = vale;
    setCookie(cname,JSON.stringify(userMarkSetting),2)
}
function canvasSize() {
    $("#Canvas").attr({width: $("#Canvas").width(), height: $("#Canvas").height()});
};
$(function () {

    //****左侧菜单****
    $(".nav-mark>dl>dd>dl>dt").click(function () {
        $(this).siblings("dt").removeClass("active");
        $(this).siblings("dt").next("dd").slideUp("fast").removeClass("active");
        $(this).addClass("active");
        $(this).next("dd").slideDown("fast").addClass("active");
    });

    $(".nav-mark dt a").click(function () {
        $(".nav-mark dt a").not(this).removeClass("cur");
        $(this).addClass("cur");
    });

    $(".nav-mark-hide").click(function () {
        $(".nav-mark").css("left", "-120px");
        $(".nav-mark-show").show();
        $(".main-mark,#canvasTool").css("padding-left", "0");
        canvasSize();
    });

    $(".nav-mark-show").click(function () {
        $(".nav-mark").css("left", "0");
        $(this).hide();
        $(".main-mark,#canvasTool").css("padding-left", "120px");
        canvasSize();
    });

    //****右侧给分板****
    $(".panel-mark-hide").click(function () {
        $(".panel-mark").css("right", "-200px");
        $(".panel-mark-show").show();
        $(".main-mark,#canvasTool").css("padding-right", "0");
        canvasSize();
        thumSize();
    });

    $(".panel-mark-show").click(function () {
        $(".panel-mark").css("right", "0");
        $(this).hide();
        $(".main-mark,#canvasTool").css("padding-right", "200px");
        canvasSize();
        thumSize();
    });

    //****弹窗****
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    });


    //****保护色****
    $("#showColor").not(".isshow").click(function () {
        $("#setColor").show().attr("tabindex", 0).focus();
        $(this).addClass("isshow");
    });
    $("#setColor").blur(function () {
        $(this).fadeOut(100, function () {
            $("#showColor").removeClass("isshow");
        });
    });

    var start = 15;


    var cdataStr = getCookie(cname);
    var initcolor = "";
    var showCanvasSetting = {};//批注状态
    if(cdataStr!=""&&cdataStr!="undefined"){
        var userMarkSetting = $.parseJSON(cdataStr);
        if(userMarkSetting.colorSetting){
            if(userMarkSetting.colorSetting.start){
                start = userMarkSetting.colorSetting.start;
            }
            if(userMarkSetting.colorSetting.color){
                initcolor = userMarkSetting.colorSetting.color;
            }
        }

        if(userMarkSetting.widthSetting){
            widthSetting = userMarkSetting.widthSetting;
        }

        if(userMarkSetting.showCanvasSetting){
            showCanvasSetting = userMarkSetting.showCanvasSetting;
        }


    }

    $(".noUiSlider1").noUiSlider({
        range: [0, 30],
        start: start,
        handles: 1,
        step: 1,
        slide: function () {
            var values = $(this).val();
            var colorSetting = {};
            colorSetting["start"] = values;
            colorSetting["color"] = $("#colorboard1").find(".cur").find("b").attr("class");
            saveUserMarkSetting("colorSetting",colorSetting)
            initcolorCover(values);
        }
    });


    
    function saveWidthState(type) {//保存放大（type==1），缩小（type==2），原图（type==3），适宽（type==4），适高（type==5）的状态,
        widthSetting["type"] = type;
        widthSetting["width"] = $(".paper-size").width();
        saveUserMarkSetting("widthSetting",widthSetting)
    }
    


    $("#colorboard1 li").click(function () {
        $(this).siblings().removeClass("cur");
        $(this).addClass("cur");
        $("#colorCover").css({'background': $(this).children('b').css('background-color')});
        var colorSetting = {};
        colorSetting["start"] = $("#num1").html();
        colorSetting["color"] = $("#colorboard1").find(".cur").find("b").attr("class");
        saveUserMarkSetting("colorSetting",colorSetting)
    });
    if(initcolor!=""){
        $("#colorboard1").find("."+initcolor).parent().click();
    }
    initcolorCover(start);
    function initcolorCover(num) {
        $("#num1").text(num);
        $("#colorCover").css({'opacity': num / 100, 'filter': 'alpha(opacity=' + num + ')'});

    }

    //****给分方式****
    $(".switch").click(function () {
        $(this).toggleClass("type");
        $(".switch b,.input-box").toggle();
    });
    // $(".way-select input").change(function () {
    //     $(".input-box").toggleClass("cur");
    // });
    // $(".score-button label").click(function(){$(this).children("input").addClass("selected");$(this).siblings("label").children("input").removeClass("selected");});

    // 零分
    // $("#zeroMark").click(function () {
    //     $(".score-button label:first-child input").click();
    //     $(".score-text input").val("0");
    //     $(".score-text input.deduct").val("-0");
    // });
    // 满分
    // $("#fullMark").click(function () {
    //     $(".score-button label:last-child input").click();
    //     $(".score-button label:first-child input.deduct").click();
    //     for (var inputIndex = 0; inputIndex < $(".score-text").length; inputIndex++) {
    //         $(".score-text input").eq(inputIndex).val($(".score-button").eq(inputIndex).find("input.selected").val());
    //     }
    // });

    //****缩放、旋转****
    $("#btnBig").click(function () {
        if ($(".paper-size").width() > 2800) {
            utils.alert('当前图像已放至最大', 'warning');
            return false;
        } else {
            $(".paper-size").width($(".paper-size").width() + 200);
            canvasSize();
            saveWidthState(1);
        }
    });
    $("#btnSmall").click(function () {
        if ($(".paper-size").width() < 400) {
            utils.alert('当前图像已放至最小', 'warning');
            return false;
        } else {
            $(".paper-size").width($(".paper-size").width() - 200);
            canvasSize();
            saveWidthState(2);
        }
    });
    $("#btnOrigin").click(function () {
        $("#paper-size").innerWidth("100%");
       //$("#paper-size").css("width","100%");
        $(this).hide();

        $("#btnWidth").show();
        $("#btnHeight").hide();
        $(".paper-img").width("auto");
        var siz = 0;
      var queryClock = window.setInterval(function () {
            $(".paper-img").each(function (i) {
                if ($(this).width()>siz) {
                    siz = $(this).width();
                }

            });
            if (siz>0) {
                $("#paper-size").width(siz);
                $(".paper-img").each(function (i) {
                   var w = $(this).width();
                  $(this).width(((w/siz)*100)+"%");

                });
               // $(".paper-img").width("100%");
                canvasSize();
                saveWidthState(3);
                clearInterval(queryClock);
                return;
            }
        }, 500);
    });
    $("#btnWidth").click(function () {
        $(this).hide();
        $("#btnOrigin").hide();
        $("#btnHeight").show();
        $(".paper-size").innerWidth("100%");
        canvasSize();
        saveWidthState(4);
    });
    $("#btnHeight").click(function () {
        $(this).hide();
        $("#btnOrigin").show();
        var heightArr = new Array();
        var th = 0;
        var h = $(".main-body").height();
        $(".paper-img").each(function (i) {
            th+= $(this).height();
        });
        $(".paper-img").each(function (i) {
            var h1 = $(this).height();
            $(this).width("auto").height((h*(h1/th)) - 10);
        });
       /* $(".paper-img").width("auto").height(($(".main-body").height()/$(".paper-img").length) - 10);*/
        var siz = 0;
        var queryClock = window.setInterval(function () {
            $(".paper-img").each(function (i) {
                if ($(this).width()>siz) {
                    siz = $(this).width();
                }

            });
            if (siz>0) {
                $(".paper-size").width(siz);
                $(".paper-img").each(function (i) {
                    var w = $(this).width();
                    $(this).width(((w/siz)*100)+"%").height("auto");;

                });
              //  $(".paper-img").width("100%")
                canvasSize();
                saveWidthState(5);
                clearInterval(queryClock);
                return;
            }
        }, 500);

    });
    $("#btnPull").click(function () {
        $(this).hide();
        $("#btnOrigin").show();
        $(".paper-size").innerWidth("100%");
        canvasSize();
    });
    var rNum = 0;
    $("#btnRotate").click(function () {
        if (rNum == 0) {
            $(".paper-box,.thum img").css("transform", "rotate(180deg)");
            rNum = 1;
        }
        else {
            $(".paper-box,.thum img").css("transform", "rotate(0deg)");
            rNum = 0;
        }
    });

    //****缩略图****
    function thumSize() {
        if ($(".main-body")[0].clientWidth < $(".paper-size").innerWidth()) {
            $(".thum .win").width($(".main-body")[0].clientWidth * 90 / $(".paper-size").innerWidth() - 4);
        } else {
            $(".thum .win").width(86);
            $(".thum .pic>img").width($(".paper-size").innerWidth() * 90 / $(".main-body")[0].clientWidth - 2);
        }
        $(".thum .win").height($(".main-body")[0].clientHeight * ($(".thum .win").width() + 4) / $(".main-body")[0].clientWidth - 4);
        $(".thum .pic").css("min-height", $(".thum .win").outerHeight());
    }

    $(function () {
        thumSize();
        var oDrag = new Drag($(".thum .win")[0],
            {
                handle: $(".thum>.pic")[0],
                maxContainer: $(".thum>.pic>img")[0]
            });

        //开始拖拽时方法
        oDrag.onMove = function () {
            $(".main-body").scrollTop(this.drag.offsetTop / ( $(".thum .win").outerHeight() / $(".main-body")[0].clientHeight));
            $(".main-body").scrollLeft(this.drag.offsetLeft / ( $(".thum .win").outerWidth() / $(".main-body")[0].clientWidth));
        };

    });
    $(window).resize(function () {
        thumSize();
    });
    $("#btnBig,#btnSmall,#btnPull,#btnOrigin,#btnWidth,#btnHeight,#btnPull").click(function () {
        thumSize();
    });
    $(".main-body").scroll(function () {
        $(".thum .win").css("top", $(".main-body").scrollTop() * $(".thum .win").outerHeight() / $(".main-body")[0].clientHeight + "px");
        $(".thum .win").css("left", $(".main-body").scrollLeft() * $(".thum .win").outerWidth() / $(".main-body")[0].clientWidth + "px");
    });

    //****批注****
    //工具栏
    $("#showCanvas,#canvasExit").click(function () {
       if (!$("#showCanvas").hasClass("isshow")) {
           showCanvasSetting["isshow"] = 1;
        }else {
           showCanvasSetting["isshow"] = 0;
       }
        $("#canvasTool").toggle();
        $("#showCanvas").toggleClass("isshow");
        $(".main-mark").toggleClass("tool-down");
        thumSize();
        saveUserMarkSetting("showCanvasSetting",showCanvasSetting)
    });

    $("#canvasTool button").not(".bt").click(function () {
        $(this).siblings().not(".bt").removeClass("cur");
        $(this).addClass("cur");
    });

    if(showCanvasSetting && showCanvasSetting.isshow && showCanvasSetting.isshow==1){
        $("#showCanvas").click();
    }

    //工具按钮

    $("#goodCanvas,#kindCanvas,#smileCanvas,#cryCanvas").click(function () {
        $(".main-body").scrollTop(0);
        $("#postilBox img.stamp-img").remove();
        if ($(this).hasClass("good")) {
            $("#postilBox").append($("<img />", {src: stampGood, "class": "stamp-img"}))
        } else if ($(this).hasClass("kind")) {
            $("#postilBox").append($("<img />", {src: stampKind, "class": "stamp-img"}))
        } else if ($(this).hasClass("smile")) {
            $("#postilBox").append($("<img />", {src: stampSmile, "class": "stamp-img"}))
        } else if ($(this).hasClass("cry")) {
            $("#postilBox").append($("<img />", {src: stampCry, "class": "stamp-img"}))
        }
    });
    $("#backCanvas").click(function () {
        $("#postilBox").children().not("#new-judge,.save-paint,[isload]").last().remove();
    });

    //canvas 初始化

    canvasSize();

    var $canvas = $("#Canvas");
    var ctx = $canvas[0].getContext("2d");

    //canvas勾叉
    $canvas.mousemove(function (e) {
        $("#new-judge").css({
            "left": e.pageX - $canvas.offset().left + "px",
            "top": e.pageY - $canvas.offset().top - 14 + "px"
        });
    })
        .mouseover(function (e) {
            if ($("#canvasTool").is(":hidden")) {
                $("#Canvas").css("cursor", "default");
            } else if ($(".paint").hasClass("cur")) {
                $("#Canvas").css("cursor", "crosshair");
            } else if ($("#tickCanvas").hasClass("cur")) {
                $("#postilBox").append($("<img />", {src: judgeTick, "class": "judge-img", "id": "new-judge"}));
                $("#Canvas").css("cursor", "crosshair");
            } else if ($("#forkCanvas").hasClass("cur")) {
                $("#postilBox").append($("<img />", {src: judgeFork, "class": "judge-img", "id": "new-judge"}));
                $("#Canvas").css("cursor", "crosshair");
            } else if ($("#textCanvas").hasClass("cur")) {
                $("#Canvas").css("cursor", "text");
            }
        })
        .mouseout(function (e) {
            $("#new-judge").remove()
        })
        .click(function (e) {
            if ($("#canvasTool").is(":visible") && $("#textCanvas").hasClass("cur")) {
                $("#postilBox").append("<input type='text' id='new-text'/>");
                $("#new-text").css({
                    "width": $("#postilBox").width() - e.pageX + $canvas.offset().left + "px",
                    "left": e.pageX - $canvas.offset().left + "px",
                    "top": e.pageY - $canvas.offset().top - 10 + "px"
                }).focus();
                $("#new-text").blur(function () {
                    if ($(this).val() != "") {
                        ctx.font = "bold 20px 微软雅黑";
                        ctx.fillStyle = "#f00";
                        ctx.fillText($(this).val(), e.pageX - $canvas.offset().left, e.pageY - $canvas.offset().top + 11);
                        $("#postilBox").append(
                            $("<img />", {src: $canvas[0].toDataURL("image/png"), "class": "draw-img"})
                        );
                        ctx.clearRect(0, 0, $canvas.width(), $canvas.height());
                    }
                    $(this).remove();
                });
            } else if ($("#canvasTool").is(":visible") && $(".judge").hasClass("cur")) {
                $("#new-judge").css("left", $("#new-judge").position().left * 100 / $canvas.width() + "%");
                $("#new-judge").css("top", $("#new-judge").position().top * 100 / $canvas.height() + "%").removeAttr("id");
                $("#postilBox").append($("<img />", {
                    src: $(".tick").hasClass("cur") ? judgeTick : judgeFork,
                    "class": "judge-img",
                    "id": "new-judge"
                }));
            }
        });


    //canvas绘画动作
    var drawMode = false;
    $canvas.mousedown(function (e) {
        ctx.beginPath();
        ctx.lineCap = "round"; //端点样式
        ctx.strokeStyle = "#f00"; //线条颜色
        ctx.lineWidth = 2; //线条宽度
        oL = e.pageX - $canvas.offset().left;
        oT = e.pageY - $canvas.offset().top;
        ctx.moveTo(oL, oT);
        if ($("#canvasTool").is(":visible")) {
            drawMode = true;
        }
    }).mousemove(function (e) {
        if (drawMode && $("#penCanvas").hasClass("cur")) {//笔画
            ctx.lineTo(e.pageX - $canvas.offset().left, e.pageY - $canvas.offset().top);
            ctx.stroke();
        } else if (drawMode && $("#waveCanvas").hasClass("cur")) {//波浪线
            var wave_r = 6;
            var wave_l = wave_r * 0.707;
            var wave_n = Math.abs(e.pageX - $canvas.offset().left - oL) * 0.12;
            ctx.clearRect(0, 0, $canvas.width(), $canvas.height());
            ctx.beginPath();
            for (var i = 1; i < wave_n; i++) {
                var wave_a = i * 2 - 1;
                if (i % 2 == 0) {
                    if (e.pageX - $canvas.offset().left - oL > 0) {
                        ctx.arc(oL + wave_a * wave_l, oT + wave_l, wave_r, 1.25 * Math.PI, 1.75 * Math.PI, false);
                    } else {
                        ctx.arc(oL - wave_a * wave_l, oT - wave_l, wave_r, 0.25 * Math.PI, 0.75 * Math.PI, false);
                    }
                } else {
                    if (e.pageX - $canvas.offset().left - oL > 0) {
                        ctx.arc(oL + wave_a * wave_l, oT - wave_l, wave_r, 0.75 * Math.PI, 0.25 * Math.PI, true);
                    } else {
                        ctx.arc(oL - wave_a * wave_l, oT + wave_l, wave_r, 1.75 * Math.PI, 1.25 * Math.PI, true);
                    }
                }
            }
            ctx.stroke();
        } else if (drawMode && $("#straightCanvas").hasClass("cur")) {//直线
            ctx.clearRect(0, 0, $canvas.width(), $canvas.height());
            ctx.beginPath();
            ctx.moveTo(oL, oT);
            ctx.lineTo(e.pageX - $canvas.offset().left, e.pageY - $canvas.offset().top);
            ctx.stroke();
        } else if (drawMode && $("#squareCanvas").hasClass("cur")) {//矩形
            ctx.clearRect(0, 0, $canvas.width(), $canvas.height());
            ctx.strokeRect(oL, oT, e.pageX - $canvas.offset().left - oL, e.pageY - $canvas.offset().top - oT);
        } else if (drawMode && $("#ovalCanvas").hasClass("cur")) {//椭圆
            ctx.clearRect(0, 0, $canvas.width(), $canvas.height());
            ctx.beginPath();
            ctx.save();
            var sL = (e.pageY - $canvas.offset().top - oT) / (e.pageX - $canvas.offset().left - oL);
            ctx.scale(1, sL);
            ctx.arc((e.pageX - $canvas.offset().left + oL) * 0.5, (e.pageY - $canvas.offset().top + oT) * 0.5 / sL, Math.abs(e.pageX - $canvas.offset().left - oL) * 0.5, 0, Math.PI * 2, false);
            ctx.restore();
            ctx.stroke();
        }
    }).mouseup(function (e) {
        drawMode = false;
        // 如果工具条打开才记录批注图片
        if (!$("#canvasTool").is(":hidden")) {
            if ($(".paint").hasClass("cur")) {
                $("#postilBox").append(
                    $("<img />", {src: $canvas[0].toDataURL("image/png"), "class": "draw-img"})
                );
                var stamp_img = $("#postilBox img").not(".stamp-img");
                // 如果画的超出20个
                if (stamp_img.length >= 15) {
                    // 将超出的5个合成一个图
                    for (var imgNum = 0; imgNum < stamp_img.length - 10; imgNum++) {
                        getCtx().drawImage(
                            stamp_img[imgNum],
                            stamp_img.eq(imgNum).position().left,
                            stamp_img.eq(imgNum).position().top,
                            stamp_img.eq(imgNum).width(),
                            stamp_img.eq(imgNum).height());
                        stamp_img.eq(imgNum).remove();
                    }
                    $("#postilBox img").eq(0).before(
                        $("<img />", {src: $("#Canvas")[0].toDataURL("image/png"), "class": "draw-img"})
                    );
                }
                ctx.clearRect(0, 0, $canvas.width(), $canvas.height());
            }
        }
    });

    window.getCtx = function () {
        return ctx;
    };
    //提交时合成一个批注图片
    // $("#scoreSubmit").click(function () {
    //     $("#new-judge").remove();
    //     for (var imgNum = 0; imgNum < $("#postilBox img").not(".stamp-img").length; imgNum++) {
    //         ctx.drawImage($("#postilBox img").not(".stamp-img")[imgNum], $("#postilBox img").not(".stamp-img").eq(imgNum).position().left, $("#postilBox img").not(".stamp-img").eq(imgNum).position().top, $("#postilBox img").not(".stamp-img").eq(imgNum).width(), $("#postilBox img").not(".stamp-img").eq(imgNum).height());
    //     }
    //     $("#postilBox img").not(".stamp-img").remove();
    //     // $("#postilBox").append(
    //     //     $("<img />", {src: $canvas[0].toDataURL("image/png"), "class": "save-paint"})
    //     // );
    //     ctx.clearRect(0, 0, $canvas.width(), $canvas.height());
    // });


});