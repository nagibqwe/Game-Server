<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<title>设置SDK评价状态</title>
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
        setTimeout(function () {
            search();
        }, 500);
    });

    function evaluateSubmit() {
        if ($("#evaluateForm").validationEngine('validate')) {
            $.ajax({
                url: base + "/evaluate/setEvaluateState",
                data: $("#evaluateForm").serialize(),
                method: "post",
                dataType: "json",
                success: function (data) {
                    alert(data.msg);
                    if (data.ok) {
                        $("#evaluateForm")[0].reset();
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
            url: base + "/evaluate/getEvaluateList",
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
                    <td>评价类型</td>\
                    <td>状态</td>\
                    <td>操作时间</td>\
                    <td>${msg['jsp.deductitem.option']}</td>\
                    </tr></thead><tbody>";
                var deItemList = data.data;
                for (var i = 0; i < deItemList.length; i++) {
                    list_html += "<tr>";
                    list_html += "<td>" + deItemList[i].id + "</td>";
                    list_html += "<td>" + platform + "</td>";
                    list_html += "<td>" + serverId + "</td>";
                    list_html += "<td>" + deItemList[i].eType + "</td>";
                    list_html += "<td>" + deItemList[i].state + "</td>";
                    list_html += "<td>" + deItemList[i].actionTime + "</td>";
                    list_html += "<td><input type='button' class='btn' value=\"${msg['activity.delete']}\" onclick='del(" + deItemList[i].id + ")'/></td>";
                    list_html += "</tr>";
                }
                list_html += "</tbody></table>";
                $("#item_list").html(list_html);
            }
        });
    }

    function getStr(data) {
        if (data == undefined || data == null) {
            return "";
        }
        return data;
    }

    function del(id) {
        $.ajax({
            url: base + "/evaluate/delEvaluate",
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
    <form action="#" id="evaluateForm" class="well form-horizontal">
        <div class="control-group">
            <label for="select_group" class="control-label">${msg['jsp.server']}</label>
            <div class="controls">
                <select id="select_group" onchange="reloadNoHeFuServer(this.value, false)" class="span2"></select>
                <select id="select_server" onchange="reloadHistory()" name="serverId" class="span2"></select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">评价类型</label>
            <div class="controls inline">
                <select id="select_eType" name="eType" class="span2">
                    <option value="1">点赞</option>
                    <option value="2">分享</option>
                </select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">状态：</label>
            <div class="controls">
                <label class="radio inline">
                    <input value="0" name="state" type="radio" onchange="changeParam(0);"/>
                    关闭
                </label>
                <label class="radio inline">
                    <input value="1" name="state" type="radio" onchange="changeParam(1);"/>
                    开启
                </label>
            </div>
        </div>

        <div class="control-group">
            <label for="reason" class="control-label">${msg['jsp.deductitem.optionReason']}</label>
            <div class="controls">
                <input id="reason" type="text" name="reason" value="用于游戏测试" class="validate[required]"/>
            </div>
        </div>

        <div class="control-group">
            <div class="controls">
                <input type="button" value="执行操作" class="btn btn-danger" onclick="evaluateSubmit()"/>
            </div>
        </div>
    </form>

    <div id="item_list"></div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>