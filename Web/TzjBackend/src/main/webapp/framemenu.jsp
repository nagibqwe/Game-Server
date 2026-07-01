<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>菜单</title>
<link rel="stylesheet" href="${base}/css/dtree/dtree.css"/>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="${base}/css/dtree/dtree.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
	var a = new dTree('a');
	var base = '${base}';
	var id = '${USER.id}';
	$(function() {
		menu_reload();
	});
	function menu_reload(){
		$.ajax({
			url : base + "/menu/queryPermisson",
			data : {
				"userid" : id,
				"available" : 1
			},
			dataType : "json",
			success : function(data) {
				//console.log(data);
				if (data.ok) {
					showMenu(data.data);
				} else {
					alert(data.msg);
				}
			}
		});
	};
	function showMenu(data){
		a.add(0, -1, "${msg['jsp.login.logo']}");
		for (var i = 0; i < data.length; i++) {
			var menu = data[i];
			var url = "";
			if(jQuery.isEmptyObject(menu.urlPath)){
				a.add(menu.menuId,menu.parentId,menu.menuName,url,menu.menuName, "Backendmainframe", null, null, true);
				continue;
			}
			url=menu.urlPath;
			if(url.indexOf("?")!=-1){
				url+="&menuId="+menu.menuId;
			}else if(url.lastIndexOf("/")==url.length-1){
				url=url.substring(0,url.length-1);
				url+="?menuId="+menu.menuId;
			}else{
				url+="?menuId="+menu.menuId;
			}
			a.add(menu.menuId,menu.parentId,menu.menuName,url,menu.menuName, "Backendmainframe", null, null, true);
		}
		document.getElementById("menu_list").innerHTML=a;
	};
</script>
</head>
<body>
<div id="menu_list">
</div>
</body>
</html>
