<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>抽奖幸运值</title>
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

    <script type="text/javascript">
        var gs=${sessionScope.groupServer};
        var actType = '${type}';
        var base = '${base}';

        $(function () {
            $('.datetimepicker').datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd hh:ii',
                todayBtn: 1,
                autoclose: true
            });

            loadAllItem();
            loadAllServerList();
            load(base, actType);
            loadActivityFestivalType();
            $("#publishActivity").modal('hide');

            $("[data-toggle='popover']").popover();
        });
    </script>

    <script>
        $('body').on('hidden.bs.modal', '.modal', function () {
            $(this).removeData('bs.modal');
        });
        $(document).on('click', '#delGridGroup', function () {
            var el = this.parentNode.parentNode
            var saltIp = $(this).parent().parent().find('#saltIp').val()
            el.parentNode.removeChild(el)
            return
            // if (saltIp == ""){
            //     el.parentNode.removeChild(el)
            //     return
            // }
            // alertify.confirm('您确定要删除选中的命令？',
            //     function (e) {
            //         el.parentNode.removeChild(el)
            //         return
            //     }
            //     )

        });
        //全选处理
        $("#testAll").click(function () {
            var flag = $("#testAll").prop("checked");
            $(".test:not(:disabled)").prop("checked", flag);
        });
        //格式化活动状态
        function formatState(value) {
            if (value == 0){
                return "已提交待发布";
            }else if (value == 3){
                return "发布成功";
            }else {
                return "error";
            }
        }
        // //格式化测试
        // function formatTest(value,row,index) {
        //     return '<input type="checkbox" class="test">';
        //
        // }
        // //格式化验证
        // function formatVerify(value,row,index) {
        //     var valiadable = row.state == 1;
        //     return "<input type='checkbox' class='valid'" + (valiadable ? "" : "disabled='disabled'") + ">";
        // }
        //格式化发布
        function formatPublish(value,row,index) {
            return "<a href='#' class='btn btn-sm btn-success' style='margin-right: 30px' onclick='publish("+row.id+","+row.totalLuckyValue+")'>发布</a>";
            // var publishable = row.state == 2;
            // return "<input type='checkbox' class='publish'" + (publishable ? "" : "disabled='disabled'") + ">";
        }
        //格式化删除
        function formatDelete(value,row,index) {
            return "<a href='#' class='btn btn-sm btn-danger' style='margin-right: 30px' onclick='deleteConfig("+row.id+")'>删除</a>";
        }
        //格式化修改
        function formatEdit(value,row,index) {
            return "<a href='#' onclick='edit("+row.id+","+row.totalLuckyValue+",\""+row.tips+"\")'>修改</a>";
        }
        //修改数据
        function edit(id,totalLuckyValue,tips) {
            $("#idCopy").val(id);
            $("#totalLuckyValue").val(totalLuckyValue);
            $("#tips").val(tips);
        }
        function reload() {
            $("#idCopy").val("");
            $("#totalLuckyValue").val("");
            $("#tips").val("");
        }
        //提交按钮
        function submitConfig() {
            var idCopy=$("#idCopy").val();
            var totalLuckyValue=$("#totalLuckyValue").val();
            var tips=$("#tips").val();
            if (!$('#activity_form').validationEngine('validate')) {
                alert("验证失败");
                return;
            }
            $.ajax({
                url: base + "/activityConfig/addLuckyValue",
                method: "post",
                dataType: "json",
                data: {"idCopy" : idCopy,
                    "totalLuckyValue" : totalLuckyValue,
                    "tips" : tips
                },
                success: function (data) {
                    if (data.ok) {
                        if($("#idCopy").val()>0){
                            $("#idCopy").val(0);
                            $("#luckyValueData").datagrid("reload");
                            reload();
                            alert("活动修改成功");
                        }else{
                            $("#luckyValueData").datagrid("reload");
                            reload();
                            alert("活动添加成功");
                        }
                    } else {
                        alert("活动添加失败" + data.msg);
                    }
                }
            });
        }
        function deleteConfig(id) {
            if (confirm("确认删除？")) {
                $.ajax({
                    url: base + "/activityConfig/deleteLuckyValue",
                    method: "post",
                    dataType: "json",
                    data: {"id": id},
                    success: function (data) {
                        if (data.ok) {
                            $("#luckyValueData").datagrid("reload");
                            alert("活动删除成功");
                        } else {
                            alert("活动删除失败");
                        }
                    }
                });
            }
        }
        $(document).ready(function(){
            $("#select_group").combobox({
                onChange:function (newvalue, oldvalue) {
                    groupChanged(newvalue);
                }
            });

            var data = [];
            for (var i = 0; i < gs.length; i++) {
                data.push(gs[i].GroupName);
            }
            // //新增所有平台
            // var groupName = {
            //     value:0,
            //     text:'全选'
            // };
            // data.push(groupName);
            $("#select_group").combobox("loadData", data);
            $('#select_group').combobox('select',data[0].value);

        });
        var id_;
        var totalLuckyValue_;
        function publish(id,totalLuckyValue) {
            id_ = id;
            totalLuckyValue_=totalLuckyValue;
            $("#selectServer").dialog("open");
        }
        //确认发布时操作
        function toSelect() {
            $("#cover").val(0);
            if(confirm("是否覆盖正在进行的活动?")){
                //点击确定后操作
                $("#loadingmodal").modal({backdrop:'static',keyboard:false});
                $("#cover").val(1);
                var serveridValues = "";
                var serverids = $('#select_server').combobox('getValues');
                for (var i=0; i<serverids.length; i++) {
                    if (serveridValues != "") {
                        serveridValues += ","
                    }
                    serveridValues += serverids[i];
                }
                $.post(base + "/activityConfig/publishActivity",
                    {   "groupName":$("#select_group").combobox('getValue'),
                        "serverids":serveridValues,
                        "id":id_,
                        "totalLuckyValue":totalLuckyValue_,
                        "cover": $("#cover").val()
                    },
                    function (data) {
                        $("#loadingmodal").modal('hide');
                        $("#msg").html("<span>" + data.msg + "</span>");
                        $("#luckyValueData").datagrid("reload");
                        $("#selectServer").dialog("close");
                    });
            }
        }
    </script>

</head>
<body style="font-size: 12px">
<div class="container-fluid">
    <div id="header">
        <h3>抽奖幸运值</h3>
    </div>

    <form id="activity_form" method="post" class="main form-horizontal" action="#">

        <div id="customForm">
            <div class="col-lg-12 col-sm-12 col-md-12">
                <div class="col-lg-1"></div>
                <div class="col-lg-11">
                    <legend>总幸运值配置</legend>
                    <fieldset>
                        <div class="input-group" id="Luckyshuju">
                            <label class="input-group-addon">总幸运值配置:</label>
                            <div class="col-md-2 col-sm-2 col-xs-2">
                                <input type="text" class="form-control" id="totalLuckyValue" name="totalLuckyValue" placeholder="例：50" onkeyup="value=value.replace(/\D/g,'')" value=""
                                       class="input-xlarge" required/>
                            </div>
                        </div>
                        <div class="input-group" id="tipshuju">
                            <label class="input-group-addon">备注说明:</label>
                            <div class="col-md-2 col-sm-2 col-xs-2">
                                <input type="text" class="form-control" id="tips" name="tips" value=""
                                       class="input-xlarge" required/>
                                <input type="hidden" id="idCopy" value="">
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
        <table id="luckyValueData" class="easyui-datagrid" style="width:1650px;height:auto" data-options="url:'${base}/activityConfig/queryActivityConfig',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
            <thead>
            <tr>
                <th field="id" width="60" align="left">活动ID</th>
                <th field="totalLuckyValue" width="100" align="center">总幸运值</th>
                <%--<th field="custom" width="300" align="left">数据</th>--%>
                <th field="tips" width="200" align="left">备注说明</th>
                <th field="state" width="120" align="left" formatter="formatState">活动状态</th>
                <th field="toSidList" width="200" align="left">发布列表</th>
                <th field="okSidList" width="200" align="left">成功列表</th>
                <%--<th field="test" width="50" align="left" formatter="formatTest">测试<input type='checkbox' id='testAll'/></th>--%>
                <%--<th field="verify" width="50" align="left" formatter="formatVerify">验证<input type='checkbox' id='verifyAll'/></th>--%>
                <th field="publish" width="50" align="left" formatter="formatPublish">发布</th>
                <th field="delete" width="50" align="left" formatter="formatDelete">删除</th>
                <th field="edit" width="50" align="left" formatter="formatEdit">操作</th>
            </tr>
            </thead>
        </table>
    </div>
    <div id="msg" class="container-fluid"></div>
</div>

<jsp:include page="../../item/itemAddModel.jsp"/>
<jsp:include page="../../commonmodal.jsp"/>

<div id="selectServer" class="easyui-dialog" data-options="title:'发布服务器选择',modal:true,iconCls:'icon-edit',width:400,height:300,closed:true,buttons:'#selectConfirm'" style="text-align:center;">
    <div style="margin-top: 100px">
        <div class="control-group">
            <label for="select_group" class="control-label">${msg['jsp.server']}</label>
            <div class="controls">
                <select id="select_group" name="groupName" panelHeight="auto" style="width:150px;" editable="false" class="easyui-combobox"></select>
                <select id="select_server" name="serverId" panelHeight="auto" multiple="true" style="width:150px;" editable="false" class="easyui-combobox"></select>
                <input id="cover" type="hidden" name="cover"/>
            </div>
        </div>
    </div>
    <div style="margin-top: 10px">
        <span>${sessionScope.lang_txt.lang1210}</span>
    </div>
</div>
<div id="selectConfirm">
    <a id="toSelect" onclick="toSelect()" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:$('#selectServer').dialog('close');">取消</a>
</div>
</body>
</html>
