var url_base = window.location.protocol + "//" + window.location.host;
var port = window.location.port;
if (port !== '') {
    url_base = url_base + ":" + port;
}


function viewDescription(s) {
    var title = $(s).attr('data-title');
    var description = $(s).attr('data-description');

    $(".knowledge_answer .title").text(title);
    $(".knowledge_answer .content").html(description);

    var $height = $(document).height();
    $("div.knowledge_mask").height($height).show();
    $(".knowledge_answer").show();
}

function searchAndView() {
    var word = $("#word").val();
    if (word == null || word.trim() == '') {
        alert("关键字不能为空");
        return;
    }
    word = word.trim();

    var url = url_base + "/category-repository/search?word=" + word;
    var request = $.ajax({
        url: url,
        method: "GET",
        dataType: 'json'
    });
    request.done(function (msg) {
        if (null==msg) {
            alert("未查询到您所要搜索的信息");
            return;
        }
        $(".knowledge_answer .title").text(msg.name);
        $(".knowledge_answer .content").html(msg.description);

        var $height = $(document).height();
        $("div.knowledge_mask").height($height).show();
        $(".knowledge_answer").show();

    });
    request.fail(function (jqXHR, textStatus) {
        alert("未查询到您所要搜索的信息");
        //alert("Request failed: " + textStatus);
    });

}

$(document).ready(function () {

    var url = url_base + "/category-repository/list";
    var request = $.ajax({
        url: url,
        method: "GET",
        dataType: 'json'
    });
    request.done(function (msg) {
        if (msg == null || msg.length == 0) {
            alert('尚未有相关文章');
        }

        for (var i in msg) {
            var row = msg[i];

            $(".garbage-kinds ul li#" + row.id).attr('data-title', row.name);
            $(".garbage-kinds ul li#" + row.id).attr('data-description', row.description);
            $(".garbage-kinds ul li#" + row.id + " p").text(row.name);
        }
    });
    request.fail(function (jqXHR, textStatus) {
        alert("Request failed: " + textStatus);
    });


//    $("#knowlege-main .knowlege-main-content .garbage-kinds ul li").on("touchstart", function () {
//        $("div.knowledge_mask").height($height).show();
//        $(".knowledge_answer").show();
//    });

});


