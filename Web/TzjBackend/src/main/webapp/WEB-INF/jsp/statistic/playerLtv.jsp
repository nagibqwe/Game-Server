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
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
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

    function search(target) {
        if ($("input[name='serverId']:checked").length == 0) {
            alert("${msg['statistic.public.syspromt.server']}");
            return;
        }
        $("#loadingmodal").modal({backdrop: "static", keyboard: false});
        $.ajax({
            url: base + "/ltvstatistic/ltvDataStatistic",
            data: $("#query_form").serialize(),
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                var ltvHtml = target(data);
                $("#ltvData").html(ltvHtml);
                $("#syspromtinfo").empty();
            }
        });
    }

    function getLtvHtml(data) {
        var ltvDays = data.ltvDays;
        var dataMapList = data.dataMapList;
        var ltvhtml = "";
        ltvhtml += "<table class='table table-bordered table-striped'>";
        ltvhtml += "<tbody>";
        ltvhtml += "<tr align='center' style='font-weight: bolder;'>";
        ltvhtml += "<td><div style=\"width:80px;\">${msg['statistic.public.date']}</div></td>";
        ltvhtml += "<td><div style=\"width:80px;\">${msg['statistic.playerLtv.addnewer']}</div></td>";
        for (var i = 0; i < ltvDays.length; i++) {
            ltvhtml += "<td>LTV" + (ltvDays[i] + 1) + "</td>";
        }
        // for (var key in dataMapList[dataMapList.length - 1].ltvMap) {
        //     if(key.indexOf("1000") != -1){
        //         ltvhtml += "<td>LTV当前" + key + "</td>";
        //     }
        // }


        // ltvhtml += "<td>LTV当前</td>";

        ltvhtml += "</tr>";
        for (var i = 0; i < dataMapList.length; i++) {
            ltvhtml += "<tr>";
            ltvhtml += "<td>" + dataMapList[i].date + "</td>";
            ltvhtml += "<td>" + dataMapList[i].reglength + "</td>";
            for (var key in dataMapList[i].ltvMap) {
                ltvhtml += "<td>" + dataMapList[i].ltvMap[key] + "</td>";
            }
            ltvhtml += "</tr>";
        }
        ltvhtml += "</tbody>";
        ltvhtml += "</table>";
        return ltvhtml;
    }

    function exportExcel() {
        $("#ltvData table").eq(0).tableExport({type: 'excel', escape: 'false'});
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <label class="label" for="select_group">${msg['statistic.public.platform']}</label>
        <select id="select_group" name="groupName" class="span2" onchange="getServer(this.value);getChannelName(this.value);"></select>
        <br/><br/>

        <label class="label">${msg['statistic.public.server']}</label>
        <span id="checkbox_server"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.Channel']}</label>
        <span id="checkbox_channel"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.time']}</label>
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="startDate" size="20" value="${newDate}" type="text" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endDate" size="20" value="${newDate}" type="text" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <br/><br/>

        <label class="label" for="isblack">${msg['statistic.public.isblack']}</label>
        <input type="checkbox" id="isblack"/>
        <br/><br/>

        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search(getLtvHtml)">
        <input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel()">
    </form>
    <div id="msg"></div>
</div>

<div class="container-fluid">
    <ul class="nav nav-tabs" id="myTab">
        <li class="record active" id="ltv"><a href="#payRecord" data-toggle="tab">${msg['statistic.playerLtv.ltvs']}</a></li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="ltvData"></div>
    </div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>