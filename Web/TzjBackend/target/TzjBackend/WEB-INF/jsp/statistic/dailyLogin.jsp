<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['statistic.dailyLogin.pageTile']}</title>
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
    	    language:  'zh-CN',
			format : 'yyyy-mm-dd',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			showMeridian : 1,
			endDate : new Date()
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
			showMeridian : 1,
			endDate : new Date()
		});
		$("#createStartDate").datetimepicker({
    	    language:  'zh-CN',
			format : 'yyyy-mm-dd',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			showMeridian : 1,
			endDate : new Date()
		});
		$("#createEndDate").datetimepicker({
			language:  'zh-CN',
			format : 'yyyy-mm-dd',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			showMeridian : 1,
			endDate : new Date()
		});
		$("#loading").shCircleLoader();
		$("#pagerDiv").css("display","none");
		$("#createDateDiv").css("display","none");
		var groupName = $("#select_group").find("option:selected").val();
		queryServerByGroupName(groupName);
	});
	
	var isSearch = 0;
	var exId = "";
	/* 点击查询按钮执行的方法 */
	function search(){
		isSearch = 1;
		var queryContent= $("#select_queryContent").find("option:selected").val();
		if(queryContent == "userLogin"){
			filedsData(1,"getUserLoginCounts");
			filedsData(1,"getUserLogin");
		}else if(queryContent == "roleLogin"){
			filedsData(1,"getRoleLoginCounts");
			filedsData(1,"getRoleLogin");
		}
	}
	
	function filedsData(currentPage ,actionType){
		exId = "dailyLoginData";
		var platfName = $("#select_group").find("option:selected").val();
		var serverIds = [];
		$("input[name='serverId']:checked").each(function(){ 
			serverIds.push($(this).val());
        });
		var condition = $("#select_condition").find("option:selected").val();
		var startDate = $("input[name='startDate']").val();
		var endDate = $("input[name='endDate']").val();
		var pageSize = $("input[name='pageSize']").val();
		var pageIndex = (currentPage-1)*parseInt(pageSize);
		var conditionContent = $("textarea[name='conditionContent']").val();
		var queryContent= $("#select_queryContent").find("option:selected").val();
		var createStartDate = "";
		var createEndDate = "";
		if(queryContent == "userLogin"){
			createStartDate = $("input[name='createStartDate']").val();
			createEndDate = $("input[name='createEndDate']").val();
			if(createStartDate == "" || createEndDate == ""){
				return;
			}
		}
		if(serverIds.length == 0){
			return;
		}
		if(isSearch == 0 || condition == "" || conditionContent == ""){
			return;
		}
		if(startDate == "" || endDate == ""){
			$("#syspromtinfo").html("${msg['statistic.public.syspromt.time']}");
			return;
		}
		console.log(serverIds);
		$("#loadingmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
		url : base + "/dailyLogin/"+actionType,
		data : {
			platfName : platfName,
			serverIds : serverIds+"",
			condition : condition,
			conditionContent : conditionContent,
			createStartDate : createStartDate,
			createEndDate : createEndDate,
			startDate : startDate,
			endDate : endDate,
			pageIndex : pageIndex ,
			pageSize : pageSize
		},
		method : "post",
		dataType : "json",
		success : function(data) {
			console.log(data);
			$("#loadingmodal").modal('hide');
			if(data == null){
				return;
			}
			if(actionType=='getUserLogin' || actionType=='getRoleLogin'){
				setData(data);
			}else if(actionType=='getUserLoginCounts'){
				setDataCounts(pageSize , data , "getUserLogin");
			}else if(actionType=='getRoleLoginCounts'){
				setDataCounts(pageSize , data , "getRoleLogin");
			}
		}
		});
		$("#syspromtinfo").html("");
	}
	//显示一共有多少条
	function setDataCounts(pageSize , dataObj , actionType){
		var recordCounts = 0 ;
		var recordCount = 0;
		var pageCount = 0;
		for(var i= 0 ; i < dataObj.length ; i++){
			recordCounts += parseInt(dataObj[i].counts);		
			if(parseInt(dataObj[i].counts) >= recordCount )
				recordCount = parseInt(dataObj[i].counts);
		}
		$("#recordCount").html(recordCounts);
		$("#pagerDiv").css("display","block");
		$("#pagerDiv").pagination({
	        recordCount: recordCount,       //总记录数(取最大，因为存在多个月的数据的情况)
	        pageSize: pageSize,                   //一页记录数
	        onPageChange: function (pageIndex) {
	        	filedsData(pageIndex,actionType);
	        }
		});
	}
	
	function setData(dataObj){
		var contentHtml = "";
		contentHtml +="<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
		contentHtml += "<tbody>";
		contentHtml += "<tr align='center' style='font-weight: bolder;'>";
		contentHtml += "<td>${msg['statistic.dailyLogin.userId']}</td>";
		contentHtml += "<td>${msg['statistic.dailyLogin.roleId']}</td>";
		contentHtml += "<td>${msg['statistic.dailyLogin.roleName']}</td>";
		contentHtml += "<td>${msg['statistic.dailyLogin.createTime']}</td>";
		contentHtml += "<td>${msg['statistic.dailyLogin.level']}</td>";
		contentHtml += "<td>${msg['statistic.dailyLogin.time']}</td>";
		
		contentHtml += "</tr>";
		for(var key in dataObj){
			contentHtml += "<tr align='center'>";
			contentHtml += "<td>"+dataObj[key].userId+"</td>";
			contentHtml += "<td>"+dataObj[key].roleId+"</td>";
			contentHtml += "<td>"+dataObj[key].roleName+"</td>";
			contentHtml += "<td>"+dataObj[key].createTime+"</td>";
			contentHtml += "<td>"+dataObj[key].level+"</td>";
			contentHtml += "<td>"+dataObj[key].time+"</td>";
			contentHtml += "</tr>";
		}
		contentHtml += "</tbody>";
		contentHtml += "</table>";
		$("#dailyLoginData").html(contentHtml);
	}
	
	function exportExcel(){
		$("#query_form").submit();
	};
	
	function displayDivByCondition(condition){
		if(condition == "userLogin"){
			$("#createDateDiv").css("display","block");
		}else{
			$("#createDateDiv").css("display","none");
		}
	};
	
	//平台名称下拉框变化后的响应方法
	function queryServerByGroupName(groupName){
		var dbServerList = '<%=request.getAttribute("dbServerList")%>';
		var list=eval("("+dbServerList+")");
		
		var contentHtml = "<label class=\"label\">${msg['statistic.public.server']}</label>&nbsp;&nbsp;";
		var groupNames = "";
		var serverIds="";
		for(var i= 0 ; i < list.length ; i++){
			if(groupName == list[i].groupName){
				contentHtml +="<input type='checkbox' name='serverId' value=\""+list[i].serverId+"\">["+list[i].serverId+"]"+list[i].serverName+"&nbsp;&nbsp;&nbsp;&nbsp";
			}
		}
		contentHtml +="<input type='checkbox' name='selectAll' id='selectAll' value=\"${msg['statistic.public.selectAll']}\" onclick=\"selectAllSid();\" />${msg['statistic.public.selectAll']}&nbsp;&nbsp;&nbsp;&nbsp;";
		$("#serverIdDiv").html(contentHtml);
	}
	
	function selectAllSid(){
		var che = $("#selectAll").is(":checked");
		$("[name='serverId']").prop("checked", che);
	}	
</script>
</head>
<body>
	<div class="container-fluid">
		<div>
			<form action="${base}/dailyLogin/exportExcel" method="post" id="query_form" class="well form-inline">
				<!-- 获取平台列表 -->
				<label class="label">${msg['statistic.public.platform']}</label>
				<select id="select_group" name="groupName"
					onchange="queryServerByGroupName(this.value)" class="span2" >
					<c:forEach var="groupName" items="${groupNameList}">
						<option value="${groupName}">
							${groupName}
						</option>
					</c:forEach>
				</select>
				<br/>
				<br/>
				<div id="serverIdDiv" >
					
				</div>
				<br/>
				<!-- 时间段 -->
				<label class="label">${msg['jsp.log.time']}</label>
				<div class="input-append date" id="start">
					<input style="width: 120px;" name="startDate" size="20" type="text" value="${nowDate}" 
						readonly> <span class="add-on"><i class="icon-th"></i></span>
				</div>
				-
				<div class="input-append date" id="end">
					<input style="width: 120px;" name="endDate" size="20" type="text" value="${nowDate}" 
						readonly> <span class="add-on"><i class="icon-th"></i></span>
				</div>
				<!-- 查询条件 -->
				<label class="label">${msg['statistic.dailyLogin.condition']}</label>
				<select id="select_condition" name="condition" class="span2" >
					<c:forEach var="condition" items="${conditionMap}">
						<option value="${condition.key}">
							${condition.value}
						</option>
					</c:forEach>
				</select>
				<br/>
				<br/>
				<!-- 查询条件内容 -->
				<label class="label">${msg['statistic.dailyLogin.conditionContent']}</label>
				<textarea style="width:650px ; height:150px;" id="conditionContent" name="conditionContent" placeholder="${msg['statistic.dailyLogin.attention']}"></textarea>
				<br/>
				<br/>
				<!-- 查询内容-->
				<label class="label">${msg['statistic.dailyLogin.queryContent']}</label>
				<select id="select_queryContent" name="queryContent" onchange="displayDivByCondition(this.value)" class="span2" >
					<option value="roleLogin">${msg['statistic.dailyLogin.roleLogin']}</option>
					<option value="userLogin">${msg['statistic.dailyLogin.userLogin']}</option>
				</select>
				<br/>
				<br/>
				<!-- 角色注册时间 -->
				<div id="createDateDiv">
					<label class="label">${msg['statistic.dailyLogin.createTime']}</label>
					<div class="input-append date" id="createStartDate">
						<input style="width: 120px;" name="createStartDate" size="20" type="text" value="${nowDate}" 
							readonly> <span class="add-on"><i class="icon-th"></i></span>
					</div>
					-
					<div class="input-append date" id="createEndDate">
						<input style="width: 120px;" name="createEndDate" size="20" type="text" value="${nowDate}" 
							readonly> <span class="add-on"><i class="icon-th"></i></span>
					</div>
				</div>
				<br/>
				<br/>
				<!-- 查询按钮 -->
				<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
				
				<!-- 显示条数 -->
				<label class="label">${msg['jsp.log.pageSize']}</label>
				<input id="pageSize" name="pageSize" value="1000" class="span1 validate[required,custom[integer],min[1]]">
				${msg['jsp.log.all']}：<label id="recordCount">0</label>${msg['jsp.log.record']}&nbsp;&nbsp;&nbsp;&nbsp;
				<br/>
				<br/>
				<!-- 导出Excel按钮 -->
				<input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" style="display: block;" class="btn btn-primary" onclick="exportExcel();" >		
				<br/>
				<!-- 分页页面部分 -->
				<div id="pagerDiv" class="pagerDiv">
			        <span name="first" class="disabled" style="cursor: pointer;">首页</span>
			        <span name="prev" class="disabled" style="cursor: pointer;">上一页</span>
			        <span name="nav" style="cursor: pointer;"><a class="navi"><span style="color: #999">1</span></a></span>
			        <span name="next" class="disabled" style="cursor: pointer;">下一页</span>
			        <span name="last" class="disabled" style="cursor: pointer;">末页</span>
			    </div>
			</form>
		</div>
		<p id="msg"></p>
	</div>
	<div class="tab-content">
		<div class="tab-pane active" id="dailyLoginData">
		${msg['statistic.public.content']}:<span style="color: red;">${msg['statistic.dailyLogin.data']}</span>
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