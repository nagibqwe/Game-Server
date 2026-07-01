<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/8/19
  Time: 10:18
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>标签库配置</title>
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
    <script type="text/javascript" src="${base}/js/dateFormat.js"></script>
    <link rel="stylesheet" href="${base}/css/fileCss.css">
    <script type="text/javascript" src="${base}/js/excel.js"></script>
    <script type="text/javascript" src="${base}/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${base}/easyui/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" href="${base}/easyui/themes/default/easyui.css" type="text/css">
    <link rel="stylesheet" href="${base}/easyui/themes/icon.css" type="text/css">

    <script type="text/javascript">
        var base = '${base}';
        $(function () {
            $('.datetimepicker').datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd hh:ii',
                todayBtn: 1,
                autoclose: true
            });

            $("[data-toggle='popover']").popover();
        });
    </script>

    <script>
        //格式化删除
        function formatDelete(value,row,index) {
            return "<a href='#' class='btn btn-sm btn-danger' style='margin-right: 30px' onclick='deleteConfig("+row.id+")'>删除</a>";
        }
        //格式化修改
        function formatEdit(value,row,index) {
            return "<a href='#' onclick='edit("+row.id+",\""+row.name+"\",\""+row.icon+"\","+row.style+")'>修改</a>";
        }
        //格式化ui风格显示
        function formatStyle(value,row,index) {
            if (value == 1){
                return "基础";
            } else if (value == 2){
                return "圣诞";
            }  else if (value == 3){
                return "元旦";
            } else if (value == 4){
                return "春节";
            } else if (value == 5){
                return "情人节";
            } else if (value == 6){
                return "泼水节";
            }
        }
        function reload() {
            $("input[name='id']").val("");
            $("input[name='name']").val("");
            $("input[name='icon']").val("");
            $("select[name='style']").eq(0).val(1);
        }

        //点击修改
        function edit(id,name,icon,style) {
            $("#dlg_edit_status").dialog('open');
            if (confirm("${msg['statistic.ConfirmEdit']}")) {
                $("input[name='id']").eq(0).val(id);
                $("input[name='name']").eq(0).val(name);
                $("input[name='icon']").eq(0).val(icon);
                $("select[name='style']").eq(0).val(style);
            }
        }

        //提交
        function submitConfig() {
            if ($("#activity_form").validationEngine('validate')) {
                var dataParam = $("#activity_form").serialize();
                $.ajax({
                    url: base + "/activityConfig/checkTag",
                    async:false,
                    method: "post",
                    dataType: "json",
                    data: dataParam,
                    success: function (data) {
                        if (data.ok) {
                            $("#loadingmodal").modal({backdrop:'static',keyboard:false});
                            $.ajax({
                                url: base + "/activityConfig/addTag",
                                method: "post",
                                dataType: "json",
                                data: dataParam,
                                success: function (data) {
                                    if (data.ok) {
                                        $("#loadingmodal").modal('hide');
                                        alert("数据库添加成功:" + data.msg);
                                        $("#tagData").datagrid("reload");
                                        reload();
                                        console.log(data);
                                    } else {
                                        $("#loadingmodal").modal('hide');
                                        alert("添加失败");
                                    }
                                }
                            });
                        } else {
                            if(confirm("是否覆盖已有的数据?")){
                                $("#loadingmodal").modal({backdrop:'static',keyboard:false});
                                //点击确定后操作
                                $.ajax({
                                    url: base + "/activityConfig/updateTag",
                                    method: "post",
                                    dataType: "json",
                                    data: dataParam,
                                    success: function (data) {
                                        if (data.ok) {
                                            $("#loadingmodal").modal('hide');
                                            alert("数据库修改成功:" + data.msg);
                                            $("#tagData").datagrid("reload");
                                            reload();
                                            console.log(data);
                                        } else {
                                            $("#loadingmodal").modal('hide');
                                            alert("修改失败");
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

            }
        }
        //删除操作
        function deleteConfig(id) {
            if (confirm("${msg['statistic.ConfirmDelete']}")) {
                $("#loadingmodal").modal({backdrop:'static',keyboard:false});
                $.ajax({
                    url: base + "/activityConfig/deleteTag",
                    method: "post",
                    dataType: "json",
                    data: {"id": id},
                    success: function (data) {
                        if (data.ok) {
                            $("#loadingmodal").modal('hide');
                            $("#tagData").datagrid("reload");
                            alert("${msg['statistic.configDeleteSuccess']}" + data.msg);
                        } else {
                            $("#loadingmodal").modal('hide');
                            alert("${msg['statistic.configDeleteFail']}");
                        }
                    }
                });
            }
        }
    </script>

</head>
<body style="font-size: 12px">
<div class="container-fluid">
    <div id="header">
        <h3>标签库配置</h3>
    </div>

    <form id="activity_form" method="post" class="main form-horizontal" action="#">

        <div id="customForm">
            <div class="col-lg-12 col-sm-12 col-md-12">
                <div class="col-lg-1"></div>
                <div class="col-lg-12">
                    <legend>标签库配置</legend>
                    <fieldset>
                        <div class="input-group" id="saltIpGroup">
                            <label class="input-group-addon">标签库配置:</label>
                            <div name="cfg1" class="input-group saltIp" style="width:60%;padding:0 0 1px 0;">
                                <label class="input-group-addon">标签ID:</label>
                                <input type="text" class="form-control validate[required,custom[number]]" style="width:100px;"  name="id" onkeyup="value=value.replace(/\D/g,'')" style="">
                                <label class="input-group-addon">标签名:</label>
                                <input type="text" class="form-control validate[required]" style="width:200px;" name="name" style="">
                                <label class="input-group-addon">标签icon:</label>
                                <input type="text" class="form-control validate[required]" style="width:433px;" name="icon" onkeyup="value=value.replace(/\D/g,'')" style="">
                                <label class="input-group-addon">UI风格:</label>
                                <select id="style" name="style" style="width: 120px" class="form-control">
                                    <option value="1">基础</option>
                                    <option value="2">圣诞</option>
                                    <option value="3">元旦</option>
                                    <option value="4">春节</option>
                                    <option value="5">情人节</option>
                                    <option value="6">泼水节</option>
                                </select>
                            </div>
                        </div>
                    </fieldset>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="col-md-12 col-sm-12 col-lg-12" style="height: 30px"></div>
<div class="col-sm-5 col-sm-5"></div>
<div class="row-fluid col-sm-7 col-md-7">
    <input type="button" value="${msg['jsp.giftbag.button.submit']}" onclick="submitConfig()"
           class="btn btn-sm btn-danger"
           style="margin-right: 30px"/>
</div>
<div class="container-fluid">
    <div class="row-fluid">
        <table id="tagData" class="easyui-datagrid" style="width:1650px;height:auto" data-options="url:'${base}/activityConfig/queryTagConfig',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
            <thead>
            <tr>
                <th field="id" width="50" align="left">ID</th>
                <th field="name" width="50" align="left">name</th>
                <th field="icon" width="100" align="left">icon</th>
                <th field="style" width="50" align="left" formatter="formatStyle">UI风格</th>
                <th field="delete" width="50" align="left" formatter="formatDelete">${msg['statistic.delete']}</th>
                <th field="edit" width="50" align="left" formatter="formatEdit">${msg['statistic.action']}</th>
            </tr>
            </thead>
        </table>
    </div>
    <div id="msg" class="container-fluid"></div>
</div>
<jsp:include page="../../item/itemAddModel.jsp"/>
<jsp:include page="../../commonmodal2.jsp"/>
</body>
</html>
