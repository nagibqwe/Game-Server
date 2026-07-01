<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['statistic.goldConsume.pagetitle']}</title>
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
<script src="${base}/js/echarts/echarts.js"></script>
<script type="text/javascript">
	var base = '${base}';
	$(function() {
		getGroup();
		setTimeout(function () {
			var groupName = $("#select_group").val();
			getChannelName(groupName);
		}, 100);
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
		$("#loading").shCircleLoader();
	});

	var isSearch = 0;
	var exId = "";
	function search(){
		isSearch = 1;
		searchData("getSecondRecharge");
	}
	
	function searchData(actionType){
		if ($("input[name='serverId']:checked").length == 0) {
			alert("${msg['statistic.public.syspromt.server']}");
			return;
		}
		exId = actionType + "List";
		$("#loadingmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
		url : base + "/secondRecharge/"+actionType,
		data : $("#query_form").serialize(),
		method : "post",
		dataType : "json",
		success : function(data) {
			console.log(data);
			$("#loadingmodal").modal('hide');
			if (data==null) {
				return;
			}
			setSecondRechargeData(data);
		}
		});
	}
	function setSecondRechargeData(dataObj){
		var timeGapMap = dataObj[0];
		var projectMap = dataObj[1];
		var contentHtml = "";
		contentHtml += "<h4>${msg['statistic.secondRecharge.gap']}</h4>";
		contentHtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
		contentHtml += "<tbody>";
		contentHtml += "<tr align='center' style='font-weight: bolder;'>";
		contentHtml += "<td>${msg['statistic.secondRecharge.gapDays']}</td>";
		contentHtml += "<td>${msg['statistic.secondRecharge.userCounts']}</td>";
		contentHtml += "</tr>";
		for(var key in timeGapMap){
			contentHtml += "<tr align='center'>";
			contentHtml += "<td>"+key+"</td>";
			contentHtml += "<td>"+timeGapMap[key]+"</td>";
			contentHtml += "</tr>";
		}
		contentHtml += "</tbody>";
		contentHtml += "</table><br/><br/>";
		contentHtml += "<h4>${msg['statistic.secondRecharge.project']}</h4>";
		contentHtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
		contentHtml += "<tbody>";
		contentHtml += "<tr align='center' style='font-weight: bolder;'>";
		contentHtml += "<td>${msg['statistic.secondRecharge.projects']}</td>";
		contentHtml += "<td>${msg['statistic.secondRecharge.userCounts']}</td>";
		contentHtml += "</tr>";
		for(var key in projectMap){
			contentHtml += "<tr align='center'>";
			contentHtml += "<td>"+key+"</td>";
			contentHtml += "<td>"+projectMap[key]+"</td>";
			contentHtml += "</tr>";
		}
		contentHtml += "</tbody>";
		contentHtml += "</table><br/><br/>";
		$("#getSecondRechargeList").html(contentHtml);
	} 
	function exportExcel(){
		$("#"+exId+" table").eq(0).tableExport({type: 'excel', escape: 'false'});		
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="query_form" class="well form-inline">
			<label class="label" for="select_group">${msg['statistic.public.platform']}</label>&nbsp;&nbsp;
			<select id="select_group" name="groupName" onchange="getServer(this.value);getChannelName(this.value);"></select>
			<br/><br/>

			<label class="label">${msg['statistic.public.server']}</label>&nbsp;&nbsp;
			<span id="checkbox_server"></span>
			<br/><br/>

			<label class="label">${msg['statistic.public.Channel']}</label>&nbsp;&nbsp;
			<span id="checkbox_channel"></span>
			<br/><br/>

			<label class="label">${msg['statistic.public.time']}</label>&nbsp;&nbsp;
			<div class="input-append date" id="start">
				<input style="width: 120px;" name="startDate" size="20" value="${newDate}" type="text" readonly>
				<span class="add-on"><i class="icon-th"></i></span>
			</div>
			-
			<div class="input-append date" id="end">
				<input style="width: 120px;" name="endDate" size="20" value="${newDate}" type="text" readonly>
				<span class="add-on"><i class="icon-th"></i></span>
			</div>
			<br/><br/>

			<label class="label">${msg['statistic.public.isblack']}</label>
			<input type="checkbox" id="isBlackList"/>&nbsp;&nbsp;
			<br/><br/>

			<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
			<input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel();"><br/><br/>
		</form>
		<div id="msg"></div>
		<ul class="nav nav-tabs" id="myTab">
			<li class="active" id="getSecondRechargeLi">
				<a href="#getSecondRechargeList" data-toggle="tab">
					${msg['statistic.secondRecharge.data']}
				</a>
			</li>
		</ul>
	</div>
	<div class="container-fluid">
		<div class="tab-content">
			<div class="tab-pane active" id="getSecondRechargeList"></div>
		</div>
	</div>
	<jsp:include page="../commonmodal.jsp"/>
</body>
</html>

