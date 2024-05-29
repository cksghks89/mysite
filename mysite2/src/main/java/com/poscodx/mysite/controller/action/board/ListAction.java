package com.poscodx.mysite.controller.action.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.Page;
import com.poscodx.mysite.vo.PageResult;

public class ListAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BoardDao boardDao = new BoardDao();
		
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
		
		
		List<BoardVo> list = boardDao.findAll(page);

		int count = boardDao.countQuery(page);
		PageResult pageResult = new PageResult(page.getPageNo(), count, page.getQuery());

		request.setAttribute("list", list);
		request.setAttribute("pageResult", pageResult);

		request.getRequestDispatcher("/WEB-INF/views/board/list.jsp").forward(request, response);
	}

}
