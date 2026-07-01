
$(function() {
    $("#help").bind("click",function(){
        layer.tips(helptip, this, {
            tips: [1,'#ffffff'],
            time: -1,
            closeBtn:1,
            area: ['auto', 'auto'],
            success: function () {
                $(".layui-layer-tips .layui-layer-setwin").css({'top':'14px','right':'16px'}); //按钮位置 
                $(".layui-layer-content").css('padding-right', '10px');
            }
        });
    });
    $("#selectServerId").bind("click",function(){
        var selectServerIdHideValue = $("#selectServerIdHide").val();
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
            content: ctx+'gmtool/dblog/selectdblog?selectServerIdList='+selectServerIdHideValue,
            btn: ['确定', '关闭',"全部选中","全部取消","连选"],
            // 弹层外区域关闭
            shadeClose: true,
            success: function(layero,index){ //弹出服务器列表页面初始化
            },
            yes: function(index, layero) { //确定按钮点击函数
                var iframeWin = layero.find('iframe')[0];
                var selections =  iframeWin.contentWindow.getSelections();
                // alert(JSON.stringify(info));
                $("#selectServerId").val("服务器选择:["+selections.length+"]");//访问父页面元素值
                $("#selectServerIdHide").val(selections.toString());
                layer.close(index)
            },
            cancel: function(index) {
                return true;
            },
            btn3: function(index, layero) {
                var iframeWin = layero.find('iframe')[0];
                iframeWin.contentWindow.selectAll();
                return false;
            },
            btn4: function(index, layero) {
                var iframeWin = layero.find('iframe')[0];
                iframeWin.contentWindow.selectCancel();
                return false;
            },
            btn5: function(index, layero) {

                var iframeWin = layero.find('iframe')[0];
                iframeWin.contentWindow.selectRange();
                return false;
            }
        });
    })

});