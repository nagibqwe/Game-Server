<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>禁言解除</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        queryPage(1);
    });

    function unForbid(id, page) {
        $.post(base + "/forbidden/unforbidPlayerChat", {id: id},
            function (data) {
                alert(data.msg);
                if (data.ok) {
                    queryPage(page);
                }
            });
    }

    function queryPage(page) {
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.post(base + "/forbidden/forbidPlayerChatList", {pageNumber: page, pageSize: 20},
            function (data) {
                $("#loadingmodal").modal('hide');
                data = data.data;
                var content = "<table class=\"table table-bordered table-striped\">\
                    <tr>\
                      <td>${msg['forbid.chat.userId']}</td>\
                      <td>${msg['forbid.chat.crime']}</td>\
                      <td>${msg['forbid.chat.type']}</td>\
                      <td>${msg['forbid.chat.createTime']}</td>\
                      <td>${msg['forbid.chat.endtime']}</td>\
                      <td>${msg['forbid.chat.backCrname']}</td>\
                      <td>${msg['forbid.chat.reason']}</td>\
                      <td>${msg['forbid.chat.dealServer']}</td>\
                      <td>${msg['jsp.server.deal']}</td>\
                    </tr>";
                for (var i = 0; i < data.list.length; ++i) {
                    var forchat = data.list[i];
                    var tmp = "<tr><td>" + forchat.userId + "</td>\
                        <td>" + getCrimeType(forchat.crimeType) + "</td>\
                        <td>" + getForbidType(forchat.forbidType) + "</td>\
                        <td>" + forchat.createTime + "</td>\
                        <td>" + forchat.endTime + "</td>\
                        <td>" + forchat.backUserName + "</td>\
                        <td>" + forchat.reason + "</td>\
                        <td>" + forchat.serverIds + "</td>\
                        <td><input type='button' class='btn btn-primary' onclick='unForbid(" + forchat.id + "," + page + ");' value='${msg['forbid.chat.jiechu']}'/></td></tr>";
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

    function getCrimeType(type){
        switch (type) {
            case 1:
                return "${msg['forbid.chat.crime1']}";
            case 2:
                return "${msg['forbid.chat.crime2']}";
            default:
                return "${msg['jsp.chat.unknown']}"+channel;
        }
    }

    function getForbidType(type){
        switch (type) {
            case 1:
                return "${msg['forbid.chat.type1']}";
            case 2:
                return "${msg['forbid.chat.type2']}";
            case 3:
                return "${msg['forbid.chat.type3']}";
            case 4:
                return "${msg['forbid.chat.type4']}";
            case 5:
                return "${msg['forbid.chat.type5']}";
            case 6:
                return "${msg['forbid.chat.type6']}";
            default:
                return "${msg['jsp.chat.unknown']}"+channel;
        }
    }

</script>
</head>
<body>
<br/>
<div class="container-fluid" id="content"></div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>