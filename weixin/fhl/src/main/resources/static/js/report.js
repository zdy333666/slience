//手机号码检验
function checkPhone(phone) {
    return (/^1(3|4|5|7|8)\d{9}$/.test(phone));
}

var url_base = window.location.protocol + "//" + window.location.host;
var port = window.location.port;
if (port !== '') {
    url_base = url_base + ":" + port;
}

var search = window.location.search;
search = search.substring(1);
var openId = search.split("&")[0].split("=")[1];
var userId = search.split("&")[1].split("=")[1];


function report() {

    var title = $("#title").val();
    if (title.trim().length === 0) {
        alert("请输入标题");
        return;
    }

    var content = $("#content").val();
    if (content.trim().length === 0) {
        alert("请输入描述");
        return;
    }


    var param = 'openId=' + openId;
    param += '&userId=' + userId;
    param += '&title=' + title;
    param += '&content=' + content;

    var url = url_base + "/report-feedback/upload?" + param;
    var request = $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json'
    });
    request.done(function (msg) {
        if (msg) {
            WeixinJSBridge.call('closeWindow');
        } else {
            alert("举报失败");
        }
    });

    request.fail(function (jqXHR, textStatus) {
        //alert("Request failed: " + textStatus);
        alert('出错了');
    });

}


// 检查字符串字数
function WidthCheck(str, maxLen) {
    var w = 0;
//length 获取字数数，不区分汉子和英文
    for (var i = 0; i < str.value.length; i++) {
//charCodeAt()获取字符串中某一个字符的编码
        var c = str.value.charCodeAt(i);
//单字节加1
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
            // 英文字符和数字
            w++;
        } else {
            // 中文字符
            w += 1;
        }
        if (w > maxLen) {
            str.value = str.value.substr(0, i);
            break;
        }
    }
}

$(document).ready(function () {

});

