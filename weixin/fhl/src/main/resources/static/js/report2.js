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
var userId = search.split("&")[1].split("=")[1];

// 检查字符串字数
function WidthCheck(str, maxLen) {
    var w = 0;
//length 获取字数数，不区分汉子和英文
    for (var i = 0; i < str.value.length; i++) {
//charCodeAt()获取字符串中某一个字符的编码
        var c = str.value.charCodeAt(i);
//单字节加1
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
            // 英文字符和数字
            w++;
        } else {
            // 中文字符
            w += 1;
        }
        if (w > maxLen) {
            str.value = str.value.substr(0, i);
            break;
        }
    }
}

//存储微信上传图片信息
var images = {
    localIds: [],
    serverIds: []
};

$(document).ready(function () {

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
                    jsApiList: ['chooseImage', 'uploadImage', 'previewImage', 'downloadImage', 'getLocalImgData']
                });

                wx.error(function (res) {
                    //alert("系统异常");
                });

                wx.ready(function () {
                });
            });


    $("#chooseImage").click(function () {

        wx.chooseImage({
            count: 4, // 默认9
            sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有 'original', 'compressed'
            sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
            success: function (res) {
                var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                images.localIds = res.localIds;

                var len = 0;
                //存放图片的父级元素
                var imgContainer = $("#photoList");
                imgContainer.html("");

                //遍历获取到得图片文件
                for (var i in localIds) {
                    //var imgUrl = window.URL.createObjectURL(file.files[i]);

//                    var img = document.createElement("img");
//                    img.setAttribute("src", localIds[i]);
//                    var imgAdd = document.createElement("div");
//                    imgAdd.setAttribute("class", "z_addImg");
//                    imgAdd.appendChild(img);
//                    imgContainer.appendChild(imgAdd);
                    imgContainer.append('<div class="z_addImg"><img src=' + localIds[i] + '></div>');

                    len += 1;
                }
                //imgRemove();
                $('#report-main .report-main-content .jietu p.font-number span').html(len);
            }
        });
    });

});


function report() {

    var title = $("#title").val();
    if (title.trim().length === 0) {
        alert("请输入标题");
        return;
    }

    var content = $("#content").val();
    if (content.trim().length === 0) {
        alert("请输入描述");
        return;
    }

    // 5.3 上传图片
//    if (images.localIds.length === 0) {
//        alert('请先使用 chooseImage 接口选择图片');
//        return;
//    }



    var i = 0;
    var length = images.localIds.length;

    function upload() {
        if (0 === length) {
            return;
        }

        wx.uploadImage({
            localId: images.localIds[i],
            isShowProgressTips: 1, // 默认为1，显示进度提示
            success: function (res) {
                i++;
                images.serverIds.push(res.serverId);
                if (i < length) {
                    upload();
                }
            },
            fail: function (res) {
                alert(JSON.stringify(res));
                images.serverIds = [];
                return;
            }
        });
    }
    upload();    /*********************多张图片，这里上传使用的是递归******************************/


    function submit() {

        if (images.serverIds.length < images.localIds.length) {
            setTimeout(submit, 1000);
            return;
        }

        var imgIds = '';
        for (var i in images.serverIds) {
            if (imgIds.length > 0) {
                imgIds += ',';
            }
            imgIds += images.serverIds[i];
        }


//        
//    var param = 'openId=' + openId;
//    param += '&userId=' + userId;
//    param += '&title=' + title;
//    param += '&content=' + content;
//    param += '&imgIds=' + imgIds;



        var param = {
            openId: openId,
            userId: userId,
            title: title,
            content: content,
            imgIds: imgIds
        };

        var url = url_base + "/report-feedback/upload";
        $.post(url, param, function (data, status) {
            var msg = jQuery.parseJSON(data);

            if (msg) {
                WeixinJSBridge.call('closeWindow');
            } else {
                alert("举报失败");
            }
        });


//        var request = $.ajax({
//            url: url,
//            method: 'POST',
//            data: JSON.stringify(param),
//            dataType: 'json'
//        });
//        request.done(function (msg) {
//            if (msg) {
//                //WeixinJSBridge.call('closeWindow');
//                //window.location.href = url_base + "/garbage.html?userId=" + userId;
//            } else {
//                alert("举报失败");
//            }
//        });
//
//        request.fail(function (jqXHR, textStatus) {
//            //alert("Request failed: " + textStatus);
//            alert('出错了');
//        });


    }
    submit();

}



