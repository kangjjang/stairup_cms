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
import util.Constant;
import util.FileUtil;
import vo.PhotoVO;

public class PhotoDao{

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	public static final String PHOTO_TABLE_NAME = "TB_PHOTO"; 
	
	private static final String COMMENT_TABLE_NAME = "TB_COMMENT";
	private static final String FEEL_TABLE_NAME = "TB_FEEL_LIKE";
	
	public PhotoDao() throws SQLException {
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
		  
	public int insertPhoto (int boardSeqNo, String boardCate,int memSeqNo, String photoName, String uploadPath, String uploadThumbPath ) throws SQLException{
		int result=0;
		StringBuffer sql = new StringBuffer();		
		sql.append("INSERT INTO " + PHOTO_TABLE_NAME + " ( 			\n");
		sql.append("	BOARD_SEQ_NO, BOARD_CATE, MEM_SEQ_NO, PHOTO_NAME,	\n");
//		if (photoAddr != null && !photoAddr.isEmpty()) {
//			sql.append("	PHOTO_ADDR,			\n");
//		}
		sql.append("	CRT_DATE, CHG_DATE  			\n");
		sql.append(" 	) VALUES ( ?, ?, ?, ?			\n");
		sql.append("	, now(), now()) ");
try{
		pstmt = conn.prepareStatement(sql.toString());
		
		pstmt.setInt(1,boardSeqNo);
		pstmt.setString(2,boardCate);
		pstmt.setInt(3,memSeqNo);
		pstmt.setString(4,photoName);
//		if (photoAddr != null && !photoAddr.isEmpty()) {
//			pstmt.setString(photoAddr);
//		}
		
		result = pstmt.executeUpdate();		
	}catch(Exception e){
		
		//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
		//////System.out.println("PhotoDao insertPhoto ERROR : " +e); 
	}finally{
		closeAll(rs, pstmt);
		/*if(rs != null) rs.close();	
		if(pstmt != null) pstmt.close();
		if(conn != null) conn.close();*/
	}
		
		return result;
	}
	
	public int delete(int photoSeqNo, int memSeqNo) throws SQLException {
		int result=0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE " + PHOTO_TABLE_NAME + " SET 	 CHG_DATE = now() 		\n");
		sql.append("	, DEL_YN 	= 'Y'								\n");
		sql.append("	, DEL_DATE 	= now()								\n");
		sql.append("	WHERE PHOTO_SEQ_NO = ? AND MEM_SEQ_NO = ?	AND DEL_YN <> 'Y'	\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		pstmt.setInt(1,photoSeqNo);
		pstmt.setInt(2,memSeqNo);	
		
		result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("PhotoDao delete2 ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	public int delete(String boardCate, int boardSeqNo, int memSeqNo) throws SQLException {
		int result = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE " + PHOTO_TABLE_NAME + " SET 	 CHG_DATE = now() 		\n");
		sql.append("	, DEL_YN 	= 'Y'											\n");
		sql.append("	, DEL_DATE 	= now()											\n");
		sql.append("	WHERE BOARD_CATE = ? AND BOARD_SEQ_NO = ? AND MEM_SEQ_NO = ? AND DEL_YN <> 'Y'	\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		
		pstmt.setString(1,boardCate);
		pstmt.setInt(2,boardSeqNo);
		pstmt.setInt(3,memSeqNo);	
		
		result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("PhotoDao delete3 ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	public String getPhotoName(int photoSeqNo, int memSeqNo) throws SQLException {
		
		String name = "";
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT PHOTO_NAME  							\n");
		sql.append("	FROM " + PHOTO_TABLE_NAME + " 				\n");
		sql.append("	WHERE PHOTO_SEQ_NO = ? AND MEM_SEQ_NO = ?  AND DEL_YN <> 'Y'	\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		
		pstmt.setInt(1,photoSeqNo);
		pstmt.setInt(2,memSeqNo);
		
		rs = pstmt.executeQuery();
		
		if (rs.next()) {
			name = rs.getString("PHOTO_NAME");
		}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("PhotoDao getPhotoName ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return name == null ? "" : name;		
	}
	
	/*
	 * 업체 목록에 대한 사진을 가져올때 사용
	 */
	public ArrayList<PhotoVO> selectList(String boardCate, int boardSeqNo) throws SQLException {		
		ArrayList<PhotoVO> list = new ArrayList<PhotoVO>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *  	\n");
		sql.append("	,(SELECT COUNT(*) FROM " + COMMENT_TABLE_NAME + " b WHERE a.PHOTO_SEQ_NO = b.BOARD_SEQ_NO AND b.DEL_YN <> 'Y') AS CMT_COUNT	\n");
		sql.append("	,(SELECT COUNT(*) FROM " + FEEL_TABLE_NAME + " c WHERE a.PHOTO_SEQ_NO = c.BOARD_SEQ_NO AND c.BOARD_CATE = '"+ Constant.CATEGORY_PHOTO +"') AS FEEL_COUNT	\n");
		sql.append("	FROM " + PHOTO_TABLE_NAME + " a										\n");
		sql.append("	WHERE a.BOARD_CATE = ? AND a.BOARD_SEQ_NO = ? AND a.DEL_YN <> 'Y'	\n");
		sql.append("	ORDER BY a.PHOTO_SEQ_NO DESC										\n");
		
//		sql.append(" SELECT PHOTO_NAME, BOARD_CATE FROM " + PHOTO_TABLE_NAME + " WHERE BOARD_CATE = ? AND BOARD_SEQ_NO = ?   	\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		
		pstmt.setString(1,boardCate);
		pstmt.setInt(2,boardSeqNo);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			PhotoVO vo = new PhotoVO();
			vo.setPhotoSeqNo(rs.getInt("PHOTO_SEQ_NO"));
			vo.setPhotoName(rs.getString("PHOTO_NAME"));
			vo.setBoardCate(rs.getString("BOARD_CATE"));
			vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
			vo.setPhotoName(rs.getString("PHOTO_NAME"));
			vo.setPhotoAddr(rs.getString("PHOTO_ADDR"));
			vo.setCrtDate(rs.getString("CRT_DATE"));
			vo.setCountFeel(rs.getInt("FEEL_COUNT"));
			vo.setCountComment(rs.getInt("CMT_COUNT"));
			
			list.add(vo);
		}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("PhotoDao selectList2 ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	
	/*
	 * 호두야 앨범, 담벼락의 사진등 좋아요 유무를 가져오기 위해 다음 코드를 사용
	 */
	public ArrayList<PhotoVO> selectList(String boardCate, int memSeqNo, int boardSeqNo) throws SQLException {		
		ArrayList<PhotoVO> list = new ArrayList<PhotoVO>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *  	\n");
		sql.append("	,(SELECT COUNT(*) FROM " + COMMENT_TABLE_NAME + " b WHERE a.PHOTO_SEQ_NO = b.BOARD_SEQ_NO AND b.DEL_YN <> 'Y') AS CMT_COUNT	\n");
		sql.append("	,(SELECT COUNT(*) FROM " + FEEL_TABLE_NAME + " c WHERE a.PHOTO_SEQ_NO = c.BOARD_SEQ_NO AND c.BOARD_CATE = '"+ Constant.CATEGORY_PHOTO +"') AS FEEL_COUNT	\n");
		sql.append("	,(SELECT COUNT(*) FROM " + FEEL_TABLE_NAME + " c WHERE c.MEM_SEQ_NO = " + memSeqNo + " AND a.PHOTO_SEQ_NO = c.BOARD_SEQ_NO AND c.BOARD_CATE = '"+ Constant.CATEGORY_PHOTO +"') AS MY_FEEL_COUNT	\n");
		sql.append("	FROM " + PHOTO_TABLE_NAME + " a										\n");
		sql.append("	WHERE a.BOARD_CATE = ? AND a.BOARD_SEQ_NO = ? AND a.DEL_YN <> 'Y'	\n");
		sql.append("	ORDER BY a.PHOTO_SEQ_NO DESC										\n");
		
//		sql.append(" SELECT PHOTO_NAME, BOARD_CATE FROM " + PHOTO_TABLE_NAME + " WHERE BOARD_CATE = ? AND BOARD_SEQ_NO = ?   	\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		
		pstmt.setString(1,boardCate);
		pstmt.setInt(2,boardSeqNo);
				
		rs = pstmt.executeQuery();
		while (rs.next()) {
			PhotoVO vo = new PhotoVO();
			vo.setPhotoSeqNo(rs.getInt("PHOTO_SEQ_NO"));
			vo.setPhotoName(rs.getString("PHOTO_NAME"));
			vo.setBoardCate(rs.getString("BOARD_CATE"));
			vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
			vo.setPhotoName(rs.getString("PHOTO_NAME"));
			vo.setPhotoAddr(rs.getString("PHOTO_ADDR"));
			vo.setCrtDate(rs.getString("CRT_DATE"));
			vo.setCountFeel(rs.getInt("FEEL_COUNT"));
			vo.setCountMyFeel(rs.getInt("MY_FEEL_COUNT"));
			vo.setCountComment(rs.getInt("CMT_COUNT"));
			
			list.add(vo);
		}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("PhotoDao selectList3 ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	
	public ArrayList<PhotoVO> selectList(int memSeqNo, int pageNo, int rowSize, String boardCate) throws SQLException {		
		ArrayList<PhotoVO> list = new ArrayList<PhotoVO>();
		int startRow = (pageNo - 1) * rowSize;
		
		StringBuffer sql = new StringBuffer();
			
		sql.append(" SELECT *  	\n");
		sql.append("	,(SELECT COUNT(*) FROM " + COMMENT_TABLE_NAME + " b WHERE a.PHOTO_SEQ_NO = b.BOARD_SEQ_NO AND b.DEL_YN <> 'Y') AS CMT_COUNT	\n");
		sql.append("	,(SELECT COUNT(*) FROM " + FEEL_TABLE_NAME + " c WHERE a.PHOTO_SEQ_NO = c.BOARD_SEQ_NO AND c.BOARD_CATE = '"+ Constant.CATEGORY_PHOTO +"') AS FEEL_COUNT	\n");
		sql.append("	,(SELECT COUNT(*) FROM " + FEEL_TABLE_NAME + " c WHERE c.MEM_SEQ_NO = " + memSeqNo + " AND a.PHOTO_SEQ_NO = c.BOARD_SEQ_NO AND c.BOARD_CATE = '"+ Constant.CATEGORY_PHOTO +"') AS MY_FEEL_COUNT	\n");
		sql.append("	FROM " + PHOTO_TABLE_NAME + " a										\n");
		sql.append("	WHERE a.MEM_SEQ_NO = ? AND a.DEL_YN <> 'Y' AND a.BOARD_CATE = ?		\n");
		sql.append("	ORDER BY a.PHOTO_SEQ_NO DESC					\n");
		sql.append("	LIMIT ?, ?										\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		
		pstmt.setInt(1,memSeqNo);
		pstmt.setString(2,boardCate);
		pstmt.setInt(3,startRow);
		pstmt.setInt(4,rowSize);		
		
		rs = pstmt.executeQuery();
		
		while (rs.next()) {
			PhotoVO vo = new PhotoVO();
			vo.setPhotoSeqNo(rs.getInt("PHOTO_SEQ_NO"));
			vo.setBoardCate(rs.getString("BOARD_CATE"));
			vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
			vo.setPhotoName(rs.getString("PHOTO_NAME"));
			vo.setPhotoAddr(rs.getString("PHOTO_ADDR"));
			vo.setPhotoLat(rs.getString("PHOTO_LAT"));
			vo.setPhotoLon(rs.getString("PHOTO_LON"));
			vo.setCrtDate(rs.getString("CRT_DATE"));	
			vo.setChgDate(rs.getString("CHG_DATE"));
			vo.setCountFeel(rs.getInt("FEEL_COUNT"));
			vo.setCountMyFeel(rs.getInt("MY_FEEL_COUNT"));
			vo.setCountComment(rs.getInt("CMT_COUNT"));
			list.add(vo);
		
		}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("PhotoDao selectList4 ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	
	public int getListCount(int memSeqNo, String boardCate) throws SQLException {
		
		int count = 0;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*)	cnt							\n");
		sql.append("FROM " + PHOTO_TABLE_NAME + " 				\n");
		sql.append("WHERE MEM_SEQ_NO = ? AND DEL_YN <> 'Y' AND 	BOARD_CATE = ?	\n");
		try{
		pstmt = conn.prepareStatement(sql.toString());
		pstmt.setInt(1,memSeqNo);
		pstmt.setString(2,boardCate);
		
		rs = pstmt.executeQuery();

		if (rs.next()) {
			count = rs.getInt("cnt");
		}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("PhotoDao getListCount ERROR : " +e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}

		return count;
	}
	
	
}
