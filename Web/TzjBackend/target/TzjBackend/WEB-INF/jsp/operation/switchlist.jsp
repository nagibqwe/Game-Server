<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>系统开关列表</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-confirmation.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var base = '${base}';
    var func_switch = [];
    $(function () {
        reloadNoHeFuServerGroups(false);
    });

    function search() {
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            url: base + "/systemSwitch/getFunctionSwitch",
            data: {
                serverId: $("#select_server").val(),
                condition: $("#condition").val()
            },
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                if (!data.ok) {
                    alert(data.msg);
                    $("#function_list").html("");
                    return;
                } else {
                    var data = data.data;
                    var html = "<table class='table table-bordered table-striped'><thead><tr>\
                        <th>${msg['sysswitch.number']}</th>\
                        <th>${msg['sysswitch.funame']}</th>\
                        <th>${msg['sysswitch.fustate']}</th>\
                        <th>${msg['sysswitch.option']}</th></tr></thead>";
                    for (var i = 0; i < data.length; i++) {
                        html += "<tr><td>" + data[i].funcId + "</td>";
                        html += "<td>" + data[i].funcName + "</td>";
                        if (data[i].openState == "1") {
                            html += "<td><span style='color:green;'>${msg['jsp.menu.open']}</span></td>";
                            html += "<td><button id='switchIs_" + data[i].funcId + "' tag='1' class='btn' onclick=\'switchIs(" + data[i].funcId + ");\'>${msg['sysswitch.close']}</button></td>";
                        } else {
                            html += "<td><span style='color:red;'>${msg['jsp.menu.close']}</span></td>";
                            html += "<td><button id='switchIs_" + data[i].funcId + "' tag='0' class='btn btn-primary' onclick=\'switchIs(" + data[i].funcId + ");\'>${msg['sysswitch.open']}</button></td>";
                        }
                        html += "</tr>";
                    }
                    html += "</tbody></table>";
                    $("#function_list").html(html);
                }
            }
        });
    }

    function switchIs(functionId) {
        var switchIs = $("#switchIs_" + functionId).attr("tag");
        var prompt = "";
        if (switchIs == "0") {
            prompt = "${msg['jsp.menu.open']}";
        } else {
            prompt = "${msg['jsp.menu.close']}";
        }
        if (confirm("是否确定" + prompt + "该功能？")) {
            console.log(switchIs);
            if (switchIs == "0") {
                switchIs = "1";
                $("#switchIs_" + functionId).removeClass("btn-primary");
                $("#switchIs_" + functionId).html("${msg['jsp.menu.open']}");
            } else {
                switchIs = "0";
                $("#switchIs_" + functionId).addClass("btn-primary");
                $("#switchIs_" + functionId).html("${msg['jsp.menu.close']}");
            }
            func_switch.push(functionId + "@" + switchIs);
        }
    }

    function update() {
        if (func_switch.length == 0) {
            return;
        }
        var serverId = $("#select_server").val();
        $.ajax({
            url: base + "/systemSwitch/switchIs",
            data: {
                serverId: serverId,
                funcSwitch: func_switch + ","
            },
            method: "post",
            dataType: "json",
            success: function (data) {
                alert(data.msg);
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
    <form action="#" id="query_form" class="well form-inline">
        <select id="select_group" onchange="reloadNoHeFuServerGroups(false)" class="span2"></select>
        <select id="select_server" name="serverId" class="span2"></select>
        <input type="text" id="condition" placeholder="Search">
		<button type="button" class="btn btn-primary" onclick="search();"><i class="icon-search icon-white"></i></button>
        <input type="button" id="excel_btn" value="${msg['jsp.save']}" class="btn btn-primary" onclick="update();">
    </form>
</div>

<div id="function_list" class="container-fluid"></div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>
