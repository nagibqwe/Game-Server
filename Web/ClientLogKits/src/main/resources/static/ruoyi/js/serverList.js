var serverInfo = new Map();
var channelInfo = new Map();
var serverIdsArr = [];//服务器列表编辑服务器选择下拉框可用的服务器数据
var serverIdsAll = [];//全部服务器的id数组
var saveServerIds = [];//点击提交到后台需要保存的服务器列表中的 server_ids 服务器ID列表
/**
 * 游戏服列表额外信息加载
 * @param serverListId
 */
var serverListExtraData;
function loadServerListExtra(serverListId) {
    $.ajax({
        url: ctx + "serverListConfig/serverExtra/selectServerExtraList",
        method: "post",
        dataType: "json",
        data: {
            "serverListId": serverListId
        },
        async:false,
        success: function (data) {
            serverListExtraData = data;
            // console.log(serverListExtraData);
        }
    });
}

/**
 * 动态生成下拉框的选中项
 * @param value
 * @param optionValueList
 * @param optionTextList
 */
function selectConfig(start,value,optionValueList,optionTextList) {
    for(var i=0;i<optionValueList.length;i++){
       if (value == i){
           start += '<option value='+i+' selected>'+optionTextList[i]+'</option>'
       }else {
           start +='<option value='+i+'>'+optionTextList[i]+'</option>'
       }
    }
    return start;
}

//加载关联渠道信息  获取可用渠道(包括已有的渠道)
function loadChannels(serverListId) {
    $.ajax({
        url: ctx + "serverListConfig/channel/selectChannels",
        method: "post",
        dataType: "json",
        data: {
            "serverListId": serverListId
        },
        async:false,
        success: function (data) {
            if (data.length > 0){
                for (var i=0;i<data.length;i++){
                    $("#channels").append("<option value='" + data[i].channelId + "'>" + data[i].channelName + "("+data[i].channelId+")</option>")
                }
            }
        }
    });
}

//加载服务器列表中已占有的渠道列表显示
function loadShowChannels(serverListId) {
    $.ajax({
        url: ctx + "serverListConfig/channel/selectShowChannels",
        method: "post",
        dataType: "json",
        data: {
            "serverListId": serverListId
        },
        async:false,
        success: function (data) {
            var html="";
            if (data.length > 0){
                var channels=[];
                for (var i=0;i<data.length;i++){
                    channels.push(data[i].channelId);
                    html+=data[i].channelName+"("+data[i].channelId+")"+"&nbsp;&nbsp;";
                }
                $('#channels').selectpicker('val', channels);//设置关联渠道下拉框选中对应的已有渠道
            }
            $("#showChannels").html(html);
        }
    });
}
//加载可选服务器
function loadServers(serverListId) {
    $.ajax({
        url: ctx + "serverListConfig/server/selectServers",
        method: "post",
        dataType: "json",
        data: {
            "serverListId": serverListId
        },
        async:false,
        success: function (data) {
            if (data.length > 0){
                $( "#servers" ).selectpicker({
                    title : '请选择' //默认显示内容
                });
                for (var i=0;i<data.length;i++){
                    serverIdsArr.push(data[i].serverId);
                    $("#servers").append("<option value='" + data[i].serverId + "'>" + data[i].serverName + "("+data[i].serverId+")</option>")
                }
                console.log("serverIdsArr:"+serverIdsArr);
            }
        }
    });
}
//加载所有服务器(serverId和serverName)
function loadServersInfo() {
    $.ajax({
        url: ctx + "serverListConfig/server/selectServersInfo",
        method: "post",
        dataType: "json",
        async:false,
        success: function (data) {
            for (var i=0;i<data.length;i++){
                serverInfo.set(data[i].serverId + "", data[i].serverName);
                serverIdsAll.push(data[i].serverId);
            }
        }
    });
}
//加载所有渠道(channelId和channelName)
function loadChannelsInfo() {
    $.ajax({
        url: ctx + "serverListConfig/channel/selectChannelsInfo",
        method: "post",
        dataType: "json",
        async:false,
        success: function (data) {
            for (var i=0;i<data.length;i++){
                channelInfo.set(data[i].channelId + "", data[i].channelName);
            }
        }
    });
}
//渠道下拉框改变选择时,文本显示追加选中的渠道
$("#channels").on('changed.bs.select',function () {
    var channelsIds = $('#channels').val();
    $("input[name='channelIds']").val($("#channels").val());
    console.log("channelsIds:"+channelsIds);
    var html="";
    if (channelsIds == null){
        $('#showChannels').html(html);
    } else {
        for (var i=0;i<channelsIds.length;i++){
            html+=channelInfo.get(channelsIds[i])+"("+channelsIds[i]+")|";
        }
        $('#showChannels').html(html);
    }
});

//选择服务器后触发增加一行额外信息
function addServerExtra() {
    $.modal.confirm("确定增加到列表吗？", function() {
        var serversId = $('#servers').val();
        var serverNameSelect = serverInfo.get(serversId);

        for(var i = 0; i < serverIdsArr.length; i++){
            if(serverIdsArr[i] == serversId){
                serverIdsArr.splice(i,1);
                console.log(serverIdsArr);
            }
        }
        $("#servers").empty();
        for (var i=0;i<serverIdsArr.length;i++){
            var serverName;
            for (var x of serverInfo){
                if (serverIdsArr[i] == parseInt(x[0])) {
                    serverName = x[1];
                }
            }
            $("#servers").append("<option value='" + serverIdsArr[i] + "'>" + serverName + "("+serverIdsArr[i]+")</option>")
        }
        $("#servers").selectpicker('refresh');
        var data = $("#bootstrap-table").bootstrapTable('getData');
        var count = data.length;
        var finallIndex=count-1;
        openAddLayer(serversId,serverNameSelect,finallIndex);
        // console.log("serversId:"+serversId+"  serverName:"+serverNameSelect);

        // var row = {
        //     serverId: serversId,
        //     serverName: serverName,
        //     sortId: "",
        //     serverStatus: "",
        //     groupType: "",
        //     serverLabel: "",
        //     sceId: "",
        //     appVersion: "",
        // };
        // sub.addColumn(row);

    });
};
//删除额外信息
function deleteConfig() {
    $.modal.confirm("确定删除吗？", function() {
        var serverIds = $.table.selectColumns("serverId");
        console.log("serverIds:"+serverIds);
        for (var i = 0; i < serverIds.length; i++) {
            serverIdsArr.push(serverIds[i]);
        }
        serverIdsArr.sort();
        reloadServers(serverIdsArr);
        sub.delColumn();
    })
}
//点击删除后重新加载服务器下拉框需要的数据
function reloadServers(serverIdsArr) {
    $("#servers").empty();
    console.log(serverIdsArr);
    for (var i = 0; i < serverIdsArr.length; i++) {
        $("#servers").append("<option value='" + serverIdsArr[i] + "'>" + serverInfo.get(serverIdsArr[i].toString()) + "(" + serverIdsArr[i] + ")</option>")
    }
    $("#servers").selectpicker('refresh');
}
//获取提交到后台需要保存的服务器列表中的 server_ids 服务器ID列表
function getSaveServerIds(serverIdsArr) {
    for(var i = 0; i < serverIdsArr.length; i++){
        for (var j = 0;j < serverIdsAll.length;j++){
            if(serverIdsArr[i] == serverIdsAll[j]){
                serverIdsAll.splice(j,1);
            }
        }
    }
    saveServerIds = serverIdsAll;
    console.log("saveServerIds:"+saveServerIds);
}
//异步自动检测添加的渠道ID是否已经存在
function autoCheckChannelId() {
    var channelId = $("input[name='channelId']").val();
    $.ajax({
        url : ctx + "serverListConfig/channel/checkChannelId",
        type :'POST',
        dataType : 'json',
        data:{
            "channelId":channelId
        },
        async : true,
        success : function(data){
            if (data.count > 0){
                $("#autoCheckChannelId").html("<font color='red'>" + data.msg + "</font>");
            }else {
                $("#autoCheckChannelId").html("<font color='#90ee90'>" + data.msg + "</font>");
            }
        }
    });
}
//异步自动检测添加的服务器ID是否已经存在
function checkServerId() {
    var serverId = $("input[name='serverId']").val();
    $.ajax({
        url : ctx + "serverListConfig/server/checkServerId",
        type :'POST',
        dataType : 'json',
        data:{
            "serverId":serverId
        },
        async : true,
        success : function(data){
            if (data.count > 0){
                $("#checkServerId").html("<font color='red'>" + data.msg + "</font>");
            }else {
                $("#checkServerId").html("<font color='#90ee90'>" + data.msg + "</font>");
            }
        }
    });
}

/**
 * 判断 str 字符串中是否含有字符串 subStr
 * @param {} str 原字符串
 * @param {} subStr 要查找的字符串
 * @param {} isIgnoreCase 是否忽略大小写
 * @return {Boolean}
 */
function contains(str, subStr, isIgnoreCase) {
    if (isIgnoreCase) {
        // 忽略大小写
        str = str.toLowerCase();
        subStr = subStr.toLowerCase();
    }
    var startChar = subStr.substring(0,1);
    var strLen = subStr.length;

    for (var j=0; j<str.length-strLen+1; j++) {
        if (str.charAt(j) == startChar) {
            /* 如果匹配起始字符,开始查找 */
            if (str.substring(j, j+strLen) == subStr) {
                /*如果从j开始的字符与 str 匹配 */
                return true;
            }
        }
    }
    return false;
}

function isContains(obj) {
    var sceId = $(obj).val();
    if ("" != sceId){
        if (contains(sceId, "，", true)){
            alert("不能含有中文逗号！");
        }
    }
}
//点击编辑按钮
function editServerExtra(index) {
    var allOldData = $("#" + table.options.id).bootstrapTable('getData');
    // console.log("old:"+JSON.stringify(allOldData));
    openEditLayer(index,allOldData[index]);
}
//点击删除按钮
function deleteServerExtra(serverId) {
    if (serverId == 0){
        var serverIds=[];
        var selectRows = $("#bootstrap-table").bootstrapTable('getSelections');
        console.log(selectRows);
        if (selectRows.length > 0){
            for (var i=0;i<selectRows.length;i++){
                serverIds.push(selectRows[i].serverId);
            }
            $.modal.confirm("确定删除吗？", function() {
                for (var i = 0; i < serverIds.length; i++) {
                    serverIdsArr.push(serverIds[i]);
                }
                serverIdsArr.sort();
                reloadServers(serverIdsArr);
                $("#bootstrap-table").bootstrapTable('remove', {field: 'serverId', values: serverIds});
            });
        }else {
            $.modal.alertWarning("请至少选择一条记录");
            return;
        }
    }else {
        $.modal.confirm("确定删除吗？", function() {
            serverIdsArr.push(serverId);
            serverIdsArr.sort();
            reloadServers(serverIdsArr);
            $("#bootstrap-table").bootstrapTable('remove', {field: 'serverId', values: serverId});
        });
    }
}
//打开修改窗口
function openEditLayer(index,data) {

    var indexInt=parseInt(index);//将string类型的index转为int类型
    layer.open({
        type: 2,
        area: [800 + 'px', 500 + 'px'],
        fix: false,
        //不固定
        maxmin: true,
        shade: 0.3,
        title: "修改",
        content: ctx+"serverListConfig/serverList/toServerListEditPage",
        btn: ['确定', '关闭'],
        // 弹层外区域关闭
        shadeClose: true,
        yes: function (index,layero) {
            var iframeWin = layero.find('iframe')[0];//找到哪个弹框
            var newData = iframeWin.contentWindow.saveClientData();
            console.log("data:"+newData);
            $("#bootstrap-table").bootstrapTable('updateRow', {index: indexInt, row: newData});
            layer.close(index);
        },
        cancel: function(index) {
            return true;
        },
        success: function(layero, index){
            var iframeWin = layero.find('iframe')[0];//找到哪个弹框
            iframeWin.contentWindow.initEditValue(data);
        }
    });
}
//打开添加窗口
function openAddLayer(serversId,serverName,finallIndex) {
    layer.open({
        type: 2,
        area: [800 + 'px', 500 + 'px'],
        fix: false,
        //不固定
        maxmin: true,
        shade: 0.3,
        title: "修改",
        content: ctx+"serverListConfig/serverList/toServerListEditPage",
        btn: ['确定', '关闭'],
        // 弹层外区域关闭
        shadeClose: true,
        yes: function (index,layero) {
            var iframeWin = layero.find('iframe')[0];//找到哪个弹框
            var newData = iframeWin.contentWindow.saveClientData();
            console.log("data:"+JSON.stringify(newData));
            $("#bootstrap-table").bootstrapTable('insertRow', {index: finallIndex+1, row: newData});
            layer.close(index);
        },
        cancel: function(index) {
            return true;
        },
        success: function(layero, index){
            var iframeWin = layero.find('iframe')[0];//找到哪个弹框
            iframeWin.contentWindow.initAddValue(serversId,serverName);
        }
    });
}