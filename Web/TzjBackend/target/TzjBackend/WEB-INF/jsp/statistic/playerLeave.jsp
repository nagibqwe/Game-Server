<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['statistic.playerLeave.pagetitle']}</title>
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
	var exId = "";//用于导出excel
	function search(){
		isSearch = 1;
		statisticData("plCount","playerLeaveCount");
		exId = "plCount";
		$("#count").addClass("active");
		$("#rank").removeClass("active");
		$("#money").removeClass("active");
		$("#plCount").addClass("active");
		$("#plRank").removeClass("active");
		$("#plMoney").removeClass("active");
	}
	/**
	*用户流失数，流失率，流失付费用户
	**/
	function statisticData(id, actionType){
		if ($("input[name='serverId']:checked").length == 0) {
			alert("${msg['statistic.public.syspromt.server']}");
			return;
		}
		exId = id;
		$("#loadingmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
			url : base + "/plstatistic/"+actionType,
			data : $("#query_form").serialize(),
			method : "post",
			dataType : "json",
			success : function(data) {
				$("#loadingmodal").modal('hide');
				if(data == null){
					return;
				}
				var countHtml;
				if(id == "plCount"){
					countHtml = getCountContentHtml(data);
				}else if(id == "plRank"){
					countHtml = getRankContentHtml(data);
				}else{
					countHtml = getAmountContentHtml(data);
				}
				$("#"+id).html(countHtml);
			}
		});
		
		function getCountContentHtml(dataObj){
			var selectCondition = $("#select_condition").val();
			var plCountHtml = "<table class='table table-bordered table-striped'>";
			plCountHtml += "<tbody>";
			plCountHtml += "<tr align='center' style='font-weight: bolder;'>";
			plCountHtml += "<td>${msg['statistic.public.date']}</td>";
			plCountHtml += "<td>${msg['statistic.playerLeave.ordfcount']}</td>";
			plCountHtml += "<td>${msg['statistic.playerLeave.ordlcount']}</td>";
			plCountHtml += "<td>${msg['statistic.playerLeave.ordrate']}</td>";
			plCountHtml += "</tr>";
			if(selectCondition==0){
				for(var i=0; i<dataObj.length; i++){
					plCountHtml += "<tr>";
					plCountHtml += "<td>"+dataObj[i].date+"</td>";
					plCountHtml += "<td>"+dataObj[i].ordfcount+"</td>";
					plCountHtml += "<td>"+dataObj[i].ordlcount+"</td>";
					plCountHtml += "<td>"+dataObj[i].ordrate.toFixed(2)+"</td>";
					plCountHtml += "</tr>";
				}
			}else{
				for(var i=0; i<dataObj.length; i++){
					plCountHtml += "<tr>";
					plCountHtml += "<td>"+dataObj[i].date+"</td>";
					plCountHtml += "<td>"+dataObj[i].vipfcount+"</td>";
					plCountHtml += "<td>"+dataObj[i].viplcount+"</td>";
					plCountHtml += "<td>"+dataObj[i].viprate.toFixed(2)+"</td>";
					plCountHtml += "</tr>";
				}
			}

			plCountHtml += "</tbody>";
			plCountHtml += "</table>";
			return plCountHtml;
		}
		
		/**
		*普通用户等级分布，付费用户等级分布
		**/
		function getRankContentHtml(dataObj){
			var amountHtml = "";
			amountHtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
			amountHtml += "<tbody>";
			amountHtml += "<tr align='center' style='font-weight: bolder;'>";
			amountHtml += "<td>${msg['statistic.playerLeave.lvdistri']}</td>";
			amountHtml += "<td>${msg['statistic.playerLeave.ordlcount']}</td>";
			amountHtml += "<td>${msg['statistic.playerLeave.viplcount']}</td>";
			amountHtml += "</tr>";
			for(var i=0; i<dataObj.length; i++){
				var ordMap = dataObj[i].ordMap;
				var vipMap = dataObj[i].vipMap;
				for(var key in ordMap){
					amountHtml += "<tr>";
					amountHtml += "<td>"+key+"</td>";
					amountHtml += "<td>"+ordMap[key]+"</td>";
					amountHtml += "<td>"+vipMap[key]+"</td>";
					amountHtml += "</tr>";
				}
			}
			amountHtml += "</tbody>";
			amountHtml += "</table>";
			return amountHtml;
		}
		
		/**
		 *流失用户付费金额分布，付费次数分布
		 */
		function getAmountContentHtml(dataObj){
			var ranlHtml = "";
			ranlHtml +="<table class='table table-bordered table-striped'>";
			ranlHtml += "<tbody>";
			ranlHtml += "<tr align='center' style='font-weight: bolder;'>";
			ranlHtml += "<td>${msg['statistic.playerLeave.amountSum']}</td>";
			ranlHtml += "<td>${msg['statistic.playerLeave.lvpaysum']}</td>";
			ranlHtml += "<td>${msg['statistic.playerLeave.lvpaymoney']}</td>";
			ranlHtml += "</tr>";
			for(var i=0; i<dataObj.length; i++){
				var paylcount = dataObj[i].paylcount;
				var rechargeSum = dataObj[i].rechargeSum;
				for(var key in paylcount){
					ranlHtml += "<tr>";
					ranlHtml += "<td>"+key+"</td>";
					ranlHtml += "<td>"+paylcount[key]+"</td>";
					ranlHtml += "<td>"+rechargeSum[key]+"</td>";
					ranlHtml += "</tr>";
				}
			}
			ranlHtml += "</tbody>";
			ranlHtml += "</table>";
			return ranlHtml;
		}
	}
	function exportExcel(){
		var contentHtml = $("#"+exId).html();
		if(contentHtml == ""){
			return;
		}
		$("#"+exId+" table").eq(0).tableExport({type: 'excel', escape: 'false'});		
	}
	
	function selectConditionChange(condition){
		if(condition==1){
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
	<form action="#" id="query_form" class="well form-inline">
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
			<input style="width: 120px;" name="startDate" size="20" value="${nowDate}" type="text" readonly>
			<span class="add-on"><i class="icon-th"></i></span>
		</div>
		-
		<div class="input-append date" id="end">
			<input style="width: 120px;" name="endDate" size="20" value="${nowDate}" type="text" readonly>
			<span class="add-on"><i class="icon-th"></i></span>
		</div>
		<br/><br/>

		<label class="label">${msg['statistic.onlinetime.condition']}</label>
		<select id="select_condition" name="select_condition" class="span2" onchange="selectConditionChange(this.value);">
			<option value="0">${msg['statistic.onlinetime.player']}</option>
			<option value="1">${msg['statistic.onlinetime.payPlayer']}</option>
		</select>
		<label class="label" for="minPay">${msg['statistic.onlinetime.minPay']}</label>
		<input type="text" id="minPay" name="minPay" class="span2"/>
		<label class="label" for="maxPay">${msg['statistic.onlinetime.maxPay']}</label>
		<input type="text" id="maxPay" name="maxPay" class="span2"/>
		<label class="label" for="isblack">${msg['statistic.public.isblack']}</label>
		<input type="checkbox" id="isblack"/>
		<br/><br/>

		<input type="button" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
		<input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel()">
	</form>
	
	<div id="msg"></div>
	<ul class="nav nav-tabs" id="myTab">
		<li class="active" id="count">
			<a href="#plCount" data-toggle="tab" onclick="statisticData('plCount','playerLeaveCount');">
				${msg['statistic.playerLeave.playerLeaveCount']}
			</a>
		</li>
		<li id="rank">
			<a href="#plRank" data-toggle="tab" onclick="statisticData('plRank','playerLeaveRank');">
				${msg['statistic.playerLeave.playerLeaveRank']}
			</a>
		</li>
		<li id="money">
			<a href="#plMoney" data-toggle="tab" onclick="statisticData('plMoney','playerLeaveAmount');">
				${msg['statistic.playerLeave.playerLeaveAmount']}
			</a>
		</li>
	</ul>
</div>

<div class="container-fluid">
	<div class="tab-content">
		<div class="tab-pane active" id="plCount"></div>
		<div class="tab-pane" id="plRank"></div>
		<div class="tab-pane" id="plMoney"></div>
	</div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>
