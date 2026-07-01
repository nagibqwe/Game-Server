var pageSize = 10;
var curItemBoxIndex;
var items = new Map();
var festivals = new Map();//节日类型
var testSeverInfo;
var officialServerInfo;
var noHeFuServerInfo;

var baseUrl;
var isActivityList = false;

/**
 * 根据活动类型加载页面动态信息
 * @param base 根路径
 * @param type 活动类型
 */
function load(base, type) {
    // baseUrl = base + "/activity";
    getBaseUrl(base);
    $("#type").val(type);
    // console.log(baseUrl);

    loadTemplate(type);
    loadActivityInfo(type);
    loadActivityList(type, 1);
    loadActivityTag();
}
function getBaseUrl(base) {
    baseUrl = base + "/activity";
}
/**
 * 获取模板列表
 * @param type 活动类型
 */
function loadTemplate(type) {
    $.ajax({
        url: baseUrl + "/getTemplateTime",
        method: "post",
        datatype: "json",
        data: {
            "type": type
        },
        success: function (data) {
            $("#template").html("<option value='0'>新模板</option>");
            $.each(data.data, function (i, n) {
                $("#template").append("<option value='" + n.id + "'>" + n.templateName + "</option>")
            });
        }
    });
}

/**
 * 加载活动信息，表单参数差异，活动列表字段信息
 * @param type 活动类型
 */
function loadActivityInfo(type) {
    // $("#activityReward").empty();
    addActivityStage();
    addActivityCondition();
    loadActivityTitle(type);
}

/**
 * 添加活动档次
 */
function addActivityStage() {
    var type = parseInt($("#type").val());
    switch (type) {
        case 10:
            break;
    }
}


/**
 * 加载活动节日类型信息
 */
function loadActivityFestivalType() {
    $.ajax({
        url: base + "/activity/getActivityFestivalType",
        method: "post",
        dataType: "json",
        async:false,//活动总览不需要异步加载,其他活动不影响
        success: function (data) {

            if (!data.ok) {
                alert("load failed");
                return;
            }
            $("#subType").empty();

            $.each(data.data, function (i, n) {
                festivals.set(n.id + "", n.name);
                $("#subType").append("<option value='" + n.id +"'>" + n.name +"(" + n.id +")</option>");
            });
        }
    });
}

/**
 * 删除最后一档
 */
function removeLastActivityStage() {
    $("#activityReward .activityReward").remove(":last");
}

/**
 * 增加活动条件
 */
function addActivityCondition() {
    var type = parseInt($("#type").val());
    switch (type) {
        case 27:
            $("#condition").html("<div class=\"span2\">\n" +
                "                        <label for=\"version\">版本号</label>\n" +
                "                        <input id=\"version\" type=\"text\" name=\"conditionList\" class=\"validate[required,custom[integer]] span12\"/>\n" +
                "                    </div>");
    }
}

/**
 * 加载活动列表字段
 * @param type 活动类型
 */
function loadActivityTitle(type) {
    var tableHtml = $("#activity_field");
	var tableStr = "<tr>\n" +
        "<th>活动ID</th>\n" +
        "<th>活动名称</th>\n" +
        "<th>活动备注</th>\n" +
        "<th>活动类型</th>\n" +
        "<th>节日类型</th>\n" +
        "<th>最小等级</th>\n" +
        "<th>最大等级</th>\n" +
        "<th>活动标签</th>\n" +
        "<th>标签排序</th>\n" +
        "<th>新服活动</th>\n" +
        "<th>时间类型</th>\n" +
        // "<th>开服天数</th>\n" +
        // "<th>持续天数</th>\n" +
        "<th>开始时间</th>\n" +
        "<th>结束时间</th>\n";
    var type = parseInt(type);
    switch (type) {
        case 3://每日充值
        case 10://招财猫
        default:
			tableStr+="<th>数据</th>\n";
            break;
    }

	tableStr+=
		"<th>活动状态</th>\n" +
        "<th>发布列表</th>\n" +
        "<th>成功列表</th>\n" +
        "<th>发布<input type='checkbox' id='testAll'/></th>\n" +
        // "<th>验证<input type='checkbox' id='verifyAll'/></th>\n" +
        // "<th>发布<input type='checkbox' id='publishAll'/></th>\n" +
        "<th>删除<input type='checkbox' id='deleteAll'/></th>\n" +
        "<th>修改</th>\n" +
        "<th>Excel</th>\n" +
        "</tr>";

	tableHtml.html(tableStr);

    //全选处理
    $("#testAll").click(function () {
        var flag = $("#testAll").prop("checked");
        $(".test:not(:disabled)").prop("checked", flag);
    });
    $("#verifyAll").click(function () {
        var flag = $("#verifyAll").prop("checked");
        $(".valid:not(:disabled)").prop("checked", flag);
    });
    $("#publishAll").click(function () {
        var flag = $("#publishAll").prop("checked");
        $(".publish:not(:disabled)").prop("checked", flag);
    });
    $("#deleteAll").click(function () {
        var flag = $("#deleteAll").prop("checked");
        $(".delete").prop("checked", flag);
    });
    $("#coverAll").click(function () {
        var flag = $("#coverAll").prop("checked");
        $(".cover").prop("checked", flag);
    });
}

function loadActivityData(i,activity) {
    var tableHtml = "<tr>\n" +
        "<td class='id'>" + activity['id'] + "</td>\n" +
        "<td>" + activity['name'] + "</td>\n" +
        "<td>" + activity['description'] + "</td>\n" +
        "<td class='type'>" + activity['type'] + "</td>\n" +
        "<td class='subType'>" + activity['subType'] + "</td>\n" +
        "<td>" + activity['minLv'] + "</td>\n" +
        "<td>" + activity['maxLv'] + "</td>\n" +
        "<td>" + activity['tag'] + "</td>\n" +
        "<td>" + activity['sort'] + "</td>\n" +
        "<td>" + activity['isOpenServer'] + "</td>\n" +
        "<td>" + activity['timeType'] + "</td>\n" +
        // "<td>" + activity['openServerOffsetBegin'] + "</td>\n" +
        // "<td>" + activity['openServerOffset'] + "</td>\n" +
        "<td>" + getBeginTime(activity) + "</td>\n" +
        "<td>" + getEndTime(activity) + "</td>\n";
    //自定义数据
    tableHtml = loadActivityTypeInfo(activity, tableHtml);

    var testable = activity['state'] == 0;
    // var valiadable = activity['state'] == 1;
    // var publishable = activity['state'] == 2;
    tableHtml += "<td>" + getState(activity['state']) + "</td>\n" +
        "<td><label name='dataDetail' style='display: none'>" + activity['toSidList'] + "</label><input type='button' name='dataBtn' value='∨' onclick='showActData(this)' /></td>\n" +
        "<td><label name='dataDetail' style='display: none'>" + activity['okSidList'] + "</label><input type='button' name='dataBtn' value='∨' onclick='showActData(this)' /></td>\n" +
        // "<td><input type='checkbox' class='test'" + (testable ? "" : "disabled='disabled'") + "></td>\n" +
        "<td><input type='checkbox' class='test' ></td>\n" +
        // "<td><input type='checkbox' class='valid'" + (valiadable ? "" : "disabled='disabled'") + "></td>\n" +
        // "<td><input type='checkbox' class='publish'" + (publishable ? "" : "disabled='disabled'") + "></td>\n" +
        "<td><input type='checkbox' class='delete'></td>\n" +
        "<td><span name='editDataBtn' class='glyphicon glyphicon-edit' onclick='editActData("+activity['id']+")' ></span></td>\n" +
        "<td><form name='actForm"+i+"' method='post' action='"+baseUrl+"/exportActivityData'><input name='actId' type='hidden' value='"+activity['id']+"'><a href='#' style='color: #5bb75b' id='export' onclick='exportActivityData("+i+",this)'>导出</a></form></td>\n" +
        "</tr>"
    $("#activity_data").append(tableHtml);
}

function getState(state) {
	switch (state) {
		case 0:
			return "已提交待测试";
		case 1:
			return "已测试待验证";
		case 2:
			return "已验证待发布";
		case 3:
			return "发布成功";
	}
	return "error";
}

function getBeginTime(activity) {
    switch (activity['timeType']) {
        case 0:
            return activity['beginTime'];
        case 1:
            return activity['openServerOffsetBegin'];
    }
    return "error";
}
function getEndTime(activity) {
    switch (activity['timeType']) {
        case 0:
            return activity['endTime'];
        case 1:
            return activity['openServerRecordOffset'];
    }
    return "error";
}

function loadActivityTypeInfo(activity, tableHtml) {
    switch (activity['type']) {
		case 99:
			// var customStr = activity['custom'];
			// var custom = $.parseJSON(customStr);
			// var rate = custom['rate'];
			// var gear = custom['gear'];
			// var client = custom['client'];
			// tableHtml += "<td>" + "奖池倍率:" + rate + "\r\n档次数据:" + gear + "\r\n客户端数据:" + client + "</td>\n";
			break;
		default:
			var customStr = activity['custom'];
            var actId = activity['id'];
			tableHtml += "<td><label name='dataDetail' style='display: none'>" + customStr + "</label></br>" +
                "<input type='button' name='dataBtn' value='∨' onclick='showActData(this)' /></td>\n";
			break;
    }
    return tableHtml;
}

/**
 * 显示/隐藏活动数据
 * @param type 活动类型
 */
function showActData(obj) {
    console.log($(obj).parent().find("label")[0]);
    if($(obj).parent().find("label")[0].style.display=='none'){
        $(obj).parent().find("label")[0].style.display='block';
        $(obj).val("∧");
    }else{
        $(obj).parent().find("label").get(0).style.display='none';
        $(obj).val("∨");
    }
}

/**
 * 编辑活动数据
 * @param type 活动类型
 */
function editActData(id) {
    if(!confirm("是否修改当前活动配置？")){
        return;
    }
    // location.reload();

    copyActivity(id);
}

/**
 * 获取活动列表
 * @param type 活动类型
 * @param pageNumber 页码
 */
function loadActivityList(type, pageNumber) {
    $.ajax({
        url: baseUrl + "/queryActivity",
        method: "post",
        datatype: "json",
        data: {
            "type": type,
            "pageNumber": pageNumber,
            "pageSize": pageSize
        },
        success: function (data) {
            if (!data.ok) {
                alert("error");
                return
            }

            //活动数据
            $("#activity_data").empty();
            var list = data.data.list;
            $.each(list, function (i, n) {
                loadActivityData(i,n);
            });

            //批量全选处理
            $("#testAll,#verifyAll,#publishAll,#deleteAll").prop("checked", false);
            $(".test").click(function () {
                var flag = $(".test:not(:disabled):checked").length == $(".test:not(:disabled)").length;
                $("#testAll").prop("checked", flag);
            });
            $(".valid").click(function () {
                var flag = $(".valid:not(:disabled):checked").length == $(".valid:not(:disabled)").length;
                $("#verifyAll").prop("checked", flag);
            });
            $(".publish").click(function () {
                var flag = $(".publish:not(:disabled):checked").length == $(".publish:not(:disabled)").length;
                $("#publishAll").prop("checked", flag);
            });
            $(".delete").click(function () {
                var flag = $(".delete:checked").length == $(".delete").length;
                $("#deleteAll").prop("checked", flag);
            });

            //分页处理
            $("#pager").empty();
            $("#pageInfo").empty();
            var pager = data.data.pager;
            if (list.length > 0) {
                for (var i = pager.pageNumber - 2; i < pager.pageNumber + 3; i++) {
                    if (i < 1) {
                        continue;
                    }
                    if (i == 1) {
                        if (pager.pageNumber == 1) {
                            $("#pager").append("<li class='disabled'><span>&laquo;</span></li>")
                        } else {
                            $("#pager").append("<li><a href=\"javascript:void(0)\" onclick='loadActivityList(" + type + "," + (pager.pageNumber - 1) + ")'>&laquo;</a></li>")
                        }
                    }
                    if (i == pager.pageNumber) {
                        $("#pager").append("<li class='disabled'><span>" + i + "</span></li>")
                    } else if (i <= pager.pageCount) {
                        $("#pager").append("<li><a href=\"javascript:void(0)\" onclick='loadActivityList(" + type + "," + i + ")'>" + i + "</a></li>")
                    }
                    if (i == pager.pageCount) {
                        if (pager.pageNumber == pager.pageCount) {
                            $("#pager").append("<li class='disabled'><span>&raquo;</span></li>")
                        } else {
                            $("#pager").append("<li><a href=\"javascript:void(0)\" onclick='loadActivityList(" + type + "," + (pager.pageNumber + 1) + ")'>&raquo;</a></li>")
                        }
                        break;
                    }
                }
                $("#pageInfo").html("共" + pager.recordCount + "条记录，当前第" + pager.pageNumber + "页， 共" + pager.pageCount + "页");
            } else {
                // console.log("no data");
                // alert("no data");
            }
        }
    });
}

/**
 * 验证活动表单信息
 * @param type 活动类型
 */
function checkActivityForm(type) {
    // var now = new Date().getTime() / 1000;
    var beginTime = new Date($("#beginTime").val()).getTime() / 1000;
    var endTime = new Date($("#endTime").val()).getTime() / 1000;
    // if (beginTime < now) {
    //     alert("开始时间不能小于当前时间！");
    //     return false;
    // }
    if (endTime < beginTime) {
        alert("结束时间不能小于开始时间！");
        return false;
    }

    // var timeType = $("#timeType").val();
    // if(timeType == 1){
    //     if($("#openServerRecordOffsetBeginDiv").val()<0){
    //         alert("开服记录天数小于0");
    //         return false;
    //     }
    //
    //     if($("#openServerRecordOffsetDiv").val()<=0){
    //         alert("记录持续时间小于或等于0");
    //         return false;
    //     }
    // }

    return true;
}

function validateAll() {
    return validate();
}

/**
 * 提交活动
 */
function submitActivity(callback) {
    var type = $("#type").val();
    if (!$('#activity_form').validationEngine('validate')) {
        alert("验证失败");
        return;
    }
    var flag = validateAll();
    if (flag == false){
        return;
    }
    if (!checkActivityForm(type)) {
        return;
    }
    $.ajax({
        url: baseUrl + "/addActivity",
        method: "post",
        dataType: "json",
        data: $("#activity_form").serialize(),
        success: function (data) {
            if (data.ok) {
                if($("#id").val()>0){
                    $("#id").val(0);
                    alert("活动修改成功");
                }else{
                    alert("活动添加成功");
                }

                loadActivityInfo(type);
                loadActivityList(type, 1);
                // copyTemplate(0);
                if (callback) {
                    callback();
                }
            } else {
                alert("活动添加失败" + data.msg);
            }
        }
    });
}

// /**
//  * 测试验证发布等点击事件
//  * @param type 类型：1测试，3发布
//  */
// function publishEvent(type) {
//     $("#operationType").val(type);
//     var actIds = new Array();
//     $("#platform").empty();//每次点击清空一下，不然会重复生成platform
//     if (type == 1) {
//         $(".test:checked").parent().prevAll(".id").each(function (i) {
//             actIds[i] = parseInt($(this).text());
//         });
//     } else if (type == 3) {
//         $(".publish:checked").parent().prevAll(".id").each(function (i) {
//             actIds[i] = parseInt($(this).text());
//         });
//     }
//     if (actIds.length <= 0) {
//         alert("未选择活动！")
//         return;
//     }
//     $("#actIds").val(actIds.join(","));
//     var serverInfo;
//     if (type == 1) {
//         serverInfo = testSeverInfo;
//     } else {
//         serverInfo = officialServerInfo;
//     }
//     for (var key in serverInfo) {
//         $("#platform").append("<option value='" + key + "'>" + key + "</option>")
//     }
//     changePlatformEvent();
//     $("#publishActivity").modal('show');
// }

/**
 * 发布活动
 * @param type 类型：3发布(将原来测试的位置替换为发布操作)
 */
function publishEvent(type) {
    $("#operationType").val(type);
    var actIds = new Array();
    $("#platform").empty();//每次点击清空一下，不然会重复生成platform
    $(".test:checked").parent().prevAll(".id").each(function (i) {
        actIds[i] = parseInt($(this).text());
    });

    if (actIds.length <= 0) {
        alert("未选择活动！")
        return;
    }
    $("#actIds").val(actIds.join(","));
    var serverInfo;
    if (type == 1) {
        serverInfo = testSeverInfo;
    } else {
        serverInfo = officialServerInfo;
    }
    for (var key in serverInfo) {
        $("#platform").append("<option value='" + key + "'>" + key + "</option>")
    }
    changePlatformEvent();
    $("#publishActivity").modal('show');
}

/**
 * 批量测试或发布活动
 */
function publishActivity() {
    $("#cover").val(0);
    if(confirm("是否覆盖正在进行的活动?")){
        //点击确定后操作
        $("#cover").val(1);
    }
    var sid = new Array();
    $("input[name='sid']:checked").each(function (i) {
        sid[i] = parseInt($(this).val());
    });
    var sids = sid.join(",");
    $.ajax({
        url: baseUrl + "/batchPublish",
        method: "post",
        dataType: "json",
        data: {
            "operationType": $("#operationType").val(),
            "actIds": $("#actIds").val(),
            "platform": $("#platform").val(),
            "sids": sids,
            "cover": $("#cover").val()
        },
        success: function (data) {
            console.log(data.msg);
            alert(data.msg);
            $("#publishActivity").modal('hide');
            if (isActivityList == true){
                doSearch($("#type").val(),$("#subType2").val(),$("#tag").val(),$("#queryString").val(),pageNum);
            }else {
                loadActivityList($("#type").val(), 1);
            }
        }
    });
}

/**
 * 批量验证活动
 */
function verifyEvent() {
    var ids = new Array();
    $(".valid:checked").parent().prevAll(".id").each(function (i) {
        ids[i] = parseInt($(this).text());
    });
    if (ids.length <= 0) {
        alert("未选择活动！")
        return;
    }
    var actIds = ids.join(",");
    $.ajax({
        url: baseUrl + "/batchVerify",
        method: "post",
        dataType: "json",
        data: {
            "actIds": actIds
        },
        success: function (data) {
            alert(data.msg);
            if (isActivityList == true){
                doSearch($("#type").val(),$("#subType2").val(),$("#tag").val(),$("#queryString").val(),pageNum);
            }else {
                loadActivityList($("#type").val(), 1);
            }
        }
    });
}

/**
 * 批量删除活动
 */
function deleteActivity() {
    var ids = new Array();
    $(".delete:checked").parent().prevAll(".id").each(function (i) {
        ids[i] = parseInt($(this).text());
    });
    if (ids.length <= 0) {
        alert("未选择活动！")
        return;
    }
    var actIds = ids.join(",");
    $.ajax({
        url: baseUrl + "/batchDelete",
        method: "post",
        dataType: "json",
        data: {
            "actIds": actIds
        },
        success: function (data) {
            alert(data.msg);
            if (isActivityList == true){
                doSearch($("#type").val(),$("#subType2").val(),$("#tag").val(),$("#queryString").val(),pageNum);
            }else {
                loadActivityList($("#type").val(), 1);
            }
        }
    });
}

/**
 * 一键删除过期活动
 */
function oneKeyDelete() {
    $.ajax({
        url: baseUrl + "/deleteAllExpiredActivity",
        method: "post",
        dataType: "json",
        data: {
            "type": $("#type").val()
        },
        success: function (data) {
            if (data.ok) {
                alert("删除" + data.msg + "条记录");
                loadActivityList($("#type").val(), 1);
            }
        }
    });
}

/**
 * 添加模板
 */
function addTemplate() {
    if (!$('#activity_form').validationEngine('validate')) {
        alert("验证失败");
        return;
    }
    $("#templateName").val("");
    $("#addTemplateModal").modal('show');
}

/**
 * 删除模板
 */
function deleteTemplate() {
    if (confirm("确认删除？")){
        var type = $("#type").val();
        var id = $("#template").val();
        if (id == 0) {
            return;
        }
        $.ajax({
            url: baseUrl + "/deleteTemplate",
            method: "post",
            dataType: "json",
            data: {
                "id": id
            },
            success: function (data) {
                if (data.ok) {
                    alert("删除成功");
                    loadTemplate(type);
                    copyTemplate(0);
                }
            }
        });
    }
}

/**
 * 提交模板
 */
function submitTemplate() {
    var type = $("#type").val();
    // if (!checkActivityForm(type)) {
    //     return;
    // }
    // console.log("reward:"+$("input[name='reward']").val());
    var templateName = $("#templateName").val();
    var templateId = $("#template").val();
    if (templateName == undefined || templateName.trim() == "") {
        templateName = type+"_"+TimeObjectUtil.UnixToymdDate(TimeObjectUtil.CurTime(), true, 8);
        // return;
    }
    var dataParam = $("#activity_form").serialize();
    dataParam = dataParam + "&templateName=" + templateName + "&templateId=" + templateId;

    $.ajax({
        url: baseUrl + "/checkTemplateName",
        async:false,
        method: "post",
        dataType: "json",
        data: dataParam,
        success: function (data) {
            if (data.ok) {
                $.ajax({
                    url: baseUrl + "/addTemplate",
                    method: "post",
                    dataType: "json",
                    data: dataParam,
                    success: function (data) {
                        $("#addTemplateModal").modal('hide');
                        if (data.ok) {
                            alert("模板添加成功");
                            console.log(data);
                            loadTemplate(type);
                            copyTemplate(0);
                        } else {
                            alert("模板添加失败" + data.msg);
                        }
                    }
                });
            } else {
                if(confirm("是否覆盖已有的模板?")){
                    //点击确定后操作
                    $.ajax({
                        url: baseUrl + "/updateTemplate",
                        method: "post",
                        dataType: "json",
                        data: dataParam,
                        success: function (data) {
                            $("#addTemplateModal").modal('hide');
                            if (data.ok) {
                                alert("模板修改成功");
                                $("#addTemplateModal").modal('hide');
                                console.log(data);
                                loadTemplate(type);
                                copyTemplate(0);
                            } else {
                                alert("模板修改失败" + data.msg);
                            }
                        }
                    });
                }else {
                    $("#addTemplateModal").modal('hide');
                }
            }
        }
    });

    // alert("dataParam："+dataParam);
}

/**
 * 复制活动模板数据,id为0表示初始化表单数据
 */
function copyActivity(id) {
    // console.log(id);
    if (id == 0) {
        return;
        // $("#publicCondition").empty();
        // location.reload();
        //
        // $("#activity_form :input[type='text']:not(#type)").each(function () {
        //     $(this).val("");
        // });
        // return;
    }

    $.ajax({
        url: baseUrl + "/queryActivityById",
        method: "post",
        dataType: "json",
        data: {
            "id": id
        },
        success: function (data) {
            if (data.ok) {
                console.log(data.data);
                //公共数据
                $("#id").val(data.data.id);
                $("#name").val(data.data.name);
                $("#description").val(data.data.description);
                data.data.minLv == 0 ? $("#minLv").val("") : $("#minLv").val(data.data.minLv);
                data.data.maxLv == 0 ? $("#maxLv").val("") : $("#maxLv").val(data.data.maxLv);
                $("#subType").val(data.data.subType);
                $("#tag").val(data.data.tag);
                data.data.sort == 0 ? $("#sort").val("") : $("#sort").val(data.data.sort);
                $("#timeType").val(data.data.timeType);
                t_v_change();

                $("#openServerOffsetBegin").val(data.data.openServerOffsetBegin);
                data.data.openServerOffset == 0 ? $("#openServerOffset").val("") : $("#openServerOffset").val(data.data.openServerOffset);
                $("#beginTime").val(data.data.beginTime);
                $("#endTime").val(data.data.endTime);

                $("#openServerRecordOffsetBegin").val(data.data.openServerRecordOffsetBegin);
                data.data.openServerRecordOffset == 0 ? $("#openServerRecordOffset").val("") : $("#openServerRecordOffset").val(data.data.openServerRecordOffset);
                $("#startRecordTime").val(data.data.startRecordTime);
                $("#endRecordTime").val(data.data.endRecordTime);

                $("#autoSend").val(data.data.autoSend);

                $("#isOpenServer").val(data.data.isOpenServer);

                //自定义数据
                parseTemplate(data.data,data.data.configData);
            } else {
                alert(data.msg)
            }
        }
    });
}

/**
 * 复制活动模板数据,id为0表示初始化表单数据
 */
function copyTemplate(id) {
    // console.log(id);
    if (id == 0) {
        // $("#publicCondition").empty();
        location.reload();

        $("#activity_form :input[type='text']:not(#type)").each(function () {
            $(this).val("");
        });
        return;
    }

    $.ajax({
        url: baseUrl + "/getTemplate",
        method: "post",
        dataType: "json",
        data: {
            "id": id
        },
        success: function (data) {
            if (data.ok) {
                console.log(data.data);
                //公共数据
                $("#name").val(data.data.name);
                $("#description").val(data.data.description);
                data.data.minLv == 0 ? $("#minLv").val("") : $("#minLv").val(data.data.minLv);
                data.data.maxLv == 0 ? $("#maxLv").val("") : $("#maxLv").val(data.data.maxLv);
                $("#subType").val(data.data.subType);
                $("#tag").val(data.data.tag);
                data.data.sort == 0 ? $("#sort").val("") : $("#sort").val(data.data.sort);
                $("#timeType").val(data.data.timeType);
                t_v_change();

                $("#openServerOffsetBegin").val(data.data.openServerOffsetBegin);
                data.data.openServerOffset == 0 ? $("#openServerOffset").val("") : $("#openServerOffset").val(data.data.openServerOffset);
                $("#beginTime").val(data.data.beginTime);
                $("#endTime").val(data.data.endTime);

                $("#openServerRecordOffsetBegin").val(data.data.openServerRecordOffsetBegin);
                data.data.openServerRecordOffset == 0 ? $("#openServerRecordOffset").val("") : $("#openServerRecordOffset").val(data.data.openServerRecordOffset);
                $("#startRecordTime").val(data.data.startRecordTime);
                $("#endRecordTime").val(data.data.endRecordTime);

                $("#autoSend").val(data.data.autoSend);

                $("#isOpenServer").val(data.data.isOpenServer);
                //自定义数据
                parseTemplate(data.data,data.data.custom);
            } else {
                alert(data.msg)
            }
        }
    });
}

/**
 * 解析运营活动模板数据
 */
function parseTemplate(data, dataStr) {
    var parsedJson = jQuery.parseJSON(dataStr);
    console.log(parsedJson);
    switch (data.type) {
        case 1://活跃活动
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            $("div[name='cfg2']").remove();
            $("#cfgCount2").val(0);

            $.each(parsedJson.coinType, function(i) {
                var coinType=parsedJson.coinType[i];
                var coinCv=parsedJson.coinCv[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='coinType']").eq(i).val(coinType);
                $("input[name='coinCv']").eq(i).val(coinCv);
            });

            var count = 0;
            $.each(parsedJson.reach, function(i) {
                var reach=parsedJson.reach[i];
                var showReward=parsedJson.showReward[i];
                var reward=parsedJson.reward[i];

                jiangliGrp($("#jiangliBtn").get(0));
                $("input[name='reach']").eq(i).val(reach);
                $("input[name='showReward']").eq(i).val(showReward);

                var reward_i=reward.toString().split(";");//例如:["12_1_1_9", "41012_1_1_9"]
                var btn_id="showAddItemModel"+i;
                $.each(reward_i,function (q) {
                    var rewardArr = reward_i[q].split("_");
                    var item_id = rewardArr[0];
                    var itemName = items.get(item_id);
                    var item_num = rewardArr[1];
                    var item_gender = rewardArr[3];
                    var item_bind = rewardArr[2];
                    var itemValue = reward_i[q];//例如:12_99_1_9
                    var btnName = itemValue + "@reward"+i+"<>"+btn_id;
                    var item_btn_id = "item_btn_id_" + count;
                    count += 1;
                    $("#"+btn_id).before("" +
                        "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                        " id= '" + item_btn_id + "' name='" + btnName + "'>");
                    formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                });
                $("input[name='reward']").eq(i).val(reward);
            });
            break;
        case 2:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            var btn_idInit=0;
            var btn_idInit2=0;
            var count=0;
            count_01=0;
            $.each(parsedJson.i_day, function(i) {
                var i_day=parsedJson.i_day[i];
                var i_reach=parsedJson.i_reach[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='i_day']").eq(i).val(i_day);
                $("input[name='i_reach']").eq(i).val(i_reach);
                //固定奖励组:
                var btn_id="showFixRewardModel"+btn_idInit;
                var i_fixRewardGroupArr=parsedJson.i_fixRewardGroup[i].split(",");//得到一行的数据
                $.each(i_fixRewardGroupArr,function (j) {
                    var wordItem=i_fixRewardGroupArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                   $.each(wordItem,function (k) {
                       var rewardArr = wordItem[k].toString().split("_");//拆分数据
                       var item_id = rewardArr[0];
                       var itemName = items.get(item_id);
                       var item_num = rewardArr[1];
                       var item_gender = rewardArr[3];
                       var item_bind = rewardArr[2];
                       itemValue = wordItem[k];//例如:12_99_1_9
                       var btnName = itemValue + "@i_fixRewardGroup"+btn_idInit+"<>"+btn_id;
                       var item_btn_id = "item_btn_id_" + count;
                       count += 1;
                       $("#"+btn_id).before("" +
                           "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                           " id= '" + item_btn_id + "' name='" + btnName + "'>");
                       formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                   });
                    $("input[name='i_fixRewardGroup']").eq(i).val(i_fixRewardGroupArr);
                    btn_idInit+=1;
                    btn_id="showFixRewardModel"+btn_idInit;
                });
                //累计奖励组:
                var btn_id2="showTotalRewardModel"+btn_idInit2;
                var i_totalRewardGroupArr=parsedJson.i_totalRewardGroup[i].split(",");//得到一行的数据
                $.each(i_totalRewardGroupArr,function (j) {
                    var wordItem=i_totalRewardGroupArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_totalRewardGroup"+btn_idInit2+"<>"+btn_id2;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id2).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_totalRewardGroup']").eq(i).val(i_totalRewardGroupArr);
                    btn_idInit2+=1;
                    btn_id2="showTotalRewardModel"+btn_idInit2;
                });
            });
            break;
        case 3:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            var coinType=parsedJson.coinType[0];
            var coinCount=parsedJson.coinCount[0];

            $("#coinType").val(coinType);
            $("#coinCount").val(coinCount);
            var btn_idInit=0;
            var btn_idInit2=0;
            var count=0;
            count_01=0;
            $.each(parsedJson.i_day, function(i) {
                var i_day=parsedJson.i_day[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='i_day']").eq(i).val(i_day);
                //固定奖励组:
                var btn_id="showRewardModel"+btn_idInit;
                var i_RewardGroupArr=parsedJson.i_RewardGroup[i].split(",");//得到一行的数据;
                $.each(i_RewardGroupArr,function (j) {
                    var wordItem=i_RewardGroupArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_RewardGroup"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_RewardGroup']").eq(i).val(i_RewardGroupArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });

                //进阶奖励组
                var btn_id2="showBuyRewardModel"+btn_idInit2;
                var i_BuyRewardGroupArr=parsedJson.i_BuyRewardGroup[i].split(",");//得到一行的数据;
                $.each(i_BuyRewardGroupArr,function (j) {
                    var wordItem=i_BuyRewardGroupArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_BuyRewardGroup"+btn_idInit2+"<>"+btn_id2;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id2).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_BuyRewardGroup']").eq(i).val(i_BuyRewardGroupArr);
                    btn_idInit2+=1;
                    btn_id2="showBuyRewardModel"+btn_idInit2;
                });
            });
            break;
        case 4:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            var btn_idInit=0;
            var count=0;
            count_01=0;
            $.each(parsedJson.i_id, function(i) {
                var i_id=parsedJson.i_id[i];
                var i_name=parsedJson.i_name[i];
                var i_limit=parsedJson.i_limit[i];
                var i_discount=parsedJson.i_discount[i];
                var i_coin=parsedJson.i_coin[i];
                var i_coin_count=parsedJson.i_coin_count[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='i_id']").eq(i).val(i_id);
                $("input[name='i_name']").eq(i).val(i_name);
                $("input[name='i_limit']").eq(i).val(i_limit);
                $("input[name='i_discount']").eq(i).val(i_discount);
                $(":input[name='i_coin']").eq(i).val(i_coin);
                $("input[name='i_coin_count']").eq(i).val(i_coin_count);

                //礼包道具组:
                var btn_id="showRewardModel"+btn_idInit;
                var i_RewardGroupArr=parsedJson.i_RewardGroup[i].split(",");//得到一行的数据;
                $.each(i_RewardGroupArr,function (j) {
                    var wordItem=i_RewardGroupArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_RewardGroup"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_RewardGroup']").eq(i).val(i_RewardGroupArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });
            });
            break;
        case 5:
            //删除之前动态生成的配置项
            $(".showItem").remove();
            $("div[name='cfg1']").remove();
            $("div[name='cfg2']").remove();
            $("div[name='cfg3']").remove();
            $("div[name='cfg6']").remove();
            $("div[name='cfg7']").remove();

            //抽奖道具
            var i_big_limit=parsedJson.i_big_limit[0];
            var i_costItem=parsedJson.i_costItem[0];
            var i_costGold=parsedJson.i_costGold[0];

            $("#i_big_limit").val(i_big_limit);
            $("#i_costItem").val(i_costItem);
            $("#i_costGold").val(i_costGold);

            var i_GoldGift=parsedJson.i_GoldGift[0].split(";");
            var count=0;
            $.each(i_GoldGift,function (i) {
                var btn_id="addGoldGiftBtn";
                var rewardArr = i_GoldGift[i].split("_");
                var item_id = rewardArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                console.log("item_num"+item_num);
                var itemValue = i_GoldGift[i];//例如:12_99_1_9
                var btnName = itemValue + "@i_GoldGift<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count += 1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='i_GoldGift']").eq(0).val(parsedJson.i_GoldGift[0]);
            //个人轮次奖励
            var btn_idInit=0;
            count_01=0;
            $.each(parsedJson.p_round, function(i) {
                var p_round=parsedJson.p_round[i];

                addSingleCfg($("#addSingleCfgBtn").get(0));
                $("input[name='p_round']").eq(i).val(p_round);
                //奖品:
                var btn_id="showRewardModel"+btn_idInit;
                var p_rewardArr=parsedJson.p_reward[i].split(",");//得到一行的数据;
                $.each(p_rewardArr,function (j) {
                    var wordItem=p_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@p_reward"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='p_reward']").eq(i).val(p_rewardArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });

            });

            //全服进度奖励
            var btn_idInit2=0;
            count_02=0;
            $.each(parsedJson.g_s_reach, function(i) {
                var g_s_reach=parsedJson.g_s_reach[i];
                var g_p_reach=parsedJson.g_p_reach[i];

                addServerCfg($("#addServerCfgBtn").get(0));
                $("input[name='g_s_reach']").eq(i).val(g_s_reach);
                $("input[name='g_p_reach']").eq(i).val(g_p_reach);

                var btn_id="showRewardModel2"+btn_idInit2;
                var g_rewardArr=parsedJson.g_reward[i].split(",");//得到一行的数据;
                $.each(g_rewardArr,function (j) {
                    var wordItem=g_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@g_reward"+btn_idInit2+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='g_reward']").eq(i).val(g_rewardArr);
                    btn_idInit2+=1;
                    btn_id="showRewardModel2"+btn_idInit2;
                });
            });

            //奖池数据配置
            var btn_idInit3=0;
            count_03=0;
            $.each(parsedJson.i_weight, function(i) {
                var i_weight=parsedJson.i_weight[i];
                var i_big=parsedJson.i_big[i];

                jiangliGrp($("#jiangliBtn").get(0));
                $("input[name='i_weight']").eq(i).val(i_weight);
                $("select[name='i_big']").eq(i).val(i_big);


                var btn_id="showRewardModel3"+btn_idInit3;
                var i_rewardArr=parsedJson.i_reward[i].split(",");//得到一行的数据;
                $.each(i_rewardArr,function (j) {
                    var wordItem=i_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_reward"+btn_idInit3+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_reward']").eq(i).val(i_rewardArr);
                    btn_idInit3+=1;
                    btn_id="showRewardModel3"+btn_idInit3;
                });
            });
            //抽奖幸运值配置
            $("#oneLuckyValue").val("");
            var oneLuckyValue=parsedJson.oneLuckyValue[0];
            $("#oneLuckyValue").val(oneLuckyValue);
            var luckyAwardGift=parsedJson.luckyAwardGift[0].split(";");

            $.each(luckyAwardGift,function (i) {
                var btn_id="addOneLuckyBtn";
                var rewardArr = luckyAwardGift[i].split("_");
                var item_id = rewardArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                console.log("item_num"+item_num);
                var itemValue = luckyAwardGift[i];//例如:12_99_1_9
                var btnName = itemValue + "@luckyAwardGift<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count+=1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='luckyAwardGift']").eq(0).val(parsedJson.luckyAwardGift[0]);

            //保底次数配置
            commonBaoDi(parsedJson,count);
            break;
        case 6:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            //累计充值配置
            var btn_idInit=0;
            var btn_idInit2=0;
            var count=0;
            count_01=0;
            $.each(parsedJson.i_reach, function(i) {
                var i_reach=parsedJson.i_reach[i];
                var i_select=parsedJson.i_select[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='i_reach']").eq(i).val(i_reach);
                $("input[name='i_select']").eq(i).val(i_select);
                //固定奖励组:
                var btn_id="showFixRewardModel"+btn_idInit;
                var i_fixRewardGroupArr=parsedJson.i_fixRewardGroup[i].split(",");//得到一行的数据;
                $.each(i_fixRewardGroupArr,function (j) {
                    var wordItem=i_fixRewardGroupArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_fixRewardGroup"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_fixRewardGroup']").eq(i).val(i_fixRewardGroupArr);
                    btn_idInit+=1;
                    btn_id="showFixRewardModel"+btn_idInit;
                });
                //可选奖励组:
                var btn_id2="showSelectRewardModel"+btn_idInit2;
                var i_SelectRewardGroupArr=parsedJson.i_SelectRewardGroup[i].split(",");//得到一行的数据;
                $.each(i_SelectRewardGroupArr,function (j) {
                    var wordItem=i_SelectRewardGroupArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_SelectRewardGroup"+btn_idInit2+"<>"+btn_id2;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id2).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_SelectRewardGroup']").eq(i).val(i_SelectRewardGroupArr);
                    btn_idInit2+=1;
                    btn_id2="showSelectRewardModel"+btn_idInit2;
                });
            });
            break;
        case 7:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            //消耗配置
            var coinType=parsedJson.coinType[0];
            $("#coinType").val(coinType);
            //累计消耗配置
            var btn_idInit=0;
            var btn_idInit2=0;
            var count=0;
            count_01=0;
            $.each(parsedJson.i_reach, function(i) {
                var i_reach=parsedJson.i_reach[i];
                var i_select=parsedJson.i_select[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='i_reach']").eq(i).val(i_reach);
                $("input[name='i_select']").eq(i).val(i_select);
                //固定奖励组:
                var btn_id="showFixRewardModel"+btn_idInit;
                var i_fixRewardGroupArr=parsedJson.i_fixRewardGroup[i].split(",");//得到一行的数据;
                $.each(i_fixRewardGroupArr,function (j) {
                    var wordItem=i_fixRewardGroupArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_fixRewardGroup"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_fixRewardGroup']").eq(i).val(i_fixRewardGroupArr);
                    btn_idInit+=1;
                    btn_id="showFixRewardModel"+btn_idInit;
                });
                //可选奖励组:
                var btn_id2="showSelectRewardModel"+btn_idInit2;
                var i_SelectRewardGroupArr=parsedJson.i_SelectRewardGroup[i].split(",");//得到一行的数据;
                $.each(i_SelectRewardGroupArr,function (j) {
                    var wordItem=i_SelectRewardGroupArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_SelectRewardGroup"+btn_idInit2+"<>"+btn_id2;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id2).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_SelectRewardGroup']").eq(i).val(i_SelectRewardGroupArr);
                    btn_idInit2+=1;
                    btn_id2="showSelectRewardModel"+btn_idInit2;
                });
            });
            break;
        case 8:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            //集物配置表
            var exChangeMaterialsId=parsedJson.exChangeMaterialsId[0];
            var returnMoneyCoinType=parsedJson.returnMoneyCoinType[0];
            var returnMoneyCoinNum=parsedJson.returnMoneyCoinNum[0];

            $("#exChangeMaterialsId").val(exChangeMaterialsId);
            $("#returnMoneyCoinType").val(returnMoneyCoinType);
            $("#returnMoneyCoinNum").val(returnMoneyCoinNum);
            //兑换配置表
            var btn_idInit=0;
            var count=0;
            count_01=0;
            $.each(parsedJson.exChangeTimes, function(i) {
                var exChangeTimes=parsedJson.exChangeTimes[i];
                var exChangePrice=parsedJson.exChangePrice[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='exChangeTimes']").eq(i).val(exChangeTimes);
                $("input[name='exChangePrice']").eq(i).val(exChangePrice);

                var btn_id="showRewardModel"+btn_idInit;
                var rewardArr=parsedJson.reward[i].split(",");//得到一行的数据
                $.each(rewardArr,function (j) {
                    var wordItem=rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@reward"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='reward']").eq(i).val(rewardArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });
            });
            break;
        case 9:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            //团购物品配置
            var career0=parsedJson.career0[0];
            var career1=parsedJson.career1[0];
            var career2=parsedJson.career2[0];
            var career3=parsedJson.career3[0];
            var targetItemNum=parsedJson.targetItemNum[0];
            var costCoinType=parsedJson.costCoinType[0];
            var oriPrice=parsedJson.oriPrice[0];

            $("#career0").val(career0);
            $("#career1").val(career1);
            $("#career2").val(career2);
            $("#career3").val(career3);
            $("#targetItemNum").val(targetItemNum);
            $("#costCoinType").val(costCoinType);
            $("#oriPrice").val(oriPrice);

            //团购打折配置
            $.each(parsedJson.buy_player, function(i) {
                var buy_player=parsedJson.buy_player[i];
                var buy_discount=parsedJson.buy_discount[i];
                var buy_price=parsedJson.buy_price[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='buy_player']").eq(i).val(buy_player);
                $("input[name='buy_discount']").eq(i).val(buy_discount);
                $("input[name='buy_price']").eq(i).val(buy_price);
            });
            break;
        case 10:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            //奖池倍率数据配置
            var rate=parsedJson.rate;
            var weight=parsedJson.weight;
            $.each(rate,function (i) {
                $("#rate"+i).val(rate[i]);
                $("#weight"+i).val(weight[i]);
            });
            //规则数据配置
            $.each(parsedJson.consume, function(i) {
                var consume=parsedJson.consume[i];
                var recharge=parsedJson.recharge[i];
                var minIndex=parsedJson.minIndex[i];
                var maxIndex=parsedJson.maxIndex[i];
                var serverRestNum=parsedJson.serverRestNum[i];

                addSingleCfg($("#jiangliBtn").get(0));
                $("input[name='consume']").eq(i).val(consume);
                $("input[name='recharge']").eq(i).val(recharge);
                $("select[name='minIndex']").eq(i).val(minIndex);
                $("select[name='maxIndex']").eq(i).val(maxIndex);
                $("input[name='serverRestNum']").eq(i).val(serverRestNum);
            });
            break;
        case 11:
            //删除之前动态生成的配置项
            $("div[name='cfg']").remove();
            $("div[name='cfg1']").remove();
            $("div[name='cfg2']").remove();
            $("div[name='cfg3']").remove();

            //客户端配置
            var magicId=parsedJson.magicId[0];
            var boxId=parsedJson.boxId[0];

            $("#magicId").val(magicId);
            $("#boxId").val(boxId);

            var showItems=parsedJson.showItems[0].split(";");
            var count=0;
            $.each(showItems,function (i) {
                var btn_id="addShowItemsBtn";
                var rewardArr = showItems[i].split("_");
                var item_id = rewardArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                console.log("item_num"+item_num);
                var itemValue = showItems[i];//例如:12_99_1_9
                var btnName = itemValue + "@showItems<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count += 1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='showItems']").eq(0).val(parsedJson.showItems[0]);

            //限购商品配置
            var btn_idInit=0;
            count_01=0;
            $.each(parsedJson.price, function(i) {
                var price=parsedJson.price[i];
                var coin=parsedJson.coin[i];
                var limit=parsedJson.limit[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='price']").eq(i).val(price);
                $("input[name='coin']").eq(i).val(coin);
                $("input[name='limit']").eq(i).val(limit);

                var btn_id="showRewardModel"+btn_idInit;
                var itemArr=parsedJson.item[i].split(",");//得到一行的数据;
                $.each(itemArr,function (j) {
                    var wordItem=itemArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@item"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='item']").eq(i).val(itemArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });
            });
            // BOSS类型配置
            loadActivityBossType(false);//判断是否为异步加载
            $.each(parsedJson.bossList, function(i) {
                var bossListid=parsedJson.bossList[i];
                $("input:checkbox").eq(bossListid-1).attr("checked",true);
            });
            //BOSS奖励
            var btn_idInit2=0;
            count_02=0;
            $.each(parsedJson.bossGiftWeight, function(i) {
                var bossGiftWeight=parsedJson.bossGiftWeight[i];

                bossjiangliGrp($("#bossjiangliBtn").get(0));
                $("input[name='bossGiftWeight']").eq(i).val(bossGiftWeight);

                var btn_id="showRewardModel2"+btn_idInit2;
                var bossGiftRewardArr=parsedJson.bossGiftReward[i].split(",");//得到一行的数据;
                $.each(bossGiftRewardArr,function (j) {
                    var wordItem=bossGiftRewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@bossGiftReward"+btn_idInit2+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='bossGiftReward']").eq(i).val(bossGiftRewardArr);
                    btn_idInit2+=1;
                    btn_id="showRewardModel2"+btn_idInit2;
                });
            });


            var boxCount = 0;
            //礼包奖励
            var btn_idInit3=0;
            count_03=0;
            $.each(parsedJson.giftId, function(i) {
                // var boxBtnName="boxjiangliGrpBtn"+giftCount;
                var giftId=parsedJson.giftId[i];
                var boxRewardCount=Number(parsedJson.boxRewardCount[i]);
                // var cfgCount=parsedJson.cfgCount[i];
                giftjiangliGrp($("#giftjiangliBtn").get(0));
                $("input[name='giftId']").eq(i).val(giftId);

                //每个礼包权重
                var boxGiftWeightAll=parsedJson.boxGiftWeight;
                var boxGiftRewardAll=parsedJson.boxGiftReward;
                // console.log(boxGiftWeightAll.toString());
                // console.log(boxGiftRewardAll.toString());
                for (var j=0;j<boxRewardCount;j++){//每个礼包ID:中礼包权重个数遍历
                    boxjiangliGrp($("#boxjiangliGrpBtn"+i).get(0));

                    var boxGiftWeight=boxGiftWeightAll[boxCount];
                    var boxGiftReward=boxGiftRewardAll[boxCount];

                    $("input[name='boxGiftWeight']").eq(boxCount).val(boxGiftWeight);
                    // $("input[name='boxGiftReward']").eq(boxCount).val(boxGiftReward);

                    var btn_id="showRewardModel3"+btn_idInit3;
                    var boxGiftRewardArr=boxGiftReward;
                    var wordItem=boxGiftRewardArr.split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@boxGiftReward"+btn_idInit3+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='boxGiftReward']").eq(boxCount).val(boxGiftRewardArr);
                    btn_idInit3+=1;
                    btn_id="showRewardModel3"+btn_idInit3;

                    boxCount+=1;
                }
            });
            break;
        case 12:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            //任务配置
            var coinId=parsedJson.coinId[0];
            $("#coinId").val(coinId);
            var btn_idInit=0;
            var count=0;
            count_01=0;
            $.each(parsedJson.taskTypes, function(i) {
                var taskTypes=parsedJson.taskTypes[i];
                var reachs=parsedJson.reachs[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='taskTypes']").eq(i).val(taskTypes);
                $("input[name='reachs']").eq(i).val(reachs);

                var btn_id="showRewardModel"+btn_idInit;
                var rewardArr=parsedJson.rewards[i].split(",");//得到一行的数据
                $.each(rewardArr,function (j) {
                    var wordItem=rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@rewards"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='rewards']").eq(i).val(rewardArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });
            });
            break;
        case 13:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            $("div[name='cfg2']").remove();
            $("div[name='cfg3']").remove();
            $("div[name='cfg4']").remove();
            //副本掉落配置
            var btn_idInit=0;
            var count=0;
            $.each(parsedJson.dropCloneMap, function(i) {
                var dropCloneMap=parsedJson.dropCloneMap[i];
                var cloneIdx=parsedJson.cloneIdx[i];

                jiangliGrp_Clone($("#jiangliBtna").get(0));
                $("input[name='dropCloneMap']").eq(i).val(dropCloneMap);

                var dropCloneReward=parsedJson.dropCloneReward[i];
                for (var j=1;j<=dropCloneReward;j++) {

                    jiangliGrp_01($("#jiangliBtn"+(i+1)).get(0),'clone_',"_"+(i+1));
                    var clone_dropWeight='clone_dropWeight_'+(i+1);
                    $("input[name=\'"+clone_dropWeight+"\']").eq(j-1).val(parsedJson["clone_dropWeight_"+(i+1)][j-1]);
                }

                var clone_dropRewardItems=parsedJson["clone_dropRewardItems_"+(i+1)].toString().split(",");//["12_1_1_9;41012_1_1_9","4_1_1_9"]

                var btn_id="showAddWordsModel"+btn_idInit;

                //处理行
                var rowCount=0;
                $.each(clone_dropRewardItems,function (k) {
                    var clone_dropRewardItemsArr=clone_dropRewardItems[k].toString().split(";");
                    $.each(clone_dropRewardItemsArr,function (q) {
                        var rewardArr = clone_dropRewardItemsArr[q].toString().split("_");
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        var itemValue = clone_dropRewardItemsArr[q];//例如:12_99_1_9
                        var btnName = itemValue + "@words"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    var clone_dropRewardItems_='clone_dropRewardItems_'+(i+1);
                    $("input[name=\'"+clone_dropRewardItems_+"\']").eq(rowCount).val(clone_dropRewardItems[k]);
                    console.log($("input[name=\'"+clone_dropRewardItems_+"\']").eq(rowCount).val());

                    btn_idInit+=1;
                    btn_id="showAddWordsModel"+btn_idInit;
                    rowCount+=1;
                });

            });
            //Boss掉落配置
            var btn_idInit2=btn_idInit;
            var count2=count+1;//防止道具id重复
            $.each(parsedJson.bossIdx, function(i) {
                var bossIdx = parsedJson.bossIdx[i];

                jiangliGrp_Boss($("#jiangliBtnb").get(0));
                $.each(parsedJson["bossList_"+bossIdx], function(i) {
                    var bossListid=parsedJson["bossList_"+bossIdx][i];
                    var bossList='bossList_'+bossIdx;
                    $(":checkbox[name=\'"+bossList+"\']").eq(bossListid-1).attr("checked",true);
                });

                var dropBossReward=parsedJson.dropBossReward[i];
                for (var j=1;j<=dropBossReward;j++) {

                    jiangliGrp_01($("#jiangliBtn3"+(i+1)).get(0),'boss_',"_"+(i+1));
                    var boss_dropWeight='boss_dropWeight_'+(i+1);
                    $("input[name=\'"+boss_dropWeight+"\']").eq(j-1).val(parsedJson["boss_dropWeight_"+(i+1)][j-1]);
                }

                var boss_dropRewardItems=parsedJson["boss_dropRewardItems_"+(i+1)].toString().split(",");//["12_1_1_9;41012_1_1_9","4_1_1_9"]

                var btn_id="showAddWordsModel"+btn_idInit2;

                //处理行
                var rowCount=0;
                $.each(boss_dropRewardItems,function (k) {
                    var boss_dropRewardItemsArr=boss_dropRewardItems[k].toString().split(";");
                    $.each(boss_dropRewardItemsArr,function (q) {
                        var rewardArr = boss_dropRewardItemsArr[q].toString().split("_");
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        var itemValue = boss_dropRewardItemsArr[q];//例如:12_99_1_9
                        var btnName = itemValue + "@words"+btn_idInit2+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count2;
                        count2 += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    var boss_dropRewardItems_='boss_dropRewardItems_'+(i+1);
                    $("input[name=\'"+boss_dropRewardItems_+"\']").eq(rowCount).val(boss_dropRewardItems[k]);
                    // alert("boss_dropRewardItems[k]:"+boss_dropRewardItems[k]);
                    console.log("boss_dropRewardItems_:"+$("input[name=\'"+boss_dropRewardItems_+"\']").eq(rowCount).val());

                    btn_idInit2+=1;
                    btn_id="showAddWordsModel"+btn_idInit2;
                    rowCount+=1;
                });
            });

            //兑换配置
            var btn_idInit3=btn_idInit2;
            //每行

            var itemValue="";
            $.each(parsedJson.exChangeLimit, function(i) {

                var btn_id="showAddWordsModel"+btn_idInit3;
                var exChangeLimit = parsedJson.exChangeLimit[i];
                var isShowRedPoint = parsedJson.isShowRedPoint[i];
                jiangliGrp_03($("#jiangliBtnc").get(0));
                $("input[name='exChangeLimit']").eq(i).val(exChangeLimit);
                $("select[name='isShowRedPoint']").eq(i).val(isShowRedPoint);
                //收集字体:
                var wordItems=parsedJson.wordItems[i].toString().split(",");
                //处理行

                $.each(wordItems,function (j) {
                    var wordItemsArr=wordItems[j].toString().split(";");
                    $.each(wordItemsArr,function (k) {
                        var rewardArr = wordItemsArr[k].toString().split("_");
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItemsArr[k];//例如:12_99_1_9
                        var btnName = itemValue + "@words"+btn_idInit3+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count2;
                        count2 += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='wordItems']").eq(i).val(wordItems[j]);
                    btn_idInit3+=1;
                    btn_id="showAddWordsModel"+btn_idInit3;
                });
            });
            var itemValue2="";
            var btn_idInit4=btn_idInit2;
            $.each(parsedJson.exChangeLimit, function(i) {
                //奖励宝箱:
                var btn_id = "showAddItemModel" + btn_idInit4;
                var rewardBox = parsedJson.rewardBox[i].toString().split(",");
                //处理行

                $.each(rewardBox, function (j) {
                    var rewardBoxsArr = rewardBox[j].toString().split(";");
                    $.each(rewardBoxsArr, function (k) {
                        var rewardArr = rewardBoxsArr[k].toString().split("_");
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue2 = rewardBoxsArr[k];//例如:12_99_1_9
                        var btnName = itemValue2 + "@reward" + btn_idInit4 + "<>" + btn_id;
                        var item_btn_id = "item_btn_id_" + count2;
                        count2 += 1;
                        $("#" + btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='rewardBox']").eq(i).val(rewardBox[j]);
                    btn_idInit4 += 1;
                    btn_id = "showAddItemModel" + btn_idInit4;
                });
            });
            break;
        case 14:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            $("#cfgCount").val(0);
            //档位奖励
            var count=0;
            countRecharge=0;//每次点击修改时将页面上的countRecharge重置为0
            $.each(parsedJson.rechargeId, function(i) {
                var rechargeId=parsedJson.rechargeId[i];
                var reward=parsedJson.reward[i];//例如:"reward":["12_1_1_9;41012_1_1_9","3_1_1_9"]   string类型

                console.log("14:"+reward);
                console.log(typeof (reward));
                jiangliGrp($("#jiangliBtn").get(0));
                $(":input[name='rechargeId']").eq(i).val(rechargeId);
                var reward_i=reward.toString().split(";");//例如:["12_1_1_9", "41012_1_1_9"]
                console.log(reward_i);
                var btn_id="showAddItemModel"+i;

                $.each(reward_i,function (q) {
                    var rewardArr = reward_i[q].toString().split("_");
                    var item_id = rewardArr[0];
                    var itemName = items.get(item_id);
                    var item_num = rewardArr[1];
                    var item_gender = rewardArr[3];
                    var item_bind = rewardArr[2];
                    var itemValue = reward_i[q];//例如:12_99_1_9
                    var btnName = itemValue + "@reward"+i+"<>"+btn_id;
                    var item_btn_id = "item_btn_id_" + count;
                    count += 1;
                    $("#"+btn_id).before("" +
                        "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                        " id= '" + item_btn_id + "' name='" + btnName + "'>");
                    formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                });
                $("input[name='reward']").eq(i).val(reward);
            });

            break;
        case 15:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            $("input[name='dayCount']").val(0);//初始化每个充值配置中充值天数计数为0
            //充值配置
            var btn_idInit=0;
            var btn_idInit2=0;
            count_01=0;
            var count=0;
            var item_btn_id_count=0;
            $.each(parsedJson.i_reach, function(i) {//充值配置个数遍历
                var i_reach = parsedJson.i_reach[i];
                $("input[name='i_reach']").eq(i).val(i_reach);
                var dayCount=Number(parsedJson.dayCount[i]);

                for (var j=0;j<dayCount;j++){//每个充值配置中充值天数遍历
                    addSaltIpGrp($("#addSaltIpGrpBtn"+i).get(0));

                    var i_day=parsedJson.i_day[count];
                    $("input[name='i_day']").eq(count).val(i_day);

                    //固定奖励组:
                    var btn_id="showFixRewardModel"+btn_idInit;
                    var i_fixRewardGroupArr=parsedJson.i_fixRewardGroup[count];
                    var wordItem=i_fixRewardGroupArr.split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_fixRewardGroup"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + item_btn_id_count;
                        item_btn_id_count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_fixRewardGroup']").eq(count).val(i_fixRewardGroupArr);
                    btn_idInit+=1;
                    btn_id="showFixRewardModel"+btn_idInit;

                    //累计奖励组:
                    var btn_id2="showTotalRewardModel"+btn_idInit2;
                    var i_totalRewardGroupArr=parsedJson.i_totalRewardGroup[count];
                    var wordItem2=i_totalRewardGroupArr.split(";")//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem2,function (k) {
                        var rewardArr = wordItem2[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem2[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_totalRewardGroup"+btn_idInit2+"<>"+btn_id2;
                        var item_btn_id = "item_btn_id_" + item_btn_id_count;
                        item_btn_id_count += 1;
                        $("#"+btn_id2).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_totalRewardGroup']").eq(count).val(i_totalRewardGroupArr);
                    btn_idInit2+=1;
                    btn_id2="showTotalRewardModel"+btn_idInit2;

                    count+=1;
                }
            });
            break;
        case 16:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            var btn_idInit=0;
            var count=0;
            count_01=0;
            //礼包配置
            $.each(parsedJson.i_id, function(i) {
                var i_id = parsedJson.i_id[i];
                var i_name = parsedJson.i_name[i];
                var i_limit = parsedJson.i_limit[i];
                var i_server_limit = parsedJson.i_server_limit[i];
                var i_discount = parsedJson.i_discount[i];
                var i_coin = parsedJson.i_coin[i];
                var i_coin_count = parsedJson.i_coin_count[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='i_id']").eq(i).val(i_id);
                $("input[name='i_name']").eq(i).val(i_name);
                $("input[name='i_limit']").eq(i).val(i_limit);
                $("input[name='i_server_limit']").eq(i).val(i_server_limit);
                $("input[name='i_discount']").eq(i).val(i_discount);
                $(":input[name='i_coin']").eq(i).val(i_coin);
                $("input[name='i_coin_count']").eq(i).val(i_coin_count);

                //礼包道具组:
                var btn_id="showRewardModel"+btn_idInit;
                var i_RewardGroupArr=parsedJson.i_RewardGroup[i].split(",");//得到一行的数据;
                $.each(i_RewardGroupArr,function (j) {
                    var wordItem=i_RewardGroupArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_RewardGroup"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_RewardGroup']").eq(i).val(i_RewardGroupArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });
            });
            break;
        case 17:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            var btn_idInit=0;
            var count=0;
            count_01=0;
            //每日充值配置
            $.each(parsedJson.i_day, function(i) {
                var i_day = parsedJson.i_day[i];
                var currencyType = parsedJson.currencyType[i];
                var price = parsedJson.price[i];
                var limitTimes = parsedJson.limitTimes[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='i_day']").eq(i).val(i_day);
                $(":input[name='currencyType']").eq(i).val(currencyType);
                $("input[name='price']").eq(i).val(price);
                $("input[name='limitTimes']").eq(i).val(limitTimes);

                //奖励组:
                var btn_id="showRewardModel"+btn_idInit;
                var rewardsArr=parsedJson.rewards[i].split(",");//得到一行的数据;
                $.each(rewardsArr,function (j) {
                    var wordItem=rewardsArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@rewards"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='rewards']").eq(i).val(rewardsArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });
            });
            break;
        case 18:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            $("div[name='cfg2']").remove();
            //排名奖励
            var btn_idInit=0;
            count_01=0;
            var count=0;
            $.each(parsedJson.start, function(i) {
                var start=parsedJson.start[i];
                var tail=parsedJson.tail[i];
                var rankScore=parsedJson.rankScore[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='start']").eq(i).val(start);
                $("input[name='tail']").eq(i).val(tail);
                $("input[name='rankScore']").eq(i).val(rankScore);

                var btn_id="showRewardModel"+btn_idInit;
                var rankRewardsArr=parsedJson.rankRewards[i].split(",");//得到一行的数据;
                $.each(rankRewardsArr,function (j) {
                    var wordItem=rankRewardsArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@rankRewards"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='rankRewards']").eq(i).val(rankRewardsArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });
            });
            //积分奖励
            var btn_idInit2=0;
            count_02=0;
            $.each(parsedJson.score, function(i) {
                var score=parsedJson.score[i];

                jiangliGrp($("#jiangliBtn").get(0));
                $("input[name='score']").eq(i).val(score);

                var btn_id="showRewardModel2"+btn_idInit2;
                var rewardsArr=parsedJson.rewards[i].split(",");//得到一行的数据;
                $.each(rewardsArr,function (j) {
                    var wordItem=rewardsArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@rewards"+btn_idInit2+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='rewards']").eq(i).val(rewardsArr);
                    btn_idInit2+=1;
                    btn_id="showRewardModel2"+btn_idInit2;
                });
            });

            //其他配置
            var limit=parsedJson.limit[0];
            var gold=parsedJson.gold[0];
            $("#limit").val(limit);
            $("#gold").val(gold);
            break;
        case 19:
            //删除之前动态生成的配置项
            $(".showItem").remove();
            $("div[name='cfg1']").remove();
            $("div[name='cfg2']").remove();
            $("div[name='cfg6']").remove();
            $("div[name='cfg7']").remove();

            //消耗配置
            var keyId=parsedJson.keyId[0];
            var oneCostKey=parsedJson.oneCostKey[0];
            var tenCostKey=parsedJson.tenCostKey[0];
            var oneCostGold=parsedJson.oneCostGold[0];
            var tenCostGold=parsedJson.tenCostGold[0];
            $("#keyId").val(keyId);
            $("#oneCostKey").val(oneCostKey);
            $("#tenCostKey").val(tenCostKey);
            $("#oneCostGold").val(oneCostGold);
            $("#tenCostGold").val(tenCostGold);

            //抽奖配置
            var addWish=parsedJson.addWish[0];
            var addScore=parsedJson.addScore[0];
            var wishMax=parsedJson.wishMax[0];
            var wasteGoldCount=parsedJson.wasteGoldCount[0];
            $("#addWish").val(addWish);
            $("#addScore").val(addScore);
            $("#wishMax").val(wishMax);
            $("#wasteGoldCount").val(wasteGoldCount);

            //保底奖励配置
            var count=0;
            var btn_idInit=0;
            count_01=0;
            $.each(parsedJson.score, function(i) {
                var score=parsedJson.score[i];

                baodijiangliGrp($("#baoDiRewardBtn").get(0));
                $("input[name='score']").eq(i).val(score);

                var btn_id="showRewardModel"+btn_idInit;
                var baoDiRewardArr=parsedJson.baoDiReward[i].split(",");//得到一行的数据;
                $.each(baoDiRewardArr,function (j) {
                    var wordItem=baoDiRewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@baoDiReward"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='baoDiReward']").eq(i).val(baoDiRewardArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });
            });
            //奖池数据配置
            var btn_idInit2=0;
            count_02=0;
            $.each(parsedJson.keyWeight, function(i) {
                var keyWeight=parsedJson.keyWeight[i];
                var goldWeight=parsedJson.goldWeight[i];
                var isBig=parsedJson.isBig[i];
                var isShow=parsedJson.isShow[i];

                jiangliGrp($("#jiangliBtn").get(0));
                $("input[name='keyWeight']").eq(i).val(keyWeight);
                $("input[name='goldWeight']").eq(i).val(goldWeight);
                $(":input[name='isBig']").eq(i).val(isBig);
                $(":input[name='isShow']").eq(i).val(isShow);

                var btn_id="showRewardModel2"+btn_idInit2;
                var rewardDataArr=parsedJson.rewardData[i].split(",");//得到一行的数据;
                $.each(rewardDataArr,function (j) {
                    var wordItem=rewardDataArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@rewardData"+btn_idInit2+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='rewardData']").eq(i).val(rewardDataArr);
                    btn_idInit2+=1;
                    btn_id="showRewardModel2"+btn_idInit2;
                });
            });
            //抽奖幸运值配置
            $("#oneLuckyValue").val("");
            var oneLuckyValue=parsedJson.oneLuckyValue[0];
            $("#oneLuckyValue").val(oneLuckyValue);
            var luckyAwardGift=parsedJson.luckyAwardGift[0].split(";");

            $.each(luckyAwardGift,function (i) {
                var btn_id="addOneLuckyBtn";
                var rewardArr = luckyAwardGift[i].split("_");
                var item_id = rewardArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                console.log("item_num"+item_num);
                var itemValue = luckyAwardGift[i];//例如:12_99_1_9
                var btnName = itemValue + "@luckyAwardGift<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count+=1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='luckyAwardGift']").eq(0).val(parsedJson.luckyAwardGift[0]);

            //保底次数配置
            commonBaoDi(parsedJson,count);
            break;
        case 20:
            //删除之前动态生成的配置项
            $(".showItem").remove();
            var rewards=parsedJson.reward[0].split(";");
            // console.log("rewards:"+rewards);
            $.each(rewards,function (i) {
                console.log("i:"+i);
                var btn_id="showAddItemModel0";
                var rewardArr = rewards[i].split("_");
                var item_id = rewardArr[0];
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                var itemValue = rewards[i];//例如:12_99_1_9
                var btnName = itemValue + "@reward0<>"+btn_id;
                var item_btn_id = "item_btn_id_" + i;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='reward']").eq(0).val(parsedJson.reward[0]);
            break;
        case 21://连续累充2(购买礼包)

            //删除之前动态生成的配置项
            $("select[name='i_reach']").attr("selected",false);
            $("div[name='cfg']").remove();
            $("input[name='dayCount']").val(0);//初始化每个充值配置中充值天数计数为0
            //充值配置
            var btn_idInit=0;
            var btn_idInit2=0;
            count_01=0;
            var count = 0;
            var item_btn_id_count=0;
            $.each(parsedJson.i_reach, function(i) {//充值配置个数遍历
                var i_reach = parsedJson.i_reach[i];
                var dayCount = parsedJson.dayCount[i];
                $("select[name='i_reach']").eq(i).val(i_reach);

                for (var j=0;j<dayCount;j++) {//每个充值配置中充值天数遍历
                    var i_day = parsedJson.i_day[count];

                    addSaltIpGrp($("#addSaltIpGrpBtn"+(i+1)).get(0));
                    $("input[name='i_day']").eq(count).val(i_day);

                    //固定奖励组:
                    var btn_id="showFixRewardModel"+btn_idInit;
                    var i_fixRewardGroupArr=parsedJson.i_fixRewardGroup[count];
                    var wordItem=i_fixRewardGroupArr.split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_fixRewardGroup"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + item_btn_id_count;
                        item_btn_id_count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_fixRewardGroup']").eq(count).val(i_fixRewardGroupArr);
                    btn_idInit+=1;
                    btn_id="showFixRewardModel"+btn_idInit;

                    //累计奖励组:
                    var btn_id2="showTotalRewardModel"+btn_idInit2;
                    var i_totalRewardGroupArr=parsedJson.i_totalRewardGroup[count];
                    var wordItem2=i_totalRewardGroupArr.split(";")//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem2,function (k) {
                        var rewardArr = wordItem2[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem2[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_totalRewardGroup"+btn_idInit2+"<>"+btn_id2;
                        var item_btn_id = "item_btn_id_" + item_btn_id_count;
                        item_btn_id_count += 1;
                        $("#"+btn_id2).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_totalRewardGroup']").eq(count).val(i_totalRewardGroupArr);
                    btn_idInit2+=1;
                    btn_id2="showTotalRewardModel"+btn_idInit2;

                    count+=1;
                }
            });
            break;
        case 22://节日祝福

            //删除之前动态生成的配置项
            $("div[name='cfgDiv']:input").val();
            $("div[name='cfg']").remove();

            //补签配置
            $("#buqianid").val(parsedJson.buqianid);
            $("#buqianCost").val(parsedJson.buqianCost);

            //签到配置
            var count=0;
            var btn_idInit=0;
            count_01=0;
            countModel=0;//每次点击修改时将页面上的countModel重置为0
            $.each(parsedJson.day, function(i) {
                var day = parsedJson.day[i];
                var modelId = parsedJson.modelId[i];

                addPlayerCfg($("#playerGroupBtn").get(0));
                $("input[name='day']").eq(i).val(day);
                $("select[name='modelId']").eq(i).val(modelId);

                var btn_id="showRewardModel"+btn_idInit;
                var dayRewardArr=parsedJson.dayReward[i].split(",");//得到一行的数据;
                $.each(dayRewardArr,function (j) {
                    var wordItem=dayRewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@dayReward"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='dayReward']").eq(i).val(dayRewardArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });
            });

            //全服奖励
            var btn_idInit2=0;
            count_02=0;
            $.each(parsedJson.boxId, function(i) {
                var boxId = parsedJson.boxId[i];
                var need = parsedJson.need[i];

                addServerCfg($("#addServerCfgBtn").get(0));
                $("input[name='boxId']").eq(i).val(boxId);
                $("input[name='need']").eq(i).val(need);

                var btn_id="showRewardModel2"+btn_idInit2;
                var serverRewardArr=parsedJson.serverReward[i].split(",");//得到一行的数据;
                $.each(serverRewardArr,function (j) {
                    var wordItem=serverRewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@serverReward"+btn_idInit2+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='serverReward']").eq(i).val(serverRewardArr);
                    btn_idInit2+=1;
                    btn_id="showRewardModel2"+btn_idInit2;
                });
            });
            break;
        case 23://掷骰子

            //删除之前动态生成的配置项
            $(".showItem").remove();
            $("div[name='cfg1'] input").val('');
            $("div[name='cfg2']").remove();
            $("div[name='cfg3']").remove();
            $("div[name='cfg4']").remove();
            $("div[name='cfg5']").remove();
            $("#gridCount").val(0);

            //消耗配置
            $("#costGold").val(parsedJson.costGold[0]);

            var cost=parsedJson.cost[0].split(";");
            var count=0;
            $.each(cost,function (i) {
                var btn_id="addCostBtn";
                var costArr = cost[i].split("_");
                var item_id = costArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = costArr[1];
                var item_gender = costArr[3];
                var item_bind = costArr[2];
                console.log("item_num"+item_num);
                var itemValue = cost[i];//例如:12_99_1_9
                var btnName = itemValue + "@cost<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count += 1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='cost']").eq(0).val(parsedJson.cost[0]);

            var dailyGain=parsedJson.dailyGain[0].split(";");
            $.each(dailyGain,function (i) {
                var btn_id="addDailyGainBtn";
                var dailyGainArr = dailyGain[i].split("_");
                var item_id = dailyGainArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = dailyGainArr[1];
                var item_gender = dailyGainArr[3];
                var item_bind = dailyGainArr[2];
                console.log("item_num"+item_num);
                var itemValue = dailyGain[i];//例如:12_99_1_9
                var btnName = itemValue + "@dailyGain<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count += 1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='dailyGain']").eq(0).val(parsedJson.dailyGain[0]);
            //格子奖励
            $.each(parsedJson.grids, function(i) {
                var grids=parsedJson.grids[i];
                var grid=grids.toString().split(";");
                var btn_id = "showAddItemModel" + (i+1);
                addGridCfgSingle($("#addSingleCfgBtn").get(0));
                $.each(grid,function (j) {
                    var gridsArr = grid[j].split("_");
                    var item_id = gridsArr[0];
                    var itemName = items.get(item_id);
                    var item_num = gridsArr[1];
                    var item_gender = gridsArr[3];
                    var item_bind = gridsArr[2];
                    var itemValue = grid[j];//例如:12_99_1_9
                    var btnName = itemValue + "@grids" + (i+1) + "<>" + btn_id;
                    var item_btn_id = "item_btn_id_" + count;
                    count+=1;
                    $("#" + btn_id).before("" +
                        "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                        " id= '" + item_btn_id + "' name='" + btnName + "'>");
                    formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                });
                $("input[name='grids']").eq(i).val(grids);
            });
            $("#addSingleCfgBtn").attr("disabled", true);//设置不可点击
            // $("#addSingleCfgBtn").style.backgroundColor="#555555";

            //大奖奖励
            var bin_idInit3=0;
            count_03=0;
            $.each(parsedJson.bigGiftWeight, function(i) {
                var bigGiftWeight=parsedJson.bigGiftWeight[i];

                addBigGridCfg($("#addBigSingleCfgBtn").get(0));
                $("input[name='bigGiftWeight']").eq(i).val(bigGiftWeight);

                var btn_id="showRewardModel3"+bin_idInit3;
                var bigGiftRewardArr=parsedJson.bigGiftReward[i].split(",");//得到一行的数据;
                $.each(bigGiftRewardArr,function (j) {
                    var wordItem=bigGiftRewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@bigGiftReward"+bin_idInit3+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='bigGiftReward']").eq(i).val(bigGiftRewardArr);
                    bin_idInit3+=1;
                    btn_id="showRewardModel3"+bin_idInit3;
                });
            });


            //个人奖励
            var btn_idInit=0;
            count_01=0;
            $.each(parsedJson.playerProc, function(p) {
                var playerProc = parsedJson.playerProc[p];

                addPlayerCfg($("#playerGroupBtn").get(0));
                $("input[name='playerProc']").eq(p).val(playerProc);

                var btn_id="showRewardModel"+btn_idInit;
                var playerTimesArr=parsedJson.playerTimes[p].split(",");//得到一行的数据;
                $.each(playerTimesArr,function (j) {
                    var wordItem=playerTimesArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@playerTimes"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='playerTimes']").eq(p).val(playerTimesArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });
            });

            //全服奖励
            var btn_idInit2=0;
            count_02=0;
            $.each(parsedJson.serverProc, function(q) {
                var serverProc = parsedJson.serverProc[q];

                addServerCfg($("#addServerCfgBtn").get(0));
                $("input[name='serverProc']").eq(q).val(serverProc);

                var btn_id="showRewardModel2"+btn_idInit2;
                var serverTimesArr=parsedJson.serverTimes[q].split(",");//得到一行的数据;
                $.each(serverTimesArr,function (j) {
                    var wordItem=serverTimesArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@serverTimes"+btn_idInit2+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='serverTimes']").eq(q).val(serverTimesArr);
                    btn_idInit2+=1;
                    btn_id="showRewardModel2"+btn_idInit2;
                });
            });
            //抽奖幸运值配置
            $("#oneLuckyValue").val("");
            var oneLuckyValue=parsedJson.oneLuckyValue[0];
            $("#oneLuckyValue").val(oneLuckyValue);
            var luckyAwardGift=parsedJson.luckyAwardGift[0].split(";");

            $.each(luckyAwardGift,function (i) {
                var btn_id="addOneLuckyBtn";
                var rewardArr = luckyAwardGift[i].split("_");
                var item_id = rewardArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                console.log("item_num"+item_num);
                var itemValue = luckyAwardGift[i];//例如:12_99_1_9
                var btnName = itemValue + "@luckyAwardGift<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count+=1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='luckyAwardGift']").eq(0).val(parsedJson.luckyAwardGift[0]);
            break;

        case 24://外观展示
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            $("#cfgCount1").val(0);
            var count = 0;
            $.each(parsedJson.show, function(i) {
                var show=parsedJson.show[i];
                var toFunction=parsedJson.toFunction[i];

                showGrp($("#showBtn").get(0));
                $("input[name='toFunction']").eq(i).val(toFunction);

                var show_i=show.toString().split(";");//例如:["12_1_1_9", "41012_1_1_9"]
                var btn_id="showAddItemModel"+i;
                $.each(show_i,function (q) {
                    var rewardArr = show_i[q].split("_");
                    var item_id = rewardArr[0];
                    var itemName = items.get(item_id);
                    var item_num = rewardArr[1];
                    var item_gender = rewardArr[3];
                    var item_bind = rewardArr[2];
                    var itemValue = show_i[q];//例如:12_99_1_9
                    var btnName = itemValue + "@show"+i+"<>"+btn_id;
                    var item_btn_id = "item_btn_id_" + count;
                    count += 1;
                    $("#"+btn_id).before("" +
                        "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                        " id= '" + item_btn_id + "' name='" + btnName + "'>");
                    formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                });
                $("input[name='show']").eq(i).val(show);
            });
            break;

        case 25://登录展示
            var xuanJianPic=parsedJson.xuanJianPic[0];
            var tianYingPic=parsedJson.tianYingPic[0];
            var diZangPic=parsedJson.diZangPic[0];
            var luoChaPic=parsedJson.luoChaPic[0];
            var toFunction=parsedJson.toFunction[0];

            $("#xuanJianPic").val(xuanJianPic);
            $("#tianYingPic").val(tianYingPic);
            $("#diZangPic").val(diZangPic);
            $("#luoChaPic").val(luoChaPic);
            $("#toFunction").val(toFunction);
            break;

        case 26://聚宝盆
            $(".showItem").remove();
            $("div[name='cfg0']").remove();
            $("div[name='cfg1']").remove();
            $("div[name='cfg2']").remove();
            $("div[name='cfg3']").remove();
            $("div[name='cfg4']").remove();
            $("div[name='cfg5']").remove();
            $("div[name='cfg6']").remove();
            $("div[name='cfg7']").remove();
            $("#gridCount").val(0);
            var count=0;
            //客户端奖励展示
            $.each(parsedJson.grids, function(i) {
                var grids=parsedJson.grids[i];
                var grid=grids.toString().split(";");
                var btn_id = "showAddItemModel" + (i+1);
                addShowGridSingle($("#addShowGridCfgBtn").get(0));
                $.each(grid,function (j) {
                    var gridsArr = grid[j].split("_");
                    var item_id = gridsArr[0];
                    var itemName = items.get(item_id);
                    var item_num = gridsArr[1];
                    var item_gender = gridsArr[3];
                    var item_bind = gridsArr[2];
                    var itemValue = grid[j];//例如:12_99_1_9
                    var btnName = itemValue + "@grids" + (i+1) + "<>" + btn_id;
                    var item_btn_id = "item_btn_id_" + count;
                    count+=1;
                    $("#" + btn_id).before("" +
                        "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                        " id= '" + item_btn_id + "' name='" + btnName + "'>");
                    formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                });
                $("input[name='grids']").eq(i).val(grids);
            });
            $("#addShowGridCfgBtn").attr("disabled", true);//设置不可点击

            //抽奖道具
            var i_big_limit=parsedJson.i_big_limit[0];
            var i_one_limit=parsedJson.i_one_limit[0];
            var i_two_limit=parsedJson.i_two_limit[0];
            var i_three_limit=parsedJson.i_three_limit[0];
            var i_gold_limit=parsedJson.i_gold_limit[0];
            var i_costItem=parsedJson.i_costItem[0];
            var i_oneCostItem=parsedJson.i_oneCostItem[0];
            var i_oneCostGold=parsedJson.i_oneCostGold[0];
            var i_tenCostItem=parsedJson.i_tenCostItem[0];
            var i_tenCostGold=parsedJson.i_tenCostGold[0];

            $("#i_big_limit").val(i_big_limit);
            $("#i_one_limit").val(i_one_limit);
            $("#i_two_limit").val(i_two_limit);
            $("#i_three_limit").val(i_three_limit);
            $("#i_gold_limit").val(i_gold_limit);
            $("#i_costItem").val(i_costItem);
            $("#i_oneCostItem").val(i_oneCostItem);
            $("#i_oneCostGold").val(i_oneCostGold);
            $("#i_tenCostItem").val(i_tenCostItem);
            $("#i_tenCostGold").val(i_tenCostGold);

            var i_GiveGift=parsedJson.i_GiveGift[0].split(";");
            $.each(i_GiveGift,function (i) {
                var btn_id="addGiveGiftBtn";
                var rewardArr = i_GiveGift[i].split("_");
                var item_id = rewardArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                console.log("item_num"+item_num);
                var itemValue = i_GiveGift[i];//例如:12_99_1_9
                var btnName = itemValue + "@i_GiveGift<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count += 1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='i_GiveGift']").eq(0).val(parsedJson.i_GiveGift[0]);

            //纯道具抽权重
            var i_item_big=parsedJson.i_item_big[0];
            var i_item_one=parsedJson.i_item_one[0];
            var i_item_two=parsedJson.i_item_two[0];
            var i_item_three=parsedJson.i_item_three[0];

            $("#i_item_big").val(i_item_big);
            $("#i_item_one").val(i_item_one);
            $("#i_item_two").val(i_item_two);
            $("#i_item_three").val(i_item_three);

            //花金元宝抽权重
            var i_gold_big=parsedJson.i_gold_big[0];
            var i_gold_one=parsedJson.i_gold_one[0];
            var i_gold_two=parsedJson.i_gold_two[0];
            var i_gold_three=parsedJson.i_gold_three[0];

            $("#i_gold_big").val(i_gold_big);
            $("#i_gold_one").val(i_gold_one);
            $("#i_gold_two").val(i_gold_two);
            $("#i_gold_three").val(i_gold_three);

            //奖池数据配置(大奖奖池数据)
            var btn_idInit=0;
            count_01=0;
            $.each(parsedJson.i_item_weight_big, function(i) {
                var i_item_weight_big=parsedJson.i_item_weight_big[i];
                var i_gold_weight_big=parsedJson.i_gold_weight_big[i];

                jiangliGrpBig($("#jiangliBtnBig").get(0));
                $("input[name='i_item_weight_big']").eq(i).val(i_item_weight_big);
                $("input[name='i_gold_weight_big']").eq(i).val(i_gold_weight_big);

                var btn_id="showRewardModel"+btn_idInit;
                var i_rewardArr=parsedJson.i_reward_big[i].split(",");//得到一行的数据;
                $.each(i_rewardArr,function (j) {
                    var wordItem=i_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_reward_big"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_reward_big']").eq(i).val(i_rewardArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel"+btn_idInit;
                });
            });

            //奖池数据配置(一等奖奖池数据)
            var btn_idInit2=0;
            count_02=0;
            $.each(parsedJson.i_item_weight_one, function(i) {
                var i_item_weight_one=parsedJson.i_item_weight_one[i];
                var i_gold_weight_one=parsedJson.i_gold_weight_one[i];

                jiangliGrpOne($("#jiangliBtnOne").get(0));
                $("input[name='i_item_weight_one']").eq(i).val(i_item_weight_one);
                $("input[name='i_gold_weight_one']").eq(i).val(i_gold_weight_one);

                var btn_id="showRewardModel2"+btn_idInit2;
                var i_rewardArr=parsedJson.i_reward_one[i].split(",");//得到一行的数据;
                $.each(i_rewardArr,function (j) {
                    var wordItem=i_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_reward_one"+btn_idInit2+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_reward_one']").eq(i).val(i_rewardArr);
                    btn_idInit2+=1;
                    btn_id="showRewardModel2"+btn_idInit2;
                });
            });

            //奖池数据配置(二等奖奖池数据)
            var btn_idInit3=0;
            count_03=0;
            $.each(parsedJson.i_item_weight_two, function(i) {
                var i_item_weight_two=parsedJson.i_item_weight_two[i];
                var i_gold_weight_two=parsedJson.i_gold_weight_two[i];

                jiangliGrpTwo($("#jiangliBtnTwo").get(0));
                $("input[name='i_item_weight_two']").eq(i).val(i_item_weight_two);
                $("input[name='i_gold_weight_two']").eq(i).val(i_gold_weight_two);

                var btn_id="showRewardModel3"+btn_idInit3;
                var i_rewardArr=parsedJson.i_reward_two[i].split(",");//得到一行的数据;
                $.each(i_rewardArr,function (j) {
                    var wordItem=i_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_reward_two"+btn_idInit3+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_reward_two']").eq(i).val(i_rewardArr);
                    btn_idInit3+=1;
                    btn_id="showRewardModel3"+btn_idInit3;
                });
            });

            //奖池数据配置(三等奖奖池数据)
            var btn_idInit4=0;
            count_04=0;
            $.each(parsedJson.i_item_weight_three, function(i) {
                var i_item_weight_three=parsedJson.i_item_weight_three[i];
                var i_gold_weight_three=parsedJson.i_gold_weight_three[i];

                jiangliGrpThree($("#jiangliBtnThree").get(0));
                $("input[name='i_item_weight_three']").eq(i).val(i_item_weight_three);
                $("input[name='i_gold_weight_three']").eq(i).val(i_gold_weight_three);

                var btn_id="showRewardModel4"+btn_idInit4;
                var i_rewardArr=parsedJson.i_reward_three[i].split(",");//得到一行的数据;
                $.each(i_rewardArr,function (j) {
                    var wordItem=i_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_reward_three"+btn_idInit4+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_reward_three']").eq(i).val(i_rewardArr);
                    btn_idInit4+=1;
                    btn_id="showRewardModel4"+btn_idInit4;
                });
            });

            //保底次数配置
            // var i_baodi_big_num=parsedJson.i_baodi_big_num[0];
            // var i_baodi_one_num=parsedJson.i_baodi_one_num[0];
            // var i_baodi_two_num=parsedJson.i_baodi_two_num[0];
            // var i_baodi_three_num=parsedJson.i_baodi_three_num[0];

            // $("#i_baodi_big_num").val(i_baodi_big_num);
            // $("#i_baodi_one_num").val(i_baodi_one_num);
            // $("#i_baodi_two_num").val(i_baodi_two_num);
            // $("#i_baodi_three_num").val(i_baodi_three_num);

            //物品幸运值配置
            $("#oneLuckyValue").val("");
            var oneLuckyValue=parsedJson.oneLuckyValue[0];
            $("#oneLuckyValue").val(oneLuckyValue);
            var luckyAwardGift=parsedJson.luckyAwardGift[0].split(";");

            $.each(luckyAwardGift,function (i) {
                var btn_id="addOneLuckyBtn";
                var rewardArr = luckyAwardGift[i].split("_");
                var item_id = rewardArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                console.log("item_num"+item_num);
                var itemValue = luckyAwardGift[i];//例如:12_99_1_9
                var btnName = itemValue + "@luckyAwardGift<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count+=1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='luckyAwardGift']").eq(0).val(parsedJson.luckyAwardGift[0]);

            //金元宝池配置
            var i_item_probability=parsedJson.i_item_probability[0];
            var i_gold_probability=parsedJson.i_gold_probability[0];
            var i_get_probability=parsedJson.i_get_probability[0];
            var i_gold_init=parsedJson.i_gold_init[0];
            var i_gold_standard=parsedJson.i_gold_standard[0];
            var i_gold_upper_limit=parsedJson.i_gold_upper_limit[0];
            var i_gold_set=parsedJson.i_gold_set[0];
            var i_total_limit=parsedJson.i_total_limit[0];
            var i_one_reward_limit=parsedJson.i_one_reward_limit[0];
            var i_day_reward_limit=parsedJson.i_day_reward_limit[0];

            $("#i_item_probability").val(i_item_probability);
            $("#i_gold_probability").val(i_gold_probability);
            $("#i_get_probability").val(i_get_probability);
            $("#i_gold_init").val(i_gold_init);
            $("#i_gold_standard").val(i_gold_standard);
            $("#i_gold_upper_limit").val(i_gold_upper_limit);
            $("#i_gold_set").val(i_gold_set);

            $("#i_total_limit").val(i_total_limit);
            $("#i_one_reward_limit").val(i_one_reward_limit);
            $("#i_day_reward_limit").val(i_day_reward_limit);

            //金元宝池保底
            var i_baodi_scope_min=parsedJson.i_baodi_scope_min[0];
            var i_baodi_scope_max=parsedJson.i_baodi_scope_max[0];

            $("#i_baodi_scope_min").val(i_baodi_scope_min);
            $("#i_baodi_scope_max").val(i_baodi_scope_max);

            //累计领奖配置
            var btn_idInit5=0;
            count_05=0;
            $.each(parsedJson.i_countReward_num, function(i) {
                var i_countReward_num=parsedJson.i_countReward_num[i];

                countRewardGrp($("#countRewardBtn").get(0));
                $("input[name='i_countReward_num']").eq(i).val(i_countReward_num);

                var btn_id="showRewardModel5"+btn_idInit5;
                var i_rewardArr=parsedJson.i_countReward[i].split(",");//得到一行的数据;
                $.each(i_rewardArr,function (j) {
                    var wordItem=i_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_countReward"+btn_idInit5+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_countReward']").eq(i).val(i_rewardArr);
                    btn_idInit5+=1;
                    btn_id="showRewardModel5"+btn_idInit5;
                });
            });

            //保底次数配置
            commonBaoDi(parsedJson,count);
            break;
        case 27://幸运砸蛋
            $(".showItem").remove();
            $("div[name='cfg0']").remove();
            $("div[name='cfg1']").remove();
            $("div[name='cfg2']").remove();
            $("div[name='cfg3']").remove();
            $("div[name='cfg4']").remove();
            $("div[name='cfg5']").remove();
            $("div[name='cfg6']").remove();
            $("div[name='cfg7']").remove();
            $("#gridCount").val(0);
            var count=0;
            //客户端奖励展示
            var btn_idInit0=0;
            count_00=0;
            $.each(parsedJson.grids, function(i) {
                var grids=parsedJson.grids[i];
                var grid=grids.toString().split(";");
                var btn_id = "showAddItemModel" + btn_idInit0;
                addShowGridCfg($("#addShowGridCfgBtn").get(0));
                $.each(grid,function (j) {
                    var gridsArr = grid[j].split("_");
                    var item_id = gridsArr[0];
                    var itemName = items.get(item_id);
                    var item_num = gridsArr[1];
                    var item_gender = gridsArr[3];
                    var item_bind = gridsArr[2];
                    var itemValue = grid[j];//例如:12_99_1_9
                    var btnName = itemValue + "@grids" + btn_idInit0 + "<>" + btn_id;
                    var item_btn_id = "item_btn_id_" + count;
                    count+=1;
                    $("#" + btn_id).before("" +
                        "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                        " id= '" + item_btn_id + "' name='" + btnName + "'>");
                    formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                });
                $("input[name='grids']").eq(i).val(grids);
                btn_idInit0+=1;
                btn_id="showRewardModel"+btn_idInit0;
            });

            //砸蛋道具
            var i_costItem=parsedJson.i_costItem[0];
            var i_oneCostItem=parsedJson.i_oneCostItem[0];
            var i_oneCostGold=parsedJson.i_oneCostGold[0];
            var dailyLimitCount=parsedJson.dailyLimitCount[0];

            $("#i_costItem").val(i_costItem);
            $("#i_oneCostItem").val(i_oneCostItem);
            $("#i_oneCostGold").val(i_oneCostGold);
            $("#dailyLimitCount").val(dailyLimitCount);

            var i_GiveGift=parsedJson.i_GiveGift[0].split(";");
            $.each(i_GiveGift,function (i) {
                var btn_id="addGiveGiftBtn";
                var rewardArr = i_GiveGift[i].split("_");
                var item_id = rewardArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                console.log("item_num"+item_num);
                var itemValue = i_GiveGift[i];//例如:12_99_1_9
                var btnName = itemValue + "@i_GiveGift<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count += 1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='i_GiveGift']").eq(0).val(parsedJson.i_GiveGift[0]);

            //纯道具抽权重
            var i_item_one=parsedJson.i_item_one[0];
            var i_item_two=parsedJson.i_item_two[0];
            var i_item_three=parsedJson.i_item_three[0];

            $("#i_item_one").val(i_item_one);
            $("#i_item_two").val(i_item_two);
            $("#i_item_three").val(i_item_three);

            //花金元宝抽权重
            var i_gold_one=parsedJson.i_gold_one[0];
            var i_gold_two=parsedJson.i_gold_two[0];
            var i_gold_three=parsedJson.i_gold_three[0];

            $("#i_gold_one").val(i_gold_one);
            $("#i_gold_two").val(i_gold_two);
            $("#i_gold_three").val(i_gold_three);

            //奖池数据配置(彩蛋奖池数据)
            var btn_idInit=0;
            count_01=0;
            $.each(parsedJson.i_item_weight_one, function(i) {
                var i_item_weight_one=parsedJson.i_item_weight_one[i];
                var i_gold_weight_one=parsedJson.i_gold_weight_one[i];
                var isShow_one=parsedJson.isShow_one[i];

                jiangliGrpOne($("#jiangliBtnOne").get(0));
                $("input[name='i_item_weight_one']").eq(i).val(i_item_weight_one);
                $("input[name='i_gold_weight_one']").eq(i).val(i_gold_weight_one);
                $("select[name='isShow_one']").eq(i).val(isShow_one);

                var btn_id="showRewardModel2"+btn_idInit;
                var i_rewardArr=parsedJson.i_reward_one[i].split(",");//得到一行的数据;
                $.each(i_rewardArr,function (j) {
                    var wordItem=i_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_reward_one"+btn_idInit+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_reward_one']").eq(i).val(i_rewardArr);
                    btn_idInit+=1;
                    btn_id="showRewardModel2"+btn_idInit;
                });
            });

            //奖池数据配置(金蛋奖池数据)
            var btn_idInit2=0;
            count_02=0;
            $.each(parsedJson.i_item_weight_two, function(i) {
                var i_item_weight_two=parsedJson.i_item_weight_two[i];
                var i_gold_weight_two=parsedJson.i_gold_weight_two[i];
                var isShow_two=parsedJson.isShow_two[i];

                jiangliGrpTwo($("#jiangliBtnTwo").get(0));
                $("input[name='i_item_weight_two']").eq(i).val(i_item_weight_two);
                $("input[name='i_gold_weight_two']").eq(i).val(i_gold_weight_two);
                $("select[name='isShow_two']").eq(i).val(isShow_two);

                var btn_id="showRewardModel3"+btn_idInit2;
                var i_rewardArr=parsedJson.i_reward_two[i].split(",");//得到一行的数据;
                $.each(i_rewardArr,function (j) {
                    var wordItem=i_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_reward_two"+btn_idInit2+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_reward_two']").eq(i).val(i_rewardArr);
                    btn_idInit2+=1;
                    btn_id="showRewardModel3"+btn_idInit2;
                });
            });
            //奖池数据配置(银蛋奖池数据)
            var btn_idInit3=0;
            count_03=0;
            $.each(parsedJson.i_item_weight_three, function(i) {
                var i_item_weight_three=parsedJson.i_item_weight_three[i];
                var i_gold_weight_three=parsedJson.i_gold_weight_three[i];
                var isShow_three=parsedJson.isShow_three[i];

                jiangliGrpThree($("#jiangliBtnThree").get(0));
                $("input[name='i_item_weight_three']").eq(i).val(i_item_weight_three);
                $("input[name='i_gold_weight_three']").eq(i).val(i_gold_weight_three);
                $("select[name='isShow_three']").eq(i).val(isShow_three);

                var btn_id="showRewardModel4"+btn_idInit3;
                var i_rewardArr=parsedJson.i_reward_three[i].split(",");//得到一行的数据;
                $.each(i_rewardArr,function (j) {
                    var wordItem=i_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_reward_three"+btn_idInit3+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_reward_three']").eq(i).val(i_rewardArr);
                    btn_idInit3+=1;
                    btn_id="showRewardModel4"+btn_idInit3;
                });
            });

            //物品幸运值配置
            $("#oneLuckyValue").val("");
            var oneLuckyValue=parsedJson.oneLuckyValue[0];
            $("#oneLuckyValue").val(oneLuckyValue);
            var luckyAwardGift=parsedJson.luckyAwardGift[0].split(";");

            $.each(luckyAwardGift,function (i) {
                var btn_id="addOneLuckyBtn";
                var rewardArr = luckyAwardGift[i].split("_");
                var item_id = rewardArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                console.log("item_num"+item_num);
                var itemValue = luckyAwardGift[i];//例如:12_99_1_9
                var btnName = itemValue + "@luckyAwardGift<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count+=1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='luckyAwardGift']").eq(0).val(parsedJson.luckyAwardGift[0]);

            //彩蛋刷新配置
            var i_refresh_item=parsedJson.i_refresh_item[0];
            var i_refresh_item_num=parsedJson.i_refresh_item_num[0];
            var i_refresh_gold_num=parsedJson.i_refresh_gold_num[0];
            var i_refresh=parsedJson.i_refresh[0];

            $("#i_refresh_item").val(i_refresh_item);
            $("#i_refresh_item_num").val(i_refresh_item_num);
            $("#i_refresh_gold_num").val(i_refresh_gold_num);
            $("#i_refresh").val(i_refresh);

            //累计领奖配置
            var btn_idInit5=0;
            count_05=0;
            $.each(parsedJson.i_countReward_num, function(i) {
                var i_countReward_num=parsedJson.i_countReward_num[i];

                countRewardGrp($("#countRewardBtn").get(0));
                $("input[name='i_countReward_num']").eq(i).val(i_countReward_num);

                var btn_id="showRewardModel5"+btn_idInit5;
                var i_rewardArr=parsedJson.i_countReward[i].split(",");//得到一行的数据;
                $.each(i_rewardArr,function (j) {
                    var wordItem=i_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                    $.each(wordItem,function (k) {
                        var rewardArr = wordItem[k].toString().split("_");//拆分数据
                        var item_id = rewardArr[0];
                        var itemName = items.get(item_id);
                        var item_num = rewardArr[1];
                        var item_gender = rewardArr[3];
                        var item_bind = rewardArr[2];
                        itemValue = wordItem[k];//例如:12_99_1_9
                        var btnName = itemValue + "@i_countReward"+btn_idInit5+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='i_countReward']").eq(i).val(i_rewardArr);
                    btn_idInit5+=1;
                    btn_id="showRewardModel5"+btn_idInit5;
                });
            });
            //保底次数配置
            commonBaoDi(parsedJson,count);
            break;
    }
}

/**
 * 添加物品显示模态框事件
 * @param index 档位
 */
function changeCurItemDocObj(index) {
    curItemBoxIndex = index;
    $("#itemId").val("");
    $("#itemNum").val("");
    $("#isBind").val("0");
    $("#addItemModal").modal('show');
}

/**
 * 添加物品
 */
function addItem() {
    var itemId = $("#itemId").val();
    var itemName = items.get(itemId);
    var itemNum = $("#itemNum").val();
    var isBind = $("#isBind").val();
    var itemValue = itemId + "," + itemNum + "," + isBind;
    console.log(itemValue + ":" + itemName);
    var lastValue = $("#reward" + curItemBoxIndex).val();
    var itemArray = lastValue.split('}');
    if (lastValue == "") {
        itemArray = new Array();
    }
    itemArray.push(itemValue);
    $("#reward" + curItemBoxIndex).val(itemArray.join("}"));
    console.log(itemArray.join("}"));
    $("#items" + curItemBoxIndex).append("<span class=\"label\">" + itemName + "*" + itemNum +
        "<i class=\"icon-remove item-remove\" onclick=\"removeItem(this, " + itemId + ", " + itemNum + ")\"></i></span>");
    $("#addItemModal").modal('hide');
}

/**
 * 删除物品
 */
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

/**
 * 加载服务器列表信息
 */
function loadAllServerList() {
    $.ajax({
        url: base + "/server/getTestServerMap",
        method: "post",
        dataType: "json",
        success: function (data) {
            // console.log(data);
            testSeverInfo = data;
        }
    });
    $.ajax({
        url: base + "/server/getOfficalServerMap",
        method: "post",
        dataType: "json",
        success: function (data) {
            // console.log(data);
            officialServerInfo = data;
        }
    });
    $.ajax({
        url: base + "/server/getNoHeFuServerMap",
        method: "post",
        dataType: "json",
        success: function (data) {
            // console.log(data);
            noHeFuServerInfo = data;
        }
    });
}

// /**
//  * 切换服务器组
//  */
// function changePlatformEvent() {
//     var operationType = $("#operationType").val();
//     var platform = $("#platform").val();
//     var serverInfo;
//     if (operationType == 1) {
//         serverInfo = testSeverInfo;
//     } else if (operationType == 3) {
//         serverInfo = officialServerInfo;
//     }
//     $("#servers").empty();
//     console.log(serverInfo);
//     $.each(serverInfo[platform], function (i, n) {
//         $("#servers").append("<label class=\"checkbox inline\">" + n.serverName+"("+n.serverId+")"+
//             "</label><input type=\"checkbox\" name=\"sid\" value=\"" + n.serverId + "\"/>");
//     });
// }

/**
 * 切换服务器组
 */
function changePlatformEvent() {
    var operationType = $("#operationType").val();
    var platform = $("#platform").val();
    console.log("aaa:"+noHeFuServerInfo);
    var serverInfo = noHeFuServerInfo;
    $("#servers").empty();
    console.log(serverInfo);
    $.each(serverInfo[platform], function (i, n) {
        $("#servers").append("<label class=\"checkbox inline\">" + n.serverName+"("+n.serverId+")"+
            "</label><input type=\"checkbox\" name=\"sid\" value=\"" + n.serverId + "\"/>");
    });
}

/**
 * 导出运营活动配置数据
 * @param obj
 */
function exportActivityData(i,obj) {
    var activityId=$(obj).parent().parent().parent().find("td").eq(0).text();
    console.log(activityId);

    var data = new FormData();
    data.append('id', activityId);
    var fname = 'actForm'+i;
    // $("#actForm").attr("action", baseUrl + "/exportActivityData");
    $("form[name=\'"+fname+"\']").submit();

    // $.post(baseUrl + "/exportActivityData", data);

    // $.ajax({
    //     url: baseUrl + "/exportActivityData",
    //     method: 'POST',
    //     data: data,
    //     processData: false,
    //     contentType: false,
    //     success: function (data) {
    //         // $("#questionnaireLoadModel").modal('hide');
    //         // alert(data.msg);
    //     }
    // });






}

/**
 * 导入运营活动配置数据
 * @param obj
 */
function importf(obj) {
    var data = new FormData();
    data.append('ActivityFile', $(obj).prop('files')[0]);
    console.log("data:"+data);
    $.ajax({
        url: baseUrl + "/importActivityData",
        method: 'POST',
        data: data,
        processData: false,
        contentType: false,
        success: function (data) {
            alert(data.msg);
            load(base, actType);
        }
    });
}
//检测每组保底配置的父类是否符合要求
function checkBaodi() {
    var flag = true;
    $("input[name='i_baoDi_min_num']").each(function (index) {
        if (Number($("input[name='i_baoDi_min_num']").eq(index).val()) >= Number($("input[name='i_baoDi_max_num']").eq(index).val())){
            alert("第"+(index + 1)+"组保底最大次数必须大于保底最小次数！");
            flag = false;
            return flag;
        }
        if (index > 0){
            if (Number($("input[name='i_baoDi_min_num']").eq(index).val()) <= Number($("input[name='i_baoDi_max_num']").eq(index - 1).val())){
                alert("第"+(index + 1)+"组保底最小次数必须大于前一组的保底最大次数！");
                flag = false;
                return flag;
            }
        }
        // console.log($(this).val());
    });
    return flag;
}
//检测每组保底配置的区间配置是否符合要求
function checkBaoDiRange() {
    var rangeCount = 0;
    var flag = true;
    $("input[name='i_baoDi_min_num']").each(function (index) {
        var count = 0;
        var baoDi_min_num = Number($("input[name='i_baoDi_min_num']").eq(index).val());
        var baoDi_max_num = Number($("input[name='i_baoDi_max_num']").eq(index).val());
        var initCount = Number($("input[name='i_baoDi_range_count']").eq(index).val());
        for (var i=0;i<initCount;i++){
            count+=1;
            if (count == 1){//标识每组保底配置的首组区间
                var rangeFirst = Number($("input[name='i_baoDi_range_min']").eq(rangeCount + i).val());
                rangeCount = rangeCount + initCount;
            }
        }
        var rangeLast = Number($("input[name='i_baoDi_range_max']").eq(rangeCount - 1).val());
        if (rangeFirst != baoDi_min_num){
            alert("第"+(index + 1)+"组保底区间第一个区间最小必须等于该组的保底最小次数！");
            flag = false;
            return flag;
        }
        if (rangeLast != baoDi_max_num){
            alert("第"+(index + 1)+"组保底区间最后一个区间最大必须等于该组的保底最大次数！");
            flag = false;
            return flag;
        }
    });
    return flag;
}
//对于每组保底配置中的区间配置信息验证(具体内部)
function checkBaoDiRangeInfo() {
    var rangeCount = 0;
    var flag = true;
    $("input[name='i_baoDi_min_num']").each(function (index) {//保底配置父级
        var initCount = Number($("input[name='i_baoDi_range_count']").eq(index).val());
        for (var i=0;i<initCount;i++){//保底配置子级
            if (i>0){//区间配置中从第二行开始需要检测
                var rangeMin = Number($("input[name='i_baoDi_range_min']").eq(rangeCount + i).val());
                var rangeMax = Number($("input[name='i_baoDi_range_max']").eq(rangeCount + i).val());
                if (rangeMin >= rangeMax){
                    alert("第"+(index + 1)+"组保底配置中的区间配置第"+(i+1)+"行区间最大必须大于区间最小！");
                    flag = false;
                    return flag;
                }
                var preRangeMax = Number($("input[name='i_baoDi_range_max']").eq(rangeCount + i-1).val());
                if (rangeMin != (preRangeMax + 1)){
                    alert("第"+(index + 1)+"组保底配置中的区间配置第"+(i+1)+"行区间最小必须等于前一行的区间最大值加1！");
                    flag = false;
                    return flag;
                }
            }
        }
        rangeCount = rangeCount + initCount;
    });
    return flag;
}

/**
 * 加载公用标签库数据
 */
function loadActivityTag() {
    $.ajax({
        url: base + "/activityConfig/getAllTag",
        method: "post",
        dataType: "json",
        success: function (data) {
            if (!data.ok) {
                alert("Tag load failed");
                return;
            }
            $("#tag").empty();
            $.each(data.data, function (i, n) {
                $("#tag").append("<option value='" + n.id +"'>"+n.name+"("+n.id+")</option>");
            });
        }
    });
}
function commonBaoDi(parsedJson,count) {
    //保底次数配置
    var rangeCount = 0;
    var btn_idInit6=0;
    count_06=0;
    $.each(parsedJson.i_baoDi_min_num, function(i) {
        var i_baoDi_min_num=parsedJson.i_baoDi_min_num[i];
        var i_baoDi_max_num=parsedJson.i_baoDi_max_num[i];
        var i_baoDi_range_count=parsedJson.i_baoDi_range_count[i];
        var range_minAll=parsedJson.i_baoDi_range_min;
        var range_maxAll=parsedJson.i_baoDi_range_max;
        var range_proAll=parsedJson.i_baoDi_range_pro;

        baoDiGrp($("#baoDiBtn").get(0));
        $("input[name='i_baoDi_min_num']").eq(i).val(i_baoDi_min_num);
        $("input[name='i_baoDi_max_num']").eq(i).val(i_baoDi_max_num);

        var btn_id="showRewardModel6"+btn_idInit6;
        var i_rewardArr=parsedJson.i_baoDiReward[i].split(",");//得到一行的数据;
        $.each(i_rewardArr,function (j) {
            var wordItem=i_rewardArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
            $.each(wordItem,function (k) {
                var rewardArr = wordItem[k].toString().split("_");//拆分数据
                var item_id = rewardArr[0];
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                var itemValue = wordItem[k];//例如:12_99_1_9
                var btnName = itemValue + "@i_baoDiReward"+btn_idInit6+"<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count += 1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='i_baoDiReward']").eq(i).val(i_rewardArr);
            btn_idInit6+=1;
            btn_id="showRewardModel6"+btn_idInit6;
        });

        //区间配置
        for (var j=0;j<i_baoDi_range_count;j++){
            baoDiRangeGrp($("#baoDiRangeBtn"+(i+1)).get(0));
            var range_min = range_minAll[rangeCount];
            var range_max = range_maxAll[rangeCount];
            var range_pro = range_proAll[rangeCount];
            $("input[name='i_baoDi_range_min']").eq(rangeCount).val(range_min);
            $("input[name='i_baoDi_range_max']").eq(rangeCount).val(range_max);
            $("input[name='i_baoDi_range_pro']").eq(rangeCount).val(range_pro);

            rangeCount+=1;
        }
    });
}

function baoDiGrp(obj) {
    var rewardInput = 'i_baoDiReward' + count_06;
    var rewardAddBtn = 'showRewardModel6' + count_06;
    count_06 += 1;
    html = '<div name="cfg6" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
        '<div class="col-lg-11">'+
        '<legend>保底配置</legend>'+
        '<fieldset>'+
        '<label class="input-group-addon" style="width: 105px;height:34px;float: left">保底最小次数:</label>' +
        '<input type="text" class="form-control" style="display: inline-block;width: 100px" id="i_baoDi_min_num" name="i_baoDi_min_num" onkeyup="value=value.replace(/\\D/g,\'\')">' +
        '<label class="input-group-addon" style="width: 105px;height:34px;float: left">保底最大次数:</label>' +
        '<input type="text" class="form-control" style="display: inline-block;width: 100px" id="i_baoDi_max_num" name="i_baoDi_max_num" onkeyup="value=value.replace(/\\D/g,\'\')">' +
        '<label class="input-group-addon" style="width: 105px;height:34px;float: left">保底奖励:</label>' +
        '<span class="input-group-btn" >' +
        '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
        '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUILimit(' + rewardInput + ',' + rewardAddBtn + ',4)">' +
        '<span class="glyphicon glyphicon-plus"></span></button>' +
        '<div class="hide"><input id="' + rewardInput + '" name="i_baoDiReward"/></div>' +
        '</span>' +
        '<div class="input-group" id="jianglishuju2" style="clear: both">'+
        '<label class="input-group-addon">区间配置:</label>'+
        '<input type="hidden" name="i_baoDi_range_count" value="0">'+
        '<button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="baoDiRangeBtn'+count_06+'"'+
        'onclick="baoDiRangeGrp(this)"><span class="glyphicon glyphicon-plus"></span>'+
        '</button>'+
        '</div>'+
        '</fieldset>'+
        '</div>'+
        '<span class="input-group-btn">' +
        '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delConfig6(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
        // '<button class="btn btn-info" type="button" data-toggle="tooltip" title="复制" id="copyGridGroup"><span class="glyphicon glyphicon-copy"></span></button>' +
        '</span>' +
        '</div>';
    obj.insertAdjacentHTML('beforeBegin', html);
}
function delConfig6(obj) {
    $(obj).parent().parent().remove();
}

function baoDiRangeGrp(obj) {
    count_07 +=1;
    html = '<div name="cfg7" class="input-group saltIp" style="width:100%;padding:0 0 1px 0; margin-left:fill  ">' +
        '<label class="input-group-addon">区间最小:</label>' +
        '<input type="text" class="form-control" id="i_baoDi_range_min" name="i_baoDi_range_min" onkeyup="value=value.replace(/\\D/g,\'\')">' +
        '<label class="input-group-addon">区间最大:</label>' +
        '<input type="text" class="form-control" id="i_baoDi_range_max" name="i_baoDi_range_max" onkeyup="value=value.replace(/\\D/g,\'\')">' +
        '<label class="input-group-addon">概率:</label>' +
        '<input type="text" class="form-control" id="i_baoDi_range_pro" name="i_baoDi_range_pro" onkeyup="value=value.replace(/\\D/g,\'\')">' +
        '<span class="input-group-btn">' +
        '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delConfig7(this,count_07)"><span class="glyphicon glyphicon-minus"></span></button>' +
        '</span>' +
        '</div>';
    obj.insertAdjacentHTML('beforeBegin', html);
    var count = $(obj).parent().find("input[name='i_baoDi_range_count']").val();
    $(obj).parent().find("input[name='i_baoDi_range_count']").val(count*1+1);
}
function delConfig7(obj,count) {
    count-=1;
    count_07=count;

    var count = $(obj).parent().parent().parent().find("input[name='i_baoDi_range_count']").val();
    if(count>0){
        $(obj).parent().parent().parent().find("input[name='i_baoDi_range_count']").val(count*1-1);
    }

    $(obj).parent().parent().remove();
}