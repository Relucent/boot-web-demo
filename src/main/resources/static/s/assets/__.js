var __ = {
	cxt : (function() {
		var scripts = document.getElementsByTagName('script'), src = scripts[scripts.length - 1].src;
		return src.substring(0, src.length - '/s/assets/__.js'.length);
	})(),
	url : function(path, params) {
		return ((/^https?:/ig).test(path) ? path : __.cxt + path) + (__.isEmpty(params) ? '' : (path.indexOf('?') != -1 ? '&' : '?') + __.encodeUrlParams(params));
	},
	navigateTo : function(url) {
		location.href = __.url(url);
	},
	api : (function() {
		var wrapCb = function(callback) {
			return !callback ? __.noop : function(response) {
				var headers = response.headers || {};
				if (headers['session-timeout-signal'] === 'is-timeout') {
					__.alert('当前会话已经失效，请重新登录', '提示', {
						callback : function(action) {
							__.navigateTo('/');
						}
					});
					return;
				}
				callback(response.data);
			}
		}, catchFn = function(error, s) {
			console.log(error);
			if (error.message === 'Network Error') {
				return;
			}
			__.vue().$message.warning('请求异常!');
		}, isIE = (window.ActiveXObject || 'ActiveXObject' in window);
		return function(method, url, data, callback) {
			method = method.toLowerCase();
			var config = {
				headers : {
					'X-Requested-with' : 'XMLHttpRequest',
					'Pragma' : 'no-cache',
					'Cache-Control' : 'no-cache',
					'Expires' : '0',
					'If-Modified-Since' : '0'
				},
				method : method,
				url : __.url(url)
			};
			if ('post' === method || 'put' === method || 'patch' === method) {
				config.data = data;
			}
			// delete', 'get', 'head', 'options
			else {
				var url = config.url, search = __.encodeUrlParams(data || {});
				!__.isEmpty(search) && (config.url = url + (url.indexOf('?') == -1 ? '?' : '&') + search);
			}
			if (isIE) {
				config.url = config.url + (config.url.indexOf('?') != -1 ? '&' : '?') + '%3A%3Atimestamp=' + (+new Date());
			}
			return axios.request(config).then(wrapCb(callback))['catch'](catchFn);
		}
	})(),
	err : function(result) {
		return result && !(result.code == 0 || result.code == 200);
	},
	pagedParams : function(currentPage, limit) {
		!limit && (limit = 10);
		return {
			':start' : Math.max((currentPage | 0) - 1, 0) * (limit | 0),
			':limit' : limit
		};
	},
	extend : function() {
		var src, copyIsArray, copy, name, options, clone, target = arguments[0] || {}, i = 1, length = arguments.length, deep = false;
		// Handle a deep copy situation
		if (typeof target === "boolean") {
			deep = target;
			// skip the boolean and the target
			target = arguments[i] || {};
			i++;
		}
		// Handle case when target is a string or something (possible in deep copy)
		if (typeof target !== "object" && !__.isFunction(target)) {
			target = {};
		}
		// extend __ itself if only one argument is passed
		if (i === length) {
			target = __;
			i--;
		}
		for (; i < length; i++) {
			// Only deal with non-null/undefined values
			if ((options = arguments[i]) != null) {
				// Extend the base object
				for (name in options) {
					src = target[name];
					copy = options[name];
					// Prevent never-ending loop
					if (target === copy) {
						continue;
					}
					// Recurse if we're merging plain objects or arrays
					if (deep && copy && (__.isPlainObject(copy) || (copyIsArray = __.isArray(copy)))) {
						if (copyIsArray) {
							copyIsArray = false;
							clone = src && __.isArray(src) ? src : [];

						} else {
							clone = src && __.isPlainObject(src) ? src : {};
						}

						// Never move original objects, clone them
						target[name] = __.extend(deep, clone, copy);

						// Don't bring in undefined values
					} else if (copy !== undefined) {
						target[name] = copy;
					}
				}
			}
		}
		// Return the modified object
		return target;
	}
};

(function() {

	var array = [];
	var slice = array.slice;
	var concat = array.concat;
	var push = array.push;
	var indexOf = array.indexOf;

	var class2type = {};
	var toString = class2type.toString;
	var hasOwn = class2type.hasOwnProperty;
	var support = {};

	__.extend({
		now : function() {
			return +(new Date());
		},
		defer : function(fn, millis) {
			millis = !isNaN(millis) && millis > 0 ? millis : 1;
			return setTimeout(fn, millis);
		},
		noop : function() {
			// ignore
		},
		each : function(o, callback) {
			var i, length = o.length;
			if (__.isArraylike(o)) {
				for (i = 0; i < length; i++) {
					if (callback.call(o[i], i, o[i]) === false) {
						break;
					}
				}
			} else {
				for (i in o) {
					if (callback.call(o[i], i, o[i]) === false) {
						break;
					}
				}
			}
		},
		indexOf : function(array, elem, i) {
			var len;
			if (array) {
				if (indexOf) {
					return indexOf.call(array, elem, i | 0);
				}
				len = array.length;
				i = i ? i < 0 ? Math.max(0, len + i) : i : 0;
				for (; i < len; i++) {
					// Skip accessing in sparse arrays
					if (i in array && array[i] === elem) {
						return i;
					}
				}
			}
			return -1;
		},
		type : (function() {
			var class2type = {
				'[object Array]' : 'array',
				'[object Boolean]' : 'boolean',
				'[object Date]' : 'date',
				'[object Error]' : 'error',
				'[object Function]' : 'function',
				'[object Number]' : 'number',
				'[object Object]' : 'object',
				'[object RegExp]' : 'regexp',
				'[object String]' : 'string'
			}, toString = class2type.toString;
			return function(o) {
				return o == null ? (o + '') //
				: (typeof o === "object" || typeof o === "function" //
				? class2type[toString.call(o)] || "object" : typeof o);
			}
		})(),
		isArray : Array.isArray || function(o) {
			return __.type(o) === "array";
		},
		isDate : function(o) {
			return __.type(o) === "date";
		},
		isRegExp : function(o) {
			return __.type(o) === "regexp";
		},
		isFunction : function(o) {
			return __.type(o) === "function";
		},
		isArray : Array.isArray || function(o) {
			return __.type(o) === "array";
		},
		isWindow : function(o) {
			return o != null && o == o.window;
		},
		isEmpty : function(o, allowBlank) {
			return o === null || o === undefined || ((__.isArray(o) && !o.length)) || (!allowBlank ? o === '' : false);
		},
		isEmptyObject : function(o) {
			var name;
			for (name in o) {
				return false;
			}
			return true;
		},
		isPlainObject : function(obj) {
			var key;
			if (!obj || __.type(obj) !== "object" || obj.nodeType || __.isWindow(obj)) {
				return false;
			}
			try {
				if (obj.constructor && !hasOwn.call(obj, "constructor") && !hasOwn.call(obj.constructor.prototype, "isPrototypeOf")) {
					return false;
				}
			} catch (e) {
				return false;// IE8,9
			}
			// Support: IE<9
			if (support.ownLast) {
				for (key in obj) {
					return hasOwn.call(obj, key);
				}
			}
			for (key in obj) {
			}
			return key === undefined || hasOwn.call(obj, key);
		},
		isArraylike : function(o) {
			var length = o.length, type = __.type(o);
			if (type === "function" || __.isWindow(o)) {
				return false;
			}
			if (o.nodeType === 1 && length) {
				return true;
			}
			return type === "array" || length === 0 || typeof length === "number" && length > 0 && (length - 1) in o;
		},
		proxy : function(fn, context) {
			if (!__.isFunction(fn)) {
				return undefined;
			}
			var args = slice.call(arguments, 2);
			proxy = function() {
				return fn.apply(context || this, args.concat(slice.call(arguments)));
			};
			return proxy;
		},
		encodeHTML : function(content) {
			return String(content).replace(new RegExp('[\"\'<>&\s]', 'g'), function($0) {
				switch ($0) {
				case '"':
					return '&quot;';
				case "'":
					return '&apos;';
				case '>':
					return '&gt;';
				case '<':
					return '&lt;';
				case ' ':
					return '&nbsp;';
				default:
					return $0;
				}
			});
		},
		encodeUrlParams : function(params) {
			var search = [];
			var addFields = function(k, v) {
				if (v == null) {
					return;
				}
				search.push([ encodeURIComponent(k), '=', encodeURIComponent(v) ].join(''));
			}
			__.each(params || {}, function(k, v) {
				if (v == null) {
					return;
				}
				if (__.isArray(v)) {
					__.each(v, function(k, v) {
						addFields(k, v);
					});
				} else {
					addFields(k, v);
				}
			});
			return search.join('&');
		},
		decodeUrlParams : function(search) {
			search = search || location.search;
			var params = {};
			// (remove any leading ? || #)(remove any trailing & || ;)(replace +'s
			// with spaces)(split & || ;)
			__.each(search.replace(/^[?#]/, '').replace(/[;&]$/, '').replace(/[+]/g, ' ').split(/[&;]/), function(i, t) {
				var kv = t.split('='), k = decodeURIComponent(kv[0] || ''), v = decodeURIComponent(kv[1] || '');
				if (!k) {
					return;
				}
				if (__.isArray(params[k])) {
					params[k].push(v);
				} else if (params[k] != null) {
					params[k] = [ params[k], v ];
				} else {
					params[k] = v;
				}
			});
			return params;
		},
		encode : (function() {
			var I = !!{}.hasOwnProperty, _ = function(I) {
				return I < 10 ? "0" + I : I;
			}, A = {
				"\b" : "\\b",
				"\t" : "\\t",
				"\n" : "\\n",
				"\f" : "\\f",
				"\r" : "\\r",
				"\"" : "\\\"",
				"\\" : "\\\\"
			}, stringify = (function(C) {
				if (typeof C == "undefined" || C === null) {
					return "null";
				} else {
					if (Object.prototype.toString.call(C) === "[object Array]") {
						var B = [ "[" ], G, E, D = C.length, F;
						for (E = 0; E < D; E += 1) {
							F = C[E];
							switch (typeof F) {
							case "undefined":
							case "function":
							case "unknown":
								break;
							default:
								if (G) {
									B.push(",");
								}
								B.push(F === null ? "null" : stringify(F));
								G = true;
							}
						}
						B.push("]");
						return B.join("");
					} else {
						if ((Object.prototype.toString.call(C) === "[object Date]")) {
							return "\"" + C.getFullYear() + "-" + _(C.getMonth() + 1) + "-" + _(C.getDate()) + "T" + _(C.getHours()) + ":" + _(C.getMinutes()) + ":" + _(C.getSeconds()) + "\"";
						} else {
							if (typeof C == "string") {
								return "\"" + C.replace(/([\x00-\x1f\\"])/g, function(B, _) {
									var I = A[_];
									if (I) {
										return I;
									}
									return '';
								}).replace(/[^\u0000-\u00FF]/g, function($0) {
									return escape($0).replace(/(%u)(\w{4})/gi, "\\u$2")
								}) + "\"";
							} else {
								if (typeof C == "number") {
									return isFinite(C) ? String(C) : "null";
								} else {
									if (typeof C == "boolean") {
										return String(C);
									} else {
										B = [ "{" ], G, E, F;
										for (E in C) {
											if (!I || C.hasOwnProperty(E)) {
												F = C[E];
												if (F === null) {
													continue;
												}
												switch (typeof F) {
												case "undefined":
												case "function":
												case "unknown":
													break;
												default:
													if (G) {
														B.push(",");
													}
													B.push(stringify(E), ":", stringify(F));
													G = true;
												}
											}
										}
										B.push("}");
										return B.join("");
									}
								}
							}
						}
					}
				}
			});
			return stringify;
		})(),
		decode : function(json, unsafe) {
			if (json == null) {
				return null;
			}
			try {
				return JSON.parse(json);
			} catch (e) {
				if (unsafe === true) {
					try {
						return eval('(' + json + ')');
					} catch (e) {
					}
				}
			}
			return undefined;
		},
		encodeReg : function(value) {
			return String(value).replace(/([.*+?^=!:${}()|[\]/\\])/g, '\\$1');
		},
		parseDate : (function() {
			var methods = {
				'y' : function(d, v) {
					d.setFullYear(v);
				},
				'M' : function(d, v) {
					d.setMonth(v - 1);
				},
				'd' : function(d, v) {
					d.setDate(v);
				},
				'H' : function(d, v) {
					d.setHours(v);
				},
				'm' : function(d, v) {
					d.setMinutes(v);
				},
				's' : function(d, v) {
					d.setSeconds(v);
				}
			};
			return function(value, pattern) {
				try {
					var matchs1 = (pattern || (value.length == 10 ? 'yyyy-MM-dd' : 'yyyy-MM-dd HH:mm:ss')).match(/([yMdHsm])(\1*)/g), matchs2 = value.match(/(\d)+/g);
					if (matchs1.length == matchs2.length) {
						var d = new Date(1970, 0, 1);
						for (var i = 0; i < matchs1.length; i++) {
							(methods[matchs1[i].charAt(0)] || __.noop)(d, parseInt(matchs2[i], 10));
						}
						return d;
					}
				} catch (err) {
				}
				return null;
			};
		})(),
		formatDate : function() {
			var SIGN_RG = /([yMdHsm])(\1*)/g;
			function padding(s, len) {
				var len = len - (s + '').length;
				for (var i = 0; i < len; i++) {
					s = '0' + s;
				}
				return s;
			}
			return function(value, pattern) {
				if (typeof value === 'string') {
					value = __.parseDate(value);
				}
				if (!__.isDate(value)) {
					return '';
				}
				try {
					pattern = pattern || 'yyyy-MM-dd';
					return pattern.replace(SIGN_RG, function($0) {
						switch ($0.charAt(0)) {
						case 'y':
							return padding(value.getFullYear(), $0.length);
						case 'M':
							return padding(value.getMonth() + 1, $0.length);
						case 'd':
							return padding(value.getDate(), $0.length);
						case 'w':
							return value.getDay() + 1;
						case 'H':
							return padding(value.getHours(), $0.length);
						case 'm':
							return padding(value.getMinutes(), $0.length);
						case 's':
							return padding(value.getSeconds(), $0.length);
						case 'q':
							return Math.floor((this.getMonth() + 3) / 3);
						default:
							return '';
						}
					});
				} catch (err) {
					return '';
				}
			};
		}(),
		uuid : function() {
			var d = __.now();
			return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
				var r = (d + Math.random() * 16) % 16 | 0;
				d = Math.floor(d / 16);
				return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
			});
		},
		trim : function(text) {
			return text == null ? '' : (text + '').replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');
		},
		top : function(selector, shift) {
			var opener = window, stack = [], pop = shift === true ? 'shift' : 'pop';
			while (opener != opener.parent) {
				stack.push(opener = opener.parent);
			}
			for (; stack.length;) {
				opener = stack[pop]();
				try {
					// 在没权限访问parent中的对象时会出错
					opener.document.getElementsByTagName('*');
					if (opener.__ && (!__.isFunction(selector) || selector(opener) === true)) {
						return opener.__;
					}
				} catch (ex) {
				}
			}
			return __;
		},
		onMessage : function(callback) {
			window.addEventListener('message', function(e) {
				var data = e.data, action = data.action || '', params = data.params;
				callback(action, params)
			});
		},
		postMessage : function(action, params, scope) {
			if (__.isWindow(params)) {
				scope = params;
				params = null;
			}
			(__.isWindow(scope) ? scope : parent).postMessage({
				action : action,
				params : params
			}, '*');
		},
		download : (function() {
			return function(url, params) {
				var oIframe = document.createElement('iframe'), oForm = document.createElement('form'), html = '';
				oIframe.style.display = 'none';
				oIframe.id = oIframe.name = 'download-' + __.uuid();
				oForm.style.display = 'none';
				oForm.method = 'post';
				oForm.action = url;
				oForm.target = oIframe.id;
				document.body.appendChild(oIframe);
				document.body.appendChild(oForm);
				if (params) {
					for ( var n in params) {
						var v = params[n];
						if (!!v) {
							if (__.isPlainObject(v)) {
								v = __.encode(v);
							} else if (__.isArray(v)) {
								v = __.encode(v);
							}
						}
						html += '<input name="' + __.encodeHTML(n) + '" value="' + __.encodeHTML(v) + '" type="hidden" /> '
					}
				}
				oForm.innerHTML = html;
				oForm.submit();
				setTimeout(function() {
					document.body.removeChild(oForm);
					document.body.removeChild(oIframe);
					oForm = oIframe = null;
				}, 10 * 1000);
			};
		})(),
		$vm : null
	});
})();

(function() {
	var vmLoading = false, maskHash = {}, MyDialog = Vue.extend({
		template : [ //
		'<div is="el-dialog" ',//
		' :title="title" ',//
		' :visible.sync="visible" ',//
		' :width="width" ',//
		' :close-on-click-modal="false" ',//
		' @close="handleClole" ',//
		' @closed="handleCloled" ',//
		'>',//
		' <iframe :name="uuid" :src="url" :height="height" width="100%" style="border:none;"></iframe>',//
		'</div>' ].join(''),
		data : function() {
			return {
				visible : false,
				result : null
			}
		},
		methods : {
			handleClole : function() {
				__.dialog.store[this.uuid].close(this.result);
			},
			handleCloled : function() {
				var uuid = this.uuid;
				__.defer(function() {
					__.dialog.store[uuid] && __.dialog.store[uuid].$destroy();
				}, 1000);
			}
		}
	}), scanDialog = function(uuid) {
		var opener = window, dialog;
		(!uuid || uuid === '_self') && (uuid = window.name);
		while (opener != null) {
			if (opener.__ && opener.__.dialog && (dialog = opener.__.dialog.store[uuid])) {
				return dialog;
			}
			opener = (opener == opener.parent) ? null : opener.parent;
		}
		return null;
	};

	__.extend(__, {
		$vm : null,
		vue : function() {
			return __.$vm || (__.$vm = new Vue());
		},
		mask : function(id) {
			maskHash[id + ''] = true;
			!__.$vm && (__.$vm = new Vue());
			!vmLoading && (vmLoading = __.$vm.$loading({
				lock : true,
				text : 'Loading',
				spinner : 'el-icon-loading',
				background : 'rgba(0, 0, 0, 0.1)'
			}));
		},
		unmask : function(id) {
			delete maskHash[id + ''];
			if (__.isEmptyObject(maskHash)) {
				try {
					vmLoading.close();
				} catch (e) {
				} finally {
					vmLoading = false;
				}
			}
		},
		dialog : {
			store : {},
			open : function(options) {
				var _opener_ = __.top();
				if (_opener_ != __) {
					return _opener_.dialog.open(options);
				}
				options = options || {};
				var uuid = __.uuid(), container = document.getElementById('#vm') || document.body;
				var dialog = __.dialog.store[uuid] = new MyDialog({
					data : {
						uuid : uuid,
						title : options.title || '#',
						url : options.url || 'about:blank',
						width : options.width || '50%',
						height : options.height || '200px',
						close : options.close || __.noop
					}
				}).$mount();
				container.appendChild(dialog.$el);
				__.defer(function() {
					dialog.visible = true;
				});
				return uuid;
			},
			close : function(result, uuid) {
				var dialog = scanDialog(uuid);
				dialog && __.extend(dialog, {
					visible : false,
					result : result
				});
			},
			resize : function(options) {
				var dialog = scanDialog('_self');
				dialog && ((options.width && (dialog.width = options.width)) | (options.height && (dialog.height = options.height)))
			}
		},
		// alert(message, title,{callback:fn1}) || alert(message, title).then(fn1,fn2)
		alert : function(message, title, options) {
			var _opener_ = __.top();
			return _opener_ != __// 
			? _opener_.alert.apply(this, arguments) //
			: __.vue().$alert(message, title || '提示', options || {});
		},
		// confirm(message, title).then(fn1,fn2)
		confirm : function(message, title) {
			var _opener_ = __.top();
			return _opener_ != __// 
			? _opener_.confirm.apply(this, arguments)//
			: __.vue().$confirm(message, title);
		}
	});
})();
