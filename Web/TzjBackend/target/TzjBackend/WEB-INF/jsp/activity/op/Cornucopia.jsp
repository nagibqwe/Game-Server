<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>聚宝盆</title>
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
        function addShowGridCfg(obj,num) {
            for (var i=0;i<num+1;i++){
                if (i<num){
                    addShowGridSingle(obj);
                }else {
                    $("#addShowGridCfgBtn").attr("disabled", true);//设置不可点击
                    $("#addShowGridCfgBtn").style.backgroundColor="#555555";
                }
            }
        }

        function addShowGridSingle(obj) {
            var count = $("#gridCount").val();
            count=Number(count)+1;
            $("#gridCount").val(count);
            var rewardInput = 'grids' + count;
            var rewardAddBtn = 'showAddItemModel' + count;
            var html = '<div name="cfg0" class="input-group saltIp" style="">' +
                '<label class="input-group-addon">位置:'+count+'</label>' +
                // '<input type="text" class="form-control" id="p_round" name="p_round" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:100px;">' +
                '<label class="input-group-addon">奖品:</label>' +
                // '<input type="text" class="form-control" id="g_reward" name="g_reward" style="max-width:auto;">' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="添加奖励" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUILimit(' + rewardInput + ',' + rewardAddBtn + ',4)">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="grids"/></div>' +
                '</span>' +
                // '<span class="input-group-btn">' +
                // '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" id="delGridGroup">' +
                // '<span class="glyphicon glyphicon-minus"></span>' +
                '</button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html)
        }
    </script>
    <script>
        function jiangliGrpBig(obj) {
            var rewardInput = 'i_reward_big' + count_01;
            var rewardAddBtn = 'showRewardModel' + count_01;
            count_01 += 1;
            html = '<div name="cfg1" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">纯道具抽权重:</label>' +
                '<input type="text" class="form-control" id="i_item_weight_big" name="i_item_weight_big" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">花金元宝抽权重:</label>' +
                '<input type="text" class="form-control" id="i_gold_weight_big" name="i_gold_weight_big" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">奖品:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUILimit(' + rewardInput + ',' + rewardAddBtn + ',4)">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="i_reward_big"/></div>' +
                '</span>' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delConfig(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                // '<button class="btn btn-info" type="button" data-toggle="tooltip" title="复制" id="copyGridGroup"><span class="glyphicon glyphicon-copy"></span></button>' +
                '</span>' +
                '</div>';
            obj.insertAdjacentHTML('beforeBegin', html);
        }
        function delConfig(obj) {
            $(obj).parent().parent().remove();
        }
    </script>

    <script>
        function jiangliGrpOne(obj) {
            var rewardInput = 'i_reward_one' + count_02;
            var rewardAddBtn = 'showRewardModel2' + count_02;
            count_02 += 1;
            html = '<div name="cfg2" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">纯道具抽权重:</label>' +
                '<input type="text" class="form-control" id="i_item_weight_one" name="i_item_weight_one" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">花金元宝抽权重:</label>' +
                '<input type="text" class="form-control" id="i_gold_weight_one" name="i_gold_weight_one" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">奖品:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUILimit(' + rewardInput + ',' + rewardAddBtn + ',4)">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="i_reward_one"/></div>' +
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
        function jiangliGrpTwo(obj) {
            var rewardInput = 'i_reward_two' + count_03;
            var rewardAddBtn = 'showRewardModel3' + count_03;
            count_03 += 1;
            html = '<div name="cfg3" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">纯道具抽权重:</label>' +
                '<input type="text" class="form-control" id="i_item_weight_two" name="i_item_weight_two" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">花金元宝抽权重:</label>' +
                '<input type="text" class="form-control" id="i_gold_weight_two" name="i_gold_weight_two" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">奖品:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUILimit(' + rewardInput + ',' + rewardAddBtn + ',4)">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="i_reward_two"/></div>' +
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
        function jiangliGrpThree(obj) {
            var rewardInput = 'i_reward_three' + count_04;
            var rewardAddBtn = 'showRewardModel4' + count_04;
            count_04 += 1;
            html = '<div name="cfg4" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">纯道具抽权重:</label>' +
                '<input type="text" class="form-control" id="i_item_weight_three" name="i_item_weight_three" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">花金元宝抽权重:</label>' +
                '<input type="text" class="form-control" id="i_gold_weight_three" name="i_gold_weight_three" onkeyup="value=value.replace(/\\D/g,\'\')">' +
                '<label class="input-group-addon">奖品:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUILimit(' + rewardInput + ',' + rewardAddBtn + ',4)">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="i_reward_three"/></div>' +
                '</span>' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delConfig4(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                // '<button class="btn btn-info" type="button" data-toggle="tooltip" title="复制" id="copyGridGroup"><span class="glyphicon glyphicon-copy"></span></button>' +
                '</span>' +
                '</div>';
            obj.insertAdjacentHTML('beforeBegin', html);
        }
        function delConfig4(obj) {
            $(obj).parent().parent().remove();
        }
    </script>

    <script>
        function countRewardGrp(obj) {
            var rewardInput = 'i_countReward' + count_05;
            var rewardAddBtn = 'showRewardModel5' + count_05;
            count_05 += 1;
            html = '<div name="cfg5" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">累计领奖次数:</label>' +
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
        function checkValue() {
            var flag = true;
            var minValue=Number($("#i_baodi_scope_min").val());
            var maxValue=Number($("#i_baodi_scope_max").val());
            if (minValue >= maxValue){
                alert("最大值不能小于等于最小值！");
                flag = false;
            }
            return flag;
        }

    </script>

    <script>
        function validate() {
            if (checkValue() && checkBaodi() && checkBaoDiRange() && checkBaoDiRangeInfo()){
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
        <h3>聚宝盆
            <span class="glyphicon glyphicon-question-sign"
                  title="${msg['jsp.activity.title26']}"
                  data-container="body" data-toggle="popover" data-placement="right"
                  data-content="${msg['jsp.activity.content26']}">
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
                                onclick="addShowGridCfg(this,8)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="costGroup" class="col-lg-12 col-sm-12 col-md-12">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>抽奖道具</legend>
                <fieldset>
                    <div class="input-group saltIp">
                        <label class="input-group-addon">大奖系数:</label>
                        <input name="i_big_limit" id="i_big_limit" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">一等奖系数:</label>
                        <input name="i_one_limit" id="i_one_limit" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">二等奖系数:</label>
                        <input name="i_two_limit" id="i_two_limit" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">三等奖系数:</label>
                        <input name="i_three_limit" id="i_three_limit" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">金元宝奖池系数:</label>
                        <input name="i_gold_limit" id="i_gold_limit" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                    </div>
                    <div style="margin-top: 5px" class="input-group saltIp">
                        <label class="input-group-addon">抽奖道具:</label>
                        <input name="i_costItem" id="i_costItem"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">1抽道具消耗数量:</label>
                        <input name="i_oneCostItem" id="i_oneCostItem"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">1抽元宝消耗:</label>
                        <input name="i_oneCostGold" id="i_oneCostGold"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">10抽道具消耗数量:</label>
                        <input name="i_tenCostItem" id="i_tenCostItem"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">10抽元宝消耗:</label>
                        <input name="i_tenCostGold" id="i_tenCostGold"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">每抽赠送道具:</label>
                        <span class="input-group-btn">
                            <button class="btn btn-info" type="button" data-toggle="tooltip" title="每抽赠送道具" id="addGiveGiftBtn"
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
                        <label class="input-group-addon">大奖权重:</label>
                        <input name="i_item_big" id="i_item_big" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">一等奖权重:</label>
                        <input name="i_item_one" id="i_item_one" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">二等奖权重:</label>
                        <input name="i_item_two" id="i_item_two" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">三等奖权重:</label>
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
                        <label class="input-group-addon">大奖权重:</label>
                        <input name="i_gold_big" id="i_gold_big" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">一等奖权重:</label>
                        <input name="i_gold_one" id="i_gold_one" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">二等奖权重:</label>
                        <input name="i_gold_two" id="i_gold_two" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">三等奖权重:</label>
                        <input name="i_gold_three" id="i_gold_three"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="rewardPool" class="col-lg-9 col-sm-9 col-md-9">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>奖池数据配置</legend>
                <fieldset>
                    <div class="input-group" id="jianglishujubig">
                        <label class="input-group-addon">大奖奖池数据:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtnBig"
                                onclick="jiangliGrpBig(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                    <div style="margin-top: 5px" class="input-group" id="jianglishujuone">
                        <label class="input-group-addon">一等奖奖池数据:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtnOne"
                                onclick="jiangliGrpOne(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                    <div style="margin-top: 5px" class="input-group" id="jianglishujutwo">
                        <label class="input-group-addon">二等奖奖池数据:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtnTwo"
                                onclick="jiangliGrpTwo(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                    <div style="margin-top: 5px" class="input-group" id="jianglishujuthree">
                        <label class="input-group-addon">三等奖奖池数据:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtnThree"
                                onclick="jiangliGrpThree(this)"><span class="glyphicon glyphicon-plus"></span></button>
                    </div>
                </fieldset>
            </div>
        </div>
        <%--<div id="baodiPool" class="col-lg-7 col-sm-7 col-md-7">--%>
            <%--&lt;%&ndash;<div class="col-lg-1"></div>&ndash;%&gt;--%>
            <%--<div class="col-lg-12">--%>
                <%--<legend>保底次数配置</legend>--%>
                <%--<fieldset>--%>
                    <%--<div class="input-group" id="baodishuju">--%>
                        <%--<label class="input-group-addon">大奖保底次数:</label>--%>
                        <%--<input type="text" class="form-control" id="i_baodi_big_num" name="i_baodi_big_num" onkeyup="value=value.replace(/\D/g,'')">--%>
                        <%--<label class="input-group-addon">一等奖保底次数:</label>--%>
                        <%--<input type="text" class="form-control" id="i_baodi_one_num" name="i_baodi_one_num" onkeyup="value=value.replace(/\D/g,'')">--%>
                        <%--<label class="input-group-addon">二等奖保底次数:</label>--%>
                        <%--<input type="text" class="form-control" id="i_baodi_two_num" name="i_baodi_two_num" onkeyup="value=value.replace(/\D/g,'')">--%>
                        <%--&lt;%&ndash;<label class="input-group-addon">三等奖保底次数:</label>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;<input type="text" class="form-control" id="i_baodi_three_num" name="i_baodi_three_num" onkeyup="value=value.replace(/\D/g,'')">&ndash;%&gt;--%>
                    <%--</div>--%>
                <%--</fieldset>--%>
            <%--</div>--%>
        <%--</div>--%>

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

        <div id="goldPool" class="col-lg-12 col-sm-12 col-md-12">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>金元宝池配置</legend>
                <fieldset>
                    <div class="input-group saltIp">
                        <label class="input-group-addon">纯道具抽概率:</label>
                        <input name="i_item_probability" id="i_item_probability" value="" placeholder="int值/万分比"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">花金元宝抽概率:</label>
                        <input name="i_gold_probability" id="i_gold_probability" value="" placeholder="int值/万分比"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">抽取奖池比例:</label>
                        <input name="i_get_probability" id="i_get_probability" value="" placeholder="int值/万分比"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">金元宝奖池起始值:</label>
                        <input name="i_gold_init" id="i_gold_init" value="0"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">触发系统金元宝基准:</label>
                        <input name="i_gold_standard" id="i_gold_standard" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                    </div>
                    <div style="margin-top: 5px" class="input-group saltIp">
                        <label class="input-group-addon">系统投入金元宝日上限:</label>
                        <input name="i_gold_upper_limit" id="i_gold_upper_limit" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">单次补充的金元宝:</label>
                        <input name="i_gold_set" id="i_gold_set" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">奖池总上限:</label>
                        <input name="i_total_limit" id="i_total_limit" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">单次奖励上限:</label>
                        <input name="i_one_reward_limit" id="i_one_reward_limit" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">每日灵玉奖励次数上限:</label>
                        <input name="i_day_reward_limit" id="i_day_reward_limit" value=""
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="goldBaoDiPool" class="col-lg-6 col-sm-6 col-md-6">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>金元宝池保底</legend>
                <fieldset>
                    <div class="input-group" id="goldbaodishuju">
                        <%--<label class="input-group-addon">金元宝池:</label>--%>
                        <%--<button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="goldbaodiBtn"--%>
                                <%--onclick="goldbaodiGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>--%>
                        <%--<input type="hidden" id="cfgCount5" value="0">--%>
                        <label class="input-group-addon">保底范围最小值:</label>
                        <input type="text" class="form-control" id="i_baodi_scope_min" name="i_baodi_scope_min" onkeyup="value=value.replace(/\D/g,'')">
                        <label class="input-group-addon">保底范围最大值:</label>
                        <input type="text" class="form-control" id="i_baodi_scope_max" name="i_baodi_scope_max" onkeyup="value=value.replace(/\D/g,'')">
                    </div>
                </fieldset>
            </div>
        </div>
        <div id="countReward" class="col-lg-9 col-sm-9 col-md-9">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>累计领奖配置</legend>
                <fieldset>
                    <div class="input-group" id="countRewardshuju">
                        <label class="input-group-addon">累计领奖:</label>
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
