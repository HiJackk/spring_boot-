package com.bishe.o2o.web.superadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/superadmin", method = { RequestMethod.GET, RequestMethod.POST })
public class SuperAdminController {

	@RequestMapping(value = "/areamanage", method = RequestMethod.GET)
	private String areamanagement() {
		// 区域管理页
		return "superadmin/areamanage";
	}

	@RequestMapping(value = "/headlinemanage", method = RequestMethod.GET)
	private String headLinemanagement() {
		// 头条管理页
		return "superadmin/headlinemanage";
	}

	@RequestMapping(value = "/shopcategorymanage", method = RequestMethod.GET)
	private String shopCategorymanage() {
		// 店铺类别管理页
		return "superadmin/shopcategorymanage";
	}

	@RequestMapping(value = "/shopmanage", method = RequestMethod.GET)
	private String shopmanage() {
		// 店铺管理页
		return "superadmin/shopmanage";
	}

	@RequestMapping(value = "/personinfomanage", method = RequestMethod.GET)
	private String personInfomanage() {
		// 用户信息管理页
		return "superadmin/personinfomanage";
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	private String main() {
		// 超级管理员主页
		return "superadmin/main";
	}

	@RequestMapping(value = "/top", method = RequestMethod.GET)
	private String top() {
		// 超级管理员frame top部分
		return "superadmin/top";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	private String login() {
		// 超级管理员登录页
		return "superadmin/login";
	}

}
