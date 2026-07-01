<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.role.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript">
	var base = '${base}';

	$(function() {
		group_reload();
		$("#loading").shCircleLoader();
		//禁止回车提交表单
		$('.forbidSubmit').keypress(function(e){
	        if(e.keyCode == 13){
	            e.preventDefault();
	        }
	    });
	});

	function query() {
		if(!$("#role_query_form").validationEngine('validate')){
			return ;
		}
		if(($("#select_queryType").val()=="3")||($("#select_queryType").val()=="5")){
			if (isNaN($("#queryString").val())) {
				alert("${msg['jsp.role.pleasetypenum']}");
				return;
			}
		}
		$("#loadingmodal").modal({backdrop:'static',keyboard:false});

		$.ajax({
			type : "POST",
			url : base + "/role/query",
			data : $("#role_query_form").serialize(),
			dataType : "json",
			success : function(data) {
				$("#msg").empty();
				$("#role_list").empty();
				$("#account_list").empty();
				$("#loadingmodal").modal('hide');
				if (!data.ok) {
					$("#msg").html(data.msg);
					return;
				}
				if (!data.hasOwnProperty("roles")) {
					createAccountTable(data.accounts);
				} else if (!data.hasOwnProperty("accounts")) {
					createRoleTable(data.roles);
				} else {
					createAccountTable(data.accounts);
					createRoleTable(data.roles);
				}
			}
		});
	}

	function createRoleTable(roles) {
		var table = $("<table>").attr("class", "table table-bordered");
		var caption = $("<caption>").text("${msg['jsp.role.roleinfo']}");
		table.append(caption);
		var thead = $("<thead>");
		var htr = $("<tr>");
		var fields = [ "${msg['jsp.role.userid']}","${msg['jsp.role.roleid']}",
				"${msg['jsp.role.rolename']}", "${msg['jsp.role.server']}",
				"${msg['jsp.role.rechargegold']}", "${msg['jsp.role.ct']}",
				"${msg['jsp.role.isdelete']}", "${msg['jsp.role.fcaccount']}",
				"${msg['jsp.role.channel']}","${msg['jsp.role.moonCardDay']}",
				"${msg['jsp.role.action']}"];
		for (var field in fields) {
			var th = $("<th>").text(fields[field]);
			htr.append(th);
		}
		thead.append(htr);
		table.append(thead);
		console.log(roles);
		var list_html = "<tbody>";
		for (var i = 0; i < roles.length; i++) {
			var role = roles[i];
			var tmp = "<tr><td>" + role.userId + "</td>"
					+ "<td>" + role.roleId  + "</td>"
					+ "</td><td>" + role.roleName + "</td>"
					+ "<td>" + role.createsid + "</td>"
					+ "<td>" + role.rechargeGold + "</td>"
					+ "<td>" + role.createTime + "</td>"
					+ "<td>" + isDeleted(role.isDelete) + "</td>"
					+ "<td>" + role.funcellUUid + "</td>"
					+ "<td>" + role.platformName + "</td>"
					+ "<td>" + role.moonCardDay + "</td>"
					+ "<td><input type='button' value='${msg['jsp.role.showdetail']}' class='btn btn-info'\
						onclick='roleDetail(\"" + $("#select_group").val() + "\"," + $("#select_server").val() + ",\"" + role.roleId + "\");'></td></tr>";
			list_html += tmp;
		}
		list_html += "</tbody>";
		table.append(list_html);
		$("#role_list").html(table);
	}

	function createAccountTable(accounts) {
		var table = $("<table>").attr("class", "table table-bordered");
		var caption = $("<caption>").text("${msg['jsp.role.acinfo']}");
		table.append(caption);
		var thead = $("<thead>");
		var htr = $("<tr>");
		var fields = [ "${msg['jsp.role.acname']}", "${msg['jsp.role.acid']}",
						"${msg['jsp.role.acpfaccount']}","${msg['jsp.role.acplatform']}", 
						"${msg['jsp.role.acct']}","${msg['jsp.role.aclt']}", 
						"${msg['jsp.role.acip']}","${msg['jsp.role.mac']}",
						"${msg['jsp.role.imei']}","${msg['jsp.role.machineCode']}"];
		for (var field in fields) {
			var th = $("<th>").text(fields[field]);
			htr.append(th);
		}
		thead.append(htr);
		table.append(thead);
		
		var list_html = "<tbody>";
		for (var i = 0; i < accounts.length; i++) {
			var account = accounts[i];
			var tmp = "<tr><td>" + account.userName + "</td><td>"
					+ account.userid + "</td><td>"
					+ account.platformAccount + "</td><td>"
					+ account.platformName + "</td><td>"
					+ TimeObjectUtil.UnixToDate(account.createTime) + "</td><td>" 
					+ TimeObjectUtil.UnixToDate(account.time)+ "</td><td>" 
					+ account.lastLoginIp + "</td><td>"
					+ account.mac + "</td><td>"
					+ account.imei + "</td><td>"
					+ account.machineCode + "</td></tr>";
			list_html += tmp;
		}
		list_html += "</tbody>";
		table.append(list_html);
		$("#account_list").html(table);
	}

	function isDeleted(value) {
		if (value == 0) {
			return "${msg['jsp.role.noout']}";
		}
		return "${msg['jsp.role.logouttime']}："+TimeObjectUtil.UnixToDate(value);
	}

	function roleDetail(groupName, serverId, roleId) {
		window.location.href = base + "/rolelog/roledetail?groupName=" + groupName + "&serverId=" + serverId + "&roleId=" + roleId+"&menuId=80";
	}

</script>
</head>
<body>
	<div class="container-fluid">
		<br/>
		<form id="role_query_form" class="well form-inline">
			<select id="select_group" name="groupName" onchange="queryServerByGroup(this.value)" class="span2"></select>
			<select id="select_server" name="serverId" class="span2"></select>
			${msg['jsp.role.searchcon']} 
			<select id="select_queryType" name="queryType" style="width: 120px;">
				<option value="1">${msg['jsp.role.rolename']}</option>
				<option value="2">${msg['jsp.role.account']}</option>
				<option value="3">${msg['jsp.role.roleidIn10']}</option>
				<option value="4">${msg['jsp.role.roleidIn36']}</option>
				<option value="5">${msg['jsp.role.userid']}</option>
				<option value="6">${msg['jsp.role.acpfaccount']}</option>
			</select>
			<input id="queryString" name="queryString" type="text" class="span2 validate[required] forbidSubmit"/>
			<input type="button" class="btn btn-primary" value="${msg['jsp.role.search']}" onclick="query()"/>
		</form>
		<p id="msg"></p>
		<div id="account_list"></div>
		<div id="role_list"></div>
	</div>

	<jsp:include page = "../commonmodal.jsp"/>
</body>
</html>