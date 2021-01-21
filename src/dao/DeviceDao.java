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
import vo.DeviceVO;

public class DeviceDao {
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public DeviceDao() throws SQLException{
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
	
	// 앱 최초실행시 디바이스 토큰 저장하기
	public int insertDeviceToken(String dvcToken, String dvcMdlName, String dvcOsGbn) throws SQLException{
		StringBuffer sql = new StringBuffer();
		//conn = ConnectionUtil.getConnection();	//열려잇는 connection
		int result=0;
		sql.append("INSERT INTO TB_DEVICE( DVC_TOKEN, DVC_MDL_NAME, DVC_OS_GBN, CRT_DATE, CHG_DATE) 	\n");
		sql.append("	   VALUES( ?, ?, ?, now(), now()) 										\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(2, dvcToken);
			pstmt.setString(3, dvcMdlName);
			pstmt.setString(4, dvcOsGbn);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao loginMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	
	// 특정 아이디를 가지고 다른 여러 폰에서 접속을 했을 경우에 토큰 업데이트
	public int updateDeviceToken(int memSeqNo, String dvcToken, String dvcMdlName, String dvcOsGbn) throws SQLException{
		StringBuffer sql = new StringBuffer();
	//	conn = ConnectionUtil.getConnection();
		int result=0;
		sql.append("UPDATE TB_DEVICE																						\n");
		sql.append("SET DVC_TOKEN = ? , DVC_OS_GBN = ? , CHG_DATE = now() \n");
		sql.append("WHERE DVC_SEQ_NO = ?  	\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, dvcToken);
			pstmt.setString(2, dvcMdlName);
			pstmt.setString(3, dvcOsGbn);
			pstmt.setInt(4, memSeqNo);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao loginMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}		
		return result;
	}
	
	// 특정 디바이트 토큰에 대해서 매칭되는 사용자를 업데이트 한다.
	public int updateDeviceToken2(int memSeqNo, String dvcToken, String dvcMdlName, String dvcOsGbn) throws SQLException{
		StringBuffer sql = new StringBuffer();
		//conn = ConnectionUtil.getConnection();
		int result=0;
		sql.append("UPDATE TB_DEVICE																						\n");
		sql.append("SET DVC_SEQ_NO = ? , CHG_DATE = now() \n");
		sql.append("WHERE DVC_TOKEN= ?  	\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setString(2, dvcToken);
			
			result = pstmt.executeUpdate();
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao loginMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return result;
	}
	
	public int cntDeviceToken(int memSeqNo, String dvcToken) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*)	cnt									\n");
		sql.append("FROM TB_DEVICE								\n");
		sql.append("WHERE DVC_SEQ_NO = ?																		\n");
		sql.append("AND DVC_TOKEN = ?																		\n");
	//	conn = ConnectionUtil.getConnection();
		int result=0;
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			pstmt.setString(2, dvcToken);
			
			rs = pstmt.executeQuery();
			result = 0;
			if(rs.next()){
				result = rs.getInt("cnt");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao loginMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return result;
	}
	
	public int cntDeviceToken(int memSeqNo) throws SQLException{
		StringBuffer sql = new StringBuffer();
		int result=0;
		sql.append("SELECT COUNT(*)	cnt									\n");
		sql.append("FROM TB_DEVICE								\n");
		sql.append("WHERE DVC_SEQ_NO = ?																		\n");
	//	conn = ConnectionUtil.getConnection();
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memSeqNo);
			
			rs = pstmt.executeQuery();
			result = 0;
			if(rs.next()){
				result = rs.getInt("cnt");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao loginMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return result;
	}
	
	public int cntDeviceToken(String dvcToken) throws SQLException{
		StringBuffer sql = new StringBuffer();
		int result=0;
		sql.append("SELECT COUNT(*)	cnt									\n");
		sql.append("FROM TB_DEVICE								\n");
		sql.append("WHERE DVC_TOKEN = ?																		\n");
	//	conn = ConnectionUtil.getConnection();
		try{
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, dvcToken);
			
			rs = pstmt.executeQuery();
			result = 0;
			if(rs.next()){
				result = rs.getInt("cnt");
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao loginMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		
		return result;
	}
	
	public DeviceVO selectDevice(int memSeqNo) throws SQLException{
		
		DeviceVO vo = new DeviceVO();
	//	conn = ConnectionUtil.getConnection();	//이미 열려있음
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DVC_TOKEN, DVC_OS	\n");
		sql.append("FROM TB_MEMBER								\n");
		sql.append("WHERE MEM_SEQ_NO = ?						\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			if(memSeqNo != 0){
				pstmt.setInt(1, memSeqNo);
			}
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				vo.setDeviceToken(rs.getString("DVC_TOKEN"));
				vo.setDeviceOsGbn(rs.getString("DVC_OS"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao loginMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}
		return vo;
	}
	
	public ArrayList<DeviceVO> selectDevice() throws SQLException{
		ArrayList<DeviceVO> list = new ArrayList<DeviceVO>();
		//conn = ConnectionUtil.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DVC_TOKEN, DVC_OS_GBN	\n");
		sql.append("FROM TB_DEVICE								\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				DeviceVO vo = new DeviceVO();
			
				vo.setDeviceToken(rs.getString("DVC_TOKEN"));
				vo.setDeviceOsGbn(rs.getString("DVC_OS_GBN"));
				
				list.add(vo);
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao loginMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}		
		return list;
	}
	public ArrayList<String> selectDeviceSeq() throws SQLException{
		//conn = ConnectionUtil.getConnection();
		ArrayList<String> list = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DVC_SEQ_NO	\n");
		sql.append("FROM TB_DEVICE								\n");
		try{
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("DVC_SEQ_NO"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao loginMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}		
		return list;
	}
	public void getDeviceToken(ArrayList<String> regidAndroid, ArrayList<String> regidIos) throws SQLException{
		ArrayList<DeviceVO> list = null;
		
			//TB_DEVICE테이블을 뒤져서 해당 ID의 token과 기기정보를 가져온다.
			DeviceDao daoDevice = new DeviceDao();	//열려있는 connection
			list = daoDevice.selectDevice();
		//만약 해당 기기정보가 안드로이드라면
		////System.out.println(list.size());
		for(int j=0; j<list.size(); j++){
			if(list.get(j).getDeviceOsGbn().equals("A")){
				if(!regidAndroid.contains(list.get(j).getDeviceToken())){
					regidAndroid.add(list.get(j).getDeviceToken());
				}
			}else{ //iOS라면 
				if(!regidIos.contains(list.get(j).getDeviceToken())){
					regidIos.add(list.get(j).getDeviceToken());
				}
			}
		}
	}
	public DeviceVO getDeviceToken(int sendToId/*, ArrayList<String> regidAndroid, ArrayList<String> regidIos*/) throws SQLException{
		DeviceVO list = null;
		if(sendToId!=0){
			//TB_DEVICE테이블을 뒤져서 해당 ID의 token과 기기정보를 가져온다.
			DeviceDao daoDevice = new DeviceDao();	//이미 열려있는 connection
			list =	daoDevice.selectDevice(sendToId);
		}else{
			//////System.out.println("fail");
		}
		//만약 해당 기기정보가 안드로이드라면
		////System.out.println(list.size());
	/*
			if(list.getDeviceOsGbn().equals("A")){
				if(!regidAndroid.contains(list.getDeviceToken())){
					regidAndroid.add(list.getDeviceToken());
				}
			}else{ //iOS라면 
				if(!regidIos.contains(list.getDeviceToken())){
					regidIos.add(list.getDeviceToken());
				}
			}*/
		return list;
	}

	public ArrayList<String> getDeviceToken(String sendToId) throws SQLException{
		ArrayList<String> list = new ArrayList<String>();
	//	conn = ConnectionUtil.getConnection();	
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT DVC_TOKEN \n");
		sql.append("FROM TB_DEVICE								\n");
		sql.append("WHERE 1 = 1					\n");
		if(sendToId != null && !sendToId.equals("")){
			sql.append("AND DVC_SEQ_NO IN ( ? )					\n");
		}	
		
		////////System.out.println("sql : " + sql);
		try{
			pstmt = conn.prepareStatement(sql.toString());
			
			if(sendToId != null && !sendToId.equals("")){
				pstmt.setString(1, sendToId);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				list.add(rs.getString("DVC_TOKEN"));
			}
		}catch(Exception e){
			
			//////System.out.println("에러 발생 시간 : " +new Timestamp(startTime));
			//////System.out.println("AdminDao loginMember ERROR : "+e);
		}finally{
			closeAll(rs, pstmt);
			/*if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();*/
		}		
		return list;
	}
	
	//모든 사용자에게
	/*
	public void getDeviceToken(String sendToId, ArrayList<String> regidAndroid, ArrayList<String> regidIos) throws SQLException{
		ArrayList<DeviceVO> list = null;
		if(sendToId != null && !sendToId.equals("")){
			String [] arrSendToId = sendToId.split(",");
			
			for(int i=0; i < arrSendToId.length; i++){
				//TB_DEVICE테이블을 뒤져서 해당 ID의 token과 기기정보를 가져온다.
				DeviceDao daoDevice = new DeviceDao();
				list = daoDevice.selectDevice(arrSendToId[i],customerType);
			}
		}else{
			DeviceDao daoDevice = new DeviceDao();
			list = daoDevice.selectDevice(null,customerType);
		}
		
		//만약 해당 기기정보가 안드로이드라면
		////System.out.println(list.size());
		for(int j=0; j<list.size(); j++){
			if(list.get(j).getDeviceOsGbn().equals("A")){
				regidAndroid.add(list.get(j).getDeviceToken());
			}else{ //iOS라면 
				regidIos.add(list.get(j).getDeviceToken());
			}
		}
	}
	*/

    public String selectAppVersion(String deviceGbn) throws SQLException{
        String result = "";

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT									\n");
        sql.append("		APP_VERSION					\n");
        sql.append("FROM									\n");
        sql.append("		TB_APP_VERSION						\n");
        sql.append("WHERE 				\n");
        sql.append("        DEVICE_GBN = ?  			\n");

        try{
            pstmt = conn.prepareStatement(sql.toString());

            pstmt.setString(1, deviceGbn);
            rs = pstmt.executeQuery();

            if(rs.next()){
                result = rs.getString("APP_VERSION");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeAll(rs, pstmt);
        }
        return result;
    }

}
