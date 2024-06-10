package com.poscodx.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poscodx.mysite.service.BoardService;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.Page;
import com.poscodx.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	private BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@RequestMapping({ "", "/list" })
	public String index(Model model, Page page) {
		model.addAllAttributes(boardService.getContentsList(page));
		return "board/list";
	}

	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(HttpSession session, Model model, Page page) {
		// access control
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		/////////////////
		model.addAttribute("page", page);
		return "board/write";
	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(HttpSession session, Page page, BoardVo vo) {
		// access control
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		/////////////////

		vo.setUserNo(authUser.getNo());
		boardService.addContents(vo);

		return String.format("redirect:/board?pageNo=%d&query=%s", 1, "");
	}

	@RequestMapping("/delete/{no}")
	public String delete(HttpSession session, Page page, @PathVariable("no") Long no) {
		// access control
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		/////////////////

		boardService.deleteContents(no, authUser.getNo());
		return String.format("redirect:/board?pageNo=%d&query=%s", page.getPageNo(), page.getQuery());
	}

	@RequestMapping(value = "/reply/{no}", method = RequestMethod.GET)
	public String reply(Model model, HttpSession session, Page page, @PathVariable("no") Long no) {
		// access control
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		/////////////////

		model.addAttribute("page", page);
		model.addAttribute("parentNo", no);

		return "board/reply";
	}

	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(HttpSession session, Page page, BoardVo vo) {
		// access control
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		/////////////////

		BoardVo parent = boardService.getContents(vo.getNo());
		if (parent != null) {
			vo.setgNo(parent.getgNo());
			vo.setoNo(parent.getoNo());
			vo.setDepth(parent.getDepth());
			vo.setUserNo(authUser.getNo());
		}

		boardService.addContents(vo);

		return String.format("redirect:/board?pageNo=%d&query=%s", page.getPageNo(), page.getQuery());
	}

	@RequestMapping("/view/{no}")
	public String view(Model model, Page page, @PathVariable("no") Long no) {
		BoardVo boardVo = boardService.getContents(no);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("page", page);
		return "board/view";
	}

	@RequestMapping(value = "/update/{no}", method = RequestMethod.GET)
	public String update(Model model, HttpSession session, Page page, @PathVariable("no") Long no) {
		// access control
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		/////////////////

		BoardVo boardVo = boardService.getContents(no, authUser.getNo());
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("page", page);

		return "board/modify";
	}

	@RequestMapping(value = "/update/{no}", method = RequestMethod.POST)
	public String update(HttpSession session, Page page, BoardVo vo, @PathVariable Long no) {
		// access control
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		/////////////////

		vo.setUserNo(authUser.getNo());
		vo.setNo(no);
		boardService.updateContents(vo);

		return String.format("redirect:/board/view/%d?pageNo=%d&query=%s", no, page.getPageNo(), page.getQuery());
	}

	@RequestMapping("/search")
	public String search(Page page) {
		return String.format("redirect:/board?pageNo=%d&query=%s", page.getPageNo(), page.getQuery());
	}
}
