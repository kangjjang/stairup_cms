package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONObject;

public class FcmPushService {

	// Method to send Notifications from server to client end.

	//public final static String AUTH_KEY_FCM = "AIzaSyDjnvZZeKKakFvwN2cbmsxwY7lhUFisTq0";
    public final static String AUTH_KEY_FCM = "AAAA0OUxFKE:APA91bGyh16Bwg-9Yp0ebXHjeFYaiD2dFXz9oDRCPjBvFXM-OlADcSDL5qHRpTK3zZMa_CrDKaq3RyP9Y-m3zl-VCm__JyPddJt8mLfIAo4ZRr9uYVMWrsYXCTdDdUumYvBmMAPXq9B2";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

	// userDeviceIdKey is the device id you will query from your database

	public static void pushFCMNotification(String title, String msg, ArrayList<String> registration_ids, int flag, int flagSeq) throws Exception {
		String authKey = AUTH_KEY_FCM; // You FCM AUTH key
		String FMCurl = API_URL_FCM;

		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + authKey);
		conn.setRequestProperty("Content-Type", "application/json");

		JSONObject json = new JSONObject();
		JSONObject info = new JSONObject();
		
		
		info.put("title", title);
		info.put("body", msg);
		info.put("sound", "default");
		info.put("click_action", "FCM_PLUGIN_ACTIVITY");
		info.put("icon", "icon");
		
		
		//데이터 전달하기
		JSONObject dataJson = new JSONObject();
		dataJson.put("flag", String.valueOf(flag));  //1:후드필터점검   2:환기필터점검  3:인테리어 댓글   4:레시피댓글  5:공기가 궁금해요 댓글  6:1:1문의 댓글   7:일반푸시 
		dataJson.put("flagSeq", String.valueOf(flagSeq));  //후드 필터일 경우 해당 myPrdSeq 인테리어/레시피/공기가궁금/1:1 문의일 경우 게시글Seq
		json.put("data", dataJson);
		
		
		json.put("notification", info);
		json.put("registration_ids", registration_ids); // deviceID
		json.put("priority", "high");
		
		
		try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
			// 혹시나 한글 깨짐이 발생하면
			// try(OutputStreamWriter wr = new
			// OutputStreamWriter(conn.getOutputStream(), "UTF-8")){ 인코딩을 변경해준다.

			wr.write(json.toString());
			wr.flush();
		} catch (Exception e) {
		}

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;
		//System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();
	}
	
	public static void pushFCMNotification(String title, String msg, String topic, int msgFlag) throws Exception {
		String authKey = AUTH_KEY_FCM; // You FCM AUTH key
		String FMCurl = API_URL_FCM;

		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + authKey);
		conn.setRequestProperty("Content-Type", "application/json");

		JSONObject json = new JSONObject();
		
		//데이터 전달하기
		JSONObject dataJson = new JSONObject();
		dataJson.put("title", title);
		dataJson.put("message", msg);
		dataJson.put("msgFlag", msgFlag);
		
		json.put("data", dataJson);
		json.put("to", topic); 
		json.put("priority", "high");
		
		try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
			// 혹시나 한글 깨짐이 발생하면
			// try(OutputStreamWriter wr = new
			// OutputStreamWriter(conn.getOutputStream(), "UTF-8")){ 인코딩을 변경해준다.

			wr.write(json.toString());
			wr.flush();
		} catch (Exception e) {
		}

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;
		//System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();
	}
	
	
	public static void pushFCMNotification(String title, String msg, int msgFlag, ArrayList<String> registration_ids, int spotType, 
			int isPop, String popTitle, String popContent, String popImage, String popSound) throws Exception {
		String authKey = AUTH_KEY_FCM; // You FCM AUTH key
		String FMCurl = API_URL_FCM;

		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + authKey);
		conn.setRequestProperty("Content-Type", "application/json");

		JSONObject json = new JSONObject();
		
		//데이터 전달하기
		JSONObject dataJson = new JSONObject();
		dataJson.put("title", title);
		dataJson.put("message", msg);
		dataJson.put("msgFlag", msgFlag);
		dataJson.put("spotType", spotType);
		dataJson.put("isPop", isPop);
		dataJson.put("popTitle", popTitle);
		dataJson.put("popContent", popContent);
		dataJson.put("popImage", popImage);
		dataJson.put("popSound", popSound);
		
		json.put("data", dataJson);
		json.put("registration_ids", registration_ids); 
		
		try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
			// 혹시나 한글 깨짐이 발생하면
			// try(OutputStreamWriter wr = new
			// OutputStreamWriter(conn.getOutputStream(), "UTF-8")){ 인코딩을 변경해준다.

			wr.write(json.toString());
			wr.flush();
		} catch (Exception e) {
		}

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;
		//System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();
	}

	

}
