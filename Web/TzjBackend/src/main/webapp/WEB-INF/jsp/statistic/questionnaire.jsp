<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>有奖问答</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript">
	var base = '${base}';
	$(function() {
		reloadCrossGroup(3);
		$("#start").datetimepicker({
			language : 'zh-CN',
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
			language : 'zh-CN',
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

	function search() {
		$("#progressmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
			url: base + "/questionnaire/searchData",
			data: $("#query_form").serialize(),
			type : "post",
			dataType: "json",
			success: function(data) {
				$("#progressmodal").modal('hide');
				if (!data.ok) {
					$("#msg").text(data.msg);
					return;
				}
				var tableStr = createTable(data.data.fields, data.data.datas);
				$("#msg").html("<input type='button' value='导出' class='btn btn-primary' onclick='exportExcel();'>");
				$("#msg").append("共" + data.data.pager.recordCount + "条记录，当前" + data.data.datas.length + "条记录");
				$("#data").html(tableStr);
			}
		});
	}
	function statistics() {
		$("#progressmodal").modal({backdrop:'static',keyboard:false});
		$.ajax({
			url: base + "/questionnaire/statistics",
			data: $("#query_form").serialize(),
			type : "post",
			dataType: "json",
			success: function(data) {
				$("#progressmodal").modal('hide');
				if (!data.ok) {
					$("#msg").text(data.msg);
					return;
				}
				$("#data").empty();
				console.log(data);
				createTableData(data.data);
			}
		});
	}

	function createTableData(data) {
		var html = "<table class='table table-bordered table-striped'>";
		for (var key in data.question) {
			html += "<tr><td rowspan='2'>" + key + "</td><td>" + data.question[key].question + "</td></tr><tr><td>";
			for (var answer in data.data[key]) {
				var percent = toPercent(data.data[key][answer] / data.joinNum)
				html += answer +"(" + percent + ")" + "&nbsp;&nbsp;&nbsp;&nbsp;";
			}
			html += "</td></tr>";
		}
		html += "</table>";
		$("#data").html(html);
	}

	function toPercent(point){
		var str=Number(point*100).toFixed(1);
		str+="%";
		return str;
	}


</script>
</head>
<body>
<div class="container-fluid">
	<form action="#" id="query_form" class="well form-inline">
		<select id="select_group" name="groupName" onchange="reloadCrossServerInfo(3, this.value);" class="span2"></select>
		<select id="select_server" name="serverId" class="span2" style="display: none;"></select>

		<!-- 时间段 -->
		<label class="label">${msg['jsp.log.time']}</label>
		<div class="input-append date" id="start">
			<input style="width: 120px;" name="startDate" size="20" type="text" value="${nowDate}"  readonly>
			<span class="add-on">
				<i class="icon-th"></i>
			</span>
		</div>
		-
		<div class="input-append date" id="end">
			<input style="width: 120px;" name="endDate" size="20" value="${nowDate}"  type="text" readonly>
			<span class="add-on">
				<i class="icon-th"></i>
			</span>
		</div>
		<input type="button" id="statistic_btn" value="统计" class="btn btn-primary" onclick="statistics()">
		<br/><br/>
		<label class="label" for="roleId">角色Id</label><input id="roleId" type="text" name="roleId" class="span2"/>
		<label class="label" for="userId">用户Id</label><input id="userId" type="text" name="userId" class="span2"/>
		<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
	</form>
	<div id="msg"></div>
	<div id="data"></div>
<%--	<script type="text/javascript" src="${base}/js/echarts/echarts-all.js"></script>--%>
<%--	<script type="text/javascript">--%>
<%--		// 初始化一个图表实例--%>
<%--		//var myChart = echarts.init(document.getElementById("level"));--%>
<%--		// echarts把复杂的图表结构化，图表的基本结构包括以下部分：标题，x轴，y轴，数值序列。--%>
<%--		var option = {--%>
<%--			title : {--%>
<%--				text : "${msg['money.echarts.maintitle']}",--%>
<%--				subtext : "${msg['money.echarts.subhead']}"--%>
<%--			},--%>
<%--			tooltip : {--%>
<%--				trigger: 'axis'--%>
<%--			},--%>
<%--			legend : {--%>
<%--				data : ["${msg['money.echarts.output']}","${msg['money.echarts.consume']}"]--%>
<%--			},--%>
<%--			//工具箱，每个图表最多仅有一个工具箱--%>
<%--			toolbox : {--%>
<%--				//显示策略，可选为：true（显示） | false（隐藏），默认值为false--%>
<%--				show : true,--%>
<%--				//启用功能，目前支持feature，工具箱自定义功能回调处理--%>
<%--				feature : {--%>
<%--					//辅助线标志--%>
<%--					mark : {--%>
<%--						show : true--%>
<%--					},--%>
<%--					//dataZoom，框选区域缩放，自动与存在的dataZoom控件同步，分别是启用，缩放后退--%>
<%--					dataZoom : {--%>
<%--						show : true,--%>
<%--						title : {--%>
<%--							dataZoom : "${msg['money.echarts.zonezoom']}",--%>
<%--							dataZoomReset : "${msg['money.echarts.zonezoomback']}"--%>
<%--						}--%>
<%--					},--%>
<%--					//数据视图，打开数据视图，可设置更多属性,readOnly 默认数据视图为只读(即值为true)，可指定readOnly为false打开编辑功能--%>
<%--					dataView : {--%>
<%--						show : true,--%>
<%--						readOnly : true--%>
<%--					},--%>
<%--					//magicType，动态类型切换，支持直角系下的折线图、柱状图、堆积、平铺转换--%>
<%--					magicType : {--%>
<%--						show : true,--%>
<%--						type : [ 'line', 'bar' ]--%>
<%--					},--%>
<%--					//restore，还原，复位原始图表--%>
<%--					restore : {--%>
<%--						show : true--%>
<%--					},--%>
<%--					//saveAsImage，保存图片（IE8-不支持）,图片类型默认为'png'--%>
<%--					saveAsImage : {--%>
<%--						show : true--%>
<%--					}--%>
<%--				}--%>
<%--			},--%>
<%--			calculable : true,--%>
<%--			xAxis : [--%>
<%--					 {--%>
<%--						 name :"${msg['money.echarts.time']}",--%>
<%--						 type : 'category',--%>
<%--						 boundaryGap : false,--%>
<%--						 data : []--%>
<%--					 }--%>
<%--				 ],--%>
<%--			yAxis : [--%>
<%--					 {--%>
<%--						 name :"${msg['money.echarts.count']}",--%>
<%--						 type : 'value',--%>
<%--						 splitArea : {--%>
<%--							show : true--%>
<%--						 },--%>
<%--						 axisLabel : {--%>
<%--							formatter : "{value}"--%>
<%--						 }--%>
<%--					 }--%>
<%--				 ],--%>
<%--			series : [ {--%>
<%--				name : "${msg['money.echarts.output']}",--%>
<%--				type : 'line',--%>
<%--				data : [],--%>
<%--				markPoint : {--%>
<%--					  data : [--%>
<%--						  {type : 'max', name: "${msg['money.echarts.max']}"},--%>
<%--						  {type : 'min', name: "${msg['money.echarts.min']}"}--%>
<%--					  ]--%>
<%--				},--%>
<%--				markLine : {--%>
<%--					  data : [--%>
<%--						  {type : 'average', name: "${msg['money.echarts.average']}"}--%>
<%--					  ]--%>
<%--				}--%>
<%--			},--%>
<%--			{--%>
<%--				name : "${msg['money.echarts.consume']}",--%>
<%--				type : 'line',--%>
<%--				data : [],--%>
<%--				markPoint : {--%>
<%--					  data : [--%>
<%--						  {type : 'max', name: "${msg['money.echarts.max']}"},--%>
<%--						  {type : 'min', name: "${msg['money.echarts.min']}"}--%>
<%--					  ]--%>
<%--				},--%>
<%--				markLine : {--%>
<%--					  data : [--%>
<%--						  {type : 'average', name: "${msg['money.echarts.average']}"}--%>
<%--					  ]--%>
<%--				}--%>
<%--			}]--%>
<%--		};--%>
<%--		// 为echarts对象加载数据--%>
<%--		//myChart.setOption(option);--%>
<%--	</script>--%>
</div>
</body>
</html>