<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.roleStatistic.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<link rel="stylesheet" href="${base}/css/pagination.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.pagination.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
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
	});
	var isSearch = 0;
	var exId = "";
	function search() {
		isSearch = 1;
		filedsData(1,"roleStateCount");
		filedsData(1,"roleState");
	}
	function filedsData(currentPage , actionType){
		exId = "roleStatistic";
		var groupName = $("#select_group").val();
		var serverId = $("#select_server").val();
		var sortType = $("#sortType").val().trim();
		var pageSize = $("#pageSize").val().trim();
		var pageIndex = (currentPage-1)*parseInt(pageSize);;
		var channelNames = [];
		$("input[name='channelName']:checked").each(function(){ 
			channelNames.push($(this).val());
        });
		$("#loadingmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
			type : "POST",
			url : base + "/roleStatistic/" + actionType,
			data : {
				groupName : groupName,
				serverId : serverId,
				channelNames : channelNames+"",
				sortType : sortType,
				pageIndex : pageIndex,
				pageSize : pageSize,
			},
			dataType : "json",
			success : function(data) {
				$("#loadingmodal").modal('hide');
				if(data == null){
					return;
				}
				if(actionType == "roleState"){
					setData(data)
				}else if(actionType == "roleStateCount"){
					setDataCounts(pageSize , data , "roleState")
				}	
			}
		});
	}
	//显示一共有多少条
	function setDataCounts(pageSize , dataObj , actionType){
		var recordCounts = 0 ;
		var recordCount = 0;
		for(var i= 0 ; i < dataObj.length ; i++){
			recordCounts += parseInt(dataObj[i].counts);		
			if(parseInt(dataObj[i].counts) >= recordCount )
				recordCount = parseInt(dataObj[i].counts);
		}
		console.log(recordCounts);
		$("#recordCount").html(recordCounts);
		$("#pagerDiv").css("display","block");
		$("#pagerDiv").pagination({
	        recordCount: recordCount,
	        pageSize: pageSize,
	        onPageChange: function (pageIndex) {
	        	filedsData(pageIndex,actionType);
	        }
		});
	}
	//显示数据
	function setData(data){
		$("#roleStatistic").html("");
		var table = $("<table>").attr("class",
		"table table-bordered table-striped");
		var thead = $("<thead>");
		var htr = $("<tr>");
		
		var fields = [
		        "序号",
				"${msg['jsp.roleStatistic.platformName']}",
				"${msg['jsp.roleStatistic.createsid']}",
				"${msg['jsp.roleStatistic.funcellUUid']}",
				"${msg['jsp.roleStatistic.userId']}",
				"${msg['jsp.roleStatistic.roleId']}",
				"${msg['jsp.roleStatistic.roleName']}",
				"${msg['jsp.roleStatistic.level']}",
				"${msg['jsp.roleStatistic.onlineTime']}",
				"${msg['jsp.roleStatistic.rechargeGold']}",
				"${msg['jsp.roleStatistic.gold']}",
				"${msg['jsp.roleStatistic.createTime']}",
				//"${msg['jsp.roleStatistic.lastLoginTime']}",
				"${msg['jsp.roleStatistic.isDelete']}",
				"${msg['jsp.roleStatistic.ts']}"];
		for ( var field in fields) {
			var th = $("<th>").text(fields[field]);
			htr.append(th);
		}
		thead.append(htr);
		table.append(thead);
		
		var tbody = $("<tbody>");
		for ( var key in data) {
			var roleStatistic = data[key]
			var tr = $("<tr>");
			var datalist = [
	        parseInt(key)+1,
			roleStatistic.platformName,
			roleStatistic.createsid,
			roleStatistic.funcellUUid,
			roleStatistic.userId,
			roleStatistic.roleId,
			roleStatistic.roleName,
			roleStatistic.level,
			roleStatistic.onlineTime,
			roleStatistic.rechargeGold,
			roleStatistic.gold,
			roleStatistic.createTime,
			//roleStatistic.lastLoginTime!=""?TimeObjectUtil.UnixToDate(roleStatistic.lastLoginTime):"",
			roleStatistic.isDelete=="0"?"未注销":TimeObjectUtil.UnixToDate(roleStatistic.isDelete),
			TimeObjectUtil.UnixToDate(roleStatistic.ts)];
			for ( var i in datalist) {
				var td = $("<td>").text(datalist[i]);
				tr.append(td);
			}
			tbody.append(tr);
		}
		table.append(tbody);
		$("#roleStatistic").show();
		$("#roleStatistic").html(table);
		if(data.msg!=null){
			$("#msg").html(msg);
		}
	}
	function exportExcel(){
		$("#query_form").submit();
	}
</script>
</head>
<div class="container-fluid">
	<form action="${base}/roleStatistic/exportExcel" id="query_form" class="well form-inline">
		<select id="select_group" name="groupName" onchange="queryServerByGroup(this.value);getChannelName(this.value);" class="span2"></select>
		<select id="select_server" name="serverId" class="span2"></select>
		<br/><br/>

		<label class="label">${msg['statistic.public.Channel']}</label>
		<span id="checkbox_channel"></span>
		<br/><br/>

		<label class="label" for="sortType">类型</label>
		<select id="sortType" name="sortType">
			<option value="rechargeGold" selected="selected">充值元宝</option>
			<option value="gold">剩余元宝</option>
		</select>

		<!-- 显示条数 -->
		<label class="label">${msg['jsp.log.pageSize']}</label>
		<input id="pageSize" name="pageSize" type="text" value="1000" class="span1 validate[required,custom[integer],min[1]]">
		${msg['jsp.log.all']}：<label id="recordCount">0</label>${msg['jsp.log.record']}
		<br/><br/>

		<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
		<input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel();" >
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
	<div class="tab-content">
		<div class="tab-pane active" id="roleStatistic"></div>
	</div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>