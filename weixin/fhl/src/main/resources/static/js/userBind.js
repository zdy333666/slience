//手机号码检验
function checkPhone(phone) {
    return (/^1(3|4|5|7|8)\d{9}$/.test(phone));
}

$(document).ready(function () {

    var url_base = window.location.protocol + "//" + window.location.host;
    var port = window.location.port;
    if (port !== '') {
        url_base = url_base + ":" + port;
    }

    //用户绑定事件
    $(".bind").click(function () {

        var search = window.location.search;
        search = search.substring(1);
        var openId = search.split("=")[1];

        var username = $("#username").attr("value");
        if (username.trim().length === 0) {
            alert("用户名无效");
            return;
        }
        var phoneno = $("#phoneno").attr("value");
        if (!checkPhone(phoneno)) {
            alert("手机号码无效");
            return;
        }

        var captcha_temp = $("#captcha_temp").attr("value");
        var captcha = $("#captcha").attr("value");
        if (captcha_temp.trim().length === 0 || captcha.trim().length === 0 || captcha_temp !== captcha) {
            alert("验证码错误");
            return;
        }

        var url = url_base + "/user-bind/add?openId=" + openId + "&username=" + username + "&phoneno=" + phoneno;
        //alert(url);
        //var input = {username: "Tom", phoneno: "13567179365"};
        var request = $.ajax({
            url: url,
            method: "GET",
            //data: JSON.stringify(input),
            dataType: 'json'
        });
        request.done(function (msg) {
            if (msg === true) {
                WeixinJSBridge.call('closeWindow');
            } else {
                alert("注册失败");
            }
        });

        request.fail(function (jqXHR, textStatus) {
            alert("Request failed: " + textStatus);
        });
    });

//------------------------------------

    $("#get-captcha").click(function () {

        var phoneno = $("#phoneno").attr("value");
        var url = url_base + "/captcha/phone?phoneno=" + phoneno;
        //alert(url);
        //var input = {username: "Tom", phoneno: "13567179365"};
        var request = $.ajax({
            url: url,
            method: "GET",
            //data: JSON.stringify(input),
            dataType: 'json'
        });
        request.done(function (msg) {
            $("#captcha_temp").val(msg);
            $("#captcha").val(msg);

        });

        request.fail(function (jqXHR, textStatus) {
            alert("Request failed: " + textStatus);
        });
    });


});


