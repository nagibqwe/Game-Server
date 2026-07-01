<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>幸运砸蛋</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${base}/css/activity.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/common.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
    <link rel="stylesheet" href="${base}/css/alertify.css">
    <link rel="stylesheet" href="${base}/css/alertify.default.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap3.css">
    <%--<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">--%>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/alertify.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
    <%--<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>--%>
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
        var count_00 = 0;
        var count_01 = 0;
        var count_02 = 0;
        var count_03 = 0;
        var count_04 = 0;
        var count_05 = 0;
        var count_06 = 0;
        var count_07 = 0;
        $(function () {

            $('.datetimepicker').datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd hh:ii',
                todayBtn: 1,
                autoclose: true
            });

            loadAllItem();
            loadAllServerList();
            load(base, actType);
            loadActivityFestivalType();
            $("#publishActivity").modal('hide');
            $("[data-toggle='popover']").popover();
        });
    </script>
    <script>
        function addShowGridCfg(obj) {
            // var count = $("#gridCount").val();
            // count=Number(count)+1;
            // $("#gridCount").val(count);
            var rewardInput = 'grids' + count_00;
            var rewardAddBtn = 'showAddItemModel' + count_00;
            count_00 += 1;
             html = '<div name="cfg0" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">位置:'+count_00+'</label>' +
                // '<input type="text" class="form-control" id="p_round" name="p_round" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:100px;">' +
                '<label class="input-group-addon">奖品:</label>' +
                // '<input type="text" class="form-control" id="g_reward" name="g_reward" style="max-width:auto;">' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="添加奖励" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUILimit(' + rewardInput + ',' + rewardAddBtn + ',4)">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="grids"/></div>' +
                // '</span>' +
                // '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delConfig(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html)
        }
        function delConfig(obj) {
            $(obj).parent().parent().remove();
        }
    </script>

    <script>
        function jiangliGrpOne(obj) {
            var rewardInput = 'i_reward_one' + count_01;
            var rewardAddBtn = 'showRewardModel2' + count_01;
            count_01 += 1;
            html = '<div name="cfg1" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">纯道具抽权重:</label>' +
                '<input type="text" class="form-control" id="i_item_weight_one" name="i_item_weight_one" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">花金元宝抽权重:</label>' +
                '<input type="text" class="form-control" id="i_gold_weight_one" name="i_gold_weight_one" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">是否特殊展示:</label>' +
                '<div class="col-md-12 col-sm-12 col-xs-12">' +
                '<select name="isShow_one" onchange="" class="form-control">' +
                '<option value="0">否</option>' +
                '<option value="1">是</option>' +
                '</select>' +
                '</div>'+
                '<label class="input-group-addon">奖品:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUILimit(' + rewardInput + ',' + rewardAddBtn + ',4)">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="i_reward_one"/></div>' +
                '</span>' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delConfig1(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                // '<button class="btn btn-info" type="button" data-toggle="tooltip" title="复制" id="copyGridGroup"><span class="glyphicon glyphicon-copy"></span></button>' +
                '</span>' +
                '</div>';
            obj.insertAdjacentHTML('beforeBegin', html);
        }
        function delConfig1(obj) {
            $(obj).parent().parent().remove();
        }
    </script>

    <script>
        function jiangliGrpTwo(obj) {
            var rewardInput = 'i_reward_two' + count_02;
            var rewardAddBtn = 'showRewardModel3' + count_02;
            count_02 += 1;
            html = '<div name="cfg2" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">纯道具抽权重:</label>' +
                '<input type="text" class="form-control" id="i_item_weight_two" name="i_item_weight_two" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">花金元宝抽权重:</label>' +
                '<input type="text" class="form-control" id="i_gold_weight_two" name="i_gold_weight_two" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">是否特殊展示:</label>' +
                '<div class="col-md-12 col-sm-12 col-xs-12">' +
                '<select name="isShow_two" onchange="" class="form-control">' +
                '<option value="0">否</option>' +
                '<option value="1">是</option>' +
                '</select>' +
                '</div>'+
                '<label class="input-group-addon">奖品:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUILimit(' + rewardInput + ',' + rewardAddBtn + ',4)">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="i_reward_two"/></div>' +
                '</span>' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delConfig2(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                // '<button class="btn btn-info" type="button" data-toggle="tooltip" title="复制" id="copyGridGroup"><span class="glyphicon glyphicon-copy"></span></button>' +
                '</span>' +
                '</div>';
            obj.insertAdjacentHTML('beforeBegin', html);
        }
        function delConfig2(obj) {
            $(obj).parent().parent().remove();
        }
    </script>

    <script>
        function jiangliGrpThree(obj) {
            var rewardInput = 'i_reward_three' + count_03;
            var rewardAddBtn = 'showRewardModel4' + count_03;
            count_03 += 1;
            html = '<div name="cfg3" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">纯道具抽权重:</label>' +
                '<input type="text" class="form-control" id="i_item_weight_three" name="i_item_weight_three" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">花金元宝抽权重:</label>' +
                '<input type="text" class="form-control" id="i_gold_weight_three" name="i_gold_weight_three" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">是否特殊展示:</label>' +
                '<div class="col-md-12 col-sm-12 col-xs-12">' +
                '<select name="isShow_three" onchange="" class="form-control">' +
                '<option value="0" selected>否</option>' +
                '</select>' +
                '</div>'+
                '<label class="input-group-addon">奖品:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUILimit(' + rewardInput + ',' + rewardAddBtn + ',4)">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="i_reward_three"/></div>' +
                '</span>' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delConfig3(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                // '<button class="btn btn-info" type="button" data-toggle="tooltip" title="复制" id="copyGridGroup"><span class="glyphicon glyphicon-copy"></span></button>' +
                '</span>' +
                '</div>';
            obj.insertAdjacentHTML('beforeBegin', html);
        }
        function delConfig3(obj) {
            $(obj).parent().parent().remove();
        }
    </script>

    <script>

    </script>

    <script>
        function countRewardGrp(obj) {
            var rewardInput = 'i_countReward' + count_05;
            var rewardAddBtn = 'showRewardModel5' + count_05;
            count_05 += 1;
            html = '<div name="cfg5" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">砸蛋次数:</label>' +
                '<input type="text" class="form-control" id="i_countReward_num" name="i_countReward_num" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">累计奖励:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + rewardInput + ',' + rewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="i_countReward"/></div>' +
                '</span>' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delConfig5(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                // '<button class="btn btn-info" type="button" data-toggle="tooltip" title="复制" id="copyGridGroup"><span class="glyphicon glyphicon-copy"></span></button>' +
                '</span>' +
                '</div>';
            obj.insertAdjacentHTML('beforeBegin', html);
        }
        function delConfig5(obj) {
            $(obj).parent().parent().remove();
        }
    </script>

    <script>
        $(document).on('click', '#delGridGroup', function () {
            var el = this.parentNode.parentNode
            var saltIp = $(this).parent().parent().find('#saltIp').val()
            el.parentNode.removeChild(el)
            return;
        })
    </script>

    <script>
        function validate() {
            if (checkBaodi() && checkBaoDiRange() && checkBaoDiRangeInfo()){
                return true;
            }else {
                return false;
            }
        }
    </script>
</head>
<body style="font-size: 12px">
<div class="container-fluid">
    <div id="header">
        <h3>幸运砸蛋
            <span class="glyphicon glyphicon-question-sign"
                  title="${msg['jsp.activity.title27']}"
                  data-container="body" data-toggle="popover" data-placement="right"
                  data-content="${msg['jsp.activity.content27']}">
            </span>
        </h3>
    </div>

    <form id="activity_form" method="post" class="main form-horizontal" action="#">
        <jsp:include page="./BaseActivity_new.jsp"/>

        <div id="showGridGroup" class="col-lg-12 col-sm-12 col-md-12">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-11">
                <legend>客户端奖励展示</legend>
                <fieldset>
                    <div class="input-group" id="showGridCfg">
                        <label class="input-group-addon">奖励展示:</label>
                        <input id="gridCount" type="hidden" value="">
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addShowGridCfgBtn"
                                onclick="addShowGridCfg(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="costGroup" class="col-lg-12 col-sm-12 col-md-12">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>砸蛋道具</legend>
                <fieldset>
                    <div class="input-group saltIp">
                        <label class="input-group-addon">道具id:</label>
                        <input name="i_costItem" id="i_costItem"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">砸一次消耗道具:</label>
                        <input name="i_oneCostItem" id="i_oneCostItem"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')" value="1" readonly/>
                        <label class="input-group-addon">单次砸蛋元宝:</label>
                        <input name="i_oneCostGold" id="i_oneCostGold"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">每日砸蛋上限数:</label>
                        <input name="dailyLimitCount" id="dailyLimitCount"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">奖励道具:</label>
                        <span class="input-group-btn">
                            <button class="btn btn-info" type="button" data-toggle="tooltip" title="奖励道具" id="addGiveGiftBtn"
                                    onclick="showAddItemUILimit(i_GiveGift,addGiveGiftBtn,1)"><span class="glyphicon glyphicon-plus"></span></button>
                            <input type="hidden" id="cfgCount1" value="0">
                        </span>
                        <input name="i_GiveGift" id="i_GiveGift" value="" type="text" class=" hide"/>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="itemWeight" class="col-lg-6 col-sm-6 col-md-6">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>纯道具抽权重</legend>
                <fieldset>
                    <div class="input-group saltIp">
                        <label class="input-group-addon">彩蛋权重:</label>
                        <input name="i_item_one" id="i_item_one" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">金蛋权重:</label>
                        <input name="i_item_two" id="i_item_two" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">银蛋权重:</label>
                        <input name="i_item_three" id="i_item_three"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="goldWeight" class="col-lg-6 col-sm-6 col-md-6">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>花金元宝抽权重</legend>
                <fieldset>
                    <div class="input-group saltIp">
                        <label class="input-group-addon">彩蛋权重:</label>
                        <input name="i_gold_one" id="i_gold_one" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">金蛋权重:</label>
                        <input name="i_gold_two" id="i_gold_two" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">银蛋权重:</label>
                        <input name="i_gold_three" id="i_gold_three"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="rewardPool" class="col-lg-12 col-sm-12 col-md-12">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>奖池数据配置</legend>
                <fieldset>
                    <div style="margin-top: 5px" class="input-group" id="jianglishujuone">
                        <label class="input-group-addon">彩蛋奖池数据:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtnOne"
                                onclick="jiangliGrpOne(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                    <div style="margin-top: 5px" class="input-group" id="jianglishujutwo">
                        <label class="input-group-addon">金蛋奖池数据:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtnTwo"
                                onclick="jiangliGrpTwo(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                    <div style="margin-top: 5px" class="input-group" id="jianglishujuthree">
                        <label class="input-group-addon">银蛋奖池数据:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtnThree"
                                onclick="jiangliGrpThree(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="luckyGroup" class="col-lg-5 col-sm-5 col-md-5">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>物品幸运值配置</legend>
                <fieldset>
                    <div class="input-group saltIp">
                        <label class="input-group-addon">参与一次增加的幸运值:</label>
                        <input name="oneLuckyValue" id="oneLuckyValue"
                               type="text" class=" form-control" style="width:100px;" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">奖励列表:</label>
                        <span class="input-group-btn">
                            <button class="btn btn-info" type="button" data-toggle="tooltip" title="奖励列表" id="addOneLuckyBtn"
                                    onclick="showAddItemUI('luckyAwardGift','addOneLuckyBtn')"><span class="glyphicon glyphicon-plus"></span></button>
                            <input type="hidden" id="cfgCount4" value="0">
                        </span>
                        <input name="luckyAwardGift" id="luckyAwardGift" value="" type="text" class=" hide"/>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="refreshPool" class="col-lg-12 col-sm-12 col-md-12">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>彩蛋刷新配置</legend>
                <fieldset>
                    <div class="input-group saltIp">
                        <label class="input-group-addon">刷新消耗的道具ID:</label>
                        <input name="i_refresh_item" id="i_refresh_item" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon" style="display: none">刷新消耗的道具数量:</label>
                        <input name="i_refresh_item_num" style="display: none" id="i_refresh_item_num" value="1" readonly
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">刷新消耗的元宝数量:</label>
                        <input name="i_refresh_gold_num" id="i_refresh_gold_num" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">刷新多少次必出保底彩蛋:</label>
                        <input name="i_refresh" id="i_refresh" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="countReward" class="col-lg-9 col-sm-9 col-md-9">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>累计奖励</legend>
                <fieldset>
                    <div class="input-group" id="countRewardshuju">
                        <label class="input-group-addon">累计奖励:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="countRewardBtn"
                                onclick="countRewardGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="baodiPool" class="col-lg-12 col-sm-12 col-md-12">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-11">
                <legend>保底次数配置</legend>
                <fieldset>
                    <div class="input-group" id="baodishuju">
                        <label class="input-group-addon">保底次数:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="baoDiBtn"
                                onclick="baoDiGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                </fieldset>
            </div>
        </div>

    </form>
</div>
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
