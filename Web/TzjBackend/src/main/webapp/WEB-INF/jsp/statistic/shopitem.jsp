<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.shopitem.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        group_reload();
        setTimeout(function () {
            var groupName = $("#select_group").val();
            getChannelName(groupName);
        }, 100);
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
        var startDate = $("#start input").val();
        var endDate = $("#end input").val();
        var isBlack = $("#isBlackList").is(":checked");
        var channelNames = [];
        $("input[name='channelName']:checked").each(function () {
            channelNames.push("'" + $(this).val() + "'");
        });
        if (checkDate(startDate, endDate)) {
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.ajax({
                type: "POST",
                url: base + "/statistic/shopItemStatistic",
                data: {
                    groupName: $("#select_group").val(),
                    serverId: $("#select_server").val(),
                    channelNames: channelNames + "",
                    startDate: startDate,
                    endDate: endDate,
                    shopId: $("#shopId").val(),
                    moneyType: $("#moneyType").val(),
                    isBlack: isBlack
                },
                dataType: "json",
                success: function (data) {
                    $("#loadingmodal").modal('hide');
                    if (!data.ok) {
                        $("#shopitem").html(data.msg);
                        return;
                    }

                    var table = $("<table>").attr("class", "table table-bordered table-striped");
                    var thead = $("<thead>");
                    var htr = $("<tr>");
                    var fields = [
                        //"${msg['jsp.shopitem.shopId']}",
                        "${msg['jsp.shopitem.itemmodelid']}",
                        "${msg['jsp.shopitem.moneyType']}",
                        "${msg['jsp.shopitem.users']}",
                        "${msg['jsp.shopitem.roles']}",
                        "${msg['jsp.shopitem.totalnum']}",
                        "${msg['jsp.shopitem.totalgold']}",
                        "${msg['jsp.shopitem.totaltimes']}"];
                    for (var field in fields) {
                        var th = $("<th>").text(fields[field]);
                        htr.append(th);
                    }
                    thead.append(htr);
                    table.append(thead);

                    var tbody = $("<tbody>");
                    console.log(data.data);
                    var consumerTime = 0;
                    var dataCount = 0;
                    var consumerCount = data.consumerCount;
                    for (var key in data.data) {
                        var shopitem = data.data[key]
                        consumerTime += parseInt(shopitem.users);
                        var tr = $("<tr>");
                        var datalist = [
                            shopitem.itemModelId,
                            shopitem.moneyType,
                            shopitem.users,
                            shopitem.roles,
                            shopitem.totalnum,
                            shopitem.totalgold,
                            shopitem.totaltimes];
                        for (var i in datalist) {
                            var td = $("<td>").text(datalist[i]);
                            tr.append(td);
                        }
                        tbody.append(tr);
                        dataCount++;
                    }

                    table.append(tbody);
                    $("#shopitem").show();
                    $("#shopitem").html(table);
                    $("#dataCount").html(dataCount);//记录数
                    $("#consumerCount").html(consumerCount);//购买总人数
                    $("#consumerTime").html(consumerTime);//购买总人次
                }
            });
        }
    }

    function exportExcel() {
        $("#shopitem table").eq(0).tableExport({type: 'excel', escape: 'false'});
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <label class="label">${msg['statistic.public.server']}</label>
        <select id="select_group" name="groupName" onchange="queryServerByGroup(this.value);getChannelName();" class="span2"></select>
        <select id="select_server" name="serverId" class="span2"></select>
        <br/><br/>

        <label class="label">${msg['statistic.public.Channel']}</label>
        <span id="checkbox_channel"></span>
        <br/><br/>

        <!-- 时间段 -->
        <label class="label">${msg['jsp.log.time']}</label>
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="startDate" size="20" type="text" value="${nowDate}" readonly> <span class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endDate" size="20" type="text" value="${nowDate}" readonly> <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <br/><br/>

        <label class="label" for="shopId">${msg['jsp.shopitem.shopType']}</label>
        <select id="shopId" name="shopId" class="span2">
            <option value="1">元宝商城</option>
            <option value="2">兑换商城</option>
        </select>
        <label class="label" for="moneyType">${msg['jsp.shopitem.moneyType']}</label>
        <select id="moneyType" name="moneyType" class="span2">
            <option value="0">${msg['jsp.shopitem.allMoneyType']}</option>
            <option value="1">元宝</option>
            <option value="12">灵石</option>
        </select>
        <br/><br/>

        <label class="label" for="isBlackList">${msg['statistic.public.isblack']}</label>
        <input type="checkbox" id="isBlackList"/>

        <br/><br/>
        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
        <input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel()"><br/>
        <div style="float:right;">
            ${msg['jsp.log.all']}：<span style="color: red;" id="dataCount">0</span>${msg['jsp.log.record']}，
            ${msg['jsp.log.totalpeoplenumber']}：<span style="color: red;" id="consumerCount">0</span>，
            ${msg['jsp.log.totalpeoplecount']}：<span style="color: red;" id="consumerTime">0</span>
        </div>
    </form>
    <div id="msg"></div>
    <div id="shopitem"></div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>