<%--
  Created by IntelliJ IDEA.
  User: 452
  Date: 2020/10/23
  Time: 10:39
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>节日集字</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${base}/css/activity.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/common.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
    <link rel="stylesheet" href="${base}/css/alertify.css">
    <link rel="stylesheet" href="${base}/css/alertify.default.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap3.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <%--<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-select.min.css">--%>
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/alertify.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
    <%--<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-select.min.js"></script>--%>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap3.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
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
        //boss副本数据
        var bossData;

        $(function () {

            $('.datetimepicker').datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd hh:ii',
                todayBtn: 1,
                autoclose: true
            });

            loadActivityBossType();
            loadAllItem();
            loadAllServerList();
            load(base, actType);
            loadActivityFestivalType();
            $("#publishActivity").modal('hide');
            $("[data-toggle='popover']").popover();
        });
    </script>
    <script>
        /**
         * 加载活动boss信息
         */
        function loadActivityBossType() {
            $.ajax({
                url: base + "/activity/getActivityBossType",
                method: "post",
                dataType: "json",
                success: function (data) {
                    if (!data.ok) {
                        alert("load failed");
                        return;
                    }
                    bossData = data.data;
                    console.log("load boss success!!");
                }
            });
        }
    </script>


    <script>
        function addSaltIpGrp(obj) {
            html = '<div class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">消耗货币:</label>' +
                '<input type="text" class="form-control"  name="coinType" onkeyup="value=value.replace(/\\D/g,\'\')" style="">' +
                '<label class="input-group-addon">兑换活跃:</label>' +
                '<input type="text" class="form-control"  name="coinCv" onkeyup="value=value.replace(/\\D/g,\'\')" style="">' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" id="delGridGroup"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html)
        }
    </script>

    <script>
        function jiangliGrp_01(obj,prefix,postfix) {
            console.log(count_01)
            var wordsRewardInput = 'words' + count_01
            var wordsRewardAddBtn = 'showAddWordsModel' + count_01
            count_01 += 1;
            html = '<div name="cfg2" class="input-group saltIp" style="width:100%;padding:0 0 1px 0; margin-left:fill  ">' +
                '<label class="input-group-addon">掉落奖励:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="添加掉落物品" id="' + wordsRewardAddBtn +'"  style="border:5px;width: 35px;height: 34px;" ' +
                'onclick="showAddItemUI(' + wordsRewardInput + ',' + wordsRewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + wordsRewardInput + '" type="text" name="'+prefix+'dropRewardItems'+postfix+'"/></div>' +
                '</span>' +
                '<label class="input-group-addon">掉落权重:</label>' +
                '<input type="text" class="form-control"  name="'+prefix+'dropWeight'+postfix+'" id="'+prefix+'dropWeight" onkeyup="value=value.replace(/\\D/g,\'\')" placeholder="掉落权重" >' +

                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delReward(this,\''+prefix+'\')"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html);
            if (prefix == "clone_") {
                var count = $(obj).parent().find("input[name='dropCloneReward']").val();
                $(obj).parent().find("input[name='dropCloneReward']").val(count*1+1);
            } else {
                var count = $(obj).parent().find("input[name='dropBossReward']").val();
                $(obj).parent().find("input[name='dropBossReward']").val(count*1+1);
            }
        }
        //删除奖励配置
        function delReward(obj,prefix) {
            if (prefix == "clone_"){
                var count = $(obj).parent().parent().parent().find("input[name='dropCloneReward']").val();
                if(count>0){
                    $(obj).parent().parent().parent().find("input[name='dropCloneReward']").val(count*1-1);
                }
                $(obj).parent().parent().remove();
            }else {
                var count = $(obj).parent().parent().parent().find("input[name='dropBossReward']").val();
                if(count>0){
                    $(obj).parent().parent().parent().find("input[name='dropBossReward']").val(count*1-1);
                }
                $(obj).parent().parent().remove();
            }
        }

        function jiangliGrp_Clone(obj) {
            console.log(count_02)
            count_02 += 1;
            var postfix = '_' + count_02;
            html = '<div name="cfg1" class="input-group saltIp" style="width:100%;padding:0 0 1px 0; margin-left:fill  ">' +
                '<div class="col-lg-11">'+
                '<legend>副本配置</legend>'+
                '<fieldset>'+
                '<label class="input-group-addon">掉落副本</label>' +
                '<input type="text" class="form-control"  name="dropCloneMap" onkeyup="value=value.replace(/\\D/g,\'\')" placeholder="掉落副本" >' +
                '<div class="hide"><input id="' +'cloneIdx'+count_02 + '" value='+count_02+' name="cloneIdx"/></div>'+
                '<div class="input-group" id="jianglishuju2">'+
                '<label class="input-group-addon">奖励配置:</label>'+
                '<input type="hidden" name="dropCloneReward" value="0">'+
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtn'+count_02+'"'+
                'onclick="jiangliGrp_01(this,\'clone_\',\''+postfix+'\')"><span class="glyphicon glyphicon-plus"></span>'+
                '</button>'+
                '</div>'+
                '</fieldset>'+
                '</div>'+
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delCloneConfig(this,count_02)"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html)
        }
        //删除副本配置
        function delCloneConfig(obj,count) {
            count-=1;
            count_02=count;
            $(obj).parent().parent().remove();
        }

        function jiangliGrp_Boss(obj) {
            console.log(count_03);
            count_03 += 1;
            var postfix = '_' + count_03;
            html = '<div name="cfg3" class="input-group saltIp" style="width:100%;padding:0 0 1px 0; margin-left:fill  ">' +
                '<div class="col-lg-11">'+
                '<legend>Boss掉落配置</legend>'+
                '<fieldset>'+
                '<label class="input-group-addon">选择Boss类型</label>';
            console.log(bossData);
            if(bossData) {
                $.each(bossData, function (i, n) {
                    html += '<input type="checkbox" name="bossList' + postfix+'" value="' + n.id + '">' +
                        '<label>' + n.name + '</label>';
                });
            }
            else{
                html +='<label>没有boss数据,请重新加载boss数据,[后台数据加载]->[其他数据]->[运营活动BOSS类型加载]</label>';
            }

            html += '<div class="input-group" id="bossCfg"  name="dropBoss" style="width:100%;padding:0 0 1px 0;">'+
                '<div class="hide"><input id="' +'bossIdx'+count_03 + '" value='+count_03+' name="bossIdx"/></div>'+
                '<div class="input-group" id="jianglishuju3">'+
                '<label class="input-group-addon">奖励配置:</label>'+
                '<input type="hidden" name="dropBossReward" value="0">'+
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtn3'+count_03+'"'+
                'onclick="jiangliGrp_01(this,\'boss_\',\''+postfix+'\')"><span class="glyphicon glyphicon-plus"></span>'+
                '</button>'+
                '</div>'+
                '</fieldset>'+
                '</div>'+
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delBossConfig(this,count_03)"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html)
        }
        //删除Boss掉落配置
        function delBossConfig(obj,count) {
            count-=1;
            count_03=count;
            $(obj).parent().parent().remove();
        }

        function jiangliGrp_03(obj) {
            console.log(count_01)
            var rewardInput = 'reward' + count_01
            var rewardAddBtn = 'showAddItemModel' + count_01
            var wordsInput = 'words' + count_01
            var wordsAddBtn = 'showAddWordsModel' + count_01
            count_01 += 1;
            html = '<div name="cfg4" class="input-group saltIp" style="width:100%;padding:0 0 1px 0; margin-left:fill  ">' +
                '<label class="input-group-addon">收集字体:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="添加字体物品" id="' + wordsAddBtn +'"  style="border:5px;width: 35px;height: 34px;" ' +
                'onclick="showAddItemUI(' + wordsInput + ',' + wordsAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + wordsInput + '" name="wordItems"/></div>' +
                '</span>' +

                '<label class="input-group-addon">奖励宝箱:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="添加奖励" id="' + rewardAddBtn +'"  style="border:5px;width: 35px;height: 34px;" ' +
                'onclick="showAddItemUI(' + rewardInput + ',' + rewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="rewardBox"/></div>' +
                '</span>' +

                '<label class="input-group-addon">最大兑换次数:</label>' +
                '<input type="text" class="form-control"  name="exChangeLimit" onkeyup="value=value.replace(/\\D/g,\'\')" placeholder="最大兑换次数" >' +
                '<label class="input-group-addon">是否显示小红点:</label>' +
                '<div class="col-md-12 col-sm-12 col-xs-12">' +
                '<select name="isShowRedPoint" onchange="" class="form-control">' +
                 '<option value="1">是</option>'+
                 '<option value="0">否</option>'+
                '</select>' +
                '</div>'+
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delExChangeConfig(this)"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'

            obj.insertAdjacentHTML('beforeBegin', html)
        }
        //删除兑换配置
        function delExChangeConfig(obj) {
            $(obj).parent().parent().remove();
        }
    </script>

    <script>
        $('body').on('hidden.bs.modal', '.modal', function () {
            $(this).removeData('bs.modal');
        });
        $(document).on('click', '#delGridGroup', function () {
            var el = this.parentNode.parentNode
            var saltIp = $(this).parent().parent().find('#saltIp').val()
            el.parentNode.removeChild(el)
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
        <h3>节日集字<span class="glyphicon glyphicon-question-sign" title="${msg['jsp.activity.title13']}"
                      data-container="body" data-toggle="popover" data-placement="right"
                      data-content="${msg['jsp.activity.content13']}"></span></h3>
    </div>

    <form id="activity_form" method="post" class="main form-horizontal" action="#">

        <jsp:include page="./BaseActivity_new.jsp"/>

        <div class="col-lg-12 col-sm-12 col-md-12">
            <div class="col-lg-1"></div>
            <div class="col-lg-11">
                <legend>副本掉落配置</legend>
                <fieldset>
                    <div class="input-group" id="jianglishujua">
                        <label class="input-group-addon">奖励池配置:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtna"
                                onclick="jiangliGrp_Clone(this)"><span class="glyphicon glyphicon-plus"></span>
                        </button>
                    </div>
                </fieldset>
            </div>
        </div>

        <div class="col-lg-12 col-sm-12 col-md-12">
            <div class="col-lg-1"></div>
            <div class="col-lg-11">
                <legend>Boss掉落配置</legend>
                <fieldset>
                    <div class="input-group" id="jianglishujub">
                        <label class="input-group-addon">奖励池配置:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtnb"
                                onclick="jiangliGrp_Boss(this)"><span class="glyphicon glyphicon-plus"></span>
                        </button>
                    </div>
                </fieldset>
            </div>
        </div>


        <div class="col-lg-12 col-sm-12 col-md-12">
            <div class="col-lg-1"></div>
            <div class="col-lg-11">
                <legend>兑换配置</legend>
                <fieldset>
                    <div class="input-group" id="jianglishujuc">
                        <label class="input-group-addon">兑换配置:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtnc"
                                onclick="jiangliGrp_03(this)"><span class="glyphicon glyphicon-plus"></span>
                        </button>
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

