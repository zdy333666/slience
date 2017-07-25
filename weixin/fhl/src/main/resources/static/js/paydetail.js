var url_base = window.location.protocol + "//" + window.location.host;
var port = window.location.port;
if (port !== '') {
    url_base = url_base + ":" + port;
}

var search = window.location.search;
search = search.substring(1);
//var openId = search.split("=")[1];

var isBind = search.split("&")[0].split("=")[1];
var openId = search.split("&")[1].split("=")[1];
var terminalNo = search.split("&")[2].split("=")[1];
var goodsId = search.split("&")[3].split("=")[1];
var goodsName = search.split("&")[4].split("=")[1];
var score = search.split("&")[5].split("=")[1];
var price = search.split("&")[6].split("=")[1];
var userName = search.split("&")[7].split("=")[1];
var phoneNo = search.split("&")[8].split("=")[1];

var date;


function nopay() {
    WeixinJSBridge.call('closeWindow');
}

function pay() {

    var tradeTime = date.getTime();

    var url = url_base
            + "/exchangeMachinePay/pay?openId=" + openId
            + "&terminalNo=" + terminalNo
            + "&goodsId=" + goodsId
            + "&score=" + score
            + "&tradeTime=" + tradeTime;
    var request = $.ajax({
        url: url,
        method: "GET",
        //data: JSON.stringify(input),
        dataType: 'json'
    });
    request.done(function (msg) {
        //msg = -1;
        if (msg === 1) {
            $(".mask_paydetail").hide();
            $(".paydetail").hide();

            window.location.href = url_base + "/paysuccess.html?score=" + score;
        } else if (-1 == msg) {
            window.location.href = url_base + "/payfailure.html";
        } else if (-2 == msg) {
            window.location.href = url_base + "/shipmentfailure.html";
        }
    });

    request.fail(function (jqXHR, textStatus) {
        alert("Request failed: " + textStatus);
        alert('出错了');
    });

}

$(document).ready(function () {

    if (0 == isBind) {
        var b = confirm('您还未注册，现在是否去注册？');
        if (b) {
            window.location.href = url_base + "/bindpage.html?openId=" + openId;
        } else {
            WeixinJSBridge.call('closeWindow');
        }
    }


    date = (new Date());


    $("#goodsName").text(decodeURI(goodsName));
    $("#score").text(score);
    $("#payScore").text(score);
    $("#terminalNo").text(terminalNo);
    $("#tradeTime").text(date.Format("yyyy-MM-dd hh:mm:ss"));
    $("#price").text(price);
    $("#userName").text(decodeURI(userName));
    $("#phoneNo").text(phoneNo);

    $("#paydetail-main").show();

});