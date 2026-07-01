<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>循环公告</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${base}/easyui/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="${base}/easyui/themes/default/easyui.css" type="text/css">
<link rel="stylesheet" href="${base}/easyui/themes/icon.css" type="text/css">
<script type="text/javascript">
    var base = "${base}";
    $(function () {
        reloadNoHefuServerGroupBox(false);
        loadAnnounce();
        setInterval(function () {
            loadAnnounce()
        }, 5000);

        $('.date').datetimepicker({
            language:  'zh-CN',
            format : 'yyyy-mm-dd hh:ii',
            weekStart: 1,
            todayBtn:  1,
            autoclose: 1,
            startView : 2,
            minView : 0
        });
    });
    function loadAnnounce() {
        paging(1);
    }

    function paging(page) {
        var pageSize = $("#pageSize").val().trim();
        $.ajax({
            url: base + "/announce/cycleAnnounceList",
            dataType: "json",
            data: {"pageNumber":page,"pageSize":pageSize},
            success: function (data) {
                console.log(data);
                if (data.ok) {
                    createAnnounceTable($("#enableList"), data,page);
                    // createAnnounceTable($("#disableList"), data.data.disableList);
                }
            }
        });
        $('#disableList').datagrid("loadData", {total:0, rows:[]});

        $("#disableList").datagrid("load", {

        });
    }

    function createAnnounceTable(div, data1,page) {
        $(div).empty();
        $("#pageUl").empty();
        $("#enable_count").html("共" + data1.qr.pager.recordCount + "个可用循环公告, 总计" + data1.qr.pager.pageCount + "页");
        var data=data1.qr.list;
        var tableHtml = "<table class=\"table table-bordered\">\n" +
            "            <tr>\n" +
            "                <th>${msg['announce.jsp.content']}</th>\n" +
            "                <th>${msg['announce.jsp.createtime']}</th>\n" +
            "                <th>${msg['announce.jsp.createUser']}</th>\n" +
            "                <th>${msg['announce.jsp.sendServers']}</th>\n" +
            "                <th>${msg['announce.jsp.begintime']}</th>\n" +
            "                <th>${msg['announce.jsp.endtime']}</th>\n" +
            "                <th>${msg['announce.jsp.totalTimes']}</th>\n" +
            "                <th>${msg['announce.jsp.currTimes']}</th>\n" +
            "                <th>${msg['announce.jsp.timeStep']}</th>\n" +
            "                <th>${msg['announce.jsp.nextFlushdate']}</th>\n" +
            "                <th>${msg['announce.jsp.state']}</th>\n" +
            "                <th>${msg['jsp.server.deal']}</th>\n" +
            "            </tr>";
        var operateHtml = "";
        for (var i = 0; i < data.length; i++) {
            if (data[i].state == 1) {
                operateHtml = "<a href='#' onclick='updateState(" + data[i].id + ", 0)'>${msg['announce.jsp.state1']}</a>";
            } else if (data[i].state == 0) {
                operateHtml = "<a href='#' onclick='updateState(" + data[i].id + ", 1)'>${msg['announce.jsp.state2']}</a>";
            } else if (data[i].state < 4) {
                operateHtml = "<a href='#' onclick='delAnnounce(" + data[i].id + ")'>${msg['jsp.server.del']}</a>";
            }
            tableHtml += "<tr><td>" + data[i].content + "</td>" +
                "<td>" + data[i].createDate + "</td>" +
                "<td>" + data[i].createUserName + "</td>" +
                "<td>" + data[i].serverIds + "</td>" +
                "<td>" + data[i].fromDate + "</td>" +
                "<td>" + data[i].toDate + "</td>" +
                "<td>" + data[i].totalTimes + "</td>" +
                "<td>" + data[i].nowTimes + "</td>" +
                "<td>" + data[i].cycleInterval + "</td>" +
                "<td>" + data[i].nextDate + "</td>" +
                "<td>" + data[i].stateStr + "</td>" +
                "<td>" + operateHtml + "</td></tr>"
        }
        tableHtml += "</table>";
        $(div).append(tableHtml);
        if (data.length == 0){
            return;
        }
        var data2 = data1.qr;
        pageQuery(data2,page);
    }

    function updateState(id, value) {
        if (confirm("${msg['announce.jsp.sureUpdate']}")) {
            $.post(base + "/announce/jYCyAnnounce",
                {announceId: id, state: value},
                function (data) {
                    loadAnnounce();
                    alert(data.msg);
                });
        }
    }

    function delAnnounce(id) {
        if (confirm("${msg['announce.jsp.sureDelete']}")) {
            $.post(base + "/announce/deleteCyAnnounce",
                {announceId: id},
                function (data) {
                    loadAnnounce();
                    alert(data.msg);
                });
        }
    }

    function showAddAnnounceModel() {
        $("#addAnnounceModel").modal('show');
    }

    function addCycleAnnounce(){
        if ($('#announceInfo').validationEngine('validate')) {
            $("#loadingmodal").modal({backdrop:'static',keyboard:false});
            $.post(base + "/announce/addAnnounce", $("#announceInfo").serialize(),
                function(data) {
                    $("#loadingmodal").modal('hide');
                    $("#addAnnounceModel").modal('hide');
                    if( data.ok){
                        $('#announceInfo')[0].reset();
                    }
                    alert(data.msg);
                }
            );
        }
    }
    //状态显示处理
    function formatState(value,row,index) {
        if (value == 0){
            return "${msg['announce.qiyong']}";
        }else if (value == 1){
            return "${msg['announce.stop']}";
        }else if (value == 2){
            return "${msg['announce.overtime']}";
        }else if (value == 3){
            return "${msg['announce.timesOver']}";
        }else if (value == 4){
            return "${msg['announce.isDelete']}";
        }
    }
    //操作显示处理
    function formatDeal(value,row,index) {
        var state=row.state;
        if (state == 1){
            return '<a href="#" onclick="updateState('+row.id+',0)">${msg['announce.jsp.state1']}</a>';
        }else if (state == 0){
            return '<a href="#" onclick="updateState('+row.id+',1)">${msg['announce.jsp.state2']}</a>';
        } else if (state < 4){
            return '<a href="#" onclick="delAnnounce('+row.id+')">${msg['jsp.server.del']}</a>';
        }
    }
</script>
</head>
<body>
<div class="container-fluid">
    <div class="well">
        <input type="button" class="btn btn-primary" value="${msg['announce.jsp.createAnnouce']}" onclick="showAddAnnounceModel()"/>
        <span for="pageSize">每页</span>
        <input type="text" name="pageSize" id="pageSize" value="10" class="span1"/>
        <input type="button" id="query_btn" value="查询" class="btn btn-primary" onclick="loadAnnounce()">
    </div>
    <p id="enable_count"></p>
    <label class="label label-info">${msg['announce.jsp.openlist']}</label>
    <div id="enableList">
        <table class="table table-bordered">
            <tr>
                <th>${msg['announce.jsp.content']}</th>
                <th>${msg['announce.jsp.createtime']}</th>
                <th>${msg['announce.jsp.createUser']}</th>
                <th>${msg['announce.jsp.sendServers']}</th>
                <th>${msg['announce.jsp.begintime']}</th>
                <th>${msg['announce.jsp.endtime']}</th>
                <th>${msg['announce.jsp.totalTimes']}</th>
                <th>${msg['announce.jsp.currTimes']}</th>
                <th>${msg['announce.jsp.timeStep']}</th>
                <th>${msg['announce.jsp.nextFlushdate']}</th>
                <th>${msg['announce.jsp.state']}</th>
                <th>${msg['jsp.server.deal']}</th>
            </tr>
        </table>
    </div>
    <div class="pagination" id="pageUl"></div>
    <label class="label label-info">${msg['announce.jsp.closeList']}</label>
    <table id="disableList" class="easyui-datagrid" style="width:1650px;height:auto" data-options="url:'${base}/announce/cycleAnnouncelist',fitColumns:true,nowrap:false,striped:true,rownumbers:true,singleSelect:true,pagination:true,pageSize:20,pageList:[20,30,50,80]">
        <thead>
        <tr>
            <th field="id" width="0" hidden="hidden" align="left"></th>
            <th field="content" width="350" align="left">${msg['announce.jsp.content']}</th>
            <th field="createDate" width="120" align="left">${msg['announce.jsp.createtime']}</th>
            <th field="createUserName" width="60" align="left">${msg['announce.jsp.createUser']}</th>
            <th field="serverIds" width="250" align="left">${msg['announce.jsp.sendServers']}</th>
            <th field="fromDate" width="120" align="left">${msg['announce.jsp.begintime']}</th>
            <th field="toDate" width="120" align="left">${msg['announce.jsp.endtime']}</th>
            <th field="totalTimes" width="60" align="left">${msg['announce.jsp.totalTimes']}</th>
            <th field="nowTimes" width="60" align="left">${msg['announce.jsp.currTimes']}</th>
            <th field="cycleInterval" width="120" align="left">${msg['announce.jsp.timeStep']}</th>
            <th field="nextDate" width="120" align="left">${msg['announce.jsp.nextFlushdate']}</th>
            <th field="state" width="60" align="left" formatter="formatState">${msg['announce.jsp.state']}</th>
            <th field="action" width="100" align="left" formatter="formatDeal">${msg['jsp.server.deal']}</th>
        </tr>
        </thead>
    </table>
</div>

<div id="addAnnounceModel" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="modelTile">新增公告</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <form id="announceInfo" action="#" method="POST" class="form form-horizontal">
                <div class="control-group">
                    <label class="control-label" for="content">${msg['jsp.server.groupname']}</label>
                    <div class="controls">
                        <select id="select_group" name="groupName" onchange="reloadNoHefuServerBox(this.value, false)"></select>
                        <input type="checkbox" id="selectAll" onchange="selectAllSid();">${msg['server.all.choice']}
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="content">${msg['jsp.server.name']}</label>
                    <div class="controls">
                        <div id="checkbox_server" class="well"></div>
                    </div>
                </div>

                <div class="control-group">
                    <label for="type" class="control-label">${msg['announce.jsp.type']}</label>
                    <div class="controls">
                        <select id="type" name="type">
                            <option value="1">${msg['announce.jsp.type1']}</option>
                            <option value="2">${msg['announce.jsp.type2']}</option>
                        </select>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="content">${msg['announce.jsp.content']}</label>
                    <div class="controls">
                        <textarea name="content" rows="7" id="content" class="validate[required]"></textarea>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">${msg['announce.jsp.begintime']}</label>
                    <div class="controls">
                        <div class="input-append date" id="fromDate">
                            <input style="width: 120px;" size="20" type="text" name="fromDate" value="${nowDate}"  readonly>
                            <span class="add-on"><i class="icon-th"></i></span>
                        </div>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">${msg['announce.jsp.endtime']}</label>
                    <div class="controls">
                        <div class="input-append date" id="toDate">
                            <input style="width: 120px;" size="20" name="toDate" type="text" value="${nowDate}"  readonly>
                            <span class="add-on"><i class="icon-th"></i></span>
                        </div>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">${msg['announce.jsp.playTimes']}</label>
                    <div class="controls">
                        <input type="text" name="totalTimes" class="input-medium validate[required],custom[integer]" placeholder="必填"/>
                    </div>
                </div>

                <div class="control-group">
                    <label for="cycleInterval" class="control-label">${msg['announce.jsp.timeStep']}</label>
                    <div class="controls">
                        <select id="cycleInterval" name="cycleInterval" class="input-mini">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="5" selected>5</option>
                            <option value="10">10</option>
                            <option value="15">15</option>
                            <option value="20">20</option>
                            <option value="30">30</option>
                            <option value="60">60</option>
                        </select>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="modal-footer">
        <input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
        <input id="addMenu" type="button" value="添加" onclick="addCycleAnnounce()" class="btn btn-primary"/>
    </div>
</div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>