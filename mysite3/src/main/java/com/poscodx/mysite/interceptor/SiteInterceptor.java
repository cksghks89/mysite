package com.poscodx.mysite.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.poscodx.mysite.service.SiteService;
import com.poscodx.mysite.vo.SiteVo;

@Component
public class SiteInterceptor implements HandlerInterceptor {
	
	private final SiteService siteService;
	private final ServletContext servletContext;

	public SiteInterceptor(SiteService siteService, ServletContext servletContext) {
		this.siteService = siteService;
		this.servletContext = servletContext;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		SiteVo siteVo = (SiteVo) servletContext.getAttribute("siteVo");		
		if (siteVo == null) {
			siteVo = siteService.getSite();
			servletContext.setAttribute("siteVo", siteVo);
		}
		return true;
	}

}
