<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.career.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
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
		$("#loading").shCircleLoader();
	});

	function search() {
		$("#career").show();
		$("#loadingmodal").modal({backdrop : 'static', keyboard : false});
		var channelNames = [];
		$("input[name='channelName']:checked").each(function(){ 
			channelNames.push($(this).val());
        });
		$.ajax({
			type : "POST",
			url : base + "/operation/career",
			data : {
				groupName : $("#select_group").val(),
				channelNames : channelNames + "" ,
				serverId : $("#select_server").val()
			},
			dataType : "json",
			success : function(data) {
				myChart.clear();
				$("#loadingmodal").modal('hide');
				if (!data.ok) {
					$("#career").html(data.msg);
					return;
				}

				var career = [];
				var dataList = [];
				for (var i = 0; i < data.data.length; i++) {
					var careerData = data.data[i];
					career[i] = getCareer(careerData.career);
					dataList[i] = {
						value : careerData.rolecount,
						name : getCareer(careerData.career)
					};
				}

				option.legend.data = career;
				option.series[0].data = dataList;
				// 取得数据后显示到图表中  
				myChart.setOption(option);
				myChart.on(echarts.config.EVENT.CLICK, eConsole);
			}
		});
	};
	// 事件的参数中包括：数据在序列中的下标dataIndex，数据的值value，x轴上的名称name  
	function eConsole(param) {
		if (typeof param.seriesIndex == 'undefined') {
			return;
		}
		if (param.type == 'click') {
			var mes = param.name + ':' + param.value;
			document.getElementById('info').innerHTML = mes;
		}
	}
	function getCareer(career) {
		switch (career) {
			case "0":
				return "${msg['jsp.career.career0']}";
			case "1":
				return "${msg['jsp.career.career1']}";
            case "2":
                return "${msg['jsp.career.career2']}";
            case "3":
                return "${msg['jsp.career.career3']}";
			default:
				return "${msg['jsp.career.unknown']}[" + career + "]";
		}
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="#" id="query_form" class="well form-inline">
			<label class="label">${msg['statistic.public.server']}</label>
			<select id="select_group" onchange="queryServerByGroup(this.value);getChannelName();" class="span2"></select> 
			<select id="select_server" name="serverId" class="span2"></select>
			<br/><br/>

			<label class="label">${msg['statistic.public.Channel']}</label>
			<span id="checkbox_channel"></span>
			<br/><br/>

			<input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
		</form>
		<div id="msg"></div>

		<div class="container-fluid">
			<div id="career" style="height: 500px"></div>
			<div id="info"></div>
		</div>

		<script type="text/javascript">
			// 初始化一个图表实例  
			var myChart = echarts.init(document.getElementById("career"));
			// echarts把复杂的图表结构化，图表的基本结构包括以下部分：标题，x轴，y轴，数值序列。
			var option = {
				title : {
					text : "${msg['career.echarts.maintitle']}",
					x : 'center'
				},
				tooltip : {
					trigger : 'item',
					formatter : "{a} <br/>{b} : {c} ({d}%)"
				},
				legend : {
					orient : 'vertical',
					x : 'left',
					data : []
				},
				toolbox : {
					show : true,
					feature : {
						mark : {
							show : true
						},
						dataView : {
							show : true,
							readOnly : false
						},
						magicType : {
							show : true,
							type : [ 'pie', 'funnel' ],
							option : {
								funnel : {
									x : '25%',
									width : '50%',
									funnelAlign : 'left',
									max : 1548
								}
							}
						},
						restore : {
							show : true
						},
						saveAsImage : {
							show : true
						}
					}
				},
				calculable : true,
				series : [ {
					name : "${msg['career.echarts.career']}",
					type : 'pie',
					radius : '55%',
					center : [ '50%', '60%' ],
					itemStyle : {
						normal : {
							label : {
								show : true,
								formatter : '{b} : {c} ({d}%)'
							},
							labelLine : {
								show : true
							}
						}
					},
					data : [
						/* {value:50, name:'法师'},
                        {value:60, name:'战士'} */
					]
				} ]
			};

			// 为echarts对象加载数据 
			//myChart.setOption(option);
		</script>
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
</body>
</html>