package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import util.ConnectionUtil;
import vo.AdminVO;

import com.mysql.jdbc.Statement;

public class AdminDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public AdminDao() throws SQLException{
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
	 * 2016-08-10 CMS 관리자 리스트
	 */
	public ArrayList<AdminVO> selectCmsAdminList(int pageno, int rowSize, String searchType, String keyword)throws SQLException{
		ArrayList<AdminVO> list = new ArrayList<AdminVO>();
		int cnt = 0;
		int page = (pageno -1)*rowSize;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																							\n");
		sql.append("		ADMIN_SEQ_NO, MEM_ID, ADMIN_AFFILIATION, MEM_NAME, ROLE, MEM_PHONE, CRT_DATE												\n");
		sql.append("FROM																							\n");
		sql.append("		TB_ADMIN																				\n");
		if(searchType.equals("0")){
			sql.append("	WHERE MEM_NAME LIKE CONCAT('%',?,'%')										 												\n");
		}else if(searchType.equals("1")){
			sql.append("	WHERE ADMIN_AFFILIATION LIKE CONCAT('%',?,'%')											\n");
		}else if(searchType.equals("2")){
			sql.append("	WHERE MEM_PHONE LIKE CONCAT('%',?,'%')													\n");
		}
		sql.append("ORDER BY																						\n");
		sql.append("		ADMIN_SEQ_NO DESC 																		\n");
		sql.append("LIMIT	?,?																						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(searchType != null && searchType != ""){
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				AdminVO vo = new AdminVO();
				vo.setNo(rs.getInt("ADMIN_SEQ_NO"));
				vo.setMemberId(rs.getString("MEM_ID"));
				vo.setMemberName(rs.getString("MEM_NAME"));
				vo.setAdminAffiliation(rs.getString("ADMIN_AFFILIATION"));
				vo.setMemberPhone(rs.getString("MEM_PHONE"));
				vo.setMemberRole(rs.getString("ROLE"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao selectCmsMemberList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return list;
	}
	
	public int cntTotalAdmin(String searchType, String keyword) throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																							\n");
		sql.append("		COUNT(1) AS CNT																			\n");
		sql.append("FROM																							\n");
		sql.append("		TB_ADMIN																				\n");
		if(searchType.equals("0")){
			sql.append("	WHERE MEM_NAME LIKE CONCAT('%',?,'%')										 												\n");
		}else if(searchType.equals("1")){
			sql.append("	WHERE ADMIN_AFFILIATION LIKE CONCAT('%',?,'%')											\n");
		}else if(searchType.equals("2")){
			sql.append("	WHERE MEM_PHONE LIKE CONCAT('%',?,'%')													\n");
		}
		try {
			pstmt = conn.prepareStatement(sql.toString());
			if(searchType != null && searchType != ""){
				pstmt.setString(++cnt, keyword);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt("CNT");
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao cntTotalMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
		}
		
		
		return result;
	}
	
	/*
	 * 어드민 소속 이름 가져오기
	 */
	
	public String selectAdminAffiliation(int affiliationSeq) throws SQLException{
		String result = "";
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																							\n");
		sql.append("		AFFILIATION_NAME																		\n");
		sql.append("FROM																							\n");
		sql.append("		TB_AFFILIATION																				\n");
		sql.append("WHERE																							\n");
		sql.append("		SEQ_NO = ?																							\n");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, affiliationSeq);
			rs = pstmt.executeQuery();
			
			//System.out.println(pstmt.toString());
			
			if (rs.next()) {
				result = rs.getString("AFFILIATION_NAME");
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao selectAdminAffiliation ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
		}
		return result;
	}
	/*
	 * 어드민 상세정보
	 */
	public AdminVO selectMember(int no) throws SQLException{
		AdminVO vo = new AdminVO();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ADMIN_SEQ_NO, ADMIN_AFFILIATION, MEM_ID, MEM_PW, MEM_NAME, MEM_PHONE, MEM_EMAIL, ROLE					\n");
		sql.append("	, date_format(CRT_DATE, '%Y.%m.%d') as CRT_DATE					\n");
		sql.append("	, date_format(UDT_DATE, '%Y.%m.%d') as UDT_DATE					\n");
		sql.append("FROM TB_ADMIN														\n");
		sql.append("WHERE ADMIN_SEQ_NO = ?														\n");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, no);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				vo.setNo(rs.getInt("ADMIN_SEQ_NO"));
				vo.setMemberId(rs.getString("MEM_ID"));
				vo.setPassword(rs.getString("MEM_PW"));
				vo.setAdminAffiliation(rs.getString("ADMIN_AFFILIATION"));
				vo.setMemberName(rs.getString("MEM_NAME"));
				vo.setMemberPhone(rs.getString("MEM_PHONE"));
				vo.setMemberEmail(rs.getString("MEM_EMAIL"));
				vo.setMemberRole(rs.getString("ROLE"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setUdtDate(rs.getString("UDT_DATE"));
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao selectMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	
	/*
	 * 어드민 등록
	 */
	public int insertMember(int affiliationSeq, String adminId, String adminPw, String adminName, String adminPhone, String affiliationName) throws SQLException{
		StringBuffer sql = new StringBuffer();
		int result = 0;
		sql.append("INSERT INTO TB_ADMIN( ROLE, MEM_ID, MEM_PW, MEM_NAME, MEM_PHONE, ADMIN_AFFILIATION			\n");
		sql.append("	, CRT_DATE, UDT_DATE)										\n");
		sql.append("	   VALUES(?, ?, ?, ?, ?, ?, now(), now()) 					\n");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, affiliationSeq);
			pstmt.setString(2, adminId);
			pstmt.setString(3, adminPw);
			pstmt.setString(4, adminName);
			pstmt.setString(5, adminPhone);
			pstmt.setString(6, affiliationName);

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao insertMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}

	/*
	 * 관리자 수정
	 */
	public int updateAdmin(int adminSeq, String memberName, String memberPhone, String affiliationName, int memberRole) throws SQLException{
		StringBuffer sql = new StringBuffer();
		int result = 0;
		int cnt = 0;
		sql.append("UPDATE TB_ADMIN														\n");
		sql.append("SET MEM_NAME = ?, MEM_PHONE = ?, ADMIN_AFFILIATION = ?, ROLE = ? 			\n");
		sql.append("WHERE ADMIN_SEQ_NO = ?													\n");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, memberName);
			pstmt.setString(++cnt, memberPhone);
			pstmt.setString(++cnt, affiliationName);
			pstmt.setInt(++cnt, memberRole);
			pstmt.setInt(++cnt, adminSeq);

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao updateMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 관리자 삭제
	 */
	public int deleteMember(int no) throws SQLException{
		StringBuffer sql = new StringBuffer();
		int result = 0;
		sql.append("DELETE FROM TB_ADMIN		\n");
		sql.append("WHERE ADMIN_SEQ_NO = ?			\n");
		
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, no);

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao deleteMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	//기존 DB 암호화 하기 위한 업데이트
	public int updatemMemberPw(String id, String password, int memSeqNo) throws SQLException{
		StringBuffer sql = new StringBuffer();
		int result = 0;
		sql.append("UPDATE TB_MEMBER	 SET											\n");
		sql.append(" MEM_PW = ?	\n");
		sql.append("	, CHG_DATE = now() 												\n");
		sql.append("WHERE MEM_SEQ_NO = ? AND MEM_ID = ?	AND DEL_YN ='N'										\n");
		try {
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, password);
			pstmt.setInt(2, memSeqNo);
			pstmt.setString(3, id);

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao updatemMemberPw ERROR : "+e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	//admin 계정 암호화
	public int updateAdminPw(String id, String pw, int no) throws SQLException{
		StringBuffer sql = new StringBuffer();
		int result = 0;
		sql.append("UPDATE TB_ADMIN														\n");
		sql.append("SET  PWD = ? 		\n");
		sql.append("	, UDT_DATE = now() 												\n");
		sql.append("WHERE SEQ_NO = ? AND ID = ?												\n");
		try {

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, pw);
			pstmt.setInt(2, no);
			pstmt.setString(3, id);

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao updateAdminPw ERROR : "+e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}

	public AdminVO loginMember(String id, String password) throws SQLException{
		AdminVO vo = new AdminVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ADMIN_SEQ_NO, MEM_ID, MEM_EMAIL					\n");
		sql.append("	, ROLE												\n");
		sql.append("	, date_format(CRT_DATE, '%Y.%m.%d') as CRT_DATE		\n");
		sql.append("	, date_format(UDT_DATE, '%Y.%m.%d') as UDT_DATE		\n");
		sql.append("FROM TB_ADMIN											\n");
		sql.append("WHERE MEM_ID = ? AND MEM_PW = ?							\n");
		
		/*sql.append("SELECT ADMIN_SEQ_NO, MEM_ID, MEM_PW 	\n");
		sql.append("FROM TB_ADMIN  								\n");
		sql.append("WHERE MEM_ID = ? AND MEM_PW = ?					\n");*/
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, password);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				//////System.out.println("rs.next");
				vo.setNo(rs.getInt("ADMIN_SEQ_NO"));
				vo.setMemberName(rs.getString("MEM_ID"));
				vo.setMemberEmail(rs.getString("MEM_EMAIL"));
				vo.setMemberRole(rs.getString("ROLE"));
				
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao loginMember ERROR : "+e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
}
