<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.log.onlinetitle']}</title>
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
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/echarts/echarts.js"></script>
<script type="text/javascript">
	var base = '${base}';
	$(function() {
		group_reload();
		setTimeout(function () {
			var groupName = $("#select_group").val();
			getChannelName(groupName);
		}, 100);
		$(".date").datetimepicker({
			initialDate : new Date(),
			language:  'zh-CN',
			format : 'yyyy-mm-dd hh:ii:ss',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 1,
			showMeridian : 1
		});
		$('#myTab a').click(function(e) {
			e.preventDefault();
			if($(this).attr("id")=="line"){
				$("#lineChart").show();
				$("#tableChart").hide();
				$("#excel_btn").hide();
			}else{
				$("#lineChart").hide();
				$("#tableChart").show();
				$("#excel_btn").show();
			}
			$(this).tab('show');
		});
		$("#excel_btn").hide();
		$("#loading").shCircleLoader();
	});

	function search() {
		var startDate=$("#start input").val();
		var endDate=$("#end input").val();
		var channelNames = [];
		$("input[name='channelName']:checked").each(function(){ 
			channelNames.push($(this).val());
        });
		var condition = $("#select_condition").val();
		var level = $("input[name='level']").val();
		if(checkDate(startDate,endDate)){
			$("#level").show();
			$("#loadingmodal").modal({backdrop : 'static', keyboard : false});
			$.ajax({
				type : "POST",
				url : base + "/operation/level",
				data : {
					groupName : $("#select_group").val(),
					channelNames : channelNames + "" ,
					serverId : $("#select_server").val(),
					condition : condition,
					level : level,
					startDate : startDate,
					endDate : endDate
				},
				dataType : "json",
				success : function(data) {
					$("#loadingmodal").modal('hide');
					if (!data.ok) {
						$("#excel_btn").hide();
						$("#level").html(data.msg);
						return;
					}
					myChart.clear();
					$("#tableChart").html("");
					var table = $("<table>").attr("class", "table table-bordered table-striped");
					var thead=$("<thead>");
					var htr = $("<tr>");
					var th1 = $("<th>").text("${msg['level.echarts.level']}");
					var th2 = $("<th>").text("${msg['level.echarts.peoplecount']}");
					htr.append(th1).append(th1).append(th2);
					thead.append(htr);
					table.append(thead);

					var tbody = $("<tbody>");
					for (var i = 0; i < data.data.length; i++) {
						var tableData = data.data[i];
						var tr = $("<tr>");
						var td1 = $("<td>").text(tableData.level);
						var td2 = $("<td>").text(tableData.rolecount);
						tr.append(td1).append(td2);
						tbody.append(tr);
					}
					table.append(tbody);
					$("#tableChart").html(table);
					$("#tableChart").hide();
					$("#excel_btn").show();
	
					var level = [];
					var num = [];
					for (var i = 0; i < data.data.length; i++) {
						var levelData = data.data[i];
						level[i] = levelData.level;
						num[i] = levelData.rolecount;
					}
					
					option.xAxis[0].data = level;
					option.series[0].data = num;
					// 取得数据后显示到图表中  
					myChart.setOption(option);
					myChart.on(echarts.config.EVENT.CLICK, eConsole);
				}
			});
		}
	}
	// 事件的参数中包括：数据在序列中的下标dataIndex，数据的值value，x轴上的名称name  
	function eConsole(param) {
		console.log(param);
		if (typeof param.seriesIndex == 'undefined') {
			return;
		}
		if (param.type == 'click') {
			var mes="";
			if(param.special==undefined){
				var sum=0;
				var clickDataList=option.series[param.seriesIndex].data;
				for (var i = 0; i <= param.dataIndex; i++) {
					sum+=clickDataList[i];
				}
				mes = param.name + ':' + param.value+"&nbsp;&nbsp;${msg['level.echarts.zonesum']}："+sum;
			}else{
				mes = param.name + ':' + param.value;
			}
			document.getElementById('info').innerHTML = mes;
		}
	}
	function exportExcel(){
		$("#tableChart table").eq(0).tableExport({type: 'excel', escape: 'false'});
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="query_form" class="well form-inline">
			<label class="label">${msg['statistic.public.server']}</label>
			<select id="select_group" onchange="queryServerByGroup(this.value);getChannelName(this.value);" class="span2"></select>
			<select id="select_server" name="serverId" class="span2"></select> 
			<br/><br/>

			<label class="label">${msg['statistic.public.Channel']}</label>
			<span id="checkbox_channel"></span>
			<br/><br/>

			<!-- 时间段 -->
			<label class="label">${msg['jsp.level.createtime']}</label>
			<div class="input-append date" id="start">
				<input style="width: 120px;" name="startDate" size="20" type="text" readonly>
				<span class="add-on"><i class="icon-th"></i></span>
			</div>
			-
			<div class="input-append date" id="end">
				<input style="width: 120px;" name="endDate" size="20" type="text" readonly>
				<span class="add-on"><i class="icon-th"></i></span>
			</div>
			<br/><br/>

			<label class="label" for="minLv">${msg['jsp.level.minlevel']}</label>
			<input id="minLv" name="level" type="text" class="span2" value="0"/>
			<select id="select_condition" name="condition" class="span3">
				<option value="0">${msg['jsp.level.offline']}</option>
				<option value="1">${msg['jsp.level.behindoffline']}</option>
			</select> 
			<br/>
			<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
			<input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel();">
		</form>
		<ul class="nav nav-tabs" id="myTab">
			<li class="active"><a id="line" href="#lineChart" data-toggle="tab">${msg['online.echarts.chartshow']}</a></li>
			<li><a href="#tableChart" data-toggle="tab">${msg['online.echarts.tableshow']}</a></li>
		</ul>
		<div id="msg"></div>
		<div class="tab-content">
			<div class="tab-pane active" id="lineChart" style="height: 500px"></div>
			<div class="tab-pane" id="tableChart"></div>
		</div>
		<div id="info"></div>
		<jsp:include page="../commonmodal.jsp"/>

		<script type="text/javascript">
			// 初始化一个图表实例  
			var myChart = echarts.init(document.getElementById("lineChart"));
			// echarts把复杂的图表结构化，图表的基本结构包括以下部分：标题，x轴，y轴，数值序列。
			var option = {
				title : {
					text : "${msg['level.echarts.maintitle']}",
					subtext : "${msg['level.echarts.subhead']}"
				},
			    tooltip : {
			        trigger: 'axis'
			    },
				legend : {
					data : ["${msg['level.echarts.peoplecount']}"]
				},
				//工具箱，每个图表最多仅有一个工具箱  
				toolbox : {
					//显示策略，可选为：true（显示） | false（隐藏），默认值为false  
					show : true,
					//启用功能，目前支持feature，工具箱自定义功能回调处理  
					feature : {
						//辅助线标志  
						mark : {
							show : true
						},
						//dataZoom，框选区域缩放，自动与存在的dataZoom控件同步，分别是启用，缩放后退  
						dataZoom : {
							show : true,
							title : {
								dataZoom : "${msg['level.echarts.zonezoom']}",
								dataZoomReset : "${msg['level.echarts.zonezoomback']}"
							}
						},
						//数据视图，打开数据视图，可设置更多属性,readOnly 默认数据视图为只读(即值为true)，可指定readOnly为false打开编辑功能  
						dataView : {
							show : true,
							readOnly : true
						},
						//magicType，动态类型切换，支持直角系下的折线图、柱状图、堆积、平铺转换  
						magicType : {
							show : true,
							type : [ 'line', 'bar' ]
						},
						//restore，还原，复位原始图表  
						restore : {
							show : true
						},
						//saveAsImage，保存图片（IE8-不支持）,图片类型默认为'png'  
						saveAsImage : {
							show : true
						}
					}
				},
				calculable : true,
			    xAxis : [
			             {
			            	 name :"${msg['level.echarts.level']}",
			                 type : 'category',
			                 boundaryGap : false,
			             }
			         ],
			    yAxis : [
			             {	
			            	 name :"${msg['level.echarts.peoplecount']}",
			            	 type : 'value',
							 splitArea : {
								show : true
							 },
							 axisLabel : {
								formatter : "{value}"
							 }
			             }
			         ],
				series : [ {
					name : "${msg['level.echarts.peoplecount']}",
					type : 'line',
					markPoint : {
		                  data : [
		                      {type : 'max', name: "${msg['level.echarts.max']}"},
		                      {type : 'min', name: "${msg['level.echarts.min']}"}
		                  ]
		            },
		            markLine : {
		                  data : [
		                      {type : 'average', name: "${msg['level.echarts.average']}"}
		                  ]
		            }
				} ]
			};
		</script>
	</div>
</body>
</html>