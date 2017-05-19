Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

$(document).ready(function () {

    var url_base = window.location.protocol + "//" + window.location.host;
    var port = window.location.port;
    if (port !== '') {
        url_base = url_base + ":" + port;
    }

    var url = url_base + "/category-policy/list";

    var request = $.ajax({
        url: url,
        method: "GET",
        //data: JSON.stringify(input),
        dataType: 'json'
    });
    request.done(function (msg) {
        for (var i in msg) {
            var row = msg[i];
            var rowHtml = '<li class="clearfix" id="' + row.id + '">';
            rowHtml += '<div class="fl">';
            rowHtml += '<p class="title">' + row.title + '</p>';
            rowHtml += '<p class="date">' + (row.createTime===null)? (new Date()).Format("yyyy年M月d日"):row.createTime.Format("yyyy年M月d日") + '</p>';
            rowHtml += '</div>';
            rowHtml += '<div class="fr">';
            rowHtml += '<img src=' + row.pic + ' alt="">';
            rowHtml += '</div>';
            rowHtml += ' </li>';

            $(".articallist-main-content ul").append(rowHtml);
        }
    });

    request.fail(function (jqXHR, textStatus) {
        alert("Request failed: " + textStatus);
    });


});//end

