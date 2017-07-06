var url_base = window.location.protocol + "//" + window.location.host;
var port = window.location.port;
if (port !== '') {
    url_base = url_base + ":" + port;
}

var search = window.location.search;
search = search.substring(1);
var openId = search.split("=")[1];


function optPhone(s) {
    var phoneno = $(s).attr('data');
    var url = "phonenumber.html?openId=" + openId + '&phoneno=' + phoneno;
    window.location.href = url;
}

function optExchange(s) {
    var url = "myexchange.html?openId=" + openId;
    window.location.href = url;
}


function optGroup(s) {
    var url = "scoregroup.html?openId=" + openId;
    window.location.href = url;
}

$(document).ready(function () {

    var url = url_base + "/person-center/overview?openId=" + openId;
    var request = $.ajax({
        url: url,
        method: "GET",
        //data: JSON.stringify(input),
        dataType: 'json'
    });
    request.done(function (msg) {

        var integral = msg.integral;
        var integralHtml = '<li>' +
                '<p class="score-num">' + integral.curr + '</p>' +
                '<p class="score-project">可兑换积分</p>' +
                '</li>' +
                '<li>' +
                '<p class="score-num">' + integral.total + '</p>' +
                '<p class="score-project">账户总积分</p>' +
                '</li>' +
                '<li>' +
                '<p class="score-num">' + integral.pick + '</p>' +
                '<p class="score-project">巡检积分</p>' +
                '</li>' +
                '<li>' +
                '<p class="score-num">' + integral.recycle + '</p>' +
                '<p class="score-project">可回收积分</p>' +
                '</li>' +
                '<li>' +
                '<p class="score-num">' + integral.put + '</p>' +
                '<p class="score-project">投放奖励积分</p>' +
                '</li>' +
                '<li>' +
                '<p class="score-num">' + integral.activity + '</p>' +
                '<p class="score-project">活动积分</p>' +
                '</li>';

        $("#headPic").attr('src', msg.headPic);
        $(".user-img p span").text(msg.username);
        $(".score-list ul").html(integralHtml);
        $("#erweima").attr('src', msg.qrPic);
        $(".customer-info #phone p.fr").html(msg.phoneno + '<span><img src="img/to_right.png" alt=""></span>');
        $(".customer-info #role p.fr").html(msg.role + '<span></span>');


        $(".customer-info #phone").attr('data', msg.phoneno);
        $("#main").show();
    });

    request.fail(function (jqXHR, textStatus) {
        alert("Request failed: " + textStatus);
    });

});
