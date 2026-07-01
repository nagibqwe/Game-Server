<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.server.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<body>
<script type="text/javascript">
var initPageNumber = 1;
var initPageSize = 20;
var base = '${base}';
$(function() {
	$("#combine").hide();
	$("#pageSize").val(initPageSize);
	$("#loading").shCircleLoader();
	paging(initPageNumber);
	
	$(".datetimepicker").datetimepicker({
   	    language:  'zh-CN',
		format : 'yyyy-mm-dd hh:ii:00',
		weekStart: 1,
	    todayBtn:  1,
		autoclose: 1,
		startView : 2,
		minView : 0
	});
	
	$("#addMod").click(function() {
		$.post(base + "/serverIsShow/add",$("#serverMod").serialize(), function(data) {
				if (data.ok) {
					paging(initPageNumber);
					$("#serverMod")[0].reset();
					//alert("添加成功");
					$("#addMod").val("${msg['jsp.server.sumbit']}");
				} else {
					alert(data.msg);
				}
			
		});
	});
	
	$("#replay").click(function(){
		var isHidden = $("#combine").is(":hidden");
		if( isHidden)
		{
			$("#combine").show('slow');
		}
		else
		{
			$("#combine").hide('slow');
		}
	});
	
	$("#combinebutton").click(function(){
		var serverid = $("form:eq(2) input:eq(0)").val();
		var sid = $("form:eq(2) input:eq(1)").val();

		var s = prompt("${msg['jsp.server.combine1']}"+serverid + "${msg['jsp.server.combine2']}" + sid +" ${msg['jsp.server.combine3']}");
		if (s == "y") {
			$.post(base + "/serverIsShow/combine",$("#combineform").serialize(), function(data) {
				if (data.ok) {
					paging(initPageNumber);
					$("#combineform")[0].reset();
					//alert("添加成功");
				} else {
					alert(data.msg);
				}
			});
		}
	});
});
function search(){
	if($("#server_query_form").validationEngine('validate')){
		paging(initPageNumber);
	}
};
function paging(page){
	var playerId=$("#playerId").val()
	var state=$("#state").val()
	var pageSize=$("#pageSize").val().trim();
	
	
	$("#loadingmodal").modal({backdrop:'static',keyboard:false});
	list_html="";
	$.ajax({
		type : "POST",
		url : base + "/headIconCheck/query",
		data : {
			"playerId":playerId,
			"state":state,
			"pageNumber":page,
			"pageSize":pageSize
		},
		dataType : "json",
	   	success: function(data){
	   		$("#loadingmodal").modal('hide');
			$("#user_count").html("${msg['jsp.server.gong']}" + 
					data.pager.recordCount + "${msg['jsp.headIcon.headNum']}" + 
					data.pager.pageCount + "${msg['jsp.server.page']}");
			var list_html ="<table class=\"table table-bordered table-striped\" border='0' cellspacing='0' cellpadding='0'>\
				<tr>\
					<td>${msg['jsp.headIcon.playerId']}</td>\
					<td>${msg['jsp.headIcon.image']}</td>\
					<td>${msg['jsp.headIcon.date']}</td>\
					<td>${msg['jsp.headIcon.state']}</td>\
					<td>${msg['server.all.choice']}&nbsp;<input type='checkbox' id='checkAll' onclick='checkAll()'>&nbsp;&nbsp;&nbsp;&nbsp;${msg['jsp.headIcon.operation']}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class='btn btn-warning' onclick='user_updateAll()'>${msg['jsp.headIcon.onePass']}</button></td>\
				</tr>";
			//console.log(data.list);
			//<td>&nbsp;"+(ser.serverType== 0 ? "${msg['jsp.server.testServer']}" : ( ser.serverType == 1 ? "${msg['jsp.server.normalServer']}" : "${msg['jsp.dblog.login']}"))+"</td>\
			for ( var i = 0; i < data.list.length; i++) {
				var ser = data.list[i];
				var tmp ="<tr>\
					<td>&nbsp;"+ser.playerId+"</td>\
					<td>&nbsp;<img src=\"../../headIcon/"+ser.playerId+"\/"+ser.playerId+".jpg\" style=\"height:20%; weight:20%;\"></td>\
					<td>&nbsp;"+ser.updateTime+"</td>\
					<td>&nbsp;"+isShow(ser.state)+"</td>"
				if(ser.state==0){
					tmp+="<td><input type='checkbox' name='message' value=\""+ser.playerId+"\">&nbsp;&nbsp;\
							<button class='btn btn-warning' onclick='user_update(\""+ser.playerId+"\");'>${msg['jsp.headIcon.pass']}</button>&nbsp;&nbsp;\
							<button class='btn btn-warning' onclick='user_delete(\""+ser.playerId+"\");'>${msg['jsp.server.del']}</button></td></tr>"
				}else{
					tmp+="<td><button class='btn btn-warning' onclick='user_delete(\""+ser.playerId+"\");'>${msg['jsp.server.del']}</button></td></tr>"
				}
				list_html += tmp;
			}
			list_html+="\n</table>";
			$("#user_list").html(list_html);
			
	   		var pages = data.pager.pageCount;//Math.ceil(data.pager.recordCount/5);//这里data里面有数据总量  
	        //var element = $("#pageUl");//对应下面ul的ID  
	        var options = {  
	            bootstrapMajorVersion:2,  
	            currentPage: page,//当前页面  
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
	        }
	        $("#pageUl").bootstrapPaginator(options);
	   	}
	});
};

function checkAll(){
	if($("#checkAll").prop("checked")){
        $("input[name='message']:checkbox").each(function(){ 
            $(this).prop("checked", true);  
      });
	}else{
        $("input[name='message']:checkbox").each(function(){ 
            $(this).prop("checked", false);  
      });
	}
	
}

function user_updateAll() {
	 $("#user_list input:checkbox[name='message']:checked").each(function(){
		 arr = $(this).val();
		 user_update(arr);
     })

}

function user_update(playerid) {
		$.ajax({
			url : base + "/headIconCheck/change",
			data : {
				"playerId" : playerid
			},
			dataType : "json",
			success : function(data) {
				if (data.id) {	
					$("#serverId").val(data.serverId);
					$("#serverName").val(data.serverName);
					$("#serverType").val(data.serverType);
					$("#isShow").val(data.isShow);
				} else {
					paging(initPageNumber);
				}
			}
		});
	
};
	
function user_delete(playerid) {
	var s = prompt("${msg['jsp.server.delete']}");
	if (s == "y") {
		$.post(base + "/headIconCheck/delete", {
				"playerId" : playerid
			},function(data) {
				if (data.ok) {
					paging(initPageNumber);
					alert("${msg['jsp.server.deleteOk']}");
				} else {
					alert(data.msg);
				}
		});
	}
};
	
function combineclear(userid)
{
	var s = prompt("${msg['jsp.server.isClearCombine']}");
	if (s == "y") {
		$.post(base + "/serverIsShow/cleancombine", {"id" : userid},function(data) {
				if (data.ok) {
					paging(initPageNumber);
				} else {
					alert(data.msg);
				}
		});
	}
};
	
function test(id)
{
	$.post(base + "/serverIsShow/test", {"id" : id},function(data) {

		if (data.ok) {
			alert("${msg['jsp.server.testsuccess']}");
		} else {
			alert("false" + data.msg);
		}
	});
};
function getServerType(type) {
	switch (type) {
	case 1:
		return type+"[正式服]";
	case 2:
		return type+"[登录服]";
	case 3:
		return type+"[跨服]";
	case 4:
		return type+"[战斗服]";
	default:
		return type+"[测试服]";
	}
};
function isDeleted(type) {
	if(type==1){
		return type+"[无效]";
	}
	return type+"[有效]";
};
function isShow(type) {
	if(type==0){
		return type+"[待审核]";
	}
	return type+"[已审核]";
};
</script>
</body>
<body>
	<div class="container-fluid">
		<form action="#" id="server_query_form" class="well form-inline"> 
			<span style="float:left;margin-right: 10px;">
				${msg['jsp.headIcon.playerId']}:<input type="text" id="playerId" name="playerId" placeholder="${msg['jsp.headIcon.playerId']}">
				${msg['jsp.headIcon.state']}:<select id="state" name="state"><option value="0" selected="selected">${msg['jsp.headIcon.wait']}</option><option value="1">已审核</option><option value="2">${msg['jsp.headIcon.all']}</option></select>
				<%-- ${msg['jsp.server.pageNum']}<input type="text" name="pageNumber" value="1"> --%>
			</span>
			${msg['jsp.server.perPage']}:<input type="text" id="pageSize" name="pageSize" class="span1 validate[required,custom[integer],min[1]]">
			<span style="float:left;margin-right: 10px;">
				<button id="server_query_btn" type="button" class="btn btn-primary" onclick="search();"><i class="icon-search icon-white"></i></button>
			</span>
		</form>
		
		<p id="user_count"></p>
		<div id="user_list"></div>
		<span style="font-size:14px;">
			<!-- <ul class="pagination" id="pageUl"></ul> -->
			<div class="pagination" id="pageUl"></div>
		</span>
	</div>
	
	<div id="user_add" class="container-fluid">

	</div>
	<div id="combine" class="container-fluid">
	    <form id="combineform" name="combineform" method="post" class="well" action="">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
		        <tr>
		          <td>${msg['jsp.combine.currentId']}:</td>
		          <td><input name="serverId" type="text" id="serverId" placeholder="${msg['jsp.combine.currentIdMore']}"/></td>
		          <td>${msg['jsp.combine.destId']}:</td>
		          <td><input name="hefuServerID" type="text" id="hefuServerID" placeholder="${msg['jsp.combine.destIdMore']}" /></td>
		        </tr>
		        <tr>
		          <td>${msg['jsp.server.hefutime']}:</td>
		          <td><input name="hefuTime" type="text" id="hefuTime" class="datetimepicker" readonly/></td>
		        </tr>
			</table>
			<input type="button" id="combinebutton" class="btn btn-primary" value="${msg['jsp.combine.add']}">
	    </form>
    </div>
  
	<div class="modal hide fade in" id="loadingmodal">
		<div class="modal-body">
			<div style="padding-left: 30%;">
			<p style="font-size: 14px;width:120px;height:30px;line-height: 30px;display:inline;float: left;">${msg['jsp.log.loading']}</p>
			<div id="loading" style="width:30px; height:30px;display:inline;float: left;"></div>
			</div>
		</div>
	</div>
</body>
</html>