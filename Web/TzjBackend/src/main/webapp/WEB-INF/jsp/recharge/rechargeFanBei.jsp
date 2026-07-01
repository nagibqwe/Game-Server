<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.gmCommand.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<body>
<script type="text/javascript">
var base = '${base}';

$(function() {
	reloadNoHeFuServerGroups(true);
	reloadNoHeFuServerGroups2(true);
	$("#loading").shCircleLoader();
});

function sendCommand(){
	if(!$("#server input[type='checkbox']").is(':checked')){
		return alert("请选择服务器!");
	}
	
	if(!$("#comman_submit").validationEngine('validate')){
		return;
	}
	
	$("#loadingmodal").modal({
		backdrop : 'static',
		keyboard : false
	});
	$("#msgbox").val("");
	$.post(base + "/recharge/setRechargeFanBei",$("#comman_submit").serialize(), function(data) {
		$("#loadingmodal").modal('hide');
		//alert(data.msg);
		$("#msgbox").html(data.msg);
	});
};

function search(){
	//alert($("#select_server2 option:eq(i)").attr("selected","selected").val());
	//alert($("#select_server2 option[value=1007]").attr('selected', "selected").text());
	//alert($("#select_server2").val());
	$.ajax({
		url : base + "/recharge/queryRechargeFanBei",
		data : {
			sid : $("#select_server2").val()
		},
		method : "post",
		dataType : "json",
		success : function(data) {
			$("#loadingmodal").modal('hide');
			//console.log(data);
			if (!data.ok) {
				alert(data.msg);
			}else{
				alert("充值倍数为："+data.data+"%");
			}
		}
	});	
};

function reloadNoHeFuServerGroups(hasFight){//平台
	$.ajax({
		type : "POST",
		url : base + "/server/group",
		data : "",
		dataType : "json",
		success : function(data) {
			var select = $("#groupName");
			select.empty();
			for (var i = 0; i < data.length; i++) {
				var option = $("<option>").val(data[i]).text(data[i]);
				select.append(option);
			}
			reloadNoHeFuServer(select.val(),hasFight);
		}
	});
};

function reloadNoHeFuServer(groupName,hasFight) {
	$.ajax({
		type : "POST",
		url : base + "/server/getNoHeFuServer",//包括测试服、正式服和战斗服
		data : {
			"groupName" : groupName,//平台组名
			"hasFight" : hasFight//是否包含战斗服
		},
		dataType : "json",
		success : function(data) {
			var select = $("#server");
			select.empty();
			for (var i = 0; i < data.length; i++) {
				var server = data[i];
				var label=$("<label>").attr("class","checkbox inline").text(server.serverName+"("+server.serverId+")");
				var option = $("<input>").attr("checked",false).attr("class","choosecheckbox").attr("type","checkbox").attr("name","serverIds").val(server.serverId);
				select.append(label);
				select.append("&nbsp;");
				select.append(option);
				select.append("&nbsp;");
			}
		}
	});
};

function reloadNoHeFuServerGroups2(hasFight){//平台
	$.ajax({
		type : "POST",
		url : base + "/server/group",
		data : "",
		dataType : "json",
		success : function(data) {
			var select = $("#select_group2");
			select.empty();
			for (var i = 0; i < data.length; i++) {
				var option = $("<option>").val(data[i]).text(data[i]);
				select.append(option);
			}
			reloadNoHeFuServer2(select.val(),hasFight);
		}
	});
};

function reloadNoHeFuServer2(groupName,hasFight) {
	$.ajax({
		type : "POST",
		url : base + "/server/getNoHeFuServer",
		data : {
			"groupName" : groupName,
			"hasFight" : hasFight//是否包含战斗服
		},
		dataType : "json",
		success : function(data) {
			var select = $("#select_server2");
			select.empty();
			for (var i = 0; i < data.length; i++) {
				var dblog = data[i];
				var option = $("<option>").val(dblog.serverId).text(dblog.serverName+"("+dblog.serverId+")");
				select.append(option);
			}
		}
	});
};

function changeGroup(groupName){
	reloadNoHeFuServer(groupName,true);
};

function changeGroup2(groupName){
	reloadNoHeFuServer2(groupName,true);
};

function checkall(obj){
	var c = $(".choosecheckbox");
	//$("#allcheck").attr('checked');
	var ac = $(obj);
	var allChecked = true, allCanceled = true;
	var i=0;
	for(i=0; i<c.length; i++){
		if(!c[i].checked){ allChecked=false; break; }
	}
	for(i=0; i<c.length; i++){
		if(c[i].checked){ allCanceled=false; break; }
	}
	if(allChecked && ac=='checked'){
		return;
	}
	if(allCanceled && ac===undefined){
		return;
	}
	for(i=0; i<c.length; i++){
		c[i].checked=allChecked?false: true;
	}
};

</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="comman_submit" class="well">
			<select class="input" id="groupName" onchange="changeGroup(this.value)"></select>
			<label id="allCheck" class="checkbox inline">全选</label>
			<input type="checkbox" onchange="checkall(this);">
			<div id="server" class="well"></div>
			
			倍数:
			<input type="text" style="width:20px;" name="num" class="validate[required,custom[integer],min[0],max[100]]">%<br/>
			<input type="button" id="submit_command" class="btn btn-primary" onclick="sendCommand();" value="执行">
		</form>
		<p id="msgbox"></p>
	</div>
	
	<div class="container-fluid">
	   <form action="#" id="query_form" class="well form-inline">
		   	&nbsp;&nbsp;
		  	<select id="select_group2" onchange="changeGroup2(this.value)"></select> 
			&nbsp;&nbsp;
			<select id="select_server2" name="sid" class="span2"></select>
			&nbsp;&nbsp;
			<input type="button" id="excel_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
	   </form>
	</div>
	
	<div class="modal hide fade in" id="loadingmodal">
		<div class="modal-body">
			<div style="padding-left: 30%;">
				<p
					style="font-size: 14px; width: 120px; height: 30px; line-height: 30px; display: inline; float: left;">${msg['jsp.log.loading']}</p>
				<div id="loading"
					style="width: 30px; height: 30px; display: inline; float: left;"></div>
			</div>
		</div>
	</div>
	
<%-- <jsp:include page="../commonmodal.jsp"/> --%>
</body>
</html>