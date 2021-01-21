package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import util.ConnectionUtil;
import vo.GiveVO;

import com.mysql.jdbc.Statement;

public class GiveDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public GiveDao() throws SQLException{
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
	 * 2015-05-08 ksy 진행중 기부 리스트
	 */
	public ArrayList<GiveVO> selectGiveList()throws SQLException{
		ArrayList<GiveVO> list = new ArrayList<GiveVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		GIVE_SEQ_NO, GIVE_AIM, CRT_DATE, END_DATE					\n");
		sql.append("FROM							\n");
		sql.append("		TB_GIVE					\n");
		sql.append("WHERER							\n");
		sql.append("		GIVE_RESULT = 'N'					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				GiveVO vo = new GiveVO();
				vo.setGiveSeqNo(rs.getInt("GIVE_SEQ_NO"));
				vo.setGiveAim(rs.getInt("GIVE_AIM"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setEndDate(rs.getString("END_DATE"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("GiveDao selectGiveList 리스트 ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-08 ksy 해당 기부에 total 걸음수 저장
	 */
	public int updateGiveTotalWork(int giveSeqNo, int totalWork)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_GIVE					\n");
		sql.append("SET								\n");
		sql.append("		TOTAL_STAIRS = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		GIVE_SEQ_NO = ?			\n");
	
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, totalWork);
			pstmt.setInt(2, giveSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("GiveDao updateGiveTotalWork 리스트 ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}

	
}
