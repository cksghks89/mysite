package com.poscodx.mysite.controller.action.guestbook;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet;
import com.poscodx.mysite.dao.GuestbookDao;

public class GuestbookDeleteAction implements ActionServlet.Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long no = Long.parseLong(request.getParameter("no"));
		String password = request.getParameter("password");

		new GuestbookDao().deleteByNoAndPassword(no, password);
		response.sendRedirect(request.getContextPath() + "/guestbook");
	}

}
