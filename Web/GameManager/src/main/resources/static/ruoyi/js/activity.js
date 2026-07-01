
var prefix = ctx + "gmtool/activity";
var festivals = new Map();//节日类型
var tags = new Map();//节日类型
var items = new Map();
var isActivityList = false;
var cover = 0;

function load(actType) {
    startPopover();
    formatterDatetimepicker();
    loadAllItem();
    loadTemplate(actType);
    loadActivityFestivalType(actType);
    loadActivityTag();
    initActivityTable(1);
}

//启用页面中的所有的弹出框（popover）
function startPopover() {
    $("[data-toggle='tooltip']").tooltip();
    $("[data-toggle=popover]").popover({
        html : true
    });
}

//设置自定义数据文本框的高度
function addHeightSet() {
    $("#customForm :input[type='text']").each(function () {
        $(this).addClass("heightSet");
    });
}
//统一格式化时间选择框
function formatterDatetimepicker() {
    $(".datetimepicker").datetimepicker({
        // language:  'zh-CN',
        format : 'yyyy-mm-dd hh:ii:00',
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        startView : 2,
        minView : 0
    });
}
//获取当前页面的活动的type并赋值给页面标签
function getActivityType(actType) {
    $("#type").val(actType);
    $("#searchType").val(actType);//表格初始化的活动type
    // console.log($("#type").val());
}

/**
 * 加载活动节日类型信息
 */
function loadActivityFestivalType(type) {
    $.ajax({
        url: prefix+"/getActivityFestivalType",
        method: "post",
        dataType: "json",
        data: {
            "type": type
        },
        async:false,//活动总览不需要异步加载,其他活动不影响
        success: function (data) {

            if (!data.ok) {
                $.modal.alertError("activity festival type load failed");
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
 * 加载公用标签库数据
 */
function loadActivityTag() {
    $.ajax({
        url: ctx + "gmtool/activityConfig/getAllTag",
        method: "post",
        dataType: "json",
        async:false,//活动总览不需要异步加载,其他活动不影响
        success: function (data) {
            if (!data.ok) {
                alert("Tag load failed");
                return;
            }
            $("#tag").empty();
            $.each(data.data, function (i, n) {
                tags.set(n.id + "", n.name);
                $("#tag").append("<option value='" + n.id +"'>"+n.name+"("+n.id+")</option>");
            });
        }
    });
}
/**
 * 获取模板列表
 * @param type 活动类型
 */
function loadTemplate(type) {
    $.ajax({
        url: prefix + "/getTemplateTime",
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
//清空界面上的数据
function cleanData(type) {
    $("#activity_form :input[type='text']:not(#type)").each(function () {
        $(this).val("");
    });

    // $("#activity_form :input[type='hidden']:not(#id):not(#type)").each(function () {
    //     $(this).val("");
    // });

    $("#activity_form select").each(function(){
        $("option:first", this).prop("selected","selected");
    });

    t_v_change();

    $("div[name^='cfg']").each(function () {
        $(this).remove();
    });

    $(".input-group-btn").each(function () {
        var itemButton = $(this).find("input[type='button']");
        if (itemButton.length > 0){
            itemButton.remove();
        }
    });

    $("input:checked").each(function(i){
        $(this).prop("checked",false);
    });

    //有特殊需要清理的数据
    switch (Number(type)) {
        case 23://掷骰子
            $("#addSingleCfgBtn").attr("disabled", false);
            break;
        case 26://聚宝盆
            $("#addShowGridCfgBtn").attr("disabled", false);
            break;
    }
}

/**
 * 切换模板下拉框操作
 * 复制活动模板数据,id为0表示初始化表单数据
 */
function copyTemplate(id) {
    $("#subType").attr("disabled",false);
    $("#id").val(0);//切换模板时表示不应该是编辑,应该是新增状态
    // console.log(id);
    var type = $("#type").val();
    if (id == 0) {
        // $("#publicCondition").empty();
        // location.reload();
        cleanData(type);
        return;
    }

    $.ajax({
        url: prefix + "/getTemplate",
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
                $.modal.alertError(data.msg);
            }
        }
    });
}
/**
 * 删除模板
 */
function deleteTemplate() {
    $.modal.confirm("确认删除？",function () {
        var type = $("#type").val();
        var id = $("#template").val();
        if (id == 0) {
            $.modal.alertWarning("请选择需要删除的模板！");
            return;
        }
        $.ajax({
            url: prefix + "/deleteTemplate",
            method: "post",
            dataType: "json",
            data: {
                "id": id
            },
            success: function (data) {
                if (data.ok) {
                    $.modal.alertSuccess("模板删除成功");
                    loadTemplate(type);
                    copyTemplate(0);
                }
            }
        });
    });
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
    var templateDec = $("#templateDec").val();
    if (templateName == undefined || templateName.trim() == "") {
        templateName = type+"_"+TimeObjectUtil.UnixToymdDate(TimeObjectUtil.CurTime(), true, 8);
        // return;
    }
    var dataParam = $("#activity_form").serialize();
    dataParam = dataParam + "&templateName=" + templateName + "&templateId=" + templateId + "&templateDec=" + templateDec;
    // console.log("data:"+dataParam);
    $.ajax({
        url: prefix + "/checkTemplateName",
        async:false,
        method: "post",
        dataType: "json",
        data: dataParam,
        success: function (data) {
            if (data.ok) {
                $.ajax({
                    url: prefix + "/addTemplate",
                    method: "post",
                    dataType: "json",
                    data: dataParam,
                    success: function (data) {
                        $("#addTemplateModal").modal('hide');
                        if (data.ok) {
                            $.modal.alertSuccess("模板添加成功");
                            console.log(data);
                            loadTemplate(type);
                            copyTemplate(0);
                        } else {
                            $.modal.alertError("模板添加失败" + data.msg);
                        }
                    }
                });
            } else {
                $.modal.confirm("是否覆盖已有的模板？",function () {
                    //点击确定后操作
                    $.ajax({
                        url: prefix + "/updateTemplate",
                        method: "post",
                        dataType: "json",
                        data: dataParam,
                        success: function (data) {
                            $("#addTemplateModal").modal('hide');
                            if (data.ok) {
                                $.modal.alertSuccess("模板修改成功");
                                $("#addTemplateModal").modal('hide');
                                console.log(data);
                                loadTemplate(type);
                                copyTemplate(0);
                            } else {
                                $.modal.alertError("模板修改失败" + data.msg);
                            }
                        }
                    });
                });
            }
        }
    });

    // alert("dataParam："+dataParam);
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
        $.modal.msgError("结束时间不能小于开始时间！");
        return false;
    }

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
    if (!$('#activity_form').validate().form()) {
        $.modal.msgError("验证失败");
        return;
    }
    var flag = validateAll();
    if (flag == false){
        return;
    }
    if (!checkActivityForm(type)) {
        return;
    }
    $("#subType").attr("disabled",false);
    $.ajax({
        url: prefix + "/addActivity",
        method: "post",
        dataType: "json",
        data: $("#activity_form").serialize(),
        success: function (data) {
            if (data.ok) {
                if($("#id").val()>0){
                    $("#id").val(0);
                    cleanData(type);
                    $.modal.alertSuccess("活动修改成功");
                    $.table.search("formId","bootstrap-table");
                }else{
                    cleanData(type);
                    $.modal.alertSuccess("活动添加成功");
                    $.table.search("formId","bootstrap-table");
                }

                // copyTemplate(0);
                if (callback) {
                    callback();
                }
            } else {
                $.modal.alertError("活动添加失败" + data.msg);
            }
        }
    });
}

/**
 * 导入运营活动配置数据
 * @param obj
 */
function importf(obj) {
    var data = new FormData();
    data.append('activityFile', $(obj).prop('files')[0]);
    console.log("data:"+data);
    $.ajax({
        url: prefix + "/importActivityData",
        method: 'POST',
        data: data,
        processData: false,
        contentType: false,
        success: function (data) {
            if (data.ok){
                $.modal.alertSuccess(data.msg);
                if (isActivityList == true){
                    $.table.search("actListFormId","bootstrap-table");//活动总览页面查询
                }else {
                    $.table.search("formId","bootstrap-table");//通用单类型活动查询
                }
            } else {
                $.modal.alertError(data.msg);
                if (isActivityList == true){
                    $.table.search("actListFormId","bootstrap-table");//活动总览页面查询
                }else {
                    $.table.search("formId","bootstrap-table");//通用单类型活动查询
                }
            }
        }
    });
}

/**
 * 运营活动编辑
 * @param id
 */
function editActData(id) {
    $.modal.confirm("是否修改当前活动配置？",function () {
        $("#subType").attr("disabled",true);
        $("#template").val(0);
        copyActivity(id);
    });
};

/**
 * 复制活动模板数据,id为0表示初始化表单数据
 */
function copyActivity(id) {
    // console.log(id);
    if (id == 0) {
        return;
    }

    $.ajax({
        url: prefix + "/queryActivityById",
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
                $.modal.alertError(data.msg)
            }
        }
    });
}

/**
 * 导出运营活动配置数据
 * @param obj
 */
function exportActivityData(i,obj) {
    var activityId=$(obj).parent().parent().parent().find("td").eq(1).text();
    console.log("activityId:"+activityId);

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
 * 运营活动批量发布
 */
function publishEvent(type) {
    var requestUrl = $("#requestUrl").val();
    var ignoreMerge = $("#ignoreMerge").val();
    var modelName = "activityServerList";
    var actIds = $.table.selectColumns("id");
    console.log("actIds:"+actIds);
    if (actIds.length == 0) {
        $.modal.alertWarning("未选择活动");
        return;
    }
    var url = 'requestUrl='+requestUrl+'&ignoreMerge='+ignoreMerge+'&modelName='+modelName+'&actIds='+actIds;
    openServerList(url,function (index, layero) {
        $.modal.confirm("是否覆盖正在进行的活动?",function () {
            //点击确定后操作
            cover = 1;
            var iframeWin = getIframeWin(layero);
            var selections =  selectionsSid(iframeWin);
            var platform = getGroupName(iframeWin);
            // console.log("aaaa:"+typeof (selections));
            $.ajax({
                url: prefix + "/publishActivity",
                method: "post",
                dataType: "json",
                async:false,
                data: {
                    "operationType": type,
                    "actIds": actIds.toString(),
                    "platform": platform,
                    "sids": selections.toString(),
                    "cover": cover
                },
                beforeSend: function () {
                    $.modal.loading("正在处理中，请稍后...");
                    $.modal.disable();
                },
                success: function (data) {
                    if (data.ok){
                        $.modal.alertMsg(data.msg,alert_type.INFO);
                    }else {
                        $.modal.alertMsg(data.msg,alert_type.FAIL);
                    }
                    if (isActivityList == true){
                        $.table.search("actListFormId","bootstrap-table");//活动总览页面查询
                    }else {
                        console.log("bbb");
                        $.table.search("formId","bootstrap-table");//通用单类型活动查询
                    }
                    $.modal.closeLoading();
                    $.modal.enable();
                }
            });
        });
    });
}

/**
 * 批量删除活动
 */
function deleteActivity() {
    var actIds = $.table.selectColumns("id");
    console.log("actIds:"+actIds);
    if (actIds.length == 0) {
        $.modal.alertWarning("未选择活动");
        return;
    }
    $.modal.confirm("确定删除活动?",function () {
        $.ajax({
            url: prefix + "/deleteActivity",
            method: "post",
            dataType: "json",
            data: {
                "actIds": actIds.toString()
            },
            beforeSend: function () {
                $.modal.loading("正在处理中，请稍后...");
                $.modal.disable();
            },
            success: function (data) {
                $.modal.alertWarning(data.msg);
                if (isActivityList == true){
                    $.table.search("actListFormId","bootstrap-table");//活动总览页面查询
                }else {
                    $.table.search("formId","bootstrap-table");//通用单类型活动查询
                }
                $.modal.closeLoading();
                $.modal.enable();
            }
        });
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
            $("div[name='config'] input").val('');
            $("div[name='cfg2']").remove();
            $("div[name='cfg3']").remove();
            $("div[name='cfg4']").remove();
            $("div[name='cfg5']").remove();
            $("div[name='cfg6']").remove();
            $("div[name='cfg7']").remove();
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

            //保底次数配置
            commonBaoDi(parsedJson,count);
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
            $("div[name='cfg8']").remove();
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

            //免费礼包
            var btn_idInit8=0;
            count_08=0;
            $.each(parsedJson.activeValue, function(i) {
                var activeValue=parsedJson.activeValue[i];

                freeGiftGrp($("#freeGiftBtn").get(0));
                $("input[name='activeValue']").eq(i).val(activeValue);

                var btn_id="showRewardModel8"+btn_idInit8;
                var i_rewardArr=parsedJson.freeGiftReward[i].split(",");//得到一行的数据;
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
                        var btnName = itemValue + "@freeGiftReward"+btn_idInit8+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='freeGiftReward']").eq(i).val(i_rewardArr);
                    btn_idInit8+=1;
                    btn_id="showRewardModel8"+btn_idInit8;
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

            if (parsedJson.i_item_weight_one != undefined){
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
            }

            //奖池数据配置(金蛋奖池数据)
            var btn_idInit2=0;
            count_02=0;

            if (parsedJson.i_item_weight_two != undefined){
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
            }

            //奖池数据配置(银蛋奖池数据)
            var btn_idInit3=0;
            count_03=0;

            if (parsedJson.i_item_weight_three != undefined){
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
            }

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

            //免费礼包
            $("#onLineTime").val("");
            var onLineTime=parsedJson.onLineTime[0];
            $("#onLineTime").val(onLineTime);
            $("#timesLimit").val("");
            var timesLimit=parsedJson.timesLimit[0];
            $("#timesLimit").val(timesLimit);
            var freeGift=parsedJson.freeGift[0].split(";");

            $.each(freeGift,function (i) {
                var btn_id="freeGiftBtn";
                var rewardArr = freeGift[i].split("_");
                var item_id = rewardArr[0];
                console.log("item_id"+item_id);
                var itemName = items.get(item_id);
                var item_num = rewardArr[1];
                var item_gender = rewardArr[3];
                var item_bind = rewardArr[2];
                console.log("item_num"+item_num);
                var itemValue = freeGift[i];//例如:12_99_1_9
                var btnName = itemValue + "@freeGift<>"+btn_id;
                var item_btn_id = "item_btn_id_" + count;
                count+=1;
                $("#"+btn_id).before("" +
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                    " id= '" + item_btn_id + "' name='" + btnName + "'>");
                formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
            });
            $("input[name='freeGift']").eq(0).val(parsedJson.freeGift[0]);

            //保底次数配置
            commonBaoDi(parsedJson,count);
            break;

        case 28:
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
        case 29:
            //删除之前动态生成的配置项
            for (var i=1; i<=9; i++){
                var cfgName="cfg"+i;
                var rewardCfgName="rewardCfg"+i;
                var costCfgName="costCfg"+i;
                $("div[name="+cfgName+"]").remove();
                $("div[name="+rewardCfgName+"]").remove();
                $("div[name="+costCfgName+"]").remove();
            }
            $(".showItem").remove();

            var count=0;
            var btn_idInit;

            var drawItemId=parsedJson.drawItemId;
            var drawItemNeedLingyu=parsedJson.drawItemNeedLingyu;
            $("input[name='drawItemId']").eq(0).val(drawItemId);
            $("input[name='drawItemNeedLingyu']").eq(0).val(drawItemNeedLingyu);
            $.each(parsedJson.allFinishDrawItemNum,function (i) {
                var allFinishDrawItemNum=parsedJson.allFinishDrawItemNum[i];
                var openNextMapDrawNum=parsedJson.openNextMapDrawNum[i];
                var resetNeedlingyu=parsedJson.resetNeedlingyu[i];

                addSaltIpGrp($("#addSaltIpGrpBtn"+(i+1)).get(0),i+1);
                showOrHide($("#addSaltIpGrpBtn"+(i+1)).get(0),i+1);
                var cfgName="cfg"+(i+1);
                var rewardCfgName="rewardCfg"+(i+1);
                var costCfgName="costCfg"+(i+1);
                $("div[name="+cfgName+"]").show();
                $("div[name="+rewardCfgName+"]").show();
                $("div[name="+costCfgName+"]").show();

                $("input[name='allFinishDrawItemNum']").eq(i).val(allFinishDrawItemNum);
                $("input[name='openNextMapDrawNum']").eq(i).val(openNextMapDrawNum);
                $("input[name='resetNeedlingyu']").eq(i).val(resetNeedlingyu);

                var weightName="weight"+(i+1);
                var minDrawNumName="minDrawNum"+(i+1);
                var costNumName="costNum"+(i+1);
                $.each(parsedJson[weightName],function (j) {
                    var weight=parsedJson[weightName][j];
                    var minDrawNum=parsedJson[minDrawNumName][j];
                    var costNum=parsedJson[costNumName][j];
                    $("input[name="+weightName+"]").eq(j).val(weight);
                    $("input[name="+minDrawNumName+"]").eq(j).val(minDrawNum);
                    $("input[name="+costNumName+"]").eq(j).val(costNum);

                    //奖励列表
                    var btn_id="showRewardModel"+(i+1)+(j+1);
                    btn_idInit=(i+1).toString()+(j+1);
                    var i_RewardGroupName="i_RewardGroup"+(i+1);
                    var i_RewardGroupArr=parsedJson[i_RewardGroupName][j].split(",");//得到一行的数据;
                    $.each(i_RewardGroupArr,function (q) {
                        var wordItem=i_RewardGroupArr[q].toString().split(";");//得到具体某个道具 例如:12_99_1_9
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
                                "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                                " id= '" + item_btn_id + "' name='" + btnName + "'>");
                            formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                        });
                        $("input[name="+i_RewardGroupName+"]").eq(j).val(i_RewardGroupArr);
                        btn_idInit+=1;
                        btn_id="showRewardModel"+btn_idInit;
                    });
                });

            });

            break;
        case 30:
            //删除之前动态生成的配置项
            $("div[name='cfg1']").remove();
            $("div[name='cfg2']").remove();
            $("div[name='cfg3']").remove();
            $("div[name='cfg6']").remove();
            $("div[name='cfg7']").remove();
            $(".showItem").remove();

            var count=0;
            rewardGrpCount=0;
            //消耗配置
            var lotteryKeyItemId=parsedJson.lotteryKeyItemId[0];
            var oneCostGemCoin=parsedJson.oneCostGemCoin[0];
            var scoreID=parsedJson.scoreID[0];
            var giveCoinNum=parsedJson.giveCoinNum[0];
            $("#lotteryKeyItemId").val(lotteryKeyItemId);
            $("#oneCostGemCoin").val(oneCostGemCoin);
            $("#scoreID").val(scoreID);
            $("#giveCoinNum").val(giveCoinNum);

            //奖池配置
            var btn_idInit1=0;
            count_01=0;
            var btn_id1;

            var btn_idInit2=0;
            count_02=0;
            var btn_id2;

            $.each(parsedJson.lotteryTabName,function (i) {
                rewardGrpCount=i+1;
                addReward(rewardGrpCount);
                var lotteryTabName = parsedJson.lotteryTabName[i];
                var lotteryNeedKey = parsedJson.lotteryNeedKey[i];
                var lotteryGetScore = parsedJson.lotteryGetScore[i];
                var leftTopLotteryMapName = parsedJson.leftTopLotteryMapName[i];
                var rightTopLotteryMapName = parsedJson.rightTopLotteryMapName[i];
                var leftBottomLotteryMapName = parsedJson.leftBottomLotteryMapName[i];
                var rightBottomLotteryMapName = parsedJson.rightBottomLotteryMapName[i];
                $("input[name='lotteryTabName']").eq(i).val(lotteryTabName);
                $("input[name='lotteryNeedKey']").eq(i).val(lotteryNeedKey);
                $("input[name='lotteryGetScore']").eq(i).val(lotteryGetScore);
                $("input[name='leftTopLotteryMapName']").eq(i).val(leftTopLotteryMapName);
                $("input[name='rightTopLotteryMapName']").eq(i).val(rightTopLotteryMapName);
                $("input[name='leftBottomLotteryMapName']").eq(i).val(leftBottomLotteryMapName);
                $("input[name='rightBottomLotteryMapName']").eq(i).val(rightBottomLotteryMapName);

                var weightName = "weight"+(rewardGrpCount);
                var isShowName = "isShow"+(rewardGrpCount);
                var weightRewardName = "weightReward"+(rewardGrpCount);
                var weightRewarArr=parsedJson[weightRewardName].toString().split(",");//得到一行的数据
                btn_id1="showRewardModel"+btn_idInit1;


                $.each(parsedJson[weightName],function (j) {
                    addWeightGrp($("#addWeightGrpBtn"+(rewardGrpCount)).get(0));
                    var weight=parsedJson[weightName][j];
                    var isShow=parsedJson[isShowName][j];
                    $("input[name="+weightName+"]").eq(j).val(weight);
                    $("select[name="+isShowName+"]").eq(j).val(isShow);
                    // $.each(weightRewarArr,function (q) {
                        var wordItem=weightRewarArr[j].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                        $.each(wordItem,function (k) {
                            var rewardArr = wordItem[k].toString().split("_");//拆分数据
                            var item_id = rewardArr[0];
                            var itemName = items.get(item_id);
                            var item_num = rewardArr[1];
                            var item_gender = rewardArr[3];
                            var item_bind = rewardArr[2];
                            itemValue = wordItem[k];//例如:12_99_1_9
                            var btnName = itemValue + "@weightReward"+btn_idInit1+"<>"+btn_id1;
                            var item_btn_id = "item_btn_id_" + count;
                            count += 1;
                            $("#"+btn_id1).before("" +
                                "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                                " id= '" + item_btn_id + "' name='" + btnName + "'>");
                            formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                        });
                        $("input[name="+weightRewardName+"]").eq(j).val(weightRewarArr[j]);
                        btn_idInit1+=1;
                        btn_id1="showRewardModel"+btn_idInit1;
                    // });
                });


                var treasureChestId = parsedJson.treasureChestId[i];
                $("input[name='treasureChestId']").eq(i).val(treasureChestId);//百宝箱ID

                var boxWeightRewardName = "boxWeightReward"+(rewardGrpCount);
                var boxWeightRewardArr=parsedJson[boxWeightRewardName].toString().split(",");//得到一行的数据
                btn_id2="showRewardModel2"+btn_idInit2;

                var boxWeightName = "boxWeight"+(rewardGrpCount);
                $.each(parsedJson[boxWeightName],function (k) {
                    addBoxWeightGrp($("#addBoxWeightGrpBtn"+(rewardGrpCount)).get(0));
                    var boxWeight=parsedJson[boxWeightName][k];
                    $("input[name="+boxWeightName+"]").eq(k).val(boxWeight);
                    // $.each(boxWeightRewardArr,function (q) {
                        var wordItem=boxWeightRewardArr[k].toString().split(";");//得到具体某个道具 例如:12_99_1_9
                        $.each(wordItem,function (k) {
                            var rewardArr = wordItem[k].toString().split("_");//拆分数据
                            var item_id = rewardArr[0];
                            var itemName = items.get(item_id);
                            var item_num = rewardArr[1];
                            var item_gender = rewardArr[3];
                            var item_bind = rewardArr[2];
                            itemValue = wordItem[k];//例如:12_99_1_9
                            var btnName = itemValue + "@boxWeightReward"+btn_idInit2+"<>"+btn_id2;
                            var item_btn_id = "item_btn_id_" + count;
                            count += 1;
                            $("#"+btn_id2).before("" +
                                "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                                " id= '" + item_btn_id + "' name='" + btnName + "'>");
                            formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                        });
                        $("input[name="+boxWeightRewardName+"]").eq(k).val(boxWeightRewardArr[k]);
                        btn_idInit2+=1;
                        btn_id2="showRewardModel2"+btn_idInit2;
                    // });
                });
            });

            //兑换商城
            var btn_idInit3=0;
            count_03=0;
            $.each(parsedJson.goodsId, function(i) {
                var goodsId=parsedJson.goodsId[i];
                var needScore=parsedJson.needScore[i];
                var limitBuy=parsedJson.limitBuy[i];

                addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                $("input[name='goodsId']").eq(i).val(goodsId);
                $("input[name='needScore']").eq(i).val(needScore);
                $("input[name='limitBuy']").eq(i).val(limitBuy);

                var btn_id="showRewardModel3"+btn_idInit3;
                var i_rewardArr=parsedJson.reward[i].split(",");//得到一行的数据;
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
                        var btnName = itemValue + "@reward"+btn_idInit3+"<>"+btn_id;
                        var item_btn_id = "item_btn_id_" + count;
                        count += 1;
                        $("#"+btn_id).before("" +
                            "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
                            " id= '" + item_btn_id + "' name='" + btnName + "'>");
                        formatGender(item_btn_id,itemName,item_num,item_gender,item_bind);
                    });
                    $("input[name='reward']").eq(i).val(i_rewardArr);
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
    }
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
                    "<input type='button' style='height:35px' class='showItem btn btn-sm btn-danger' onclick='func_edit_item(this)' " +
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
    addHeightSet();
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
    addHeightSet();
    var count = $(obj).parent().find("input[name='i_baoDi_range_count']").val();
    $(obj).parent().find("input[name='i_baoDi_range_count']").val(count*1+1);
}

//这种没有道具相关动态加减的配置可以计数
function delConfig7(obj,count) {
    count-=1;
    count_07=count;

    var count = $(obj).parent().parent().parent().find("input[name='i_baoDi_range_count']").val();
    if(count>0){
        $(obj).parent().parent().parent().find("input[name='i_baoDi_range_count']").val(count*1-1);
    }

    $(obj).parent().parent().remove();
}


function loadFestivalTypeAddTag() {
    festivals.set(-1, "所有");//设置一个所有条件
    festivals.forEach(function(value, key) {
        if (key != -1){
            $("#subType2").append("<option value='" + key +"'>" + value +"(" + key +")</option>");
        }else {
            $("#subType2").append("<option value='" + key +"' selected>" + value +"</option>");
        }
    });

    tags.set(-1, "所有");//设置一个所有条件
    tags.forEach(function(value, key) {
        if (key != -1){
            $("#tag2").append("<option value='" + key +"'>" + value +"(" + key +")</option>");
        }else {
            $("#tag2").append("<option value='" + key +"' selected>" + value +"</option>");
        }
    });
}