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
    <script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
    <script type="text/javascript">
        var base = '${base}';
        $(function () {
            group_reload();
            $("#loading").shCircleLoader();
        });

        function search() {
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.ajax({
                type: "POST",
                url: base + "/log/guild",
                data: $("#query_form").serialize(),
                dataType: "json",
                success: function (data) {
                    $("#loadingmodal").modal('hide');
                    if (!data.ok) {
                        $("#guild").html(data.msg);
                        return;
                    }

                    var table = $("<table class='table table-bordered table-striped'>");
                    var thead = $("<thead>");
                    var tr = $("<tr>");
                    var fields = ["${msg['jsp.guild.id']}",
                        "${msg['jsp.guild.guildname']}",
                        "${msg['jsp.guild.action']}"];
                    for (var field in fields) {
                        var th = $("<th>").text(fields[field]);
                        tr.append(th);
                    }
                    thead.append(tr);
                    table.append(thead);

                    var tbody = $("<tbody>");
                    for (var key in data.data) {
                        var guild = data.data[key];
                        var dtr = $("<tr>");
                        var datalist = [guild.guildId, guild.guildName];
                        for (var i in datalist) {
                            var td = $("<td>").text(datalist[i]);
                            dtr.append(td);
                        }
                        var button1 = $("<input type='button' class='btn btn-primary' value='${msg['jsp.guild.guildMember']}' onclick='searchGuildMember(this)'/>");
                        var button2 = $("<input type='button' class='btn btn-primary' value='${msg['jsp.guild.guildDynamic']}' onclick='searchGuildChange(this)'/>");
                        dtr.append($("<td>").append(button1).append("&nbsp;&nbsp;").append(button2));
                        tbody.append(dtr);
                    }
                    table.append(tbody);
                    $("#guild").html(table);
                }
            });
        }

        function searchGuildMember(obj) {
            var guildId = $(obj).parent().parent().find("td").first().text();
            console.log(guildId);
            window.location.href = base + "/log/guildpage?type=1&menuId=67" +
                "&groupName=" + $("#select_group").val() + "&serverId=" + $("#select_server").val() + "&guildId=" + guildId;
        }

        function searchGuildChange(obj) {
            var guildId = $(obj).parent().parent().find("td").first().text();
            console.log(guildId);
            window.location.href = base + "/log/guildpage?type=2&menuId=68" +
                "&groupName=" + $("#select_group").val() + "&serverId=" + $("#select_server").val() + "&guildId=" + guildId;
        }
    </script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <select id="select_group" onchange="reloadNoHeFuDBs(this.value)" class="span2"></select>
        <select id="select_server" name="serverId" class="span2"></select>
        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search();"/>
    </form>
    <div id="msg"></div>
    <div id="guild"></div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>