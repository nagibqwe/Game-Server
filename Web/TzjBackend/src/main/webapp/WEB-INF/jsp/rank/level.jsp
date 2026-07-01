<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="${base}/js/global.js"></script>
    <script type="text/javascript" src="${base}/js/tableExport.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
    <script type="text/javascript">
        var base = '${base}';
        $(function () {
            group_reload();
            $("#loading").shCircleLoader();
        });

        function search() {
            $("#rank").show();
            $("#loadingmodal").modal({
                backdrop: 'static',
                keyboard: false
            });
            $.ajax({
                type: "POST",
                url: base + "/rank/getRank",
                data: {
                    "serverId": $("#select_server").val(),
                    "rankType": $("#rankType").val()
                },
                dataType: "json",
                success: function (data) {
                    $("#loadingmodal").modal('hide');
                    if (!data.ok) {
                        $("#rank").html(data.msg);
                        return;
                    }
                    var rankType = $("#rankType").val();
                    var tableHtml = "<table class='table table-bordered table-striped'>" +
                        "<thead><tr>" +
                            "<td>${msg['jsp.rank.rank']}</td>" +
                            "<td>${msg['jsp.rank.roleid']}</td>" +
                            "<td>${msg['jsp.rank.rolename']}</td>" +
                            "<td>" + getRankTypeFieldInfo(rankType) +"</td>" +
                        "</tr></thead>" +
                        "<tbody>";
                    for (var key in data.data) {
                        var rankInfo = data.data[key]
                        tableHtml += "<tr><td>" + rankInfo.rank + "</td>" +
                            "<td>" + rankInfo.roleId + "</td>" +
                            "<td>" + rankInfo.roleName + "</td>" +
                            "<td>" + rankInfo.rankData + "</td></tr>";
                    }
                    tableHtml += "</tbody></table>";
                    $("#rank").html(tableHtml);
                }
            });
        }

        function getRankTypeFieldInfo(rankType) {
            switch (rankType) {
                case 101:
                    return "等级";
                default:
                    return "排行数据"
            }
        }

        function exportExcel() {
            $("#rank table").eq(0).tableExport({type: 'excel', escape: 'false'});
        }
    </script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <select id="select_group" onchange="queryServerByGroup(this.value)" class="span2"></select>
        <select id="select_server" class="span2"></select>
        <select id="rankType" name="rankType" class="span2">
            <option value="101" selected>等级排行榜</option>
            <option value="102">战力排行榜</option>
            <option value="103">坐骑排行榜</option>
            <option value="104">仙羽战力排行榜</option>
            <option value="105">装备战力排行榜</option>
            <option value="106">法宝战力排行榜</option>
            <option value="107">法器战力排行榜</option>
            <option value="108">阵法战力排行榜</option>
            <option value="109">神兵战力排行榜</option>
            <option value="110">宝石战力排行榜</option>
            <option value="111">脱机效率排行榜</option>
            <option value="112">装备洗练排行榜</option>
            <option value="112">装备强化等级排行榜</option>
            <option value="113">宝石等级排行榜</option>
            <option value="114">装备总星级排行榜</option>
            <option value="102">竞技场排行榜</option>
            <option value="202">识海排行榜</option>
            <option value="301">宗派战力排行榜</option>
        </select>
        <input type="button" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();"/>
        <input type="button" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel();"/>
    </form>
    <div id="msg"></div>
    <div id="rank"></div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>