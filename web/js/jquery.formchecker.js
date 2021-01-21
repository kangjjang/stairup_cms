/*
--------------------------------------------------------------------------------------------------------------------------------------------
Redbrush javascript Validation library version 1.61
--------------------------------------------------------------------------------------------------------------------------------------------
메소드														설명												
--------------------------------------------------------------------------------------------------------------------------------------------
[jQuery Object Test : Validator]
boolean		isEmpty(target, message, focus)					target이 비었는지 확인, 비었을 경우 message출력, focus이동(자동 trim)			
boolean		isEquals(target1, target2, message, focus)		target1, target2의 값이 일치하는지 확인, 일치하는 경우 message출력, focus이동	
boolean		testValue(target, regexp, message, focus)		target의 해당 정규식에 맞는지 확인, 맞지 않을 경우 message출력, focus이동		

[Value Test : Validator]
boolean		isValidSocialNumber(val)						주민등록번호가 유효한지 검사 ("-" 포함되어 있을 경우 제외하고 검사)

[String Utils]


String		trim(string)						string의 공백 제거										1.0
int			getLength(string)					문자열 길이를 바이트 단위로 리턴한다.						1.0
boolean		checkLength(element, min, max)		element.value.length가 min, max 이상인지 확인				1.0
boolean		isNumber(id)						element.value가 숫자인지 판별								1.0
boolean		isInteger(element)					element.value가 정수형인지 판별							1.0
boolean		isFloat(element)					element.value가 부동소수형인지 판별						1.0
boolean		isAlphabet(element)					element.value가 영문자인지 판별							1.0
boolean		isAlphabetString(String)			string가 영문자인지 판별									1.0
boolean		isAlphaNumeric(element)				element.value가 영숫자인지 판별							1.0
boolean		isKorean(String)					string이 한글인지 판별									1.0
boolean		isUpperCase(element)				element.value가 모두 UpperCase인지 판별					1.0
boolean		isLowerCase(element)				element.value가 모두 LowerCase인지 판별					1.0
boolean		checkAlphabet(char)					char이 영문자인지 판별										1.0
boolean		checkNumber(char)					char이 숫자인지 판별										1.0
boolean		checkUpperCase(char)				char이 UpperCase인지 판별									1.0
boolean		checkLowerCase(char)				char이 LowerCase인지 판별									1.0
boolean		regTest(element, expr)				element.value를 정규식판별								1.0
String		getSelectedValue(element)			select폼 element의 선택된 값을 리턴						1.0
String		getSelectedIndex(element)			select폼 element의 선택된 인덱스를 리턴						1.0
void		selectValue(element, value)			select폼 element의 value값을 가진 것을 선택				1.0
void		selectIndex(element, index)			select폼 element의 index를 선택							1.0
void		selectText(element, text)			select폼 element의 text를 선택							1.0
void		checkValue(formId, element.name, value)	checkbox name과 value가 일치하면 선택					1.0
void		setChecked(formId, element.name, value)	checkValue와 동일한 기능								1.0
void		getCheckedValue(formId, name)			checkbox중 name과 일치하면서 check된 것의 값을 리턴			1.0
void		isChecked(id)						checkbox가 체크되었는지 확인								1.0
String		getFileName(String path)			path를 받아서 파일 이름을 분리해낸다.						1.0
String		getFileExt(String fileName)			fileName을 받아서 파일 확장자를 분리해낸다.					1.0
boolean		isValidFile(fileName, ext)			fileName과 ,로 분리된 확장자를 받아서 유효한 파일인지 확인	1.0
void		goUrl(String url)					url로 이동												1.0
void		openImage(path, title)				path 이미지창 띄우기										1.12
boolean		isValidDate(String date)			String의 처음 8자리 숫자를 가져와서 yyyyMMdd 유효성 판별	 	1.0
void		showBlock(id)						id요소를 보여준다.										1.0
void		hideBlock(id)						id요소를 숨긴다.											1.0
void		displayBlock(id)					id를 보여주거나 숨긴다.(toggle)							1.0
void		checkSCN(String scn)				주민번호 유효성 체크 13자리									1.0
void        setDate(form.year, form.month, form.day, year, month, day, minYear, maxYear) select form을 fill 하고 year, month, day로 채운다. 1.0
void		numToHan(element1, element2)		element1의 숫자를 한글숫자로 바꾸어 element2에 쓰는 함수		1.0
String		getKoreanNumber(num)				num을 받아서 한글숫자로 넘겨주는 함수						1.0
String		makeUnitNumber(str)					str을 ,를 붙인 숫자로 바꿔어주는 함수						1.0
String		resetUnitNumber(str)				str을 ,를 뺀 숫자로 바꾸어주는 함수							1.0
String		setUnitAmount(element)				element을 ,를 붙인 숫자로 바꾸어주는 함수					1.0
String		resetUnitAmount(element)			element을 ,를 뺀 숫자로 바꾸어주는 함수	 					1.0
String		addMonth(String, delta)				yyyyMMdd 형식의 날짜에서 delta월을 더해준다.				1.0
boolean		isValidMail(id)						이메일이 알맞은 형식인지 체크한다.							1.0
boolean		isValidEmail(id)					이메일이 알맞은 형식인지 체크한다.							1.0
boolean		checkEmail(id)						이메일이 알맞은 형식인지 체크한다.							1.0
void		addItem(id, value, text)			select element에 item을 추가한다.							1.0
void		checkDuplicateItemByValue(id, value)	select element에 item을 추가한다.							1.0
void		checkDuplicateItemByText(id, value)	select element에 item을 추가한다.							1.0
void		deleteItemByIndex(id, index)		select element에 index 위치의 item을 삭제한다.				1.0
void		deleteItemBySelectedIndex(id)		select element에 selected index 위치의 item을 삭제		1.0
void		selectUp(selectElement)				select element에 선택된 아이템을 위로 한칸 올린다.			1.0
void		selectDown(selectElement)			select element에 선택된 아이템을 아래로 한칸 내린다.			1.0
void		selectAll(id)						select element의 아이템을 모두 선택한다.					1.0
void		optionSwap(selectElement, srcIndex, targetIndex) select element 두 Index 아이템을 Swap		1.0
void		setOptions(selectElement, src, delim) select element에 src를 delim으로 split하여 저장한다.		1.0
void		setCookie(name, value, expiredays)  cookie에 name=value를 expiredays 기간동안 설정				1.0
String		getCookie(name)						cookie에 name에 해당하는 값을 가져온다.						1.0
void		setFocus(id)						element에 focus를 줌										1.0
void		setDateFormatDash(element)			element에 yyyyMMdd를 입력하면 yyyy-MM-dd로 자동변환			1.0
void		checkAll(form, elementName)			form내의 name으로 명명된 element 모두 check				1.0
void		uncheckAll(form, elementName)			form내의 name으로 명명된 element 모두 uncheck				1.0
---------------------------------------------------------------------------------------------------------------------------*/

/* 
	Validator Factory
*/
var Validator = {
	/*-----------------------------------------------
	jQuery Object Test
	-------------------------------------------------*/
	// 주어진 Form Value가 비었는지 검사 (자동 Trim됨)
	isEmpty : function(target, message, focusTarget) {
		var str = $.trim($(target).val());

		if (str.length==0) {
			if (message) {
				alert(message);

				if (focusTarget) {
					$(focusTarget).focus();
				} else {
					$(target).focus();
				}
			}
			return true;
		}

		return false;
	},

	// 두 개의 Form value가 같은지 확인
	isNotEquals: function(target1, target2, message) {
		if ($(target1).val()!=$(target2).val()) {
			alert(message);
			$(target1).focus();
			return true;
		} else {
			return false;
		}
	},

	// 주어진 Form Value가 숫자인지 검사 (자동 Trim됨)
	isNotNumber : function(target, message, focusTarget) {
		var str = $.trim($(target).val());

		if (isNaN(str)) {
			if (message) {
				alert(message);

				if (focusTarget) {
					$(focusTarget).focus();
				} else {
					$(target).focus();
				}
			}
			return true;
		}

		return false;
	},

	// 주어진 Form Value에 대한 정규식 검사
	testValue: function(target, expression, message) {
		var regExp=new RegExp(expression);

		if (!regExp.test($(target).val())) {
			alert(message);
			$(target).focus();
		
			return false;
		}

		return true;
	},
	// 주민등록 번호 체크
	isNotValidSocialNumber: function(target, message) {
		 var socnoStr = $(target).val().replace("-", "");
		 a = socnoStr.substring(0, 1);
		 b = socnoStr.substring(1, 2);
		 c = socnoStr.substring(2, 3);
		 d = socnoStr.substring(3, 4);
		 e = socnoStr.substring(4, 5);
		 f = socnoStr.substring(5, 6);
		 g = socnoStr.substring(6, 7);
		 h = socnoStr.substring(7, 8);
		 i = socnoStr.substring(8, 9);
		 j = socnoStr.substring(9, 10);
		 k = socnoStr.substring(10, 11);
		 l = socnoStr.substring(11, 12);
		 m = socnoStr.substring(12, 13);

		 temp=a*2+b*3+c*4+d*5+e*6+f*7+g*8+h*9+i*2+j*3+k*4+l*5;
		 temp=temp%11;
		 temp=11-temp;
		 temp=temp%10;
		
		 if (temp != m) {
			alert(message)
			$(target).focus();
			return true; 
		 } else {
			return false;
		 }
	},

	// 이메일 체크
	isNotValidEmail: function(target, message) {
		var reg = new RegExp("^[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+)*@[0-9a-zA-Z-]+(\.[0-9a-zA-Z-]+)*$");
		
		return !this.testValue(target, reg, message);
	},

	// 리스트(selectBox)가 비어있는지 확인
	isEmptyList: function(target, message, focusTarget) {
		if ($(target + " option").size()==0) {
			alert(message);
			if (focusTarget) {
				$(focusTarget).focus();
			} else {
				$(target).focus();
			}
			return true;
		}

		return false;
	},

	// target의 value가 알파벳만으로 구성되어 있는지 확인
	isAlphabetString: function(target) {
		var str = $(target).val();

		for ( i=0; str.length >  i ; i++) {
			if(this.checkAlphabet(str.charAt(i))) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	},

	// target의 value가 숫자로 구성되어 있는지 확인
	isNumberString: function(target) {
		var str = $(target).val();

		for ( i=0; str.length >  i ; i++) {
			if (i==0 && str.charAt(i)=="-") continue;

			if(this.checkNumber(str.charAt(i))) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	},

	checkAlphabet: function(ch) {
		if ((ch  >="a"  &&  "z"  >= ch) || (ch  >="A"  &&  "Z"  >= ch))
			return true;
		
		return false;
	},

	checkNumber: function(ch) {
		if (ch  >="0"  &&  "9"  >= ch)
			return true;
		
		return false;
	},

	/*-----------------------------------------------
	jQuery Value Test
	-------------------------------------------------*/
	// 두 개의 Form value가 같은지 확인
	isValueNotEquals: function(target, value, message, focus) {
		if ($(target).val()!=value) {
			alert(message)
			if (focus) $(focus).focus();
			return true;
		} else {
			return false;
		}
	},

	/*-----------------------------------------------
	jQuery Date Test pattern : yyyy-MM-dd HH:mm
	-------------------------------------------------*/
	isDateTimeAfterNow: function(targetDate, targetTime, message, focus) {
		if ($(targetDate).val()=="") {
			alert(message)
			if (focus) $(focus).focus();
			return true;
		} else if ($(targetTime).val()=="") {
			alert(message)
			if (focus) $(focus).focus();
			return true;
		} else {
			var datetime = $(targetDate).val() + " " + $(targetTime).val();
			var year = parseInt(datetime.substr(0, 4), 10);
			var month = parseInt(datetime.substr(5, 2), 10);
			var day = parseInt(datetime.substr(8, 2), 10);
			var hour = parseInt(datetime.substr(11, 2), 10);
			var minute = parseInt(datetime.substr(14, 2), 10);

			var d = new Date(year, month-1, day, hour, minute, 0);
			var now = new Date();

			if (d>now) {
				alert(message);
				return true;
			}

			return false;
		}
	},

	isDateTimeBeforeNow: function(targetDate, targetTime, message, focus) {
		if ($(targetDate).val()=="") {
			alert(message)
			if (focus) $(focus).focus();
			return true;
		} else if ($(targetTime).val()=="") {
			alert(message)
			if (focus) $(focus).focus();
			return true;
		} else {
			var datetime = $(targetDate).val() + " " + $(targetTime).val();
			var year = parseInt(datetime.substr(0, 4), 10);
			var month = parseInt(datetime.substr(5, 2), 10);
			var day = parseInt(datetime.substr(8, 2), 10);
			var hour = parseInt(datetime.substr(11, 2), 10);
			var minute = parseInt(datetime.substr(14, 2), 10);

			var d = new Date(year, month-1, day, hour, minute, 0);
			var now = new Date();

			if (d<now) {
				alert(message);
				return true;
			}

			return false;
		}
	}
};

/*--------------------------------------------------------------------------------------------------------------------------------------------
String Utils
--------------------------------------------------------------------------------------------------------------------------------------------*/
// Trim
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

// LTrim
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/, "");
}

// RTrim
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/, "");    
}

// Byte Count
String.prototype.byteCount = function() {
	var cnt = 0;

	for (var i = 0; i < this.length; i++) {
		if (this.charCodeAt(i) > 127)
			cnt += 2;
		else
			cnt++;
	}

	return cnt;
}

// ToInt
String.prototype.toInt = function() {
	if(!isNaN(this)) {
		return parseInt(this);
	} else {
		return null;    
	}
}

// GetNum
String.prototype.getNum = function() {
	return (this.trim().replace(/[^0-9]/g, ""));
}


/*--------------------------------------------------------------------------------------------------------------------------------------------
jQuery Etc Extension Utils
--------------------------------------------------------------------------------------------------------------------------------------------*/
// NextFocus : KeyUp시 해당 Element의 값 길이가 length가 되면, target으로 자동 focus 이동
$.fn.nextFocus = function(target) {
	$(this).bind("keyup", function(e) {
		var maxlength = 0;
		if ($(this).attr("maxlength")) {
			maxlength = parseInt($(this).attr("maxlength"), 10);
		}

		if (maxlength==$(this).val().length) {
			$(target).focus();
		}
	});

	return this;
}

// NumberInput : 숫자만 입력가능하도록 함.
$.fn.numberInput = function() {
	$(this).bind("keydown", function(e) {
		//입력 허용 키
		if( ( e.keyCode >=  48 && e.keyCode <=  57 ) ||   //숫자열 0 ~ 9 : 48 ~ 57
			( e.keyCode >=  96 && e.keyCode <= 105 ) ||   //키패드 0 ~ 9 : 96 ~ 105
			e.keyCode ==   8 ||    //BackSpace
			e.keyCode ==  46 ||    //Delete
			e.keyCode == 110 ||    //소수점(.) : 문자키배열
			e.keyCode == 190 ||    //소수점(.) : 키패드
			e.keyCode ==  37 ||    //좌 화살표
			e.keyCode ==  39 ||    //우 화살표
			e.keyCode ==  35 ||    //End 키
			e.keyCode ==  36 ||    //Home 키
			e.keyCode ==   9 ||    //Tab 키
			e.keyCode ==  13	   // Enter 키

		) {
			return true;
		} else {
			// - 키
			if (e.keyCode==189)	{

				if ($(this).val().charAt(0)!="-") {
					$(this).val("-" + $(this).val());
				} else {
					$(this).val($(this).val().substring(1, $(this).val().length));
				}
			}

			return false;
		}
	});

	return this;
}

// AlphabetInput : 숫자만 입력가능하도록 함.
$.fn.alphabetInput = function() {
	$(this).bind("keydown", function(e) {
		//입력 허용 키
		if( ( e.keyCode >=  'a' && e.keyCode <=  'z' ) ||   //숫자열 0 ~ 9 : 48 ~ 57
			( e.keyCode >=  'A' && e.keyCode <=  'Z') ||   //키패드 0 ~ 9 : 96 ~ 105
			e.keyCode ==   8 ||    //BackSpace
			e.keyCode ==  46 ||    //Delete
			//e.keyCode == 110 ||    //소수점(.) : 문자키배열
			//e.keyCode == 190 ||    //소수점(.) : 키패드
			e.keyCode ==  37 ||    //좌 화살표
			e.keyCode ==  39 ||    //우 화살표
			e.keyCode ==  35 ||    //End 키
			e.keyCode ==  36 ||    //Home 키
			e.keyCode ==   9       //Tab 키
		) {
			return true;
		} else {
			return false;
		}
	});

	return this;
}

// AlphaNumericInput : 숫자만 입력가능하도록 함.
$.fn.alphaNumericInput = function() {
	$(this).bind("keydown", function(e) {
		//입력 허용 키
		if( ( e.keyCode >=  'a' && e.keyCode <=  'z' ) ||   //숫자열 0 ~ 9 : 48 ~ 57
			( e.keyCode >=  'A' && e.keyCode <=  'Z') ||   //키패드 0 ~ 9 : 96 ~ 105
			( e.keyCode >=  48 && e.keyCode <=  57 ) ||   //숫자열 0 ~ 9 : 48 ~ 57
			( e.keyCode >=  96 && e.keyCode <= 105 ) ||   //키패드 0 ~ 9 : 96 ~ 105
			e.keyCode ==   8 ||    //BackSpace
			e.keyCode ==  46 ||    //Delete
			//e.keyCode == 110 ||    //소수점(.) : 문자키배열
			//e.keyCode == 190 ||    //소수점(.) : 키패드
			e.keyCode ==  37 ||    //좌 화살표
			e.keyCode ==  39 ||    //우 화살표
			e.keyCode ==  35 ||    //End 키
			e.keyCode ==  36 ||    //Home 키
			e.keyCode ==   9       //Tab 키
		) {
			return true;
		} else {
			return false;
		}
	});

	return this;
}

/*--------------------------------------------------------------------------------------------------------------------------------------------
Previous Version
--------------------------------------------------------------------------------------------------------------------------------------------*/

// Base on prototype
function checkEmpty(id, message, focus) {
	$(id).value = trim($F(id));
	if (!$(id).present()) {
		alert(message);
		if (focus==undefined) setFocus($(id));
		else setFocus($(focus));
		return true;
	}

	return false;
}

// Base on prototype
function checkFalse(checkValue, id, message, focus) {
	if (!checkValue) {
		alert(message);
		if (focus==undefined) setFocus($(id));
		else setFocus($(focus));
		return true;
	}

	return false;
}

// Base on prototype
function checkEquals(id1, id2, message, focus) {
	if ($F(id1)!=$F(id2)) {
		alert(message);
		if (focus==undefined) setFocus($(id1));
		else setFocus($(focus));
		return false;
	}

	return true;
}

// Base on prototype
function checkValueEquals(id, value, message, focus) {
	if ($F(id)!=value) {
		alert(message);
		if (focus==undefined) setFocus($(id));
		else setFocus($(focus));

		return false;
	}

	return true;
}

// str의 space를 trim
function trim(str) {
	return str.replace(/^(\s+)|(\s+)$/g,""); 
}

// 문자의 길이를 바이트 단위로 리턴한다.
function getByteLength(val) {
      // 입력받은 문자열을 escape() 를 이용하여 변환한다.
      // 변환한 문자열 중 유니코드(한글 등)는 공통적으로 %uxxxx로 변환된다.
      var temp_estr = escape(val);
      var s_index   = 0;
      var e_index   = 0;
      var temp_str  = "";
      var cnt       = 0;

      // 문자열 중에서 유니코드를 찾아 제거하면서 갯수를 센다.
      while ((e_index = temp_estr.indexOf("%u", s_index)) >= 0)  // 제거할 문자열이 존재한다면
      {
        temp_str += temp_estr.substring(s_index, e_index);
        s_index = e_index + 6;
        cnt ++;
      }

      temp_str += temp_estr.substring(s_index);

      temp_str = unescape(temp_str);  // 원래 문자열로 바꾼다.

      // 유니코드는 2바이트 씩 계산하고 나머지는 1바이트씩 계산한다.
      return ((cnt * 2) + temp_str.length) + "";
}

// input type="text" 객체를 받아서 그 안의 값의 길이가 min보다 크고 max보다 작은지 검사
// Unicode 문자의 경우에는 2바이트가 1글자, ASCII 문자의 경우에는 1바이트가 1글자이다.
function checkLength(theForm, minLength, maxLength) {
	var str=theForm.value;

	if (str.length>=minLength && str.length<=maxLength) return true;

	return false;
}	

// Base on prototype
function isNumber(id) {
	var str=$F(id);
	for ( i=0; str.length >  i ; i++) {
        	if(checkNumber(str.charAt(i))) {
      		     continue;
       		} else {
				return false;
        	}
    }
   	return true;
}

function isInteger(element) {
	var str=element.value;
	var value = parseInt(str);

	if (isNaN(value)) {
		element.value = 0;
		return false;
	}
	element.value = value;

   	return true;
}

function isFloat(element) {
	var str=element.value;
	var value = parseFloat(str);

	if (isNaN(value)) {
		element.value = 0;
		return false;
	}

	element.value = value;

   	return true;
}

function isFloat(element) {
	var str=theForm.value;
	for ( i=0; str.length >  i ; i++) {
        	if(checkNumber(str.charAt(i))) {
      		     	continue;
       		} else {
				return false;
        	}
    }
   	return true;
}

// input type="text" 객체를 받아서 그 안의 값이 영문인지 검사하는 함수
// isAlphabet(textForm)
function isAlphabet(theForm) {
	var str=theForm.value;
	for ( i=0; str.length >  i ; i++) {
        	if(checkAlphabet(str.charAt(i))) {
      		     	continue;
       		} else {
				return false;
        	}
    }
   	return true;
}

// input type="text" 객체를 받아서 그 안의 값이 영문인지 검사하는 함수
// isAlphabet(textForm)
function isAlphabetString(str) {
	for ( i=0; str.length >  i ; i++) {
        	if(checkAlphabet(str.charAt(i))) {
      		     	continue;
       		} else {
				return false;
        	}
    }
   	return true;
}

// input type="text" 객체를 받아서 그 안의 값이 영숫자인지 검사하는 함수
// isAlphaNumeric(textForm)
function isAlphaNumeric(theForm) {
	var str=theForm.value;
	for ( i=0; str.length >  i ; i++) {
        	if(checkAlphabet(str.charAt(i)) || checkNumber(str.charAt(i))) {
      		     	continue;
       		} else {
				return false;
        	}
    }
   	return true;
}

// input type="text" 객체를 받아서 그 안의 값이 영문 대문자인지 검사하는 함수
// isAlphabet(textForm)
function isUpperCase(theForm) {
	var str=theForm.value;
	for ( i=0; str.length >  i ; i++) {
        	if(checkUpperCase(str.charAt(i))) {
      		     	continue;
       		} else {
				return false;
        	}
    }
   	return true;
}

// input type="text" 객체를 받아서 그 안의 값이 영문 소문자인지 검사하는 함수
// isAlphabet(textForm)
function isLowerCase(theForm) {
	var str=theForm.value;
	for ( i=0; str.length >  i ; i++) {
        	if(checkLowerCase(str.charAt(i))) {
      		     	continue;
       		} else {
				return false;
        	}
    }
   	return true;
}

// char 하나가 알파벳인지 검사하는 함수
/*function checkAlphabet(ch) {
	if ((ch  >="a"  &&  "z"  >= ch) || (ch  >="A"  &&  "Z"  >= ch))
		return true;
	
	return false;
}*/

// char 하나가 숫자 인지 검사하는 함수
function checkNumber(ch) {
	if (ch  >="0"  &&  ch<="9")
		return true;
	
	return false;
}

// char 하나가 대문자인지 검사하는 함수
function checkUpperCase(ch) {
	if (ch  >="A"  &&  "Z"  >= ch)
		return true;
	
	return false;
}

// char 하나가 소문자인지 검사하는 함수
function checkUpperCase(ch) {
	if (ch  >="a"  &&  "z"  >= ch)
		return true;
	
	return false;
}

// 정규표현식을 받아서 Test하는 함수
function regTest(theForm, expression) {
    var regExp=new RegExp(expression);

	return regExp.test(theForm.value);

}

// 셀렉트 박스에서 선택된 option의 값을 돌려받는 함수
function getSelectedValue(theForm) {
	return theForm.options[theForm.options.selectedIndex].value;
}

// 셀렉트 박스에서 선택된 option의 인덱스를 돌려받는 함수
function getSelectedIndex(theForm) {
	return theForm.options.selectedIndex;
}

// 셀렉트 박스에서 해당 값으로 선택한다.
function selectValue(theForm, value) {
	for (i=0;i<theForm.options.length; i++ ) {
		if (theForm.options[i].value==value) {
			theForm.options.selectedIndex = i;
		}
	}
}

function checkValue(formId, name, value) {
	for (i=0;i<$(formId).elements.length; i++ ) {
		if ($(formId).elements[i].name==name && $(formId).elements[i].value==value) {
			$(formId).elements[i].checked = true;
		}
	}
}

function setChecked(theForm, name, value) {
	checkValue(theForm, name, value);
}

function getCheckedValue(formId, id) {
	for (i=0;i<$(formId).elements.length; i++ ) {
		if ($(formId).elements[i].name==id && $(formId).elements[i].checked) {
			return $(formId).elements[i].value;
		}
	}
}

function isChecked(id) {
	if ($(id).type=="checkbox" || $(id).type=="radio")
	{
		return $(id).checked;
	}

	return false;
}

// 셀렉트 박스에서 해당 index로 선택한다.
function selectIndex(theForm, index) {
	theForm.options.selectedIndex = index;
}

// 셀렉트 박스에서 해당 text로 선택한다.
function selectText(theForm, text) {
	for (i=0;i<theForm.options.length; i++ ) {
		if (theForm.options[i].text==text) {
			theForm.options.selectedIndex = i;
		}
	}
}

// 파일 경로를 받아서 파일 이름을 분리해낸다.
function getFileName(path) {
	split=path.split("\\");

	if (split.length>0)	{
		return split[split.length-1];
	} else {
		return "";
	}
}

// 파일 확장자를 분리해낸다.
function getFileExt(fileName) {
	split=fileName.split(".");

	if (split.length>0)	{
		return split[split.length-1];
	} else {
		return "";
	}
}

// 한글인지 아닌지 구별해낸다.
function isKorean(str) {
	var check=false;
	for (i=0; i<str.length; i++) {
		if (str.charCodeAt(i)>255) {
			check=true;
		}
	}

	return check;
}

// 파일 확장자를 받아서 유효한 파일인지 확인한다.
function isValidFile(fileName, fileExt) {
	var ext=getFileExt(fileName);

	if (ext!="" && fileExt!="") {
		var exts=fileExt.split(",");

		for (i=0; i<exts.length ; i++) {
			if (ext==exts[i].toLowerCase())
				return true;
		}
	}

	return false;
}

function goURL(url) {
	location.href=url;
}

// version 1.12
/*
function openImage(path, title) { 
var imgwin = window.open("",'WIN','scrollbars=no,status=no,toolbar=no,resizable=no,location=no,menu=no,width=10,height=10'); 
imgwin.focus(); 
imgwin.document.open(); 
imgwin.document.write("<html>\n"); 
imgwin.document.write("<head>\n"); 
imgwin.document.write("<title>" + title + "</title>\n"); //오픈창 타이틀 이름 지정하는 부분 

imgwin.document.write("<script>\n"); 
imgwin.document.write("function resize() {\n"); 
imgwin.document.write("pic = document.il;\n"); 
//imgwin.document.write("alert(eval(pic).height);\n"); 
imgwin.document.write("if (eval(pic).height) { var name = navigator.appName\n"); 
imgwin.document.write(" if (name == 'Microsoft Internet Explorer') { myHeight = eval(pic).height + 0; myWidth = eval(pic).width + 0;\n"); 
imgwin.document.write(" } else { myHeight = eval(pic).height + 9; myWidth = eval(pic).width; }\n"); 
imgwin.document.write(" clearTimeout();\n"); 
imgwin.document.write(" var height = screen.height;\n"); 
imgwin.document.write(" var width = screen.width;\n"); 
imgwin.document.write(" var leftpos = width / 2 - myWidth / 2;\n"); 
imgwin.document.write(" var toppos = height / 2 - myHeight / 2; \n"); 
imgwin.document.write(" self.moveTo(leftpos, toppos);\n"); 
imgwin.document.write(" self.resizeTo(myWidth+20, myHeight+30);\n"); 
imgwin.document.write("}else setTimeOut(resize(), 100);}\n"); 
imgwin.document.write("</script>\n"); 

imgwin.document.write("</head>\n"); 
imgwin.document.write('<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0" bgcolor="#FFFFFF">\n'); 
imgwin.document.write("<center>\n"); 
imgwin.document.write("<img border=0 src="+path+" xwidth=100 xheight=9 name=il onload='resize();' onclick='window.close();'>\n"); 
imgwin.document.write("</center>\n"); 
imgwin.document.write("</body>\n"); 
imgwin.document.close(); 
}*/
//----------------------------------------------------------------------------
function isValidDate(str){ 
	str = makeDate(str);
	if( str.length == 8 ){ 
	  vDate = new Date(); 
	  vDate.setFullYear(str.substring(0, 4)); 
	  vDate.setMonth(str.substring(4, 6)-1);  /* -- -1을 해주었숩니당 --*/ 
	  vDate.setDate(str.substring(6)); 

	  if( vDate.getFullYear() != str.substring(0, 4) || 
	   vDate.getMonth()    != (str.substring(4, 6)-1) ||  /* -- -1을 해주었숩니당 --*/ 
	   vDate.getDate()     != str.substring(6) ){ 
	   return false; 
	  } 
	  return true;
	} 
} 

function makeDate(str) {
	var dateStr="";
	for (i=0; i<str.length; i++) {
		if (checkNumber(str.charAt(i))) {
			dateStr += str.charAt(i);
		}
	}

	return dateStr;
}
//----------------------------------------------------------------------------------------

function showBlock(id) {
	var element = eval(id);

	element.style.display = "block";
//	element.style.position = "relative";
}

function hideBlock(id) {
	var element = eval(id);

	element.style.display = "none";
//	element.style.position = "absolute";
}

function displayBlock(id) {
	var element = eval(id);

	if (element.style.display=="none") {
		showBlock(id);
	} else {
		hideBlock(id);
	}
}

/*******error 처리위한 스크립트 시작 ***************/
/*window.onerror = ErrorSetting 

var e_msg="";
var e_file="";
var e_line=""; 


function ErrorSetting(msg, file_loc, line_no) {
     e_msg=msg;
     e_file=file_loc;
     e_line=line_no;
     return true; 
} 
*/

/*******error 처리위한 스크립트 끝 ***************/

// 주민등록 번호 체크
function checkSCN(socno)
{
     var socnoStr = socno.toString();
     a = socnoStr.substring(0, 1);
     b = socnoStr.substring(1, 2);
     c = socnoStr.substring(2, 3);
     d = socnoStr.substring(3, 4);
     e = socnoStr.substring(4, 5);
     f = socnoStr.substring(5, 6);
     g = socnoStr.substring(6, 7);
     h = socnoStr.substring(7, 8);
     i = socnoStr.substring(8, 9);
     j = socnoStr.substring(9, 10);
     k = socnoStr.substring(10, 11);
     l = socnoStr.substring(11, 12);
     m = socnoStr.substring(12, 13);

     temp=a*2+b*3+c*4+d*5+e*6+f*7+g*8+h*9+i*2+j*3+k*4+l*5;
     temp=temp%11;
     temp=11-temp;
     temp=temp%10;
    
     if(temp == m)
        return true;
     else
        return false; 
}


// select box를 날짜로 채운다.
/*
function setDate(form.year, form.month, form.day, year, month, day, minYear, maxYear)
{
  var current, year, month, day, days, i, j;
  current = new Date();
  year = (year) ? year : current.getFullYear();
  for (i=maxYear, j=0; i >= minYear; i--, j++) form.year.options[j] = new Option(i, i);
  month = (month) ? month : current.getMonth()+1;
  for (i=0; i < 12; i++) {
    j = (i < 9) ? '0'+(i+1) : i+1;
    form.month.options[i] = new Option(j, j);
  }
  day = (day) ? day : current.getDate();
  days = new Date(new Date(year, month, 1)-86400000).getDate();
  from.day.length = 0;
  for (i=0, j; i < days; i++) {
    j = (i < 9) ? '0'+(i+1) : i+1;
    from.day.options[i] = new Option(j, j);
  }
  form.year.value = year;
  form.month.options[month-1].selected = true;
  form.day.options[day-1].selected = true;
}*/


//==================================================================================================
var han; 
var flag = true; // 단위 판별 플래그 

/** 
* 숫자로 된 문자열을 입력받아 숫자에 해당하는 한글명을 리턴한다. 
* @Param : 숫자로된 문자열 
* @Return : 숫자에 해당하는 한글명 
*/ 
function numToHan2(val) { 
	switch(Number(val)) { 
		case 1 : han = "일"; break; 
		case 2 : han = "이"; break; 
		case 3 : han = "삼"; break; 
		case 4 : han = "사"; break; 
		case 5 : han = "오"; break; 
		case 6 : han = "육"; break; 
		case 7 : han = "칠"; break; 
		case 8 : han = "팔"; break; 
		case 9 : han = "구"; break; 
		case 0 : han = ""; break; 
	} 
	return han; 
} 

/** 
* 숫자의 인덱스를 입력받아 단위에 해당하는 한글명을 리턴한다. 
* @Param : 숫자의 인덱스 
* @Return : 인덱스에 해당하는 단위 
*/ 
function danwiToHan(val)  { 
	/* 일 단위는 Skip */ 
	if (val == 0) 
	return ""; 

	var namerji = val % 4; 

	switch(namerji)  { 
		case 0: // 나머지가 '0'이면 만, 억, 조 중에 하나이다. 
			if (val / 4 == 1) { 
				han = "만"; 
				flag = false; 
			} else if (val / 4 == 2) { 
				han = "억"; 
				flag = false; 
			} else if (val / 4 == 3) { 
				han = "조"; 
				flag = false; 
			} 
			break; 
		case 1: // 나머지가 '1'이면 십단위이다. 
			han = "십"; flag = true; break; 
		case 2: // 나머지가 '2'이면 백단위이다. 
			han = "백"; flag = true; break; 
		case 3: // 나머지가 '3'이면 천단위이다. 
			han = "천"; flag = true; break; 
	} 

	return han; 
} 

/** 
* 숫자로된 금액을 한글로 바꾼다. 
* @Param : obj1 - 숫자를 입력할 객체 
* obj2 - 한글을 보여줄 객체 
*/ 
function numToHan(obj1, obj2) { 
	var str = ""; 
	var num = obj1.value; 
	var digit = new Array(); 
	var j = num.length - 1; 

	if (num.length > 16)  { 
		alert("금액이 너무 큽니다."); 
		return; 
	} 

	for (var i = 0; i < num.length; i ++) { 
		digit[j] = num.charAt(i); // 숫자를 거꾸로 담는다.(배열에 reverse()라는 function이 이 역할을 한다.) 
		str += numToHan2(digit[j]); 

		/*------------------------------------------------------------------*/ 
		/* 숫자가 0이 아닐 경우 단위 무조건 판별 */ 
		/* 숫자가 0인 경우 인덱스가 4로 나누어 떨어지는 경우(만, 억, 조) 중 */ 
		/* 하나만 선택할 수 있도록 flag를 둔다. */ 
		/* 잘 이해가 안되면 flag를 빼고 1억을 쳐보셈... ^^; */ 
		/*------------------------------------------------------------------*/ 
		if (digit[j] != "0" || (j % 4 == 0) && flag) 
			str += danwiToHan(j); 

		j --; 
	} 

	if (str != "") 
		obj2.value = str + " 원정"; 
	else 
		obj2.value = ""; 
} 

function getKoreanNumber(obj, unit) { 
	var str = ""; 
	var num = obj; 
	var digit = new Array(); 
	var j = num.length - 1; 

	if (num.length > 16)  { 
		alert("금액이 너무 큽니다."); 
		return; 
	} 

	for (var i = 0; i < num.length; i ++) { 
		digit[j] = num.charAt(i); // 숫자를 거꾸로 담는다.(배열에 reverse()라는 function이 이 역할을 한다.) 
		str += numToHan2(digit[j]); 

		/*------------------------------------------------------------------*/ 
		/* 숫자가 0이 아닐 경우 단위 무조건 판별 */ 
		/* 숫자가 0인 경우 인덱스가 4로 나누어 떨어지는 경우(만, 억, 조) 중 */ 
		/* 하나만 선택할 수 있도록 flag를 둔다. */ 
		/* 잘 이해가 안되면 flag를 빼고 1억을 쳐보셈... ^^; */ 
		/*------------------------------------------------------------------*/ 
		if (digit[j] != "0" || (j % 4 == 0) && flag) 
			str += danwiToHan(j); 

		j --; 
	} 

	if (str != "") 
		return str + unit; 
	else 
		return "";
} 
//===============================================================================================
function makeUnitNumber(str)
{
   var temp = str;

   if (str=="") return "";

   var Money;
   Money = "";
   while(parseInt(str) >= 1000)
   {
      if((parseInt(str)/1000) > 0)
      {
         if((parseInt(str) % 1000) == 0 )
         {
            Money = "," + parseInt(str) % 1000 + "00" + Money;
         }
         else
         {
            if((parseInt(str) % 1000) > 99 )
               Money = "," + parseInt(str) % 1000 + Money;
            else if((parseInt(str) % 1000) > 9 )
               Money = ",0" + parseInt(str) % 1000 + Money;
            else
               Money = ",00" + parseInt(str) % 1000 + Money;
         }
      }
      str = parseInt(str)/1000;
   }
   Money = parseInt(str) + Money;

   if (Money == "NaN") {
	   if (temp.length==1 && temp.charAt(0)=="-") {
		   return "-";
	   }
	   return "";
   }

   if (Money==0)
	   return "";

   return Money;
}

/* -----------------------------------------------------
   금액을 넘겨받아서 , 를 제거하고 반환하는 함수
----------------------------------------------------- */
function parseUnitNumber(value) {
	var num = resetUnitNumber(value);

	if (num=="") return 0;

	return parseInt(num, 10);
}

function resetUnitNumber(Money)
{
   var str;
   str = "";
   for(i=0;i<Money.length;i++)
   {
      if(Money.charAt(i) != ",")
         str = str + Money.charAt(i);
   }
   return str;
}

function setUnitAmount(element) {
	element.value = resetUnitNumber(element.value);
	element.value = makeUnitNumber(element.value);

	if (isEmpty(element)) {
		element.value="";
	}
}

function resetUnitAmount(element) {
	element.value = resetUnitNumber(element.value);

	if (isEmpty(element)) {
		element.value="";
	}
}

function setEmptyIfZero(element) {
	if (element.value=="0")
		element.value = 0;
}

// yyyyMMdd 형식의 날짜에서 delta월을 더해준다.
function addMonth(dateString, delta) {
	if (dateString.length!=8)
		return "";

	date = new Date(eval(dateString.substring(0,4)), eval(dateString.substring(4,6)), eval(dateString.substring(6,8)));
	date.setDate(date.getDate()+1);

	alert(date);
}

// Base on prototype
function isValidMail(id) {
	var reg = new RegExp("^[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+)*@[0-9a-zA-Z-]+(\.[0-9a-zA-Z-]+)*$");

	return reg.test($F(id));
}

// Base on prototype
/*function isValidEmail(id) {
	var reg = new RegExp("^[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+)*@[0-9a-zA-Z-]+(\.[0-9a-zA-Z-]+)*$");

	return reg.test($F(id));
}*/

// Base on prototype
function checkEmail(mail) {
	var reg = new RegExp("^[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+)*@[0-9a-zA-Z-]+(\.[0-9a-zA-Z-]+)*$");

	return reg.test($F(mail));
}

function addItem(id, value, text) {
	if (value.length>0 && text.length>0) {
		$(id).length = $(id).length + 1;
		$(id).options[$(id).length-1] = new Option(value, text);
		return $(id).options[$(id).length-1];
	}
}

function checkDuplicateItemByValue(id, value) {
	for (i=$(id).options.length-1; i>=0; i--)	{
		if ($(id).options[i].value==value) {
			return true;
		}
	}

	return false;
}

function checkDuplicateItemByText(id, value) {
	for (i=$(id).options.length-1; i>=0; i--)	{
		if ($(id).options[i].text==value) {
			return true;
		}
	}

	return false;
}

function deleteItemByIndex(id, delete_index) {
	if (delete_index>-1) {
/*		$(id).options[delete_index].value = $(id).options[$(id).options.length-1].value;
		$(id).options[delete_index].text = $(id).options[$(id).options.length-1].text;
		$(id).options[delete_index].selected = false;*/
		$(id).options[delete_index] = null;
		if ($(id).options[delete_index]!=null) {
			$(id).options[delete_index].selected = false;
		}
	}
}

function deleteItemBySelectedIndex(id) {
	for (i=$(id).options.length-1; i>=0; i--)	{
		if ($(id).options[i].selected) {
			deleteItemByIndex(id, i);
		}
	}
}

function selectUp(element) {
	var count = 0;
	var index = 0;

	for (i=0; i<element.length; i++) {
		if (element.options[i].selected) {
			count++;
			index = i;
		}
	}

	if (count!=1) {
		alert("선택된 아이템이 없습니다.");
		return;
	}

	if (index>0) {
		optionSwap(element, index, index-1);
	}
}

function selectDown(element) {
	var count = 0;
	var index = 0;

	for (i=0; i<element.length; i++) {
		if (element.options[i].selected) {
			count++;
			index = i;
		}
	}

	if (count!=1) {
		alert("선택된 아이템이 없습니다.");
		return;
	}

	if ((index+1)<element.options.length) {
		optionSwap(element, index, index+1);
	}
}

function optionSwap(element, srcIndex, targetIndex) {
	var src = element.options[srcIndex];
	var target = element.options[targetIndex];

	var opSrc;
	var opTarget;

	opSrc = new Option(target.text, target.value);
	opTarget = new Option(src.text, src.value);

	element.options[srcIndex] = opSrc;
	element.options[targetIndex] = opTarget;
	element.options[targetIndex].selected = true;
}

function setOptions(element, src, delim) {
	if (src.length>0) {
		items = src.split(delim);
		if (items.length>0) {
			for (i=0; i<items.length; i++) {
				addItem(element, items[i]);
			}
		}
	}
}

function selectAll(id) {
	for (i=0; i<$(id).options.length; i++) {
		$(id).options[i].selected=true;
	}
}

function setCookie( name, value, expiredays ) {
	var todayDate = new Date();
         
	todayDate.setDate( todayDate.getDate() + expiredays );
         
	document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"
}
 
function getCookie( name ){
	var nameOfCookie = name + "=";
	var x = 0;
 
	while ( x <= document.cookie.length ){
		var y = (x+nameOfCookie.length);
                 
		if ( document.cookie.substring( x, y ) == nameOfCookie ) {
			if ( (endOfCookie=document.cookie.indexOf( ";", y )) == -1 )
                  endOfCookie = document.cookie.length;
            return unescape( document.cookie.substring( y, endOfCookie ) );
        }
                 
		x = document.cookie.indexOf( " ", x ) + 1;
 
        if ( x == 0 ) break;
    }
 
    return "";
}

// id에 해당하는 element focus
function setFocus(id) {
	if ($(id).type!="hidden")	{
		$(id).focus();
	}
}

// id에 해당하는 element가 focus가능한지 확인
function isFocusable(id) {
	if ($(id).type=="hidden")	{
		return false;
	}

	return true;
}

function reverseCheckBox(element, id) {
	if (element.checked) {
		checkAll(id);
	} else {
		uncheckAll(id);
	}
}

function checkAll(id) {
	var nodeList = document.getElementsByName(id);
	var nodes = $A(nodeList);
	nodes.each(function(node) {
		if (node.type=="checkbox" && !node.disabled) {
			node.checked = true;
		}
	});
}

function uncheckAll(id) {
	var nodeList = document.getElementsByName(id);
	var nodes = $A(nodeList);
	nodes.each(function(node) {
		if (node.type=="checkbox" && !node.disabled) {
			node.checked = false;
		}
	});
}

function toggleText(id, text1, text2) {
	var nodeList = document.getElementsByName(id);
	var nodes = $A(nodeList);
	nodes.each(function(node) {
		if (node.innerHTML == text1)	{
			node.innerHTML=text2;
		} else if (node.innerHTML == text2) {
			node.innerHTML=text1;
		}
	});
}

// 클리핑(복사하기)
function makeClipping(value, message) {
 window.clipboardData.setData('Text', value);
 if (message.length>0) {
	 alert('복사되었습니다. 마우스 오른쪽 버튼으로 붙여넣기를 하실 수 있습니다.');
 }
}

function replace(value, from, to) {
	var result = "";
	var index = value.indexOf(from);
	if (index>-1) {
		result = value.substring(0, index);
		result = result + to;
		result = result + value.substring(index + from.length, value.length);
	}

	return result;
}

function moveNextFocus(src, dest, len) {
		var  val = $F(src);
		if (val.length==len) {
			setFocus(dest);
		}
}

// 특정 테이블의 모든 ROW를 삭제
function resetTable(id) {
	for (i=$(id).rows.length-1; i>=0; i--) {
		$(id).deleteRow(i);
	}
}

// replaceAll
function replaceAll(str,orgStr,repStr)
{
    return str.split(orgStr).join(repStr);
} 

function emptyFocusText(element, text) {
	if (element.value==text) {
		element.value = "";
	}
}

function emptyBlurText(element, text) {
	if (element.value=="") {
		element.value = text;
	}
}

function setDateFormatDash(element) {
	if (element.value.length==8) {
		var year = element.value.substring(0, 4);
		var month = element.value.substring(4, 6);
		var date = element.value.substring(6, 8);

		element.value = year + "-" + month + "-" + date;
	}
} 
/*
// name로 명명된 모든 checkBox check
function checkAll(theForm, name) {
	for (i=0; i<theForm.elements.length; i++) {
		if (theForm.elements[i].type=="checkbox" && theForm.elements[i].name==name) {
			theForm.elements[i].checked = true;
		}
	}
}

// name로 명명된 모든 checkBox uncheck
function uncheckAll(theForm, name) {
	for (i=0; i<theForm.elements.length; i++) {
		if (theForm.elements[i].type=="checkbox" && theForm.elements[i].name==name) {
			theForm.elements[i].checked = false;
		}
	}
}
*/