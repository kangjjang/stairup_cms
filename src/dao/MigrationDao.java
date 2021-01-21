package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import com.mysql.jdbc.Statement;

import util.ConnectionUtil;
import vo.DepartVO;
import vo.MemberVO;

public class MigrationDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public MigrationDao() throws SQLException{
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
	
	public ArrayList<DepartVO> selectDepartList() throws SQLException{
		ArrayList<DepartVO> list = new ArrayList<DepartVO>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																							\n");
		sql.append("		DEPART_SEQ_NO, DEPART_NAME												\n");
		sql.append("FROM																							\n");
		sql.append("		TB_DEPART2																				\n");
		sql.append("ORDER BY																						\n");
		sql.append("		DEPART_SEQ_NO DESC 																		\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				DepartVO vo = new DepartVO();
				vo.setDepartSeqNo(rs.getInt("DEPART_SEQ_NO"));
				vo.setDepartName(rs.getString("DEPART_NAME"));
				list.add(vo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return list;
	}
	
	public ArrayList<MemberVO> selectMemberList() throws SQLException{
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																							\n");
		sql.append("		MEM_SEQ_NO, MEM_NAME, MEM_DEPART, MEM_NUMBER, MEM_RESULT, MEM_PIC, CRT_DATE												\n");
		sql.append("FROM																							\n");
		sql.append("		TB_MEMBER2																				\n");
		//sql.append("WHERE																							\n");
		//sql.append("		MEM_SEQ_NO = 5																				\n");
		sql.append("ORDER BY																						\n");
		sql.append("		MEM_SEQ_NO 																		\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				MemberVO vo = new MemberVO();
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setDepartSeq(rs.getInt("MEM_DEPART"));
				vo.setMemNumber(rs.getString("MEM_NUMBER"));
				vo.setMemResult(rs.getString("MEM_RESULT"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				list.add(vo);
			
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return list;
	}
	
	public int updateMemberDepart( int memDepartOld, int memDepartNew)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MEMBER2				\n");
		sql.append("SET								\n");
		sql.append("		MEM_DEPART = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_DEPART = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, memDepartNew);
			pstmt.setInt(++cnt, memDepartOld);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	public int updateBeaconLog( int memSeqOld, int memSeqNew)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_BEACON_LOG2				\n");
		sql.append("SET								\n");
		sql.append("		MEM_SEQ_NO = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, memSeqNew);
			pstmt.setInt(++cnt, memSeqOld);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	public int updateMaster( int memSeqOld, int memSeqNew)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MASTER2				\n");
		sql.append("SET								\n");
		sql.append("		MEM_SEQ_NO = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, memSeqNew);
			pstmt.setInt(++cnt, memSeqOld);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	public int updateMemStay( int memSeqOld, int memSeqNew)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MEM_STAY2				\n");
		sql.append("SET								\n");
		sql.append("		MEM_SEQ_NO = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, memSeqNew);
			pstmt.setInt(++cnt, memSeqOld);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	public int updateWorld( int memSeqOld, int memSeqNew)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_WORLD2				\n");
		sql.append("SET								\n");
		sql.append("		MEM_SEQ_NO = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, memSeqNew);
			pstmt.setInt(++cnt, memSeqOld);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	public int updateFriend( int memSeqOld, int memSeqNew)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_FRIEND2				\n");
		sql.append("SET								\n");
		sql.append("		MEM_SEQ_NO = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, memSeqNew);
			pstmt.setInt(++cnt, memSeqOld);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	public int updateFriend2( int memSeqOld, int memSeqNew)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_FRIEND2				\n");
		sql.append("SET								\n");
		sql.append("		HE_SEQ_NO = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		HE_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, memSeqNew);
			pstmt.setInt(++cnt, memSeqOld);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	public int updateLike( int memSeqOld, int memSeqNew)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_LIKE2				\n");
		sql.append("SET								\n");
		sql.append("		MEM_SEQ_NO = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, memSeqNew);
			pstmt.setInt(++cnt, memSeqOld);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	public int updateLike2( int memSeqOld, int memSeqNew)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_LIKE2				\n");
		sql.append("SET								\n");
		sql.append("		TAGET_SEQ_NO = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		TAGET_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, memSeqNew);
			pstmt.setInt(++cnt, memSeqOld);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	
}
