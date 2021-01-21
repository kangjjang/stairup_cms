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
import vo.MemberVO;

import com.mysql.jdbc.Statement;

public class DepartDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public DepartDao() throws SQLException{
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
	
	private static final String TEAMLIST [] = {
		"LEAGUE_DATE >=CURDATE()",
		"WEEKOFYEAR(LEAGUE_DATE) = WEEKOFYEAR(CURDATE())-1",
		
	};
	private static final String CONDITIONS [] = {
		"b.CRT_DATE >= CURDATE()",
		"b.CRT_DATE >= ? AND b.CRT_DATE <= ?",
		
	};
	private static final String WEEKOF [] = {
		"LEAGUE_DATE >=CURDATE()",
		"LEAGUE_DATE <=CURDATE()",
		
	};
	private static final String LEAGUERESULT [] = {
		"DEPART_WIN = DEPART_WIN+1",
		"DEPART_LOSE = DEPART_LOSE+1",
		"DEPART_DRAW = DEPART_DRAW+1"
	};
	public ArrayList<DepartVO> departList()throws SQLException{
		StringBuffer sql = new StringBuffer();
		ArrayList<DepartVO> list = new ArrayList<DepartVO>();
		
		sql.append("SELECT								\n");
		sql.append("		DEPART_SEQ_NO, DEPART_NAME	\n");
		sql.append("FROM								\n");
		sql.append("		TB_DEPART					\n");
		sql.append("ORDER BY DEPART_SEQ_NO ASC			\n");
		
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
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao departList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	
	public ArrayList<DepartVO> departListByAffiliation(int affiliationSeq)throws SQLException{
		
		StringBuffer sql = new StringBuffer();
		ArrayList<DepartVO> list = new ArrayList<DepartVO>();
		int cnt = 0;
		sql.append("SELECT														\n");
		sql.append("		DEPART_SEQ_NO, DEPART_NAME							\n");
		sql.append("FROM														\n");
		sql.append("		TB_DEPART											\n");
		sql.append("WHERE														\n");
		sql.append("		AFFILIATION_SEQ	= ?									\n");
		sql.append("ORDER BY DEPART_SEQ_NO ASC								\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, affiliationSeq);
			
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
	
	/*
	 * 소속 정보로 검색한 부서 리스트
	 */
	
	public ArrayList<DepartVO> MemberDepartList(String affiliationSeqNo, String departName)throws SQLException{
		StringBuffer sql = new StringBuffer();
		ArrayList<DepartVO> list = new ArrayList<DepartVO>();
		int cnt = 0;
		sql.append("SELECT														\n");
		sql.append("		DEPART_SEQ_NO, DEPART_NAME							\n");
		sql.append("FROM														\n");
		sql.append("		TB_DEPART											\n");
		sql.append("WHERE														\n");
		sql.append("		AFFILIATION_SEQ	= ?									\n");
		if(departName != null || !departName.equals("")){
			sql.append("AND	 DEPART_NAME LIKE CONCAT('%',?,'%')				\n");
		}
		sql.append("ORDER BY DEPART_SEQ_NO ASC								\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, affiliationSeqNo);
			if(departName != null || !departName.equals("")){
				pstmt.setString(++cnt, departName);
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				DepartVO vo = new DepartVO();
				vo.setDepartSeqNo(rs.getInt("DEPART_SEQ_NO"));
				vo.setDepartName(rs.getString("DEPART_NAME"));
				list.add(vo);
			}
						
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao departList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	
	/*
	 *  소속 정보 리스트 2016-07-20 이준호
	 */
	
	public ArrayList<AffiliationVO> affiliationtList(String searchKeyword)throws SQLException{
		StringBuffer sql = new StringBuffer();
		ArrayList<AffiliationVO> list = new ArrayList<AffiliationVO>();
		int cnt = 0;
		sql.append("SELECT												\n");
		sql.append("		SEQ_NO, AFFILIATION_NAME, AFFILIATION_PIC	\n");
		sql.append("FROM												\n");
		sql.append("		TB_AFFILIATION								\n");
		if(searchKeyword != null || !searchKeyword.equals("")){
			sql.append("WHERE  AFFILIATION_NAME LIKE CONCAT('%',?,'%')				\n");
		}
		sql.append("ORDER BY SEQ_NO DESC									\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(searchKeyword != null || !searchKeyword.equals("")){
				pstmt.setString(++cnt, searchKeyword);
			}
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
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	
	/*
	 * 2015-05-19 ksy 활성화된 부서 개수
	 */
	public int selectDepartVi()throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																\n");
		sql.append("		COUNT(DISTINCT a.MEM_DEPART) AS CNT							\n");
		sql.append("FROM																\n");
		sql.append("		TB_MEMBER a, TB_DEPART b									\n");
		sql.append("WHERE																\n");
		sql.append("		a.MEM_DEPART = b.DEPART_SEQ_NO AND a.MEM_RESULT <>'Y'		\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				result =rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao selectTim ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-14 ksy 팀별리그 등록 종료일은 등록일로부터 7일뒤
	 */
	public int insertTim(int homeDepartSeq, int awayDepartSeq, int plus)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO													\n");
		sql.append("			TB_TEAM_LEAGUE									\n");
		sql.append("(															\n");
		sql.append("	HOME_SEQ_NO, AWAY_SEQ_NO, CRT_DATE, LEAGUE_DATE			\n");
		sql.append(")															\n");
		sql.append("VALUES(														\n");
		sql.append("	?,?,NOW(), DATE_ADD(NOW(), INTERVAL +? DAY)				\n");
		sql.append(")															\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, homeDepartSeq);
			pstmt.setInt(2, awayDepartSeq);
			pstmt.setInt(3, plus);
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
			//////System.out.println("DepartDao insertTim ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-14 ksy 팀별리그 리스트
	 */
	public ArrayList<DepartVO> selectTimeLeagueList(int pageNo, int rowSize, int gubun)throws SQLException{
		int page = (pageNo -1)*rowSize;
		ArrayList<DepartVO> list = new ArrayList<DepartVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT														\n");
		sql.append("		b.DEPART_NAME AS HOME_NAME							\n");
		sql.append("	,	a.HOME_SEQ_NO AS HOME_SEQ							\n");
		sql.append("	,	b.DEPART_PIC  AS HOME_PIC							\n");
		sql.append("	,	c.DEPART_NAME AS AWAY_NAME							\n");
		sql.append("	,	a.AWAY_SEQ_NO AS AWAY_SEQ							\n");
		sql.append("	,	c.DEPART_PIC  AS AWAY_PIC							\n");
		sql.append("	,	CRT_DATE											\n");
		sql.append("	,	LEAGUE_DATE											\n");
		sql.append("	,	TEAM_SEQ_NO											\n");
		sql.append("FROM														\n");
		sql.append("		TB_TEAM_LEAGUE a									\n");
		sql.append("LEFT OUTER JOIN												\n");
		sql.append("		(													\n");
		sql.append("			SELECT											\n");
		sql.append("					DEPART_NAME, DEPART_SEQ_NO, DEPART_PIC	\n");
		sql.append("			FROM											\n");
		sql.append("					TB_DEPART)	b							\n");
		sql.append("ON															\n");
		sql.append("			a.HOME_SEQ_NO = b.DEPART_SEQ_NO					\n");
		sql.append("LEFT OUTER JOIN												\n");
		sql.append("		(													\n");
		sql.append("			SELECT											\n");
		sql.append("					DEPART_NAME, DEPART_SEQ_NO, DEPART_PIC	\n");
		sql.append("			FROM											\n");
		sql.append("					TB_DEPART) c							\n");
		sql.append("ON															\n");
		sql.append("			a.AWAY_SEQ_NO = c.DEPART_SEQ_NO					\n");
		sql.append("WHERE	"+WEEKOF[gubun]+"									\n");
		sql.append("ORDER BY													\n");
		sql.append("			CRT_DATE DESC									\n");
		sql.append("LIMIT ?,?													\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, page);
			pstmt.setInt(2, rowSize);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				DepartVO vo = new DepartVO();
				vo.setHomeName(rs.getString("HOME_NAME"));
				vo.setHomeSeqNo(rs.getInt("HOME_SEQ"));
				vo.setHomePic(rs.getString("HOME_PIC"));
				vo.setAwayName(rs.getString("AWAY_NAME"));
				vo.setAwaySeqNo(rs.getInt("AWAY_SEQ"));
				vo.setAwayPic(rs.getString("AWAY_PIC"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setEndDate(rs.getString("LEAGUE_DATE"));
				vo.setLeagueSeqNo(rs.getInt("TEAM_SEQ_NO"));
				list.add(vo);
			}
			
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao selectTimeLeagueList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
		
	}
	/*
	 * 2015-05-15 ksy 팀별리그 총 개수
	 */
	public int leagueCnt()throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT									\n");
		sql.append("		COUNT(*) AS CNT					\n");
		sql.append("FROM									\n");
		sql.append("		TB_TEAM_LEAGUE					\n");
		sql.append("WHERE									\n");
		sql.append("		LEAGUE_DATE >=CURDATE()			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao leagueCnt ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-15 ksy 팀별리그 api 리스트
	 */
	public ArrayList<DepartVO> selectTeamLeagueList(int memSeqNo, int rowCate, int pageNo, int rowSize)throws SQLException{
		int page = (pageNo -1)*rowSize;
		int cnt = 0;
		/*int row = rowCate -1;*/
		ArrayList<DepartVO> list = new ArrayList<DepartVO>();
		StringBuffer sql = new StringBuffer();
		if(rowCate==1){
			TeamLeagueLive(memSeqNo, pageNo, rowSize);					// 팀별리그 라이브
		}else{
			TeamLeagueLastWeek(memSeqNo, pageNo, rowSize);				// 팀별리그 지난주
		}
		
		try{
			/*pstmt = conn.prepareStatement(sql.toString());
			if(rowCate>=1)pstmt.setInt(++cnt, memSeqNo);
			if(rowCate == 2){
				pstmt.setInt(++cnt, memSeqNo);
			}
			if(rowCate>=1)pstmt.setInt(++cnt, page);
			if(rowCate>=1)pstmt.setInt(++cnt, rowSize);*/
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				DepartVO vo = new DepartVO();
				vo.setHomeName(rs.getString("HOME_NAME"));
				vo.setHomeSeqNo(rs.getInt("HOME_SEQ_NO"));
				vo.setHomePic(rs.getString("HOME_PIC"));
				vo.setAwayName(rs.getString("AWAY_NAME"));
				vo.setAwaySeqNo(rs.getInt("AWAY_SEQ_NO"));
				vo.setAwayPic(rs.getString("AWAY_PIC"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setLeagueSeqNo(rs.getInt("TEAM_SEQ_NO"));
				vo.setEndDate(rs.getString("LEAGUE_DATE"));
				if(rowCate ==2){
					vo.setHomeWalkCnt(rs.getInt("HOME_WALK_CNT"));
					vo.setAwayWalkCnt(rs.getInt("AWAY_WALK_CNT"));
					vo.setDepartPeople(rs.getInt("HOME_PEOPLE"));
					vo.setAwayDepartPeople(rs.getInt("AWAY_PEOPLE"));
				}
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao selectTeamLeagueList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-26 ksy 팀별리그 라이브
	 */
	public String TeamLeagueLive(int memSeqNo, int pageNo, int rowSize) throws SQLException{
		int page = (pageNo -1)*rowSize;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																							\n");
		sql.append("		b.DEPART_NAME AS HOME_NAME																\n");
		sql.append("	,	a.HOME_SEQ_NO AS HOME_SEQ_NO															\n");
		sql.append("	,	b.DEPART_PIC  AS HOME_PIC																\n");
		sql.append("	,	c.DEPART_NAME AS AWAY_NAME																\n");
		sql.append("	,	a.AWAY_SEQ_NO AS AWAY_SEQ_NO															\n");
		sql.append("	,	c.DEPART_PIC  AS AWAY_PIC																\n");
		sql.append("	,	a.CRT_DATE																				\n");
		sql.append("	,	LEAGUE_DATE																				\n");
		sql.append("	,	TEAM_SEQ_NO																				\n");
		sql.append("FROM																							\n");
		sql.append("		TB_TEAM_LEAGUE a																		\n");
		sql.append("LEFT OUTER JOIN																					\n");
		sql.append("		(																						\n");
		sql.append("			SELECT																				\n");
		sql.append("					DEPART_NAME, DEPART_SEQ_NO, DEPART_PIC										\n");
		sql.append("			FROM																				\n");
		sql.append("					TB_DEPART)	b																\n");
		sql.append("ON																								\n");
		sql.append("			a.HOME_SEQ_NO = b.DEPART_SEQ_NO														\n");
		sql.append("LEFT OUTER JOIN																					\n");
		sql.append("		(																						\n");
		sql.append("			SELECT																				\n");
		sql.append("					DEPART_NAME, DEPART_SEQ_NO, DEPART_PIC										\n");
		sql.append("			FROM																				\n");
		sql.append("					TB_DEPART) c																\n");
		sql.append("ON																								\n");
		sql.append("			a.AWAY_SEQ_NO = c.DEPART_SEQ_NO														\n");
		sql.append("LEFT OUTER JOIN																					\n");
		sql.append("			TB_MEMBER d																			\n");
		sql.append("ON																								\n");
		sql.append("			a.HOME_SEQ_NO = d.MEM_DEPART OR a.AWAY_SEQ_NO = d.MEM_DEPART						\n");
		sql.append("WHERE		LEAGUE_DATE >=CURDATE() AND MEM_SEQ_NO = ?											\n");
		sql.append("UNION																							\n");
		sql.append("SELECT																							\n");
		sql.append("		b.DEPART_NAME AS HOME_NAME																\n");
		sql.append("	,	a.HOME_SEQ_NO AS HOME_SEQ																\n");
		sql.append("	,	b.DEPART_PIC  AS HOME_PIC																\n");
		sql.append("	,	c.DEPART_NAME AS AWAY_NAME																\n");
		sql.append("	,	a.AWAY_SEQ_NO AS AWAY_SEQ																\n");
		sql.append("	,	c.DEPART_PIC  AS AWAY_PIC																\n");
		sql.append("	,	CRT_DATE																				\n");
		sql.append("	,	LEAGUE_DATE																				\n");
		sql.append("	,	TEAM_SEQ_NO																				\n");
		sql.append("FROM																							\n");
		sql.append("		TB_TEAM_LEAGUE a																		\n");
		sql.append("LEFT OUTER JOIN																					\n");
		sql.append("		(																						\n");
		sql.append("			SELECT																				\n");
		sql.append("					DEPART_NAME, DEPART_SEQ_NO, DEPART_PIC										\n");
		sql.append("			FROM																				\n");
		sql.append("					TB_DEPART)	b																\n");
		sql.append("ON																								\n");
		sql.append("			a.HOME_SEQ_NO = b.DEPART_SEQ_NO														\n");
		sql.append("LEFT OUTER JOIN																					\n");
		sql.append("		(																						\n");
		sql.append("			SELECT																				\n");
		sql.append("					DEPART_NAME, DEPART_SEQ_NO, DEPART_PIC										\n");
		sql.append("			FROM																				\n");
		sql.append("					TB_DEPART) c																\n");
		sql.append("ON																								\n");
		sql.append("			a.AWAY_SEQ_NO = c.DEPART_SEQ_NO														\n");
		sql.append("WHERE		LEAGUE_DATE >=CURDATE()																\n");
		sql.append("LIMIT ?,?																						\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, page);
			pstmt.setInt(3, rowSize);
			//////System.out.println("팀별리그 라이브 : "+pstmt.toString());
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao TeamLeagueLive ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return sql.toString();
	}
	/*
	 * 2015-05-26 ksy 팀별 리그 지난주
	 */
	public String TeamLeagueLastWeek(int memSeqNo, int pageNo, int rowSize)throws SQLException{
		int page = (pageNo - 1)*rowSize;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		(SELECT SUM(TODAY_TOTAL) FROM TB_MASTER WHERE WEEKOFYEAR(CRT_DATE) = WEEKOFYEAR(CURDATE())-1 AND MEM_SEQ_NO IN (SELECT MEM_SEQ_NO FROM TB_MEMBER WHERE MEM_DEPART = z.HOME_SEQ_NO )) AS HOME_WALK_CNT,					\n");
		sql.append("		(SELECT SUM(TODAY_TOTAL) FROM TB_MASTER WHERE WEEKOFYEAR(CRT_DATE) = WEEKOFYEAR(CURDATE())-1 AND MEM_SEQ_NO IN (SELECT MEM_SEQ_NO FROM TB_MEMBER WHERE MEM_DEPART = z.AWAY_SEQ_NO )) AS AWAY_WALK_CNT,					\n");
		sql.append("		(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO = z.HOME_SEQ_NO) AS HOME_NAME,																																	\n");
		sql.append("		(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO = z.AWAY_SEQ_NO) AS AWAY_NAME,																																	\n");
		sql.append("		(SELECT DEPART_PIC FROM TB_DEPART WHERE DEPART_SEQ_NO = z.HOME_SEQ_NO) AS HOME_PIC,																																		\n");
		sql.append("		(SELECT DEPART_PIC FROM TB_DEPART WHERE DEPART_SEQ_NO = z.AWAY_SEQ_NO) AS AWAY_PIC,																																		\n");
		sql.append("		(SELECT DEPART_people FROM TB_DEPART WHERE DEPART_SEQ_NO = z.HOME_SEQ_NO) AS HOME_PEOPLE,																																\n");
		sql.append("		(SELECT DEPART_people FROM TB_DEPART WHERE DEPART_SEQ_NO = z.AWAY_SEQ_NO) AS AWAY_PEOPLE,																																\n");
		sql.append("		z.CRT_DATE, LEAGUE_DATE, TEAM_SEQ_NO, z.HOME_SEQ_NO, z.AWAY_SEQ_NO																																						\n");
		sql.append("FROM																																																							\n");
		sql.append("		(SELECT HOME_SEQ_NO, AWAY_SEQ_NO, TEAM_SEQ_NO, LEAGUE_DATE, CRT_DATE FROM TB_TEAM_LEAGUE WHERE WEEKOFYEAR(LEAGUE_DATE) = WEEKOFYEAR(CURDATE())-1)z																		\n");
		sql.append("LEFT OUTER JOIN 																																																				\n");
		sql.append("		TB_MEMBER p																																																				\n");
		sql.append("ON																																																								\n");
		sql.append("		z.HOME_SEQ_NO = p.MEM_DEPART OR z.AWAY_SEQ_NO = p.MEM_DEPART																																							\n");
		sql.append("WHERE																																																							\n");
		sql.append("		p.MEM_SEQ_NO = ?																																																		\n");
		sql.append("UNION ALL																																																						\n");
		sql.append("SELECT																																																							\n");
		sql.append("		(SELECT SUM(TODAY_TOTAL) FROM TB_MASTER WHERE WEEKOFYEAR(CRT_DATE) = WEEKOFYEAR(CURDATE())-1 AND MEM_SEQ_NO IN (SELECT MEM_SEQ_NO FROM TB_MEMBER WHERE MEM_DEPART = z.HOME_SEQ_NO )) AS HOME_WALK_CNT,					\n");
		sql.append("		(SELECT SUM(TODAY_TOTAL) FROM TB_MASTER WHERE WEEKOFYEAR(CRT_DATE) = WEEKOFYEAR(CURDATE())-1 AND MEM_SEQ_NO IN (SELECT MEM_SEQ_NO FROM TB_MEMBER WHERE MEM_DEPART = z.AWAY_SEQ_NO )) AS AWAY_WALK_CNT,					\n");
		sql.append("		(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO = z.HOME_SEQ_NO) AS HOME_NAME,																																	\n");
		sql.append("		(SELECT DEPART_NAME FROM TB_DEPART WHERE DEPART_SEQ_NO = z.AWAY_SEQ_NO) AS AWAY_NAME,																																	\n");
		sql.append("		(SELECT DEPART_PIC FROM TB_DEPART WHERE DEPART_SEQ_NO = z.HOME_SEQ_NO) AS HOME_PIC,																																		\n");
		sql.append("		(SELECT DEPART_PIC FROM TB_DEPART WHERE DEPART_SEQ_NO = z.AWAY_SEQ_NO) AS AWAY_PIC,																																		\n");
		sql.append("		(SELECT DEPART_people FROM TB_DEPART WHERE DEPART_SEQ_NO = z.HOME_SEQ_NO) AS HOME_PEOPLE,																																\n");
		sql.append("		(SELECT DEPART_people FROM TB_DEPART WHERE DEPART_SEQ_NO = z.AWAY_SEQ_NO) AS AWAY_PEOPLE,																																\n");
		sql.append("		z.CRT_DATE, LEAGUE_DATE, TEAM_SEQ_NO, z.HOME_SEQ_NO, z.AWAY_SEQ_NO																																						\n");
		sql.append("FROM																																																							\n");
		sql.append("		(SELECT HOME_SEQ_NO, AWAY_SEQ_NO, TEAM_SEQ_NO, LEAGUE_DATE, CRT_DATE FROM TB_TEAM_LEAGUE WHERE WEEKOFYEAR(LEAGUE_DATE) = WEEKOFYEAR(CURDATE())-1)z																		\n");
		sql.append("LIMIT ?,?																																																						\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, page);
			pstmt.setInt(3, rowSize);
			//////System.out.println("팀별리그 지난주 : " +pstmt.toString());
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao TeamLeagueLastWeek ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return sql.toString();
		
	}
	/*
	 * 2015-05-15 ksy 나의 부서 경기
	 */
	public ArrayList<DepartVO> selectCMSLeaguReulst()throws SQLException{
		ArrayList<DepartVO> list = new ArrayList<DepartVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		(SELECT SUM(TODAY_TOTAL) FROM TB_MASTER WHERE WEEKOFYEAR(CRT_DATE) = WEEKOFYEAR(CURDATE())-1 AND MEM_SEQ_NO IN (SELECT MEM_SEQ_NO FROM TB_MEMBER WHERE MEM_DEPART = z.HOME_SEQ_NO )) AS HOME_WALK_CNT,					\n");
		sql.append("		(SELECT SUM(TODAY_TOTAL) FROM TB_MASTER WHERE WEEKOFYEAR(CRT_DATE) = WEEKOFYEAR(CURDATE())-1 AND MEM_SEQ_NO IN (SELECT MEM_SEQ_NO FROM TB_MEMBER WHERE MEM_DEPART = z.AWAY_SEQ_NO )) AS AWAY_WALK_CNT,					\n");
		sql.append("		(SELECT DEPART_people FROM TB_DEPART WHERE DEPART_SEQ_NO IN (SELECT MEM_DEPART FROM TB_MEMBER WHERE MEM_DEPART = z.HOME_SEQ_NO)) AS HOME_PEOPLE,																		\n");
		sql.append("		(SELECT DEPART_people FROM TB_DEPART WHERE DEPART_SEQ_NO IN (SELECT MEM_DEPART FROM TB_MEMBER WHERE MEM_DEPART = z.AWAY_SEQ_NO)) AS AWAY_PEOPLE,																		\n");
		sql.append("		z.CRT_DATE, LEAGUE_DATE, TEAM_SEQ_NO, z.HOME_SEQ_NO, z.AWAY_SEQ_NO																																						\n");
		sql.append("FROM																																																							\n");
		sql.append("		(SELECT HOME_SEQ_NO, AWAY_SEQ_NO, TEAM_SEQ_NO, LEAGUE_DATE, CRT_DATE FROM TB_TEAM_LEAGUE)z																																\n");
		sql.append("WHERE																																																							\n");
		sql.append("		WEEKOFYEAR(LEAGUE_DATE) = WEEKOFYEAR(CURDATE())-1 																													 													\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				DepartVO vo = new DepartVO();
				vo.setHomeWalkCnt(rs.getInt("HOME_WALK_CNT"));
				vo.setAwayWalkCnt(rs.getInt("AWAY_WALK_CNT"));
				vo.setHomeSeqNo(rs.getInt("HOME_SEQ_NO"));
				vo.setAwaySeqNo(rs.getInt("AWAY_SEQ_NO"));
				vo.setDepartPeople(rs.getInt("HOME_PEOPLE"));
				vo.setAwayDepartPeople(rs.getInt("AWAY_PEOPLE"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao selectCMSLeaguReulst ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-19 ksy 부서 등록
	 */
	public int insertDepart(String departName, int departCnt, String fileName, int affiliationSeq)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO																	\n");
		sql.append("			TB_DEPART														\n");
		sql.append("(																			\n");
		sql.append("	DEPART_NAME, DEPART_PIC, DEPART_PEOPLE, AFFILIATION_SEQ, CRT_DATE		\n");
		sql.append(")																			\n");
		sql.append("VALUES(																			\n");
		sql.append("	?, ?, ?, ?, NOW()															\n");
		sql.append(")											\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, departName);
			pstmt.setString(2, fileName);
			pstmt.setInt(3, departCnt);
			pstmt.setInt(4, affiliationSeq);
			
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
			//////System.out.println("DepartDao insertDepart ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-19 ksy CMS 부서 리스트
	 */
	public ArrayList<DepartVO> departList(int pageno, int rowSize,String searchType, String keyword, int affiliationSeq) throws SQLException{
		int cnt =0;
		int page = (pageno -1)*rowSize;
		ArrayList<DepartVO> list = new ArrayList<DepartVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT											\n");
		sql.append("		AFFILIATION_NAME, DEPART_SEQ_NO, DEPART_NAME, DEPART_PIC,  DEPART_PEOPLE										\n");
		sql.append("FROM											\n");
		sql.append("		TB_DEPART b								\n");
		sql.append("LEFT OUTER JOIN								\n");
		sql.append("		TB_AFFILIATION a								\n");
		sql.append("ON a.SEQ_NO = b.AFFILIATION_SEQ								\n");
		if(affiliationSeq > 0){
			sql.append("WHERE		b.AFFILIATION_SEQ = ?		\n");
			if(searchType.equals("1")){
				sql.append("AND		DEPART_NAME LIKE CONCAT('%',?,'%')		\n");
			}
		}else if(searchType.equals("1")){
			sql.append("WHERE		DEPART_NAME LIKE CONCAT('%',?,'%')		\n");
			if(affiliationSeq > 0){
				sql.append("AND			b.AFFILIATION_SEQ = ?		\n");
			}
		}
		sql.append("ORDER BY						\n");
		sql.append("		DEPART_SEQ_NO DESC			\n");
		sql.append("LIMIT ?,?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(affiliationSeq > 0){
				pstmt.setInt(++cnt, affiliationSeq);
				if(searchType.equals("1")){
					pstmt.setString(++cnt, keyword);
				}
			}else if(searchType.equals("1")){
				pstmt.setString(++cnt, keyword);
				if(affiliationSeq > 0){
					pstmt.setInt(++cnt, affiliationSeq);
				}
			}
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				DepartVO vo = new DepartVO();
				vo.setHomeName(rs.getString("DEPART_NAME"));
				vo.setHomePic(rs.getString("DEPART_PIC"));
				vo.setHomeSeqNo(rs.getInt("DEPART_SEQ_NO"));
				vo.setDepartPeople(rs.getInt("DEPART_PEOPLE"));
				vo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao departList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}

		return list;
	}
	/*
	 * 2015-05-19 ksy 부서 총 개수
	 */
	public int departCnt()throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT						\n");
		sql.append("		COUNT(*) AS CNT		\n");
		sql.append("FROM						\n");
		sql.append("		TB_DEPART			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao departCnt ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-19 ksy 부서 상세 화면 (부서명, 사진, 인원수)
	 */
	public DepartVO departSelectView(int departSeqNo)throws SQLException{
		DepartVO vo = new DepartVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																						\n");
		sql.append("		AFFILIATION_NAME, DEPART_SEQ_NO, DEPART_NAME, DEPART_PIC, DEPART_PEOPLE, a.CRT_DATE						\n");
		sql.append("FROM																						\n");
		sql.append("		TB_DEPART a																		\n");
		sql.append("LEFT OUTER JOIN																			\n");
		sql.append("		TB_AFFILIATION b																\n");
		sql.append("ON a.AFFILIATION_SEQ = b.SEQ_NO															\n");
		sql.append("WHERE																						\n");
		sql.append("		DEPART_SEQ_NO = ?																	\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, departSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setHomeSeqNo(rs.getInt("DEPART_SEQ_NO"));
				vo.setHomePic(rs.getString("DEPART_PIC"));
				vo.setHomeName(rs.getString("DEPART_NAME"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setDepartPeople(rs.getInt("DEPART_PEOPLE"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao departSelectView ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	/*
	 * 2015-05-19 ksy 부서 정보 변경
	 */
	public int updateDepart(int departSeqNo, String departName, int departPeople, String listImgName, int affiliationSeq)throws SQLException{
		int cnt =0;
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE											\n");
		sql.append("		TB_DEPART								\n");
		sql.append("SET												\n");
		sql.append("		AFFILIATION_SEQ = ?							\n");
		sql.append("	,	DEPART_NAME = ?							\n");
		sql.append("	,	DEPART_PEOPLE = ?						\n");
		if(listImgName !=null&&!listImgName.equals(""))sql.append("	,	DEPART_PIC = ?							\n");
		sql.append("WHERE											\n");
		sql.append("		DEPART_SEQ_NO = ?						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, affiliationSeq);
			pstmt.setString(++cnt, departName);
			pstmt.setInt(++cnt, departPeople);
			if(listImgName != null&&!listImgName.equals(""))pstmt.setString(++cnt, listImgName);
			pstmt.setInt(++cnt, departSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao updateDepart ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
	/*
	 * 2015-05-21 ksy 팀별리그 상세화면
	 */
	public DepartVO selectTeamTotal(int teamSeq, String startDay, String endDay)throws SQLException{
		DepartVO vo = new DepartVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																					\n");
		sql.append("		 SUM(a.TODAY_TOTAL) AS TOTAL_WORK		\n");
		sql.append("FROM																					\n");
		sql.append("		TB_MASTER a																		\n");
		sql.append("LEFT OUTER JOIN																			\n");
		sql.append("		TB_MEMBER b																		\n");
		sql.append("ON																						\n");
		sql.append("		a.MEM_SEQ_NO = b.MEM_SEQ_NO														\n");
		sql.append("LEFT OUTER JOIN																			\n");
		sql.append("		TB_DEPART c																		\n");
		sql.append("ON																						\n");
		sql.append("		b.MEM_DEPART = c.DEPART_SEQ_NO													\n");
		sql.append("WHERE																					\n");
		sql.append("		DEPART_SEQ_NO = ?																\n");
		sql.append("	AND	a.CRT_DATE >= ? 																\n");
		sql.append("	AND a.CRT_DATE <= ?																	\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, teamSeq);
			pstmt.setString(2, startDay);
			pstmt.setString(3, endDay);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setWorkCnt(rs.getInt("TOTAL_WORK"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao selectTeamTotal ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
		
	}
	/*
	 * 2015-05-19 ksy 팀별리그 결성
	 */
	public ArrayList<DepartVO> selectTeamList(int gubun)throws SQLException{
		ArrayList<DepartVO> list = new ArrayList<DepartVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		DISTINCT DEPART_SEQ_NO			\n");
		sql.append("FROM							\n");
		sql.append("		TB_MEMBER a, TB_DEPART b				\n");
		sql.append("WHERE							\n");
		sql.append("		a.MEM_DEPART = b.DEPART_SEQ_NO		\n");
		sql.append("	AND a.MEM_RESULT <> 'Y'			\n");
		if(gubun==1){
			sql.append("	AND	DEPART_SEQ_NO <>37		\n");
		}
		sql.append("ORDER BY						\n");
		sql.append("		RAND()					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				DepartVO vo = new DepartVO();
				vo.setDepartSeqNo(rs.getInt("DEPART_SEQ_NO"));
				
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao selectTeamList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
		
	}
	/*
	 * 2015-05-21 ksy 오늘 팀리그 걸음수
	 */
	public int selectTeamToday(int teamSeq)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT										\n");
		sql.append("		SUM(a.TODAY_TOTAL) AS TODAY_TOTAL	\n");
		sql.append("FROM										\n");
		sql.append("		TB_MASTER a							\n");
		sql.append("LEFT OUTER JOIN								\n");
		sql.append("		TB_MEMBER b							\n");
		sql.append("ON											\n");
		sql.append("		a.MEM_SEQ_NO = b.MEM_SEQ_NO			\n");
		sql.append("LEFT OUTER JOIN								\n");
		sql.append("		TB_DEPART c							\n");
		sql.append("ON											\n");
		sql.append("		b.MEM_DEPART = c.DEPART_SEQ_NO		\n");
		sql.append("WHERE										\n");
		sql.append("		MEM_DEPART = ?						\n");
		sql.append("	AND a.CRT_DATE >= CURDATE()				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, teamSeq);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("TODAY_TOTAL");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao selectTeamToday ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-21 ksy 팀 리그 mvp
	 */
	public MemberVO selectTeamTop(int rowCate, int teamSeq, String startDay, String endDay)throws SQLException{
		int row = rowCate -1;
		int cnt = 0;
		MemberVO vo = new MemberVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT															\n");
		sql.append("		MEM_NAME, MEM_PIC, a.MEM_SEQ_NO							\n");
		sql.append("FROM															\n");
		sql.append("		TB_MEMBER a												\n");
		sql.append("LEFT OUTER JOIN													\n");
		sql.append("		TB_MASTER b												\n");
		sql.append("ON																\n");
		sql.append("		a.MEM_SEQ_NO = b.MEM_SEQ_NO								\n");
		sql.append("LEFT OUTER JOIN													\n");
		sql.append("		TB_DEPART c												\n");
		sql.append("ON																\n");
		sql.append("		a.MEM_DEPART = c.DEPART_SEQ_NO							\n");
		sql.append("WHERE															\n");
		sql.append("		a.MEM_DEPART = ?										\n");
		sql.append("	AND "+CONDITIONS[row]+"										\n");
		sql.append("GROUP BY														\n");
		sql.append("		a.MEM_SEQ_NO											\n");
		sql.append("ORDER BY														\n");
		sql.append("		TODAY_TOTAL DESC LIMIT 1								\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, teamSeq);
			if(rowCate ==2){
				pstmt.setString(++cnt, startDay);
				pstmt.setString(++cnt, endDay);
			}
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao selectTeamTop ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
		
	}
	/*
	 * 2015-05-21 kys 부서 정보
	 */
	public DepartVO selectDepartView(int teamSeq)throws SQLException{
		DepartVO vo = new DepartVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																					\n");
		sql.append("		DEPART_NAME, DEPART_PIC, DEPART_SEQ_NO, DEPART_PEOPLE, CRT_DATE					\n");
		sql.append("FROM																					\n");
		sql.append("		TB_DEPART																		\n");
		sql.append("WHERE																					\n");
		sql.append("		DEPART_SEQ_NO = ?																\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, teamSeq);
			rs=pstmt.executeQuery();
			if(rs.next()){
				vo.setDepartSeqNo(rs.getInt("DEPART_SEQ_NO"));
				vo.setDepartName(rs.getString("DEPART_NAME"));
				vo.setDepartPeople(rs.getInt("DEPART_PEOPLE"));
				vo.setHomePic(rs.getString("DEPART_PIC"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao selectDepartView ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
		
	}
	/*
	 * 2015-05-26 ksy 해당주에 등록된 리그가 있는지 확인
	 */
	public int checktoWeekLeague()throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																\n");
		sql.append("		COUNT(*) AS CNT												\n");
		sql.append("FROM																\n");
		sql.append("		TB_TEAM_LEAGUE												\n");
		sql.append("WHERE																\n");
		sql.append("		WEEKOFYEAR(LEAGUE_DATE) = WEEKOFYEAR(CURDATE())				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao checktoWeekLeague ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
	/*
	 * 2015-05-26 ksy  지난주 팀별리그 결과 저장
	 */
	public int departResultUpdate(int gubun, int departSeqNo)throws SQLException{
		int result =0;
		int gubunCate = gubun -1;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE														\n");
		sql.append("		TB_DEPART											\n");
		sql.append("SET		"+LEAGUERESULT[gubunCate]+"							\n");
		sql.append("	,	TOTAL_SCORE	=((DEPART_WIN)*3)+((DEPART_DRAW)*1)		\n");
		sql.append("WHERE														\n");
		sql.append("		DEPART_SEQ_NO = ?									\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, departSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao departResultUpdate ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-26 ksy  팀별랭킹
	 */
	public ArrayList<DepartVO> selectTeamRank(int memSeqNo, int pageNo, int rowSize)throws SQLException{
		int page = (pageNo -1)*rowSize;
		ArrayList<DepartVO> list = new ArrayList<DepartVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																											\n");
		sql.append("		z.*,																									\n");
		sql.append("		(SELECT COUNT(*)+1 FROM TB_DEPART WHERE TOTAL_SCORE >z.TOTAL_SCORE) AS RANK								\n");
		sql.append("FROM																											\n");
		sql.append("		(SELECT 																								\n");
		sql.append("				DEPART_SEQ_NO, DEPART_NAME, DEPART_PIC,															\n");
		sql.append("				DEPART_WIN, DEPART_LOSE, DEPART_DRAW, TOTAL_SCORE FROM TB_DEPART)z,								\n");
		sql.append("		TB_MEMBER p																								\n");
		sql.append("WHERE																											\n");
		sql.append("		z.DEPART_SEQ_NO = p.MEM_DEPART AND MEM_SEQ_NO = ?														\n");
		sql.append("UNION																											\n");
		sql.append("SELECT																											\n");
		sql.append("		z.*,																									\n");
		sql.append("		(SELECT COUNT(*)+1 FROM TB_DEPART WHERE TOTAL_SCORE >z.TOTAL_SCORE) AS RANK								\n");
		sql.append("FROM																											\n");
		sql.append("		(SELECT 																								\n");
		sql.append("				DEPART_SEQ_NO, DEPART_NAME, DEPART_PIC,															\n");
		sql.append("				DEPART_WIN, DEPART_LOSE, DEPART_DRAW, TOTAL_SCORE FROM TB_DEPART ORDER BY TOTAL_SCORE DESC)z	\n");
		sql.append("LIMIT ?,?																										\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, page);
			pstmt.setInt(3, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				DepartVO vo = new DepartVO();
				vo.setDepartName(rs.getString("DEPART_NAME"));
				vo.setHomePic(rs.getString("DEPART_PIC"));
				vo.setDepartWin(rs.getInt("DEPART_WIN"));
				vo.setDepartLose(rs.getInt("DEPART_LOSE"));
				vo.setDepartDraw(rs.getInt("DEPART_DRAW"));
				vo.setDepartRank(rs.getInt("RANK"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao selectTeamRank ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-28 ksy 팀별 리그 결과 초기화
	 */
	public int resetLeagueResult()throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE								\n");
		sql.append("		TB_DEPART					\n");
		sql.append("SET									\n");
		sql.append("		DEPART_WIN = 0				\n");
		sql.append("	,	DEPART_LOSE = 0				\n");
		sql.append("	,	DEPART_DRAW = 0				\n");
		sql.append("	,	TOTAL_SCORE = 0				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao resetLeagueResult ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016 - 07 - 27 CMS  부서 삭제
	 */
	public int departDelete(int departSeq) throws SQLException {
		int result=0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM 							\n");
		sql.append("		TB_DEPART				\n");
		sql.append("WHERE   DEPART_SEQ_NO = ? 		\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		pstmt.setInt(1,departSeq);
		
		result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("departDelete ERROR : " +e); 

		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016 - 07 - 27 CMS  소속 삭제
	 */
	public int affiliationDelete(int affiliationSeq) throws SQLException {
		int result=0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM 							\n");
		sql.append("		TB_AFFILIATION				\n");
		sql.append("WHERE   SEQ_NO = ? 		\n");
		affilDepartDelete(affiliationSeq);
		try{
		pstmt = conn.prepareStatement(sql.toString());
		pstmt.setInt(1,affiliationSeq);
		
		result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("affiliationDelete ERROR : " +e); 

		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016 - 07 - 27 CMS  소속의 하위 부서들 삭제
	 */
	public int affilDepartDelete(int affiliationSeq) throws SQLException {
		int result=0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM 							\n");
		sql.append("		TB_DEPART				\n");
		sql.append("WHERE   AFFILIATION_SEQ = ? 		\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		pstmt.setInt(1,affiliationSeq);
		
		result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("affilDepartDelete ERROR : " +e); 

		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-07-22 부서 검색
	 */
	/*public ArrayList<DepartVO> searchMemberList(String memberName, int memAffiliation)throws SQLException{
		ArrayList<DepartVO> list = new ArrayList<DepartVO>();
		StringBuffer sql = new StringBuffer();
		int cnt = 0;
		sql.append("SELECT																			\n");
		sql.append("		DEPART_SEQ_NO, DEPART_NAME, DEPART_PIC												\n");
		sql.append("FROM																			\n");
		sql.append("		TB_MEMBER a, TB_DEPART b												\n");
		sql.append("WHERE																			\n");
		sql.append("		DEPART_NAME"
				+ " LIKE CONCAT('%',?,'%')											\n");
		sql.append("	AND a.MEM_DEPART = b.DEPART_SEQ_NO											\n");
		sql.append("	AND a.MEM_RESULT = 'N'														\n");
		if(memAffiliation > 0){
			sql.append("	AND a.MEM_AFFILIATION = ? 																		\n");
		}
		sql.append("ORDER BY 																		\n");
		sql.append("		MEM_NAME ASC															\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, memberName);
			if(memAffiliation > 0){
				pstmt.setInt(++cnt, memAffiliation);
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				DepartVO vo = new DepartVO();
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemDepart(rs.getString("DEPART_NAME"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao searchMemberList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}
		
		return list;
	}*/
	
	/*
	 * 삭제 전 해당부서에 사람이 있는지 조회
	 */
	
	/*
	 * 2015-05-26 ksy 해당주에 등록된 리그가 있는지 확인
	 */
	public int selectDepartMember(int departSeq)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																\n");
		sql.append("		COUNT(*) AS CNT												\n");
		sql.append("FROM																\n");
		sql.append("		TB_MEMBER												\n");
		sql.append("WHERE																\n");
		sql.append("		MEM_DEPART = ?				\n");
		sql.append("AND																\n");
		sql.append("		MEM_RESULT <> 'Y'				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1,departSeq);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DepartDao checktoWeekLeague ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
}
