var url_base = window.location.protocol + "//" + window.location.host;
var port = window.location.port;
if (port !== '') {
    url_base = url_base + ":" + port;
}

var search = window.location.search;
search = search.substring(1);
var openId = search.split("=")[1];

//----------------------------------------------

//手机号码检验
function checkPhone(phone) {
    return (/^1(3|4|5|7|8)\d{9}$/.test(phone));
}

//获取验证码
var wait = 60;
var send = true;
function getCaptcha(o) {

    if (0 == wait) {
        o.removeAttribute("disabled");
        o.value = "获取验证码";

        wait = 60;
        send = true;

    } else {
        if (send) {
            var phoneno = $("#phoneno").val();
            if (!checkPhone(phoneno)) {
                alert("请输入有效的手机号码");
                return;
            }

            var url = url_base + "/captcha/phone?phoneno=" + phoneno;
            var request = $.ajax({
                url: url,
                method: "GET",
                dataType: 'text'
            });
            request.done(function (msg) {
                $("#captcha_temp").val(msg);
                //$("#captcha").val(msg);
            });
            request.fail(function (jqXHR, textStatus) {
                //alert("Request failed: " + textStatus);
                alert('出错了');
            });
        }
        send = false;

        o.setAttribute("disabled", true);
        o.value = "重新发送(" + wait + ")";
        wait--;
        setTimeout(function () {
            getCaptcha(o);
        }, 1000);
    }
}


$(document).ready(function () {

    //手机号码输入限制
    $("#phoneno").keyup(function () {
        var phone_v = $(this).val();
        if (phone_v.length >= 11) {
            $(this).val(phone_v.substr(0, 11));
        }
        $(this).val($(this).val().replace(/[^0-9.]/g, ''));
    });


    //用户绑定事件
    $(".bind").click(function () {

        var phoneno = $("#phoneno").val();//.attr("value");
        if (!checkPhone(phoneno)) {
            alert("请输入有效的手机号码");
            return;
        }

        var captcha_temp = $("#captcha_temp").val();//.attr("value");
        var captcha = $("#captcha").val();//.attr("value");
        if (captcha_temp.trim().length === 0 || captcha.trim().length === 0 || captcha_temp !== captcha) {
            alert("验证码错误");
            return;
        }

        var region = $("#city .city").val();//.attr("value");

        var url = url_base + "/user-bind/add?openId=" + openId + "&region=" + region + "&phoneno=" + phoneno;
        var request = $.ajax({
            url: url,
            method: "GET",
            //data: JSON.stringify(input),
            dataType: 'json'
        });
        request.done(function (msg) {
            if (msg === 1) {
                WeixinJSBridge.call('closeWindow');
            } else if (msg === -1) {
                alert("该手机号码已被注册");
            } else {
                alert("注册失败");
            }
        });

        request.fail(function (jqXHR, textStatus) {
            alert("Request failed: " + textStatus);
        });
    });

////------------------------------------
//    var wait = 0;
//    //获取验证码
//    $("#get-captcha").click(function () {
//        var o = $("#get-captcha");
//
//        if (wait == 0) {
//            //o.removeAttribute("disabled");
//            alert(11);
//            o.value = "获取验证码";
//            wait = 60;
//
//
//            var phoneno = $("#phoneno").attr("value");
//            if (!checkPhone(phoneno)) {
//                alert("手机号码无效");
//                return;
//            }
//            var url = url_base + "/captcha/phone?phoneno=" + phoneno;
//            //alert(url);
//            //var input = {username: "Tom", phoneno: "13567179365"};
//            var request = $.ajax({
//                url: url,
//                method: "GET",
//                //data: JSON.stringify(input),
//                dataType: 'json'
//            });
//            request.done(function (msg) {
//                $("#captcha_temp").val(msg);
//                //$("#captcha").val(msg);
//            });
//            request.fail(function (jqXHR, textStatus) {
//                alert("Request failed: " + textStatus);
//            });
//
//        } else {
//            o.setAttribute("disabled", "disabled");
//            o.value = "重新发送(" + wait + ")";
//            wait--;
//            setTimeout(function () {
//                time(o)
//            },
//                    1000)
//        }
//    });


});


