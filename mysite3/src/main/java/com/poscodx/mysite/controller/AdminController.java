package com.poscodx.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poscodx.mysite.security.Auth;
import com.poscodx.mysite.service.SiteService;
import com.poscodx.mysite.vo.SiteVo;

@Auth(role = "ADMIN")
@Controller
@RequestMapping("/admin")
public class AdminController {
	private SiteService siteService;

	public AdminController(SiteService adminService) {
		this.siteService = adminService;
	}

	@RequestMapping({ "" })
	public String main(Model model) {
		model.addAttribute("siteVo", siteService.getSite());
		return "admin/main";
	}

	@RequestMapping(value = "/main/update", method = RequestMethod.POST)
	public String updateSite(SiteVo vo) {
		siteService.updateSite(vo);
		return "redirect:/admin";
	}

	@RequestMapping("/guestbook")
	public String guestbook() {
		return "admin/guestbook";
	}

	@RequestMapping("/board")
	public String board() {
		return "admin/board";
	}

	@RequestMapping("/user")
	public String user() {
		return "admin/user";
	}
}
