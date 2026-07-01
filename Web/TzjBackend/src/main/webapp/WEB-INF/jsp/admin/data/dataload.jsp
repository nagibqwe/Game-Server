<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.datareload.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.ajaxupload.js"></script>
<script type="text/javascript">
	var base = '${base}';
	function reload(name){
		$.ajax({
			url : base + "/admin/" + name,
			dataType : "json",
			success : function(data) {
				if (!data.ok) {
					alert("${msg['jsp.dataload.loadfail']}");
				}else{
					alert("${msg['jsp.dataload.loadfinish']}");
				}
			}
		});
	}

	function loadItem() {
		var data = new FormData();
		data.append('itemFile', $("#items").prop('files')[0]);
		$.ajax({
			url: base + "/admin/loadItem",
			method: 'POST',
			data: data,
			processData: false,
			contentType: false,
			success: function (data) {
				$("#itemLoadModel").modal('hide');
				alert(data.msg);
			}
		});
	}

	function loadFunction() {
		var data = new FormData();
		data.append('functionFile', $("#functions").prop('files')[0]);
		$.ajax({
			url: base + "/admin/loadFunction",
			method: 'POST',
			data: data,
			processData: false,
			contentType: false,
			success: function (data) {
				$("#functionLoadModel").modal('hide');
				alert(data.msg);
			}
		});
	}

	function loadItemChangeReason() {
		var data = new FormData();
		data.append('reasonFile', $("#changeReasonFile").prop('files')[0]);
		$.ajax({
			url: base + "/admin/loadChangeReason",
			method: 'POST',
			data: data,
			processData: false,
			contentType: false,
			success: function (data) {
				$("#changeReasonLoadModel").modal('hide');
				alert(data.msg);
			}
		});
	}

	function loadQuestionnaire() {
		var data = new FormData();
		data.append('questionnaireFile', $("#questionnaireFile").prop('files')[0]);
		$.ajax({
			url: base + "/admin/loadQuestionnaire",
			method: 'POST',
			data: data,
			processData: false,
			contentType: false,
			success: function (data) {
				$("#questionnaireLoadModel").modal('hide');
				alert(data.msg);
			}
		});
	}

	function loadActivityBossType() {
		var data = new FormData();
		data.append('activityBossTypeFile', $("#activityBossTypeFile").prop('files')[0]);
		$.ajax({
			url: base + "/admin/loadActivityBossType",
			method: 'POST',
			data: data,
			processData: false,
			contentType: false,
			success: function (data) {
				$("#activityBossTypeLoadModel").modal('hide');
				alert(data.msg);
			}
		});
	}

	function loadActivityFestivalType() {
		var data = new FormData();
		data.append('activityFestivalTypeFile', $("#activityFestivalTypeFile").prop('files')[0]);
		$.ajax({
			url: base + "/admin/loadActivityFestivalType",
			method: 'POST',
			data: data,
			processData: false,
			contentType: false,
			success: function (data) {
				$("#activityFestivalTypeLoadModel").modal('hide');
				alert(data.msg);
			}
		});
	}

</script>
</head>
<body>
	<br/>
	<div class="container-fluid">
		<table class="table table-bordered table-striped">
			<thead>
				<tr>
					<th>功能模块</th>
					<th>${msg['jsp.dataload.data']}</th>
					<th>${msg['jsp.dataload.action']}</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td rowspan="5" style="vertical-align: middle;"><p class="text-center">服务器信息</p></td>
					<td>所有服务器相关信息</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadAllServer');"></td>
				</tr>
				<tr>
					<td>登录服相关数据(t_dblog和t_server表),相关配置文件:lslog.js,db_lslog.properties</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadLoginServer');"></td>
				</tr>
				<tr>
					<td>游戏服日志库连接信息(t_dblog)</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadPlatFormServerInfo');"></td>
				</tr>
				<tr>
					<td>游戏服服务器连接信息(t_server表)</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadServerList');"></td>
				</tr>
				<tr>
					<td>跨服、公共服服务器及DB连接相关信息(t_dblog和t_server表)</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadPublic');"></td>
				</tr>
				<tr>
					<td rowspan="5" style="vertical-align: middle;"><p class="text-center">配置文件相关</p></td>
					<td>所有GM后台配置文件</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadAllConfigFile');"></td>
				</tr>
				<tr>
					<td>货币汇率配置。相关配置文件:currency.properties</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadCurrencyRateConfig');"></td>
				</tr>
				<tr>
					<td>日志查询字段配置。相关配置文件:fields.js</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadLogFieldConfig');"></td>
				</tr>
				<tr>
					<td>服务器语言包配置。相关配置文件:languageNo.properties</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadServerLanguageConfig');"></td>
				</tr>
				<tr>
					<td>原因码配置。相关配置文件:reasons.properties</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadReasonConfig');" disabled></td>
				</tr>
				
				<tr>
					<td rowspan="9" style="vertical-align: middle;"><p class="text-center">其他数据</p></td>
					<td>公告数据加载</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadAnnouce');"></td>
				</tr>
				<tr>
					<td>黑名单数据加载</td>
					<td><input type="button" value="${msg['jsp.dataload.reload']}" class="btn btn-primary" onclick="reload('reloadBlackList');"></td>
				</tr>
				<tr>
					<td>物品装备数据加载</td>
					<td><a href="#itemLoadModel" role="button" class="btn btn-primary" data-toggle="modal">重新加载</a></td>
				</tr>
				<tr>
					<td>功能开放列表加载</td>
					<td><a href="#functionLoadModel" role="button" class="btn btn-primary" data-toggle="modal">重新加载</a></td>
				</tr>
				<tr>
					<td>原因码加载</td>
					<td><a href="#changeReasonLoadModel" role="button" class="btn btn-primary" data-toggle="modal">重新加载</a></td>
				</tr>
				<tr>
					<td>有奖问答加载</td>
					<td><a href="#questionnaireLoadModel" role="button" class="btn btn-primary" data-toggle="modal">重新加载</a></td>
				</tr>
				<tr>
					<td>运营活动BOSS类型加载</td>
					<td><a href="#activityBossTypeLoadModel" role="button" class="btn btn-primary" data-toggle="modal">重新加载</a></td>
				</tr>
				<tr>
					<td>运营活动节日类型加载</td>
					<td><a href="#activityFestivalTypeLoadModel" role="button" class="btn btn-primary" data-toggle="modal">重新加载</a></td>
				</tr>
			</tbody>
		</table>

		<div id="itemLoadModel" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3>物品装备表</h3>
			</div>
			<div class="modal-body">
				<input id="items" type="file" name="itemFile"/>
				<label>注意，先加载item.xls，再加载equip.xls</label>
			</div>
			<div class="modal-footer">
				<input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
				<input type="button" value="上传" onclick="loadItem()" class="btn btn-primary"/>
			</div>
		</div>

		<div id="functionLoadModel" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3>功能开放配置表</h3>
			</div>
			<div class="modal-body">
				<input id="functions" type="file" name="functionFile"/>
			</div>
			<div class="modal-footer">
				<input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
				<input type="button" value="上传" onclick="loadFunction()" class="btn btn-primary"/>
			</div>
		</div>

		<div id="changeReasonLoadModel" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3>原因码配置表</h3>
			</div>
			<div class="modal-body">
				<input id="changeReasonFile" type="file" name="changeReasonFile"/>
			</div>
			<div class="modal-footer">
				<input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
				<input type="button" value="上传" onclick="loadItemChangeReason()" class="btn btn-primary"/>
			</div>
		</div>

		<div id="questionnaireLoadModel" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3>有奖问卷配置表</h3>
			</div>
			<div class="modal-body">
				<input id="questionnaireFile" type="file" name="questionnaireFile"/>
			</div>
			<div class="modal-footer">
				<input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
				<input type="button" value="上传" onclick="loadQuestionnaire()" class="btn btn-primary"/>
			</div>
		</div>

		<div id="activityBossTypeLoadModel" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3>运营活动BOSS类型配置表</h3>
			</div>
			<div class="modal-body">
				<input id="activityBossTypeFile" type="file" name="activityBossTypeFile"/>
			</div>
			<div class="modal-footer">
				<input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
				<input type="button" value="上传" onclick="loadActivityBossType()" class="btn btn-primary"/>
			</div>
		</div>

		<div id="activityFestivalTypeLoadModel" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3>运营活动节日类型配置表</h3>
			</div>
			<div class="modal-body">
				<input id="activityFestivalTypeFile" type="file" name="activityFestivalTypeFile"/>
			</div>
			<div class="modal-footer">
				<input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
				<input type="button" value="上传" onclick="loadActivityFestivalType()" class="btn btn-primary"/>
			</div>
		</div>
	</div>
</body>
</html>