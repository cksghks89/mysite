package com.poscodx.mysite.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.poscodx.mysite.vo.UserVo;

public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1. handler 종류 확인
		if (!(handler instanceof HandlerMethod)) {
			// DefaultServletHandler 가 처리하는 경우(정적자원, /assets/**, mapping이 안되어 있는 URL)
			return true;
		}

		// 2. casting
		HandlerMethod handlerMethod = (HandlerMethod) handler;

		// 3. handlerMethod의 @Auth 가져오기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);

		// 4-1. HandlerMethod에 @Auth가 없는 경우 Class Type 에서 @Auth 를 탐색
		if (auth == null) {
			auth = handlerMethod.getBeanType().getAnnotation(Auth.class);
		}

		// 4-2. Method, Class Type 둘 다 @Auth가 없는 경우 (인증 불필요)
		if (auth == null) {
			return true;
		}

		// 5. @Auth 가 붙어있기 때문에 인증(Authentication) 확인
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		if (authUser == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}

		// 6. 권한(Authorization) 체크를 위해 @Auth의 role 가져오기 ("USER", "ADMIN")
		String role = auth.role();

		// 7. @Auth role 이 "USER"인 경우, authUser의 role 은 상관없다.
		if ("USER".equals(role)) {
			return true;
		}

		// 8. @Auth role 이 "ADMIN"인 경우, authUser의 role 은 반드시 "ADMIN"
		if (!"ADMIN".equals(authUser.getRole())) {
			response.sendRedirect(request.getContextPath());
			return false;
		}

		// 9. 옳은 관리자 권한 @Auth(role="ADMIN"), authUser.getRole() == "ADMIN"
		return true;
	}

}
