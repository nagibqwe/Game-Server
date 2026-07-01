<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>${msg['jsp.log.acodetitle']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
<link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
<script type="text/javascript" src="${base}/js/global.js"></script>
<script>
    var base = '${base}';
    var items = new Map();
    $(function () {
        getGroup();
        loadAllItem();
        $("#codeDiv").hide();
        $(".datetimepicker").datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm-dd hh:00:00',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            startView: 2,
            minView: 1
        });
    });

    function insert() {
        if ($("#activeCodeForm").validationEngine('validate')) {
            var sDate = $("#valide_time_begin").val();
            var eDate = $("#valide_time_begin").val();
            if (!checkDate(sDate, eDate)) {
                return;
            }
            var items = $("#reward").val();
            if (items == undefined || items == "") {
                alert("未选择物品！");
                return;
            }
            $("#activeCodeForm").submit();
            $("#activeCodeForm")[0].reset();
        }
    }

    function changeParam(type) {
        if (type == 1) {
            $("#code").val("");
            $("#codeDiv").show();
            $("#tips").html("");
            $("#codeNum").attr("readonly", "readonly").val(1);
        } else {
            $("#code").val("");
            $("#codeDiv").hide();
            $("#tips").html("");
            $("#codeNum").attr("readonly", false).val("");
        }
    }

    function showAddItemModel() {
        $("#addItemModal").modal('toggle');
        $("#addItemForm")[0].reset();
    }

    function addItem() {
        if (!$('#addItemForm').validationEngine('validate')) {
            alert("物品配置错误!")
            return;
        }
        var itemId = $("#itemId").val();
        var itemName = items.get(itemId);
        var itemNum = $("#itemNum").val();
        var isBind = $("#isBind").val();
        var itemValue = itemId + "," + itemNum + "," + isBind;
        console.log(itemValue + ":" + itemName);
        var lastValue = $("#reward").val();
        var itemArray = lastValue.split(';');
        if (lastValue == "") {
            itemArray = new Array();
        }
        itemArray.push(itemValue);
        $("#reward").val(itemArray.join(";"));
        console.log(itemArray.join(";"));
        $("#items").append("<span class=\"label\" style=\"float: left\">" + itemName + "*" + itemNum +
            "<i class=\"icon-remove item-remove\" onclick=\"removeItem(this, " + itemId + ", " + itemNum + ")\"></i></span>");
        $("#addItemModal").modal('hide');
    }

    function removeItem(obj, itemId, itemNum) {
        var pos;
        var value = $(obj).parent().parent().prev(".rewardInput").val();
        var itemArray = value.split(';');
        $.each(itemArray, function (i, n) {
            var item = n.split(",");
            if (item[0] == itemId && item[1] == itemNum) {
                pos = i;
            }
        });
        itemArray.splice(pos, 1);
        $(obj).parent().parent().prev(".rewardInput").val(itemArray.join(";"));
        console.log(itemArray.join(";"));
        $(obj).parent().remove();
        event.stopPropagation();
    }

    function checkCode() {
        var code = $("#code").val();
        if (code == "") {
            alert("礼包码不能为空！")
            return;
        }
        $.ajax({
            type: "POST",
            url: base + "/operation/checkActiveCode",
            data: {
                "code": code
            },
            dataType: "json",
            success: function (data) {
                if (data.ok) {
                    $("#tips").html("<span style='color:green'>激活码可用</span>")
                } else {
                    $("#tips").html("<span style='color:red'>激活码重复</span>")
                }
            }
        });
    }

</script>
</head>
<body>
<div class="container-fluid">
    <form id="activeCodeForm" action="${base}/operation/activecode" method="post" class="form-horizontal">
        <fieldset>
            <div id="legend">
                <legend>${msg['jsp.log.acodetitle']}</legend>
            </div>

            <div class="control-group">
                <%--<label class="control-label">${msg['jsp.activecode.universalcode']}</label>--%>
                <div class="controls">
                    <label class="radio">
                        <input checked="checked" value="0" name="param" type="radio" onchange="changeParam(0);"/>
                        普通码(一批可生成多个,同一批激活码1个角色只能使用其中一个,获得1次奖励)
                    </label>
                    <label class="radio">
                        <input value="1" name="param" type="radio" onchange="changeParam(1);"/>
                        万能码(一批生成一个,不同角色均可使用此激活码)
                    </label>
                    <label class="radio">
                        <input value="2" name="param" type="radio" onchange="changeParam(2);"/>
                        多次码(一批可生成多个,同一批激活码1个角色可以使用多个,获得多次奖励)
                    </label>
                </div>
            </div>

            <div id="codeDiv" class="control-group">
                <label for="code" class="control-label">${msg['jsp.activecode.code']}</label>
                <div class="form-group controls">
                    <input id="code" name="code" type="text" class="form-control input validate[required]" onblur="checkCode()"/>
                    <div id="tips" class="help-inline"></div>
                </div>
            </div>

            <div class="control-group">
                <label for="codeNum" class="control-label">${msg['jsp.activecode.num']}</label>
                <div class="form-group controls">
                    <input id="codeNum" name="codeNum" type="text" class="form-control input validate[required,custom[integer],min[1]]"/>
                </div>
            </div>

            <div class="control-group">
                <label for="activeName" class="control-label">${msg['jsp.activecode.name']}</label>
                <div class="controls">
                    <input id="activeName" name="activeName" type="text" class="validate[required]">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label">${msg['activity.login.fujianItem']}</label>
                <div class="controls">
                    <div class="span6" style="border: 1px solid rgb(204, 204, 204); border-radius: 2px; height: 60px; margin-left: 0" onclick="showAddItemModel()">
                        <input type="hidden" id="reward" name="itemList" class="rewardInput validate[required]"/>
                        <div id="items" class="items" style="margin-top: 3px; display: block"></div>
                    </div>
                </div>
            </div>

            <div class="control-group">
                <label for="valide_time_begin" class="control-label">${msg['jsp.activecode.starttime']}</label>
                <div class="controls">
                    <input id="valide_time_begin" name="valide_time_begin" type="text" class="datetimepicker input validate[required]" readonly/>
                </div>
            </div>

            <div class="control-group">
                <label for="valide_time_end" class="control-label">${msg['jsp.activecode.endtime']}</label>
                <div class="controls">
                    <input id="valide_time_end" name="valide_time_end" type="text" class="datetimepicker input validate[required]" readonly/>
                </div>
            </div>

            <div class="control-group">
                <label for="select_group" class="control-label">${msg['jsp.activecode.platform']}</label>
                <div class="controls">
                    <select id="select_group" name="groupName" class="input validate[required]"
                            onchange="getChannelNameSelect(this.value); getServer(this.value)"></select>
                </div>
            </div>

            <div class="control-group">
                <label for="select_channel" class="control-label">${msg['jsp.activecode.channel']}</label>
                <div class="controls">
                    <select id="select_channel" name="plateform_name_big">
                        <option value="">所有渠道</option>
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label for="checkbox_server" class="control-label">${msg['jsp.activecode.server']}</label>
                <div class="controls">
                    <div id="checkbox_server" class="well span6" style="margin-left: 0"></div>
                </div>
            </div>
            <div style="margin-top: -12px;margin-bottom: 10px">
                <span style="color: red;margin-left: 180px;">（若不勾选任何选项，则默认所有区服，包括后续新加入的区服）</span>
            </div>
            <div class="control-group">
                <div class="controls">
                    <input type="button" value="${msg['jsp.activecode.generate']}" class="btn btn-primary" onclick="insert()"/>
                </div>
            </div>
        </fieldset>
    </form>
</div>
<jsp:include page="../commonmodal.jsp"/>

<div id="addItemModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>添加物品</h3>
    </div>
    <div class="modal-body">
        <form id="addItemForm" class="row-fluid">
            <div class="offset1 span10">
                <input id="curObj" type="hidden"/>
                <label for="itemId">物品ID</label><input type="text" id="itemId" list="itemList" class="span8 validate[required,custom[integer],min[1]]"/>
                <datalist id="itemList"></datalist>
                <label for="itemNum">物品数量</label><input id="itemNum" type="text" class="span8 validate[required,custom[integer],min[1]]"/>
                <label for="isBind">是否绑定</label>
                <select id="isBind" class="span8 validate[required,custom[integer],min[0],max[1]]">
                    <option value="0" selected>非绑</option>
                    <option value="1">绑定</option>
                </select>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
        <input type="button" value="添加" onclick="addItem()" class="btn btn-primary"/>
    </div>
</div>

</body>
</html>