/**
 * ajax 를 호출하기 위한 함수
 * @param $.ajax()를 호출하기 위한 인자값
 * @returns
 */
function gfnc_Ajax( options ) {
	var defaults = {
    	type: 'POST',
    	url: '',
    	data: {},
    	async: true,
    	cache: true,
    	headers: {}
    };
	var settings = $.extend({}, defaults, options);
	return $.ajax(settings);
}


