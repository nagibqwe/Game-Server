<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>玩家封号</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {

        reloadNoHeFuServerGroups(false);

        $('.datetimepicker').datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm-dd hh:ii:ss',
            todayBtn: 1,
            startDate: new Date(),
            autoclose: true
        });

        //禁止回车提交表单
        $('.forbidSubmit').keypress(function (e) {
            if (e.keyCode == 13) {
                e.preventDefault();
            }
        });
    });

    function forbidUser() {
        if ($('#forbidUserForm').validationEngine('validate')) {
            if ($("#condition").val() == undefined || $("#condition").val() == "") {
                alert("condition error");
                return;
            }
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.post(base + "/forbidden/forbidUser",
                {
                    "groupName": $("#select_group").val(),
                    "type": $("#type").val(),
                    "condition" : $("#condition").val(),
                    "endTime" : $("#endTime").val(),
                    "reason" : $("#reason").val()
                },
                function (data) {
                    $("#loadingmodal").modal('hide');
                    $("#forbidUserForm")[0].reset();
                    for (var key in data.data) {
                        var label = $("<label>").text(data.data[key]);
                        $("#messResult").append(label);
                    }
                });
        } else {
            alert("${msg['forbid.dataerror']}");
        }
    }
</script>
<jsp:include page="../role/roleInfo.jsp"/>
</head>
<body>
<div class="container-fluid">
    <form id="role_query_form" class="well form-inline">
        <select id="select_group" name="groupName" onchange="queryServerByGroup(this.value)" class="span2"></select>
        <select id="select_server" name="serverId" class="span2"></select>
        ${msg['jsp.role.searchcon']}
        <select id="select_queryType" name="queryType" class="span2">
            <option value="1">${msg['jsp.role.rolename']}</option>
            <option value="2">${msg['jsp.role.account']}</option>
            <option value="3">${msg['jsp.role.roleid']}</option>
            <option value="4">${msg['jsp.role.userid']}</option>
            <option value="5">${msg['jsp.role.acpfaccount']}</option>
        </select>
        <input id="queryString" type="text" name="queryString" class="span3 validate[required] forbidSubmit">
        <input type="button" id="role_query_btn" value="${msg['jsp.role.search']}" class="btn btn-primary" onclick="role_reload()">
    </form>
    <div id="msg"></div>
    <div id="account_list"></div>
    <div id="role_list"></div>
</div>

<div class="container-fluid">
    <form action="#" id="forbidUserForm" method="post" class="well form-inline">
        <label for="type" class="span2" style="margin-left: 0">${msg['forbid.user.type']}</label>
        <select id="type"  name="type" class="input=medium validate[required] span4">
            <option value="1">${msg['forbid.user.type1']}</option>
            <option value="2">${msg['forbid.user.type2']}</option>
            <option value="3">${msg['forbid.user.type3']}</option>
            <option value="4">${msg['forbid.user.type4']}</option>
            <option value="5">${msg['forbid.user.type5']}</option>
        </select>
        <br/><br/>
        <label for="condition" class="span2" style="margin-left: 0">${msg['forbid.user.userid']}</label>
        <input id="condition" type="text" name="condition" class="input=medium validate[required] span4" placeholder="IP、账号名、mac地址、imei和机器唯一码"/>
        <br/><br/>
        <label for="endTime" class="span2" style="margin-left: 0">${msg['forbid.user.endtime']}</label>
        <input id="endTime" type="text" name="endTime" class="input=medium datetimepicker validate[required] span4" readonly/>
        <br/><br/>
        <label for="reason" class="span2" style="margin-left: 0">${msg['forbid.chat.reason']}</label>
        <textarea id="reason" name="reason" class="input=medium validate[required] span4" rows="4" cols="60"></textarea>
        <br/><br/>
        <input type="button" class="btn btn-primary" value="${msg['forbid.user.add']}" onclick="forbidUser()"/>
    </form>
</div>
<div class="container-fluid" id="messResult"></div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>