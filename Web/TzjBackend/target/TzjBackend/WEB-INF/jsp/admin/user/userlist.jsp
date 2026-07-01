<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/zTreeStyle.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
	var pageNumber = 1;
	var pageSize = 10;
	var base = '${base}';
	var userId ='${USER.id}';
	var curLang = '${USER.language}';
	var langMap = ${langMap};

	$(function() {
		initLang();
		user_reload();
	});

	function initLang() {
		var lang=$("#lang");
		for ( var key in langMap) {
			var op1=$("<option>").val(key).text(langMap[key]);
			if(key==curLang){
				op1.attr("selected","selected");
			}
			lang.append(op1);
		}
	}

	function user_reload() {
        paging(1);

	}
	function paging(page) {
	    var name = $("#name").val().trim();
	    var pageSize = $("#pageSize").val().trim();
        $.ajax({
            type : "POST",
            url : base + "/user/query",
            data : {"name":name,"pageNumber":page,"pageSize":pageSize},
            dataType : "json",
            success : function(data) {
                $("#pageUl").empty();
                var roles = data.roles;
                var data = data.qr;

                $("#user_count").html("共" + data.pager.recordCount + "个用户, 总计"+ data.pager.pageCount + "页");
                var list_html = "<table class='table table-bordered table-striped'>" +
                    "<tr><th>用户ID</th><th>用户名称</th><th>创建时间</th><th>修改时间</th><th>登录IP</th><th>用户语言</th><th>是否生效</th><th>操作</th></tr>";
                for (var i = 0; i < data.list.length; i++) {
                    var user = data.list[i];
                    console.log(user);
                    var tmp = "<tr><td>" + user.id + "</td>" +
                        "<td>" + user.name + "["+roles[i]+"]</td>" +
                        "<td>" + user.createTime + "</td>" +
                        "<td>" + user.updateTime + "</td>"
                        + "<td>" + user.ip + "</td>"
                        + "<td>" + user.language + "</td>"
                        + "<td>" + (user.isDeleted == 1 ? "禁用" : "启用") + "</td>"
                        + "<td><button class='btn btn-warning' onclick='user_update("+ user.id + ");'>修改密码</button> "
                        + "<button class='btn btn-info' onclick=assign_role("+ user.id + ",'"+user.name+"');>分配角色</button> "
                        + "<button class='btn btn-danger' onclick='user_delete("+ user.id + ","+user.isDeleted+");'>" + (user.isDeleted == 0 ? "禁用" : "启用") + "</button></td> "
                        + "</tr>";
                    list_html += tmp;
                }
                list_html += "</table>";
                $("#user_list").html(list_html);
                pageQuery(data,page);
            }
        });
    }


	var userNameCheck=false;
	var passwordCheck = false;

	//验证输入的用户名是否为空
	function checkUserName(){
		var username = $("input[name='username']").val();
		if(username == ""){
			$("#checkUserName").html("${msg['jsp.login.nullusername']}");
			userNameCheck=false;
		}else{
			$("#checkUserName").html("");
			userNameCheck=true;
		}
	}

	//验证输入的密码是否为空，而且密码必须要12位以上，必须包含数字、大小写字母及特殊符号。
	function checkPassword(password){
		passwordCheck = true;
		<%--var numasc = 0;  //数字的个数--%>
        <%--var charasc = 0;  //字母的个数--%>
        <%--var otherasc = 0; //特殊符号的个数--%>
		<%--if(password == "" || password.length == 0){--%>
		<%--	alert("${msg['jsp.login.nullpassword']}");--%>
		<%--	passwordCheck=false;--%>
		<%--}else if(password.length < 12){--%>
		<%--	alert("${msg['jsp.login.minpasswordlength']}");--%>
		<%--	passwordCheck=false;--%>
		<%--}else{--%>
		<%--	 for (var i = 0; i < password.length; i++) {--%>
        <%--        var asciiNumber = password.substr(i, 1).charCodeAt();--%>
        <%--        if (asciiNumber >= 48 && asciiNumber <= 57) {--%>
        <%--            numasc += 1;--%>
        <%--        }--%>
        <%--        if ((asciiNumber >= 65 && asciiNumber <= 90)||(asciiNumber >= 97 && asciiNumber <= 122)) {--%>
        <%--            charasc += 1;--%>
        <%--        }--%>
        <%--        if ((asciiNumber >= 33 && asciiNumber <= 47)||(asciiNumber >= 58 && asciiNumber <= 64)||(asciiNumber >= 91 && asciiNumber <= 96)||(asciiNumber >= 123 && asciiNumber <= 126)) {--%>
        <%--            otherasc += 1;--%>
        <%--        }--%>
        <%--    }--%>
        <%--    if(0==numasc)  {--%>
        <%--    	alert("${msg['jsp.login.passwordnofigure']}");--%>
        <%--    	passwordCheck=false;--%>
        <%--    }else if(0==charasc){--%>
        <%--    	alert("${msg['jsp.login.passwordnoletter']}");--%>
        <%--    	passwordCheck=false;--%>
        <%--    }else if(0==otherasc){--%>
        <%--    	alert("${msg['jsp.login.passwordnocharacter']}");--%>
        <%--    	passwordCheck=false;--%>
        <%--    }else{--%>
        <%--    	passwordCheck=true;--%>
        <%--    }--%>
		<%--}--%>
	}

	function showUserAdd() {
		$("#addUser").modal('show');
	}

	function user_add() {
		if(!$("#user_add_form").validationEngine('validate')){
			return;
		}
		if(userNameCheck && passwordCheck){
			var username = $("input[name='username']").val();
			var password = $("input[name='password']").val();
			var language = $("#lang").find("option:selected").val();
			$.ajax({
				url : base + "/user/add",
				data : {
					"name":username,
					"password":password,
					"language":language
				},
				dataType : "json",
				success : function(data) {
					if (data.ok) {
						user_reload();
						alert("添加成功");
					} else {
						alert(data.msg);
					}
					$("#addUser").modal('hide');
				}
			});
		}
	}

	function user_update(id) {
		var passwd = prompt("请输入新的密码,密码不少于12个字符,必须包括字母、数字以及特殊符号");
		checkPassword(passwd);
		if(passwordCheck){
			if (passwd) {
				$.ajax({
					url : base + "/user/update",
					data : {
						"id" : id,
						"password" : passwd
					},
					dataType : "json",
					success : function(data) {
						alert(data.msg);
						if (data.ok) {
							if(userId==id){
								top.location.href="${base}/user/logout";
								return;
							}
							user_reload();
						}
					}
				});
			}
		}
	}

	function user_delete(id,isDeleted) {
		if(id == userId){
			alert("系统提示：不能禁用自己！");
			return;
		}
		var prompt_html = "";
		if (isDeleted == 1) {
			isDeleted = 0;
			prompt_html = "确定启用此用户？";
		} else {
			isDeleted = 1;
			prompt_html = "确定禁用此用户？";
		}
		if(confirm(prompt_html)){
			$.ajax({
				url : base + "/user/delete",
				data : {
					id : id,
					isDeleted : isDeleted
				},
				dataType : "json",
				success : function(data) {
					if (data.ok) {
						user_reload();
						alert("操作成功！");
					} else {
						alert("操作失败！");
					}
				}
			});
		}
	}

	function assign_role(user_id,user_name){
		$("#assignRole").modal('toggle');
		$("#assign_role_tile").html("为用户"+user_name+"分配角色");
		$("#role_assign_update input[name='userId']").val(user_id);
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

		var zNodes ="";
		$.ajax({
			url : base + "/backrole/getRoleListData",
			data : {
				userId : user_id
			},
			method : "post",
			async : false,
			dataType : "json",
			success : function(data) {
				zNodes = data.data;
			}
		});
		$(document).ready(function(){
			$.fn.zTree.init($("#treeRole"), setting, zNodes);
			var zTree = $.fn.zTree.getZTreeObj("treeRole");
			zTree.setting.check.chkboxType = {"Y": "ps", "N": "ps"};
		});

	}

	function submitAssign(){
		var userId = $("#role_assign_update input[name='userId']").val();
		var treeObj=$.fn.zTree.getZTreeObj("treeRole");
        var nodes=treeObj.getCheckedNodes(true);
        var roleIds = [];
        for(var i=0;i<nodes.length;i++){
        	roleIds.push(nodes[i].id);
		}
		$.ajax({
			url : base + "/backrole/reAssignRole",
			data : {
				userId : userId,
				roleIds : roleIds + ","
			},
			method : "post",
			async : false,
			dataType : "json",
			success : function(data) {
				if(data.ok){
					alert("系统提示：角色分配成功！");
					user_reload();
					$("#assignRole").modal('hide');
				}else{
					alert("系统提示：角色分配失败！");
				}
			}
		});
	}
</script>
</head>
<body>
<div class="container-fluid">
	<form action="#" id="user_query_form" class="well form-inline">
		<label class="label">用户名称</label><input type="text" name="name" id="name" class="span2"/>
		<%--<label class="label">页数</label><input type="text" name="pageNumber" value="1" class="span1"/>--%>
		<label class="label">每页</label><input type="text" name="pageSize" id="pageSize" value="10" class="span1"/>
		<input type="button" value="查询" onclick="user_reload()" class="btn btn-primary">
		<input type="button" value="添加用户" onclick="showUserAdd()" class="btn btn-primary">
	</form>
	<p id="user_count"></p>
	<div id="user_list"></div>
	<div class="pagination" id="pageUl"></div>

	<div id="addUser" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3>增加用户</h3>
		</div>
		<div class="modal-body">
			<form action="#" id="user_add_form" class="form-horizontal">
				<div class="control-group">
					<label for="userName" class="control-label">用户名称</label>
					<div class="controls">
						<input type="text" id="userName" name="username" class="validate[required,minSize[2]]" onblur="checkUserName();">
						<span style="color:#b94a48" id="checkUserName"></span>
					</div>
				</div>

				<div class="control-group">
					<label class="control-label">用户密码</label>
					<div class="controls">
						<input type="password" name="password" class="validate[required,minSize[2]]" onblur="checkPassword(this.value);" >
						<span style="color:#b94a48" id="checkPassword"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">语言选择</label>
					<div class="controls">
						<select id="lang" name="language"></select>
					</div>
				</div>
			</form>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="user_add()">新增用户</button>
		</div>
	</div>

	<div id="assignRole" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 id="assign_role_tile">角色分配</h3>
		</div>
		<div class="modal-body">
			<form class="form-horizontal" id="role_assign_update">
				<fieldset>
					<input name="userId" type="hidden">
					<div class="control-group">
						<div class="zTreeDemoBackground left">
							<ul id="treeRole" class="ztree"></ul>
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