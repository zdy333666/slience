var search = window.location.search;
search = search.substring(1);

var openId = search.split("&")[0].split("=")[1];
var windowclose = search.split("&")[1].split("=")[1];

$('#storeopenid').attr("value", openId);
$('#windowclose').attr("value", windowclose);
//----------------------------------------------
var pInterval;
var tInterval; //小画面的计时器

//画面初始化 开通微信权限
$.post(
        "/WeixinServer/apiRoll?" + new Date().getTime(),
        {
            apiRoll: window.location.href		//权限验证用地址
        },
        function (data, status) {
            var obj = jQuery.parseJSON(data);
            wx.config({
                debug: false,
                appId: obj.appId,
                timestamp: obj.timestamp,
                nonceStr: obj.nonceStr,
                signature: obj.signature,
                jsApiList: ['scanQRCode']
            });

            wx.error(function (res) {
                //alert("系统异常");
            });

            wx.ready(function () {
                //缓存清空
                sessionStorage.scanRsValue = "";
                sessionStorage.userscore = "";
                sessionStorage.citizenId = "";
                //如果不是收银员，关闭窗口
                //if ($('#windowclose').attr("value") == "0") {
                if (0 == windowclose) {
                    alert('您还未绑定收银员角色，不能进行此项操作');
                    closeWindow();
                }
            });

        });

//显示扫码成功模板
function showSuccess() {
    var allGroup = $(".fail");
    for (var i = 0; i < allGroup.length; i++) {
        allGroup[i].style.display = "none";
    }
    var allGroup = $(".success");
    for (var i = 0; i < allGroup.length; i++) {
        allGroup[i].style.display = "";
    }

    //其他
    $("#payScore").removeAttr("disabled");
    $("#immbutton").removeAttr("disabled");

}

//显示扫码失败模板
function showFail() {
    var allGroup = $(".success");
    for (var i = 0; i < allGroup.length; i++) {
        allGroup[i].style.display = "none";
    }
    var allGroup = $(".fail");
    for (var i = 0; i < allGroup.length; i++) {
        allGroup[i].style.display = "";
    }
}

//显示扫码失败模板
function showFail() {
    var allGroup = $(".success");
    for (var i = 0; i < allGroup.length; i++) {
        allGroup[i].style.display = "none";
    }
    var allGroup = $(".fail");
    for (var i = 0; i < allGroup.length; i++) {
        allGroup[i].style.display = "";
    }
}

//隐藏小画面
function hiddleAll(modal) {
    //小画面过来的会隐藏
    if (modal != "") {
        $('#' + modal).modal('hide');
    }
}


//点击扫描按钮，扫描二维码并返回结果
function scanFunction(modal) {
    //缓存清空
    //alert(1);
    sessionStorage.scanRsValue = "";
    sessionStorage.userscore = "";
    sessionStorage.citizenId = "";
    wx.scanQRCode({
        needResult: 1,
        desc: 'scanQRCode desc',
        success: function (res) {
            hiddleAll(modal);
            var obj = jQuery.parseJSON(JSON.stringify(res));
            //json解析
            var patt = new RegExp("LYZH[0-9]{12}");

            if (obj.resultStr.length == 16 && patt.test(obj.resultStr)) {
                getUserInfo(obj.resultStr);
            } else {
                showFail();
            }
        }
    });
}
;

//获得业主信息
function getUserInfo(scanRsValue) {
    $.post(
            "/pay/getUserInfo?" + new Date().getTime(),
            {
                scanRsValue: scanRsValue  	//业主信息
            },
            function (obj, status) {
                //alert(data.code);
                //var obj = jQuery.parseJSON(data);
                if (obj.code == "ok") {

                    //标题
                    $("#title").html("扫码成功!");
                    $("#userscore").html(obj.userinfo.userscoreShow);
                    //信息部分payScore
                    $("#username").html(obj.userinfo.username);
                    $("#useraddr").html(obj.userinfo.useraddr);
                    $("#usertel").html(obj.userinfo.usertel);
                    //输入积分
                    $("#payScore").attr("value", "");
                    //隐藏部分
                    sessionStorage.citizenId = obj.userinfo.citizenId;	//业主的id
                    sessionStorage.userscore = obj.userinfo.userscore; 	//业主的分数 需要清空
                    sessionStorage.scanRsValue = scanRsValue;	//扫码结果 扫码前需要清空 OK
                    showSuccess();
                } else {
                    showFail();
                }
            });
}

//全部兑换，简单的值拷贝
function copyAllScore() {
    $("#payScore").attr("value", sessionStorage.userscore);
}

//显示兑换确认画面 
function immFunction() {
    //如果画面内容不对，不能进行跳转
    //没有有效用户，或者输入内容不对
    if (sessionStorage.scanRsValue == "") {
        alert("请先扫描")
        return;
    }

    if ($("#payScore").val() == "" || $("#payScore").val() < 10) {
        alert("请输入有效兑换积分")
        return;
    }

    if (Number($("#payScore").val()) > Number(sessionStorage.userscore)) {
        alert("输入数值不能超过总积分数")
        return;
    }

    if (Number($("#payScore").val()) % 10 != 0) {
        alert("个位数积分不予兑换,自动补零处理，请确认")
        $("#payScore").attr("value", parseInt(Number($("#payScore").val()) / 10) * 10);
    }

    //清除缓存
    sessionStorage.scanRsValue = "";
    sessionStorage.userscore = "";
    //2，显示小画面
    $('#myModal1').modal({backdrop: 'static'});
    //加载完毕后，触发loading动画效果
    $('#myModal1').on('shown.bs.modal', function (e) {
        $("#con_username").html($("#username").html());
        $("#con_useraddr").html($("#useraddr").html());
        $("#con_usertel").html($("#usertel").html());
        $("#con_userpayscore").html($("#payScore").val());
    });
}

//确认兑换
function confirmFunction(modal) {

    //可能要加个处理，防止重复提交 TODO
    //进行兑换操作，并根据结果进行跳转
//    $.ajax({
//        type: "POST",
//        url: "/paynow?" + new Date().getTime(),
//        async: false,
//        data: {
//            citizenId: sessionStorage.citizenId, //业主信息
//            payScore: Number($("#con_userpayscore").html()), //消费积分
//            storeopenid: $('#storeopenid').attr("value")		//收银员信息
//
//        },
//        dataType: "json",
//        success: function (data) {
    $.post(
            "/paynow?" + new Date().getTime(),
            {
                citizenId: sessionStorage.citizenId, //业主信息
                payScore: Number($("#con_userpayscore").html()), //消费积分
                storeopenid: $('#storeopenid').attr("value")		//收银员信息
            },
            function (obj, status) {

                $('#' + modal).modal('hide');
                $('#' + modal).on('hidden.bs.modal', function (e) {
                    //var obj = jQuery.parseJSON(data);
                    if (obj.code == "ok") {
                        //显示成功小画面
                        $('#myModal2').modal({backdrop: 'static'});
                        //加载完毕后，触发loading动画效果
                        $('#myModal2').on('shown.bs.modal', function (e) {
                            //更新画面上的消费积分和剩余积分
                            $("#usedScore").html(obj.usedScore);
                            $("#lastScore").html(obj.lastScore);
                        })
                    } else {
                        //显示失败小画面
                        $('#myModal3').modal({backdrop: 'static'});
                    }
                })

            }
    )
    //});
}

function closeWindow() {
    WeixinJSBridge.call('closeWindow');
}
