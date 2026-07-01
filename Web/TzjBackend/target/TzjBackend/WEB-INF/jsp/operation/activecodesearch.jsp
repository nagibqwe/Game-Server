<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>${msg['jsp.acodesearch.titile']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.base64.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${base}/easyui/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="${base}/easyui/themes/default/easyui.css" type="text/css">
<link rel="stylesheet" href="${base}/easyui/themes/icon.css" type="text/css">
<script type="text/javascript" src="${base}/js/excel.js"></script>
<script type="text/javascript">
    //定义一个全局变量来记录当前页面是哪个datagrid
    var dataGrid;
    var base = '${base}';
    $(function () {
        $("#excel_btn").hide();
        $("#code").hide();
        $("#activeCodeListPatch").datagrid("getPanel").hide();
        $("#activeCodeListCode").datagrid("getPanel").hide();
        search();
    });

    function search() {
        // $("#loadingmodal").modal({backdrop: 'static', keyboard: false});

        $("#activeCodeListPatch").datagrid("getPanel").hide();
        $("#activeCodeListCode").datagrid("getPanel").hide();
        $('#activeCodeList').datagrid("getPanel").show();
        $('#activeCodeList').datagrid("loadData", {total:0, rows:[]});
        $("#excel_btn").show();
        $("#activeCodeList").datagrid("load", {
        });
        //有这个数据才能显示出来
        $("#activeCodeList").datagrid({
            onLoadSuccess:function(data) {
                console.log("total:"+data.total+" rows:"+data.rows);
            }
        });
        dataGrid='#activeCodeList1 table';
    }

    function deleteCode(code) {
        if(confirm("确认是否禁用？")) {
            $.ajax({
                type: "POST",
                url: base + "/operation/deleteCode",
                data: {"code" : code},
                dataType: "json",
                success: function(data){
                    if (!data.ok) {
                        alert(data.msg);
                    }
                    search();
                }
            });
        }
    }

    function searchByCode() {
        if ($("#searchType").val() == "1") {
            var batchId = $("#batchId").val();
            if (batchId == undefined || batchId == "") {
                $('#activeCodeList').datagrid("getPanel").hide();
                search();
                return;
            }
            $('#activeCodeList').datagrid("getPanel").hide();
            searchByBatch();
            return;
        }

        $('#activeCodeList').datagrid("getPanel").hide();
        $('#activeCodeListPatch').datagrid("getPanel").hide();
        $('#activeCodeListCode').datagrid("loadData", {total:0, rows:[]});
        $("#excel_btn").show();
        $("#activeCodeListCode").datagrid("load", {
            "code":$("#code").val()
        });
        //有这个数据才能显示出来
        $("#activeCodeListCode").datagrid({
            onLoadSuccess:function(data) {

            }
        });
        dataGrid='#activeCodeListCode1 table';
    }

    function searchByBatch() {
        $("#activeCodeListPatch").datagrid("loadData", {total:0, rows:[]});
        $('#activeCodeList').datagrid("getPanel").hide();
        $("#activeCodeListCode").datagrid("getPanel").hide();
        $("#excel_btn").show();
        $("#activeCodeListPatch").datagrid("load", {
            "batchId":$("#batchId").val()
        });
        //有这个数据才能显示出来
        $("#activeCodeListPatch").datagrid({
            onLoadSuccess:function(data) {

            }
        });
        dataGrid='#activeCodeListPatch1 table';
    }

    // function getString(value) {
    //     if (value == "null") {
    //         return "";
    //     }
    //     return value;
    // }

    function changeSearchType() {
        var type = $("#searchType").val();
        if (type == "0") {
            $("#code").show();
            $("#batchId").hide();
        } else {
            $("#code").hide();
            $("#batchId").show();
        }
    }

    function exportExcel() {
        // alert($(dataGrid).eq(2).add($(dataGrid).eq(3)).length);//datagrid有六个table组成
        // alert($("#activeCodeList1 table").length);//datagrid有六个table组成
        $(dataGrid).eq(2).add($(dataGrid).eq(3)).tableExport({type: 'excel', escape: 'false'});
    }
    // function exportExcel() {
    //     var JSONData = JSON.stringify(dataGrid.datagrid('getData').rows);
    //     console.log("JSONData"+JSONData);
    //     var ReportTitle = "activeCode";
    //     export_excel(JSONData,ReportTitle);
    // }
    //格式化是否为万能码
    function formatParam(value) {
        if (value == 1) {
            return "${msg['jsp.acodesearch.yes']}";
        }else {
            return "${msg['jsp.acodesearch.no']}";
        }
    }
    //格式化操作
    function formatAction(value,row,index) {
        if (value == 0){
            return "<a href='#' onclick='deleteCode(\""+row.code+"\")'>${msg['jsp.giftbag.button.forbidden']}</a>";
        }else {
            return value;
        }
    }
</script>
</head>
<body>
<div id="activecode">
<div class="container-fluid">
    <form id="query_form" class="well form-inline">
        <select id="searchType" class="span2" onchange="changeSearchType();">
            <option value="1" selected="selected">${msg['jsp.acodesearch.selectbatch']}</option>
            <option value="0">${msg['jsp.acodesearch.selectcodeuse']}</option>
        </select>
        <input id="code" type="text" name="code" class="span2">
        <input id="batchId" type="text" name="batchId" class="span1">
        <input type="button" id="query_btn" value="${msg['jsp.log.search']}" class="btn btn-primary" onclick="searchByCode()">
        <input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel()">
    </form>
    <div id="msg"></div>
    <div id="activeCodeList1">
        <table id="activeCodeList" class="easyui-datagrid" style="width:1650px;height:auto" data-options="url:'${base}/operation/queryActiveCode',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
            <thead>
            <tr>
                <th field="code" width="100" align="left">${msg['jsp.acodesearch.code']}</th>
                <th field="activeName" width="85" align="left">${msg['jsp.acodesearch.activeName']}</th>
                <th field="batch" width="40" align="left">${msg['jsp.acodesearch.batch']}</th>
                <th field="itemList" width="250" align="left">${msg['jsp.acodesearch.itemList']}</th>
                <th field="param" width="40" align="left" formatter="formatParam">${msg['jsp.acodesearch.param']}</th>
                <th field="valide_time_begin" width="120" align="left">${msg['jsp.acodesearch.valide_time_begin']}</th>
                <th field="valide_time_end" width="120" align="left">${msg['jsp.acodesearch.valide_time_end']}</th>
                <th field="plateform_name_big" width="100" align="left">${msg['jsp.acodesearch.plateform_name_big']}</th>
                <th field="valide_server_id_list" width="150" align="left">${msg['jsp.acodesearch.valide_server_id_list']}</th>
                <th field="create_time" width="120" align="left">${msg['jsp.acodesearch.create_time']}</th>
                <th field="deleteTime" width="100" align="left" formatter="formatAction">${msg['jsp.acodesearch.opt']}</th>
            </tr>
            </thead>
        </table>
    </div>
    <div id="activeCodeListPatch1">
        <table id="activeCodeListPatch" class="easyui-datagrid" style="width:1650px;height:auto" data-options="url:'${base}/operation/queryActiveCodeByBatchId',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
            <thead>
            <tr>
                <th field="code" width="70" align="left">${msg['jsp.acodesearch.code']}</th>
                <th field="activeName" width="70" align="left">${msg['jsp.acodesearch.activeName']}</th>
                <th field="batch" width="40" align="left">${msg['jsp.acodesearch.batch']}</th>
                <th field="itemList" width="220" align="left">${msg['jsp.acodesearch.itemList']}</th>
                <th field="param" width="40" align="left" formatter="formatParam">${msg['jsp.acodesearch.param']}</th>
                <th field="valide_time_begin" width="120" align="left">${msg['jsp.acodesearch.valide_time_begin']}</th>
                <th field="valide_time_end" width="120" align="left">${msg['jsp.acodesearch.valide_time_end']}</th>
                <th field="plateform_name_big" width="100" align="left">${msg['jsp.acodesearch.plateform_name_big']}</th>
                <th field="valide_server_id_list" width="150" align="left">${msg['jsp.acodesearch.valide_server_id_list']}</th>
                <th field="create_time" width="120" align="left">${msg['jsp.acodesearch.create_time']}</th>
                <th field="get_time" width="85" align="left">${msg['jsp.acodesearch.get_time']}</th>
                <th field="get_player_id" width="100" align="left">${msg['jsp.acodesearch.get_player_id']}</th>
                <th field="get_plateform_aid" width="100" align="left">${msg['jsp.acodesearch.get_plateform_aid']}</th>
                <th field="get_account_id" width="100" align="left">${msg['jsp.acodesearch.get_account_id']}</th>
                <th field="get_plateform_name" width="80" align="left">${msg['jsp.acodesearch.get_plateform_name']}</th>
                <th field="get_server_id" width="100" align="left">${msg['jsp.acodesearch.get_server_id']}</th>
            </tr>
            </thead>
        </table>
    </div>
    <div id="activeCodeListCode1">
        <table id="activeCodeListCode" class="easyui-datagrid" style="width:1650px;height:auto" data-options="url:'${base}/operation/queryActiveCodeByCode',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
            <thead>
            <tr>
                <th field="activeCode" width="150" align="left">${msg['jsp.acodesearch.activeCode']}</th>
                <th field="platformName" width="150" align="left">${msg['jsp.acodesearch.platformName']}</th>
                <th field="sid" width="150" align="left">${msg['jsp.acodesearch.sid']}</th>
                <th field="roleId" width="150" align="left">${msg['jsp.acodesearch.roleId']}</th>
                <th field="userId" width="150" align="left">${msg['jsp.acodesearch.userId']}</th>
                <th field="itemList" width="200" align="left">${msg['jsp.acodesearch.itemList']}</th>
                <th field="actionId" width="150" align="left">${msg['jsp.acodesearch.actionId']}</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</div>
<%--<div class="container-fluid tab-content">--%>
    <%--<div class="tab-pane active" id="activecode"></div>--%>
<%--</div>--%>
<%--<jsp:include page="../commonmodal.jsp"/>--%>
</body>
</html>