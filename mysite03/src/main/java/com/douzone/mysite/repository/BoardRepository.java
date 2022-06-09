package com.douzone.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	
	@Autowired
	private SqlSession sqlSession;
	
	
	public List<BoardVo> findAll(int currentPage, String kwd) {
		List<BoardVo> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = getConnection();
			if(kwd != null) {
				String sql = "select a.no, a.title, a.contents, a.hit, date_format(a.reg_date, '%Y-%m-%d %H:%i:%s'), a.g_no, a.o_no, a.depth, b.name, a.user_no" +
							 " from board a, user b" + 
							 " where a.user_no = b.no" + 
							 " and title like ? or contents like ?)" +
							 " order by g_no desc, o_no asc, depth asc" +
							 " limit 5";
				pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, "%" + kwd + "%");
			} else {
				String sql =
						" select a.no, a.title, a.contents, a.hit, a.reg_date, a.g_no, a.o_no, a.depth, b.name, a.user_no " +
						" from board a , user b" + 
						" where a.user_no = b.no" +
						" order by g_no desc, o_no asc, depth asc" +
						" limit ?, 5";			
					pstmt = connection.prepareStatement(sql);
					pstmt.setLong(1, (currentPage - 1) * 5);
			}
				rs = pstmt.executeQuery();


			while (rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				Long hit = rs.getLong(4);
				String date = rs.getString(5);
				Long gNo = rs.getLong(6);
				Long oNo = rs.getLong(7);
				Long depth = rs.getLong(8);
				String user_name = rs.getString(9);
				Long user_no = rs.getLong(10);
				
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hit);
				vo.setReg_date(date);
				vo.setG_no(gNo);
				vo.setO_no(oNo);
				vo.setDepth(depth);
				vo.setUser_no(user_no);
				vo.setUser_name(user_name);

				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@SuppressWarnings("resource")
	public List<BoardVo> findbyNo(int no) {
		List<BoardVo> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;

		try {
			connection = getConnection();

			String sql = "select b.no, b.title, b.contents, b.hit, b.reg_date, " +
						 " b.g_no, b.o_no, b.depth, u.name, b.user_no" + 
						 " from board b , user u" +
						 " where b.user_no = u.no " + 
						 " and b.no = ? ";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, no);

			rs = pstmt.executeQuery();
			
			long hit = 0;
			while(rs.next()) {				
				BoardVo vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setTitle(rs.getString(2));
				vo.setContents(rs.getString(3));
				hit = rs.getInt(4);
				vo.setHit(hit);
				vo.setReg_date(rs.getString(5));
				vo.setG_no(rs.getLong(6));
				vo.setO_no(rs.getLong(7));
				vo.setDepth(rs.getLong(8));
				vo.setUser_name(rs.getString(9));
				vo.setUser_no(rs.getLong(10));

				result.add(vo);
			}

			String update_hit = "update board set hit = ? where no = ?";
			pstmt = connection.prepareStatement(update_hit);
			pstmt.setInt(1, count);
			pstmt.setLong(2, no);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public boolean remove(BoardVo vo) {
		boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = getConnection();

			String sql = "delete from board" + 
						 " where no=?";

			pstmt = connection.prepareStatement(sql);

			pstmt.setLong(1, vo.getNo());

			int count = pstmt.executeUpdate();
			result = count == 1;
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;

	}

	public boolean modify(BoardVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();

			String sql = "update board" + 
						 " set title=?," + 
						 " contents=?" + 
						 " where no=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getNo());

			int count = pstmt.executeUpdate();
			result = count == 1;
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public int countBoard() {
		int result = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql =
				" select count(*)" + 
				" from board";
			pstmt = connection.prepareStatement(sql);
						
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}	

	public void insert(BoardVo vo) {
		Connection connection = null;
		PreparedStatement pstmt = null;
				
		try {
			String title = vo.getTitle();
			String contents = vo.getContents();		
			long userNo = vo.getUser_no();
			
			connection = getConnection();
			
			String sql = "insert into"
					+ " board (title, contents, hit, reg_date, g_no, o_no, depth, user_no) "
					+ " select ?, ?, 0, now(), ifnull(MAX(g_no) + 1, 1) , 1, 0, ? "
					+ " from board";
			pstmt = connection.prepareStatement(sql);			
			pstmt.setString(1, title);
			pstmt.setString(2, contents);
			pstmt.setLong(3, userNo);
								
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}
	
	@SuppressWarnings("null")
	public void insertComent(BoardVo vo) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		
		try {
			String title = vo.getTitle();
			String contents = vo.getContents();
			long g_no = vo.getG_no();
			long o_no = vo.getO_no();
			long depth = vo.getDepth();
			long userNo = vo.getUser_no();
			long no = vo.getNo();
			
			connection = getConnection();
			if(o_no == 1) {
				String sql = "insert into"
						+ " board (title, contents, hit, reg_date, g_no, o_no, depth, user_no) "
						+ " select  ?, ?, 0, now(), ? , MAX(o_no)+1, ?, ? "
						+ " from board where g_no = ?";
				pstmt = connection.prepareStatement(sql);			
				
				pstmt.setString(1, title);
				pstmt.setString(2, contents);
				pstmt.setLong(3, g_no);
				pstmt.setLong(4, depth+1);
				pstmt.setLong(5, userNo);
				pstmt.setLong(6, g_no);
				
			}
			else {
				String sql = "insert into"
						+ " board (title, contents, hit, reg_date, g_no, o_no, depth, user_no) "
						+ " select  ?, ?, 0, now(), ? , ?, ?, ? "
						+ " from board where no = ?";
				pstmt = connection.prepareStatement(sql);			
				
				pstmt.setString(1, title);
				pstmt.setString(2, contents);
				pstmt.setLong(3, g_no);
				pstmt.setLong(4, o_no);
				pstmt.setLong(5, depth+1);
				pstmt.setLong(6, userNo);
				pstmt.setLong(7, no);
				
			}	
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		
	}
	
	private Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mysql://192.168.10.37:3306/webdb?charset=utf8";
			connection = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
		return connection;
	}
}
