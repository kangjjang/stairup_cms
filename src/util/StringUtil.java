package util;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
<p>
<code>StringUtil</code>은 String의 handling과 관련된 class이다.
<p>
*/

public class StringUtil
{

   /**
    * String을 읽어 알파벳과 숫자만 모아 return ('_', '-' 포함)
    * @param s source String
    * @return 알파벳을 제외하고 걸려진 String
    */
    public static String alphaNumOnly(String s)
    {
        int i = s.length();
        StringBuffer stringbuffer = new StringBuffer(i);
        for(int j = 0; j < i; j++)
        {
            char c = s.charAt(j);
            if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '-')
                stringbuffer.append(c);
        }

        return stringbuffer.toString();
        
        
    }

    /**
     * String을 읽어 알파벳과 숫자만 있는지 check ('_', '-'포함)
     * @param s source String
 	 */
    public static boolean isAlphaNumOnly(String s)
    {
        int i = s.length();
        for(int j = 0; j < i; j++)
        {
            char c = s.charAt(j);
            if((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c < '0' || c > '9') && c != '_' && c != '-')
                return false;
        }

        return true;
    }

    /**
     * String을 읽어 알파벳만 있는지 check
     * @param s source String
     */
    public static boolean isAlphaOnly(String s)
    {
    	int i = s.length();
    	for(int j = 0; j < i; j++)
    	{
    		char c = s.charAt(j);
    		if((c < 'a' || c > 'z') && (c < 'A' || c > 'Z'))
    			return false;
    	}
    	
    	return true;
    	
    	
    }
    
	/**
	* Alphabet 문자인지 체크
	* @param ch 체크할 문자
	* @return Alphabet 문자이면 true, 그렇지 않으면 false
	*/
	public static boolean isAlpha(char ch)
	{
		if( ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' )
			return true;
		else
			return false;
	}

	/**
     * String을 읽어 숫자만 있는지 check
     * @param s source String
 	 */
    public static boolean isNumOnly(String s)
    {
        int i = s.length();
        for(int j = 0; j < i; j++)
        {
            char ch = s.charAt(j);
            if( ch < '0' || ch > '9')
                return false;
        }

        return true;
    }
    
	/**
	* 숫자 문자인지 체크
	* @param ch 체크할 문자
	* @return 숫자 문자이면 true, 그렇지 않으면 false
	*/
	public static boolean isNumeric(char ch)
	{
		if( ch >= '0' && ch <= '9')
			return true;
		else
			return false;
	}

	/**
     * String s에서 연속되는 space들을 하나로 압축한 String으로 return
     * @param s source String
 	 */
    public static String normalizeWhitespace(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        int i = s.length();
        boolean flag = false;
        for(int j = 0; j < i; j++)
        {
            char c = s.charAt(j);
            switch(c)
            {
            case 9: // '\t'
            case 10: // '\n'
            case 13: // '\r'
            case 32: // ' '
                if(!flag)
                {
                    stringbuffer.append(' ');
                    flag = true;
                }
                break;

            default:
                stringbuffer.append(c);
                flag = false;
                break;
            }
        }

        return stringbuffer.toString();
    }

	/**
     * String s에서 character c가 몇 개가 있는지 return
     * @param s source String
     * @param c 찾을 character
 	 */
    public static int numOccurrences(String s, char c)
    {
        int i = 0;
        int j = 0;
        int l;
        for(int k = s.length(); j < k; j = l + 1)
        {
            l = s.indexOf(c, j);
            if(l < 0)
                break;
            i++;
        }

        return i;
    }

	/**
     * String s에서 String s1에 포함되는 모든 char를 제거한 String으로 return
     * @param s source String
     * @param s1 삭제시킬 sub String
 	 */
    public static String removeCharacters(String s, String s1)
    {
        int i = s.length();
        StringBuffer stringbuffer = new StringBuffer(i);
        for(int j = 0; j < i; j++)
        {
            char c = s.charAt(j);
            if(s1.indexOf(c) == -1)
                stringbuffer.append(c);
        }

        return stringbuffer.toString();
    }

	/**
     * String s에서 존재하는 space들을 모두 제거한 String으로 return
     * @param s source String
 	 */
    public static String removeWhiteSpace(String s)
    {
        int i = s.length();
        StringBuffer stringbuffer = new StringBuffer(i);
        for(int j = 0; j < i; j++)
        {
            char c = s.charAt(j);
            if(!Character.isWhitespace(c))
                stringbuffer.append(c);
        }

        return stringbuffer.toString();
    }

    /**
     * String target의 arguments[0],arguments[1]..부분을 replacements[0],replacements[1]..으로 바꾸어 return
     * @param target source String
     * @param arguments 바뀌어질 대상의 String 배열
     * @param replacements 대체될 String 배열
 	 */
    public static String replace( String target, String[] arguments, String[] replacements )
    {
        if( target == null || arguments == null || replacements == null ) return target;

        for( int index = 0; index < arguments.length; index++ )
        {
            target = replace( target, arguments[index], replacements[index] );
        }

        return target;
    }

    /**
     * String target에 포함되어 있는 argument을 replacement로 바꾸어 return
     * @param target source String
     * @param argument old String
     * @param replacement new String
 	 */
    public static String replace( String target, String argument, String replacement )
    {
        if ( target == null || argument == null || replacement == null ) return target;

        int i = target.indexOf(argument);

        if ( i == -1 ) return target;

        StringBuffer targetSB = new StringBuffer(target);
        while (i != -1)
        {
            targetSB.delete( i, i + argument.length() );
            targetSB.insert( i, replacement );
            //check for any more
            i = targetSB.toString().indexOf(argument,i + replacement.length());
        }

        return targetSB.toString();
    }

    /**
     * String s에 있는 character c를 이용하여 String을 분리한다.
     * @param s source String
     * @param c String s를 분리할 character
     * @return 분리된 String 배열
 	 */
    public static String[] splitStringAtCharacter(String s, char c)
    {
        String as[] = new String[numOccurrences(s, c) + 1];
        splitStringAtCharacter(s, c, as, 0);
        return as;
    }

    protected static int splitStringAtCharacter(String s, char c, String as[], int i)
    {
        int j = 0;
        int k = i;
        int l = 0;
        int j1;
        for(int i1 = s.length(); l <= i1 && k < as.length; l = j1 + 1)
        {
            j1 = s.indexOf(c, l);
            if(j1 < 0)
                j1 = i1;
            as[k] = s.substring(l, j1);
            j++;
            k++;
        }

        return j;
    }

    /**
     * Convert a String to a boolean
	 * <p>
	 * 대소문자 상관없이 "true","yes","ok","okay","on","1"인 경우 true를 return한다.
     * @param data the thing to convert
     * @return the converted data
     */
    public static boolean string2Boolean(String data)
    {
    	if(data==null) return false;
        if (data.equalsIgnoreCase("true")) return true;
        if (data.equalsIgnoreCase("yes")) return true;
        if (data.equalsIgnoreCase("ok")) return true;
        if (data.equalsIgnoreCase("okay")) return true;
        if (data.equalsIgnoreCase("on")) return true;
        if (data.equalsIgnoreCase("1")) return true;
        if (data.equalsIgnoreCase("y")) return true;
        

        return false;
    }

    /**
     * Convert a String to an int
     * @param data the thing to convert
     * @return the converted data
     */
    public static int string2Int(String data)
    {
        try
        {
            return Integer.parseInt(data);
        }
        catch (NumberFormatException ex)
        {
            return 0;
        }
    }
    
	/**
	 * 문자열을 ArrayList로 변환 
	 * @param strValue	String
	 * @return List
	 */
    public static List string2ArrayList(String strValue) {
		
    	List arrResult = new ArrayList();
		strValue = StringUtil.nchk(strValue);
		
		if (strValue == null || strValue.equals("")) {
			return arrResult;
		}
		
		String [] arrBuff = strValue.split(",");
		
		if (arrBuff == null) {
			return arrResult;
		}
		
		for (int i=0; i<arrBuff.length; i++) {
			arrResult.add(arrBuff[i].trim());
		}
		
		return arrResult;
	}

    /**
     * Convert a String to a Hashtable
	 * <p>
	 * "key1=value1 key2=value2 .... " 구조의 string을 Hashtable로 변환
     * @param data the thing to convert
     * @return the converted data
     */
    public static Map string2Hashtable(String data)
    {
        Map commands = new HashMap();

        data = normalizeWhitespace(data);
        String[] data_arr = splitStringAtCharacter(data, ' ');

        for (int i=0; i<data_arr.length; i++)
        {
            int equ_pos = data_arr[i].indexOf('=');
            String key = data_arr[i].substring(0, equ_pos);
            String value = data_arr[i].substring(equ_pos + 1);

            commands.put(key, value);
        }

        return commands;
    }

    /**
     * Convert a Hashtable to a Sting
	 * <p>
	 * "key1=value1 key2=value2 .... " 구조의 string으로 변환
     * @param data the thing to convert
     * @return the converted data
     */
    public static String hashtable2String(Map commands)
    {
        Iterator it = commands.keySet().iterator();
        StringBuffer retcode = new StringBuffer();

        while (it.hasNext())
        {
            String key = "";
            String value = "";

            try
            {
                key = (String) it.next();
                value = (String) commands.get(key);

                retcode.append(key);
                retcode.append("=");
                retcode.append(value);
                retcode.append(" ");
            }
            catch (ClassCastException ex)
            {
            }
        }

        return retcode.toString().trim();
    }

    /**
     * String s에 있는 alphabet을 모두 소문자로 바꾸어 return
     * @param s source String
 	 */
    public static String toLowerCase(String s)
    {
        int i;
        int j;
        char c;
label0:
        {
            i = s.length();
            for(j = 0; j < i; j++)
            {
                char c1 = s.charAt(j);
                c = Character.toLowerCase(c1);
                if(c1 != c)
                    break label0;
            }

            return s;
        }
        char ac[] = new char[i];
        int k;
        for(k = 0; k < j; k++)
            ac[k] = s.charAt(k);

        ac[k++] = c;
        for(; k < i; k++)
            ac[k] = Character.toLowerCase(s.charAt(k));

        String s1 = new String(ac, 0, i);
        return s1;
    }

    /**
     * String s에 있는 alphabet을 모두 대문자로 바꾸어 return
     * @param s source String
 	 */
    public static String toUpperCase(String s)
    {
        int i;
        int j;
        char c;
label0:
        {
            i = s.length();
            for(j = 0; j < i; j++)
            {
                char c1 = s.charAt(j);
                c = Character.toUpperCase(c1);
                if(c1 != c)
                    break label0;
            }

            return s;
        }
        char ac[] = new char[i];
        int k;
        for(k = 0; k < j; k++)
            ac[k] = s.charAt(k);

        ac[k++] = c;
        for(; k < i; k++)
            ac[k] = Character.toUpperCase(s.charAt(k));

        return new String(ac, 0, i);
    }

   /**
    * String s에 있는 sub string s1을 이용하여 String을 분리한다.
    * @param s source String
    * @param s1 String s를 분리할 sub string
    * @return 분리된 string의 벡터
    */
    public static Vector tokenizer(String s, String s1)
    {
        if(s == null)
            return null;
        Vector vector = null;
        for(StringTokenizer stringtokenizer = new StringTokenizer(s, s1); stringtokenizer.hasMoreTokens(); vector.addElement(stringtokenizer.nextToken().trim()))
            if(vector == null)
                vector = new Vector();

        return vector;
    }

   /**
    * &, <, >, "를 &amp;amp;, &amp;lt;, &amp;gt;, &amp;quot; 로 대체한 string으로 바꾸어 줌
    * @param s source String
    */
    public static String escapeHtmlString(String s)
    {
        String s1 = s;
        if(s1 == null)
            return null;
        if(s1.indexOf(38, 0) != -1)
            s1 = replace(s1, "&", "&amp;");
        if(s1.indexOf(60, 0) != -1)
            s1 = replace(s1, "<", "&lt;");
        if(s1.indexOf(62, 0) != -1)
            s1 = replace(s1, ">", "&gt;");
        if(s1.indexOf(34, 0) != -1)
            s1 = replace(s1, "\"", "&quot;");
        if(s1.indexOf(13, 0) != -1)
            s1 = replace(s1, "\\n", "<br>");
       return s1;
    }

   /**
    * &amp;amp;, &amp;lt;, &amp;gt;, &amp;quot;를 &, <, >, " 로 대체한 string으로 바꾸어 줌
    * @param s source String
    */
    public static String reEscapeHtmlString(String s)
    {
        String s1 = s;
        if(s1 == null)
            return null;
		String[] arguments = {"&amp;","&lt;","&gt;","&quot;"};
		String[] replacements = {"&","<",">","\""};
        return replace(s1, arguments, replacements);
    }

    /**
     * character c로 length만큼 채워진 String을 return
     * @param c string으로 채워질 character
     * @param length 원하는 character 갯수
     * @return charracter c로 length 갯수 만큼 채워진 string
     */
    public static String fill( char c, int length )
    {
        if( length <= 0 ) return "";

        char[] ca = new char[length];
        for( int index = 0; index < length; index++ )
        {
            ca[index] = c;
        }

        return new String( ca );
    }

   /**
    * 주어진 length를 유지하기 위해 String s에 character c를 오른쪽으로 덧댄다.
	* <p>
	* <pre>
	* StringUtil.padRight("hahahaha", '.', 14);
	* StringUtil.padRight("hihihi", '.', 14);
	* StringUtil.padRight("hohohohoho", '.', 14);

	* 은 다음과 같은 결과를 보여줄 것이다.

	* hahahaha.....
	* hihihi.......
	* hohohohoho...
	* </pre>
	* 위와 같이 일정한 사이즈로 문단을 구성하고자 할 때 유용할 것 임
    * @param s source String
    * @param c String s에 덧대질 character
    * @param length return될 String의 length
    */
    public static String padRight( String s, char c, int length )
    {
        return s + fill( c, length - s.length() );
    }

   /**
    * 주어진 length를 유지하기 위해 String s에 character c를 왼쪽으로 덧댄다.
	* <p>
	* <pre>
	* StringUtil.padRight("hahahaha", '.', 14);
	* StringUtil.padRight("hihihi", '.', 14);
	* StringUtil.padRight("hohohohoho", '.', 14);

	* 은 다음과 같은 결과를 보여줄 것이다.

	* .....hahahaha
	* .......hihihi
	* ...hohohohoho
	* </pre>
	* 위와 같이 일정한 사이즈로 문단을 구성하고자 할 때 유용할 것 임
    * @param s source String
    * @param c String s에 덧대질 character
    * @param length return될 String의 length
    */
    public static String padLeft( String s, char c, int length )
    {
        return fill( c, length - s.length() ) + s;
    }

    


    /**
     * comma 구분자를 가지고 Array를 String으로 변환한다.
     * <p>
	 * 예를들면<br>
	 * {"aaa","bbbb","cc"} ---> "aaa,bbbb,cc"
     */
    public static String toString( Object[] args )
    {
        return toString( args, "," );
    }

    /**
     * separator 구분자를 가지고 Array를 String으로 변환
     */
    public static String toString( Object[] args, String separator )
    {
        if( args == null ) return null;

        StringBuffer buf = new StringBuffer();

        for( int index = 0; index < args.length; index++ )
        {
            if( index > 0 ) buf.append( separator );

            if( args[index] == null ) buf.append( "" );
            else buf.append( args[index].toString() );
        }

        return buf.toString();
    }

    /**
     * separator 구분자를 가지고 List를 String으로 변환
     */
    public static String toString( List list, String separator )
    {
        StringBuffer buf = new StringBuffer();
        for( int index = 0; index < list.size(); index++ )
        {
            if( index > 0 ) buf.append( separator );
            buf.append( list.get( index ).toString() );
        }
        return buf.toString();
    }

    /**
     * separator 구분자를 가지고 List를 String으로 변환
     */
    public static String toString( List list, String mapname, String separator )
    {
        StringBuffer buf = new StringBuffer();
        for( int index = 0; index < list.size(); index++ )
        {
        	HashMap info = (HashMap)list.get( index );
        	
            if( index > 0 ) buf.append( separator );
            buf.append( info.get(mapname).toString() );
        }
        return buf.toString();
    }
    
    /**
    * 전달된 문자열을 src_enc 방식에서 dest_enc 방식으로 변환한다.
    * @author 민선기
    *
    * @param String str           변환시킬 문자열
    * @param String src_enc       원래 문자의 encoding방식
    * @param String des_enc       변환시킬 encoding방식.
    *
    * @return String  desc_enc 방식으로 변환된 문자열
    *
    * @throws UnsupportedEncodingException :  Encoding이 지원되지 않는 문자열 변환시
    */
    public static  String toConvert(String str, String src_enc, String dest_enc)  throws java.io.UnsupportedEncodingException
    {
        if (str == null)
            return "";
        else
            return new String( str.getBytes(src_enc), dest_enc );
    }

    /**
    * Null String을 "" String으로 바꿔준다.
    * @author 민선기
    *
    * @param str   Null 문자열
    *
    * @return "" 문자열(null이 아닐 경우는 변환할 문자열이 그대로 리턴)
    */
    static public String NVL(String str)
    {
        if(str == null)
            return "";
        else
            return str.trim();
    }

    // 문자열이 null인경우 replace_str을 Return한다.
    // 사용 예) 테이블의 <td>str</td>에서 str이 null인 경우
    // replate_str이 &nbsp;로 지정한다.
    /**
    * 문자열이 null인경우 replace_str을 Return한다.
	* 사용 예) 테이블의 <td>str</td>에서 str이 null인 경우
	* replate_str이 &nbsp;로 지정한다.
    * @author 민선기
    *
    * @param str Null 문자열
    * @param replace_str 변환할 문자열
    * @return 변환할 문자열
    */
    static public String NVL(String str, String replace_str)
    {
        if( str == null ||  str.length()<=0) return replace_str;
        else return str;
    }

    	

    /**
     * 숫자 포맷을 ,구분자로 표시 
     * @author 권연선
     * 
     * @param str Null 문자열
     * @param replace_str 변환할 문자열
     * @return 변환할 문자열
     */
    public static String getToCommaInt(String stText){
    	if(stText == null || stText.trim().equals("")) return "";
	    String ch = "#,###,##0";
	    java.text.DecimalFormat df = new java.text.DecimalFormat(ch);
	    String stResult = df.format(Integer.parseInt(stText));
	    return stResult;
    }
    /**
     * CLOB 데이터를 String으로 변 
     * @author 박진식
     * 
     * @param str Null 문자열
     * @param replace_str 변환할 문자열
     * @return 변환할 문자열
     */
    public static String clobToString(Clob clob) 
	{
    	String clobString = "";
   		try 
		{
			
			
			if (clob != null)
			{	
				Reader reader = clob.getCharacterStream();		
						
				BufferedReader clobReader = new BufferedReader(reader);                	

				StringWriter clobWriter = new StringWriter();
    	   		char[] buffer = new char[1024];
   	    		int size = 0;
                	
   	    		while((size = clobReader.read(buffer, 0, 1024)) != -1) 
					clobWriter.write(buffer, 0, size);
			
				clobString = clobWriter.toString();

			}

			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
	   	} 
	return clobString;
	}
    
    /**
     * 사용자가 선택한 길이만큼 문자열을 보여주고 그 이후부분은 사용자가 입력한 스트링으로 대체 
     * @author 박진식
     * 
     * @param str Null 문자열
     * @param replace_str 변환할 문자열
     * @return 변환할 문자열
     */
    static public String cutString(String src, int str_length, String att_str)
	{
		int ret_str_length = 0;
        
		String ret_str = new String("");

		if (src == null)
		{
			return ret_str;
		}

		// 현재 환경의 Character length를 구한다.
		String tempMulLanChar = new String("가");
		int lanCharLength = tempMulLanChar.length();
        
		// Character가 중간에 잘리지 않게 하기위해 넣은 변수
		int multiLanCharIndex = 0;

		for (int i=0; i<src.length(); i++)
		{
			ret_str += src.charAt(i);
            
			if (src.charAt(i)>'~')
			{
				ret_str_length = ret_str_length + 2/lanCharLength;
				multiLanCharIndex++;
			}
			else
			{
				ret_str_length = ret_str_length + 1;
			}
			if(ret_str_length >= str_length && (multiLanCharIndex%lanCharLength) == 0  )
			{
				ret_str += nchk(att_str);
				break;
			}
		}

		return ret_str;
	}
    /**
     * NULL 값을 ""로 변경  
     * @author 박진식
     * 
     * @param str Null 문자열
     * @param replace_str 변환할 문자열
     * @return 변환할 문자열
     */
    
    static public String nchk(String str)
	{
		if (str == null || str.equals(""))
			return "";
		else
			return str;
	}
    
    /**
     * NULL 값을 ""로 변경  
     * @author 박진식
     * 
     * @param obj Object
     * @param replace_str 변환할 문자열
     * @return 변환할 문자열
     */
    
    static public String nchk(Object obj)
	{
		if (obj == null)
			return "";
		else
			return obj.toString();
	}    
    /**
     * NULL 값을 ""로 변경  
     * @author 박진식
     * 
     * @param str Null 문자열
     * @param replace_str 변환할 문자열
     * @return 변환할 문자열
     */
    
    static public String nchk(String str, String dstr)
	{
		if (str == null || str.equals("")) {
			if (dstr == null || dstr.equals("")) {
				return "";
			} else {
				return dstr;
			}
		} else {
			return str;
		}
	}
    
    /**
     * NULL 값을 ""로 변경  
     * @author 박진식
     * 
     * @param str Object
     * @param replace_str 변환할 문자열
     * @return 변환할 문자열
     */
    
    static public String nchk(Object obj, String dstr)
	{
		if (obj == null)
			if (dstr == null)
				return "";
			else
				return dstr;
		else
			return obj.toString();
	}
		
	/**
	 * right
	 *
	 * 주어진 값을 오른쪽에서 부터 len만큼 짤라서 반환
	 *
	 * @param s 소스 스트링
	 * @param len 자를 길이
	 *
	 * @return 주어진 스트링을 len만큼 오른쪽에서 자른 값
	 */
	public static String right(String s, int len) {
	  if (s == null)
		return "";
	  int L = s.length();
	  if (L <= len)
		return s;
	  return s.substring(L - len, L);
	}

	/**
	 * right
	 *
	 * 주어진 값을 왼쪽에서 부터 len만큼 짤라서 반환
	 *
	 * @param s 소스 스트링
	 * @param len 자를 길이
	 *
	 * @return 주어진 스트링을 len만큼 왼쪽에서 자른 값
	 */
	public static String left(String s, int len) {
	  if (s == null)
		return "";
	  if (s.length() <= len)
		return s;
	  return s.substring(0, len);
	}
	
	
	/**
	 * right
	 *
	 * 주어진 값을 왼쪽에서 부터 len만큼 짤라서 반환
	 *
	 * @param s 소스 스트링
	 * @param len 자를 길이
	 *
	 * @return 주어진 스트링을 len만큼 왼쪽에서 자른 값
	 */
	static public String cutLeft(String s, int len, String replace_str) {
	  if (s == null){
		return "";
	  }else if (s.length() <= len){
		return s;
	  }else {
	  	return s.substring(0, len)+replace_str;
	  }
	}	
	
	/**
	 * 영문 or 한글 or 숫자로 된 문자열인지  체크(정규표현식)
	 * 
	 * @param s	소스 스트링
	 * @return	정규표현식을 만족하면 true, 만족하지 않으면 false
	 */
	public static boolean isAlphaHangulNumOnly(String s) {
		
		Pattern pattern = Pattern.compile("^[a-zA-Zㄱ-힣0-9]+");
		Matcher match = pattern.matcher(s);
		
		if(match.find()){	
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 한글로만 된 문자열인지 체크(정규표현식)
	 * 
	 * @param s	소스 스트링
	 * @return	정규표현식을 만족하면 true, 만족하지 않으면 false
	 */
	public static boolean isHangulOnly(String s) {
		
		Pattern pattern = Pattern.compile("^[ㄱ-힣]+");
		Matcher match = pattern.matcher(s);
		
		if(match.find()){	
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 이메일 형식 체크 (정규표현식)
	 * @param s	소스 스트링
	 * @return	정규표현식을 만족하면 true, 만족하지 않으면 false
	 */
	public static boolean isEmail(String s) {
		
	    Pattern pattern = Pattern.compile("\\w+[@]\\w+\\.\\w+\\.*\\w*");
	    Matcher match = pattern.matcher(s);
	    return match.matches();
	}
	
	/**
	 * isAcceptedAlpha (정규표현식 - 한글을 제외한 영문, 특수문자 허용, 공백 불허)
	 * @param s 소스 스트링
	 * @return 정규표현식을 만족하면 true, 만족하지 않으면 false
	 */
	public static boolean isAcceptedAlpha(String s) {
		
		Pattern pattern = Pattern.compile("[^가-힣\\s]");
	    Matcher match = pattern.matcher(s);
	    
		if(match.find()){	
			return true;
		}else{
			return false;
		}
	}
}






