$(function() {
	// 支付遮罩层
	$(".paydetail-main-content .pay").on("touchstart",function(){
		$(".mask_paydetail").height($(window).height()).show();
		$(".paydetail").show();
	});
//	$(".paydetail p button").on("touchstart",function(){
//		$(".mask_paydetail").hide();
//		$(".paydetail").hide();
//	});

    //设置容器为手机屏幕的高度
    window.onresize = function(){
    	$("#paydetail-main").css("min-height",$(window).height());
    };
    $("#paydetail-main").css("min-height",$(window).height());
    

});
