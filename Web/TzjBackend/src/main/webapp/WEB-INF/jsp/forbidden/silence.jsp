<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>玩家禁言</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        reloadNoHeFuServerGroups(false);
        reloadNoHefuServerGroupBox(false);

        //禁止回车提交表单
        $('.forbidSubmit').keypress(function (e) {
            if (e.keyCode == 13) {
                e.preventDefault();
            }
        });
    });

    function closeChat() {
        if ($('#closeChatForm').validationEngine('validate')) {
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            if ($("[name='serverId']:checked").length <= 0) {
                alert("未选择服务器");
            }
            $.post(base + "/forbidden/playerCloseChat", $("#closeChatForm").serialize(),
                function (data) {
                    $("#loadingmodal").modal('hide');
                    $("#closeChatForm")[0].reset();
                    $("#messResult").html(data.msg);
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
        <select id="select_group" name="groupName" onchange="reloadNoHeFuServer(this.value)" class="span2"></select>
        <select id="select_server" name="serverId" class="span2"></select>
        ${msg['jsp.role.searchcon']}
        <select id="select_queryType" name="queryType" class="span2">
            <option value="1">${msg['jsp.role.rolename']}</option>
            <option value="2">${msg['jsp.role.account']}</option>
            <option value="3">${msg['jsp.role.roleid']}</option>
            <option value="4">${msg['jsp.role.userid']}</option>
            <option value="5">${msg['jsp.role.acpfaccount']}</option>
        </select>
        <input id="queryString" type="text" name="queryString" class="span2 validate[required] forbidSubmit"/>
        <input type="button" id="role_query_btn" value="${msg['jsp.role.search']}" class="btn btn-primary" onclick="role_reload()">
    </form>
    <div id="msg"></div>
    <div id="account_list"></div>
    <div id="role_list"></div>
</div>
<div class="container-fluid">
    <form action="#" id="closeChatForm" method="post" class="well form-inline">
        <input type="checkbox" id="selectAll" onchange="selectAllSid();">${msg['server.all.choice']}
        <div id="checkbox_server" class="well"></div>

        <label for="crimeType" class="span2" style="margin-left: 0">${msg['forbid.chat.crime']}</label>
        <select id="crimeType" name="crimeType" class="span4">
            <option value="1">${msg['forbid.chat.crime1']}</option>
            <option value="2">${msg['forbid.chat.crime2']}</option>
        </select>
        <br/><br/>
        <label for="forbidType" class="span2" style="margin-left: 0">${msg['forbid.chat.type']}</label>
        <select id="forbidType" name="forbidType" class="span4">
            <option value="1">${msg['forbid.chat.type1']}</option>
            <option value="2">${msg['forbid.chat.type2']}</option>
            <option value="3">${msg['forbid.chat.type3']}</option>
            <option value="4">${msg['forbid.chat.type4']}</option>
            <option value="5">${msg['forbid.chat.type5']}</option>
            <option value="6">${msg['forbid.chat.type6']}</option>
        </select>
        <br/><br/>
        <label for="playerId" class="span2" style="margin-left: 0">${msg['forbid.chat.userId']}</label>
        <input id="playerId" type="text" name="playerId" class="input=medium validate[required],custom[integer] span4"/>
        <br/><br/>
        <label for="times" class="span2" style="margin-left: 0">${msg['forbid.chat.endtime']}</label>
        <input id="times" type="text" name="times" class="input=medium validate[required],custom[integer],min[-1] span4"/>
        <br/><br/>
        <label for="reason" class="span2" style="margin-left: 0">${msg['forbid.chat.reason']}</label>
        <textarea id="reason" name="reason" class="input=medium validate[required] span4" rows="5" cols="60"></textarea>
        <br/><br/>
        <input type="button" id="addSubmit" class="btn btn-primary" value="${msg['forbid.chat.deal']}" onclick="closeChat()"/>
    </form>
</div>
<div class="container-fluid" id="messResult"></div>
<br/>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>