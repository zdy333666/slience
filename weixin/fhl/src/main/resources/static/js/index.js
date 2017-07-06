$(function() {
	// 积分礼遇遮罩层
//	var $button=$("#scoregift-main .scoregift-main-content ul li .fr .clearfix button");
var $height=$(document).height();
//	$button.on("touchstart",function(){	
//		$("div.mask").height($height).show();
//		$(".change-score").show();
//	});
//	var $change=$(".change-score .bottom-change button");
//	$change.on("touchstart",function(){
//		$("div.mask").hide();
//		$(".change-score").hide();
//	});
$("div.mask").on("touchstart",function(event){
	$("div.mask").hide();
	$(".change-score").hide();
        return false;
});

	// 手机注销遮罩层
//	$("#phonenumber-main .phonenumber-main-content button:last-child").on("touchstart",function(event){
//		$("div.mask_num").height($height).show();
//		$(".num_cancel").show();
//                return false;
//	});
//	$(".num_cancel p button").on("touchstart",function(event){
//		$("div.mask_num").hide();
//		$(".num_cancel").hide();
//                 return false;
//	});
	$("div.mask_num").on("touchstart",function(event){
		$("div.mask_num").hide();
		$(".num_cancel").hide();
		return false;
	});

	//我的兑换遮罩层
//	$("#myexchange-main .myexchange-main-content ul li .fr .clearfix button").on("touchstart",function(){
//		$("div.mask_myexchange").height($height).show();
//		$(".myexchange").show();
//	});
//	$(".myexchange button").on("touchstart",function(event){
//		$("div.mask_myexchange").hide();
//		$(".myexchange").hide();
//                 return false;
//	});
	$("div.mask_myexchange").on("touchstart",function(event){
		$("div.mask_myexchange").hide();
		$(".myexchange").hide();
                return false;
	});

	//知识库查询弹出窗
//	$("#knowlege-main .knowlege-main-content .garbage-search .clearfix button").on("touchstart",function(){
//		$("div.knowledge_mask").height($height).show();
//		$(".knowledge_answer").show();
//	});
//	$("#knowlege-main .knowlege-main-content .garbage-kinds ul li").on("touchstart",function(){
//		$("div.knowledge_mask").height($height).show();
//		$(".knowledge_answer").show();
//	});
$(".knowledge_mask").on("touchstart",function(event){
	$("div.knowledge_mask").hide();
	$(".knowledge_answer").hide();
        return false;
});


    //设置容器为手机屏幕的高度
    window.onresize = function(){
    	$("#bindpage-main").css({"min-height":$height});
    	$("#phonenumber-main").css({"min-height":$height});
    	$("#knowlege-main").css({"min-height":$height});
//    	$("#detail-main").height($(window).height());
};
$("#bindpage-main").css({"min-height":$height});
$("#phonenumber-main").css({"min-height":$height});
$("#knowlege-main").css({"min-height":$height});
$("#myexchange-main").css({"min-height":$height});
   $("#detail-main").css({"min-height":$height});

    // report页面动态监听长度
    $("#report-main .report-main-content ul li textarea").bind('input propertychange', function() {
    	$('#report-main .report-main-content ul li p.font-number span').html($(this).val().trim().length);
    });

    // 图片上传
    var len=0;
    function imgChange(obj1, obj2) {	
    	if(len<4){
			//获取点击的文本框
			var file = document.getElementById("file");
		//存放图片的父级元素
		var imgContainer = document.getElementsByClassName(obj1)[0];
		//获取的图片文件
		var fileList = file.files;
		//文本框的父级元素
		var input = document.getElementsByClassName(obj2)[0];
		var imgArr = [];
		//遍历获取到得图片文件
		for (var i = 0; i < fileList.length; i++) {
			var imgUrl = window.URL.createObjectURL(file.files[i]);
			imgArr.push(imgUrl);
			var img = document.createElement("img");
			img.setAttribute("src", imgArr[i]);
			var imgAdd = document.createElement("div");
			imgAdd.setAttribute("class", "z_addImg");
			imgAdd.appendChild(img);
			imgContainer.appendChild(imgAdd);
		};
		len+=1;
		imgRemove();
		$('#report-main .report-main-content .jietu p.font-number span').html(len);
	}
};

function imgRemove() {
	var imgList = document.getElementsByClassName("z_addImg");
	for (var j = 0; j < imgList.length; j++) {
		imgList[j].index = j;
		imgList[j].onclick = function() {
			this.style.display="none";
			len-=1;
			$('#report-main .report-main-content .jietu p.font-number span').html(len);
		}
	};
};

$(".z_file input").on("change",function(){
	imgChange('z_photo','z_file');
});



});
