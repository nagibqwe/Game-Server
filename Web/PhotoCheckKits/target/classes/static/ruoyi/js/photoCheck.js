//放到回收站
function putBack(id) {
    $.modal.confirm("确定要放到回收站吗？", function() {
        $.post(prefix + "/putBack", { "ids": id}, function(result) {
            if (result){
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgSuccess(result.msg);
                    $.table.refresh();
                    layer.closeAll('iframe');//关闭所有的iframe层
                }else {
                    $.modal.msgError("操作失败");
                }
            }
        });

    })
}

// 批量将数据放入回收站中(没有真正删除)
function putBackMore(){
    var isCustom = $(".fixed-table-custom-view").css('display');//判断当前浏览模式是表格还是自定义方式
    var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
    if (isCustom == "block"){
        var ids = getAllSelect();
        sendPutBack(ids);
    }else {
        sendPutBack(rows);
    }
}

function sendPutBack(rows) {
    $.modal.confirm("确认要将选中的" + rows.length + "条数据放到回收站吗?", function() {
        $.post(prefix + "/putBack",{ "ids": rows.join()},function(result) {
            if (result){
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgSuccess(result.msg);
                    $.table.refresh();
                }else {
                    $.modal.msgError("操作失败");
                }
            }
        });
    })
}

//单个退回未审核状态
function backNotCheck(id) {
    $.modal.confirm("确定要退回未审核状态吗？", function() {
        $.post(prefix + "/backNotCheckMore",{"ids": id},function(result) {
            if (result){
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgSuccess(result.msg);
                    $.table.refresh();
                    layer.closeAll('iframe');//关闭所有的iframe层
                }else {
                    $.modal.msgError("操作失败");
                }
            }
        });
    })
}

//批量退回未审核状态
function backNotCheckMore() {
    var isCustom = $(".fixed-table-custom-view").css('display');//判断当前浏览模式是表格还是自定义方式
    var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
    if (isCustom == "block"){
        var ids = getAllSelect();
        sendBackNotCheckMore(ids);
    }else {
        sendBackNotCheckMore(rows);
    }
}

function sendBackNotCheckMore(rows) {
    $.modal.confirm("确定退回选中的"+ rows.length +"数据为未审核状态吗？", function() {
        $.post(prefix + "/backNotCheckMore",{ "ids": rows.join()},function(result) {
            if (result){
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgSuccess(result.msg);
                    $.table.refresh();
                }else {
                    $.modal.msgError("操作失败");
                }
            }
        });
    })
}

//格式化自定义表格
function customViewFormatter (data) {
    $(".multiple").addClass("disabled");
    count = 0;
    var template = $('#profileTemplate').html();
    var view = '';
    $.each(data, function (i, row) {
        view += template.replace('%id%', row.id)
            .replace('%checkboxId%', row.id)
            .replace('%id2%', row.id)
            .replace('%desc6%', row.desc6)
            .replace('%checkStatus%', formatterCheckStatus(row.checkStatus))
            .replace('%uploadTime%', row.uploadTime)
    })

    return `<div class="row mx-0">${view}</div>`
}

//格式化审核状态
function formatterCheckStatus(checkStatus,row,index) {
    if (checkStatus == 0){
        return "未审核"
    } else if (checkStatus == 1){
        return "审核中"
    } else if (checkStatus == 2){
        return "审核通过"
    }else if (checkStatus == 3){
        return "审核未通过"
    }
}
//获取每条数据的ID值
function getId(obj){
    return $(obj).parent().find("input[name='id']")[0].value;
}
//鼠标移进样式
function changeBorder(obj) {
    $(obj).removeClass("contact-box");
    $(obj).addClass("contact-box2");
    $(obj).css("cursor","Pointer");
}
//鼠标移出样式
function changeBorder2(obj) {
    $(obj).removeClass("contact-box2");
    $(obj).addClass("contact-box");
}
//点击选中当前的图片框
var count = 0;
function selectBox(obj) {
    var checkbox = $(obj).find("input[name='checkboxId']")[0];
    var flag = $(checkbox).is(':checked');
    if (flag == true){
        flag = false;
        count-=1;
    } else {
        flag = true;
        count+=1;
    }
    $(checkbox).prop("checked",flag);
    console.log("count:"+count);
    if (count > 0){
        $(".multiple").removeClass("disabled");
    }else {
        $(".multiple").addClass("disabled");
    }
}
//点击批量操作按钮时获取选中的内容
function getAllSelect() {
    var ids =[];
    $('input[name="checkboxId"]:checked').each(function(){
        ids.push($(this).val());
    });
    console.log("ids:"+ids);
    return ids;
}