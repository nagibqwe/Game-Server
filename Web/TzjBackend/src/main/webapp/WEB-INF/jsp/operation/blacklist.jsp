<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<title>${msg['jsp.blacklist.pagetitle']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-confirmation.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var base = '${base}';
    var pageNumber = 1;
    var pageSize = 20;
    $(function () {
        search(pageNumber, pageSize);
        reloadNoHeFuServerGroups(false);
    });

    function importBlackList() {
        $("#importBlackList").modal('toggle');
    }

    function convertBlackList() {
        $("#convertBlackList").modal('toggle');
    }

    function search(pageNumber, pageSize) {
        $.ajax({
            url: base + "/operation/getBlackList",
            data: {
                condition: $("#condition").val(),
                pageNumber: pageNumber,
                pageSize: pageSize
            },
            method: "post",
            async: false,
            dataType: "json",
            success: function (data) {
                var data = data.data;
                var pager = data.pager;
                var content = "<table class='table table-bordered table-striped'>\
                    <thead><tr align='center'>\
                    <td>${msg['jsp.blacklist.number']}</td>\
                    <td>${msg['jsp.blacklist.userId']}</td>\
                    <td>${msg['jsp.blacklist.platform']}</td>\
                    <td>${msg['jsp.server.deal']}</td>\
                    </tr></thead><tbody>";
                for (var i = 0; i < data.list.length; i++) {
                    content += "<tr align='center'>";
                    content += "<td>" + data.list[i].id + "</td>";
                    content += "<td>" + data.list[i].userNumberStr + "</td>";
                    content += "<td>" + data.list[i].platform + "</td>";
                    content += "<td><button class='btn btn-danger' onclick='deleteBlack(" + data.list[i].id + ")'>${msg['jsp.server.del']}</button></td>";
                    content += "</tr>";
                }
                content += "</tbody></table>";
                $("#black_list").html(content);

                var pageInfo = "一共<span style='color: blue;'>" + pager.pageCount + "</span>页，当前第<span style='color: blue;'>" + pager.pageNumber + "</span>页";
                pageInfo += "<a href=javascript:initDatalist(" + (pager.pageNumber - 1) + "," + pager.pageCount + "');>${msg['jsp.blacklist.previouspage']}</a>";
                pageInfo += "&nbsp;&nbsp;&nbsp;";
                pageInfo += "<a href=javascript:initDatalist(" + (pager.pageNumber + 1) + "," + pager.pageCount + ",'" + condition + "');>${msg['jsp.blacklist.nextpage']}</a>";
                $("#page_info").html(pageInfo);
            }
        });
    }

    function deleteBlack(id) {
        $.ajax({
            url: base + "/operation/deleteBlack",
            data: {
                id: id
            },
            method: "post",
            async: false,
            dataType: "json",
            success: function (data) {
                alert(data.msg);
                if (data.ok) {
                    search(pageNumber, pageSize);
                }
            }
        });
    }
</script>
</head>
<body>
<div class="container-fluid">
    <div class="container-fluid well form-inline">
        <label for="condition" class="label">${msg['jsp.blacklist.list']}</label>
        <input id="condition" type="text" placeholder="search"/>
        <button type="button" class="btn btn-primary" onclick="search()"><i class="icon-search icon-white"></i></button>
        <button class="btn btn-warning" onclick="convertBlackList()">黑名单转换</button>
        <button class="btn btn-danger" onclick="importBlackList()">${msg['jsp.blacklist.export']}</button>
    </div>

    <div id="black_list" ></div>
    <div id="page_info"></div>
</div>

<div id="convertBlackList" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>黑名单转换</h3>
    </div>
    <form class="form-horizontal" id="convertExl" method="post" enctype="multipart/form-data" action="${base}/operation/blackListConvert">
        <div class="modal-body">
                <div class="control-group">
                    <label for="select_group" class="control-label">${msg['jsp.server']}</label>
                    <div class="controls">
                        <select id="select_group" name="platform" onchange="queryServerByGroup(this.value)" class="span2"></select>
                        <select id="select_server" name="serverId" class="span2"></select>
                    </div>
                </div>
                <div class="control-group">
                    <label for="select_server" class="control-label">${msg['jsp.blacklist.selectfile']}</label>
                    <div class="controls">
                        <input id="file" name="file" type="file"/>
                    </div>
                </div>
        </div>
        <div class="modal-footer">
            <input type="submit" class="btn btn-primary" value="${msg['jsp.blacklist.button.sure']}"/>
            <input type="button" class="btn btn-default"  data-dismiss="modal" aria-hidden="true" value="${msg['jsp.blacklist.button.close']}"/>
        </div>
    </form>
</div>

<div id="importBlackList" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>${msg['jsp.blacklist.export']}</h3>
    </div>
    <form class="form-horizontal" id="uploadExl" method="post" enctype="multipart/form-data" action="${base}/operation/upLoadBlackExcl">
        <div class="modal-body">
            <div class="control-group">
                <label class="control-label">${msg['jsp.blacklist.selectfile']}</label>
                <div class="controls">
                    <input name="file" type="file"/>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <input type="submit" class="btn btn-primary" value="${msg['jsp.blacklist.button.sure']}"/>
            <input type="button" class="btn btn-default" value="${msg['jsp.blacklist.button.close']}" data-dismiss="modal"/>
        </div>
    </form>
</div>
</body>
</html>
