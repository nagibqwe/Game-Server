<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>更新公告</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script>
    var base = "${base}";
    var items = new Map();
    $(function () {
        loadAllItem();
        reloadNoHeFuServerGroups(false);
        reloadNoHefuServerGroupBox(false)
        queryNoticeList();
        // $("#itemDiv").hide();
    });

    function sendUpdateNotice() {
        if ($('#sendNoticeForm').validationEngine('validate')) {
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.post(base + "/announce/sendUpdateNotice", $("#sendNoticeForm").serialize(),
                function (data) {
                    $("#loadingmodal").modal('hide');
                    $("#msg").html("<span>" + data.msg + "</span>");
                    queryNoticeList();
                });
        } else {
            $("#alert").html("${msg['mail.save.paramError']}");
        }
    }

    function queryNoticeList() {
        paging(1);
    }
    function paging(page){
        var pageSize = $("#pageSize").val().trim();
        $.post(base + "/announce/queryNoticeList", {pageNumber: page, pageSize: pageSize},
            function (data) {
                $("#pageUl").empty();
                $("#updateNotice_count").html("共" + data.pager.recordCount + "个更新公告, 总计" + data.pager.pageCount + "页");
                var content = "<table class=\"table table-bordered table-striped\">\
                    <tr>\
                      <th>公告ID</th>\
                      <th>服务器ID</th>\
                      <th>公告内容</th>\
                      <th>公告奖励</th>\
                      <th>操作类型</th>\
                      <th>${msg['jsp.server.deal']}</th>\
                    </tr>";
                for (var i = 0; i < data.list.length; ++i) {
                    var noticeData = data.list[i];
                    var tmp = "<tr><td>" + noticeData.id + "</td>\
                        <td>" + noticeData.serverIds + "</td>\
                        <td>" + noticeData.content + "</td>\
                        <td>" + noticeData.reward + "</td>\
                        <td>" + noticeData.type + "</td>\
                        <td><input type='button' class='btn btn-info' onclick='parse(" + noticeData.id + ");' value='${msg['jsp.parse.content']}'/></td></tr>";
                    content += tmp;
                }
                content += "</table>";
                $("#contentDiv").html(content);
                pageQuery(data,page);
            });
    }

    function parse(id) {
        $.post(base + "/announce/queryById", {id: id},
            function (data) {
                if (data.ok) {
                    var noticeData = data.data;
                    $("#content").val(noticeData.content);
                    $("#type").val(noticeData.type);
                    // $("#reward").val(noticeData.reward);
                    // console.log(noticeData);
                    // if (noticeData.reward.length > 0) {
                    //     $("#item").show();
                    //     $("#sitem").attr("checked", true);
                    //     $("#rewardList").val(noticeData.reward);
                    // }
                    // $("#roleId").val("");
                }
            }
        );
    }

    function showItemDiv() {
        var isShow = $("#show").prop("checked");
        $("#itemDiv").toggle(isShow);
        if (!isShow){
            $("#items").empty();
            $("#reward").val('');
        }
    }

    function showAddItemModel() {
        $("#addItemModal").modal('toggle');
        $("#addItemForm")[0].reset();
    }

    function addItem() {
        var itemId = $("#itemId").val();
        var itemName = items.get(itemId);
        var itemNum = $("#itemNum").val();
        var isBind = $("#isBind").val();
        var itemValue = itemId + "," + itemNum + "," + isBind;
        console.log(itemValue + ":" + itemName);
        var lastValue = $("#reward").val();
        var itemArray = lastValue.split('}');
        if (lastValue == "") {
            itemArray = new Array();
        }
        itemArray.push(itemValue);
        $("#reward").val(itemArray.join("}"));
        console.log(itemArray.join("}"))
        $("#items").append("<span class=\"label\">" + itemName + "*" + itemNum +
            "<i class=\"icon-remove item-remove\" onclick=\"removeItem(this, " + itemId +", " + itemNum + ")\"></i></span>");
        $("#addItemModal").modal('hide');
    }

    function removeItem(obj, itemId, itemNum) {
        var pos;
        var value = $(obj).parent().parent().prev(".rewardInput").val();
        var itemArray = value.split('}');
        $.each(itemArray, function (i, n) {
            var item = n.split(",");
            if (item[0] == itemId && item[1] == itemNum) {
                pos = i;
            }
        });
        itemArray.splice(pos, 1);
        $(obj).parent().parent().prev(".rewardInput").val(itemArray.join("}"));
        console.log(itemArray.join(";"));
        $(obj).parent().remove();
        event.stopPropagation();
    }

</script>
</head>
<body>
<div class="container-fluid">
    <div class="control-group">
        <label for="select_group" class="control-label">${msg['jsp.server']}</label>
        <div class="controls">
            <select id="select_group" name="groupName" onchange="reloadNoHefuServerBox(this.value, false)" class="span2"></select>
        </div>
    </div>
    <form id="sendNoticeForm" method="post" class="form-horizontal well">
        <div class="control-group">
            <label for="checkbox_server" class="control-label">发送服务器：</label>
            <input type="checkbox" id="selectAll" onchange="selectAllSid();">${msg['server.all.choice']}
            <div id="checkbox_server" class="well"></div>
        </div>

        <div class="control-group">
            <label for="content" class="control-label">更新公告：</label>
            <div class="controls">
                <textarea id="content" name="content" rows="20" class="validate[required],maxSize[2048],minSize[1] span8"></textarea>
            </div>
        </div>

        <div class="control-group" id="itemDiv">
            <label class="control-label">奖励道具：</label>
            <div class="span8" style="border: 1px solid rgb(204, 204, 204); border-radius: 2px; height: 25px" onclick="showAddItemModel()">
                <input type="hidden" id="reward" name="items" class="rewardInput"/>
                <div id="items" class="items" style="margin-top: 3px"></div>
            </div>
        </div>

        <div class="control-group">
            <label for="type" class="control-label">操作类型：</label>&nbsp;&nbsp;&nbsp;&nbsp;
            <select id="type" name="type" class="span2">
                <option value="0">更新公告和奖励信息</option>
                <option value="1">更新公告和奖励信息并且重置领奖状态</option>
            </select>
        </div>

        <div class="controls">
            <input type="button" class="btn btn-primary" value="${msg['jsp.server.sumbit']}" onclick="sendUpdateNotice()"/>
        </div>
    </form>
</div>

<div id="addItemModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>添加物品</h3>
    </div>
    <div class="modal-body">
        <form id="addItemForm" class="row-fluid">
            <div class="offset1 span10">
                <input id="curObj" type="hidden"/>
                <label for="itemId">物品ID</label><input type="text" id="itemId" list="itemList" class="span8"/>
                <datalist id="itemList"></datalist>
                <label for="itemNum">物品数量</label><input id="itemNum" type="text" class="span8"/>
                <label for="isBind">是否绑定</label>
                <select id="isBind" class="span8">
                    <option value="0" selected>非绑</option>
                    <option value="1">绑定</option>
                </select>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
        <input type="button" value="添加" onclick="addItem()" class="btn btn-primary"/>
    </div>
</div>

<div id="msg" class="container-fluid"></div>
<span for="pageSize">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;每页</span>
<input type="text" name="pageSize" id="pageSize" value="10" class="span1"/>
<input type="button" id="query_btn" value="查询" class="btn btn-primary" onclick="queryNoticeList()">
<p id="updateNotice_count"></p>
<div class="container-fluid" id="contentDiv"></div>

<div class="pagination" id="pageUl"></div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>