<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.guildchange.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/log.js"></script>
<script type="text/javascript" src="${base}/js/role.js"></script>
<script type="text/javascript">
	var base = '${base}';
	var groupName='${groupName}';
	var serverId='${serverId}';
	var guildId='${guildId}';
	var logType = '${logType}';
	$(function() {
		console.log(groupName, serverId, guildId, logType);
		$("#loading").shCircleLoader();
		if(guildId==""){
			group_reload();
		}else{
			chooseGroup(groupName, serverId);
			$("#guildId").val(guildId);
			search();
		}
	});
	function search(){
		if ($("#guildId").val() == "") {
			return;
		}
		$("#loadingmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
			type: "POST",
			url: base + "/log/getLog",
			data: {
				"serverId": $("#select_server").val(),
				"guildId": $("#guildId").val(),
				"logType": logType
			},
			dataType: "json",
			success: function (data) {
				$("#loadingmodal").modal('hide');
				if (!data.ok) {
					$("#msg").html(data.msg);
					return;
				}
				var tableStr = createTable(data.data.fields, data.data.datas);
				$("#guildchange").html(tableStr);
			}
		});
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="query_form" class="well form-inline">
			<select id="select_group" onchange="reloadNoHeFuDBs(this.value)" class="span2"></select>
			<select id="select_server" name="serverId" class="span2"></select>
			<label for="guildId" class="label">公会ID</label>
			<input type="text" id="guildId" name="guildId" class="span3"/>
			<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
		</form>
		<div id="msg"></div>
		<div id="guildchange"></div>
	</div>
	<jsp:include page="../commonmodal.jsp"/>
</body>
</html>