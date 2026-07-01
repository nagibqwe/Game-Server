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
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
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
    var exId = "";

    function search() {
        searchData("getAllRechargeOverview");
    }

    function searchData(actionType) {
        if ($("input[name='serverId']:checked").length == 0) {
            alert("${msg['statistic.public.syspromt.server']}");
            return;
        }
        exId = actionType + "List";
        var startDate = $("#start input").val();
        var endDate = $("#end input").val();
        if (checkDate(startDate, endDate)) {
            $("#online").show();
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.ajax({
                type: "POST",
                url: base + "/rechargeOverview/" + actionType,
                data: $("#query_form").serialize(),
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    $("#loadingmodal").modal('hide');
                    if (data == null) {
                        return;
                    }
                    setAllRechargeOverviewData(data);

                }
            });
        }
    }

    function setAllRechargeOverviewData(data) {
        var tableHtml = "";
        tableHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        tableHtml += "<thead>";
        tableHtml += "<tr align='center' style='font-weight: bolder;'>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.date']}</th>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.totalPayment']}</th>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.DAU']}</th>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.newUsers']}</th>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.rechargeUsers']}</th>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.newRechargeUsers']}</th>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.oldRechargeUsers']}</th>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.newrechargeRate']}</th>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.oldrechargeRate']}</th>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.rechargeRate']}</th>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.ARPU']}</th>";
        tableHtml += "<th>${msg['jsp.rechargeOverview.ARPPU']}</th>";
        tableHtml += "</tr>";
        tableHtml += "</thead>";
        for (var key in data) {
            tableHtml += "<tr align='center'>";
            tableHtml += "<td>" + data[key].date + "</td>";
            tableHtml += "<td>" + data[key].totalPayment + "</td>";
            tableHtml += "<td>" + data[key].DAU + "</td>";
            tableHtml += "<td>" + data[key].newUsers + "</td>";
            tableHtml += "<td>" + data[key].rechargeUsers + "</td>";
            tableHtml += "<td>" + data[key].newRechargeUsers + "</td>";
            tableHtml += "<td>" + data[key].oldRechargeUsers + "</td>";
            tableHtml += "<td>" + data[key].newrechargeRate + "</td>";
            tableHtml += "<td>" + data[key].oldrechargeRate + "</td>";
            tableHtml += "<td>" + data[key].rechargeRate + "</td>";
            tableHtml += "<td>" + data[key].ARPU + "</td>";
            tableHtml += "<td>" + data[key].ARPPU + "</td>";
            tableHtml += "</tr>";
        }
        tableHtml += "</table>";
        $("#getAllRechargeOverviewList").html(tableHtml);
    }

    function checkDate(startDate, endDate) {
        if (startDate == "" || endDate == "") {
            alert("${msg['jsp.log.pleasechoosedate']}");
            return false;
        }
        return true;
    }

    function exportExcel() {
        $("#" + exId + " table").eq(0).tableExport({type: 'excel', escape: 'false'});
    }

</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <label class="label">${msg['statistic.public.platform']}</label>&nbsp;&nbsp;
        <select id="select_group" name="groupName" onchange="getServer(this.value);getChannelName(this.value);"></select>
        <br/><br/>

        <label class="label">${msg['statistic.public.server']}</label>&nbsp;&nbsp;
        <span id="checkbox_server"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.Channel']}</label>&nbsp;&nbsp;
        <span id="checkbox_channel"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.time']}</label>&nbsp;&nbsp;
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="startDate" size="20" value="${newDate}" type="text"
                   readonly> <span class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endDate" size="20" value="${newDate}" type="text" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <br/><br/>

        <label class="label" for="isblack">${msg['statistic.public.isblack']}</label>
        <input type="checkbox" id="isblack" name="isblack"/>&nbsp;&nbsp;
        <br/><br/>

        <input type="button" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()"/>
        <input type="button" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel()"/>
    </form>
    <div id="msg"></div>
    <div class="tab-content">
        <div class="tab-pane active" id="getAllRechargeOverviewList"></div>
    </div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>