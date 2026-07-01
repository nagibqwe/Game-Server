<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>游戏信息</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript">
    var initPageNumber = 1;
    var initPageSize = 20;
    var base = '${base}';
    $(function () {
        $("#serverAddModel").hide();
        paging(initPageNumber);

        $(".datetimepicker").datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm-dd hh:ii:00',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            startView: 2,
            minView: 0
        });

        $("#addMod").click(function () {
            $.post(base + "/gameInfo/add", $("#serverMod").serialize(), function (data) {
                if (data.ok) {
                    paging(initPageNumber);
                    $("#serverMod")[0].reset();
                    $("#addMod").val("${msg['jsp.server.sumbit']}");
                } else {
                    alert(data.msg);
                }

            });
        });
    });

    function search() {
        if ($("#server_query_form").validationEngine('validate')) {
            paging(initPageNumber);
        }
    }

    function paging(page) {
        var query_gameId = $("#query_gameId").val();
        // var name = $("#name").val().trim();
        var pageSize = $("#pageSize").val().trim();


        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            type: "POST",
            url: base + "/gameInfo/query",
            data: {
                "query_gameId": query_gameId,
                "pageNumber": page,
                "pageSize": pageSize
            },
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                $("#user_count").html("${msg['jsp.server.gong']}" +
                    data.pager.recordCount + "${msg['jsp.server.serverNum']}" +
                    data.pager.pageCount + "${msg['jsp.server.page']}");

                var list_html = "<table class=\"table table-bordered table-striped\"><tr>\
                    <td>游戏ID</td>\
                    <td>充值配置密钥</td>\
                    <td>自动开服检查范围起始服ID</td>\
                    <td>自动开服注册人数条件</td>\
                    <td>自动开服进度</td>\
                    <td>最后修改时间</td><td>操作</td></tr>";

                for (var i = 0; i < data.list.length; i++) {
                    var ser = data.list[i];
                    var tmp = "<tr>\
                        <td>&nbsp;" + ser.gameId + "</td>\
                        <td>&nbsp;" + ser.rechargeSecretkey + "</td>\
                        <td>&nbsp;" + ser.autoFirstServerId + "</td>\
                        <td>&nbsp;" + ser.autoUserCount + "</td>\
                        <td>&nbsp;" + ser.autoServerId + "</td>\
                        <td>&nbsp;" + ser.time + "</td>\
                        <td><button class='btn btn-warning' onclick='user_update(" + ser.gameId
                        + ");'>${msg['jsp.server.mod']}</button> "
                        + " <button class='btn btn-danger' onclick='user_delete(" + ser.gameId
                        + ");'>${msg['jsp.server.del']}</button></td></tr>";

                    list_html += tmp;
                }
                list_html += "\n</table>";
                $("#user_list").html(list_html);

                var pages = data.pager.pageCount;//这里data里面有数据总量
                var options = {
                    bootstrapMajorVersion: 2,
                    currentPage: page,//当前页面
                    numberOfPages: 5,//一页显示几个按钮（在ul里面生成5个li）
                    totalPages: pages, //总页数
                    itemTexts: function (type, page, current) {
                        switch (type) {
                            case "first":
                                return "首页";
                            case "prev":
                                return "上一页";
                            case "next":
                                return "下一页";
                            case "last":
                                return "末页";
                            case "page":
                                return page;
                        }
                    }
                };
                $("#pageUl").bootstrapPaginator(options);
            }
        });
    }

    function user_update(gameId) {
        $.ajax({
            url: base + "/gameInfo/queryByGameId",
            data: {
                "gameId": gameId
            },
            dataType: "json",
            success: function (data) {
                $("#serverAddModel").show();
                $("#addMod").val("修改");
                console.log(data);
                $("#gameId").val(data.gameId);
                $("#rechargeSecretkey").val(data.rechargeSecretkey);
                $("#autoFirstServerId").val(data.autoFirstServerId);
                $("#autoUserCount").val(data.autoUserCount);
                $("#autoServerId").val(data.autoServerId);
                $("#time").val(data.time/1000);
            }
        });
    }

    function user_delete(gameId) {
        var s = prompt("${msg['jsp.server.delete']}");
        if (s == "y") {
            $.post(base + "/gameInfo/delete", {"gameId": gameId}, function (data) {
                if (data.ok) {
                    paging(initPageNumber);
                    alert("${msg['jsp.server.deleteOk']}");
                } else {
                    alert(data.msg);
                }
            });
        }
    }

    function showServerAddModel() {
        var isHidden = $("#serverAddModel").is(":hidden");
        if (isHidden) {
            $("#serverAddModel").show('slow');
        } else {
            $("#serverAddModel").hide('slow');
        }
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="server_query_form" class="well form-inline">
        <input type="text" id="query_gameId" name="query_gameId" placeholder="游戏ID">
        <label class="label">${msg['jsp.server.perPage']}</label>
        <input type="text" id="pageSize" name="pageSize" value="10" class="span1 validate[required,custom[integer],min[1]]">
        <button id="server_query_btn" type="button" class="btn btn-primary" onclick="search();"><i class="icon-search icon-white"></i></button>
        <input type="button" class='btn btn-info' onclick='showServerAddModel();' value="${msg['jsp.add']}">
    </form>

    <p id="user_count"></p>
    <div id="user_list"></div>
    <div class="pagination" id="pageUl"></div>
</div>

<div id="serverAddModel" class="container-fluid">
    <form id="serverMod" name="serverMod" method="post" action="#" class="well">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td>游戏ID：</td>
                <td><input name="gameId" type="text" id="gameId" placeholder="游戏ID"/>
                </td>
                <td>充值配置密钥：</td>
                <td><input name="rechargeSecretkey" type="text" id="rechargeSecretkey" placeholder="充值配置密钥"/></td>
            </tr>
            <tr>
                <td>自动开服检查范围起始服ID：</td>
                <td><input name="autoFirstServerId" type="text" id="autoFirstServerId" placeholder="自动开服起始服务器ID"/></td>
                <td>自动开服注册人数条件：</td>
                <td><input name="autoUserCount" type="text" id="autoUserCount" placeholder="自动开服注册人数条件,不检查填0"/></td>
            </tr>
            <tr>
                <td>自动开服进度：</td>
                <td><input name="autoServerId" type="text" id="autoServerId" placeholder="自动开服截止服务器"/></td>
            </tr>
        </table>
        <input type="button" class="btn btn-primary" id="addMod" value="${msg['jsp.add']}"/>
    </form>
</div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>