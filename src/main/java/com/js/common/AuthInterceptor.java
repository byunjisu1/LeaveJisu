package com.js.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		if(session.getAttribute("loginId") != null) {
			return true;	// 로그인 O 상황으로 판단
		}
		// 로그인 x 상황으로 판단
		System.out.println("인터셉터에 의해 경로 설정 : " + request.getRequestURI() + " --> /");
		
		session.setAttribute("msg", "로그인부터 하세요");
		request.getRequestDispatcher("/Login").forward(request, response);
		return false;
	}
}
