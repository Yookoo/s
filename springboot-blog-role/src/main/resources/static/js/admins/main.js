/**
 * Bolg main JS.
 * Created by waylau.com on 2017/3/9.
 */
"use strict";
//# sourceURL=main.js

// DOM 加载完再执行
$(function() {

	// 搜索
	$(".blog-menu .list-group-item").click(function() {
		//console.log($(this).value);
		
		var url = $(this).attr("url");
		
		//先移除其他点击样式，再添加当前点击样式
		$(".blog-menu .list-group-item").removeClass("active");
		$(this).addClass("active");
		
		//加载其他模块的页面到右侧工作区
		$.ajax({
			url: url,
			success: function(data) {
				$("#rightContainer").html(data);
			},
			error : function() {
				alert("error");
			},
		});
		
	});
	
	//默认选中第一项
	$(".blog-menu .list-group-item:first").trigger("click");

});