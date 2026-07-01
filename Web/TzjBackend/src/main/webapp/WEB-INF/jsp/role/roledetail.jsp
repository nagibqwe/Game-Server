<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${msg['jsp.role.title']}</title>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
    <link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
    <script type="text/javascript" src="${base}/js/dateFormat.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
    <script type="text/javascript" src="${base}/js/global.js"></script>
    <script type="text/javascript" src="${base}/js/role.js"></script>
    <script type="text/javascript">
        var base = '${base}';
        var role = <%=request.getAttribute("ROLEINFO")%>;
        $(function () {
            if (role == null) {
                group_reload();
            } else {
                chooseGroup(role.groupName, role.serverId);
                $("#select_queryType").val(role.queryType);
                $("#queryString").val(role.queryString);
                role_reload();
            }

            $(".date").datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd hh:ii:ss',
                weekStart: 1,
                todayBtn: 1,
                autoclose: 1,
                todayHighlight: 1,
                startView: 2,
                minView: 0,
                showMeridian: 1
            });
            $(".condition").hide();
            $("#loading").shCircleLoader();
        });
    </script>
    <jsp:include page="roleInfo.jsp"/>
</head>
<body>
<div class="container-fluid">
    <form id="role_query_form" class="well form-inline">
        <select id="select_group" name="groupName" onchange="queryServerByGroup(this.value)" class="span2"></select>
        <select id="select_server" name="serverId" class="span2"></select>
        <select id="select_queryType" name="queryType" style="width: 120px;">
            <option value="1">${msg['jsp.role.rolename']}</option>
            <option value="3">${msg['jsp.role.roleidIn10']}</option>
            <option value="4">${msg['jsp.role.roleidIn36']}</option>
        </select>
        <input type="text" id="queryString" name="queryString" class="span2 validate[required] forbidSubmit">
        <input type="button" id="role_query_btn" value="${msg['jsp.role.search']}" class="btn btn-primary" onclick="role_reload();">
    </form>
    <div id="msg"></div>
    <div id="account_list"></div>
    <div id="role_list"></div>

    <form class="well form-inline">
        <label class="label">${msg['jsp.rolelog.searchDate']}</label>
        <div class="input-append date" id="startdate">
            <input type="text" value="${nowDate}" readonly/>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="enddate">
            <input type="text" value="${nowDate}" readonly/>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <label class="label">显示记录数</label>
        <input type="text" id="pageSize" name="pageSize" value="1000"/>
    </form>

    <!-- 登录和登出日志 -->
    <div id="roleloginlog">
        <label class="label label-info" style="height: 25px; line-height: 25px; font-size: 12px;">
            ${msg['jsp.rolelog.rolelogin']}
            <i class="icon-plus icon-white" onclick="showResult(this, true);"></i>
            <i class="icon-minus icon-white" onclick="showResult(this, false);"></i>
            <i class="icon-search icon-white" onclick="getLog(1, this, null);"></i>
        </label>
        <div class="result"></div>
    </div>

    <!-- 元宝变化日志 -->
    <div id="goldchangelog">
        <label class="label label-info" style="height: 25px; line-height: 25px; font-size: 12px;">
            元宝变化日志
            <i class="icon-plus icon-white" onclick="showResult(this, true);"></i>
            <i class="icon-minus icon-white" onclick="showResult(this, false);"></i>
            <i class="icon-filter icon-white" onclick="showCondition(this);"></i>
            <span class="condition">
                ${msg['jsp.itemchange.reason']}:
                <input type="text" id="reason2" class="input-mini" onkeyup="checkNum(this);"/>
            </span>
            <i class="icon-search icon-white" onclick="getLog(2, this, {'reason':$('#reason2').val()});"></i>
        </label>
        <div class="result"></div>
    </div>

    <!-- 物品变化日志 -->
    <div id="item" style="clear: both;">
        <label class="label label-info" style="height: 25px; line-height: 25px; font-size: 12px;">
            ${msg['jsp.rolelog.itemchange']}
            <i class="icon-plus icon-white" onclick="showResult(this, true);"></i>
            <i class="icon-minus icon-white" onclick="showResult(this, false);"></i>
            <i class="icon-filter icon-white" onclick="showCondition(this);"></i>
            <span class="condition">
                ${msg['jsp.itemchange.modelid']}:
                <input type="text" id="itemId" class="input-mini" onkeyup="checkNum(this);"/>
                ${msg['jsp.itemchange.reason']}:
                <input type="text" id="reason3" class="input-mini" onkeyup="checkNum(this);"/>
            </span>
            <i class="icon-search icon-white" onclick="getLog(3, this, {'modelId': $('#itemId').val(), 'reason':$('#reason3').val()});"></i>
        </label>
        <div class="result"></div>
    </div>

    <!-- 经验变化日志 -->
    <div id="expchangelog">
        <label class="label label-info" style="height: 25px; line-height: 25px; font-size: 12px;">
            经验变化日志
            <i class="icon-plus icon-white" onclick="showResult(this, true);"></i>
            <i class="icon-minus icon-white" onclick="showResult(this, false);"></i>
            <i class="icon-filter icon-white" onclick="showCondition(this);"></i>
            <span class="condition">
                ${msg['jsp.itemchange.reason']}:
                <input type="text" id="reason4" class="input-mini" onkeyup="checkNum(this);"/>
            </span>
            <i class="icon-search icon-white" onclick="getLog(4, this, {'reason':$('#reason4').val()});"></i>
        </label>
        <div class="result"></div>
    </div>
</div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>