<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.dblog.title']}</title>
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
    var type = ${type};
    $(function () {
        $("#pageSize").val(initPageSize);
        $("#loading").shCircleLoader();
        paging(initPageNumber);
        $("#user_add").hide();
    });

    function search() {
        if ($("#serverDB_query_form").validationEngine('validate')) {
            paging(initPageNumber);
        }
    }

    function paging(page) {
        var groupName = $("#groupName").val().trim();
        var name = $("#name").val().trim();
        var pageSize = $("#pageSize").val().trim();

        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        $.ajax({
            type: "POST",
            url: base + "/dblog/query",
            data: {
                "groupName": groupName,
                "name": name,
                "type": type,
                "pageNumber": page,
                "pageSize": pageSize
            },
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                $("#user_count").html("${msg['jsp.server.gong']}" + data.pager.recordCount + "${msg['jsp.server.serverNum']}" + data.pager.pageCount + "${msg['jsp.server.page']}");
                var list_html = "<table class=\"table table-bordered table-striped\" border='0' cellspacing='0' cellpadding='0'>\
            <tr>\
                <td>${msg['jsp.server.id']}</td>\
                <td>${msg['jsp.server.name']}</td>\
                <td>${msg['jsp.server.groupname']}</td>\
                <td>${msg['jsp.dblog.dblogurl']}</td>\
                <td>${msg['jsp.dblog.dblogname']}</td>\
                <td>${msg['jsp.dblog.dbloguser']}</td>\
                <td>${msg['jsp.dblog.serverlist']}</td>\
                <td>是否合服</td>\
                <td>合到的区服ID</td>\
                <td>合到时间</td>\
                <td>${msg['jsp.dblog.serverType']}</td>\
                <td>类型</td>\
                <td>是否生效</td>\
                <td>${msg['jsp.server.deal']}</td>\
            </tr>";
                for (var i = 0; i < data.list.length; i++) {
                    var ser = data.list[i];
                    var tmp = "<tr>\
                <td>&nbsp;" + ser.serverId + "</td>\
                <td>&nbsp;" + ser.serverName + "</td>\
                <td>&nbsp;" + ser.groupName + "</td>\
                <td>&nbsp;" + ser.serverIpPort + "</td>\
                <td>&nbsp;" + ser.dbname + "</td>\
                <td>&nbsp;" + ser.dbuser + "</td>\
                <td>&nbsp;" + ser.owerlist + "</td>\
                <td>&nbsp;" + ser.isHeFu + "</td>\
                <td>&nbsp;" + ser.hefuServerID + "</td>\
                <td>&nbsp;" + ser.hefuTime + "</td>\
                <td>&nbsp;" + getServerType(ser.serverType) + "</td>\
                <td>&nbsp;" + getType(ser.type) + "</td>\
                <td>&nbsp;" + isDeleted(ser.isDeleted) + "</td>\
                <td><button class='btn btn-warning' onclick='user_update(" + ser.id + ");'>${msg['jsp.server.mod']}</button> "
                        + " <button class='btn btn-danger' onclick='user_delete(" + ser.id + ");'>${msg['jsp.server.del']}</button> </td>\
            </tr>";
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

    function dbSubmit() {
        $.post(base + "/dblog/add", $("#serverMod").serialize(), function (data) {
            if (data.ok) {
                paging(initPageNumber);
                $("#serverMod")[0].reset();
                $("#addMod").val("${msg['jsp.server.sumbit']}");
            } else {
                alert(data.msg);
            }
        });
    }

    function user_add() {
        var isHidden = $("#user_add").is(":hidden");
        if (isHidden) {
            $("#user_add").show('slow');
        } else {
            $("#user_add").hide('slow');
        }
        $("#serverMod")[0].reset();
        $("#dbpassword").attr("readonly", false);
        $("#addMod").val("${msg['jsp.add']}");
    }

    function user_update(userId) {
        $("#user_add").show('slow');
        $.ajax({
            url: base + "/dblog/modify",
            data: {
                "id": userId
            },
            dataType: "json",
            success: function (data) {
                if (data.id) {
                    $("#addMod").val("${msg['jsp.server.mod']}");

                    $("#serverId").val(data.serverId);
                    $("#serverName").val(data.serverName);
                    $("#serverIpPort").val(data.serverIpPort);
                    $("#dbname").val(data.dbname);
                    $("#groupName1").val(data.groupName);
                    $("#dbuser").val(data.dbuser);
                    $("#dbpassword").attr("readonly", "readonly").val(data.dbpassword);
                    $("#owerlist").val(data.owerlist);
                    $("#serverType").val(data.serverType);
                    if (data.isDeleted == 1) {
                        $("#isD").prop("checked", "checked");
                        $("#noD").prop("checked", false);
                    } else {
                        $("#isD").prop("checked", false);
                        $("#noD").prop("checked", "checked");
                    }
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    function user_delete(userId) {
        var s = prompt("${msg['jsp.server.delete']}");
        if (s == "y") {
            $.post(
                base + "/dblog/delete",
                {
                    "id": userId
                },
                function (data) {
                    if (data.ok) {
                        paging(initPageNumber);
                        alert("${msg['jsp.server.deleteOk']}");
                    } else {
                        alert(data.msg);
                    }
                });
        }
    }

    function getServerType(type) {
        switch (type) {
            case 1:
                return type + "[正式服]";
            case 2:
                return type + "[登录服]";
            case 3:
                return type + "[跨服]";
            case 4:
                return type + "[战斗服]";
            default:
                return type + "[测试服]";
        }
    }

    function getType(type) {
        switch (type) {
            case 0:
                return type + "[游戏库]";
            case 1:
                return type + "[日志库]";
        }
        return "";
    }

    function isDeleted(type) {
        if (type == 1) {
            return type + "[无效]";
        }
        return type + "[有效]";
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form id="serverDB_query_form" class="well form-inline">
        <input type="text" id="groupName" name="groupName" placeholder="${msg['jsp.server.groupname']}">
        <input type="text" id="name" name="name" placeholder="${msg['jsp.server.name']}">
        <label class="label">${msg['jsp.server.perPage']}</label>
        <input type="text" id="pageSize" name="pageSize" value="10" class="span1 validate[required,custom[integer],min[1]]"/>
        <button id="server_query_btn" type="button" class="btn btn-primary" onclick="search();"><i class="icon-search icon-white"></i></button>
        <input type="button" class='btn btn-info' onclick='user_add();' value="${msg['jsp.add']}">
    </form>
    <p id="user_count"></p>
    <div id="user_list"></div>
    <div class="pagination" id="pageUl"></div>
</div>

<div id="user_add" class="container-fluid">
    <form id="serverMod" name="serverMod" method="post" action="#" class="well">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td>${msg['jsp.server.areaid']}：</td>
                <td><input name="serverId" type="text" id="serverId"/>
                    <input type="hidden" name="type" id="type" value="${type}"/>
                </td>
                <td>${msg['jsp.server.moreareaid']}</td>
                <td>${msg['jsp.server.name']}：</td>
                <td><input name="serverName" type="text" id="serverName"/>
                </td>
                <td>${msg['jsp.server.morename']}</td>
            </tr>
            <tr>
                <td>${msg['jsp.dblog.dblogurl']}：</td>
                <td><input name="serverIpPort" type="text" id="serverIpPort"/>
                </td>
                <td>${msg['jsp.dblog.dblogurlmore']}</td>
                <td>${msg['jsp.dblog.dblogname']}：</td>
                <td><input name="dbname" type="text" id="dbname"/>
                </td>
                <td>${msg['jsp.dblog.namemore']}</td>
            </tr>
            <tr>
                <td>${msg['jsp.server.groupname']}</td>
                <td><input type="text" name="groupName" id="groupName1"></td>
                <td>${msg['jsp.server.groupmore']}</td>
                <td>${msg['jsp.dblog.dbloguser']}<br/></td>
                <td><input type="text" name="dbuser" id="dbuser"></td>
                <td>${msg['jsp.dblog.usermore']}<br/></td>
            </tr>
            <tr>
                <td>${msg['jsp.dblog.dblogpass']}：<br/></td>
                <td><input type="password" name="dbpassword" id="dbpassword">
                </td>
                <td>${msg['jsp.dblog.passmore']}<br/></td>
                <td>${msg['jsp.dblog.serverlist']}<br/></td>
                <td><input type="text" name="owerlist" id="owerlist"></td>
                <td>${msg['jsp.dblog.serverlistmore']}<br/></td>
            </tr>
            <tr>
                <td>${msg['jsp.dblog.serverType']}：<br/></td>
                <td>
                    <select id="serverType" name="serverType">
                        <option value="0" selected="selected">${msg['jsp.server.testServer']}</option>
                        <option value="1">${msg['jsp.dblog.game']}</option>
                        <option value="2">${msg['jsp.dblog.login']}</option>
                        <option value="3">跨服</option>
                        <option value="4">战斗服</option>
                    </select>
                </td>
                <td>${msg['jsp.dblog.serverTypeMore']}<br/></td>
                <td>&nbsp;<br/></td>
                <td>&nbsp;</td>
                <td>&nbsp;<br/></td>
            </tr>
            <tr>
                <td>是否生效:</td>
                <td>
                    <label class="radio inline">
                        是<input id="noD" value="0" name="isDeleted" type="radio" checked="checked"/>
                    </label>
                    <label class="radio inline">
                        否<input id="isD" value="1" name="isDeleted" type="radio">
                    </label>
                </td>
            </tr>
        </table>
        <input name="addMod" class="btn btn-primary" type="button" id="addMod" onclick="dbSubmit();" value="${msg['jsp.add']}"/>
    </form>
</div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>