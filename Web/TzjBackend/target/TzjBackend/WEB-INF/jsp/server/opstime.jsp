<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置开服时间</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        reloadNoHefuServerGroupBox(false);

        $(".date").datetimepicker({
            language:  'zh-CN',
            format : 'yyyy-mm-dd hh:ii:00',
            weekStart: 1,
            todayBtn:  1,
            autoclose: 1,
            startView : 2,
            minView : 0
        });
    });
    
    function queryServerInfo() {
        var num = $("[name='serverId']:checked").length;
        if (num == 0) {
            alert("未选择服务器！")
            return;
        }
        if (num > 1) {
            alert("只能选择一个服务器！");
            return;
        }
        $("#timeMsg").empty();
        $("#numMsg").empty();
        $.post(base + "/gm/queryOpsTime", $("#form").serialize(), function (data) {
            if (data.ok) {
                $("#timeMsg").show();
                $("#timeMsg").html("开服时间：" + data.data);
            } else {
                alert(data.msg);
            }
        });

        $("#num").empty();
        $.post(base + "/gm/setRegisterLimitNum", $("#form").serialize(), function (data) {
            if (data.ok) {
                $("#numMsg").show();
                $("#numMsg").html("注册限制人数 ：" + data.data);
            } else {
                alert(data.msg);
            }
        });
    }

    function setOpsTime() {
        if (!checkServerOpenTimeValid()) {
            return;
        }
        var num = $("[name='serverId']:checked").length;
        if (num > 1) {
            alert("只能选择一个服务器");
            return;
        }
        if ($("[name='time']").val() == "") {
            alert("未选择时间");
            return;
        }
        $.post(base + "/gm/setOpsTime", $("#form").serialize(), function (data) {
            alert(data.msg);
            queryServerInfo();
        });
    }

    function setRegisterLimitNum() {
        var num = $("[name='serverId']:checked").length;
        if (num > 1) {
            alert("只能选择一个服务器");
            return;
        }
        $.post(base + "/gm/setRegisterLimitNum", $("#form").serialize(), function (data) {
            alert(data.msg);
            $("#num").val("");
            queryServerInfo();
        });
    }

    function checkServerOpenTimeValid() {
        var timeStr = $("[name='time']").val();
        var openData = new Date(timeStr);
        if (openData < new Date) {
            alert("时间不能小于当前时间");
            return false;
        }
        return true;
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="form" class="well form-inline">
        <select id="select_group" class="span3"></select>
        <label id="allCheck" class="checkbox inline">全选</label>
        <input type="checkbox" onclick="selectAllSid()"/>
        <div id="checkbox_server" class="well"></div>

        <input type="button" class="btn btn-info" value="查看服务器设置信息" onclick="queryServerInfo()"/>
        <label class="label" id="timeMsg" style="display: none"></label>
        <label class="label" id="numMsg" style="display: none"></label>
        <br/><br/>

        <div class="inline">
            <label class="label">设置开服时间</label>
            <div class="input-append date">
                <input id="time" class="span2" size="20" type="text" name="time" value="${nowDate}" readonly>
                <span class="add-on"><i class="icon-th"></i></span>
            </div>
            <input type="button" class="btn btn-info" value="设置" onclick="setOpsTime()"/>
        </div>

        <div class="inline">
            <label class="label">设置注册限制人数</label>
            <input id="num" class="offset2 span2" size="20" type="text" name="num"/>
            <input type="button" class="btn btn-info" value="设置" onclick="setRegisterLimitNum()"/>
        </div>
    </form>
</div>
</body>
</html>