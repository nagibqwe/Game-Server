<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>节日祝福</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${base}/css/activity.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/common.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
    <link rel="stylesheet" href="${base}/css/alertify.css">
    <link rel="stylesheet" href="${base}/css/alertify.default.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap3.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-select.min.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/alertify.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-select.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap3.js"></script>
    <script type="text/javascript"
            src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript"
            src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${base}/js/activity.js"></script>
    <script type="text/javascript" src="${base}/js/global.js"></script>
    <link rel="stylesheet" href="${base}/css/fileCss.css">
    <script type="text/javascript">
        var actType = '${type}';
        var base = '${base}';
        var itemMap = new Object();//用来存放itemId和对应的蓝钻价值
        var changeStyle = false;
        var models=new Map();
        var count_01 = 0;
        var count_02 = 0;
        $(function () {

            $('.datetimepicker').datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd hh:ii',
                todayBtn: 1,
                autoclose: true
            });

            loadAllItem();
            loadModel();
            loadAllServerList();
            load(base, actType);
            loadActivityFestivalType();
            $("#publishActivity").modal('hide');
            $("[data-toggle='popover']").popover();
        });

        function changeStyleClass() {
            if (!changeStyle) {
                // $("#singleGroup").removeClass("col-lg-3 col-sm-3 col-md-3").addClass("col-lg-6 col-sm-6 col-md-6");
                // $("#serverGroup").removeClass("col-lg-3 col-sm-3 col-md-3").addClass("col-lg-6 col-sm-6 col-md-6");
                // $("#rewardPool").removeClass("col-lg-3 col-sm-3 col-md-3").addClass("col-lg-6 col-sm-6 col-md-6");
                // $("#costGroup").removeClass("col-lg-3 col-sm-3 col-md-3").addClass("col-lg-6 col-sm-6 col-md-6");
                changeStyle = true;
            }
        }
        //加载所有模型库
        function loadModel() {
            $.ajax({
                url: base + "/activityConfig/getAllModel",
                method: "post",
                dataType: "json",
                async:false,
                success: function (data) {
                    if (!data.ok) {
                        alert("Model load failed");
                        return;
                    }
                    $.each(data.data, function (i, n) {
                        models.set(n.id + "",n.tips);
                    });
                }
            });
        }
    </script>

    <script>
        function addPlayerCfg(obj) {
            changeStyleClass();
            console.log(count_01);
            var rewardInput = 'dayReward' + count_01;
            var rewardAddBtn = 'showRewardModel' + count_01;
            count_01 += 1;
            var html = '<div name="cfg" class="input-group saltIp" style="">' +
                '<label class="input-group-addon">签到天数:</label>' +
                '<input type="text" class="form-control" name="day" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:100px;">' +
                '<label class="input-group-addon">模型ID:</label>' +
                '<div class="col-md-12 col-sm-12 col-xs-12">' +
                '<select name="modelId" onchange="" class="form-control"></select>' +
                '</div>'+
                // '<input type="text" class="form-control" name="modelId" style="max-width:auto;" onkeyup="value=value.replace(/\\D/g,\'\')" placeholder="例：1001">' +
                '<label class="input-group-addon">奖励:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖励" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + rewardInput + ',' + rewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="dayReward"/></div>' +
                '</span>' +
                // '<input type="text" class="form-control" name="dayReward" style="max-width:auto;" placeholder="例：3_1_1_9;1017_1_1_9">' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delExChangeConfig(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html);
            loadModelData();
        }
        //删除配置
        function delExChangeConfig(obj) {
            $(obj).parent().parent().remove();
        }
        //下拉框选项生成
        var countModel=0;
        function loadModelData() {
            models.forEach(function(value, key) {
                $("select[name='modelId']").eq(countModel).append("<option value='" + key +"'>" + value +"</option>");
            });
            countModel+=1;
        }
    </script>

    <script>
        function addServerCfg(obj) {
            changeStyleClass();
            var rewardInput = 'serverReward' + count_02;
            var rewardAddBtn = 'showRewardModel2' + count_02;
            count_02 += 1;
            var html = '<div name="cfg" class="input-group saltIp" style="">' +
                '<label class="input-group-addon">宝箱ID:</label>' +
                '<input type="text" class="form-control" name="boxId" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:100px;" placeholder="例：100">' +
                '<label class="input-group-addon">达成进度:</label>' +
                '<input type="text" class="form-control" name="need" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:100px;" placeholder="例：500">' +
                '<label class="input-group-addon">奖励:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖励" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + rewardInput + ',' + rewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="serverReward"/></div>' +
                '</span>' +
                // '<input type="text" class="form-control" name="serverReward" style="max-width:auto;" placeholder="例：3_1_1_9;1017_1_1_9">' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delConfig(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html)
        }
        //删除配置
        function delConfig(obj) {
            $(obj).parent().parent().remove();
        }
    </script>

    <script>
        $(document).on('click', '#copyGridGroup', function () {
            var el = this.parentNode.parentNode
            var newEl = el.cloneNode(true);
            el.parentNode.insertBefore(newEl, el);
        })
        $(document).on('click', '#delGridGroup', function () {
            var el = this.parentNode.parentNode
            var saltIp = $(this).parent().parent().find('#saltIp').val()
            el.parentNode.removeChild(el)

            var count = $("#gridCount").val();
            if (count > 0) {
                count = Number(count) - 1;
                $("#gridCount").val(count);
            }
            return
        })
    </script>
    <script>
        function validate() {
            return true;
        }
    </script>
</head>
<body style="font-size: 12px">
<div class="container-fluid">
    <div id="header">
        <h3>节日祝福<span class="glyphicon glyphicon-question-sign" title="${msg['jsp.activity.title22']}"
                      data-container="body" data-toggle="popover" data-placement="right"
                      data-content="${msg['jsp.activity.content22']}"></span></h3>
    </div>

    <form id="activity_form" method="post" class="main form-horizontal" action="#">

        <jsp:include page="./BaseActivity_new.jsp"/>

        <div id="costGroup" class="col-lg-12 col-sm-12 col-md-12">
            <div class="col-lg-1"></div>
            <div class="col-lg-11">
                <legend>补签配置</legend>
                <fieldset>
                    <div name="cfgDiv" class="input-group saltIp">
                        <label class="input-group-addon">补签道具ID:</label>
                        <input name="buqianid" id="buqianid" value="" title="补签道具ID" placeholder="例：1017"
                               type="text" class="form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <%--                        <span class="input-group-btn">--%>
                        <%--                            <button class="btn btn-info" type="button" data-toggle="tooltip" title="补签配置" id="addGoldGiftBtn"--%>
                        <%--                                    onclick="showAddItemUI('i_GoldGift','addGoldGiftBtn')"><span class="glyphicon glyphicon-plus"></span></button>--%>
                        <%--                        </span>--%>
                        <%--                        <input name="i_GoldGift" id="i_GoldGift" value="" type="text" class="hide"/>--%>
                        <label class="input-group-addon">补签消耗:</label>
                        <input name="buqianCost" id="buqianCost" value="" title="补签消耗" placeholder="例：200"
                               type="text" class="form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="playerGroup" class="col-lg-12 col-sm-12 col-md-12">
            <div class="col-lg-1"></div>
            <div class="col-lg-11">
                <legend>签到配置</legend>
                <fieldset>
                    <div class="input-group" id="serverPrcCfg">
                        <label class="input-group-addon">奖励配置:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="playerGroupBtn"
                                onclick="addPlayerCfg(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="serverGroup" class="col-lg-12 col-sm-12 col-md-12">
            <div class="col-lg-1"></div>
            <div class="col-lg-11">
                <legend>全服奖励</legend>
                <fieldset>
                    <div class="input-group" id="serverRewardCfg">
                        <label class="input-group-addon">奖励配置:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addServerCfgBtn"
                                onclick="addServerCfg(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                </fieldset>
            </div>
        </div>
    </form>
</div>
<div class="col-md-12 col-sm-12 col-lg-12" style="height: 30px"></div>
<div class="col-sm-5 col-sm-5"></div>
<jsp:include page="./import.jsp"/>
<div class="container-fluid ">
    <div class="row-fluid">
        <table id="activity_list" class="table table-bordered table-hover table-striped" style="margin-bottom: 5px">
            <thead id="activity_field"></thead>
            <tbody id="activity_data"></tbody>
        </table>

        <div class="row-fluid" style="height: 28px">
            <div class="col-sm-8 col-md-8"></div>
            <div class="span5 col-sm-4 col-md-4">
                <input type="button" id="batchTest" value="批量发布" onclick="publishEvent(3)" class="btn btn-primary">
                <%--<input type="button" id="batchValid" value="批量验证" onclick="verifyEvent()" class="btn btn-primary">--%>
                <%--<input type="button" id="batchPublish" value="批量发布" onclick="publishEvent(3)" class="btn btn-primary">--%>
                <input type="button" id="batchDelete" value="批量删除" onclick="deleteActivity()" class="btn btn-primary">
                <%--                <input type="button" id="oneKeyDelete" value="一键删除过期活动" onclick="oneKeyDelete()" class="btn btn-primary">--%>
            </div>
            <div class="pagination pagination-large offset1 span4" style="margin: 0">
                <ul id="pager">
                </ul>
            </div>
            <div id="pageInfo" class="span3" style="text-align: right"></div>
        </div>
    </div>
</div>

<jsp:include page="../../item/itemAddModel.jsp"/>

<jsp:include page="./ActivityTemplate.jsp"/>

<div id="publishActivity" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
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
