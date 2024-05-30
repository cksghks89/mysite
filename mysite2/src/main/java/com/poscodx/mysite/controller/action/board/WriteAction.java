package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.Page;
import com.poscodx.mysite.vo.UserVo;

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		// Access Control
		if (session == null) {
			response.sendRedirect(request.getContextPath());
			return;
		}

		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			response.sendRedirect(request.getContextPath());
			return;
		}
		///////////////////////////////////////////////////////

		// 페이징 처리 ------ start
		Page page = new Page();

		String pageNo = request.getParameter("p");
		String keyword = request.getParameter("kwd");

		if (!("".equals(pageNo) || pageNo == null)) {
			page.setPageNo(Integer.parseInt(pageNo));
		}
		if (keyword == null) {
			keyword = "";
		}
		page.setQuery(keyword);
		// 페이징 처리 ------ end

		String title = request.getParameter("title");
		String contents = request.getParameter("content");
		
		if (title == null || "".equals(title)) {
			response.sendRedirect(request.getContextPath());
			return;
		}
		
		BoardVo boardVo = new BoardVo();
		boardVo.setTitle(title);
		boardVo.setContents(contents);
		boardVo.setUserNo(authUser.getNo());

		int result = new BoardDao().insert(boardVo);

		response.sendRedirect(
				request.getContextPath() + "/board?a=list&p=" + page.getPageNo() + "&kwd=" + page.getQuery());
	}

}
