<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>连续累充</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${base}/css/activity.css" type="text/css" />
    <link rel="stylesheet" href="${base}/css/common.css" type="text/css" />
    <link rel="stylesheet" href="${base}/css/boxy.css" type="text/css" />
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
        $(function () {

            $('.datetimepicker').datetimepicker({
                language:  'zh-CN',
                format: 'yyyy-mm-dd hh:ii',
                todayBtn: 1,
                autoclose:true
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
        function addSaltIpGrp(obj){
            console.log(count_01);
            var fixRewardInput = 'i_fixRewardGroup' + count_01;
            var fixRewardAddBtn = 'showFixRewardModel' + count_01;
            var totalRewardInput = 'i_totalRewardGroup' + count_01;
            var totalRewardAddBtn = 'showTotalRewardModel' + count_01;
            count_01 += 1;
            var html = '<div name="cfg1" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">'+
                '<label class="input-group-addon">充值天数:</label>'+
                '<input type="text" class="form-control" name="i_day" onkeyup="value=value.replace(/\\D/g,\'\')" style="max-width:auto;">'+
                '<label class="input-group-addon">固定奖励组:</label>'+
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="固定奖励组" id="' + fixRewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + fixRewardInput + ',' + fixRewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + fixRewardInput + '" name="i_fixRewardGroup"/></div>' +
                '</span>' +
                // '<input type="text" class="form-control" name="i_fixRewardGroup"  style="max-width:auto;">'+
                '<label class="input-group-addon">累计奖励组:</label>'+
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="累计奖励组" id="' + totalRewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + totalRewardInput + ',' + totalRewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + totalRewardInput + '" name="i_totalRewardGroup"/></div>' +
                '</span>' +
                // '<input type="text" class="form-control" name="i_totalRewardGroup"  style="max-width:auto;">'+
                '<span class="input-group-btn">'+
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" onclick="delExChangeConfig(this)"><span class="glyphicon glyphicon-minus"></span></button>'+
                '</span>'+
                '</div>'
            obj.insertAdjacentHTML('beforeBegin',html);

            var dayCount = $(obj).parent().parent().find("input[name='dayCount']").val();
            $(obj).parent().parent().find("input[name='dayCount']").val(dayCount*1+1);//每个充值配置中的充值天数计数
        }
        //删除配置
        function delExChangeConfig(obj) {
            var dayCount = $(obj).parent().parent().parent().find("input[name='dayCount']").val();
            $(obj).parent().parent().parent().find("input[name='dayCount']").val(dayCount*1-1);//每个充值配置中的充值天数计数
            $(obj).parent().parent().remove();
        }
    </script>

    <script>
        $(document).on('click','#delGridGroup',function(){
            var el = this.parentNode.parentNode
            var saltIp = $(this).parent().parent().find('#saltIp').val()
            el.parentNode.removeChild(el)

            var dayCount = $(obj).parent().parent().find("input[name='dayCount']").val();
            if(dayCount>0){
                $(obj).parent().parent().find("input[name='dayCount']").val(dayCount*1-1);
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
        <h3>连续累充<span class="glyphicon glyphicon-question-sign" title="${msg['jsp.activity.title15']}"
                      data-container="body" data-toggle="popover" data-placement="right"
                      data-content="${msg['jsp.activity.content15']}"></span></h3>
    </div>

    <form id="activity_form" method="post" class="main form-horizontal" action="#">

        <jsp:include page="./BaseActivity_new.jsp"/>

        <div class="col-lg-12 col-sm-12 col-md-12">
<%--            <div class="col-lg-1"></div>--%>
            <div class="col-lg-12">
                <legend>充值配置1</legend>
                <fieldset>
                    <div class="input-group" id="saltIpGroup1">
                        <label class="input-group-addon">充值金额:</label>
                        <input type="text" class="form-control" name="i_reach" onkeyup="value=value.replace(/\D/g,'')" style="max-width:200px;" placeholder="单位分">
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addSaltIpGrpBtn0" onclick="addSaltIpGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>
                        <input type="hidden" name="dayCount" value="0">
                    </div>
                </fieldset>
            </div>

            <div class="col-lg-12">
                <legend>充值配置2</legend>
                <fieldset>
                    <div class="input-group" id="saltIpGroup2">
                        <label class="input-group-addon">充值金额:</label>
                        <input type="text" class="form-control" id="i_reach" name="i_reach" onkeyup="value=value.replace(/\D/g,'')" style="max-width:200px;" placeholder="单位分">
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addSaltIpGrpBtn1" onclick="addSaltIpGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>
                        <input type="hidden" name="dayCount" value="0">
                    </div>
                </fieldset>
            </div>

            <div class="col-lg-12">
                <legend>充值配置3</legend>
                <fieldset>
                    <div class="input-group" id="saltIpGroup3">
                        <label class="input-group-addon">充值金额:</label>
                        <input type="text" class="form-control" name="i_reach" onkeyup="value=value.replace(/\D/g,'')" style="max-width:200px;" placeholder="单位分">
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addSaltIpGrpBtn2" onclick="addSaltIpGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>
                        <input type="hidden" name="dayCount" value="0">
                    </div>
                </fieldset>
            </div>

            <div class="col-lg-12">
                <legend>充值配置4</legend>
                <fieldset>
                    <div class="input-group" id="saltIpGroup4">
                        <label class="input-group-addon">充值金额:</label>
                        <input type="text" class="form-control" name="i_reach" onkeyup="value=value.replace(/\D/g,'')" style="max-width:200px;" placeholder="单位分">
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addSaltIpGrpBtn3" onclick="addSaltIpGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>
                        <input type="hidden" name="dayCount" value="0">
                    </div>
                </fieldset>
            </div>

            <div class="col-lg-12">
                <legend>充值配置5</legend>
                <fieldset>
                    <div class="input-group" id="saltIpGroup5">
                        <label class="input-group-addon">充值金额:</label>
                        <input type="text" class="form-control" name="i_reach" onkeyup="value=value.replace(/\D/g,'')" style="max-width:200px;" placeholder="单位分">
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addSaltIpGrpBtn4" onclick="addSaltIpGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>
                        <input type="hidden" name="dayCount" value="0">
                    </div>
                </fieldset>
            </div>

            <div class="col-lg-12">
                <legend>充值配置6</legend>
                <fieldset>
                    <div class="input-group" id="saltIpGroup6">
                        <label class="input-group-addon">充值金额:</label>
                        <input type="text" class="form-control" name="i_reach" onkeyup="value=value.replace(/\D/g,'')" style="max-width:200px;" placeholder="单位分">
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="addSaltIpGrpBtn5" onclick="addSaltIpGrp(this)"><span class="glyphicon glyphicon-plus"></span></button>
                        <input type="hidden" name="dayCount" value="0">
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

<div id="publishActivity" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
