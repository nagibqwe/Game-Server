<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.money.title']}</title>
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
		group_reload();
		//$("#excel_btn").hide();
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
		initType();
		$("#loading").shCircleLoader();
		window.setTimeout(getChannelName,1000); 
	});
	function getChannelName(){
		var platfName = $("#select_group").val();
		$.ajax({
			url : base + "/channel/getChannelName",
			data : {
				platfName : platfName,
			},
			method : "post",
			dataType : "json",
			success : function(data) {
				var channelHtml = "";
				if(data == null){
					$("#channelDiv").html(channelHtml);
					return;
				}
				for(var i = 0 ; i < data.length ; i++){
					channelHtml += "<input type='checkbox' name='channelName' value=\""+data[i]+"\" />"+data[i]+"&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				$("#channelDiv").html(channelHtml);
			}
		});
	}
	function initType(){
		var div = $("#chooseDiv");
		div.empty();
		var allcheckLabel=$("<label>").attr("class","checkbox inline").text("${msg['jsp.money.allcheck']}");
		var allcheck=$("<input>").attr("type","checkbox").attr("checked",false).attr("onchange","checkall(this);");
		div.append(allcheckLabel);
		div.append(allcheck);
		div.append("&nbsp;&nbsp;");
		
		for(var i=1;i<=4;i++){
			var label=$("<label>").attr("class","checkbox inline").text(getMoneyName(i.toString())+"["+i+"]");
			var checkbox = $("<input>").attr("type","checkbox").attr("class","choosecheckbox").attr("name","moneyTypeList").val(i);
			div.append(label);
			div.append(checkbox);
			div.append("&nbsp;&nbsp;");
		}
	};
	function checkall(obj){
		var c = $(".choosecheckbox");
		//$("#allcheck").attr('checked');
		var ac = $(obj);
		var allChecked = true, allCanceled = true;
		for(i=0; i<c.length; i++){
			if(!c[i].checked){ allChecked=false; break; }
		}
		for(i=0; i<c.length; i++){
			if(c[i].checked){ allCanceled=false; break; }
		}
		if(allChecked && ac=='checked'){
			return;
		}
		if(allCanceled && ac===undefined){
			return;
		}
		for(i=0; i<c.length; i++){
			c[i].checked=allChecked?false: true;
		}
	};
	function search() {
		var startDate = $("#start input").val();
		var endDate = $("#end input").val();
		var isBlack = $("#isBlackList").is(":checked");//用来过滤黑名单
		var channelNames = [];
		$("input[name='channelName']:checked").each(function(){ 
			channelNames.push($(this).val());
        });
		$("#isBlack").val(isBlack);
		if (checkDate(startDate, endDate)) {
			$("#loadingmodal").modal({backdrop:'static',keyboard:false});
			$
					.ajax({
						type : "POST",
						url : base + "/statistic/moneyOutputAndConsumeStatistic",
						data : $("#query_form").serialize(),
						dataType : "json",
						success : function(data) {
							//console.log(data);
							$("#money").empty();
							$("#loadingmodal").modal('hide');
							if (!data.ok) {
								/* $("#excel_btn").hide(); */
								$("#money").html(data.msg);
								return;
							}

							var allData=data.data;
							//console.log(allData);
							
							for ( var key in allData) {
								var div=$("<div>").attr("style","height: 600px;");
								$("#money").append(div);
								var myChart = echarts.init(div[0]);
								//console.log(myChart);
								
								var time = [];
								var output = [];
								var consume = [];
								
								var i=0;
								for ( var day in allData[key]) {
									var money=allData[key][day];
									//console.log(money);
									time[i] = money.day;
									output[i] = money.output;
									consume[i] = money.consume;
									i++;
								}
								var legend =[getMoneyName(key)+"${msg['money.echarts.output']}",
								             getMoneyName(key)+"${msg['money.echarts.consume']}"];
								
								option.title.text = getMoneyName(key)+"${msg['money.echarts.maintitle']}";
								option.title.subtext = getMoneyName(key)+"${msg['money.echarts.subhead']}";
								
								option.series[0].name = getMoneyName(key)+"${msg['money.echarts.output']}";
								option.series[1].name = getMoneyName(key)+"${msg['money.echarts.consume']}";
								
								option.legend.data = legend;
								
								//console.log(time);
								//console.log(output);
								option.xAxis[0].data = time;
								option.series[0].data = output;
								option.series[1].data = consume;
								// 取得数据后显示到图表中  
								myChart.setOption(option);
								/*
								//console.log(key);
								var table = $("<table>").attr("class",
								"table table-bordered table-striped");
								var thead = $("<thead>");
								var htr = $("<tr>");
								var fields = [ "${msg['jsp.money.day']}",
								        getMoneyName(key)+"${msg['jsp.money.output']}",
								        getMoneyName(key)+"${msg['jsp.money.consume']}",
								        getMoneyName(key)+"${msg['jsp.money.balance']}" ];
								for ( var field in fields) {
									var th = $("<th>").text(fields[field]);
									htr.append(th);
								}
								thead.append(htr);
								table.append(thead);
								
								var tbody = $("<tbody>");
								for ( var day in allData[key]) {
									//console.log(allData[key][day]);
									var tr = $("<tr>");
									var datalist = [
									        allData[key][day].day,
									        allData[key][day].output,
									        allData[key][day].consume,
									        Math.abs(allData[key][day].output-allData[key][day].consume) ];
									for ( var i in datalist) {
										var td = $("<td>").text(datalist[i]);
										tr.append(td);
									}
									tbody.append(tr);
								}
								table.append(tbody);
								divs.append(table);*/
							}
						}
					});
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
		$("#money div table").eq(0).tableExport({type: 'excel', escape: 'false'});
	};

	function getMoneyName(moneyType){
		switch (moneyType) {
		case "1":
			return "${msg['jsp.money.1']}";
		case "2":
			return "${msg['jsp.money.2']}";
		case "3":
			return "${msg['jsp.money.3']}";
		case "4":
			return "${msg['jsp.money.4']}";
		default:
			return "${msg['jsp.money.unknown']}"+moneyType;
		}
	};
</script>
</head>
<body>
	<div class="container-fluid">
		
			<form action="#" id="query_form" class="well form-inline">
				<select id="select_group" name="groupName"
					onchange="queryServerByGroup(this.value);getChannelName();" class="span2"></select> <select
					id="select_server" name="serverId" class="span2"></select>

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
				<br/>
				<b>${msg['jsp.money.choosediv']}</b>
				<div id="chooseDiv" class="well form-inline"></div>
				<label class="label">${msg['statistic.public.Channel']}</label>
				<br/>
				<!-- 获取渠道列表 -->
				<div id="channelDiv">
				</div>
				<br/>
				<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
				<%-- <input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel();"> --%>
				${msg['statistic.public.isblack']}:<input type="checkbox" id="isBlackList"/><input id="isBlack" name="isBlack" type="hidden">
			</form>
		
		<p id="msg"></p>
		
		<div id="money"></div>
		<div id="level" style="height: 500px"></div>
		<script type="text/javascript" src="${base}/js/echarts/echarts-all.js"></script>
		<script type="text/javascript">
			// 初始化一个图表实例  
			//var myChart = echarts.init(document.getElementById("level"));
			// echarts把复杂的图表结构化，图表的基本结构包括以下部分：标题，x轴，y轴，数值序列。
			var option = {
				title : {
					text : "${msg['money.echarts.maintitle']}",
					subtext : "${msg['money.echarts.subhead']}"
				},
			    tooltip : {
			        trigger: 'axis'
			    },
				legend : {
					data : ["${msg['money.echarts.output']}","${msg['money.echarts.consume']}"]
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
								dataZoom : "${msg['money.echarts.zonezoom']}",
								dataZoomReset : "${msg['money.echarts.zonezoomback']}"
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
			            	 name :"${msg['money.echarts.time']}",
			                 type : 'category',
			                 boundaryGap : false,
			                 data : []
			             }
			         ],
			    yAxis : [
			             {	
			            	 name :"${msg['money.echarts.count']}",
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
					name : "${msg['money.echarts.output']}",
					type : 'line',
					data : [],
					markPoint : {
		                  data : [
		                      {type : 'max', name: "${msg['money.echarts.max']}"},
		                      {type : 'min', name: "${msg['money.echarts.min']}"}
		                  ]
		            },
		            markLine : {
		                  data : [
		                      {type : 'average', name: "${msg['money.echarts.average']}"}
		                  ]
		            }
				},
				{
					name : "${msg['money.echarts.consume']}",
					type : 'line',
					data : [],
					markPoint : {
		                  data : [
		                      {type : 'max', name: "${msg['money.echarts.max']}"},
		                      {type : 'min', name: "${msg['money.echarts.min']}"}
		                  ]
		            },
		            markLine : {
		                  data : [
		                      {type : 'average', name: "${msg['money.echarts.average']}"}
		                  ]
		            }
				}]
			};

			// 为echarts对象加载数据 
			//myChart.setOption(option);
		</script>
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