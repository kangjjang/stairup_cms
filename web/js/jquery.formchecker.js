/*
--------------------------------------------------------------------------------------------------------------------------------------------
Redbrush javascript Validation library version 1.61
--------------------------------------------------------------------------------------------------------------------------------------------
�޼ҵ�														����												
--------------------------------------------------------------------------------------------------------------------------------------------
[jQuery Object Test : Validator]
boolean		isEmpty(target, message, focus)					target�� ������� Ȯ��, ����� ��� message���, focus�̵�(�ڵ� trim)			
boolean		isEquals(target1, target2, message, focus)		target1, target2�� ���� ��ġ�ϴ��� Ȯ��, ��ġ�ϴ� ��� message���, focus�̵�	
boolean		testValue(target, regexp, message, focus)		target�� �ش� ���ԽĿ� �´��� Ȯ��, ���� ���� ��� message���, focus�̵�		

[Value Test : Validator]
boolean		isValidSocialNumber(val)						�ֹε�Ϲ�ȣ�� ��ȿ���� �˻� ("-" ���ԵǾ� ���� ��� �����ϰ� �˻�)

[String Utils]


String		trim(string)						string�� ���� ����										1.0
int			getLength(string)					���ڿ� ���̸� ����Ʈ ������ �����Ѵ�.						1.0
boolean		checkLength(element, min, max)		element.value.length�� min, max �̻����� Ȯ��				1.0
boolean		isNumber(id)						element.value�� �������� �Ǻ�								1.0
boolean		isInteger(element)					element.value�� ���������� �Ǻ�							1.0
boolean		isFloat(element)					element.value�� �ε��Ҽ������� �Ǻ�						1.0
boolean		isAlphabet(element)					element.value�� ���������� �Ǻ�							1.0
boolean		isAlphabetString(String)			string�� ���������� �Ǻ�									1.0
boolean		isAlphaNumeric(element)				element.value�� ���������� �Ǻ�							1.0
boolean		isKorean(String)					string�� �ѱ����� �Ǻ�									1.0
boolean		isUpperCase(element)				element.value�� ��� UpperCase���� �Ǻ�					1.0
boolean		isLowerCase(element)				element.value�� ��� LowerCase���� �Ǻ�					1.0
boolean		checkAlphabet(char)					char�� ���������� �Ǻ�										1.0
boolean		checkNumber(char)					char�� �������� �Ǻ�										1.0
boolean		checkUpperCase(char)				char�� UpperCase���� �Ǻ�									1.0
boolean		checkLowerCase(char)				char�� LowerCase���� �Ǻ�									1.0
boolean		regTest(element, expr)				element.value�� ���Խ��Ǻ�								1.0
String		getSelectedValue(element)			select�� element�� ���õ� ���� ����						1.0
String		getSelectedIndex(element)			select�� element�� ���õ� �ε����� ����						1.0
void		selectValue(element, value)			select�� element�� value���� ���� ���� ����				1.0
void		selectIndex(element, index)			select�� element�� index�� ����							1.0
void		selectText(element, text)			select�� element�� text�� ����							1.0
void		checkValue(formId, element.name, value)	checkbox name�� value�� ��ġ�ϸ� ����					1.0
void		setChecked(formId, element.name, value)	checkValue�� ������ ���								1.0
void		getCheckedValue(formId, name)			checkbox�� name�� ��ġ�ϸ鼭 check�� ���� ���� ����			1.0
void		isChecked(id)						checkbox�� üũ�Ǿ����� Ȯ��								1.0
String		getFileName(String path)			path�� �޾Ƽ� ���� �̸��� �и��س���.						1.0
String		getFileExt(String fileName)			fileName�� �޾Ƽ� ���� Ȯ���ڸ� �и��س���.					1.0
boolean		isValidFile(fileName, ext)			fileName�� ,�� �и��� Ȯ���ڸ� �޾Ƽ� ��ȿ�� �������� Ȯ��	1.0
void		goUrl(String url)					url�� �̵�												1.0
void		openImage(path, title)				path �̹���â ����										1.12
boolean		isValidDate(String date)			String�� ó�� 8�ڸ� ���ڸ� �����ͼ� yyyyMMdd ��ȿ�� �Ǻ�	 	1.0
void		showBlock(id)						id��Ҹ� �����ش�.										1.0
void		hideBlock(id)						id��Ҹ� �����.											1.0
void		displayBlock(id)					id�� �����ְų� �����.(toggle)							1.0
void		checkSCN(String scn)				�ֹι�ȣ ��ȿ�� üũ 13�ڸ�									1.0
void        setDate(form.year, form.month, form.day, year, month, day, minYear, maxYear) select form�� fill �ϰ� year, month, day�� ä���. 1.0
void		numToHan(element1, element2)		element1�� ���ڸ� �ѱۼ��ڷ� �ٲپ� element2�� ���� �Լ�		1.0
String		getKoreanNumber(num)				num�� �޾Ƽ� �ѱۼ��ڷ� �Ѱ��ִ� �Լ�						1.0
String		makeUnitNumber(str)					str�� ,�� ���� ���ڷ� �ٲ���ִ� �Լ�						1.0
String		resetUnitNumber(str)				str�� ,�� �� ���ڷ� �ٲپ��ִ� �Լ�							1.0
String		setUnitAmount(element)				element�� ,�� ���� ���ڷ� �ٲپ��ִ� �Լ�					1.0
String		resetUnitAmount(element)			element�� ,�� �� ���ڷ� �ٲپ��ִ� �Լ�	 					1.0
String		addMonth(String, delta)				yyyyMMdd ������ ��¥���� delta���� �����ش�.				1.0
boolean		isValidMail(id)						�̸����� �˸��� �������� üũ�Ѵ�.							1.0
boolean		isValidEmail(id)					�̸����� �˸��� �������� üũ�Ѵ�.							1.0
boolean		checkEmail(id)						�̸����� �˸��� �������� üũ�Ѵ�.							1.0
void		addItem(id, value, text)			select element�� item�� �߰��Ѵ�.							1.0
void		checkDuplicateItemByValue(id, value)	select element�� item�� �߰��Ѵ�.							1.0
void		checkDuplicateItemByText(id, value)	select element�� item�� �߰��Ѵ�.							1.0
void		deleteItemByIndex(id, index)		select element�� index ��ġ�� item�� �����Ѵ�.				1.0
void		deleteItemBySelectedIndex(id)		select element�� selected index ��ġ�� item�� ����		1.0
void		selectUp(selectElement)				select element�� ���õ� �������� ���� ��ĭ �ø���.			1.0
void		selectDown(selectElement)			select element�� ���õ� �������� �Ʒ��� ��ĭ ������.			1.0
void		selectAll(id)						select element�� �������� ��� �����Ѵ�.					1.0
void		optionSwap(selectElement, srcIndex, targetIndex) select element �� Index �������� Swap		1.0
void		setOptions(selectElement, src, delim) select element�� src�� delim���� split�Ͽ� �����Ѵ�.		1.0
void		setCookie(name, value, expiredays)  cookie�� name=value�� expiredays �Ⱓ���� ����				1.0
String		getCookie(name)						cookie�� name�� �ش��ϴ� ���� �����´�.						1.0
void		setFocus(id)						element�� focus�� ��										1.0
void		setDateFormatDash(element)			element�� yyyyMMdd�� �Է��ϸ� yyyy-MM-dd�� �ڵ���ȯ			1.0
void		checkAll(form, elementName)			form���� name���� ���� element ��� check				1.0
void		uncheckAll(form, elementName)			form���� name���� ���� element ��� uncheck				1.0
---------------------------------------------------------------------------------------------------------------------------*/

/* 
	Validator Factory
*/
var Validator = {
	/*-----------------------------------------------
	jQuery Object Test
	-------------------------------------------------*/
	// �־��� Form Value�� ������� �˻� (�ڵ� Trim��)
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

	// �� ���� Form value�� ������ Ȯ��
	isNotEquals: function(target1, target2, message) {
		if ($(target1).val()!=$(target2).val()) {
			alert(message);
			$(target1).focus();
			return true;
		} else {
			return false;
		}
	},

	// �־��� Form Value�� �������� �˻� (�ڵ� Trim��)
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

	// �־��� Form Value�� ���� ���Խ� �˻�
	testValue: function(target, expression, message) {
		var regExp=new RegExp(expression);

		if (!regExp.test($(target).val())) {
			alert(message);
			$(target).focus();
		
			return false;
		}

		return true;
	},
	// �ֹε�� ��ȣ üũ
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

	// �̸��� üũ
	isNotValidEmail: function(target, message) {
		var reg = new RegExp("^[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+)*@[0-9a-zA-Z-]+(\.[0-9a-zA-Z-]+)*$");
		
		return !this.testValue(target, reg, message);
	},

	// ����Ʈ(selectBox)�� ����ִ��� Ȯ��
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

	// target�� value�� ���ĺ������� �����Ǿ� �ִ��� Ȯ��
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

	// target�� value�� ���ڷ� �����Ǿ� �ִ��� Ȯ��
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
	// �� ���� Form value�� ������ Ȯ��
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
// NextFocus : KeyUp�� �ش� Element�� �� ���̰� length�� �Ǹ�, target���� �ڵ� focus �̵�
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

// NumberInput : ���ڸ� �Է°����ϵ��� ��.
$.fn.numberInput = function() {
	$(this).bind("keydown", function(e) {
		//�Է� ��� Ű
		if( ( e.keyCode >=  48 && e.keyCode <=  57 ) ||   //���ڿ� 0 ~ 9 : 48 ~ 57
			( e.keyCode >=  96 && e.keyCode <= 105 ) ||   //Ű�е� 0 ~ 9 : 96 ~ 105
			e.keyCode ==   8 ||    //BackSpace
			e.keyCode ==  46 ||    //Delete
			e.keyCode == 110 ||    //�Ҽ���(.) : ����Ű�迭
			e.keyCode == 190 ||    //�Ҽ���(.) : Ű�е�
			e.keyCode ==  37 ||    //�� ȭ��ǥ
			e.keyCode ==  39 ||    //�� ȭ��ǥ
			e.keyCode ==  35 ||    //End Ű
			e.keyCode ==  36 ||    //Home Ű
			e.keyCode ==   9 ||    //Tab Ű
			e.keyCode ==  13	   // Enter Ű

		) {
			return true;
		} else {
			// - Ű
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

// AlphabetInput : ���ڸ� �Է°����ϵ��� ��.
$.fn.alphabetInput = function() {
	$(this).bind("keydown", function(e) {
		//�Է� ��� Ű
		if( ( e.keyCode >=  'a' && e.keyCode <=  'z' ) ||   //���ڿ� 0 ~ 9 : 48 ~ 57
			( e.keyCode >=  'A' && e.keyCode <=  'Z') ||   //Ű�е� 0 ~ 9 : 96 ~ 105
			e.keyCode ==   8 ||    //BackSpace
			e.keyCode ==  46 ||    //Delete
			//e.keyCode == 110 ||    //�Ҽ���(.) : ����Ű�迭
			//e.keyCode == 190 ||    //�Ҽ���(.) : Ű�е�
			e.keyCode ==  37 ||    //�� ȭ��ǥ
			e.keyCode ==  39 ||    //�� ȭ��ǥ
			e.keyCode ==  35 ||    //End Ű
			e.keyCode ==  36 ||    //Home Ű
			e.keyCode ==   9       //Tab Ű
		) {
			return true;
		} else {
			return false;
		}
	});

	return this;
}

// AlphaNumericInput : ���ڸ� �Է°����ϵ��� ��.
$.fn.alphaNumericInput = function() {
	$(this).bind("keydown", function(e) {
		//�Է� ��� Ű
		if( ( e.keyCode >=  'a' && e.keyCode <=  'z' ) ||   //���ڿ� 0 ~ 9 : 48 ~ 57
			( e.keyCode >=  'A' && e.keyCode <=  'Z') ||   //Ű�е� 0 ~ 9 : 96 ~ 105
			( e.keyCode >=  48 && e.keyCode <=  57 ) ||   //���ڿ� 0 ~ 9 : 48 ~ 57
			( e.keyCode >=  96 && e.keyCode <= 105 ) ||   //Ű�е� 0 ~ 9 : 96 ~ 105
			e.keyCode ==   8 ||    //BackSpace
			e.keyCode ==  46 ||    //Delete
			//e.keyCode == 110 ||    //�Ҽ���(.) : ����Ű�迭
			//e.keyCode == 190 ||    //�Ҽ���(.) : Ű�е�
			e.keyCode ==  37 ||    //�� ȭ��ǥ
			e.keyCode ==  39 ||    //�� ȭ��ǥ
			e.keyCode ==  35 ||    //End Ű
			e.keyCode ==  36 ||    //Home Ű
			e.keyCode ==   9       //Tab Ű
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

// str�� space�� trim
function trim(str) {
	return str.replace(/^(\s+)|(\s+)$/g,""); 
}

// ������ ���̸� ����Ʈ ������ �����Ѵ�.
function getByteLength(val) {
      // �Է¹��� ���ڿ��� escape() �� �̿��Ͽ� ��ȯ�Ѵ�.
      // ��ȯ�� ���ڿ� �� �����ڵ�(�ѱ� ��)�� ���������� %uxxxx�� ��ȯ�ȴ�.
      var temp_estr = escape(val);
      var s_index   = 0;
      var e_index   = 0;
      var temp_str  = "";
      var cnt       = 0;

      // ���ڿ� �߿��� �����ڵ带 ã�� �����ϸ鼭 ������ ����.
      while ((e_index = temp_estr.indexOf("%u", s_index)) >= 0)  // ������ ���ڿ��� �����Ѵٸ�
      {
        temp_str += temp_estr.substring(s_index, e_index);
        s_index = e_index + 6;
        cnt ++;
      }

      temp_str += temp_estr.substring(s_index);

      temp_str = unescape(temp_str);  // ���� ���ڿ��� �ٲ۴�.

      // �����ڵ�� 2����Ʈ �� ����ϰ� �������� 1����Ʈ�� ����Ѵ�.
      return ((cnt * 2) + temp_str.length) + "";
}

// input type="text" ��ü�� �޾Ƽ� �� ���� ���� ���̰� min���� ũ�� max���� ������ �˻�
// Unicode ������ ��쿡�� 2����Ʈ�� 1����, ASCII ������ ��쿡�� 1����Ʈ�� 1�����̴�.
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

// input type="text" ��ü�� �޾Ƽ� �� ���� ���� �������� �˻��ϴ� �Լ�
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

// input type="text" ��ü�� �޾Ƽ� �� ���� ���� �������� �˻��ϴ� �Լ�
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

// input type="text" ��ü�� �޾Ƽ� �� ���� ���� ���������� �˻��ϴ� �Լ�
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

// input type="text" ��ü�� �޾Ƽ� �� ���� ���� ���� �빮������ �˻��ϴ� �Լ�
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

// input type="text" ��ü�� �޾Ƽ� �� ���� ���� ���� �ҹ������� �˻��ϴ� �Լ�
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

// char �ϳ��� ���ĺ����� �˻��ϴ� �Լ�
/*function checkAlphabet(ch) {
	if ((ch  >="a"  &&  "z"  >= ch) || (ch  >="A"  &&  "Z"  >= ch))
		return true;
	
	return false;
}*/

// char �ϳ��� ���� ���� �˻��ϴ� �Լ�
function checkNumber(ch) {
	if (ch  >="0"  &&  ch<="9")
		return true;
	
	return false;
}

// char �ϳ��� �빮������ �˻��ϴ� �Լ�
function checkUpperCase(ch) {
	if (ch  >="A"  &&  "Z"  >= ch)
		return true;
	
	return false;
}

// char �ϳ��� �ҹ������� �˻��ϴ� �Լ�
function checkUpperCase(ch) {
	if (ch  >="a"  &&  "z"  >= ch)
		return true;
	
	return false;
}

// ����ǥ������ �޾Ƽ� Test�ϴ� �Լ�
function regTest(theForm, expression) {
    var regExp=new RegExp(expression);

	return regExp.test(theForm.value);

}

// ����Ʈ �ڽ����� ���õ� option�� ���� �����޴� �Լ�
function getSelectedValue(theForm) {
	return theForm.options[theForm.options.selectedIndex].value;
}

// ����Ʈ �ڽ����� ���õ� option�� �ε����� �����޴� �Լ�
function getSelectedIndex(theForm) {
	return theForm.options.selectedIndex;
}

// ����Ʈ �ڽ����� �ش� ������ �����Ѵ�.
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

// ����Ʈ �ڽ����� �ش� index�� �����Ѵ�.
function selectIndex(theForm, index) {
	theForm.options.selectedIndex = index;
}

// ����Ʈ �ڽ����� �ش� text�� �����Ѵ�.
function selectText(theForm, text) {
	for (i=0;i<theForm.options.length; i++ ) {
		if (theForm.options[i].text==text) {
			theForm.options.selectedIndex = i;
		}
	}
}

// ���� ��θ� �޾Ƽ� ���� �̸��� �и��س���.
function getFileName(path) {
	split=path.split("\\");

	if (split.length>0)	{
		return split[split.length-1];
	} else {
		return "";
	}
}

// ���� Ȯ���ڸ� �и��س���.
function getFileExt(fileName) {
	split=fileName.split(".");

	if (split.length>0)	{
		return split[split.length-1];
	} else {
		return "";
	}
}

// �ѱ����� �ƴ��� �����س���.
function isKorean(str) {
	var check=false;
	for (i=0; i<str.length; i++) {
		if (str.charCodeAt(i)>255) {
			check=true;
		}
	}

	return check;
}

// ���� Ȯ���ڸ� �޾Ƽ� ��ȿ�� �������� Ȯ���Ѵ�.
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
imgwin.document.write("<title>" + title + "</title>\n"); //����â Ÿ��Ʋ �̸� �����ϴ� �κ� 

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
	  vDate.setMonth(str.substring(4, 6)-1);  /* -- -1�� ���־����ϴ� --*/ 
	  vDate.setDate(str.substring(6)); 

	  if( vDate.getFullYear() != str.substring(0, 4) || 
	   vDate.getMonth()    != (str.substring(4, 6)-1) ||  /* -- -1�� ���־����ϴ� --*/ 
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

/*******error ó������ ��ũ��Ʈ ���� ***************/
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

/*******error ó������ ��ũ��Ʈ �� ***************/

// �ֹε�� ��ȣ üũ
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


// select box�� ��¥�� ä���.
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
var flag = true; // ���� �Ǻ� �÷��� 

/** 
* ���ڷ� �� ���ڿ��� �Է¹޾� ���ڿ� �ش��ϴ� �ѱ۸��� �����Ѵ�. 
* @Param : ���ڷε� ���ڿ� 
* @Return : ���ڿ� �ش��ϴ� �ѱ۸� 
*/ 
function numToHan2(val) { 
	switch(Number(val)) { 
		case 1 : han = "��"; break; 
		case 2 : han = "��"; break; 
		case 3 : han = "��"; break; 
		case 4 : han = "��"; break; 
		case 5 : han = "��"; break; 
		case 6 : han = "��"; break; 
		case 7 : han = "ĥ"; break; 
		case 8 : han = "��"; break; 
		case 9 : han = "��"; break; 
		case 0 : han = ""; break; 
	} 
	return han; 
} 

/** 
* ������ �ε����� �Է¹޾� ������ �ش��ϴ� �ѱ۸��� �����Ѵ�. 
* @Param : ������ �ε��� 
* @Return : �ε����� �ش��ϴ� ���� 
*/ 
function danwiToHan(val)  { 
	/* �� ������ Skip */ 
	if (val == 0) 
	return ""; 

	var namerji = val % 4; 

	switch(namerji)  { 
		case 0: // �������� '0'�̸� ��, ��, �� �߿� �ϳ��̴�. 
			if (val / 4 == 1) { 
				han = "��"; 
				flag = false; 
			} else if (val / 4 == 2) { 
				han = "��"; 
				flag = false; 
			} else if (val / 4 == 3) { 
				han = "��"; 
				flag = false; 
			} 
			break; 
		case 1: // �������� '1'�̸� �ʴ����̴�. 
			han = "��"; flag = true; break; 
		case 2: // �������� '2'�̸� ������̴�. 
			han = "��"; flag = true; break; 
		case 3: // �������� '3'�̸� õ�����̴�. 
			han = "õ"; flag = true; break; 
	} 

	return han; 
} 

/** 
* ���ڷε� �ݾ��� �ѱ۷� �ٲ۴�. 
* @Param : obj1 - ���ڸ� �Է��� ��ü 
* obj2 - �ѱ��� ������ ��ü 
*/ 
function numToHan(obj1, obj2) { 
	var str = ""; 
	var num = obj1.value; 
	var digit = new Array(); 
	var j = num.length - 1; 

	if (num.length > 16)  { 
		alert("�ݾ��� �ʹ� Ů�ϴ�."); 
		return; 
	} 

	for (var i = 0; i < num.length; i ++) { 
		digit[j] = num.charAt(i); // ���ڸ� �Ųٷ� ��´�.(�迭�� reverse()��� function�� �� ������ �Ѵ�.) 
		str += numToHan2(digit[j]); 

		/*------------------------------------------------------------------*/ 
		/* ���ڰ� 0�� �ƴ� ��� ���� ������ �Ǻ� */ 
		/* ���ڰ� 0�� ��� �ε����� 4�� ������ �������� ���(��, ��, ��) �� */ 
		/* �ϳ��� ������ �� �ֵ��� flag�� �д�. */ 
		/* �� ���ذ� �ȵǸ� flag�� ���� 1���� �ĺ���... ^^; */ 
		/*------------------------------------------------------------------*/ 
		if (digit[j] != "0" || (j % 4 == 0) && flag) 
			str += danwiToHan(j); 

		j --; 
	} 

	if (str != "") 
		obj2.value = str + " ����"; 
	else 
		obj2.value = ""; 
} 

function getKoreanNumber(obj, unit) { 
	var str = ""; 
	var num = obj; 
	var digit = new Array(); 
	var j = num.length - 1; 

	if (num.length > 16)  { 
		alert("�ݾ��� �ʹ� Ů�ϴ�."); 
		return; 
	} 

	for (var i = 0; i < num.length; i ++) { 
		digit[j] = num.charAt(i); // ���ڸ� �Ųٷ� ��´�.(�迭�� reverse()��� function�� �� ������ �Ѵ�.) 
		str += numToHan2(digit[j]); 

		/*------------------------------------------------------------------*/ 
		/* ���ڰ� 0�� �ƴ� ��� ���� ������ �Ǻ� */ 
		/* ���ڰ� 0�� ��� �ε����� 4�� ������ �������� ���(��, ��, ��) �� */ 
		/* �ϳ��� ������ �� �ֵ��� flag�� �д�. */ 
		/* �� ���ذ� �ȵǸ� flag�� ���� 1���� �ĺ���... ^^; */ 
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
   �ݾ��� �Ѱܹ޾Ƽ� , �� �����ϰ� ��ȯ�ϴ� �Լ�
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

// yyyyMMdd ������ ��¥���� delta���� �����ش�.
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
		alert("���õ� �������� �����ϴ�.");
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
		alert("���õ� �������� �����ϴ�.");
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

// id�� �ش��ϴ� element focus
function setFocus(id) {
	if ($(id).type!="hidden")	{
		$(id).focus();
	}
}

// id�� �ش��ϴ� element�� focus�������� Ȯ��
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

// Ŭ����(�����ϱ�)
function makeClipping(value, message) {
 window.clipboardData.setData('Text', value);
 if (message.length>0) {
	 alert('����Ǿ����ϴ�. ���콺 ������ ��ư���� �ٿ��ֱ⸦ �Ͻ� �� �ֽ��ϴ�.');
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

// Ư�� ���̺��� ��� ROW�� ����
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
// name�� ���� ��� checkBox check
function checkAll(theForm, name) {
	for (i=0; i<theForm.elements.length; i++) {
		if (theForm.elements[i].type=="checkbox" && theForm.elements[i].name==name) {
			theForm.elements[i].checked = true;
		}
	}
}

// name�� ���� ��� checkBox uncheck
function uncheckAll(theForm, name) {
	for (i=0; i<theForm.elements.length; i++) {
		if (theForm.elements[i].type=="checkbox" && theForm.elements[i].name==name) {
			theForm.elements[i].checked = false;
		}
	}
}
*/