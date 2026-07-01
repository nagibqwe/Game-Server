<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['fileds.roleCollection.pageTile']}</title>
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
		group_reload();
		setTimeout(function () {
			var groupName = $("#select_group").val();
			getChannelName(groupName);
		}, 100);
		$("#loading").shCircleLoader();
		$("#pagerDiv").css("display","none");
		$("#start").datetimepicker({
    	    language:  'zh-CN',
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
			language:  'zh-CN',
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
		searchData(1,"getDataCounts");
		searchData(1,"getRoleCollectionData");
	}
	
	function searchData(currentPage, actionType){
		var platfName = $("#select_group").find("option:selected").val();
		var serverId = $("#select_server").find("option:selected").val();
		var date = $("input[name='endDate']").val();
		var pageSize = $("input[name='pageSize']").val();
		var pageIndex = (currentPage-1)*parseInt(pageSize);
		var channelNames = [];
		$("input[name='channelName']:checked").each(function(){ 
			channelNames.push($(this).val());
        });
		var isBlack = $("#isBlackList").is(":checked");
		$("#loadingmodal").modal({backdrop:'static',keyboard:false});
		exId = actionType;
		$.ajax({
			url : base + "/roleCollection/"+actionType,
			data : {
				platfName : platfName,
				serverId : serverId,
				channelNames : channelNames+"",
				date : date,
				pageIndex : pageIndex,
				pageSize : pageSize,
				isBlack : isBlack
			},
			method : "post",
			dataType : "json",
			success : function(data) {
				console.log(data);
				$("#loadingmodal").modal('hide');
				if(data == null){
					return;
				}
				if(actionType == 'getRoleCollectionData'){
					setroleCollectionLog(data);
				}else if(actionType == 'getDataCounts'){
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
	        	searchData(pageIndex,"getRoleCollectionData");
	        }
		});
	}
	
	//将获取的数据在页面展示出来
	function setroleCollectionLog(dataObj){
		var contentHtml = "";
		contentHtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
		contentHtml += "<tbody>";
		contentHtml += "<tr align='center' style='font-weight: bolder;'>";
		contentHtml += "<td>${msg['fileds.roleCollection.platformName']}</td>";
		contentHtml += "<td>${msg['fileds.roleCollection.userId']}</td>";
		contentHtml += "<td>${msg['fileds.roleCollection.roleId']}</td>";
		contentHtml += "<td>${msg['fileds.roleCollection.roleName']}</td>";
		contentHtml += "<td>${msg['fileds.roleCollection.collectHorses']}</td>";
		contentHtml += "<td>${msg['fileds.roleCollection.collectWings']}</td>";
		contentHtml += "<td>${msg['fileds.roleCollection.collectPets']}</td>";
		contentHtml += "<td>${msg['fileds.roleCollection.date']}</td>";
		contentHtml += "</tr>";
		for(var i= 0 ; i < dataObj.length ; i++){
				contentHtml += "<tr align='center'>";
				contentHtml += "<td>"+dataObj[i].platformName+"</td>";
				contentHtml += "<td>"+dataObj[i].userId+"</td>";
				contentHtml += "<td>"+dataObj[i].roleId+"</td>";
				contentHtml += "<td>"+dataObj[i].roleName+"</td>";
				contentHtml += "<td>"+dataObj[i].horseIds+"</td>";
				contentHtml += "<td>"+dataObj[i].wingIds+"</td>";
				contentHtml += "<td>"+dataObj[i].petIds+"</td>";
				contentHtml += "<td>"+dataObj[i].date+"</td>";
				contentHtml += "</tr>";
		}
		contentHtml += "</tbody>";
		contentHtml += "</table>";
		$("#getroleCollection").html(contentHtml);
	}
	
	function exportExcel(){
		$("#query_form").submit();
	}
</script>
</head>
<body>
<div class="container-fluid">
	<form action="${base}/roleCollection/exportExcel" method="post" id="query_form" class="well form-inline">
		<select id="select_group" onchange="queryServerByGroup(this.value);getChannelName();" class="span2"></select>
		<select id="select_server" name="serverId" class="span2"></select>
		<br/><br/>

		<label class="label">${msg['statistic.public.Channel']}</label>
		<span id="checkbox_channel"></span>
		<br/><br/>

		<label class="label">${msg['jsp.log.time']}</label>
		<div class="input-append date" id="end">
			<input style="width: 120px;" name="endDate" size="20" type="text" value="${nowDate}" readonly>
			<span class="add-on"><i class="icon-th"></i></span>
		</div>
		<br/><br/>

		<label class="label" for="isBlackList">${msg['statistic.public.isblack']}</label>
		<input type="checkbox" id="isBlackList"/>&nbsp;&nbsp;
		<label class="label">${msg['jsp.log.pageSize']}</label>
		<input id="pageSize" type="text" name="pageSize" value="1000" class="span1 validate[required,custom[integer],min[1]]">
		&nbsp;&nbsp;${msg['jsp.log.all']}：<label id="recordCount">0</label>${msg['jsp.log.record']}
		<br/><br/>

		<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()"/>
		<input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel()"/>

		<div id="pagerDiv" class="pagerDiv">
			<span name="first" class="disabled" style="cursor: pointer;">首页</span>
			<span name="prev" class="disabled" style="cursor: pointer;">上一页</span>
			<span name="nav" style="cursor: pointer;"><a class="navi"><span style="color: #999">1</span></a></span>
			<span name="next" class="disabled" style="cursor: pointer;">下一页</span>
			<span name="last" class="disabled" style="cursor: pointer;">末页</span>
		</div>
	</form>
	<div class="tab-content">
		<div class="tab-pane active" id="getroleCollection"></div>
	</div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>