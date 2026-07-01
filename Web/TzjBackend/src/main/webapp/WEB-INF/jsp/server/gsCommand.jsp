<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>服务器参数设置</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
var base = '${base}';
var pageNumber = 1;
$(function() {
	reloadNoHefuServerGroupBox(true);
	paging(1);
	changeCMDType($("#cmdType").val());
});

function search(){
	if($("#logForm").validationEngine('validate')){
		paging(1);
	}
}

function sendCommand(){
	if(!$("#checkbox_server input[type='checkbox']").is(':checked')){
		return alert("请选择服务器!");
	}
	if(!$("#comman_submit").validationEngine('validate')){
		return;
	}
	
	$("#progressmodal").modal({backdrop:'static',keyboard:false});
	$("#proDiv").attr("class","progress progress-striped active");
	$("#bar").attr("style","width: 5%;");
	$("#progressdetail").empty();
	$("#closemodal").attr("disabled","disabled");
	$.post(base + "/gm/sendCommand",$("#comman_submit").serialize(), function(data) {
		if (data.ok) {
			$("#proDiv").attr("class","progress progress-success progress-striped active");
		} else {
			$("#proDiv").attr("class","progress progress-danger progress-striped active");
		}
		var msg=data.msg;
		var reg=new RegExp("\n","g"); 
		msg = msg.replace(reg,"<br/>");
		
		$("#command").empty();
		$("#msg").empty();
		
		$("#bar").attr("style","width: 100%;");
		$("#progressdetail").html(msg);
		$("#closemodal").removeAttr("disabled");
		setTimeout(function () {
			$("#progressmodal").modal('hide');
			paging(pageNumber);
		}, 3000)
	});
}

function paging(pageNumber){
	$("#progressdetail").modal('hide');
	var action=$("#action").val().trim();
	var params=$("#params").val().trim();
	var pSize=$("#pageSize").val().trim();
	$("#loadingmodal").modal({backdrop:'static',keyboard:false});
	$.ajax({
		type : "POST",
		url : base + "/gm/query",
		data : {
			"action": action,
			"params": params,
			"pageNumber": pageNumber,
			"pageSize": pSize,
			"gmType": 0
		},
		dataType : "json",
	   	success: function(data){
	   		$("#loadingmodal").modal('hide');
			var list_html = "<table class=\"table table-bordered table-striped\">\
				<tr><td>服务器</td><td>执行语句</td><td>用户</td><td>操作结果</td><td>结果描述</td><td>操作者IP</td><td>操作时间</td></tr>";
			for ( var i = 0; i < data.list.length; i++) {
				var ser = data.list[i];
				var tmp ="<tr align='center'>\
					<td>"+ser.serverName+"("+ser.serverId+")</td>\
					<td>"+ser.action+"("+ser.params+")</td>\
					<td>"+ser.user+"</td>\
					<td>"+ser.isOk+"</td>\
					<td>"+ser.result+"</td>\
					<td>"+ser.ip+"</td>\
					<td>"+ser.operDate+"</td>\
					</tr>";
					list_html += tmp;
			}
			list_html+="\n</table>";
			$("#user_log").html(list_html);
			
	   		var pages = data.pager.pageCount;//这里data里面有数据总量
	        var options = {
	            bootstrapMajorVersion:2,  
	            currentPage: data.pager.pageNumber,//当前页面
	            numberOfPages: 5,//一页显示几个按钮（在ul里面生成5个li）  
	            totalPages:pages, //总页数 
	            itemTexts: function (type, page, current) {
                    switch (type) {
                        case "first":
                            return "首页";
                        case "prev":
                            return "上一页";
                        case "next":
                            return "下一页";
                        case "last":
                            return "末页";
                        case "page":
                            return page;
                    }
                }
	        };
	        $("#pageUl").bootstrapPaginator(options);
	   	}
	});
}

function changeCMDType(type){
	$("#cmdAction").val(type);
	$("#cmdParam").val("");
}
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="comman_submit" class="well form-inline">
			<select id="select_group" onchange="reloadNoHefuServerGroupBox(this.value, true)"></select>
			<label for="selectAll">全选</label>
			<input id="selectAll" type="checkbox" onclick="selectAllSid()"/>
			<br/><br/>
			<div id="checkbox_server" class="well"></div>

			<label for="cmdType" class="label">命令类型</label>
			<select id="cmdType" onchange="changeCMDType(this.value)" class="span2">
				<option value="gmReloadScript" selected="selected">热更新脚本</option>
				<option value="gmReloadConfig">热更新配置表</option>
				<option value="gmRefreshShop">刷新商城</option>
                <option value="gmSetWorldLevel">设置世界等级</option>
				<option value="">自定义命令</option>
			</select>
			<label for="cmdAction" class="label">命令</label>
			<input type="text" id="cmdAction" name="action" placeholder="命令" class="validate[required] span2">
			<label for="cmdParam" class="label">参数</label>
			<input type="text" id="cmdParam" name="params" placeholder="参数" class="span2"/>
			<input type="button" id="submit_command" class="btn btn-primary" onclick="sendCommand();" value="执行命令">
		</form>
	</div>
	
	<div class="container-fluid form-inline">
		<div id="logForm" class="validationEngineContainer well">
			<label class="label">查询条件</label>
			<input type="text" id="action" placeholder="命令" class="span2"/>
			<input type="text" id="params" placeholder="参数值" class="span2"/>
			<label class="label">每页显示条数</label>
			<input type="text" id="pageSize" value="20" class="span1 validate[required,custom[integer],min[1]]" />
			<button type="button" class="btn btn-primary" onclick="search();"><i class="icon-search icon-white"></i></button>
		</div>
		<div id="user_log"></div>
		<div class="pagination" id="pageUl"></div>
	</div>

 	<jsp:include page="../commonmodal.jsp"/>
</body>
</html>