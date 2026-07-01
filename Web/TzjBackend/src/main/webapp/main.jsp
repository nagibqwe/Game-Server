<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.main.title']}</title>
</head>
<frameset id="main" name="main" rows="40,*" cols="*" frameborder="NO" border="0" framespacing="0">
	<frame id="top" name="topFrame" src="${base}/frametop.jsp" scrolling="NO" noresize />
	<frameset rows="*" cols="230,*" framespacing="0" frameborder="NO" border="0">
		<frame id="left" name="leftFrame" src="${base}/framemenu.jsp" scrolling="AUTH" noresize />
		<frame id="right" name="Backendmainframe" src="${base}/mainframe.jsp" />
  	</frameset>
</frameset>

<noframes><body></body></noframes>
</html>