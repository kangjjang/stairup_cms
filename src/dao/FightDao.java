package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import util.ConnectionUtil;
import vo.MemberVO;

import com.mysql.jdbc.Statement;

public class FightDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public FightDao() throws SQLException{
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
	 * 2015-05-12 ksy 힘내요 등록
	 */
	public int insertFight(int memSeqNo, int boardCate, int boardSeqNo)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO														\n");
		sql.append("			TB_LIKE												\n");
		sql.append("(																\n");
		sql.append("	MEM_SEQ_NO, TAGET_GUBUN, TAGET_SEQ_NO, CRT_DATE				\n");
		sql.append(")																\n");
		sql.append("VALUES(															\n");
		sql.append("	?, ?, ?, NOW()												\n");
		sql.append(")																\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, boardCate);
			pstmt.setInt(3, boardSeqNo);
			result = pstmt.executeUpdate();
			if (result > 0) {
				result = -1;
				try {
					rs = pstmt.getGeneratedKeys();
					if (rs.next())
						result = rs.getInt(1);
				} catch (SQLException e) {
					result = -1;
				}/*finally{
					closeAll(rs, pstmt);
				}*/
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("FightDao insertFight ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-12 ksy 힘내요 취소
	 */
	public int deleteFight(int memSeqNo, int boardCate, int boardSeqNo)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE	FROM					\n");
		sql.append("			TB_LIKE				\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		sql.append("	AND	TAGET_GUBUN = ?			\n");
		sql.append("	AND TAGET_SEQ_NO = ?		\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, boardCate);
			pstmt.setInt(3, boardSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("FightDao deleteFight ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-12 ksy 힘내요 개수
	 */
	public int selectFightCnt(int boardCate, int boardSeqNo)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		COUNT(*) AS CNT				\n");
		sql.append("FROM								\n");
		sql.append("		TB_LIKE						\n");
		sql.append("WHERE								\n");
		sql.append("		TAGET_GUBUN = ?				\n");
		sql.append("	AND TAGET_SEQ_NO = ?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, boardCate);
			pstmt.setInt(2, boardSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("FightDao selectFightCnt ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-12 ksy 힘내요 리스트
	 */
	public ArrayList<MemberVO> selectFight(int gubun, int memSeqNo, int pageNo, int rowSize)throws SQLException{
		int page = (pageNo -1) * rowSize;
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT										\n");
		sql.append("		MEM_PIC, MEM_NAME, b.MEM_SEQ_NO		\n");
		sql.append("FROM										\n");
		sql.append("		TB_LIKE a, TB_MEMBER b				\n");
		sql.append("WHERE										\n");
		sql.append("		a.MEM_SEQ_NO = b.MEM_SEQ_NO			\n");
		sql.append("	AND a.TAGET_SEQ_NO = ?					\n");
		sql.append("	AND a.TAGET_GUBUN  = ?					\n");
		sql.append("ORDER BY									\n");
		sql.append("		a.CRT_DATE DESC						\n");
		sql.append("LIMIT ?,?									\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, gubun);
			pstmt.setInt(3, page);
			pstmt.setInt(4, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("FightDao selectFight ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return list;
	}
}
