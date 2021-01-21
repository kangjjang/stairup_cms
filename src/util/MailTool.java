package util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;




/**
 * 클래스 설명글 작성   
 * 
 *@author : Kim Jae-Myoung ( jaeming.com@gmail.com ) 
 *@version : 1.0
 *@since : 2012. 4. 19.
 *update history 
 *-------------------------------------------------
 *@editor : 
 *@edit date : 
 *@edit content :
 *-------------------------------------------------
 */



public class MailTool {
 
	private static String EMAIL_HOST = "";
	private static String EMAIL_HOST_PORT = "25";
	private static String EMAIL_ID = "";
	private static String EMAIL_PW = "";
	
	public MailTool(){
		EMAIL_HOST = "127.0.0.1";
	}
 
	public MailTool(String host, String port, String id, String password ) {
		EMAIL_HOST = host;
		EMAIL_HOST_PORT = port;
		EMAIL_ID = id;
		EMAIL_PW = password;
	}

	public boolean sendEmailUseGMail(
	   String senderEmailAddr
	   , String senderName
	   , String receiverEmailAddr
	   , String receiverName
	   , String subject
	   , String content
	){
  
		boolean flag = false;
  
		try {
   
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.host", EMAIL_HOST);
			props.put("mail.smtp.port", EMAIL_HOST_PORT);
			props.put("mail.smtp.auth", "true");
               
   
			Authenticator auth = new SMTPAuthenticator();
			Session sess = Session.getDefaultInstance(props, auth);
			sess.setDebug(false);
      
			Message msg = new MimeMessage(sess);
   
			InternetAddress sendAddr = new InternetAddress(senderEmailAddr, MimeUtility.encodeText(senderName,"EUC-KR","B"));
			InternetAddress receiveAddr = new InternetAddress(receiverEmailAddr, receiverName);
			msg.setSentDate(new Date());
   
			msg.setFrom(sendAddr);
			msg.setRecipient(Message.RecipientType.TO, receiveAddr);
			msg.setSubject(MimeUtility.encodeText(subject, "EUC-KR", "B"));
			//msg.setText(content);
			msg.setContent(content,  "text/html; charset=EUC-KR");
   
			Transport.send(msg);
   
			flag = true;  
		} catch (Exception e) {
			e.printStackTrace();   
		} 
  
		return flag;
	}

	private class SMTPAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication(){
			return new PasswordAuthentication(EMAIL_ID, EMAIL_PW);
		}
	}
 
	public static void main(String[] args) {
		System.out.println(DateUtil.getTime("yyyy-MM-dd'T'HH:mm:ss.SSS"));
		// TODO Auto-generated method stub
		MailTool mail = new MailTool("smtp.gmail.com", "465", "neohamin@gmail.com", "g1029qpwo");
		mail.sendEmailUseGMail("neohamin@gmail.com", "하창민", "hamin70@naver.com" , "유타카", "subject", "content");
	}

}
