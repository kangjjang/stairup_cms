package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import util.ConnectionUtil;
import vo.MemberVO;
import vo.StatisticsVO;

import com.mysql.jdbc.Statement;

public class StatisticsDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public StatisticsDao() throws SQLException{
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
		 	"SUBSTRING(a.CRT_DATE,1,10) = SUBSTRING(?,1,10) GROUP BY a.MEM_SEQ_NO DESC",
	        "WEEKOFYEAR(a.CRT_DATE) = WEEKOFYEAR(?) GROUP BY a.MEM_SEQ_NO DESC",
	        "SUBSTRING(a.CRT_DATE,1,7) = SUBSTRING(?,1,7) GROUP BY a.MEM_SEQ_NO DESC",
	        "SUBSTRING(a.CRT_DATE,1,4) = SUBSTRING(?,1,4) GROUP BY a.MEM_SEQ_NO DESC"
	};

	private static final String WHEREQUERYNEW [] = {
			"a.CRT_DATE >= ? AND a.CRT_DATE <= DATE_ADD(?, INTERVAL +1 DAY) GROUP BY a.MEM_SEQ_NO DESC",
			"SUBSTRING(a.CRT_DATE,1,10) = SUBSTRING(?,1,10) GROUP BY a.MEM_SEQ_NO DESC",
			"WEEKOFYEAR(a.CRT_DATE) = WEEKOFYEAR(?) GROUP BY a.MEM_SEQ_NO DESC",
			"SUBSTRING(a.CRT_DATE,1,7) = SUBSTRING(?,1,7) GROUP BY a.MEM_SEQ_NO DESC",
			"SUBSTRING(a.CRT_DATE,1,4) = SUBSTRING(?,1,4) GROUP BY a.MEM_SEQ_NO DESC"
	};

	public ArrayList<StatisticsVO> selectStatistics(String startDate, int date, int pageno, int rowSize, int stair){
		int page = (pageno -1)*rowSize;
		ArrayList<StatisticsVO> list = new ArrayList<StatisticsVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT z.MEM_SEQ_NO , z.MEM_NAME , z.MEM_NUMBER , z.TODAY_TOTAL , z.MEM_DEPART, z.MEM_AFFILIATION																																													\n");
		sql.append("FROM																																													\n");
		sql.append("(																																														\n");
		sql.append("SELECT																																													\n");
		sql.append("		m.MEM_SEQ_NO, m.MEM_NAME, m.MEM_NUMBER, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																								\n");
		sql.append("(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO =m.MEM_DEPART) AS MEM_DEPART,																										\n");
		sql.append("(SELECT AFFILIATION_NAME FROM TB_AFFILIATION WHERE SEQ_NO = m.MEM_AFFILIATION) AS MEM_AFFILIATION 								\n");
		sql.append("FROM																																													\n");
		sql.append("(SELECT MEM_SEQ_NO, MEM_DEPART, MEM_NAME, MEM_NUMBER, MEM_AFFILIATION FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																							\n");
		sql.append("LEFT OUTER JOIN																																											\n");
		sql.append("(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL , crt_date FROM TB_MASTER a WHERE "+WHEREQUERY[date]+")g					\n");
		sql.append("ON																																														\n");
		sql.append("	m.MEM_SEQ_NO = g.MEM_SEQ_NO 																																						\n");
		sql.append("ORDER BY TODAY_TOTAL DESC																																								\n");
		sql.append(")z																																														\n");
		sql.append("WHERE																																													\n");
		sql.append("z.TODAY_TOTAL >= ?																																										\n");
		sql.append("LIMIT ?,?																																										\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, startDate);
			pstmt.setInt(2, stair);
			pstmt.setInt(3, page);
			pstmt.setInt(4, rowSize);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				StatisticsVO vo = new StatisticsVO();
				vo.setMemSeq(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemNumber(rs.getString("MEM_NUMBER"));
				vo.setDepartName(rs.getString("MEM_DEPART"));
				vo.setAffiliationName(rs.getString("MEM_AFFILIATION"));
				vo.setTodaytotal(rs.getInt("TODAY_TOTAL"));
				list.add(vo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		
		return list;
	}
	
	public ArrayList<StatisticsVO> selectStatistics(String startDate, int date, int pageno, int rowSize, int stair, int roles){

		int cnt = 0;
		int page = (pageno -1)*rowSize;
		
		ArrayList<StatisticsVO> list = new ArrayList<StatisticsVO>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT z.MEM_SEQ_NO , z.MEM_NAME , z.MEM_NUMBER , z.TODAY_TOTAL , z.MEM_DEPART, z.MEM_AFFILIATION, z.MEM_AFFILIATION_NAME																																													\n");
		sql.append("FROM																																													\n");
		sql.append("(																																														\n");
		sql.append("SELECT																																													\n");
		sql.append("		m.MEM_SEQ_NO, m.MEM_NAME, m.MEM_NUMBER, m.MEM_AFFILIATION, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																								\n");
		sql.append("(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO =m.MEM_DEPART) AS MEM_DEPART,																										\n");
		sql.append("(SELECT AFFILIATION_NAME FROM TB_AFFILIATION WHERE SEQ_NO = m.MEM_AFFILIATION) AS MEM_AFFILIATION_NAME 								\n");
		sql.append("FROM																																													\n");
		sql.append("(SELECT MEM_SEQ_NO, MEM_DEPART, MEM_NAME, MEM_NUMBER, MEM_AFFILIATION FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																							\n");
		sql.append("LEFT OUTER JOIN																																											\n");
		sql.append("(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL , crt_date FROM TB_MASTER a WHERE "+WHEREQUERY[date]+")g					\n");
		sql.append("ON																																														\n");
		sql.append("	m.MEM_SEQ_NO = g.MEM_SEQ_NO 																																						\n");
		sql.append(")z																																														\n");
		sql.append("WHERE																																													\n");
		sql.append("z.TODAY_TOTAL >= ?																																										\n");
		if(roles > 0){
			sql.append("AND z.MEM_AFFILIATION = ?																																										\n");
		}
		sql.append("ORDER BY TODAY_TOTAL DESC																																								\n");
		sql.append("LIMIT ?,?																																										\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, startDate);
			pstmt.setInt(++cnt, stair);
			if(roles > 0){
				pstmt.setInt(++cnt, roles);
			}
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);
			
			//System.out.println(pstmt);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				StatisticsVO vo = new StatisticsVO();
				vo.setMemSeq(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemNumber(rs.getString("MEM_NUMBER"));
				vo.setDepartName(rs.getString("MEM_DEPART"));
				vo.setAffiliationName(rs.getString("MEM_AFFILIATION_NAME"));
				vo.setTodaytotal(rs.getInt("TODAY_TOTAL"));
				list.add(vo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		
		return list;
	}

	//skhero.kang 2018-07-02 계단업 통계 일자별 검색
	public ArrayList<StatisticsVO> selectStatisticsNew(String startDate, String endDate, int date, int pageno, int rowSize, int stair, int roles){

		int cnt = 0;
		int page = (pageno -1)*rowSize;

		ArrayList<StatisticsVO> list = new ArrayList<StatisticsVO>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT z.MEM_SEQ_NO , z.MEM_NAME , z.MEM_NUMBER , z.TODAY_TOTAL , z.MEM_DEPART, z.MEM_AFFILIATION, z.MEM_AFFILIATION_NAME																																													\n");
		sql.append("FROM																																													\n");
		sql.append("(																																														\n");
		sql.append("SELECT																																													\n");
		sql.append("		m.MEM_SEQ_NO, m.MEM_NAME, m.MEM_NUMBER, m.MEM_AFFILIATION, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																								\n");
		sql.append("(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO =m.MEM_DEPART) AS MEM_DEPART,																										\n");
		sql.append("(SELECT AFFILIATION_NAME FROM TB_AFFILIATION WHERE SEQ_NO = m.MEM_AFFILIATION) AS MEM_AFFILIATION_NAME 								\n");
		sql.append("FROM																																													\n");
		sql.append("(SELECT MEM_SEQ_NO, MEM_DEPART, MEM_NAME, MEM_NUMBER, MEM_AFFILIATION FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																							\n");
		sql.append("LEFT OUTER JOIN																																											\n");
		sql.append("(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL , crt_date FROM TB_MASTER a WHERE "+WHEREQUERYNEW[date]+")g					\n");
		sql.append("ON																																														\n");
		sql.append("	m.MEM_SEQ_NO = g.MEM_SEQ_NO 																																						\n");
		sql.append(")z																																														\n");
		sql.append("WHERE																																													\n");
		sql.append("z.TODAY_TOTAL >= ?																																										\n");
		if(roles > 0){
			sql.append("AND z.MEM_AFFILIATION = ?																																										\n");
		}
		sql.append("ORDER BY TODAY_TOTAL DESC																																								\n");
		sql.append("LIMIT ?,?																																										\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, startDate);
			pstmt.setString(++cnt, endDate);
			pstmt.setInt(++cnt, stair);
			if(roles > 0){
				pstmt.setInt(++cnt, roles);
			}
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);

			//System.out.println(pstmt);

			rs = pstmt.executeQuery();

			while(rs.next()){
				StatisticsVO vo = new StatisticsVO();
				vo.setMemSeq(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemNumber(rs.getString("MEM_NUMBER"));
				vo.setDepartName(rs.getString("MEM_DEPART"));
				vo.setAffiliationName(rs.getString("MEM_AFFILIATION_NAME"));
				vo.setTodaytotal(rs.getInt("TODAY_TOTAL"));
				list.add(vo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}


		return list;
	}
	
	public ArrayList<StatisticsVO> selectStatisticsExcel(String startDate, int date, int stair, int roles){
		
		int cnt = 0;
		
		ArrayList<StatisticsVO> list = new ArrayList<StatisticsVO>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT z.MEM_SEQ_NO , z.MEM_NAME , z.MEM_NUMBER , z.TODAY_TOTAL , z.MEM_DEPART, z.MEM_AFFILIATION, z.MEM_AFFILIATION_NAME																																													\n");
		sql.append("FROM																																													\n");
		sql.append("(																																														\n");
		sql.append("SELECT																																													\n");
		sql.append("		m.MEM_SEQ_NO, m.MEM_NAME, m.MEM_NUMBER, m.MEM_AFFILIATION, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																								\n");
		sql.append("(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO =m.MEM_DEPART) AS MEM_DEPART,																										\n");
		sql.append("(SELECT AFFILIATION_NAME FROM TB_AFFILIATION WHERE SEQ_NO = m.MEM_AFFILIATION) AS MEM_AFFILIATION_NAME 								\n");
		sql.append("FROM																																													\n");
		sql.append("(SELECT MEM_SEQ_NO, MEM_DEPART, MEM_NAME, MEM_NUMBER, MEM_AFFILIATION FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																							\n");
		sql.append("LEFT OUTER JOIN																																											\n");
		sql.append("(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL , crt_date FROM TB_MASTER a WHERE "+WHEREQUERY[date]+")g					\n");
		sql.append("ON																																														\n");
		sql.append("	m.MEM_SEQ_NO = g.MEM_SEQ_NO 																																						\n");
		sql.append(")z																																														\n");
		sql.append("WHERE																																													\n");
		sql.append("z.TODAY_TOTAL >= ?																																										\n");
		if(roles > 0){
			sql.append("AND z.MEM_AFFILIATION = ?																																										\n");
		}
		sql.append("ORDER BY TODAY_TOTAL DESC																																								\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, startDate);
			pstmt.setInt(++cnt, stair);
			if(roles > 0){
				pstmt.setInt(++cnt, roles);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				StatisticsVO vo = new StatisticsVO();
				vo.setMemSeq(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemNumber(rs.getString("MEM_NUMBER"));
				vo.setDepartName(rs.getString("MEM_DEPART"));
				vo.setAffiliationName(rs.getString("MEM_AFFILIATION_NAME"));
				vo.setTodaytotal(rs.getInt("TODAY_TOTAL"));
				list.add(vo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		
		return list;
	}

	public ArrayList<StatisticsVO> selectStatisticsExcelNew(String startDate, String endDate, int date, int stair, int roles){

		int cnt = 0;

		ArrayList<StatisticsVO> list = new ArrayList<StatisticsVO>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT z.MEM_SEQ_NO , z.MEM_NAME , z.MEM_NUMBER , z.TODAY_TOTAL , z.MEM_DEPART, z.MEM_AFFILIATION, z.MEM_AFFILIATION_NAME																																													\n");
		sql.append("FROM																																													\n");
		sql.append("(																																														\n");
		sql.append("SELECT																																													\n");
		sql.append("		m.MEM_SEQ_NO, m.MEM_NAME, m.MEM_NUMBER, m.MEM_AFFILIATION, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																								\n");
		sql.append("(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO =m.MEM_DEPART) AS MEM_DEPART,																										\n");
		sql.append("(SELECT AFFILIATION_NAME FROM TB_AFFILIATION WHERE SEQ_NO = m.MEM_AFFILIATION) AS MEM_AFFILIATION_NAME 								\n");
		sql.append("FROM																																													\n");
		sql.append("(SELECT MEM_SEQ_NO, MEM_DEPART, MEM_NAME, MEM_NUMBER, MEM_AFFILIATION FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																							\n");
		sql.append("LEFT OUTER JOIN																																											\n");
		sql.append("(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL , crt_date FROM TB_MASTER a WHERE "+WHEREQUERYNEW[date]+")g					\n");
		sql.append("ON																																														\n");
		sql.append("	m.MEM_SEQ_NO = g.MEM_SEQ_NO 																																						\n");
		sql.append(")z																																														\n");
		sql.append("WHERE																																													\n");
		sql.append("z.TODAY_TOTAL >= ?																																										\n");
		if(roles > 0){
			sql.append("AND z.MEM_AFFILIATION = ?																																										\n");
		}
		sql.append("ORDER BY TODAY_TOTAL DESC																																								\n");

		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, startDate);
			pstmt.setString(++cnt, endDate);
			pstmt.setInt(++cnt, stair);
			if(roles > 0){
				pstmt.setInt(++cnt, roles);
			}

			rs = pstmt.executeQuery();

			while(rs.next()){
				StatisticsVO vo = new StatisticsVO();
				vo.setMemSeq(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemNumber(rs.getString("MEM_NUMBER"));
				vo.setDepartName(rs.getString("MEM_DEPART"));
				vo.setAffiliationName(rs.getString("MEM_AFFILIATION_NAME"));
				vo.setTodaytotal(rs.getInt("TODAY_TOTAL"));
				list.add(vo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}


		return list;
	}
	
	public StatisticsVO weekDate(String startDate){
		StatisticsVO vo = new StatisticsVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT					\n");
		sql.append("		IFNULL(MIN(CRT_DATE),'') AS STARTDATE, IFNULL(MAX(CRT_DATE),'') AS ENDDATE			\n");
		sql.append("FROM					\n");
		sql.append("		TB_MASTER			\n");
		sql.append("WHERE					\n");
		sql.append("WEEKOFYEAR(CRT_DATE) =  WEEKOFYEAR(?)					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, startDate);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setStartDate(rs.getString("STARTDATE"));
				vo.setEndDate(rs.getString("ENDDATE"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs,pstmt);
		}
		
		return vo;
		
	}
	
	//검색 결과 총 개 수
	public int selectTotalCnt(String startDate,int date, int pageno,int rowSize,int stair, int roles){
		int result =0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT COUNT(*) AS CNT 																																												\n");
		sql.append("FROM																																													\n");
		sql.append("(																																														\n");
		sql.append("SELECT																																													\n");
		sql.append("		m.MEM_SEQ_NO, m.MEM_NAME, m.MEM_NUMBER, m.MEM_AFFILIATION, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																								\n");
		sql.append("(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO =m.MEM_DEPART) AS MEM_DEPART,																										\n");
		sql.append("(SELECT AFFILIATION_NAME FROM TB_AFFILIATION WHERE SEQ_NO = m.MEM_AFFILIATION) AS MEM_AFFILIATION_NAME 								\n");
		sql.append("FROM																																													\n");
		sql.append("(SELECT MEM_SEQ_NO, MEM_DEPART, MEM_NAME, MEM_NUMBER, MEM_AFFILIATION FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																							\n");
		sql.append("LEFT OUTER JOIN																																											\n");
		sql.append("(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL , crt_date FROM TB_MASTER a WHERE "+WHEREQUERY[date]+")g					\n");
		sql.append("ON																																														\n");
		sql.append("	m.MEM_SEQ_NO = g.MEM_SEQ_NO 																																						\n");
		sql.append("ORDER BY TODAY_TOTAL DESC																																								\n");
		sql.append(")z																																														\n");
		sql.append("WHERE																																													\n");
		sql.append("z.TODAY_TOTAL >= ?																																										\n");
		if(roles > 0){
			sql.append("AND z.MEM_AFFILIATION = ?																																										\n");
		}
		try{
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(++cnt, startDate);
			pstmt.setInt(++cnt, stair);
			if(roles > 0){
				pstmt.setInt(++cnt, roles);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				result = rs.getInt("CNT");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		
		return result;
	}

	//검색 결과 총 개 수 skhero.kang 2018-07-02
	public int selectTotalCntNew(String startDate, String endDate, int date, int pageno,int rowSize,int stair, int roles){
		int result =0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT COUNT(*) AS CNT 																																												\n");
		sql.append("FROM																																													\n");
		sql.append("(																																														\n");
		sql.append("SELECT																																													\n");
		sql.append("		m.MEM_SEQ_NO, m.MEM_NAME, m.MEM_NUMBER, m.MEM_AFFILIATION, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																								\n");
		sql.append("(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO =m.MEM_DEPART) AS MEM_DEPART,																										\n");
		sql.append("(SELECT AFFILIATION_NAME FROM TB_AFFILIATION WHERE SEQ_NO = m.MEM_AFFILIATION) AS MEM_AFFILIATION_NAME 								\n");
		sql.append("FROM																																													\n");
		sql.append("(SELECT MEM_SEQ_NO, MEM_DEPART, MEM_NAME, MEM_NUMBER, MEM_AFFILIATION FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																							\n");
		sql.append("LEFT OUTER JOIN																																											\n");
		sql.append("(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL , crt_date FROM TB_MASTER a WHERE "+WHEREQUERYNEW[date]+")g					\n");
		sql.append("ON																																														\n");
		sql.append("	m.MEM_SEQ_NO = g.MEM_SEQ_NO 																																						\n");
		sql.append("ORDER BY TODAY_TOTAL DESC																																								\n");
		sql.append(")z																																														\n");
		sql.append("WHERE																																													\n");
		sql.append("z.TODAY_TOTAL >= ?																																										\n");
		if(roles > 0){
			sql.append("AND z.MEM_AFFILIATION = ?																																										\n");
		}
		try{
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(++cnt, startDate);
			pstmt.setString(++cnt, endDate);
			pstmt.setInt(++cnt, stair);
			if(roles > 0){
				pstmt.setInt(++cnt, roles);
			}

			rs = pstmt.executeQuery();

			if(rs.next()){
				result = rs.getInt("CNT");
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}


		return result;
	}
	
	//검색 결과 총 개 수
	public int selectTotalCnt(String startDate,int date, int pageno,int rowSize,int stair){
		int result =0;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) AS CNT 																																												\n");
		sql.append("FROM																																													\n");
		sql.append("(																																														\n");
		sql.append("SELECT																																													\n");
		sql.append("		m.MEM_SEQ_NO, m.MEM_NAME, m.MEM_NUMBER, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																								\n");
		sql.append("(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO =m.MEM_DEPART) AS MEM_DEPART,																										\n");
		sql.append("(SELECT AFFILIATION_NAME FROM TB_AFFILIATION WHERE SEQ_NO = m.MEM_AFFILIATION) AS MEM_AFFILIATION 								\n");
		sql.append("FROM																																													\n");
		sql.append("(SELECT MEM_SEQ_NO, MEM_DEPART, MEM_NAME, MEM_NUMBER, MEM_AFFILIATION FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																							\n");
		sql.append("LEFT OUTER JOIN																																											\n");
		sql.append("(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL , crt_date FROM TB_MASTER a WHERE "+WHEREQUERY[date]+")g					\n");
		sql.append("ON																																														\n");
		sql.append("	m.MEM_SEQ_NO = g.MEM_SEQ_NO 																																						\n");
		sql.append("ORDER BY TODAY_TOTAL DESC																																								\n");
		sql.append(")z																																														\n");
		sql.append("WHERE																																													\n");
		sql.append("z.TODAY_TOTAL >= ?																																										\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, startDate);
			pstmt.setInt(2, stair);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				result = rs.getInt("CNT");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		
		return result;
	}
	
	
/*	public ArrayList<MemberVO> selectStatistics(String startDate, String endDate, String date){
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		StringBuffer sql = new StringBuffer();
		if(date.equals("0")){
			selectTodaylist(startDate, endDate, date);
		}else if(date.equals("1")){
			selectWeeklist(startDate, endDate, date);
		}else if(date.equals("2")){
			selectMonthlist(startDate, endDate, date);
		}else if(date.equals("3")){
			selectYearlist(startDate, endDate, date);
		}
		
		try{
			while(rs.next()){
				MemberVO vo = new MemberVO();
				if(date.equals("1")){
					//주 일때 날짜
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs,pstmt);
		}
		
		return list;
	}
	
	
	
	public void selectTodaylist(String startDate, String endDate, String date){
		
	}
	
	
	public void selectWeeklist(String startDate, String endDate, String date){
		
	}
	
	
	public void selectMonthlist(String startDate, String endDate, String date){
	
	}
	
	
	public void selectYearlist(String startDate, String endDate, String date){
		
	}*/
	
	

}
