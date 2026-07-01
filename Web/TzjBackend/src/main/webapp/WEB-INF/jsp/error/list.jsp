<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>错误日志显示页</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        queryPage(1);
        $('.date').datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm-dd hh:ii:ss',
            todayBtn: 1,
            endDate: new Date(),
            minView: 3,
            autoclose: true
        });
    });

    function queryPage(page) {
        var type = $("#mType").val();
        var timeBegin = $("#msdate").val();
        var timeEnd = $("#medate").val();
        var key = $("#mkey").val();
        var serverId = $("#serverId").val();

        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            url: base + "/error/query",
            type: 'post',
            dataType: 'json',
            data: {
                pageNumber: page,
                pageSize: 40,
                reKey: key,
                type: type,
                begin: timeBegin,
                end: timeEnd,
                serverId: serverId
            },
            success: function (data) {
                $("#loadingmodal").modal('hide');
                var content = "<table class=\"table table-bordered table-striped\">\
                    <tr>\
                      <td>${msg['jsp.server']}</td>\
                      <td>${msg['announce.jsp.createtime']}</td>\
                      <td>${msg['jsp.server.groupname']}</td>\
                      <td>${msg['error.type']}</td>\
                      <td>${msg['error.title']}</td>\
                      <td>${msg['error.content']}</td>\
                      <td>${msg['error.current.value']}</td>\
                      <td>${msg['error.deal']}</td>\
                    </tr>";

                var prev = "上一页";
                var ls = data.pager.pageNumber - 1;
                if (data.pager.pageCount > 0 && data.pager.pageNumber > 1) {
                    prev = "<a href='#' onclick='queryPage(" + ls + ");'>上一页</a>";
                }

                var next = "下一页";
                ls = data.pager.pageNumber + 1;
                if (data.pager.pageCount > 0 && data.pager.pageNumber != data.pager.pageCount) {
                    next = "<a href='#' onclick='queryPage(" + ls + ");'>下一页</a>";
                }

                var pageList = "<tr><td aligen='center' colspan=8>" +
                    "${msg['jsp.server.gong']}"
                    + data.pager.recordCount
                    + "${msg['forbid.chat.tiao']}"
                    + data.pager.pageCount
                    + "${msg['jsp.server.page']},${msg['forbid.chat.currpage']}"
                    + data.pager.pageNumber
                    + "${msg['forbid.chat.currpageend']}"
                    + data.pager.pageSize
                    + "${msg['forbid.chat.tiao']}&nbsp;&nbsp;";

                pageList += prev;
                if (data.pager.pageCount < 20) {
                    for (var k = 1; k <= data.pager.pageCount; ++k) {
                        if (k != data.pager.pageNumber) {
                            pageList += "&nbsp;<a href='#' onclick='queryPage(" + k + ");'>" + k + "</a>";
                        } else {
                            pageList += "&nbsp;" + k;
                        }
                    }
                } else {
                    var ben = data.pager.pageNumber - 10;
                    if (ben < 1) {
                        ben = 1;
                    } else {
                        pageList += "...";
                    }

                    var end = data.pager.pageNumber + 10;
                    if (end > data.pager.pageCount) {
                        end = data.pager.pageCount;
                    }

                    for (var k = ben; k <= end; ++k) {
                        if (k != data.pager.pageNumber) {
                            pageList += "&nbsp;<a href='#' onclick='queryPage("
                                + k + ");'>" + k + "</a>";
                        } else {
                            pageList += "&nbsp;" + k;
                        }
                    }

                    if (end != data.pager.pageCount) {
                        pageList += "...";
                    }
                }
                pageList += "&nbsp;&nbsp;" + next;
                pageList += "</td></tr>";

                content = "<table class=\"table table-bordered table-striped\">"
                    + pageList + "</table>" + content;

                for (var i = 0; i < data.list.length; ++i) {
                    var errorLog = data.list[i];

                    var tmp = "<tr><td>" + errorLog.serverId + "</td>" +
                        "<td>" + errorLog.receTime + "</td>" +
                        "<td>" + errorLog.platform + "</td>" +
                        "<td>" + errorLog.type + "</td>" +
                        "<td>" + errorLog.mKey + "</td>" +
                        "<td>" + errorLog.content + "</td>" +
                        "<td>" + errorLog.lastValue + "</td>" +
                        "<td><input type='button' onclick='unDelete(" + errorLog.id + "," + page + ");' value='隐藏当前收到此类值'/></td></tr>";
                    content += tmp;
                }
                content += pageList;
                content += "</table>";
                $("#content").html(content);
            }
        });
    }

    function unDelete(id) {
        $.post(base + "/error/delete", {id: id},
            function (data) {
                alert(data.msg);
                if (data.ok) {
                    queryPage(1);
                }
        });
    }
</script>
</head>
<body>
<div class="container-fluid">
    <label class="label label-info">${msg['errot.report']}</label>
    <form class="well form-inline">
        <label for="serverId">${msg['jsp.server']}</label>
        <input id='serverId' type='text' style="width: 30px" size="2"/>
        <label for="mType">${msg['error.type']}</label>
        <input id='mType' type='text' style="width: 50px"/>
        <label for="mkey">${msg['error.title']}</label>
        <input id='mkey' type='text' class=''/>
        <label for="msdate">${msg['announce.jsp.begintime']}</label>
        <div class="input-append date">
            <input id="msdate" style="width: 120px;" size="20" type="text" value="${yesDate}"  readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <label for="medate">${msg['announce.jsp.endtime']}</label>
        <div class="input-append date">
            <input id="medate" style="width: 120px;" size="20" type="text" value="${nowDate}"  readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        <input type="button" class="btn btn-primary" value="${msg['jsp.choice']}" onclick="queryPage(1)"/>
    </form>
</div>
<div class="container-fluid" id="content"></div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>