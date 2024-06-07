package com.poscodx.mysite.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.poscodx.mysite.service.BoardService;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.Page;
import com.poscodx.mysite.vo.PageResult;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {
	@InjectMocks
	BoardController boardController;

	@Mock
	BoardService boardService;

	MockMvc mockMvc;

	@BeforeEach
	public void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
	}

	@Test
	public void boardListTest() throws Exception {
		// given
		List<BoardVo> list = new ArrayList<>(List.of(new BoardVo(), new BoardVo()));
		PageResult pageResult = new PageResult(1, 2, "");
		Map<String, Object> map = Map.of("list", list, "pageResult", pageResult);

		BDDMockito.given(boardService.getContentsList(ArgumentMatchers.any())).willReturn(map);

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.get("/board"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("list"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pageResult"))
				.andExpect(MockMvcResultMatchers.model().attribute("list", Matchers.hasSize(2)));

		Mockito.verify(boardService).getContentsList(ArgumentMatchers.any());
	}
	
	
	@Test
	public void boardWriteFormTest() throws Exception {
		//given
		Page page = new Page(2, 5);
		page.setQuery("hello");
		
		// when, then
		mockMvc.perform(MockMvcRequestBuilders.get("/write"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("page"));
	}

}
