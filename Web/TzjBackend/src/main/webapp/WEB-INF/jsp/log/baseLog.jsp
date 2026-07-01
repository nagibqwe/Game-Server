<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/log.js"></script>
<script type="text/javascript">
    var base = '${base}';
    var logType = ${logType};
    var conditions = ${conditions};
    var fields = ${fields};
    var crossLogType = ${crossLogType};
    var hasPlatform = false;
    $(function () {
        //1跨服 2公共服
        if (crossLogType == 0) {
            group_reload();
        } else if (crossLogType == 1) {
            reloadCrossGroup(4);
        } else if (crossLogType == 2){
            reloadCrossGroup(3)
        }

        hasPlatform = fields['platformName'] !== undefined;
        if (!hasPlatform) {
            $("#platformDiv").hide();
        }
        if (conditions.length <= 0) {
            $("#conditionDiv").hide();
        } else {
            createCondition(logType, conditions, fields);
        }

        $("#logType").val(logType);
        $("#condition").hide();

        setTimeout(function () {
            getChannelName($("#select_group").val());
        }, 50);


        $(".date").datetimepicker({
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
    });
</script>
</head>
<body>
<div class="container-fluid">
    <form action="${base}/log/exportExcel" method="post" id="query_form" class="well form-inline">
        <div class="row-fluid">
            <select id="select_group" onchange="getChannelName();" class="span2"></select>
            <select id="select_server" name="serverId" class="span2"></select>
            <input type="hidden" id="logType" name="logType"/>

            <!-- 时间段 -->
            <label class="label">${msg['jsp.log.timechoose']}</label>
            <div class="input-append date" id="start">
                <input style="width: 120px;" name="startDate" size="20" type="text" value="${nowDate}" readonly>
                <span class="add-on"><i class="icon-th"></i></span>
            </div>
            -
            <div class="input-append date" id="end">
                <input style="width: 120px;" name="endDate" size="20" type="text" value="${nowDate}" readonly>
                <span class="add-on"><i class="icon-th"></i></span>
            </div>
            <label class="label">${msg['jsp.log.pageSize']}</label>
            <input type="text" name="pageSize" value="1000" class="span1 validate[required,custom[integer],min[1]]"/>
        </div>
        <br/>

        <!-- 渠道列表 -->
        <div id="platformDiv" class="row-fluid">
            <label class="label">${msg['statistic.public.Channel']}</label>
            <div id="checkbox_channel"></div>
            <br/>
        </div>

        <!-- 查询条件 -->
        <div id="conditionDiv" class="row-fluid">
            <label class="label" onclick="showCondition()">${msg['jsp.log.searchcon']}</label>
            <div id="condition"></div>
            <br/>
        </div>

        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();">
        <input type="submit" id="exportAll" value="${msg['jsp.log.exportall']}" class="btn btn-primary">
    </form>

    <div id="msg"></div>
    <div id="data" style="width: 100%"></div>
</div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>