<%@ page import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
  <head>
    
    <title>${msg['jsp.giftbag.pagetitle']}</title>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
	<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-confirmation.js"></script>
	<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
    <script type="text/javascript" src="${base}/js/common/page.js"></script>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript">
		var initPageNumber = 1;
		var initPageSize = 20;
		var base = '${base}';
		var type = <%=request.getAttribute("type")%>;
		$(function() {
			$('.datetimepicker').datetimepicker({  //日期时间选择器配置
	    		language:  'zh-CN',
	    	    format: 'yyyy-mm-dd hh:ii',    	    
	    	    todayBtn: 1,          
	    	    autoclose:true
    		});
    		paging(initPageNumber);
		});
		
    	function search(){
    		var condition = $("#condition").val();
    		paging(1,condition);
    	}
    	
    	function batchOpt(){
    		var actTemps = [];
    		$("input[name='actTemp']:checked").each(function(){ 
				actTemps.push($(this).val());
	        });
	        if(actTemps.length == 0){
	        	alert("系统提示：请先选择需要修改的模板！");
	        	return;
	        }
	        $("#optmodal").modal('toggle');
	        $("#tmpIds").val(actTemps+"");
    	}
    	
    	function checkAll(){
    		var che = $("#checkAll").is(":checked");
			$("[name='actTemp']").prop("checked", che);
    	}
    	
    	function Submit(){
	    	var curTime = new Date();
    		var actTemps = $("#tmpIds").val();
    		var beginTime = $("#beginTime").val();
    		var endTime = $("#endTime").val();
    		if(beginTime == "" || endTime == ""){
	    		alert("系统提示：活动起始时间不能为空！");
	    		return;
    		}
    		var beginDate = new Date(beginTime.replace(/\-/g, "\/"));
    		var endDate = new Date(endTime.replace(/\-/g, "\/"));
    		if(endDate < beginDate){
    			alert("系统提示：活动结束时间小于开始时间！");
    			return;
    		}
    		if(beginDate < curTime){
    			alert("系统提示：活动开始时间小于当前系统时间！");
    			return;
    		}
    		
    		
    		var rechargeStartTime = "";
    		var rechargeEndTime = "";
    		var reStartDate = "";
    		var reEndDate = "";
    		if(type == "0"){
	    		rechargeStartTime = $("#rechargeStartTime").val();
	    		rechargeEndTime = $("#rechargeEndTime").val();
	    		if(rechargeStartTime == "" || rechargeEndTime == ""){
	    			alert("系统提示：充值起始时间不能为空！");	
	    			return;
	    		}
	    		reStartDate = new Date(rechargeStartTime.replace(/\-/g, "\/"));
	    		reEndDate = new Date(rechargeEndTime.replace(/\-/g, "\/"));
	    		if(reEndDate < reStartDate){
	    			alert("系统提示：充值结束时间小于开始时间！");
	    			return;
	    		}
	    		if(reStartDate < curTime){
	    			alert("系统提示：充值开始时间小于当前系统时间！");
	    			return;
	    		}
	    		
	    		if(beginDate > reStartDate || beginDate > reEndDate){
	    			alert("系统提示：充值时间设置小于了会议起始时间！");
	  	 	 		return;
	    		}
	    		
	    		if(endDate < reStartDate || endDate < reEndDate){
	  	 	 		alert("系统提示：充值时间设置超过了会议起始时间！");
	  	 	 		return;
	    		}
    		}
    		$.ajax({
				url : base + "/activity/updateActTmp",
				data : {
					actTemps : actTemps,
					type : type,
					beginTime : beginTime,
					endTime : endTime,
					rechargeStartTime : rechargeStartTime,
					rechargeEndTime : rechargeEndTime
				},
				method : "post",
				dataType : "json",
				success : function(data) {
					if(!data.ok){
						alert("操作失败！");
					}else{
						alert("操作成功");
					}
					location.reload();
				}
			});
    		
    	}
    	function updateTemName(tempId){
    		$("#tmpId").val(tempId);
    		$("#updTempNameModal").modal('toggle');
    	}
    	
    	function SubmitTemName(){
    		var tmpName = $("#temName").val();
    		var tmpId = $("#tmpId").val();
    		if(tmpName == ""){
    			return;
    		}
    		$.ajax({
				url : base + "/activity/updateActTmpName",
				data : {
					tmpName : tmpName,
					tmpId : tmpId
				},
				method : "post",
				dataType : "json",
				success : function(data) {
					if(!data.ok){
						alert("模板名称修改失败！");
					}else{
						alert("模板名称修改成功");
					}
					location.reload();
				}
			});
    	}
    	function paging(page,con){
	
	$("#loadingmodal").modal({backdrop:'static',keyboard:false});
	$.ajax({
		type : "POST",
		url : base + "/activity/templatelist",
		data : {
			"type":type,
		 	"pageNumber":page,
			"pageSize":100,
			"condition":con
		},
		dataType : "json",
	   	success: function(data){
	   		/* $("#loadingmodal").modal('hide'); */
	   		
	   		$("#count").html("${msg['jsp.server.gong']}" + 
					data.pager.recordCount + "条记录，总计" + 
					data.pager.pageCount + "${msg['jsp.server.page']}");
	   		
	   		console.log(data);
	   		
	   		var tempHtml = "";
	   		tempHtml += "\
	   		<tr id=\"contentTitle\" align=\"center\" style=\"font-weight: bolder;\">\
   			<td>活动模板ID</td>\
   			<td>活动名称</td>\
   			<td>活动类型</td>\
   			<td>模板名称</td>\
   			<td>活动开始时间</td>\
   			<td>活动结束时间</td>" 
   			if(type==0){
   				tempHtml += "<td>充值开始时间</td>\
   				<td>充值结束时间</td>";
   			}
   			tempHtml+="<td>修改模板名字</td>\
   			<td width=\"10%\"><input type=\"checkbox\" id=\"checkAll\" onclick=\"checkAll();\"/></td>\
   			</tr>\
	   		";
	   		
	   		var list = data.templateList;
	   		for(var i=0;i<list.length;i++){
	   			tempHtml += "<tr align=\"center\">";
	   			tempHtml += "<td>"+ list[i].id + "</td>";
	   			tempHtml += "<td>"+ list[i].name + "</td>";
	   			tempHtml += "<td>"+ list[i].typeName + "</td>";
	   			tempHtml += "<td>"+ list[i].templateName + "</td>";
	   			tempHtml += "<td>"+ list[i].beginTime + "</td>";
	   			tempHtml += "<td>"+ list[i].endTime + "</td>";
	   			if(type == 0){
	   				tempHtml += "<td>"+ list[i].rechargeStartTime + "</td>";
	   				tempHtml += "<td>"+ list[i].rechargeEndTime + "</td>";
	   			}
	   			tempHtml += "<td><button class=\"btn btn-primary\" onclick=\"updateTemName('" + list[i].id + "');\">修改</button></td>";
	   			tempHtml += "<td><input type=\"checkbox\" name=\"actTemp\" value=\"" + list[i].id + "\"/></td>";
	   			tempHtml += "</tr>";
	   		}
	   		$("#tempContent").html(tempHtml);
			
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
	</script>
  </head>
  
  <body>
  	
   <table class="table table-bordered table-striped">
   <tbody>
   <tr>
   <th>
   <span>${type}</span>
	<span style="float:right;margin-right: 10px;">
		<button type="button" class="btn btn-primary" onclick="search();"><i class="icon-search icon-white"></i></button>
	</span>
	<span style="float:right;margin-right: 5px;">
	<input type="text" style='height: 26px;' id="condition" placeholder="Search">
	</span>
   <span style="float:right;margin-right: 20px;">
   <button class="btn btn-danger" onclick="batchOpt();">批量操作</button>
   </span>
   </th>
   </tr>
   </tbody>
   </table>
   <div id="count"></div>
   <table class="table table-bordered table-striped" border="1" id="page_item_list" cellspacing="0">
   <tbody id="tempContent">
  
   </tbody>
   </table>
	<div class="pagination" id="pageUl" ></div>
   <div id="optmodal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 id='update_title'>批量修改时间</h3>
		</div>
		<div class="modal-body">
			<form class="form-horizontal" id="gift_update_add_form">
				<fieldset>
					<input id="tmpIds" type="hidden" value="">
					活动开始时间：<input id="beginTime" style="height: 26px;" type="text" class="datetimepicker validate[required]" readonly><br/>
					活动开始时间：<input id="endTime" style="height: 26px;" type="text" class="datetimepicker validate[required]" readonly><br/>
					<c:if test="${type == 0 }">
						充值开始时间：<input id="rechargeStartTime" style="height: 26px;" type="text" class="datetimepicker validate[required]" readonly><br/>
						充值结束时间：<input id="rechargeEndTime" style="height: 26px;" type="text" class="datetimepicker validate[required]" readonly><br/>
					</c:if>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary" onclick="Submit();">保存</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		</div>
   </div>
   <div id="updTempNameModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 id='update_title'>修改模板名称</h3>
		</div>
		<div class="modal-body">
			<form class="form-horizontal" id="gift_update_add_form">
				<fieldset>
					<input id="tmpId" type="hidden" value="">
					模板新名称：<input id="temName" style="height: 26px;" type="text"><br/>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary" onclick="SubmitTemName();">确定</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		</div>
   </div>
  </body>
</html>
