<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/4/9
  Time: 14:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${msg['statistic.recharge.rechargeConfig']}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${base}/css/activity.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/common.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
    <link rel="stylesheet" href="${base}/css/alertify.css">
    <link rel="stylesheet" href="${base}/css/alertify.default.css">
    <%--<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap3.css">--%>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <%--<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">--%>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/alertify.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
    <%--    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.13.1/xlsx.full.min.js"></script>--%>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-select.min.css">
    <script src="${base}/css/bootstrap/js/bootstrap-select.min.js"></script>
    <script src="${base}/css/bootstrap/js/bootstrap-select.zh_CN.min.js"></script>
    <%--<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>--%>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap3.js"></script>
    <script type="text/javascript"
            src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript"
            src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${base}/js/activity.js"></script>
    <script type="text/javascript" src="${base}/js/global.js"></script>
    <script type="text/javascript" src="${base}/js/groupServer.js"></script>
    <script type="text/javascript" src="${base}/js/dateFormat.js"></script>
    <link rel="stylesheet" href="${base}/css/fileCss.css">
    <script type="text/javascript" src="${base}/js/excel.js"></script>
    <script type="text/javascript" src="${base}/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${base}/easyui/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" href="${base}/easyui/themes/default/easyui.css" type="text/css">
    <link rel="stylesheet" href="${base}/easyui/themes/icon.css" type="text/css">
    <script type="text/javascript" src="${base}/js/jquery/jquery.ajaxupload.js"></script>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap3.css">
    <script type="text/javascript" src="${base}/js/tableExport.js"></script>
    <script type="text/javascript">
        var base = "${base}";
        var tableName="rechargeItem";//选项卡第一个
        $(function () {
            $("#rechargeItemData").datagrid({
                onLoadSuccess:function(data){
                    $("#MD5").text(data.md5);
                    console.log(data.md5);
                }
            });

            $("#tt").tabs({
                border:false,
                onSelect:function(title){
                    // var title = $('.tabs-selected').text();
                    if ("第三方充值配置表" == title){
                        tableName="otherRechargeItem";
                    }else {
                        tableName="rechargeItem";
                    }
                    console.log(title);
                }
            });
        });

        function formatEdit(value,row,index) {
            return '<a href="#" style="color: blueviolet" onclick="showDialog('+index+','+row.goods_pay_channel+')">修改</a>';
        }
        function showDialog(index,goods_pay_channel) {
            var row = new Object();
            if (goods_pay_channel=="" || goods_pay_channel==undefined){
                $('#rechargeItemData').datagrid('selectRow',index);//先选中普通充值配置这行
                row = $('#rechargeItemData').datagrid('getSelected');//然后获取选中行的数据
            }else {
                $('#rechargeItemData3').datagrid('selectRow',index);//先选中第三方充值配置这行
                row = $('#rechargeItemData3').datagrid('getSelected');//然后获取选中行的数据
            }

            if (row){
                $("#dlg_edit_status").dialog('open');
                $("#goods_id").val(row.goods_id);
                $("#goods_system_cfg_id").val(row.goods_system_cfg_id);
                $("#goods_name").val(row.goods_name);
                $("#goods_pay_channel").val(row.goods_pay_channel);
                $("#goods_pay_type").val(row.goods_pay_type);
                $("#goods_type").val(row.goods_type);
                $("#goods_subtype").val(row.goods_subtype);
                $("#goods_limit").val(row.goods_limit);
                $("#goods_icon").val(row.goods_icon);
                $("#goods_price").val(row.goods_price);
                $("#goods_price_point").val(row.goods_price_point);
                $("#goods_show_price").val(row.goods_show_price);
                $("#goods_reward").val(row.goods_reward);
                $("#goods_multiple").val(row.goods_multiple);
                $("#goods_extra_reward").val(row.goods_extra_reward);
                $("#goods_extra_reward_limit").val(row.goods_extra_reward_limit);
                $("#isTotalRecharge").val(row.isTotalRecharge);
                $("#totalVipPower").val(row.totalVipPower);
            }
        }
        //保存修改
        function save() {
            $.messager.confirm('确认','确认修改',function(r){
                var rechargeTableName = tableName;
                if(r){
                    $("#loadingmodal").modal({backdrop:'static',keyboard:false});
                    $.ajax({
                        url : base + "/rechargeItem/saveRechargeItem",
                        type :'POST',
                        dataType : 'json',
                        data:{
                            "rechargeTableName":rechargeTableName,
                            "goods_id":$("#goods_id").val(),
                            "goods_system_cfg_id":$("#goods_system_cfg_id").val(),
                            "goods_name":$("#goods_name").val(),
                            "goods_pay_channel":$("#goods_pay_channel").val(),
                            "goods_pay_type":$("#goods_pay_type").val(),
                            "goods_type":$("#goods_type").val(),
                            "goods_subtype":$("#goods_subtype").val(),
                            "goods_limit":$("#goods_limit").val(),
                            "goods_icon":$("#goods_icon").val(),
                            "goods_price":$("#goods_price").val(),
                            "goods_price_point":$("#goods_price_point").val(),
                            "goods_show_price":$("#goods_show_price").val(),
                            "goods_reward":$("#goods_reward").val(),
                            "goods_multiple":$("#goods_multiple").val(),
                            "goods_extra_reward":$("#goods_extra_reward").val(),
                            "goods_extra_reward_limit":$("#goods_extra_reward_limit").val(),
                            "isTotalRecharge":$("#isTotalRecharge").val(),
                            "totalVipPower":$("#totalVipPower").val()
                        },
                        async : false,
                        success : function(data){
                            $("#loadingmodal").modal('hide');
                            if(data.ok) {
                                $.messager.alert("信息","修改成功","info",function () {
                                    $("#rechargeItemData").datagrid("reload");
                                    $("#rechargeItemData3").datagrid("reload");
                                    $("#rechargeItemLog").datagrid("reload");
                                    $("#MD5").text(data.md5);
                                    $("#msg").text(data.msg);
                                    $("#dlg_edit_status").dialog("close");
                                });
                            } else{
                                $.messager.alert("错误","修改失败","error");
                            }
                        }
                    });
                }
            })
        }

        /**
         *  导入普通充值配置
         */
        function loadRechargeItem() {
            var data = new FormData();
            data.append('rechargeItemFile', $("#rechargeItems").prop('files')[0]);
            $("#loadingmodal").modal({backdrop:'static',keyboard:false});
            $.ajax({
                url: base + "/rechargeItem/loadRechargeItem",
                method: 'POST',
                data: data,
                processData: false,
                contentType: false,
                success: function (data) {
                    $("#loadingmodal").modal('hide');
                    $("#rechargeItemLoadModel").modal('hide');
                    $("#rechargeItemData").datagrid("reload");
                    $("#rechargeItemData3").datagrid("reload");
                    $("#rechargeItemLog").datagrid("reload");
                    alert(data.msg);
                }
            });
        }

        /**
         *  导入第三方充值配置
         */
        function loadOtherRechargeItem() {
            var data = new FormData();
            data.append('otherRechargeItemFile', $("#otherRechargeItems").prop('files')[0]);
            $("#loadingmodal").modal({backdrop:'static',keyboard:false});
            $.ajax({
                url: base + "/rechargeItem/loadOtherRechargeItem",
                method: 'POST',
                data: data,
                processData: false,
                contentType: false,
                success: function (data) {
                    $("#loadingmodal").modal('hide');
                    $("#otherRechargeItemLoadModel").modal('hide');
                    $("#rechargeItemData").datagrid("reload");
                    $("#rechargeItemData3").datagrid("reload");
                    $("#rechargeItemLog").datagrid("reload");
                    alert(data.msg);
                }
            });
        }

        /**
         * 导出普通充值/第三方充值配置
         */
        function exportExcel(obj) {
            var type=$(obj).parent().find("input").eq(0).val();
            console.log("type:"+type);
            var data = new FormData();
            data.append('type', type);
            if (type == 0){
                $("form[name='rechargeItem']").submit();//向服务器请求需要下载的数据并导出EXCEL
            }else {
                $("form[name='rechargeItem3']").submit();
            }
            // alert($("#rechargeItemDataDiv table").length);//datagrid有六个table组成
            // $("#rechargeItemDataDiv table").eq(2).add($("#rechargeItemDataDiv table").eq(3)).tableExport({type: 'excel', escape: 'false',worksheetName:'普通充值配置'});
            
        }
        //格式化详情记录
        function formatDetail(value,row,index) {
            return '<a href="#" style="color: blueviolet" onclick="showRechargeItemLog('+index+')">点击查看</a>';
        }
        //格式化导出历史记录EXCEL
        function formatExport(value,row,index) {
            return '<form name="exportRechargeItemLogForm" id="exportRechargeItemLogForm" method="post" action="${base}/rechargeItem/exportRechargeItemLog">' +
                    '<input id="hId" name="hId" type="hidden" value="'+row.id+'">'+
                    '<input id="hId" name="tableName" type="hidden" value="'+row.tableName+'">'+
                    '<a href="#" style="color: blueviolet" onclick="exportRechargeItemLog('+index+')">导出EXCEL</a>' +
                    '</form>';
        }
        //点击查看显示历史记录datagrid
        function showRechargeItemLog(index) {
            $("#dlg_log").dialog('open');
            $("#rechargeItemContent").datagrid("load",
                {
                    "index":index
                });
        }
        //历史记录导出EXCEL
        function exportRechargeItemLog(index) {
            $("form[name='exportRechargeItemLogForm']").eq(index).submit();//确定是哪个table表格
        }
        //普通充值及第三方充值数据清除
        function clearData() {
            if (confirm("确认清除？")) {
                $.ajax({
                    url: base + "/rechargeItem/clearRechargeItem",
                    type: 'POST',
                    dataType : "json",
                    success: function (data) {
                        console.log(data);
                        if (data.ok){
                            $("#rechargeItemData").datagrid("reload");
                            $("#rechargeItemData3").datagrid("reload");
                            $("#rechargeItemLog").datagrid("reload");
                            alert("删除普通充值及第三方充值数据成功");
                        }else {
                            alert("删除普通充值及第三方充值数据失败");
                        }
                    }
                });
            }
        }
    </script>
</head>
<body>
<div id="tt" class="easyui-tabs" style="width:100%;height:65%;">
    <div title="普通充值配置表" style="padding:0px;display:none;">
        <div class="container-fluid">
            <div class="row-fluid" id="rechargeItemDataDiv">
                <table id="rechargeItemData" class="easyui-datagrid" style="width:100%;height:auto;" data-options="url:'${base}/rechargeItem/queryRechargeItem',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
                    <thead>
                    <tr>
                        <th field="goods_id" width="60" align="left">${msg['statistic.recharge.id']}</th>
                        <th field="goods_system_cfg_id" width="120" align="left">${msg['statistic.recharge.systemCfgId']}</th>
                        <th field="goods_name" width="250" align="left">${msg['statistic.recharge.goodsDesc']}</th>
                        <th field="goods_pay_channel" width="100" align="center">${msg['statistic.recharge.goods_pay_channel']}</th>
                        <th field="goods_pay_type" width="120" align="left">${msg['statistic.recharge.goods_pay_type']}</th>
                        <th field="goods_type" width="120" align="left">${msg['statistic.recharge.type']}</th>
                        <th field="goods_subtype" width="200" align="left">${msg['statistic.recharge.subType']}</th>
                        <th field="goods_limit" width="200" align="left">${msg['statistic.recharge.goodsLimit']}</th>
                        <th field="goods_icon" width="120" align="left">${msg['statistic.recharge.icon']}</th>
                        <th field="goods_price" width="220" align="left">${msg['statistic.recharge.money']}</th>
                        <th field="goods_price_point" width="240" align="left">${msg['statistic.recharge.moneyPoint']}</th>
                        <th field="goods_show_price" width="120" align="left">${msg['statistic.recharge.showMoneyIcon']}</th>
                        <th field="goods_reward" width="220" align="left">${msg['statistic.recharge.reward']}</th>
                        <th field="goods_multiple" width="180" align="left">${msg['statistic.recharge.multiple']}</th>
                        <th field="goods_extra_reward" width="200" align="left">${msg['statistic.recharge.extraReward']}</th>
                        <th field="goods_extra_reward_limit" width="180" align="left">${msg['statistic.recharge.extraRewardCount']}</th>
                        <th field="isTotalRecharge" width="180" align="left">${msg['statistic.recharge.isTotalRecharge']}</th>
                        <th field="totalVipPower" width="180" align="left">${msg['statistic.recharge.totalVipPower']}</th>
                        <th field="edit" width="80" align="left" formatter="formatEdit">修改</th>
                        <%--<th field="delete" width="50" align="left" >删除</th>--%>
                    </tr>
                    </thead>
                </table>
            </div>
            <div style="margin-left: 80%;display: inline">
                <a href="#rechargeItemLoadModel" role="button" class="btn btn-primary" data-toggle="modal">导入EXCEL</a>
                <form name="rechargeItem" id="rechargeItem" method="post" action="${base}/rechargeItem/exportRechargeExcel" style="display: inline-block">
                    <input name="type" type="hidden" value="0">
                    <input type="button" value="导出EXCEL" class="btn btn-primary" onclick="exportExcel(this)">
                </form>
                <%--<a style="margin-left: 50px" role="button" class="btn btn-primary" onclick="exportExcel()">导出EXCEL</a>--%>
            </div>
        </div>
    </div>
    <div title="第三方充值配置表" style="padding:0px;display:none;">
        <div class="container-fluid">
            <div class="row-fluid" id="rechargeItemData3Div">
                <table id="rechargeItemData3" class="easyui-datagrid" style="width:100%;height:auto;" data-options="url:'${base}/rechargeItem/queryRechargeItem3',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
                    <thead>
                    <tr>
                        <th field="goods_id" width="60" align="left">${msg['statistic.recharge.id']}</th>
                        <th field="goods_system_cfg_id" width="120" align="left">${msg['statistic.recharge.systemCfgId']}</th>
                        <th field="goods_name" width="250" align="left">${msg['statistic.recharge.goodsDesc']}</th>
                        <th field="goods_pay_channel" width="100" align="center">${msg['statistic.recharge.goods_pay_channel']}</th>
                        <th field="goods_pay_type" width="120" align="left">${msg['statistic.recharge.goods_pay_type']}</th>
                        <th field="goods_type" width="120" align="left">${msg['statistic.recharge.type']}</th>
                        <th field="goods_subtype" width="200" align="left">${msg['statistic.recharge.subType']}</th>
                        <th field="goods_limit" width="200" align="left">${msg['statistic.recharge.goodsLimit']}</th>
                        <th field="goods_icon" width="120" align="left">${msg['statistic.recharge.icon']}</th>
                        <th field="goods_price" width="220" align="left">${msg['statistic.recharge.money']}</th>
                        <th field="goods_price_point" width="240" align="left">${msg['statistic.recharge.moneyPoint']}</th>
                        <th field="goods_show_price" width="120" align="left">${msg['statistic.recharge.showMoneyIcon']}</th>
                        <th field="goods_reward" width="220" align="left">${msg['statistic.recharge.reward']}</th>
                        <th field="goods_multiple" width="180" align="left">${msg['statistic.recharge.multiple']}</th>
                        <th field="goods_extra_reward" width="200" align="left">${msg['statistic.recharge.extraReward']}</th>
                        <th field="goods_extra_reward_limit" width="180" align="left">${msg['statistic.recharge.extraRewardCount']}</th>
                        <th field="isTotalRecharge" width="180" align="left">${msg['statistic.recharge.isTotalRecharge']}</th>
                        <th field="totalVipPower" width="180" align="left">${msg['statistic.recharge.totalVipPower']}</th>
                        <th field="edit" width="80" align="left" formatter="formatEdit">修改</th>
                        <%--<th field="delete" width="50" align="left" >删除</th>--%>
                    </tr>
                    </thead>
                </table>
            </div>
            <div style="margin-left: 80%;display: inline">
                <a href="#otherRechargeItemLoadModel" role="button" class="btn btn-primary" data-toggle="modal">导入EXCEL</a>
                <form name="rechargeItem3" id="rechargeItem" method="post" action="${base}/rechargeItem/exportRechargeExcel" style="display: inline-block">
                    <input name="type" type="hidden" value="3">
                    <input type="button" value="导出EXCEL" class="btn btn-primary" onclick="exportExcel(this)">
                </form>
                <%--<a style="margin-left: 50px" role="button" class="btn btn-primary" onclick="exportExcel3()">导出EXCEL</a>--%>
            </div>
        </div>
    </div>
    <input type="button" value="普通充值及第三方充值数据清除" class="btn btn-primary" onclick="clearData()">
</div>

<div class="container-fluid"><span style="color: #00ee00">MD5:</span><span id="MD5"></span></div>
<div class="container-fluid"><span style="color: #00ee00"></span><span id="msg"></span></div>
<!-- 修改窗口 -->
<div id="dlg_edit_status" class="easyui-dialog" data-options="title:'修改',modal:true,iconCls:'icon-edit',width:800,height:700,closed:true,buttons:'#dlg_edit_status_buttons'" style="text-align:left;">
    <p style="margin-top: 20px">
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 200px">${msg['statistic.recharge.id']}: </label><input id="goods_id" type="text" readonly name="goods_id" style="border-radius: 5px;width: auto"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 152px">${msg['statistic.recharge.systemCfgId']}: </label><input id="goods_system_cfg_id" type="text" name="goods_system_cfg_id" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 33px">${msg['statistic.recharge.goodsDesc']}: </label><input id="goods_name" type="text" name="goods_name" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 190px">${msg['statistic.recharge.goods_pay_channel']}: </label><input id="goods_pay_channel" type="text" name="goods_pay_channel" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 190px">${msg['statistic.recharge.goods_pay_type']}: </label><input id="goods_pay_type" type="text" name="goods_pay_type" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 191px">${msg['statistic.recharge.type']}: </label><input id="goods_type" type="text" name="goods_type" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 180px">${msg['statistic.recharge.subType']}: </label><input id="goods_subtype" type="text" name="goods_subtype" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 9px">${msg['statistic.recharge.goodsLimit']}: </label><input id="goods_limit" type="text" name="goods_limit" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 157px">${msg['statistic.recharge.icon']}: </label><input id="goods_icon" type="text" name="goods_icon" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 85px">${msg['statistic.recharge.money']}: </label><textarea id="goods_price" name="goods_price" style="border-radius: 5px;width: 300px" rows="3"></textarea></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 182px">${msg['statistic.recharge.moneyPoint']}: </label><input id="goods_price_point" type="text" name="goods_price_point" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 146px">${msg['statistic.recharge.showMoneyIcon']}: </label><input id="goods_show_price" type="text" name="goods_show_price" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 194px">${msg['statistic.recharge.reward']}: </label><input id="goods_reward" type="text" name="goods_reward" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 170px">${msg['statistic.recharge.multiple']}: </label><input id="goods_multiple" type="text" name="goods_multiple" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 194px">${msg['statistic.recharge.extraReward']}: </label><input id="goods_extra_reward" type="text" name="goods_extra_reward" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 170px">${msg['statistic.recharge.extraRewardCount']}: </label><input id="goods_extra_reward_limit" type="text" name="goods_extra_reward_limit" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 170px">${msg['statistic.recharge.isTotalRecharge']}: </label><input id="isTotalRecharge" type="text" name="isTotalRecharge" style="border-radius: 5px"></span>
    </p>
    <p>
        <span style="display:inline-block;width:600px;text-align: left"><label style="margin-right: 170px">${msg['statistic.recharge.totalVipPower']}: </label><input id="totalVipPower" type="text" name="totalVipPower" style="border-radius: 5px"></span>
    </p>

    <p id="dlg_edit_status_error" style="display:none;">
        <span class="icon-no">&nbsp;&nbsp;</span><span style="color:red;"></span>
    </p>
</div>
<div id="dlg_edit_status_buttons">
    <a id="editStatusy2" onclick="save()" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">保存</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlg_edit_status').dialog('close');">关闭</a>
</div>

<div id="rechargeItemLoadModel" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3>普通充值配置表</h3>
            </div>
            <div class="modal-body">
                <input id="rechargeItems" type="file" name="rechargeItemFile"/>
                <label>加载rechargeItem.xlsx表</label>
            </div>
            <div class="modal-footer">
                <input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
                <input type="button" value="上传" onclick="loadRechargeItem()" class="btn btn-primary"/>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div id="otherRechargeItemLoadModel" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3>第三方充值配置表</h3>
            </div>
            <div class="modal-body">
                <input id="otherRechargeItems" type="file" name="otherRechargeItemFile"/>
                <label>加载rechargeItem.xlsx表</label>
            </div>
            <div class="modal-footer">
                <input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
                <input type="button" value="上传" onclick="loadOtherRechargeItem()" class="btn btn-primary"/>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<jsp:include page="../commonmodal2.jsp"/>

<div class="container-fluid">
    <div class="row-fluid" id="rechargeItemLogDiv">
        <table id="rechargeItemLog" class="easyui-datagrid" style="width:100%;height:auto;" data-options="url:'${base}/rechargeItem/queryRechargeItemLog',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
            <thead>
            <tr>
                <th field="id" hidden></th>
                <th field="userName" width="60" align="left">修改人</th>
                <th field="time" width="120" align="left">修改时间</th>
                <th field="tableName" width="120" align="left">操作表名</th>
                <th field="content" width="100" align="left" formatter="formatDetail">记录详情</th>
                <th field="edit" width="80" align="left" formatter="formatExport">导出EXCEL</th>
                <%--<th field="delete" width="50" align="left" >删除</th>--%>
            </tr>
            </thead>
        </table>
    </div>
</div>
<div id="dlg_log" class="easyui-dialog" data-options="title:'历史记录',modal:true,iconCls:'icon-edit',width:1500,height:800,closed:true" style="text-align:left;">
    <div class="container-fluid">
        <div class="row-fluid" id="rechargeItemContentDiv">
            <table id="rechargeItemContent" class="easyui-datagrid" style="width:100%;height:auto;" data-options="url:'${base}/rechargeItem/queryRechargeItemContent',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
                <thead>
                <tr>
                    <th field="goods_id" width="60" align="left">${msg['statistic.recharge.id']}</th>
                    <th field="goods_system_cfg_id" width="120" align="left">${msg['statistic.recharge.systemCfgId']}</th>
                    <th field="goods_name" width="250" align="left">${msg['statistic.recharge.goodsDesc']}</th>
                    <th field="goods_pay_channel" width="100" align="center">${msg['statistic.recharge.goods_pay_channel']}</th>
                    <th field="goods_pay_type" width="120" align="left">${msg['statistic.recharge.goods_pay_type']}</th>
                    <th field="goods_type" width="120" align="left">${msg['statistic.recharge.type']}</th>
                    <th field="goods_subtype" width="200" align="left">${msg['statistic.recharge.subType']}</th>
                    <th field="goods_limit" width="200" align="left">${msg['statistic.recharge.goodsLimit']}</th>
                    <th field="goods_icon" width="120" align="left">${msg['statistic.recharge.icon']}</th>
                    <th field="goods_price" width="220" align="left">${msg['statistic.recharge.money']}</th>
                    <th field="goods_price_point" width="240" align="left">${msg['statistic.recharge.moneyPoint']}</th>
                    <th field="goods_show_price" width="120" align="left">${msg['statistic.recharge.showMoneyIcon']}</th>
                    <th field="goods_reward" width="220" align="left">${msg['statistic.recharge.reward']}</th>
                    <th field="goods_multiple" width="180" align="left">${msg['statistic.recharge.multiple']}</th>
                    <th field="goods_extra_reward" width="200" align="left">${msg['statistic.recharge.extraReward']}</th>
                    <th field="goods_extra_reward_limit" width="180" align="left">${msg['statistic.recharge.extraRewardCount']}</th>
                    <th field="isTotalRecharge" width="180" align="left">${msg['statistic.recharge.isTotalRecharge']}</th>
                    <th field="totalVipPower" width="180" align="left">${msg['statistic.recharge.totalVipPower']}</th>
                    <%--<th field="delete" width="50" align="left" >删除</th>--%>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</body>
</html>
