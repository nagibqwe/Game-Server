<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${activityTitle}</title>

    <link rel="stylesheet" href="${base}/css/activity.css" type="text/css" />
    <link rel="stylesheet" href="${base}/css/common.css" type="text/css" />
    <link rel="stylesheet" href="${base}/css/boxy.css" type="text/css" />
    <link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">

    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>

    <script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
    <jsp:include page="operator.jsp"/>
    <script type="text/javascript"> 
    var base = '${base}';
    var actType = ${actType};
    console.log("base=" + base + ", actType=" + actType);
	
    $(function() {      	    	
    	activity_reload(actType); //js文件里无法直接取得语言配置，只好又搬回到JSP页面里来
    	showPage(1, actType);
    	
    	$("#activity_add").validationEngine();
    	
    	$('.datetimepicker').datetimepicker({  //日期时间选择器配置
    		language:  'zh-CN',
    	    format: 'yyyy-mm-dd hh:ii',    	    
    	    todayBtn: 1,          
    	    autoclose:true
    	});
    	
    	$("#addSubmit").click(function() {
    		if ($('#activity_add').validationEngine('validate')) {    			
    			var bTime = new Date($("#beginTime").val().replace(/\-/g, "\/"));
    			var eTime = new Date($("#endTime").val().replace(/\-/g, "\/"));    			
    			var curTime = new Date();
    			
    			if (eTime <= bTime) {
    				alert("${msg['activity.ebtime.alert']}");
    			} else if (eTime <= curTime) {
    				alert("${msg['activity.ectime.alert']}");
    			} else {
    				$("#actType").val(actType);
    				$("#isTemplate").val("0");
    				$.post(base + "/activity/addFirstKill", $("#activity_add").serialize(), function(data, status) {
    					alert(data.msg);
    					if (data.ok) {
    						activity_reload(actType);
    						showPage(pageNum, actType);
    					}				
    				});
    			}
    		} else {
    			alert("${msg['activity.validation.fail']}");
    		}
    	});    	
    	
    });
    	
    
    </script>
    
</head>

<body>

	<table width="95%" height="95%" align="center">
		<tr><td valign="top"><br/>
		    <font style="white-space:nowrap" size=6><a href="${base}/activity/getPage?actType=0"><strong>${msg['activity.manager']}</strong></a></font>
		    <strong>——>${msg['activity.firstkill']}</strong><br/><br/>
        </td></tr>
        
        <tr><td>       
            <form id="activity_add" method="post" action="#">
            
            <table frame="box" width="95%" cellspacing="10">
                <tr align="left">
                <td colspan="2"><strong>${msg['activity.base.info']}</strong></td>
                <td colspan="1"><input style="display:none" id="actType" name="actType" type="text" value="0"/></td> <!-- 提交到后台时设置活动类型 -->
                </tr>
                
                <tr align="left">
                <td>${msg['activity.begintime']}<input name="beginTime" id="beginTime" type="text" class="datetimepicker validate[required]" readonly/></td>
                <td>${msg['activity.endtime']}<input name="endTime" id="endTime" type="text" class="datetimepicker validate[required]" readonly/></td>
                </tr>                 
                             
                <tr><td colspan="4"><input type="button" id="addSubmit" value="${msg['activity.add.submit']}"/></td></tr>
            </table>
                      
            </form>
        </td></tr>
        
        <tr><td>
            <div id="activity_list"></div>            
        </td></tr>
        
        <tr><td>
            <div id="pageIndex" style="float:left"></div>            
        </td></tr>        
        
	</table>

</body>

</html>