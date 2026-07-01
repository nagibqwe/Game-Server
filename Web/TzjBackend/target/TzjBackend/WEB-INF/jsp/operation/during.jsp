<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.during.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        getGroup();
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
        $("#loading").shCircleLoader();
    });
    var exId = "";

    function search() {
        searchData("getAllDuringList");
        $("#getAllDuringLi").addClass("active");
        $("#getRechargeDuringLi").removeClass("active");
        $("#getAllDuringList").addClass("active");
        $("#getRechargeDuringList").removeClass("active");
    }

    function searchData(actionType) {
        if ($("input[name='serverId']:checked").length == 0) {
            alert("${msg['statistic.public.syspromt.server']}");
            return;
        }
        $("#actionType").val(actionType);
        var startDate = $("#start input").val();
        var endDate = $("#end input").val();
        if (checkDate(startDate, endDate)) {
            $("#loadingmodal").modal({
                backdrop: 'static',
                keyboard: false
            });
            $.ajax({
                type: "POST",
                url: base + "/operation/getDuring",
                data: $("#query_form").serialize(),
                dataType: "json",
                success: function (data) {
                    $("#loadingmodal").modal('hide');
                    if (!data.ok) {
                        $("#msg").html(data.msg);
                        return;
                    }
                    setAllDuringData(data, actionType);
                }
            });
        }
    }

    function setAllDuringData(data, actionType) {
        var myData = data.myData;
        var durDays = data.durDays;
        var tableHtml = "";
        tableHtml += "<table class='table table-bordered table-striped'>";
        tableHtml += "<thead>";
        tableHtml += "<tr>";
        tableHtml += "<th>${msg['statistic.public.date']}</th>";
        if (actionType == "getAllDuringList") {
            tableHtml += "<th>${msg['jsp.during.newuser']}</th>";
        } else if (actionType == "getRechargeDuringList") {
            tableHtml += "<th>${msg['jsp.during.rechargeuser']}</th>";
        }
        for (var i = 0; i < durDays.length; i++) {
            if(durDays[i] == 1){
                tableHtml += "<th>${msg['jsp.during.onedayduring']}</th>";
            }else{
                tableHtml += "<th>" + (durDays[i] + 1) + "${msg['jsp.during.dayduring']}</th>";
            }
        }
        tableHtml += "</tr>";
        tableHtml += "</thead>";
        for (var i = 0; i < myData.length; i++) {
            tableHtml += "<tr>";
            tableHtml += "<td>" + myData[i].date + "</td>";
            tableHtml += "<td>" + myData[i].registerCount + "</td>";
            for (var j = 0; j < myData[i].rateDataStatistic.length; j++) {
                var rate = myData[i].rateDataStatistic[j] == 0?0:myData[i].rateDataStatistic[j]*100;
                tableHtml += "<td>" + myData[i].countDataStatistic[j] + "(<span style='color:red;'>" + rate.toFixed(2) + "%</span>)</td>";
            }
            tableHtml += "</tr>";
        }
        tableHtml += "</table>";
        if (actionType == "getAllDuringList") {
            $("#getAllDuringList").html(tableHtml);
        } else if (actionType == "getRechargeDuringList") {
            $("#getRechargeDuringList").html(tableHtml);
        }
        exId = actionType;
    }

    function exportExcel() {
        $("#" + exId + " table").eq(0).tableExport({type: 'excel', escape: 'false'});
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <label class="label" for="select_group">${msg['statistic.public.platform']}</label>&nbsp;&nbsp;
        <select id="select_group" name="groupName" onchange="getServer(this.value);getChannelName(this.value);"></select>
        <br/><br/>

        <label class="label" for="checkbox_server">${msg['statistic.public.server']}</label>&nbsp;&nbsp;
        <span id="checkbox_server"></span>
        <br/><br/>

        <label class="label" for="checkbox_channel">${msg['statistic.public.Channel']}</label>&nbsp;&nbsp;
        <span id="checkbox_channel"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.time']}</label>&nbsp;&nbsp;
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="startDate" size="20" value="${newDate}" type="text" readonly> <span
                class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endDate" size="20" value="${newDate}" type="text" readonly><span
                class="add-on"><i class="icon-th"></i></span>
        </div>
        <br/><br/>

        <label class="label">${msg['statistic.public.isblack']}</label>
        <input type="checkbox" id="isblack" name="isblack"/>
        <br/><br/>
        <input type="hidden" id="actionType" name="actionType">

        <input type="button" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()"/>
        <input type="button" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel()"/>
    </form>
    <div id="msg"></div>
    <ul class="nav nav-tabs" id="myTab">
        <li class="active" id="getAllDuringLi">
            <a href="#getAllDuringList" data-toggle="tab" onclick="searchData('getAllDuringList')">
                ${msg['jsp.during.getAllDuringList']}
            </a>
        </li>
        <li id="getRechargeDuringLi">
            <a href="#getRechargeDuringList" data-toggle="tab" onclick="searchData('getRechargeDuringList')">
                ${msg['jsp.during.getRechargeDuringList']}
            </a>
        </li>
    </ul>
</div>
<div class="tab-content container-fluid">
    <div class="tab-pane active" id="getAllDuringList"></div>
    <div class="tab-pane" id="getRechargeDuringList"></div>
</div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>