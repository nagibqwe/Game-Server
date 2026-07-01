<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>${msg['activity.recharge']}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${base}/css/activity.css" type="text/css" />
<link rel="stylesheet" href="${base}/css/common.css" type="text/css" />
<link rel="stylesheet" href="${base}/css/boxy.css" type="text/css" />
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/activity.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script type="text/javascript">
    var type = '${type}';
    var base = '${base}';

    $(function () {
        $('.datetimepicker').datetimepicker({
            language:  'zh-CN',
            format: 'yyyy-mm-dd hh:ii',
            todayBtn: 1,
            autoclose:true
        });
        loadAllItem();
        loadAllServerList();
        load(base, type);
    });
</script>
</head>
<body>
<div class="container-fluid">
    <div id="header">
        <h3>累充活动</h3>
    </div>

    <form id="activity_form" method="post" action="#">
        <div class="container-fluid well">
            <div class="row-fluid">
                <div class="row-fluid">
                    <div class="span10"><b>选择模板</b></div>
                </div>
                <div class="row-fluid">
                    <div class="span3">
                        <select id="template" onchange="copyTemplateData(this.value)" class="span12"></select>
                    </div>
                </div>

                <div class="row-fluid">
                    <div class="span10"><b>${msg['activity.base.info']}</b></div>
                </div>
                <div class="row-fluid">
                    <div class="span3">
                        <label for="name">${msg['activity.name']}</label>
                        <input id="name" name="name" type="text" class="validate[required] span10"/>
                        <input id="type" type="hidden" name="type">
                    </div>
                    <div class="span3">
                        <label for="labelPosition">${msg['activity.labelpos']}</label>
                        <input id="labelPosition" name="labelPosition" type="text" class="validate[required,custom[integer]] span10"/>
                    </div>
                    <div class="span3">
                        <label for="labelOrder">${msg['activity.labelorder']}</label>
                        <input id="labelOrder" name="labelOrder" type="text" class="validate[required,custom[integer]] span10"/>
                    </div>
                    <div class="span3">
                        <label for="numLimit">${msg['activity.numlimit']}</label>
                        <input id="numLimit" name="numLimit" type="text" class="validate[required,custom[integer],min[0]] span10"/>
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
                    <div class="span3">
                        <label for="panelImageId">${msg['activity.panelimageid']}</label>
                        <input id="panelImageId" name="panelImageId" type="text" class="validate[required,custom[integer],min[0]] span10"/>
                    </div>
                    <div class="span3">
                        <label for="panelText">${msg['activity.paneltext']}</label>
                        <input id="panelText" name="panelText" type="text" class="validate[required] span10"/>
                    </div>
                </div>

                <br/>
                <div class="row-fluid">
                    <div class="span3"><b>${msg['activity.reward']}</b></div>
                </div>
                <div id="activityReward" class="row-fluid">
<%--                        <div id="activityReward1" class="row-fluid activityReward">--%>

<%--                            <div class="span2">--%>
<%--                                <label for="position1">类型</label>--%>
<%--                                <select id="position1" name="position" class="span12">--%>
<%--                                    <option value="1">每日</option>--%>
<%--                                    <option value="2">累计</option>--%>
<%--                                </select>--%>
<%--                            </div>--%>
<%--                            <div class="span2">--%>
<%--                                <label for="day1">档位/累计天数</label>--%>
<%--                                <input id="day1" name="position" type="text" class="span12">--%>
<%--                            </div>--%>
<%--                            <div class="span2">--%>
<%--                                <label for="money1">金额</label>--%>
<%--                                <input id="money1" name="position" type="text" class="span12">--%>
<%--                            </div>--%>
<%--                            <div class="span6">--%>
<%--                                <label for="rewardDiv1">奖励</label>--%>
<%--                                <div id="rewardDiv1" class="row-fluid span11" onclick="changeCurItemDocObj(this)"--%>
<%--                                     style="border: 1px solid rgb(204, 204, 204); border-radius: 2px; height: 28px">--%>
<%--                                    <input type="hidden" id="reward1" value="" class="rewardInput"/>--%>
<%--                                    <div id="items1" class="items" style="margin-top: 5px">--%>
<%--                                    </div>--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
                </div>

                <div id="stage" class="row-fluid">
                    <input id="addStage" type="button" value="+" class=".btn-small" onclick="addActivityStage()"/>
                    <input id="removeStage" type="button" value="-" class=".btn-small" onclick="removeLastActivityStage()"/>
                </div>

                <br/>
                <div class="row-fluid">
                    <div class="span3"><b>${msg['activity.reward.condition']}</b></div>
                </div>
                <div id="condition" class="row-fluid">
                </div>
                <br/>

                <div class="row-fluid">
                    <input type="button" value="${msg['activity.add.submit']}" onclick="submitActivity()" class="btn btn-primary" style="margin-right: 30px"/>
                    <input type="button" value="添加此模板" onclick="addTemplate()" class="btn"/>
                    <input type="button" value="删除此模板" onclick="deleteTemplate()" class="btn"/>
                </div>
            </div>
        </div>
    </form>
</div>

<div class="container-fluid ">
    <div class="row-fluid">
        <table id="activity_list" class="table table-bordered table-hover table-striped" style="margin-bottom: 5px">
            <thead id="activity_field"></thead>
            <tbody id="activity_data"></tbody>
        </table>

        <div class="row-fluid" style="height: 28px">
            <div class="span5">
                <input type="button" id="batchTest" value="批量发布" onclick="publishEvent(3)" class="btn btn-primary">
                <%--<input type="button" id="batchValid" value="批量验证" onclick="verifyEvent()" class="btn btn-primary">--%>
                <%--<input type="button" id="batchPublish" value="批量发布" onclick="publishEvent(3)" class="btn btn-primary">--%>
                <input type="button" id="batchDelete" value="批量删除" onclick="deleteActivity()" class="btn btn-primary">
                <input type="button" id="oneKeyDelete" value="一键删除过期活动" onclick="oneKeyDelete()" class="btn btn-primary">
            </div>
            <div class="pagination pagination-large offset1 span4" style="margin: 0">
                <ul id="pager">
                </ul>
            </div>
            <div id="pageInfo" class="span3" style="text-align: right"></div>
        </div>
    </div>
</div>


<div id="addItemModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>添加物品</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <div class="offset1 span10">
                <input id="curObj" type="hidden"/>
                <label for="itemId">物品ID</label><input type="text" id="itemId" list="itemList" class="span8"/>
                <datalist id="itemList"></datalist>
                <label for="itemNum">物品数量</label><input id="itemNum" type="text" class="span8"/>
                <label for="isBind">是否绑定</label>
                <select id="isBind" class="span8">
                    <option value="0" selected>非绑</option>
                    <option value="1">绑定</option>
                </select>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
        <input type="button" value="添加" onclick="addItem()" class="btn btn-primary"/>
    </div>
</div>

<div id="addTemplateModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>添加模板</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <div class="offset1 span10">
                <label for="templateName">模板名</label><input type="text" id="templateName" class="span8"/>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
        <input type="button" value="添加" onclick="submitTemplate()" class="btn btn-primary"/>
    </div>
</div>

<div id="publishActivity" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>发布活动</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <form id="publish_form" action="#" class="well span12">
                <input id="operationType" type="hidden" name="operationType"/>
                <input id="actIds" type="hidden" name="actIds"/>
                <input id="cover" type="hidden" name="cover"/>
                <select id="platform" name="platform" onchange="changePlatformEvent()" class="span4"></select>
                <br/><br/>
                <div id="servers" class="row-fluid"></div>
            </form>
        </div>
    </div>
    <div class="modal-footer">
        <input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
        <input type="button" value="发布" onclick="publishActivity()" class="btn btn-primary"/>
    </div>
</div>

</body>
</html>
