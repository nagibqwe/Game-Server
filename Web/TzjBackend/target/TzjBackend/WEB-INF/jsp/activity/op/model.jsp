<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/4/8
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>${msg['statistic.model.modelConfig']}</title>
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
            $("#idCopy").val(0);
            $('.datetimepicker').datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd hh:ii',
                todayBtn: 1,
                autoclose: true
            });

            loadAllItem();
            $("[data-toggle='popover']").popover();
        });
    </script>

    <script>
        function addSaltIpGrp(obj) {
            // console.log(obj);
            var html = '<div name="cfg1" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label for="career">职业:</label>'+
                '<select id="career" name="career" class="career span8">'+
                '<option value="0">玄剑</option>'+
                '<option value="1">天英</option>'+
                '<option value="2">地藏</option>'+
                '<option value="3">罗刹</option>'+
                '<option value="9" selected>通用</option>'+
                '</select>'+
                '<label class="input-group-addon">模型ID:</label>' +
                '<input type="text" class="form-control validate[required]" style="width:100px;"  name="modelId" onkeyup="value=value.replace(/\\D/g,\'\')" style="">' +
                '<label class="input-group-addon">模型大小倍数:</label>' +
                '<input type="number" class="form-control validate[required,custom[number]]" style="width:100px;" name="scale" style="">' +
                '<label class="input-group-addon">旋转参数x:</label>' +
                '<input type="number" class="form-control validate[required,custom[number]]" style="width:100px;" name="rotX" style="">' +
                '<label class="input-group-addon">旋转参数y:</label>' +
                '<input type="number" class="form-control validate[required,custom[number]]" style="width:100px;" name="rotY" style="">' +
                '<label class="input-group-addon">旋转参数z:</label>' +
                '<input type="number" class="form-control validate[required,custom[number]]" style="width:100px;" name="rotZ" style="">' +
                '<label class="input-group-addon">位置参数x:</label>' +
                '<input type="number" class="form-control validate[required,custom[number]]" style="width:100px;" name="posX" style="">' +
                '<label class="input-group-addon">位置参数y:</label>' +
                '<input type="number" class="form-control validate[required,custom[number]]" style="width:100px;" name="posY" οnkeyup="value=value.replace(/[^\\d\\.]/g,\'\')" style="">' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" id="delGridGroup"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html)
        }
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
        //格式化删除
        function formatDelete(value,row,index) {
            return "<a href='#' class='btn btn-sm btn-danger' style='margin-right: 30px' onclick='deleteConfig("+row.id+")'>删除</a>";
        }
        //格式化修改
        function formatEdit(value,row,index) {
            return "<a href='#' onclick='edit("+row.id+",\""+row.tips+"\",\""+row.career+"\",\""+row.modelId+"\",\""+row.scale+"\",\""+row.rotX+"\",\""+row.rotY+"\",\""+row.rotZ+"\",\""+row.posX+"\",\""+row.posY+"\")'>修改</a>";
        }
        function reload() {
            $("#idCopy").val(0);
            $("#tips").val("");
            $(".career").val("9");
            $("input[name='modelId']").val("");
            $("input[name='scale']").val("");
            $("input[name='rotX']").val("");
            $("input[name='rotY']").val("");
            $("input[name='rotZ']").val("");
            $("input[name='posX']").val("");
            $("input[name='posY']").val("");
        }

        //点击修改
        var idCopy=0;
        function edit(id,tips,career,modelId,scale,rotX,rotY,rotZ,posX,posY) {
            if (confirm("${msg['statistic.ConfirmEdit']}")) {
                //删除之前动态生成的配置项
                $("div[name='cfg1']").remove();
                idCopy = id;
                $("#idCopy").val(id);
                $("#tips").val(tips);
                var career=stringToArry(career);
                var modelId=stringToArry(modelId);
                var scale=stringToArry(scale);
                var rotX=stringToArry(rotX);
                var rotY=stringToArry(rotY);
                var rotZ=stringToArry(rotZ);
                var posX=stringToArry(posX);
                var posY=stringToArry(posY);
                $.each(career,function (i) {
                    var icareer=career[i];
                    var imodelId=modelId[i];
                    var iscale=scale[i];
                    var irotX=rotX[i];
                    var irotY=rotY[i];
                    var irotZ=rotZ[i];
                    var iposX=posX[i];
                    var iposY=posY[i];

                    addSaltIpGrp($("#addSaltIpGrpBtn").get(0));
                    $("select[name='career']").eq(i).val(icareer);
                    $("input[name='modelId']").eq(i).val(imodelId);
                    $("input[name='scale']").eq(i).val(iscale);
                    $("input[name='rotX']").eq(i).val(irotX);
                    $("input[name='rotY']").eq(i).val(irotY);
                    $("input[name='rotZ']").eq(i).val(irotZ);
                    $("input[name='posX']").eq(i).val(iposX);
                    $("input[name='posY']").eq(i).val(iposY);
                })
            }
        }

        function stringToArry(str) {
            var arr = str.split(",");
            return arr;
        }
        //提交
        function submitConfig() {
            if ($("#activity_form").validationEngine('validate')) {
                $.ajax({
                    url: base + "/activityConfig/addOrEditModel",
                    method: "post",
                    dataType: "json",
                    data: $("#activity_form").serialize(),
                    success: function (data) {
                        if (data.ok) {
                            if (idCopy > 0) {
                                console.log("xiugai:"+idCopy);
                                $("#idCopy").val(0);
                                idCopy=0;
                                $("#modelData").datagrid("reload");
                                reload();
                                //删除之前动态生成的配置项
                                $("div[name='cfg1']").remove();
                                alert("${msg['statistic.configEditSuccess']}");
                            } else {
                                console.log("tianjia:"+idCopy);
                                $("#modelData").datagrid("reload");
                                reload();
                                //删除之前动态生成的配置项
                                $("div[name='cfg1']").remove();
                                alert("${msg['statistic.configAddSuccess']}");
                            }
                        } else {
                            alert("${msg['statistic.configEditOrAddFail']}" + data.msg);
                        }
                    }
                });
            }
        }
        //删除操作
        function deleteConfig(id) {
            if (confirm("${msg['statistic.ConfirmDelete']}")) {
                $.ajax({
                    url: base + "/activityConfig/deleteModel",
                    method: "post",
                    dataType: "json",
                    data: {"id": id},
                    success: function (data) {
                        if (data.ok) {
                            $("#modelData").datagrid("reload");
                            alert("${msg['statistic.configDeleteSuccess']}");
                        } else {
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
        <h3>${msg['statistic.model.modelConfig']}</h3>
    </div>

    <form id="activity_form" method="post" class="main form-horizontal" action="#">

        <div id="customForm">
            <div class="col-lg-12 col-sm-12 col-md-12">
                <div class="col-lg-1"></div>
                <div class="col-lg-12">
                    <legend>${msg['statistic.model.modelConfig']}</legend>
                    <fieldset>
                        <div class="input-group" id="tipshuju">
                            <input type="hidden" id="idCopy" name="idCopy">
                            <label class="input-group-addon">${msg['statistic.tips']}:</label>
                            <div class="col-md-2 col-sm-2 col-xs-2">
                                <input type="text" class="form-control validate[required]" id="tips" name="tips" value="" required/>
                            </div>
                        </div>
                        <div class="input-group" id="saltIpGroup">
                            <label class="input-group-addon">${msg['statistic.model.modelConfig']}:</label>
                            <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addSaltIpGrpBtn"
                                    onclick="addSaltIpGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>
                            <input type="hidden" id="cfgCount1" value="0">
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
        <table id="modelData" class="easyui-datagrid" style="width:1650px;height:auto" data-options="url:'${base}/activityConfig/queryModelConfig',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
            <thead>
            <tr>
                <th field="id" width="50" align="left">ID</th>
                <th field="career" width="50" align="left">${msg['activity.login.career']}</th>
                <th field="modelId" width="100" align="left">${msg['statistic.model.modelId']}</th>
                <th field="scale" width="80" align="left">${msg['statistic.model.scale']}</th>
                <th field="rotX" width="80" align="left">${msg['statistic.model.rotX']}</th>
                <th field="rotY" width="80" align="left">${msg['statistic.model.rotY']}</th>
                <th field="rotZ" width="80" align="left">${msg['statistic.model.rotZ']}</th>
                <th field="posX" width="80" align="left">${msg['statistic.model.posX']}</th>
                <th field="posY" width="80" align="left">${msg['statistic.model.posY']}</th>
                <th field="tips" width="200" align="left">${msg['statistic.model.tips']}</th>
                <th field="delete" width="50" align="left" formatter="formatDelete">${msg['statistic.delete']}</th>
                <th field="edit" width="50" align="left" formatter="formatEdit">${msg['statistic.action']}</th>
            </tr>
            </thead>
        </table>
    </div>
    <div id="msg" class="container-fluid"></div>
</div>
<jsp:include page="../../item/itemAddModel.jsp"/>
</body>
</html>

