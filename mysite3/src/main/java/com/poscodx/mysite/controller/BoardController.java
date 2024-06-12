package com.poscodx.mysite.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poscodx.mysite.security.Auth;
import com.poscodx.mysite.security.AuthUser;
import com.poscodx.mysite.service.BoardService;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.Page;
import com.poscodx.mysite.vo.PageResult;
import com.poscodx.mysite.vo.UserVo;
import com.poscodx.mysite.web.WebUtil;

@Controller
@RequestMapping("/board")
public class BoardController {
	private BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@RequestMapping({ "", "/list" })
	public String index(Model model, Page page) {
		Map<String, Object> contentsList = boardService.getContentsList(page);
		PageResult pageResult = (PageResult) contentsList.get("pageResult");
		if (pageResult.getPageNo() > pageResult.getEndPage()) {
			return "redirect:/board";
		}
		
		model.addAllAttributes(contentsList);
		return "board/list";
	}

	@Auth
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(Model model, Page page) {
		model.addAttribute("page", page);
		return "board/write";
	}

	@Auth
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(@AuthUser UserVo authUser, Page page, BoardVo vo) {
		vo.setUserNo(authUser.getNo());
		boardService.addContents(vo);

		return String.format("redirect:/board?pageNo=%d&query=%s", 1, "");
	}

	@Auth
	@RequestMapping("/delete/{no}")
	public String delete(@AuthUser UserVo authUser, Page page, @PathVariable("no") Long no) {
		boardService.deleteContents(no, authUser.getNo());
		return String.format("redirect:/board?pageNo=%d&query=%s", page.getPageNo(),
				WebUtil.encodeURL(page.getQuery(), "utf-8"));
	}

	@Auth
	@RequestMapping(value = "/reply/{no}", method = RequestMethod.GET)
	public String reply(Model model, HttpSession session, Page page, @PathVariable("no") Long no) {
		model.addAttribute("page", page);
		model.addAttribute("parentNo", no);
		return "board/reply";
	}

	@Auth
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(@AuthUser UserVo authUser, Page page, BoardVo vo) {
		BoardVo parent = boardService.getContents(vo.getNo());
		if (parent != null) {
			vo.setgNo(parent.getgNo());
			vo.setoNo(parent.getoNo());
			vo.setDepth(parent.getDepth());
			vo.setUserNo(authUser.getNo());
		}

		boardService.addContents(vo);

		return String.format("redirect:/board?pageNo=%d&query=%s", page.getPageNo(),
				WebUtil.encodeURL(page.getQuery(), "utf-8"));
	}

	@RequestMapping("/view/{no}")
	public String view(Model model, Page page, @PathVariable("no") Long no) {
		BoardVo boardVo = boardService.getContents(no);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("page", page);
		return "board/view";
	}

	@Auth
	@RequestMapping(value = "/update/{no}", method = RequestMethod.GET)
	public String update(@AuthUser UserVo authUser, Model model, Page page, @PathVariable("no") Long no) {
		BoardVo boardVo = boardService.getContents(no, authUser.getNo());
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("page", page);

		return "board/modify";
	}

	@Auth
	@RequestMapping(value = "/update/{no}", method = RequestMethod.POST)
	public String update(@AuthUser UserVo authUser, Page page, BoardVo vo, @PathVariable Long no) {
		vo.setUserNo(authUser.getNo());
		vo.setNo(no);
		boardService.updateContents(vo);

		return String.format("redirect:/board/view/%d?pageNo=%d&query=%s", no, page.getPageNo(),
				WebUtil.encodeURL(page.getQuery(), "utf-8"));
	}

	@RequestMapping("/search")
	public String search(Page page) {
		return String.format("redirect:/board?pageNo=%d&query=%s", page.getPageNo(),
				WebUtil.encodeURL(page.getQuery(), "utf-8"));
	}
}
