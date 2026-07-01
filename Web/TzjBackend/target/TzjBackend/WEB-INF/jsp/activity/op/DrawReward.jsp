<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>天帝宝库</title>
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
        var changeStyle = false;
        var count_01 = 0;
        var count_02 = 0;
        var count_03 = 0;
        var count_04 = 0;
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

        function changeStyleClass() {
            if (!changeStyle) {
                $("#singleGroup").removeClass("col-lg-3 col-sm-3 col-md-3").addClass("col-lg-6 col-sm-6 col-md-6");
                $("#serverGroup").removeClass("col-lg-3 col-sm-3 col-md-3").addClass("col-lg-6 col-sm-6 col-md-6");
                $("#rewardPool").removeClass("col-lg-3 col-sm-3 col-md-3").addClass("col-lg-6 col-sm-6 col-md-6");
                $("#costGroup").removeClass("col-lg-3 col-sm-3 col-md-3").addClass("col-lg-6 col-sm-6 col-md-6");
                changeStyle = true;
            }
        }
    </script>

    <script>
        function addSingleCfg(obj) {
            changeStyleClass();
            console.log(count_01);
            var rewardInput = 'p_reward' + count_01;
            var rewardAddBtn = 'showRewardModel' + count_01;
            count_01 += 1;
            html = '<div name="cfg1" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">轮次:</label>' +
                '<input type="text" class="form-control" id="p_round" name="p_round" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:100px;">' +
                '<label class="input-group-addon">奖品:</label>' +
                // '<input type="text" class="form-control" id="p_reward" name="p_reward" style="max-width:auto;">' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + rewardInput + ',' + rewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="p_reward"/></div>' +
                '</span>' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delExChangeConfig(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html)
        }
        //删除配置
        function delExChangeConfig(obj) {
            $(obj).parent().parent().remove();
        }
    </script>
    <script>
        function addServerCfg(obj) {
            changeStyleClass();
            var rewardInput = 'g_reward' + count_02;
            var rewardAddBtn = 'showRewardModel2' + count_02;
            count_02 += 1;
            html = '<div name="cfg2" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">全服进度:</label>' +
                '<input type="text" class="form-control" id="g_s_reach" name="g_s_reach" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:100px;">' +
                '<label class="input-group-addon">个人进度:</label>' +
                '<input type="text" class="form-control" id="g_p_reach" name="g_p_reach" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:100px;">' +
                '<label class="input-group-addon">奖品:</label>' +
                // '<input type="text" class="form-control" id="g_reward" name="g_reward" style="max-width:auto;">' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + rewardInput + ',' + rewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="g_reward"/></div>' +
                '</span>' +
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
        function jiangliGrp(obj) {
            changeStyleClass();
            var rewardInput = 'i_reward' + count_03;
            var rewardAddBtn = 'showRewardModel3' + count_03;
            count_03 += 1;
            html = '<div name="cfg3" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">奖品权重:</label>' +
                '<input type="text" class="form-control" id="i_weight" name="i_weight" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:100px;">' +
                '<label class="input-group-addon">大奖:</label>' +
                '<select id="i_big" name="i_big" onchange="" class="form-control" style="min-width:40px;width: 61px;">' +
                '<option value="0">否</option>' +
                '<option value="1">是</option>' +
                '</select>' +
                '<label class="input-group-addon">奖品:</label>' +
                // '<input type="text" class="form-control" id="i_reward" name="i_reward" style="max-width:auto;">' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖品" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + rewardInput + ',' + rewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="i_reward"/></div>' +
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
        $(document).on('click', '#copyGridGroup', function () {
            var el = this.parentNode.parentNode;
            var newEl = el.cloneNode(true);
            el.parentNode.insertBefore(newEl, el);
        });
        $(document).on('click', '#delGridGroup', function () {
            var el = this.parentNode.parentNode;
            var saltIp = $(this).parent().parent().find('#saltIp').val();
            el.parentNode.removeChild(el);
            return;
            // if (saltIp == ""){
            //     el.parentNode.removeChild(el)
            //     return
            // }
            // alertify.confirm('您确定要删除选中的命令？',
            //     function (e) {
            //         el.parentNode.removeChild(el)
            //         return
            //     }
            //     )

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
        <h3>天帝宝库<span class="glyphicon glyphicon-question-sign" title="${msg['jsp.activity.title5']}"
                      data-container="body" data-toggle="popover" data-placement="right"
                      data-content="${msg['jsp.activity.content5']}"></span></h3>
    </div>

    <form id="activity_form" method="post" class="main form-horizontal" action="#">

        <jsp:include page="./BaseActivity_new.jsp"/>
        <div id="costGroup" class="col-lg-3 col-sm-3 col-md-3">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>抽奖道具</legend>
                <fieldset>
                    <div class="input-group saltIp">
                        <label class="input-group-addon">大奖系数:</label>
                        <input name="i_big_limit" id="i_big_limit" value="" title="默认抽奖道具" placeholder="大奖n次之后开放"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">抽奖道具:</label>
                        <input name="i_costItem" id="i_costItem" value="1061" title="默认抽奖道具"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">抽奖元宝:</label>
                        <input name="i_costGold" id="i_costGold" value="50" title="默认抽奖道具"
                               type="text" class=" form-control" onkeyup="value=value.replace(/\D/g,'')"/>
                        <span class="input-group-btn">
                            <button class="btn btn-info" type="button" data-toggle="tooltip" title="元宝抽奖赠送道具" id="addGoldGiftBtn"
                                    onclick="showAddItemUI('i_GoldGift','addGoldGiftBtn')"><span class="glyphicon glyphicon-plus"></span></button>
                            <input type="hidden" id="cfgCount1" value="0">
                        </span>
                        <input name="i_GoldGift" id="i_GoldGift" value="" type="text" class=" hide"/>
                    </div>
                </fieldset>
            </div>
        </div>
        <div id="singleGroup" class="col-lg-3 col-sm-3 col-md-3">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>个人轮次奖励</legend>
                <fieldset>
                    <div class="input-group" id="singlePrcCfg">
                        <label class="input-group-addon">进度配置:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addSingleCfgBtn"
                                onclick="addSingleCfg(this)"><span class="glyphicon glyphicon-plus"></span></button>
                        <input type="hidden" id="cfgCount2" value="0">
                    </div>
                </fieldset>
            </div>
        </div>
        <div id="serverGroup" class="col-lg-3 col-sm-3 col-md-3">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>全服进度奖励</legend>
                <fieldset>
                    <div class="input-group" id="serverPrcCfg">
                        <label class="input-group-addon">进度配置:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addServerCfgBtn"
                                onclick="addServerCfg(this)"><span class="glyphicon glyphicon-plus"></span></button>
                        <input type="hidden" id="cfgCount3" value="0">
                    </div>
                </fieldset>
            </div>
        </div>
        <div id="rewardPool" class="col-lg-3 col-sm-3 col-md-3">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>奖池数据配置</legend>
                <fieldset>
                    <div class="input-group" id="jianglishuju">
                        <label class="input-group-addon">奖池数据:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtn"
                                onclick="jiangliGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>
                        <input type="hidden" id="cfgCount4" value="0">
                    </div>
                </fieldset>
            </div>
        </div>
        <div id="luckyGroup" class="col-lg-3 col-sm-3 col-md-3">
            <%--<div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>抽奖幸运值配置</legend>
                <fieldset>
                    <div class="input-group saltIp">
                        <label class="input-group-addon">参与一次增加的幸运值:</label>
                        <input name="oneLuckyValue" id="oneLuckyValue"
                               type="text" class=" form-control" style="width:100px;" onkeyup="value=value.replace(/\D/g,'')"/>
                        <label class="input-group-addon">奖励列表:</label>
                        <span class="input-group-btn">
                            <button class="btn btn-info" type="button" data-toggle="tooltip" title="奖励列表" id="addOneLuckyBtn"
                                    onclick="showAddItemUI('luckyAwardGift','addOneLuckyBtn')"><span class="glyphicon glyphicon-plus"></span></button>
                            <input type="hidden" id="cfgCount5" value="0">
                        </span>
                        <input name="luckyAwardGift" id="luckyAwardGift" value="" type="text" class=" hide"/>
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
