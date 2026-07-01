<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.dailydata.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        getGroup();
        $("#start").datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm-dd',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            showMeridian: 1
        });
        $("#end").datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm-dd',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            showMeridian: 1
        });
        $("#loading").shCircleLoader();
    });

    function search() {
        if ($("input[name='serverId']:checked").length == 0) {
            alert("${msg['statistic.public.syspromt.server']}");
            return;
        }
        var startDate = $("#start input").val();
        var endDate = $("#end input").val();
        if (checkDate(startDate, endDate)) {
            $("#dailydata").empty();
            $("#allDailyData").empty();
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.ajax({
                type: "POST",
                url: base + "/statistic/dailyData",
                data: $("#query_form").serialize(),
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    $("#loadingmodal").modal('hide');
                    if (!data.ok) {
                        $("#dailydata").html(data.msg);
                        return;
                    }

                    if (data.type == "all") {
                        createAllDailyData(data.all);
                        return;
                    }

                    var table = $("<table>").attr("class", "table table-bordered table-striped");
                    var thead = $("<thead>");
                    var htr = $("<tr>");

                    var fields = [
                        "${msg['jsp.dailydata.day']}",
                        "${msg['jsp.dailydata.totalmoney']}",
                        "${msg['jsp.dailydata.totalgold']}",
                        "${msg['jsp.dailydata.totaltimes']}",
                        <%--"${msg['jsp.dailydata.totalbindgold']}",--%>
                        "${msg['jsp.dailydata.totaluser']}",
                        "${msg['jsp.dailydata.rateofpay']}",
                        "${msg['jsp.dailydata.activenum']}",
                        "${msg['jsp.dailydata.addnum']}",
                        "${msg['jsp.dailydata.addRechargeNum']}",
                        "${msg['jsp.dailydata.addRechargRate']}",
                        "${msg['jsp.dailydata.devicenum']}",
                        "${msg['jsp.dailydata.deviceaddnum']}",
                        "${msg['jsp.dailydata.arpu']}",
                        "${msg['jsp.dailydata.arppu']}",
                        "${msg['jsp.dailydata.maxnum']}",
                        "${msg['jsp.dailydata.avgnum']}"];
                    for (var field in fields) {
                        var th = $("<th>").text(fields[field]);
                        htr.append(th);
                    }
                    thead.append(htr);
                    table.append(thead);

                    var tbody = $("<tbody>");
                    console.log(data.data);
                    for (var key in data.data) {
                        var dailydata = data.data[key]
                        var tr = $("<tr>");
                        var datalist = [
                            key,
                            dailydata.totalmoney == null ? 0 : dailydata.totalmoney,
                            dailydata.totalgold,
                            dailydata.totaltimes,
                            // dailydata.totalbindgold,
                            dailydata.totaluser,
                            toPercent((dailydata.totaluser / dailydata.activenum).toFixed(2)),
                            dailydata.activenum,
                            dailydata.addnum,
                            dailydata.addRechargeNum,
                            toPercent((dailydata.addRechargeNum / dailydata.addnum).toFixed(2)),
                            dailydata.deviceNum,
                            dailydata.deviceaddnum,
                            (dailydata.totalmoney / dailydata.activenum).toFixed(2),
                            (dailydata.totalmoney / dailydata.totaluser).toFixed(2),
                            dailydata.maxnum,
                            dailydata.avgnum];
                        for (var i in datalist) {
                            var td = $("<td>").text(datalist[i]);
                            tr.append(td);
                        }
                        tbody.append(tr);
                    }
                    table.append(tbody);
                    $("#dailydata").show();
                    $("#dailydata").html(table);
                }
            });
        }
    }

    function createAllDailyData(data) {
        var table = $("<table>").attr("class",
            "table table-bordered table-striped");
        var thead = $("<thead>");
        var htr = $("<tr>");

        var fields = [
            "${msg['jsp.dailydata.allpay']}",
            "${msg['jsp.dailydata.allactiveuser']}",
            "${msg['jsp.dailydata.allpayrate']}",
            "${msg['jsp.dailydata.allmoney']}",
            "${msg['jsp.dailydata.allgold']}",
            "${msg['jsp.dailydata.allbindgold']}",
            "${msg['jsp.dailydata.arpu']}",
            "${msg['jsp.dailydata.arppu']}"];
        for (var i in data) {
            var th = $("<th>").text(i);
            htr.append(th);
        }
        thead.append(htr);
        table.append(thead);

        var tbody = $("<tbody>");
        var tr = $("<tr>");
        for (var key in data) {
            var td = $("<td>").text(data[key]);
            tr.append(td);
        }
        tbody.append(tr);
        table.append(tbody);
        $("#allDailyData").show();
        $("#allDailyData").html(table);
    }

    function toPercent(value) {
        return (value * 100).toFixed(1) + "%";
    }
</script>
</head>
<body>
<div class="container-fluid">
    <div>
        <form action="#" id="query_form" class="well form-inline">
            <label class="label" for="select_group">${msg['statistic.public.platform']}</label>&nbsp;&nbsp;
            <select id="select_group" name="groupName" onchange="getServer(this.value);getChannelName(this.value);"></select>
            <br/><br/>

            <label class="label">${msg['statistic.public.server']}</label>&nbsp;&nbsp;
            <span id="checkbox_server"></span>
            <br/><br/>

            <label class="label">${msg['statistic.public.Channel']}</label>&nbsp;&nbsp;
            <span id="checkbox_channel"></span>
            <br/><br/>

            <label class="label">${msg['statistic.public.time']}</label>&nbsp;&nbsp;
            <div class="input-append date" id="start">
                <input style="width: 120px;" name="startDate" size="20" type="text" value="${nowDate}" readonly> <span class="add-on"><i class="icon-th"></i></span>
            </div>
            -
            <div class="input-append date" id="end">
                <input style="width: 120px;" name="endDate" size="20" type="text" value="${nowDate}" readonly> <span class="add-on"><i class="icon-th"></i></span>
            </div>
            <br/><br/>

            <label class="label" for="isBlackList">${msg['statistic.public.isblack']}</label>
            <input type="checkbox" id="isBlackList"/>
            <br/><br/>

            <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
        </form>
    </div>
    <div id="msg"></div>
    <div id="allDailyData"></div>
    <div id="dailydata"></div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>