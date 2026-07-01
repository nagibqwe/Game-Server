<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>活跃兑换</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${base}/css/activity.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/common.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
    <link rel="stylesheet" href="${base}/css/alertify.css">
    <link rel="stylesheet" href="${base}/css/alertify.default.css">
    <%--<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap3.css">--%>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <%--<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">--%>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/alertify.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<%--    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.13.1/xlsx.full.min.js"></script>--%>
    <%--<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-select.min.css">--%>
    <%--<script src="${base}/css/bootstrap/js/bootstrap-select.min.js"></script>--%>
    <%--<script src="${base}/css/bootstrap/js/bootstrap-select.zh_CN.min.js"></script>--%>
    <%--<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>--%>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap3.js"></script>
    <script type="text/javascript"
            src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript"
            src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${base}/js/activity.js"></script>
    <script type="text/javascript" src="${base}/js/global.js"></script>
    <script type="text/javascript" src="${base}/js/dateFormat.js"></script>
    <link rel="stylesheet" href="${base}/css/fileCss.css">
    <script type="text/javascript" src="${base}/js/excel.js"></script>

    <script type="text/javascript">
        var actType = '${type}';
        var base = '${base}';

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
        function addSaltIpGrp(obj) {
            // console.log(obj);
            var html = '<div name="cfg1" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
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
        function jiangliGrp(obj) {
            var count = $("#cfgCount2").val();
            var rewardInput = 'reward' + count
            var rewardAddBtn = 'showAddItemModel' + count
            $("#cfgCount2").val(Number(count)+1);
            var html = '<div name="cfg2" class="input-group saltIp" style="width:100%;padding:0 0 1px 0;">' +
                '<label class="input-group-addon">活动活跃:</label>' +
                '<input type="text" class="form-control"  name="reach" onkeyup="value=value.replace(/\\D/g,\'\')" style="">' +
                '<label class="input-group-addon">展示奖励项:</label>' +
                '<input type="text" class="form-control"  name="showReward" onkeyup="value=value.replace(/\\D/g,\'\')" style=";">' +
                '<label class="input-group-addon">奖励列表:</label>' +
                '<span class="input-group-btn" >' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" data-target="#addItemModal" title="添加奖励" id="' + rewardAddBtn +
                '"  style="border:5px;width: 35px;height: 34px;" onclick="showAddItemUI(' + rewardInput + ',' + rewardAddBtn + ')">' +
                '<span class="glyphicon glyphicon-plus"></span></button>' +
                '<div class="hide"><input id="' + rewardInput + '" name="reward"/></div>' +
                '</span>' +
                '<span class="input-group-btn">' +
                '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" id="delGridGroup"><span class="glyphicon glyphicon-minus"></span></button>' +
                '</span>' +
                '</div>'

            obj.insertAdjacentHTML('beforeBegin', html)
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
        <h3>活跃兑换<span class="glyphicon glyphicon-question-sign" title="${msg['jsp.activity.title1']}"
                      data-container="body" data-toggle="popover" data-placement="right"
                      data-content="${msg['jsp.activity.content1']}"></span></h3>
    </div>

    <form id="activity_form" method="post" class="main form-horizontal" action="#">

        <jsp:include page="./BaseActivity_new.jsp"/>

        <div id="customForm">
        <div class="col-lg-12 col-sm-12 col-md-12">
            <div class="col-lg-1"></div>
            <div class="col-lg-6">
                <legend>活动货币兑换</legend>
                <fieldset>
                    <div class="input-group" id="saltIpGroup">
                        <label class="input-group-addon">货币兑换配置:</label>
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
                <legend>奖励数据兑换</legend>
                <fieldset>
                    <div class="input-group" id="jianglishuju">
                        <label class="input-group-addon">奖励数据配置:</label>
                        <button class="btn btn-info" type="button" data-toggle="tooltip" title="新增" id="jiangliBtn"
                                onclick="jiangliGrp(this)"><span class="glyphicon glyphicon-plus"></span>
                        </button>
                        <input type="hidden" id="cfgCount2" value="0">
                    </div>
                </fieldset>
            </div>
        </div>
        </div>
    </form>
</div>
<jsp:include page="./import.jsp"/>

<div class="container-fluid">
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
