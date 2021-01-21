package dao;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.Lock;

import javax.imageio.ImageIO;

import util.ConnectionUtil;
import vo.DonationVO;
import vo.MemberVO;

import com.mysql.jdbc.Statement;

public class DonationDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public DonationDao() throws SQLException{
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
	 * 2015-05-11 ksy 힘내요 기부 리스트
	 */
	public ArrayList<DonationVO> selectDonationList(int pageNo, int rowSize)throws SQLException{
		
		int pageno = (pageNo -1) * rowSize;
		ArrayList<DonationVO> list = new ArrayList<DonationVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																															\n");
		sql.append("		GIVE_TITLE, b.GIVE_AIM, CRT_DATE, GIVE_SEQ_NO, GIVE_PIC, END_DATE,													\n");
		sql.append("		(SELECT COUNT(*) FROM TB_LIKE WHERE TAGET_GUBUN = 1 AND TAGET_SEQ_NO = GIVE_SEQ_NO) AS FCNT,							\n");
		sql.append("		(SELECT SUM(TODAY_TOTAL) FROM TB_MASTER a	WHERE a.CRT_DATE >= b.CRT_DATE AND a.CRT_DATE <= END_DATE) AS CNT,			\n");
		sql.append("		(SELECT COUNT(*) FROM TB_REVIEW c WHERE c.TAGET_GUBUN = 1 AND c.TAGET_SEQ_NO = GIVE_SEQ_NO) AS RCNT						\n");
		sql.append("FROM																															\n");
		sql.append("		TB_GIVE	b																												\n");
		sql.append("WHERE																															\n");
		sql.append("		b.GIVE_YN <> 'Y'																											\n");
		sql.append("ORDER BY																														\n");
		sql.append("		b.CRT_DATE DESC																											\n");
		sql.append("LIMIT ?,?																														\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, pageno);
			pstmt.setInt(2, rowSize);
			rs=pstmt.executeQuery();
			while(rs.next()){
				DonationVO vo = new DonationVO();
				vo.setTitle(rs.getString("GIVE_TITLE"));
				vo.setAim(rs.getInt("GIVE_AIM"));
				vo.setGiveSeqNo(rs.getInt("GIVE_SEQ_NO"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setCnt(rs.getInt("CNT"));
				vo.setPic(rs.getString("GIVE_PIC"));
				vo.setEndDate(rs.getString("END_DATE"));
				vo.setReviewCnt(rs.getInt("RCNT"));
				vo.setFightCnt(rs.getInt("FCNT"));
				list.add(vo);
			}
		}catch(Exception e){
		
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DonationDao selectDonationList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-11 ksy 힘내요 기부 상세화면 기본정보
	 */
	public DonationVO selectDonationView(int doSeqNo)throws SQLException{
		int cnt = 0;
		DonationVO vo = new DonationVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT											\n");
		sql.append("		GIVE_TITLE, GIVE_CONTENT, GIVE_PIC		\n");
		sql.append("FROM											\n");
		sql.append("		TB_GIVE									\n");
		sql.append("WHERE											\n");
		sql.append("		GIVE_SEQ_NO = ?							\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(++cnt, doSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setTitle(rs.getString("GIVE_TITLE"));
				vo.setContent(rs.getString("GIVE_CONTENT"));
				vo.setPic(rs.getString("GIVE_PIC"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DonationDao selectDonationList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	/*
	 * 2015-05-11 ksy 힘내라 기부 기부천사 top3
	 */
	public ArrayList<MemberVO> selectGiveTop(String endDate, String startDate)throws SQLException{
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date a = new Date();
		SimpleDateFormat aa = new SimpleDateFormat(endDate);
		int compare = sdf.format(d).compareTo(aa.format(d));	//날짜 비교 함수, sdf 와 aa 를 비교한다. sdf가 aa 보다 클경우 1 작을경우 -1 같을 경우 0
		if(compare<0||compare==0){
			sql.append(selectNowGiveTop());						// 진행중 기부천사 top3
		}else{
			sql.append(selectBeforeGiveTop());					// 종료된 기부천사 top3
		}
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(compare>0){
				pstmt.setString(++cnt, startDate);
				pstmt.setString(++cnt, endDate);
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				MemberVO vo = new MemberVO();
				vo.setMemPic(rs.getString("MEM_PIC"));
				vo.setMemName(rs.getString("MEM_NAME"));
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemDepart(rs.getString("DEPART_NAME"));
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DonationDao selectGiveTop ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-11 ksy 힘내요 기부 상세화면 진행중 기부천사 top3
	 */
	private String selectNowGiveTop(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT													\n");
		sql.append("		a.MEM_SEQ_NO, MEM_PIC, MEM_NAME, DEPART_NAME	\n");
		sql.append("FROM													\n");
		sql.append("		TB_MASTER a										\n");
		sql.append("LEFT OUTER JOIN											\n");
		sql.append("		TB_MEMBER b										\n");
		sql.append("ON														\n");
		sql.append("		a.MEM_SEQ_NO = b.MEM_SEQ_NO						\n");
		sql.append("LEFT OUTER JOIN											\n");
		sql.append("		TB_DEPART c										\n");
		sql.append("ON														\n");
		sql.append("		b.MEM_DEPART = c.DEPART_SEQ_NO					\n");
		sql.append("WHERE													\n");
		sql.append("		a.CRT_DATE >= CURDATE()							\n");
		sql.append("ORDER BY												\n");
		sql.append("		TODAY_TOTAL DESC								\n");
		sql.append("LIMIT 3													\n");
		
		return sql.toString();
	}
	/*
	 * 2015-05-11 ksy 힘내요 기부 상세화면 종료 기부천사 top3
	 */
	private String selectBeforeGiveTop(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																					\n");
		sql.append("		SUM(TODAY_TOTAL) AS CNT, MEM_PIC, MEM_NAME, a.MEM_SEQ_NO, DEPART_NAME			\n");
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
		sql.append("		a.CRT_DATE >= ?																	\n");
		sql.append("	AND	a.CRT_DATE <= ?																	\n");
		sql.append("GROUP BY																				\n");
		sql.append("		a.MEM_SEQ_NO																	\n");
		sql.append("ORDER BY																				\n");
		sql.append("		CNT DESC																		\n");
		sql.append("LIMIT 3																					\n");
		
		return sql.toString();
	}

	/*
	 * 2015-04-30 ksy 힘내라 기부 리스트
	 */
	public ArrayList<DonationVO> giveList(int pageNo, int rowSize, int searchType, String keyword)throws SQLException{
		int page = (pageNo -1)*rowSize;
		int cnt =0;
		ArrayList<DonationVO> list = new ArrayList<DonationVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		GIVE_SEQ_NO,"
						+ "	GIVE_TITLE,"	
						+ " GIVE_CONTENT,"
						+ " GIVE_PIC,"
						+ " GIVE_AIM,"
						+ " CRT_DATE,"
						+ " END_DATE				\n");
		sql.append("FROM							\n");
		sql.append("		TB_GIVE					\n");
		sql.append("WHERE							\n");
		sql.append("		GIVE_YN <>'Y'			\n");
		if(!keyword.isEmpty()){
			
			sql.append("	AND GIVE_TITLE = ?		\n"); //검색어가 입력이 되어 있을경우
		}
		sql.append("ORDER BY GIVE_SEQ_NO DESC		\n");
		sql.append("LIMIT ?,?						\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(!keyword.isEmpty())pstmt.setString(++cnt, keyword);
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				DonationVO vo = new DonationVO();
				vo.setGiveSeqNo(rs.getInt("GIVE_SEQ_NO"));
				vo.setTitle(rs.getString("GIVE_TITLE"));
				vo.setContent(rs.getString("GIVE_CONTENT"));
				vo.setPic(rs.getString("GIVE_PIC"));
				vo.setAim(rs.getInt("GIVE_AIM"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setEndDate(rs.getString("END_DATE"));
				
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("DonationDao giveList ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return list;
	}
	/*
	 * 2015-05-13 ksy 기부 총 개수
	 */
	public int giveCnt()throws SQLException{
		int result =0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT						\n");
		sql.append("		COUNT(*) AS CNT		\n");
		sql.append("FROM						\n");
		sql.append("		TB_GIVE				\n");
		sql.append("WHERE						\n");
		sql.append("		GIVE_YN <>'Y'		\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("CNT");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("GiveDao giveCnt 리스트 ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	/*
	 * 2015-04-30 ksy 힘내라 기부 등록
	 */
	public int insert(String giveTitle, String giveContent, String fileName, int stairs, String endDate)throws SQLException{
		int result =0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT	INTO				\n");
		sql.append("				TB_GIVE		\n");
		sql.append("(							\n");
		sql.append("				GIVE_TITLE,"
								+ "	GIVE_CONTENT,"
								+ " GIVE_AIM,"
								+ " GIVE_PIC,"
								+ " GIVE_RESULT,"
								+ " GIVE_YN,"
								+ " CRT_DATE,"
								+ " END_DATE	\n");
		sql.append(")							\n");
		sql.append("VALUES						\n");
		sql.append("(							\n");
		sql.append("				?,"
								+ "	?,"
								+ " ?,"
								+ " ?,"
								+ " 'N',"
								+ " 'N',"
								+ " NOW(),"
								+ " ?		\n");
		sql.append(")				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(++cnt, giveTitle);
			pstmt.setString(++cnt, giveContent);
			pstmt.setInt(++cnt, stairs);
			pstmt.setString(++cnt, fileName);
			pstmt.setString(++cnt, endDate);
			
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
			//////System.out.println("DonationDao insert ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
	
		return result;
	}
	
	/*
	 * 2015-05-13 ksy 힘내라 기부 상세화면
	 */
	public DonationVO giveView(int giveSeqNo)throws SQLException{
		DonationVO vo = new DonationVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT																					\n");
		sql.append("		GIVE_SEQ_NO, GIVE_TITLE, GIVE_CONTENT, CRT_DATE, END_DATE, GIVE_PIC, GIVE_AIM				\n");
		sql.append("FROM																					\n");
		sql.append("		TB_GIVE																			\n");
		sql.append("WHERE																					\n");
		sql.append("		GIVE_SEQ_NO = ?																	\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, giveSeqNo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setTitle(rs.getString("GIVE_TITLE"));
				vo.setAim(rs.getInt("GIVE_AIM"));
				vo.setPic(rs.getString("GIVE_PIC"));
				vo.setContent(rs.getString("GIVE_CONTENT"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setEndDate(rs.getString("END_DATE"));
				vo.setGiveSeqNo(rs.getInt("GIVE_SEQ_NO"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("GiveDao giveView 리스트 ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return vo;
	}
	/*
	 * 2015-05-14 ksy 힘내라 기부 수정
	 */
	public int updateGive(int giveSeqNo, String giveTitle, String giveContent, String endDate, int aim, String listImgName)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE								\n");
		sql.append("		TB_GIVE						\n");
		sql.append("SET									\n");
		sql.append("		GIVE_TITLE 	 = ?			\n");
		sql.append("	,	GIVE_CONTENT = ?			\n");
		sql.append("	,	END_DATE     = ?			\n");
		sql.append("	,	GIVE_AIM     = ?			\n");
		if(listImgName!=null&&listImgName != "")sql.append("	,	GIVE_PIC     = ?			\n");
		sql.append("WHERE								\n");
		sql.append("		GIVE_SEQ_NO = ?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, giveTitle);
			pstmt.setString(++cnt, giveContent);
			pstmt.setString(++cnt, endDate);
			pstmt.setInt(++cnt, aim);
			if(listImgName!=null&&listImgName != "")pstmt.setString(++cnt, listImgName);
			pstmt.setInt(++cnt, giveSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("GiveDao updateGive 리스트 ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
	/*
	 * 2015-05-14 ksy 힘내라 기부 삭제
	 */
	public int deleteGive(int giveSeqNo)throws SQLException{
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE						\n");
		sql.append("		TB_GIVE				\n");
		sql.append("SET							\n");
		sql.append("		GIVE_YN = 'Y'		\n");
		sql.append("WHERE						\n");
		sql.append("		GIVE_SEQ_NO = ?		\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, giveSeqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("GiveDao deleteGive 리스트 ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	
	/*
	 * 기부 이미지 한글 처리
	 */
	public int updateImage(String imgAddre, int seqNo)throws SQLException{
		int result = 0;
		int cnt = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE								\n");
		sql.append("		TB_GIVE						\n");
		sql.append("SET									\n");
		sql.append("		GIVE_PIC 	 = ?			\n");
		sql.append("WHERE								\n");
		sql.append("		GIVE_SEQ_NO = ?				\n");
		
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(++cnt, imgAddre);
			pstmt.setInt(++cnt, seqNo);
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("GiveDao 이미지 한글처리 ERROR : "+e); 
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
		
	}
	
	
}
