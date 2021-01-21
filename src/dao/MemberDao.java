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
import vo.MemberProfileVO;
import vo.MemberVO;

import com.mysql.jdbc.Statement;

public class MemberDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public MemberDao() throws SQLException{
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
	
	private static final String SELECTQUERY[] = {
		"WEEKOFYEAR(CRT_DATE) AS CRT_DATE",
		"SUBSTRING(CRT_DATE,1,7) AS CRT_DATE",
		
	};
	private static final String WHEREQUERY[] = {
		"AND WEEKOFYEAR(CRT_DATE) >= WEEKOFYEAR(CURDATE() - INTERVAL 49 DAY) AND WEEKOFYEAR(CRT_DATE)<= WEEKOFYEAR(CURDATE()) GROUP BY WEEKOFYEAR(CRT_DATE) ORDER BY WEEKOFYEAR(CRT_DATE) DESC LIMIT 7",
		"AND SUBSTRING(CRT_DATE,1,7) <= SUBSTRING(CURDATE(),1,7) GROUP BY SUBSTRING(CRT_DATE,1,7) ORDER BY SUBSTRING(CRT_DATE,1,7) DESC LIMIT 12",
		
	};
	
	
	
	/*
	 * 2016-10-04 회원가입부분 수정
	 */
	public int memberInsert(String memPic, String memId, String memberPw, String memName, String memNumber, String dvcToken, String dvcOs, int affiliationSeq, int departSeq)throws SQLException{
		
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("INSERT INTO 																																\n");
		sql.append("			TB_MEMBER																														\n");
		sql.append("(																																			\n");
		sql.append("			MEM_PIC, MEM_AFFILIATION, MEM_DEPART, MEM_ID, MEM_PW, MEM_NAME, MEM_NUMBER, MEM_RESULT, DVC_TOKEN, DVC_OS, CRT_DATE, CHG_DATE						\n");
		sql.append(")																																			\n");
		sql.append("SELECT 		?, ?, ?, ?, ?, ?, ?, 'N', ?, ?, NOW(), NOW()																						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(++cnt, memPic);
			pstmt.setInt(++cnt, affiliationSeq);
			pstmt.setInt(++cnt, departSeq);
			pstmt.setString(++cnt, memId);
			pstmt.setString(++cnt, memberPw);
			
			pstmt.setString(++cnt, memName);
			pstmt.setString(++cnt, memNumber);
			pstmt.setString(++cnt, dvcToken);
			pstmt.setString(++cnt, dvcOs);
			
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
			//////System.out.println("MemberDao memberInsert ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 *   2016-10-03 ljh 아이디 중복 체크
	 */
	public MemberVO selectNumCheck(String memId, String memPw, int memAffiliation){
		MemberVO vo = new MemberVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		MEM_ID					\n");
		sql.append("FROM								\n");
		sql.append("		TB_MEMBER					\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_ID = ?					\n");
		sql.append("AND		MEM_PW = ?					\n");
		sql.append("AND 	MEM_AFFILIATION = ?			\n");
		sql.append("AND 	MEM_RESULT <> 'Y'			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, memId);
			pstmt.setString(2, memPw);
			pstmt.setInt(3, memAffiliation);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemId(rs.getString("MEM_ID"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs,pstmt);
		}
		return vo;
	}
	
	
	/*
	 *   2016-01-14 ljh 아이디 중복 체크
	 */
	public MemberVO selectMemberCheck(String memId, String memPw, int memAffiliation){
		MemberVO vo = new MemberVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		MEM_ID, MEM_SEQ_NO			\n");
		sql.append("FROM								\n");
		sql.append("		TB_MEMBER a					\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_ID = ?					\n");
		//sql.append("AND		MEM_PW = ?					\n");
		//sql.append("AND 	MEM_AFFILIATION = ?			\n");
		sql.append("AND 	MEM_RESULT <> 'Y'			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, memId);
			//pstmt.setString(2, memPw);
			//pstmt.setInt(3, memAffiliation);
			
			//System.out.println(pstmt);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemId(rs.getString("MEM_ID"));
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs,pstmt);
		}
		return vo;
	}
	
	/*
	 *   2018-02-01 로그인
	 */
	public MemberVO selectMemberLogin(String memId, String memPw, int memAffiliation){
		MemberVO vo = new MemberVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		MEM_ID, MEM_SEQ_NO			\n");
		sql.append("FROM								\n");
		sql.append("		TB_MEMBER a					\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_ID = ?					\n");
		sql.append("AND		MEM_PW = ?					\n");
		sql.append("AND 	MEM_AFFILIATION = ?			\n");
		sql.append("AND 	MEM_RESULT <> 'Y'			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, memId);
			pstmt.setString(2, memPw);
			pstmt.setInt(3, memAffiliation);
			
			//System.out.println(pstmt);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemId(rs.getString("MEM_ID"));
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs,pstmt);
		}
		return vo;
	}
	
	public int selectMemberInfoById(String memId){
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		COUNT(*) AS CNT			\n");
		sql.append("FROM								\n");
		sql.append("		TB_MEMBER a					\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_ID = ?					\n");
		sql.append("AND 								\n");
		sql.append("	 	MEM_RESULT <> 'Y'			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, memId);
			
			//System.out.println(pstmt);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs,pstmt);
		}
		return result;
	}
	
	/*
	 * 2015-04-20 ksy 회원 로그인
	 */
	public MemberVO memberLogin(String memName, String memDepart, String memNumber,int memAffiliation)throws SQLException{
		
		MemberVO vo = new MemberVO();
		StringBuffer sql = new StringBuffer();
		int cnt =0;
		sql.append("SELECT																																\n");
		sql.append("		a.MEM_SEQ_NO, MEM_RESULT, MEM_NAME,DEPART_NAME,																				\n");
		sql.append("		(SELECT WALK_UP FROM TB_MASTER WHERE MEM_SEQ_NO = a.MEM_SEQ_NO AND CRT_DATE >=CURDATE()) AS ONN,							\n");
		sql.append("		(SELECT WALK_DOWN FROM TB_MASTER WHERE MEM_SEQ_NO = a.MEM_SEQ_NO AND CRT_DATE >=CURDATE()) AS OFF,							\n");
		if(memAffiliation > 0){
			sql.append("		(SELECT COUNT(*)+1 FROM TB_MASTER a, TB_MEMBER b WHERE a.MEM_SEQ_NO = b.MEM_SEQ_NO AND a.CRT_DATE >= CURDATE() AND b.MEM_AFFILIATION = ? AND TODAY_TOTAL > ONN+OFF )AS RANK,														\n");
		}else{
			sql.append("		(SELECT COUNT(*)+1 FROM TB_MASTER WHERE CRT_DATE>=CURDATE() AND TODAY_TOTAL > ONN+OFF)AS RANK,														\n");
		}
		sql.append("		MEM_PIC, c.CITY_SEQ_NO, d.ORDERLY, DAY_PIC, NIGHT_PIC, c.START_DATE															\n");
		sql.append("	,(SELECT START_DATE FROM TB_MEM_STAY WHERE MEM_SEQ_NO = a.MEM_SEQ_NO AND ORDERLY = 1 ORDER BY START_DATE DESC LIMIT 1) AS a		\n");
		sql.append("	,CITY_NAME, COUNTRY_NAME, COUNTRY_PIC																							\n");
		sql.append("	,(SELECT SUM(WALK_UP) FROM TB_MASTER WHERE MEM_SEQ_NO = a.MEM_SEQ_NO) AS LIFE_UP															\n");
		sql.append("	,(SELECT SUM(WALK_DOWN) FROM TB_MASTER WHERE MEM_SEQ_NO = a.MEM_SEQ_NO) AS LIFE_DOWN														\n");
		sql.append("FROM																																\n");
		sql.append("		TB_MEMBER a																													\n");
		sql.append("LEFT OUTER JOIN																														\n");
		sql.append("				TB_DEPART b																											\n");
		sql.append("ON																																	\n");
		sql.append("		a.MEM_DEPART = b.DEPART_SEQ_NO																								\n");
		sql.append("LEFT OUTER JOIN																														\n");
		sql.append("				TB_MEM_STAY c																										\n");
		sql.append("ON																																	\n");
		sql.append("		a.MEM_SEQ_NO = c.MEM_SEQ_NO																									\n");
		sql.append("LEFT OUTER JOIN																														\n");
		sql.append("				TB_CITY d																											\n");
		sql.append("ON																																	\n");
		sql.append("		c.CITY_SEQ_NO = d.CITY_SEQ_NO																								\n");
		sql.append("LEFT OUTER JOIN																														\n");
		sql.append("				TB_COUNTRY e																										\n");
		sql.append("ON																																	\n");
		sql.append("		d.COUNTRY_SEQ_NO = e.COUNTRY_SEQ_NO																							\n");
		sql.append("WHERE																																\n");
		sql.append("		a.MEM_ID = ?																												\n");
		sql.append("	AND MEM_DEPART = ?																												\n");
		sql.append("	AND MEM_PW = ?																												\n");
		if(memAffiliation > 0){
			sql.append("	AND a.MEM_AFFILIATION = ?																												\n");
		}
		sql.append("ORDER BY START_DATE DESC LIMIT 1																									\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(memAffiliation > 0){
				pstmt.setInt(++cnt, memAffiliation);
			}
			pstmt.setString(++cnt, memName);
			pstmt.setString(++cnt, memDepart);
			pstmt.setString(++cnt, memNumber);
			if(memAffiliation > 0){
				pstmt.setInt(++cnt, memAffiliation);
			}
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemDepart(rs.getString("DEPART_NAME"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setUpCnt(rs.getInt("ONN"));
				vo.setDownCnt(rs.getInt("OFF"));
				vo.setRank(rs.getInt("RANK"));
				vo.setMemResult(rs.getString("MEM_RESULT"));
				vo.setLifeUp(rs.getInt("LIFE_UP"));
				vo.setLifeDown(rs.getInt("LIFE_DOWN"));
				CityVO city = vo.getCityVo();
				city.setCitySeqNo(rs.getInt("CITY_SEQ_NO"));
				city.setOrderLy(rs.getInt("ORDERLY"));
				city.setCityPic(rs.getString("DAY_PIC"));
				city.setCityNPic(rs.getString("NIGHT_PIC"));
				city.setStartDate(rs.getString("START_DATE"));
				city.setFstartDate(rs.getString("a"));
				city.setCountryName(rs.getString("COUNTRY_NAME"));
				city.setCountryPic(rs.getString("COUNTRY_PIC"));
				city.setCityName(rs.getString("CITY_NAME"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberLogin ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	/*
	 * 2015-04-30 ksy 최초 로그인
	 */
	public MemberVO memberFirstLogin(int memSeqNo)throws SQLException{
		StringBuffer sql = new StringBuffer();
		MemberVO vo = new MemberVO();
		sql.append("SELECT					\n");
		sql.append("		MEM_SEQ_NO,"
						+ "	MEM_RESULT		\n");
		sql.append("FROM					\n");
		sql.append("		TB_MEMBER		\n");
		sql.append("WHERE 					\n");
		sql.append("		MEM_SEQ_NO =?	\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO")); 
				vo.setMemResult(rs.getString("MEM_RESULT"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberFirstLogin ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return vo;
	}
	
	/*
	 * 2015-04-21 ksy 회원 정보 test
	 */
/*	public int memberUpdate(memName, memDepart, memNumber)throws SQLException{
		
	}*/
	/*
	 * 2015-04-21 kys test
	 */
	
	public ArrayList<MemberVO> testList(int memSeqNo)throws SQLException{
		StringBuffer sql = new StringBuffer();
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		
		sql.append("SELECT						\n");
		sql.append("		MEM_PIC				\n");
		sql.append("FROM						\n");
		sql.append("		TB_MEMBER			\n");
		sql.append("WHERE						\n");
		sql.append("		MEM_SEQ_NO = ?		\n");
	
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemPic(rs.getString("MEM_PIC"));
				
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao testList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return list;
	}
	/*
	 * 2015-04-29 ksy 가입유무 확인
	 */
	public MemberVO memberResult(String dvcToken) throws SQLException{
		MemberVO vo = new MemberVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT									\n");
		sql.append("		COUNT(*) AS CNT, MEM_RESULT		\n");
		sql.append("FROM									\n");
		sql.append("		TB_MEMBER						\n");
		sql.append("WHERE									\n");
		sql.append("		DVC_TOKEN = ?"
					+ "	AND MEM_RESULT <>'Y'				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, dvcToken);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setCnt(rs.getInt("CNT"));
				vo.setMemResult(rs.getString("MEM_RESULT"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberResult ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	/*
	 * 2015-05-06 ksy 회원 정보 수정
	 */
	public int memberUpdate( int memDepart, String listImg, int memSeqNo, int affiliationSeq)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MEMBER				\n");
		sql.append("SET								\n");
		sql.append("		MEM_DEPART = ?,			\n");
		sql.append("		MEM_AFFILIATION = ?			\n");
		if(listImg!=null&&!listImg.equals(""))sql.append("		,MEM_PIC = ?			\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, memDepart);
			pstmt.setInt(++cnt, affiliationSeq);
			if(listImg!=null&&!listImg.equals(""))pstmt.setString(++cnt, listImg);
			pstmt.setInt(++cnt, memSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberUpdate ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	public int memberUpdate(String memId, String memberPw, String memName, int affiliationSeq, int departSeq, int memSeqNo)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MEMBER				\n");
		sql.append("SET								\n");
		sql.append("		MEM_ID = ?,			\n");
		sql.append("		MEM_PW = ?,			\n");
		sql.append("		MEM_NAME = ?,			\n");
		sql.append("		MEM_AFFILIATION = ?,			\n");
		sql.append("		MEM_DEPART = ?			\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setString(++cnt, memId);
			pstmt.setString(++cnt, memberPw);
			pstmt.setString(++cnt, memName);
			pstmt.setInt(++cnt, affiliationSeq);
			pstmt.setInt(++cnt, departSeq);
			pstmt.setInt(++cnt, memSeqNo);
			
			result = pstmt.executeUpdate();
			
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberUpdate ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2017-07-028 회원 정보 - 소속 변경시 시장 정보 변경
	 */
	public int memberMayorUpdate(int memSeqNo, int memaffiliation)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM							\n");
		sql.append("		TB_MAYOR				\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		sql.append("AND		AFFILIATION_NO = ?		\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memaffiliation);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberUpdate ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	
	/*
	 * 2015-05-06 ksy 회원검색 및 리스트
	 */
	public ArrayList<MemberVO> searchMemberList(String memberName, int memAffiliation)throws SQLException{
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		StringBuffer sql = new StringBuffer();
		int cnt = 0;
		sql.append("SELECT																			\n");
		sql.append("		MEM_PIC, MEM_NAME, DEPART_NAME, MEM_SEQ_NO								\n");
		sql.append("FROM																			\n");
		sql.append("		TB_MEMBER a, TB_DEPART b												\n");
		sql.append("WHERE																			\n");
		sql.append("		MEM_NAME LIKE CONCAT('%',?,'%')											\n");
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
				MemberVO vo = new MemberVO();
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
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-06-03 ksy 검색 총 인원수
	 */
	public int searchMemberListCount(String schWord, int affiliation)throws SQLException{
		int result =0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT											\n");
		sql.append("		COUNT(*) AS CNT							\n");
		sql.append("FROM											\n");
		sql.append("		TB_MEMBER a, TB_DEPART b				\n");
		sql.append("WHERE											\n");
		sql.append("		MEM_NAME LIKE CONCAT('%',?,'%')			\n");
		sql.append("	AND a.MEM_DEPART = b.DEPART_SEQ_NO			\n");
		sql.append("	AND a.MEM_RESULT = 'N'						\n");
		if(affiliation > 0){
			sql.append("	AND a.MEM_AFFILIATION = ? 																		\n");
		}
	/*	sql.append("ORDER BY 										\n");
		sql.append("		MEM_NAME ASC							\n");*/
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, schWord);
			if(affiliation > 0){
				pstmt.setInt(++cnt, affiliation);
			}
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao searchMemberList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-06 ksy 내가 시장인 도시 상세 리스트
	 */
	
	public ArrayList<CityVO> memberMayorList(int memSeqNo) throws SQLException{
		ArrayList<CityVO> list = new ArrayList<CityVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT												\n");
		sql.append("		a.CITY_SEQ_NO,"
						+ "	CITY_NAME,"
						+ " CITY_PIC,"
						+ " COUNTRY_NAME,"
						+ " a.CRT_DATE									\n");
		sql.append("FROM												\n");
		sql.append("		TB_MAYOR a									\n");
		sql.append("LEFT OUTER JOIN										\n");
		sql.append("		TB_CITY b									\n");
		sql.append("ON a.CITY_SEQ_NO = b.CITY_SEQ_NO					\n");
		sql.append("LEFT OUTER JOIN										\n");
		sql.append("		TB_COUNTRY c								\n");
		sql.append("ON b.COUNTRY_SEQ_NO = c.COUNTRY_SEQ_NO				\n");
		sql.append("WHERE												\n");
		sql.append("		a.MEM_SEQ_NO = ?							\n");
		sql.append("ORDER BY 											\n");
		sql.append("		a.CRT_DATE DESC								\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			rs = pstmt.executeQuery();
			while(rs.next()){
				CityVO vo = new CityVO();
				vo.setCitySeqNo(rs.getInt("CITY_SEQ_NO"));
				vo.setCityName(rs.getString("CITY_NAME"));
				vo.setCityPic(rs.getString("CITY_PIC"));
				vo.setCountryCity(rs.getString("COUNTRY_NAME"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberMayorList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	
	/*
	 * 2015-05-07 ksy 관심친구 등록
	 * memSeqNo : 내 memSeqNo
	 * matMemSeq : 상대 memSeqNo
	 */
	public int friendAdd(int memSeqNo, int matMemSeq)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO				\n");
		sql.append("			TB_FRIEND	\n");
		sql.append("(						\n");
		sql.append("	MEM_SEQ_NO,"
					+ "	HE_SEQ_NO,"
					+ " FRIEND_LEVEL,"
					+ " CRT_DATE			\n");
		sql.append(")						\n");
		sql.append("VALUES(					\n");
		sql.append("	?,"
					+ "	?,"
					+ " 1,"
					+ " NOW()				\n");
		sql.append(")						\n");
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, matMemSeq);
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
			//////System.out.println("MemberDao friendAdd ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-07 ksy 친구 취소
	 * memSeqNo : 내 memSeqNo
	 * matMemSeq : 상대 memSeqNo
	 */
	public int friendDelete(int memSeqNo, int matMemSeq)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE	FROM						\n");
		sql.append("				TB_FRIEND			\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?"
					+ "	AND HE_SEQ_NO  = ?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, matMemSeq);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao friendDelete ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-07 ksy 친구 리스트
	 */
	public ArrayList<MemberVO>friendList(int memSeqNo)throws SQLException{
		int cnt = 0;
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																							\n");
		sql.append("		HE_SEQ_NO,"
						+ "	MEM_PIC,"
						+ " MEM_NAME,"
						+ " (SELECT 																																				\n");
		sql.append("				TODAY_TOTAL																																		\n");
		sql.append("		FROM																																					\n");
		sql.append("				TB_MASTER c																																		\n");
		sql.append("		WHERE																																					\n");
		sql.append("				c.CRT_DATE > CURDATE()																															\n");
		sql.append("			AND a.HE_SEQ_NO = c.MEM_SEQ_NO																														\n");
		sql.append("		) AS CNT																																				\n");
		sql.append("	,	(SELECT COUNTRY_PIC FROM TB_COUNTRY WHERE COUNTRY_SEQ_NO = (SELECT COUNTRY_SEQ_NO FROM TB_CITY WHERE CITY_SEQ_NO = d.CITY_SEQ_NO)) AS COUNTRY_PIC			\n");
		sql.append("	,	(SELECT CITY_NAME FROM TB_CITY WHERE CITY_SEQ_NO = d.CITY_SEQ_NO) AS CITY_NAME																			\n");
		sql.append("	,	(SELECT COUNT(*) FROM TB_WORLD WHERE MEM_SEQ_NO =?) AS WORLD_CNT																						\n");
		sql.append("	,	(SELECT COUNT(*)+1 FROM (SELECT MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER WHERE CRT_DATE >= CURDATE() GROUP BY MEM_SEQ_NO DESC)n 		\n");
		sql.append("			WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK																							\n");
		sql.append("FROM																																							\n");
		sql.append("		TB_FRIEND	a,"
						+ "	TB_MEMBER b																																				\n");
		sql.append("LEFT OUTER JOIN 																																				\n");
		sql.append("		TB_MEM_STAY d																																			\n");
		sql.append("ON																																								\n");
		sql.append("		b.MEM_SEQ_NO = d.MEM_SEQ_NO AND END_DATE IS NULL																										\n");
		sql.append("LEFT OUTER JOIN																																					\n");
		sql.append("		(SELECT MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER WHERE CRT_DATE >= CURDATE() GROUP BY MEM_SEQ_NO DESC)g								\n");
		sql.append("ON																																								\n");
		sql.append("		b.MEM_SEQ_NO = g.MEM_SEQ_NO																																\n");
		sql.append("WHERE																																							\n");
		sql.append("		a.MEM_SEQ_NO = ?																																		\n");
		sql.append("	AND a.HE_SEQ_NO = b.MEM_SEQ_NO																																\n");
		sql.append("	AND FRIEND_LEVEL = 1																																		\n");
		sql.append("ORDER BY 																																						\n");
		sql.append("		MEM_NAME ASC																																			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemSeqNo(rs.getInt("HE_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setFloorCnt(rs.getInt("CNT"));
				vo.setWorldCnt(rs.getInt("WORLD_CNT"));
				vo.setRank(rs.getInt("RANK"));
				CityVO city = vo.getCityVo();
				city.setCountryPic(rs.getString("COUNTRY_PIC"));
				city.setCityName(rs.getString("CITY_NAME"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao friendList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-07 ksy 즐겨찾기 친구 리스트
	 */
	public ArrayList<MemberVO> bestFriendList(int memSeqNo)throws SQLException{
		int cnt = 0;
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																							\n");
		sql.append("		HE_SEQ_NO,"
						+ "	MEM_PIC,"
						+ " MEM_NAME,"
						+ " (SELECT 																																				\n");
		sql.append("				TODAY_TOTAL																																		\n");
		sql.append("		FROM																																					\n");
		sql.append("				TB_MASTER c																																		\n");
		sql.append("		WHERE																																					\n");
		sql.append("				c.CRT_DATE >= CURDATE()																															\n");
		sql.append("			AND a.HE_SEQ_NO = c.MEM_SEQ_NO																														\n");
		sql.append("		) AS CNT																																				\n");
		sql.append("	,	(SELECT COUNTRY_PIC FROM TB_COUNTRY WHERE COUNTRY_SEQ_NO = (SELECT COUNTRY_SEQ_NO FROM TB_CITY WHERE CITY_SEQ_NO = d.CITY_SEQ_NO)) AS COUNTRY_PIC			\n");
		sql.append("	,	(SELECT CITY_NAME FROM TB_CITY WHERE CITY_SEQ_NO = d.CITY_SEQ_NO) AS CITY_NAME																			\n");
		sql.append("	,	(SELECT COUNT(*) FROM TB_WORLD WHERE MEM_SEQ_NO =?) AS WORLD_CNT																						\n");
		sql.append("	,	(SELECT COUNT(*)+1 FROM (SELECT MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER WHERE CRT_DATE >= CURDATE() GROUP BY MEM_SEQ_NO DESC)n 		\n");
		sql.append("			WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK																							\n");
		sql.append("FROM																																							\n");
		sql.append("		TB_FRIEND	a,"
						+ "	TB_MEMBER b																																				\n");
		sql.append("LEFT OUTER JOIN 																																				\n");
		sql.append("		TB_MEM_STAY d																																			\n");
		sql.append("ON																																								\n");
		sql.append("		b.MEM_SEQ_NO = d.MEM_SEQ_NO AND END_DATE IS NULL																										\n");
		sql.append("LEFT OUTER JOIN																																					\n");
		sql.append("		(SELECT MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER WHERE CRT_DATE >= CURDATE() GROUP BY MEM_SEQ_NO DESC)g								\n");
		sql.append("ON																																								\n");
		sql.append("		b.MEM_SEQ_NO = g.MEM_SEQ_NO																																\n");
		sql.append("WHERE																																							\n");
		sql.append("		a.MEM_SEQ_NO = ?																																		\n");
		sql.append("	AND a.HE_SEQ_NO = b.MEM_SEQ_NO																																\n");
		sql.append("	AND FRIEND_LEVEL = 2																																		\n");
		sql.append("ORDER BY 																																						\n");
		sql.append("		MEM_NAME ASC																																			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemSeqNo(rs.getInt("HE_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setFloorCnt(rs.getInt("CNT"));
				vo.setWorldCnt(rs.getInt("WORLD_CNT"));
				vo.setRank(rs.getInt("RANK"));
				CityVO city = vo.getCityVo();
				city.setCountryPic(rs.getString("COUNTRY_PIC"));
				city.setCityName(rs.getString("CITY_NAME"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao bestFriendList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-07 ksy 회원 기본 적인 정보 / 이름,사진, 부서, 오른층수, 내려간층수, 도시 이름, 도시 사진
	 */
	public MemberVO memberInfo(int memSeqNo, int memAffiliation)throws SQLException{
		MemberVO vo = new MemberVO();
		int cnt=0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																											\n");
		sql.append("		a.MEM_SEQ_NO, b.DEPART_SEQ_NO, DEPART_NAME, AFFILIATION_NAME, MEM_NAME, MEM_PIC, c.CITY_SEQ_NO, c.ORDERLY, MEM_RESULT, DAY_PIC, NIGHT_PIC, c.START_DATE, CITY_NAME, COUNTRY_NAME, COUNTRY_PIC				\n");
		sql.append("	,(SELECT WALK_UP FROM TB_MASTER WHERE MEM_SEQ_NO = ? AND CRT_DATE >=CURDATE()) AS ONN																																				\n");
		sql.append("	,(SELECT WALK_DOWN FROM TB_MASTER WHERE MEM_SEQ_NO = ? AND CRT_DATE >=CURDATE()) AS OFF																														\n");
		if(memAffiliation > 0){
			sql.append("		, (SELECT COUNT(*)+1 FROM (SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a LEFT OUTER JOIN TB_MEMBER b ON a.MEM_SEQ_NO = b.MEM_SEQ_NO LEFT OUTER JOIN TB_MEMBER_AFFILIATION c ON b.MEM_SEQ_NO = c.MEMBER_SEQ WHERE b.MEM_RESULT <> 'Y' AND c.MAIN_YN <> 'N' AND c.AFFILIATION_SEQ = ? AND a.CRT_DATE >= CURDATE() GROUP BY a.MEM_SEQ_NO DESC)n WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK														\n");
		}else{
			sql.append("		, (SELECT COUNT(*)+1 FROM (SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a LEFT OUTER JOIN TB_MEMBER b ON a.MEM_SEQ_NO = b.MEM_SEQ_NO LEFT OUTER JOIN TB_MEMBER_AFFILIATION c ON b.MEM_SEQ_NO = c.MEMBER_SEQ WHERE b.MEM_RESULT <> 'Y' AND c.MAIN_YN <> 'N' AND a.CRT_DATE >= CURDATE() GROUP BY a.MEM_SEQ_NO DESC)n WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK																							\n");
		}
		sql.append("	,(SELECT START_DATE FROM TB_MEM_STAY WHERE MEM_SEQ_NO = ? AND ORDERLY = 1 ORDER BY START_DATE DESC LIMIT 1) AS a	\n");
		sql.append("	,(SELECT SUM(WALK_UP) FROM TB_MASTER WHERE MEM_SEQ_NO = ?) AS LIFE_UP												\n");
		sql.append("	,(SELECT SUM(WALK_DOWN) FROM TB_MASTER WHERE MEM_SEQ_NO =?) AS LIFE_DOWN											\n");
		sql.append("	,(SELECT COUNT(*) FROM TB_WORLD WHERE MEM_SEQ_NO = ?) AS CNT_WORLD													\n");
		sql.append("FROM 			TB_MEMBER a																																		\n");
		sql.append("LEFT OUTER JOIN																																					\n");
		sql.append("				TB_MEMBER_AFFILIATION f																															\n");
		sql.append("ON																																								\n");
		sql.append("	a.MEM_SEQ_NO = f.MEMBER_SEQ																																	\n");
		sql.append("LEFT OUTER JOIN 																																				\n");
		sql.append("				TB_DEPART b																																		\n");
		sql.append("ON																																								\n");
		sql.append("	f.DEPART_SEQ = b.DEPART_SEQ_NO																																\n");
		sql.append("LEFT OUTER JOIN																																					\n");
		sql.append("				TB_MEM_STAY c																																	\n");
		sql.append("ON																																								\n");
		sql.append("	a.MEM_SEQ_NO = c.MEM_SEQ_NO	AND END_DATE IS NULL																											\n");
		sql.append("LEFT OUTER JOIN 																																				\n");
		sql.append("				TB_CITY d																																		\n");
		sql.append("ON																																								\n");
		sql.append("	c.CITY_SEQ_NO = d.CITY_SEQ_NO																																\n");
		sql.append("LEFT OUTER JOIN																																					\n");
		sql.append("				TB_COUNTRY e																																	\n");
		sql.append("ON																																								\n");
		sql.append("	d.COUNTRY_SEQ_NO = e.COUNTRY_SEQ_NO																															\n");
		sql.append("LEFT OUTER JOIN																													\n");
		sql.append("				TB_AFFILIATION h																										\n");
		sql.append("ON																													\n");
		sql.append("	f.AFFILIATION_SEQ = h.SEQ_NO																													\n");
		
		sql.append("LEFT OUTER JOIN																													\n");
		sql.append("				(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a WHERE a.CRT_DATE >= CURDATE() GROUP BY a.MEM_SEQ_NO DESC)g																										\n");
		sql.append("ON																													\n");
		sql.append("	a.MEM_SEQ_NO = g.MEM_SEQ_NO																														\n");
		
		sql.append("WHERE																													\n");
		sql.append("		a.MEM_SEQ_NO = ? AND f.MAIN_YN = 'Y'																			\n");
		if(memAffiliation > 0){
			sql.append("	AND f.AFFILIATION_SEQ = ?																												\n");
		}
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			if(memAffiliation > 0){
				pstmt.setInt(++cnt, memAffiliation);
			}
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			if(memAffiliation > 0){
				pstmt.setInt(++cnt, memAffiliation);
			}
			
			//////System.out.println("##############memberInfo : " + pstmt);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemDepart(rs.getString("DEPART_NAME"));
				vo.setDepartSeq(rs.getInt("DEPART_SEQ_NO"));
				vo.setMemAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setUpCnt(rs.getInt("ONN"));
				vo.setDownCnt(rs.getInt("OFF"));
				vo.setRank(rs.getInt("RANK"));
				vo.setMemResult(rs.getString("MEM_RESULT"));
				vo.setLifeUp(rs.getInt("LIFE_UP"));
				vo.setLifeDown(rs.getInt("LIFE_DOWN"));
				vo.setWorldCnt(rs.getInt("CNT_WORLD"));
				CityVO city = vo.getCityVo();
				city.setCitySeqNo(rs.getInt("CITY_SEQ_NO"));
				city.setOrderLy(rs.getInt("ORDERLY"));
				city.setCityPic(rs.getString("DAY_PIC"));
				city.setCityNPic(rs.getString("NIGHT_PIC"));
				city.setStartDate(rs.getString("START_DATE"));
				city.setFstartDate(rs.getString("a"));
				city.setCityName(rs.getString("CITY_NAME"));
				city.setCountryName(rs.getString("COUNTRY_NAME"));
				city.setCountryPic(rs.getString("COUNTRY_PIC"));
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
	 * 2016-08-23 메인화면에 총 걸음수, 내가 상위 몇 % 인지 
	 */
	public MemberVO jehozzang(int memSeqNo)throws SQLException{
		MemberVO vo = new MemberVO();
		int cnt=0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																											\n");
		sql.append("	m.MEM_SEQ_NO, m.MEM_NAME, IFNULL((g.TODAY_TOTAL),0) AS TODAY_TOTAL,																											\n");
		sql.append("	(SELECT COUNT(*)+1 FROM (SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a, TB_MEMBER b WHERE a.MEM_SEQ_NO = b.MEM_SEQ_NO AND b.MEM_RESULT <>'Y'		\n");
		sql.append("	GROUP BY a.MEM_SEQ_NO DESC)n WHERE n.TODAY_TOTAL > IFNULL((g.TODAY_TOTAL),0)) AS RANK,																						\n");
		sql.append("	(SELECT COUNT(1) FROM TB_MEMBER) AS TOTAL_MEMBER																															\n");
		sql.append("FROM																																											\n");
		sql.append("	(SELECT MEM_SEQ_NO, MEM_NAME FROM TB_MEMBER WHERE MEM_RESULT <>'Y')m																	\n");
		sql.append("LEFT OUTER JOIN																																									\n");
		sql.append("	(SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a WHERE a.MEM_SEQ_NO = ?)g																			\n");
		sql.append("ON		 m.MEM_SEQ_NO = g.MEM_SEQ_NO																																			\n");
		sql.append("WHERE    m.MEM_SEQ_NO = ?																																				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setToday(rs.getInt("TODAY_TOTAL"));
				vo.setRank(rs.getInt("RANK"));
				vo.setCnt(rs.getInt("TOTAL_MEMBER"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberInfo ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return vo;
	}
	
	/*
	 * 2015-05-07 ksy 시장 정보 
	 */
	public MemberVO cityMayorMem(int citySeqNo, int affiliationSeq)throws SQLException{
		MemberVO vo = new MemberVO();
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																															\n");
		sql.append("		CITY_NAME, MEM_NAME, a.MEM_SEQ_NO, MEM_PIC,																				\n");
		sql.append("		(SELECT TO_DAYS(CURDATE()) - TO_DAYS(CRT_DATE) FROM TB_MAYOR WHERE CITY_SEQ_NO = ? AND DEL_YN <>'Y' 					\n");
		if(affiliationSeq > 0){
			sql.append("	AND AFFILIATION_NO = ? ORDER BY WALK_PERIOD ASC LIMIT 1 ) AS CHA,																														\n");
		}else{
			sql.append("	ORDER BY WALK_PERIOD ASC LIMIT 1 ) AS CHA,																														\n");
		}
		sql.append("		(SELECT GREETING_CONTENT FROM TB_GREETING ORDER BY RAND() LIMIT 1) AS CONTENT											\n");
		sql.append("FROM																															\n");
		sql.append("		TB_MAYOR a																												\n");
		sql.append("LEFT OUTER JOIN																													\n");
		sql.append("		TB_CITY b																												\n");
		sql.append("ON																																\n");
		sql.append("		a.CITY_SEQ_NO = b.CITY_SEQ_NO																							\n");
		sql.append("LEFT OUTER JOIN																													\n");
		sql.append("		TB_MEMBER c																												\n");
		sql.append("ON																																\n");
		sql.append("		a.MEM_SEQ_NO = c.MEM_SEQ_NO																								\n");
		sql.append("WHERE																															\n");
		sql.append("		a.CITY_SEQ_NO = ?	AND a.DEL_YN <>'Y'																					\n");
		if(affiliationSeq > 0){
			sql.append("	AND AFFILIATION_NO = ? 	ORDER BY WALK_PERIOD ASC LIMIT 1																													\n");
		}else{
			sql.append("	ORDER BY WALK_PERIOD ASC LIMIT 1																														\n");
		}
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, citySeqNo);
			if(affiliationSeq > 0){
				pstmt.setInt(++cnt, affiliationSeq);
			}
			pstmt.setInt(++cnt, citySeqNo);
			if(affiliationSeq > 0){
				pstmt.setInt(++cnt, affiliationSeq);
			}
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				CityVO city = vo.getCityVo();
				city.setCityName(rs.getString("CITY_NAME"));
				city.setCityDay(rs.getInt("CHA"));
				city.setContent(rs.getString("CONTENT"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao cityMayorMem ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	
	/*
	 * 2015-05-07 ksy 현재 위치에 있는 회원 리스트
	 */
	public ArrayList<MemberVO> cityMemberList(int citySeqNo, int memAffiliation, int memSeqNo)throws SQLException{
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		StringBuffer sql = new StringBuffer();
		int cnt = 0;
		sql.append("SELECT																								\n");
		sql.append("		a.MEM_SEQ_NO,	MEM_NAME, MEM_PIC, SUM(TODAY_TOTAL) AS TODAY_TOTAL							\n");
		sql.append("FROM																								\n");
		sql.append("		TB_MEM_STAY a																				\n");
		sql.append("LEFT OUTER JOIN																						\n");
		sql.append("		TB_MEMBER b																					\n");
		sql.append("ON																									\n");
		sql.append("		a.MEM_SEQ_NO = b.MEM_SEQ_NO																	\n");
		sql.append("LEFT OUTER JOIN																						\n");
		sql.append("		TB_MASTER c																					\n");
		sql.append("ON																									\n");
		sql.append("		b.MEM_SEQ_NO = c.MEM_SEQ_NO																	\n");
		sql.append("LEFT OUTER JOIN																								\n");
		sql.append("		TB_MEMBER_AFFILIATION d																					\n");
		sql.append("ON		b.MEM_SEQ_NO = d.MEMBER_SEQ																						\n");
		sql.append("WHERE																								\n");
		sql.append("		CITY_SEQ_NO = ?																				\n");
		sql.append("	AND END_DATE IS NULL																			\n");
		if(memAffiliation > 0){
			sql.append("	AND d.AFFILIATION_SEQ = ?																	\n");
		}
		sql.append("GROUP BY c.MEM_SEQ_NO																				\n");
		sql.append("ORDER BY b.MEM_SEQ_NO = ? DESC, c.TODAY_TOTAL DESC	LIMIT 5											\n");
		
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, citySeqNo);
			if(memAffiliation > 0){
				pstmt.setInt(++cnt, memAffiliation);
			}
			pstmt.setInt(++cnt, memSeqNo);
			rs = pstmt.executeQuery();
			////System.out.println(pstmt.toString());
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao cityMemberList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-07 ksy 처음 시작시 처음 스테이지로 위치 테이블 입력
	 */
	public int insertMemberPosition(int memSeqNo)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO						\n");
		sql.append("			TB_MEM_STAY		\n");
		sql.append("(								\n");
		sql.append("		MEM_SEQ_NO,				\n");
		sql.append("		ORDERLY,				\n");
		sql.append("		CITY_SEQ_NO,			\n");
		sql.append("		START_DATE				\n");
		sql.append(")								\n");
		sql.append("VALUES(							\n");
		sql.append("		?,						\n");
		sql.append("		1,						\n");
		sql.append("		1,						\n");
		sql.append("		NOW()					\n");
		sql.append(")								\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, memSeqNo);
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
			//////System.out.println("MemberDao insertMemberPosition ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-12 ksy 회원정보 보기 이름 부서 칼로리 건강수명 힘내요
	 */
	public MemberVO selectMemberInfoBasic(int memSeqNo, int myMemSeqNo)throws SQLException{
		int cnt = 0;
		MemberVO vo = new MemberVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																\n");
		sql.append("		MEM_SEQ_NO, DEPART_NAME, MEM_NAME, MEM_PIC,	AFFILIATION_NAME,																				\n");
		sql.append("		(SELECT WALK_UP FROM TB_MASTER WHERE MEM_SEQ_NO = ? AND CRT_DATE >= CURDATE()) AS ONN,										\n");
		sql.append("		(SELECT WALK_DOWN FROM TB_MASTER WHERE MEM_SEQ_NO = ? AND CRT_DATE >= CURDATE()) AS OFF,									\n");
		sql.append("		(SELECT COUNT(*) FROM TB_LIKE WHERE TAGET_GUBUN = 2 AND TAGET_SEQ_NO = ?) AS FCNT,											\n");
		sql.append("		(SELECT IFNULL(TODAY_TOTAL,0) FROM TB_MASTER WHERE MEM_SEQ_NO = ? AND SUBSTRING(CRT_DATE,1,10) = CURDATE() ) AS TODAY,			\n");//2016-01-11 KSY 수정
		sql.append("		(SELECT IFNULL(SUM(TODAY_TOTAL),0) FROM TB_MASTER WHERE MEM_SEQ_NO = ? AND WEEKOFYEAR(CRT_DATE) = WEEKOFYEAR(CURDATE()) GROUP BY MEM_SEQ_NO DESC) AS WEEK,			\n");//2016-01-11 KSY 수정
		sql.append("		(SELECT IFNULL(SUM(TODAY_TOTAL),0) FROM TB_MASTER WHERE MEM_SEQ_NO = ? AND SUBSTRING(CRT_DATE,1,7) = SUBSTRING(curdate(),1,7) GROUP BY a.MEM_SEQ_NO DESC) AS MON,			\n");//2016-01-11 KSY 수정
		sql.append("		(SELECT IFNULL(SUM(TODAY_TOTAL),0) FROM TB_MASTER WHERE MEM_SEQ_NO = ? AND SUBSTRING(CRT_DATE,1,4) = SUBSTRING(curdate(),1,4) GROUP BY MEM_SEQ_NO DESC ) AS YEAR_DATE,		\n"); // 2015-01-11 ksy 수정
		sql.append("		(SELECT COUNT(*) FROM TB_LIKE WHERE TAGET_GUBUN = 2 AND TAGET_SEQ_NO = ?) AS FCNT,											\n");
		sql.append("		(SELECT COUNT(*) FROM TB_FRIEND WHERE MEM_SEQ_NO = ? AND HE_SEQ_NO = ?)	AS FRCNT,											\n");
		sql.append("		(SELECT COUNT(*) FROM TB_LIKE WHERE TAGET_GUBUN = 2 AND TAGET_SEQ_NO = ? AND MEM_SEQ_NO = ?) AS LIKE_SELECT,				\n");
		sql.append("		(SELECT SUM(WALK_UP) FROM TB_MASTER WHERE MEM_SEQ_NO = ?) AS LIFE_UP,														\n");
		sql.append("		(SELECT SUM(WALK_DOWN) FROM TB_MASTER WHERE MEM_SEQ_NO = ?) AS LIFE_DOWN													\n");
		sql.append("FROM																																\n");
		sql.append("		TB_MEMBER a																													\n");
		sql.append("LEFT OUTER JOIN 																													\n");
		sql.append("		TB_MEMBER_AFFILIATION f																										\n");
		sql.append("ON																																	\n");
		sql.append("		a.MEM_SEQ_NO = f.MEMBER_SEQ																									\n");
		sql.append("LEFT OUTER JOIN																														\n");
		sql.append("		TB_DEPART b																													\n");
		sql.append("ON																																	\n");
		sql.append("		f.DEPART_SEQ = b.DEPART_SEQ_NO																								\n");
		sql.append("LEFT OUTER JOIN																														\n");
		sql.append("		TB_AFFILIATION c																											\n");
		sql.append("ON																																	\n");
		sql.append("		f.AFFILIATION_SEQ = c.SEQ_NO																								\n");
		sql.append("WHERE																																\n");
		sql.append("		MEM_SEQ_NO = ?	AND f.MAIN_YN <> 'N'																											\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, myMemSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, myMemSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemDepart(rs.getString("DEPART_NAME"));
				vo.setMemAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setUpCnt(rs.getInt("ONN"));
				vo.setDownCnt(rs.getInt("OFF"));
				vo.setFightCnt(rs.getInt("FCNT"));
				vo.setLifeUp(rs.getInt("LIFE_UP"));
				vo.setLifeDown(rs.getInt("LIFE_DOWN"));
				vo.setToday(rs.getInt("TODAY"));
				vo.setWeek(rs.getInt("WEEK"));
				vo.setMon(rs.getInt("MON"));
				vo.setYear(rs.getInt("YEAR_DATE"));
				vo.setFightCnt(rs.getInt("FCNT"));
				vo.setFrcnt(rs.getInt("FRCNT"));
				vo.setLikeSelect(rs.getInt("LIKE_SELECT"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao selectMemberInfoBasic ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return vo;
	}
	/*
	 * 2015-05-13 ksy cms 회원 리스트
	 * searchType : 1 = 성명, 2 = 사번
	 */
	public ArrayList<MemberVO> selectCmsMemberList(int pageno, int rowSize, String searchType, String keyword, int roles)throws SQLException{
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		int cnt = 0;
		int page = (pageno -1)*rowSize;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																					\n");
		sql.append("		MEM_SEQ_NO, MEM_NAME, MEM_ID, MEM_NUMBER, AFFILIATION_NAME, DEPART_NAME, MEM_RESULT, a.CRT_DATE			\n");
		sql.append("FROM																					\n");
		sql.append("		TB_MEMBER a  										\n");
		sql.append("LEFT JOIN													\n");
		sql.append("		TB_DEPART b											\n");
		sql.append("ON																					\n");
		sql.append("		a.MEM_DEPART = b.DEPART_SEQ_NO													\n");
		sql.append("LEFT JOIN																					\n");
		sql.append("		TB_AFFILIATION c														\n");
		sql.append("ON															\n");
		sql.append("		a.MEM_AFFILIATION = c.SEQ_NO													\n");
		sql.append("WHERE 1 = 1													\n");
		
		if(searchType.equals("1")){
			sql.append("	AND MEM_NAME LIKE CONCAT('%',?,'%') 											\n");
		}else if(searchType.equals("2")){
			sql.append("	AND MEM_NUMBER LIKE CONCAT('%',?,'%')											\n");
		}
		else if(searchType.equals("3")){
			sql.append("	AND AFFILIATION_NAME LIKE CONCAT('%',?,'%')											\n");
		}
		else if(searchType.equals("4")){
			sql.append("	AND DEPART_NAME LIKE CONCAT('%',?,'%')											\n");
		}
		sql.append("	AND MEM_RESULT <> 'Y'																\n");
		if(roles > 0){
			sql.append("	AND a.MEM_AFFILIATION = ?														\n");
		}
		sql.append("ORDER BY																				\n");
		sql.append("		a.CRT_DATE DESC																	\n");
		sql.append("LIMIT	?,?																				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(searchType != null && searchType != ""){
				pstmt.setString(++cnt, keyword);
			}
			if(roles > 0){
				pstmt.setInt(++cnt, roles);
			}
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemId(rs.getString("MEM_ID"));
				vo.setMemNumber(rs.getString("MEM_NUMBER"));
				vo.setMemAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setMemDepart(rs.getString("DEPART_NAME"));
				vo.setMemResult(rs.getString("MEM_RESULT"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao selectCmsMemberList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-13 ksy 총 회원수
	 */
	public int memberCnt(String searchType, String keyword, int roles)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		COUNT(*) AS CNT				\n");
		sql.append("FROM																					\n");
		sql.append("		TB_MEMBER a  										\n");
		sql.append("LEFT JOIN													\n");
		sql.append("		TB_DEPART b											\n");
		sql.append("ON																					\n");
		sql.append("		a.MEM_DEPART = b.DEPART_SEQ_NO													\n");
		sql.append("LEFT JOIN																					\n");
		sql.append("		TB_AFFILIATION c														\n");
		sql.append("ON															\n");
		sql.append("		a.MEM_AFFILIATION = c.SEQ_NO													\n");
		sql.append("WHERE 1 = 1													\n");
		if(searchType.equals("1")){
			sql.append("	AND MEM_NAME LIKE CONCAT('%',?,'%') 											\n");
		}else if(searchType.equals("2")){
			sql.append("	AND MEM_NUMBER LIKE CONCAT('%',?,'%')											\n");
		}
		else if(searchType.equals("3")){
			sql.append("	AND AFFILIATION_NAME LIKE CONCAT('%',?,'%')											\n");
		}
		else if(searchType.equals("4")){
			sql.append("	AND DEPART_NAME LIKE CONCAT('%',?,'%')											\n");
		}
		sql.append("	AND MEM_RESULT <> 'Y'																\n");
		if(roles > 0){
			sql.append("	AND a.MEM_AFFILIATION = ?														\n");
		}
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(searchType != null && searchType != ""){
				pstmt.setString(++cnt, keyword);
			}
			if(roles > 0){
				pstmt.setInt(++cnt, roles);
			}
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberCnt ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-13 ksy 회원 승인
	 */
	
	public int memberApprove(int memSeqNo)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE								\n");
		sql.append("		TB_MEMBER					\n");
		sql.append("SET									\n");
		sql.append("		MEM_RESULT ='N'				\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_SEQ_NO = ?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberApprove ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-13 ksy 회원 삭제
	 */
	public int delectMember(int memSeqNo)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM							\n");
		sql.append("		TB_MEMBER					\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_SEQ_NO = ?				\n");
		deleteMayor(memSeqNo); // 해당 회원의 시장 정보 삭제
		deleteMemberMaster(memSeqNo);	//해당 회원의 계단 걸음수 삭제
		deleteMemberStage(memSeqNo);		//해당 회원의 스테이지 기록 삭제
		deleteMmemberAffiliation(memSeqNo);		// 회원 부서 삭제
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao delectMember ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
	
	/*
	 * 2017-07-28 회원 삭제 시 해당 회원 걸음 수 삭제
	 */
	public int deleteMemberMaster(int memSeqNo) throws SQLException {
		int result=0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM 							\n");
		sql.append("		TB_MASTER				\n");
		sql.append("WHERE   MEM_SEQ_NO = ? 		\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		pstmt.setInt(1,memSeqNo);
		
		result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("deleteMemberMaster ERROR : " +e); 

		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2017-07-28 회원 삭제 시 해당 스테이지 삭제
	 */
	public int deleteMemberStage(int memSeqNo) throws SQLException {
		int result=0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM 							\n");
		sql.append("		TB_MEM_STAY				\n");
		sql.append("WHERE   MEM_SEQ_NO = ? 		\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		pstmt.setInt(1,memSeqNo);
		
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
	 * 2015-05-13 ksy 회원정보 운동기록 상세화면
	 * rowCate :  1= 주, 2= 월, 3=년
	 */
	public ArrayList<MemberVO> selectHealthList(int rowCate, int memSeqNo)throws SQLException{
		int row = rowCate -1;
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		StringBuffer sql = new StringBuffer();
		if(rowCate == 1){
			selectHealthWeek(memSeqNo);
		}else{
			selectHealthMonYear(rowCate, memSeqNo);
		}
		
		try{
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setToday(rs.getInt("TOTAL"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setUpCnt(rs.getInt("WALK_UP"));
				vo.setDownCnt(rs.getInt("WALK_DOWN"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao selectHealthList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-27 ks 운동기록 주간 
	 */
	public String selectHealthWeek(int memSeqNo)throws SQLException{
		StringBuffer sql =new StringBuffer();
		sql.append("SELECT													\n");
		sql.append("		TODAY_TOTAL AS TOTAL, CRT_DATE AS CRT_DATE		\n");
		sql.append("	,	WALK_UP AS WALK_UP, WALK_DOWN AS WALK_DOWN		\n");
		sql.append("FROM													\n");
		sql.append("		TB_MASTER										\n");
		sql.append("WHERE													\n");
		sql.append("		MEM_SEQ_NO = ?									\n");
		sql.append("	AND CRT_DATE >= (CURDATE() - INTERVAL 7 DAY)		\n");
		/*sql.append("ORDER BY												\n");
		sql.append("		CRT_DATE DESC									\n");
		sql.append("LIMIT 7													\n");*/
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao selectHealthWeek ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return sql.toString();
	}
	/*
	 * 2015-05-27 ksy 회원정보 운동기록 상세 월, 년간
	 */
	public String selectHealthMonYear(int rowCate, int memSeqNo)throws SQLException{
		int row = rowCate -2;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																		\n");
		sql.append("		SUM(TODAY_TOTAL) AS TOTAL,"+SELECTQUERY[row]+						"\n");
		sql.append("	,	SUM(WALK_UP) AS WALK_UP, SUM(WALK_DOWN) AS WALK_DOWN				\n");
		sql.append("FROM																		\n");
		sql.append("		TB_MASTER															\n");
		sql.append("WHERE																		\n");
		sql.append("		MEM_SEQ_NO = ?														\n");
		sql.append(WHEREQUERY[row]+																"\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao selectHealthMonYear ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return sql.toString();
	}
	/*
	 * 2015-05-28 ksy 즐겨찾기 친구 등록/ 취소
	 */
	public int bestFriendUpdate(int memSeqNo, int matMemSeq, int gubun)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE									\n");
		sql.append("		TB_FRIEND						\n");
		sql.append("SET										\n");
		if(gubun ==1){
			sql.append("		FRIEND_LEVEL = 2			\n");
		}else if(gubun ==2){
			sql.append("		FRIEND_LEVEL = 1			\n");
		}
		
		sql.append("	,	CHG_DATE = NOW()				\n");
		sql.append("WHERE									\n");
		sql.append("		MEM_SEQ_NO = ?					\n");
		sql.append("	AND	HE_SEQ_NO = ?					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, matMemSeq);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao bestFriendUpdate ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
	/*
	 * 2015-05-30 ksy 회원정보 수정 후 업데이트 된 사진 전송
	 */
	public String selectMemPic(int memSeqNo)throws SQLException{
		String memPic="";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT											\n");
		sql.append("		MEM_PIC									\n");
		sql.append("FROM											\n");
		sql.append("		TB_MEMBER								\n");
		sql.append("WHERE											\n");
		sql.append("		MEM_SEQ_NO = ?							\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				memPic = rs.getString("MEM_PIC");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao selectMemPic ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return memPic;
	}
	/*
	 * 2015-06-01 ksy 회원삭제시 관련 시장 정보 삭제
	 */
	public String deleteMayor(int memSeqNo)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM								\n");
		sql.append("		TB_MAYOR					\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_SEQ_NO = ?				\n");
	
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao deleteMayor ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return sql.toString();
	}
	
	
	/*
	 * 2016-07-25 해당인원의 소속 정보 검색
	 */
	
	public int selectMemberAffiliation(int memberSeq)throws SQLException{
		int result =0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT											\n");
		sql.append("		MEM_AFFILIATION							\n");
		sql.append("FROM											\n");
		sql.append("		TB_MEMBER								\n");
		sql.append("WHERE											\n");
		sql.append("		MEM_SEQ_NO = ?							\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, memberSeq);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("MEM_AFFILIATION");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao selectMemberAffiliation ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-07-25 해당인원의 부서 정보 검색
	 */
	
	public int selectMemberDepartPassive(int memberSeq)throws SQLException{
		int result =0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT											\n");
		sql.append("		MEM_DEPART						\n");
		sql.append("FROM											\n");
		sql.append("		TB_MEMBER								\n");
		sql.append("WHERE											\n");
		sql.append("		MEM_SEQ_NO = ?							\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, memberSeq);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("MEM_DEPART");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao selectMemberAffiliation ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-08-24 회원가입 시 master에 로그 추가
	 */
	public int insertMasterRegist(int memSeqNo, int memAffiliation, int memDepart)throws SQLException{
		
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("INSERT INTO 																										\n");
		sql.append("			TB_MASTER																								\n");
		sql.append("(																													\n");
		sql.append("			MEM_SEQ_NO, MEM_AFFILIATION, MEM_DEPART, TOTAL_WALK, WALK_UP, WALK_DOWN, TODAY_TOTAL, CRT_DATE										\n");
		sql.append(")																													\n");
		sql.append("VALUES( ?, ?, ?, 0, 0, 0, 0, NOW() )																						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memAffiliation);
			pstmt.setInt(++cnt, memDepart);
			
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
			//////System.out.println("MemberDao insertMasterRegist ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 모든 회원 리스트 검색
	 */
	
	public ArrayList<MemberVO> masterInsertMember(int memSeqNo)throws SQLException{
		StringBuffer sql = new StringBuffer();
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		
		sql.append("SELECT						\n");
		sql.append("		MEM_PIC				\n");
		sql.append("FROM						\n");
		sql.append("		TB_MEMBER			\n");
		sql.append("WHERE						\n");
		sql.append("		MEM_SEQ_NO = ?		\n");
	
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemPic(rs.getString("MEM_PIC"));
				
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao testList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return list;
	}
	
	/*
	 * 2016-10-04 db 마이그레이션을 위한 회원 id,pw 가져오기
	 */
	public ArrayList<MemberVO> selectMemberInfo()throws SQLException{
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																									\n");
		sql.append("		MEM_SEQ_NO, MEM_NAME, MEM_NUMBER, MEM_AFFILIATION, MEM_DEPART																\n");
		sql.append("FROM																									\n");
		sql.append("		TB_MEMBER																						\n");		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemNumber(rs.getString("MEM_NUMBER"));
				vo.setAffiliationSeq(rs.getInt("MEM_AFFILIATION"));
				vo.setMemDepart(rs.getString("MEM_DEPART"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao selectMemberInfo ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	
	/*
	 * 2016-10-04 db 마이그레이션을 위해 id,pw를 회원 이름, 전화번호로 삽입
	 */
	public int memberInfoUpdate(int memSeq, String memName, String memPw)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MEMBER				\n");
		sql.append("SET								\n");
		sql.append("		MEM_ID = ?				\n");
		sql.append("	,	MEM_PW = ?				\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setString(++cnt, memName);
			pstmt.setString(++cnt, memPw);
			pstmt.setInt(++cnt, memSeq);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberUpdate ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	public int memberInfoUpdate(String memId, String memName, int affiliationSeq, int departSeq, int memSeq)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MEMBER				\n");
		sql.append("SET								\n");
		sql.append("		MEM_ID = ?				\n");
		sql.append("	,	MEM_NAME = ?				\n");
		sql.append("	,	MEM_AFFILIATION = ?				\n");
		sql.append("	,	MEM_DEPART = ?				\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ? 			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setString(++cnt, memId);
			pstmt.setString(++cnt, memName);
			pstmt.setInt(++cnt, affiliationSeq);
			pstmt.setInt(++cnt, departSeq);
			pstmt.setInt(++cnt, memSeq);
			
			//System.out.println(pstmt);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	/*
	 * 2016-10-04 db 마이그레이션을 위해 id,pw를 회원 이름, 전화번호로 삽입
	 */
	public int memberInfoUpdateMaster(int memSeq, int affiliationSeq, int departSeq)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MASTER				\n");
		sql.append("SET								\n");
		sql.append("		MEM_AFFILIATION = ?			\n");
		sql.append("	,	MEM_DEPART = ?			\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, affiliationSeq);
			pstmt.setInt(++cnt, departSeq);
			pstmt.setInt(++cnt, memSeq);
			
			//System.out.println(pstmt);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberUpdate ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-10-04 db 마이그레이션 소속정보 입력
	 */
	public int memberInsertGroup(int groupSeq, String departSeq, int memSeq)throws SQLException{
		
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("INSERT INTO 																										\n");
		sql.append("			TB_MEMBER_AFFILIATION																					\n");
		sql.append("(																													\n");
		sql.append("			AFFILIATION_SEQ, DEPART_SEQ, MEMBER_SEQ, MAIN_YN, DEL_YN, CRT_DATE										\n");
		sql.append(")																													\n");
		sql.append("VALUES 	(	?, ?, ?, 'Y', 'N', NOW() )																	\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(++cnt, groupSeq);
			pstmt.setString(++cnt, departSeq);
			pstmt.setInt(++cnt, memSeq);
			
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
			//////System.out.println("MemberDao memberInsert ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-10-04 db 마이그레이션을 위한 
	 */
	public ArrayList<MemberVO> selectMemberInfoMaster()throws SQLException{
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																			\n");
		sql.append("		MEM_SEQ_NO																\n");
		sql.append("FROM																			\n");
		sql.append("		TB_MEMBER																\n");
		sql.append("WHERE																			\n");
		sql.append("		MEM_AFFILIATION = 135													\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao selectMemberInfo ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	
	
	/*
	 * 2016 - 09 - 01 APP 회원 부서 등록
	 */
	public int memberDepartInsert(int affiliationSeq, int departSeq, int memberSeq, int affilCnt)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("INSERT INTO 																														\n");
		sql.append("			TB_MEMBER_AFFILIATION																									\n");
		sql.append("(																																	\n");
		sql.append("			AFFILIATION_SEQ, DEPART_SEQ, MEMBER_SEQ, MAIN_YN, DEL_YN, CRT_DATE														\n");
		sql.append(")																																	\n");
		if(affilCnt == 0){
			sql.append("VALUES(		?, ?, ?, 'Y', 'N', NOW()		)																						\n");
		}else{
			sql.append("VALUES(		?, ?, ?, 'N', 'N', NOW()		)																						\n");
		}
		
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(++cnt, affiliationSeq);
			pstmt.setInt(++cnt, departSeq);
			pstmt.setInt(++cnt, memberSeq);
			
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
			
			//////System.out.println("MemberDao memberDepartInsert : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberDepartInsert ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	
	/*
	 * 2016-10-04 회원 로그인
	 */
	public int memberLoginOk(String memberEmail, String memberPw)throws SQLException{
		StringBuffer sql = new StringBuffer();
		int result = 0;
		int cnt =0;
		sql.append("SELECT								\n");
		sql.append("		MEM_SEQ_NO					\n");
		sql.append("FROM								\n");
		sql.append("		TB_MEMBER					\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_ID = ?			\n");
		sql.append("AND		MEM_PW = ?			\n");

		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, memberEmail);
			pstmt.setString(++cnt, memberPw);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("MEM_SEQ_NO");

			}
		}catch(Exception e){
			
			//////System.out.println("�뿉�윭 諛쒖깮 �떆媛� : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberLogin ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-09-08 회원 탈퇴 시 부서 삭제
	 */
	public int deleteMmemberAffiliation(int memSeqNo)throws SQLException{
		//////System.out.println("asdasd");
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM							\n");
		sql.append("		TB_MEMBER_AFFILIATION	\n");
		sql.append("WHERE							\n");
		sql.append("		MEMBER_SEQ = ?			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MEMBER DAO deleteMmemberAffiliation ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2015-05-28 ksy 즐겨찾기 친구 등록/ 취소
	 */
	public int updateMemberPw(int memSeqNo, String memPw)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE									\n");
		sql.append("		TB_MEMBER						\n");
		sql.append("SET										\n");
		sql.append("		MEM_PW = ?				\n");
		sql.append("WHERE									\n");
		sql.append("		MEM_SEQ_NO = ?					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, memPw);
			pstmt.setInt(2, memSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao updateMemberPw ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
	
	/*
	 * 2016-10-05 cms 비번 찾기 회원정보
	 */
	public MemberVO SelectCmsMemInfo(int memSeqNo)throws SQLException{
		StringBuffer sql = new StringBuffer();
		MemberVO vo = new MemberVO();
		sql.append("SELECT								\n");
		sql.append("		MEM_SEQ_NO, MEM_ID			\n");
		sql.append("FROM								\n");
		sql.append("		TB_MEMBER					\n");
		sql.append("WHERE 								\n");
		sql.append("		MEM_SEQ_NO =?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO")); 
				vo.setMemId(rs.getString("MEM_ID"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberFirstLogin ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return vo;
	}
	
	public MemberVO selectMemInfo(int memSeqNo)throws SQLException{
		StringBuffer sql = new StringBuffer();
		MemberVO vo = new MemberVO();
		sql.append("SELECT								\n");
		sql.append("		MEM_SEQ_NO, MEM_ID, MEM_AFFILIATION, MEM_DEPART, MEM_NAME, MEM_NUMBER			\n");
		sql.append("FROM								\n");
		sql.append("		TB_MEMBER					\n");
		sql.append("WHERE 								\n");
		sql.append("		MEM_SEQ_NO =?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO")); 
				vo.setMemId(rs.getString("MEM_ID"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemNumber(rs.getString("MEM_NUMBER"));
				vo.setAffiliationSeq(rs.getInt("MEM_AFFILIATION"));
				vo.setDepartSeq(rs.getInt("MEM_DEPART"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberFirstLogin ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return vo;
	}
	
	/*
	 * 2016-09-02 회원 정보 수정 (비밀번호 변경 안함) 앵귤러 버젼
	 */
	public int angularMemberUpdate(int memSeqNo, String memberName, String memberPhone, String listImg)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MEMBER				\n");
		sql.append("SET								\n");
		sql.append("		MEM_NAME = ? ,			\n");
		sql.append("		MEM_NUMBER = ?			\n");
		if(listImg!=null&&!listImg.equals(""))sql.append("		,MEM_PIC = ?			\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setString(++cnt, memberName);
			pstmt.setString(++cnt, memberPhone);
			if(listImg!=null&&!listImg.equals(""))pstmt.setString(++cnt, listImg);
			pstmt.setInt(++cnt, memSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("�뿉�윭 諛쒖깮 �떆媛� : " +new Timestamp(startTime));
			//////System.out.println("MemberDao AngularMemberUpdate ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 *   APP 2016-08-26 ljh 회원정보 수정에서 비밀번호 맞는지 확인 앵귤러
	 */
	public int angularSelectMemberPwUd(int memSeqNo, String memberPw){
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		COUNT(1) AS CNT				\n");
		sql.append("FROM								\n");
		sql.append("		TB_MEMBER		\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_SEQ_NO = ?				\n");
		sql.append("AND									\n");
		sql.append("		MEM_PW = ?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setString(++cnt, memberPw);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeAll(rs,pstmt);
		}
		return result;
	}
	
	/*
	 * 2016-09-02 회원 정보 수정 (비밀번호도 같이 변경)
	 */
	public int angularMemberUpdate(int memSeqNo, String memberName, String memberPhone, String listImg, String memberPw)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MEMBER				\n");
		sql.append("SET								\n");
		sql.append("		MEM_NAME = ? ,			\n");
		sql.append("		MEM_PHONE = ?,			\n");
		sql.append("		MEM_PW = ?			\n");
		if(listImg!=null&&!listImg.equals(""))sql.append("		,MEM_PIC = ?			\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setString(++cnt, memberName);
			pstmt.setString(++cnt, memberPhone);
			pstmt.setString(++cnt, memberPw);
			if(listImg!=null&&!listImg.equals(""))pstmt.setString(++cnt, listImg);
			pstmt.setInt(++cnt, memSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("�뿉�윭 諛쒖깮 �떆媛� : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberUpdate ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-10-05 부서 등록 시 master 테이블 소속정보 변경
	 */
	public int updateMasterInfo(int memberSeqNo, int affiliationSeqNo, int departSeqNo)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_MASTER				\n");
		sql.append("SET								\n");
		sql.append("		MEM_AFFILIATION = ? 	\n");
		sql.append("	,	MEM_DEPART = ?			\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
		
			pstmt.setInt(++cnt, affiliationSeqNo);
			pstmt.setInt(++cnt, departSeqNo);
			pstmt.setInt(++cnt, memberSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao updateMasterInfo ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return result;
	}
	
	/*
	 * 2016-09-21 소속 변경시 소속 태이블 main 변경
	 */
	public int memberMainaffilUpdate(int memSeqNo, int affiliationSeq, int departSeq)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE										\n");
		sql.append("		TB_MEMBER_AFFILIATION				\n");
		sql.append("SET		AFFILIATION_SEQ = ?					\n");
		sql.append("	,	DEPART_SEQ = ?					\n");
		sql.append("WHERE										\n");
		sql.append("		MEMBER_SEQ = ?						\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, affiliationSeq);
			pstmt.setInt(++cnt, departSeq);
			pstmt.setInt(++cnt, memSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("�발생 시간� : " +new Timestamp(startTime));
			//////System.out.println("MemberDao memberMainaffilUpdate ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-10-10 경남은행 회원이 master 테이블에 데이터가 있는지 검사
	 */
	public int dbMigrateSelectCnt(int memSeqNo)throws SQLException{
		int result =0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT											\n");
		sql.append("		COUNT(*) AS CNT							\n");
		sql.append("FROM											\n");
		sql.append("		TB_MASTER								\n");
		sql.append("WHERE											\n");
		sql.append("		MEM_SEQ_NO = ?							\n");
			
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, memSeqNo);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao searchMemberList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2016-08-24 회원가입 시 master에 로그 추가
	 */
	public int insertMasterInfo(int memSeqNo, int memAffiliation, int memDepart)throws SQLException{
		
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("INSERT INTO 																										\n");
		sql.append("			TB_MASTER																								\n");
		sql.append("(																													\n");
		sql.append("			MEM_SEQ_NO, MEM_AFFILIATION, MEM_DEPART, TOTAL_WALK, WALK_UP, WALK_DOWN, TODAY_TOTAL, CRT_DATE										\n");
		sql.append(")																													\n");
		sql.append("VALUES( ?, ?, ?, 0, 0, 0, 0, '2016-09-01 09:00:00' )																						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memAffiliation);
			pstmt.setInt(++cnt, memDepart);
			
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
			//////System.out.println("MemberDao insertMasterRegist ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2015-11-03 메인화면 회원 정보 (시퀀스,이름,사진,삭제여부,부서시퀀스,부서명,소속명)
	 */
	public MemberVO mainMemberInfo(int memSeqNo)throws SQLException{
		MemberVO vo = new MemberVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																										\n");
		sql.append("		MEM_SEQ_NO, MEM_NAME, MEM_PIC, MEM_RESULT, DEPART_SEQ_NO, DEPART_NAME, AFFILIATION_NAME				\n");
		sql.append("FROM																										\n");
		sql.append("		TB_MEMBER a																							\n");
		sql.append("LEFT OUTER JOIN																								\n");
		sql.append("		TB_MEMBER_AFFILIATION b																				\n");
		sql.append("ON 	a.MEM_SEQ_NO = b.MEMBER_SEQ																				\n");
		sql.append("LEFT OUTER JOIN																								\n");
		sql.append("		TB_DEPART c																							\n");
		sql.append("ON	b.DEPART_SEQ = c.DEPART_SEQ_NO																			\n");
		sql.append("LEFT OUTER JOIN																								\n");
		sql.append("		TB_AFFILIATION d																					\n");
		sql.append("ON	b.AFFILIATION_SEQ = d.SEQ_NO																			\n");
		sql.append("WHERE																										\n");
		sql.append("		MEMBER_SEQ = ?																						\n");

		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setMemResult(rs.getString("MEM_RESULT"));
				vo.setDepartSeq(rs.getInt("DEPART_SEQ_NO"));
				vo.setMemDepart(rs.getString("DEPART_NAME"));
				vo.setMemAffiliationName(rs.getString("AFFILIATION_NAME"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao mainMemberInfo ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return vo;
	}
	
	/*
	 * 2015-11-03 메인화면 회원 도시 정보 (도시 시퀀스,낮 사진, 밤 사진, 도시 이름, 나라 이름, 나라 사진, 도시 순서, 시작시간, 최초 시작시간)
	 */
	public MemberVO mainMemberCityInfo(int memSeqNo)throws SQLException{
		MemberVO vo = new MemberVO();
		int cnt=0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																		\n");
		sql.append("		a.CITY_SEQ_NO, a.ORDERLY, DAY_PIC, NIGHT_PIC, START_DATE, CITY_NAME, COUNTRY_NAME, COUNTRY_PIC										\n");
		sql.append("		,(SELECT START_DATE FROM TB_MEM_STAY WHERE MEM_SEQ_NO = 433 AND ORDERLY = 1 ORDER BY START_DATE DESC LIMIT 1) AS FIRST_DATE			\n");
		sql.append("FROM																																		\n");
		sql.append("		TB_MEM_STAY a																														\n");
		sql.append("LEFT OUTER JOIN																																\n");
		sql.append("		TB_CITY b																															\n");
		sql.append("ON a.CITY_SEQ_NO = b.CITY_SEQ_NO																											\n");
		sql.append("LEFT OUTER JOIN																																\n");
		sql.append("		TB_COUNTRY c																														\n");
		sql.append("ON b.COUNTRY_SEQ_NO = c.COUNTRY_SEQ_NO																										\n");
		sql.append("WHERE																																		\n");
		sql.append("		a.END_DATE IS NULL																													\n");
		sql.append("AND																																			\n");
		sql.append("		a.MEM_SEQ_NO = ?																													\n");

		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, memSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				CityVO city = vo.getCityVo();
				city.setCitySeqNo(rs.getInt("CITY_SEQ_NO"));
				city.setOrderLy(rs.getInt("ORDERLY"));
				city.setCityNPic(rs.getString("DAY_PIC"));
				city.setCityNPic(rs.getString("NIGHT_PIC"));
				city.setStartDate(rs.getString("START_DATE"));
				city.setFstartDate(rs.getString("FIRST_DATE"));
				city.setCityName(rs.getString("CITY_NAME"));
				city.setCountryName(rs.getString("COUNTRY_NAME"));
				city.setCountryPic(rs.getString("COUNTRY_PIC"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao mainMemberInfo ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return vo;
	}
	
	/*
	 * 2015-11-03 메인화면 회원 계단 정보 (오늘 오름수, 오늘 내림 수, 랭크, 총 계단오름수, 총 계단내림수, 세계일주 수)
	 */
	public MemberVO mainMemberStairInfo(int memSeqNo, int memAffiliation)throws SQLException{
		MemberVO vo = new MemberVO();
		int cnt=0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																											\n");
		sql.append("		a.MEM_SEQ_NO, WALK_UP, WALK_DOWN, TODAY_TOTAL,IFNULL(b.CNT,0) AS CNT_WORLD								\n");
		sql.append("		, (SELECT COUNT(*)+1 FROM (SELECT a.MEM_SEQ_NO, SUM(TODAY_TOTAL) AS TODAY_TOTAL FROM TB_MASTER a LEFT OUTER JOIN TB_MEMBER b ON a.MEM_SEQ_NO = b.MEM_SEQ_NO LEFT OUTER JOIN TB_MEMBER_AFFILIATION c ON b.MEM_SEQ_NO = c.MEMBER_SEQ WHERE b.MEM_RESULT <> 'Y' AND c.MAIN_YN <> 'N' AND a.CRT_DATE >= CURDATE() 						\n");
		if(memAffiliation > 0){
			sql.append("	AND c.AFFILIATION_SEQ = ?																				\n");
		}
		sql.append("		GROUP BY a.MEM_SEQ_NO DESC)n WHERE n.TODAY_TOTAL > IFNULL((a.TODAY_TOTAL),0)) AS RANK					\n");
		sql.append("		,(SELECT SUM(WALK_UP) FROM TB_MASTER WHERE MEM_SEQ_NO = a.MEM_SEQ_NO) AS LIFE_UP						\n");
		sql.append("		,(SELECT SUM(WALK_DOWN) FROM TB_MASTER WHERE MEM_SEQ_NO =a.MEM_SEQ_NO) AS LIFE_DOWN						\n");
		sql.append("FROM																											\n");
		sql.append("		TB_MASTER a																								\n");
		sql.append("LEFT OUTER JOIN 																								\n");
		sql.append("		(SELECT MEM_SEQ_NO, COUNT(*) AS CNT FROM TB_WORLD WHERE MEM_SEQ_NO = ?) b								\n");
		sql.append("ON a.MEM_SEQ_NO = b.MEM_SEQ_NO																					\n");
		sql.append("WHERE																											\n");
		sql.append("		a.MEM_SEQ_NO = ?																						\n");
		sql.append("AND		a.CRT_DATE >= CURDATE()																					\n");

		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(memAffiliation > 0){
				pstmt.setInt(++cnt, memAffiliation);
			}
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setUpCnt(rs.getInt("WALK_UP"));
				vo.setDownCnt(rs.getInt("WALK_DOWN"));
				vo.setToday(rs.getInt("TODAY_TOTAL"));
				vo.setWorldCnt(rs.getInt("CNT_WORLD"));
				vo.setRank(rs.getInt("RANK"));
				vo.setLifeUp(rs.getInt("LIFE_UP"));
				vo.setLifeDown(rs.getInt("LIFE_DOWN"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("MemberDao mainMemberStairInfo ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
		}
		
		return vo;
	}
	
}
