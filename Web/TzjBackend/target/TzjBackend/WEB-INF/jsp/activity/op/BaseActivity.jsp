<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%--                <div class="row-fluid">--%>
<%--                    <div class="span10"><b>选择模板</b></div>--%>
<%--                </div>--%>
<%--                <div class="row-fluid">--%>
<%--                    <div class="span3">--%>
<%--                        <select id="template" onchange="copyTemplateData(this.value)" class="span12"></select>--%>
<%--                    </div>--%>
<%--                </div>--%>

<div class="row-fluid">
	<div class="span10"><b>${msg['activity.base.info']}</b></div>
</div>
<div class="row-fluid">
	<div class="span3">
		<label for="name">${msg['activity.name']}</label>
		<input id="name" name="name" type="text" class="" placeholder="${msg['activity.name']}"/>
		<input id="type" type="hidden" name="type">
	</div>
</div>

<div class="row-fluid">
	<div class="span3">
		<label for="minLv">最小开放等级</label>
		<input name="minLv" id="minLv" type="text" class="" value="1" />
	</div>
	<div class="span3">
		<label for="maxLv">最大开放等级</label>
		<input name="maxLv" id="maxLv" type="text" class="" value="800" />
	</div>
</div>

<div class="row-fluid">
	<div class="span3">
		<label for="tag">标签(用于区分展示在哪个活动标签下)</label>
<%--		<input name="tag" id="tag" type="text" class="" value="1"/>--%>
		<select id="tag" name="tag" onchange="" class="form-control">
			<option value="1">限时活动</option>
			<option value="2">定制活动</option>
		</select>
	</div>
	<div class="span3">
		<label for="sort">标签中的排序</label>
		<input name="sort" id="sort" type="text" class="" value="1"/>
	</div>
</div>

<div class="row-fluid">
	<div class="span3">
		<label for="timeType">时间类型</label>
		<div class="span3">
			<select id="timeType" onchange="" class="">
				<option value="0">固定时间</option>
				<option value="1">时间变量</option>
				<option value="2">开服时间变量</option>
			</select>
		</div>
	</div>
	<div class="span3">
		<label for="timeOffset">时间变量</label>
		<input name="timeOffset" id="timeOffset" type="text" class="" value="0"/>
	</div>
</div>

<div class="row-fluid">
	<div class="span3">
		<label for="beginTime">${msg['activity.begintime']}</label>
		<input name="beginTime" id="beginTime" type="text" class="datetimepicker validate[required] span10" readonly/>
	</div>
	<div class="span3">
		<label for="endTime">${msg['activity.endtime']}</label>
		<input name="endTime" id="endTime" type="text" class="datetimepicker validate[required] span10" readonly/>
	</div>
</div>