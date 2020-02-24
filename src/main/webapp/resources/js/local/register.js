/**
 * 
 */
$(function() {
	var usertype='1';
	var userPostUrl = '/o2o/local/registert';
	// 提交按钮的事件响应，分别对商品添加和编辑操作做不同响应
	$('#submit').click(function() {
		// 获取输入的帐号
		var userName = $('#user-name').val();
		// 获取输入的密码
		var password = $('#user-pwd').val();
	
		// 访问后台，绑定帐号
		$.ajax({
			url : userPostUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				userName : userName,
				password : password,
				
			},
			success : function(data) {
				if (data.success) {
					$.toast('注册成功成功！');
					if (usertype == 1) {
						// 若用户在前端展示系统页面则自动退回到前端展示系统首页
						window.location.href = '/o2o/frontend/index';
					} else {
						// 若用户是在店家管理系统页面则自动回退到店铺列表页中
						window.location.href = '/o2o/shopadmin/shoplist';
					}

				} else {
					$.toast('提交失败！' + data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});
	
});