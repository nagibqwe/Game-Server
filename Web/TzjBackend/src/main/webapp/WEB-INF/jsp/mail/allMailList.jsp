<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/4/1
  Time: 15:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>全服邮件列表</title>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${base}/js/global.js"></script>
    <script type="text/javascript" src="${base}/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${base}/easyui/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" href="${base}/easyui/themes/default/easyui.css" type="text/css">
    <link rel="stylesheet" href="${base}/easyui/themes/icon.css" type="text/css">
    <script>
        var base = "${base}";
        var items = new Map();
        $(function () {
            loadAllItem2();
            loadAllMailList();
            $(".datetimepicker").datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                weekStart: 1,
                todayBtn: 1,
                autoclose: 1,
                minView: 3
            });
        });
        function loadAllMailList() {
            waitDeal();
            mine();
            history();
        }
        //查询待处理邮件列表
        function waitDeal() {
            $("#waitDealDiv").show();
            $('#waitDeal').datagrid("loadData", {total:0, rows:[]});
            $("#waitDeal").datagrid("load",
                {
                    "type":1
                });
        }
        //隐藏
        function doClean() {
            $("#waitDealDiv").hide();
        }
        //格式化附件道具显示
        function formatItems(value,row,index) {
            if(row.items==""||row.items==undefined||row.items==null){
                return "";
            }
            var itemArr=row.items.split("}");
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
        //格式化待处理邮件的操作
        function formatOperate(value,row,index) {
            return '<input type="button" class="btn btn-primary pull-left sendall" value="${msg['mail.list.operate2']}" onclick="operateMail('+row.id+',1)">' +
                '<input type="button" class="btn btn-primary pull-right sendall" value="${msg['mail.list.operate3']}" onclick="operateMail('+row.id+',2)">';
        }

        //查询待处理邮件列表
        function mine() {
            $("#mineDiv").show();
            $('#mine').datagrid("loadData", {total:0, rows:[]});
            $("#mine").datagrid("load",
                {
                    "type":2
                });
        }
        //隐藏
        function doClean2() {
            $("#mineDiv").hide();
        }
        //格式化我的邮件的操作
        function formatOperate2(value,row,index) {
            return '<input type="button" class="btn btn-primary pull-center sendall" value="${msg['mail.list.operate4']}" onclick="operateMail('+row.id+',3)">'
        }

        //查询历史邮件列表
        function history() {
            $("#historyDiv").show();
            $('#history').datagrid("loadData", {total:0, rows:[]});
            $("#history").datagrid("load",
                {
                    "type":3
                });
        }
        //隐藏
        function doClean3() {
            $("#historyDiv").hide();
        }
        //对邮件列表的操作处理 1：发送，2不予发送，3：删除
        function operateMail(id, type) {
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.post(base + "/mail/adminAllMail", {id: id, type: type}, function (data) {
                $("#loadingmodal").modal('hide');
                loadAllMailList();
                alert(data.msg);
            });
        }
        //格式化一键发送全服邮件
        function formatOneKeySendAll(value,row,index) {
            if (index==0){
                return '<input type="button" class="btn btn-primary pull-center sendall" value="${msg['mail.list.operate1']}" onclick="oneKeySendAll()">'
            }
        }
        //一键发送全服邮件
        function oneKeySendAll() {
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.post(base + "/mail/onekeySendAll", {}, function (data) {
                $("#loadingmodal").modal('hide');
                loadAllMailList();
                alert(data.msg);
            });
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
    </script>
</head>
<body>
<div>
    <div style="display: inline-block;width: 150px" >
        <a href="#" class="easyui-linkbutton" plain="true" onclick="waitDeal()" iconCls="icon-reload">${msg['mail.waitlist']}</a>
    </div>
    <a href="#" class="easyui-linkbutton" plain="true" onclick="doClean()" iconCls="icon-clear">隐藏</a>
    <div id="waitDealDiv" style="padding:3px">
        <table id="waitDeal" class="easyui-datagrid" style="width:1650px;height:auto" data-options="url:'${base}/mail/queryAll',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
            <thead>
            <tr>
                <th field="id" width="50" align="left">${msg['jsp.deductitem.td.number']}</th>
                <th field="minLv" width="60" align="left">${msg['activity.login.minLv']}</th>
                <th field="maxLv" width="60" align="left">${msg['activity.login.maxLv']}</th>
                <th field="career" width="50" align="left" formatter="formatCareer">${msg['activity.login.career']}</th>
                <th field="serverIdList" width="250" align="left">${msg['jsp.serverList']}</th>
                <th field="title" width="85" align="left">${msg['activity.login.mailTitle']}</th>
                <th field="content" width="200" align="left">${msg['activity.login.mailcontent']}</th>
                <th field="reason" width="150" align="left">${msg['mail.jsp.reason']}</th>
                <th field="items" width="150" align="left">${msg['mail.jsp.fujian']}</th>
                <th field="itemStr" width="200" align="left" formatter="formatItems">${msg['mail.jsp.fujian']}${msg['activity.login.mailcontent']}</th>
                <th field="createUser" width="150" align="left">${msg['announce.jsp.createUser']}</th>
                <th field="createDate" width="150" align="left">${msg['announce.jsp.createtime']}</th>
                <th field="stateStr" width="150" align="left">${msg['mail.jsp.mailState']}</th>
                <th field="action" width="220" align="center" formatter="formatOperate">${msg['mail.jsp.operate']}</th>
                <th field="action1" width="150" align="left" formatter="formatOneKeySendAll">${msg['mail.list.operate1']}</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<div>
    <div style="display: inline-block;width: 150px" >
        <a href="#" class="easyui-linkbutton" plain="true" onclick="mine()" iconCls="icon-reload">${msg['mail.minelist']}</a>
    </div>
    <a href="#" class="easyui-linkbutton" plain="true" onclick="doClean2()" iconCls="icon-clear">隐藏</a>
    <div id="mineDiv" style="padding:3px">
        <table id="mine" class="easyui-datagrid" style="width:1650px;height:auto" data-options="url:'${base}/mail/queryAll',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
            <thead>
            <tr>
                <th field="id" width="50" align="left">${msg['jsp.deductitem.td.number']}</th>
                <th field="minLv" width="60" align="left">${msg['activity.login.minLv']}</th>
                <th field="maxLv" width="60" align="left">${msg['activity.login.maxLv']}</th>
                <th field="career" width="50" align="left" formatter="formatCareer">${msg['activity.login.career']}</th>
                <th field="serverIdList" width="250" align="left">${msg['jsp.serverList']}</th>
                <th field="title" width="85" align="left">${msg['activity.login.mailTitle']}</th>
                <th field="content" width="200" align="left">${msg['activity.login.mailcontent']}</th>
                <th field="reason" width="150" align="left">${msg['mail.jsp.reason']}</th>
                <th field="items" width="150" align="left">${msg['mail.jsp.fujian']}</th>
                <th field="itemStr" width="200" align="left" formatter="formatItems">${msg['mail.jsp.fujian']}${msg['activity.login.mailcontent']}</th>
                <th field="createUser" width="150" align="left">${msg['announce.jsp.createUser']}</th>
                <th field="createDate" width="150" align="left">${msg['announce.jsp.createtime']}</th>
                <th field="adminUser" width="150" align="left">${msg['mail.jsp.adminUser']}</th>
                <th field="adminDate" width="150" align="left">${msg['mail.jsp.adminTime']}</th>
                <th field="sendErrorMess" width="150" align="left">${msg['mail.jsp.demo']}</th>
                <th field="stateStr" width="150" align="left">${msg['mail.jsp.mailState']}</th>
                <th field="action" width="200" align="center" formatter="formatOperate2">${msg['mail.jsp.operate']}</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<div>
    <div style="display: inline-block;width: 150px" >
        <a href="#" class="easyui-linkbutton" plain="true" onclick="history()" iconCls="icon-reload">${msg['mail.historylist']}</a>
    </div>
    <a href="#" class="easyui-linkbutton" plain="true" onclick="doClean3()" iconCls="icon-clear">隐藏</a>
    <div id="historyDiv" style="padding:3px">
        <table id="history" class="easyui-datagrid" style="width:1650px;height:auto" data-options="url:'${base}/mail/queryAll',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
            <thead>
            <tr>
                <th field="id" width="50" align="left">${msg['jsp.deductitem.td.number']}</th>
                <th field="minLv" width="60" align="left">${msg['activity.login.minLv']}</th>
                <th field="maxLv" width="60" align="left">${msg['activity.login.maxLv']}</th>
                <th field="career" width="50" align="left" formatter="formatCareer">${msg['activity.login.career']}</th>
                <th field="serverIdList" width="250" align="left">${msg['jsp.serverList']}</th>
                <th field="title" width="85" align="left">${msg['activity.login.mailTitle']}</th>
                <th field="content" width="200" align="left">${msg['activity.login.mailcontent']}</th>
                <th field="reason" width="150" align="left">${msg['mail.jsp.reason']}</th>
                <th field="items" width="150" align="left">${msg['mail.jsp.fujian']}</th>
                <th field="itemStr" width="200" align="left" formatter="formatItems">${msg['mail.jsp.fujian']}${msg['activity.login.mailcontent']}</th>
                <th field="createUser" width="150" align="left">${msg['announce.jsp.createUser']}</th>
                <th field="createDate" width="150" align="left">${msg['announce.jsp.createtime']}</th>
                <th field="adminUser" width="150" align="left">${msg['mail.jsp.adminUser']}</th>
                <th field="adminDate" width="150" align="left">${msg['mail.jsp.adminTime']}</th>
                <th field="sendErrorMess" width="150" align="left">${msg['mail.jsp.demo']}</th>
                <th field="stateStr" width="150" align="left">${msg['mail.jsp.mailState']}</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>
