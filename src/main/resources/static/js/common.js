/* 通过对象创建带参数的请求URL */
function createUrl(obj) {
	var length = obj && obj.length,
	idx = 0,
	url = obj.url + '?';
	for (var key in obj) {
		if (key != 'url' && obj[key] !== null) {
			url += (key + '=' + encodeURIComponent(obj[key]) + '&');
		}
	}
	return url.substring(0, url.lastIndexOf('&'));
}

//layui表单验证
window.ValidFormComponent = {
  layerFilter: {
    positiveInteger: function (value, item) { // 正整数
      if (value < 0)
        return '必须为正整数';
      if (value.indexOf('.') !== -1)
        return '必须为正整数';
    },
    cardNo: function (value, item) { // 身份证号码
      var reg = /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
      if (!reg.test(value)) {
        return '身份证号码格式不正确';
      }
    },
    positive: function (value, item) {
      if (value < 0)
        return '必须为正数';
    },
    integer: function (value, item) {
      if (value.indexOf('.') !== -1)
        return '必须为整数';
    }

  }
};

function formatMoney(number, places, symbol, thousand, decimal) {
  number = number || 0;
  places = !isNaN(places = Math.abs(places)) ? places : 2;
  symbol = symbol !== undefined ? symbol : "";
  thousand = thousand || ",";
  decimal = decimal || ".";
  var negative = number < 0 ? "-" : "",
      i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
      j = (j = i.length) > 3 ? j % 3 : 0;
  return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
}