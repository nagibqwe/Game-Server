/**
 * 表单数据转对象
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/**
 * 日志库组
 */
function group_reload() {
    $.ajax({
        type: "POST",
        url: base + "/dblog/group",
        data: "",
        dataType: "json",
        success: function (data) {
            var select = $("#select_group");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var option = $("<option>").val(data[i]).text(data[i]);
                select.append(option);
            }
            queryServerByGroup(select.val());
        }
    });
}

/**
 * 获取日志库列表
 * @param groupName 组名
 */
function queryServerByGroup(groupName) {
    $.ajax({
        type: "POST",
        url: base + "/dblog/serverByGroup",
        data: {
            "groupName": groupName
        },
        dataType: "json",
        success: function (data) {
            var select = $("#select_server");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var dblog = data[i];
                var option = $("<option>").val(dblog.serverId).text(dblog.serverName + "(" + dblog.serverId + ")");
                select.append(option);
            }
        }
    });
}

/**
 * 未合服的日志库组
 */
function reloadNoHeFuDBGroups() {
    $.ajax({
        type: "POST",
        url: base + "/dblog/group",
        data: "",
        dataType: "json",
        success: function (data) {
            var select = $("#select_group");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var option = $("<option>").val(data[i]).text(data[i]);
                select.append(option);
            }
            reloadNoHeFuDBs(select.val());
        }
    });
}

/**
 * 未合服的日志库列表
 * @param groupName
 */
function reloadNoHeFuDBs(groupName) {
    $.ajax({
        type: "POST",
        url: base + "/dblog/getHeFuDBs",
        data: {
            "groupName": groupName
        },
        dataType: "json",
        success: function (data) {
            var select = $("#select_server");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var dblog = data[i];
                var option = $("<option>").val(dblog.serverId).text(dblog.serverName + "(" + dblog.serverId + ")");
                select.append(option);
            }
        }
    });
}


/**
 * 获取服务器组
 * @param hasFight 是否包含跨服
 */
function reloadNoHeFuServerGroups(hasFight) {
    $.ajax({
        type: "POST",
        url: base + "/server/group",
        data: "",
        dataType: "json",
        success: function (data) {
            var select = $("#select_group");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var option = $("<option>").val(data[i]).text(data[i]);
                select.append(option);
            }
            reloadNoHeFuServer(select.val(), hasFight);
        }
    });
}

/**
 * 获取服务器列表
 * @param groupName 组名
 * @param hasFight 是否包含跨服
 */
function reloadNoHeFuServer(groupName, hasFight) {
    $.ajax({
        type: "POST",
        url: base + "/server/getNoHeFuServer",
        data: {
            "groupName": groupName,
            "hasFight": hasFight//是否包含战斗服
        },
        dataType: "json",
        success: function (data) {
            var select = $("#select_server");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var server = data[i];
                var option = $("<option>").val(server.serverId).text(server.serverName + "(" + server.serverId + ")");
                select.append(option);
            }
        }
    });
}

/**
 * 获取未合服服务器组（多选框）
 */
function reloadNoHefuServerGroupBox(hasFight) {
    $.ajax({
        type: "POST",
        url: base + "/server/group",
        data: "",
        dataType: "json",
        success: function (data) {
            var select = $("#select_group");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var option = $("<option>").val(data[i]).text(data[i]);
                select.append(option);
            }
            reloadNoHefuServerBox(select.val(), hasFight);
        }
    });
}

/**
 * 获取服务器列表（多选框）
 * @param groupName 组名
 * @param hasFight 是否包含跨服
 */
function reloadNoHefuServerBox(groupName, hasFight) {
    $.ajax({
        type: "POST",
        url: base + "/server/getNoHeFuServer",
        data: {
            "groupName": groupName,
            "hasFight": hasFight//是否包含战斗服
        },
        dataType: "json",
        success: function (data) {
            var select = $("#checkbox_server");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var server = data[i];
                var label = $("<label>").attr("class", "checkbox inline").text(server.serverName);
                var option = $("<input>").attr("checked", false).attr("class", "choosecheckbox")
                    .attr("type", "checkbox").attr("name", "serverId").val(server.serverId);
                select.append(label);
                select.append(option);
                select.append("&nbsp;&nbsp;&nbsp;");
            }
        }
    });
}

/**
 * 加载跨服，公共服组
 * @param serverType 服务器类型
 */
function reloadCrossGroup(serverType) {
    $.ajax({
        type: "POST",
        url: base + "/cross/getDbGroupNames",
        data: {
            "serverType": serverType
        },
        dataType: "json",
        success: function (data) {
            var select = $("#select_group");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var option = $("<option>").val(data[i]).text(data[i]);
                select.append(option);
            }
            reloadCrossServerInfo(serverType, select.val());
        }
    });
}

/**
 * 加载跨服、公共服日志库列表
 * @param serverType 跨服类型
 * @param groupName 组名
 */
function reloadCrossServerInfo(serverType, groupName) {
    $.ajax({
        type: "POST",
        url: base + "/cross/getDBs",
        data: {
            "serverType": serverType,
            "groupName": groupName
        },
        dataType: "json",
        success: function (data) {
            var select = $("#select_server");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var dblog = data[i];
                var option = $("<option>").val(dblog.serverId).text(dblog.serverName + "(" + dblog.serverId + ")");
                select.append(option);
            }
        }
    });
}

/**
 * 用在一个页面有多个版面上
 * @param id1
 * @param id2
 * @param idLists
 */
function pannelShow(id1, id2, idLists) {
    $("#" + id1).addClass("active");
    $("#" + id2).addClass("active");
    for (var i = 0; i < idLists.length; i++) {
        if (id1 != idLists[i] && id2 != idLists[i]) {
            $("#" + idLists[i]).removeClass("active");
        }
    }
}

/**
 * 获取组名，渠道列表，服务器列表（多选框）
 */
function getGroup() {
    $.ajax({
        type: "POST",
        url: base + "/dblog/group",
        data: "",
        dataType: "json",
        success: function (data) {
            var select = $("#select_group");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var option = $("<option>").val(data[i]).text(data[i]);
                select.append(option);
            }
            getServer(select.val());
            getChannelName(select.val());
        }
    });
}

/**
 * 获取服务器列表（多选框）
 * @param groupName 组名
 */
function getServer(groupName) {
    $.ajax({
        type: "POST",
        url: base + "/dblog/serverByGroup",
        data: {
            "groupName": groupName
        },
        dataType: "json",
        success: function (data) {
            var select = $("#checkbox_server");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var option = "<input type='checkbox' name='serverId' value='" + data[i].serverId + "' onclick='checkChooseAllSid()'/>" +
                    "&nbsp;" + data[i].serverName + "&nbsp;&nbsp;&nbsp;";
                select.append(option);
            }
            var selectAll = "<input type='checkbox' id='selectAll' onclick='selectAllSid();' />" +
                "&nbsp;全选";
            select.append(selectAll);
        }
    });
}

/**
 * 获取渠道名
 * @param groupName 组名
 */
function getChannelName(groupName) {
    $.ajax({
        url: base + "/channel/getChannelName",
        data: {
            groupName: groupName
        },
        method: "post",
        dataType: "json",
        success: function (data) {
            if (data == null) {
                $("#checkbox_channel").empty();
                return;
            }
            var channelHtml = "";
            for (var i = 0; i < data.length; i++) {
                channelHtml += "<input type='checkbox' name='channelName' value=\"" + data[i] + "\" onclick='checkChooseAllChannel()'/>"
                    + data[i] + "&nbsp;&nbsp;&nbsp;&nbsp;";
            }
            channelHtml += "<input type='checkbox' id='chooseAllChannel' onclick='selectAllChannel()'/>全选";
            $("#checkbox_channel").html(channelHtml);
        }
    });
}


/**
 * 获取渠道名
 * @param groupName 组名
 */
function getChannelNameSelect(groupName) {
    $.ajax({
        url: base + "/channel/getChannelName",
        data: {
            groupName: groupName
        },
        method: "post",
        dataType: "json",
        success: function (data) {
            if (data == null) {
                $("#select_channel").empty();
                return;
            }
            var channelHtml = "<option value='0' selected>所有</option>";
            for (var i = 0; i < data.length; i++) {
                channelHtml += "<option value='" + data[i] + "'>" + data[i] + "</option>";
            }
            $("#select_channel").html(channelHtml);
        }
    });
}

/**
 * 加载公共服服务器组（多选框）
 */
function loadPublicGroup(jspName) {
    $.ajax({
        type: "POST",
        url: base + "/cross/getServerGroupNames",
        data: {"serverType": 3},
        dataType: "json",
        success: function (data) {
            var select = $("#select_group");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var option = $("<option>").val(data[i]).text(data[i]);
                select.append(option);
            }
            if (jspName=="psCommand.jsp") {
                loadPublicServers(select.val());
            }else if (jspName == "serverGroup.jsp") {
                loadPublic(select.val());
            }
        }
    });
}

/**
 * 加载公共服服务器列表（多选框）
 * @param groupName 组名
 */
function loadPublicServers(groupName) {
    $.ajax({
        type: "POST",
        url: base + "/cross/getServers",
        data: {
            "groupName": groupName,
            "serverType": 3
        },
        dataType: "json",
        success: function (data) {
            var select = $("#checkbox_server");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var server = data[i];
                var label = $("<label class='checkbox inline'>").text(server.serverName);
                var option = $("<input type='checkbox' name='serverId'>").val(server.serverId);
                select.append(label);
                select.append(option);
                select.append("&nbsp;&nbsp;");
            }
        }
    });
}

function loadPublic(groupName) {
    $.ajax({
        type: "POST",
        url: base + "/cross/getServers",
        data: {
            "groupName": groupName,
            "serverType": 3
        },
        dataType: "json",
        success: function (data) {
            var select = $("#select_server");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var ps = data[i];
                var option = $("<option>").val(ps.serverId).text(ps.serverName + "(" + ps.serverId + ")");
                select.append(option);
            }
            select.find("option").first().attr("selected", true);
            getServerGroupList();
        }
    });
}

/**
 * 服务器列表全选事件
 */
function selectAllSid() {
    var che = $("#selectAll").is(":checked");
    $("[name='serverId']").prop("checked", che);
}

/**
 * 选择服务器触发全选按钮事件
 */
function checkChooseAllSid() {
    var checked = $("[name=serverId]:checked").length == $("[name=serverId]").length;
    $("#selectAll").prop("checked", checked);
}

/**
 * 渠道全选事件
 */
function selectAllChannel(){
    var checked = $("#chooseAllChannel").prop("checked");
    $("[name='channelName']").prop("checked", checked);
}

/**
 * 选择渠道触发全选按钮事件
 */
function checkChooseAllChannel() {
    var checked = $("[name=channelName]:checked").length == $("[name=channelName]").length;
    $("#chooseAllChannel").prop("checked", checked);
}

/**
 * 创建表格
 * @param fields 字段名数组
 * @param datas  对应字段数据数组，json格式
 * @returns {string}
 */
function createTable(fields, datas) {
    var list_html = "<table class='table table-bordered table-striped'>";
    list_html += "<tr>";
    for (var field in fields) {
        list_html += "<th>" + fields[field] + "</th>";
    }
    list_html += "</tr>";
    for (var i = 0; i < datas.length; i++) {
        var record = datas[i];
        list_html += "<tr>";
        for (var field in fields) {
            list_html += "<td>" + record[field] + "</td>";
        }
        list_html += "</tr>";
    }
    list_html += "</table>";
    return list_html;
}

/**
 * 检查时间段
 * @param startDate
 * @param endDate
 * @returns {boolean}
 */
function checkDate(startDate, endDate) {
    if (startDate == "" || startDate == null) {
        alert("starDate is empty");
        return false;
    }
    if (endDate == "" || endDate == null) {
        alert("endDate is empty");
        return false;
    }
    var start = new Date(startDate.replace("-", "/").replace("-", "/"));
    var end = new Date(endDate.replace("-", "/").replace("-", "/"));
    if (end < start) {
        alert("endDate is less than starDate");
        return false;
    }
    return true;
}

/**
 * 加载所有物品
 */
function loadAllItem() {
    $.ajax({
        url: base + "/admin/getAllItem",
        method: "post",
        dataType: "json",
        success: function (data) {
            if (!data.ok) {
                alert("item load failed");
                return;
            }
            $("#itemList").empty();
            $.each(data.data, function (i, n) {
                items.set(n.itemId + "", n.itemName);
                $("#itemList").append("<option value='" + n.itemId +"'>" + n.itemName +"[" + n.itemId +"]</option>");
            });
        }
    });
}
//邮件显示道具名
function loadAllItem2() {
    $.ajax({
        url: base + "/admin/getAllItem",
        method: "post",
        dataType: "json",
        async : false,
        success: function (data) {
            if (!data.ok) {
                alert("item load failed");
                return;
            }
            $.each(data.data, function (i, n) {
                items.set(n.itemId + "", n.itemName);
            });
        }
    });
}

/**
 * 分页查询
 * @param data
 * @param page
 */
function pageQuery(data,page) {
    var pages = data.pager.pageCount;
    var options = {
        bootstrapMajorVersion:2,
        currentPage: page,//当前页面
        numberOfPages: 5,//一页显示几个按钮（在ul里面生成5个li）
        totalPages:pages, //总页数
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