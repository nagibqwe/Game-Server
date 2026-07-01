<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['statistic.goldConsume.pagetitle']}</title>
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
    var exId = "";

    function search() {
        isSearch = 1;
        searchData("getFirstRechargeProject");
        $("#getFirstRechargeProjectLi").addClass("active");
        $("#getFirstRechargeRoleLevelLi").removeClass("active");
        $("#getFirstRechargeOnlineTimeLi").removeClass("active");
        $("#getFirstRechargeProjectList").addClass("active");
        $("#getFirstRechargeRoleLevelList").removeClass("active");
        $("#getFirstRechargeOnlineTimeList").removeClass("active");
    }

    function searchData(actionType) {
        if ($("input[name='serverId']:checked").length == 0) {
            alert("${msg['statistic.public.syspromt.server']}");
            return;
        }
        exId = actionType + "List";
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            url: base + "/firstRecharge/" + actionType,
            data: $("#query_form").serialize(),
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                if (data == null) {
                    return;
                }
                if (actionType == "getFirstRechargeProject") {
                    setFirstRechargeProjectData(data);
                } else if (actionType == "getFirstRechargeRoleLevel") {
                    setFirstRechargeRoleLevelData(data);
                } else if (actionType == "getFirstRechargeOnlineTime") {
                    setFirstRechargeOnlineTimeData(data);
                }
            }
        });
    }

    function setFirstRechargeProjectData(dataObj) {
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.firstRecharge.date']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeRoles']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.proportion']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.smallRolesProportion']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeAmount6']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeAmount30']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeAmount68']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeAmount98']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeAmount128']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeAmount188']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeAmount258']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeAmount328']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeAmount648']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeisMoon1']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.firstRechargeisMoon2']}</td>";
        contentHtml += "</tr>";
        for (var i = 0; i < dataObj.length; i++) {
            contentHtml += "<tr align='center'>";
            contentHtml += "<td>" + dataObj[i].date + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeRoles + "</td>";
            contentHtml += "<td>" + dataObj[i].proportion + "</td>";
            contentHtml += "<td>" + dataObj[i].smallRolesProportion + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeAmount6 + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeAmount30 + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeAmount68 + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeAmount98 + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeAmount128 + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeAmount188 + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeAmount258 + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeAmount328 + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeAmount648 + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeisMoon1 + "</td>";
            contentHtml += "<td>" + dataObj[i].firstRechargeisMoon2 + "</td>";
            contentHtml += "</tr>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        $("#getFirstRechargeProjectList").html(contentHtml);
    }

    function setFirstRechargeRoleLevelData(dataObj) {
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.firstRecharge.roleLevel']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.roleCounts']}</td>";
        contentHtml += "</tr>";
        for (var key in dataObj) {
            contentHtml += "<tr align='center'>";
            contentHtml += "<td>" + key + "</td>";
            contentHtml += "<td>" + dataObj[key] + "</td>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        $("#getFirstRechargeRoleLevelList").html(contentHtml);
    }

    function setFirstRechargeOnlineTimeData(dataObj) {
        console.log(dataObj);
        var channelResMap = dataObj[0];
        var serverResMap = dataObj[1];
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.firstRecharge.time']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.channelRoleCounts']}</td>";
        contentHtml += "</tr>";
        for (var key in channelResMap) {
            contentHtml += "<tr align='center'>";
            contentHtml += "<td>" + key + "</td>";
            contentHtml += "<td>" + channelResMap[key] + "</td>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table><br/><br/>";

        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.firstRecharge.time']}</td>";
        contentHtml += "<td>${msg['statistic.firstRecharge.serverRoleCounts']}</td>";
        contentHtml += "</tr>";
        for (var key in serverResMap) {
            contentHtml += "<tr align='center'>";
            contentHtml += "<td>" + key + "</td>";
            contentHtml += "<td>" + serverResMap[key] + "</td>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        $("#getFirstRechargeOnlineTimeList").html(contentHtml);
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

        <label class="label" for="isBlackList">${msg['statistic.public.isblack']}</label>
        <input type="checkbox" id="isBlackList"/>&nbsp;&nbsp;
        <br/><br/>

        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()"/>
        <input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel()"/>
    </form>

    <div id="msg"></div>
    <ul class="nav nav-tabs" id="myTab">
        <li class="active" id="getFirstRechargeProjectLi">
            <a href="#getFirstRechargeProjectList" data-toggle="tab" onclick="searchData('getFirstRechargeProject')">
                ${msg['statistic.firstRecharge.project']}
            </a>
        </li>
        <li id="getFirstRechargeRoleLevelLi">
            <a href="#getFirstRechargeRoleLevelList" data-toggle="tab" onclick="searchData('getFirstRechargeRoleLevel')">
                ${msg['statistic.firstRecharge.roleLevel']}
            </a>
        </li>
        <li id="getFirstRechargeOnlineTimeLi">
            <a href="#getFirstRechargeOnlineTimeList" data-toggle="tab" onclick="searchData('getFirstRechargeOnlineTime')">
                ${msg['statistic.firstRecharge.rechargeOnlineTime']}
            </a>
        </li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="getFirstRechargeProjectList"></div>
        <div class="tab-pane" id="getFirstRechargeRoleLevelList"></div>
        <div class="tab-pane" id="getFirstRechargeOnlineTimeList"></div>
    </div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>

