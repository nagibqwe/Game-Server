<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.recharge.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/boxy.css" type="text/css" />
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script>
var base = "${base}";
$(function() {
	reloadNoHeFuServerGroups(false);
});

function recharge(){
	if($("#rechargeForm").validationEngine('validate')){
		$.post(base+ "/recharge/rechargeOrder", 
		$("#rechargeForm").serialize(),
		function (data){
			$("#msg").html(data.msg);
			if(data.ok){
				alert(data.msg);
				$("#rechargeForm")[0].reset();
			}
		});
	}
}
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="rechargeForm" class="form-horizontal well">
			<div class="control-group">
				<label class="control-label">服务器</label>
				<div class="controls">
					<select id="select_group" class="span2" onchange="reloadNoHeFuServer(this.value, false)"></select>
					<select id="select_server" name="serverId" class="span2"></select>
				</div>
			</div>

			<div class="control-group">
				<label for="roleId" class="control-label">${msg['jsp.recharge.roleid']}</label>
				<div class="controls">
					<input id="roleId" name="roleId" type="text" class="form-control input validate[required,custom[integer]]">
				</div>
			</div>

			<div class="control-group">
				<label for="rechargeGold" class="control-label">${msg['jsp.recharge.rechargegold']}</label>
				<div class="controls">
					<input id="rechargeGold" name="rechargeGold" type="text" class="form-control input validate[required,custom[integer],min[1]],max[100000]" placeholder="真实金额，单位分">
				</div>
			</div>

			<div class="control-group">
				<label for="rechargeTotalGold" class="control-label">${msg['jsp.recharge.rechargeTotalGold']}</label>
				<div class="controls">
					<input id="rechargeTotalGold" name="rechargeTotalGold" type="text" class="form-control input validate[required,custom[integer],min[0]],max[100000]" placeholder="对应灵玉数量">
				</div>
			</div>

			<div class="control-group">
				<label for="rechargeVipExp" class="control-label">${msg['jsp.recharge.rechargeVipExp']}</label>
				<div class="controls">
					<input id="rechargeVipExp" name="rechargeVipExp" type="text" class="form-control input validate[required,custom[integer],min[0]],max[100000]" placeholder="对应增加的VIP经验数量">
				</div>
			</div>

			<div class="control-group">
				<label for="reason" class="control-label">${msg['jsp.recharge.rechargeReason']}</label>
				<div class="controls">
					<input id="reason" name="reason" type="text" class="form-control input validate[required]">
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<input type="button" value="${msg['jsp.recharge.recharge']}" class="btn btn-primary" onclick="recharge()">
				</div>
			</div>
		</form>
	</div>
	<p id="msg"></p>
</body>
</html>