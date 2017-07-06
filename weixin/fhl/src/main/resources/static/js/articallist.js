var url_base = window.location.protocol + "//" + window.location.host;
var port = window.location.port;
if (port !== '') {
    url_base = url_base + ":" + port;
}

var search = window.location.search;
search = search.substring(1);
var openId = search.split("=")[1];


function viewDetail(s) {
    var id = $(s).attr('data');
    window.location.href = url_base + "/detail.html?id=" + id;
}

$(document).ready(function () {

    var page = 1;
    // 每页展示5个
    var size = 5;
    var count = 0;
    var lock = false;
    // dropload
    $('.articallist-main-content').dropload({
        scrollArea: window,
        loadDownFn: function (me) {

            if (lock) {
                return;
            }
            lock = true;

            var url = url_base + "/category-policy/list?openId=" + openId + "&page=" + page + "&size=" + size;
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
                    $("body").html("");

                    alert('尚未有相关文章');
                    return;
                }

                page++;
                var result = '';
                var rows = data.rows;
                for (var i in rows) {
                    var row = rows[i];
                    var date = '';
                    if (null != row.createTime) {
                        date = (new Date(row.createTime)).Format("yyyy年MM月dd日");
                    }
                    var pic = row.pic;
                    if (null == pic) {
                        pic = '/img/policy.png';
                    }

                    var title = row.title;
                    if (null == title) {
                        title = "";
                    }
                    if (title.length > 27) {
                        title = title.substr(0, 27) + '...'
                    }

                    var rowHtml = '<li class="clearfix" data=' + row.id + ' onclick="viewDetail(this)">';
                    rowHtml += '<div class="fl">';
                    rowHtml += '<p class="title">' + title + '</p>';
                    rowHtml += '<p class="date">' + date + '</p>';
                    rowHtml += '</div>';
                    rowHtml += '<div class="fr">';
                    rowHtml += '<img src=' + pic + ' alt="">';
                    rowHtml += '</div>';
                    rowHtml += '</li>';
                    result += rowHtml;
                    
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
                    $(".articallist-main-content ul").append(result);
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

