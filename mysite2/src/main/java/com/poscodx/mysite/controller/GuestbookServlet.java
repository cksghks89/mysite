package com.poscodx.mysite.controller;

import java.util.Map;

import com.poscodx.mysite.controller.action.guestbook.GuestbookDeleteAction;
import com.poscodx.mysite.controller.action.guestbook.GuestbookDeleteFormAction;
import com.poscodx.mysite.controller.action.guestbook.GuestbookInsertAction;
import com.poscodx.mysite.controller.action.guestbook.GuestbookListAction;

public class GuestbookServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;

	private Map<String, Action> mapAction = Map.of(
			"insert", new GuestbookInsertAction(),
			"deleteform", new GuestbookDeleteFormAction(),
			"delete", new GuestbookDeleteAction()
	);

	@Override
	protected Action getAction(String actionName) {
		return mapAction.getOrDefault(actionName, new GuestbookListAction());
	}

}
