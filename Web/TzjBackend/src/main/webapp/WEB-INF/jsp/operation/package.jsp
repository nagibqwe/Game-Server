<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.packageSearch.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<!-- 导入分页css -->
<link rel="stylesheet" href="${base}/css/pagination.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<!-- 加载jQuery分页插件 -->
<script type="text/javascript" src="${base}/js/jquery/jquery.pagination.js"></script>
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
		$("#conditionDiv").css("display","none");
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
	var exId = "";
	function search() {
		statisticData("getPackage");
	}
	function statisticData(actionType){
		exId = actionType;
		var serverIds = [];
		$("input[name='serverId']:checked").each(function(){ 
			serverIds.push($(this).val());
        });
		var platfName = $("#select_group").val();
		var channelNames = [];
		$("input[name='channelName']:checked").each(function(){ 
			channelNames.push($(this).val());
        });
		
		var selectType = $("#selectType").val();
		var condition =  $("input[name='condition']").val();
		var startDate = $("#start input").val();
		var endDate = $("#end input").val();
		
		if(serverIds.length == 0){
			$("#syspromtinfo").html("${msg['statistic.public.syspromt.server']}");
			return;
		}
		var isblack = $("#isblack").is(":checked");//用来过滤黑名单
		//加载
		$("#loadingmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
		url : base + "/packageSearch/" + actionType,
		data : {
			serverIds : serverIds+"",
			platfName : platfName,
			channelNames : channelNames+"",
			isblack : isblack,
			selectType : selectType ,
			condition : condition,
			startDate : startDate,
			endDate : endDate
		},
		method : "post",
		dataType : "json",
		success : function(data) {
			$("#loadingmodal").modal('hide');
			if(data == null){
				return;
			}
			console.log(data);
			setDailyOnlineTimeHtml(data);
			$("#syspromtinfo").html("");
		}
		});
	}
	
	function setDailyOnlineTimeHtml(data){
		var contenthtml = "";
		contenthtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
		contenthtml += "<tbody>";
		contenthtml += "<tr align='center' style='font-weight: bolder;'>";
		contenthtml += "<td>${msg['jsp.packageSearch.roleId']}</td>";
		contenthtml += "<td>${msg['jsp.packageSearch.bags']}</td>";
		contenthtml += "<td>${msg['jsp.packageSearch.stores']}</td>";
		contenthtml += "<td>${msg['jsp.packageSearch.equips']}</td>";
		contenthtml += "</tr>";
		for(var key in data){
			contenthtml += "<tr>";
			contenthtml += "<td>"+data[key].roleId+"</td>";
			contenthtml += "<td>"+data[key].bags+"</td>";
			contenthtml += "<td>"+data[key].stores+"</td>";
			contenthtml += "<td>"+data[key].equips+"</td>";
			contenthtml += "</tr>";
		}
		contenthtml += "</tbody>";
		contenthtml += "</table>";
		$("#getPackage").html(contenthtml);
	}
	
	function exportExcel(){
		$("#"+exId+" table").eq(0).tableExport({type: 'excel', escape: 'false'});		
	}
	
	function selectConditionChange(selectType){
		if(selectType!='-1'){
			$("#conditionDiv").css("display","inline");
			$("#dateDiv").css("display","none");
		}else{
			$("#conditionDiv").css("display","none");
			$("#dateDiv").css("display","inline");
		}
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
			<div style="float:left;">
			<label class="label">${msg['jsp.packageSearch.condition']}</label>&nbsp;&nbsp;
			<select id="selectType" name="selectType" class="span2" onchange="selectConditionChange(this.value);">
				<option value="-1">${msg['jsp.packageSearch.allroleId']}</option>
				<option value="0">${msg['jsp.packageSearch.roleId']}</option>
				<option value="1">${msg['jsp.packageSearch.roleName']}</option>
			</select>
			</div>
			<div id="conditionDiv" style="float:left;">&nbsp;&nbsp;<input type="text" id="condition" name="condition"/></div>
			<div id="dateDiv"  style="float:left;">
				<label class="label">${msg['statistic.public.time']}</label>&nbsp;&nbsp;
				<div class="input-append date" id="start">
				<input style="width: 120px;" name="startDate" size="20" value="${nowDate}" type="text"
					readonly> <span class="add-on"><i class="icon-th"></i></span>
				</div>
				-
				<div class="input-append date" id="end">
					<input style="width: 120px;" name="endDate" size="20" value="${nowDate}"  type="text" readonly>
					<span class="add-on"><i class="icon-th"></i></span>
				</div>
			</div>
			<br/>
			<br/>
			${msg['statistic.public.isblack']}：<input type="checkbox" id="isblack"/>&nbsp;&nbsp;
			<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
			&nbsp;&nbsp;
			<input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel();">
		</form>
		
		<p id="msg"></p>
	</div>
	<div class="tab-content">
		<div class="tab-pane active" id="getPackage">
		${msg['statistic.public.content']}:<span style="color: red;">${msg['jsp.packageSearch.packageInfo']}</span>
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