package com.poscodx.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.GuestbookVo;
import com.poscodx.mysite.vo.Page;

public class BoardDao {

	public List<BoardVo> findAll(Page page) {
		List<BoardVo> result = new ArrayList<>();

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"select a.no, a.title, a.contents, a.hit, date_format(a.reg_date, '%Y-%m-%d %h:%i:%s') 'reg_date', a.g_no, a.o_no, a.depth, a.user_no, b.name \n"
								+ "from board a join user b on a.user_no = b.no\n"
								+ "where a.title like ? or b.name like ?\n" + "order by g_no desc, o_no asc\n"
								+ "limit ?, 5");) {
			pstmt.setString(1, "%" + page.getQuery() + "%");
			pstmt.setString(2, "%" + page.getQuery() + "%");
			pstmt.setLong(3, (page.getPageNo() - 1) * page.getListSize());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(2);
				Long hit = rs.getLong(4);
				String regDate = rs.getString(5);
				Long gNo = rs.getLong(6);
				Long oNo = rs.getLong(7);
				Long depth = rs.getLong(8);
				Long userNo = rs.getLong(9);
				String userName = rs.getString(10);

				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setgNo(gNo);
				vo.setoNo(oNo);
				vo.setDepth(depth);
				vo.setUserNo(userNo);
				vo.setUserName(userName);

				result.add(vo);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}

		return result;
	}

	public Integer countQuery(Page page) {
		Integer result = null;

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"select count(*) 'count' from board a join user b on a.user_no = b.no where a.title like ? or b.name like ?");) {
			pstmt.setString(1, "%" + page.getQuery() + "%");
			pstmt.setString(2, "%" + page.getQuery() + "%");
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}

		return result;
	}

	public int insert(BoardVo vo) {
		int result = 0;

		try (Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement(
						"insert into board(title, contents, hit, reg_date, g_no, o_no, depth, user_no) "
								+ "values (?, ?, 0, now(), (select ifnull(max(g_no), 0) + 1 from board a), 1, 0, ?)");
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");) {
			pstmt1.setString(1, vo.getTitle());
			pstmt1.setString(2, vo.getContents());
			pstmt1.setLong(3, vo.getUserNo());

			result = pstmt1.executeUpdate();

			ResultSet rs = pstmt2.executeQuery();
			vo.setNo(rs.next() ? rs.getLong(1) : null);
			rs.close();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}

		return result;
	}

	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");

			String url = "jdbc:mariadb://192.168.64.3:3306/webdb?charset=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}

		return conn;
	}

	public BoardVo findByNo(Long no) {
		BoardVo result = null;
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"select no, title, contents, user_no, g_no, o_no, depth, hit from board where no = ?");) {
			pstmt.setLong(1, no);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String title = rs.getString(2);
				String contents = rs.getString(3);
				Long userNo = rs.getLong(4);
				Long gNo = rs.getLong(5);
				Long oNo = rs.getLong(6);
				Long depth = rs.getLong(7);
				Long hit = rs.getLong(8);

				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setUserNo(userNo);
				vo.setgNo(gNo);
				vo.setoNo(oNo);
				vo.setDepth(depth);
				vo.setHit(hit);

				result = vo;
			}
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}

		return result;
	}

	public int update(BoardVo vo) {
		int result = 0;

		try (Connection conn = getConnection();
				PreparedStatement pstmt1 = conn
						.prepareStatement("update board set title = ?, contents = ? where no = ? and user_no = ?");
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");) {
			pstmt1.setString(1, vo.getTitle());
			pstmt1.setString(2, vo.getContents());
			pstmt1.setLong(3, vo.getNo());
			pstmt1.setLong(4, vo.getUserNo());

			result = pstmt1.executeUpdate();

			ResultSet rs = pstmt2.executeQuery();
			vo.setNo(rs.next() ? rs.getLong(1) : null);
			rs.close();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}

		return result;
	}

	public int insertReply(BoardVo boardVo, BoardVo parentVo) {
		Integer result = null;

		try (Connection conn = getConnection();
				PreparedStatement pstmt1 = conn
						.prepareStatement("update board set o_no = o_no + 1 where g_no = ? and o_no > ?");
				PreparedStatement pstmt2 = conn.prepareStatement(
						"insert into board (title, contents, hit, reg_date, g_no, o_no, depth, user_no)\n"
								+ "values (?, ?, 0, now(), ?, ?, ?, ?)");) {
			pstmt1.setLong(1, parentVo.getgNo());
			pstmt1.setLong(2, parentVo.getoNo());
			result = pstmt1.executeUpdate();

			pstmt2.setString(1, boardVo.getTitle());
			pstmt2.setString(2, boardVo.getContents());
			pstmt2.setLong(3, parentVo.getgNo());
			pstmt2.setLong(4, parentVo.getoNo() + 1);
			pstmt2.setLong(5, parentVo.getDepth() + 1);
			pstmt2.setLong(6, boardVo.getUserNo());
			result += pstmt2.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}

		return result;
	}

	public int deleteByNoAndUserNo(Long no, Long userNo) {
		int result = 0;

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from board where no = ? and user_no = ?");) {
			pstmt.setLong(1, no);
			pstmt.setLong(2, userNo);

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}

		return result;
	}

	public int increaseViewCount(BoardVo boardVo) {
		int result = 0;

		try (Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("update board set hit = hit + 1 where no = ?");) {
			pstmt1.setLong(1, boardVo.getNo());
			result = pstmt1.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}

		return result;
	}

}
