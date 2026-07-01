<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['rank.pay.pagetitle']}</title>
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
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript">
	var base = '${base}';
	$(function() {
		$(".date").datetimepicker({
			language : 'zh-CN',
            format: 'yyyy-mm-dd hh:ii:00',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			showMeridian : 1
		});
		getGroup();
	});

	function getRechargeRank(){
        var startDate = $("#start input").val();
        var endDate = $("#end input").val();
        if (!checkDate(startDate, endDate)) {
            return;
        }
        $("#loadingmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
            url : base + "/rank/getRechargeRank",
            data : $("#query_form").serialize(),
            method : "post",
            dataType : "json",
            success : function(data) {
                $("#loadingmodal").modal('hide');
                $("#msg").empty();
                $("#data").empty();
                console.log(data);
                if (!data.ok) {
                    $("#msg").html(data.msg);
                    return;
                }
                var list_html = "<table class='table table-bordered table-striped'>";
                list_html += "<tr>";
                list_html += "<th>排名</th>";
                list_html += "<th>用户ID</th>";
                list_html += "<th>角色ID</th>";
                list_html += "<th>角色名</th>";
                list_html += "<th>等级</th>";
                list_html += "<th>职业</th>";
                list_html += "<th>总充值元宝</th>";
                list_html += "<th>剩余元宝</th>";
                list_html += "<th>创建时间</th>";
                list_html += "<th>在线时长(秒)</th>";
                list_html += "<th>上次登录时间</th>";
                list_html += "<th>所在服</th>";
                list_html += "<th>充值金额(元)</th>";
                list_html += "<th>充值次数</th>";
                list_html += "<th>单次最大充值(元)</th>";
                list_html += "<th>单次最小充值(元)</th>";
                list_html += "<th>平均充值(元)</th>";
                list_html += "<th>最后充值时间</th>";
                list_html += "</tr>";
                for (var i = 0; i < data.data.length; i++) {
                    var record = data.data[i];
                    list_html += "<tr>";
                    list_html += "<td>" + (i + 1) + "</td>";
                    list_html += "<td>" + record.userId + "</td>";
                    list_html += "<td>" + record.roleId + "</td>";
                    list_html += "<td>" + record.roleName + "</td>";
                    list_html += "<td>" + record.level + "</td>";
                    list_html += "<td>" + getCareer(record.career) + "</td>";
                    list_html += "<td>" + record.rechargeGold + "</td>";
                    list_html += "<td>" + record.remainGold + "</td>";
                    list_html += "<td>" + record.createTime + "</td>";
                    list_html += "<td>" + record.onlineTime + "</td>";
                    list_html += "<td>" + record.lastLoginTime + "</td>";
                    list_html += "<td>" + record.createSid + "</td>";
                    list_html += "<td><b>" + record.totalRecharge / 100 + "</b></td>";
                    list_html += "<td>" + record.rechargeCount + "</td>";
                    list_html += "<td>" + record.maxRecharge / 100 + "</td>";
                    list_html += "<td>" + record.minRecharge / 100 + "</td>";
                    list_html += "<td>" + record.avgRecharge / 100 + "</td>";
                    list_html += "<td>" + record.lastRechargeTime + "</td>";
                    list_html += "</tr>";
                }
                list_html += "</table>";
                $("#data").html(list_html);
            }
		});
	}
	function getCareer(career){
		switch(career){
            case 0:
                return "${msg['jsp.career.career0']}";
			case 1:
				return "${msg['jsp.career.career1']}";
            case 2:
                return "${msg['jsp.career.career2']}";
            case 3:
                return "${msg['jsp.career.career3']}";
			default:
				return "${msg['jsp.career.unknown']}";	
		}
	}
	function exportExcel(){
		$("#"+expId+" table").eq(0).tableExport({type: 'excel', escape: 'false'});		
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="query_form" class="well form-inline">
            <div class="row-fluid">
                <label class="label">${msg['statistic.public.platform']}</label>
                <select id="select_group" onchange="getServer(this.value);getChannelName(this.value);"></select><br/><br/>
                <label class="label">${msg['statistic.public.server']}</label>
                <span id="checkbox_server"></span><br/><br/>
                <label class="label">${msg['statistic.public.Channel']}</label>&nbsp;&nbsp;
                <span id="checkbox_channel"></span><br/><br/>
                <label class="label">${msg['statistic.public.time']}</label>&nbsp;&nbsp;
                <div class="input-append date" id="start">
                <input style="width: 120px;" name="startDate" size="20" value="${nowDate}" type="text"
                    readonly> <span class="add-on"><i class="icon-th"></i></span>
                </div>
                -
                <div class="input-append date" id="end">
                    <input style="width: 120px;" name="endDate" size="20" value="${nowDate}" type="text" readonly>
                    <span class="add-on"><i class="icon-th"></i></span>
                </div>
                <br/><br/>

                <label class="label">查询类型</label>
                <select id="select_type" name="type" class="span2">
                    <option value="0">角色</option>
                    <option value="1">用户</option>
                </select>
                <br/><br/>
                <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="getRechargeRank();">
                <input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel();">
            </div>
		</form>
    </div>

    <div class="container-fluid">
		<p id="msg"></p>
        <div id="data"></div>
    </div>
    <jsp:include page="../commonmodal.jsp"/>
</body>
</html>