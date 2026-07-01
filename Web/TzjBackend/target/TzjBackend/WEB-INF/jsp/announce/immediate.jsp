<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>即时公告</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="${base}/css/common.css"/>
<link type="text/css" rel="stylesheet" href="${base}/css/boxy.css"/>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css"/>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css"/>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script>
    var base = '${base}';
    $(function () {
        reloadNoHefuServerGroupBox(false);
        reloadAnnounce();
    });

    function publishAnnounce() {
        if (confirm("${msg['announce.jsp.sureimedia']}")) {
            if ($('#immediate_Add').validationEngine('validate')) {
                $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
                $.ajax({
                    url: base + "/announce/addImmediateAnnounce",
                    type: "post",
                    dataType: "json",
                    data: $("#immediate_Add").serialize(),
                    success: function (data) {
                        $("#loadingmodal").modal('hide');
                        if (data.ok) {
                            reloadAnnounce();
                            $('#immediate_Add')[0].reset();
                        }
                        alert(data.msg);
                    }
                });
            } else {
                alert("${msg['activity.validation.fail']}");
            }
        }
    }

    function addAnnounce() {
        var content = $("#content").val();
        if (content == "") {
            return;
        }
        $.post(base + "/announce/addAnnounceTemplate",
            {content: content},
            function (data) {
                alert(data.msg);
                $("#content").val('');
                reloadAnnounce();
            }
        );
    }

    function deleteAnnounce(announceId) {
        $.post(base + "/announce/deleteImmediateAnnounce",
            {announceId: announceId},
            function () {
                reloadAnnounce();
            }
        );
    }

    function paste(announceId) {
        $.post(base + "/announce/parseAnnounce",
            {announceId: announceId, type: 0},
            function (data) {
                $("#content").val(data.content);
            }
        );
    }

    //加载数据
    function reloadAnnounce() {
        paging(1);
    }
    function paging(page){
        var pageSize = $("#pageSize").val().trim();
        $.ajax({
            url: base + "/announce/queryByType",
            type: "post",
            dataType: "json",
            data: {
                type: "0",
                pageNumber: page,
                pageSize: pageSize
            },
            success: function (data) {
                var data=data.qr;
                $("#immediate_count").html("共" + data.pager.recordCount + "个即时公告, 总计"+ data.pager.pageCount + "页");
                var contentHtml = "<table class=\"table table-bordered\">" +
                    "<tr><th>${msg['announce.jsp.content']}</th><th>${msg['announce.jsp.createtime']}</th>\
                    <th>${msg['announce.jsp.createUser']}</th><th>${msg['jsp.server.deal']}</th></tr>";
                for (var i = 0; i < data.list.length; ++i) {
                    var announce = data.list[i];
                    contentHtml += "<tr><td>" + announce.content + "</td><td>" + announce.createDate + "</td><td>" + announce.userName + "</td><td>\
                        <a href=\"#\" onclick=\"paste('" + announce.id + "');\"><i class=\"icon-ok\" title=\"${msg['jsp.parse.content']}\"></i></a>&nbsp;&nbsp;\
                        <a href=\"#\" onclick=\"deleteAnnounce('" + announce.id + "');\"><i class=\"icon-trash\" title=\"${msg['jsp.server.del']}\"></i></a>\
                        </td></tr>"
                }
                contentHtml += "</table>";
                $("#history").html(contentHtml);
                pageQuery(data,page);
            }
        });
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="immediate_Add" method="post" class="well">
        <select id="select_group" name="groupName" onchange="reloadNoHefuServerBox(this.value, false)" class="span2"></select>
        <input type="checkbox" id="selectAll" onchange="selectAllSid();">${msg['server.all.choice']}
        <div id="checkbox_server" class="well"></div>

        <label for="type">${msg['announce.jsp.type']}</label>
        <select name="type" id="type">
            <option value="1" selected>${msg['announce.jsp.type1']}</option>
            <option value="2">${msg['announce.jsp.type2']}</option>
        </select>

        <label for="content">${msg['announce.jsp.content']}</label>
        <textarea name="content" rows="5" id="content" class='span5 validate[required],maxSize[100],minSize[5]'>${content}</textarea>
        <label for="reason">${msg['forbid.chat.reason']}</label>
        <input id="reason" type="text" name="reason" class="input=medium validate[required]" placeholder="${msg['forbid.needwrite']}"/>
        <br/>
        <input id="save" type="button" value="${msg['jsp.save']}" class="btn btn-primary" onclick="addAnnounce()"/>
        <input id="publish" type="button" value="${msg['jsp.push']}" class="btn btn-primary" onclick="publishAnnounce()"/>
    </form>
    <span>每页</span><input type="text" name="pageSize" id="pageSize" value="10" class="span1"/>
    <input type="button" class="btn btn-info" value="查询" onclick="reloadAnnounce();"/>
    <p id="immediate_count"></p>
    <div id="msg"></div>
</div>

<div class="container-fluid">
    <div id="history"> ${msg['announce.jsp.loading']} </div>
</div>
<div class="pagination" id="pageUl"></div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>