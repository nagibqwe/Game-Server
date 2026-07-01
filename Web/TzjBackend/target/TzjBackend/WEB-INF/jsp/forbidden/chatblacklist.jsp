<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<title>禁言黑名单列表</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-confirmation.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
       reloadNoHeFuServerGroups(false);
    });

    function search() {
        $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
        var serverId = $("#select_server").val();
        $.ajax({
            url: base + "/forbidden/getChatBlackList",
            data: {
                serverId: serverId
            },
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#loadingmodal").modal('hide');

                if (!data.ok) {
                    alert(data.msg);
                    $("#keyword_list").empty();
                } else {
                    // console.log(data);
                    // var keyData = eval("(" + data.data + ")");
                    var keyData = data.data;
                    var html = "<table class=\"table table-bordered table-striped\" id=\"keyword_list\"><tbody>";
                    html += "<tr><th>服务器ID</th><th>用户ID</th><th>操作</th></tr>";
                    for (var key in keyData) {
                        var typeInfo = keyData[key].type == 0 ? "关键字" : "一句话";
                        html += "<tr><td>" + keyData[key].serverId + "</td><td>" + keyData[key].userId +"</td>" +
                            "<td><input type='button' class='btn btn-warning' value='删除' onclick='deleteShieldKeyWord(" + keyData[key].userId + ")'</td>";
                    }
                    html += "</tbody></table>";
                    $("#keyword_list").html(html);
                }
            }
        });
    }

    function addShieldKeyWordModal() {
        $("#addModal").modal('toggle');
    }

    function addShieldKeyWord() {
        var serverId = $("#select_server").val();
        var keyword = $("#keyword").val();
        var replace = $("#replace").val();
        var type = $("#type").val();
        $.ajax({
            url: base + "/forbidden/addChatBlackList",
            method: "post",
            dataType: "json",
            data: {
                serverId: serverId,
                userId: keyword
            },
            success: function (data) {
                // alert(data.msg);
                $("#addModal").modal('hide');
                search();
            }
        });
    }
    function deleteShieldKeyWord(wordId) {
        $.ajax({
            url: base + "/forbidden/deleteChatBlackList",
            method: "post",
            dataType: "json",
            data: {
                serverId: $("#select_server").val(),
                userId: wordId
            },
            success: function (data) {
                // alert(data.msg);
                search();
            }
        });
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <select id="select_group" onchange="reloadNoHeFuServer(this.value, false)" class="span2"></select>
        <select id="select_server" name="serverId" class="span2"></select>
        <input type="button" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="search()">
        <input type="button" value="添加" class="btn btn-primary" onclick="addShieldKeyWordModal()">
    </form>

    <div id="keyword_list"></div>

    <div id="addModal" class="modal hide fade">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3 id='add_title'>添加聊天黑名单用户</h3>
        </div>
        <div class="modal-body">
            <form id="add_keyword" class="form form-horizontal">
                <div class="control-group">
                    <label for="keyword" class="control-label">用户ID</label>
                    <div class="controls">
                        <input id="keyword" type='text' placeholder='用户ID'/>
                    </div>
                </div>
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" onclick="addShieldKeyWord()">${msg['jsp.giftbag.button.sure']}</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">${msg['jsp.giftbag.button.close']}</button>
        </div>
    </div>
    <jsp:include page="../commonmodal.jsp"/>
</div>
</body>
</html>
