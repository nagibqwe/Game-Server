<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自定义sql语句</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/log.js"></script>
<script type="text/javascript">
var base = '${base}';
$(function() {
	reloadNoHeFuDBGroups();
});
function search(){
	if($("#result_query_form").validationEngine('validate')) {
		var sql = $("#sqlStr").val().trim();
		$("#loadingmodal").modal({backdrop: 'static', keyboard: false});
		$.ajax({
			type: "POST",
			url: base + "/server/customSql",
			data: {
				"serverId": $("#select_server").val(),
				"sql": sql
			},
			dataType: "json",
			error: function () {
				$("#loadingmodal").modal('hide');
				alert("可能是网络、sql语句、权限问题造成了错误");
			},
			success: function (data) {
				$("#loadingmodal").modal('hide');
				var list_html = "<table class='table table-bordered table-striped'>";
				for (var i = 0; i < data.length; i++) {
					var tmp = "<tr>";
					for (var j = 0; j < data[i].length; j++) {
						tmp += "<td>" + data[i][j] + "</td>";
					}
					tmp += "</tr>";
					list_html += tmp;
				}
				list_html += "</table>";
				$("#data").html(list_html);
			}
		});
	}
}
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="result_query_form" class="well form-inline">
			<select id="select_group" onchange="reloadNoHeFuDBs(this.value)" class="span2"></select>
			<select id="select_server" name="serverId" class="span2"></select>
			<input type="button" value="查询" class="btn btn-primary" onclick="search()"/>
			<input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel();">
			<br/><br/>
			<div class="control-group">
				<label for="sqlStr" class="label control-label">要查询的SQL语句:</label>
				<div class="controls">
					<textarea id="sqlStr" class="span10" rows="3" style="height: 100px"></textarea>
				</div>
			</div>
		</form>
		<div id="data"></div>
	</div>
	<jsp:include page="../commonmodal.jsp"/>
</body>
</html>