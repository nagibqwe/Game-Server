<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.menu.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-confirmation.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
	var base = '${base}';
	$(function() {
		menu_reload();
		$("#menu_update_btn").confirmation({
			placement:"left",
			title:"${msg['jsp.menu.sure']}",
			btnOkLabel:"<i class='icon-ok-sign icon-white'></i> ${msg['jsp.menu.yes']}",
			btnCancelLabel:"<i class='icon-remove-sign'></i> ${msg['jsp.menu.no']}",
			onConfirm: function() {
				menu_update();
			}
		});
	});

	function menu_reload() {
	    paging(1);
    }
    function paging(page){
        var pageSize = $("#pageSize").val().trim();
		$.ajax({
			type : "POST",
			url : base + "/menu/query",
			data : {
				"menuName": $("#queryMenuName").val(),
                "pageNumber":page,
                "pageSize":pageSize
			},
			dataType : "json",
			success : function(data) {
                $("#pageUl").empty();
                var data=data.qr;
                $("#menu_count").html("共" + data.pager.recordCount + "个菜单, 总计"+ data.pager.pageCount + "页");
				var list_html = "<table class='table table-bordered table-striped'>" +
						"<tr><th>菜单ID</th><th>菜单名称</th><th>菜单等级</th>" +
						"<th>父级菜单</th><th>菜单别名</th><th>访问路径</th>" +
						"<th>菜单描述</th><th>是否生效</th><th>操作</th></tr>";
				for (var i = 0; i < data.list.length; i++) {
					var menu = data.list[i];
					var tmp = "<tr><td>" + menu.menuId + "</td>" +
							"<td>" + menu.menuName + "</td>" +
							"<td>" + menu.level + "</td>" +
							"<td>" + menu.parentId + "</td>" +
							"<td>" + menu.alias + "</td>"  +
							"<td>" + menu.urlPath + "</td>" +
							"<td>" + menu.description + "</td>" +
							"<td>" + (menu.isDeleted == 1 ? "无效" : "生效") + "</td>" +
							"<td><button class='btn btn-warning' onclick='queryMenu(" + menu.menuId + ");'>修改</button> " +
							"<button class='btn btn-danger' onclick='menu_delete(" + menu.menuId + ");'>删除</button></td></tr>";
					list_html += tmp;
				}
				list_html += "</table>";
				$("#menu_list").html(list_html);
                pageQuery(data,page);
			}
		});
    }
	/**
     * 查询菜单用于修改
     */
	function queryMenu(menuId) {
		$.ajax({
			type : "POST",
			url : base + "/menu/singleMenu",
			data : {
				"menuId" : menuId
			},
			dataType : "json",
			success : function(data) {
			    showMenuInfo(true, data);
			}
		});
	}

    /**
     * 改变菜单等级事件
     */
	function changeLevel(level) {
		switch (level) {
            case "1":
                $("#parentId").val(0);
                $("#parentMenu").hide();
                $("#urlPath").val("");
                $("#url").hide();
                break;
            case "2":
                getParentMenu(level);
                $("#parentMenu").show();
                $("#urlPath").val("");
                $("#url").hide();
                break;
            case "3":
                getParentMenu(level);
                $("#parentMenu").show();
                $("#url").show();
                break;
            default:
                break;
		}
	}

	/**
     * 根据菜单等级父级菜单名
     */
	function getParentMenu(level, parentId) {
		$.ajax({
			url : base + "/menu/parentMenu",
			data : {
				"level" : level
			},
			dataType : "json",
			success : function(data) {
				var select = $("#parentId");
				select.empty();
				for (var i = 0; i < data.length; i++) {
					var menu = data[i];
					var option = $("<option>").val(menu.menuId).text(menu.menuName);
					if (menu.menuId == parentId) {
					    option.prop("selected", select);
                    }
                    select.append(option);
                }
			}
		});
	}

    /**
     * 显示增加菜单、修改菜单模态框
     * @param update 是否是修改
     * @param data   菜单数据
     */
	function showMenuInfo(update, data) {
	    console.log(data);
	    $("#menu_form")[0].reset();
	    var title = update ? "修改菜单" : "新增菜单";
	    var submitText = update ? "修改" : "增加";
	    $("#modelTile").text(title);
        $("#submit").text(submitText);
        if (update) {
            $("#menuId").val(data.menuId);
            $("#menuName").val(data.menuName);
            $("#urlPath").val(data.urlPath);
            $("#description").val(data.description);
            changeLevel(data.level + "");
            $("#menuLevel option[value='" + data.level + "']").prop("selected", "selected");
            $("[name='isDeleted'][value='" + data.isDeleted +"']").prop("checked", true);
            getParentMenu(data.level, data.parentId);
            $("#addMenu").hide();
            $("#updateMenu").show();
        } else {
            changeLevel("3");
            getParentMenu(3, null);
            $("#addMenu").show();
            $("#updateMenu").hide();
        }
        $("#menuInfo").modal('show');
    }

    /**
     * 添加菜单
     */
    function addMenu() {
        $.ajax({
            type : "POST",
            url : base + "/menu/add",
            data : $("#menu_form").serialize(),
            dataType : "json",
            success : function(data) {
                if (data.ok) {
                    $("#menuInfo").modal('hide');
                    menu_reload();
                    alert("${msg['jsp.menu.addsuccess']}");
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    /**
     * 修改菜单
     */
    function updateMenu() {
        $.ajax({
            type : "POST",
            url : base + "/menu/update",
            data :$("#menu_form").serialize(),
            dataType : "json",
            success : function(data) {
                if (data.ok) {
                    $("#menuInfo").modal('hide');
                    menu_reload();
                    window.parent.leftFrame.location.reload();
                    alert("修改成功");
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    /**
     * 删除菜单
     * @param menuId 菜单ID
     */
    function menu_delete(menuId) {
        if (confirm("${msg['jsp.server.delConfirm']}")){
                $.ajax({
                    url : base + "/menu/delete",
                    data : {
                        "menuId" : menuId
                    },
                    dataType : "json",
                    success : function(data) {
                        if (data.ok) {
                            menu_reload();
                            window.parent.leftFrame.location.reload();
                            alert("删除成功");
                        } else {
                            alert(data.msg);
                        }
                    }
                });
        }
    }
</script>
</head>
<body>
<div class="container-fluid">
	<form class="well form-inline">
		<label for="queryMenuName" class="label">菜单名称</label>
		<input id="queryMenuName" type="text"/>
        <span>每页</span><input type="text" name="pageSize" id="pageSize" value="10" class="span1"/>
		<input type="button" class="btn btn-info" value="查询" onclick="menu_reload();"/>
        <input type="button" value="增加菜单" onclick="showMenuInfo(false, null)" class="btn btn-primary"/>
	</form>
    <p id="menu_count"></p>
	<div id="menu_list"></div>
    <div class="pagination" id="pageUl"></div>
	<div id="menuInfo" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="modelTile">新增菜单</h3>
		</div>
        <div class="modal-body">
            <div class="row-fluid">
                <form id="menu_form" action="#" method="post" class="form-horizontal">
                    <input id="menuId" type="hidden" name="menuId"/>
                    <div class="control-group">
                        <label for="menuName" class="control-label">菜单名称</label>
                        <div class="controls">
                            <input type="text" id="menuName" name="menuName"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label for="urlPath" class="control-label">菜单等级</label>
                        <div class="controls">
                            <select id="menuLevel" name="level" onchange="changeLevel(this.value);">
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3" selected>3</option>
                            </select>
                        </div>
                    </div>


                    <div id="parentMenu" class="control-group">
                        <label for="parentId" class="control-label">父级菜单</label>
                        <div class="controls">
                            <select id="parentId" name="parentId"></select>
                        </div>
                    </div>


                    <div id="url" class="control-group">
                        <label for="urlPath" class="control-label">访问路径</label>
                        <div class="controls">
                            <input type="text" id="urlPath" name="urlPath"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label for="urlPath" class="control-label">菜单描述</label>
                        <div class="controls">
                            <textarea id="description" rows="3" name="description"></textarea>
                        </div>
                    </div>

                    <div class="control-group">
                        <label  class="control-label">是否生效</label>
                        <div class="controls">
                            是<input type="radio" name="isDeleted" value="0" checked/>
                            否<input type="radio" name="isDeleted" value="1"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
		<div class="modal-footer">
			<input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
			<input id="addMenu" type="button" value="添加" onclick="addMenu()" class="btn btn-primary"/>
            <input id="updateMenu" type="button" value="修改" onclick="updateMenu()" class="btn btn-primary"/>
		</div>
	</div>
</div>
</body>
</html>