package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import com.mysql.jdbc.Statement;

import util.ConnectionUtil;
import vo.GreetingVO;
import vo.NoticeVO;

public class NoticeDao{
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public NoticeDao() throws SQLException {
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
		  
	public int insert (String notiTitle, String notiContent, String img, int affiliationSeq) throws SQLException {
		
		StringBuffer sql = new StringBuffer();
		int result=0;
		sql.append("INSERT INTO TB_NOTICE ( 																\n");
		sql.append("	NOTICE_TITLE, NOTICE_CONTENT, NOTICE_PIC,CRT_DATE, CHG_DATE, DEL_YN, AFFILIATION_SEQ 		\n");
		sql.append(")	VALUES (?, ?, ?,now(), now(), 'N' , ?	 						\n");
		sql.append(" 	)													\n");
		
		
		int cnt = 0;
		try{
		pstmt = conn.prepareStatement(sql.toString());
		
		pstmt.setString(++cnt,notiTitle);
		pstmt.setString(++cnt,notiContent);		
		pstmt.setString(++cnt,img);		
		pstmt.setInt(++cnt, affiliationSeq);
		
		result= pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("NoticeDao insert ERROR : " +e);  
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}

	
	public int delete(int boardSeqNo) throws SQLException {
		int result=0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE 							\n");
		sql.append("		TB_NOTICE				\n");
		sql.append("SET								\n");
		sql.append("		CHG_DATE = NOW(),		\n");
		sql.append("		DEL_YN   = 'Y'			\n");
		sql.append("WHERE NOTICE_SEQ_NO = ? 		\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		pstmt.setInt(1,boardSeqNo);
		
		result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("NoticeDao delete ERROR : " +e); 

		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	public int update(int faqSeqNo, String faqTitle, String faqContent, String img, int affiliationSeq) throws SQLException {
		int result=0;
		StringBuffer sql = new StringBuffer();		
		
		sql.append("UPDATE TB_NOTICE SET CHG_DATE = now(), AFFILIATION_SEQ = ? 		\n");
		if(!faqTitle.isEmpty()) 	sql.append("	, NOTICE_TITLE = ? 	\n");
		if(!faqContent.isEmpty()) 	sql.append("	, NOTICE_CONTENT = ? 	\n");
		if(!img.isEmpty()) 			sql.append("	, NOTICE_PIC = ?		\n");
		sql.append("		WHERE NOTICE_SEQ_NO = ? \n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			int cnt =0;
			pstmt.setInt(++cnt,affiliationSeq);
			if(!faqTitle.isEmpty()) 	pstmt.setString(++cnt,faqTitle);
			if(!faqContent.isEmpty()) 	pstmt.setString(++cnt,faqContent);
			if(!img.isEmpty()) 			pstmt.setString(++cnt, img);
			pstmt.setInt(++cnt,faqSeqNo);
			
			result = pstmt.executeUpdate();		
			//System.out.println(pstmt.toString());
			if (result > 0) {
				result = faqSeqNo;
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("NoticeDao update ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
	
		return result;
	}
	
	/*
	 * 2015-04-30 ksy 공지사항 리스트
	 */
	public ArrayList<NoticeVO> noticeListCms(int pageNo, int rowSize, int roles)throws SQLException{
		int page = (pageNo - 1) *rowSize;
		StringBuffer sql = new StringBuffer();
		ArrayList<NoticeVO> list = new ArrayList<NoticeVO>();
		int cnt = 0;		
		sql.append("SELECT																		\n");
		sql.append("		NOTICE_SEQ_NO,	NOTICE_TITLE, a.CRT_DATE, AFFILIATION_NAME, AFFILIATION_SEQ			\n");
		sql.append("FROM																		\n");
		sql.append("		TB_NOTICE a															\n");
		sql.append("LEFT OUTER JOIN																\n");
		sql.append("		TB_AFFILIATION b													\n");
		sql.append("ON a.AFFILIATION_SEQ = b.SEQ_NO												\n");
		sql.append("WHERE																		\n");
		sql.append("		a.DEL_YN <>'Y'														\n");
		if(roles > 0){
			sql.append("AND		a.AFFILIATION_SEQ = ?																		\n");
		}
		sql.append("ORDER BY NOTICE_SEQ_NO DESC													\n");
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
				NoticeVO vo = new NoticeVO();
				vo.setNotiSeqNo(rs.getInt("NOTICE_SEQ_NO"));
				vo.setNotiTitle(rs.getString("NOTICE_TITLE"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setAffiliationSeq(rs.getInt("AFFILIATION_SEQ"));
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
	
	/*
	 * 2016-07-20 api 공지사항 목록
	 */
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
	/*
	 * 2015-04-30 ksy 공지사항 상세화면
	 */
	public NoticeVO noticeView(int noticeSeqNo) throws SQLException{
		StringBuffer sql = new StringBuffer();
		NoticeVO vo = new NoticeVO();
		sql.append("SELECT																					\n");
		sql.append("		AFFILIATION_SEQ, AFFILIATION_NAME, NOTICE_TITLE, NOTICE_PIC, NOTICE_CONTENT, a.CRT_DATE			\n");
		sql.append("FROM																					\n");
		sql.append("		TB_NOTICE a																		\n");
		sql.append("LEFT OUTER JOIN																			\n");
		sql.append("		TB_AFFILIATION b																\n");
		sql.append("ON a.AFFILIATION_SEQ = b.SEQ_NO															\n");
		sql.append("WHERE																					\n");
		sql.append("		NOTICE_SEQ_NO = ?																\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, noticeSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setAffiliationSeq(rs.getInt("AFFILIATION_SEQ"));
				vo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setNotiTitle(rs.getString("NOTICE_TITLE"));
				vo.setNoticePic(rs.getString("NOTICE_PIC"));
				vo.setNotiContent(rs.getString("NOTICE_CONTENT"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("NoticeDao noticeView ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return vo;
	}
	
	/*
	 * 2015-04-30 ksy 공지사항 총 개수
	 */
	public int cntNotice()throws SQLException{
		int result=0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT						\n");
		sql.append("		COUNT(*) AS CNT		\n");
		sql.append("FROM						\n");
		sql.append("		TB_NOTICE a															\n");
		sql.append("LEFT OUTER JOIN																\n");
		sql.append("		TB_AFFILIATION b													\n");
		sql.append("ON a.AFFILIATION_SEQ = b.SEQ_NO												\n");
		sql.append("WHERE																		\n");
		sql.append("		a.DEL_YN <>'Y'														\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
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
	/*
	 * 2015-05-27 ksy 시장 인사말 리스트
	 */
	public ArrayList<GreetingVO>selectGreetingList(int pageno, int rowSize)throws SQLException{
		int page = (pageno -1)*rowSize;
		ArrayList<GreetingVO> list = new ArrayList<GreetingVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																\n");
		sql.append("		GREETING_SEQ_NO, GREETING_CONTENT, CRT_DATE					\n");
		sql.append("FROM																\n");
		sql.append("		TB_GREETING													\n");
		sql.append("WHERE																\n");
		sql.append("		DEL_YN <> 'Y'												\n");
		sql.append("ORDER BY CRT_DATE DESC															\n");
		sql.append("LIMIT ?,?															\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, page);
			pstmt.setInt(2, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				GreetingVO vo = new GreetingVO();
				vo.setGreetingSeqNo(rs.getInt("GREETING_SEQ_NO"));
				vo.setGreetingContent(rs.getString("GREETING_CONTENT"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("NoticeDao selectGreetingList ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-27 ksy 인사말 총 개수
	 */
	public int greetingCnt()throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		COUNT(*) AS CNT			\n");
		sql.append("FROM							\n");
		sql.append("		TB_GREETING				\n");
		sql.append("WHERE							\n");
		sql.append("		DEL_YN <> 'Y'			\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("NoticeDao greetingCnt ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-27 ksy 인사말 등록
	 */
	public int insertGreeting(String greetingContent)throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO							\n");
		sql.append("			TB_GREETING				\n");
		sql.append("(									\n");
		sql.append("	GREETING_CONTENT, DEL_YN, CRT_DATE		\n");
		sql.append(")									\n");
		sql.append("VALUES(								\n");
		sql.append("	?, 'N', NOW()							\n");
		sql.append(")							\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, greetingContent);
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
			//////System.out.println("NoticeDao insertGreeting ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-27 ksy 인사말 수정화면
	 */
	public GreetingVO greetingModify(int greetingSeqNo)throws SQLException{
		GreetingVO vo = new GreetingVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT															\n");
		sql.append("		GREETING_SEQ_NO, GREETING_CONTENT, CRT_DATE				\n");
		sql.append("FROM															\n");
		sql.append("		TB_GREETING												\n");
		sql.append("WHERE															\n");
		sql.append("		GREETING_SEQ_NO = ?										\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, greetingSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setGreetingSeqNo(rs.getInt("GREETING_SEQ_NO"));
				vo.setGreetingContent(rs.getString("GREETING_CONTENT"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("NoticeDao greetingModify ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	/*
	 * 2015-05-27 ksy 인사말 수정
	 */
	public int updateGreeting(String greetingContent, int greetingSeqNo)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE									\n");
		sql.append("		TB_GREETING						\n");
		sql.append("SET										\n");
		sql.append("		GREETING_CONTENT = ?			\n");
		sql.append("WHERE									\n");
		sql.append("		GREETING_SEQ_NO = ?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, greetingContent);
			pstmt.setInt(2, greetingSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("NoticeDao greetingModify ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-05-27 ksy 인사말 삭제
	 */
	public int deleteGreeting(int greetingSeqNo) throws SQLException {
		int result=0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE 							\n");
		sql.append("		TB_GREETING				\n");
		sql.append("SET								\n");
		sql.append("		DEL_YN   = 'Y'			\n");
		sql.append("WHERE GREETING_SEQ_NO = ? 		\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		pstmt.setInt(1,greetingSeqNo);
		
		result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("NoticeDao delete ERROR : " +e); 

		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
}
