<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>角色转移</title>
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
<script>
    var base = '${base}';
    $(function() {
        reloadNoHeFuServerGroups(false);
    });

    function showList() {
        $.ajax({
            url: base + "/transfer/list",
            method: "post",
            data: {
                serverId: $("#select_server").val()
            },
            success: function(data) {
                var list_html = "<table class='table table-bordered table-striped'>";
                list_html += "<tr>";
                list_html += "<th>" + '${msg['jsp.transfer.serverId']}' + "</th>";
                list_html += "<th>" + '${msg['jsp.transfer.roleId']}' + "</th>";
                list_html += "<th>" + '${msg['jsp.transfer.srcUserId']}' + "</th>";
                list_html += "<th>" + '${msg['jsp.transfer.targetUserId']}' + "</th>";
                list_html += "<th>" + '${msg['jsp.transfer.reason']}' + "</th>";
                list_html += "</tr>";
                for (var i = 0; i < data.data.length; i++) {
                    var record = data.data[i];
                    list_html += "<tr>";
                    list_html += "<td>" + record['serverId'] + "</td>";
                    list_html += "<td>" + record['roleId'] + "</td>";
                    list_html += "<td>" + record['srcUserId'] + "</td>";
                    list_html += "<td>" + record['targetUserId'] + "</td>";
                    list_html += "<td>" + record['reason'] + "</td>";
                    list_html += "</tr>";
                }
                list_html += "</table>";
                $("#table").html(list_html)
            }
        });
    }

    function transfer() {
        if (!$("#transferForm").validationEngine('validate')) {
            return;
        }
        $.ajax({
           url: base + "/transfer/transfer",
           method: "post",
           data: {
               serverId: $("#select_server").val(),
               roleId: $("#roleId").val(),
               userId: $("#userId").val(),
               reason: $("#reason").val()
           },
           success: function(data) {
               alert(data.msg);
               $("#transferForm")[0].reset();
               showList();
           }
        });
    }
</script>
<jsp:include page="../role/roleInfo.jsp"/>
</head>
<body>
<div class="container-fluid">
    <form id="role_query_form" class="well form-inline">
        <select id="select_group" name="groupName" onchange="queryServerByGroup(this.value)" class="span2"></select>
        <select id="select_server" name="serverId" class="span3"></select>
        <label class="label">${msg['jsp.role.searchcon']}</label>
        <select id="select_queryType" name="queryType" class="span2">
            <option value="1">${msg['jsp.role.rolename']}</option>
            <option value="2">${msg['jsp.role.account']}</option>
            <option value="5">${msg['jsp.role.userid']}</option>
            <option value="3">${msg['jsp.role.roleid']}</option>
        </select>
        <input id="queryString" type="text" name="queryString" class="span3 validate[required] forbidSubmit">
        <input type="button" id="role_query_btn" value="${msg['jsp.role.search']}" class="btn btn-primary" onclick="role_reload()">
    </form>
    <div id="msg"></div>
    <div id="account_list"></div>
    <div id="role_list"></div>
</div>

<div class="container-fluid">
    <form action="#" id="transferForm" method="post" class="well form-inline">
        <label for="roleId" class="span2" style="margin-left: 0">${msg['jsp.transfer.roleId']}</label>
        <input id="roleId" type="text" name="roleId" class="input=medium validate[required] span4" placeholder=""/>
        <br/><br/>
        <label for="userId" class="span2" style="margin-left: 0">${msg['jsp.transfer.targetUserId']}</label>
        <input id="userId" type="text" name="userId" class="input=medium datetimepicker validate[required] span4"/>
        <br/><br/>
        <label for="reason" class="span2" style="margin-left: 0">${msg['jsp.transfer.reason']}</label>
        <textarea id="reason" name="reason" class="input=medium validate[required] span4" rows="4" cols="60"></textarea>
        <br/><br/>
        <input type="button" class="btn btn-primary" value="${msg['jsp.transfer.submit']}" onclick="transfer()"/>
        <input type="button" class="btn btn-info" value="${msg['jsp.transfer.history']}" onclick="showList()"/>
    </form>
</div>
<div class="container-fluid" id="table"></div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>
