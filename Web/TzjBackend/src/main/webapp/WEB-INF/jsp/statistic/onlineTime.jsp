<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['statistic.onlinetime.pagetitle']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<link rel="stylesheet" href="${base}/css/pagination.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.pagination.js"></script>
<script type="text/javascript">
	var base = '${base}';
	$(function() {
		getGroup();
		$("#loading").shCircleLoader();
		$("#pagerDiv").css("display","none");
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

	var exId = "";

	function search() {
		statisticData(1,"onlineTimeStatistic");
		$("#onlineLi").addClass("active");
		$("#dailyonlineLi").removeClass("active");
		$("#onlineTimeStatistic").addClass("active");
		$("#dailyOnlineTimeStatistic").removeClass("active");
	}

	function statisticData(currentPage,actionType){
		exId = actionType;
		var pageSize = $("input[name='pageSize']").val();
		var fromData = $("#query_form").serializeObject();
		//加载
		$("#loadingmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
			url : base + "/onlinestatistic/" + actionType,
			data : fromData,
			method : "post",
			dataType : "json",
			success : function(data) {
				$("#loadingmodal").modal('hide');
				if(data == null){
					return;
				}
				console.log(actionType);
				if(actionType == "onlineTimeStatistic"){
					setOnLineTimeHtml(data);
				}else if(actionType == "dailyOnlineTimeStatistic"){
					setDailyOnlineTimeHtml(data);
				}else if(actionType == "dailyOnlineTimeCount"){
					setDataCounts(pageSize , data);
				}
			}
		});
	}

	//显示一共有多少条
	function setDataCounts(pageSize , dataObj){
		var recordCounts = 0 ;
		var recordCount = 0;
		for(var i= 0 ; i < dataObj.length ; i++){
			recordCounts += parseInt(dataObj[i].counts);		
			if(parseInt(dataObj[i].counts) >= recordCount )
				recordCount = parseInt(dataObj[i].counts);
		}
		$("#recordCount").html(recordCounts);
		$("#pagerDiv").css("display","block");
		$("#pagerDiv").pagination({
	        recordCount: recordCount,
	        pageSize: pageSize,
	        onPageChange: function (pageIndex) {
	        	// statisticData(pageIndex,"dailyOnlineTimeStatistic");
	        }
		});
	}
	
	function setOnLineTimeHtml(data){
		var onlinehtml = "";
		onlinehtml +="<table class='table table-bordered table-striped'>";
		onlinehtml += "<tbody>";
		onlinehtml += "<tr align='center' style='font-weight: bolder;'>";
		onlinehtml += "<td>${msg['statistic.onlinetime.time']}</td>";
		onlinehtml += "<td>${msg['statistic.onlinetime.rolecount']}</td>";
		onlinehtml += "</tr>";
		for(var key in data){
			onlinehtml += "<tr>";
			onlinehtml += "<td>"+key+"</td>";
			onlinehtml += "<td>"+data[key]+"</td>";
			onlinehtml += "</tr>";
		}
		onlinehtml += "</tbody>";
		onlinehtml += "</table>";
		$("#onlineTimeStatistic").html(onlinehtml);
	}
	
	function setDailyOnlineTimeHtml(data){
		var contenthtml = "";
		contenthtml +="<table class='table table-bordered table-striped'>";
		contenthtml += "<tbody>";
		contenthtml += "<tr align='center' style='font-weight: bolder;'>";
		contenthtml += "<td>${msg['statistic.onlinetime.userId']}</td>";
		contenthtml += "<td>${msg['statistic.onlinetime.roleId']}</td>";
		contenthtml += "<td>${msg['statistic.onlinetime.roleName']}</td>";
		contenthtml += "<td>${msg['statistic.onlinetime.createTime']}</td>";
		contenthtml += "<td>${msg['statistic.onlinetime.platformName']}</td>";
		contenthtml += "<td>${msg['statistic.onlinetime.serverId']}</td>";
		contenthtml += "<td>${msg['statistic.onlinetime.level']}</td>";
		contenthtml += "<td>${msg['statistic.onlinetime.onlineTime']}</td>";
		contenthtml += "<td>${msg['statistic.onlinetime.time']}</td>";
		contenthtml += "</tr>";
		for(var key in data){
			contenthtml += "<tr>";
			contenthtml += "<td>"+data[key].userId+"</td>";
			contenthtml += "<td>"+data[key].roleId+"</td>";
			contenthtml += "<td>"+data[key].roleName+"</td>";
			contenthtml += "<td>"+data[key].createTime+"</td>";
			contenthtml += "<td>"+data[key].platformName+"</td>";
			contenthtml += "<td>"+data[key].serverId+"</td>";
			contenthtml += "<td>"+data[key].level+"</td>";
			contenthtml += "<td>"+data[key].onlineTime+"</td>";
			contenthtml += "<td>"+data[key].time+"</td>";
			contenthtml += "</tr>";
		}
		contenthtml += "</tbody>";
		contenthtml += "</table>";
		$("#dailyOnlineTimeStatistic").html(contenthtml);
	}
	
	function exportExcel(){
		if(exId=="onlineTimeStatistic"){
			$("#"+exId+" table").eq(0).tableExport({type: 'excel', escape: 'false'});
		}else{
			$("#query_form").submit();
		}		
	}
	
	function selectConditionChange(condition){
		if(condition==1 || condition==3){
			$("#minPayDiv").css("display","inline");
			$("#maxPayDiv").css("display","inline");
		}else{
			$("#minPayDiv").css("display","none");
			$("#maxPayDiv").css("display","none");
		}
	}
	
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="${base}/onlinestatistic/exportExcel" id="query_form" class="well form-inline">
			<label class="label">${msg['statistic.public.platform']}</label>&nbsp;&nbsp;
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
				<input style="width: 120px;" name="startDate" size="20" type="text" value="${nowDate}" readonly> 
				<span class="add-on"><i class="icon-th"></i></span>
			</div>
			-
			<div class="input-append date" id="end">
				<input style="width: 120px;" name="endDate" size="20" type="text" value="${nowDate}" readonly>
				<span class="add-on"><i class="icon-th"></i></span>
			</div>
			<br/><br/>

			<label class="label" for="select_condition">${msg['statistic.onlinetime.condition']}</label>
			<select id="select_condition" name="selectCondition" class="span2" onchange="selectConditionChange(this.value);">
				<option value="-1">${msg['statistic.onlinetime.allplayer']}</option>
				<option value="0">${msg['statistic.onlinetime.player']}</option>
				<option value="1">${msg['statistic.onlinetime.payPlayer']}</option>
				<option value="2">${msg['statistic.onlinetime.losePlayer']}</option>
				<option value="3">${msg['statistic.onlinetime.loseRechargePlayer']}</option>
			</select>
			<label class="label" for="minPay">${msg['statistic.onlinetime.minPay']}</label>
			<input type="text" id="minPay" name="minPay" class="span2"/>
			<label class="label" for="maxPay">${msg['statistic.onlinetime.maxPay']}</label>
			<input type="text" id="maxPay" name="maxPay" class="span2"/>
			<label class="label" for="isblack">${msg['statistic.public.isblack']}</label>
			<input type="checkbox" id="isblack"/>&nbsp;&nbsp;
			<br/><br/>

			<label class="label" id="pageLabel">${msg['jsp.log.pageSize']}</label>
			<input id="pageSize" name="pageSize" value="1000" class="span1 validate[required,custom[integer],min[1]]">
			${msg['jsp.log.all']}：<label id="recordCount">0</label>${msg['jsp.log.record']}
			<br/><br/>

			<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
			<input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel()">
			<br/><br/>

			<!-- 分页页面部分 -->
			<div id="pagerDiv" class="pagerDiv">
		        <span name="first" class="disabled" style="cursor: pointer;">首页</span>
		        <span name="prev" class="disabled" style="cursor: pointer;">上一页</span>
		        <span name="nav" style="cursor: pointer;"><a class="navi"><span style="color: #999">1</span></a></span>
		        <span name="next" class="disabled" style="cursor: pointer;">下一页</span>
		        <span name="last" class="disabled" style="cursor: pointer;">末页</span>
		    </div>
		</form>
		
		<div id="msg"></div>
		<ul class="nav nav-tabs" id="myTab">
			<li class="active" id="onlineLi">
				<a href="#onlineTimeStatistic" data-toggle="tab" onclick="statisticData(1,'onlineTimeStatistic');">
					${msg['statistic.onlinetime.pagetitle']}
				</a>
			</li>
			<li id="dailyonlineLi">
				<a href="#dailyOnlineTimeStatistic" data-toggle="tab"
				    onclick="statisticData(1,'dailyOnlineTimeCount'); statisticData(1,'dailyOnlineTimeStatistic');">
					${msg['statistic.onlinetime.dailyOnline']}
				</a>
			</li>
		</ul>

		<div class="tab-content">
			<div class="tab-pane active" id="onlineTimeStatistic"></div>
			<div class="tab-pane" id="dailyOnlineTimeStatistic"></div>
		</div>
	</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>