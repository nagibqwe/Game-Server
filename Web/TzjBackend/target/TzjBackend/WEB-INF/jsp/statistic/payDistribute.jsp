<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['statistic.payDistribute.pagetitle']}</title>
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
<script src="${base}/js/echarts/echarts.js"></script>
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
    var isSearch = 0;
    var expId = "";
    var idLists = ["day", "payDay", "level", "payLevel"];

    var panelIdList = [];//主要是为了避免用户在使用时，又去请求服务器
    function search() {
        panelIdList = [];//设置成默认
        isSearch = 1;
        var initId1 = "level";
        var initId2 = "payLevel";
        statisticData(initId1, initId2, "payLevelStatistic", getPayLevelContentHtml);
        //设置选项卡的显示
        pannelShow(initId1, initId2, idLists);
    }

    function statisticData(id1, id2, actionType, target) {
        if ($("input[name='serverId']:checked").length == 0) {
            alert("${msg['statistic.public.syspromt.server']}");
            return;
        }
        expId = id2;
        //首先判断结束时间是否需要隐藏
        if (id1 == "level") {
            $("#end").show();
            $("#toEnd").show();
        } else {
            $("#end").hide();
            $("#toEnd").hide();
        }
        var isContain = 0;
        for (var i = 0; i < panelIdList.length; i++) {
            if (panelIdList[i] == id2) {
                isContain = 1;
                break;
            }
        }
        if (!isContain) {
            panelIdList.push(id2);
        } else {
            pannelShow(id1, id2, idLists);
            return;
        }
        //加载
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            url: base + "/paydiststatistic/" + actionType,
            data: $("#query_form").serialize(),
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                var countHtml = target(data);
                $("#" + id2).html(countHtml);
                $("#syspromtinfo").html("");
            }
        });
    }

    /**
     *选定时间段内，充值档次以及人数统计
     **/
    function getPayLevelContentHtml(data) {
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.payDistribute.money']}</td>";
        contentHtml += "<td>${msg['statistic.public.playercount']}</td>";
        contentHtml += "</tr>";
        for (var i = 0; i < data.length; i++) {
            contentHtml += "<tr>";
            contentHtml += "<td>" + data[i].amount + "</td>";
            contentHtml += "<td>" + data[i].count + "</td>";
            contentHtml += "</tr>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        return contentHtml;
    }

    /**
     *各个时间段中充值金额以及人数
     **/
    function getPayDayContentHtml(data) {
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.payDistribute.timesection']}</td>";
        contentHtml += "<td>${msg['statistic.payDistribute.money']}</td>";
        contentHtml += "<td>${msg['statistic.public.playercount']}</td>";
        contentHtml += "</tr>";
        for (var i = 0; i < data.length; i++) {
            contentHtml += "<tr>";
            contentHtml += "<td>" + data[i].time + "</td>";
            contentHtml += "<td>" + data[i].amountSum + "</td>";
            contentHtml += "<td>" + data[i].countSum + "</td>";
            contentHtml += "</tr>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        return contentHtml;
    }


    function exportExcel() {
        if (expId == "") {
            return;
        }
        $("#" + expId + " table").eq(0).tableExport({type: 'excel', escape: 'false'});
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <label class="label" for="select_group">${msg['statistic.public.platform']}</label>
        <select id="select_group" name="groupName" onchange="getServer(this.value);getChannelName(this.value);"></select>
        <br/><br/>

        <label class="label">${msg['statistic.public.server']}</label>
        <span id="checkbox_server"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.Channel']}</label>
        <span id="checkbox_channel"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.time']}</label>
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="startDate" size="20" value="${nowDate}" type="text"
                   readonly> <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <span id="toEnd">-</span>
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endDate" size="20" value="${nowDate}" type="text" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <br/><br/>

        <label class="label" for="isblack">${msg['statistic.public.isblack']}</label>
        <input type="checkbox" id="isblack"/>
        <br/><br/>

        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
        <input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel()">
    </form>
    <div id="msg"></div>
</div>

<div class="container-fluid">
    <ul class="nav nav-tabs" id="myTab">
        <li class="active" id="level">
            <a href="#payLevel" data-toggle="tab" onclick="statisticData('level','payLevel','payLevelStatistic',getPayLevelContentHtml);">
                ${msg['statistic.payDistribute.paydis']}
            </a>
        </li>
        <li id="day">
            <a href="#payDay" data-toggle="tab" onclick="statisticData('day','payDay','payDayStatistic',getPayDayContentHtml);">
                ${msg['statistic.payDistribute.paydaymoney']}
            </a>
        </li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="payLevel"></div>
        <div class="tab-pane" id="payDay"></div>
    </div>
</div>
</body>
</html>
