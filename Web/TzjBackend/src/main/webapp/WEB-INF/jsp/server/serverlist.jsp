<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.server.title']}</title>
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
        $("#combine").hide();
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
            $.post(base + "/server/add", $("#serverMod").serialize(), function (data) {
                if (data.ok) {
                    paging(initPageNumber);
                    $("#serverMod")[0].reset();
                    $("#addMod").val("${msg['jsp.server.sumbit']}");
                } else {
                    alert(data.msg);
                }

            });
        });

        $("#replay").click(function () {
            var isHidden = $("#combine").is(":hidden");
            if (isHidden) {
                $("#combine").show('slow');
            } else {
                $("#combine").hide('slow');
            }
        });

        $("#combinebutton").click(function () {
            var serverid = $("form:eq(2) input:eq(0)").val();
            var sid = $("form:eq(2) input:eq(1)").val();

            var s = prompt("${msg['jsp.server.combine1']}" + serverid + "${msg['jsp.server.combine2']}" + sid + " ${msg['jsp.server.combine3']}");
            if (s == "y") {
                $.post(base + "/server/combine", $("#combineform").serialize(), function (data) {
                    if (data.ok) {
                        paging(initPageNumber);
                        $("#combineform")[0].reset();
                    } else {
                        alert(data.msg);
                    }
                });
            }
        });
    });

    function search() {
        if ($("#server_query_form").validationEngine('validate')) {
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
            url: base + "/server/query",
            data: {
                "groupName": groupName,
                "name": name,
                "pageNumber": page,
                "pageSize": pageSize
            },
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                $("#user_count").html("${msg['jsp.server.gong']}" +
                    data.pager.recordCount + "${msg['jsp.server.serverNum']}" +
                    data.pager.pageCount + "${msg['jsp.server.page']}");
                var list_html = "<table class=\"table table-bordered table-striped\">\
                <tr><td>${msg['jsp.server.id']}</td>\
                <td>${msg['jsp.server.name']}</td>\
                <td>${msg['jsp.server.groupname']}</td>\
                <td>${msg['jsp.server.worldIp']}</td>\
                <td>${msg['jsp.server.worldPort']}</td>\
                <td>${msg['jsp.server.ishefu']}</td>\
                <td>${msg['jsp.server.hefutime']}</td>\
                <td>${msg['jsp.server.hefuserver']}</td>\
                <td>${msg['jsp.server.serverType']}</td>\
                <td>开服时间</td>\
                <td>开服状态</td>\
                <td>是否生效</td>\
                <td>${msg['jsp.server.deal']}</td></tr>";
                for (var i = 0; i < data.list.length; i++) {
                    var ser = data.list[i];
                    var tmp = "<tr>\
                <td>&nbsp;" + ser.serverId + "</td>\
                <td>&nbsp;" + ser.serverName + "</td>\
                <td>&nbsp;" + ser.groupName + "</td>\
                <td>&nbsp;" + ser.WorldIP + "</td>\
                <td>&nbsp;" + ser.worldPort + "</td>\
                <td>&nbsp;" + ser.isHeFu + "</td>\
                <td>&nbsp;" + ser.hefuTime + "</td>\
                <td>&nbsp;" + ser.hefuServerID + "</td>\
                <td>&nbsp;" + getServerType(ser.serverType) + "</td>\
                <td>&nbsp;" + ser.serverOpenTime + "</td>\
                <td>&nbsp;" + getServerOpenState(ser.openState) + "</td>\
                <td>&nbsp;" + (ser.isDeleted == 1 ? "无效" : "有效") + "</td>\
                <td><button class='btn btn-warning' onclick='user_update(" + ser.id
                        + ");'>${msg['jsp.server.mod']}</button> "
                        + " <button class='btn btn-danger' onclick='user_delete(" + ser.id
                        + ");'>${msg['jsp.server.del']}</button> <button class='btn btn-info' onclick='combineclear(" + ser.id
                        + ");'>${msg['jsp.server.combineclear']}</button> <button class='btn btn-success' onclick='test(" + ser.id
                        + ");'>${msg['jsp.server.test']}</button></td></tr>";

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

    function user_update(userId) {
        $.ajax({
            url: base + "/server/modify",
            data: {
                "id": userId
            },
            dataType: "json",
            success: function (data) {
                $("#serverAddModel").show();
                $("#addMod").val("${msg['jsp.server.mod']}");

                $("#serverId").val(data.serverId);
                $("#serverName").val(data.serverName);
                $("#WorldIP").val(data.WorldIP);
                $("#worldPort").val(data.worldPort);
                $("#groupName1").val(data.groupName);
                $("#isHeFu").val(data.isHeFu);
                $("#hefuTime").val(data.hefuTime);
                $("#hefuServerID").val(data.hefuServerID);
                $("#serverType").val(data.serverType);
                $("#serverOpenTime").val(data.serverOpenTime);
                $("#openState").val(data.openState);
                if (data.isDeleted == 1) {
                    $("#isD").prop("checked", "checked");
                    $("#noD").prop("checked", false);
                } else {
                    $("#isD").prop("checked", false);
                    $("#noD").prop("checked", "checked");
                }
            }
        });
    }

    function user_delete(userId) {
        var s = prompt("${msg['jsp.server.delete']}");
        if (s == "y") {
            $.post(base + "/server/delete", {"id": userId}, function (data) {
                if (data.ok) {
                    paging(initPageNumber);
                    alert("${msg['jsp.server.deleteOk']}");
                } else {
                    alert(data.msg);
                }
            });
        }
    }

    function combineclear(userid) {
        var s = prompt("${msg['jsp.server.isClearCombine']}");
        if (s == "y") {
            $.post(base + "/server/cleancombine", {"id": userid}, function (data) {
                if (data.ok) {
                    paging(initPageNumber);
                } else {
                    alert(data.msg);
                }
            });
        }
    }

    function test(id) {
        $.post(base + "/server/test", {"id": id}, function (data) {
            if (data.ok) {
                alert("${msg['jsp.server.testsuccess']}");
            } else {
                alert(data.msg);
            }
        });
    }

    function getServerType(type) {
        switch (type) {
            case 0:
                return type + "[测试服]";
            case 1:
                return type + "[正式服]";
            case 2:
                return type + "[登录服]";
            case 3:
                return type + "[公共服]";
            case 4:
                return type + "[跨服服]";
        }
    }

    function getServerOpenState(type) {
        switch (type) {
            case 0:
                return type + "[备服状态]";
            case 1:
                return type + "[开启状态]";
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
        <input type="text" id="groupName" name="groupName" placeholder="${msg['jsp.server.groupname']}">
        <input type="text" id="name" name="name" placeholder="${msg['jsp.server.name']}">
        <label class="label">${msg['jsp.server.perPage']}</label>
        <input type="text" id="pageSize" name="pageSize" value="10" class="span1 validate[required,custom[integer],min[1]]">
        <button id="server_query_btn" type="button" class="btn btn-primary" onclick="search();"><i class="icon-search icon-white"></i></button>
        <input type="button" class='btn btn-info' onclick='showServerAddModel();' value="${msg['jsp.add']}">
        <input type="button" class="btn btn-info" id="replay" value="${msg['jsp.combine']}"/>
    </form>

    <p id="user_count"></p>
    <div id="user_list"></div>
    <div class="pagination" id="pageUl"></div>
</div>

<div id="serverAddModel" class="container-fluid">
    <form id="serverMod" name="serverMod" method="post" action="#" class="well">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td>${msg['jsp.server.areaid']}：</td>
                <td><input name="serverId" type="text" id="serverId" placeholder="${msg['jsp.server.moreareaid']}"/>
                </td>
                <td>${msg['jsp.server.name']}：</td>
                <td><input name="serverName" type="text" id="serverName" placeholder="${msg['jsp.server.morename']}"/>
                </td>
            </tr>
            <tr>
                <td>${msg['jsp.server.worldIp']}：</td>
                <td><input name="WorldIP" type="text" id="WorldIP" placeholder="${msg['jsp.server.worldMore']}"/></td>
                <td>${msg['jsp.server.worldPort']}：</td>
                <td><input name="worldPort" type="text" id="worldPort" placeholder="${msg['jsp.server.worldPortMore']}"/></td>
            </tr>
            <tr>
                <td>${msg['jsp.server.groupname']}:</td>
                <td><input type="text" name="groupName" id="groupName1" placeholder="${msg['jsp.server.groupmore']}"></td>
                <td>${msg['jsp.server.serverType']}:</td>
                <td>
                    <select id="serverType" name="serverType">
                        <option value="0" selected="selected">测试服</option>
                        <option value="1">正式服</option>
                        <option value="2">登录服</option>
                        <option value="3">公共服</option>
                        <option value="4">跨服</option>
                    </select>
                </td>
            </tr>

            <tr>
                <td>开服时间:</td>
                <td><input type="text" name="serverOpenTime" id="serverOpenTime" placeholder="2021-05-08 17:45:00"></td>
                <td>开服状态:</td>
                <td>
                    <select id="openState" name="openState">
                        <option value="0" selected="selected">备服状态</option>
                        <option value="1">开启状态</option>
                    </select>
                </td>
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
        <input type="button" class="btn btn-primary" id="addMod" value="${msg['jsp.add']}"/>
    </form>
</div>

<div id="combine" class="container-fluid">
    <form id="combineform" name="combineform" method="post" class="well" action="">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td>${msg['jsp.combine.currentId']}:</td>
                <td><input name="serverId" type="text" placeholder="${msg['jsp.combine.currentIdMore']}"/></td>
                <td>${msg['jsp.combine.destId']}:</td>
                <td><input name="hefuServerID" type="text" id="hefuServerID" placeholder="${msg['jsp.combine.destIdMore']}"/></td>
            </tr>
            <tr>
                <td>${msg['jsp.server.hefutime']}:</td>
                <td><input name="hefuTime" type="text" id="hefuTime" class="datetimepicker" readonly/></td>
            </tr>
        </table>
        <input type="button" id="combinebutton" class="btn btn-primary" value="${msg['jsp.combine.add']}">
    </form>
</div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>