package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import util.ConnectionUtil;
import vo.AffiliationVO;
import vo.DepartVO;

import com.mysql.jdbc.Statement;

public class AffiliationDao {

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public AffiliationDao() throws SQLException{
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
	 * 2016-07-20 CMS 소속 리스트
	 */
	public ArrayList<AffiliationVO> affiliationList(int pageno, int rowSize,String searchType, String keyword) throws SQLException{
		int cnt =0;
		int page = (pageno -1)*rowSize;
		ArrayList<AffiliationVO> list = new ArrayList<AffiliationVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT											\n");
		sql.append("		SEQ_NO, AFFILIATION_NAME, AFFILIATION_PIC										\n");
		sql.append("FROM											\n");
		sql.append("		TB_AFFILIATION							\n");
		sql.append("WHERE	DEL_YN <> 'Y'							\n");
		if(searchType.equals("1")){
			sql.append("AND		AFFILIATION_NAME LIKE CONCAT('%',?,'%')		\n");
		}
		sql.append("ORDER BY						\n");
		sql.append("		SEQ_NO DESC			\n");
		sql.append("LIMIT ?,?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(searchType.equals("1")){
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				AffiliationVO vo = new AffiliationVO();
				vo.setSeqNo(rs.getInt("SEQ_NO"));
				vo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setAffiliationPic(rs.getString("AFFILIATION_PIC"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao departList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}

		return list;
	}
	
	/*
	 * 2016-07-20 CMS 셀렉트 박스 전용
	 */
	public ArrayList<AffiliationVO> affiliationList() throws SQLException{
		ArrayList<AffiliationVO> list = new ArrayList<AffiliationVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																					\n");
		sql.append("		SEQ_NO, AFFILIATION_NAME														\n");
		sql.append("FROM																					\n");
		sql.append("		TB_AFFILIATION																	\n");
		sql.append("WHERE	DEL_YN <> 'Y'																	\n");
		sql.append("ORDER BY																				\n");
		sql.append("		AFFILIATION_NAME ASC																		\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				AffiliationVO vo = new AffiliationVO();
				vo.setSeqNo(rs.getInt("SEQ_NO"));
				vo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao departList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}

		return list;
	}
	
	/*
	 * 2016-07-20 CMS 소속 총 수량
	 */
	public int affiliationCnt()throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT						\n");
		sql.append("		COUNT(*) AS CNT		\n");
		sql.append("FROM						\n");
		sql.append("		TB_AFFILIATION			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("affiliationCntDao affiliationCnt ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	/*
	 * 2016-07-20 CMS 소속 상세 화면
	 */
	public AffiliationVO affiliationSelectView(int affiliationSeqNo)throws SQLException{
		AffiliationVO vo = new AffiliationVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																						\n");
		sql.append("		SEQ_NO, AFFILIATION_NAME, AFFILIATION_PIC						\n");
		sql.append("FROM																						\n");
		sql.append("		TB_AFFILIATION																			\n");
		sql.append("WHERE																						\n");
		sql.append("		SEQ_NO = ?																	\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, affiliationSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setSeqNo(rs.getInt("SEQ_NO"));
				vo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setAffiliationPic(rs.getString("AFFILIATION_PIC"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("affiliationCntDao affiliationCnt ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return vo;
	}
	
	/*
	 * 2016-07-20 CMS 소속 정보 변경
	 */
	public int affiliationUpdate(int affiliationSeqNo, String affiliationName, String listImgName)throws SQLException{
		int cnt =0;
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE											\n");
		sql.append("		TB_AFFILIATION								\n");
		sql.append("SET												\n");
		sql.append("		AFFILIATION_NAME = ?							\n");
		if(listImgName !=null&&!listImgName.equals(""))sql.append("	,	AFFILIATION_PIC = ?							\n");
		sql.append("WHERE											\n");
		sql.append("		SEQ_NO = ?						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, affiliationName);
			if(listImgName != null&&!listImgName.equals(""))pstmt.setString(++cnt, listImgName);
			pstmt.setInt(++cnt, affiliationSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("affiliationCntDao affiliationCnt ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
		
	}
	
	/*
	 * 2016-07-20 CMS 소속 등록
	 */
	public int insertDepart(String affiliationName, String fileName)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO													\n");
		sql.append("			TB_AFFILIATION									\n");
		sql.append("(															\n");
		sql.append("	AFFILIATION_NAME, AFFILIATION_PIC, DEL_YN, CRT_DATE			\n");
		sql.append(")															\n");
		sql.append("VALUES(														\n");
		sql.append("	?, ?, 'N', NOW()												\n");
		sql.append(")															\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, affiliationName);
			pstmt.setString(2, fileName);
			
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
			//////System.out.println("affiliationCntDao affiliationCnt ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	
}
