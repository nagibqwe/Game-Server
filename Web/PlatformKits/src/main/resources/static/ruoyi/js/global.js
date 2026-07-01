var channelInfo = new Map();

//加载所有渠道(channelId和channelName)生成下拉框
function loadChannelsInfo() {
    $.ajax({
        url: ctx + "serverListConfig/channel/selectChannelsInfo",
        method: "post",
        dataType: "json",
        async:false,
        success: function (data) {
            if (data.length > 0){
                $( "#channel" ).selectpicker({
                    title : '请选择渠道' //默认显示内容
                });
                for (var i=0;i<data.length;i++){
                    channelInfo.set(data[i].channelId + "", data[i].channelName);
                    $( "#channel" ).append("<option value='" + data[i].channelId + "'>" + data[i].channelName + "("+data[i].channelId+")</option>")
                }
            }
        }
    });
}
//渠道多选下拉框改变时存值
$("#channel").on('changed.bs.select',function () {
    var channelsIds = $('#channel').val();
    $("input[name='channel']").val($("#channel").val()+",");
    console.log("channel:"+$("input[name='channel']").val());
});
//渠道多选下拉框加载列表
function loadHaveChannelsInfo(hasChannels) {
    $('#channel').selectpicker('val', hasChannels.split(","));
    console.log(hasChannels);
}
//通用隐藏展示数据符号∧∨
function showDetail(obj) {
    if($(obj).parent().find("label")[0].style.display=='none'){
        $(obj).parent().find("label")[0].style.display='block';
        $(obj).val("∧");
    }else{
        $(obj).parent().find("label").get(0).style.display='none';
        $(obj).val("∨");
    }
}
//加载所有渠道存入channelInfo中
function loadChannels() {
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
