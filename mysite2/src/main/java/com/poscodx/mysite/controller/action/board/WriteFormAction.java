package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.vo.Page;

public class WriteFormAction implements Action{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
		request.getRequestDispatcher("/WEB-INF/views/board/write.jsp").forward(request, response);
	}

}
