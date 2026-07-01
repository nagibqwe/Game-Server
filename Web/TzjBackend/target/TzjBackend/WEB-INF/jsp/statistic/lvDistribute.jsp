<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['statistic.lvDistribute.pagetitle']}</title>
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
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        group_reload();
        setTimeout(function () {
            var groupName = $("#select_group").val();
            getChannelName(groupName);
        }, 100);
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
    var idLists = ["horse", "horseData"];

    function search() {
        panelIdList = [];//设置成默认
        isSearch = 1;
        var initId1 = "weapon";
        var initId2 = "weaponData";
        statisticData(initId1, initId2, "horseLvStatistic", getHorseLvContentHtml);
        //设置选项卡的显示
        pannelShow(initId1, initId2, idLists);
    }

    function statisticData(id1, id2, actionType, target) {
        expId = id2;
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
            //return;
        }
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            url: base + "/lvdistriStatistic/" + actionType,
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

    function getHorseLvContentHtml(data) {
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.lvDistribute.horselv']}</td>";
        contentHtml += "<td>${msg['statistic.lvDistribute.lvcount']}</td>";
        contentHtml += "</tr>";
        for (var i = 0; i < data.length; i++) {
            contentHtml += "<tr>";
            contentHtml += "<td>" + data[i].beforeHorseLayer + "</td>";
            contentHtml += "<td>" + data[i].lvcount + "</td>";
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
        <label class="label" for="select_group">${msg['statistic.public.platform']}</label>&nbsp;&nbsp;
        <select id="select_group" name="groupName" onchange="queryServerByGroup(this.value);getChannelName(this.value);" class="span2"></select>
        <label class="label" for="select_server">${msg['statistic.public.server']}</label>
        <select id="select_server" name="serverId" class="span2"></select>
        <br/><br/>

        <label class="label">${msg['statistic.public.Channel']}</label>
        <span id="checkbox_channel"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.time']}</label>&nbsp;&nbsp;
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="startDate" size="20" type="text" value="${nowDate}" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endDate" size="20" type="text" value="${nowDate}" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <br/><br/>

        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
        <input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel()">
    </form>

    <div id="msg"></div>
    <ul class="nav nav-tabs" id="myTab">
        <li class="record" id="horse">
            <a href="#horseData" data-toggle="tab" onclick="statisticData('horse','horseData','horseLvStatistic',getHorseLvContentHtml);">
                ${msg['statistic.lvDistribute.horseData']}
            </a>
        </li>
    </ul>

    <div class="tab-content">
        <div class="tab-pane" id="horseData"></div>
    </div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>