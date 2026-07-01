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
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script src="${base}/js/echarts/echarts.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        getGroup();
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
    });

    function searchData() {
        if(!$("#query_form").validationEngine('validate')){
            return;
        }
        if ($("input[name='serverId']:checked").length == 0) {
            alert("${msg['statistic.public.syspromt.server']}");
            return;
        }
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            url: base + "/dauStatistic/getDAU",
            data: $("#query_form").serialize(),
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                if (!data.ok) {
                    alert(data.msg);
                    return;
                }
                setDauData(data.data);
            }
        });
        $("#syspromtinfo").html("");
    }

    function setDauData(dataObj) {
        var contentHtml = "";
        contentHtml += "<table class='table table-bordered table-striped' border='1' cellspacing='0'>";
        contentHtml += "<tbody>";
        contentHtml += "<tr align='center' style='font-weight: bolder;'>";
        contentHtml += "<td>${msg['statistic.dau.date']}</td>";
        contentHtml += "<td>${msg['statistic.dau.DAU']}</td>";
        contentHtml += "</tr>";
        for (var key in dataObj) {
            contentHtml += "<tr align='center'>";
            contentHtml += "<td>" + key + "</td>";
            contentHtml += "<td>" + dataObj[key] + "</td>";
            contentHtml += "</tr>";
        }
        contentHtml += "</tbody>";
        contentHtml += "</table>";
        $("#getDauList").html(contentHtml);
    }

    function exportExcel() {
        $("#getDAUList table").eq(0).tableExport({type: 'excel', escape: 'false'});
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <label class="label" for="select_group">${msg['statistic.public.platform']}</label>
        <select id="select_group" onchange="getServer(this.value);getChannelName(this.value);"></select>
        <br/><br/>

        <label class="label">${msg['statistic.public.server']}</label>
        <span id="checkbox_server"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.Channel']}</label>
        <span id="checkbox_channel"></span>
        <br/><br/>

        <label class="label">${msg['statistic.public.time']}</label>
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="startDate" size="20" value="${nowDate}" type="text" readonly><span class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endDate" size="20" value="${nowDate}" type="text" readonly><span class="add-on"><i class="icon-th"></i></span>
        </div>
        <br/><br/>

        <label class="label" for="level">${msg['statistic.dau.level']}</label>
        <input id="level" name="level" size="20" type="text" class="span2 validate[custom[integer],min[1]]"/>
        <label class="label" for="isBlackList">${msg['statistic.public.isblack']}</label>
        <input type="checkbox" id="isBlackList"/>
        <br/><br/>

        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="searchData()">
        <input type="button" value="${msg['statistic.public.exportExcel']}" class="btn btn-primary" onclick="exportExcel()"><br/><br/>
    </form>
    <div id="msg"></div>
    <ul class="nav nav-tabs" id="myTab">
        <li class="active" id="getDauLi">
            <a href="#getDauList" data-toggle="tab">
                ${msg['statistic.dau.titile']}
            </a>
        </li>
    </ul>
</div>
<div class="container-fluid">
    <div class="tab-content">
        <div class="tab-pane active" id="getDauList"></div>
    </div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>

