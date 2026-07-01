<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.server.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var pageNumber = 1;
    var pageSize = 20;
    var base = '${base}';
    var jspName = "psCommand.jsp";

    $(function () {
        loadPublicGroup(jspName);
        changeAction();
        paging(pageNumber);
    });

    function search() {
        if ($("#logForm").validationEngine('validate')) {
            paging(pageNumber);
        }
    }

    function sendCommand() {
        if (!$("#checkbox_server input[type='checkbox']").is(':checked')) {
            return alert("请选择服务器!");
        }

        if (!$("#commandFrom").validationEngine('validate')) {
            return;
        }

        var params = "";
        $("input[name='pName']").each(function (i) {
            console.log($(this).next("input[name='pValue']"));
            params += $(this).val() + "=" + $(this).next().next("input[name='pValue']").val() + "&";
        });

        $("#progressmodal").modal({backdrop: 'static', keyboard: false});
        $("#proDiv").attr("class", "progress progress-striped active");
        $("#bar").attr("style", "width: 5%;");
        $("#progressdetail").html("");
        $("#closemodal").attr("disabled", "disabled");

        var formData = $("#commandFrom").serializeObject();
        formData['params'] = params;
        $.post(base + "/gm/sendPS", formData, function (data) {
            $("#command").empty();
            $("#msg").empty();

            $("#bar").attr("style", "width: 100%;");
            $("#progressdetail").html(data.msg);
            setTimeout(function () {
                $("#progressmodal").modal('hide');
                paging(pageNumber);
            }, 3000)
        });
    }

    function changeAction() {
        var chActVal = $("#chooseAction").val();
        $("#gmAction").val(chActVal);
        if (chActVal === "script") {
            addParam();
            $("[name='pName']").val("scriptId");
        } else if (chActVal === "test") {
            $("#pDiv").empty();
        } else {
            $("[name='pName']").val("");
        }
        $("[name='pValue']").val("");
    }

    function addParam() {
        var pDiv = $("#pDiv");
        var div = $("<div>").attr("name", "params");
        div.append("<label class=\"label\">参数名</label>");
        div.append("<input type='text' name='pName' class='validate[required] span2' placeholder='参数名'>");
        div.append("<label class=\"label\">参数值</label>");
        div.append("<input type='text' name='pValue' class='span2' placeholder='参数值'>");
        div.append("<input type='button' onclick='delParam(this)' class='btn btn-danger' value='删除'>");
        div.append("<br/>");
        pDiv.append(div);
        var chActVal = $("#chooseAction").val();
        if (chActVal === "script") {
            $("[name='pName']").val("scriptId");
        }
    }

    function delParam(o) {
        $(o).parent().remove();
    }

    function paging(page) {
        var action = $("#action").val().trim();
        var params = $("#params").val().trim();
        var pSize = $("#pageSize").val().trim();
        $("#progressmodal").modal('hide');
        $.ajax({
            type: "POST",
            url: base + "/gm/query",
            data: {
                "action": action,
                "params": params,
                "pageNumber": page,
                "pageSize": pSize,
                "gmType": 1
            },
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');
                var list_html = "<table class='table table-bordered table-striped'>\
                    <tr><td>服务器</td><td>执行命令</td><td>执行参数</td><td>用户</td>\
                    <td>操作结果</td><td>结果描述</td><td>操作者IP</td><td>操作时间</td></tr>";
                for (var i = 0; i < data.list.length; i++) {
                    var ser = data.list[i];
                    var tmp = "<tr>\
                        <td>" + ser.serverName + "(" + ser.serverId + ")</td>\
                        <td>" + ser.action + "</td>\
                        <td>" + ser.params + "</td>\
                        <td>" + ser.user + "</td>\
                        <td>" + ser.isOk + "</td>\
                        <td>" + ser.result + "</td>\
                        <td>" + ser.ip + "</td>\
                        <td>" + ser.operDate + "</td>\
                        </tr>";
                    list_html += tmp;
                }
                list_html += "\n</table>";
                $("#user_log").html(list_html);

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
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="commandFrom" class="well form-inline">
        <select class="input" id="select_group" onchange="loadPublicServers(this.value)"></select>
        <label id="allCheck" class="checkbox inline">全选</label>
        <input type="checkbox" onclick="selectAllSid()"/>
        <div id="checkbox_server" class="well"></div>

        <label for="chooseAction" class="label">命令</label>
        <select id="chooseAction" onchange="changeAction()" class="span2">
            <option value="test" selected="selected">测试连接</option>
            <option value="script">加载脚本</option>
            <option value="cgm">gm命令</option>
            <option value="">自定义命令</option>
        </select>
        <input type="text" id="gmAction" name="action" placeholder="命令，例如:script" class="validate[required] span2"/>
        <input type="button" class="btn btn-info" value="添加参数" onclick="addParam()">
        <br/><br/>
        <div id="pDiv"></div>
        <br/>
        <input type="button" id="submit_command" class="btn btn-primary" onclick="sendCommand()" value="执行命令"/>
    </form>
</div>

<div class="container-fluid">
    <div id="logForm" class="validationEngineContainer well form-inline">
        <label class="label" for="action">查询条件</label>
        <input type="text" id="action" placeholder="命令" class="span2"/>
        <input type="text" id="params" placeholder="参数" class="span2"/>
        <label for="pageSize" class="label">每页显示条数</label>
        <input type="text" id="pageSize" value="20" class="validate[required,custom[integer],min[1]] span1"/>
        <button type="button" class="btn btn-primary" onclick="search()"><i class="icon-search icon-white"></i></button>
    </div>
    <div id="user_log"></div>
    <div class="pagination" id="pageUl"></div>
</div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>