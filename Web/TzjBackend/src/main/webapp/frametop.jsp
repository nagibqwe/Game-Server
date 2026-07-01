<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.nutz.json.Json,org.nutz.mvc.Mvcs,java.util.HashMap,java.util.Map"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<%
	Map<String,String> langMap=new HashMap<>();
	for (String lang : Mvcs.getLocalizationKeySet()) {
		langMap.put(lang,Mvcs.getMessage(Mvcs.getReq(),"jsp.language."+lang));
	}
	langMap.remove("$default");
	Mvcs.getReq().setAttribute("langMap", Json.toJson(langMap));
%>
<script type="text/javascript">
	var base = '${base}';
	var langMap = ${langMap};
	var curLang = '${USER.language}';
	$(function() {
		initLang();
	});
	function initLang() {
		var lang=$("#lang");
		for ( var key in langMap) {
			var op=$("<option>");
			if(key==curLang){
				op.attr("selected","selected");
			}
			op.val(key).text(langMap[key]);
			lang.append(op);
		}
	}
	
	function changeLang(obj){
		$(obj).attr("href",base + "/user/langswitch?lang="+$("#lang").val());
		$(obj).triggerHandler("click");
	}
</script>
</head>
<body>
<div class="alert alert-success">
	<form id="topForm" action="${base}/user/langSwitch" method="POST">
		${msg['jsp.login.user']}:<b>${USER.name}</b> 
		<a href="${base}/mspwd.jsp" class="" target="Backendmainframe">${msg['jsp.login.changepwd']}</a>
		<a href="${base}/user/logout" class="" target="_parent">${msg['jsp.login.logout']}</a>
		<div style="display:inline;float: right;">
			${msg['jsp.user.curlang']}:
			<select id="lang" name="lang"></select>
			<a id="change" onclick="changeLang(this);" target="_parent" style="cursor:pointer;">${msg['jsp.user.langswitch']}</a>
		</div>
	</form>
</div>
</body>
</html>