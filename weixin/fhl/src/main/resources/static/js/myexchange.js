var url_base = window.location.protocol + "//" + window.location.host;
var port = window.location.port;
if (port !== '') {
    url_base = url_base + ":" + port;
}

var search = window.location.search;
search = search.substring(1);
var openId = search.split("=")[1];

var $height = $(document).height();

function readyMakeExchangeUsed(s) {
    var id = $(s).attr('data-id');
    var productId = $(s).attr('data-productId');

    $("#makeExchangeUsed").attr('data-id', id);
    $("#makeExchangeUsed").attr('data-productId', productId);

    $("div.mask_myexchange").height($height).show();
    $(".myexchange").show();
}

function makeExchangeUsed(s) {

    var id = $("#makeExchangeUsed").attr('data-id');
    var productId = $("#makeExchangeUsed").attr('data-productId');
    var code = $("#code").val();

    if (code.trim().length === 0) {
        alert("请输入确认代码");
        return;
    }

    var url = url_base + "/integral/makeExchangeUsed?openId=" + openId + '&id=' + id + '&productId=' + productId + '&code=' + code;
    var request = $.ajax({
        url: url,
        method: "GET",
        dataType: 'json'
    });
    request.done(function (msg) {
        if (msg) {
            $("div.mask_myexchange").hide();
            $(".myexchange").hide();
            alert('代码确认成功');
            window.location.reload();
        } else {
            alert('代码确认失败');
        }
    });
    request.fail(function (jqXHR, textStatus) {
        alert("Request failed: " + textStatus);
    });

}

$(document).ready(function () {

    $("div.mask_myexchange").on("touchstart", function () {
        $("div.mask_myexchange").hide();
        $(".myexchange").hide();
    });

    var page = 1;
    // 每页展示5个
    var size = 5;
    var count = 0;
    var lock = false;

    // dropload
    $('.myexchange-main-content').dropload({
        scrollArea: window,
        loadDownFn: function (me) {

            if (lock) {
                return;
            }
            lock = true;

            var url = url_base + "/integral/listExchanged?openId=" + openId + "&page=" + page + "&size=" + size;
            var request = $.ajax({
                url: url,
                method: "GET",
                dataType: 'json'
            });
            request.done(function (data) {
                if (0 == data.count) {
                    me.resetload();
                    // 锁定
                    me.lock();
                    // 无数据
                    me.noData();

                    lock = false;
                    $("body").html("");

                    alert('没有兑换记录');
                    return;
                }

                page++;
                var result = '';
                var rows = data.rows;
                for (var i in rows) {
                    var row = rows[i];
                    var itemHtml = '<li class="clearfix">';
                    itemHtml += '<div class="fl"><img src="img/score.png" alt=""></div>';
                    itemHtml += '<div class="fr">';
                    itemHtml += '<p class="title">' + row.name + '</p>';
                    itemHtml += '<p class="clearfix">';
                    //itemHtml += '<span class="fl">畅想阅读新方式</span>';

                    if (row.status == 0) {
                        itemHtml += '<button class="fr" data-id=' + row.id + ' data-productId=' + row.productId + ' onclick="readyMakeExchangeUsed(this)">立即兑换</button>';
                    } else {
                        itemHtml += '<button class="fr" style="background:#CCCCCC" disabled="disabled" data-id=' + row.id + ' data-productId=' + row.productId + '>已兑换</button>';
                    }
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
                    $(".myexchange-main-content ul").append(result);
                    // 每次数据加载完，必须重置
                    me.resetload();
                }, 1000);
                lock = false;
            });
            request.fail(function (jqXHR, textStatus) {
                me.resetload();
                lock = false;
                alert('出错了');
            });

        }
    });

});
