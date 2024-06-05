package com.poscodx.mysite.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.poscodx.mysite.repository.BoardRepository;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.Page;
import com.poscodx.mysite.vo.PageResult;

@Service
public class BoardService {
	private BoardRepository boardRepository;
	
	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}
	
	public void addContents(BoardVo vo) {
		if (vo.getgNo() != null) {
			boardRepository.updateOrder(vo);
			vo.setoNo(vo.getoNo() + 1);
			vo.setDepth(vo.getDepth() + 1);
		}
		boardRepository.insert(vo);
	}
	
	public BoardVo getContents(Long no) {
		BoardVo vo = boardRepository.findByNo(no);
		if (vo != null) {
			boardRepository.addViewCount(vo);
		}
		return vo;
	}
	
	public BoardVo getContents(Long boardNo, Long userNo) {
		return boardRepository.findByNoAndUserNo(boardNo, userNo);
	}
	
	public void updateContents(BoardVo vo) {
		boardRepository.update(vo);
	}
	
	public void deleteContents(Long boardNo, Long no) {
		boardRepository.deleteByNoAndUserNo(boardNo, no);
	}
	
	public Map<String, Object> getContentsList(Page page) {
		List<BoardVo> list = boardRepository.findAll(page);
		
		int count = boardRepository.countQuery(page);
		PageResult pageResult = new PageResult(page.getPageNo(), count, page.getQuery());
		
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("pageResult", pageResult);
		
		return map;
	}
	
	public void addViewCount(BoardVo vo) {
		boardRepository.addViewCount(vo);
	}
}
