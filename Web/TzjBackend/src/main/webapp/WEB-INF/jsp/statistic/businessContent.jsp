<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['statistic.bindgold.pagetitle']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript"
        src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
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
        statisticData("businessContent", "businessContentInfo");
        $("#businessContent").addClass("active");
        $("#businessContents").removeClass("active");
        $("#businessContentInfo").addClass("active");
        $("#businessContentStatis").removeClass("active");
    }

    function statisticData(id, actionType) {
        if ($("input[name='serverId']:checked").length == 0) {
            alert("${msg['statistic.public.syspromt.server']}");
            return;
        }
        exId = actionType;
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            url: base + "/businessContent/" + actionType,
            data: $("#query_form").serialize(),
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                if (data == null) {
                    return;
                }
                if (id == "businessContent") {
                    countHtml = businessContentInfo(data);
                } else if (id == "businessContents") {
                    countHtml = businessContentStatis(data);
                }
            }
        });
        $("#syspromtinfo").html("");
    }

    function businessContentInfo(dataObj) {
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.businessContent.date']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.one']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.two']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.three']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.four']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.five']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.six']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.seven']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.eight']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.nine']}</td>";
        contentHtml += "</tr>";
        for (var key in dataObj) {
            contentHtml += "<tr align='center'>";
            contentHtml += "<td>" + key + "</td>";
            contentHtml += "<td>" + dataObj[key].one + "</td>";
            contentHtml += "<td>" + dataObj[key].two + "</td>";
            contentHtml += "<td>" + dataObj[key].three + "</td>";
            contentHtml += "<td>" + dataObj[key].four + "</td>";
            contentHtml += "<td>" + dataObj[key].five + "</td>";
            contentHtml += "<td>" + dataObj[key].six + "</td>";
            contentHtml += "<td>" + dataObj[key].seven + "</td>";
            contentHtml += "<td>" + dataObj[key].eight + "</td>";
            contentHtml += "<td>" + dataObj[key].nine + "</td>";
            contentHtml += "</tr>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        $("#businessContentInfo").html(contentHtml);
    }

    function businessContentStatis(dataObj) {
        var daygift = dataObj["daygift"];
        var dailyrecharge = dataObj["dailyrecharge"];
        var contentHtml = "";
        contentHtml += "<h4>${msg['statistic.businessContent.dayGift']}</h4>";
        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td></td>";
        contentHtml += "<td>${msg['statistic.businessContent.buyRoleNumber']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.continuityRoleNumber']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.continuityThree']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.continuitySeven']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.continuityFourteen']}</td>";
        contentHtml += "</tr>";
        for (var key in daygift) {
            contentHtml += "<tr align='center'>";
            if ("com.yzry.1.day" == key) {
                contentHtml += "<td>${msg['statistic.businessContent.oneGift']}</td>";
            }
            if ("com.yzry.3.day" == key) {
                contentHtml += "<td>${msg['statistic.businessContent.threeGift']}</td>";
            }
            if ("com.yzry.8.day" == key) {
                contentHtml += "<td>${msg['statistic.businessContent.eightGift']}</td>";
            }
            contentHtml += "<td>" + daygift[key].one + "</td>";
            contentHtml += "<td>" + daygift[key].two + "</td>";
            contentHtml += "<td>" + daygift[key].three + "</td>";
            contentHtml += "<td>" + daygift[key].seven + "</td>";
            contentHtml += "<td>" + daygift[key].fourteen + "</td>";
            contentHtml += "</tr>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table><br/><br/>";
        contentHtml += "<h4>${msg['statistic.businessContent.dailyRecharge']}</h4>";
        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td></td>";
        contentHtml += "<td>${msg['statistic.businessContent.buyRoleNumber']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.continuityRoleNumber']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.continuityOne']}</td>";
        contentHtml += "<td>${msg['statistic.businessContent.continuityTwo']}</td>";
        contentHtml += "</tr>";
        for (var key in dailyrecharge) {
            contentHtml += "<tr align='center'>";
            if ("1" == key) {
                contentHtml += "<td>${msg['statistic.businessContent.dailyRecharge60']}</td>";
            }
            if ("2" == key) {
                contentHtml += "<td>${msg['statistic.businessContent.dailyRecharge300']}</td>";
            }
            if ("3" == key) {
                contentHtml += "<td>${msg['statistic.businessContent.dailyRecharge680']}</td>";
            }
            contentHtml += "<td>" + dailyrecharge[key].one + "</td>";
            contentHtml += "<td>" + dailyrecharge[key].two + "</td>";
            contentHtml += "<td>" + dailyrecharge[key].three + "</td>";
            contentHtml += "<td>" + dailyrecharge[key].four + "</td>";
            contentHtml += "</tr>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        $("#businessContentStatis").html(contentHtml);
    }

    function exportExcel() {
        $("#" + exId + " table").tableExport({type: 'excel', escape: 'false'});
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

        <label class="label">${msg['statistic.public.time']}</label>&nbsp;&nbsp;
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="startDate" size="20" value="${nowDate}" type="text"
                   readonly> <span class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endDate" size="20" value="${nowDate}" type="text" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <br/><br/>

        <label class="label" for="isBlackList">${msg['statistic.public.isblack']}</label>
        <input type="checkbox" id="isBlackList"/>
        <br/><br/>

        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()"/>
        <input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel()"/>
    </form>
    <div id="msg"></div>
    <ul class="nav nav-tabs" id="myTab">
        <li class="active" id="businessContent">
            <a href="#businessContentInfo" data-toggle="tab"
               onclick="statisticData('businessContent','businessContentInfo');">
                ${msg['statistic.businessContent.buyNum']}
            </a>
        </li>
        <li id="businessContents">
            <a href="#businessContentStatis" data-toggle="tab"
               onclick="statisticData('businessContents','businessContentStatis');">
                ${msg['statistic.businessContent.continuityBuy']}
            </a>
        </li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="businessContentInfo"></div>
        <div class="tab-pane" id="businessContentStatis"></div>
    </div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>
