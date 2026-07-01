<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.recharge.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.pagination.js"></script>
<script>
    var base = '${base}';
    $(function () {
        loadRecharge(1);
    });

    function sendOk(id, send) {
        var message = send ? "确认发送吗？" : "确认不发送吗？";
        if (confirm(message)) {
            $.ajax({
                type: "POST",
                url: base + "/recharge/rechargeShenHeResult",
                data: {
                    id: id,
                    send: send
                },
                dataType: "json",
                success: function (data) {
                    alert(data.msg);
                    if (data.ok) {
                        loadRecharge(1);
                    }
                }
            });
        }
    }

    function loadRecharge(currentPage) {
        var pageSize = 15;
        $.ajax({
            type: "POST",
            url: base + "/recharge/list",
            data: {
                pageSize: pageSize,
                pageIndex: currentPage
            },
            dataType: "json",
            success: function (data) {
                if (data.ok) {
                    rechargeShenHeShow(data.list);
                    setDataCounts(list.count);
                }
            }
        });
    }

    function rechargeShenHeShow(data) {
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['jsp.recharge.serverId']}</td>";
        contentHtml += "<td>${msg['jsp.recharge.roleId']}</td>";
        contentHtml += "<td>${msg['jsp.recharge.rechargeGold']}</td>";
        contentHtml += "<td>${msg['jsp.recharge.createUser']}</td>";
        contentHtml += "<td>${msg['jsp.recharge.createTime']}</td>";
        contentHtml += "<td>${msg['jsp.recharge.rechargeState']}</td>";
        contentHtml += "<td>${msg['jsp.recharge.rechargeReason']}</td>";
        contentHtml += "<td colspan='2'>${msg['jsp.recharge.rechargeSend']}</td>";

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
            contentHtml += "<td><button class='btn btn-small btn-success' type='button' onclick='sendOk(" + data[i].id + ", true)'>${msg['jsp.recharge.trueSend']}</button></td>";
            contentHtml += "<td><button class='btn btn-small btn btn-warning' type='button' onclick='sendOk(" + data[i].id + ", false)'>${msg['jsp.recharge.falseSend']}</button></td>";
            contentHtml += "</tr>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        $("#recharge").html(contentHtml);
    }

    //显示一共多少条
    function setDataCounts(pageSize, data) {
        var recordCounts = 0;
        var recordCount = 0;
        recordCounts += parseInt(data);
        if (parseInt(data) >= recordCount) {
            recordCount = parseInt(data);
        }
        $("#recordCount").html(recordCounts);
        $("#pagerDiv").css("display", "block");
        $("#pagerDiv").pagination({
            recordCount: recordCount,           //总记录数(取最大，因为存在多个月的数据的情况)
            pageSize: pageSize,                 //一页记录数
            onPageChange: function (pageIndex) {
                loadRecharge(pageIndex, "rechargeShenHe");
            }
        });
    }
</script>
</head>
<body>
<div class="container-fluid">
    <div class="tab-content">
        <div class="tab-pane active" id="recharge"></div>
        <br/>
        <div id="pagerDiv" class="pagerDiv" style="float: right">
            <span name="first" class="disabled" style="cursor: pointer;">首页</span>
            <span name="prev" class="disabled" style="cursor: pointer;">上一页</span>
            <span name="nav" style="cursor: pointer;"><a class="navi"><span style="color: #999">1</span></a></span>
            <span name="next" class="disabled" style="cursor: pointer;">下一页</span>
            <span name="last" class="disabled" style="cursor: pointer;">末页</span>&nbsp;
            ${msg['jsp.log.all']}:<span id="recordCount">0</span> ${msg['jsp.log.record']}
        </div>
    </div>
</div>
</body>
</html>