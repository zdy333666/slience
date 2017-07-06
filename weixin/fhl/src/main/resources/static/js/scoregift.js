var url_base = window.location.protocol + "//" + window.location.host;
var port = window.location.port;
if (port !== '') {
    url_base = url_base + ":" + port;
}

var search = window.location.search;
search = search.substring(1);
//var openId = search.split("=")[1];
var openId = search.split("&")[0].split("=")[1];
var isBind = search.split("&")[1].split("=")[1];

function redyExchange(s) {
    if (0 == isBind) {
        if (confirm('您还未注册，现在是否去注册？')) {
            window.location.href = url_base + "/bindpage.html?openId=" + openId;
        }
        return;
    }

    var id = $(s).attr('data');
    var name = $('#' + id + ' .title').text();
    var price = $('#' + id).attr('data-price');
    var unitprice = $('#' + id).attr('data-unitprice');

    $(".change-score #id").attr('data', id);
    $(".change-score #name").attr('data', name);
    $(".change-score #price").attr('data', price);
    $(".change-score #unitprice").attr('data', unitprice);
    //$(".change-score #exchange").attr('data',id);

    $(".change-score #name").text(name);
    $(".change-score #price").text('0元');
    $(".change-score #unitprice").text(unitprice + '积分');
    $(".change-score #unitprice-need").text(unitprice + '积分');

    var $height = $(document).height();
    $("div.mask").height($height).show();
    $(".change-score").show();
}

function exchange(s) {

    var id = $(".change-score #id").attr('data');
    var unitprice = $(".change-score #unitprice").attr('data');

    var url = url_base + "/integral/exchange?openId=" + openId + '&productId=' + id + '&unitprice=' + unitprice;
    var request = $.ajax({
        url: url,
        method: "GET",
        dataType: 'text'
    });
    request.done(function (msg) {
        $("div.mask").hide();
        $(".change-score").hide();

        if (-1 == msg) {
            alert('您的积分不足');
            return;
        }

        if (1 == msg) {
            alert('恭喜您兑换成功');
            window.location.reload();
            return;
        }
        if (0 == msg) {
            alert('很抱歉，本次兑换没有成功，请您稍后再试');
        }

    });
    request.fail(function (jqXHR, textStatus) {
        alert("Request failed: " + textStatus);
    });
}

//跳转商品详情页面
function viewDetail(s) {
    var id = $(s).attr('data');
    var url = url_base + '/goodsdetail.html?openId=' + openId + '&id=' + id;
    window.location.href = url;
}


$(document).ready(function () {

    var page = 1;
    // 每页展示5个
    var size = 5;
    var count = 0;
    var lock = false;

    // dropload
    $('.scoregift-main-content').dropload({
        scrollArea: window,
        loadDownFn: function (me) {

            if (lock) {
                return;
            }
            lock = true;

            var url = url_base + "/integral/goods?openId=" + openId + "&page=" + page + "&size=" + size;
            var request = $.ajax({
                url: url,
                method: "GET",
                dataType: 'json'
            });
            request.done(function (data) {
                if (0 == data.count) {
                    // 锁定
                    me.lock();
                    // 无数据
                    me.noData();

                    lock = false;
                    $("#goodsList").html("");
                    $(".dropload-load").html('<span class="loading"></span>');

                    alert("尚无在售商品");
                    return;
                }

                page++;
                var result = '';
                var rows = data.rows;
                for (var i in rows) {
                    var row = rows[i];

                    var title = row.name;
                    if (null === title) {
                        title = "";
                    }
                    if (title.length > 27) {
                        title = title.substr(0, 27) + '...'
                    }

                    var pic = row.pic;
                    if (null === pic || "" === pic) {
                        pic = "img/score.png";
                    }

                    var itemHtml = '<li class="clearfix" id=' + row.id + ' data-price=' + row.price + ' data-unitprice=' + row.unitprice + '>';
                    itemHtml += '<div class="fl"><img src=' + pic + ' alt="" data=' + row.id + ' onclick="viewDetail(this)"></div>';
                    itemHtml += '<div class="fr">';
                    itemHtml += '<p class="title">' + title + '</p>';
                    itemHtml += '<p class="subtitle"></p>';
                    itemHtml += '<p class="clearfix">';
                    itemHtml += '<span class="fl">' + row.unitprice + '积分</span>';
                    itemHtml += '<button class="fr" onclick="redyExchange(this)" data=' + row.id + '>立即兑换</button>';
                    itemHtml += '</p>';
                    itemHtml += '</div>';
                    itemHtml += '</li>';

                    result += itemHtml;

                    count++;
                    if (count >= data.count) {
                        // 锁定
                        me.lock();
                        // 无数据
                        me.noData();
                        break;
                    }
                }
                // 为了测试，延迟1秒加载
                setTimeout(function () {
                    $("#goodsList").append(result);
                    // 每次数据加载完，必须重置
                    me.resetload();
                }, 1000);
                lock = false;
            });
            request.fail(function (jqXHR, textStatus) {
                //alert("Request failed: " + textStatus);
                me.resetload();
                lock = false;
                alert('出错了');
            });
        }
    });

});