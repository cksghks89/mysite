package com.poscodx.mysite.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.poscodx.mysite.vo.UserVo;

public class AuthUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// @AuthUser가 있고, 해당 파라미터 타입이 UserVo라면 resolveArgument를 실행
		return parameter.getParameterAnnotation(AuthUser.class) != null
				&& parameter.getParameterType().equals(UserVo.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		if (!supportsParameter(parameter)) {
			return WebArgumentResolver.UNRESOLVED;
		}

		// tomcat에 종속적인 request로 변환 (tomcat 전용 argument resolver가 되는 것)
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		HttpSession session = request.getSession();

		return session.getAttribute("authUser");
	}

}
