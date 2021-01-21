package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import util.ConnectionUtil;
import vo.BeaconVO;
import vo.CityVO;
import vo.MayorVO;

import com.mysql.jdbc.Statement;
import vo.NoticeVO;

public class BeaconDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public BeaconDao() throws SQLException{
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
	 * 2015-05-06 ksy 비콘 정보를 insert, update 파악하기 위한 값 가져오기
	 
	public BeaconVO firstBeacon(int memSeqNo)throws SQLException{
		BeaconVO vo = new BeaconVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT										\n");
		sql.append("		BELOG_SEQ_NO,"
						+ "	MEM_SEQ_NO,"
						+ " START_BEACON,"
						+ " END_BEACON,"
						+ " a.CRT_DATE,"
						+ " BEACON_SEQ_NO,"
						+ " BEACON_NAME,"
						+ " BUIDING_NAME,"
						+ " STAIRS_POSITION,"
						+ " STAIRS_NAME							\n");
		sql.append("FROM										\n");
		sql.append("		TB_BEACON_LOG a,"
						+ "	TB_BEACON b							\n");
		sql.append("WHERE										\n");
		sql.append("		MEM_SEQ_NO = ?"
					+ " AND a.START_BEACON = b.BEACON_NAME		\n");
		sql.append("ORDER BY CRT_DATE DESC						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setBelogSeqNo(rs.getInt("BELOG_SEQ_NO"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setEndBeacon(rs.getString("END_BEACON"));
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setStartBeacon(rs.getString("START_BEACON"));
				vo.setBeaconName(rs.getString("BEACON_NAME"));
				vo.setBeaconSeqNo(rs.getInt("BEACON_SEQ_NO"));
				vo.setBuidingName(rs.getString("BUIDING_NAME"));
				vo.setStairsName(rs.getString("STAIRS_NAME"));
				vo.setStairsPosition(rs.getInt("STAIRS_POSITION"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao firstBeacon ERROR : " +e);
			if(conn!=null) conn.close();
			if(rs!=null) rs.close();
			if(pstmt!=null) rs.close();
		}
		if(conn!=null) conn.close();
		if(rs!=null) rs.close();
		if(pstmt!=null) rs.close();
		return vo;
	}*/
	/*
	 * 2015-05-29 ksy 마스터 테이블 입력전 오늘 날짜 값 있는지 파악
	 */
	public int selectMaster(int memSeqNo)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		MASTER_SEQ_NO			\n");
		sql.append("FROM 							\n");
		sql.append("		TB_MASTER				\n");
		sql.append("WHERE							\n");
		sql.append("		MEM_SEQ_NO = ?			\n");
		sql.append("	AND CRT_DATE >=CURDATE()	\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("MASTER_SEQ_NO");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao selectMaster ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
	/*
	 * 2015-05-06 ksy 비콘정보 입력(비콘로그)
	 */
	public int insertBeacon(int major, int minor, int memSeqNo) throws SQLException{
		int result=0;
		int cnt =0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO																	\n");
		sql.append("			TB_BEACON_LOG													\n");
		sql.append("(																			\n");
		sql.append("			MEM_SEQ_NO, START_BEACON, CRT_DATE, POSITION_BEACON)			\n");
		sql.append("VALUES(																		\n");
		sql.append("			?,	?, NOW(), ?)												\n");
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, minor);
			pstmt.setInt(3, major);
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
			//////System.out.println("BeaconDao insertBeacon ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-06 ksy 앱에서 받은 비콘 맥어드레스 값으로 건물,위치 파악
	 */
	public BeaconVO selectBeacon(int memSeqNo)throws SQLException{
		BeaconVO vo = new BeaconVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																	\n");
		sql.append("		POSITION_BEACON, START_BEACON, CRT_DATE, BELOG_SEQ_NO			\n");
		sql.append("FROM																	\n");
		sql.append("		TB_BEACON_LOG													\n");
		sql.append("WHERE																	\n");
		sql.append("		MEM_SEQ_NO = ? AND END_BEACON IS NULL 							\n");
		sql.append("ORDER BY CRT_DATE DESC 													\n");
		sql.append("LIMIT 1																	\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setStairsPosition(rs.getInt("POSITION_BEACON"));
				vo.setStartBeacon(rs.getInt("START_BEACON"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setBelogSeqNo(rs.getInt("BELOG_SEQ_NO"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao selectBeacon ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	
	/*
	 * 2015-05-06 ksy 끝 비콘 입력
	 * beaconAddress : 비콘 mac_address
	 * beLogSeqNo : 비콘로그 seqno
	 * a : 오름, 내림 체크 1: 올름, 9999 : 내림
	 */
	public int updateEndBeacon(int minor, int beLogSeqNo, int a) throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_BEACON_LOG			\n");
		sql.append("SET								\n");
		sql.append("		END_BEACON = ?,"
						+ "	RESULT_BEACON = ?		\n");
		sql.append("WHERE							\n");
		sql.append("		BELOG_SEQ_NO = ?		\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, minor);
			pstmt.setInt(2, a);
			pstmt.setInt(3, beLogSeqNo);
			result= pstmt.executeUpdate();
			
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao updateEndBeacon ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-06 ksy 마지막 입력된 비콘로그 삭제(끝비콘이 null인것)
	 */
	public int deleteBeacon(int beLogSeqNO)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE	FROM						\n");
		sql.append("				TB_BEACON_LOG		\n");
		sql.append("WHERE								\n");
		sql.append("				BELOG_SEQ_NO = ?	\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, beLogSeqNO);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao deleteBeacon ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-08 ksy 진행중기부리스트에 입력할 걸음수 파악
	 */
	public int selectGiveWork(String startDate, String endDate) throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		COUNT(*) AS CNT			\n");
		sql.append("FROM							\n");
		sql.append("		TB_BEACON_LOG			\n");
		sql.append("WHERE							\n");
		sql.append("		CRT_DATE > ?			\n");
		sql.append("	AND	CRT_DATE < ?			\n");
	
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao selectGiveWork ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	/*
	 * 2015-05-18 ksy 비콘 입력 테스트
	 */
	public int beaconTestInsert(String beaconAddr, String uuid, int major, int minor, int rssi, int txpower, String dvcToken, String beaconName)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO																			\n");
		sql.append("			TB_TEST																	\n");
		sql.append("(																					\n");
		sql.append("	BEACON_ADDR, UUID, MAJOR, MINOR, RSSI, TXPOWER,CRT_DATE,DVC_TOKEN, BEACON_NAME	\n");
		sql.append(")																					\n");
		sql.append("VALUES(																				\n");
		sql.append("	?,?,?,?,?,?,NOW(),?,?															\n");
		sql.append(")																					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, beaconAddr);
			pstmt.setString(2, uuid);
			pstmt.setInt(3, major);
			pstmt.setInt(4, minor);
			pstmt.setInt(5, rssi);
			pstmt.setInt(6, txpower);
			pstmt.setString(7, dvcToken);
			pstmt.setString(8, beaconName);
			
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
			//////System.out.println("BeaconDao beaconTestInsert ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-29 ksy 마스터 테이블 업데이트
	 */
	public int updateMaster(int masterSeq, int CHECK)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE									\n");
		sql.append("		TB_MASTER						\n");
		sql.append("SET										\n");
		if(CHECK >0){
			sql.append("	WALK_DOWN = WALK_DOWN+1			\n");
		}else{
			sql.append("	WALK_UP = WALK_UP+1				\n");
		}
		sql.append("		,TOTAL_WALK = TOTAL_WALK+1		\n");
		sql.append("		,TODAY_TOTAL = TODAY_TOTAL+1	\n");
		sql.append("WHERE									\n");
		sql.append("		MASTER_SEQ_NO = ?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, masterSeq);
			
			////System.out.println("##########" + pstmt);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao updateMaster ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
	/*
	 * 2015-05-29 ksy 총 걸음수 가져오기
	 */
	public int selectToTalWalk(int memSeqNo)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT      						\n");
		sql.append("		TOTAL_WALK+1 AS TOTAL_WALK	\n");
		sql.append("FROM								\n");
		sql.append("		TB_MASTER					\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_SEQ_NO = ? 				\n");
		sql.append("ORDER BY CRT_DATE DESC				\n");
		sql.append("LIMIT 1								\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			rs= pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("TOTAL_WALK");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao selectToTalWalk ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
	/*
	 * 2015-05-29 ksy 마스트 테이블 인설트
	 */
	public int insertMaster(int memSeqNo, int CHECK, int totalWalk, int memDepart, int memAffiliation)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO																									\n");
		sql.append("				TB_MASTER																					\n");
		sql.append("(																											\n");
		if(CHECK >0){
			sql.append("	 TOTAL_WALK, WALK_DOWN, TODAY_TOTAL, MEM_SEQ_NO, CRT_DATE, MEM_DEPART, MEM_AFFILIATION											\n");
		}else{
			sql.append("	 TOTAL_WALK, WALK_UP, TODAY_TOTAL, MEM_SEQ_NO, CRT_DATE, MEM_DEPART, MEM_AFFILIATION												\n");
		}
		sql.append(")																											\n");
		sql.append("VALUES(																										\n");
		sql.append("		?,1,1,?,NOW(), ?, ?																			 			\n");
		sql.append(")																											\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, totalWalk);
			pstmt.setInt(2, memSeqNo);
			pstmt.setInt(3, memDepart);
			pstmt.setInt(4, memAffiliation);
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
			//////System.out.println("BeaconDao insertMaster ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-29 ksy tb_mem_stay에서 내 현재위치한 도시 seq, 현 도시까지의 목표 층수합, 첫도시부터의 총 걸음수, orderly
	 */
	public CityVO selectCurrentCityAndStair(int memSeqNo)throws SQLException{
		CityVO vo = new CityVO();
		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT																																										\n");
		sql.append("		STAY_SEQ_NO, a.CITY_SEQ_NO, a.ORDERLY, START_DATE																													\n");
		sql.append("	,	(SELECT SUM(STAIR_NUMBER) FROM TB_CITY WHERE ORDERLY <= a.ORDERLY) AS STAIR_CNT																						\n");
		sql.append("	,	(SELECT SUM(TODAY_TOTAL) FROM TB_MASTER WHERE CRT_DATE >= 																											\n");
		sql.append("		SUBSTRING((SELECT START_DATE FROM TB_MEM_STAY WHERE ORDERLY =1 AND MEM_SEQ_NO = ? ORDER BY START_DATE ASC LIMIT 1),1,10) AND MEM_SEQ_NO = ?) AS WALK_CNT			\n");
		sql.append("	,	(SELECT SUM(STAIR_NUMBER) FROM TB_CITY) AS TOTAL_STAIR																												\n");
		sql.append("	,	(SELECT COUNT(*) FROM TB_WORLD WHERE MEM_SEQ_NO = ?) AS WORLD_CNT																									\n");
		sql.append("FROM																																										\n");
		sql.append("		TB_MEM_STAY a																																						\n");
		sql.append("WHERE 																																										\n");
		sql.append("		MEM_SEQ_NO =? AND END_DATE IS NULL																																	\n");
		sql.append("ORDER BY																																									\n");
		sql.append("		START_DATE DESC																																						\n");
		sql.append("LIMIT 1																																										\n");*/
		sql.append("SELECT																																										\n");
		sql.append("		STAY_SEQ_NO, a.CITY_SEQ_NO, a.ORDERLY, START_DATE																													\n");
		sql.append("	,	(SELECT SUM(STAIR_NUMBER) FROM TB_CITY WHERE ORDERLY <= a.ORDERLY) AS STAIR_CNT																						\n");
		sql.append("	,	(SELECT TOTAL_WALK FROM TB_MASTER WHERE MEM_SEQ_NO = ? ORDER BY CRT_DATE DESC LIMIT 1) AS TOTAL_WALK 																											\n");
		sql.append("	,	(SELECT SUM(STAIR_NUMBER) FROM TB_CITY) AS TOTAL_STAIR																												\n");
		sql.append("	,	(SELECT COUNT(*) FROM TB_WORLD WHERE MEM_SEQ_NO = ?) AS WORLD_CNT																									\n");
		sql.append("FROM																																										\n");
		sql.append("		TB_MEM_STAY a																																						\n");
		sql.append("WHERE 																																										\n");
		sql.append("		MEM_SEQ_NO =? AND END_DATE IS NULL																																	\n");
		sql.append("ORDER BY																																									\n");
		sql.append("		START_DATE DESC																																						\n");
		sql.append("LIMIT 1																																										\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, memSeqNo);
			pstmt.setInt(3, memSeqNo);
		/*	pstmt.setInt(4, memSeqNo);*/
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setCitySeqNo(rs.getInt("CITY_SEQ_NO"));
				vo.setOrderLy(rs.getInt("ORDERLY"));
				vo.setStartDate(rs.getString("START_DATE"));
				vo.setTotalStair(rs.getInt("STAIR_CNT"));
				vo.setTotalWork(rs.getInt("TOTAL_WALK"));
				vo.setStaySeqNo(rs.getInt("STAY_SEQ_NO"));
				vo.setAllStair(rs.getInt("TOTAL_STAIR"));
				vo.setWorldCnt(rs.getInt("WORLD_CNT"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao selectCurrentCityAndStair ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	/*
	 * 2015-05-29 ksy 현 스테이지 클리어
	 */
	public int updateMemStay(int memSeqNo, int citySeqCurrent, int staySeqNo)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE								\n");
		sql.append("		TB_MEM_STAY					\n");
		sql.append("SET									\n");
		sql.append("		END_DATE = NOW()			\n");
		sql.append("WHERE								\n");
		sql.append("		MEM_SEQ_NO = ?				\n");
		sql.append("	AND CITY_SEQ_NO = ?				\n");
		sql.append("	AND STAY_SEQ_NO = ?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, citySeqCurrent);
			pstmt.setInt(3, staySeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao updateMemStay ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-29 ksy 다음 도시 입력
	 */
	public int insertMemStay(int memSeqNo, int cityVONextSeqNo, int cityVOOrderLy)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO												\n");
		sql.append("			TB_MEM_STAY									\n");
		sql.append("(														\n");
		sql.append("	MEM_SEQ_NO, CITY_SEQ_NO, ORDERLY, START_DATE		\n");
		sql.append(")														\n");
		sql.append("VALUES(													\n");
		sql.append("	?,?,?,NOW()											\n");
		sql.append(")										\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, cityVONextSeqNo);
			pstmt.setInt(3, cityVOOrderLy);
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
			//////System.out.println("BeaconDao insertMemStay ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-29 ksy 처음도시의 시작일과 마지막 도시의 끝나는 시간을 계산한다.
	 */
	public int selectMemStayAllTime(int memSeqNo ,int orderly)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																																				\n");
		sql.append("		TO_DAYS(p.END_DATE) - TO_DAYS(z.START_DATE)	AS ALL_TIME																						\n");
		sql.append("FROM																																				\n");
		sql.append("		(SELECT START_DATE FROM TB_MEM_STAY WHERE END_DATE IS NOT NULL AND MEM_SEQ_NO = ? AND ORDERLY = 1 ORDER BY START_DATE DESC LIMIT 1)z,		\n");
		sql.append("		(SELECT END_DATE FROM TB_MEM_STAY WHERE END_DATE IS NOT NULL AND MEM_SEQ_NO = ? AND ORDERLY = ? ORDER BY END_DATE LIMIT 1)p					\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, memSeqNo);
			pstmt.setInt(3, orderly);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("ALL_TIME");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao selectMemStayAllTime ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-29 ksy 세계일주 입력
	 * 
	 */
	public int insertWorld(int memSeqNo, int worldWalkPeriod)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO										\n");
		sql.append("			TB_WORLD							\n");
		sql.append("(												\n");
		sql.append("	MEM_SEQ_NO, WORLD_DATE, CRT_DATE			\n");
		sql.append(")												\n");
		sql.append("VALUES(											\n");
		sql.append("	?,?,NOW()									\n");
		sql.append(")												\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, worldWalkPeriod);
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
			//////System.out.println("BeaconDao insertWorld ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-29 ksy 도시를 클리어 하면서 현도시의 시장과 비교
	 */
	public int selectMemStayCityPeriod(int memSeqNo, int citySeqCurrent)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																\n");
		sql.append("		UNIX_TIMESTAMP(END_DATE) - UNIX_TIMESTAMP(START_DATE) AS CLEAR_TIME		\n");
		sql.append("FROM																\n");
		sql.append("		TB_MEM_STAY													\n");
		sql.append("WHERE																\n");
		sql.append("		MEM_SEQ_NO = ?												\n");
		sql.append("	AND CITY_SEQ_NO = ?												\n");
		sql.append("ORDER BY START_DATE DESC LIMIT 1									\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, citySeqCurrent);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CLEAR_TIME");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao selectMemStayCityPeriod ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
	/*
	 * 2015-05-29 ksy 해동 도시의 시장 CLEAR_TIME 을 가져옴
	 */
	public MayorVO selectMayorPeriod(int citySeqCurrent, int memAffiliation)throws SQLException{
		MayorVO vo = new MayorVO();
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		WALK_PERIOD, MAYOR_SEQ_NO	\n");
		sql.append("FROM								\n");
		sql.append("		TB_MAYOR					\n");
		sql.append("WHERE								\n");
		sql.append("		CITY_SEQ_NO = ?				\n");
		sql.append("AND		AFFILIATION_NO = ?								\n");
		sql.append("	AND DEL_YN <>'Y'				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, citySeqCurrent);
			pstmt.setInt(2, memAffiliation);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setWalkPeriod(rs.getInt("WALK_PERIOD"));
				vo.setMayorSeq(rs.getInt("MAYOR_SEQ_NO"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("BeaconDao selectMemStayCityPeriod ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	/*
	 * 2015-05-29 ksy 시장 입력
	 */
	public int insetMayor(int memSeqNo, int citySeqCurrent, int cityWalkPeriod, int memAffiliation)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		int cnt = 0;
		sql.append("INSERT INTO															\n");
		sql.append("	TB_MAYOR												\n");
		sql.append("(																	\n");
		sql.append("	MEM_SEQ_NO, AFFILIATION_NO,  CITY_SEQ_NO, WALK_PERIOD, CRT_YYYYMMDD, CRT_DATE					\n");
		sql.append(")																	\n");
		sql.append("VALUES(																\n");
		sql.append("	?, ?, ?, ?, REPLACE(SUBSTRING(NOW(),1,10),'-',''), NOW()														\n");
		sql.append(")																	\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, memAffiliation);
			pstmt.setInt(++cnt, citySeqCurrent);
			pstmt.setInt(++cnt, cityWalkPeriod);
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
			//////System.out.println("BeaconDao insetMayor ERROR : " +e);
			 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-29 ksy 시장 삭제
	 */
	public int deleteMayor(int mayorSeq, int citySeqCurrent, int memAffiliation)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE							\n");
		sql.append("		TB_MAYOR				\n");
		sql.append("SET 							\n");
		sql.append("		DEL_YN = 'Y'			\n");
		sql.append("WHERE							\n");
		sql.append("		MAYOR_SEQ_NO = ?		\n");
		sql.append("AND 	AFFILIATION_NO = ?		\n");
		sql.append("AND 	CITY_SEQ_NO = ?			\n");
		
		/*sql.append("DELETE FROM							\n");
		sql.append("		TB_MAYOR				\n");
		sql.append("WHERE							\n");
		sql.append("		MAYOR_SEQ_NO = ?		\n");
		sql.append("AND 	AFFILIATION_NO = ?		\n");
		sql.append("AND 	CITY_SEQ_NO = ?			\n");*/
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, mayorSeq);
			pstmt.setInt(2, memAffiliation);
			pstmt.setInt(3, citySeqCurrent);
			result = pstmt.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace(); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}

    public ArrayList<NoticeVO> noticeList(int pageNo, int rowSize, int affiliationSeq)throws SQLException{
        int page = (pageNo - 1) *rowSize;
        StringBuffer sql = new StringBuffer();
        ArrayList<NoticeVO> list = new ArrayList<NoticeVO>();
        int cnt = 0;
        sql.append("SELECT													\n");
        sql.append("		NOTICE_SEQ_NO,	NOTICE_TITLE, CRT_DATE			\n");
        sql.append("FROM													\n");
        sql.append("		TB_NOTICE										\n");
        sql.append("WHERE													\n");
        sql.append("		DEL_YN <>'Y'									\n");
        sql.append("AND		( AFFILIATION_SEQ = 0								\n");
        if(affiliationSeq > 0){
            sql.append("OR		AFFILIATION_SEQ = ?								\n");
        }
        sql.append(")								\n");
        sql.append("ORDER BY NOTICE_SEQ_NO DESC								\n");
        sql.append("LIMIT ?,?												\n");

        try{
            pstmt = conn.prepareStatement(sql.toString());
            if(affiliationSeq > 0){
                pstmt.setInt(++cnt, affiliationSeq);
            }
            pstmt.setInt(++cnt, page);
            pstmt.setInt(++cnt, rowSize);
            rs = pstmt.executeQuery();
            //System.out.println(pstmt.toString());
            while(rs.next()){
                NoticeVO vo = new NoticeVO();
                vo.setNotiSeqNo(rs.getInt("NOTICE_SEQ_NO"));
                vo.setNotiTitle(rs.getString("NOTICE_TITLE"));
                vo.setCrtDate(rs.getString("CRT_DATE"));
                list.add(vo);
            }
        }catch(Exception e){

            //////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
            //////System.out.println("NoticeDao noticeList ERROR : " +e);
        }finally{
            closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
        }

        return list;
    }

    public int beaconLogCnt(int memSeq)throws SQLException{

        int result=0;

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT						\n");
        sql.append("		COUNT(*) AS CNT		\n");
        sql.append("FROM						\n");
        sql.append("		TB_BEACON_LOG 															\n");
        sql.append("WHERE																		\n");
        sql.append("		MEM_SEQ_NO = ?														\n");

        try{
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, memSeq);
            rs = pstmt.executeQuery();
            if(rs.next()){
                result = rs.getInt("CNT");
            }
        }catch(Exception e){
            //////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
            //////System.out.println("NoticeDao cntNotice ERROR : " +e);
        }finally{
            closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
        }
        return result;
    }

    public ArrayList<BeaconVO> beaconLogList(int pageNo, int rowSize, int memSeq)throws SQLException{

        int page = (pageNo - 1) *rowSize;

        StringBuffer sql = new StringBuffer();
        ArrayList<BeaconVO> list = new ArrayList<BeaconVO>();

        int cnt = 0;

        sql.append("SELECT													\n");
        sql.append("		BELOG_SEQ_NO, MEM_SEQ_NO, POSITION_BEACON, START_BEACON, END_BEACON, RESULT_BEACON, CRT_DATE			\n");
        sql.append("FROM													\n");
        sql.append("		TB_BEACON_LOG										\n");
        sql.append("WHERE													\n");
        sql.append("		MEM_SEQ_NO = ?									\n");
        sql.append("ORDER BY BELOG_SEQ_NO DESC								\n");
        sql.append("LIMIT ?,?												\n");

        try{
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(++cnt, memSeq);
            pstmt.setInt(++cnt, page);
            pstmt.setInt(++cnt, rowSize);
            rs = pstmt.executeQuery();
            //System.out.println(pstmt.toString());
            while(rs.next()){
                BeaconVO vo = new BeaconVO();

                vo.setBelogSeqNo(rs.getInt("BELOG_SEQ_NO"));
                vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
                vo.setStairsPosition(rs.getInt("POSITION_BEACON"));
                vo.setStartBeacon(rs.getInt("START_BEACON"));
                vo.setEndBeacon(rs.getInt("END_BEACON"));
                vo.setResultBeacon(rs.getInt("RESULT_BEACON"));
                vo.setCrtDate(rs.getString("CRT_DATE"));

                list.add(vo);
            }
        }catch(Exception e){

            //////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
            //////System.out.println("NoticeDao noticeList ERROR : " +e);
        }finally{
            closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
        }

        return list;
    }


}
