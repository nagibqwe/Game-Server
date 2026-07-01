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
        statisticData("bindGoldi", "bindGoldInfo");
        $("#bindGoldi").addClass("active");
        $("#bindGolds").removeClass("active");
        $("#bindGoldInfo").addClass("active");
        $("#bindGoldStatis").removeClass("active");
    }

    function statisticData(id, actionType) {
        if ($("input[name='serverId']:checked").length == 0) {
            alert("${msg['statistic.public.syspromt.server']}");
            return;
        }
        exId = actionType;
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            url: base + "/bindgold/" + actionType,
            data: $("#query_form").serialize(),
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                if (data == null) {
                    return;
                }
                if (id == "bindGoldi") {
                    countHtml = bindGoldInfo(data);
                } else if (id == "bindGolds") {
                    countHtml = bindGoldStatis(data);
                }
            }
        });
    }

    function bindGoldInfo(dataObj) {
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.bindgold.sid']}</td>";
        contentHtml += "<td>${msg['statistic.bindgold.itemModelId']}</td>";
        contentHtml += "<td>${msg['statistic.bindgold.itemName']}</td>";
        contentHtml += "<td>${msg['statistic.bindgold.userCount']}</td>";
        contentHtml += "<td>${msg['statistic.bindgold.itemCount']}</td>";
        contentHtml += "<td>${msg['statistic.bindgold.itemSum']}</td>";
        contentHtml += "</tr>";
        for (var key in dataObj) {
            contentHtml += "<tr align='center'>";
            contentHtml += "<td>" + dataObj[key].sid + "</td>";
            contentHtml += "<td>" + dataObj[key].itemModelId + "</td>";
            contentHtml += "<td>" + dataObj[key].itemName + "</td>";
            contentHtml += "<td>" + dataObj[key].userCount + "</td>";
            contentHtml += "<td>" + dataObj[key].itemCount + "</td>";
            contentHtml += "<td>" + dataObj[key].itemSum + "</td>";
            contentHtml += "</tr>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        $("#bindGoldInfo").html(contentHtml);
    }

    function bindGoldStatis(dataObj) {
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.public.date']}</td>";
        contentHtml += "<td>${msg['statistic.bindgold.sid']}</td>";
        contentHtml += "<td>${msg['statistic.bindgold.payCount']}</td>";
        contentHtml += "<td>${msg['statistic.bindgold.itemCount']}</td>";
        contentHtml += "<td>${msg['statistic.bindgold.itemSum']}</td>";
        contentHtml += "</tr>";
        for (var i = 0; i < dataObj.length; i++) {
            contentHtml += "<tr align='center'>";
            contentHtml += "<td>" + dataObj[i].rtime + "</td>";
            contentHtml += "<td>" + dataObj[i].sid + "</td>";
            contentHtml += "<td>" + dataObj[i].userCount + "</td>";
            contentHtml += "<td>" + dataObj[i].timeSum + "</td>";
            contentHtml += "<td>" + dataObj[i].moneySum + "</td>";
            contentHtml += "</tr>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        $("#bindGoldStatis").html(contentHtml);
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
        <select id="select_group" name="groupName"
                onchange="getServer(this.value);getChannelName(this.value);"></select>
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

        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary"
               onclick="search();">
        <input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary"
               onclick="exportExcel();"><br/><br/>
    </form>
    <p id="msg"></p>
    <ul class="nav nav-tabs" id="myTab">
        <li class="active" id="bindGoldi">
            <a href="#bindGoldInfo" data-toggle="tab" onclick="statisticData('bindGoldi','bindGoldInfo');">
                ${msg['statistic.bindgold.bindgoldUsage']}
            </a>
        </li>
        <li id="bindGolds">
            <a href="#bindGoldStatis" data-toggle="tab" onclick="statisticData('bindGolds','bindGoldStatis');">
                ${msg['statistic.bindgold.bindgoldSummar']}
            </a>
        </li>
    </ul>
</div>

<div class="container-fluid">
    <div class="tab-content">
        <div class="tab-pane active" id="bindGoldInfo"></div>
        <div class="tab-pane" id="bindGoldStatis"></div>
    </div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>
