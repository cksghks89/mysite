package com.poscodx.mysite.controller.action.board;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.Page;

public class ViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String no = request.getParameter("no");

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

		BoardDao boardDao = new BoardDao();

		BoardVo boardVo = boardDao.findByNo(Long.parseLong(no));
		request.setAttribute("boardVo", boardVo);
		request.setAttribute("page", page);

		// 조회수 처리 -------- start
		ObjectMapper objectMapper = new ObjectMapper();
		Cookie[] cookies = request.getCookies();
		Map<String, String> visitMap = new HashMap<>();
		boolean alreadyVisited = false;
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if ("visitCount".equals(cookie.getName())) {
					String decodeJson = URLDecoder.decode(cookie.getValue());
					visitMap = objectMapper.readValue(decodeJson, new TypeReference<HashMap<String, String>>() {
					});
				}
			}
		}

		LocalDateTime now = LocalDateTime.now();
		// DateTimeFormatter를 사용하여 형식 지정
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm:ss");
		// LocalDateTime을 문자열로 변환
		String formatNow = now.format(formatter);

		if (!visitMap.containsKey(no)) {
			boardDao.increaseViewCount(boardVo);

			visitMap.put(no, formatNow);
			String jsonMap = URLEncoder.encode(objectMapper.writeValueAsString(visitMap));
			Cookie cookie = new Cookie("visitCount", jsonMap);

			cookie.setPath(request.getContextPath());
			cookie.setMaxAge(24 * 60 * 60); // 1day (초 단위)
			response.addCookie(cookie);
		} else {
			LocalDateTime prevTime = LocalDateTime.parse(visitMap.get(no), formatter);
			boolean isOneDayPassed = now.isAfter(prevTime.plusDays(1));

			if (isOneDayPassed) {
				visitMap.put(no, formatNow);
				Cookie cookie = new Cookie("visitCount", objectMapper.writeValueAsString(visitMap));

				cookie.setPath(request.getContextPath());
				cookie.setMaxAge(24 * 60 * 60); // 1day (초 단위)
				response.addCookie(cookie);
			}
		}
		// 조회수 처리 -------- end

		request.getRequestDispatcher("/WEB-INF/views/board/view.jsp").forward(request, response);
	}

}
