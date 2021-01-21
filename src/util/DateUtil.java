package util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

/**
<p>
<code>DateUtil</code>은 Date와 Time과 관련된 class이다.
이것은 일반적인 프로그램에서 사용할 수 있는 class이므로 날짜와 관련된
보다 복잡한 업무가 있을 때에는 GregorianCalendar나 Calendar class를 이용하기 바랍니다.
<p>
 <strong>Time Format Syntax:</strong>
 <p>
 <em>time pattern</em> string을 이용하여 time format을 만든다.
 pattern은 다음과 같이 define되어 있다:
 <blockquote>
 <pre>
 Symbol   Meaning                 Presentation        Example
 ------   -------                 ------------        -------
 G        era designator          (Text)              AD
 y        year                    (Number)            1996
 M        month in year           (Text & Number)     July & 07
 d        day in month            (Number)            10
 h        hour in am/pm (1~12)    (Number)            12
 H        hour in day (0~23)      (Number)            0
 m        minute in hour          (Number)            30
 s        second in minute        (Number)            55
 S        millisecond             (Number)            978
 E        day in week             (Text)              Tuesday
 D        day in year             (Number)            189
 F        day of week in month    (Number)            2 (2nd Wed in July)
 w        week in year            (Number)            27
 W        week in month           (Number)            2
 a        am/pm marker            (Text)              PM
 k        hour in day (1~24)      (Number)            24
 K        hour in am/pm (0~11)    (Number)            0
 z        time zone               (Text)              Pacific Standard Time
 '        escape for text         (Delimiter)
 ''       single quote            (Literal)           '
 </pre>
 </blockquote>
 <p>
 <strong>Examples Using the US Locale:</strong>
 <blockquote>
 <pre>
 Format Pattern                         Result
 --------------                         -------
 "yyyy.MM.dd G 'at' HH:mm:ss z"    ->>  1996.07.10 AD at 15:08:56 PDT
 "EEE, MMM d, ''yy"                ->>  Wed, July 10, '96
 "h:mm a"                          ->>  12:08 PM
 "hh 'o''clock' a, zzzz"           ->>  12 o'clock PM, Pacific Daylight Time
 "K:mm a, z"                       ->>  0:00 PM, PST
 "yyyyy.MMMMM.dd GGG hh:mm aaa"    ->>  1996.July.10 AD 12:08 PM
 </pre>
 </blockquote>
 <p>
 <strong>Code Sample:</strong>
 <blockquote>
 <pre>
 DateUtil.compareDate("2002-10-02","2002-10-01","yyyy-MM-dd");  // 두 날짜를 비교 --> 1 이 return
 DateUtil.compareDate("23:52:12","23:52:12","HH:mm:ss"); // 두 시간을 비교 --> 0 이 return
 int diff = DateUtil.daysDiff("2002-10-02","2002-10-03","yyyy-MM-dd"); // 두 날짜의 차이일수 계산
 String dateString4 = DateUtil.formatCalendar(cal, "yyyy-MM-dd");  // Calendar --> String
 String dateString3 = DateUtil.formatDate(date, "yyyy-MM-dd");  // Date --> String
 String dateString1 = DateUtil.getTime("yyyy-MM-dd");  // 날짜만 있는 format
 String dateString2 = DateUtil.getTime("yyyy/MM/dd HH:mm:ss");  // 날짜와 시간이 있는 format
 String dateString3 = DateUtil.getTime("HH:mm:ss");  // 시간만 있는 format
 Calendar cal = DateUtil.stringToCalendar("2002/09/12", "yyyy/MM/dd");  // String --> Calendar
 Date date = DateUtil.stringToDate("2002/09/11", "yyyy/MM/dd");  //String --> Date
 </pre>
 </blockquote>
*/

public class DateUtil
{

    /**
     * 현재 년월일시분초를 "yyyyMMddHHmmss"의 time format으로 구함
     * @return 현재 년월일시분초
     */
    public static String getTime() {
		return getTime("yyyyMMddHHmmss");
	}

    /**
     * 현재 년월일을 "yyyyMMdd"의 time format으로 구함
     * @return 현재 년월일
     */
	public static String getDate() {
		return getTime("yyyyMMdd");
	}

    /**
     * 현재 년월일시간을 주어진 time format으로 구함
     * @param format time format
     * @return 주어진 time format의 현재 년월일
 	*/
	public static String getTime(String format)	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}

    /**
     * Date Object를 구하기 위해 String을 그 String의 time format을 이용하여 parse
     * @param dateString 특정 time format으로된 String
     * @param format time format
     * @return 주어진 String으로 된 날짜의 Date Object
 	*/
	public static Date stringToDate(String dateString, String format) {
		
		Date d = null;
		try{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
			d= sdf.parse(dateString, pos);
		}catch(Exception e){
			d = new Date();
		}
		
		return d;
	}
	  public static boolean isDate(CharSequence date) {
		  
		    // some regular expression
		    String time = "(\\s(([01]?\\d)|(2[0123]))[:](([012345]\\d)|(60))"
		        + "[:](([012345]\\d)|(60)))?"; // with a space before, zero or one time
		 
		    // no check for leap years (Schaltjahr)
		    // and 31.02.2006 will also be correct
		    String day = "(([12]\\d)|(3[01])|(0?[1-9]))"; // 01 up to 31
		    String month = "((1[012])|(0\\d))"; // 01 up to 12
		    String year = "\\d{4}";
		 
		    // define here all date format
		    ArrayList<Pattern> patterns = new ArrayList<Pattern>();
		    patterns.add(Pattern.compile(day + "[-.:]" + month + "[-.:]" + year + time));
		    patterns.add(Pattern.compile(year + "[-.:]" + month + "[-.:]" + day + time));
		    // here you can add more date formats if you want
		 
		    // check dates
		    for (Pattern p : patterns)
		      if (p.matcher(date).matches())
		        return true;
		 
		    return false;
		 
	}
	  
    /**
     * Calendar Object를 구하기 위해 String을 그 String의 time format을 이용하여 parse
     * @param dateString 특정 time format으로된 String
     * @param format time format
     * @return 주어진 String으로 된 날짜의 Calendar Object
 	*/
	public static Calendar stringToCalendar(String dateString, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
		Date date = sdf.parse(dateString, pos);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

    /**
     * 특정 Date Object를 time format을 이용하여 String 변환
     * @param date 특정 일자의 Date Object
     * @param format time format
     * @return 특정일의 String
 	*/
	public static String formatDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return sdf.format(cal.getTime());
	}

    /**
     * 특정 Calendar Object를 time format을 이용하여 String 변환
     * @param cal 특정 일자의 Calendar Object
     * @param format time format
     * @return 특정일의 String
 	*/
	public static String formatCalendar(Calendar cal, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}

	/**
     * 해당 연도가 윤년인지를 판단
     * @param year 특정 연도
     */
    public static boolean isLeapYear(int year)
    {
        if(year % 4 != 0)
            return false;
        if(year % 400 == 0)
            return true;
        return year % 100 != 0;
    }

    /**
     * 특정 연도 특정 월의 총 일수를 얻을 수 있다.
	 *
     * @param year 특정 연도
	 * @param month 특정 월 (1 ~ 12)
     * @return 특정 연도 월의 총 일수
     */
    public static int lastDate(int year, int month)
    {
        int kLastDates[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		if(month > 12 || month < 0)
			month = 0;

        if(month == 2 && isLeapYear(year))
            return kLastDates[month] + 1;
        else
            return kLastDates[month];
    }

    /**
     * 두 날짜의 차이일수 계산
     */
    public static int daysDiff( String earlierDate, String laterDate, String format )
    {
        if( earlierDate == null || laterDate == null ) return 0;

        Date d1 = null;
        Date d2 = null;
        try{	
			d1 = stringToDate(earlierDate, format);
			d2 = stringToDate(laterDate, format);
        }catch(Exception e){
        	return 65530;
        }
        
        return (int)((d2.getTime() - d1.getTime())/(24*60*60*1000));
    }
    
    /**
     * 두 날짜의 차이시간 계산
     */
    public static int daysDiffHour( String earlierDate, String laterDate, String format )
    {
        if( earlierDate == null || laterDate == null ) return 0;

        Date d1 = null;
        Date d2 = null;
        try{	
			d1 = stringToDate(earlierDate, format);
			d2 = stringToDate(laterDate, format);
        }catch(Exception e){
        	return 65530;
        }
        
        return (int)((d2.getTime() - d1.getTime())/(60*60*1000));
    }
    
     /**
      * 2개의 날짜를 비교할 수 있다. 마찬가지로 시간도 비교할 수 있다.
      * @return -1 : date1 < date2
      *          0 : date1 = date2
      *          1 : date1 > date2
      */
     public static int compareDate( String date1, String date2, String format )
     {
		Calendar c1 = stringToCalendar(date1, format);
		Calendar c2 = stringToCalendar(date2, format);

		return compareDate(c1, c2);
	 }

     /**
      * 2개의 날짜를 비교할 수 있다.
      * @return -1 : date1 < date2
      *          0 : date1 = date2
      *          1 : date1 > date2
      */
     public static int compareDate( Date date1, Date date2 )
     {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);

		return compareDate(c1, c2);
	 }
     /**
      * 2개의 날짜를 비교할 수 있다.
      * @return -1 : cal1 < cal2
      *          0 : cal1 = cal2
      *          1 : cal1 > cal2
      */
     public static int compareDate( Calendar cal1, Calendar cal2 )
     {
		int value = 9;

		if (cal1.before(cal2)) {
			value = -1;
        }
        if (cal1.after(cal2)) {
            value = 1;
        }
        if (cal1.equals(cal2)) {
            value = 0;
        }
		return value;
	 }

	/**
    * 특정 날짜에 연도를 더하거나 뺀 결과를 반환한다.
    * @param startDate - 기준 날짜
    * @param years - 더하거나 뺄 연도. 기준 날짜보다 과거로 가고자 한다면 음수 값을 넣는다.
    */
    public static java.sql.Date rollYears( java.util.Date startDate, int years)
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(startDate);
        gc.add(Calendar.YEAR, years);
        return new java.sql.Date(gc.getTime().getTime());
    }

   /**
    * 특정 날짜에 달을 더하거나 뺀 결과를 반환한다.
    * @param startDate - 기준 날짜
    * @param months - 더하거나 뺄 개월수. 기준 날짜보다 과거로 가고자 한다면 음수 값을 넣는다.
    */
    public static java.sql.Date rollMonths( java.util.Date startDate, int months )
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(startDate);
        gc.add(Calendar.MONTH, months);
        return new java.sql.Date(gc.getTime().getTime());
    }

   /**
    * 특정 날짜에 일을 더하거나 뺀 결과를 반환한다.
    * @param startDate - 기준 날짜
    * @param days - 더하거나 뺄 일수. 기준 날짜보다 과거로 가고자 한다면 음수 값을 넣는다.
    */
    public static java.sql.Date rollDays( java.util.Date startDate, int days )
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(startDate);
        gc.add(Calendar.DATE, days);
        return new java.sql.Date(gc.getTime().getTime());
    }
    
   /**
    * 특정 날짜에 시간을 더하거나 뺀 결과를 반환한다.
    * @param startDate - 기준 날짜
    * @param days - 더하거나 뺄  시간수. 기준 날짜보다 과거로 가고자 한다면 음수 값을 넣는다.
    */
    public static java.sql.Date rollHours( java.util.Date startDate, int hours )
    {
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(startDate);
    	gc.add(Calendar.HOUR, hours);
    	return new java.sql.Date(gc.getTime().getTime());
    }
    
    /**
     * 특정 날짜에 분을 더하거나 뺀 결과를 반환한다.
     * @param startDate - 기준 날짜
     * @param days - 더하거나 뺄 분수. 기준 날짜보다 과거로 가고자 한다면 음수 값을 넣는다.
     */
    public static java.sql.Date rollMins( java.util.Date startDate, int mins )
    {
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(startDate);
    	gc.add(Calendar.MINUTE, mins);
    	return new java.sql.Date(gc.getTime().getTime());
    }
    
    /**
     * 특정 날짜에 초를 더하거나 뺀 결과를 반환한다.
     * @param startDate - 기준 날짜
     * @param days - 더하거나 뺄 초수. 기준 날짜보다 과거로 가고자 한다면 음수 값을 넣는다.
     */
    public static java.sql.Date rollSecs( java.util.Date startDate, int secs )
    {
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(startDate);
    	gc.add(Calendar.SECOND, secs);
    	return new java.sql.Date(gc.getTime().getTime());
    }

    /**
     * 내일 날짜를 yyyyMMdd 형식으로 받는다.
     *
     * @return String
     */
    public static String getTomorrow() {
    	return getDate(1, "yyyyMMdd");
    }

    /**
     * 내일 날짜를 지정한 형식으로 받는다.
     * @param format 날짜 문자열 형식
     * @return String
     */
    public static String getTomorrow(String format) {
    	return getDate(1, format);
    }

    /**
     * 어제 날짜를 yyyyMMdd 형식으로 받는다.
     * @return String
     */
    public static String getYesterday() {
    	return getDate(-1, "yyyyMMdd");
    }

    /**
     * 어제 날짜를 지정한 형식으로 받는다.
     * @param format 날짜 문자열 형식
     * @return String
     */
    public static String getYesterday(String format) {
    	return getDate(-1, format);
    }

    /**
     * 오늘 날짜에 특정 일을 더하거나 뺀 결과를 yyyyMMdd 형식으로 반환한다.
     * @param days 더하거나 뺄 일 수. 오늘보다 과거로 가려면 음수 값을 넣는다.
     * @return String
     */
    public static String getDate(int days) {
    	return getDate(days, "yyyyMMdd");
    }

    /**
     * 오늘 날짜에 특정 일을 더하거나 뺀 결과를 지정한 형식으로 반환한다.
     * @param days 더하거나 뺄 일 수. 오늘보다 과거로 가려면 음수 값을 넣는다.
     * @param format 날짜 문자열 형식
     * @return String
     */
    public static String getDate(int days, String format) {
    	GregorianCalendar gc = new GregorianCalendar();
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	gc.add(Calendar.DATE, days);

    	return sdf.format(gc.getTime());
    }

    public static String dateFilterTrim(String src, String format) {
    	String trimStr = src.replace("-", "");
    	return dateFilter(trimStr, trimStr);
    }
   /**
    * 전달된 String ('YYYYMMDDHH24MISS')을  formate에 맞게 변환
    * YYYY는 1900으로 MM은 12등으로 변환.
    * 예) src : 20011213134532
    *      format : "YYYY년 MM월 DD일 HH24:MI:SS"
    *   -> 2001년 12월 13일 13:45:32
    *
    * @author 민선기
    *
    * @param str           변환시킬 문자열
    * @param format     변환형식
    *
    * @return 변환된 문자열
    */
    public static String dateFilter(String src, String format)
    {
        if ( src == null || src.length() <14 || (format.length() < 2) )
            return "";

        String str_ret="";
        int iyyyy, iyy, imm, idd, i24hh, ihh, imi, iss;

        iyyyy = Integer.parseInt(src.substring(0,4));
        iyy = Integer.parseInt(src.substring(2,4));
        imm = Integer.parseInt(src.substring(4,6));
        idd = Integer.parseInt(src.substring(6,8));
        i24hh = Integer.parseInt(src.substring(8,10));
        if( i24hh > 12 ) ihh = i24hh-12;
        else ihh = i24hh;
        imi = Integer.parseInt(src.substring(10,12));
        iss = Integer.parseInt(src.substring(12,14));

        // YYYY-MM-DD 12시25분13초
        str_ret = replaceString(format, "YYYY", intToString(iyyyy));
        str_ret = replaceString(str_ret, "YY", intToString(iyy));
        str_ret = replaceString(str_ret, "MM", intToString(imm));
        str_ret = replaceString(str_ret, "DD", intToString(idd));
        str_ret = replaceString(str_ret, "HH24", intToString(i24hh));
        str_ret = replaceString(str_ret, "HH", intToString(ihh));
        str_ret = replaceString(str_ret, "MI", intToString(imi));
        str_ret = replaceString(str_ret, "SS", intToString(iss));

        return str_ret;
    }

    /**
    * 전달된 String ('YYYYMMDDHH24MISS')을  formate에 맞게 변환
    * YYYY는 1900으로 MM은 12등으로 변환.
    * 예) src : 20011213134532
    *      format : "YYYY년 MM월 DD일 HH24:MI:SS"
    *   -> 2001년 12월 13일 13:45:32
    *
    * @author 민선기(update 이시형 - 2003/06/02)
    * @param str           변환시킬 문자열
    * @param format     변환형식
	* @param zeroFG       (월,일,시,분 중)한자리일때 앞에 0을 붙일지 여부(true: 붙임, false:않붙임)
    *
    * @return 변환된 문자열
    */
    public static String dateFilter(String src, String format, boolean zeroFG)
    {
        if ( src == null || src.length() <14 || (format.length() < 2) )
            return "";

        String str_ret = "";
        int iyyyy, iyy, imm, idd, i24hh, ihh, imi, iss,temphh;
		String strhh = "";

		if(zeroFG) {     //한자리 일때 0이 붙임
			temphh = Integer.parseInt(src.substring(8,10));		//시간(작업용)

			if( temphh > 12 ) ihh = temphh-12;
			else ihh = temphh;

			strhh = intToString(ihh);
			if(strhh.length() <= 1) strhh =  "0" + strhh;

		    str_ret = replaceString(format, "YYYY", src.substring(0,4));
	        str_ret = replaceString(str_ret, "YY", src.substring(2,4));
	        str_ret = replaceString(str_ret, "MM", src.substring(4,6));
	        str_ret = replaceString(str_ret, "DD", src.substring(6,8));
	        str_ret = replaceString(str_ret, "HH24", src.substring(8,10));
	        str_ret = replaceString(str_ret, "HH", strhh);
	        str_ret = replaceString(str_ret, "MI", src.substring(10,12));
	        str_ret = replaceString(str_ret, "SS", src.substring(12,14));

		} else {		//한자리 일때 0 붙이지 않음

			iyyyy = Integer.parseInt(src.substring(0,4));			//년
			iyy = Integer.parseInt(src.substring(2,4));				//년(두자리)
			imm = Integer.parseInt(src.substring(4,6));			//월
			idd = Integer.parseInt(src.substring(6,8));			//일
			i24hh = Integer.parseInt(src.substring(8,10));		//시간

			if( i24hh > 12 ) ihh = i24hh-12;
			else ihh = i24hh;

			imi = Integer.parseInt(src.substring(10,12));			//분
			iss = Integer.parseInt(src.substring(12,14));			//초

	        str_ret = replaceString(format, "YYYY", intToString(iyyyy));
	        str_ret = replaceString(str_ret, "YY", intToString(iyy));
	        str_ret = replaceString(str_ret, "MM", intToString(imm));
	        str_ret = replaceString(str_ret, "DD", intToString(idd));
	        str_ret = replaceString(str_ret, "HH24", intToString(i24hh));
		    str_ret = replaceString(str_ret, "HH", intToString(ihh));
	        str_ret = replaceString(str_ret, "MI", intToString(imi));
	        str_ret = replaceString(str_ret, "SS", intToString(iss));
		}
		return str_ret;
    }

    private static String intToString(int i)
    {
        return (new Integer(i)).toString();
    }

    private static String replaceString(String src, String from, String to)
    {
        StringBuffer res_Buff = new StringBuffer();
        int pos=-1;

        if (src == null || from == null || from.equals("")) return src;
        if (to == null) to = "";

        while (true)
        {
            if ((pos = src.indexOf(from)) == -1)
            {
                res_Buff.append(src);
                break;
            }
            res_Buff.append(src.substring(0, pos)).append(to);
            src = src.substring(pos + from.length());
        }
        return res_Buff.toString();
    }
    /**
     * 전달된 String ('YYYYMMDD')을  구분자를 첨부해서 변환
     * ex)dateFormatString(20040101,"-")
     *    return 2004-01-01
     */
    public static String dateFormatString(String dateString, String gubun)
    {
    	String returnStr = "-";
    	if(dateString.length()>6){
	    	String yy = dateString.substring(0,4);
			String mm = dateString.substring(4,6);
			String dd = dateString.substring(6);
			returnStr = yy+gubun+mm+gubun+dd;
    	}else if(dateString.length()>4 && dateString.length()<=6){
	    	String yy = dateString.substring(0,4);
			String mm = dateString.substring(4);
			returnStr = yy+gubun+mm;
    	}else if(dateString.length()>0){
    		returnStr = dateString;
    	}
    	return returnStr;
    }

	/**
	 * 오늘부터 validToDate 가지 남은 일수를 계산한다.
	 * 
	 * @param validToDate
	 * @return
	 */
	public static int daysDiffFromToday(String validToDate) {
		return daysDiff(getTime(),validToDate,"yyyyMMddHHmmss");
	}

	
	/**
	 *  입력된 구분자를 넣어 발매일을 만든다. 
	 *  예: 20050505=>2005.05.05,  20050500=>2005.05,  20050000=>2005  
	 * @param date  날짜
	 * @param gubun 날짜 구분자
	 * 
	 * @return 
	 */
	public static String formatIssueDate(String date, String gubun){
        String returnDate = "-";
        
        if(date.length() < 8) {
        	return returnDate;
        
        }else {
        	if(date.equals("00000000")){
            	returnDate = "";
        	
        	}else {	
	        	String year = date.substring(0,4);
	            String month = date.substring(4,6);
	            String day = date.substring(6,8);
	            
	            if(!year.equals("0000")) returnDate = year;
	            if(!month.equals("00")) returnDate = returnDate + gubun + month;
	            if(!day.equals("00")) returnDate = returnDate + gubun + day;
	        }
            return returnDate;
       }
   }

	
	/**
	 *  입력된 발매일과 현재날짜를 비교하여 7일 이내이면  true를 리턴한다.  (쿨메뉴 음악장르용)
	 * @param date  발매일
	 * 
	 * @return 
	 */
	public static boolean chkGenreOrderIssueDate(String orderIssueDate){
		String date = orderIssueDate.trim();
        boolean returnValue = false;
        int daydiff = 0;
        
        try{
	        if(date.length() < 8 || date.equals("0000000000") || date.equals("") || date == null) {
	        	return returnValue;
	        
	        }else {
	        	if(((date.substring(0,8)).trim()).length() == 8){
	        		daydiff = daysDiff( date.substring(0,8) , getDate(), "yyyyMMdd");
	        		if(daydiff<=7){
		        		returnValue = true;
		        	}
	        	}	
	        }
	        
        }catch (Exception e){
        	return false;
        }
       
        return returnValue;
   }
   

	/**
	 *  입력된 발매일과 현재날짜를 비교하여 30일 이내이면  true를 리턴한다. (쿨메뉴 그외장르용)
	 * @param date  발매일
	 * 
	 * @return 
	 */
	public static boolean chkOtherGenreOrderIssueDate(String orderIssueDate){
		String date = orderIssueDate.trim();
        boolean returnValue = false;
        int daydiff = 0;
        
        try{
	        if(date.length() < 8 || date.equals("0000000000") || date.equals("") || date == null) {
	        	return returnValue;
	        
	        }else {
	        	if(((date.substring(0,8)).trim()).length() == 8){
	        		daydiff = daysDiff( date.substring(0,8) , getDate(), "yyyyMMdd");
	        		if(daydiff<=30){
		        		returnValue = true;
		        	}
	        	}	
	        }
	        
        }catch (Exception e){
        	return false;
        }
        
        return returnValue;
   }   
   
 	/**
	 *  입력된 발매일과 현재날짜를 비교하여 입력된 기간 이내이면  true를 리턴한다. 
	 * @param date   발매일
	 * @param period   기간
	 * @return 
	 */
	public static boolean chkOrderIssueDate(String orderIssueDate, int period){
		String date = orderIssueDate.trim();
        boolean returnValue = false;
        int daydiff = 0;
        
        try{
	        if(date.length() < 8 || date.equals("0000000000") || date.equals("") || date == null) {
	        	return returnValue;
	        
	        }else {
	        	if(((date.substring(0,8)).trim()).length() == 8){
	        		daydiff = daysDiff( date.substring(0,8) , getDate(), "yyyyMMdd");
	        		if(daydiff <= period){
		        		returnValue = true;
		        	}
	        	}	
	        }
	       
        }catch (Exception e){
        	return false;
        }    
       
        return returnValue;
   } 
	
	/**
	 * 입력된 날짜의 차이를 구해서 duration값으로 반환
	 * @param startDate
	 * @param triggerDate
	 * @param format
	 * @return
	 */	
	public static String StringDur(String createdTime){
		Date d2 = DateUtil.stringToDate(createdTime, "yyyyMMddHHmmss");
		return StringDur(d2);
	}	
	
	public static String StringDur(Date createdTime){
		String returnValue = "";
		int duration = 0;
		Date d1 = DateUtil.stringToDate(getTime(), "yyyyMMddHHmmss");
		Date d2 = createdTime;
		duration = (int)((d1.getTime() - d2.getTime())/(60*1000));
		int result = 0;
		if(duration != 0 && duration%60 == 0){ //시간
			result = duration / 60;
			returnValue = result + "H";
			
			if(result%24 == 0){
				result = result / 24;
				returnValue = result + "D";
				
				if(result%7 == 0){
					result = result / 7;
					returnValue = result + "W";
				}
			}
		}else{
			returnValue = duration + "M";
		}
		
		return returnValue;
	}
	
	public static void main(String args[]) {
		System.out.println(DateUtil.getTime("yyyy-MM-dd'T'HH:mm:ss.SSS"));
	}
	
    /**
     * 1일 이전은 시간 차이 값을 반환하고, 1일 보다 초과하면 원래 시간을 반환 한다.
     * 
     * @param earlierDate
     * @param format
     * @return
     */
    public static String diffCurrent( String earlierDate, String format )
    {
        if( earlierDate == null ) return "";

        Date d1 = null;
        Date d2 = null;
        try{	
			d1 = stringToDate(earlierDate, format);
			d2 = DateUtil.stringToDate(getTime(), "yyyyMMddHHmmss");
        }catch(Exception e){
        	return "";
        }
        
        long diffTime = ((d2.getTime() - d1.getTime())/(60*1000));	// 총 분
               
        int min = 0;
        if (diffTime > 0) {
        	min = (int)diffTime % 60;			// 분
        	diffTime /= 60;
        }
        
        int hour = 0;
        if (diffTime > 0) {
        	hour = (int) diffTime % 24;			// 시간
        	diffTime /= 24;
        }
        
        int day = 0;
        if (diffTime > 0) {
        	day = (int) diffTime;
        }

        StringBuffer strTime = new StringBuffer();
        if(day>0) strTime.append("" + day + "일");
        if(hour>0) strTime.append("" + hour + "시간");
        if(min>0) strTime.append("" + min + "분");
        

        	
        return (day > 0 ? formatDate(d1, "yyyy년 MM월 dd일 HH:mm:ss") : strTime.toString() + "전");
    }
}

