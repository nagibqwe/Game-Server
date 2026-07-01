<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>${msg['activity.verify']}</title>

<link rel="stylesheet" href="${base}/css/activity.css" type="text/css" />
<link rel="stylesheet" href="${base}/css/common.css" type="text/css" />
<link rel="stylesheet" href="${base}/css/boxy.css" type="text/css" />
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">

<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>

<script type="text/javascript">
var base = '${base}';

var pageSize = 15;
var actList;
var actNum = 0;
var pageNum = 0;

function get_verify_activity() {
	$.post(base + "/activity/getVerifyActivity",
			function(dataResult, status) {
		        actList = dataResult.list;
	            actNum = actList.length;
	            var remainder = actNum % pageSize;
	            if (remainder > 0) {
	    	        pageNum = parseInt(actNum/pageSize) + 1;
	            } else {
	            	pageNum = parseInt(actNum/pageSize);
	            }
				var activity_list_html = "<table id='actTable' frame='box' width='95%' cellspacing='10'>";
				var title_html = "";
				title_html += "<tr>";
				title_html += "<th>${msg['activity.num']}</th>";
				title_html += "<th>${msg['activity.name']}</th>";
				title_html += "<th>${msg['activity.time']}</th>";
				title_html += "<th>${msg['activity.condition']}</th>";
				title_html += "<th>${msg['activity.reward']}</th>";
				title_html += "<th>${msg['activity.numlimit']}</th>";
				title_html += "<th>${msg['activity.operator']}</th>";
				title_html += "<th>${msg['activity.verify']}<input type='checkbox' id='verify_100'/></th>";
				title_html += "</tr>";
				activity_list_html += title_html;
				activity_list_html += "</table>";
				$("#activity_list").html(activity_list_html);
				
				var page_index_html = "";
				for (var i = 1; i <= pageNum; i++) {
					var index = "<a id='index' href='####' onclick='showPage(" + i + "); return false;'>" + i + "</a>&nbsp;";
					page_index_html += index;
				}
				page_index_html += "${msg['activity.page.gong']}" + pageNum + "${msg['activity.page.ye']}<br/>";
				page_index_html += "<button id='batchVerify' >${msg['activity.batch.verify']}</button>&nbsp;&nbsp;";
				$("#pageIndex").html(page_index_html);
		});
	
	showPage(1);
}

function showPage(pageIndex) {
	if (pageIndex > pageNum) {
		return;
	}
	
	var fromIndex = (pageIndex - 1) * pageSize;
	var toIndex = fromIndex + pageSize - 1;
	if (toIndex > (actNum - 1)) {
		toIndex = actNum - 1;
	}
	
	var act_list_html = "";
	for (var i = fromIndex; i <= toIndex; i++) {
		var activity = actList[i];
		var content_html = "<tr id='activity'>\
		    <td>"+activity.id+"</td>\
		    <td>"+activity.name+"</td>\
		    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
		    <td width='20%'>"+activity.conditionList+"</td>\
		    <td width='20%'>"+activity.rewardList+"</td>\
		    <td align='center'>"+activity.numLimit+"</td>";	
		    
		if (activity.state == 0) {
			content_html += "<td><button id='verify' value='" + activity.id + "'>${msg['activity.verify']}</button></td>";
		} else {
			content_html += "<td><button disabled='disabled' id='verified' value='" + activity.id + "'>${msg['activity.verify.success']}</button></td>";
		}
		content_html += "<td align='center'><input type='checkbox' name='verifyAct' value='" + activity.id + "'/></td>";
		content_html += "</tr>";
		act_list_html += content_html;
	}
	
	$("tr").remove("#activity"); //清楚上一页活动，然后贴上请求页的活动
	$("#actTable").append(act_list_html);  
	
	$('body').on('click', '#index', function() {
    	$(this).css({"background-color":"red","font-size":"130%"});
    	$(this).siblings("a").css({"background-color":"white","font-size":"100%"});
    });
}

$(function() {
	$.ajaxSetup({ //全局设置ajax为同步方式
	    async : false 
	});
	get_verify_activity();
	$('body').on('click', '#verify', function() {
		var actId = $(this).val();		
		var verifyResult = false;
		$.post(base + "/activity/verifyActivity", {actId:actId},
		    function(data, status) {
		        alert(data.msg);
		        if (data.ok) {
		        	verifyResult = true; //请求为同步方式这里才有效
		        }
		});
		if (verifyResult == true) {
        	$(this).text("${msg['activity.verify.success']}");
        	$(this).attr("disabled", true);
        	$.each(actList, function(i) {
				var act= actList[i];
				if (act.id == actId) {
					act.state = 1;
				}    					
			});
		}
	});
	/**
	*批量操作
	**/
	$('body').on('click', '#verify_100', function() {
		var che = $(this).is(":checked");
		$("[name='verifyAct']").attr("checked", che);
	});
	$('body').on('click', '#batchVerify', function() {
		var actId =[]; 
		$("input[name='verifyAct']:checked").each(function(){ 
			actId.push($(this).val()); 
		});
		if(actId.length == 0){
			alert("please check one!");
			return;
		}
		if(!confirm("Are you sure ?")){
			return;
		}
		$.post(base + "/activity/verifyBatch", {actIds:actId+","},
			function(data, status) {
				alert(data.msg);
				location.reload();
		});
	});
});

</script>

</head>

<body>

	<table width="95%" height="95%" align="center">
		<tr><td valign="top"><br/><font size='5'><strong>${msg['activity.verify.please']}</strong></font></td></tr>
		
		<tr><td><br/><div id="activity_list"></div></td></tr>
		
		<tr><td><div id="pageIndex" style="float:left"></div></td></tr>		
		
	</table>

</body>

</html>