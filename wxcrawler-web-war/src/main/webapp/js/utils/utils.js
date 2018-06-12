//初始化插件
//对话框默认配置
swal.setDefaults({
	width: 360,
	padding: 20,
	animation: true,
	allowOutsideClick: false,
	allowEscapeKey: true,
	confirmButtonColor: '#01aa93',
	cancelButtonColor: '#ddd',
	confirmButtonText: '确定',
	cancelButtonText: '取消',
	confirmButtonClass: 'btn btn-primary',
	cancelButtonClass: 'btn btn-default',
	buttonsStyling: true,
	reverseButtons: true,
	showCloseButton: true,
	timer: null
});

/**
 * 常用辅助工具类
 */
(function (window) {
	if (window.utils) {
		return;
	}
	var my = {};

	/**
	 * 根据ID查找源数组中的项
	 * @param list  源数组
	 * @param id    目标ID
	 * @returns {*} 返回NULL为没找到
	 */
	my.findList = function (list, id) {
		for (var i = 0; i < list.length; i++) {
			var obj = list[i];
			if (obj.id == id) {
				return obj;
			}
		}
		return null;
	};

	/**
	 * 根据ID查找源数组中的项
	 * @param list  源数组
	 * @param id    目标ID
	 * @returns {*} 返回NULL为没找到
	 */
	my.findListIndex = function (list, id) {
		for (var i = 0; i < list.length; i++) {
			var obj = list[i];
			if (obj.id == id) {
				return i;
			}
		}
		return null;
	};

	/**
	 * 根据查找源数组中的项
	 * @param sourceList    源数组
	 * @param pk            源标识,例如:'id'
	 * @param targetId      目标ID
	 * @returns {*}         返回NULL为没找到
	 */
	my.findObj = function (sourceList, pk, targetId) {
		for (var i = 0; i < sourceList.length; i++) {
			var obj = sourceList[i];
			if (obj[pk] == targetId) {
				return obj;
			}
		}
		return null;
	};

	/**
	 * 根据查找源数组中的子项
	 * @param sourceList    源数组
	 * @param parentKey        父节点名称，如：'parentId'
	 * @param parentVal        父节点值
	 */
	my.findChildList = function (sourceList, parentKey, parentVal) {
		var list = [];
		for (var i = 0; i < sourceList.length; i++) {
			var obj = sourceList[i];
			if (obj[parentKey] == parentVal) {
				list.push(obj);
			}
		}
		return list;
	};

	//页码限制只能输入数字
	my.pageNum = function (e) {
		//考虑到不通平台键码不一样，所以改用正则更实际
		if (!/^(?!0)\d{1,5}$/.test(e.value)) {
			//替换非数字字符
			e.value = e.value.replace(/[^1-9]/g, '');
		}
	};

	//限制只能输入浮点
	my.onlyFloat = function (e) {
		var k = window.event ? e.keyCode : e.which;

		if (((k >= 48) && (k <= 57)) || k == 8 || k == 0 || k == 46) {
		} else {
			if (window.event) {
				window.event.returnValue = false;
			}
			else {
				e.preventDefault(); //for firefox
			}
		}
	};


	/**
	 * 信息提示框
	 * @param msg       提示内容
	 * @param type      类型: warning, error, success, info, question, 默认是info
	 * @param callback  回调
	 */
	my.alert = function (msg, type, okCallback,canselCallback) {
		swal({
			title: '',
			text: msg || '操作成功',
			type: type || 'info',
			showCancelButton: false
		}).then(function (isConfirm) {
			if (isConfirm === true) {
				if ($.isFunction(okCallback))
                    okCallback();
			}else{
                if ($.isFunction(canselCallback))
                    canselCallback();
			}
		});

		//示例
		//utils.alert('警告信息,例如:请输入账号之类的', 'warning');
		//utils.alert('失败或异常提示', 'error');
		//utils.alert('成功提示', 'success');
		//utils.alert('普通信息', 'info');
		//utils.alert('我需要回调', 'success', function () {
		//    // 这里是回调事件
		//})
	};

	/**
	 * 自动关闭信息提示框
	 * @param msg       提示内容
	 * @param time      超时时间
	 * @param type      类型，warning, error, success, info, question
	 * @param callback  回调
	 */
	my.alertAutoClose = function (msg, time, type) {
		return my.alert(msg,type);

		/*swal({
			title: '',
			text: msg || '操作成功',
			type: type || 'info',
			showCancelButton: false,
			timer: time || 3000
		});*/
	};

	my.msg = function (msg, time, callback) {
		return my.alert(msg, 'success');
		swal({
			title: '',
			text: msg || '操作成功',
			type: 'info',
			showCancelButton: false,
			timer: time || 2000
		}).then(function () {

			if ($.isFunction(callback))
				callback();

		});
	};

	/**
	 * 确认提示框
	 * @param msg       提示信息
	 * @param callback  确认后的回调事件
	 */
	my.confirm = function (msg, callback) {
		swal({
			title: '',
			text: msg || '操作成功',
			type: 'question',
			showCancelButton: true
		}).then(function (isConfirm) {
			if (isConfirm === true) {
				if ($.isFunction(callback))
					callback(isConfirm);
			}
		});
	};

	/**
	 * 确认提示框（扩展，无论是否确认都会执行回调，以执行点击取消后的操作）
	 * @param msg       提示信息
	 * @param callback  确认后的回调事件
	 */
	my.confirmEx = function (msg, callback) {
		swal({
			title: '',
			text: msg || '操作成功',
			type: 'question',
			showCancelButton: true
		}).then(function (isConfirm) {
			if ($.isFunction(callback))
				callback(isConfirm);
		});
	};

	/**
	 * 确认或取消提示框
	 * @param msg           提示信息
	 * @param successEvent  成功回调事件
	 * @param cancelEvent   取消回调事件
	 */
	my.confirmAndCancel = function (msg, successEvent, cancelEvent) {
		swal({
			title: '',
			text: msg || '操作成功',
			type: 'question',
			showCancelButton: true
		}).then(function (isConfirm) {
			if (isConfirm === true) {
				if ($.isFunction(successEvent))
					successEvent();
			}
			else if (isConfirm === false) {
				if ($.isFunction(cancelEvent))
					cancelEvent();
			}
		});
	};

	/**
	 * 绑定回车
	 * @param callback 回调
	 */
	my.bindEnter = function (callback) {
		$(document).bind("keydown", function (e) {
			if (e.keyCode == 13) {
				callback();
			}
		});
	};

	/**
	 * 地址重定位
	 * @param url 目标地址
	 */
	my.gourl = function (url) {
		window.location.href = url;
	};

	/**
	 * 刷新当前页
	 */
	my.reload = function () {
		window.location.reload();
	};

	/**
	 * 取得URL参数
	 * @param paras 参数值
	 * @returns {*}
	 */
	my.request = function (paras) {
		var url = location.href;
		var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
		var paraObj = {};
		for (i = 0; j = paraString[i]; i++) {
			paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
		}
		var returnValue = paraObj[paras.toLowerCase()];
		if (typeof (returnValue) == "undefined") {
			return "";
		} else {
			return decodeURIComponent(returnValue);
		}
	};

	/**
	 * 打开或关闭元素
	 * @param ele 元素名称，带#
	 * @param value true为关闭，false为打开
	 */
	my.disabled = function (ele, value) {
		if (value == true) {
			$(ele).prop('disabled', 'disabled');
		} else {
			$(ele).removeAttr('disabled');
		}
	};

	my.guid = function () {
		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
			var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(16);
		});
	};

	/**
	 * 随机数，用在新建考试页面
	 * @returns {string}
	 */
	my.oid = function () {
		return 'xxxxxxxx'.replace(/[xy]/g, function (c) {
			var r = Math.random() * 8 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(8);
		});
	};

	/**
	 * 检查输入手机号码是否正确
	 * @param str 手机号码
	 * @returns {boolean} 如果通过验证返回true,否则返回false
	 */
	my.isMobile = function (str) {
		var re = new RegExp(/^(1[0-9])\d{9}$/);
		return re.test(str);
	};

	/**
	 * 检查输入的身份证号是否正确
	 * @param str 身份证号
	 * @returns {boolean} 返回:true 或 flase; true表示格式正确
	 */
	my.isIDCard = function (str) {
		//15位数身份证正则表达式
		var arg1 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
		//18位数身份证正则表达式
		var arg2 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z])$/;
		if (str.match(arg1) == null && str.match(arg2) == null) {
			return false;
		}
		else {
			return true;
		}
	};

	/**
	 * 检查输入的URL地址是否正确
	 * @param str URL地址
	 * @returns {boolean} 返回:true 或 flase; true表示格式正确
	 */
	my.isURL = function (str) {
		return str.match(/(http[s]?|ftp):\/\/[^\/\.]+?\..+\w$/i) == null ? false : true;
	};

	/**
	 * 检查输入的电话号码格式是否正确
	 * @param str 电话号码
	 * @returns {boolean} 如果通过验证返回true,否则返回false
	 */
	my.isPhone = function (str) {
		var re1 = /^1[34578]\d{9}$/;
		var re2 = /^[1-9]{1}[0-9]{5,8}$/;
		if (str.length > 9) {
			return re1.test(str);
		} else {
			return re2.test(str);
		}
	};

    my.isPhone2 = function (str) {
        var re1 = /^(1[0-9])\d{9}$/;
        var re2 = /^[1-9]{1}[0-9]{5,8}$/;
        if (str.length > 9) {
            return re1.test(str);
        } else {
            return re2.test(str);
        }
    };

	/**
	 * 检查输入字符串是否为空或者全部都是空格
	 * @param str 字符串
	 * @returns {boolean} 如果全是空返回true,否则返回false
	 */
	my.isNull = function (str) {
		if (str == "" || str == 'undefined' || !str)
			return true;
		var re = new RegExp("^[ ]+$");
		return re.test(str);
	}

	/**
	 * 检查输入对象的值是否符合整数格式
	 * @param str 字符串
	 * @returns {boolean} 如果通过验证返回true,否则返回false
	 */
	my.isInteger = function (str) {
		var re = /^[-]{0,1}[0-9]{1,}$/;
		return re.test(str);
	}

	/**
	 * 检查输入字符串是否符合正整数格式
	 * @param str 字符串
	 * @returns {boolean} 如果通过验证返回true,否则返回false
	 */
	my.isNumber = function (str) {
		var re = new RegExp("^[0-9]+$");
		return str.search(re) != -1;
	};

	/**
	 * 检查输入字符串是否是带小数的数字格式,可以是负数
	 * @param str 字符串
	 * @returns {boolean} 如果通过验证返回true,否则返回false
	 */
	my.isDecimal = function (str) {
		if (isInteger(str))
			return true;
		var re = /^[-]{0,1}(\d+)[\.]+(\d+)$/;
		if (re.test(str)) {
			return (RegExp.$1 == 0 && RegExp.$2 == 0) ? false : true;
		}
		return false;
	};

	/**
	 * 检查输入字符串是否符合金额格式(格式定义为带小数的正数，小数点后最多三位)
	 * @param str 字符串
	 * @returns {boolean} 如果通过验证返回true,否则返回false
	 */
	my.isMoney = function (str) {
		var re = new RegExp("^[0-9]+[\.][0-9]{0,3}$");
		return re.test(str);
	};

	/**
	 * 检查字符串是否只由英文字母和数字和下划线组成
	 * @param str 字符串
	 * @returns {boolean} 如果通过验证返回true,否则返回false
	 */
	my.isNumberOr_Letter = function (str) {
		var re = new RegExp("^[0-9a-zA-Z\_]+$");
		return re.test(str);
	};

	/**
	 * 检查字符串是否只由英文字母和数字组成
	 * @param str 字符串
	 * @returns {boolean} 如果通过验证返回true,否则返回false
	 */
	my.isNumberOrLetter = function (str) {
		//判断是否是数字或字母
		var re = new RegExp("^[0-9a-zA-Z]+$");
		return re.test(str);
	};

	/**
	 * 检查字符串是否只由汉字、字母、数字组成
	 * @param str 字符串
	 * @returns {boolean} 如果通过验证返回true,否则返回false
	 */
	my.isChinaOrNumbOrLett = function (str) {
		var re = new RegExp("^[0-9a-zA-Z\u4e00-\u9fa5]+$");
		return re.test(str);
	};

	/**
	 * 检查Email格式
	 * @param str 字符串
	 * @returns {boolean} 如果通过验证返回true,否则返回false
	 */
	my.isEmail = function (str) {
		var re = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
		return re.test(str);
	};

	/**
	 * 检查日期格式
	 * @param str
	 * @returns {boolean}
	 */
	my.isDate = function (str) {
		if (str == '') return true;
		if (str.length != 8 || !isNumber(str))
			return false;
		var year = str.substring(0, 4);
		if (year > "2100" || year < "1900")
			return false;
		var month = str.substring(4, 6);
		if (month > "12" || month < "01")
			return false;
		var day = str.substring(6, 8);
		if (day > getMaxDay(year, month) || day < "01")
			return false;
		return true;
	};

	/**
	 * 删除左右两端的空格
	 * @param str
	 * @returns {*}
	 */
	my.trim = function (str) {
		return my.isNull(str) ? '' : str.replace(/(^\s*)|(\s*$)/g, "");
	}

	/**
	 * 删除左边的空格
	 * @param str
	 * @returns {*}
	 */
	my.ltrim = function (str) {
		return str.replace(/(^\s*)/g, "");
	};

	/**
	 * 删除右边的空格
	 * @param str
	 * @returns {*}
	 */
	my.rtrim = function (str) {
		return str.replace(/(\s*$)/g, "");
	};

	/**
	 * 删除最后一个字符
	 * @param str   字符串
	 * @param char  欲删除的字符
	 */
	my.remove_last_char = function (str, char) {
		var $idx = str.lastIndexOf(char);
		if ($idx == -1) {
			return str;
		} else {
			return str.substring(0, str.length - 1);
		}
	};

	/**
	 * 格式化日期时间
	 * @param date      字符串
	 * @param format    格式
	 * @returns {*}     返回已格式化的字符串
	 */
	my.formatDate = function (date, format) {
		if (!date) return;
		if (!format) format = "yyyy-MM-dd";
		switch (typeof date) {
			case "string":
				date = new Date(date.replace(/-/, "/"));
				break;
			case "number":
				date = new Date(date);
				break;
		}
		if (!date instanceof Date) return;
		var dict = {
			"yyyy": date.getFullYear(),
			"M": date.getMonth() + 1,
			"d": date.getDate(),
			"H": date.getHours(),
			"m": date.getMinutes(),
			"s": date.getSeconds(),
			"MM": ("" + (date.getMonth() + 101)).substr(1),
			"dd": ("" + (date.getDate() + 100)).substr(1),
			"HH": ("" + (date.getHours() + 100)).substr(1),
			"mm": ("" + (date.getMinutes() + 100)).substr(1),
			"ss": ("" + (date.getSeconds() + 100)).substr(1)
		};
		return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function () {
			return dict[arguments[0]];
		});
	};

	my.uuid = function uuid() {
		var s = [];
		var hexDigits = "0123456789abcdef";
		for (var i = 0; i < 36; i++) {
			s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
		}
		s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
		s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
		s[8] = s[13] = s[18] = s[23] = "-";
		return s.join("");
	};

	/**
	 * 分数文本框验证
	 * 规则：最多包含两位小数
	 */
	my.scoreCheck = function (e, val) {
		if (!/^\d+[.]?[1-9]?$/.test(val)) {
			var newValue = /^\d+/.exec(e.value);
			if (newValue != null) {
				e.value = newValue;
			}
			else {
				e.value = "";
			}
		}
		return false;
	};

	/**
	 * 分页输入
	 */
	my.pageNumCheck = function (e, val) {
		if (!/^\d+$/.test(val)) {
			var newValue = /^\d+/.exec(e.value);
			if (newValue != null) {
				e.value = newValue;
			}
			else {
				e.value = 1;
			}
		}
		return false;
	};

	/**
	 * JSON 数组排序
	 * @param filed         排序字段
	 * @param rev           方向，false为升序
	 * @param primer        类型，parseInt为数字排序，String为字符串排序
	 * @returns {Function}  返回排序后的结果
	 * @constructor
	 */
	my.JsonSort = function (filed, rev, primer) {
		rev = (rev) ? -1 : 1;
		return function (a, b) {
			a = a[filed];
			b = b[filed];
			if (typeof (primer) != 'undefined') {
				a = primer(a);
				b = primer(b);
			}
			if (a < b) {
				return rev * -1;
			}
			if (a > b) {
				return rev * 1;
			}
			return 1;
		}
	};

	/**
	 * 检查有无怪字符
	 * @param content
	 * @returns {boolean}
	 */
	my.warnStrange = function (content) {
		//匹配这些中文标点符号 。 ？ ！ ， 、 ； ： “ ” ‘ ' （ ） 《 》 〈 〉 【 】 『 』 「 」 ﹃ ﹄ 〔 〕 … — ～ ﹏ ￥
		var reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]/;
		if (reg.test(content)) {
			return true;
		}

		var strangeStr = ["<", "--", ";", ">", "=", "script", "&", "alert", "document", "<iframe", "&lt;iframe", "&ltiframe", "javascript", "vbscript", "vb-script", "applet", "frameset", "expression", "%", "#", "^", "\\", "'", " ", "~", "!", "@", "*", "+", "{", "}", ":", ",", "！", "@", "#", "￥", "%", "……", "&", "*", "—", "+", "{", "}", "|", "【", "】", "‘", "；", "：", "”", "“", "。", "，", "、", "？", "(", ")", ".", "`", "$", "_", "-", "[", "]", "?", '"', "/"];
		for (var i = 0; i < strangeStr.length; i++) {
			if (content.indexOf(strangeStr[i]) > -1) {
				return true;
			}
		}
		return false;
	};

	/**
	 * 删除下拉选项
	 * @param sel
	 */
	my.removeSelBlank = function (sel) {
		if (sel.children().get(0).value.indexOf("?") == 0)
			sel.children().get(0).remove();
	};

	/**
	 * 获取指定日期
	 * @param day 天数(0;今天,1:前天,-1明天)
	 * @returns {*}
	 */
	my.getDate = function (day) {
		var zdate = new Date();
		var sdate = zdate.getTime();    // - (1 * 24 * 60 * 60 * 1000);
		var edate = new Date(sdate - (day * 24 * 60 * 60 * 1000)).Format("yyyy-MM-dd");
		return edate;

	};

	my.verificationMobile = function (mobile) {
		if ($.trim(mobile) == undefined || $.trim(mobile) == "") {
			utils.alert("手机号码不能为空", 'warning');
			return false;
		} else if (mobile) {
			//var isMobile = /^((13[0-9])|(15[^4,\D])|(18[0,5-9]))\d{8}$/;
            var isMobile = /^(1[0-9])\d{9}$/;
			if (!isMobile.exec(mobile) && mobile != '') {
				utils.alert("请输入正确的手机号码", 'warning');
				return false;
			}
		}
		return true;
	};

	/**
	 * 多选下拉列表
	 */
	my.multiSelectBox = function () {
		$(".select-cover").click(function () {
			$(this).siblings(".select-text,.select-num").addClass("open");
			$(this).siblings(".select-list").show().attr("tabindex", "0").focus();
			$(this).hide();
		});
		$(".select-list").blur(function () {
			$(this).siblings(".select-text,.select-num").removeClass("open");
			$(this).hide();
			$(this).siblings(".select-cover").show();
		});
		$(".select-list").on('click', 'dd b', function () {
			if ($(this).siblings("input").prop("disabled") == true) {
				return;
			}
			else if ($(this).siblings("input").prop("checked") == true) {
				$(this).parent().parent().parent().siblings(".select-text").children("#" + $(this).siblings("input").attr("data-tip")).remove();
				$(this).siblings("input").prop("checked", false);
				$(this).parent().siblings("dt").children("input").prop("checked", false);
			} else {
				$(this).parent().parent().parent().siblings(".select-text").append($("<span></span>").html('<b>，</b>' + $(this).parent().text()).attr('id', $(this).siblings('input').attr('data-tip')));
				$(this).siblings("input").prop("checked", true);
			}
		});
		$(".select-list").on('click', 'dt b', function () {
			if ($(this).siblings("input").prop("disabled") == true) {
				return;
			}
			else if ($(this).siblings("input").prop("checked") == true) {
				$(this).siblings("input").prop("checked", false);
				$(this).parent().siblings("dd").children("input:checked").siblings("b").click();
			} else {
				$(this).siblings("input").prop("checked", true);
				$(this).parent().siblings("dd").children("input").not(":checked").siblings("b").click();
			}
		});
		$(".select-list").on('click', 'b', function () {
			if ($(this).siblings("input").prop("disabled") == true) {
				return;
			}
			else {
				$(this).parent().parent().parent().siblings(".select-num").children("b").html($(this).parent().parent().children("dd").children("input:checked").length);
			}
		});
	};

	/**
	 * 删除下拉选项
	 * @param model    可传入单个或数组
	 */
	my.removeSel = function (model) {
		var arr = [];
		if (model instanceof Array) {
			arr = model;
		} else {
			arr.push(model);
		}

		window.setTimeout(function () {
			for (var i = 0; i < arr.length; i++) {
				var sel = $('select[ng-model="' + arr[i] + '"]');
				if ((sel.children().get(0) != null) && sel.children().get(0).value.indexOf("?") == 0)
					sel.children().get(0).remove();
			}
		});
	};

	my.safeApply = function (scope, fn) {
		var phase = scope.$root.$$phase;
		if (phase == '$apply' || phase == '$digest') {
			if (fn && (typeof(fn) === 'function')) {
				fn();
			}
		} else {
			scope.$apply(fn);
		}
	};

	my.setMenu = function (url) {
		var list = $(".nav a");
		var cur;
		for (var i = 0; i < list.length; i++) {
			var obj = list[i];
			if ($(obj).attr('href').indexOf(url) > -1) {
				cur = $(obj);
				break;
			}
		}
		if (cur) {
			$(".nav .active").removeClass("active");
			$(".nav .cur").removeClass("cur");
			cur.addClass('cur').parent().closest('dd').addClass('active');
		}
	};

	/**
	 * 模态窗口关闭事件
	 * @param eleId        对象ID
	 * @param callback    回调事件
	 */
	my.modalCloseEvent = function (eleId, callback) {
		$(eleId).on('hidden.bs.modal', function (e) {
			callback();
		})
	};

	/**
	 * 数组去重
	 * @param arr    需要去重的数组
	 * @returns {Array}    返回已去重的数组
	 */
	my.unique = function (arr) {
		//用一个hashtable的结构记录已有的元素，这样就可以避免内层循环，提高效率
		var result = [], hash = {};
		for (var i = 0, elem; (elem = arr[i]) != null; i++) {
			if (!hash[elem]) {
				result.push(elem);
				hash[elem] = true;
			}
		}
		return result;
	};

	my.isRepeat = function (arr) {
		var hash = {};
		for (var i in arr) {
			if (hash[arr[i]])
				return true;
			hash[arr[i]] = true;
		}
		return false;
	};

// my.ready = function (scope, fn) {
// 	angular.element(document).ready(function () {
//
// 		var $injector = angular.bootstrap(document, ['myApp']);
// 		var $controller = $injector.get('$controller');
// 		var MyController = $controller('MyController');
// 		MyController.userId = data;
// 	});
// }
	/**
	 *  是否成功返回
	 * @param data        返回结果
	 */
	my.isSuccess = function (data) {
		var rtnCode = data;
		if (data.hasOwnProperty("rtnCode"))
			rtnCode = data.rtnCode;
		return "0000000" == rtnCode;
	};
	/**
	 *  是否会话过期
	 * @param data        返回结果
	 */
	my.isSessionExpired = function (data) {
		return data.msg == undefined;
	}

	/**
	 *  是否为空
	 * @param obj        对象
	 */
	my.isNull = function (obj) {
		if (typeof(obj) == "undefined" || obj == "undefined") {
			return true;
		} else {
			return (obj == null || obj.length <= 0 ) ? true : false;
		}
	}

	/**
	 *  删除URL参数
	 * @param key        键值
	 * @param sourceURL        原URL
	 * @returns 处理URL
	 */
	my.removeURLParam = function (key, sourceURL) {
		var rtn = sourceURL.split("?")[0],
			param,
			params_arr = [],
			queryString = (sourceURL.indexOf("?") !== -1) ? sourceURL.split("?")[1] : "";
		if (queryString !== "") {
			params_arr = queryString.split("&");
			for (var i = params_arr.length - 1; i >= 0; i -= 1) {
				param = params_arr[i].split("=")[0];
				if (param === key) {
					params_arr.splice(i, 1);
				}
			}
			rtn = rtn + "?" + params_arr.join("&");
		}
		return rtn;
	}

	/**
	 * 获取字符串长度（英文占1个字符，中文汉字占2个字符）
	 * @param str
	 * @returns {number}
	 */
	my.getStrLength = function (str) {
		if (str == null) return 0;
		if (typeof str != "string") {
			str += "";
		}
		return str.replace(/[^\x00-\xff]/g, "01").length;
	}

	/**
	 * 获取考试范围（3-所有，1-单考，2-联考,-1没有权限）
	 * @param code  功能代码
	 * @returns {number}
	 */
	my.getExamScope = function (code) {
		if (code == null)
			return -1;

		var examScopeMap = $.parseJSON($("#s_examScopeMap").val());

		return my.isNull(examScopeMap[code]) ? 0 : examScopeMap[code];
	};

	my.hasSingleScope = function (code) {
		return (my.getExamScope(code) & 1) != 0;
	};
	my.hasMultiScope = function (code) {
		return (my.getExamScope(code) & 2) != 0;
	};
	my.hasAllScope = function (code) {
		return my.getExamScope(code) == 3;
	};

	window.utils = my;
})
(window);


//扩展部分原型
/**
 * 对Date的扩展，将 Date 转化为指定格式的String
 * 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
 * 例子：
 * (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
 * (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
 * @param fmt
 * @returns {*}
 * @constructor
 */
Date.prototype.Format = function (fmt) { //author: meizz
	var o = {
		"M+": this.getMonth() + 1, //月份
		"d+": this.getDate(), //日
		"h+": this.getHours(), //小时
		"m+": this.getMinutes(), //分
		"s+": this.getSeconds(), //秒
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度
		"S": this.getMilliseconds() //毫秒
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};

Array.prototype.max = function () {
	var max = this[0];
	this.forEach(function (ele, index, arr) {
		if (ele > max) {
			max = ele;
		}
	});
	return max;
};

Array.prototype.min = function () {
	var min = this[0];
	this.forEach(function (ele, index, arr) {
		if (ele < min) {
			min = ele;
		}
	});
	return min;
};