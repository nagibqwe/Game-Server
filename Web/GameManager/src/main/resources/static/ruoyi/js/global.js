var pre = ctx + "gmtool";

//格式化值(值太多时能显示和隐藏)
function formatterValue(value, row, index){
    return '<input type=\'button\' name=\'dataBtn\' value=\'∨\' onclick=\'showOrHideValue(this)\'><label style=\'display:none\'>'+value+'</label>';
}

//格式化时间
function formatterDate(value){
    if(value.length == 13){
        return TimeObjectUtil.longMsTimeConvertToDateTime(value*1);
    }else {
        return TimeObjectUtil.longMsTimeConvertToDateTime(value*1000);
    }
}

function formatterMailAction(value){
    var mailActionName = "";
    switch (value) {
        case 1:
            mailActionName = "邮件收取";
            break;
        case 2:
            mailActionName ="邮件读取";
            break;
        case 3:
            mailActionName = "邮件附件领取";
            break;
        case 4:
            mailActionName = "玩家一键删除邮件";
            break;
        case 5:
            mailActionName = "邮件到期系统删除";
            break;
        default:
            mailActionName = "未知类型";
    }
    return mailActionName+"["+value+"]";
}
function formatterRoleLevel(value){
    if(value > 360){
        return "飞升"+(value*1 - 360);
    }else {
        return value;
    }
}

function formatterChatChannel(value){
    var name = "";
    switch (value) {
        case 0:
            name = "世界";
            break;
        case 1:
            name = "公会";
            break;
        case 2:
            name ="队伍";
            break;
        case 3:
            name = "私人";
            break;
        case 8:
            name = "地图";
            break;
        case 13:
            name = "跨服";
            break;
        default:
            name = "未知类型";
    }
    return name+"["+value+"]";
}

function getChannel(channel){
    switch (channel) {
        case 0:
            return "世界";
        case 1:
            return "公会";
        case 2:
            return "队伍";
        case 3:
            return "私人";
        default:
            return "未知类型"+channel;
    }
};

function showOrHideValue(obj){
    if($(obj).parent().find("label")[0].style.display=='none'){
        $(obj).parent().find("label")[0].style.display='block';
        $(obj).val("∧");
    }else{
        $(obj).parent().find("label").get(0).style.display='none';
        $(obj).val("∨");
    }
}
//点击后选中这行数据(适用于某行中有特殊点击操作)
function selectRow(index,obj) {
    var row = $(obj).parent().parent().parent().find("tr");
    for (var i=0;i<row.length;i++){
        if (Number($(row[i]).attr("data-index")) == index){
            var checkbox = $(obj).parent().parent().find("input[type='checkbox']");
            if (!$(checkbox).prop("checked")){
                $(checkbox).prop("checked",true);
                $(row[i]).addClass("selected");
            }
        }else {
            $(row[i]).find("input[type='checkbox']").prop("checked",false);
            $(row[i]).removeClass("selected");
        }
    }
}
//表格中显示序号数字
function showNumber(tableId,index) {
    var pageSize = $("#"+tableId).bootstrapTable('getOptions').pageSize;     //通过table的#id 得到每页多少条
    var pageNumber = $("#"+tableId).bootstrapTable('getOptions').pageNumber; //通过table的#id 得到当前第几页
    return pageSize * (pageNumber - 1) + index + 1;    // 返回每条的序号： 每页条数 *（当前页 - 1 ）+ 序号
}
//格式化时间控件
function formatDate() {
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

/**
 * 检查时间段
 * @param startDate
 * @param endDate
 * @returns {boolean}
 */
function checkDate(startDate, endDate) {
    if (startDate == "" || startDate == null) {
        $.modal.alertError("starDate is empty");
        return false;
    }
    if (endDate == "" || endDate == null) {
        $.modal.alertError("endDate is empty");
        return false;
    }
    var start = new Date(startDate.replace("-", "/").replace("-", "/"));
    var end = new Date(endDate.replace("-", "/").replace("-", "/"));
    if (end < start) {
        $.modal.alertError("endDate is less than starDate");
        return false;
    }
    return true;
}

/**
 * 加载所有物品
 */
function loadAllItem() {
    $.ajax({
        url: pre + "/backenddataload/getAllItem",
        method: "post",
        dataType: "json",
        async : false,
        success: function (data) {
            if (!data.ok) {
                $.modal.alertError("item load failed");
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
        url: pre + "/backenddataload/getAllItem",
        method: "post",
        dataType: "json",
        async : false,
        success: function (data) {
            if (!data.ok) {
                $.modal.alertError("item load failed");
                return;
            }
            $.each(data.data, function (i, n) {
                items.set(n.itemId + "", n.itemName);
            });
        }
    });
}

//格式化附件Items显示
function formatItems(itemData, row, index) {
    console.log("itemData:"+itemData);
    if(itemData==""||itemData==undefined||itemData==null){
        return "";
    }
    var itemArr=itemData.split("}");
    var itemShow="";
    $.each(itemArr,function (i) {
        var itemArr_=itemArr[i].toString().split(",");
        var itemId=itemArr_[0];
        var itemName=items.get(itemId);
        var itemNumber=itemArr_[1];
        var isBind=itemArr_[2]==0?"非绑":"绑定";
        itemShow+=itemName+"*"+itemNumber+"("+isBind+")"+"}";
    });
    return itemShow;
}
//格式化邮件状态
function formatterState(value, row, index) {
    if (value == 1){
        return "待批准";
    } else if (value == 2){
        return "发送未成功";
    } else if (value == 3){
        return "发送成功";
    }else if (value == 4){
        return "不予发送";
    }else if (value == 5){
        return "已删除";
    }else if (value == 6){//全服邮件
        return row.sendErrorMess
    }
}
//格式化职业显示
function formatCareer(value,row,index) {
    if (value==0){
        return "玄剑";
    }else if (value==1){
        return "天英";
    }else if (value==2){
        return "地藏";
    }else if (value==3){
        return "罗刹";
    } else if (value==9){
        return "通用";
    }
}
function toggleTable(obj) {
    $(obj).toggle();
}

//文本域增加统一样式
function addClass() {
    $("textarea").eq(0).addClass("selectClass");
    $("textarea").eq(0).css("marginLeft","290px");
}

/**
 * 通用服务器多选择点击事件
 */
function selectServer(){
    var selectServerIdHideValue = $("#selectServerIdHide").val();
    var selectGroupNameHideValue = $("#selectGroupNameHide").val();
    var requestUrl = $("#selectRequestUrl").val();
    var ignoreMerge = $("#selectIgnoreMerge").val();
    var modelName = "serverList";
    var url = 'selectServerIdList='+selectServerIdHideValue+'&selectGroupName='+selectGroupNameHideValue+'&requestUrl='+requestUrl+'&ignoreMerge='+ignoreMerge+'&modelName='+modelName;
    openServerList(url,function (index, layero) {
        var iframeWin = getIframeWin(layero);
        var selections =  selectionsSid(iframeWin);
        var selectionsDetail =  iframeWin.contentWindow.getSelectionsDetail();
        var selectGroupName =  getGroupName(iframeWin);
        console.log(JSON.stringify(selections));
        console.log("selectGroupName:"+selectGroupName);
        $("#selectServerId").val("服务器选择:["+selectionsDetail.join()+"]");//访问父页面元素值
        $("#selectServerIdHide").val(selections.toString());
        $("#selectGroupNameHide").val(selectGroupName);
        layer.close(index);
    });
}
//获取当前弹出的layer对象
function getIframeWin(layero) {
    return layero.find('iframe')[0];
}
//获取选中的数据的serverId
function selectionsSid(iframeWin) {
    return iframeWin.contentWindow.getSelections();
}
//获取选中的分组名
function getGroupName(iframeWin) {
    return iframeWin.contentWindow.getSelectGroupName();
}

/**
 * 弹出服务器列表选择框
 * @param url
 * @param yesFunction
 */
function openServerList(url,yesFunction){
    layer.open({
        type: 2,
        shade: [0.05, '#393D49'],
        area: ['45%' ,'100%'],
        //offset:[top+'px', left+'px'] ,
        offset:"rb",
        fix: false,
        btnAlign: 'l',
        //不固定
        maxmin: true,
        title: "服务器多选",
        content: ctx+'gmtool/server/selectserver?'+url,
        btn: ['确定', '关闭',"全部选中","全部取消","连选"],
        // 弹层外区域关闭
        shadeClose: true,
        success: function(layero,index){ //弹出服务器列表页面初始化
        },
        yes: function(index, layero) { //确定按钮点击函数

            yesFunction(index, layero);

        },
        cancel: function(index) {
            return true;
        },
        btn3: function(index, layero) {
            var iframeWin = getIframeWin(layero);
            iframeWin.contentWindow.selectAll();
            return false;
        },
        btn4: function(index, layero) {
            var iframeWin = getIframeWin(layero);
            iframeWin.contentWindow.selectCancel();
            return false;
        },
        btn5: function(index, layero) {

            var iframeWin = getIframeWin(layero);
            iframeWin.contentWindow.selectRange();
            return false;
        }
    });
}

//单选服务器组改变时调用
function serverGroupChange(){
    $("#select_server").change(function(){
        $.table.search();
    });

    $("#select_group").change(function(){
        queryServerByGroup($("#select_group").val());
        $.table.search();
    });
}

function isEmpty(strIn) {
    if (strIn === undefined) {
        return true;
    } else if (strIn == null) {
        return true;
    } else if (strIn == '') {
        return true;
    } else {
        return false;
    }
}