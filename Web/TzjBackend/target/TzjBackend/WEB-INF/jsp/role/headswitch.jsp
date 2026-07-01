<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.server.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>

<script type="text/javascript">
var base = '${base}';
$(function(){
	query();
});

function query(){
	$("#operation td").remove();
	$.ajax({
		url : base + "/headSwitch/query",
		data : {},
		dataType : "json",
		success : function(data) {
			var html="";
			if(data==0){
				html+="<td>${msg['jsp.menu.close']}</td><td><button class='btn btn-primary' onclick='change()'>${msg['jsp.menu.open']}</button></td>"
				
			}else{
				html+="<td>${msg['jsp.menu.open']}</td><td><button class='btn btn-primary' onclick='change()'>${msg['jsp.menu.close']}</button></td>"
			}
			$("#operation").append(html)
		}
	});
}

function change(){
	$.ajax({
		url : base + "/headSwitch/change",
		data : {},
		dataType : "json",
		success : function(data) {
			if(data.ok){
				query();
				alert(data.msg);
			}
		}
	});
}
</script>
</head>
<body>
	<table class="table table-bordered table-striped">
		<tr class="info">
		<td colspan="2">${msg['jsp.headIcon.switch']}</td>
		</tr>
		<tr>
		<td>${msg['jsp.headIcon.nowstate']}</td>
		<td>${msg['jsp.headIcon.operation']}</td>
		</tr>
		<tr id="operation">
		</tr>
	</table>
</body>
</html>