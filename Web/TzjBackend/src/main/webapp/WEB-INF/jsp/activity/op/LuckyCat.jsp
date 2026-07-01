<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>招财猫</title>
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
    <%--    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>--%>
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
            $("#ratePool").removeClass("col-lg-4 col-sm-4 col-md-4").addClass("col-lg-6 col-sm-6 col-md-6");
            $("#gearPool").removeClass("col-lg-9 col-sm-9 col-md-9").addClass("col-lg-9 col-sm-9 col-md-9");
        }
    </script>

    <script>
        function jiangliGrp(obj) {
            changeStyleClass();
            html = '<div class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">奖品倍率:</label>' +
                '<input type="text" class="form-control" id="i_weight" name="i_rate" placeholder="例:120" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:auto;">' +
                '<label class="input-group-addon">奖品权重:</label>' +
                '<input type="text" class="form-control" id="i_weight" name="i_weight" placeholder="例:1000" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:auto;">' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" id="delGridGroup"><span class="glyphicon glyphicon-minus"></span></button>' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="复制" id="copyGridGroup"><span class="glyphicon glyphicon-copy"></span></button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html)
        }
    </script>

    <script>
        function addSingleCfg(obj) {
            changeStyleClass();
            html = '<div name="cfg1" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">消耗元宝:</label>' +
                '<input type="text" class="form-control" id="consume" name="consume" placeholder="例:200" onkeyup="value=value.replace(/\\D/g,\'\')" class="input-xlarge" style="max-width:70px;" required/>' +
                '<label class="input-group-addon">充值档位:</label>' +
                '<input type="text" class="form-control" id="recharge" name="recharge" placeholder="例:100(分)" onkeyup="value=value.replace(/\\D/g,\'\')" class="input-xlarge" style="max-width:110px;" required/>' +
                '<label class="input-group-addon">最小奖池序号:</label>' +
                '<select id="minIndex" name="minIndex" onchange="" class="form-control" style="min-width:100px;width: 101px;">' +
                '<option value="0">1号奖品</option>' +
                '<option value="1">2号奖品</option>' +
                '<option value="2">3号奖品</option>' +
                '<option value="3">4号奖品</option>' +
                '<option value="4">5号奖品</option>' +
                '<option value="5">6号奖品</option>' +
                '<option value="6">7号奖品</option>' +
                '<option value="7">8号奖品</option>' +
                '</select>' +
                '<label class="input-group-addon">最大奖池序号:</label>' +
                '<select id="maxIndex" name="maxIndex" onchange="" class="form-control" style="min-width:100px;width: 101px;">' +
                '<option value="0">1号奖品</option>' +
                '<option value="1">2号奖品</option>' +
                '<option value="2">3号奖品</option>' +
                '<option value="3">4号奖品</option>' +
                '<option value="4">5号奖品</option>' +
                '<option value="5">6号奖品</option>' +
                '<option value="6">7号奖品</option>' +
                '<option value="7">8号奖品</option>' +
                '</select>' +
                // '<label class="input-group-addon">最小奖池:</label>' +
                // '<input type="text" class="form-control" id="minIndex" name="minIndex" placeholder="例:0" onkeyup="value=value.replace(/\\D/g,\'\')" class="input-xlarge" style="max-width:60px;" required/>' +
                '<label class="input-group-addon">全服剩余次数:</label>' +
                '<input type="text" class="form-control" id="serverRestNum" name="serverRestNum" placeholder="例:7" onkeyup="value=value.replace(/\\D/g,\'\')" class="input-xlarge" style="max-width:60px;" required/>' +

                // '<input type="text" class="form-control" id="p_reward" name="p_reward" style="max-width:auto;">' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" id="delGridGroup"><span class="glyphicon glyphicon-minus"></span></button>' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="复制" id="copyGridGroup"><span class="glyphicon glyphicon-copy"></span></button>' +
                '</span>' +
                '</div>'
            obj.insertAdjacentHTML('beforeBegin', html)
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
            return
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
            return true;
        }
    </script>
</head>
<body style="font-size: 12px">
<div class="container-fluid">
    <div id="header">
        <h3>招财猫活动<span class="glyphicon glyphicon-question-sign" title="${msg['jsp.activity.title10']}"
                       data-container="body" data-toggle="popover" data-placement="right"
                       data-content="${msg['jsp.activity.content10']}"></span></h3>
    </div>

    <form id="activity_form" method="post" class="main form-horizontal" action="#">
        <div class="container-fluid well">
            <div class="row-fluid">
                <jsp:include page="./BaseActivity_new.jsp"/>

                <div class="row-fluid">
                    <div class="span3"><b>活动参数</b></div>
                </div>

                <div id="ratePool" class="col-lg-6 col-sm-6 col-md-6">
                    <div class="col-lg-6">
                        <legend>奖池倍率数据配置</legend>
                        <fieldset>
                            <div class="input-group" id="rateshuju">
                                <%--                                <label class="input-group-addon">奖池数据:</label>--%>
                                <div class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">
                                    <label class="input-group-addon">奖品1倍率:</label>
                                    <input type="text" class="form-control" id="rate0" name="rate" placeholder="例:120"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:70px;">
                                    <label class="input-group-addon">奖品1权重:</label>
                                    <input type="text" class="form-control" id="weight0" name="weight" placeholder="例:1000"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:75px;">
                                </div>
                                <div class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">
                                    <label class="input-group-addon">奖品2倍率:</label>
                                    <input type="text" class="form-control" id="rate1" name="rate" placeholder="例:120"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:70px;">
                                    <label class="input-group-addon">奖品2权重:</label>
                                    <input type="text" class="form-control" id="weight1" name="weight" placeholder="例:1000"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:75px;">
                                </div>
                                <div class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">
                                    <label class="input-group-addon">奖品3倍率:</label>
                                    <input type="text" class="form-control" id="rate2" name="rate" placeholder="例:120"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:70px;">
                                    <label class="input-group-addon">奖品3权重:</label>
                                    <input type="text" class="form-control" id="weight2" name="weight" placeholder="例:1000"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:75px;">
                                </div>
                                <div class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">
                                    <label class="input-group-addon">奖品4倍率:</label>
                                    <input type="text" class="form-control" id="rate3"  name="rate" placeholder="例:120"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:70px;">
                                    <label class="input-group-addon">奖品4权重:</label>
                                    <input type="text" class="form-control" id="weight3" name="weight" placeholder="例:1000"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:75px;">
                                </div>
                                <div class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">
                                    <label class="input-group-addon">奖品5倍率:</label>
                                    <input type="text" class="form-control" id="rate4"  name="rate" placeholder="例:120"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:70px;">
                                    <label class="input-group-addon">奖品5权重:</label>
                                    <input type="text" class="form-control" id="weight4" name="weight" placeholder="例:1000"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:75px;">
                                </div>
                                <div class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">
                                    <label class="input-group-addon">奖品6倍率:</label>
                                    <input type="text" class="form-control" id="rate5"  name="rate" placeholder="例:120"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:70px;">
                                    <label class="input-group-addon">奖品6权重:</label>
                                    <input type="text" class="form-control" id="weight5" name="weight" placeholder="例:1000"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:75px;">
                                </div>
                                <div class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">
                                    <label class="input-group-addon">奖品7倍率:</label>
                                    <input type="text" class="form-control" id="rate6"  name="rate" placeholder="例:120"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:70px;">
                                    <label class="input-group-addon">奖品7权重:</label>
                                    <input type="text" class="form-control" id="weight6" name="weight" placeholder="例:1000"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:75px;">
                                </div>
                                <div class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">
                                    <label class="input-group-addon">奖品8倍率:</label>
                                    <input type="text" class="form-control" id="rate7"  name="rate" placeholder="例:120"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:70px;">
                                    <label class="input-group-addon">奖品8权重:</label>
                                    <input type="text" class="form-control" id="weight7" name="weight" placeholder="例:1000"
                                           onkeyup="value=value.replace(/\D/g,'')" style="max-width:75px;">
                                </div>
                                <%--                                <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtn"--%>
                                <%--                                        onclick="jiangliGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>--%>
                            </div>
                        </fieldset>
                    </div>
                </div>

                <div id="gearPool" class="col-lg-11 col-sm-11 col-md-11">
                    <div class="col-lg-11">
                        <legend>规则数据配置</legend>
                        <fieldset>
                            <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtn"
                                    onclick="addSingleCfg(this)"><span class="glyphicon glyphicon-plus"></span></button>
                            <input type="hidden" id="cfgCount1" value="0">
                        </fieldset>
                    </div>
                </div>

                <div id="condition" class="row-fluid">
                </div>

                <br/>
            </div>
        </div>
    </form>
</div>
<div class="col-md-12 col-sm-12 col-lg-12" style="height: 30px"></div>
<div class="col-sm-5 col-sm-5"></div>
<div class="row-fluid col-sm-7 col-md-7">
    <input type="button" value="${msg['activity.add.submit']}" onclick="submitActivity()"
           class="btn btn-sm btn-danger" style="margin-right: 30px"/>
    <a href="javascript:;" class="file">${msg['jsp.giftbag.button.import']}
        <input type="file" name="" id="" onchange="importf(this)">
    </a>
    <%--                    <input type="button" value="添加此模板" onclick="addTemplate()" class="btn"/>--%>
    <%--                    <input type="button" value="删除此模板" onclick="deleteTemplate()" class="btn"/>--%>
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


<div id="addItemModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
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
