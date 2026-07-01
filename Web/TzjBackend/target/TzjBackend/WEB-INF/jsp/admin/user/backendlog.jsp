<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>后台操作日志</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${base}/easyui/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="${base}/easyui/themes/default/easyui.css" type="text/css">
<link rel="stylesheet" href="${base}/easyui/themes/icon.css" type="text/css">
<script type="text/javascript">
var base = '${base}';
$(function() {
	user_reload();

	$(".date").datetimepicker({
   	    language:  'zh-CN',
		format : 'yyyy-mm-dd hh:ii:00',
		weekStart: 1,
	    todayBtn:  1,
		autoclose: 1,
		startView : 2,
		minView : 0
	});
});

function user_reload() {
    paging(1);
}
    function paging(page) {
        var name = $("#name").val().trim();
        var pageSize = $("#pageSize1").val().trim();
		$.ajax({
			type : "POST",
			url : base + "/user/query",
			data : {"name":name,"pageNumber":page,"pageSize":pageSize},
			dataType : "json",
			success : function(data) {
                $("#pageUl").empty();
				$("#user_count").html("共" + data.qr.pager.recordCount + "个用户, 总计" + data.qr.pager.pageCount + "页");
				var list_html = "<table class='table table-bordered table-striped'><tr><th>用户ID</th><th>用户名</th><th>操作</th></tr>";
				for (var i = 0; i < data.qr.list.length; i++) {
					var user = data.qr.list[i];
					var tmp = "<tr><td>" + user.id + "</td>" +
							"<td>" + user.name + "</td>" +
							"<td><button class='btn btn-primary' onclick='chooseUser(" + user.id + ",\"" + user.name + "\")'>选择</button>" +
							"</td></tr>";
					list_html += tmp;
				}
				list_html += "</table>";
				$("#user_list").html(list_html);
				var data = data.qr;
                pageQuery(data,page);
			}
		});
    }

function queryLog() {
	if($("#logForm").validationEngine('validate')){
		paging2();
	}
}

function paging2(){
	var startDate = $("#startdate input").val();
	var endDate = $("#enddate input").val();
	if (checkDate(startDate, endDate)) {
		var userId = $("#chooseId").val();
		// var pageSize = $("#pageSize").val().trim();
		var content = $("#content").val().trim();
		$("#loadingmodal").modal({backdrop:'static',keyboard:false});
		// $("#backendLog").html("");
        $('#backendLog').datagrid("loadData", {total:0, rows:[]});

        $("#backendLog").datagrid("load", {
            "id":userId,
            "startDate":startDate,
            "endDate":endDate,
            "content" : content
        });
	}
}

function chooseUser(id, name) {
	$("#chooseId").val(id);
	$("#chooseName").text(name);
}
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="user_query_form" class="well form-inline">
			<label for="name" class="label">条件(用户名称)</label>
			<input id="name" type="text" name="name" class="span2"/>
			<%--<label for="pageNumber" class="label">页数</label>--%>
			<%--<input id="pageNumber" type="text" name="pageNumber" value="1" class="span1"/>--%>
			<label for="pageSize1" class="label">每页</label>
			<input type="text" name="pageSize" id="pageSize1" value="10" class="span1"/>
			<input type="button" id="user_query_btn" value="查询" class="btn btn-primary" onclick="user_reload()">
		</form>
		<p id="user_count"></p>
		<div id="user_list"></div>
		<div class="pagination" id="pageUl"></div>
	</div>
	<div class="container-fluid">
		<form class="well form-inline">
			<div>
				<p class="text-success">
					你选择了用户：<b id="chooseName"></b>
				</p>
				<input id="chooseId" value="0" type="hidden">
			</div>
			
			<label class="label">选择时间</label>
			<div class="input-append date" id="startdate">
				<input style="width: 120px;" size="20" type="text" value="${nowDate}"  readonly>
				<span class="add-on"><i class="icon-th"></i></span>
			</div>
			-
			<div class="input-append date" id="enddate">
				<input style="width: 120px;" size="20" type="text" value="${nowDate}"  readonly>
				<span class="add-on"><i class="icon-th"></i></span>
			</div>

			<div id="logForm" class="validationEngineContainer" style="display: inline;">
				<label for="content" class="label">查询条件</label>
				<input id="content" type="text" placeholder="操作内容" />
				<%--<label for="pageSize" class="label">每页显示条数</label>--%>
				<%--<input type="text" id="pageSize" value="50" class="span1 validate[required,custom[integer],min[1]]" />--%>
				<button type="button" class="btn btn-primary" onclick="queryLog();"><i class="icon-search icon-white"></i></button>
			</div>
		</form>
		<%--<div id="backendLog"></div>--%>
        <table id="backendLog" class="easyui-datagrid" toolbar="#tb1" style="width:1650px;height:auto" data-options="url:'${base}/admin/getBackendLog',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:10,pageList:[10,30,50,80]">
            <div id="tb1" style="padding:3px">

            </div>
            <thead>
            <tr>
                <th data-options="field:'userId',width:12,align:'left'">用户ID</th>
                <th data-options="field:'userName',width:13,align:'left'">用户名	</th>
                <th data-options="field:'content',width:450,align:'left'">操作内容</th>
                <th data-options="field:'time',width:25,align:'left'">操作时间</th>
                <th data-options="field:'ip',width:25,align:'left'">IP</th>
            </tr>
            </thead>
        </table>

	</div>
	<%--<jsp:include page="../../commonmodal.jsp"/>--%>
</body>
</html>