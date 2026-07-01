<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['statistic.playeractive.title']}</title>
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
		getGroup();
		$("#loading").shCircleLoader();
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
	});

	var isSearch = 0;
	var exId = "";

	function search(){
		isSearch = 1;
		statisticData("userRole","userRoleCount");
		$("#userRole").addClass("active");
		$("#user").removeClass("active");
		$("#userRoleCount").addClass("active");
		$("#userCount").removeClass("active");
	}
	
	function statisticData(id,actionType){
		if ($("input[name='serverId']:checked").length == 0) {
			alert("${msg['statistic.public.syspromt.server']}");
			return;
		}
		exId = actionType;
		var cloneIds = [];
		$("input[name='cloneId']:checked").each(function(){ 
			cloneIds.push($(this).val());
        });

		$("#loadingmodal").modal({backdrop:'static',keyboard:false});
		var formData = $("#query_form").serializeObject();
		formData['cloneIds'] = cloneIds.join(",");
		$.ajax({
			url : base + "/playeractive/"+actionType,
			data : formData,
			method : "post",
			dataType : "json",
			success : function(data) {
				console.log(data);
				$("#loadingmodal").modal('hide');
				if(data == null){
					return;
				}
				if(id == "userRole"){
					userRoleCount(data);
				}else if(id == "user"){
					userCount(data);
				}
			}
		});
	}

	function userRoleCount(dataObj){
		var contentHtml = "";
		contentHtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
		contentHtml += "<tbody>";
		contentHtml += "<tr align='center' style='font-weight: bolder;'>";
		contentHtml += "<td>${msg['statistic.playeractive.row']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.userId']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.roleId']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.roleName']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.platformName']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.level']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.serverId']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.cloneName']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.camp']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.count']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.date']}</td>";
		contentHtml += "</tr>";
		for(var key in dataObj){
			var row = parseInt(key) +1;
			contentHtml += "<tr align='center'>";
			contentHtml += "<td>"+row+"</td>";
			contentHtml += "<td>"+dataObj[key].userId+"</td>";
			contentHtml += "<td>"+dataObj[key].roleId+"</td>";
			contentHtml += "<td>"+dataObj[key].roleName+"</td>";
			contentHtml += "<td>"+dataObj[key].platformName+"</td>";
			contentHtml += "<td>"+dataObj[key].level+"</td>";
			contentHtml += "<td>"+dataObj[key].sid+"</td>";
			contentHtml += "<td>"+dataObj[key].cloneId+"</td>";
			contentHtml += "<td>"+dataObj[key].groupNo+"</td>";
			contentHtml += "<td>"+dataObj[key].counts+"</td>";
			contentHtml += "<td>"+dataObj[key].joinTime+"</td>";
			contentHtml += "</tr>";
		}
		contentHtml += "</tbody>";
		contentHtml += "</table>";
		$("#userRoleCount").html(contentHtml);
	}

	function userCount(dataObj){
		var contentHtml = "";
		contentHtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
		contentHtml += "<tbody>";
		contentHtml += "<tr align='center' style='font-weight: bolder;'>";
		contentHtml += "<td>${msg['statistic.playeractive.row']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.userId']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.serverId']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.platformName']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.cloneName']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.camp']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.count']}</td>";
		contentHtml += "<td>${msg['statistic.playeractive.date']}</td>";
		contentHtml += "</tr>";
		for(var key in dataObj){
			var row = parseInt(key) +1;
			contentHtml += "<tr align='center'>";
			contentHtml += "<td>"+row+"</td>";
			contentHtml += "<td>"+dataObj[key].userId+"</td>";
			contentHtml += "<td>"+dataObj[key].sid+"</td>";
			contentHtml += "<td>"+dataObj[key].platformName+"</td>";
			contentHtml += "<td>"+dataObj[key].cloneId+"</td>";
			contentHtml += "<td>"+dataObj[key].groupNo+"</td>";
			contentHtml += "<td>"+dataObj[key].counts+"</td>";
			contentHtml += "<td>"+dataObj[key].joinDate+"</td>";
			contentHtml += "</tr>";
		}
		contentHtml += "</tbody>";
		contentHtml += "</table>";
		$("#userCount").html(contentHtml);
	}

	function exportExcel(){
		$("#query_form").submit();	
	}
	
	function selectAllCloneId(){
		var che = $("#selectAllClone").is(":checked");
		$("[name='cloneId']").prop("checked", che);
	}
	
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="${base}/playeractive/exportExcel" id="query_form" class="well form-inline">
			<label class="label">${msg['statistic.public.platform']}</label>&nbsp;&nbsp;
			<select id="select_group" onchange="getServer(this.value);getChannelName(this.value);"></select>
			<br/><br/>

			<label class="label">${msg['statistic.public.server']}</label>&nbsp;&nbsp;
			<span id="checkbox_server"></span>
			<br/><br/>

			<label class="label">${msg['statistic.public.Channel']}</label>&nbsp;&nbsp;
			<span id="checkbox_channel"></span>
			<br/><br/>

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
			<br/><br/>

			<label class="label">${msg['statistic.playeractive.cloneType']}</label>
			<input type="checkbox" name="cloneId" value="6001-未知副本"/>6001 &nbsp;
			<input type='checkbox' name='selectAllClone' id='selectAllClone' value="${msg['statistic.public.selectAll']}" onclick="selectAllCloneId();" />${msg['statistic.public.selectAll']}&nbsp;&nbsp;&nbsp;&nbsp;
			<br/><br/>

			<label class="label" for="isBlackList">${msg['statistic.public.isblack']}</label>
			<input type="checkbox" id="isBlackList"/>
			<br/><br/>

			<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
			<input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel()">
		</form>
	</div>
	<div id="msg"></div>
	<ul class="nav nav-tabs" id="myTab">
		<li class="active" id="userRole">
			<a href="#userRoleCount" data-toggle="tab" onclick="statisticData('userRole','userRoleCount');">
				${msg['statistic.playeractive.userRoleCount']}
			</a>
		</li>
		<li id="user">
			<a href="#userCount" data-toggle="tab" onclick="statisticData('user','userCount');">
				${msg['statistic.playeractive.userCount']}
			</a>
		</li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active" id="userRoleCount"></div>
		<div class="tab-pane" id="userCount"></div>
	</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>