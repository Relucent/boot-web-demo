~function() {
	var $ctx = (function() {
		var scripts = document.getElementsByTagName('script'), src = scripts[scripts.length - 1].src;
		return src.substring(0, src.length - '/__/import.js'.length);
	})();
	document.writeln('<meta name="renderer" content="webkit" />');
	document.writeln('<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">');
	document.writeln('<meta name="description" content="">');
	document.writeln('<meta http-equiv="pragma" content="no-cache">');
	document.writeln('<meta http-equiv="cache-control" content="no-cache">');
	document.writeln('<meta http-equiv="expires" content="0">');
	document.writeln('<link  href="' + $ctx + '/__/element-ui/2.15.6/theme-chalk/index.css" type="text/css" rel="stylesheet"/>');
	document.writeln('<link  href="' + $ctx + '/__/__/__.css" type="text/css" rel="stylesheet"/>');
	document.writeln('<script src="' + $ctx + '/__/vue/2.6.14/vue.min.js"></script>');
	document.writeln('<script src="' + $ctx + '/__/element-ui/2.15.6/index.js"></script>');
	document.writeln('<script src="' + $ctx + '/__/axios/0.22.0/axios.min.js"></script>');
	document.writeln('<script src="' + $ctx + '/__/cookie/3.0.1/js.cookie.min.js"></script>');
	document.writeln('<script src="' + $ctx + '/__/__/__.js" data-context-path="' + $ctx + '"></script>');
}();
