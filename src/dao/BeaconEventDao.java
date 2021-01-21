package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import util.ConnectionUtil;
import vo.*;

import com.mysql.jdbc.Statement;

public class BeaconEventDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public BeaconEventDao() throws SQLException {
		conn = ConnectionUtil.getConnection();
	}

	public void closeConn() throws SQLException {
		conn.close();
	}

	public static void closeAll(final Object... things) {
		for (final Object thing : things) {
			if (null != thing) {
				try {
					if (thing instanceof ResultSet) {
						try {
							((ResultSet) thing).close();
						} catch (final SQLException e) {
							/* No Op */
						}
					}
					if (thing instanceof Statement) {
						try {
							((Statement) thing).close();
						} catch (final SQLException e) {
							/* No Op */
						}
					}
					if (thing instanceof Connection) {
						try {
							((Connection) thing).close();
						} catch (final SQLException e) {
							/* No Op */
						}
					}

					if (thing instanceof Lock) {
						try {
							((Lock) thing).unlock();
						} catch (final IllegalMonitorStateException e) {
							/* No Op */
						}
					}
					if (thing instanceof PreparedStatement) {
						try {
							((PreparedStatement) thing).close();
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

	public int BeaconEventCnt() throws SQLException {
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT								\n");
		sql.append("		COUNT(*) AS CNT				\n");
		sql.append("FROM								\n");
		sql.append("		TB_BEACON_EVENT					\n");
		sql.append("WHERE								\n");
		sql.append("		DEL_YN <>'Y'			\n");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt("CNT");
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("MemberDao memberCnt ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
			/*
			 * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
			 * if(conn != null) conn.close();
			 */
		}

		return result;
	}

    public int stampEventCnt() throws SQLException {
        int result = 0;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT								\n");
        sql.append("		COUNT(*) AS CNT				\n");
        sql.append("FROM								\n");
        sql.append("		TB_STAMP_EVENT					\n");
        sql.append("WHERE								\n");
        sql.append("		DEL_YN <>'Y'			\n");

        try {
            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("CNT");
            }
        } catch (Exception e) {

            //////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
            //////System.out.println("MemberDao memberCnt ERROR : " + e);
        } finally {
            closeAll(rs, pstmt);
            /*
             * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
             * if(conn != null) conn.close();
             */
        }

        return result;
    }

	public ArrayList<BeaconEventVO> selectCmsBeaconEventList(int pageno, int rowSize, String searchType, String keyword)
			throws SQLException {
		ArrayList<BeaconEventVO> list = new ArrayList<BeaconEventVO>();
		int cnt = 0;
		int page = (pageno - 1) * rowSize;
		StringBuffer sql = new StringBuffer();
		sql.append(
				"SELECT																							\n");
		sql.append(
				"		SEQ_NO, BEACON_MAJOR, BEACON_MINOR, BEACON_LOCATION, BEACON_POSITION, BEACON_USE, CRT_DATE			\n");
		sql.append(
				"FROM																							\n");
		sql.append(
				"		TB_BEACON_EVENT																			\n");
		sql.append(
				"WHERE																							\n");
		sql.append(
				"		 DEL_YN <> 'Y'																			\n");
		if (searchType.equals("0")) {
			sql.append(
					"	AND BEACON_MAJOR LIKE CONCAT('%',?,'%')										 												\n");
		} else if (searchType.equals("1")) {
			sql.append(
					"	AND BEACON_MINOR LIKE CONCAT('%',?,'%')													\n");
		} else if (searchType.equals("2")) {
			sql.append(
					"	AND BEACON_POSITION LIKE CONCAT('%',?,'%')												\n");
		} else if (searchType.equals("3")) {
			sql.append(
					"	AND BEACON_USE LIKE CONCAT('%',?,'%')													\n");
		}
		sql.append(
				"ORDER BY																						\n");
		sql.append(
				"		BEACON_MAJOR DESC , BEACON_MINOR ASC																			\n");
		sql.append(
				"LIMIT	?,?																						\n");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			if (searchType != null && searchType != "") {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BeaconEventVO vo = new BeaconEventVO();
				vo.setSeqNo(rs.getInt("SEQ_NO"));
				vo.setBeaconMajor(rs.getInt("BEACON_Major"));
				vo.setBeaconMinor(rs.getInt("BEACON_Minor"));
				vo.setBeaconLocation(rs.getString("BEACON_LOCATION"));
				vo.setBeaconPosition(rs.getString("BEACON_POSITION"));
				vo.setBeaconUse(rs.getString("BEACON_USE"));
				vo.setCrtDate(rs.getString("CRT_DATE"));

				list.add(vo);
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("MemberDao selectCmsMemberList ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
			/*
			 * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
			 * if(conn != null) conn.close();
			 */
		}

		return list;
	}

    //skhero.kang 2019-04-02 스탬프 이벤트 리스트 가져오
    public ArrayList<StampEventVO> selectStampEventList(int pageno, int rowSize, String keyword)
            throws SQLException {

	    ArrayList<StampEventVO> list = new ArrayList<>();

	    int cnt = 0;
        int page = (pageno - 1) * rowSize;
        StringBuffer sql = new StringBuffer();

        sql.append(
                "SELECT																							\n");
        sql.append(
                "		SEQ_NO, STAMP_EVENT_NAME, STAMP_EVENT_REGION, STAMP_EVENT_ADDRESS, STAMP_EVENT_TARGET, STAMP_EVENT_COURSE_DISTANCE,			\n");
        sql.append(
                "		STAMP_EVENT_COURSE_TIME, STAMP_EVENT_START_DATE, STAMP_EVENT_END_DATE, STAMP_EVENT_IMAGE, STAMP_EVENT_COURSE_IMAGE, DEL_YN,			\n");
        sql.append(
                "		CRT_DATE, UPD_DATE			\n");
        sql.append(
                "FROM																							\n");
        sql.append(
                "		TB_STAMP_EVENT																			\n");
        sql.append(
                "WHERE																							\n");
        sql.append(
                "		 DEL_YN <> 'Y'																			\n");
        if (keyword != null && keyword.length() > 0) {
            sql.append(
                    "	AND STAMP_EVENT_NAME LIKE CONCAT('%',?,'%')										 												\n");
        }
        sql.append(
                "ORDER BY																						\n");
        sql.append(
                "		SEQ_NO DESC																\n");
        sql.append(
                "LIMIT	?,?																						\n");

        try {
            pstmt = conn.prepareStatement(sql.toString());

            if (keyword != null && keyword.length() > 0) {
                pstmt.setString(++cnt, keyword);
            }
            pstmt.setInt(++cnt, page);
            pstmt.setInt(++cnt, rowSize);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                StampEventVO vo = new StampEventVO();

                vo.setSeqNo(rs.getInt("SEQ_NO"));
                vo.setStampEventName(rs.getString("STAMP_EVENT_NAME"));
                vo.setStampEventRegion(rs.getString("STAMP_EVENT_REGION"));
                vo.setStampEventAddress(rs.getString("STAMP_EVENT_ADDRESS"));
                vo.setStampEventTarget(rs.getInt("STAMP_EVENT_TARGET"));
                vo.setStampEventCourseDistance(rs.getString("STAMP_EVENT_COURSE_DISTANCE"));
                vo.setStampEventCourseTime(rs.getString("STAMP_EVENT_COURSE_TIME"));
                vo.setStampEventStartDate(rs.getString("STAMP_EVENT_START_DATE"));
                vo.setStampEventEndDate(rs.getString("STAMP_EVENT_END_DATE"));
                vo.setStampEventImage(rs.getString("STAMP_EVENT_IMAGE"));
                vo.setStampEventCourseImage(rs.getString("STAMP_EVENT_COURSE_IMAGE"));
                vo.setDelYn(rs.getString("DEL_YN"));
                vo.setCrtDate(rs.getString("CRT_DATE"));
                vo.setUpdDate(rs.getString("UPD_DATE"));

                list.add(vo);
            }
        } catch (Exception e) {

            //////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
            //////System.out.println("MemberDao selectCmsMemberList ERROR : " + e);
        } finally {
            closeAll(rs, pstmt);
            /*
             * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
             * if(conn != null) conn.close();
             */
        }

        return list;
    }

    //skhero.kang 2019-04-05 스탬프 이벤트 비콘 리스트 가져오기
    public ArrayList<StampEventBeaconVO> selectStampEventBeaconList(int seq)
            throws SQLException {

        ArrayList<StampEventBeaconVO> list = new ArrayList<>();

        int cnt = 0;
        StringBuffer sql = new StringBuffer();

        sql.append(
                "SELECT																							\n");
        sql.append(
                "		SEQ_NO, STAMP_EVENT_SEQ, STAMP_EVENT_BEACON_MAJOR, STAMP_EVENT_BEACON_MINOR, CRT_DATE, UPD_DATE			\n");
        sql.append(
                "FROM																							\n");
        sql.append(
                "		TB_STAMP_EVENT_BEACON																			\n");
        sql.append(
                "WHERE																							\n");
        sql.append(
                "		STAMP_EVENT_SEQ = ?																			\n");
        sql.append(
                "ORDER BY																						\n");
        sql.append(
                "		SEQ_NO DESC																\n");

        try {
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(++cnt, seq);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                StampEventBeaconVO vo = new StampEventBeaconVO();

                vo.setSeqNo(rs.getInt("SEQ_NO"));
                vo.setStampEventSeq(rs.getInt("STAMP_EVENT_SEQ"));
                vo.setStampBeaconMajor(rs.getInt("STAMP_EVENT_BEACON_MAJOR"));
                vo.setStampBeaconMinor(rs.getInt("STAMP_EVENT_BEACON_MINOR"));
                vo.setCrtDate(rs.getString("CRT_DATE"));
                vo.setUpdDate(rs.getString("UPD_DATE"));

                list.add(vo);
            }
        } catch (Exception e) {


        } finally {
            closeAll(rs, pstmt);

        }

        return list;
    }

	/*
	 * CMS 비콘 등록
	 */
	public int insertBeaconEvent(int major, int minor, String beaconLocation, String beaconUse, String beaconPosition,
			String beaconUrl, String beaconContent, String img, String beaconTitle, String urlUse, int beaconSound,
			String logoImage, String beaconPopFromDateTime, String beaconPopToDateTime, int beaconPopCnt)
			throws SQLException {

		StringBuffer sql = new StringBuffer();
		int result = 0;
		sql.append("INSERT INTO TB_BEACON_EVENT ( 								\n");
		sql.append(
				"	BEACON_MAJOR, BEACON_MINOR, BEACON_LOCATION, BEACON_USE, BEACON_POSITION, BEACON_TITLE, BEACON_URL, BEACON_CONTENT, BEACON_IMAGE, CRT_DATE, DEL_YN, BEACON_URL_YN, BEACON_SOUND, LOGO_IMAGE, BEACON_POP_FROM_DATE, BEACON_POP_TO_DATE, BEACON_POP_CNT 		\n");
		sql.append(")	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, now(), 'N', ?, ?, ?, ?, ?, ?	 						\n");
		sql.append(" 	)																		\n");

		int cnt = 0;
		try {
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setInt(++cnt, major);
			pstmt.setInt(++cnt, minor);
			pstmt.setString(++cnt, beaconLocation);
			pstmt.setString(++cnt, beaconUse);
			pstmt.setString(++cnt, beaconPosition);
			pstmt.setString(++cnt, beaconTitle);
			pstmt.setString(++cnt, beaconUrl);
			pstmt.setString(++cnt, beaconContent);
			pstmt.setString(++cnt, img);
			pstmt.setString(++cnt, urlUse);
			pstmt.setInt(++cnt, beaconSound);
			pstmt.setString(++cnt, logoImage);
			pstmt.setString(++cnt, beaconPopFromDateTime);
			pstmt.setString(++cnt, beaconPopToDateTime);
			pstmt.setInt(++cnt, beaconPopCnt);

			result = pstmt.executeUpdate();
			//System.out.println(pstmt.toString());
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("BeaconEventDao insertBeaconEvent ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);

		}

		return result;
	}

    public int insertStampEvent(int stampEventTarget, String stampEventName, String stampEventRegion, String stampEventAddress, String stampEventCourseDistanc,
                                String stampEventCourseTime, String stampEventStartDate, String stampEventEndDate, String stampEventImage, String stampEventCourseImage)
            throws SQLException {

        StringBuffer sql = new StringBuffer();
        int result = 0;
        sql.append("INSERT INTO TB_STAMP_EVENT ( 								\n");
        sql.append("	STAMP_EVENT_NAME, STAMP_EVENT_REGION, STAMP_EVENT_ADDRESS, STAMP_EVENT_TARGET, STAMP_EVENT_COURSE_DISTANCE, 		\n");
        sql.append("	STAMP_EVENT_COURSE_TIME, STAMP_EVENT_START_DATE, STAMP_EVENT_END_DATE, STAMP_EVENT_IMAGE, STAMP_EVENT_COURSE_IMAGE,  		\n");
        sql.append("	DEL_YN, CRT_DATE, UPD_DATE 		\n");
        sql.append(") VALUES  (	\n");
        sql.append("     ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'N', now(), now()	 						\n");
        sql.append(")																		\n");

        int cnt = 0;
        try {
            pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(++cnt, stampEventName);
            pstmt.setString(++cnt, stampEventRegion);
            pstmt.setString(++cnt, stampEventAddress);
            pstmt.setInt(++cnt, stampEventTarget);
            pstmt.setString(++cnt, stampEventCourseDistanc);
            pstmt.setString(++cnt, stampEventCourseTime);
            pstmt.setString(++cnt, stampEventStartDate);
            pstmt.setString(++cnt, stampEventEndDate);
            pstmt.setString(++cnt, stampEventImage);
            pstmt.setString(++cnt, stampEventCourseImage);

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

        } catch (Exception e) {

        } finally {
            closeAll(rs, pstmt);

        }

        return result;
    }

    //skhero.kang 2019-04-05 stamp beacon 등록하는 함수 추가
    public int insertStampEventBeacon(int stampEventSeq, int stampEventBeaconMajor, int stampEventBeaconMinor)
            throws SQLException {

        StringBuffer sql = new StringBuffer();
        int result = 0;
        sql.append("INSERT INTO TB_STAMP_EVENT_BEACON  								\n");
        sql.append("( 								\n");
        sql.append("	STAMP_EVENT_SEQ, STAMP_EVENT_BEACON_MAJOR, STAMP_EVENT_BEACON_MINOR, CRT_DATE, UPD_DATE 		\n");
        sql.append(")	 						\n");
        sql.append("VALUES 	 						\n");
        sql.append("(	 						\n");
        sql.append("	?, ?, ?, now(), now()	 						\n");
        sql.append(" )																		\n");
        int cnt = 0;
        try {
            pstmt = conn.prepareStatement(sql.toString());

            pstmt.setInt(++cnt, stampEventSeq);
            pstmt.setInt(++cnt, stampEventBeaconMajor);
            pstmt.setInt(++cnt, stampEventBeaconMinor);

            result = pstmt.executeUpdate();

        } catch (Exception e) {

            //////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
            //////System.out.println("NoticeDao insert ERROR : " + e);
        } finally {
            closeAll(rs, pstmt);
        }

        return result;
    }

	/*
	 * 비콘 대량 등록
	 */
	public int insertBeaconEventAdmin(int major, int minor, String beaconLocation, String beaconPosition)
			throws SQLException {

		StringBuffer sql = new StringBuffer();
		int result = 0;
		sql.append("INSERT INTO TB_BEACON_EVENT ( 								\n");
		sql.append(
				"	BEACON_MAJOR, BEACON_MINOR, BEACON_LOCATION, BEACON_POSITION, BEACON_USE, BEACON_TITLE, BEACON_URL, BEACON_CONTENT, BEACON_IMAGE, CRT_DATE, DEL_YN, BEACON_URL_YN 		\n");
		sql.append(")	VALUES (?, ?, ?, ?, 'N', '', 'http://', '', '', now(), 'N', 'N'	 						\n");
		sql.append(" 	)																		\n");
		int cnt = 0;
		try {
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setInt(++cnt, major);
			pstmt.setInt(++cnt, minor);
			pstmt.setString(++cnt, beaconLocation);
			pstmt.setString(++cnt, beaconPosition);
			result = pstmt.executeUpdate();
			//System.out.println(pstmt.toString());
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("NoticeDao insert ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
		}

		return result;
	}

	public BeaconEventVO BeaconEventView(int beaconSeqNo) throws SQLException {
		StringBuffer sql = new StringBuffer();
		BeaconEventVO vo = new BeaconEventVO();
		sql.append(
				"SELECT																																																\n");
		sql.append(
				"		BEACON_MAJOR, BEACON_MINOR, BEACON_LOCATION, BEACON_USE, BEACON_POSITION, BEACON_TITLE, BEACON_IMAGE, BEACON_URL, BEACON_CONTENT, a.CRT_DATE, BEACON_URL_YN, SOUND_NAME, LOGO_IMAGE, b.SEQ_NO AS SOUND_SEQ, BEACON_POP_FROM_DATE, BEACON_POP_TO_DATE, BEACON_POP_CNT			\n");
		sql.append(
				"FROM																																																\n");
		sql.append(
				"		TB_BEACON_EVENT a																																											\n");
		sql.append(
				"LEFT OUTER JOIN																																														\n");
		sql.append(
				"		TB_BEACON_SOUND	b																																											\n");
		sql.append(
				"ON		a.BEACON_SOUND = b.SEQ_NO																																									\n");
		sql.append(
				"WHERE																																																\n");
		sql.append(
				"		a.SEQ_NO = ?																																												\n");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, beaconSeqNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				vo.setBeaconMajor(rs.getInt("BEACON_MAJOR"));
				vo.setBeaconMinor(rs.getInt("BEACON_MINOR"));
				vo.setBeaconLocation(rs.getString("BEACON_LOCATION"));
				vo.setBeaconUse(rs.getString("BEACON_USE"));
				vo.setBeaconPosition(rs.getString("BEACON_POSITION"));
				vo.setBeaconTitle(rs.getString("BEACON_TITLE"));
				vo.setBeaconImage(rs.getString("BEACON_IMAGE"));
				vo.setBeaconUrl(rs.getString("BEACON_URL"));
				vo.setBeaconContent(rs.getString("BEACON_CONTENT"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				vo.setBeaconUrlYN(rs.getString("BEACON_URL_YN"));
				vo.setBeaconSoundName(rs.getString("SOUND_NAME"));
				vo.setLogoImage(rs.getString("LOGO_IMAGE"));
				vo.setBeaconSoundSeq(rs.getInt("SOUND_SEQ"));
				vo.setBeaconPopFromDateTime(rs.getString("BEACON_POP_FROM_DATE"));
				vo.setBeaconPopToDateTime(rs.getString("BEACON_POP_TO_DATE"));
				vo.setBeaconPopCnt(rs.getInt("BEACON_POP_CNT"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt);

		}
		return vo;
	}

	//skhero.kang 2019-04-05
    public StampEventVO stampEventView(int seq) throws SQLException {
        StringBuffer sql = new StringBuffer();

        StampEventVO vo = new StampEventVO();

        sql.append("SELECT																																																\n");
        sql.append("	STAMP_EVENT_NAME, STAMP_EVENT_REGION, STAMP_EVENT_ADDRESS, STAMP_EVENT_TARGET, STAMP_EVENT_COURSE_DISTANCE, 		\n");
        sql.append("	STAMP_EVENT_COURSE_TIME, STAMP_EVENT_START_DATE, STAMP_EVENT_END_DATE, STAMP_EVENT_IMAGE, STAMP_EVENT_COURSE_IMAGE,  		\n");
        sql.append("	DEL_YN, CRT_DATE, UPD_DATE 		\n");
        sql.append("FROM																																																\n");
        sql.append("		TB_STAMP_EVENT 																																											\n");
        sql.append("WHERE																					\n");
        sql.append("		SEQ_NO = ?																																												\n");

        try {
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, seq);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                vo.setStampEventName(rs.getString("STAMP_EVENT_NAME"));
                vo.setStampEventRegion(rs.getString("STAMP_EVENT_REGION"));
                vo.setStampEventAddress(rs.getString("STAMP_EVENT_ADDRESS"));
                vo.setStampEventTarget(rs.getInt("STAMP_EVENT_TARGET"));
                vo.setStampEventCourseDistance(rs.getString("STAMP_EVENT_COURSE_DISTANCE"));
                vo.setStampEventCourseTime(rs.getString("STAMP_EVENT_COURSE_TIME"));
                vo.setStampEventStartDate(rs.getString("STAMP_EVENT_START_DATE"));
                vo.setStampEventEndDate(rs.getString("STAMP_EVENT_END_DATE"));
                vo.setStampEventImage(rs.getString("STAMP_EVENT_IMAGE"));
                vo.setStampEventCourseImage(rs.getString("STAMP_EVENT_COURSE_IMAGE"));
                vo.setCrtDate(rs.getString("CRT_DATE"));
                vo.setUpdDate(rs.getString("UPD_DATE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt);

        }
        return vo;
    }

	public int BeaconEventdelete(int beaconSeqNo) throws SQLException {
		int result = 0;
		StringBuffer sql = new StringBuffer();

		sql.append("UPDATE 							\n");
		sql.append("		TB_BEACON_EVENT				\n");
		sql.append("SET								\n");
		sql.append("		DEL_YN   = 'Y'			\n");
		sql.append("WHERE   SEQ_NO = ? 		\n");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, beaconSeqNo);

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("NoticeDao delete ERROR : " + e);

		} finally {
			closeAll(rs, pstmt);
			/*
			 * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
			 * if(conn != null) conn.close();
			 */
		}

		return result;
	}

	//skhero.kang 2019-04-05 stamp event 삭제
    public int deleteStampEvent(int seq) throws SQLException {
        int result = 0;
        StringBuffer sql = new StringBuffer();

        sql.append("UPDATE 							\n");
        sql.append("		TB_STAMP_EVENT				\n");
        sql.append("SET								\n");
        sql.append("		DEL_YN   = 'Y'			\n");
        sql.append("WHERE   SEQ_NO = ? 		\n");
        try {
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, seq);

            result = pstmt.executeUpdate();
        } catch (Exception e) {

        } finally {
            closeAll(rs, pstmt);
            /*
             * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
             * if(conn != null) conn.close();
             */
        }

        return result;
    }

    //skhero.kang 2019-04-05 stamp event 관련된 비콘 삭제
    public int deleteStampEventBeacon(int seq) throws SQLException {
        int result = 0;
        StringBuffer sql = new StringBuffer();

        sql.append("DELETE FROM 							\n");
        sql.append("		TB_STAMP_EVENT_BEACON				\n");
        sql.append("WHERE   STAMP_EVENT_SEQ = ? 		\n");
        try {
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, seq);

            result = pstmt.executeUpdate();
        } catch (Exception e) {

        } finally {
            closeAll(rs, pstmt);
        }

        return result;
    }

	/*
	 * 2016-08-10 CMS 비콘 대량 삭제
	 */

	public int BeaconAdmindelete(int beaconMajor) throws SQLException {
		int result = 0;
		StringBuffer sql = new StringBuffer();

		sql.append("DELETE FROM 							\n");
		sql.append("		TB_BEACON_EVENT					\n");
		sql.append("WHERE   BEACON_MAJOR = ? 				\n");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, beaconMajor);
			result = pstmt.executeUpdate();
			//System.out.println(pstmt.toString());
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("BeaconAdmindelete ERROR : " + e);

		} finally {
			closeAll(rs, pstmt);
			/*
			 * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
			 * if(conn != null) conn.close();
			 */
		}

		return result;
	}

	public int BeaconEventupdate(int no, int major, int minor, String beaconLocation, String beaconUse,
			String beaconPosition, String beaconUrl, String beaconContent, String imgAddre, String beaconTitle,
			String urlUse, int beaconSound, String logoImage, String beaconPopFromDateTime, String beaconPopToDateTime,
			int beaconPopCnt) throws SQLException {
		int result = 0;
		StringBuffer sql = new StringBuffer();

		sql.append("UPDATE TB_BEACON_EVENT  		\n");
		sql.append("SET   BEACON_MAJOR = ? 	\n");
		sql.append("	, BEACON_MINOR = ? 	\n");
		sql.append("	, BEACON_LOCATION = ? 	\n");
		sql.append("	, BEACON_USE = ? 	\n");
		sql.append("	, BEACON_POSITION = ? 	\n");
		sql.append("	, BEACON_URL = ?		\n");
		sql.append("	, BEACON_CONTENT = ?		\n");
		if (imgAddre.length() > 0) {
			sql.append("	, BEACON_IMAGE = ?		\n");
		}
		sql.append("	, BEACON_TITLE = ?		\n");
		sql.append("	, BEACON_URL_YN = ?		\n");
		sql.append("	, BEACON_SOUND = ?		\n");
		sql.append("	, LOGO_IMAGE = ?		\n");
		if (beaconPopFromDateTime.length() > 0) {
			sql.append("	, BEACON_POP_FROM_DATE = ?		\n");
		}
		if (beaconPopToDateTime.length() > 0) {
			sql.append("	, BEACON_POP_TO_DATE = ?		\n");
		}
		sql.append("	, BEACON_POP_CNT = ?		\n");
		sql.append("WHERE SEQ_NO = ? \n");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			int cnt = 0;
			pstmt.setInt(++cnt, major);
			pstmt.setInt(++cnt, minor);
			pstmt.setString(++cnt, beaconLocation);
			pstmt.setString(++cnt, beaconUse);
			pstmt.setString(++cnt, beaconPosition);
			pstmt.setString(++cnt, beaconUrl);
			pstmt.setString(++cnt, beaconContent);
			if (imgAddre.length() > 0) {
				pstmt.setString(++cnt, imgAddre);
			}
			pstmt.setString(++cnt, beaconTitle);
			pstmt.setString(++cnt, urlUse);
			pstmt.setInt(++cnt, beaconSound);
			pstmt.setString(++cnt, logoImage);
			if (beaconPopFromDateTime.length() > 0) {
				pstmt.setString(++cnt, beaconPopFromDateTime);
			}
			if (beaconPopToDateTime.length() > 0) {
				pstmt.setString(++cnt, beaconPopToDateTime);
			}
			pstmt.setInt(++cnt, beaconPopCnt);
			pstmt.setInt(++cnt, no);

			//System.out.println(pstmt.toString());

			result = pstmt.executeUpdate();

			if (result > 0) {
				result = no;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt);
		}

		return result;
	}

	//skhero.kang 2019-04-05 스탬프 이벤트 수정 처리
    public int updateStampEvent(int no, int stampEventTarget, String stampEventName, String stampEventRegion, String stampEventAddress, String stampEventCourseDistanc,
                                String stampEventCourseTime, String stampEventStartDate, String stampEventEndDate, String stampEventImage, String stampEventCourseImage) throws SQLException {
        int result = 0;
        int cnt = 0;

        StringBuffer sql = new StringBuffer();

        sql.append("UPDATE TB_STAMP_EVENT  		\n");
        sql.append("SET                     	\n");
        sql.append("	  STAMP_EVENT_NAME = ? 	\n");
        sql.append("	, STAMP_EVENT_REGION = ? 	\n");
        sql.append("	, STAMP_EVENT_ADDRESS = ? 	\n");
        sql.append("	, STAMP_EVENT_TARGET = ? 	\n");
        sql.append("	, STAMP_EVENT_COURSE_DISTANCE = ? 	\n");
        sql.append("	, STAMP_EVENT_COURSE_TIME = ? 	\n");
        sql.append("	, STAMP_EVENT_START_DATE = ? 	\n");
        sql.append("	, STAMP_EVENT_END_DATE = ? 	\n");

        if(stampEventImage != null && stampEventImage.length() > 0){
            sql.append("	, STAMP_EVENT_IMAGE = ? 	\n");
        }
        if(stampEventCourseImage != null && stampEventCourseImage.length() > 0){
            sql.append("	, STAMP_EVENT_COURSE_IMAGE = ? 	\n");
        }

        sql.append("	, UPD_DATE = now() 	\n");

        sql.append("WHERE SEQ_NO = ? \n");

        try {
            pstmt = conn.prepareStatement(sql.toString());

            pstmt.setString(++cnt, stampEventName);
            pstmt.setString(++cnt, stampEventRegion);
            pstmt.setString(++cnt, stampEventAddress);
            pstmt.setInt(++cnt, stampEventTarget);
            pstmt.setString(++cnt, stampEventCourseDistanc);
            pstmt.setString(++cnt, stampEventCourseTime);
            pstmt.setString(++cnt, stampEventStartDate);
            pstmt.setString(++cnt, stampEventEndDate);
            if(stampEventImage != null && stampEventImage.length() > 0) {
                pstmt.setString(++cnt, stampEventImage);
            }
            if(stampEventCourseImage != null && stampEventCourseImage.length() > 0) {
                pstmt.setString(++cnt, stampEventCourseImage);
            }
            pstmt.setInt(++cnt, no);

            result = pstmt.executeUpdate();

            if (result > 0) {
                result = no;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt);
        }

        return result;
    }

	/*
	 * 비콘의 팝업 여부 확인
	 */

	public BeaconEventVO selectBeaconEventUse(int major, int minor) throws SQLException {
		StringBuffer sql = new StringBuffer();
		BeaconEventVO vo = new BeaconEventVO();
		sql.append("SELECT							\n");
		sql.append("		BEACON_USE				\n");
		sql.append("FROM							\n");
		sql.append("		TB_BEACON_EVENT				\n");
		sql.append("WHERE							\n");
		sql.append("		BEACON_MAJOR = ?		\n");
		sql.append("AND		BEACON_MINOR = ?		\n");
		sql.append("AND		DEL_YN <> 'Y'		\n");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, major);
			pstmt.setInt(2, minor);
			rs = pstmt.executeQuery();
			/* //System.out.println(pstmt.toString()); */
			if (rs.next()) {
				vo.setBeaconUse(rs.getString("BEACON_USE"));
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("beaconEvent beaconTransfer.jsp ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
			/*
			 * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
			 * if(conn != null) conn.close();
			 */
		}
		return vo;
	}

	public BeaconEventVO selectBeaconEvent(int major, int minor) throws SQLException {
		StringBuffer sql = new StringBuffer();
		BeaconEventVO vo = new BeaconEventVO();
		sql.append(
				"SELECT																																\n");
		sql.append(
				"		BEACON_USE, BEACON_IMAGE, BEACON_URL, BEACON_CONTENT, BEACON_TITLE, BEACON_URL_YN, a.BEACON_SOUND AS SOUND_SEQ, b.BEACON_SOUND, LOGO_IMAGE, BEACON_POP_CNT			\n");
		sql.append(
				"FROM																																\n");
		sql.append(
				"		TB_BEACON_EVENT a																											\n");
		sql.append(
				"LEFT OUTER JOIN																															\n");
		sql.append(
				"		TB_BEACON_SOUND b																											\n");
		sql.append(
				"ON	a.BEACON_SOUND = b.SEQ_NO																										\n");
		sql.append("WHERE							\n");
		sql.append("		BEACON_MAJOR = ?		\n");
		sql.append("AND		BEACON_MINOR = ?		\n");
		sql.append("AND		a.DEL_YN <> 'Y'		\n");
		sql.append("AND		a.BEACON_POP_FROM_DATE <= NOW()		\n");
		sql.append("AND		a.BEACON_POP_TO_DATE >= NOW()		\n");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, major);
			pstmt.setInt(2, minor);
			rs = pstmt.executeQuery();
			//System.out.println(pstmt.toString());
			if (rs.next()) {
				vo.setBeaconUse(rs.getString("BEACON_USE"));
				vo.setBeaconImage(rs.getString("BEACON_IMAGE"));
				vo.setBeaconUrl(rs.getString("BEACON_URL"));
				vo.setBeaconContent(rs.getString("BEACON_CONTENT"));
				vo.setBeaconTitle(rs.getString("BEACON_TITLE"));
				vo.setBeaconUrlYN(rs.getString("BEACON_URL_YN"));
				vo.setBeaconSoundSeq(rs.getInt("SOUND_SEQ"));
				vo.setSoundUrl(rs.getString("BEACON_SOUND"));
				vo.setLogoImage(rs.getString("LOGO_IMAGE"));
				vo.setBeaconPopCnt(rs.getInt("BEACON_POP_CNT"));
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("NoticeDao noticeView ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
			/*
			 * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
			 * if(conn != null) conn.close();
			 */
		}
		return vo;
	}

	public BeaconEventVO selectResult(int memSeqNo, int major, int minor) {
		BeaconEventVO vo = new BeaconEventVO();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT							\n");
		sql.append("		MEMBER_SEQ				\n");
		sql.append("FROM							\n");
		sql.append("		TB_BEACON_EVENT_LOG				\n");
		sql.append("WHERE							\n");
		sql.append("		MEMBER_SEQ = ?			\n");
		sql.append("AND		MAJOR = ?			\n");
		sql.append("AND		MINOR = ?			\n");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, major);
			pstmt.setInt(3, minor);
			rs = pstmt.executeQuery();
			/* //////System.out.println("pstmt.tostring : "+pstmt.toString()); */
			if (rs.next()) {
				vo.setMemberSeq(rs.getInt("MEMBER_SEQ"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt);
		}
		return vo;
	}

	/*
	 * GCM 발송 기록
	 */

	public int insertEventLog(int memSeqNo, int major, int minor) throws SQLException {

		StringBuffer sql = new StringBuffer();
		int result = 0;
		sql.append("INSERT INTO TB_BEACON_EVENT_LOG ( 								\n");
		sql.append("	MEMBER_SEQ, MAJOR, MINOR, TRANSFER_TIME, CRT_DATE		\n");
		sql.append(")	VALUES (?, ?, ?, now(), now()	 						\n");
		sql.append(" 	)													\n");

		int cnt = 0;
		try {
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setInt(++cnt, memSeqNo);
			pstmt.setInt(++cnt, major);
			pstmt.setInt(++cnt, minor);

			result = pstmt.executeUpdate();
			// //System.out.println(pstmt.toString());
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("NoticeDao insert ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
			/*
			 * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
			 * if(conn != null) conn.close();
			 */
		}

		return result;
	}

	public int updateEventLog(int memSeqNo, int major, int minor) throws SQLException {
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE							\n");
		sql.append("		TB_BEACON_EVENT_LOG				\n");
		sql.append("SET								\n");
		sql.append("		TRANSFER_TIME = NOW()			\n");
		sql.append("WHERE							\n");
		sql.append("		MEMBER_SEQ = ?			\n");
		sql.append("AND		MAJOR = ?			\n");
		sql.append("AND		MINOR = ?			\n");
		try {
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setInt(1, memSeqNo);
			pstmt.setInt(2, major);
			pstmt.setInt(3, minor);
			result = pstmt.executeUpdate();
			//////System.out.println("pstmt : " + pstmt.toString());
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("MemberDao memberUpdate ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
			/*
			 * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
			 * if(conn != null) conn.close();
			 */
		}

		return result;
	}

	public int selectTransferTime(String tranferTime) {

		int result = 0;
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT							\n");
		sql.append("TIMESTAMPDIFF(MINUTE, ?,  NOW()) TRANSTIME							\n"); // 발송
																								// 기록이
																								// 있으면

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, tranferTime);

			//System.out.println(pstmt);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt("TRANSTIME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt);
		}
		return result;
	}

	public String selectTransTime(int major, int minor, int memberSeq) {

		String result = "";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT										\n");
		sql.append("	IFNULL(TRANSFER_TIME,'') AS TRANSFER_TIME							\n");
		sql.append("FROM										\n");
		sql.append("	TB_BEACON_EVENT_LOG						\n");
		sql.append("WHERE										\n");
		sql.append("	MAJOR = ?								\n");
		sql.append("AND MINOR = ?								\n");

		if (memberSeq > 0) {
			sql.append("AND MEMBER_SEQ = ?							\n");
		}
		sql.append("ORDER BY SEQ_NO DESC LIMIT 1				\n");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, major);
			pstmt.setInt(2, minor);
			if (memberSeq > 0) {
				pstmt.setInt(3, memberSeq);
			}

			//System.out.println(pstmt);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getString("TRANSFER_TIME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt);
		}
		return result;
	}

	/*
	 * 2016-11-15 팝업 사운드 리스트
	 */
	public ArrayList<BeaconSoundVO> selectCmsBeaconSoundtList(int pageno, int rowSize, String searchType,
			String keyword) throws SQLException {
		ArrayList<BeaconSoundVO> list = new ArrayList<BeaconSoundVO>();
		int cnt = 0;
		int page = (pageno - 1) * rowSize;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT															\n");
		sql.append("		SEQ_NO, SOUND_NAME, SOUND_LOCATION, CRT_DATE			\n");
		sql.append("FROM															\n");
		sql.append("		TB_BEACON_SOUND											\n");
		sql.append("WHERE															\n");
		sql.append("		 DEL_YN <> 'Y'											\n");
		if (searchType.equals("1")) {
			sql.append("	AND SOUND_NAME LIKE CONCAT('%',?,'%')					\n");
		} else if (searchType.equals("2")) {
			sql.append("	AND SOUND_LOCATION LIKE CONCAT('%',?,'%')					\n");
		}
		sql.append("ORDER BY														\n");
		sql.append("		SEQ_NO DESC 											\n");
		sql.append("LIMIT	?,?														\n");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			if (searchType != null && searchType != "") {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, page);
			pstmt.setInt(++cnt, rowSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BeaconSoundVO vo = new BeaconSoundVO();
				vo.setSeqNo(rs.getInt("SEQ_NO"));
				vo.setSoundName(rs.getString("SOUND_NAME"));
				vo.setSoundLocation(rs.getString("SOUND_LOCATION"));
				vo.setCrtDate(rs.getString("CRT_DATE"));

				list.add(vo);
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("BeaconEventDao selectCmsBeaconSoundtList ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
			/*
			 * if(rs != null) rs.close(); if(pstmt != null) pstmt.close();
			 * if(conn != null) conn.close();
			 */
		}

		return list;
	}

	/*
	 * 2016-11-15 CMS 팝업 사운드 총 수량
	 */
	public int selectCmsBeaconSoundtListCnt(String searchType, String keyword) throws SQLException {
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT															\n");
		sql.append("		COUNT(*) AS CNT											\n");
		sql.append("FROM															\n");
		sql.append("		TB_BEACON_SOUND											\n");
		sql.append("WHERE															\n");
		sql.append("		 DEL_YN <> 'Y'											\n");
		if (searchType.equals("1")) {
			sql.append("	AND SOUND_NAME LIKE CONCAT('%',?,'%')					\n");
		} else if (searchType.equals("2")) {
			sql.append("	AND SOUND_LOCATION LIKE CONCAT('%',?,'%')					\n");
		}

		try {
			pstmt = conn.prepareStatement(sql.toString());
			if (searchType != null && searchType != "") {
				pstmt.setString(1, keyword);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt("CNT");
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("BeaconEventDao selectCmsBeaconSoundtListCnt ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
		}

		return result;
	}

	/*
	 * 2016-11-15 팝업 사운드 등록
	 */
	public int insertBeaconSound(String soundName, String soundLocation, String beaconSound) throws SQLException {
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO															\n");
		sql.append("			TB_BEACON_SOUND											\n");
		sql.append("(																	\n");
		sql.append("	SOUND_NAME, SOUND_LOCATION, BEACON_SOUND, DEL_YN, CRT_DATE		\n");
		sql.append(")																	\n");
		sql.append("VALUES(																\n");
		sql.append("	?, ?, ?, 'N', NOW()												\n");
		sql.append(")																	\n");

		try {
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, soundName);
			pstmt.setString(2, soundLocation);
			pstmt.setString(3, beaconSound);
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
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("BeaconEventDao insertBeaconSound ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
		}

		return result;
	}

	/*
	 * 2016-11-15 팝업 사운드 리스트
	 */
	public ArrayList<BeaconSoundVO> selectBoxBeaconSoundList() throws SQLException {
		ArrayList<BeaconSoundVO> list = new ArrayList<BeaconSoundVO>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT															\n");
		sql.append("		SEQ_NO, SOUND_NAME, SOUND_LOCATION, CRT_DATE			\n");
		sql.append("FROM															\n");
		sql.append("		TB_BEACON_SOUND											\n");
		sql.append("WHERE															\n");
		sql.append("		 DEL_YN <> 'Y'											\n");
		sql.append("ORDER BY														\n");
		sql.append("		SEQ_NO DESC 											\n");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BeaconSoundVO vo = new BeaconSoundVO();
				vo.setSeqNo(rs.getInt("SEQ_NO"));
				vo.setSoundName(rs.getString("SOUND_NAME"));
				vo.setSoundLocation(rs.getString("SOUND_LOCATION"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				list.add(vo);
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("BeaconEventDao selectBoxBeaconSoundList ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
		}

		return list;
	}

	/*
	 * 2016-11-15 팝업 사운드 리스트
	 */
	public ArrayList<AffiliationVO> selectAffiliationLogo() throws SQLException {
		ArrayList<AffiliationVO> list = new ArrayList<AffiliationVO>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT															\n");
		sql.append("		*														\n");
		sql.append("FROM															\n");
		sql.append("		TB_AFFILIATION											\n");
		sql.append("WHERE															\n");
		sql.append("		 DEL_YN <> 'Y'											\n");
		sql.append("ORDER BY														\n");
		sql.append("		SEQ_NO DESC 											\n");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				AffiliationVO vo = new AffiliationVO();
				vo.setSeqNo(rs.getInt("SEQ_NO"));
				vo.setAffiliationName(rs.getString("AFFILIATION_NAME"));
				vo.setAffiliationPic(rs.getString("AFFILIATION_PIC"));
				vo.setCrtDate(rs.getString("CRT_DATE"));
				list.add(vo);
			}
		} catch (Exception e) {
			
			//////System.out.println("에러 발생 시간 : " + new Timestamp(startTime));
			//////System.out.println("BeaconEventDao selectAffiliationLogo ERROR : " + e);
		} finally {
			closeAll(rs, pstmt);
		}

		return list;
	}
	
	public MemberVO selectBeaconEventLogTopMember(int beaconMajor, int beaconMinor) throws SQLException {
		
		StringBuffer sql = new StringBuffer();
		MemberVO vo = new MemberVO();
		
		sql.append(
				"SELECT																																																\n");
		sql.append(
				"		b.MEM_SEQ_NO, b.MEM_NAME		\n");
		sql.append(
				"FROM																																																\n");
		sql.append(
				"		TB_BEACON_EVENT_LOG a																																											\n");
		sql.append(
				"INNER JOIN																																														\n");
		sql.append(
				"		TB_MEMBER b																																											\n");
		sql.append(
				"ON		a.MEMBER_SEQ = b.MEM_SEQ_NO																																									\n");
		sql.append(
				"WHERE																																																\n");
		sql.append(
				"		a.MAJOR = ?																																												\n");
		sql.append(
				"AND																																																\n");
		sql.append(
				"		a.MINOR = ?																																												\n");
		sql.append(
				"	LIMIT 1																																																\n");
			
		try {
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setInt(1, beaconMajor);
			pstmt.setInt(2, beaconMinor);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				vo.setMemSeqNo(rs.getInt("MEM_SEQ_NO"));
				vo.setMemName(rs.getString("MEM_NAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt);

		}
		return vo;
	}
	
	/*
	 * 2016-08-10 CMS 비콘 대량 삭제
	 */

	public int BeaconEventLogDelete(int beaconMajor, int beaconMinor) throws SQLException {
		int result = 0;
		StringBuffer sql = new StringBuffer();

		sql.append("DELETE FROM 							\n");
		sql.append("		TB_BEACON_EVENT_LOG					\n");
		sql.append("WHERE   MAJOR = ? 				\n");
		sql.append("AND   MINOR = ? 				\n");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setInt(1, beaconMajor);
			pstmt.setInt(2, beaconMinor);
			
			//System.out.println(pstmt);
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			closeAll(rs, pstmt);
		}

		return result;
	}

}
