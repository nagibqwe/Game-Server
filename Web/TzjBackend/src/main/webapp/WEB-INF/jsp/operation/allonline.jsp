<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.log.onlinetitle']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/dateFormat.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript">
	var base = '${base}';
	$(function() {
		$("#loading").shCircleLoader();
		//setInterval ("reload()", 60*1000);
		setTimeout("reload()", 60*1000);
		reload();
	});
	function reload() {
		/* $("#loadingmodal").modal({
			backdrop : 'static',
			keyboard : false
		}); */
		$.ajax({
			type : "POST",
			url : base + "/operation/allOnlineCount",
			data : "",
			dataType : "json",
			success : function(data) {
				//console.log(data);
				//$("#loadingmodal").modal('hide');
				if (!data.ok) {
					$("#online").html(data.msg);
					return;
				}

				//加入table显示
				var table = $("<table>").attr("class",
						"table table-bordered table-striped");
				var thead = $("<thead>");
				var htr = $("<tr>");

				var fields = [ "${msg['jsp.online.serverid']}",
				        "${msg['jsp.online.servername']}",
						"${msg['jsp.online.time']}",
						"${msg['jsp.online.num']}"];
						//"${msg['jsp.online.action']}"];
				for ( var field in fields) {
					var th = $("<th>").text(fields[field]);
					htr.append(th);
				}
				thead.append(htr);
				table.append(thead);

				var tbody = $("<tbody>");
				for (var i = 0; i < data.data.length; i++) {
					var onlineData = data.data[i];
					var tr = $("<tr>");
					tr.append($("<td>").text(onlineData.serverId))
					.append($("<td>").text(onlineData.serverName))
					.append($("<td>").text(TimeObjectUtil.UnixToDate(onlineData.time)))
					.append($("<td>").text(onlineData.num));
					/* var button = $("<input>").attr("type", "button")
							.attr("class", "btn btn-primary").attr(
									"value",
									"${msg['jsp.online.refresh']}")
							.attr("onclick", "refresh(this);");
					tr.append($("<td>").append(button)); */
					tbody.append(tr);
				}
				table.append(tbody);
				//$("#loadingmodal").modal('hide');
				$("#online").html(table);
			}
		});

	};
	function refresh(obj) {
		var tds = $(obj).parent().parent().find("td");
		var serverId = tds.first().text();
		//$(obj).attr("disabled",true).val("${msg['jsp.online.loading']}");
		
		$.ajax({
			type : "POST",
			url : base + "/operation/refreshOnlineCount",
			data : {
				"serverId" : serverId
			},
			dataType : "json",
			success : function(data) {
				//$(obj).attr("disabled",false).val("${msg['jsp.online.refresh']}");
				if (!data.ok) {
					alert("${msg['jsp.online.refreshfail']}");
					$("#msg").html(data.msg);
					return;
				}
				tds.eq(1).text(TimeObjectUtil.UnixToDate(data.data[0].time));
				tds.eq(2).text(data.data[0].num);
			}
		});
	};
</script>
</head>
<body>
	<div class="container-fluid">
		<%-- <input type="button" id="query_btn" value="${msg['jsp.online.refreshall']}" class="btn btn-primary" onclick="reload();"> --%>
		<p id="msg"></p>
		<div id="online"></div>
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