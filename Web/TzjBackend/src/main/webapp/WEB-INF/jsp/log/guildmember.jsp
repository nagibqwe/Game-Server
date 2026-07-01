<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.guildmember.title']}</title>
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
	$(function() {
		console.log(groupName, serverId, guildId);
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
			type : "POST",
			url : base + "/log/guildmember",
			data : {
				"serverId": $("#select_server").val(),
				"guildId": $("#guildId").val()
			},
			dataType : "json",
			success : function(data) {
				$("#loadingmodal").modal('hide');
				if (!data.ok) {
					$("#msg").html(data.msg);
				}

				var table = $("<table class='table table-bordered table-striped'>");
				var thead = $("<thead>");
				var tr = $("<tr>");
				var fields = ["${msg['log.title.roleId']}",
					"${msg['log.title.roleName']}"];
				for (var field in fields) {
					var th = $("<th>").text(fields[field]);
					tr.append(th);
				}
				thead.append(tr);
				table.append(thead);

				var tbody = $("<tbody>");
				for (var key in data.data) {
					var member = data.data[key];
					var dtr = $("<tr>");
					var datalist = [member.roleId, member.roleName];
					for (var i in datalist) {
						var td = $("<td>").text(datalist[i]);
						dtr.append(td);
					}
					tbody.append(dtr);
				}
				table.append(tbody);
				$("#guildmember").html(table);
			}
		});
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="query_form" class="well form-inline">
			<select id="select_group" onchange="queryServerByGroup(this.value)" class="span2"></select>
			<select id="select_server" name="serverId" class="span2"></select>
			<label for="guildId" class="label">公会ID</label>
			<input type="text" id="guildId" name="guildId" class="span3"/>
			<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
		</form>
		<div id="msg"></div>
		<div id="guildmember"></div>
	</div>
	<jsp:include page="../commonmodal.jsp"/>
</body>
</html>