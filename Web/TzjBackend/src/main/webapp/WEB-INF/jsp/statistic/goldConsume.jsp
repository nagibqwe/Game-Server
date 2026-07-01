<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

    function search() {
        isSearch = 1;
        statisticData("gold", "goldConsume");
    }

    function statisticData(id, actionType) {
        if ($("input[name='serverId']:checked").length == 0) {
            alert("${msg['statistic.public.syspromt.server']}");
            return;
        }
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            url: base + "/goldpurstatistic/" + actionType,
            data: $("#query_form").serialize(),
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                if (!data.ok) {
                    $("#goldConsume").html(data.msg);
                    return;
                }
                if (id == "gold") {
                    horseAdanceStatis(data.data);
                }
            }
        });
    }

    function horseAdanceStatis(dataObj) {
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.goldconsume.reason']}</td>";
        contentHtml += "<td>${msg['statistic.goldconsume.users']}</td>";
        contentHtml += "<td>${msg['statistic.goldconsume.totalConsume']}</td>";
        contentHtml += "<td>${msg['statistic.goldconsume.sid']}</td>";
        contentHtml += "</tr>";
        for (var i = 0; i < dataObj.length; i++) {
            contentHtml += "<tr align='center'>";
            contentHtml += "<td>" + dataObj[i].reason + "</td>";
            contentHtml += "<td>" + dataObj[i].users + "</td>";
            contentHtml += "<td>" + dataObj[i].totalConsume + "</td>";
            contentHtml += "<td>" + dataObj[i].sid + "</td>";
            contentHtml += "</tr>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        $("#goldConsume").html(contentHtml);
    }

    function exportExcel() {
        $("#goldConsume table").eq(0).tableExport({type: 'excel', escape: 'false'});
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <label class="label" for="select_group">${msg['statistic.public.platform']}</label>&nbsp;&nbsp;
        <select id="select_group" onchange="getServer(this.value);getChannelName(this.value);" class="span3"></select>
        <br/><br/>

        <label class="label">${msg['statistic.public.server']}</label>&nbsp;&nbsp;
        <span id="checkbox_server"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.Channel']}</label>&nbsp;&nbsp;
        <span id="checkbox_channel"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.time']}</label>&nbsp;&nbsp;
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

        <label class="label" for="goldType">${msg['statistic.public.prop']}</label>&nbsp;&nbsp;
        <select id="goldType" class="span3">
            <option value="0" selected="selected">${msg['statistic.goldconsume.gold']}</option>
            <option value="1">${msg['statistic.goldconsume.bindgold']}</option>
        </select>
        <br/><br/>

        <label class="label" for="isBlackList">${msg['statistic.public.isblack']}</label>
        <input type="checkbox" id="isBlackList"/>&nbsp;&nbsp;
        <br/><br/>

        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
        <input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel()"><br/><br/>
    </form>
    <p id="msg"></p>
    <ul class="nav nav-tabs" id="myTab">
        <li class="active" id="consume">
            <a href="#goldConsume" data-toggle="tab" onclick="statisticData('gold','goldConsume');">
                ${msg['statistic.goldconsume.pagetitle']}
            </a>
        </li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="goldConsume"></div>
    </div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>

