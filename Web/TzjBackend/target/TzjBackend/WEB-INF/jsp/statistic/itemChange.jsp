<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.money.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        getGroup();
        $("#loading").shCircleLoader();
        $("#start").datetimepicker({
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
        $("#end").datetimepicker({
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
    });

    function search() {
        searchData('getConsumeAndOutput');
        $("#getConsumeAndOutputLi").addClass("active");
        $("#getConsumeLi").removeClass("active");
        $("#getOutputLi").removeClass("active");
        $("#getConsumeAndOutputList").addClass("active");
        $("#getConsumeList").removeClass("active");
        $("#getOutputList").removeClass("active");
    }

    function searchData(actionType) {
        if ($("input[name='serverId']:checked").length == 0) {
            alert("${msg['statistic.public.syspromt.server']}");
            return;
        }
        var startDate = $("#start input").val();
        var endDate = $("#end input").val();
        if (checkDate(startDate, endDate)) {
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.ajax({
                type: "POST",
                url: base + "/itemChange/" + actionType,
                data: $("#query_form").serialize(),
                dataType: "json",
                success: function (data) {
                    $("#loadingmodal").modal('hide');
                    if (data == null) {
                        return;
                    }
                    if (actionType == "getConsumeAndOutput") {
                        $("#getConsumeAndOutputList").empty();
                        setConsumeAndOutput(data);
                    } else if (actionType == "getConsume") {
                        $("#getConsumeList").empty();
                        setConsume(data);
                    } else if (actionType == "getOutput") {
                        $("#getOutputList").empty();
                        setOutput(data);
                    }
                }
            });
        }
    }

    function setConsumeAndOutput(data) {
        for (var key in data) {
            var div = $("<div>").attr("style", "height: 600px;");
            $("#getConsumeAndOutputList").append(div);
            var myChart = echarts.init(div[0]);
            var time = [];
            var output = [];
            var consume = [];

            var i = 0;
            for (var day in data[key]) {
                var item = data[key][day];
                time[i] = item.day;
                output[i] = item.output;
                consume[i] = item.consume;
                i++;
            }
            var legend = [key + "${msg['money.echarts.output']}", key + "${msg['money.echarts.consume']}"];

            option.title.text = key + "${msg['money.echarts.maintitle']}";
            option.title.subtext = key + "${msg['money.echarts.subhead']}";

            option.series[0].name = key + "${msg['money.echarts.output']}";
            option.series[1].name = key + "${msg['money.echarts.consume']}";

            option.legend.data = legend;

            option.xAxis[0].data = time;
            option.series[0].data = output;
            option.series[1].data = consume;
            myChart.setOption(option);
        }
    }

    function setConsume(data) {
        for (var key in data) {
            var div = $("<div>").attr("style", "height: 600px;");
            $("#getConsumeList").append(div);
            var myChart2 = echarts.init(div[0]);
            var reasons = [];
            var dataList = [];
            var i = 0;
            for (var r in data[key]) {
                var reasonData = data[key][r];
                //console.log(reasonData);
                reasons[i] = reasonData.reason;
                dataList[i] = {
                    value: reasonData.consume,
                    name: reasonData.reason
                };
                i++;
            }

            option2.title.text = key + "${msg['reason.echarts.maintitle']}";
            option2.legend.data = reasons;

            option2.series[0].data = dataList;
            myChart2.setOption(option2);
        }
    }

    function setOutput(data) {
        for (var key in data) {
            var div = $("<div>").attr("style", "height: 600px;");
            $("#getOutputList").append(div);
            var myChart2 = echarts.init(div[0]);
            var reasons = [];
            var dataList = [];
            var i = 0;
            for (var r in data[key]) {
                var reasonData = data[key][r];
                //console.log(reasonData);
                reasons[i] = reasonData.reason;
                dataList[i] = {
                    value: reasonData.output,
                    name: reasonData.reason
                };
                i++;
            }

            option2.title.text = key + "${msg['reason.echarts.maintitle']}";
            option2.legend.data = reasons;

            option2.series[0].data = dataList;
            // 取得数据后显示到图表中
            myChart2.setOption(option2);
        }
    }

    function selectAllReasonId() {
        var che = $("#selectAll").is(":checked");
        $("[name='reasonId']").prop("checked", che);
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <label class="label">${msg['statistic.public.platform']}</label>&nbsp;&nbsp;
        <select id="select_group" onchange="getServer(this.value);getChannelName(this.value);"></select>
        <br/><br/>

        <label class="label">${msg['statistic.public.server']}</label>&nbsp;&nbsp;
        <span id="checkbox_server"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.Channel']}</label>&nbsp;&nbsp;
        <span id="checkbox_channel"></span>
        <br/><br/>

        <label class="label">${msg['jsp.log.time']}</label>&nbsp;&nbsp;
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="startDate" size="20" type="text" value="${nowDate}" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endDate" size="20" value="${nowDate}" type="text" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <br/><br/>

        <label class="label">${msg['statistic.itemChange.condition']}</label>&nbsp;&nbsp;
        <input name="conditionList" id="conditionList" type="text" size="60" class="validate[required]" readonly/>
        <input type="button" id="addItem" value="+"/><input type="button" id="deleCondition" value="-"/><input
            type="button" id="empty" value="${msg['activity.empty']}"/>
        <br/><br/>

        <!-- 获取原因列表 -->
        <label class="label">${msg['statistic.public.reason']}</label>&nbsp;&nbsp;
        <table style="width:100%;">
            <% int i = 0;%>
            <c:forEach var="reason" items="${reasonMap}">
                <% if (i % 5 == 0) { %>
                <tr>
                    <% }%>
                    <td>
                        <input type='checkbox' name='reasonId' value='${reason.key}'/>[${reason.key}]${reason.value}&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <% i++; %>
                    <% if (i % 5 == 0) { %>
                </tr>
                <% }%>
            </c:forEach>
            <% if (i % 5 != 0) { %>
            </tr>
            <% }%>
        </table>

        <input type='checkbox' name='selectAll' id='selectAll' value="${msg['statistic.public.selectAll']}" onclick="selectAllReasonId()"/>${msg['statistic.public.selectAll']}&nbsp;&nbsp;&nbsp;&nbsp;
        <br/><br/>

        <label class="label" for="isBlackList">${msg['statistic.public.isblack']}</label>
        <input type="checkbox" id="isBlackList"/>
        <br/><br/>

        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()"/>
    </form>
    <ul class="nav nav-tabs" id="myTab">
        <li class="active" id="getConsumeAndOutputLi">
            <a href="#getConsumeAndOutputList" data-toggle="tab" onclick="searchData('getConsumeAndOutput')">
                ${msg['statistic.itemChange.consumeAndOutput']}
            </a>
        </li>
        <li id="getConsumeLi">
            <a href="#getConsumeList" data-toggle="tab" onclick="searchData('getConsume')">
                ${msg['statistic.itemChange.consume']}
            </a>
        </li>
        <li id="getOutputLi">
            <a href="#getOutputList" data-toggle="tab" onclick="searchData('getOutput')">
                ${msg['statistic.itemChange.output']}
            </a>
        </li>
    </ul>
    <div id="msg"></div>
    <div class="tab-content">
        <div class="tab-pane active" id="getConsumeAndOutputList"></div>
        <div class="tab-pane" id="getConsumeList"></div>
        <div class="tab-pane" id="getOutputList"></div>
    </div>
    <div id="info"></div>
</div>
<jsp:include page="../commonmodal.jsp"/>
<script type="text/javascript">
    // 初始化一个图表实例
    //var myChart = echarts.init(document.getElementById("level"));
    // echarts把复杂的图表结构化，图表的基本结构包括以下部分：标题，x轴，y轴，数值序列。
    var option = {
        title: {
            text: "${msg['money.echarts.maintitle']}",
            subtext: "${msg['money.echarts.subhead']}"
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ["${msg['money.echarts.output']}", "${msg['money.echarts.consume']}"]
        },
        //工具箱，每个图表最多仅有一个工具箱
        toolbox: {
            //显示策略，可选为：true（显示） | false（隐藏），默认值为false
            show: true,
            //启用功能，目前支持feature，工具箱自定义功能回调处理
            feature: {
                //辅助线标志
                mark: {
                    show: true
                },
                //dataZoom，框选区域缩放，自动与存在的dataZoom控件同步，分别是启用，缩放后退
                dataZoom: {
                    show: true,
                    title: {
                        dataZoom: "${msg['money.echarts.zonezoom']}",
                        dataZoomReset: "${msg['money.echarts.zonezoomback']}"
                    }
                },
                //数据视图，打开数据视图，可设置更多属性,readOnly 默认数据视图为只读(即值为true)，可指定readOnly为false打开编辑功能
                dataView: {
                    show: true,
                    readOnly: true
                },
                //magicType，动态类型切换，支持直角系下的折线图、柱状图、堆积、平铺转换
                magicType: {
                    show: true,
                    type: ['line', 'bar']
                },
                //restore，还原，复位原始图表
                restore: {
                    show: true
                },
                //saveAsImage，保存图片（IE8-不支持）,图片类型默认为'png'
                saveAsImage: {
                    show: true
                }
            }
        },
        calculable: true,
        xAxis: [
            {
                name: "${msg['money.echarts.time']}",
                type: 'category',
                boundaryGap: false,
                data: []
            }
        ],
        yAxis: [
            {
                name: "${msg['money.echarts.count']}",
                type: 'value',
                splitArea: {
                    show: true
                },
                axisLabel: {
                    formatter: "{value}"
                }
            }
        ],
        series: [{
            name: "${msg['money.echarts.output']}",
            type: 'line',
            data: [],
            markPoint: {
                data: [
                    {type: 'max', name: "${msg['money.echarts.max']}"},
                    {type: 'min', name: "${msg['money.echarts.min']}"}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: "${msg['money.echarts.average']}"}
                ]
            }
        },
            {
                name: "${msg['money.echarts.consume']}",
                type: 'line',
                data: [],
                markPoint: {
                    data: [
                        {type: 'max', name: "${msg['money.echarts.max']}"},
                        {type: 'min', name: "${msg['money.echarts.min']}"}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: "${msg['money.echarts.average']}"}
                    ]
                }
            }]
    };
    // 初始化一个图表实例
    /* var myChart2 = echarts.init(document.getElementById("getConsumeList")); */
    // echarts把复杂的图表结构化，图表的基本结构包括以下部分：标题，x轴，y轴，数值序列。
    var option2 = {
        title: {
            text: "${msg['reason.echarts.maintitle']}",
            x: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            x: 'left',
            data: []
        },
        toolbox: {
            show: true,
            feature: {
                mark: {
                    show: true
                },
                dataView: {
                    show: true,
                    readOnly: false
                },
                magicType: {
                    show: true,
                    type: ['pie', 'funnel'],
                    option: {
                        funnel: {
                            x: '25%',
                            width: '50%',
                            funnelAlign: 'left',
                            max: 1548
                        }
                    }
                },
                restore: {
                    show: true
                },
                saveAsImage: {
                    show: true
                }
            }
        },
        calculable: true,
        series: [{
            name: "${msg['reason.echarts.reason']}",
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            itemStyle: {
                normal: {
                    label: {
                        show: true,
                        formatter: '{b} : {c} ({d}%)'
                    },
                    labelLine: {
                        show: true
                    }
                }
            },
            data: [
                /* {value:50, name:'法师'},
                {value:60, name:'战士'} */
            ]
        }]
    };
    // 为echarts对象加载数据
    //myChart.setOption(option);
</script>
</body>
</html>