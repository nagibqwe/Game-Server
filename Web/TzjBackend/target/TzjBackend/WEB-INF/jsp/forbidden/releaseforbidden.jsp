<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>账号解封</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        group_reload()
        queryPage(1);
        $('.datetimepicker').datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm-dd hh:ii:ss',
            todayBtn: 1,
            autoclose: true
        });
    });

    function unForbid(id, page) {
        $.post(base + "/forbidden/unForbidUser",
            {
                "groupName" : $("#select_group").val(),
                "id": id
            },
            function (data) {
                alert(data.msg);
                if (data.ok) {
                    queryPage(page);
                }
            });
    }

    function queryPage(page) {
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.post(base + "/forbidden/forbidUserList",
            {queryString: $("#queryString").val().trim(), pageNumber: page, pageSize: 20},
            function (data) {
                $("#loadingmodal").modal('hide');
                var content = "<table class=\"table table-bordered table-striped\">\
                    <tr>\
                      <td>${msg['forbid.user.userid']}</td>\
                      <td>${msg['forbid.chat.createTime']}</td>\
                      <td>${msg['forbid.user.endtime']}</td>\
                      <td>${msg['forbid.chat.backCrname']}</td>\
                      <td>${msg['forbid.chat.reason']}</td>\
                      <td>${msg['forbid.user.ls']}</td>\
                      <td>${msg['forbid.user.lsResult']}</td>\
                      <td>${msg['jsp.server.deal']}</td>\
                    </tr>";
                for (var i = 0; i < data.list.length; ++i) {
                    var forchat = data.list[i];
                    var tmp = "<tr><td>" + forchat.userId + "\
                        </td><td>" + forchat.createTime + "</td>\
                        <td>" + forchat.endTime + "</td>\
                        <td>" + forchat.backUserName + "</td>\
                        <td>" + forchat.reason + "</td>\
                        <td>" + forchat.serverIds + "</td>\
                        <td>" + forchat.backStr + "</td>\
                        <td><input type='button' class='btn btn-info' onclick='unForbid(" + forchat.id + "," + page + ")' value='${msg['forbid.user.jiechu']}'/></td></tr>";
                    content += tmp;
                }

                var pageList = "<tr><td aligen='center' colspan=8>"
                    + "${msg['jsp.server.gong']}" + data.pager.recordCount + "${msg['forbid.chat.tiao']}"
                    + data.pager.pageCount + "${msg['jsp.server.page']},"
                    + "${msg['forbid.chat.currpage']}" + data.pager.pageNumber + "${msg['forbid.chat.currpageend']}"
                    + data.pager.pageSize + "${msg['forbid.chat.tiao']}";
                for (var k = 1; k <= data.pager.pageCount; ++k) {
                    if (k != data.pager.pageNumber) {
                        pageList += "&nbsp;<a href='#' onclick='queryPage(" + k + ");'>" + k + "</a>";
                    } else {
                        pageList += "&nbsp;" + k;
                    }
                }

                pageList += "</td></tr>";
                content += pageList;
                content += "</table>";
                $("#content").html(content);
            });
    }

</script>
</head>
<body>
<div class="container-fluid">
    <div class="well form-inline">
        <select id="select_group" name="groupName" class="span2"></select>
        <label for="queryString">${msg['forbid.user.userid']}</label><input id="queryString" type="text"/>
        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="queryPage(1)"/>
    </div>
</div>
<div class="container-fluid" id="content"></div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>