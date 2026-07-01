<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>踢玩家下线</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/role.js"></script>
<script type="text/javascript">
var base = '${base}';
$(function() {
	reloadNoHeFuServerGroups(false);
	//禁止回车提交表单
	$('.forbidSubmit').keypress(function(e){
        if(e.keyCode==13){
            e.preventDefault();
        }
    });
});

function kickPlayer(){
	if ($('#kickPlayerForm').validationEngine('validate')) {
		$("#loadingmodal").modal({backdrop: 'static', keyboard: false});
		$.ajax({
			url: base + "/forbidden/kickPlayer",
			type: "post",
			dataType: "json",
			data: {
				"serverId": $("#select_server").val(),
				"roleId": $("#playerId").val(),
				"reason": $("#reason").val()
			},
			success: function (data) {
				$("#loadingmodal").modal('hide');
				alert(data.msg);
			}
		});
	} else{
		alert("${msg['forbid.dataerror']}");
	}
}
</script>
<jsp:include page="../role/roleInfo.jsp"/>
</head>
<body>
<div class="container-fluid">
	<form id="role_query_form" class="well form-inline">
		<select id="select_group" name="groupName" onchange="reloadNoHeFuServerGroups(this.value)" class="span2"></select>
		<select id="select_server" name="serverId" class="span2"></select>
		<select id="select_queryType" name="queryType" class="span2">
			<option value="1">${msg['jsp.role.rolename']}</option>
			<option value="2">${msg['jsp.role.account']}</option>
			<option value="3">${msg['jsp.role.roleid']}</option>
			<option value="4">${msg['jsp.role.userid']}</option>
			<option value="5">${msg['jsp.role.acpfaccount']}</option>
		</select>
		<input id="queryString" name="queryString" type="text" class="validate[required] forbidSubmit span2">
		<input type="button" value="${msg['jsp.role.search']}" class="btn btn-primary" onclick="role_reload()">
	</form>
	<div id="msg"></div>
	<div id="account_list"></div>
	<div id="role_list"></div>
</div>

<div class="container-fluid">
	<form action="#" id="kickPlayerForm" method="post" class="well form-inline">
		<label for="playerId" class="span2" style="margin-left: 0">${msg['kick.player']}</label>
		<input id="playerId" type="text" name="playerId" class="validate[required],custom[integer],min[0] span4"/>
		<br/><br/>
		<label for="reason" class="span2" style="margin-left: 0">${msg['forbid.chat.reason']}</label>
		<textarea id="reason" name="reason" class="validate[required] span4" rows="4"></textarea>
		<br/><br/>
		<input type="button" class="btn btn-primary" value="${msg['kick.deal']}" onclick="kickPlayer()"/>
	</form>
</div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>