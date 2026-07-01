<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['statistic.playerPay.pagetitle']}</title>
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
	var expId = "";//用于导出
	var idLists = ["added","payAdded","rank","payRank","interval","payInterval","firstpay","payFirstpay"];
	
	var panelIdList = [];//主要是为了避免用户在使用时，又去请求服务器
	function search(){
		panelIdList = [];//设置成默认
		isSearch = 1;
		var initId1 = "added";
		var initId2 = "payAdded";
		statisticData(initId1,initId2,"addedStatistic",getPayAddedContentHtml);
		//设置选项卡的显示
		
		pannelShow(initId1,initId2,idLists);
	}
	
	function statisticData(id1,id2,actionType,target){
		if ($("input[name='serverId']:checked").length == 0) {
			alert("${msg['statistic.public.syspromt.server']}");
			return;
		}
		expId = id2;
		var isContain = 0;
		for(var i=0; i<panelIdList.length; i++){
			if(panelIdList[i] == id2){
				isContain = 1;
				break;
			}
		}
		if(!isContain){
			panelIdList.push(id2);
		}else{
			pannelShow(id1,id2,idLists);
			return;
		}
		$("#loadingmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
			url : base + "/paystatistic/"+actionType,
			data : $("#query_form").serialize(),
			method : "post",
			dataType : "json",
			success : function(data) {
				$("#loadingmodal").modal('hide');
				//console.log(data);
				if(data == null){
					return;
				}
				var countHtml = target(data);
				$("#"+id2).html(countHtml);
			}
		});
	}

	function getPayAddedContentHtml(dataObj){
		if(!dataObj.ok){
			alert(dataObj.msg);
			return;
		}
		var dataList = dataObj.dataList;
		var contentHtml = "";
		contentHtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
		contentHtml += "<tbody>";
		contentHtml += "<tr align='center' style='font-weight: bolder;'>";
		contentHtml += "<th>${msg['statistic.public.date']}</th>";
		contentHtml += "<th>${msg['statistic.playerPay.addnewer']}</th>";
		contentHtml += "<th>${msg['statistic.playerPay.addvipnewer']}</th>";
		contentHtml += "<th>${msg['statistic.playerPay.addpaymoney']}（首次）</th>";
		contentHtml += "<th>${msg['statistic.playerPay.addpaymoney']}（首日）</th>";
		contentHtml += "</tr>";
		for(var i=0; i<dataList.length; i++){
			contentHtml += "<tr>";
			contentHtml += "<td>"+dataList[i].date+"</td>";
			contentHtml += "<td>"+dataList[i].allCreateRoleCounts+"</td>";
			contentHtml += "<td>"+dataList[i].allRechargeRoleCounts+"</td>";
			contentHtml += "<td>"+dataList[i].allfirstRechargeCounts+"</td>";
			contentHtml += "<td>"+dataList[i].allfirstDayRechargeCounts+"</td>";
			contentHtml += "</tr>";
		}
		contentHtml += "</tbody>";
		contentHtml += "</table>";
		return contentHtml;
	}
		
	/**
	*付费等级—金额，付费等级—次数
	**/
	function getPayRankContentHtml(dataObj){
		if(!dataObj.ok){
			alert(dataObj.msg);
			return;
		}
		var data = dataObj.mapData;
		var contentHtml = "";
		contentHtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
		contentHtml += "<tbody>";
		contentHtml += "<tr align='center' style='font-weight: bolder;'>";
		contentHtml += "<th>${msg['statistic.playerPay.payLevel']}</th>";
		contentHtml += "<th>${msg['statistic.playerPay.payRoleCount']}</th>";
		contentHtml += "<th>${msg['statistic.playerPay.paycounts']}</th>";
		contentHtml += "<th>${msg['statistic.playerPay.payAmount']}</th>";
		contentHtml += "</tr>";
		for(var i=0; i<data.length; i++){
			contentHtml += "<tr>";
			contentHtml += "<td>"+data[i].levels+"</td>";
			contentHtml += "<td>"+data[i].roleCount+"</td>";
			contentHtml += "<td>"+data[i].fisrtPayCount+"</td>";
			contentHtml += "<td>"+data[i].amountSum+"</td>";
			contentHtml += "</tr>";
		}
		contentHtml += "</tbody>";
		contentHtml += "</table>";
		return contentHtml;
	}
		
	function getPayIntervalContentHtml(dataObj){
		if(!dataObj.ok){
			alert(dataObj.msg);
			return;
		}
		var data = dataObj.mapData;
		var timeDisStrList = data.timeDisStrList;
		var firstCount = data.firstCount;
		var secondCount = data.secondCount;
		var thirdCount = data.thirdCount;
		var contentHtml = "";
		contentHtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
		contentHtml += "<tbody>";
		contentHtml += "<tr align='center' style='font-weight: bolder;'>";
		contentHtml += "<th>${msg['statistic.playerPay.timeGap']}</th>";
		contentHtml += "<th>${msg['statistic.playerPay.firstRechargeRole']}</th>";
		contentHtml += "<th>${msg['statistic.playerPay.secondRechargeRole']}</th>";
		contentHtml += "<th>${msg['statistic.playerPay.thirdRechargeRole']}</th>";
		contentHtml += "</tr>";
		for(var i=0; i<timeDisStrList.length; i++){
			contentHtml += "<tr>";
			contentHtml += "<td>"+timeDisStrList[i]+"</td>";
			contentHtml += "<td>"+firstCount[i]+"</td>";
			contentHtml += "<td>"+secondCount[i]+"</td>";
			contentHtml += "<td>"+thirdCount[i]+"</td>";
			contentHtml += "</tr>";
		}
		contentHtml += "</tbody>";
		contentHtml += "</table>";
		return contentHtml;
	}
		
	function exportExcel(){
		if(expId == ""){
			return;
		}
		$("#"+expId+" table").eq(0).tableExport({type: 'excel', escape: 'false'});		
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
			<input style="width: 120px;" name="startDate" size="20" value="${nowDate}" type="text" readonly>
			<span class="add-on"><i class="icon-th"></i></span>
		</div>
		-
		<div class="input-append date" id="end">
			<input style="width: 120px;" name="endDate" size="20" value="${nowDate}" type="text" readonly>
			<span class="add-on"><i class="icon-th"></i></span>
		</div>
		<br/><br/>

		<label class="label" for="isblack">${msg['statistic.public.isblack']}</label>
		<input type="checkbox" id="isblack"/>
		<br/><br/>

		<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
		<input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel()"><br/><br/>
	</form>
	
	<div id="msg"></div>
	<ul class="nav nav-tabs" id="myTab">
		<li class="active" id="added">
			<a href="#payAdded" data-toggle="tab" onclick="statisticData('added','payAdded','addedStatistic',getPayAddedContentHtml);">
				${msg['statistic.playerPay.addvipnewer']}
			</a>
		</li>
		<li id="rank">
			<a href="#payRank" data-toggle="tab" onclick="statisticData('rank','payRank','rankStatistic',getPayRankContentHtml);">
				${msg['statistic.playerPay.payRank']}
			</a>
		</li>
		<li id="interval">
			<a href="#payInterval" data-toggle="tab" onclick="statisticData('interval','payInterval','intervalStatistic',getPayIntervalContentHtml);">
				${msg['statistic.playerPay.payInterval']}
			</a>
		</li>
	</ul>
</div>

<div class="container-fluid">
	<div class="tab-content">
		<div class="tab-pane active" id="payAdded"></div>
		<div class="tab-pane" id="payRank"></div>
		<div class="tab-pane" id="payInterval"></div>
	</div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>
