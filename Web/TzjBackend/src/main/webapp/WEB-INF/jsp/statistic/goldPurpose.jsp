<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>元宝用途统计</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript">
	var base = '${base}';
	$(function() {
		$("#start").datetimepicker({
			language : 'zh-CN',
			format : 'yyyy-mm-dd',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			showMeridian : 1
		});
		$("#end").datetimepicker({
			language : 'zh-CN',
			format : 'yyyy-mm-dd',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			showMeridian : 1
		});
		getGroup();
		$("#loading").shCircleLoader();
	});
	function getGroup(){
		$.ajax({
			type : "POST",
			url : base + "/dblog/group",
			data : "",
			dataType : "json",
			success : function(data) {
				var select = $("#select_group");
				select.empty();
				for (var i = 0; i < data.length; i++) {
					var option = $("<option>").val(data[i]).text(data[i]);
					select.append(option);
				}
				getServer(select.val());
				getChannelName(select.val());
			}
		});
	};
	function getServer(groupName){
		$.ajax({
			type : "POST",
			url : base + "/dblog/serverByGroup",
			data : {
				"groupName" : groupName
			},
			dataType : "json",
			success : function(data) {
				var select = $("#checkbox_server");
				select.empty();
				for (var i = 0; i < data.length; i++) {
					var option = "<input type='checkbox' name='serverId' value='"+data[i].serverId+"' />&nbsp;"+data[i].serverName+"&nbsp;&nbsp;";
					select.append(option);
				}
				var selectAll = "<input type='checkbox' id='selectAll' onclick='selectAllSid();' />&nbsp;${msg['statistic.public.selectAll']}";
				select.append(selectAll);
			}
		});
		
	};
	function selectAllSid(){
		var che = $("#selectAll").is(":checked");
		$("[name='serverId']").prop("checked", che);
	};
		
	function getChannelName(groupName){
		$.ajax({
			url : base + "/channel/getChannelName",
			data : {
				platfName : groupName,
			},
			method : "post",
			dataType : "json",
			success : function(data) {
				var channelHtml = "";
				if(data == null){
					$("#checkbox_channel").html(channelHtml);
					return;
				}
				for(var i = 0 ; i < data.length ; i++){
					channelHtml += "<input type='checkbox' name='channelName' value=\""+data[i]+"\" />"+data[i]+"&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				$("#checkbox_channel").html(channelHtml);
			}
		});
	};
	function search(target) {
		var serverId = $("#select_server").val();
		var platfName = $("#select_group").val();
		var startDate = $("input[name='startDate']").val();
		var endDate = $("input[name='endDate']").val();
		if(startDate == "" || endDate == ""){
			$("#syspromtinfo").html("${msg['statistic.public.syspromt.time']}");
			return;
		}
		
		var isblack = $("#isblack").is(":checked");//用来过滤黑名单
		//加载
		$("#loadingmodal").modal({backdrop:"static",keyboard:false});
		$.ajax({
		url : base + "/ltvstatistic/ltvDataStatistic",
		data : {
			serverId : serverId,
			platfName : platfName,
			startDate : startDate,
			endDate : endDate,
			isblack : isblack
		},
		method : "post",
		dataType : "json",
		success : function(data) {
			$("#loadingmodal").modal('hide');
			var ltvHtml = target(data);
			$("#ltvData").html(ltvHtml);
			$("#syspromtinfo").html("");
		}
		});
	}
	
	
	function getLtvHtml(data){
		
		var ltvDays = data.ltvDays;
		var dataMapList = data.dataMapList;
		var ltvhtml = "";
		ltvhtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
		ltvhtml += "<tbody>";
		ltvhtml += "<tr align='center' style='font-weight: bolder;'>";
		ltvhtml += "<td>${msg['statistic.public.date']}</td>";
		ltvhtml += "<td>${msg['statistic.playerLtv.addnewer']}</td>";
		for(var i=0; i<ltvDays.length; i++){
			ltvhtml += "<td>LTV"+(ltvDays[i]+1)+"</td>";
		}
		ltvhtml += "</tr>";
		for(var i=0; i<dataMapList.length; i++){
			ltvhtml += "<tr>";
			ltvhtml += "<td>"+dataMapList[i].date+"</td>";
			ltvhtml += "<td>"+dataMapList[i].reglength+"</td>";
			for(var key in dataMapList[i].ltvMap){
				ltvhtml += "<td>"+dataMapList[i].ltvMap[key]+"</td>";
			}
			ltvhtml += "</tr>";
		}
		ltvhtml += "</tbody>";
		ltvhtml += "</table>";
		return ltvhtml;
	}
	
	function exportExcel(){
		$("#ltvData table").eq(0).tableExport({type: 'excel', escape: 'false'});		
	}
	
	
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="query_form" class="well form-inline">
			<label class="label">${msg['statistic.public.platform']}</label>&nbsp;&nbsp;
			<select id="select_group" onchange="getServer(this.value);getChannelName(this.value);"></select><br/><br/>
			<label class="label">${msg['statistic.public.server']}</label>&nbsp;&nbsp;
			<span id="checkbox_server"></span><br/><br/>
			<label class="label">${msg['statistic.public.Channel']}</label>&nbsp;&nbsp;
			<span id="checkbox_channel"></span><br/><br/>
			<label class="label">${msg['statistic.public.time']}</label>&nbsp;&nbsp;
			<div class="input-append date" id="start">
			<input style="width: 120px;" name="startDate" size="20" value="${nowDate}" type="text"
				readonly> <span class="add-on"><i class="icon-th"></i></span>
			</div>
			-
			<div class="input-append date" id="end">
				<input style="width: 120px;" name="endDate" size="20" value="${nowDate}" type="text" readonly>
				<span class="add-on"><i class="icon-th"></i></span>
			</div>
			<br/>
			<br/>
			${msg['statistic.public.isblack']}：<input type="checkbox" id="isblack"/>&nbsp;&nbsp;
			<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search(getLtvHtml);">
			&nbsp;&nbsp;
			<input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel();">
			&nbsp;&nbsp;
			<span id="syspromtinfo" style="color: red;"></span>
			
		</form>
		
		<p id="msg"></p>
		<ul class="nav nav-tabs" id="myTab">
			<li class="record active" id="ltv"><a href="#payRecord" data-toggle="tab">元宝用途</a></li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="ltvData">${msg['statistic.public.content']}：<span style="color: red;">元宝\绑定元宝途径消耗统计</span></div>
		</div>
		
	</div>

	<div class="modal hide fade in" id="loadingmodal">
		<div class="modal-body">
			<div style="padding-left: 30%;">
				<p style="font-size: 14px; width: 120px; height: 30px; line-height: 30px; display: inline; float: left;">${msg['jsp.log.loading']}</p>
				<div id="loading" style="width: 30px; height: 30px; display: inline; float: left;"></div>
			</div>
		</div>
	</div>
</body>
</html>