$(function() {

	var $height=$(document).height();
	// 添加成员
//	$("#group-main .group-main-content .search p.fr a").on("touchstart",function(){
//		$("div.mask").height($height).show();
//		$(".add-member").show();
//	})
$(".add-member>p a").on("touchstart",function(){
	$("div.mask").hide();
	$(".add-member").hide();
})
	// 删除成员
//	$("#group-main .group-main-content .content-list li p.fr a").on("touchstart",function(){
//		$("div.mask").height($height).show();
//		$(".delete-member").show();
//	})
//	$(".delete-member .delete-member-button .yes").on("touchstart",function(){
//		// $("div.mask").height($height).hide();
//		$(".delete-member").hide();
//		$(".no-root").show();
//	})
//	$(".delete-member .delete-member-button .no").on("touchstart",function(){
//		$("div.mask").hide();
//		$(".delete-member").hide();
//	})
	// 权限
	$(".no-root>p a").on("touchstart",function(){
		$("div.mask").hide();
		$(".no-root").hide();
	})
	// 个人中心遮罩
	$("div.personalcenter_mask").height($height);
	$(".personalcenter_attitude .personalcenter_attitude-button a").on("touchstart",function(){
		$("div.personalcenter_mask").hide();
		$(".personalcenter_attitude").hide();
	})

    // 遮罩层点击
    $("div.mask").on("touchstart",function(event){
    	$("div.personalcenter_mask").hide();
    	$(".add-member").hide();
    	$(".no-root").hide();
    	$(".delete-member").hide();
    	$(".personalcenter_attitude").hide();

    	return false;
    });
    

    //群组选项卡
    $("#group-main .group-main-content .main-content .erweima p a").on("click",function(){
    	$(this).addClass("active").siblings().removeClass("active");
    	var index=$(this).index();
    	$("#group-main .group-main-content .main-content .erweima img").hide();
    	$("#group-main .group-main-content .main-content .erweima img").eq(index).show();
    })

});
