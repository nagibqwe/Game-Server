/**
 * 加载服务器组并选中
 */
function chooseGroup(groupName, serverId) {
    $.ajax({
        type: "POST",
        url: base + "/dblog/group",
        data: "",
        async: false,
        dataType: "json",
        success: function (data) {
            var select = $("#select_group");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var option = $("<option>").val(data[i]).text(data[i]);
                if (option.val() === groupName) {
                    option.attr("selected", true);
                }
                select.append(option);
            }
            chooseServer(select.val(), serverId);
        }
    });
}

/**
 * 加载服务器列表并选中
 */
function chooseServer(groupName, serverId) {
    $.ajax({
        type: "POST",
        url: base + "/dblog/serverByGroup",
        data: {
            "groupName": groupName
        },
        async: false,
        dataType: "json",
        success: function (data) {
            var select = $("#select_server");
            select.empty();
            for (var i = 0; i < data.length; i++) {
                var dblog = data[i];
                var option = $("<option>").val(dblog.serverId).text(dblog.serverName);
                select.append(option);
            }
            select.val(serverId);
        }
    });
}

/**
 * 获取日志
 * @param logType   日志类型
 * @param obj       当前div对象
 * @param other     额外查询参数
 */
function getLog(logType, obj, other) {
    if (!$("#role_query_form").validationEngine('validate')) {
        return;
    }
    var startDate = $("#startdate input").val();
    var endDate = $("#enddate input").val();
    if (!checkDate(startDate, endDate)) {
        return;
    }
    var data = {
        "logType" : logType,
        "serverId": $("#select_server").val(),
        "roleId": $("#queryString").val(),
        "pageSize": $("#pageSize").val(),
        "startDate": startDate,
        "endDate": endDate
    };
    if (other != null && other != undefined) {
        Object.assign(data, other);
    }
    var resultBox = $(obj).parent().next(".result");
    resultBox.show();
    resultBox.shCircleLoader();
    $.ajax({
        type: "POST",
        url: base + "/log/getLog",
        data: data,
        dataType: "json",
        success: function (data) {
            if (!data.ok) {
                resultBox.shCircleLoader('destroy');
                resultBox.html(data.msg);
                return;
            }
            var tableStr = createTable(data.data.fields, data.data.datas);
            resultBox.shCircleLoader('destroy');
            resultBox.html(tableStr);
        }
    });
}

/**
 * 显示隐藏结果
 * @param obj       当前div对象
 * @param isShow    是否显示
 */
function showResult(obj, isShow) {
    if (isShow) {
        $(obj).parent().next(".result").show('slow');
    } else {
        $(obj).parent().next(".result").hide('slow');
    }
}

/**
 * 查询条件参数
 * @param obj
 */
function showCondition(obj) {
    $(obj).next(".condition").toggle('fast');
    $(obj).next(".condition").find(":input").val("");
}

function checkNum(obj) {
    if (isNaN(obj.value)) {
        alert("请输入数字!");
        obj.value = "";
    }
}