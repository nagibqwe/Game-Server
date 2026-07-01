<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>${msg['activity.consume']}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
    var actType = 2;
    var itemMap = new Object();//用来存放itemId和对应的蓝钻价值
    
    $(function() {    	    	
    	activity_reload(actType); //js文件里无法直接取得语言配置，只好又搬回到JSP页面里来
    	showPage(1, actType);
    	itemMap = getItemInfoList();
    	activity_template_reload(actType);
    	
    	$("#activity_add").validationEngine();
    	
    	$('.datetimepicker').datetimepicker({  //日期时间选择器配置
    		language:  'zh-CN',
    	    format: 'yyyy-mm-dd hh:ii',    	    
    	    todayBtn: 1,          
    	    autoclose:true
    	});
    	add_top_condition(rewardIndex,actType);
    	$("#addCondition").click(function() {                               	
    		add_top_condition(rewardIndex,actType);		
    	});
    	$("#deleteCondition").click(function() {
    		delete_top_condition(rewardIndex-1);	
    	});
    	
    	$("#addSubmit").click(function() {
    		if ($('#activity_add').validationEngine('validate')) {
    			var bTime = new Date($("#beginTime").val().replace(/\-/g, "\/"));
    			var eTime = new Date($("#endTime").val().replace(/\-/g, "\/"));
    			var coBTime = new Date($("#coBeginTime").val().replace(/\-/g, "\/"));
    			var coETime = new Date($("#coEndTime").val().replace(/\-/g, "\/"));
    			var curTime = new Date();
    			   			
    			if (eTime <= bTime) {
    				alert("${msg['activity.ebtime.alert']}");
    			} else if (eTime <= curTime) {
    				alert("${msg['activity.ectime.alert']}");
    			} else if (coETime <= coBTime) {
    				alert("${msg['activity.cctime.alert']}");
    			} else {
    				$("#isTemplate").val("0");
    				var itemPrice = 0;
    				var n = 1;
    	    		while(n){
    	    			var rewardList = $("#rewardList"+n).val();
        	    		if(rewardList != "" && rewardList != undefined){
        	    			var rewardLists = rewardList.split(";");
        	        		for(var i = 0 ; i < rewardLists.length ; i++){
        	        			var itemObj = rewardLists[i].split(",");
        	        			itemPrice += itemMap.get(itemObj[0])*itemObj[1];
        	        		}
        	    		}else{
        	    			break;
        	    		}
        	    		n++;
    	    		}
    	    		var con = confirm("${msg['activity.item.itemPrice']}"+itemPrice+"${msg['activity.item.isContinue']}");
    	    		if(con == false){
    	    			return;
    	    		}
    				$.post(base + "/activity/addCommonConsume", $("#activity_add").serialize(), function(data, status) {
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
    	
    	$("#addTemplate").click(function() {
    		if ($('#activity_add').validationEngine('validate')) { //验证通过后才能add添加    			
    			var bTime = new Date($("#beginTime").val().replace(/\-/g, "\/"));
    			var eTime = new Date($("#endTime").val().replace(/\-/g, "\/"));
    			var coBTime = new Date($("#coBeginTime").val().replace(/\-/g, "\/"));
    			var coETime = new Date($("#coEndTime").val().replace(/\-/g, "\/"));
    			var curTime = new Date();
    			   			
    			if (eTime <= bTime) {
    				alert("${msg['activity.ebtime.alert']}");
    			} else if (eTime <= curTime) {
    				alert("${msg['activity.ectime.alert']}");
    			} else if (coETime <= coBTime) {
    				alert("${msg['activity.cctime.alert']}");
    			} else {
    				$("#isTemplate").val("1");
    				$.post(base + "/activity/addCommonConsume", $("#activity_add").serialize(), function(data, status) {
    					alert(data.msg);
    					if (data.ok) {
    						activity_template_reload(actType);
    					}
    				});  				
    			}    			    			
    		} else {
    			alert("${msg['activity.validation.fail']}");
    		}
    	});
    	
    	$("body").on("change", "#template", function() {    		
    		var createTime = $("#template").val();
    		if (createTime != "") {
    			$("#delTemplate").show();//将删除模板按钮显示
        		set_template(createTime, actType);
    		}else{
    			$("#delTemplate").hide();//将删除模板按钮隐藏
    		}    		
    	});
    });    
    </script>
    
</head>

<body>

	<table width="95%" height="95%" align="center">
		<tr><td valign="top"><br/>
		    <font style="white-space:nowrap" size=6><a href="${base}/activity/getPage?actType=0"><strong>${msg['activity.manager']}</strong></a></font>
		    <strong>——>${msg['activity.consume']}:${msg['activity.consume.common']}</strong><br/><br/>
        </td></tr>
        
        <tr><td>       
            <form id="activity_add" method="post" action="#">
            
            <table frame="box" width="95%" cellspacing="10">
                <tr align="left">
                <td colspan="2"><strong>${msg['activity.base.info']}</strong></td>
                <td colspan="2"  align="left">
                	模板名字：
                	<input type="text"  name="templateName" id="templateName" placeholder="自定义模板名字"/>
                	<input type="button" id="addTemplate" value="添加此模板"/>
                    <input style="display:none" id="isTemplate" name="isTemplate" type="text" value="1"/>选择模板<select id="template"></select>
                    <input type="button" style="display: none;" id="delTemplate" value="删除此模板"/>
                </td>
                </tr>
                           
                <tr align="left">
                <td>${msg['activity.name']}&nbsp;&nbsp;&nbsp;&nbsp;<input id="name" name="name" type="text" class="validate[required]"/></td>
                <td>${msg['activity.labelpos']}<input id="labelPosition" name="labelPosition" type="text" class="validate[required],custom[integer]]"/></td>
                <td>${msg['activity.labelorder']}<input id="labelOrder" name="labelOrder" type="text" class="validate[required,custom[integer]]"/></td>                        
                <td>${msg['activity.numlimit']}<input id="numLimit" name="numLimit" type="text" class="validate[required,custom[integer],min[0]]"/></td>
                </tr>
                
                <tr align="left">
                <td>${msg['activity.begintime']}<input name="beginTime" id="beginTime" type="text" class="datetimepicker validate[required]" readonly/></td>
                <td>${msg['activity.endtime']}<input name="endTime" id="endTime" type="text" class="datetimepicker validate[required]" readonly/></td>
                <td>${msg['activity.panelimageid']}<input id="panelImageId" name="panelImageId" type="text" class="validate[required,custom[integer],min[0]]"/></td>
                <td>${msg['activity.paneltext']}<input id="panelText" name="panelText" type="text" class="validate[required]"/></td>               
                </tr> 
                
                <tr align="left">
                <td colspan="4"><strong>${msg['activity.reward']}</strong></td>
                </tr>
                
                <tr align="left">
                <td id="selectCondition" colspan="4">                 
                </td>
                </tr> 
                <tr>
                <td colspan="1"><input type="button" id="addCondition" value="+"/>
                <input type="button" id="deleteCondition" value="-"/></td>
                </tr>                  
                
                <tr align="left">
                <td colspan="4"><strong>${msg['activity.reward.condition']}</strong></td>
                </tr>

                <tr align="left">
                <td>${msg['activity.consume.begintime']}<input name="coBeginTime" id="coBeginTime" type="text" class="datetimepicker validate[required]" readonly/></td>
                <td>${msg['activity.consume.endtime']}<input name="coEndTime" id="coEndTime" type="text" class="datetimepicker validate[required]" readonly/></td>
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