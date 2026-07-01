<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
    <title>活动查询</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-confirmation.js"></script>
    <script type="text/javascript" src="${base}/js/dateFormat.js"></script>
    <script type="text/javascript" src="${base}/js/global.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
    <script type="text/javascript">
        var base = '${base}';

        $(function () {
            reloadNoHeFuServerGroups(false);
            $("#loading").shCircleLoader();
        });

        function search() {
            $("#loadingmodal").modal({
                backdrop: 'static',
                keyboard: false
            });
            $.ajax({
                url: base + "/activity/queryActivityByServer",
                data: {
                    serverId:  $("#select_server").val()
                },
                method: "post",
                dataType: "json",
                success: function (data) {
                    $("#loadingmodal").modal('hide');
                    if (!data.ok) {
                        alert(data.msg);
                        $("#activity_list").html("");
                        return;
                    }
                    var activitydata = data.data;
                    var html = "";
                    html += "<tbody>";
                    html += "<tr>";
                    html += "<th>活动序号</th>";
                    html += "<th>活动名称</th>";
                    html += "<th>活动时间</th>";
                    html += "<th>领取条件</th>";
                    html += "<th>活动奖励</th>";
                    html += "<th>活动封顶次数</th>";
                    html += "<th>活动类型</th>";
                    html += "<th>公告内容</th>";
                    html += "<th>操作</th>";
                    html += "</tr>";
                    for (var i = 0; i < activitydata.length; i++) {
                        html += "<tr>";
                        html += "<td>" + activitydata[i].id + "</td>";
                        html += "<td>" + activitydata[i].name + "</td>";
                        html += "<td>" + TimeObjectUtil.UnixToDate(activitydata[i].activityStartTime / 1000) + "~" + TimeObjectUtil.UnixToDate(activitydata[i].activityEndTime / 1000) + "</td>";
                        html += "<td>" + activitydata[i].activityConlist + "</td>";
                        html += "<td>" + activitydata[i].rewardList + "</td>";
                        html += "<td>" + activitydata[i].activityReplay + "</td>";
                        html += "<td>" + getType(activitydata[i].type) + "</td>";
                        html += "<td>" + activitydata[i].noteTitle + "</td>";
                        html += "<td><input type='button' value=\"${msg['activity.delete']}\" class='btn btn-primary' onclick='deleteAct(" + activitydata[i].id + ");'></td>";
                        html += "</tr>";
                    }
                    html += "</tbody>";
                    $("#activity_list").html(html);
                }
            });
        }

        function deleteAct(actId) {
            $.ajax({
                url: base + "/activity/deleteActivityByServer",
                data: {
                    serverId: $("#select_server").val(),
                    actId: actId
                },
                method: "post",
                dataType: "json",
                success: function (data) {
                    if (!data.ok) {
                        alert(data.msg);
                    } else {
                        alert(data.msg);
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
            <select id="select_group" onchange="reloadNoHeFuServer(this.value,true);"></select>
            <select id="select_server" name="serverId" class="span2"></select>
            <input type="button" id="excel_btn" value="${msg['jsp.log.search']}" class="btn btn-primary"
                   onclick="search();">
        </form>
    </div>

    <div class="container-fluid">
        <table class="table table-bordered table-striped" id="activity_list">
        </table>
    </div>

    <jsp:include page="../commonmodal.jsp"/>
</body>
</html>
