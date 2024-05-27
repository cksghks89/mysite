package com.poscodx.mysite.controller.action.guestbook;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet;

public class GuestbookDeleteFormAction implements ActionServlet.Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String no = request.getParameter("no");

		request.setAttribute("no", no);
		request.getRequestDispatcher("WEB-INF/views/guestbook/deleteform.jsp").forward(request, response);
	}

}
