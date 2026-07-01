<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['statistic.playerLtv.pagetitle']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.pagination.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        reloadNoHefuServerGroupBox(false);
        $("#start").datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm-dd hh:ii:00',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 0,
            showMeridian: 1
        });
        $("#end").datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm-dd hh:ii:00',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 0,
            showMeridian: 1
        });
        $("#loading").shCircleLoader();
        $("#pagerDiv").css("display", "none");
    });

    function search() {
        var serverIds = [];
        $("input[name='serverId']:checked").each(function () {
            serverIds.push($(this).val());
        });
        var startDate = $("input[name='startDate']").val();
        var endDate = $("input[name='endDate']").val();
        if (startDate == "" || endDate == "") {
            $("#syspromtinfo").html("${msg['statistic.public.syspromt.time']}");
            return;
        }
        if (serverIds.length == 0) {
            $("#syspromtinfo").html("${msg['statistic.public.syspromt.server']}");
            return;
        }
        //加载
        $("#loadingmodal").modal({backdrop: "static", keyboard: false});
        $.ajax({
            url: base + "/recharge/rechargeShow",
            data: {
                serverIds: serverIds + "",
                startDate: startDate,
                endDate: endDate
            },
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                $("#syspromtinfo").empty();
                data = data.data;
                var contentHtml = "<table class='table table-bordered table-striped'>";
                contentHtml += "<tbody>";
                contentHtml += "<tr align='center' style='font-weight: bolder;'>";
                contentHtml += "<td>${msg['jsp.recharge.serverId']}</td>";
                contentHtml += "<td>${msg['jsp.recharge.roleId']}</td>";
                contentHtml += "<td>${msg['jsp.recharge.rechargeGold']}</td>";
                contentHtml += "<td>${msg['jsp.recharge.createUser']}</td>";
                contentHtml += "<td>${msg['jsp.recharge.createTime']}</td>";
                contentHtml += "<td>${msg['jsp.recharge.rechargeState']}</td>";
                contentHtml += "<td>${msg['jsp.recharge.rechargeReason']}</td>";
                contentHtml += "<td>${msg['jsp.recharge.approvalUser']}</td>";
                contentHtml += "<td>${msg['jsp.recharge.approvalTime']}</td>";
                contentHtml += "</tr>";
                for (var i = 0; i < data.length; i++) {
                    contentHtml += "<tr align='center'>";
                    contentHtml += "<td>" + data[i].serverId + "</td>";
                    contentHtml += "<td>" + data[i].roleId + "</td>";
                    contentHtml += "<td>" + data[i].rechargeNumber + "</td>";
                    contentHtml += "<td>" + data[i].createUser + "</td>";
                    contentHtml += "<td>" + data[i].createTime + "</td>";
                    if (data[i].rechargeState == 0) {
                        contentHtml += "<td style='color:orange'>${msg['jsp.recharge.rechargeWait']}</td>";
                    }
                    if (data[i].rechargeState == 1) {
                        contentHtml += "<td style='color:green'>${msg['jsp.recharge.rechargeSuccess']}</td>";
                    }
                    if (data[i].rechargeState == 2) {
                        contentHtml += "<td style='color:red'>${msg['jsp.recharge.rechargeFail']}</td>";
                    }
                    contentHtml += "<td>" + data[i].reason + "</td>";
                    contentHtml += "<td>" + (data[i].approvalUser === undefined ? " " : data[i].approvalUser) + "</td>";
                    contentHtml += "<td>" + (data[i].approvalTime === undefined ? " " : data[i].approvalTime)  + "</td>";
                    contentHtml += "</tr>";
                }
                contentHtml += "</tbody>";
                contentHtml += "</table>";
                $("#getRecharge").html(contentHtml);
            }
        });
    }

    function exportExcel() {
        $("#getRecharge table").eq(0).tableExport({type: 'excel', escape: 'false'});
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <label class="label" for="select_group">${msg['statistic.public.platform']}</label>
        <select id="select_group" onchange="reloadNoHefuServerBox(this.value, false)" class="span3"></select>
        <br/><br/>

        <label class="label">${msg['statistic.public.server']}</label>
        <div id="checkbox_server"></div>
        <br/>

        <label class="label">${msg['statistic.public.time']}</label>
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="startDate" size="20" value="${nowDate}" type="text" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endDate" size="20" value="${nowDate}" type="text" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <br/><br/>

        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
        <br/><br/>

        <input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel()">
        <span id="syspromtinfo" style="color: red;"></span>
    </form>

    <div id="msg"></div>
    <ul class="nav nav-tabs" id="myTab">
        <li class="record active" id="ltv"><a href="#payRecord" data-toggle="tab">${msg['jsp.recharge.recharge']}</a></li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="getRecharge"></div>
    </div>
</div>
</body>
</html>