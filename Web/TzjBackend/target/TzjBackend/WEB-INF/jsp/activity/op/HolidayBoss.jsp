<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>首领狂欢</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${base}/css/activity.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/common.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
    <link rel="stylesheet" href="${base}/css/alertify.css">
    <link rel="stylesheet" href="${base}/css/alertify.default.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap3.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/alertify.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<%--    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.13.1/xlsx.full.min.js"></script>--%>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-select.min.css">
    <script src="${base}/css/bootstrap/js/bootstrap-select.min.js"></script>
    <script src="${base}/css/bootstrap/js/bootstrap-select.zh_CN.min.js"></script>
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
            loadActivityBossType(true);
            loadActivityFestivalType();
            $("#publishActivity").modal('hide');
            $("[data-toggle='popover']").popover();
        });
    </script>

    <script>
        /**
         * 加载活动boss信息
         */
        function loadActivityBossType(isAsync) {
            $.ajax({
                url: base + "/activity/getActivityBossType",
                method: "post",
                dataType: "json",
                async: isAsync,
                success: function (data) {
                    if (!data.ok) {
                        alert("load failed");
                        return;
                    }
                    $("#bossCfg").empty();
                    var html = '';
                    $.each(data.data, function (i, n) {
                        html += '<input type="checkbox" name="bossList" value="'+n.id+'">' +
                        '<label>'+n.name+'</label>';
                    });
                    $("#bossCfg").html(html);
                }
            });
        }
        //加载所有模型库
        function loadModel() {
            $.ajax({
                url: base + "/activityConfig/getAllModel",
                method: "post",
                dataType: "json",
                success: function (data) {
                    if (!data.ok) {
                        alert("Model load failed");
                        return;
                    }
                    $("#magicId").empty();
                    $.each(data.data, function (i, n) {
                        $("#magicId").append("<option value='" + n.id +"'>" + n.tips +"</option>");
                    });
                }
            });
        }
    </script>

    <script>
        function addSaltIpGrp(obj) {
            var rewardInput = 'item' + count_01;
            var rewardAddBtn = 'showRewardModel' + count_01;
            count_01 += 1;
            var html = '<div name="cfg1" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">单价:</label>' +
                '<input type="text" class="form-control" name="price" onkeyup="value=value.replace(/\\D/g,\'\')" style="">' +
                '<label class="input-group-addon">出售货币:</label>' +
                '<input type="text" class="form-control" name="coin" onkeyup="value=value.replace(/\\D/g,\'\')" style="">' +
                '<label class="input-group-addon">单日限购:</label>' +
                '<input type="text" class="form-control" name="limit" onkeyup="value=value.replace(/\\D/g,\'\')" style="">' +
                '<label class="input-group-addon">出售道具:</label>'+
                // '<input type="text" class="form-control" id="item" name="item" style="max-width:auto;">'+
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="出售道具" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + rewardInput + ',' + rewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="item"/></div>' +
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
        function bossjiangliGrp(obj) {
            var rewardInput = 'bossGiftReward' + count_02;
            var rewardAddBtn = 'showRewardModel2' + count_02;
            count_02 += 1;
            var html = '<div name="cfg2" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">奖励权重:</label>' +
                '<input type="text" class="form-control" name="bossGiftWeight" onkeyup="value=value.replace(/\\D/g,\'\')" style="">' +
                '<label class="input-group-addon">奖励组:</label>'+
                // '<input type="text" class="form-control" name="bossGiftReward"  style="max-width:auto;">'+
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖励组" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + rewardInput + ',' + rewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="bossGiftReward"/></div>' +
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
        function giftjiangliGrp(obj) {
            var count = $("#cfgCount3").val();
            console.log("cfgCount3:"+count);
            $("#cfgCount3").val(Number(count)+1);
            var html = '<div name="cfg3" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">礼包ID:</label>' +
                '<input type="text" class="form-control" name="giftId" onkeyup="value=value.replace(/\\D/g,\'\')" style="">' +
                '<input type="hidden" name="boxRewardCount" value="0">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="boxjiangliGrpBtn'+count+'" ' +
                'onclick="boxjiangliGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>' +
                '<input type="hidden" name="cfgCount" value="0">' +
                '</div>'

            obj.insertAdjacentHTML('beforeBegin', html)
        }
    </script>
    <script>
        function boxjiangliGrp(obj) {
            var count = $(obj).parent().find("input[name='cfgCount']").eq(0).val();
            // console.log("cfgCount:"+count);
            var boxGiftWeight = 'boxGiftWeight' + count;
            var boxGiftReward = 'boxGiftReward' + count;
            $("input[name='cfgCount']").eq(0).val(Number(count)+1);
            var rewardInput = 'boxGiftReward' + count_03;
            var rewardAddBtn = 'showRewardModel3' + count_03;
            count_03 += 1;
            var html = '<div name="cfg" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">礼包权重:</label>' +
                '<input type="text" class="form-control" id="'+boxGiftWeight+'" name="boxGiftWeight" onkeyup="value=value.replace(/\\D/g,\'\')" style="">' +
                '<label class="input-group-addon">奖励组:</label>'+
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="奖励组" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + rewardInput + ',' + rewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="boxGiftReward"/></div>' +
                '</span>' +
                // '<input type="text" class="form-control" id="'+boxGiftReward+'" name="boxGiftReward" style="max-width:auto;">'+
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" id="delSubGridGroup" onclick="delboxjiangliGrp(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'

            obj.insertAdjacentHTML('beforeBegin', html);

            // console.log($(obj).parent().find("input[name='boxRewardCount']"));
            var count = $(obj).parent().find("input[name='boxRewardCount']").val();
            $(obj).parent().find("input[name='boxRewardCount']").val(count*1+1);
        }

        function delboxjiangliGrp(obj) {
            // console.log($(obj).parent().parent().parent().find("input[name='boxRewardCount']"));

            var count = $(obj).parent().parent().parent().find("input[name='boxRewardCount']").val();
            if(count>0){
                $(obj).parent().parent().parent().find("input[name='boxRewardCount']").val(count*1-1);
            }

            $(obj).parent().parent().remove();
        }

    </script>

    <script>
        $('body').on('hidden.bs.modal', '.modal', function () {
            $(this).removeData('bs.modal');
        });
        $(document).on('click', '#delGridGroup', function () {
            var el = this.parentNode.parentNode;
            var saltIp = $(this).parent().parent().find('#saltIp').val();
            el.parentNode.removeChild(el)

            var count = $(this).parent().parent().find("input[name='boxRewardCount']").val();
            if(count>0){
                $(this).parent().parent().find("input[name='boxRewardCount']").val(count*1-1);
            }

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
        <h3>首领狂欢<span class="glyphicon glyphicon-question-sign" title="${msg['jsp.activity.title11']}"
                      data-container="body" data-toggle="popover" data-placement="right"
                      data-content="${msg['jsp.activity.content11']}"></span></h3>
    </div>

    <form id="activity_form" method="post" class="main form-horizontal" action="#">

        <jsp:include page="./BaseActivity_new.jsp"/>

        <div class="col-lg-12 col-sm-12 col-md-12">
            <div class="col-lg-1"></div>
            <div class="col-lg-11">
                <legend>客户端配置</legend>
                <fieldset>
                    <div class="input-group" id="clientGroup">
                        <label class="input-group-addon">奖励展示:</label>
                        <div class="col-md-12 col-sm-12 col-xs-12">
                            <select id="magicId" name="magicId" onchange="" class="form-control">

                            </select>
                        </div>
                        <%--<label class="input-group-addon">奖励展示:</label>--%>
                        <%--<input type="text" class="form-control" id="magicId" name="magicId" style="">--%>
                        <label class="input-group-addon">礼包奖励展示:</label>
                        <input type="text" class="form-control" id="boxId" name="boxId" style="">
                        <label class="input-group-addon">奖励展示列表:</label>
                        <span class="input-group-btn">
                            <button class="btn btn-info" type="button" data-toggle="tooltip" title="奖励展示列表" id="addShowItemsBtn"
                                    onclick="showAddItemUI('showItems','addShowItemsBtn')"><span class="glyphicon glyphicon-plus"></span></button>
                        </span>
                        <input type="hidden" class="form-control" id="showItems" name="showItems" style="">
                    </div>
                </fieldset>
            </div>
        </div>

        <div class="col-lg-12 col-sm-12 col-md-12">
            <div class="col-lg-1"></div>
            <div class="col-lg-11">
                <legend>限购商品配置</legend>
                <fieldset>
                    <div class="input-group" id="saltIpGroup">
                        <label class="input-group-addon">限购商品配置:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addSaltIpGrpBtn"
                                onclick="addSaltIpGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>
                        <input type="hidden" id="cfgCount1" value="0">
                    </div>
                </fieldset>
            </div>
        </div>

        <div class="col-lg-12 col-sm-12 col-md-12">
            <div class="col-lg-1"></div>
            <div class="col-lg-11">
                <legend>BOSS类型配置</legend>
                <fieldset>
                    <div class="input-group" id="otherCfg">
                        <div class="input-group" id="bossCfg" style="width:100%;padding:0 0 1px 0;">
<%--                            <label class="input-group-addon">BOSS配置:</label>--%>
<%--                            <input type="checkbox" class="form-control" name="bossList" style="">--%>
                        </div>
                    </div>
                </fieldset>
            </div>

            <div class="col-lg-1"></div>
            <div class="col-lg-11">
                <legend>BOSS奖励</legend>
                <fieldset>
                    <div class="input-group" id="bossjiangli">
                        <label class="input-group-addon">BOSS奖励配置:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="bossjiangliBtn"
                                onclick="bossjiangliGrp(this)"><span class="glyphicon glyphicon-plus"></span>
                        </button>
                        <input type="hidden" id="cfgCount2" value="0">
                    </div>
                </fieldset>
            </div>
        </div>

        <div class="col-lg-12 col-sm-12 col-md-12">
            <div class="col-lg-1"></div>
            <div class="col-lg-11">
                <legend>礼包奖励</legend>
                <fieldset>
                    <div class="input-group" id="giftjiangli">
                        <label class="input-group-addon">礼包奖励配置:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="giftjiangliBtn"
                                onclick="giftjiangliGrp(this)"><span class="glyphicon glyphicon-plus"></span>
                        </button>
                        <input type="hidden" id="cfgCount3" value="0">
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
