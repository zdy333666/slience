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
var groupId = search.split("&")[1].split("=")[1];
//var name = search.split("&")[2].split("=")[1];

//初始化页面
function init() {


    var url = url_base + "/group/listMember?openId=" + openId + '&groupId=' + groupId;
    var request = $.ajax({
        url: url,
        method: "GET",
        dataType: 'json'
    });
    request.done(function (data) {
        if (null === data || 0 === data.length) {
            alert("尚无群组成员信息");
            return;
        }

        var rows = data.rows;

        $(document).attr("title", data.name);
        $("#memberCount").text(rows.length);
        $("#scoreCount").text(data.score);
        $("#kitchenQR").attr('src', data.kitchenQR);
        $("#otherQR").attr('src', data.otherQR);


        var allHtml = "";

        for (var i in rows) {
            var row = rows[i];

            var itemHtml = '<li class="clearfix">';
            itemHtml += '<p class="fl">';
            itemHtml += '<span class="title">' + row.phoneno + '</span>';
            itemHtml += '<span class="admin">' + (row.asAdmin ? '管理员' : '成员') + '</span>';
            itemHtml += '</p>';
//            itemHtml += '<p class="fl center">';
//            itemHtml += '<span class="title">' + row.score + '</span>';
//            itemHtml += '<span>积分</span>';
//            itemHtml += '</p>';
            itemHtml += ' <p class="fr">';
            itemHtml += '<a href="#"><img src="img/close.png" alt="" data=' + row.id + ' onclick=readyDeleteMember(this)></a>';
            itemHtml += '</p>';
            itemHtml += '</li>';

            allHtml += itemHtml;
        }

        $("#memberList").html(allHtml);
        $("#group-main").show();
    });

    request.fail(function (jqXHR, textStatus) {
        alert("出错了");
        //alert("Request failed: " + textStatus);
    });

}

var toDeleteId = null;
function readyDeleteMember(s) {
    toDeleteId = $(s).attr('data');
    var $height = $(document).height();
    $("div.mask").height($height).show();
    $(".delete-member").show();
}

//移除群组成员
function confirmDelete(s) {
    $("div.mask").hide();
    $(".delete-member").hide();
    if (!s) {
        return;
    }

    var url = url_base + "/group/deleteMember?openId=" + openId + '&groupId=' + groupId + '&id=' + toDeleteId;
    toDeleteId = null;
    var request = $.ajax({
        url: url,
        method: "GET",
        dataType: 'text'
    });
    request.done(function (data) {

        if (-1 == data) {
            alert("很抱歉，您没有足够的权限");
            return;
        }

        if (0 == data) {
            alert("操作失败");
            return;
        }

        if (1 == data) {
            //window.location.reload();
            init();
        }
    });

    request.fail(function (jqXHR, textStatus) {
        alert("Request failed: " + textStatus);
    });
}

//通过手机号码查找群组成员
function searchByPhoneno() {
    var phoneno = $("#searchPhone").val();
    if (!checkPhone(phoneno)) {
        alert("请输入有效的手机号码");
        return;
    }

    $("#memberList").html("");

    var url = url_base + "/group/searchMember?openId=" + openId + '&groupId=' + groupId + '&phoneno=' + phoneno;
    var request = $.ajax({
        url: url,
        method: "GET",
        dataType: 'json'
    });
    request.done(function (msg) {
        if (0 == msg.code) {
            alert("未匹配到任何成员信息");
            return;
        }

        var row = msg.data;

        var itemHtml = '<li class="clearfix">';
        itemHtml += '<p class="fl">';
        itemHtml += '<span class="title">' + row.phoneno + '</span>';
        itemHtml += '<span class="admin">' + (row.asAdmin ? '管理员' : '成员') + '</span>';
        itemHtml += '</p>';
//            itemHtml += '<p class="fl center">';
//            itemHtml += '<span class="title">' + row.score + '</span>';
//            itemHtml += '<span>积分</span>';
//            itemHtml += '</p>';
        itemHtml += ' <p class="fr">';
        itemHtml += '<a href="#"><img src="img/close.png" alt="" data=' + row.id + ' onclick=readyDeleteMember(this)></a>';
        itemHtml += '</p>';
        itemHtml += '</li>';

        $("#memberList").html(itemHtml);
    });

    request.fail(function (jqXHR, textStatus) {
        alert("出错了");
    });
}

//扫描个人中心二维码，获取用户手机号码
function scanFunction() {
    //点击扫描按钮，扫描二维码并返回结果
    wx.scanQRCode({
        needResult: 1,
        desc: 'scanQRCode desc',
        success: function (res) {
            var obj = jQuery.parseJSON(JSON.stringify(res));
            addMember(obj.resultStr);
        }
    });
}

//添加群组用户
function addMember(phoneno) {

    var url = url_base + "/group/addMember?openId=" + openId + '&groupId=' + groupId + '&phoneno=' + phoneno;
    var request = $.ajax({
        url: url,
        method: "GET",
        dataType: 'text'
    });
    request.done(function (data) {

        if (-1 == data) {
            alert("很抱歉，您没有足够的权限");
            return;
        }

        if (0 == data) {
            alert("操作失败");
            return;
        }

        if (1 == data) {
            //window.location.reload();
            alert("添加成功");
            init();
        }
    });

    request.fail(function (jqXHR, textStatus) {
        alert("出错了");
        //alert("Request failed: " + textStatus);
    });
}

$(document).ready(function () {

    init();

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
                    alert(data);
                });

                wx.ready(function () {
                });
            });


    //手机号码输入限制
    $("#searchPhone").keyup(function () {
        var phone_v = $(this).val();
        if (phone_v.length >= 11) {
            $(this).val(phone_v.substr(0, 11));
        }
        $(this).val($(this).val().replace(/[^0-9.]/g, ''));
    });
});
