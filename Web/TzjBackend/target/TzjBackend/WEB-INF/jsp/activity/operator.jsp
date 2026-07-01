   <%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <script type="text/javascript">
    var base = '${base}';

    var pageSize = 10; //每页展示的活动数量
    var actList;
    var actNum = 0;
    var pageNum = 0;
    var curPageIndex = 1;

    //重新加载活动列表
    function activity_reload(actType) {
    	$.ajax({
    		type : "POST",
    		url : base + "/activity/queryShow",
    		data : {actType : actType},
			dataType : "json",
			async: false, //要分页展示数据，此处需同步处理
			success : function(dataResult) {
				actList = dataResult.list;
			    actNum = actList.length;
	            var remainder = actNum % pageSize;
	            if (remainder > 0) {
	    	        pageNum = parseInt(actNum/pageSize) + 1;
	            } else {
	            	pageNum = parseInt(actNum/pageSize);
	            }
			    console.log("actNum=" + actNum + ", pageNum=" + pageNum);

				var activity_list_html = "<table id='actTable' frame='box' width='95%' cellspacing='10'>";
				title_html = getTitle(actType);
		    	activity_list_html += title_html;
				activity_list_html += "</table>";
				$("#activity_list").html(activity_list_html);

				var page_index_html = "";
				for (var i = 1; i <= pageNum; i++) {
					var index = "<a id='index' href='####' onclick='showPage(" + i + ", " + actType + "); return false;'>" + i + "</a>&nbsp;";
					page_index_html += index;
				}
				page_index_html += "${msg['activity.page.gong']}" + pageNum + "${msg['activity.page.ye']}<br/>";
				page_index_html += "<button onclick=submitReleaseBatch('test',0,"+actType+");>${msg['activity.batch.test']}</button>&nbsp;&nbsp;";
				page_index_html += "<button onclick=submitReleaseBatch('release',1,"+actType+");>${msg['activity.batch.release']}</button>&nbsp;&nbsp;";
				page_index_html += "<button onclick=submitDeleteBatch('delete');>${msg['activity.batch.delete']}</button>&nbsp;&nbsp;";
				page_index_html += "<button onclick=submitDeleteAllExpired("+actType+");>${msg['activity.batch.deleteAllExpired']}</button>&nbsp;&nbsp;";
				$("#pageIndex").html(page_index_html);
			}
    	});
    }

    function showPage(pageIndex, actType) {
    	if (pageIndex == 0 || pageIndex > pageNum) {
    		return;
    	}

    	curPageIndex = pageIndex;
    	var fromIndex = (pageIndex - 1) * pageSize;
    	var toIndex = fromIndex + pageSize - 1;
    	if (toIndex > (actNum - 1)) {
    		toIndex = actNum - 1;
    	}

    	var act_list_html = "";
    	for (var i = fromIndex; i <= toIndex; i++) {
    		var activity = actList[i];
    		var content_html = setContentHtml(actType, activity);

		    var eTime = new Date((activity.endTime).replace(/\-/g, "\/"));
    		var curTime = new Date();
    		var status = 0;
    		if (eTime < curTime) { //活动结束时间小于当前时间，表示此活动已过期
    			content_html += "<td>${msg['activity.state.expired']}</td><td>";
    			status = 3;
    		} else {
    			if (activity.state == 0) { //未验证，需测试验证
    				var okSid = "";
    				if (activity.okSidList != null && activity.okSidList != "") {
    					okSid = activity.okSidList;
    				}
    				content_html += "<td>${msg['activity.state.notverified']}" + okSid + "</td><td><button class='button' onclick='activity_send(" + activity.id + ",0);'>${msg['activity.test']}</button>";
				} else if (activity.state == 1) { //已验证，可发布出去
					content_html += "<td>${msg['activity.state.hasverified']}</td><td><button class='button' onclick='activity_send(" + activity.id + ",1);'>${msg['activity.release']}</button>";
				} else if (activity.state == 2) { //已发布
					content_html += "<td>${msg['activity.state.released']}</td><td><button class='button' onclick='activity_send(" + activity.id + ",1);'>${msg['activity.release']}</button>";
				}
    		}
			content_html += "<button onclick='activity_delete(" + activity.id + ");'>${msg['activity.delete']}</button></td>";
			//状态为0，可以测试，不能发布，可以删除
			//状态为1或者2，不可以测试，能发布，可以删除
			//3，不可以测试，不能发布，只能删除
			if(status == 3){
				content_html += "<td align='center'><input type='checkbox' disabled='disabled' value='"+activity.id+"'/></td>";
				content_html += "<td align='center'><input type='checkbox' disabled='disabled' value='"+activity.id+"'/></td>";
			}else if(status != 3){
				if(activity.state == 0){
					content_html += "<td align='center'><input type='checkbox' name='testAct' value='"+activity.id+"'/></td>";
					content_html += "<td align='center'><input type='checkbox' disabled='disabled' value='"+activity.id+"'/></td>";
				}else{
					content_html += "<td align='center'><input type='checkbox' disabled='disabled' value='"+activity.id+"'/></td>";
					content_html += "<td align='center'><input type='checkbox' name='releaseAct' value='"+activity.id+"'/></td>";
				}
			}
			content_html += "<td align='center'><input type='checkbox' value='"+activity.id+"' name='deleteAct'/></td>\</tr>";
			act_list_html += content_html;
    	}
    	$("tr").remove("#activity"); //清楚上一页活动，然后贴上请求页的活动
    	$("#actTable").append(act_list_html);

    	$('body').on('click', '#index', function() {
        	$(this).css({"background-color":"red","font-size":"130%"});
        	$(this).siblings("a").css({"background-color":"white","font-size":"100%"});
        });
    }
    //发布活动
    function activity_send(actId, serverType) {
    	console.log("发布活动Id=" + actId);

    	$.ajax({
			url : base + "/activity/queryPlatSid",
			data : {
				"serverType" : serverType
			},
			dataType : "json",
			success : function(jsonData) {
				var boxy_html = "<div>${msg['activity.release.plat.select']}<br/><select id='platform'><option value='' selected='selected'>--------${msg['activity.release.plat']}--------</option>";

				var data = eval('(' + jsonData + ')');
				$.each(data, function(key, value) {
					console.log(key);
					var plat_option = "<option value='" + key + "'>" + key + "</option>";
					boxy_html += plat_option;
				});
				boxy_html += "</select></div><span>${msg['activity.release.sid.select']}</span><div id='sid'></div><div><input type='button' value='${msg['activity.release']}'></div>";
				var width = 400;
				var height = 10;
		    	var boxy = new Boxy(boxy_html,
		        {
	    		       title:"${msg['activity.release.act']}"+actId, //Box标题
	    		       closeText:"[${msg['activity.item.cancel']}]",     //Box关闭文字
	    		       modal:false,          //Box背景是否变暗
	    		       fixed:false,          //Box窗口是否固定
	    		       cache:false,          //Box是否被遮挡
	    		       draggable:true,       //Box是否可以拖动，要定义title才有效，设定了modal就无效
	    		       center:true,          //Box是否居中
	    		       unloadOnHide:true,    //点击"取消"后box是隐藏并没有卸载掉，此处设置在隐藏后则卸掉
	    		       afterShow:function() {
	    		    	   //选择不同平台展示该平台下的区服列表
						   $("#platform").change(function() {
							   var plat = $("#platform").val();
							   var sid_html = "";
							   if (plat != '') {
								   var size=0;
								   sid_html += "<input type='checkbox' name='cha' onclick=\"checkAll(this,'cho')\">${msg['activity.selectall']}<br/>";
								   $.each(data, function(key, value) {
										if (key == plat) {
											$.each(value, function(i) {
												var server = value[i].split("_");
												sid_html += "<input type='checkbox' name='cho' onclick=\"checkItem(this,'cha')\" value='" + server[0] + "'/>" + "[" + server[0] + "]" + server[1] + "&nbsp;";
												size++;
											});
										}
									});
							   }
							   $("#sid").html(sid_html);
							   boxy.resize(width+size*2, height+size*4);
						   });

	    		    	   $("input:button").click(function() {
	    		    	       var platform = $("#platform").val();
	    		    	       var s = "";
	                           $("input[name='cho']:checkbox:checked").each(function() {
	                        	   //console.log($(this));
	                        	   s += $(this).val() + "_";
							   });
	                           var sids = s.substring(0, s.length-1);
	                           console.log(platform + ":" + sids);
	                           if (platform == "" || sids == "") {
	                                Boxy.alert("${msg['activity.release.error.tip']}", null, {title:"${msg['activity.release.error']}"});
	                           } else {
	                                $.post(base + "/activity/sendActivity", {actId:actId, platform:platform, sids:sids, serverType:serverType},
	                               	function(data, status) {
	                               		alert(data.msg);
	                               		//if (data.ok == true && serverType == 1) {
	                               			//activity_reload(actType);
	                               		//}
	                               		if (data.ok == true) {
	                               			activity_reload(actType);
	                               			showPage(curPageIndex, actType);
	                               		}
	                               	});
	                                Boxy.get(this).unload(); //点击发布后删除此对话框
	                           }
	    		           });
	    		       } //打开对话框后执行的
		        });
		    	boxy.centerAt(800, 400);
		    	//boxy.resize(300, 5);
			}
		});

    }

    //通过活动id来删除这一行的活动
    function activity_delete(actId) {
    	if(confirm("${msg['activity.delete.confirm']}")) {
        	$.post(base + "/activity/delete", {actId:actId},
            	function(data, status) {
            		alert(data.msg);
            		if (data.ok) {
            			activity_reload(actType);
            			showPage(curPageIndex, actType);
            		}
            });
    	}
    }

    //设置活动列表显示的数据
    function setContentHtml(actType, activity)
    {
    	switch(actType)
    	{
	    	case 1:
	    		{
	    		var conArr = activity.conditionList.split("_");
				var rechargeNum = conArr[0];
				var rechargeTime = conArr[1] + "_" + conArr[2];

				var content_html = "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+rechargeNum+"</td>\
				    <td>"+rechargeTime+"</td>\
				    <td width='25%'>"+activity.rewardListStr+"</td>\
				    <td align='center'>"+activity.numLimit+"</td>";
				    return content_html;
	    		}
	    		break;
	    	case 2:
	    		{
	    		var conArr = activity.conditionList.split("_");
				var consumeNum = conArr[0];
				var consumeTime = conArr[1] + "_" + conArr[2];

				var content_html = "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+consumeNum+"</td>\
				    <td>"+consumeTime+"</td>\
				    <td width='20%'>"+activity.rewardListStr+"</td>\
				    <td>"+activity.numLimit+"</td>";
				    return content_html;
	    		}
	    		break;
	    	case 3:
	    		{
	    		var conArr = activity.conditionList.split("_");
	    		var needItem = conArr[0];
	    		var countArr = conArr[1].split(";");
	    		var maxCount = countArr[0];
	    		var eveMaxCount = countArr[1];
	    		var content_html = "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+needItem+"</td>\
				    <td>"+maxCount+"</td>\
				    <td>"+eveMaxCount+"</td>\
				    <td>"+activity.rewardListStr+"</td>\
				    <td>"+activity.numLimit+"</td>";
					return content_html;
	    		}
	    		break;
	    	case 4:
	    		{
				var conArr = activity.conditionList.split("_");
				var dropProb = conArr[0];
				var dropType = conArr[1] + "_" + conArr[2];
				var dropLimit = conArr[3];
				var perLimit = conArr[4];
				var isNotice = conArr[5];
				var content = "";
				if (isNotice == 1) {
					content = conArr[6];
				}

				var content_html = "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td width='20%'>"+activity.rewardListStr+"</td>\
				    <td>"+dropProb+"</td>\
				    <td>"+dropLimit+"</td>\
				    <td>"+perLimit+"</td>\
				    <td>"+dropType+"</td>\
				    <td align='center'>"+isNotice+"</td>\
				    <td>"+content+"</td>\
				    <td align='center'>"+activity.numLimit+"</td>";
				    return content_html;
	    		}
	    		break;
	    	case 5:
	    		var conArr = activity.conditionList.split("_");
				var monsterMapId = conArr[0];
				var monsterId = conArr[1];
				var monsterNum = conArr[2];
				var monsterXY = conArr[3];
				var timeInterval = conArr[4];

				var content_html = "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+monsterMapId+"</td>\
				    <td>"+monsterId+"</td>\
				    <td>"+monsterNum+"</td>\
				    <td>"+monsterXY+"</td>\
				    <td>"+timeInterval+"</td>\
				    <td>"+activity.numLimit+"</td>";

				    return content_html;
	    		break;
	    	case 6:
	    		{
	    		var conArr = activity.conditionList.split("_");
				var rechargeNum = conArr[0];
				var type = conArr[1];

				var loopType = "";
				switch(type) {
				case "1":
					loopType = "${msg['activity.loop.day']}";
					break;
				case "2":
					loopType = "${msg['activity.loop.week']}";
					break;
				case "3":
					loopType = "${msg['activity.loop.month']}";
					break;
				}

				var content_html = "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td width='20%'>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+rechargeNum+"</td>\
				    <td>"+loopType+"</td>\
				    <td  width='25%'>"+activity.rewardListStr+"</td>\
				    <td>"+activity.numLimit+"</td>";
				    return content_html;
	    		}
	    		break;
	    	case 7:
	    		{
				var conArr = activity.conditionList.split("_");
				var consumeNum = conArr[0];
				var type = conArr[1];
				var loopType = "";
				switch(type) {
				case "1":
					loopType = "${msg['activity.loop.day']}";
					break;
				case "2":
					loopType = "${msg['activity.loop.week']}";
					break;
				case "3":
					loopType = "${msg['activity.loop.month']}";
					break;
				}

				var content_html = "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+consumeNum+"</td>\
				    <td>"+loopType+"</td>\
				    <td width='20%'>"+activity.rewardListStr+"</td>\
				    <td>"+activity.numLimit+"</td>";
				    return content_html;
	    		}
	    		break;
	    	case 8:
	    		{
	    		return "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+activity.conditionList+"</td>\
				    <td>"+activity.rewardListStr+"</td>\
				    <td>"+activity.numLimit+"</td>";
	    		}
	    		break;
	    	case 9:
	    		return "<tr id='activity'>\
			    <td>"+activity.id+"</td>\
			    <td>"+activity.name+"</td>\
			    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
			    <td>"+activity.conditionList+"</td>\
			    <td width='25%'>"+activity.rewardListStr+"</td>\
			    <td align='center'>"+activity.numLimit+"</td>";
	    		break;
	    	case 10:
	    		return "<tr id='activity'>\
			    <td>"+activity.id+"</td>\
			    <td>"+activity.name+"</td>\
			    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
			    <td>"+activity.conditionList+"</td>\
			    <td width='25%'>"+activity.rewardListStr+"</td>\
			    <td align='center'>"+activity.numLimit+"</td>";
	    		break;
	    	case 11:
	    	case 12:
	    	case 13:
	    	case 14:
	    		{
	    		var conArr = activity.conditionList.split("|");
	    		var timeArr = conArr[0].split("_");
	    		var conStr = "";
	    		for (var i = 1; i < conArr.length; i++) {
	    			if( i > 1){
	    				conStr +="<br/>";
	    			}
	    			conStr +=conArr[i];
	    		}
	    		return "<tr id='activity'>\
			    <td>"+activity.id+"</td>\
			    <td>"+activity.name+"</td>\
			    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
			    <td>"+timeArr[0]+'~'+timeArr[1]+"</td>\
			    <td width='20%'>"+activity.rewardListStr+"</td>\
			    <td align='center'>"+activity.numLimit+"</td>";
	    		}
	    		break;
	    	case 15:
	    		{
	    		var conArr = activity.conditionList.split("_");

	    		return "<tr id='activity'>\
			    <td>"+activity.id+"</td>\
			    <td>"+activity.name+"</td>\
			    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
			    <td>"+conArr[0]+'~'+conArr[1]+"</td>\
			    <td>"+conArr[2]+'~'+conArr[3]+"</td>\
			    <td width='18%'>"+activity.rewardListStr+"</td>\
			    <td>"+activity.okSidList+"</td>";
	    		}
	    		break;

	    	case 17:
			    return "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+activity.conditionList+"</td>\
				    <td width='20%'>"+activity.rewardListStr+"</td>\
				    <td>"+activity.numLimit+"</td>";
			    break;

			case 18:
	    		{
	    		var conArr = activity.conditionList.split("_");
				var type = conArr[0];
				var loopType = "";
				switch(type) {
				case "1":
					loopType = "${msg['activity.loop.day']}";
					break;
				case "2":
					loopType = "${msg['activity.loop.week']}";
					break;
				case "3":
					loopType = "${msg['activity.loop.month']}";
					break;
				}
				var content_html = "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td width='20%'>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+loopType+"</td>\
				    <td  width='25%'>"+activity.rewardListStr+"</td>\
				    <td>"+activity.numLimit+"</td>";
				    return content_html;
	    		}
	    		break;

	    	case 19:
			    return "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+activity.conditionList+"</td>\
				    <td>"+activity.numLimit+"</td>";
			    break;
 	    	case 20:
	    		return "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>";

			    break;
	    	case 22:
			    return "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+activity.conditionList+"</td>\
				    <td>"+activity.rewardListStr+"</td>\
				    <td>"+activity.numLimit+"</td>";
			    break;

	    	case 23:
			    return "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+activity.conditionList+"</td>\
				    <td>"+activity.numLimit+"</td>";
			    break;

	    	case 24:
			    return "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+activity.conditionList+"</td>\
				    <td>"+activity.numLimit+"</td>";
			    break;

	    	case 27:
			    return "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+activity.conditionList+"</td>\
				    <td>"+activity.rewardListStr+"</td>\
				    <td>"+activity.numLimit+"</td>";
			    break;

	    	case 28:
			    return "<tr id='activity'>\
				    <td>"+activity.id+"</td>\
				    <td>"+activity.name+"</td>\
				    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
				    <td>"+activity.conditionList+"</td>\
				    <td>"+activity.numLimit+"</td>"
			    break;
	    	case 32:
	    		return "<tr id='activity'>\
			    <td>"+activity.id+"</td>\
			    <td>"+activity.name+"</td>\
			    <td>"+activity.beginTime+'~'+activity.endTime+"</td>"
		   		 break;
	    	case 33:
	    		return "<tr id='activity'>\
			    <td>"+activity.id+"</td>\
			    <td>"+activity.name+"</td>\
			    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
			    <td>"+activity.conditionList+"</td>"
		   		 break;
	    	case 34:
	    		return "<tr id='activity'>\
			    <td>"+activity.id+"</td>\
			    <td>"+activity.name+"</td>\
			    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
			    <td>"+activity.conditionList+"</td>\
			    <td>"+activity.rewardListStr+"</td>"
		   		 break;
	    	case 35:
	    		return "<tr id='activity'>\
			    <td>"+activity.id+"</td>\
			    <td>"+activity.name+"</td>\
			    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
			    <td>"+activity.conditionList+"</td>\
			    <td>"+activity.rewardListStr+"</td>\
			    <td>"+activity.numLimit+"</td>";
	    		break;
	    	case 36:
	    		return "<tr id='activity'>\
			    <td>"+activity.id+"</td>\
			    <td>"+activity.name+"</td>\
			    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
			    <td>"+activity.conditionList+"</td>\
			    <td>"+activity.rewardListStr+"</td>\
			    <td>"+activity.numLimit+"</td>";
	    		break;
	    	case 101:
	    		return "<tr id='activity'>\
			    <td>"+activity.id+"</td>\
			    <td>"+activity.name+"</td>\
			    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
			    <td>"+activity.conditionList+"</td>\
			    <td>"+activity.otherList+"</td>\
			    <td>"+activity.submitBeginTime+'~'+activity.submitEndTime+"</td>\
			    <td>"+activity.tag+"</td>\
			    <td>"+activity.numLimit+"</td>";
	    		break;
	    	case 102:
	    		return "<tr id='activity'>\
			    <td>"+activity.id+"</td>\
			    <td>"+activity.name+"</td>\
			    <td>"+activity.beginTime+'~'+activity.endTime+"</td>\
			    <td>"+activity.conditionList+"</td>\
			    <td>"+activity.rewardListStr+"</td>\
			    <td>"+activity.otherList+"</td>\
			    <td>"+activity.submitBeginTime+'~'+activity.submitEndTime+"</td>\
			    <td>"+activity.tag+"</td>\
			    <td>"+activity.numLimit+"</td>";
	    		break;

    	}

    	return "";
    }

    //获取活动列表的标题栏
    function getTitle(actType)
    {
    	var title_html = "";
    	switch(actType)
    	{
	    	case 1:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.recharge.num']}</th><th>${msg['activity.recharge.time']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_1' onclick=selectAll('test',1);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_1' onclick=selectAll('release',1);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_1' onclick=selectAll('delete',1);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 2:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.consume.num']}</th><th>${msg['activity.consume.time']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_2' onclick=selectAll('test',2);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_2' onclick=selectAll('release',2);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_2' onclick=selectAll('delete',2);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 3:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.exchange.item']}</th><th>${msg['activity.exchange.maxCount']}</th><th>${msg['activity.exchange.eveMaxCount']}</th><th>${msg['activity.exchange.goal']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_3' onclick=selectAll('test',3);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_3' onclick=selectAll('release',3);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_3' onclick=selectAll('delete',3);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 4:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.drop.item']}</th><th>${msg['activity.drop.prob']}</th><th>${msg['activity.drop.limit']}</th><th>${msg['activity.drop.perLimit']}</th><th>${msg['activity.drop.type']}</th><th>${msg['activity.drop.notice']}</th><th>${msg['activity.drop.notice.content']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_4' onclick=selectAll('test',4);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_4' onclick=selectAll('release',4);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_4' onclick=selectAll('delete',4);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 5:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.monster.mapid']}</th><th>${msg['activity.monster.id']}</th><th>${msg['activity.monster.num']}</th><th>${msg['activity.monster.xy']}</th><th>${msg['activity.monster.time']}(s)</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_5' onclick=selectAll('test',5);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_5' onclick=selectAll('release',5);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_5' onclick=selectAll('delete',5);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 6:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.recharge.num']}</th><th>${msg['activity.loop.type']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_6' onclick=selectAll('test',6);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_6' onclick=selectAll('release',6);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_6' onclick=selectAll('delete',6);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 7:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.consume.num']}</th><th>${msg['activity.loop.type']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_7' onclick=selectAll('test',7);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_7' onclick=selectAll('release',7);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_7' onclick=selectAll('delete',7);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 8:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.receive.condition']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_8' onclick=selectAll('test',8);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_8' onclick=selectAll('release',8);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_8' onclick=selectAll('delete',8);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 9:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.receive.condition']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_9' onclick=selectAll('test',9);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_9' onclick=selectAll('release',9);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_9' onclick=selectAll('delete',9);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 10:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.receive.condition']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_10' onclick=selectAll('test',10);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_10' onclick=selectAll('release',10);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_10' onclick=selectAll('delete',10);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 11:
	    	case 12:
	    	case 13:
	    	case 14:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.di.startTime']}</th><th>${msg['activity.receive.condition']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_14' onclick=selectAll('test',14);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_14' onclick=selectAll('release',14);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_14' onclick=selectAll('delete',14);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 15:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.login.mailTitle']}</th><th>${msg['activity.login.sendTime']}</th><th>${msg['activity.login.level']}</th><th>${msg['activity.logintime']}</th><th>${msg['activity.login.fujianItem']}</th><th>${msg['activity.login.servereList']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_15' onclick=selectAll('test',15);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_15' onclick=selectAll('release',15);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_15' onclick=selectAll('delete',15);></input></th>";
	    		title_html += "</tr>";
	    		break;

	    	case 17:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.buylimit.condition']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_17' onclick=selectAll('test',17);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_17' onclick=selectAll('release',17);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_17' onclick=selectAll('delete',17);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 18:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.loop.type']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_18' onclick=selectAll('test',18);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_18' onclick=selectAll('release',18);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_18' onclick=selectAll('delete',18);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 19:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.monsterexp.multiple']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_19' onclick=selectAll('test',19);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_19' onclick=selectAll('release',19);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_19' onclick=selectAll('delete',19);></input></th>";
	    		title_html += "</tr>";
	    		break;
 	    	case 20:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_20' onclick=selectAll('test',20);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_20' onclick=selectAll('release',20);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_20' onclick=selectAll('delete',20);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 22:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.killboss.bossinfo']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_22' onclick=selectAll('test',22);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_22' onclick=selectAll('release',22);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_22' onclick=selectAll('delete',22);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 23:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.receive.condition']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_23' onclick=selectAll('test',23);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_23' onclick=selectAll('release',23);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_23' onclick=selectAll('delete',23);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 24:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.rechargeExtra.rate']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_24' onclick=selectAll('test',24);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_24' onclick=selectAll('release',24);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_24' onclick=selectAll('delete',24);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 27:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.reward.condition']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_27' onclick=selectAll('test',27);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_27' onclick=selectAll('release',27);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_27' onclick=selectAll('delete',27);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 28:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th>"
	    		title_html += "<th>${msg['activity.reward.condition']}</th><th>${msg['activity.numlimit']}</th>"
	    		title_html += "<th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>"
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_28' onclick=selectAll('test',28);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_28' onclick=selectAll('release',28);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_28' onclick=selectAll('delete',28);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 32:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th></th><th></th>"
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_32' onclick=selectAll('test',32);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_32' onclick=selectAll('release',32);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_32' onclick=selectAll('delete',32);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 33:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th>"
	    		title_html += "<th>${msg['activity.condition']}</th><th></th><th></th>"
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_33' onclick=selectAll('test',33);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_33' onclick=selectAll('release',33);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_33' onclick=selectAll('delete',33);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 34:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th>"
	    		title_html += "<th>${msg['activity.condition']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.state']}</th><th></th>"
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_34' onclick=selectAll('test',34);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_34' onclick=selectAll('release',34);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_34' onclick=selectAll('delete',34);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 35:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.condition']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_35' onclick=selectAll('test',35);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_35' onclick=selectAll('release',35);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_35' onclick=selectAll('delete',35);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 36:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.condition']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_36' onclick=selectAll('test',36);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_36' onclick=selectAll('release',36);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_36' onclick=selectAll('delete',36);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 101:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.condition']}</th><th>${msg['activity.reward']}</th><th>${msg['activity.glories.time']}</th><th>${msg['activity.activityType']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_101' onclick=selectAll('test',101);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_101' onclick=selectAll('release',101);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_101' onclick=selectAll('delete',101);></input></th>";
	    		title_html += "</tr>";
	    		break;
	    	case 102:
	    		title_html += "<tr>";
	    		title_html += "<th>${msg['activity.num']}</th><th>${msg['activity.name']}</th><th>${msg['activity.time']}</th><th>${msg['activity.condition']}</th><th>${msg['activity.submitMaterial.personalMinReward']}</th><th>${msg['activity.submitMaterial.personalReward']}</th><th>${msg['activity.submitMaterial.time']}</th><th>${msg['activity.activityType']}</th><th>${msg['activity.numlimit']}</th><th>${msg['activity.state']}</th><th>${msg['activity.operator']}</th>";
	    		title_html += "<th>${msg['activity.test']}<input type='checkbox' id='test_102' onclick=selectAll('test',102);></th>";
	    		title_html += "<th>${msg['activity.release']}<input type='checkbox' id='release_102' onclick=selectAll('release',102);></th>";
	    		title_html += "<th>${msg['activity.delete']}<input type='checkbox' id='delete_102' onclick=selectAll('delete',102);></input></th>";
	    		title_html += "</tr>";
	    		break;
    	}
    	return title_html;
    }

    /**
     *全选optType,actType
     **/
     function selectAll(optType,actType){
     	var id=optType+"_"+actType;
     	var name = optType+"Act";
     	var che = $("#"+id).is(":checked");
     	$("[name='"+name+"']").prop("checked", che);
     }


    //领取活动的条件选择框
    var receive_condition_html = "<div>${msg['activity.receive.condition.select']}\
    	<select id='condition' class='validate[required]'>\
    	<option value='0' selected='selected'>--${msg['activity.receive.condition']}--</option>\
    	<option value='2'>${msg['activity.receive.condition.level']}</option>\
        <option value='9'>${msg['activity.receive.condition.vip']}</option>\
        <option value='10'>${msg['activity.receive.condition.equip.totalstar']}</option>\
        <option value='11'>${msg['activity.receive.condition.equip.star']}</option>\
        <option value='12'>${msg['activity.receive.condition.equip.posstar']}</option>\
        <option value='13'>${msg['activity.receive.condition.equip.strenlv']}</option>\
    	</select>\
    	<span></span>\
    	</div>";
    	/* <input type='button' id='delete' value='-'/>\ */
    //活动奖励的条件框
    var rewardIndex = 1;
    function add_top_condition(index,actType) {
    	if(index > 10) {
    		alert("${msg['activity.di.tenLimit']}");
    		return;
    	}
    	var condition_html = "";
    	switch(actType){
    		case 1:
    		case 6:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.level']}"+index+"&nbsp;&nbsp;&nbsp;\
    			&nbsp;&nbsp;&nbsp;${msg['activity.recharge.num']}&nbsp;&nbsp;&nbsp;&nbsp;<input id=\"rechargeNum"+index+"\" name=\"rechargeNum"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[1]]\"/>\
    			&nbsp;&nbsp;&nbsp;${msg['activity.di.reward']}<input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"60\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        ${msg['activity.rowText']}<input id=\"rowText"+index+"\" name=\"rowText"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\"/>\
    	        </div>";
                break;
    		case 2:
    		case 7:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.level']}"+index+"&nbsp;&nbsp;&nbsp;\
    			&nbsp;&nbsp;&nbsp;${msg['activity.consume.num']}&nbsp;&nbsp;&nbsp;&nbsp;<input id=\"consumeNum"+index+"\" name=\"consumeNum"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[1]]\"/>\
    			&nbsp;&nbsp;&nbsp;${msg['activity.di.reward']}<input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"60\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        ${msg['activity.rowText']}<input id=\"rowText"+index+"\" name=\"rowText"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\"/>\
    	        </div>";
                break;
    		case 3:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.exchange.goal']}"+index+"&nbsp;&nbsp;\
    			<input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"30\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        ${msg['activity.rowText']}<input id=\"rowText"+index+"\" name=\"rowText"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\"/>\
    	        &nbsp;&nbsp;${msg['activity.exchange.maxCount']}<input id=\"maxCount"+index+"\" name=\"maxCount"+index+"\" type=\"text\" size=\"15\" class=\"validate[required,custom[integer],min[-1]]\"/>\
    	        &nbsp;&nbsp;${msg['activity.exchange.eveMaxCount']}<input id=\"eveMaxCount"+index+"\" name=\"eveMaxCount"+index+"\" type=\"text\" size=\"15\" class=\"validate[required,custom[integer],min[-1]]\"/>\
    	        &nbsp;&nbsp;${msg['activity.exchange.item']}&nbsp;&nbsp;&nbsp;&nbsp;\
    	        <input name=\"needItem"+index+"\" id=\"needItem"+index+"\" type=\"text\" size=\"30\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addNeedItem"+index+"\" value=\"+\" onclick=\"needItemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleNeedItem("+index+");\"/>\
    	        <input type=\"button\" id=\"emptyNeedItem"+index+"\" onclick=\"needItemEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        </div>";
    			break;
    		 case 4:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.drop.item']}"+index+"&nbsp;&nbsp;&nbsp;&nbsp;\
    			<input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBoxIn4("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        ${msg['activity.rowText']}<input id=\"rowText"+index+"\" name=\"rowText"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\"/>\
    	        &nbsp;&nbsp;${msg['activity.drop.prob']}<input id=\"dropProb"+index+"\" name=\"dropProb"+index+"\" type=\"text\" size=\"15\" class=\"validate[required,custom[integer],min[0]]\"/>\
    	        &nbsp;&nbsp;${msg['activity.drop.limit']}<input id=\"dropLimit"+index+"\" name=\"dropLimit"+index+"\" type=\"text\" size=\"15\" class=\"validate[required,custom[integer],min[0]]\"/>\
    	        &nbsp;&nbsp;${msg['activity.drop.perLimit']}<input id=\"perLimit"+index+"\" name=\"perLimit"+index+"\" type=\"text\" size=\"15\" class=\"validate[required,custom[integer],min[0]]\"/>\
    	        <br/><br/>\
    	        ${msg['activity.drop.type']}<select id=\"dropType"+index+"\" name=\"dropType"+index+"\" class=\"validate[required]\" onchange=\"dropTypeChange("+index+");\">\
    	        <option value=\"1\" selected=\"selected\">${msg['activity.drop.world']}</option>\
    	        <option value=\"2\">${msg['activity.drop.map']}</option>\
    	        <option value=\"3\">${msg['activity.drop.monster']}</option></select>\
    	        &nbsp;<div id=\"mapIdinput"+index+"\" style=\"display:none\">${msg['activity.drop.map.id']}<input id=\"mapId"+index+"\" name=\"mapId"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[0]]\"/></div>\
    	        &nbsp;<div id=\"monsterIdinput"+index+"\" style=\"display:none\">${msg['activity.drop.monster.id']}<input id=\"monsterId"+index+"\" name=\"monsterId"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[0]]\"/></div>\
    	        &nbsp;&nbsp;${msg['activity.drop.notice']}<select id=\"isNotice"+index+"\" name=\"isNotice"+index+"\" class=\"validate[required]\" onchange=\"noticeChange("+index+");\">\
    	        <option value=\"0\" selected=\"selected\">${msg['activity.no']}</option>\
    	        <option value=\"1\">${msg['activity.yes']}</option></select>\
    	        &nbsp;<div id=\"contentinput"+index+"\" style=\"display:none\">${msg['activity.drop.notice.content']}<input id=\"content"+index+"\" name=\"content"+index+"\" type=\"text\" size=\"80\" class=\"validate[required]\"/></div>\
    	        </div>";
    			break;
     		case 5:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">\
    	        &nbsp;&nbsp;${msg['activity.monster.mapid']}<input id=\"monsterMapId"+index+"\" name=\"monsterMapId"+index+"\" type=\"text\" size=\"10\" class=\"validate[required,custom[integer],min[0]]\"/>\
    	        &nbsp;&nbsp;${msg['activity.monster.id']}<input id=\"monsterId"+index+"\" name=\"monsterId"+index+"\" type=\"text\" size=\"10\" class=\"validate[required,custom[integer],min[0]]\"/>\
    	        &nbsp;&nbsp;${msg['activity.monster.num']}<input id=\"monsterNum"+index+"\" name=\"monsterNum"+index+"\" type=\"text\" size=\"10\" class=\"validate[required,custom[integer],min[1],max[100]]\"/>\
    	        &nbsp;&nbsp;${msg['activity.monster.xy']}<input name=\"monsterXY"+index+"\" id=\"monsterXY"+index+"\" type=\"text\" size=\"15\"/>\
    	        &nbsp;&nbsp;${msg['activity.monster.time']}<input id=\"timeInterval"+index+"\" name=\"timeInterval"+index+"\" type=\"text\" size=\"10\" class=\"validate[required,custom[integer],min[0]]\"/>\
    	        </div>";
    			break;
    		case 8:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.reward']}"+index+"&nbsp;&nbsp;\
    			<input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        ${msg['activity.rowText']}<input id=\"rowText"+index+"\" name=\"rowText"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\"/>\
    	        &nbsp;&nbsp;${msg['activity.receive.condition.select']}\
    	    	<select id=\"condition"+index+"\" name=\"condition"+index+"\" onchange=\"getCondition("+index+")\" class='validate[required]'>\
    	    	<option value='0' selected='selected'>--${msg['activity.receive.condition']}--</option>\
    	    	<option value='2'>${msg['activity.receive.condition.level']}</option>\
    	        <option value='9'>${msg['activity.receive.condition.vip']}</option>\
    	        <option value='10'>${msg['activity.receive.condition.equip.totalstar']}</option>\
    	        <option value='11'>${msg['activity.receive.condition.equip.star']}</option>\
    	        <option value='12'>${msg['activity.receive.condition.equip.posstar']}</option>\
    	        <option value='13'>${msg['activity.receive.condition.equip.strenlv']}</option>\
    	    	</select>\
    	    	<span id=\"conditionDetail"+index+"\"></span>\
    	    	</div>";
    			break;
    		case 9:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.reward']}"+index+"&nbsp;&nbsp;\
    			<input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        ${msg['activity.rowText']}<input id=\"rowText"+index+"\" name=\"rowText"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\"/>\
    	        &nbsp;&nbsp;${msg['activity.daySend.itemNum']}<input name=\"sendNum"+index+"\" id=\"sendNum"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[0],max[9999]]\"/>\
    	        &nbsp;&nbsp;${msg['activity.loop.type']}<select id=\"loopType"+index+"\" name=\"loopType"+index+"\" class=\"validate[required]\"><option value=\"1\">${msg['activity.loop.day']}</option>\
                <option value=\"2\">${msg['activity.loop.week']}</option><option value=\"3\">${msg['activity.loop.month']}</option><option value=\"0\">${msg['activity.loop.no']}</option></select>\
    	        </div>";
    			break;
    		case 10:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.reward']}"+index+"&nbsp;&nbsp;\
    			<input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        ${msg['activity.rowText']}<input id=\"rowText"+index+"\" name=\"rowText"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\"/>\
    	        &nbsp;&nbsp;${msg['activity.dayReceive.itemNum']}<input name=\"receiveNum"+index+"\" id=\"receiveNum"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[0],max[9999]]\"/>\
    	        &nbsp;&nbsp;${msg['activity.loop.type']}<select id=\"loopType"+index+"\" name=\"loopType"+index+"\" class=\"validate[required]\"><option value=\"1\">${msg['activity.loop.day']}</option>\
                <option value=\"2\">${msg['activity.loop.week']}</option><option value=\"3\">${msg['activity.loop.month']}</option><option value=\"0\">${msg['activity.loop.no']}</option></select>\
    	        </div>";
    			break;
    		case 17:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.activityReward']}"+index+"\
    			<input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        ${msg['activity.rowText']}<input id=\"rowText"+index+"\" name=\"rowText"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\"/>\
    	        &nbsp;&nbsp;${msg['activity.buylimit.goldtype']}<select id=\"needGold"+index+"\" name=\"needGold"+index+"\" class=\"validate[required]\">\
                <option value=\"3\">${msg['activity.buylimit.notbindgold']}</option>\
                <option value=\"4\">${msg['activity.buylimit.bindgold']}</option></select>\
    	        &nbsp;&nbsp;${msg['activity.buylimit.goldnum']}<input id=\"goldNum"+index+"\" name=\"goldNum"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[1],max[99999999]]\"/>\
    	        &nbsp;&nbsp;${msg['activity.loop.type']}<select id=\"loopType"+index+"\" name=\"loopType"+index+"\" class=\"validate[required]\"><option value=\"1\">${msg['activity.loop.day']}</option>\
                <option value=\"2\">${msg['activity.loop.week']}</option><option value=\"3\">${msg['activity.loop.month']}</option><option value=\"0\">${msg['activity.loop.no']}</option></select>\
    	        &nbsp;&nbsp;${msg['activity.buylimit.discount']}<select id=\"buyDiscount"+index+"\" name=\"buyDiscount"+index+"\" class=\"validate[required]\">\
                <option value=\"0\"></option>\
                <option value=\"1\">1${msg['activity.buylimit.discount.zhe']}</option>\
                <option value=\"2\">2${msg['activity.buylimit.discount.zhe']}</option>\
                <option value=\"3\">3${msg['activity.buylimit.discount.zhe']}</option>\
                <option value=\"4\">4${msg['activity.buylimit.discount.zhe']}</option>\
                <option value=\"5\">5${msg['activity.buylimit.discount.zhe']}</option>\
                <option value=\"6\">6${msg['activity.buylimit.discount.zhe']}</option>\
                <option value=\"7\">7${msg['activity.buylimit.discount.zhe']}</option>\
                <option value=\"8\">8${msg['activity.buylimit.discount.zhe']}</option>\
                <option value=\"9\">9${msg['activity.buylimit.discount.zhe']}</option></select>\
                &nbsp;&nbsp;${msg['activity.buylimit.limitTime']}<input id=\"limitTimes"+index+"\" name=\"limitTimes"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[1],max[99999999]]\"/>\
    	        </div>";
    			break;
     		case 20:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">道具id\
    			<input name=\"rewardId"+index+"\" id=\"rewardId"+index+"\" size=\"5\" type=\"text\" class=\"validate[required,custom[integer],min[1],max[99999999]]\"/>&nbsp;&nbsp;\
    			道具 <input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	                需要积分 <input id=\"integral"+index+"\" name=\"integral"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\"/>\
    	                兑换次数上限 <input id=\"changeCount"+index+"\" name=\"changeCount"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\"/>\
    	    	</div>";
    			break;
    		case 23:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.numlimitbuy.itemId']}"+index+"&nbsp;&nbsp;\
    			<input name=\"itemId"+index+"\" id=\"itemId"+index+"\" type=\"text\" list=\"itemIdList"+index+"\" class=\"validate[required]\"/><datalist id=\"itemIdList"+index+"\"></datalist>\
    			&nbsp;&nbsp;${msg['activity.item.bind']}<select id=\"isBind"+index+"\" name=\"isBind"+index+"\" ><option value=\"0\" selected=\"selected\">${msg['activity.no']}</option><option value=\"1\">${msg['activity.yes']}</option></select>\
    	        &nbsp;&nbsp;${msg['activity.numlimitbuy.num']}<input id=\"itemnumlimit"+index+"\" name=\"itemnumlimit"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[1],max[99999999]]\"/>\
    	        &nbsp;&nbsp;${msg['activity.buylimit.goldtype']}<select id=\"needGold"+index+"\" name=\"needGold"+index+"\" class=\"validate[required]\">\
                <option value=\"3\">${msg['activity.buylimit.notbindgold']}</option>\
                <option value=\"4\">${msg['activity.buylimit.bindgold']}</option></select>\
    	        &nbsp;&nbsp;${msg['activity.buylimit.goldnum']}<input id=\"goldNum"+index+"\" name=\"goldNum"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[1],max[99999999]]\"/>\
    	        ${msg['activity.rowText']}<input id=\"rowText"+index+"\" name=\"rowText"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\"/>\
    	        </div>";
    			break;
    		case 27:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.first']}"+index+"${msg['activity.di.last']}\
    	        <input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"60\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        </div>";
                break;
    		case 28:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.level']}"+index+"&nbsp;&nbsp;&nbsp;\
    			&nbsp;&nbsp;&nbsp;${msg['activity.recharge.lowrank']}<input id =\"lowRankNum"+index+"\" name=\"lowRankNum"+index+"\" type=text size=10 class=\"validate[required,custom[integer],min[1]]\"/>\
    			&nbsp;&nbsp;&nbsp;${msg['activity.recharge.highRank']}<input id =\"highRankNum"+index+"\" name=\"highRankNum"+index+"\" type=text size=10 class=\"validate[required,custom[integer],min[1]]\"/>\
    			&nbsp;&nbsp;&nbsp;${msg['activity.di.reward']}<input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"60\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        </div>";
                break;
    		 case 33:
    			 var timeLimitInfo = <%=request.getAttribute("TimeLimitGiftInfo")%>;
    			 if(timeLimitInfo != null){
    				//var timeLimitInfo = ${TimeLimitGiftInfo};
     				condition_html = "<div id=\"rewardConditionList"+index+"\">&nbsp;&nbsp;&nbsp;<select id=\"goldLevel"+index+"\" name=\"goldLevel"+index+"\" class=\"validate[required]\" >";
         			for(var i in timeLimitInfo ) {
     					var timeLimit = timeLimitInfo[i];
     					//console.log(timeLimit.id + "," + timeLimit.name);
     					condition_html += "<option value='" + timeLimit.id + "'>" + "[" + timeLimit.id + "]&nbsp;&nbsp;" + timeLimit.name + "</option>";
     				}
         			condition_html += "</select>&nbsp;&nbsp;&nbsp;${msg['activity.di.reward']}<input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"60\" class=\"validate[required]\" readonly/>\
         	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
         	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
         	        </div>";
    			 }

                break;
    		 case 35:
    			 condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.first']}"+index+"${msg['activity.di.day']}\
     	        <input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"60\" class=\"validate[required]\" readonly/>\
     	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
     	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
     	        </div>";
                 break;

    		 case 36:
    			 condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.reward']}\
     	        <input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"60\" class=\"validate[required]\" readonly/>\
     	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
     	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>&nbsp;&nbsp;URL\
     	        <input id =\"url"+index+"\" name=\"url"+index+"\" type=\"text\" size=\"60\" class=\"validate[required]\"/><br/><br/>\
     	        ${msg['activity.rowText']}&nbsp;<input id =\"rowText"+index+"\" name=\"rowText"+index+"\" type=\"text\" size=\"100\"/>\
     	        </div>";
                 break;

    		 case 101:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.first']}"+index+"${msg['activity.di.type']}\
    			 &nbsp;&nbsp;${msg['activity.glories.rewardId']}<input name=\"rewardId"+index+"\" id=\"rewardId"+index+"\" size=\"10\" type=\"text\" class=\"validate[required,custom[integer],min[1],max[99999999]]\"/>\
    			 &nbsp;&nbsp;${msg['activity.reward']}<input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"40\" class=\"validate[required]\" readonly/>\
     	         <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
     	         <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    			 &nbsp;&nbsp;${msg['activity.glories.needGlories']}<input id=\"needGlories"+index+"\" name=\"needGlories"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[1],max[99999999]]\"/>\
     	         &nbsp;&nbsp;${msg['activity.glories.exchangeLimit']}<input id=\"exchangeLimit"+index+"\" name=\"exchangeLimit"+index+"\" type=\"text\" class=\"validate[required,custom[integer],min[1],max[99999999]]\"/>\
     	        </div>";
    			 break;

    		default:
    			condition_html = "<div id=\"rewardConditionList"+index+"\">${msg['activity.di.first']}"+index+"${msg['activity.di.end']}\
    	        <input name=\"rewardList"+index+"\" id=\"rewardList"+index+"\" type=\"text\" size=\"60\" class=\"validate[required]\" readonly/>\
    	        <input type=\"button\" id=\"addItem"+index+"\" value=\"+\" onclick=\"itemShowBox("+index+");\"/><input type=\"button\" value=\"-\" onclick=\"deleItem("+index+");\"/>\
    	        <input type=\"button\" id=\"empty"+index+"\" onclick=\"rewardEmpty("+index+");\" value=\"${msg['activity.empty']}\"/>\
    	        &nbsp;&nbsp;&nbsp;${msg['activity.di.shangBanCondition']}<input id =\"rewardNum"+index+"\" name=\"rewardNum"+index+"\" type=text size=10 class=\"validate[required,custom[integer],min[1]]\"/></div>";
    	        break;
    	}
    	$("#selectCondition").append(condition_html);
        rewardIndex += 1;
    }

    function delete_top_condition(index){
    	if(index >= 1){
    		$("#rewardConditionList"+index).remove();
    		rewardIndex -= 1;
    	}
    }

    function rewardEmpty(index) {
		$("#rewardList"+index).val("");
	}

	function deleItem(index) {
		var tempItem = $("#rewardList"+index).val();
		if (tempItem != null && tempItem != "") {
			var strItem = "";
			var idx = tempItem.lastIndexOf(";");
			if (idx > -1) {
				strItem = tempItem.slice(0, idx);
			}
			$("#rewardList"+index).val(strItem);
		}
	}

	function itemShowBoxIn4(index) {
		$.ajax({
			url : base + "/global/getItemInfo",
			dataType : "json",
			success : function(jsonData) {
				var data = eval('(' + jsonData + ')');

				var boxy_html = "<form id='add_item' method='post' action='#'><div>${msg['activity.item.select']}<input id='itemId' list='itemId1' class='validate[required]' type='text' name='itemId_list' /><datalist id='itemId1'><option value='${msg['activity.item.itemId']}' selected='selected'>----${msg['activity.item']}----</option>";
				var idList = new Array();
				$.each(data, function(i) {
					var item = data[i];
					//console.log(item.itemId + "," + item.itemName + "," + item.itemMax);
					boxy_html += "<option value='" + item.itemId + "'>" + item.itemId + "_" + item.itemName + "_" + item.itemPrice + "_" + item.itemMax + "</option>";
					idList.push(item.itemId + "");
				});

				boxy_html += "</datalist>*<br/>${msg['activity.item.num']}<input type='text' id='itemNum' class='validate[required,custom[integer],min[1],max[2100000000]]'/>*\
						<br/>${msg['activity.item.bind']}<select id='isBind'><option value='' selected='selected'></option><option value='1'>${msg['activity.yes']}</option><option value='0'>${msg['activity.no']}</option></select>\
						<br/>${msg['activity.item.losttime']}<input id='lostTime' type='text' class='datetimepicker1' readonly/>\
					    <br/>${msg['activity.item.dropprob']}<input id='weight' type='text' class='validate[required]'/>*\
					    <br/>${msg['activity.item.career']}<select id='career'><option value='' selected='selected'></option><option value='0'>${msg['activity.item.career.a']}</option><option value='1'>${msg['activity.item.career.b']}</option><option value='2'>${msg['activity.item.career.c']}</option><option value='3'>${msg['activity.item.career.d']}</option><option value='4'>${msg['activity.item.career.e']}</option><option value='5'>${msg['activity.item.career.f']}</option></select>\
					    <br/><br/><input id='add' type='button' value='${msg['activity.item.add']}'/></div></form>";

		    	var boxy = new Boxy(boxy_html,
		        {
		    		title:"${msg['activity.item.select']}",        //Box标题
		    		closeText:"[${msg['activity.item.cancel']}]",     //Box关闭文字
		    		modal:true,           //Box背景是否变暗，此处必须为true，为false的话Boxy选择框会遮挡住弹出的时间选择控件
		    		fixed:false,          //Box窗口是否固定
		    		cache:false,          //Box是否被遮挡
		    		draggable:true,       //Box是否可以拖动，要定义title才有效，设定了modal就无效
		    		center:true,          //Box是否居中
		    		unloadOnHide:true,    //点击"取消"后box是隐藏并没有卸载掉，此处设置在隐藏后则卸掉
		    		afterShow:function() {
		    			$('.datetimepicker1').datetimepicker({  //日期时间选择器配置
		    				language:  'zh-CN',
		    			    format: 'yyyy-mm-dd hh:ii',
		    			    todayBtn: 1,
		    			    autoclose:true
		    			});
		    			$("#add_item").validationEngine();

		    			$("#add").click(function() {
		    				if ($('#add_item').validationEngine('validate')) { //物品信息验证通过后组装物品信息为字串填到奖励物品栏中
		    					if(jQuery.inArray($("#itemId").val(), idList) == -1){
		    						alert("${msg['activity.item.idnotexist']}");
		    						return ;
		    					}

		    					var itemInfo = $("#rewardList"+index).val();
		    					if (itemInfo != '') {
		    						itemInfo += ";";
		    					}

		    					itemInfo += $("#itemId").val() + "," + $("#itemNum").val();

		    					if ($("#career").val() != "") {
		    						itemInfo += ","  + $("#isBind").val() + "," + $("#lostTime").val() + "," + $("#weight").val() + "," + $("#career").val();
		    					} else if ($("#starLevel").val() != "") {
		    						itemInfo += ","  + $("#isBind").val() + "," + $("#lostTime").val() + "," + $("#weight").val();
		    					}  else if ($("#lostTime").val() != "") {
		    						itemInfo += ","  + $("#isBind").val() + "," + $("#lostTime").val();
		    					} else if ($("#isBind").val() != "") {
		    						itemInfo += ","  + $("#isBind").val();
		    					}

		    					//console.log(itemInfo);
		    					$("#rewardList"+index).val(itemInfo);

		    					Boxy.get(this).hideAndUnload(); //点击发布后隐藏并删除此对话框，直接unload()的话，模态框的暗背景不会消失
		    				}
		    			});

		    		} //打开对话框后执行的
		        });
		    	boxy.centerAt(600, 300);
		    	boxy.resize(350, 220);
			}
		});
	}

	function itemShowBox(index) {
		$.ajax({
			url : base + "/global/getItemInfo",
			dataType : "json",
			success : function(jsonData) {
				var data = eval('(' + jsonData + ')');

				var boxy_html = "<form id='add_item' method='post' action='#'><div>${msg['activity.item.select']}<input id='itemId' list='itemId1' class='validate[required]' type='text' name='itemId_list' /><datalist id='itemId1'><option value='${msg['activity.item.itemId']}' selected='selected'>----${msg['activity.item']}----</option>";
				var idList = new Array();
				$.each(data, function(i) {
					var item = data[i];
					//console.log(item.itemId + "," + item.itemName + "," + item.itemMax);
					boxy_html += "<option value='" + item.itemId + "'>" + item.itemId + "_" + item.itemName + "_" + item.itemPrice + "_" + item.itemMax + "</option>";
					idList.push(item.itemId + "");
				});

				boxy_html += "</datalist>*<br/>${msg['activity.item.num']}<input type='text' id='itemNum' class='validate[required,custom[integer],min[1],max[2100000000]]'/>*\
						<br/>${msg['activity.item.bind']}<select id='isBind'><option value='' selected='selected'></option><option value='1'>${msg['activity.yes']}</option><option value='0'>${msg['activity.no']}</option></select>\
						<br/>${msg['activity.item.losttime']}<input id='lostTime' type='text' class='datetimepicker1' readonly/>\
						<br/>${msg['activity.item.strenlv']}<input id='strenLevel' type='text' class='validate[custom[integer],min[0]]'/>\
					    <br/>${msg['activity.item.starlv']}<input id='starLevel' type='text' class='validate[custom[integer],min[0]]'/>\
					    <br/>${msg['activity.item.career']}<select id='career'><option value='' selected='selected'></option><option value='0'>${msg['activity.item.career.a']}</option><option value='1'>${msg['activity.item.career.b']}</option><option value='2'>${msg['activity.item.career.c']}</option><option value='3'>${msg['activity.item.career.d']}</option><option value='4'>${msg['activity.item.career.e']}</option><option value='5'>${msg['activity.item.career.f']}</option></select>\
					    <br/><br/><input id='add' type='button' value='${msg['activity.item.add']}'/></div></form>";

		    	var boxy = new Boxy(boxy_html,
		        {
		    		title:"${msg['activity.item.select']}",        //Box标题
		    		closeText:"[${msg['activity.item.cancel']}]",     //Box关闭文字
		    		modal:true,           //Box背景是否变暗，此处必须为true，为false的话Boxy选择框会遮挡住弹出的时间选择控件
		    		fixed:false,          //Box窗口是否固定
		    		cache:false,          //Box是否被遮挡
		    		draggable:true,       //Box是否可以拖动，要定义title才有效，设定了modal就无效
		    		center:true,          //Box是否居中
		    		unloadOnHide:true,    //点击"取消"后box是隐藏并没有卸载掉，此处设置在隐藏后则卸掉
		    		afterShow:function() {
		    			$('.datetimepicker1').datetimepicker({  //日期时间选择器配置
		    				language:  'zh-CN',
		    			    format: 'yyyy-mm-dd hh:ii',
		    			    todayBtn: 1,
		    			    autoclose:true
		    			});
		    			$("#add_item").validationEngine();

		    			$("#add").click(function() {
		    				if ($('#add_item').validationEngine('validate')) { //物品信息验证通过后组装物品信息为字串填到奖励物品栏中
		    					if(jQuery.inArray($("#itemId").val(), idList) == -1){
		    						alert("${msg['activity.item.idnotexist']}");
		    						return ;
		    					}

		    					var itemInfo = $("#rewardList"+index).val();
		    					if (itemInfo != '') {
		    						itemInfo += ";";
		    					}

		    					itemInfo += $("#itemId").val() + "," + $("#itemNum").val();

		    					if ($("#career").val() != "") {
		    						itemInfo += ","  + $("#isBind").val() + "," + $("#lostTime").val() + "," + $("#strenLevel").val() + "," + $("#starLevel").val() + "," + $("#career").val();
		    					} else if ($("#starLevel").val() != "") {
		    						itemInfo += ","  + $("#isBind").val() + "," + $("#lostTime").val() + "," + $("#strenLevel").val() + "," + $("#starLevel").val();
		    					} else if ($("#strenLevel").val() != "") {
		    						itemInfo += ","  + $("#isBind").val() + "," + $("#lostTime").val() + "," + $("#strenLevel").val();
		    					} else if ($("#lostTime").val() != "") {
		    						itemInfo += ","  + $("#isBind").val() + "," + $("#lostTime").val();
		    					} else if ($("#isBind").val() != "") {
		    						itemInfo += ","  + $("#isBind").val();
		    					}

		    					//console.log(itemInfo);
		    					$("#rewardList"+index).val(itemInfo);

		    					Boxy.get(this).hideAndUnload(); //点击发布后隐藏并删除此对话框，直接unload()的话，模态框的暗背景不会消失
		    				}
		    			});

		    		} //打开对话框后执行的
		        });
		    	boxy.centerAt(600, 300);
		    	boxy.resize(350, 220);
			}
		});
	}
	//加载活动模板时间列表，设置选择模板的下拉列表
	function activity_template_reload(actType) {
		$.post(base + "/activity/getTemplateTime", {actType:actType},
			function(dataResult, status) {
		    	//console.log(dataResult);
		    	var option_html = "<option value=''>-----</option>";
		    	for (var i = 0; i < dataResult.length; i++) {
				    var template = dataResult[i];
					option_html += "<option value='" + template.createTime + "'>" + template.templateName +"("+ template.createTime +")</option>";
		    	}
		    	//console.log("option_html=" + option_html);
		    	$("#template").empty();
		    	$("#template").append(option_html);
		});
	}

	//选择时间模板设置页面内容
	function set_template(createTime, actType) {
		$.post(base + "/activity/getTemplate", {createTime:createTime, actType:actType},
			function(dataResult, status) {
			    if (dataResult.ok) {
			    	var template = dataResult.data;
				    //设置活动基础信息
				    $("#name").val(template.name);
				    $("#labelPosition").val(template.labelPosition);
				    $("#labelOrder").val(template.labelOrder);
				    $("#numLimit").val(template.numLimit);
				    $("#beginTime").val(template.beginTime); //不予填充
				    $("#endTime").val(template.endTime);
				    $("#panelImageId").val(template.panelImageId);
				    $("#panelText").val(template.panelText);
				    $("#rewardList").val(template.rewardList);
				    $("#templateName").val(template.templateName);

				    //设置活动条件
				    switch(actType) {
				    case 1:
				    	var conArr = template.conditionList.split("#");

					    var timeArr = conArr[1].split("_");
					    $("#reBeginTime").val(timeArr[0]);
					    $("#reEndTime").val(timeArr[1]);
					    var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			var rechargeNumArr = conArr[0].split("_");
            			for( var i = 0; i < rewards.length; i++){
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,1);
							}
							$("#rechargeNum" + (i+1)).val(rechargeNumArr[i]);
							$("#rewardList" + (i+1)).val(rewards[i]);
						}

				    	break;
				    case 2:
				    	var conArr = template.conditionList.split("#");

					    var timeArr = conArr[1].split("_");
					    $("#coBeginTime").val(timeArr[0]);
					    $("#coEndTime").val(timeArr[1]);
					    var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			var consumeNumArr = conArr[0].split("_");
            			for( var i = 0; i < rewards.length; i++){
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,2);
							}
							$("#consumeNum" + (i+1)).val(consumeNumArr[i]);
							$("#rewardList" + (i+1)).val(rewards[i]);
						}
				    	break;
				    case 3:
						var conArr = template.conditionList.split("#");
					    $("#loopType").val(conArr[1]);
					    var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			var conditionArr = conArr[0].split("_");
            			for( var i = 0; i < rewards.length; i++){
           					var condition = conditionArr[i].split("-");
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,3);
							}
							$("#needItem" + (i+1)).val(condition[0]);
							$("#maxCount" + (i+1)).val(condition[1]);
							$("#eveMaxCount" + (i+1)).val(condition[2]);
							$("#rewardList" + (i+1)).val(rewards[i]);
						}
				    	break;
				    case 4:
				    	var conArr = template.conditionList.split("#");

				    	var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();

            			for( var i = 0; i < rewards.length; i++){
           					var condition = conArr[i].split("_");
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,4);
							}
							$("#rewardList" + (i+1)).val(rewards[i]);
							$("#dropProb" + (i+1)).val(condition[0]);
							var dropType = condition[1];
						    $("#dropType"+(i+1)).val(dropType);
							if (dropType == 1) {
				        		$("#mapIdinput"+(i+1)).css("display","none");//隐藏div
				        		$("#monsterIdinput"+(i+1)).css("display","none");//隐藏div
							} else if (dropType == 2) {
								$("#mapIdinput"+(i+1)).css("display","inline");//显示div
								$("#mapId"+(i+1)).val(condition[2]);
				        		$("#monsterIdinput").css("display","none");//隐藏div
							} else if (dropType == 3) {
								$("#mapIdinput"+(i+1)).css("display","none");//隐藏div
				        		$("#monsterIdinput"+(i+1)).css("display","inline");//显示div
				        		$("#monsterId"+(i+1)).val(condition[2]);
							}
							$("#dropLimit"+(i+1)).val(condition[3]);
							$("#perLimit"+(i+1)).val(condition[4]);
							var isNotice = condition[5];
							$("#isNotice"+(i+1)).val(isNotice);
							if (isNotice == 1) {
								$("#contentinput"+(i+1)).css("display","inline");//显示div
								$("#content"+(i+1)).val(condition[6]);
							} else {
								$("#contentinput"+(i+1)).css("display","none");//隐藏div
							}
						}
				    	break;
				    case 5:
				    	var conArr = template.conditionList.split("#");
				    	 var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			for( var i = 0; i < conArr.length; i++){
           					var condition = conArr[i].split("_");
							var ac = $("#monsterMapId" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,5);
							}
							$("#monsterMapId" + (i+1)).val(condition[0]);
							$("#monsterId" + (i+1)).val(condition[1]);
							$("#monsterNum" + (i+1)).val(condition[2]);
							$("#monsterXY" + (i+1)).val(condition[3]);
							$("#timeInterval" + (i+1)).val(condition[4]);
						}
				    	break;
				    case 6:
						var conArr = template.conditionList.split("#");

						$("#loopType").val(conArr[1]);
					    var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			var rechargeNumArr = conArr[0].split("_");
            			for( var i = 0; i < rewards.length; i++){
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,6);
							}
							$("#rechargeNum" + (i+1)).val(rechargeNumArr[i]);
							$("#rewardList" + (i+1)).val(rewards[i]);
						}

				    	break;
				    case 7:
						var conArr = template.conditionList.split("#");
						$("#loopType").val(conArr[1]);
					    var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			var consumeNumArr = conArr[0].split("_");
            			for( var i = 0; i < rewards.length; i++){
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,7);
							}
							$("#consumeNum" + (i+1)).val(consumeNumArr[i]);
							$("#rewardList" + (i+1)).val(rewards[i]);
						}
				    	break;
				    case 8:
				    	var conArr = template.conditionList.split("#");
				    	var isOnceAgain = conArr[1];
				    	var conList = conArr[0].split("_");
				    	var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
				    	for (var i = 0; i < rewards.length; i++) {
				    		var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,8);
							}
				    		var con = conList[i].split(",");
				    		var condition = con[0];
				    		$("#condition"+(i+1)).val(condition);
				    		switch(condition) {
					    		case "2":
					    			var detail_condition ="<input id=\"level"+(i+1)+"\" name=\"level"+(i+1)+"\" type='text' size='5' class='validate[required,custom[integer],min[0]]'/>${msg['activity.receive.condition.lv']}";
					       			$("#conditionDetail"+(i+1)).html(detail_condition);
					    			$("#level"+(i+1)).val(con[1]);
					    			break;
					    		case "9":
					    			var detail_condition = "${msg['activity.receive.condition.vip.lv']}<input id=\"vipLevel"+(i+1)+"\" name=\"vipLevel"+(i+1)+"\" type='text' size='8' class='validate[required,custom[integer],min[1],max[12]]'/>";
					    			$("#conditionDetail"+(i+1)).html(detail_condition);
					    			$("#vipLevel"+(i+1)).val(con[1]);
					    			break;
					    		case "10":
					    			var detail_condition = "${msg['activity.receive.condition.equip.tostar']}<input id=\"equipTotalStar"+(i+1)+"\" name=\"equipTotalStar"+(i+1)+"\" type='text' size='8' class='validate[required,custom[integer],min[0]]'/>";
					       			$("#conditionDetail"+(i+1)).html(detail_condition);
					    			$("#equipTotalStar"+(i+1)).val(con[1]);
					    			break;
					    		case "11":
					    			var detail_condition = "${msg['activity.receive.condition.equip.id']}<input id=\"equipID"+(i+1)+"\" name=\"equipID"+(i+1)+"\" type='text' size='8' class='validate[required,custom[integer],min[0]]'/>\
	   				                	&nbsp;${msg['activity.receive.condition.equip.id.star']}<input id=\"needStar1"+(i+1)+"\" name=\"needStar1"+(i+1)+"\" type='text' size='8' class='validate[required,custom[integer],min[0]]'/>";
					    			$("#conditionDetail"+(i+1)).val(detail_condition);
					    			$("#equipID"+(i+1)).val(con[1]);
					    			$("#needStar1"+(i+1)).val(con[2]);
					    			break;
					    		case "12":
					    			var detail_condition = "${msg['activity.receive.condition.equip.pos']}<input id=\"equipPos"+(i+1)+"\" name=\"equipPos"+(i+1)+"\" type='text' size='8' class='validate[required,custom[integer],min[0],max[10]]'/>\
	   				                	&nbsp; ${msg['activity.receive.condition.equip.id.star']}<input id=\"needStar2"+(i+1)+"\" name=\"needStar2"+(i+1)+"\" type='text' size='8' class='validate[required,custom[integer],min[0]]'/>";
					       			$("#conditionDetail"+(i+1)).html(detail_condition);
					    			$("#equipPos"+(i+1)).val(con[1]);
					    			$("#needStar2"+(i+1)).val(con[2]);
					    			break;
					    		case "13":
					    			var detail_condition = "${msg['activity.receive.condition.equip.tostrenlv']}<input id=\"totalStrengthLevel"+(i+1)+"\" name=\"totalStrengthLevel"+(i+1)+"\" type='text' size='8' class='validate[required,custom[integer],min[0]]'/>";
					       			$("#conditionDetail"+(i+1)).html(detail_condition);
					    			$("#totalStrengthLevel"+(i+1)).val(con[1]);
					    			break;
					    		default:
					    			break;
				    		}
				    		$("#rewardList" + (i+1)).val(rewards[i]);
				    	}
				    	break;
				    case 9:
				    	var conArr = template.conditionList.split("#");
				    	var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			var itemIdSet = false;
            			for( var i = 0; i < rewards.length; i++){
            				var condition = conArr[i].split("_");
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,9);
							}
							if(itemIdSet == false){
								$("#giftItemId").val(condition[0]);
								itemIdSet = true;
							}
							$("#loopType" + (i+1)).val(condition[1]);
							$("#sendNum" + (i+1)).val(condition[2]);
							$("#rewardList" + (i+1)).val(rewards[i]);
						}
				    	break;
				    case 10:
				    	var conArr = template.conditionList.split("#");

					    var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			var itemIdSet = false;
            			for( var i = 0; i < rewards.length; i++){
            				var condition = conArr[i].split("_");
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,10);
							}
							if(itemIdSet == false){
								$("#giftItemId").val(condition[0]);
								itemIdSet = true;
							}
							$("#loopType" + (i+1)).val(condition[1]);
							$("#receiveNum" + (i+1)).val(condition[2]);
							$("#rewardList" + (i+1)).val(rewards[i]);
						}
				    	break;
				    case 11:
				    case 12:
				    case 13:
				    case 14:
				    	var conArr = template.conditionList.split("|");

            			var timeArr = conArr[0].split("_");
            			$("#dayStartTime").val(timeArr[0]);
            			$("#dayEndTime").val(timeArr[1]);

            			rewardIndex = 1;
            			$("#selectCondition").empty();
            			for( var i = 1; i < conArr.length; ++i){
							var ac = $("#rewardList" + i).length;
							if( ac < 1){
								add_top_condition(i);
							}
							var text = conArr[i].split("_");
							$("#rewardList" + i).val(text[2]);
							$("#rewardNum"+ i).val(text[1]);
						}
				    	break;

				    case 15:
            			var conArr = template.conditionList.split("_");
            			$("#mailminlevel").val(conArr[0]);
            			$("#mailmaxlevel").val(conArr[1]);
            			$("#mailStartTime").val(conArr[2]);
            			$("#mailEndTime").val(conArr[3]);
            			$("#isServer").val(conArr[4]);
				    	break;

				    case 17:
            			var conArr = template.conditionList.split("#");
					    var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			for( var i = 0; i < rewards.length; i++){
            				var condition = conArr[i].split("_");
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,17);
							}

							$("#rewardList" + (i+1)).val(rewards[i]);
							$("#needGold" + (i+1)).val(condition[0]);
							$("#goldNum" + (i+1)).val(condition[1]);
							$("#loopType" + (i+1)).val(condition[2]);
							$("#buyDiscount" + (i+1)).val(condition[3]);
							$("#limitTimes" + (i+1)).val(condition[3]);
						}
				    	break;

				    case 19:
            			var conArr = template.conditionList.split("_");
            			$("#multiple").val(conArr[0]);
				    	break;
				    case 20:
            			var conArr = template.conditionList.split(";");
            			for(var  i  = 0 ; i < conArr.length; i++){
            				var conArr2=conArr[i].split("#");
            				var condition =conArr2[0].split("_");
            				$("#selectType"+(i+1)).val(condition[1]);
            				selectType(i+1);
            				$("#conditionList"+(i+1)).val(condition[2]);
            				$("#probability"+(i+1)).val(condition[3]);
            				$("#notice"+(i+1)).val(condition[4]);
            				$("#effect"+(i+1)).val(condition[5]);
            				$("#joinCounts").val(conArr2[1]);
            			}
            			var rewArr = template.rewardList.split(";");
            			if(rewArr.length!=1){
                			for(var  i  = 0 ; i < rewArr.length; i++){
                				var reward =rewArr.split("_");
    							var ac = $("#rewardList" +(i+1)).length;
    							if( ac < 1){
    								add_top_condition(i+1,20);
    							}
    							$("#rewardId"+(i+1)).val(reward[0]);
    							$("#rewardList"+(i+1)).val(reward[1]);
    							$("#integral"+(i+1)).val(reward[2]);
    							$("#changeCount"+(i+1)).val(reward[3]);
                			}
            			}
            			var reward =template.rewardList.split("_");
						$("#rewardId1").val(reward[0]);
						$("#rewardList1").val(reward[1]);
						$("#integral1").val(reward[2]);
						$("#changeCount1").val(reward[3]);
				    	break;
				    case 22:
            			$("#bossInfo").val(template.conditionList);
				    	break;

				    case 23:
				    	var conArr = template.conditionList.split("#");
				    	for(var  i  = 0 ; i < (conArr.length-1); i++){
				    		var condition = conArr[i].split("_");
				    		var ac = $("#itemId" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,23);
							}
				    		$("#itemId"+(i+1)).val(condition[0]);
	            			$("#isBind"+(i+1)).val(condition[1]);
	            			$("#itemnumlimit"+(i+1)).val(condition[2]);
	            			$("#needGold"+(i+1)).val(condition[3]);
	            			$("#goldNum"+(i+1)).val(condition[4]);
				    	}
				    	break;
				    case 24:
            			var conArr = template.conditionList.split("_");
            			$("#rate").val(conArr[0]);
				    	break;

				    case 27:
						var conArr = template.conditionList.split("_");

            			$("#rechargeCount").val(conArr[0]);
            			$("#rechargeValue").val(conArr[1]);
						var rewards = conArr[2].split("|");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			for( var i = 0; i < rewards.length; i++){
							var ac = $("#rewardList" + (i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,27);
							}
							var text = rewards[i].split(":");
							$("#rewardList" + (i+1)).val(text[1]);
						}
            			$("#conmulateRewardList").val(template.rewardList);
				    	break;
				    case 28:
						var conArr = template.conditionList.split("|");
				    	var timeArr = conArr[0].split("_");
            			$("#dayStartTime").val(timeArr[0]);
            			$("#dayEndTime").val(timeArr[1]);
            			$("#rankStyle").val(conArr[1]);
						var rewards = conArr[2].split(":");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			for( var i = 0; i < rewards.length; i++){
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,28);
							}
							var text = rewards[i].split("_");
							$("#rewardList" + (i+1)).val(text[3]);
							$("#lowRankNum" + (i+1)).val(text[1]);
							$("#highRankNum" + (i+1)).val(text[2]);
						}
				    	break;
				    case 33:
				    	var rewards = template.conditionList.split("|");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			for( var i = 0; i < rewards.length; i++){
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,33);
							}
							var text = rewards[i].split("_");
							$("#goldLevel" + (i+1)).val(text[0]);
							$("#rewardList" + (i+1)).val(text[1]);
						}
				    	break;
				    case 34:
						var conArr = template.conditionList.split("_");
						var rewardLists = template.rewardList;

            			/* $("#rechargeCount").val(conArr[0]);
            			$("#rechargeValue").val(conArr[1]); */
						var rewards = rewardLists.split(";");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			for( var i = 0; i < rewards.length; i++){
							var ac = $("#rewardList" + (i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,34);
							}
							$("#rewardList" + (i+1)).val(rewards[i]);
						}
            			var probabilityArr = conArr[0].split(",");
            			for(var j = 0; j < probabilityArr.length; j++){
            				var ac = $("#probability" + (j+1)).length;
							if( ac < 1){
								add_top_condition(j+1,34);
							}
							$("#probability" + (j+1)).val(probabilityArr[j]);
            			}
            			$("#luckyRewardList").val(conArr[1]);
            			$("#joinCounts").val(conArr[2]);
            			$("#copies").val(conArr[3]);
				    	break;
				    case 35:
				    	var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			for( var i = 0; i < rewards.length; i++){
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,35);
							}
							$("#rewardList" + (i+1)).val(rewards[i]);
						}
            			var conArr = template.conditionList;
            			$("#isOnceAgain").val(conArr);
				    	break;
				    case 36:
				    	var rewards = template.rewardList.split("_");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			for( var i = 0; i < rewards.length; i++){
							var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,36);
							}
							$("#rewardList" + (i+1)).val(rewards[i]);
						}
				    	break;
				    case 101:
				    	$("#gloBeginTime").val(template.submitBeginTime);
				    	$("#gloEndTime").val(template.submitEndTime);
				    	$("#tag").val(template.tag);
				    	var rewards = template.otherList.split("#");
				    	var conArr = template.conditionList.split("#");
						rewardIndex = 1;
            			$("#selectCondition").empty();
            			for( var i = 0; i < rewards.length; i++){
							var ac = $("#rewardId" +(i+1)).length;
							if( ac < 1){
								add_top_condition(i+1,101);
							}
							//console.log(i+"==="+rewards[i]);
							if(rewards[i]=="" || rewards[i]==null){
								continue;
							}
							var rewardStr = rewards[i].split("_");
							$("#rewardId" + (i+1)).val(rewardStr[0]);
							$("#needGlories" + (i+1)).val(rewardStr[1]);
							$("#exchangeLimit" + (i+1)).val(rewardStr[2]);
							$("#rewardList" + (i+1)).val(rewardStr[3]);
						}
            			for( var i = 0; i < conArr.length; i++){
							var ac = $("#getGloriesWay" +(i+1)).length;
							if( ac < 1){
								add_condition(i+1,101);
							}
							//console.log(i+"==="+conArr[i]);
							if(conArr[i]=="" || conArr[i]==null){
								continue;
							}
							var conditionStr = conArr[i].split("_");
							var gloriesWay = conditionStr[0].split(",");
							if(gloriesWay.length > 1){
								switch(gloriesWay[0]){
									case "1":
										var detail_condition ="${msg['activity.glories.itemId']}<input id=\"itemId"+(i+1)+"\" name=\"itemId"+(i+1)+"\" type='text' size='10' class='validate[required,custom[integer],min[0]]'/>";
										$("#conditionDetail"+(i+1)).html(detail_condition);
										$("#itemId" + (i+1)).val(gloriesWay[1]);
										break;
									case "2":
										var detail_condition = "${msg['activity.glories.bossId']}<input id=\"bossId"+(i+1)+"\" name=\"bossId"+(i+1)+"\" type='text' size='10' class='validate[required,custom[integer],min[0]'/>";
										$("#conditionDetail"+(i+1)).html(detail_condition);
										$("#bossId" + (i+1)).val(gloriesWay[1]);
										break;
									case "6":
										var detail_condition = "${msg['activity.glories.mapId']}<input id=\"mapId"+index+"\" name=\"mapId"+index+"\" type='text' size='10' class='validate[required,custom[integer],min[0]'/>";
							   			$("#conditionDetail"+(i+1)).html(detail_condition);
										$("#mapId" + (i+1)).val(gloriesWay[1]);
										break;
								}
								$("#getGloriesWay" + (i+1)).val(gloriesWay[0]);
							}else{
								$("#getGloriesWay" + (i+1)).val(conditionStr[0]);
							}

							$("#getNumEveTimes" + (i+1)).val(conditionStr[1]);
							$("#maxNum" + (i+1)).val(conditionStr[2]);
						}
				    	break;
				    case 102:
				    	$("#personalRewardList").val(template.rewardList);
				    	$("#subBeginTime").val(template.submitBeginTime);
				    	$("#subEndTime").val(template.submitEndTime);
				    	$("#tag").val(template.tag);

				    	var conArr = template.conditionList.split("#");
				    	$("#materialId").val(conArr[0]);
				    	var condition = conArr[1].split("@");
				    	for(var i = 0 ; i < condition.length ; i++ ){
				    		var ac = $("#alRewardType" +(i+1)).length;
							if( ac < 1){
								add_condition(i+1,"allServer");
							}

							var conditionStr = condition[i].split("_");
							$("#alRewardType" + (i+1)).val(conditionStr[0]);
							if(conditionStr.length > 2){
								switch(conditionStr[0]){
									case "2":
										var detail_condition ="${msg['activity.submitMaterial.itemId']}\
								   			<input name=\"alItemId"+(i+1)+"\" id=\"alItemId"+(i+1)+"\" type=\"text\" size=\"60\" class=\"validate[required]\" readonly/>\
								            <input type=\"button\" id=\"addAlItemId"+(i+1)+"\" value=\"+\" onclick=\"itemsShowBox("+(i+1)+");\"/>\
								            <input type=\"button\" id=\"delAlItemId"+(i+1)+"\" value=\"-\" onclick=\"deleItems("+(i+1)+");\" />\
								            <input type=\"button\" id=\"alItemIdEmpty"+(i+1)+"\" value=\"${msg['activity.empty']}\" onclick=\"itemsEmpty("+(i+1)+");\"/>";
								   		$("#alConditionDetail"+(i+1)).html(detail_condition);
										$("#alItemId" + (i+1)).val(conditionStr[1]);
										break;
								}

								$("#alNum" + (i+1)).val(conditionStr[2]);
							}else{
								$("#alNum" + (i+1)).val(conditionStr[1]);
							}
				    	}
				    	$("#itemIds").val(conArr[2]);

				    	var rewards = template.otherList.split("#");
				    	for(var i = 0 ; i < rewards.length ; i++){
				    		var ac = $("#rewardList" +(i+1)).length;
							if( ac < 1){
								add_condition(i+1,"personal");
							}
							var rewardStr = rewards[i].split("_");
							$("#rewardList" + (i+1)).val(rewardStr[0]);
							$("#perNum" + (i+1)).val(rewardStr[1]);
				    	}

				    	break;
				    default:
				    	break;
				    }
			    } else {
			    	alert(dataResult.msg);
			    }
		});
	}

    /**
    *批量提交（删除操作）
    **/
    function submitDeleteBatch(optType){
    	var name = optType+"Act";
    	var actId =[];
		$("input[name="+name+"]:checked").each(function(){
			actId.push($(this).val());
		});
		if(actId.length == 0){
			alert("${msg['activity.delete.choose']}");
			return;
		}
		if(!confirm("${msg['activity.delete.confirm']}")){
			return;
		}
		$.ajax({
    		type : "POST",
    		url : base + "/activity/deleteBatch",
    		data : {
    			actIds : actId+","
    		},
			dataType : "json",
			async: false,
			success : function(dataResult) {
				var promptInfo = dataResult.msg;
				alert(promptInfo);
				location.reload();
			}
    	});
    }
    /**
     *一键删除过期活动
     **/
     function submitDeleteAllExpired(actType){
 		if(!confirm("Are you sure ?")){
 			return;
 		}
 		$.ajax({
     		type : "POST",
     		url : base + "/activity/deleteAllExpiredActivity",
     		data : {
     			actType : actType
     		},
 			dataType : "json",
 			async: false,
 			success : function(dataResult) {
 				var promptInfo = dataResult.msg;
 				alert(promptInfo);
 				location.reload();
 			}
     	});
     }
    /**
    *批量提交（测试）
    **/
    function submitReleaseBatch(optType,serverType,actType){
    	var name = optType+"Act";
    	var actId =[];
		$("input[name="+name+"]:checked").each(function(){
			actId.push($(this).val());
		});
		if(actId.length == 0){
			alert("please check some activities firstly!");
			return;
		}

    	$.ajax({
			url : base + "/activity/queryPlatSid",
			data : {
				"serverType" : serverType
			},
			dataType : "json",
			success : function(jsonData) {
				var boxy_html = "<div>${msg['activity.release.plat.select']}<br/><select id='platform'><option value='' selected='selected'>--------${msg['activity.release.plat']}--------</option>";
				var data = eval('(' + jsonData + ')');
				$.each(data, function(key, value) {
					console.log(key);
					var plat_option = "<option value='" + key + "'>" + key + "</option>";
					boxy_html += plat_option;
				});
				boxy_html += "</select></div><span>${msg['activity.release.sid.select']}</span><div id='sid'></div><div><input type='checkbox' id='selAll'>${msg['activity.selectall']}&nbsp;&nbsp;<input type='button' id='release' value='${msg['activity.release']}'></div>";

				var width = 400;
				var height = 10;
		    	var boxy = new Boxy(boxy_html,
		        {
		    		       title:"${msg['activity.release.act']}", //Box标题
		    		       closeText:"[${msg['activity.item.cancel']}]",     //Box关闭文字
		    		       modal:false,          //Box背景是否变暗
		    		       fixed:false,          //Box窗口是否固定
		    		       cache:false,          //Box是否被遮挡
		    		       draggable:true,       //Box是否可以拖动，要定义title才有效，设定了modal就无效
		    		       center:true,          //Box是否居中
		    		       unloadOnHide:true,    //点击"取消"后box是隐藏并没有卸载掉，此处设置在隐藏后则卸掉
		    		       afterShow:function() {
		    		    	   //选择不同平台展示该平台下的区服列表
							   $("#platform").change(function() {
								   var plat = $("#platform").val();
								   var sid_html = "";
								   if (plat != '') {
									   var size=0;
									   $.each(data, function(key, value) {
											if (key == plat) {
												$.each(value, function(i) {
													var server = value[i].split("_");
													sid_html += "<input type='checkbox' name='serverId' value='" + server[0] + "'/>" + server[1] + "&nbsp;";
													size++;
												});
											}
										});
								   }
								   $("#sid").html(sid_html);
								   boxy.resize(width+size*2, height+size*4);
							   });
		    		    	   $("#selAll").click(function() {
		    		    	   		var s = $(this).is(":checked");
		    		    	   		$("[name='serverId']").attr("checked", s);
		    		    	   });
		    		    	   $("#release").click(function() {
		    		    	       var platform = $("#platform").val();
		    		    	       var sid = [];
		                           $("input[name='serverId']:checked").each(function() {
		                           		sid.push($(this).val());
		                           });
		                           if(sid.length == 0){
		                           		return;
		                           }
		                           $.post(
		                           base + "/activity/sendBatch",
		                           {
		                           		actIds : actId+",",
		                            	platform : platform,
		                            	sids : sid+",",
		                            	serverType : serverType
		                           },
		                           function(data, status) {
		                           		alert(data.msg);
		                           		location.reload();
		                           });
		                           Boxy.get(this).unload();
		    		           });
		    		           //selAll
		    		       } //打开对话框后执行的
		        });
		    	boxy.centerAt(800, 400);
		    	//boxy.resize(300, 20);
			}
		});
    }
    $(function() {
    	$("#delTemplate").click(function() {
    		var templateTime = $("#template option:selected").val();
    		if(confirm("确定删除该模板？")){
    			$.post(
    				base + "/activity/deleteActivity",
		    		{actType : actType,templateTime : templateTime},
		    		function(data, status) {
		    			alert(data.msg);
		     			location.reload();
		    		}
		    	);
    		}
    	});
    });
    function checkAll(e,itemName){    //全选函数
        var aa = document.getElementsByName(itemName);
     for(var i=0;i<aa.length;i++){
         aa[i].checked = e.checked;

     }
    }
    function checkItem(e, allName) {
        var ab = document.getElementsByName(e.name);
        var Y=true;//假设已经是全选
        for (var i = 0; i < ab.length; i++) {
            if (!ab[i].checked) {
                Y=false;
                break;
            }
        }
        document.getElementsByName(allName)[0].checked = Y;//全选框对象
    }

    function getMap(){//初始化map，给map对象增加方法，使map像个Map
        var map=new Object();
        map.put=function(key,value){
        	map[key]=value;
        }
        map.get=function(key){
        	return map[key];
        }
        return map;
    }
    //初始化加载itemInfo信息
    function getItemInfoList(){
    	var map=getMap();
    	$.ajax({
			url : base + "/global/getItemInfo",
			dataType : "json",
			success : function(jsonData) {
				var data = eval('(' + jsonData + ')');
				$.each(data, function(i) {
					var item = data[i];
					//console.log(item.itemId+"--"+item.itemPrice);
					map.put(item.itemId,item.itemPrice);
				});
			}
		});
    	return map;
    }
    </script>


