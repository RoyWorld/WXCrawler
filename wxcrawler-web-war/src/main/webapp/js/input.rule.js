/**
 * 本类主要针对前端输入框输入内容的限制
 */
$(function() {
	var $body = $('body');
	
	/**
	 * 返回字符串中的单字节字符(ascii 0~255 内的字符)
	 */
	var singleString = function(source) {
		if(source) {
			source = source.replace(/[^\x00-\xff]/g, '');
		}
		
		return source;
	};
	
	/**
	 * 限制只能输入常规字符串(单字符)
	 * 只能在 ascii 0~255 内的字符
	 */
	$body.on('keyup', '.rule-singlestring', function(data) {
		var _result = singleString(this.value);
		
		if( this.value !== _result ){
			this.value = _result;
		}
	});

	/**
	 * 只能输入正整数
	 */
	$body.on('keyup', '.rule-digits', function(data) {
		var _value = this.value;
		_value = _value.replace(/^0*/g, '');
		_value = _value.replace(/\D/g, ''); 
		this.value = _value;
	});
	
	/**
	 * 只能输入自然数
	 */
	$body.on('keyup', '.rule-natural-number', function(data) {
		var _value = this.value;
		if(_value.length > 1) {
			_value = _value.replace(/^0*/g, '');
		}
		_value = _value.replace(/\D/g, ''); 
		this.value = _value;
	});
	
	/**
	 * 只能输入英文或者数字
	 */
	$body.on('keyup', '.rule-word', function(data) {
		var _value = this.value;
		var _result = this.value.replace(/\W/g, '').replace('_', ''); 
		if(_value !== _result){
			this.value = _result;
		}
	});
	
	/**
	 * 只能输入数字类型字符，包括小数
	 */
	$body.on('keyup', '.rule-number', function(data) {
		var v = this.value.replace(/[^\d\.]/g, '');
		
		// 除了第一个点 . 外，其他直接替换掉
		var index = v.indexOf('.');
		
		if(index !== -1) {
			var d = v.substring(0, index);
			var n = (v.substring(index+1)).replace(/\./g, '');
			
			v = d + '.' + n;
		}
		
		if(this.value !== v){
			this.value = v;
		}
	});
	
	
	/**
	 * 只能输入数字类型字符，包括小数
	 * 限制两位小数
	 */
	$body.on('keyup', '.rule-money', function(data) {
		var _value = this.value;
		if(_value){
			var pattern = /^\d+(\.[0-9]{0,2})?$/;
			while (_value && !pattern.test(_value)) {
				var length = _value.length;
				if( length > 1){
					_value = _value.substring(0,length-1);
				}else{
					_value = '';
				}
			}
		}
		
		if(_value !== this.value){
			this.value = _value;
		}
	});
	
	/**
	 * 只能输入数字类型字符，包括小数
	 * 限制两位小数
	 */
	$body.on('change', '.rule-money', function(data) {
		var _value = this.value;
		if(!_value){
			return true;
		}
		var pattern = /^\d+(\.[0-9]{0,2})?$/;
		if(pattern.test(_value)){
			_value = parseFloat(_value);
			_value = _value.toFixed(2);
		}
		this.value = _value;
	});
	
	/**
	 * 金额
	 */
	$body.on('blur', '.rule-money', function(data) {
		var _value = this.value;
		if(!_value){
			return true;
		}
		var pattern = /^\d+(\.[0-9]{0,2})?$/;
		if(pattern.test(_value)){
			_value = parseFloat(_value);
			_value = _value.toFixed(2);
		}
		this.value = _value;
	});
	/**
	 * 只能输入英文,数字或汉字
	 */
	$body.on('keyup', '.rule-userName', function(data) {
		var _value = this.value.replace('_', '');
		var _result = this.value.replace(/[|0-9A-z|\u4E00-\u9FFF]/g,'');
		if( _result!=''){
			this.value = _value.substring(0,_value.length-1);
		}
		this.value=this.value.replace('_', '')
	});
});

