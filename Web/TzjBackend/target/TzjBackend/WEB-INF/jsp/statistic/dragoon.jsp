<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.dragoon.title']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript">
	var base = '${base}';
	$(function() {
		$("#excel_btn").hide();
		group_reload();
		$("#start").datetimepicker({
			language : 'zh-CN',
			format : 'yyyy-mm-dd',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			showMeridian : 1
		});
		$("#end").datetimepicker({
			language : 'zh-CN',
			format : 'yyyy-mm-dd',
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			showMeridian : 1
		});
		$("#loading").shCircleLoader();
	});
	function search() {
		var startDate = $("#start input").val();
		var endDate = $("#end input").val();
		if (checkDate(startDate, endDate)) {
			$("#loadingmodal").modal({backdrop:'static',keyboard:false});
			$
					.ajax({
						type : "POST",
						url : base + "/statistic/dragoonStatistic",
						data : $("#query_form").serialize(),
						dataType : "json",
						success : function(data) {
							console.log(data);
							$("#loadingmodal").modal('hide');
							if (!data.ok) {
								$("#excel_btn").hide();
								$("#dragoon").html(data.msg);
								return;
							}

							var table = $("<table>").attr("class",
									"table table-bordered table-striped");
							var thead = $("<thead>");
							var htr = $("<tr>");
							var fields = [ "${msg['jsp.dragoon.key']}",
								"${msg['jsp.dragoon.upplayernumber']}",
								"${msg['jsp.dragoon.clickcount']}"];
							for ( var field in fields) {
								var th = $("<th>").text(fields[field]);
								htr.append(th);
							}
							thead.append(htr);
							table.append(thead);

							var tbody = $("<tbody>");
							//console.log(data.data);
							for ( var key in data.data) {
								var dragoon = data.data[key]
								console.log(dragoon);
								var tr = $("<tr>");
								var datalist = [
								        key,
								        dragoon.totalcount,
								        dragoon.totaltimes ];
								for ( var i in datalist) {
									var td = $("<td>").text(datalist[i]);
									tr.append(td);
								}
								tbody.append(tr);
							}

							table.append(tbody);
							$("#excel_btn").show();
							$("#dragoon").show();
							$("#dragoon").html(table);

						}
					});
		}
	};
	function checkDate(startDate, endDate) {
		if (startDate == "" || startDate == null) {
			alert("${msg['jsp.log.sdateempty']}");
			return false;
		}
		if (endDate == "" || endDate == null) {
			alert("${msg['jsp.log.edateempty']}");
			return false;
		}
		var start = new Date(startDate.replace("-", "/").replace("-", "/"));
		var end = new Date(endDate.replace("-", "/").replace("-", "/"));
		if (end < start) {
			alert("${msg['jsp.log.sdatelessthanedate']}");
			return false;
		}
		return true;
	};
	function exportExcel(){
		$("#dragoon table").eq(0).tableExport({type: 'excel', escape: 'false'});
	};
</script>
</head>
<body>
	<div class="container-fluid">
		<div>
			<form action="#" id="query_form" class="well form-inline">
				<select id="select_group" name="groupName"
					onchange="queryServerByGroup(this.value)" class="span2"></select> <select
					id="select_server" name="serverId" class="span2"></select>

				<!-- 时间段 -->
				<label class="label">${msg['jsp.log.time']}</label>
				<div class="input-append date" id="start">
					<input style="width: 120px;" name="startDate" size="20" type="text" value="${nowDate}" 
						readonly> <span class="add-on"><i class="icon-th"></i></span>
				</div>
				-
				<div class="input-append date" id="end">
					<input style="width: 120px;" name="endDate" size="20" type="text" value="${nowDate}" 
						readonly> <span class="add-on"><i class="icon-th"></i></span>
				</div>

				<input type="button" id="query_btn" value="${msg['jsp.log.search']}"
					class="btn btn-primary" onclick="search();">
				<input type="button" id="excel_btn" value="${msg['jsp.log.exportexcel']}" class="btn btn-primary" onclick="exportExcel();">
			</form>
		</div>
		<p id="msg"></p>
		
		<div id="dragoon"></div>
	</div>
	<div class="modal hide fade in" id="loadingmodal">
		<div class="modal-body">
			<div style="padding-left: 30%;">
				<p style="font-size: 14px;width:120px;height:30px;line-height: 30px;display:inline;float: left;">${msg['jsp.log.loading']}</p>
				<div id="loading" style="width:30px; height:30px;display:inline;float: left;"></div>
			</div>
		</div>
	</div>

</body>
</html>