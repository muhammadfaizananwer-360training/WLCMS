// JavaScript Document

var xmlData = {
	init: function(path,returnFn){
		$.ajax({
			type: "GET",
			url: path,
			dataType: "xml",
			success: function(xml) {
				returnFn(xml);
			}
		});
	}
}