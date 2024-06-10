package com.poscodx.mysite.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.poscodx.mysite.service.BoardService;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.Page;
import com.poscodx.mysite.vo.PageResult;
import com.poscodx.mysite.vo.UserVo;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {
	@InjectMocks
	BoardController boardController;

	@Mock
	BoardService boardService;

	MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
	}

	@Test
	public void boardListTest() throws Exception {
		// given
		List<BoardVo> list = new ArrayList<>(List.of(new BoardVo(), new BoardVo()));
		PageResult pageResult = new PageResult(1, 2, "");
		Map<String, Object> map = Map.of("list", list, "pageResult", pageResult);

		BDDMockito.given(boardService.getContentsList(ArgumentMatchers.any())).willReturn(map);

		// when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/board"));

		// then
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("list"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pageResult"))
				.andExpect(MockMvcResultMatchers.model().attribute("list", Matchers.hasSize(2)));

		Mockito.verify(boardService).getContentsList(ArgumentMatchers.any());
	}

	@Test
	public void boardWriteGetTest() throws Exception {
		// given
		Page page = new Page(2, 5);
		page.setQuery("hello");

		// when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/board/write?pageNo=2&query=hello"));

		// then
		resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(model().attributeExists("page"))
				.andExpect(model().attribute("page", Matchers.hasProperty("pageNo", Matchers.is(2))))
				.andExpect(model().attribute("page", Matchers.hasProperty("query", Matchers.is("hello"))))
				.andExpect(forwardedUrl("board/write"));
	}

	@Test
	public void boardWriterPostTest() {
		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("authUser", new UserVo());
		
		BoardVo vo = new BoardVo();
		vo.setTitle("dooly");
		vo.setContents("hello world");
		
		// when
		
		// then
		
	}

}
