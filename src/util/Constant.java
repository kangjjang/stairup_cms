package util;

public class Constant {
	//public static final String HOST_URL = "http://jerry39391.cafe24.com/upload/gcm/";
		public static final String HOST_URL = "http://jerry39394.cafe24.com/upload/gcm/";
	/*메뉴 구분*/
	public static final String MENU_NOTICE = "1";
	public static final String MENU_CONTENT = "2";
	public static final String MENU_MEMBER = "3";
	public static final String MENU_STAY = "4";
	public static final String MENU_TEAM = "8";
	public static final String MENU_BEACON = "5";
	public static final String MENU_DEPART = "6";
	public static final String MENU_ADMIN = "7";
	
	
	
	public static final String SUBMENU_NOTICE = "1";
	public static final String SUBMENU_BEACON = "7";
	public static final String SUBMENU_FAQ = "2";
	public static final String SUBMENU_USER_REPORT = "3";
	
	public static final String SUBMENU_EVENT = "2";
	
	
	public static final String SUBMENU_CITY = "2";
	public static final String SUBMENU_COUNTRY = "1";
	public static final String SUBMENU_CITY_COMENT = "3";
	
	public static final String SUBMENU_STAY = "1";
	public static final String SUBMENU_DEPART = "1";
	public static final String SUBMENU_AFFILIATION = "2";
	
	public static final String SUBMENU_WALL_BOARD = "1";
	public static final String SUBMENU_TRAIN_BAORD = "2";
	public static final String SUBMENU_FIND_BAORD = "3";
	public static final String SUBMENU_WIT_BOARD = "4";
/*	public static final String SUBMENU_BREED_BOARD = "5";
	public static final String SUBMENU_WEDDING_BOARD = "6";*/
	public static final String SUBMENU_CLINIC_BOARD = "5";	//원래는 7
	public static final String SUBMENU_GCM = "6";	//원래는 8
	
	public static final String SUBMENU_MEMBER = "1";
	public static final String SUBMENU_STATIS = "2";
	
	public static final String SUBMENU_APMS_REQ = "1";
	public static final String SUBMENU_ORDER_OFF = "2";
	
	public static final String SUBMENU_TEAM_LIST = "1";
	public static final String SUBMENU_LAST_WEEK = "2";
	/*public static final String SUBMENU_TEAM_LIST = "2";*/
	public static final String SUBMENU_EVENT_OFF = "3";
	
	public static final String SUBMENU_SAFEPHONE = "1";
	
	public static final String SUBMENU_ADMIN = "1";
	
	/*파일*/
	//public static final String DIR_FILE = "c:\\MallShopping\\workspace\\audicms\\WebContent\\files";
	public static final int MAX_SIZE = 1024*1024*10;
	
	/*출력 row수*/
	public static final String DEFAULT_ROW_CNT = "10";
	
	/*페이지 block size*/
	public static final String DEFAULT_BLOCK_SIZE = "10";
	
	/*검색 조건*/
	public static final String SEARCH_NAME = "1";
	public static final String SEARCH_MAIL = "2";
	public static final String SEARCH_PHONE = "3";
	public static final String SEARCH_INTER_MODEL = "4";
	public static final String SEARCH_REGION = "5";
	public static final String SEARCH_MASTER = "6";
	public static final String SEARCH_BREED = "7";
	public static final String SEARCH_ID = "8";
	public static final String SEARCH_SAFEPHONE = "9";
	// 게시판 카테고리
	// 1 : 담벼락,
	// 2 : 사진게시판
	// 3 : 결혼정보게시판
	// 4 : 분양정보게시판
	// 5 : 실종게시판
	// 6 : 제보게시판
	// 7 : 업체정보게시판
	// 8 : 친구신청게시판
	public static final String CATEGORY_WALL_BOARD = "1";
	public static final String CATEGORY_PHOTO = "2";
	public static final String CATEGORY_WEDDING = "3";
	public static final String CATEGORY_PETSELL = "4";
	public static final String CATEGORY_LOST = "5";
	public static final String CATEGORY_WIT = "6";
	public static final String CATEGORY_BIZ = "7";
	public static final String CATEGORY_FRIEND_REQ = "8";
	public static final String CATEGORY_FRIEND_ACCEPT = "9";
	public static final String CATEGORY_TRAINING = "10";
	public static final String CATEGORY_EVENT = "11";
	public static final String CATEGORY_POPUP = "12";
	// 알림 Category
	public static final String RPT_CATE_COMMENT = "1";
	public static final String RPT_CATE_FEEL = "2";
	public static final String RPT_CATE_NEW_WRITE = "3";
	public static final String RPT_CATE_ACCEPT_FRIEND = "4";
	public static final String RPT_CATE_REQUEST_FRIEND = "5";
	public static final String RPT_CATE_BIRTYDAY = "6";	
	
	public static final int UPLOAD_PHOTO_SIZE = 10 * 1024 * 1024;
	
	public static final String PHOTO_UPLOAD_URL = "/upload/";
	
	public static final String PHOTO_UPLOAD_URL_THUMBNAIL = "Thumb/";	
	public static final String PHOTO_UPLOAD_URL_WALL = "wall/";
	public static final String PHOTO_UPLOAD_URL_MEMBER_PROFILE = "memberProfile/";
	public static final String PHOTO_UPLOAD_URL_PET_BOARD = "petBoard/";
	public static final String PHOTO_UPLOAD_URL_PET_PROFILE = "petProfile/";
	public static final String PHOTO_UPLOAD_URL_PHOTO = "photo/";
	public static final String PHOTO_UPLOAD_URL_BIZ = "biz/";
	public static final String PHOTO_UPLOAD_URL_TRAINING = "training/";
	public static final String PHOTO_UPLOAD_URL_EVENT = "event/";
	public static final String PHOTO_UPLOAD_URL_POPUP = "popup/";
	public static final String PHOTO_UPLOAD_URL_GCM = "gcm/";
	public static final String PHOTO_UPLOAD_URL_APM = "apm/";
	public static final String PROFILE_PHOTO_FOR_MEMBER = "1";
	public static final String PROFILE_PHOTO_FOR_PET = "2";
	
	public static final String DEFAULT_PROFILE_PHOTO = "../img/profile_info.jpg";
	
	//관리자 memSEQ
	public static final int ADMIN_MEM_SEQ = 1404;
	
	/*push메세지 종류 */
	//앱 깨우기
	public static final int PUSH_AWAKE = 1;		
	//CMS에서 푸시보내기
	public static final int PUSH_NOTICE = 2;
	//비콘인식되었을때 푸시보내기
	public static final int PUSH_RECIVER_RECOG = 3;
}
