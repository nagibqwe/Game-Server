/**
 * 创建日志查询条件参数
 * @param logType	日志类型
 * @param conditons 条件参数key
 * @param fields	字段名map
 */
function createCondition(logType, conditons, fields) {
	for (var index in conditions) {
		var name = conditons[index]
		$("#condition").append("<label for='" + name + "'>" + fields[name] + "</label>&nbsp;");
		$("#condition").append("<input type='text' id='" + name + "' name='" + name + "' class='span1'/>");
		$("#condition").append("&nbsp;&nbsp;&nbsp;&nbsp;");
	}
	//特殊日志查询条件处理
}

/**
 * 渠道全选事件
 */
function selectAllChannel(){
	console.log(1);
	var checked = $("#chooseAllChannel").prop("checked");
	$("[name='channelName']").prop("checked", checked);
}

/**
 * 选择渠道触发全选按钮事件
 */
function checkChooseAllChannel() {
	var checked = $("[name=channelName]:checked").length == $("[name=channelName]").length;
	$("#chooseAllChannel").prop("checked", checked);
}

/**
 * 查询条件显示隐藏
 */
function showCondition() {
	$("#condition").toggle('fast');
}

/**
 * 查询日志
 */
function search(){
	var startDate = $("#start input").val();
	var endDate = $("#end input").val();
	if (!checkDate(startDate, endDate)) {
		return;
	}
	$("#progressmodal").modal({backdrop:'static',keyboard:false});
	$.ajax({
		type: "POST",
		url: base + "/log/getLog",
		data: $("#query_form").serialize(),
		dataType: "json",
		success: function (data) {
			$("#progressmodal").modal('hide');
			if (!data.ok) {
                $("#data").html("");
				$("#msg").text(data.msg);
				return;
			}
			var tableStr = createTable(data.data.fields, data.data.datas);
			$("#msg").html("<input type='button' value='导出' class='btn btn-primary' onclick='exportExcel();'>");
			$("#msg").append("共" + data.data.pager.recordCount + "条记录，当前" + data.data.datas.length + "条记录");
			$("#data").html(tableStr);
		}
	});
}

/**
 * 导出所有数据
 */
function exportAllData(){
	var startDate=$("#start input").val();
	var endDate=$("#end input").val();
	if(!checkDate(startDate,endDate)){
		return;
	}
	$.post(base + "/log/exportExcel", $("#query_form").serialize());
}

/**
 * 导出表格数据
 */
function exportExcel(){
	$("#data table").eq(0).tableExport({type: 'excel', escape: 'false'});
}