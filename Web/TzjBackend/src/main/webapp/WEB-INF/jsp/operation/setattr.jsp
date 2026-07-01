<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<title>修改角色属性</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-confirmation.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var base = '${base}';
    var items = new Map();
    $(function () {
        loadAllItem();
        reloadNoHeFuServerGroups(false);
        // $("#mailDiv").hide();
        setTimeout(function () {
            search();
        }, 500);
    });

    function deductSubmit() {
        if ($("#roleAttrForm").validationEngine('validate')) {
            $.ajax({
                url: base + "/roleattr/setRoleAttrByRoleId",
                data: $("#roleAttrForm").serialize(),
                method: "post",
                dataType: "json",
                success: function (data) {
                    alert(data.msg);
                    if (data.ok) {
                        $("#roleAttrForm")[0].reset();
                        // $("#mailDiv").hide();
                        search();
                    }
                }
            });
        }
    }

    function reloadHistory(){
        search();
    }

    function search() {
        var platform = $("#select_group").val();
        var serverId = $("#select_server").val();
        $.ajax({
            url: base + "/roleattr/getRoleAttrList",
            data: {
                serverId: serverId
            },
            method: "post",
            dataType: "json",
            async: false,
            success: function (data) {
                var list_html = "<table class='table table-bordered table-striped'><thead>\
                    <tr align='center' style='font-weight: bolder;'>\
                    <td>${msg['jsp.deductitem.td.number']}</td>\
                    <td>${msg['jsp.deductitem.td.platform']}</td>\
                    <td>${msg['jsp.deductitem.td.sid']}</td>\
                    <td>属性类型</td>\
                    <td>${msg['jsp.deductitem.td.roleId']}</td>\
                    <td>属性值</td>\
                    <td>真实属性值</td>\
                    <td>操作时间</td>\
                    <td>${msg['jsp.deductitem.option']}</td>\
                    </tr></thead><tbody>";
                var deItemList = data.data;
                for (var i = 0; i < deItemList.length; i++) {
                    list_html += "<tr>";
                    list_html += "<td>" + deItemList[i].id + "</td>";
                    list_html += "<td>" + platform + "</td>";
                    list_html += "<td>" + serverId + "</td>";
                    list_html += "<td>" + deItemList[i].attrType + "</td>";
                    list_html += "<td>" + deItemList[i].roleId + "</td>";
                    list_html += "<td>" + deItemList[i].attrValue + "</td>";
                    list_html += "<td>" + deItemList[i].realValue + "</td>";
                    list_html += "<td>" + deItemList[i].actionTime + "</td>";
                    list_html += "<td><input type='button' class='btn' value=\"${msg['activity.delete']}\" onclick='del(" + deItemList[i].id + ")'/></td>";
                    list_html += "</tr>";
                }
                list_html += "</tbody></table>";
                $("#deduct_item_list").html(list_html);
            }
        });
    }
    // function showMailDiv() {
    //     var show = $("#mailCheckBox").prop("checked");
    //     $("#mailDiv").toggle(show);
    //     $("#isMail").val(show ? "1" : "0");
    // }

    function getStr(data) {
        if (data == undefined || data == null) {
            return "";
        }
        return data;
    }

    function del(id) {
        $.ajax({
            url: base + "/roleattr/delRoleAttr",
            data: {
                id: id
            },
            method: "post",
            dataType: "json",
            async: false,
            success: function (data) {
                if (data.ok) {
                    search();
                }
            }
        });
    }
</script>
</head>

<body>
    <div class="container-fluid">
    <form action="#" id="roleAttrForm" class="well form-horizontal">
        <div class="control-group">
            <label for="select_group" class="control-label">${msg['jsp.server']}</label>
            <div class="controls">
                <select id="select_group" onchange="reloadNoHeFuServer(this.value, false)" class="span2"></select>
                <select id="select_server" onchange="reloadHistory()" name="serverId" class="span2"></select>
            </div>
        </div>

        <div class="control-group">
            <label for="roleId" class="control-label">${msg['jsp.deductitem.roleId']}</label>
            <div class="controls">
                <input id="roleId" name="roleId" type="text" class="validate[required]"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">属性类型</label>
            <div class="controls inline">
<%--                <input type="text" id="itemId" name="itemId" list="itemList" placeholder="${msg['jsp.deductitem.itemId']}" class="validate[required] span2"/>--%>
<%--                <datalist id="itemList"></datalist>--%>
<%--                <input type="text" id="itemNum" name="dedCount" placeholder="${msg['jsp.deductitem.itemNum']}" class="validate[required] span2"/>--%>
<%--                <select name="isBind" class="span1" style="height: 26px">--%>
<%--                    <option value="0">非绑</option>--%>
<%--                    <option value="1">绑定</option>--%>
<%--                </select>--%>
                <select id="select_attrType" name="attrType" class="span2">
                    <option value="0">等级</option>
                    <option value="1">境界</option>
                    <option value="2">主线任务</option>
                </select>
                <input type="text" id="attrValue" name="attrValue" placeholder="属性值" class="validate[required] span2"/>
            </div>
        </div>

        <div class="control-group">
            <label for="reason" class="control-label">${msg['jsp.deductitem.optionReason']}</label>
            <div class="controls">
                <input id="reason" type="text" name="reason" value="用于游戏测试" class="validate[required]"/>
<%--                <label class="checkbox inline">--%>
<%--                    <input id="mailCheckBox" type="checkbox" onclick="showMailDiv()"/>${msg['jsp.deductitem.ismail']}是否邮件通知--%>
<%--                </label>--%>
<%--                <input type="hidden" id="isMail" name="isMail" value="0"/>--%>
            </div>
        </div>

<%--        <div id="mailDiv">--%>
<%--            <div class="control-group">--%>
<%--                <label for="mailTitle" class="control-label">${msg['jsp.deductitem.mailTitle']}</label>--%>
<%--                <div class="controls">--%>
<%--                    <input id="mailTitle" type="text" name="mailTitle"/>--%>
<%--                </div>--%>
<%--            </div>--%>

<%--            <div class="control-group">--%>
<%--                <label for="mailContent" class="control-label">${msg['jsp.deductitem.mailContent']}</label>--%>
<%--                <div class="controls">--%>
<%--                    <textarea id="mailContent" name="mailContent" rows="3"></textarea>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>

        <div class="control-group">
            <div class="controls">
                <input type="button" value="执行操作" class="btn btn-danger" onclick="deductSubmit()"/>
            </div>
        </div>
    </form>

    <div id="deduct_item_list"></div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>