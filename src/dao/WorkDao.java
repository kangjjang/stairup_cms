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

public class WorkDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public WorkDao() throws SQLException{
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
	 * 2015-05-07 ksy 회원별 총 걸음 total 가져오기
	 */
	public ArrayList<MemberVO>selectMemberWork()throws SQLException{
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT									\n");
		sql.append("		MEM_SEQ_NO,						\n");
		sql.append("		COUNT(*) AS CNT,					\n");
		sql.append("(SELECT COUNT(*) FROM TB_BEACON_LOG WHERE CHK = 1 AND CRT_DATE > CURDATE()) AS ONN,				\n");
		sql.append("(SELECT COUNT(*) FROM TB_BEACON_LOG WHERE CHK = 9999 AND CRT_DATE > CURDATE()) AS OFF				\n");
		sql.append("FROM									\n");
		sql.append("		TB_BEACON_LOG					\n");
/*		sql.append("WHERE									\n");
		sql.append("		CRT_DATE > CURDATE()			\n");*/
		sql.append("GROUP BY 								\n");
		sql.append("			MEM_SEQ_NO					\n");

		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setFloorCnt(rs.getInt("CNT"));
				vo.setUpCnt(rs.getInt("ONN"));
				vo.setDownCnt(rs.getInt("OFF"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("WorkDao selectMemberWork ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-07 ksy 걸음수 회원 테이블에 업데이트
	 * memSeqNo : 회원 seqno
	 * cnt : 총 오늘 걸음수
	 * upCnt : 오늘 오른 층수
	 * downCnt : 오늘 내려간 층수
	 */
	public int updateMemberWork(int memSeqNo, int cnt, int upCnt, int downCnt)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MEMBER				\n");
		sql.append("SET								\n");
		sql.append("		WORK_CNT  = ?,			\n");
		sql.append("		WORK_UP   = ?,			\n");
		sql.append("		WORK_DOWN = ?			\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");

		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, cnt);
			pstmt.setInt(2, upCnt);
			pstmt.setInt(3, downCnt);
			pstmt.setInt(4, memSeqNo);
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
			//////System.out.println("workDao updateMemberWork ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-08 ksy 회원별 총걸음수 오늘 걸음수 저장
	 */
	/*public int insertMemberWork(int memSeqNo, int totalWork, int toDayUp, int toDayDown)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT	INTO							\n");
		sql.append("				TB_MEM_WORK				\n");
		sql.append("(								\n");
		sql.append("		WORK_CNT, WORK_UP, WORK_DOWN, MEM_SEQ_NO, CRT_DATE						\n");
		sql.append(")								\n");
		sql.append("VALUES(								\n");
		sql.append("		?, ?, ?, ?, NOW						\n");
		sql.append(")								\n");
		
		
	}*/
}
