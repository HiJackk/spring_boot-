package com.bishe.o2o.interceptor.superadmin;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bishe.o2o.entity.PersonInfo;

public class SuperAdminLoginInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从session中获取登录的用户信息
		Object userObj = request.getSession().getAttribute("user");
		if (userObj != null) {
			PersonInfo user = (PersonInfo) userObj;
			// 判断用户是否真的为管理员
			if (user != null && user.getUserId() != null && user.getUserId() > 0 && user.getUserType() == 3)
				return true;
		}
		// 不满足条件的话，则退回到登录界面
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<script>");
		out.println("window.open ('" + request.getContextPath() + "/superadmin/login','_top')");
		out.println("</script>");
		out.println("</html>");
		return false;
	}
}
