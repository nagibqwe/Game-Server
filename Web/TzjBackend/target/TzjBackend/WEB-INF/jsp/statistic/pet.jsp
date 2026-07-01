<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.pet.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        group_reload();
        $("#excel_btn").hide();
        $("#loading").shCircleLoader();
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
    });

    function search() {
        var startDate = $("#start input").val();
        var endDate = $("#end input").val();
        var isBlack = $("#isBlackList").is(":checked");//用来过滤黑名单
        if (checkDate(startDate, endDate)) {
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.ajax({
                type: "POST",
                url: base + "/statistic/petStatistic",
                data: {
                    "groupName": $("#select_group").val(),
                    "serverId": $("#select_server").val(),
                    "startDate": startDate,
                    "endDate": endDate,
                    "isBlack": isBlack
                },
                dataType: "json",
                success: function (data) {
                    $("#loadingmodal").modal('hide');
                    if (!data.ok) {
                        $("#excel_btn").hide();
                        $("#pet").html(data.msg);
                        return;
                    }

                    var table = $("<table>").attr("class", "table table-bordered table-striped");
                    var thead = $("<thead>");
                    var htr = $("<tr>");
                    var fields = ["${msg['jsp.pet.key']}",
                        "${msg['jsp.pet.totaltimes']}"];
                    for (var field in fields) {
                        var th = $("<th>").text(fields[field]);
                        htr.append(th);
                    }
                    thead.append(htr);
                    table.append(thead);

                    var tbody = $("<tbody>");
                    for (var key in data.data) {
                        var pet = data.data[key]
                        console.log(pet);
                        var tr = $("<tr>");
                        var datalist = [key, pet.totaltimes];
                        for (var i in datalist) {
                            var td = $("<td>").text(datalist[i]);
                            tr.append(td);
                        }
                        tbody.append(tr);
                    }
                    table.append(tbody);
                    $("#excel_btn").show();
                    $("#pet").show();
                    $("#pet").html(table);
                }
            });
        }
    }

    function exportExcel() {
        $("#pet table").eq(0).tableExport({type: 'excel', escape: 'false'});
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <select id="select_group" name="groupName" onchange="queryServerByGroup(this.value)" class="span2"></select>
        <select id="select_server" name="serverId" class="span2"></select>

        <!-- 时间段 -->
        <label class="label">${msg['jsp.log.time']}</label>
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="startDate" size="20" type="text" value="${nowDate}" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endDate" size="20" type="text" value="${nowDate}" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>

        <label class="label" for="isBlackList">${msg['statistic.public.isblack']}</label>
        <input type="checkbox" id="isBlackList"/>
        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
        <input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel()">
    </form>
    <div id="msg"></div>
    <div id="pet"></div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>