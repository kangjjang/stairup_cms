package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
	
	/*
	public static void main(String[] args) {
		HashUtil hs = new HashUtil();
		try {
			System.out.println(hs.encryptPassword("qwertasdfasdfasy@naver.com", "qwertyasd66665656977"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

	public static String encryptPassword(String id, String pw) throws Exception {

	    System.out.println(id);
        System.out.println(pw);

		StringBuffer message = new StringBuffer();
		StringBuffer encrypted = new StringBuffer();
		MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");

            message.append(id); 
            message.append(pw);
          
            md.update(message.toString().getBytes());
           
            byte[] mb = md.digest();
          
            for (int i = 0; i < mb.length; i++) {
                byte temp = mb[i];
              
                String s = Integer.toHexString(temp & 0xFF);
              
                while (s.length() < 2) {
                    s = "0" + s;
                  
                }
                s = s.substring(s.length() - 2);
             
                encrypted.append(s);
              
            }

        } catch (NoSuchAlgorithmException e) {        	
            
        }
        return encrypted.toString();
	}

}
