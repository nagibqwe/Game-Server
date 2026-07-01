<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['activity.manager']}</title>

<link rel="stylesheet" href="${base}/css/activity.css" type="text/css" />
<script type="text/javascript" src="${base}/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript">

$(function() {   	
	$("#recharge").click(function() {
		$("#rShow").toggle();
	});
	
	$("#consume").click(function() {
		$("#cShow").toggle();
	});
});

</script>

</head>

<body>

	<table width="95%" height="95%" align="center">
		<tr><td valign="top"><br/>
			<font size=6><strong>${msg['activity.manager']}</strong></font><br/><br/>
			
			<a id="recharge" href="javascript:;"><strong>>> ${msg['activity.recharge']}</strong></a><br/><br/>
			<div id="rShow" style="display:none">
			<a href="${base}/activity/getPage?actType=1">&nbsp;&nbsp;&nbsp;${msg['activity.recharge.common']}</a><br/><br/>
			<a href="${base}/activity/getPage?actType=6">&nbsp;&nbsp;&nbsp;${msg['activity.recharge.loop']}</a><br/><br/>
			</div>
			
			<a id="consume" href="javascript:;"><strong>>> ${msg['activity.consume']}</strong></a><br/><br/>
			<div id="cShow" style="display:none">
			<a href="${base}/activity/getPage?actType=2">&nbsp;&nbsp;&nbsp;${msg['activity.consume.common']}</a><br/><br/>
			<a href="${base}/activity/getPage?actType=7">&nbsp;&nbsp;&nbsp;${msg['activity.consume.loop']}</a><br/><br/>
			</div>
			
			<a href="${base}/activity/getPage?actType=3"><strong>>> ${msg['activity.exchange']}</strong></a><br/><br/>
			<a href="${base}/activity/getPage?actType=4"><strong>>> ${msg['activity.drop']}</strong></a><br/><br/>
			<a href="${base}/activity/getPage?actType=5"><strong>>> ${msg['activity.monster']}</strong></a><br/><br/>
			<a href="${base}/activity/getPage?actType=8"><strong>>> ${msg['activity.receive']}</strong></a><br/><br/>
			
			<a href="${base}/activity/getPage?actType=9"><strong>>> ${msg['activity.daySend']}</strong></a><br/><br/>
			<a href="${base}/activity/getPage?actType=10"><strong>>> ${msg['activity.dayReceive']}</strong></a><br/><br/>
			<a href="${base}/activity/getPage?actType=11"><strong>>> ${msg['activity.rechargeTop']}</strong></a><br/><br/>
			<a href="${base}/activity/getPage?actType=12"><strong>>> ${msg['activity.consumeTop']}</strong></a><br/><br/>
			<a href="${base}/activity/getPage?actType=13"><strong>>> ${msg['activity.daySendTop']}</strong></a><br/><br/>
			<a href="${base}/activity/getPage?actType=14"><strong>>> ${msg['activity.dayGetTop']}</strong></a><br/><br/>
			<a href="${base}/activity/getPage?actType=15"><strong>>> ${msg['activity.login.title']}</strong></a>
			
			
			<br/><br/><br/><br/>
			<a href="${base}/activity/getPage?actType=100"><font size='5' color='red'><strong>${msg['activity.verify.link']}</strong></font></a>
			
        </td></tr>
	</table>

</body>

</html>