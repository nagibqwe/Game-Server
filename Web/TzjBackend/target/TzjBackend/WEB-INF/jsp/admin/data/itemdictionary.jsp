<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.itemDictionary.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript">
    var base = '${base}';
    var itemList = ${itemList};

    $(function () {
        loadList();
    });

    function queryItem() {
        if (!$("#query_form").validationEngine('validate')) {
            return;
        }
        if($("#itemId").val() == "") {
            loadList();
            return;
        }
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            type: "POST",
            url: base + "/dictionary/getItemInfo",
            data: $("#query_form").serialize(),
            dataType: "json",
            success: function (data) {

                $("#loadingmodal").modal('hide');
                if (!data.ok) {
                    $("#itemInfo").html(data.msg);
                    return;
                }

                var table = $("<table>").attr("class", "table table-bordered table-striped");
                var thead = $("<thead>");
                var htr = $("<tr>");

                var fields = ["${msg['jsp.itemDictionary.itemId']}",
                    "${msg['jsp.itemDictionary.itemName']}",
                    "${msg['jsp.itemDictionary.itemMax']}"];
                for (var field in fields) {
                    var th = $("<th>").text(fields[field]);
                    htr.append(th);
                }
                thead.append(htr);
                table.append(thead);

                var item = data.data;
                var tbody = $("<tbody>");
                var tr = $("<tr>").append($("<td>").text(item.itemId))
                    .append($("<td>").text(item.itemName))
                    .append($("<td>").text(item.itemMax));
                tbody.append(tr);
                table.append(tbody);
                $("#itemInfo").html(table);
            }
        });
    }

    function loadList() {
        if (jQuery.isEmptyObject(itemList)) {
            console.log("itemList为空");
            return;
        }
        var table = $("<table>").attr("class", "table table-bordered table-striped");
        var thead = $("<thead>");
        var htr = $("<tr>");

        var fields = ["${msg['jsp.itemDictionary.itemId']}",
            "${msg['jsp.itemDictionary.itemName']}",
            "${msg['jsp.itemDictionary.itemMax']}"];
        for (var field in fields) {
            var th = $("<th>").text(fields[field]);
            htr.append(th);
        }
        thead.append(htr);
        table.append(thead);

        var tbody = $("<tbody>");
        for (var i = 0; i < itemList.length; i++) {
            var item = itemList[i];
            var tr = $("<tr>").append($("<td>").text(item.itemId))
                .append($("<td>").text(item.itemName))
                .append($("<td>").text(item.itemType));
            tbody.append(tr);
        }
        table.append(tbody);
        $("#itemInfo").html(table);
    }

    function exportExcel() {
        $("#itemInfo table").eq(0).tableExport({type: 'excel', escape: 'false'});
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <label for="itemId" class="label">${msg['jsp.itemDictionary.itemId']}</label>
        <input type="text" id="itemId" name="itemId" class="span1 validate[required,custom[integer]]">
        <input type="button" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="queryItem()">
        <input type="button" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel()">
    </form>
    <div id="msg"></div>
    <div id="itemInfo"></div>
</div>

<jsp:include page="../../commonmodal.jsp"/>
</body>
</html>