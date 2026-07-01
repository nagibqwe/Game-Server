<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>角色列表界面</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/zTreeStyle.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-confirmation.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var base = '${base}';

    $(function () {
        paging(1);
    });

    function paging(page) {
        var pageSize = $("#pageSize").val().trim();
        $.ajax({
            url: base + "/backrole/roleList",
            data: {"pageNumber":page,"pageSize":pageSize},
            method: "post",
            dataType: "json",
            success: function (data) {
                var data=data.qr;
                $("#role_count").html("共" + data.pager.recordCount + "个角色, 总计"+ data.pager.pageCount + "页");
                var tableHtml = "<table class='table table-bordered table-striped'>" +
                    "<thead><tr><th>角色编号</th><th>角色名称</th><th>创建时间</th><th>是否禁用</th><th>操作</th></tr></thead><tbody>";
                for (var i = 0; i < data.list.length; i++) {
                    var role = data.list[i];
                    tableHtml += "<tr><td>" + role.roleId + "</td>" +
                        "<td>" + role.roleName + "</td>" +
                        "<td>" + role.createTime + "</td>" +
                        "<td>" + (role.isDeleted == 1 ? "是" : "否") + "</td>" +
                        "<td><button class='btn btn-info' onclick='showRoleInfoModel(" + role.roleId + ")'>修改</button>&nbsp;&nbsp;" +
                        "<button class='btn btn-warning' onclick='assign_menu(" + role.roleId + ",\"" + role.roleName + "\")'>分配权限</button></td>"
                }
                tableHtml += "</tbody></table>";
                $("#roleList").html(tableHtml);
                pageQuery(data,page);
            }
        });
    }

    function showRoleInfoModel(roleId) {
        $("#role_form")[0].reset();
        $("#roleInfoModel").modal('show');
        var update = roleId != 0;
        var modelTitle = update ? "修改角色" : "添加角色";
        $("#modelTitle").text(modelTitle);
        if (update) {
            $("#isDeleted_group").show();
            $("#addRole").hide();
            $("#updateRole").show();
            $.ajax({
                url: base + "/backrole/getRoleInfoById",
                data: {
                    roleId: roleId
                },
                method: "post",
                dataType: "json",
                success: function (data) {
                    var role = data.data;
                    $("#roleId").val(role.roleId);
                    $("#roleName").val(role.roleName);
                    $("#description").val(role.description);
                    console.log($("[name='isDeleted']").length)
                    $("[name='isDeleted'][value='" + role.isDeleted + "']").prop("checked", true);
                }
            });
        } else {
            $("#isDeleted_group").hide();
            $("#updateRole").hide();
            $("#addRole").show();
        }
    }

    function addRole() {
        var roleName = $("#roleName").val();
        var description = $("#description").val();
        if (roleName == "") {
            alert("系统提示：角色名不能为空！");
            return;
        }

        $.ajax({
            url: base + "/backrole/addRole",
            data: {
                roleName: roleName,
                description: description
            },
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#roleInfoModel").modal('hide');
                var flag = data.flag;
                if (flag == 1) {
                    alert("系统提示：角色名已经存在！");
                }
                if (flag == 2) {
                    alert("系统提示：角色添加成功！");
                    reload_role();
                }
                if (flag == 0) {
                    alert("系统提示：角色添加失败！");
                }
            }
        });
    }

    function updateRole() {
        $.ajax({
            url: base + "/backrole/updateRoleInfoById",
            data: {
                roleId: $("#roleId").val(),
                roleName: $("#roleName").val(),
                description: $("#description").val(),
                isDeleted: $("#isDeleted").val()
            },
            method: "post",
            dataType: "json",
            success: function (data) {
                $("#roleInfoModel").modal('hide');
                if (data.ok) {
                    reload_role();
                    window.parent.leftFrame.location.reload();
                } else {
                    alert("系统提示：角色信息修改失败！");
                }
            }
        });
    }

    function assign_menu(roleId, roleName) {
        $("#assignMenu").modal('toggle');
        $("#assign_menu_tile").html("为" + roleName + "分配菜单权限");
        $("#role_assign_menu input[name='roleId']").val(roleId);

        var setting = {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };
        var zNodes = "";
        $.ajax({
            url: base + "/backrole/getMenuListData",
            data: {
                roleId: roleId
            },
            method: "post",
            async: false,
            dataType: "json",
            success: function (data) {
                zNodes = data.data;
            }
        });
        $(document).ready(function () {
            $.fn.zTree.init($("#treeMenu"), setting, zNodes);
            var zTree = $.fn.zTree.getZTreeObj("treeMenu");
            zTree.setting.check.chkboxType = {"Y": "ps", "N": "ps"};
        });
    }

    function submitAssign() {
        var roleId = $("#role_assign_menu input[name='roleId']").val();
        var treeObj = $.fn.zTree.getZTreeObj("treeMenu");
        var nodes = treeObj.getCheckedNodes(true);
        var menuIds = [];
        for (var i = 0; i < nodes.length; i++) {
            menuIds.push(nodes[i].id);
        }
        $.ajax({
            url: base + "/backrole/reAssignMenu",
            data: {
                roleId: roleId,
                menuIds: menuIds + ","
            },
            method: "post",
            async: false,
            dataType: "json",
            success: function (data) {
                if (data.ok) {
                    alert("系统提示：权限分配成功！");
                    $("#assignMenu").modal('hide');
                } else {
                    alert("系统提示：权限分配失败！");
                }
            }
        });
    }
</script>
</head>
<body>
<div class="container-fluid">
    <button class="btn btn-primary" onclick="showRoleInfoModel(0);">添加角色</button>
    <span>每页</span><input type="text" name="pageSize" id="pageSize" value="10" class="span1"/>
    <input type="button" value="查询" onclick="paging(1)" class="btn btn-primary">
    <p id="role_count"></p>
    <div id="roleList"></div>
    <div class="pagination" id="pageUl"></div>
    <div id="roleInfoModel" class="modal hide fade">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3 id="modelTitle">添加角色</h3>
        </div>
        <div class="modal-body">
            <form id="role_form" class="form-horizontal">
                <fieldset>
                    <input id="roleId" name="roleId" type="hidden"/>
                    <div class="control-group">
                        <label for="roleName" class="control-label">角色名称：</label>
                        <div class="controls">
                            <input id="roleName" name="roleName" type="text"/>
                            <p class="help-block"></p>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="description" class="control-label">角色描述：</label>
                        <div class="controls">
                            <div class="textarea">
                                <textarea id="description" placeholder='角色描述' name="description" rows="3"></textarea>
                            </div>
                        </div>
                    </div>
                    <div id="isDeleted_group" class="control-group">
                        <label class="control-label">是否禁用：</label>
                        <div class="controls">
                            <label class="radio inline"><input name="isDeleted" type="radio" value="1"/>是</label>
                            <label class="radio inline"><input name="isDeleted" type="radio" value="0"/>否</label>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            <button id="addRole" type="button" onclick="addRole();" class="btn btn-primary">添加</button>
            <button id="updateRole" type="button" onclick="updateRole();" class="btn btn-primary">修改</button>
        </div>
    </div>

    <div id="assignMenu" class="modal hide fade">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3 id="assign_menu_tile">权限分配</h3>
        </div>
        <div class="modal-body">
            <form class="form-horizontal" id="role_assign_menu">
                <fieldset>
                    <input name="roleId" type="hidden" value="">
                    <div class="control-group">
                        <div class="zTreeDemoBackground left">
                            <ul id="treeMenu" class="ztree"></ul>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            <button type="button" onclick="submitAssign();" class="btn btn-primary">确认</button>
        </div>
    </div>
</div>
</body>
</html>
