<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
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
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/echarts/echarts.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        group_reload();
        $("#lineChart").hide();
        $("#date").datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm-dd',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            showMeridian: 1
        });

        $('#myTab a').click(function (e) {
            e.preventDefault();
            if ($(this).attr("id") == "line") {
                $("#lineChart").show();
                $("#tableChart").hide();
            } else {
                $("#lineChart").hide();
                $("#tableChart").show();
            }
            $(this).tab('show');
        });
    });
</script>
<script type="text/javascript">
    function search() {
        $("#lineChart").show();
        $("#loadingmodal").modal({
            backdrop: 'static',
            keyboard: false
        });
        $.ajax({
            type: "POST",
            url: base + "/operation/online",
            data: $("#query_form").serialize(),
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                $("#tableChart").html("");
                if (!data.ok) {
                    $("#msg").html(data.msg);
                    return;
                }

                //加入table显示
                var tableHtml = "<table class='table table-bordered table-striped'>" +
                    "<thead><tr><th>${msg['jsp.online.time']}</th><th>${msg['jsp.online.num']}</th></tr></thead><tbody>";
                var time = [];
                var num = [];
                for (var i = 0; i < data.data.length; i++) {
                    var onlineData = data.data[i];
                    tableHtml += "<tr><td>" + TimeObjectUtil.UnixToDate(onlineData.time) + "</td><td>" + onlineData.num + "</td>";
                    time[i] = TimeObjectUtil.UnixToHIS(onlineData.time);
                    num[i] = onlineData.num;
                }
                tableHtml += "</tbody></table>";
                $("#tableChart").html(tableHtml);
                $("#tableChart").hide();

                option.xAxis[0].data = time;
                option.series[0].name = "在线人数";
                option.series[0].data = num;
                // 取得数据后显示到图表中
                myChart.setOption(option);
                myChart.on(echarts.config.EVENT.CLICK, eConsole);
            }
        });
        getCurrentOnline();
    }

    function getCurrentOnline() {
        $.ajax({
            type: "POST",
            url: base + "/operation/refreshOnlineCount",
            data: $("#query_form").serialize(),
            dataType: "json",
            success: function (data) {
                if (!data.ok) {
                    $("#onlinecount").html(data.msg);
                    return;
                }
                $("#onlinecount").text(data.data[0].num + " (" + TimeObjectUtil.UnixToDate(data.data[0].time) + ")");
            }
        });
    }

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
</script>
</head>
<body>
<form action="#" id="query_form" class="well form-inline">
    <select id="select_group" onchange="queryServerByGroup(this.value)" class="span2"></select>
    <select id="select_server" name="serverId" class="span2"></select>
    <label class="label">${msg['jsp.log.timechoose']}</label>
    <div class="input-append date" id="date">
        <input style="width: 120px;" name="date" size="20" value="${newDate}" type="text" readonly>
        <span class="add-on"><i class="icon-th"></i></span>
    </div>

    <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
    <label style="height: 25px; line-height: 25px; font-size: 12px;">${msg['jsp.online.conlinecount']}：<b id="onlinecount"></b></label>
    <i class="icon-retweet" style="cursor: pointer;" id="refresh" onclick="getCurrentOnline();"></i>
</form>

<p id="msg"></p>
<ul class="nav nav-tabs" id="myTab">
    <li class="active"><a id="line" href="#lineChart" data-toggle="tab">${msg['online.echarts.chartshow']}</a></li>
    <li><a href="#tableChart" data-toggle="tab">${msg['online.echarts.tableshow']}</a></li>
</ul>


<div class="container-fluid">
    <div class="tab-content">
        <div class="tab-pane active" id="lineChart" style="height: 500px"></div>
        <div class="tab-pane" id="tableChart"></div>
        <div id="info"></div>
    </div>
</div>
<script>
    // 初始化一个图表实例
    var myChart = echarts.init(document.getElementById('lineChart'));
    // echarts把复杂的图表结构化，图表的基本结构包括以下部分：标题，x轴，y轴，数值序列。
    var option = {
        title: {
            text: "${msg['online.echarts.maintitle']}",
            subtext: "${msg['online.echarts.subhead']}"
        },
        tooltip: {
            show: true
        },
        //工具箱，每个图表最多仅有一个工具箱
        toolbox: {
            //显示策略，可选为：true（显示） | false（隐藏），默认值为false
            show: true,
            //启用功能，目前支持feature，工具箱自定义功能回调处理
            feature: {
                //辅助线标志
                mark: {show: true},
                //dataZoom，框选区域缩放，自动与存在的dataZoom控件同步，分别是启用，缩放后退
                dataZoom: {
                    show: true,
                    title: {
                        dataZoom: "${msg['online.echarts.zonezoom']}",
                        dataZoomReset: "${msg['online.echarts.zonezoomback']}"
                    }
                },
                //数据视图，打开数据视图，可设置更多属性,readOnly 默认数据视图为只读(即值为true)，可指定readOnly为false打开编辑功能
                dataView: {show: true, readOnly: true},
                //magicType，动态类型切换，支持直角系下的折线图、柱状图、堆积、平铺转换
                magicType: {show: true, type: ['line', 'bar']},
                //restore，还原，复位原始图表
                restore: {show: true},
                //saveAsImage，保存图片（IE8-不支持）,图片类型默认为'png'
                saveAsImage: {show: true}
            }
        },
        xAxis: [{
            name: "${msg['online.echarts.time']}",
            type: 'category',
            boundaryGap: false,
            data: []
        }],
        yAxis: [{
            type: 'value',
            splitArea: {show: true},
            axisLabel: {
                formatter: "{value}"
            }
        }],
        calculable: true,
        series: [{
            name: "${msg['online.echarts.onlinepeople']}",
            type: "line",
            data: [],
            markPoint: {
                data: [
                    {type: 'max', name: "${msg['online.echarts.max']}"},
                    {type: 'min', name: "${msg['online.echarts.min']}"}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: "${msg['online.echarts.average']}"}
                ]
            }
        }]
    };
    myChart.setOption(option);
</script>
<jsp:include page="../commonmodal.jsp"></jsp:include>
</body>
</html>