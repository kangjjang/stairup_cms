package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import util.ConnectionUtil;
import vo.CityVO;
import vo.MemberVO;

import com.mysql.jdbc.Statement;

public class RankDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public RankDao() throws SQLException {
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
	
	
	private static final String WHEREQUERY [] = {
		 "a.CRT_DATE >= CURDATE() GROUP BY a.MEM_SEQ_NO DESC",
		 	"SUBSTRING(a.CRT_DATE,1,7) = SUBSTRING(curdate(),1,7) AND WEEKOFYEAR(a.CRT_DATE) = WEEKOFYEAR(CURDATE()) GROUP BY a.MEM_SEQ_NO DESC",
	        "SUBSTRING(a.CRT_DATE,1,7) = SUBSTRING(curdate(),1,7) GROUP BY a.MEM_SEQ_NO DESC",
	        "SUBSTRING(a.CRT_DATE,1,4) = SUBSTRING(curdate(),1,4) GROUP BY a.MEM_SEQ_NO DESC"
	};
	
	private static final String WHEREQUERY1 [] = {
		 "a.CRT_DATE >= CURDATE() GROUP BY a.MEM_AFFILIATION DESC",
	        "SUBSTRING(a.CRT_DATE,1,7) = SUBSTRING(curdate(),1,7) AND WEEKOFYEAR(a.CRT_DATE) = WEEKOFYEAR(CURDATE()) GROUP BY b.AFFILIATION_SEQ DESC",
	        "SUBSTRING(a.CRT_DATE,1,7) = SUBSTRING(curdate(),1,7) GROUP BY b.AFFILIATION_SEQ DESC",
	        "SUBSTRING(a.CRT_DATE,1,4) = SUBSTRING(curdate(),1,4) GROUP BY b.AFFILIATION_SEQ DESC"
	};
	
	private static final String WHEREQUERY2 [] = {
		 "a.CRT_DATE >= CURDATE() GROUP BY a.MEM_DEPART DESC",
	        "SUBSTRING(a.CRT_DATE,1,7) = SUBSTRING(curdate(),1,7) AND WEEKOFYEAR(a.CRT_DATE) = WEEKOFYEAR(CURDATE()) GROUP BY a.MEM_DEPART DESC",
	        "SUBSTRING(a.CRT_DATE,1,7) = SUBSTRING(curdate(),1,7) GROUP BY a.MEM_DEPART DESC",
	        "SUBSTRING(a.CRT_DATE,1,4) = SUBSTRING(curdate(),1,4) GROUP BY a.MEM_DEPART DESC"
	};
	
	/*
	 * 2015-05-27 ksy 개인랭킹 리스트
	 * rowCate 1 : 일
	 * rowCate 2 : 주
	 * rowCate 3 : 월
	 * rowCate 4 : 년
	 */
	/*public ArrayList<MemberVO> selectPrivateRank(int memSeqNo, int rowCate, int pageNo, int rowSize, int affiliationGubun)throws SQLException{
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		int row = rowCate -1;
		int result =0;
		int page = (pageNo -1)*rowSize;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																																																	\n");
		sql.append("		m.MEM_SEQ_NO, m.MEM_NAME, m.MEM_PIC, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																																													\n");
		sql.append("		(SELECT COUNT(*) FROM TB_WORLD WHERE MEM_SEQ_NO = m.MEM_SEQ_NO) AS MEM_WORLD,																																													\n");
		sql.append("		(SELECT CITY_NAME FROM TB_CITY WHERE CITY_SEQ_NO =																																																				\n");
		sql.append("		(SELECT CITY_SEQ_NO FROM TB_MEM_STAY WHERE MEM_SEQ_NO = m.MEM_SEQ_NO AND END_DATE IS NULL)) AS CITY_NAME,																																						\n");
		sql.append("		(SELECT COUNT(*)+1 FROM (SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a, TB_MEMBER b WHERE a.MEM_SEQ_NO = b.MEM_SEQ_NO AND b.MEM_RESULT <>'Y' 		\n");
		if(affiliationGubun > 0){
			sql.append("		AND MEM_AFFILIATION = ?																																																															\n");
		}
		sql.append("		AND "+WHEREQUERY[row]+")n WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK,																																																															\n");
		sql.append("		(SELECT COUNTRY_PIC FROM TB_COUNTRY WHERE COUNTRY_SEQ_NO =																																																		\n");
		sql.append("		(SELECT COUNTRY_SEQ_NO FROM TB_CITY WHERE CITY_SEQ_NO = (SELECT CITY_SEQ_NO FROM TB_MEM_STAY WHERE MEM_SEQ_NO = m.MEM_SEQ_NO AND END_DATE IS NULL))) AS COUNTRY_PIC,																							\n");
		sql.append("		(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO =m.MEM_DEPART) AS MEM_DEPART																																												\n");
		if(affiliationGubun > 0){
			sql.append("		,m.MEM_AFFILIATION																																																														\n");
		}
		sql.append(",(SELECT AFFILIATION_NAME FROM TB_AFFILIATION  WHERE SEQ_NO = m.MEM_AFFILIATION) AS AFF_NAME \n");
		sql.append("FROM																																																																	\n");
		sql.append("		(SELECT MEM_SEQ_NO, MEM_DEPART, MEM_NAME, MEM_PIC, MEM_AFFILIATION FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																																										\n");
		sql.append("LEFT OUTER JOIN																																																															\n");
		sql.append("		(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a WHERE "+WHEREQUERY[row]+")g																																								\n");
		sql.append("ON																																																																		\n");
		sql.append("		m.MEM_SEQ_NO = g.MEM_SEQ_NO																																																										\n");
		if(affiliationGubun > 0){
			sql.append("		WHERE   m.MEM_AFFILIATION = ?																																																														\n");
		}
		sql.append("ORDER BY																																																																\n");
		sql.append("		m.MEM_SEQ_NO = ? DESC, RANK ASC, MEM_NAME ASC																																																					\n");
		sql.append("LIMIT ?,?																																																																\n");
		
		
		try{
				pstmt = conn.prepareStatement(sql.toString());
				if(affiliationGubun > 0){
					pstmt.setInt(++cnt, affiliationGubun);
					pstmt.setInt(++cnt, affiliationGubun);
				}
				pstmt.setInt(++cnt, memSeqNo);
				pstmt.setInt(++cnt, page);
				pstmt.setInt(++cnt, rowSize);
				rs = pstmt.executeQuery();
				//////System.out.println("개인랭킹 : " +pstmt.toString());
				while(rs.next()){
					MemberVO vo = new MemberVO();
					vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
					vo.setMemName(rs.getString("MEM_NAME"));
					vo.setMemPic(rs.getString("MEM_PIC"));
					vo.setWorldCnt(rs.getInt("MEM_WORLD"));
					vo.setRank(rs.getInt("RANK"));
					vo.setMemDepart(rs.getString("MEM_DEPART"));
					vo.setToday(rs.getInt("TODAY_TOTAL"));
					vo.setMemAffiliationName(rs.getString("AFF_NAME"));
					CityVO civo = vo.getCityVo();
					civo.setCityName(rs.getString("CITY_NAME"));
					civo.setCountryPic(rs.getString("COUNTRY_PIC"));
					list.add(vo);
				}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("RankDao selectPrivateRank ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return list;
	}*/
	
	
	/*
	 * 2015-05-27 ksy 개인랭킹 리스트
	 * rowCate 1 : 일
	 * rowCate 2 : 주
	 * rowCate 3 : 월
	 * rowCate 4 : 년
	 */
	public ArrayList<MemberVO> selectPrivateRank(int memSeqNo, int rowCate, int pageNo, int rowSize, int groupSeq)throws SQLException{
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		int row = rowCate -1;
		int result =0;
		int page = (pageNo -1)*rowSize;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																																																						\n");
		sql.append("		m.MEM_SEQ_NO, m.MEM_NAME, m.MEM_PIC, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																																																		\n");
		sql.append("		(SELECT COUNT(*) FROM TB_WORLD WHERE MEM_SEQ_NO = m.MEM_SEQ_NO) AS MEM_WORLD,																																																		\n");
		sql.append("		(SELECT CITY_NAME FROM TB_CITY WHERE CITY_SEQ_NO =																																																									\n");
		sql.append("		(SELECT CITY_SEQ_NO FROM TB_MEM_STAY WHERE MEM_SEQ_NO = m.MEM_SEQ_NO AND END_DATE IS NULL LIMIT 1)) AS CITY_NAME,																																											\n");
		sql.append("		(SELECT COUNT(*)+1 FROM (SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a LEFT OUTER JOIN TB_MEMBER b ON a.MEM_SEQ_NO = b.MEM_SEQ_NO LEFT OUTER JOIN TB_MEMBER_AFFILIATION c ON b.MEM_SEQ_NO = c.MEMBER_SEQ WHERE b.MEM_RESULT <>'Y' AND c.MAIN_YN <> 'N'			\n");
		if(groupSeq > 0){
			sql.append("		AND c.AFFILIATION_SEQ = ?																																																														\n");
		}
		sql.append("		AND "+WHEREQUERY[row]+")n WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK,																																																															\n");
		sql.append("		(SELECT COUNTRY_PIC FROM TB_COUNTRY WHERE COUNTRY_SEQ_NO =																																																							\n");
		sql.append("		(SELECT COUNTRY_SEQ_NO FROM TB_CITY WHERE CITY_SEQ_NO = (SELECT CITY_SEQ_NO FROM TB_MEM_STAY WHERE MEM_SEQ_NO = m.MEM_SEQ_NO AND END_DATE IS NULL LIMIT 1))) AS COUNTRY_PIC,																												\n");
		sql.append("		(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO =f.DEPART_SEQ) AS MEM_DEPART																																																	\n");
		if(groupSeq > 0){
			sql.append("		,f.AFFILIATION_SEQ																																																																\n");
		}
		sql.append(",(SELECT AFFILIATION_NAME FROM TB_AFFILIATION  WHERE SEQ_NO = f.AFFILIATION_SEQ) AS AFF_NAME 			\n");
		sql.append("FROM																																																																						\n");
		sql.append("		(SELECT MEM_SEQ_NO,  MEM_NAME, MEM_PIC FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																																																			\n");
		sql.append("LEFT OUTER JOIN																																																																				\n");
		sql.append("		(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a WHERE "+WHEREQUERY[row]+")g																																													\n");
		sql.append("ON																																																																							\n");
		sql.append("		m.MEM_SEQ_NO = g.MEM_SEQ_NO																																																															\n");
		sql.append("LEFT OUTER JOIN																																																																				\n");
		sql.append("		(SELECT * FROM TB_MEMBER_AFFILIATION WHERE DEL_YN <> 'Y') f																																																							\n");
		sql.append("ON																																																																							\n");
		sql.append("		m.MEM_SEQ_NO = f.MEMBER_SEQ																																																															\n");
		sql.append("WHERE	 f.MAIN_YN <> 'N'	AND TODAY_TOTAL > 0																																																																\n");
		if(groupSeq > 0){
			sql.append("		AND   f.AFFILIATION_SEQ = ?																																																														\n");
		}
		sql.append("ORDER BY																																																																					\n");
		sql.append("		RANK ASC, MEM_NAME ASC																																																										\n");
		sql.append("LIMIT ?,?																																																																					\n");
		
		
		try{
				pstmt = conn.prepareStatement(sql.toString());
				if(groupSeq > 0){
					pstmt.setInt(++cnt, groupSeq);
					pstmt.setInt(++cnt, groupSeq);
				}
				pstmt.setInt(++cnt, page);
				pstmt.setInt(++cnt, rowSize);
				
				//System.out.println(pstmt);
				rs = pstmt.executeQuery();
				while(rs.next()){
					MemberVO vo = new MemberVO();
					vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
					vo.setMemName(rs.getString("MEM_NAME"));
					vo.setMemPic(rs.getString("MEM_PIC"));
					vo.setWorldCnt(rs.getInt("MEM_WORLD"));
					vo.setRank(rs.getInt("RANK"));
					vo.setMemDepart(rs.getString("MEM_DEPART"));
					vo.setToday(rs.getInt("TODAY_TOTAL"));
					vo.setMemAffiliationName(rs.getString("AFF_NAME"));
					CityVO civo = vo.getCityVo();
					civo.setCityName(rs.getString("CITY_NAME"));
					civo.setCountryPic(rs.getString("COUNTRY_PIC"));
					list.add(vo);
				}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("RankDao selectPrivateRank ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return list;
	}
	
	public MemberVO selectPrivateRankMy(int memSeqNo, int rowCate, int groupSeq)throws SQLException{
		MemberVO vo = new MemberVO();
		int row = rowCate -1;
		int result =0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																																																						\n");
		sql.append("		m.MEM_SEQ_NO, m.MEM_NAME, m.MEM_PIC, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																																																		\n");
		sql.append("		(SELECT COUNT(*) FROM TB_WORLD WHERE MEM_SEQ_NO = m.MEM_SEQ_NO) AS MEM_WORLD,																																																		\n");
		sql.append("		(SELECT CITY_NAME FROM TB_CITY WHERE CITY_SEQ_NO =																																																									\n");
		sql.append("		(SELECT CITY_SEQ_NO FROM TB_MEM_STAY WHERE MEM_SEQ_NO = m.MEM_SEQ_NO AND END_DATE IS NULL LIMIT 1)) AS CITY_NAME,																																											\n");
		sql.append("		(SELECT COUNT(*)+1 FROM (SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a LEFT OUTER JOIN TB_MEMBER b ON a.MEM_SEQ_NO = b.MEM_SEQ_NO LEFT OUTER JOIN TB_MEMBER_AFFILIATION c ON b.MEM_SEQ_NO = c.MEMBER_SEQ WHERE b.MEM_RESULT <>'Y' AND c.MAIN_YN <> 'N'			\n");
		if(groupSeq > 0){
			sql.append("		AND c.AFFILIATION_SEQ = ?																																																														\n");
		}
		sql.append("		AND "+WHEREQUERY[row]+")n WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK,																																																															\n");
		sql.append("		(SELECT COUNTRY_PIC FROM TB_COUNTRY WHERE COUNTRY_SEQ_NO =																																																							\n");
		sql.append("		(SELECT COUNTRY_SEQ_NO FROM TB_CITY WHERE CITY_SEQ_NO = (SELECT CITY_SEQ_NO FROM TB_MEM_STAY WHERE MEM_SEQ_NO = m.MEM_SEQ_NO AND END_DATE IS NULL LIMIT 1))) AS COUNTRY_PIC,																												\n");
		sql.append("		(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO =f.DEPART_SEQ) AS MEM_DEPART																																																	\n");
		if(groupSeq > 0){
			sql.append("		,f.AFFILIATION_SEQ																																																																\n");
		}
		sql.append(",(SELECT AFFILIATION_NAME FROM TB_AFFILIATION  WHERE SEQ_NO = f.AFFILIATION_SEQ) AS AFF_NAME 			\n");
		sql.append("FROM																																																																						\n");
		sql.append("		(SELECT MEM_SEQ_NO,  MEM_NAME, MEM_PIC FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																																																			\n");
		sql.append("INNER JOIN																																																																				\n");
		sql.append("		(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a WHERE "+WHEREQUERY[row]+")g																																													\n");
		sql.append("ON																																																																							\n");
		sql.append("		m.MEM_SEQ_NO = g.MEM_SEQ_NO																																																															\n");
		sql.append("AND																																																																							\n");
		sql.append("		m.MEM_SEQ_NO = ?																																																															\n");
		sql.append("INNER JOIN																																																																				\n");
		sql.append("		(SELECT * FROM TB_MEMBER_AFFILIATION WHERE DEL_YN <> 'Y') f																																																							\n");
		sql.append("ON																																																																							\n");
		sql.append("		m.MEM_SEQ_NO = f.MEMBER_SEQ																																																															\n");
		sql.append("WHERE	 f.MAIN_YN <> 'N'	AND TODAY_TOTAL > 0																																																																\n");
		if(groupSeq > 0){
			sql.append("		AND   f.AFFILIATION_SEQ = ?																																																														\n");
		}
		
		try{
				pstmt = conn.prepareStatement(sql.toString());
				if(groupSeq > 0){
					pstmt.setInt(++cnt, groupSeq);
				}
				pstmt.setInt(++cnt, memSeqNo);
				if(groupSeq > 0){
					pstmt.setInt(++cnt, groupSeq);
				}
				
				//System.out.println(pstmt);
				rs = pstmt.executeQuery();
				if(rs.next()){
					vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
					vo.setMemName(rs.getString("MEM_NAME"));
					vo.setMemPic(rs.getString("MEM_PIC"));
					vo.setWorldCnt(rs.getInt("MEM_WORLD"));
					vo.setRank(rs.getInt("RANK"));
					vo.setMemDepart(rs.getString("MEM_DEPART"));
					vo.setToday(rs.getInt("TODAY_TOTAL"));
					vo.setMemAffiliationName(rs.getString("AFF_NAME"));
					CityVO civo = vo.getCityVo();
					civo.setCityName(rs.getString("CITY_NAME"));
					civo.setCountryPic(rs.getString("COUNTRY_PIC"));
				}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return vo;
	}
	
	/*
	 * 2016-08-07 그룹랭킹
	 * rowCate 1 : 일
	 * rowCate 2 : 주
	 * rowCate 3 : 월
	 * rowCate 4 : 년
	 */
	public ArrayList<MemberVO> selectPrivateGroupRank(int rowCate, int pageNo, int rowSize, int groupSeq, int departSeq, int mode)throws SQLException{
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		int row = rowCate -1;
		int result =0;
		int page = (pageNo -1)*rowSize;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		if(mode == 0){
			//////System.out.println("전체");
			sql.append("SELECT																																																								\n");
			sql.append("	m.SEQ_NO, m.AFFILIATION_NAME, m.AFFILIATION_PIC, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																																		\n");
			sql.append("	(SELECT COUNT(*)+1 FROM (SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a INNER JOIN TB_MEMBER_AFFILIATION b ON a.MEM_SEQ_NO = b.MEMBER_SEQ AND b.DEL_YN <> 'Y' INNER JOIN TB_AFFILIATION c ON b.AFFILIATION_SEQ = c.SEQ_NO AND c.DEL_YN <> 'Y'		\n");
			sql.append("	AND "+WHEREQUERY1[row]+")n WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK																																				\n");
			sql.append("FROM																																												\n");
			sql.append("	(SELECT SEQ_NO, AFFILIATION_NAME, AFFILIATION_PIC FROM TB_AFFILIATION )m																										\n");
			sql.append("LEFT OUTER JOIN																																										\n");
			sql.append("	(SELECT b.AFFILIATION_SEQ, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a LEFT OUTER JOIN TB_MEMBER_AFFILIATION b ON a.MEM_SEQ_NO = b.MEMBER_SEQ WHERE "+WHEREQUERY1[row]+")g										\n");
			sql.append("ON																																													\n");
			sql.append("	m.SEQ_NO = g.AFFILIATION_SEQ																																					\n");
			//sql.append("WHERE 	TODAY_TOTAL > 0																																												\n");
			//sql.append("WHERE 	SEQ_NO <> 137																																												\n");
			sql.append("ORDER BY																																											\n");
			sql.append("  RANK ASC, AFFILIATION_NAME ASC																																\n");
			sql.append("LIMIT ?,?																																											\n");
		}else{
			//////System.out.println("부서");
			sql.append("SELECT																																												\n");
			sql.append("	m.DEPART_SEQ_NO, m.DEPART_NAME, m.AFFILIATION_PIC, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																					\n");
			sql.append("	(SELECT COUNT(*)+1 FROM (SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a, TB_MEMBER b WHERE a.MEM_SEQ_NO = b.MEM_SEQ_NO AND b.MEM_RESULT <>'Y'			\n");
			sql.append("	AND b.MEM_AFFILIATION = ? AND "+WHEREQUERY2[row]+")n WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK																															\n");
			sql.append("	,m.SEQ_NO																																										\n");
			sql.append("FROM																																												\n");
			sql.append("	(SELECT DEPART_SEQ_NO, DEPART_NAME, AFFILIATION_PIC, b.SEQ_NO FROM TB_DEPART a INNER JOIN TB_AFFILIATION b ON a.AFFILIATION_SEQ = b.SEQ_NO)m											\n");
			sql.append("LEFT OUTER JOIN																																										\n");
			sql.append("	(SELECT a.MEM_DEPART, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a WHERE "+WHEREQUERY2[row]+")g									\n");
			sql.append("ON																																													\n");
			sql.append("	m.DEPART_SEQ_NO = g.MEM_DEPART																																					\n");
			sql.append("WHERE   																																											\n");
			sql.append("	m.SEQ_NO = ?	AND TODAY_TOTAL > 0																																									\n");
			sql.append("ORDER BY																																											\n");
			sql.append("	RANK ASC, DEPART_NAME ASC																																\n");
			sql.append("LIMIT ?,?																																											\n");
		}
		try{
				pstmt = conn.prepareStatement(sql.toString());
				if(mode == 0){
					pstmt.setInt(++cnt, page);
					pstmt.setInt(++cnt, rowSize);
				}else{
					pstmt.setInt(++cnt, groupSeq);
					pstmt.setInt(++cnt, groupSeq);
					pstmt.setInt(++cnt, page);
					pstmt.setInt(++cnt, rowSize);
				}
				
				//System.out.println(pstmt);
				rs = pstmt.executeQuery();
				while(rs.next()){
					MemberVO vo = new MemberVO();
					if(mode == 0){
						vo.setMemSeqNo(rs.getInt("SEQ_NO"));
						vo.setMemAffiliationName(rs.getString("AFFILIATION_NAME"));
						vo.setMemPic(rs.getString("AFFILIATION_PIC"));
						vo.setToday(rs.getInt("TODAY_TOTAL"));
						vo.setRank(rs.getInt("RANK"));
					}else{
						vo.setMemSeqNo(rs.getInt("DEPART_SEQ_NO"));
						vo.setMemAffiliationName(rs.getString("DEPART_NAME"));
						vo.setMemPic(rs.getString("AFFILIATION_PIC"));
						vo.setToday(rs.getInt("TODAY_TOTAL"));
						vo.setRank(rs.getInt("RANK"));
					}
					
					
					list.add(vo);
				}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("RankDao selectPrivateGroupRank ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return list;
	}
	
	public MemberVO selectPrivateGroupRankMy(int rowCate, int groupSeq, int departSeq, int mode)throws SQLException{
		MemberVO vo = new MemberVO();
		int row = rowCate -1;
		int result =0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		if(mode == 0){
			//////System.out.println("전체");
			sql.append("SELECT																																																								\n");
			sql.append("	m.SEQ_NO, m.AFFILIATION_NAME, m.AFFILIATION_PIC, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																																		\n");
			sql.append("	(SELECT COUNT(*)+1 FROM (SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a INNER JOIN TB_MEMBER_AFFILIATION b ON a.MEM_SEQ_NO = b.MEMBER_SEQ AND b.DEL_YN <> 'Y' INNER JOIN TB_AFFILIATION c ON b.AFFILIATION_SEQ = c.SEQ_NO AND c.DEL_YN <> 'Y'		\n");
			sql.append("	AND "+WHEREQUERY1[row]+")n WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK																																				\n");
			sql.append("FROM																																												\n");
			sql.append("	(SELECT SEQ_NO, AFFILIATION_NAME, AFFILIATION_PIC FROM TB_AFFILIATION )m																										\n");
			sql.append("INNER JOIN																																										\n");
			sql.append("	(SELECT b.AFFILIATION_SEQ, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a INNER JOIN TB_MEMBER_AFFILIATION b ON a.MEM_SEQ_NO = b.MEMBER_SEQ AND b.AFFILIATION_SEQ = ? WHERE "+WHEREQUERY1[row]+")g										\n");
			sql.append("ON																																													\n");
			sql.append("	m.SEQ_NO = g.AFFILIATION_SEQ																																					\n");
			//sql.append("WHERE 	TODAY_TOTAL > 0																																												\n");
			//sql.append("WHERE 	SEQ_NO <> 137																																												\n");
		}else{
			sql.append("SELECT																																												\n");
			sql.append("	m.DEPART_SEQ_NO, m.DEPART_NAME, m.AFFILIATION_PIC, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																					\n");
			sql.append("	(SELECT COUNT(*)+1 FROM (SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a, TB_MEMBER b WHERE a.MEM_SEQ_NO = b.MEM_SEQ_NO AND b.MEM_RESULT <>'Y'			\n");
			sql.append("	AND b.MEM_AFFILIATION = ? AND "+WHEREQUERY2[row]+")n WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK																															\n");
			sql.append("	,m.SEQ_NO																																										\n");
			sql.append("FROM																																												\n");
			sql.append("	(SELECT DEPART_SEQ_NO, DEPART_NAME, AFFILIATION_PIC, b.SEQ_NO FROM TB_DEPART a , TB_AFFILIATION b WHERE a.AFFILIATION_SEQ = b.SEQ_NO)m											\n");
			sql.append("INNER JOIN																																											\n");
			sql.append("	(SELECT a.MEM_DEPART, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a WHERE MEM_DEPART  = ? AND "+WHEREQUERY2[row]+" )g									\n");
			sql.append("ON																																													\n");
			sql.append("	m.DEPART_SEQ_NO = g.MEM_DEPART																																					\n");
			sql.append("WHERE   																																											\n");
			sql.append("	m.SEQ_NO = ?																																								\n");
		}
		try{
				pstmt = conn.prepareStatement(sql.toString());
				if(mode == 0){
					pstmt.setInt(++cnt, groupSeq); 
				}else{
					pstmt.setInt(++cnt, groupSeq);
					pstmt.setInt(++cnt, departSeq);
					pstmt.setInt(++cnt, groupSeq);
				}
				
				//System.out.println(pstmt);
				rs = pstmt.executeQuery();
				if(rs.next()){
					if(mode == 0){
						vo.setMemSeqNo(rs.getInt("SEQ_NO"));
						vo.setMemAffiliationName(rs.getString("AFFILIATION_NAME"));
						vo.setMemPic(rs.getString("AFFILIATION_PIC"));
						vo.setToday(rs.getInt("TODAY_TOTAL"));
						vo.setRank(rs.getInt("RANK"));
					}else{
						vo.setMemSeqNo(rs.getInt("DEPART_SEQ_NO"));
						vo.setMemAffiliationName(rs.getString("DEPART_NAME"));
						vo.setMemPic(rs.getString("AFFILIATION_PIC"));
						vo.setToday(rs.getInt("TODAY_TOTAL"));
						vo.setRank(rs.getInt("RANK"));
					}
				}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return vo;
	}
	
	
	
	/* 2015-05-27 ksy 임시테이블 생성
	public String creatTemporary(int gubun)throws SQLException{
		int row = gubun-2;
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TEMPORARY TABLE TB_RANK AS								\n");
		sql.append("SELECT															\n");
		sql.append("			MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL			\n");
		sql.append("FROM															\n");
		sql.append("			TB_MASTER											\n");
		sql.append("WHERE		" + QUERY[row] + "									\n");
		
		try {
			pstmt = conn.prepareStatement(sql.toString(),Statement.RETURN_GENERATED_KEYS);
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
			//////System.out.println("임시 : " + pstmt.toString());
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("RankDao selectTemporaryInsert ERROR : " + e);
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				pstmt.close();
		}
		return sql.toString();
	}*/
	
}
