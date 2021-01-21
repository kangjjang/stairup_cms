package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import com.mysql.jdbc.Statement;

import util.ConnectionUtil;
import vo.MemberVO;
import vo.ReviewVO;

public class ReviewDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public ReviewDao() throws SQLException{
		conn = ConnectionUtil.getConnection();	
	}
	
	public void closeConn() throws SQLException{
		  conn.close();
	}
	public static void closeAll (final Object... things) {
	    for (final Object thing : things) {
	        if (null != thing) {
	            try {
	                if (thing instanceof ResultSet) {
	                    try {
	                        ((ResultSet) thing).close ();
	                    } catch (final SQLException e) {
	                        /* No Op */
	                    }
	                }
	                if (thing instanceof Statement) {
	                    try {
	                        ((Statement) thing).close ();
	                    } catch (final SQLException e) {
	                        /* No Op */
	                    }
	                }
	                if (thing instanceof Connection) {
	                    try {
	                        ((Connection) thing).close ();
	                    } catch (final SQLException e) {
	                        /* No Op */
	                    }
	                }
	                if (thing instanceof Lock) {
	                    try {
	                        ((Lock) thing).unlock ();
	                    } catch (final IllegalMonitorStateException e) {
	                        /* No Op */
	                    }
	                }
	                if (thing instanceof PreparedStatement) {
	                    try {
	                        ((PreparedStatement) thing).close ();
	                    } catch (final SQLException e) {
	                        /* No Op */
	                    }
	                }
	            } catch (final RuntimeException e) {
	                /* No Op */
	            }
	        }
	    }
	}
	
	/*
	 * 2015-05-11 ksy 댓글 리스트
	 */
	public ArrayList<MemberVO> selectReview(int gubun, int doSeqNo, int limitGubun)throws SQLException{
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																										\n");
		sql.append("		MEM_PIC, MEM_NAME, a.MEM_SEQ_NO, REVIEW_CONTENT, b.CRT_DATE, REVIEW_SEQ_NO							\n");
		sql.append("FROM																										\n");
		sql.append("		TB_MEMBER a, TB_REVIEW b																			\n");
		sql.append("WHERE																										\n");
		sql.append("		a.MEM_SEQ_NO = b.MEM_SEQ_NO																			\n");
		sql.append("	AND	TAGET_GUBUN = ?																						\n");
		sql.append("	AND TAGET_SEQ_NO = ?																					\n");
		sql.append("ORDER BY 																									\n");
		sql.append("		CRT_DATE DESC																						\n");
		if(limitGubun == 1){
			sql.append("LIMIT 3																										\n");
		}
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, gubun);
			pstmt.setInt(2, doSeqNo);
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				ReviewVO review = vo.getReviewVo();
				review.setCrtDate(rs.getString("CRT_DATE"));
				review.setReviewSeqNo(rs.getInt("REVIEW_SEQ_NO"));
				review.setReviewContent(rs.getString("REVIEW_CONTENT"));
				
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("ReviewDao selectReview ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return list;
	}
	/*
	 * 2015-05-11 ksy 댓글입력
	 * memSeqNo : 내 seq
	 * boardCate : 1 = 기부 , 2 = 회원
	 * boardSeqNo : 상대혹은 기부 seq
	 * content : 내용
	 */
	public int insertReview(int memSeqNo, int boardCate, int boardSeqNo, String content)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO																	\n");
		sql.append("			TB_REVIEW														\n");
		sql.append("(																			\n");
		sql.append("	TAGET_GUBUN, TAGET_SEQ_NO, MEM_SEQ_NO, REVIEW_CONTENT, CRT_DATE			\n");
		sql.append(")																			\n");
		sql.append("VALUES(																		\n");
		sql.append("	?, ?, ?, ?, NOW()														\n");
		sql.append(")																			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, boardCate);
			pstmt.setInt(2, boardSeqNo);
			pstmt.setInt(3, memSeqNo);
			pstmt.setString(4, content);
			result = pstmt.executeUpdate();
			if (result > 0) {
				result = -1;
				try {
					rs = pstmt.getGeneratedKeys();
					if (rs.next())
						result = rs.getInt(1);
				} catch (SQLException e) {
					result = -1;
				}
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("ReviewDao insertReview ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-12 ksy 댓글 수정
	 */
	public int updateReview(int memSeqNo, int boardCate, int boardSeqNo, String content, int conSeqNo)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_REVIEW				\n");
		sql.append("SET								\n");
		sql.append("		REVIEW_CONTENT = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		sql.append("	AND TAGET_GUBUN = ?			\n");
		sql.append("	AND	TAGET_SEQ_NO = ?		\n");
		sql.append("	AND REVIEW_SEQ_NO = ?		\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, content);
			pstmt.setInt(2, memSeqNo);
			pstmt.setInt(3, boardCate);
			pstmt.setInt(4, boardSeqNo);
			pstmt.setInt(5, conSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("ReviewDao updateReview ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2015-05-12 ksy 댓글 삭제
	 */
	public int deleteReview(int memSeqNo, int boardCate, int boardSeqNo, int conSeqNo)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE	FROM						\n");
		sql.append("		TB_REVIEW					\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_SEQ_NO = ?				\n");
		sql.append("	AND	TAGET_GUBUN	= ?				\n");
		sql.append("	AND	TAGET_SEQ_NO = ?			\n");
		sql.append("	AND REVIEW_SEQ_NO = ?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, boardCate);
			pstmt.setInt(3, boardSeqNo);
			pstmt.setInt(4, conSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("ReviewDao deleteReview ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-09-02 리뷰 삭제
	 */
	public int reviewDelete(int reviewSeqNo)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM								\n");
		sql.append("		TB_REVIEW						\n");
		sql.append("WHERE									\n");
		sql.append("		REVIEW_SEQ_NO = ?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, reviewSeqNo);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt);
		}
		
		return result;
	}
}
