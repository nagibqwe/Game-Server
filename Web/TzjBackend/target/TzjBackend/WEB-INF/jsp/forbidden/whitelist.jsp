<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>白名单管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        group_reload()
        queryPage(1);
    });
    function writeWhiteList() {
        if ($('#whiteListForm').validationEngine('validate')) {
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.post(base + "/forbidden/writeWhiteList", $("#whiteListForm").serialize(),
                function (data) {
                    $("#loadingmodal").modal('hide');
                    $("#messResult").html(data.msg);
                });
        } else {
            alert("${msg['forbid.dataerror']}");
        }
    }

    function deleteWhiteList() {
        if ($('#whiteListForm').validationEngine('validate')) {
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.post(base + "/forbidden/deleteWhiteList", $("#whiteListForm").serialize(),
                function (data) {
                    $("#loadingmodal").modal('hide');
                    $("#messResult").html(data.msg);
                });
        } else {
            alert("${msg['forbid.dataerror']}");
        }
    }

    function queryPage(page) {
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.post(base + "/forbidden/queryWhiteList", {"lsId": $("#lsId").val(), pageNumber: page},
            function (data) {
                $("#loadingmodal").modal('hide');
                if (data == null) {
                    alert("error");
                }
                data = data.data;
                var content = "<table class=\"table table-bordered table-striped\">\
                    <tr>\
                      <td>${msg['whitelist.jsp.condition']}</td>\
                      <td>${msg['whitelist.jsp.type']}</td>\
                      <td>${msg['announce.jsp.createtime']}</td>\
                      <td>${msg['announce.jsp.createUser']}</td>\
                      <td>${msg['whitelist.jsp.ip']}</td>\
                      <td>${msg['whitelist.jsp.loginback']}</td>\
                      <td>${msg['jsp.server.deal']}</td>\
                    </tr>";

                var prev = "${msg['jsp.prev.page']}";
                var ls = data.pager.pageNumber - 1;
                if (data.pager.pageNumber > 1) {
                    prev = "<a href='#' onclick='queryPage(" + ls + ");'>上一页</a>";
                }

                var next = "${msg['jsp.next.page']}";
                ls = data.pager.pageNumber + 1;
                if (data.pager.pageNumber != data.pager.pageCount) {
                    next = "<a href='#' onclick='queryPage(" + ls + ");'>下一页</a>";
                }


                var pageList = "<tr><td aligen='center' colspan=7>"
                    + "${msg['jsp.server.gong']}" + data.pager.recordCount + "${msg['forbid.chat.tiao']}"
                    + data.pager.pageCount + "${msg['jsp.server.page']},"
                    + "${msg['forbid.chat.currpage']}" + data.pager.pageNumber + "${msg['forbid.chat.currpageend']}"
                    + data.pager.pageSize + "${msg['forbid.chat.tiao']}&nbsp;&nbsp;";

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
                            pageList += "&nbsp;<a href='#' onclick='queryPage(" + k + ");'>" + k + "</a>";
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

                for (var i = 0; i < data.list.length; ++i) {
                    var whiteList = data.list[i];
                    var tmp = "<tr><td>" + whiteList.whiteCon + "\
                        </td><td>" + (whiteList.ctype == 0 ? "${msg['whitelist.allowLogin']}" : "${msg['whitelist.notallowLogin']}") + "</td>\
                        <td>" + whiteList.createtime + "</td>\
                        <td>" + whiteList.userName + "</td>\
                        <td>" + whiteList.userIP + "</td>\
                        <td>" + whiteList.backStr + "</td>\
                        <td><input type='button' onclick='unDelete(" + whiteList.id + "," + page + ");' value='" + (whiteList.ctype == 1 ? "${msg['whitelist.allowLogin']}" : "${msg['whitelist.notallowLogin']}") + "'></input> </td>\
                        </tr>";
                    content += tmp;
                }
                content += pageList;
                content += "</table>";
                $("#messResult").html(content);
                $("#loadingmodal").modal('hide');
            });
    }

    function unDelete(id, page) {
        $.post(base + "/forbidden/updateWhileList", {id: id},
            function (data) {
                $("#dealResult").html(data.msg);
                if (data.ok) {
                    queryPage(page);
                }
            }
        );
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="whiteListForm" method="post" class="well form-inline">
        <select id="select_group" name="groupName" class="span2"></select>
        <label for="condition" class="label">${msg['whitelist.condition']}</label>
        <input type="text" name="condition" id="condition" class="input=medium validate[required]"/>
        <br/>
        <label>${msg['forbid.user.conditioninfo']}</label>
        <br/>
        <input type="button" class="btn btn-primary" value="${msg['whitelist.allowLogin']}" onclick="writeWhiteList()"/>
        <input type="button" class="btn btn-primary" value="${msg['whitelist.notallowLogin']}" onclick="deleteWhiteList()"/>
    </form>
</div>

<div class="container-fluid" id="dealResult"></div>

<div class="container-fluid" id="messResult"></div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>