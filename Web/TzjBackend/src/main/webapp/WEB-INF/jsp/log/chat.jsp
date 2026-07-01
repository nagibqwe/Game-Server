<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.chat.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/log.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript">
	var base = '${base}';
	var logType = ${logType};
	var conditions=${conditions};
	var fields=${fields};
	var crossLogType = ${crossLogType};
	var hasPlatform = false;
	$(function() {
		//1跨服 2公共服
		if (crossLogType == 0) {
			group_reload();
		} else if (crossLogType == 1) {
			reloadCrossGroup(4);
		} else if (crossLogType == 2){
			reloadCrossGroup(3)
		}

		hasPlatform = fields['platformName'] !== undefined;
		if (!hasPlatform) {
			$("#platformDiv").hide();
		}
		if (conditions.length <= 0) {
			$("#conditionDiv").hide();
		} else {
			createCondition(logType, conditions, fields);
		}

		$("#logType").val(logType);
		$("#excel_btn").hide();
		// $("#condition").hide();

		setTimeout(function () {
			getChannelName($("#select_group").val());
		}, 50);

		$(".date").datetimepicker({
			language: 'zh-CN',
			format: 'yyyy-mm-dd hh:ii:00',
			weekStart: 1,
			todayBtn: 1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 0,
			showMeridian: 1
		});

		$("#times").datetimepicker({//日期时间选择器配置
			language: 'zh-CN',
			format: 'yyyy-mm-dd hh:ii:ss',
			todayBtn: 1,
			startDate:new Date(),
			autoclose:true
		});

		$("#batchTimes").datetimepicker({//日期时间选择器配置
			language: 'zh-CN',
			format: 'yyyy-mm-dd hh:ii:ss',
			todayBtn: 1,
			startDate:new Date(),
			autoclose:true
		});
	});
	function search(){
		var startDate = $("#start input").val();
		var endDate = $("#end input").val();
		if (!checkDate(startDate, endDate)) {
			return;
		}
		$("#condition div input[type='checkbox']").not("input:checked").each(function(){
			$(this).parent().find("input[type='text']").removeAttr("name");
		});
		$("#progressmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
			type: "POST",
			url: base + "/log/getLog",
			data: $("#query_form").serialize(),
			dataType: "json",
			success: function (data) {
				// console.log(data);
				$("#progressmodal").modal('hide');
				if (!data.ok) {
					$("#data").html("");
					$("#msg").text(data.msg);
					return;
				}

				var tableStr = createChatTable(data.data.fields, data.data.datas);

				$("#condition div input[type='checkbox']").not("input:checked").each(function(){
					$(this).parent().find("input[type='text']").attr("name",$(this).val());
				});
				$("#msg").html("<input type='button' value='导出' class='btn btn-primary' onclick='exportExcel();'>" +
						"<input type='button' value='批量封号' class='btn btn-primary' onclick='batchForbid();'>" +
						"<input type='button' value='批量禁言' class='btn btn-primary' onclick='batchForbidChat();'>");
				$("#msg").append("共" + data.data.pager.recordCount + "条记录，当前" + data.data.datas.length + "条记录");
				$("#data").html(tableStr);

				initTableCheckbox();
			}
		});
	}

	function createChatTable(fields, datas) {
		var list_html = "<table class='table table-bordered table-striped'>";
		list_html += "<thead><tr>";
		// list_html += "<th><input type='checkbox' id='checkAll' name='checkAll' /></th>"

		for (var field in fields) {
			list_html += "<th>" + fields[field] + "</th>";
		}
		list_html += "<th>${msg['jsp.th.forbid.option']}</th>"
		list_html += "</tr></thead>";

		list_html += "<tbody>";
		for (var i = 0; i < datas.length; i++) {
			var record = datas[i];
			list_html += "<tr>";
			// list_html += "<td><input type='checkbox' name='checkItem' /></td>";
			var roleId = "";
			for (var field in fields) {
                list_html += "<td>";
				if(field == "roleId"){
					roleId = record[field];
                    list_html += "<input type='hidden' name='chooseId' value='"+roleId+"' />";
				}
				list_html += record[field];
				if(field == "channel"){
					list_html += "["+getChannel(record[field])+"]";
				}
				list_html += "</td>";
			}
			list_html += "<td><input type='button' class='btn btn-primary' onmousedown=forbidOption('"+roleId.trim()+"') value='${msg['jsp.th.forbid.fastbutton']}' />"
			list_html += "<input type='button' class='btn btn-primary' onmousedown=forbidChatOption('"+roleId.trim()+"') value='${msg['forbid.chat.deal']}' />"
			list_html += "<input type='button' class='btn btn-primary' onmousedown=chatOption('"+roleId.trim()+"') value='${msg['forbid.chat.showrecent']}' /></td>"
			list_html += "</tr>";
		}
		list_html += "</tbody></table>";
		return list_html;
	};

	function initTableCheckbox() {
		var $thr = $("#data table thead tr");
		var $checkAllTh = $('<th><input type="checkbox" id="checkAll" name="checkAll" /></th>');
		/*将全选/反选复选框添加到表头最前，即增加一列*/
		$thr.prepend($checkAllTh);
		// $("[name='serverId']:checked")
		// var $checkAllTh = $("#data table thead [name='checkAll']");
		/*“全选/反选”复选框*/
		var $checkAll = $thr.find('input');
		$checkAll.click(function(event){
			/*将所有行的选中状态设成全选框的选中状态*/
			$tbr.find('input').prop('checked',$(this).prop('checked'));
			/*并调整所有选中行的CSS样式*/
			if ($(this).prop('checked')) {
				$tbr.find('input').parent().parent().addClass('warning');
			} else{
				$tbr.find('input').parent().parent().removeClass('warning');
			}
			/*阻止向上冒泡，以防再次触发点击操作*/
			event.stopPropagation();
		});
		/*点击全选框所在单元格时也触发全选框的点击操作*/
		$checkAllTh.click(function(){
			$(this).find('input').click();
		});
		var $tbr = $("#data table tbody tr");
		var $checkItemTd = $('<td><input type="checkbox" name="checkItem" /></td>');
		/*每一行都在最前面插入一个选中复选框的单元格*/
		$tbr.prepend($checkItemTd);
		/*点击每一行的选中复选框时*/
		$tbr.find('input').click(function(event){
			/*调整选中行的CSS样式*/
			$(this).parent().parent().toggleClass('warning');
			/*如果已经被选中行的行数等于表格的数据行数，将全选框设为选中状态，否则设为未选中状态*/
			$checkAll.prop('checked',$tbr.find('input:checked').length == $tbr.length ? true : false);
			/*阻止向上冒泡，以防再次触发点击操作*/
			event.stopPropagation();
		});
		/*点击每一行时也触发该行的选中操作*/
		$tbr.click(function(){
			$(this).find('input').click();
		});
	}

	function chatOption(roleId){
		$("#roleId").val(roleId);
		$("#chatOptionModal").modal('toggle');
		var serverId = $("#select_server").val();
		var platform = $("#select_group").val();
		var startDate = $("#start input").val();
		// var start = startDate.trim().split(" "); //字符分割
		var endDate = $("#end input").val();
		// var end = endDate.trim().split(" "); //字符分割

        var dateStr = endDate;
        dateStr = dateStr.substring(0,19);
        dateStr = dateStr.replace(/-/g,'/'); //必须把日期'-'转为'/'
		console.log(dateStr);
        var timestamp = new Date(dateStr).getTime();
        timestamp = timestamp - 2*24*60*60*1000;
        console.log(timestamp);
        var d = new Date(timestamp);    //根据时间戳生成的时间对象
        var date = (d.getFullYear()) + "-" +
            ((d.getMonth() + 1)>=10?(d.getMonth() + 1):("0"+(d.getMonth() + 1))) + "-" +
            ((d.getDate())>=10?(d.getDate()):("0"+(d.getDate()))) + " 00:00:00";

		// $("#chatStartTimes").val(startDate);
		// $("#chatEndTimes").val(endDate);
		$.ajax({
			type: "POST",
			url: base + "/log/chat",
			// data: $("#query_form").serialize(),
			data:{
				logType:21,
				// channel:"",
				serverId:serverId,
				startDate:date,
				endDate:endDate,
				roleId:roleId
				// level:level,
				// receRoleId:receRoleId
				// pageSize:1000
			},
			dataType: "json",
			success: function (data) {
				// console.log(data);
				$("#progressmodal").modal('hide');
				if (!data.ok) {
					$("#chatData").html("");
					$("#msg").text(data.msg);
					return;
				}

				var tableStr = createChatResult(data.data.fields, data.data.datas);


				$("#chatData").html(tableStr);

				// initTableCheckbox();
			}
		});
	}

    function searchChat(roleId){
        var startDate = $("#start input").val();
        var endDate = $("#end input").val();
        if (!checkDate(startDate, endDate)) {
            return;
        }
        $("#condition div input[type='checkbox']").not("input:checked").each(function(){
            $(this).parent().find("input[type='text']").removeAttr("name");
        });
        $("#progressmodal").modal({backdrop:'static',keyboard:false});
        $.ajax({
            type: "POST",
            url: base + "/log/chat",
            // data: $("#query_form").serialize(),
			data:{
				serverId: serverId,
			},
            dataType: "json",
            success: function (data) {
                // console.log(data);
                $("#progressmodal").modal('hide');
                if (!data.ok) {
                    $("#data").html("");
                    $("#msg").text(data.msg);
                    return;
                }

                var tableStr = createChatResult(data.data.fields, data.data.datas);

                $("#condition div input[type='checkbox']").not("input:checked").each(function(){
                    $(this).parent().find("input[type='text']").attr("name",$(this).val());
                });
                $("#msg").append("共" + data.data.pager.recordCount + "条记录，当前" + data.data.datas.length + "条记录");
                $("#chatData").html(tableStr);

                initTableCheckbox();
            }
        });
    }

	function createChatResult(fields, datas) {
        var list_html = "<div>";
        for (var i = 0; i < datas.length; i++) {
			var record = datas[i];
			list_html += "<label>";
			list_html += record["time"]+"["+getChannel(record["channel"])+"]";
			list_html += "<span style='color: #0e90d2'>"+record["roleName"]+"</span>";
			if(record["receRoleId"] != "0"){
				list_html += "${msg['forbid.chat.to']}"+record["receRoleId"];
			}
			list_html += "${msg['forbid.chat.say']}"+record["content"];
			list_html += "</label></br>";
        }
		list_html += "</div>";
		return list_html;
	};

	function exportAllData(){
		var startDate=$("#start input").val();
		var endDate=$("#end input").val();
		if(!checkDate(startDate,endDate)){
			return;
		}
		if(typeof($("#logName").attr("name"))=="undefined"){
			$("#logName").attr("name","logName");
		}
		$("#condition div input[type='checkbox']").not("input:checked").each(function(){
			$(this).parent().find("input[type='text']").removeAttr("name");
	    });
		$("#query_form").submit();
	};

	var forbidCn = [];
	function forbidOption(roleId){
		forbidCn = [];
		$("#roleId").val(roleId);
		$("#forbidOptionModal").modal('toggle');
		var serverId = $("#select_server").val();
		var platform = $("#select_group").val();
		$.ajax({
		url : base + "/forbidden/getRoleLoinInfo",
		data : {
			roleId : roleId,
			serverId : serverId,
			platform : platform
		},
		method : "post",
		dataType : "json",
		success : function(data) {
			if(data == null){
				alert("未查询到玩家相关信息，无法操作！");
				return;
			}
			$("#select_forbidId").empty();
			var imei = data.imei;
			var lastLoginIp = data.lastLoginIp;
			var mac = data.mac;
			var machineCode = data.machineCode;
			var userName = data.userName;
			if(imei != null && imei != "null" && imei != ""){
				forbidCn.push(imei);
				$("#select_forbidId").append("<option value='"+imei+"'>${msg['forbid.user.con4']}"+imei+"</option>");
			}
			if(lastLoginIp != null && lastLoginIp != "null" && lastLoginIp != ""){
				forbidCn.push(lastLoginIp);
				$("#select_forbidId").append("<option value='"+lastLoginIp+"'>${msg['forbid.user.con1']}"+lastLoginIp+"</option>");
			}
			if(mac != null && mac != "null" && mac != "" && mac != "02:00:00:00:00:00"){
				forbidCn.push(mac);
				$("#select_forbidId").append("<option value='"+mac+"'>${msg['forbid.user.con3']}"+mac+"</option>");
			}
			if(machineCode != null && machineCode != "null" && machineCode != ""){
				forbidCn.push(machineCode);
				$("#select_forbidId").append("<option value='"+machineCode+"'>${msg['forbid.user.con5']}"+machineCode+"</option>");
			}
			if(userName != null && userName != "null" && userName != ""){
				forbidCn.push(userName);
				$("#select_forbidId").append("<option value='"+userName+"'>${msg['forbid.user.con2']}"+userName+"</option>");
			}
		}
		});
	}
	
	function submitForbid(){
		if(forbidCn.length == 0){
			return;
		}
		var roleId = $("#roleId").val();
		var times = $("#times").val();

		if(times == ""){
			return;
		}
		var reason = $("#reason").val();
		var serverId = $("#select_server").val();
		var platform = $("#select_group").val();
		
		$.ajax({//先踢下线
			url : base + "/forbidden/kickPlayer",
			data : {
				roleId : roleId,
				reason : reason,
				serverId : serverId
			},
			method : "post",
			dataType : "json",
			success : function(data) {
				// for(var i=0; i<forbidCn.length; i++){
					$.ajax({//然后封号
					url : base + "/forbidden/forbidUser",
					data : {
						// condition : forbidCn[i],
						condition : $("#select_forbidId").val(),
						endTime : times,
						reason : reason
					},
					method : "post",
					dataType : "json",
					success : function(data) {
						// alert("OK!");
                        $("#forbidOptionModal").modal('hide');
					}
					});
				// }
			}
		});
	}

	function forbidChatOption(roleId){
		$("#roleId").val(roleId);
		$("#forbidChatOptionModal").modal('toggle');
		var serverId = $("#select_server").val();
		var platform = $("#select_group").val();
		$.ajax({
			url : base + "/forbidden/getRoleLoinInfo",
			data : {
				roleId : roleId,
				serverId : serverId,
				platform : platform
			},
			method : "post",
			dataType : "json",
			success : function(data) {
				if(data == null){
					alert("未查询到玩家相关信息，无法操作！");
					return;
				}
				$("#select_forbidChatId").empty();
				var userid = data.userid;
				if(userid != null && userid != "null" && userid != ""){
					$("#select_forbidChatId").append("<option value='"+userid+"'>"+userid+"</option>");
				}
			}
		});
	}

	function submitForbidChat(){
		var roleId = $("#roleId").val();
		var times = $("#chatTimes").val();
		// alert(times);
		if(times == ""){
			return;
		}
		var reason = $("#chatReason").val();
		var serverId = $("#select_server").val();
		var platform = $("#select_group").val();
		$.ajax({
			url : base + "/forbidden/playerCloseChat",
			data : {
				serverId : serverId,
				playerId : $("#select_forbidChatId").val(),
				times : times,
				crimeType : $("#select_chatCrimeType").val(),
				forbidType : $("#select_chatForbidType").val(),
				reason : reason
			},
			method : "post",
			dataType : "json",
			success : function(data) {
				// alert("OK!");
                $("#forbidChatOptionModal").modal('hide');
			}
		});
	}

	function batchForbid(){
		var str="";
		// console.log($("input[name='checkItem']:checked").parents("tr").find("input[name='chooseId']").val());
		var $checkArray = $("input[name='checkItem']:checked");
		if(!$checkArray)return;
		for(var i=0;i<$checkArray.length;i++){
			if(i>0){
				str+=",";
			}
			if($checkArray[i].checked==true){
				str+=$checkArray.eq(i).parents("tr").find("input[name='chooseId']").val();
			}
		}
		if(str!=""){
			$("#batchForbidOptionModal").modal('toggle');
			$("#batchForbidId").val(str);
		}
	}

	//批量封号
	function submitBatchForbid(){
		var serverId = $("#select_server").val();
		var times = $("#batchTimes").val();
		var reason = $("#batchReason").val();

		$.ajax({
			url : base + "/forbidden/forbidUsers",
			data : {
				serverId : serverId,
				roleIds : $("#batchForbidId").val(),
				endTime : times,
				reason : reason
			},
			method : "post",
			dataType : "json",
			success : function(data) {
				// alert("OK!");
                $("#batchForbidOptionModal").modal('hide');
			}
		});
	}

	//批量禁言
	function batchForbidChat(){
		var str="";
		// console.log($("input[name='checkItem']:checked").parents("tr").find("input[name='chooseId']").val());
		var $checkArray = $("input[name='checkItem']:checked");
		if(!$checkArray)return;
		for(var i=0;i<$checkArray.length;i++){
			if(i>0){
				str+=",";
			}
			if($checkArray[i].checked==true){
				str+=$checkArray.eq(i).parents("tr").find("input[name='chooseId']").val();
			}
		}
		if(str!=""){
			$("#batchForbidChatOptionModal").modal('toggle');
			$("#batchForbidChatId").val(str);
		}
	}

	//批量禁言
	function submitBatchForbidChat(){
		var serverId = $("#select_server").val();
		var times = $("#batchChatTimes").val();
		var reason = $("#batchChatReason").val();

		$.ajax({
			url : base + "/forbidden/batchForbidChat",
			data : {
				serverId : serverId,
				roleIds : $("#batchForbidChatId").val(),
				times : times,
				crimeType : $("#select_batchChatCrimeType").val(),
				forbidType : $("#select_batchChatForbidType").val(),
				reason : reason
			},
			method : "post",
			dataType : "json",
			success : function(data) {
				// alert("OK!");
                $("#batchForbidChatOptionModal").modal('hide');
			}
		});
	}
	
	function getChannel(channel){
		switch (channel) {
		case "0":
			return "${msg['jsp.chat.world']}";
		case "1":
			return "${msg['jsp.chat.guild']}";
		case "2":
			return "${msg['jsp.chat.team']}";
		case "3":
			return "${msg['jsp.chat.private']}";
		default:
			return "${msg['jsp.chat.unknown']}"+channel;
		}
	};
	function checkDate(startDate, endDate) {
		if (startDate == "" || startDate == null) {
			alert("${msg['jsp.log.sdateempty']}");
			return false;
		}
		if (endDate == "" || endDate == null) {
			alert("${msg['jsp.log.edateempty']}");
			return false;
		}
		var start = new Date(startDate.replace("-", "/").replace("-", "/"));
		var end = new Date(endDate.replace("-", "/").replace("-", "/"));
		if (end < start) {
			alert("${msg['jsp.log.sdatelessthanedate']}");
			return false;
		}
		return true;
	};
	function exportExcel(){
		$("#data table").eq(0).tableExport({type: 'excel', escape: 'false'});
	};
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="${base}/log/exportExcel" method="post" id="query_form" class="well form-inline">
			<div class="row-fluid">
				<select id="select_group" onchange="getChannelName();" class="span2"></select>
				<select id="select_server" name="serverId" class="span2"></select>
				<input type="hidden" id="logType" name="logType"/>

				<!-- 时间段 -->
				<label class="label">${msg['jsp.log.timechoose']}</label>
				<div class="input-append date" id="start">
					<input style="width: 120px;" name="startDate" size="20" type="text" value="${nowDate}" readonly>
					<span class="add-on"><i class="icon-th"></i></span>
				</div>
				-
				<div class="input-append date" id="end">
					<input style="width: 120px;" name="endDate" size="20" type="text" value="${nowDate}" readonly>
					<span class="add-on"><i class="icon-th"></i></span>
				</div>
				<label class="label">${msg['jsp.log.pageSize']}</label>
				<input type="text" name="pageSize" value="1000" class="span1 validate[required,custom[integer],min[1]]"/>

<%--				<select name="channel" class="span2">--%>
<%--					<option value="-1">All</option>--%>
<%--					<option value="0">${msg['jsp.chat.world']}</option>--%>
<%--					<option value="1">${msg['jsp.chat.guild']}</option>--%>
<%--					<option value="2">${msg['jsp.chat.team']}</option>--%>
<%--					<option value="3">${msg['jsp.chat.private']}</option>--%>
<%--				</select>--%>
			</div>
			<br/>

			<!-- 渠道列表 -->
			<div id="platformDiv" class="row-fluid">
				<label class="label">${msg['statistic.public.Channel']}</label>
				<div id="checkbox_channel"></div>
				<br/>
			</div>

			<!-- 查询条件 -->
			<div id="conditionDiv" class="row-fluid">
				<label class="label" id="add" onclick="showCondition()">${msg['jsp.log.searchcon']}</label>
				<div id="condition"></div>
				<br/>
			</div>

			<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
			<input type="button" id="exportAll" value="${msg['jsp.log.exportall']}" class="btn btn-primary" onclick="exportAllData();">
<%--			<input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel();">--%>
		</form>

		<div id="msg"></div>
		<div id="data" style="width: 100%"></div>
	</div>
	<div id="forbidOptionModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 id='update_title'>${msg['jsp.th.forbid.option']}</h3>
		</div>
		<div class="modal-body">
			<form class="form-horizontal" id="forbid_option_form">
				<fieldset>
					<label for="condition" class="span2" style="margin-left: 0">${msg['forbid.user.userid']}</label>
					<select id="select_forbidId" name="condition" class="span2"></select>
					<input id="forbidId" type="hidden" value="">
					<br/><br/>

					<label for="times" class="span2" style="margin-left: 0">${msg['forbid.user.endtime']}</label>
					<input type="text" name="times" id="times" class="input=medium datetimepicker validate[required]" readonly />
					${msg['forbid.needwrite']} ，${msg['forbid.user.endtimeInfo']}
					<br/><br/>

					<label for="reason" class="span2" style="margin-left: 0">${msg['forbid.chat.reason']}</label>
					<textarea  name="reason" id="reason" class="input=medium validate[required]" rows="5" cols="60" ></textarea>
					${msg['forbid.needwrite']}
					<br/><br/>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary" onclick="submitForbid();">${msg['jsp.giftbag.button.sure']}</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">${msg['jsp.giftbag.button.close']}</button>
		</div>
   	</div>

	<div id="batchForbidOptionModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 id='update_title_batch'>${msg['jsp.th.forbid.option']}</h3>
		</div>
		<div class="modal-body">
			<form class="form-horizontal" id="batch_forbid_option_form">
				<fieldset>
<%--					<label for="condition" class="span2" style="margin-left: 0">${msg['forbid.user.userid']}</label>--%>
					<input id="batchForbidId" type="hidden">
					<br/><br/>

					<label for="times" class="span2" style="margin-left: 0">${msg['forbid.user.endtime']}</label>
					<input type="text" name="times" id="batchTimes" class="input=medium datetimepicker validate[required]" readonly />
					${msg['forbid.needwrite']} ，${msg['forbid.user.endtimeInfo']}
					<br/><br/>

					<label for="reason" class="span2" style="margin-left: 0">${msg['forbid.chat.reason']}</label>
					<textarea  name="reason" id="batchReason" class="input=medium validate[required]" rows="5" cols="60" ></textarea>
					${msg['forbid.needwrite']}
					<br/><br/>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary" onclick="submitBatchForbid();">${msg['jsp.giftbag.button.sure']}</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">${msg['jsp.giftbag.button.close']}</button>
		</div>
	</div>

	<div id="forbidChatOptionModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 id='update_title_chat'>${msg['forbid.chat.deal']}</h3>
		</div>
		<div class="modal-body">
			<form class="form-horizontal" id="forbid_chat_option_form">
				<fieldset>
					<label for="select_chatCrimeType" class="span2" style="margin-left: 0">${msg['forbid.chat.crime']}</label>
					<select id="select_chatCrimeType" name="select_chatCrimeType" class="span2">
                        <option value="1">${msg['forbid.chat.crime1']}</option>
                        <option value="2">${msg['forbid.chat.crime2']}</option>
                    </select>
					<br/><br/>

					<label for="select_chatForbidType" class="span2" style="margin-left: 0">${msg['forbid.chat.type']}</label>
					<select id="select_chatForbidType" name="select_chatForbidType" class="span2">
                        <option value="1">${msg['forbid.chat.type1']}</option>
                        <option value="2">${msg['forbid.chat.type2']}</option>
                        <option value="3">${msg['forbid.chat.type3']}</option>
                        <option value="4">${msg['forbid.chat.type4']}</option>
                        <option value="5">${msg['forbid.chat.type5']}</option>
                        <option value="6">${msg['forbid.chat.type6']}</option>
                    </select>
					<br/><br/>

					<label for="select_forbidChatId" class="span2" style="margin-left: 0">${msg['forbid.chat.userId']}</label>
					<select id="select_forbidChatId" name="select_forbidChatId" class="span2"></select>
					<input id="forbidIdChatId" type="hidden" value="">
					<br/><br/>

					<label for="chatTimes" class="span2" style="margin-left: 0">${msg['forbid.chat.endtime']}</label>
<%--					<input type="text" name="times" id="chatTimes" class="input=medium datetimepicker validate[required]" readonly />--%>
					<input id="chatTimes" type="text" name="chatTimes" class="input=medium validate[required],custom[integer],min[-1] span2"/>
<%--					${msg['forbid.needwrite']} ，${msg['forbid.user.endtimeInfo']}--%>
					<br/><br/>

					<label for="chatReason" class="span2" style="margin-left: 0">${msg['forbid.chat.reason']}</label>
					<textarea name="chatReason" id="chatReason" class="input=medium validate[required]" rows="5" cols="60" ></textarea>
					${msg['forbid.needwrite']}
					<br/><br/>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary" onclick="submitForbidChat();">${msg['jsp.giftbag.button.sure']}</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">${msg['jsp.giftbag.button.close']}</button>
		</div>
	</div>

	<div id="batchForbidChatOptionModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 id='update_title_batch_chat'>${msg['forbid.chat.deal']}</h3>
		</div>
		<div class="modal-body">
			<form class="form-horizontal" id="batch_forbid_chat_option_form">
				<fieldset>
					<input id="batchForbidChatId" type="hidden">

					<label for="select_batchChatCrimeType" class="span2" style="margin-left: 0">${msg['forbid.chat.crime']}</label>
					<select id="select_batchChatCrimeType" name="select_batchChatCrimeType" class="span2">
                        <option value="1">${msg['forbid.chat.crime1']}</option>
                        <option value="2">${msg['forbid.chat.crime2']}</option>
                    </select>
					<br/><br/>

					<label for="select_batchChatForbidType" class="span2" style="margin-left: 0">${msg['forbid.chat.type']}</label>
					<select id="select_batchChatForbidType" name="select_batchChatForbidType" class="span2">
                        <option value="1">${msg['forbid.chat.type1']}</option>
                        <option value="2">${msg['forbid.chat.type2']}</option>
                        <option value="3">${msg['forbid.chat.type3']}</option>
                        <option value="4">${msg['forbid.chat.type4']}</option>
                        <option value="5">${msg['forbid.chat.type5']}</option>
                        <option value="6">${msg['forbid.chat.type6']}</option>
                    </select>
					<br/><br/>

					<label for="batchChatTimes" class="span2" style="margin-left: 0">${msg['forbid.chat.endtime']}</label>
					<input id="batchChatTimes" type="text" name="batchChatTimes" class="input=medium validate[required],custom[integer],min[-1] span2"/>
					<br/><br/>

					<label for="batchChatReason" class="span2" style="margin-left: 0">${msg['forbid.chat.reason']}</label>
					<textarea name="batchChatReason" id="batchChatReason" class="input=medium validate[required]" rows="5" cols="60" ></textarea>
					${msg['forbid.needwrite']}
					<br/><br/>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary" onclick="submitBatchForbidChat();">${msg['jsp.giftbag.button.sure']}</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">${msg['jsp.giftbag.button.close']}</button>
		</div>
	</div>

	<div id="chatOptionModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3>${msg['forbid.chat.showrecent']}</h3>
		</div>
		<div class="modal-body">
			<form class="form-horizontal" id="chat_option_form">
				<fieldset>
					<%--					<label for="condition" class="span2" style="margin-left: 0">${msg['forbid.user.userid']}</label>--%>
					<input id="chatId" type="hidden">
<%--					<br/><br/>--%>

<%--					<label for="times" class="span2" style="margin-left: 0">${msg['forbid.user.endtime']}</label>--%>
<%--					<input type="hidden" name="times" id="chatStartTimes" class="input=medium datetimepicker validate[required]" readonly />--%>
<%--					<label for="times" class="span2" style="margin-left: 0">${msg['forbid.user.endtime']}</label>--%>
<%--					<input type="hidden" name="times" id="chatEndTimes" class="input=medium datetimepicker validate[required]" readonly />--%>
<%--					<br/><br/>--%>
					<div id="chatData">

					</div>
					<br/><br/>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
<%--			<button type="button" class="btn btn-primary" onclick="submitBatchForbidChat();">${msg['jsp.giftbag.button.sure']}</button>--%>
			<button type="button" class="btn btn-default" data-dismiss="modal">${msg['jsp.giftbag.button.close']}</button>
		</div>
	</div>

	<div class="modal hide fade in" id="loadingmodal">
		<div class="modal-body">
			<div style="padding-left: 30%;">
				<p
					style="font-size: 14px; width: 120px; height: 30px; line-height: 30px; display: inline; float: left;">${msg['jsp.log.loading']}</p>
				<div id="loading"
					style="width: 30px; height: 30px; display: inline; float: left;"></div>
			</div>
		</div>
	</div>

	<div class="modal hide fade in" id="progressmodal">
		<div class="modal-header">
			<h3 id="progressdetailhead">${msg['jsp.dealing']}</h3>
		</div>
		<div class="modal-body">
			<div class="progress">
				<div class="bar" id="bar" style="width: 1%;"></div>
			</div>
			<div id="progressdetail"></div>
		</div>
	</div>
	
</body>
</html>