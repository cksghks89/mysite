package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.vo.Page;
import com.poscodx.mysite.vo.UserVo;

public class ReplyFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		// Access Control
		if (session == null) {
			response.sendRedirect(request.getContextPath());
			return;
		}

		UserVo authUser = (UserVo) session.getAttribute("authUser");
		String no = request.getParameter("no");
		if (authUser == null || !no.matches("[0-9]+")) {
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
		request.setAttribute("page", page);
		// 페이징 처리 ------ end
		
		request.setAttribute("parentNo", no);
		
		request.getRequestDispatcher("/WEB-INF/views/board/reply.jsp").forward(request, response);
	}

}
