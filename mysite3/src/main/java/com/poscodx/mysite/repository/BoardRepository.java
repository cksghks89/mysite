package com.poscodx.mysite.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.Page;

@Repository
public class BoardRepository {
	private SqlSession sqlSession;
	
	public BoardRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public List<BoardVo> findAll(Page page) {
		Map<String, Object> map = Map.of("query", page.getQuery(), "offset", (page.getPageNo() - 1) * page.getListSize(), "listSize", page.getListSize());
		return sqlSession.selectList("board.findAll", map);
	}

	public Integer countQuery(Page page) {
		return sqlSession.selectOne("board.countQuery", page);
	}

	public int insert(BoardVo vo) {
		return sqlSession.insert("board.insert", vo);
	}

	public BoardVo findByNo(Long no) {
		return sqlSession.selectOne("board.findByNo", no);
	}

	public int update(BoardVo vo) {
		return sqlSession.update("board.update", vo);
	}

	public int deleteByNoAndUserNo(Long no, Long userNo) {
		return sqlSession.delete("board.deleteByNoAndUserNo", Map.of("no", no, "userNo", userNo));
	}

	public int addViewCount(BoardVo boardVo) {
		return sqlSession.update("board.addViewCount", boardVo);
	}

	public BoardVo findByNoAndUserNo(Long no, Long userNo) {
		return sqlSession.selectOne("board.findByNoAndUserNo", Map.of("no", no, "userNo", userNo));
	}

	public void updateOrder(BoardVo vo) {
		sqlSession.update("board.updateOrder", vo);
	}

}
