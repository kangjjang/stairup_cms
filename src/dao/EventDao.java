package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import util.ConnectionUtil;
import util.Constant;
import vo.EventVO;
import vo.MemberProfileVO;

public class EventDao{

	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public static final String BOARD_TABLE_NAME = "TB_EVENT";
	private static final String BOARD_COMMENT_TABLE_NAME = "TB_COMMENT";
	private static final String BOARD_FEEL_TABLE_NAME = "TB_FEEL_LIKE";
	
	public static final int WALL_LIST_TYPE_RECENT = 1;		// 최신글
	public static final int WALL_LIST_TYPE_POPULAR = 2;	    // 인기글
	public static final int WALL_LIST_TYPE_FRIEND = 3;		// 친구들
	public static final int WALL_LIST_TYPE_PERSON = 4;		// 특정인
	
	public EventDao() throws SQLException {
		conn = ConnectionUtil.getConnection();	
		// TODO Auto-generated constructor stub
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
	
	public int insert(String wbContent, String notiTitle, String img, int affiliationSeq) throws SQLException{
		int result=0;	  
		StringBuffer sql = new StringBuffer();		
		sql.append("INSERT INTO " + BOARD_TABLE_NAME + " ( 			\n");
		sql.append("	EVENT_TITLE, EVENT_CONTENT, EVENT_PIC, DEL_YN, CRT_DATE, CHG_DATE, AFFILIATION_SEQ		\n");
		sql.append(" 	) VALUES ( ?, ?, ?,'N', now(), now(), ? )				\n");
		try{
		pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
		
		pstmt.setString(1,notiTitle);
		pstmt.setString(2,wbContent);
		pstmt.setString(3,img);
		pstmt.setInt(4,affiliationSeq);
		result	 = pstmt.executeUpdate();	
		if(result > 0){
				result = -1;
					try{
						rs = pstmt.getGeneratedKeys();
							if(rs.next())
								result = rs.getInt(1);
					}catch(SQLException e){
						result = -1;
					}
			}
		}catch(Exception e){
				
				//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
				//////System.out.println("EventDao  insert ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	public int update(int no, String wbContent, String evnetTitle, String listImgName, int affiliationSeq) throws SQLException{
		int result=0;  
		StringBuffer sql = new StringBuffer();		
		
		sql.append("UPDATE " + BOARD_TABLE_NAME + " SET 	 CHG_DATE = now(), AFFILIATION_SEQ = ? 			\n");
		
		if(!wbContent.isEmpty())  sql.append("	, EVENT_CONTENT 	= ?			\n");
		if(!evnetTitle.isEmpty()) sql.append("	, EVENT_TITLE 		= ?			\n");
		if(!listImgName.isEmpty())sql.append("	, EVENT_PIC 		= ?			\n");
		sql.append("		WHERE EVENT_SEQ_NO = ? 										\n");
		
		int cnt =0;
		try{
		pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
		pstmt.setInt(++cnt,affiliationSeq);
		if(!wbContent.isEmpty()) 	pstmt.setString(++cnt,wbContent);
		if(!evnetTitle.isEmpty()) 	pstmt.setString(++cnt,evnetTitle);
		if(!listImgName.isEmpty())   pstmt.setString(++cnt,listImgName);
		pstmt.setInt(++cnt,no);
		
	//	//System.out.println(getPstmt());
		result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("EventDao update ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	public int delete(int trSeqNo) throws SQLException {
		int result=0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE " + BOARD_TABLE_NAME + " SET 	 CHG_DATE = now() 		\n");
		sql.append("	, DEL_YN 	= 'Y'											\n");
		sql.append("	WHERE EVENT_SEQ_NO = ? AND DEL_YN <> 'Y'						\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		pstmt.setInt(1,trSeqNo);
		
		result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("EventDao delete ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return result;
	}
	
	/*
	 * 2015-04-30 ksy 이벤트 리스트
	 */
	
	public ArrayList<EventVO> eventList(int pageNo,int rowSize) throws SQLException{
		int page = (pageNo - 1) * rowSize;
		ArrayList<EventVO> list = new ArrayList<EventVO>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT																		\n");
		sql.append("		AFFILIATION_NAME, EVENT_SEQ_NO, EVENT_TITLE, a.CRT_DATE				\n");
		sql.append("FROM																		\n");
		sql.append("		TB_EVENT a															\n");
		sql.append("LEFT OUTER JOIN																\n");
		sql.append("		TB_AFFILIATION b													\n");
		sql.append("ON a.AFFILIATION_SEQ = b.SEQ_NO												\n");
		sql.append("WHERE																		\n");
		sql.append("		a.DEL_YN <>'Y'														\n");
		sql.append("ORDER BY EVENT_SEQ_NO DESC													\n");
		sql.append("LIMIT ?,?																	\n");
	
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, page);
			pstmt.setInt(2, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				EventVO vo = new EventVO();
				vo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setEventSeqNo(rs.getInt("EVENT_SEQ_NO"));
				vo.setEventTitle(rs.getString("EVENT_TITLE"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("EventDao eventList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return list;
	}
	
	/*
	 * 2016-07-20 api 이벤트 리스트 수정중
	 */
	
	public ArrayList<EventVO> eventList(int pageNo,int rowSize, int affiliationSeq) throws SQLException{
		int page = (pageNo - 1) * rowSize;
		ArrayList<EventVO> list = new ArrayList<EventVO>();
		StringBuffer sql = new StringBuffer();
		int cnt = 0;
		sql.append("SELECT													\n");
		sql.append("		EVENT_SEQ_NO, EVENT_TITLE, CRT_DATE				\n");
		sql.append("FROM													\n");
		sql.append("		TB_EVENT										\n");
		sql.append("WHERE													\n");
		sql.append("		DEL_YN <>'Y'									\n");
		sql.append("AND		( AFFILIATION_SEQ = 0								\n");
		if(affiliationSeq > 0){
			sql.append("OR		AFFILIATION_SEQ = ?								\n");
		}
		sql.append(")								\n");
		sql.append("ORDER BY EVENT_SEQ_NO DESC								\n");
		sql.append("LIMIT ?,?												\n");
	
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(affiliationSeq > 0){
				pstmt.setInt(++cnt, affiliationSeq);
			}
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				EventVO vo = new EventVO();
				vo.setEventSeqNo(rs.getInt("EVENT_SEQ_NO"));
				vo.setEventTitle(rs.getString("EVENT_TITLE"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("EventDao eventList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return list;
	}
	
	/*
	 * CMS 이벤트 리스트
	 */
	/*
	 * 2016-07-20 api 이벤트 리스트 수정중
	 */
	
	public ArrayList<EventVO> CmsEventList(int pageNo,int rowSize, int roles) throws SQLException{
		int page = (pageNo - 1) * rowSize;
		ArrayList<EventVO> list = new ArrayList<EventVO>();
		StringBuffer sql = new StringBuffer();
		int cnt = 0;
		sql.append("SELECT																		\n");
		sql.append("		EVENT_SEQ_NO, EVENT_TITLE, a.CRT_DATE, AFFILIATION_NAME, AFFILIATION_SEQ									\n");
		sql.append("FROM	TB_EVENT a															\n");
		sql.append("LEFT OUTER JOIN																\n");
		sql.append("		TB_AFFILIATION b													\n");
		sql.append("ON a.AFFILIATION_SEQ = b.SEQ_NO												\n");
		sql.append("WHERE																		\n");
		sql.append("		a.DEL_YN <>'Y'														\n");
		if(roles > 0){
			sql.append("AND		a.AFFILIATION_SEQ = ?											\n");
		}
		sql.append("ORDER BY EVENT_SEQ_NO DESC													\n");
		sql.append("LIMIT ?,?																	\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(roles > 0){
				pstmt.setInt(++cnt, roles);
			}
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				EventVO vo = new EventVO();
				vo.setEventSeqNo(rs.getInt("EVENT_SEQ_NO"));
				vo.setEventTitle(rs.getString("EVENT_TITLE"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setAffiliationSeq(rs.getInt("AFFILIATION_SEQ"));
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
	 * 2015-04-30 ksy 이벤트 상세화면
	 */
	public EventVO eventView(int eventSeqNo)throws SQLException{
		EventVO vo = new EventVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																				\n");
		sql.append("		AFFILIATION_SEQ, AFFILIATION_NAME, EVENT_TITLE, EVENT_PIC, EVENT_CONTENT, a.CRT_DATE			\n");
		sql.append("FROM																					\n");
		sql.append("		TB_EVENT a																		\n");
		sql.append("LEFT OUTER JOIN																			\n");
		sql.append("		TB_AFFILIATION b																\n");
		sql.append("ON a.AFFILIATION_SEQ = b.SEQ_NO															\n");
		sql.append("WHERE																					\n");
		sql.append("		EVENT_SEQ_NO = ?																\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, eventSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setAffiliationSeq(rs.getInt("AFFILIATION_SEQ"));
				vo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setEventTitle(rs.getString("EVENT_TITLE"));
				vo.setEventPic(rs.getString("EVENT_PIC"));
				vo.setEventContent(rs.getString("EVENT_CONTENT"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("EventDao eventView ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return vo;
	}
	
	/*
	 * 2015-04-30 ksy 이벤트 총 개수
	 */
	public int getListCount()throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																		\n");
		sql.append("		COUNT(*) AS CNT														\n");
		sql.append("FROM																		\n");
		sql.append("		TB_EVENT a															\n");
		sql.append("LEFT OUTER JOIN																\n");
		sql.append("		TB_AFFILIATION b													\n");
		sql.append("ON a.AFFILIATION_SEQ = b.SEQ_NO												\n");
		sql.append("WHERE																		\n");
		sql.append("		a.DEL_YN <>'Y'														\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs =pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("EventDao getListCount ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return result;
	}
}

	
