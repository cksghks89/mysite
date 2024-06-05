package com.poscodx.mysite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poscodx.mysite.service.GuestbookService;
import com.poscodx.mysite.vo.GuestbookVo;

@Controller
@RequestMapping("/guestbook")
public class GuestbookController {
	@Autowired
	private GuestbookService guestbookService;
	
	@RequestMapping("")
	public String list(Model model) {
		List<GuestbookVo> contentsList = guestbookService.getContentsList();
		model.addAttribute("list", contentsList);
		return "guestbook/list";
	}
	
	@RequestMapping("/add")
	public String add(GuestbookVo vo) {
		guestbookService.addContents(vo);
		return "redirect:/guestbook";
	}
	
	@RequestMapping(value = "/delete/{no}", method = RequestMethod.GET)
	public String delete(@PathVariable Long no, Model model) { 
		model.addAttribute("no", no);
		return "guestbook/delete";
	}
	
	@RequestMapping(value = "/delete/{no}", method = RequestMethod.POST)
	public String delete(GuestbookVo vo) { 
		guestbookService.deleteContents(vo.getNo(), vo.getPassword());
		return "redirect:/guestbook";
	}
}
