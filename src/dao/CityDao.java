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
import vo.CityVO;
import vo.DepartVO;
import vo.MemberVO;

import com.mysql.jdbc.Statement;
public class CityDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public CityDao() throws SQLException{
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
	
	private static final String TABLE [] = {
		"TB_COUNTRY",
		"TB_CITY",
	};
	/*
	 * 2015-06-04 ksy 처음 도시 국가 정보
	 */
	public CityVO selectfirstCity()throws SQLException{
		CityVO vo = new CityVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																											\n");
		sql.append("		CITY_NAME,																								\n");
		sql.append("		(SELECT COUNTRY_NAME FROM TB_COUNTRY WHERE COUNTRY_SEQ_NO = a.COUNTRY_SEQ_NO) AS COUNTRY_NAME,			\n");
		sql.append("		(SELECT COUNTRY_PIC FROM TB_COUNTRY WHERE COUNTRY_SEQ_NO = a.COUNTRY_SEQ_NO) AS COUNTRY_PIC 			\n");
		sql.append("FROM																											\n");
		sql.append("		TB_CITY a																								\n");
		sql.append("WHERE																											\n");
		sql.append("		ORDERLY = 1																								\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setCityName(rs.getString("CITY_NAME"));
				vo.setCountryName(rs.getString("COUNTRY_NAME"));
				vo.setCountryPic(rs.getString("COUNTRY_PIC"));
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectfirstCity ERROR : " +e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
		
	}
	/*
	 * 2015-05-06 ksy 도시별시장 정보 리스트
	 */
	public ArrayList<CityVO> selectCityMayorList(int pageNo, int rowSize, int affiliationSeq)throws SQLException{
		int page = (pageNo - 1) * rowSize;
		int cnt = 0 ;
		ArrayList<CityVO> list = new ArrayList<CityVO>();
		StringBuffer sql = new StringBuffer();
		if(affiliationSeq > 0){
			sql.append("SELECT																							\n");
			sql.append("		CITY_NAME,	MEM_NAME, a.MEM_SEQ_NO, COUNTRY_PIC, p.CITYDATE, DEPART_NAME, MEM_PIC																		\n");
			sql.append("FROM																							\n");
			sql.append("		TB_MAYOR a																				\n");
			sql.append("LEFT OUTER JOIN  																				\n");
			sql.append("		TB_CITY b																				\n");
			sql.append("		ON a.CITY_SEQ_NO = b.CITY_SEQ_NO														\n");
			sql.append("LEFT OUTER JOIN																					\n");
			sql.append("				TB_COUNTRY d																	\n");
			sql.append("		ON b.COUNTRY_SEQ_NO = d.COUNTRY_SEQ_NO													\n");
			sql.append("LEFT OUTER JOIN 																				\n");
			sql.append("				TB_MEMBER c																		\n");
			sql.append("		ON a.MEM_SEQ_NO = c.MEM_SEQ_NO															\n");
			sql.append("LEFT OUTER JOIN		(SELECT																		\n");
			sql.append("		TO_DAYS(CURDATE()) - TO_DAYS(CRT_DATE) AS CITYDATE, MEM_SEQ_NO, CITY_SEQ_NO				\n");
			sql.append("		FROM TB_MAYOR) p																		\n");
			sql.append("		ON p.MEM_SEQ_NO = a.MEM_SEQ_NO AND p.CITY_SEQ_NO = a.CITY_SEQ_NO						\n");
			sql.append("LEFT OUTER JOIN																					\n");
			sql.append("				TB_DEPART e																		\n");
			sql.append("		ON c.MEM_DEPART = e.DEPART_SEQ_NO														\n");
			sql.append("WHERE	DEL_YN <>'Y'																			\n");
			sql.append("AND	  a.AFFILIATION_NO = ?																		\n");
			sql.append("AND	  MEM_RESULT <>'Y'																			\n");
			sql.append("ORDER BY b.ORDERLY ASC																			\n");
			//sql.append("GROUP BY a.CITY_SEQ_NO																			\n");
			sql.append("LIMIT ?,?																						\n");
		}else{
			sql.append("SELECT b.CITY_NAME																							\n");
			sql.append("		, c.MEM_NAME																					\n");
			sql.append("		, a.MEM_SEQ_NO																					\n");
			sql.append("		, d.COUNTRY_PIC																					\n");
			sql.append("		, TO_DAYS(CURDATE()) - TO_DAYS(a.CRT_DATE) AS CITYDATE																					\n");
			sql.append("		, e.DEPART_NAME																					\n");
			sql.append("		, c.MEM_PIC																								\n");
			sql.append("	FROM (SELECT CITY_SEQ_NO, MIN(WALK_PERIOD) WALK_PERIOD														\n");
			sql.append("			FROM TB_MAYOR																						\n");
			sql.append("			WHERE DEL_YN <> 'Y'																					\n");
			sql.append("			AND CITY_SEQ_NO > 0																					\n");
			sql.append("			GROUP BY CITY_SEQ_NO																				\n");
			sql.append("		 ) p																									\n");
			sql.append("INNER JOIN TB_MAYOR a																							\n");
			sql.append("	ON p.CITY_SEQ_NO = a.CITY_SEQ_NO																			\n");
			sql.append("	AND p.WALK_PERIOD = a.WALK_PERIOD																			\n");
			sql.append("LEFT OUTER JOIN TB_CITY b																						\n");
			sql.append("	ON a.CITY_SEQ_NO = b.CITY_SEQ_NO																			\n");
			sql.append("LEFT OUTER JOIN TB_COUNTRY d																					\n");
			sql.append("	ON b.COUNTRY_SEQ_NO = d.COUNTRY_SEQ_NO																		\n");
			sql.append("LEFT OUTER JOIN TB_MEMBER c																						\n");
			sql.append("	ON a.MEM_SEQ_NO = c.MEM_SEQ_NO																				\n");
			sql.append("LEFT OUTER JOIN TB_DEPART e																						\n");
			sql.append("	ON c.MEM_DEPART = e.DEPART_SEQ_NO																			\n");
			sql.append("WHERE a.DEL_YN <> 'Y'																							\n");
			sql.append("AND	  MEM_RESULT <>'Y'																							\n");
			sql.append("ORDER BY b.ORDERLY DESC																							\n");
			sql.append("LIMIT ?,?																										\n");
		}
		
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			
			if(affiliationSeq > 0){
				pstmt.setInt(++cnt, affiliationSeq);
			}
			
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				CityVO vo = new CityVO();
				vo.setCityDay(rs.getInt("CITYDATE"));
				vo.setCityName(rs.getString("CITY_NAME"));
				vo.setCityMem(rs.getString("MEM_NAME"));
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setCountryPic(rs.getString("COUNTRY_PIC"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				
				DepartVO city = vo.getDepartVo();
				city.setDepartName(rs.getString("DEPART_NAME"));
				
				list.add(vo);
			}
		}catch(Exception e){
			e.printStackTrace(); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
		
	}
	/*
	 * 2015-05-12 ksy 다음도시정보 다녀온 국가, 도시 수 총 국가, 도시 수
	 */
	/*
	public CityVO selectClearCity(int memSeqNo)throws SQLException{
		CityVO vo = new CityVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELELCT																																		\n");
		sql.append("		(SELECT COUNT(*) FROM TB_COUNTRY) AS TOTAL_COUNTRY,																					\n");
		sql.append("		(SELECT COUNT(*) FROM TB_CITY) AS TOTAL_CITY,																						\n");
		sql.append("		(SELECT COUNT(DISTINCT COUNTRY_SEQ_NO) FROM TB_CITY a, TB_MEM_STAY b WHERE a.CITY_SEQ_NO = b.CITY_SEQ_NO) AS CLEAR_COUNTRY,			\n");
		sql.append("		(SELECT COUNT(*) FROM TB_MEM_STAY WHERE MEM_SEQ_NO = ? AND END_DATE IS NOT NULL) AS CLEAR_CITY										\n");
		sql.append("FROM																																		\n");
		sql.append("		TB_MEM_STAY																															\n");
		sql.append("WHERE																																		\n");
		sql.append("		MEM_SEQ_NO = ?																														\n");
		sql.append("GROUP BY																																	\n");
		sql.append("		MEM_SEQ_NO																															\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, memSeqNo);
			rs=pstmt.executeQuery();
			if(rs.next()){
				vo.setTotalCountry(rs.getInt("TOTAL_COUNTRY"));
				vo.setTotalCity(rs.getInt("TOTAL_CITY"));
				vo.setClearCountry(rs.getInt("CLEAR_COUNTRY"));
				vo.setClearCity(rs.getInt("CLEAR_CITY"));
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectClearCity ERROR : " +e);
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}
		if(rs != null) rs.close();
		if(pstmt != null) pstmt.close();
		if(conn != null) conn.close();
		
		return vo;
	}*/
	/*
	 * 2015-05-12 ksy 다음국가,도시 현재 남은 층
	 */
	/*public CityVO selectNextCity(int memSeqNo, int orderlry)throws SQLException{
		CityVO vo = new CityVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																										\n");
		sql.append("		COUNTRY_NAME, CITY_NAME,																			\n");
		sql.append("		(SELECT TOTAL_WORK FROM TB_MASTER WHERE MEM_SEQ_NO = ? AND CRT_DATE > CURDATE()) AS TOTAL_WORK,		\n");
		sql.append("		(SELECT SUM(STAIR_NUMBER) FROM TB_CITY WHERE ORDERLRY <= ?) AS TOTAL_STAIR							\n");
		sql.append("FROM																										\n");
		sql.append("		TB_CITY a, TB_COUNTRY b																				\n");
		sql.append("WHERE																										\n");
		sql.append("		a.COUNTRY_SEQ_NO = b.COUNTRY_SEQ_NO																	\n");
		sql.append("	AND ORDERLRY = ?																						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, orderlry);
			pstmt.setInt(3, orderlry);
			rs= pstmt.executeQuery();
			if(rs.next()){
				vo.setCountryName(rs.getString("COUNTRY_NAME"));
				vo.setCityName(rs.getString("CITY_NAME"));
				vo.setTotalWork(rs.getInt("TOTAL_WORK"));
				vo.setTotalStair(rs.getInt("TOTAL_STAIR"));
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectNextCity ERROR : " +e);
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}
		if(rs != null) rs.close();
		if(pstmt != null) pstmt.close();
		if(conn != null) conn.close();
		
		return vo;
	}*/
	/*
	 * 2015-05-12 ksy 클리어한 도시 리스트
	 */
	public ArrayList<CityVO> selectClearCityList(int memSeqNo, String startDate)throws SQLException{
		ArrayList<CityVO> list = new ArrayList<CityVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT												\n");
		sql.append("		c.COUNTRY_SEQ_NO, COUNTRY_NAME				\n");
		sql.append("FROM												\n");
		sql.append("		TB_MEM_STAY a								\n");
		sql.append("LEFT OUTER JOIN										\n");
		sql.append("		TB_CITY b									\n");
		sql.append("ON													\n");
		sql.append("		b.ORDERLY <= a.ORDERLY						\n");
		sql.append("LEFT OUTER JOIN										\n");
		sql.append("		TB_COUNTRY c								\n");
		sql.append("ON													\n");
		sql.append("		b.COUNTRY_SEQ_NO = c.COUNTRY_SEQ_NO			\n");
		sql.append("WHERE												\n");
		sql.append("		a.MEM_SEQ_NO = ?							\n");
		sql.append("	AND a.START_DATE >= DATE(?)							\n");
		sql.append("GROUP BY											\n");
		sql.append("		c.COUNTRY_SEQ_NO							\n");

		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setString(2, startDate);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				CityVO vo = new CityVO();
				vo.setCountrySeqNo(rs.getInt("COUNTRY_SEQ_NO"));
				vo.setCountryName(rs.getString("COUNTRY_NAME"));
				list.add(vo);
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectClearCityList ERROR : " +e);
			
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-12 ksy 세계일주 리스트
	 */
	public ArrayList<CityVO>selectWorldList(int memSeqNo,int pageNo, int rowSize)throws SQLException{
		int page = (pageNo -1)*rowSize;
		ArrayList<CityVO> list = new ArrayList<CityVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		MEM_SEQ_NO, WORLD_DATE		\n");
		sql.append("FROM								\n");
		sql.append("		TB_WORLD						\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_SEQ_NO = ?				\n");
		sql.append("ORDER BY							\n");
		sql.append("		CRT_DATE DESC				\n");
		sql.append("LIMIT ?,?				\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, page);
			pstmt.setInt(3, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				CityVO vo = new CityVO();
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setCityDay(rs.getInt("WORLD_DATE"));
				list.add(vo);
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectWorldList ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return list;
	}
	/*
	 * 2015-05-13 ksy 내가 시장인 도시 리스트
	 */
	public ArrayList<CityVO>selectMayorList(int memSeqNo, int pageNo, int rowSize)throws SQLException{
		int page = (pageNo -1) * rowSize;
		ArrayList<CityVO> list = new ArrayList<CityVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																			\n");
		sql.append("		COUNTRY_NAME, COUNTRY_PIC, CITY_NAME, d.AFFILIATION_NAME, d.SEQ_NO			\n");
		sql.append("FROM													\n");
		sql.append("		TB_MAYOR a										\n");
		sql.append("LEFT OUTER JOIN											\n");
		sql.append("		TB_CITY b										\n");
		sql.append("ON														\n");
		sql.append("		a.CITY_SEQ_NO = b.CITY_SEQ_NO					\n");
		sql.append("LEFT OUTER JOIN											\n");
		sql.append("		TB_COUNTRY c									\n");
		sql.append("ON														\n");
		sql.append("		b.COUNTRY_SEQ_NO = c.COUNTRY_SEQ_NO				\n");
		sql.append("LEFT OUTER JOIN											\n");
		sql.append("		TB_AFFILIATION d								\n");
		sql.append("ON														\n");
		sql.append("		a.AFFILIATION_NO = d.SEQ_NO						\n");
		sql.append("WHERE													\n");
		sql.append("		a.MEM_SEQ_NO = ? AND a.DEL_YN <>'Y'				\n");
		sql.append("ORDER BY												\n");
		sql.append("		a.CRT_DATE ASC									\n");
		sql.append("LIMIT ?,?												\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, page);
			pstmt.setInt(3, rowSize);
			rs = pstmt.executeQuery();
			////System.out.println(pstmt.toString());
			while(rs.next()){
				CityVO vo = new CityVO();
				vo.setCountryName(rs.getString("COUNTRY_NAME"));
				vo.setCityName(rs.getString("CITY_NAME"));
				vo.setCountryPic(rs.getString("COUNTRY_PIC"));
				AffiliationVO avo = vo.getAffiliationVo();
				avo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				avo.setSeqNo(rs.getInt("SEQ_NO"));
				list.add(vo);
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectWorldList ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return list;
	}
	/*
	 * 2015-05-13 ksy 오전 과 오후에 따른 메인화면 도시 사진 변경
	 */
	public CityVO selectCityPic(int gubun, int citySeqNo)throws SQLException{
		CityVO vo = new CityVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		PHOTO_NAME				\n");
		sql.append("FROM							\n");
		sql.append("		TB_CITY_PHOTO			\n");
		sql.append("WHERE							\n");
		sql.append("		CITY_SEQ_NO = ?			\n");
		sql.append("	AND	PHOTO_GUBUN = ?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, citySeqNo);
			pstmt.setInt(2, gubun);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setCityPic(rs.getString("PHOTO_NAME"));
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectCityPic ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	/*
	 * 2015-05-14 ksy 국가 입력
	 */
	public int countryInsert(String countryName, int orderLy,String fileName)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO												\n");
		sql.append("			TB_COUNTRY									\n");
		sql.append("(														\n");
		sql.append("	COUNTRY_NAME, COUNTRY_PIC, ORDERLY, CRT_DATE		\n");
		sql.append(")														\n");
		sql.append("VALUES(													\n");
		sql.append("	?,?,?,NOW()											\n");
		sql.append(")							\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, countryName);
			pstmt.setString(2, fileName);
			pstmt.setInt(3, orderLy);
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
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao countryInsert ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-14 ksy 진행순서 확인
	 */
	public int selectOrderly(int orderLy, int gubun)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		COUNT(*) AS CNT			\n");
		sql.append("FROM	"+TABLE[gubun]+"						\n");
		sql.append("		TB_COUNTRY				\n");
		sql.append("WHERE							\n");
		sql.append("		ORDERLY = ?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, orderLy);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectOrderly ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-14 ksy 국가 리스트
	 */
	public ArrayList<CityVO> selectCountryList(int pageNo, int rowSize)throws SQLException{
		int page = (pageNo -1)*rowSize;
		ArrayList<CityVO> list = new ArrayList<CityVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT															\n");
		sql.append("		COUNTRY_SEQ_NO, COUNTRY_NAME, ORDERLY					\n");
		sql.append("FROM															\n");
		sql.append("		TB_COUNTRY												\n");
		sql.append("ORDER BY 														\n");
		sql.append("		ORDERLY ASC											\n");
		sql.append("LIMIT ?,?														\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, page);
			pstmt.setInt(2, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				CityVO vo = new CityVO();
				vo.setCountrySeqNo(rs.getInt("COUNTRY_SEQ_NO"));
				vo.setCountryName(rs.getString("COUNTRY_NAME"));
				vo.setOrderLy(rs.getInt("ORDERLY"));
				
				list.add(vo);
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectCountryList ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
		
	}
	/*
	 * 2015-05-14 ksy 국가 총 개수
	 */
	public int countryCnt()throws SQLException{
		int result=0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		COUNT(*) AS CNT				\n");
		sql.append("FROM								\n");
		sql.append("		TB_COUNTRY					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao countryCnt ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-14 ksy 국가 상세화면 cms
	 */
	public CityVO countryView(int countrySeqNo)throws SQLException{
		CityVO vo = new CityVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT											\n");
		sql.append("		COUNTRY_NAME, COUNTRY_PIC, ORDERLY		\n");
		sql.append("FROM											\n");
		sql.append("		TB_COUNTRY								\n");
		sql.append("WHERE											\n");
		sql.append("		COUNTRY_SEQ_NO = ?						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, countrySeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setCountryName(rs.getString("COUNTRY_NAME"));
				vo.setCountryPic(rs.getString("COUNTRY_PIC"));
				vo.setOrderLy(rs.getInt("ORDERLY"));
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao countryView ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	/*
	 * 2015-05-14 ksy 국가에 따른 도시 리스트
	 */
	public ArrayList<CityVO> selectCountryCity(int countrySeqNo)throws SQLException{
		ArrayList<CityVO> list = new ArrayList<CityVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT											\n");
		sql.append("		CITY_NAME, b.ORDERLY, STAIR_NUMBER		\n");
		sql.append("FROM											\n");
		sql.append("		TB_COUNTRY a, TB_CITY b					\n");
		sql.append("WHERE											\n");
		sql.append("		a.COUNTRY_SEQ_NO = b.COUNTRY_SEQ_NO		\n");
		sql.append("	AND	b.COUNTRY_SEQ_NO = ?					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, countrySeqNo);
			rs = pstmt.executeQuery();
			while(rs.next()){
				CityVO vo = new CityVO();
				vo.setCityName(rs.getString("CITY_NAME"));
				vo.setOrderLy(rs.getInt("ORDERLY"));
				vo.setTotalWork(rs.getInt("STAIR_NUMBER"));
				
				list.add(vo);
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectCountryCity ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-20 ksy CMS도시 리스트
	 */
	public ArrayList<CityVO> selectCityList(int pageno, int rowSize) throws SQLException{
		int page = (pageno -1)*rowSize;
		ArrayList<CityVO> list = new ArrayList<CityVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																									\n");
		sql.append("		b.COUNTRY_SEQ_NO, COUNTRY_NAME, CITY_SEQ_NO, CITY_NAME, a.ORDERLY, STAIR_NUMBER				\n");
		sql.append("FROM																									\n");
		sql.append("		TB_CITY a, TB_COUNTRY b																			\n");
		sql.append("WHERE																									\n");
		sql.append("		a.COUNTRY_SEQ_NO = b.COUNTRY_SEQ_NO																\n");
		sql.append("ORDER BY																								\n");
		sql.append("		a.ORDERLY ASC																					\n");
		sql.append("LIMIT ?,?																								\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, page);
			pstmt.setInt(2, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				CityVO vo = new CityVO();
				vo.setCountrySeqNo(rs.getInt("COUNTRY_SEQ_NO"));
				vo.setCountryName(rs.getString("COUNTRY_NAME"));
				vo.setCitySeqNo(rs.getInt("CITY_SEQ_NO"));
				vo.setCityName(rs.getString("CITY_NAME"));
				vo.setOrderLy(rs.getInt("ORDERLY"));
				vo.setTotalStair(rs.getInt("STAIR_NUMBER"));
				list.add(vo);
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectCityList ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-20 ksy CMS 도시 등록
	 */
	public int cityInsert(String cityName, int order, int totalstair, String fileName, String fileNamea, int countrySeqNo)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO																					\n");
		sql.append("			TB_CITY																			\n");
		sql.append("(																							\n");
		sql.append("	COUNTRY_SEQ_NO, CITY_NAME, ORDERLY, STAIR_NUMBER, CRT_DATE, DAY_PIC, NIGHT_PIC			\n");
		sql.append(")																							\n");
		sql.append("VALUES(																						\n");
		sql.append("	?,?,?,?,NOW(),?,?																		\n");
		sql.append(")																							\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, countrySeqNo);
			pstmt.setString(2, cityName);
			pstmt.setInt(3, order);
			pstmt.setInt(4, totalstair);
			pstmt.setString(5, fileName);
			pstmt.setString(6, fileNamea);
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
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao cityInsert ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-20 ksy 국가 수정
	 */
	public int countryUpdate(int no, String countryName, String listImgName)throws SQLException{
		int cnt = 0;
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE									\n");
		sql.append("		TB_COUNTRY						\n");
		sql.append("SET										\n");
		sql.append("		COUNTRY_NAME = ?				\n");
		if(listImgName != null && !listImgName.equals("")){
			sql.append("	,	COUNTRY_PIC  = ?				\n");
		}
		sql.append("WHERE									\n");
		sql.append("		COUNTRY_SEQ_NO = ?				\n");
	
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, countryName);
			if(listImgName != null && !listImgName.equals("")){
				pstmt.setString(++cnt, listImgName);
			}
			pstmt.setInt(++cnt, no);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao countryUpdate ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-20 ksy 도시 상세화면
	 */
	public CityVO cityView(int no)throws SQLException{
		int cnt =0;
		CityVO vo = new CityVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																					\n");
		sql.append("		b.COUNTRY_NAME, CITY_NAME, STAIR_NUMBER, DAY_PIC, NIGHT_PIC, a.ORDERLY			\n");
		sql.append("FROM																					\n");
		sql.append("		TB_CITY a , TB_COUNTRY b																		\n");
		sql.append("WHERE																					\n");
		sql.append("		CITY_SEQ_NO = ?																	\n");
		sql.append("AND a.COUNTRY_SEQ_NO = b.COUNTRY_SEQ_NO;																					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, no);
			rs =pstmt.executeQuery();
			if(rs.next()){
				vo.setCountryName(rs.getString("COUNTRY_NAME"));
				vo.setCityName(rs.getString("CITY_NAME"));
				vo.setTotalStair(rs.getInt("STAIR_NUMBER"));
				vo.setCityPic(rs.getString("DAY_PIC"));
				vo.setCityNPic(rs.getString("NIGHT_PIC"));
				vo.setOrderLy(rs.getInt("ORDERLY"));
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao cityView ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	/*
	 * 2015-05-20 ksy 도시 정보 수정
	 */
	public int cityUpdate(String cityName, int totalstair, String fileName, String fileNamea, int citySeqNo)throws SQLException{
		int cnt = 0;
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE										\n");
		sql.append("		TB_CITY								\n");
		sql.append("SET											\n");
		sql.append("		CITY_NAME = ?						\n");
		sql.append("	,	STAIR_NUMBER = ?					\n");
		if(fileName != null && !fileName.equals("")){
			sql.append("	,	DAY_PIC = ?							\n");
		}
		if(fileNamea != null && !fileNamea.equals("")){
			sql.append("	,	NIGHT_PIC = ?						\n");
		}
		sql.append("WHERE										\n");
		sql.append("		CITY_SEQ_NO = ?						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, cityName);
			pstmt.setInt(++cnt, totalstair);
			if(fileName != null && !fileName.equals("")){
				pstmt.setString(++cnt, fileName);
			}
			if(fileNamea != null && !fileNamea.equals("")){
				pstmt.setString(++cnt, fileNamea);
			}
			pstmt.setInt(++cnt, citySeqNo);
			result = pstmt.executeUpdate();
			
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao cityUpdate ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-22 ksy 클리어한 도시/국가, 총 도시/국가
	 */
	public CityVO selectMemCityInfo(int memSeqNo, String fstartDate)throws SQLException{
		CityVO vo = new CityVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																																			\n");
		sql.append("		(SELECT COUNT(DISTINCT COUNTRY_SEQ_NO) FROM TB_CITY WHERE CITY_SEQ_NO IN (SELECT CITY_SEQ_NO FROM TB_MEM_STAY WHERE END_DATE IS NOT NULL AND MEM_SEQ_NO = ? AND START_DATE >= DATE(?))) AS CLEAR_COUNTRY,			\n");
		sql.append("		(SELECT COUNT(*) FROM TB_MEM_STAY WHERE MEM_SEQ_NO = ? AND END_DATE IS NOT NULL AND START_DATE >= DATE(?)) AS CLEAR_CITY,																						\n");
		sql.append("		(SELECT COUNT(*) FROM TB_COUNTRY) AS CNT_COUNTRY,																																						\n");
		sql.append("		(SELECT COUNT(*) FROM TB_CITY) AS CNT_CITY																																								\n");
		sql.append("FROM																																																			\n");
		sql.append("		(SELECT CITY_SEQ_NO, MEM_SEQ_NO FROM TB_MEM_STAY WHERE END_DATE IS NOT NULL AND MEM_SEQ_NO = ? AND START_DATE >= DATE(?))z																					\n");
		sql.append("GROUP BY																																																		\n");
		sql.append("		MEM_SEQ_NO																																																\n");
		
		
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setString(2, fstartDate);
			pstmt.setInt(3, memSeqNo);
			pstmt.setString(4, fstartDate);
			pstmt.setInt(5, memSeqNo);
			pstmt.setString(6, fstartDate);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setClearCountry(rs.getInt("CLEAR_COUNTRY"));
				vo.setClearCity(rs.getInt("CLEAR_CITY"));
				vo.setTotalCountry(rs.getInt("CNT_COUNTRY"));
				vo.setTotalCity(rs.getInt("CNT_CITY"));
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectMemCityInfo ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
		
	}
	/*
	 * 2015-05-22 ksy 다음도시 정보
	 */
	public CityVO selectNextCity(int memSeqNo, int orderly)throws SQLException{
		CityVO vo = new CityVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																								\n");
		sql.append("		(SELECT COUNTRY_NAME FROM TB_CITY a, TB_COUNTRY b WHERE a.ORDERLY = ?+1 AND a.COUNTRY_SEQ_NO = b.COUNTRY_SEQ_NO) AS NEXT_COUNTRY,							\n");
		sql.append("		(SELECT COUNTRY_PIC FROM TB_CITY a, TB_COUNTRY b WHERE a.ORDERLY = ?+1 AND  a.COUNTRY_SEQ_NO = b.COUNTRY_SEQ_NO) AS NEXT_COUNTRY_PIC,						\n");
		sql.append("		(SELECT CITY_NAME FROM TB_CITY WHERE ORDERLY = ?+1) AS NEXT_CITY,																							\n");
		sql.append("		(SELECT SUM(STAIR_NUMBER) FROM TB_CITY WHERE ORDERLY <= ?) AS STAIR,																						\n");
		sql.append("		TOTAL_WALK,																																	\n");
		sql.append("		(SELECT SUM(STAIR_NUMBER) FROM TB_CITY) AS TOTAL_STAIR,																										\n");
		sql.append("		(SELECT CITY_COMENT FROM TB_CITY_COMENT a, TB_CITY b WHERE a.CITY_SEQ = b.CITY_SEQ_NO AND b.ORDERLY = ?+1) AS CITY_COMENT																																								\n");
		sql.append("FROM																																								\n");
		sql.append("		TB_MASTER 																																					\n");
		sql.append("WHERE																																								\n");
		sql.append("		MEM_SEQ_NO = ?																															\n");
		sql.append("ORDER BY CRT_DATE DESC LIMIT 1				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, orderly);
			pstmt.setInt(2, orderly);
			pstmt.setInt(3, orderly);
			pstmt.setInt(4, orderly);
			pstmt.setInt(5, orderly);
			pstmt.setInt(6, memSeqNo);
			
			////System.out.println(pstmt);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setCountryName(rs.getString("NEXT_COUNTRY"));
				vo.setCountryPic(rs.getString("NEXT_COUNTRY_PIC"));
				vo.setCityName(rs.getString("NEXT_CITY"));
				vo.setTotalStair(rs.getInt("STAIR"));
				vo.setTotalWork(rs.getInt("TOTAL_WALK"));
				vo.setAllStair(rs.getInt("TOTAL_STAIR"));
				vo.setCityComent(rs.getString("CITY_COMENT"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
		
	}
	/*
	 * 2015-05-29 ksy 도시 총 개수
	 */
	public int countCity()throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		COUNT(*) AS CNT			\n");
		sql.append("FROM							\n");
		sql.append("		TB_CITY					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao countCity ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-29 ksy 다음 도시에 대한 정보를 가져옴
	 */
	public CityVO selectCityInfo(int nextOrderly)throws SQLException{
		CityVO vo = new CityVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT											\n");
		sql.append("		CITY_SEQ_NO, CITY_NAME, ORDERLY			\n");
		sql.append("FROM											\n");
		sql.append("		TB_CITY									\n");
		sql.append("WHERE											\n");
		sql.append("		ORDERLY = ?								\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, nextOrderly);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setCitySeqNo(rs.getInt("CITY_SEQ_NO"));
				vo.setCityName(rs.getString("CITY_NAME"));
				vo.setOrderLy(rs.getInt("ORDERLY"));
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectCityInfo ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
		
	}
	
	/*
	 * 2015-05-14 ksy 국가 상세화면 cms
	 */
	public int selectLastOrder()throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																		\n");
		sql.append("		ORDERLY																\n");
		sql.append("FROM																		\n");
		sql.append("		TB_COUNTRY															\n");
		sql.append("WHERE																		\n");
		sql.append("		ORDERLY=(SELECT MAX(ORDERLY) FROM TB_COUNTRY)						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("ORDERLY");
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao countryView ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	
	public int selectCityOrder(int country)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		MAX(ORDERLY) AS CNT				\n");
		sql.append("FROM								\n");
		sql.append("		TB_CITY						\n");
		sql.append("WHERE								\n");
		sql.append("		COUNTRY_SEQ_NO = ?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, country);
			rs = pstmt.executeQuery();
			////System.out.println(pstmt.toString());
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectOrderly ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	
	public int updateOrderList(int order)throws SQLException{
		int cnt = 0;
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE										\n");
		sql.append("		TB_CITY								\n");
		sql.append("SET											\n");
		sql.append("		ORDERLY = (ORDERLY+1)				\n");
		sql.append("WHERE										\n");
		sql.append("		ORDERLY >= ? 						\n");
	
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, order);
			result = pstmt.executeUpdate();
			////System.out.println(pstmt.toString());
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao countryUpdate ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	
	public int selectCountryName(String countryName)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		COUNTRY_SEQ_NO				\n");
		sql.append("FROM								\n");
		sql.append("		TB_COUNTRY					\n");
		sql.append("WHERE								\n");
		sql.append("		COUNTRY_NAME = ?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, countryName);
			rs = pstmt.executeQuery();
			////System.out.println(pstmt.toString());
			if(rs.next()){
				result = rs.getInt("COUNTRY_SEQ_NO");
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectOrderly ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	public int selectCountCnt(int countrySeqNo)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		COUNT(*) AS CNT			\n");
		sql.append("FROM							\n");
		sql.append("		TB_CITY				\n");
		sql.append("WHERE							\n");
		sql.append("		COUNTRY_SEQ_NO = ?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, countrySeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectOrderly ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	
	public int selectMaxCity()throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		MAX(ORDERLY) AS CNT				\n");
		sql.append("FROM								\n");
		sql.append("		TB_CITY						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			////System.out.println(pstmt.toString());
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectOrderly ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2016-08-24 도시별 코멘트 리스트
	 */
	public ArrayList<CityVO> selectCityComentList(int pageno, int rowSize) throws SQLException{
		int page = (pageno -1)*rowSize;
		ArrayList<CityVO> list = new ArrayList<CityVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																										\n");
		sql.append("		COUNTRY_NAME, CITY_NAME, CITY_COMENT_SEQ, CITY_COMENT												\n");
		sql.append("FROM																										\n");
		sql.append("		TB_CITY a, TB_COUNTRY b, TB_CITY_COMENT c															\n");
		sql.append("WHERE																										\n");
		sql.append("		a.COUNTRY_SEQ_NO = b.COUNTRY_SEQ_NO																	\n");
		sql.append("AND																											\n");
		sql.append("		a.CITY_SEQ_NO = c.CITY_SEQ																		\n");
		sql.append("AND		c.DEL_YN <> 'Y'																						\n");
		sql.append("ORDER BY																									\n");
		sql.append("		c.ORDERLY ASC																					\n");
		sql.append("LIMIT ?,?																									\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, page);
			pstmt.setInt(2, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				CityVO vo = new CityVO();
				vo.setCountryName(rs.getString("COUNTRY_NAME"));
				vo.setCityComentSeq(rs.getInt("CITY_COMENT_SEQ"));
				vo.setCityName(rs.getString("CITY_NAME"));
				vo.setCityComent(rs.getString("CITY_COMENT"));
				list.add(vo);
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao selectCityList ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	
	/*
	 * 2016-08-24 도시별 인사말 총 수량
	 */
	public int countCityComent()throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		COUNT(*) AS CNT			\n");
		sql.append("FROM																										\n");
		sql.append("		TB_CITY a, TB_COUNTRY b, TB_CITY_COMENT c															\n");
		sql.append("WHERE																										\n");
		sql.append("		a.COUNTRY_SEQ_NO = b.COUNTRY_SEQ_NO																	\n");
		sql.append("AND																											\n");
		sql.append("		a.CITY_SEQ_NO = c.CITY_SEQ																		\n");
		sql.append("AND		c.DEL_YN <> 'Y'																						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao countCity ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-08-24 CMS 도시별 인사말 등록
	 */
	public int insertCityGreeting(int citySeq, String cityComent, int orderly)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO											\n");
		sql.append("			TB_CITY_COMENT							\n");
		sql.append("(													\n");
		sql.append("	CITY_SEQ, CITY_COMENT, ORDERLY, DEL_YN, CRT_DATE			\n");
		sql.append(")													\n");
		sql.append("VALUES(												\n");
		sql.append("	?, ?, ?, 'N', NOW()								\n");
		sql.append(")													\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, citySeq);
			pstmt.setString(2, cityComent);
			pstmt.setInt(3, orderly);
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
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("NoticeDao insertGreeting ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-08-24 CMS 도시별 코멘트 상세보기
	 */
	public CityVO cityComentView(int no)throws SQLException{
		int cnt =0;
		CityVO vo = new CityVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																					\n");
		sql.append("		CITY_COMENT_SEQ, CITY_NAME, CITY_COMENT											\n");
		sql.append("FROM																					\n");
		sql.append("		TB_CITY a , TB_CITY_COMENT b																		\n");
		sql.append("WHERE																					\n");
		sql.append("		CITY_COMENT_SEQ = ?																	\n");
		sql.append("AND a.CITY_SEQ_NO = b.CITY_SEQ;																					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, no);
			rs =pstmt.executeQuery();
			if(rs.next()){
				vo.setCityComentSeq(rs.getInt("CITY_COMENT_SEQ"));
				vo.setCityName(rs.getString("CITY_NAME"));
				vo.setCityComent(rs.getString("CITY_COMENT"));
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao cityView ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	
	/*
	 * 2016-08-24 CMS 도시별 인사말 수정
	 */
	public int updateCityGreeting(int cityComentSeq, String cityComent )throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE									\n");
		sql.append("		TB_CITY_COMENT					\n");
		sql.append("SET										\n");
		sql.append("		CITY_COMENT = ?			\n");
		sql.append("WHERE									\n");
		sql.append("		CITY_COMENT_SEQ = ?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, cityComent);
			pstmt.setInt(2, cityComentSeq);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("NoticeDao greetingModify ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	
	/*
	 * 2016-08-24 해당 도시의 순서 가져오기
	 */
	public int selectCityOrderly(int citySeq)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		ORDERLY					\n");
		sql.append("FROM							\n");
		sql.append("		TB_CITY 				\n");
		sql.append("WHERE							\n");
		sql.append("		CITY_SEQ_NO = ?			\n");
		
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, citySeq);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("ORDERLY");
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao countCity ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	
	/*
	 * 2016-08-24 앱 메인화면 도시 코멘트 가져오기
	 */
	public String selectCityComent(int citySeq)throws SQLException{
		String coment = "";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		CITY_COMENT			\n");
		sql.append("FROM																										\n");
		sql.append("		TB_CITY_COMENT															\n");
		sql.append("WHERE																										\n");
		sql.append("		a.COUNTRY_SEQ_NO = b.COUNTRY_SEQ_NO																	\n");
		sql.append("AND																											\n");
		sql.append("		a.CITY_SEQ_NO = c.CITY_SEQ																		\n");
		sql.append("AND		c.DEL_YN <> 'Y'																						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				coment = rs.getString("CNT");
			}
		}catch(Exception e){
			
			////////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			////////System.out.println("CityDao countCity ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return coment;
	}
	
}
