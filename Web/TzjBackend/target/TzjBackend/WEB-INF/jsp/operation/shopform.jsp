<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>商城配置</title>
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

    var id = '${id}';
    var shopInfo = ${shopInfo};
    var groupName = '${groupName}';
    var serverId = '${serverId}';
    console.log(shopInfo);
    console.log(groupName);
    console.log(serverId);
    $(function () {
        group_reload();
        loadAllItem();
        if (id != '0' && shopInfo != "null") {
            $("#id").val(shopInfo.id);
            $("#id").prop("readonly", true);
            $("#type").val(1);
            loadShopInfo(shopInfo);
            $("#update").val("修改");
            $("#update").addClass("btn-warning");
            $("#update").removeClass("btn-primary");
        }
        setTimeout(function () {
            if (groupName != "" && serverId != '0') {
                $("#select_group").val(groupName);
                $("#select_server").val(serverId);
            }
        }, 100);

        $(".date").datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm-dd hh:ii:00',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 0,
            showMeridian: 1,
            minuteStep: 5
        });
    });

    function loadShopInfo(info) {
        $("input[name='itemid']").val(info.itemid);
        $("select[name='shopid']").val(info.shopid);
        $("input[name='labelid']").val(info.labelid);
        $("input[name='level']").val(info.level);
        $("input[name='guildlevel']").val(info.guildlevel);
        $("input[name='guildshoplvlstart']").val(info.guildshoplvlstart);
        $("input[name='guildshoplvlend']").val(info.guildshoplvlend);
        $("input[name='worldlvlstart']").val(info.worldlvlstart);
        $("input[name='worldlvlend']").val(info.worldlvlend);
        $("select[name='isdiscount']").val(info.isdiscount);
        $("input[name='viplevel']").val(info.viplevel);
        $("select[name='occupation']").val(info.occupation);
        $("select[name='limittype']").val(info.limittype);
        $("input[name='buynum']").val(info.buynum);
        $("select[name='currencyid']").val(info.currencyid);
        $("input[name='price']").val(info.price);
        $("input[name='discountprice']").val(info.discountprice);
        $("input[name='discount']").val(info.discount);
        $("select[name='promotion']").val(info.promotion);
        $("input[name='sort']").val(info.sort);
        $("input[name='uptime']").val(info.uptime);
        $("input[name='downtime']").val(info.downtime);
        $("select[name='bind']").val(info.bind);
        $("select[name='refreshcurrency']").val(info.refreshcurrency);
        $("input[name='refreshnum']").val(info.refreshnum);
        $("input[name='shoptype']").val(info.shoptype);
    }

    function updateShopForm() {
        if (!$("#shop_form").validationEngine('validate')) {
            return;
        }
        $.ajax({
            type : "POST",
            url: base + "/shop/updateShop",
            data: $("#shop_form").serialize(),
            success: function(data) {
                alert(data.msg);
                if (!data.ok) {
                    return
                }
                toShopList();
            }
        });
    }

    function toShopList() {
        window.location.href = base + "/shop?menuId=208";
    }
</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="shop_form" class="well form-inline">
        <div class="row">
            <div class="span12">
                <label class="label label-info" for="select_server">${msg['jsp.shoplist.server']}</label>
                <select id="select_group" onchange="queryServerByGroup(this.value)" class="span2"></select>
                <select id="select_server" name="serverId" class="span2"></select>
            </div>
        </div>
        <br/><hr/>

        <!--0新增/1更新-->
        <input type="hidden" id="type" name="type" value="0"/>

        <!--商品ID、类型-->
        <div class="row">
            <div class="span6">
                <label class="label label-info" for="id">${msg['jsp.shoplist.id']}</label>
                <input type="text" id="id" name="id" class="validate[required,custom[integer],min[100000],max[110000]] span2" placeholder="唯一id,100000~110000"/>
            </div>
            <div class="span6">
                <label class="label label-info" for="select_shop">${msg['jsp.shoplist.chooseShopType']}</label>
                <select id="select_shop" name="shopid" class="span2" style="height: 26px; padding: 0">
                    <option value="1">${msg['jsp.shoplist.shopId1']}</option>
                    <option value="2">${msg['jsp.shoplist.shopId2']}</option>
                    <option value="3">${msg['jsp.shoplist.shopId3']}</option>
                    <option value="4">${msg['jsp.shoplist.shopId4']}</option>
                </select>
            </div>
        </div>
        <br/>

        <!--商城标签，促销排序-->
        <div class="row">
            <div class="span3">
                <label class="label label-info" for="shoptype">${msg['jsp.shoplist.shopType']}</label>
                <input type="text" id="shoptype" name="shoptype" class="span2 validate[required]" placeholder="标签数组,如:1_2_3"/>
            </div>
            <div class="span3">
                <label class="label label-info" for="labelid">${msg['jsp.shoplist.labelID']}</label>
                <input type="text" id="labelid" name="labelid" class="span2 validate[required]"/>
            </div>
            <div class="span3">
                <label class="label label-info" for="promotion">${msg['jsp.shoplist.promotion']}</label>
                <select id="promotion" name="promotion" class="span2">
                    <option value="0" selected>${msg['jsp.shoplist.promotion0']}</option>
                    <option value="1">${msg['jsp.shoplist.promotion1']}</option>
                    <option value="2">${msg['jsp.shoplist.promotion2']}</option>
                    <option value="3">${msg['jsp.shoplist.promotion3']}</option>
                    <option value="4">${msg['jsp.shoplist.promotion4']}</option>
                </select>
            </div>
            <div class="span3">
                <label class="label label-info" for="sort">${msg['jsp.shoplist.sort']}</label>
                <input type="text" id="sort" name="sort" class="span2 validate[required]"/>
            </div>
        </div>
        <hr/>

        <!--商品-->
        <div class="row">
            <br/>
            <div class="span3">
                <label class="label label-info">${msg['jsp.shoplist.itemID']}</label>
                <input type="text" name="itemid" list="itemList" class="span2 validate[required]"/>
                <datalist id="itemList"></datalist>
            </div>
            <div class="span3">
                <label class="label label-info" for="bind">${msg['jsp.shoplist.bind']}</label>
                <select id="bind" name="bind" class="span2" style="height: 26px; padding: 0">
                    <option value="0" selected>${msg['jsp.shoplist.nobind']}</option>
                    <option value="1">${msg['jsp.shoplist.isbind']}</option>
                </select>
            </div>
            <div class="span3">
                <label class="label label-info">${msg['jsp.shoplist.limitType']}</label>
                <select name="limittype" class="span2" style="height: 26px; padding: 0">
                    <option value="0">${msg['jsp.shoplist.limit0']}</option>
                    <option value="1">${msg['jsp.shoplist.limit1']}</option>
                    <option value="2">${msg['jsp.shoplist.limit2']}</option>
                    <option value="3">${msg['jsp.shoplist.limit3']}</option>
                    <option value="4">${msg['jsp.shoplist.limit4']}</option>
                    <option value="5">${msg['jsp.shoplist.limit5']}</option>
                </select>
            </div>
            <div class="span3">
                <label class="label label-info">${msg['jsp.shoplist.buyNum']}</label>
                <input type="text" name="buynum" class="span2 validate[required]" value="-1" placeholder="-1表示不限次"/>
            </div>
        </div>
        <br/>

        <!--价格打折-->
        <div class="row">
            <div class="span3">
                <label class="label label-info" for="currencyid">${msg['jsp.shoplist.currencyID']}</label>
                <select id="currencyid" name="currencyid" class="span2" style="height: 26px; padding: 0">
                    <option value="1">金元宝</option>
                    <option value="9">声望</option>
                    <option value="11">仙盟贡献</option>
                    <option value="13">福地积分</option>
                </select>
            </div>
            <div class="span3">
                <label class="label label-info" for="price">${msg['jsp.shoplist.price']}</label>
                <input type="text" id="price" name="price" class="span2 validate[required]"/>
            </div>
            <div class="span3">
                <label class="label label-info" for="discount">${msg['jsp.shoplist.discount']}</label>
                <input type="text" id="discount" name="discount" class="span2 validate[required]"/>
            </div>
            <div class="span3">
                <label class="label label-info" for="discountprice">${msg['jsp.shoplist.discountPrice']}</label>
                <input type="text" id="discountprice" name="discountprice" class="span2 validate[required]"/>
            </div>
        </div>
        <br/><br/>

        <!--上架下架时间，持续过期时间-->
        <div class="row">
            <div class="span6">
                <label class="label label-info">${msg['jsp.shoplist.upTime']}</label>
                <div class="input-append date" id="startdate">
                    <input type="text" class="span2" name="uptime" value="${nowDate}" readonly/>
                    <span class="add-on"><i class="icon-th"></i></span>
                </div>
                -
                <div class="input-append date" id="enddate">
                    <input type="text" class="span2" name="downtime" value="${nowDate}" readonly/>
                    <span class="add-on"><i class="icon-th"></i></span>
                </div>
            </div>
        </div>
        <br/><hr/>

        <!--限制条件-->
        <div class="row">
            <div class="span6">
                <label class="label label-info">${msg['jsp.shoplist.occupation']}</label>
                <select name="occupation" class="span2" style="height: 26px; padding: 0">
                    <option value="-1">${msg['jsp.shoplist.career-1']}</option>
                    <option value="0">${msg['jsp.shoplist.career0']}</option>
                    <option value="1">${msg['jsp.shoplist.career1']}</option>
                    <option value="2">${msg['jsp.shoplist.career2']}</option>
                    <option value="3">${msg['jsp.shoplist.career3']}</option>
                </select>
            </div>
            <div class="span6">
                <label class="label label-info">${msg['jsp.shoplist.level']}</label>
                <input type="text" name="level" class="span2 validate[required]" value="0"/>
            </div>
        </div>
        <br/>

        <div class="row">
            <div class="span6">
                <label class="label label-info">${msg['jsp.shoplist.vipLevel']}</label>
                <input type="text" name="viplevel" class="span2 validate[required]" value="0"/>
            </div>
            <div class="span6">
                <label class="label label-info">${msg['jsp.shoplist.guildLevel']}</label>
                <input type="text" name="guildlevel" class="span2 validate[required]" value="0"/>
            </div>
        </div>
        <br/>

        <!--购买需求仙盟等级,世界等级-->
        <div class="row">
            <div class="span6">
                <label class="label label-info">${msg['jsp.shoplist.guildShopLvl']}</label>
                <input type="text" name="guildshoplvlstart" class="span1 validate[required]" value="0"/> -
                <input type="text" name="guildshoplvlend" class="span1 validate[required]" value="999"/>
            </div>
            <div class="span6">
                <label class="label label-info">${msg['jsp.shoplist.worldLvl']}</label>
                <input type="text" name="worldlvlstart" class="span1 validate[required]" value="0"/> -
                <input type="text" name="worldlvlend" class="span1 validate[required]" value="999"/>
            </div>
        </div>
        <br/>

        <!--刷新消耗货币-->
        <div class="row">
            <div class="span6">
                <label class="label label-info">${msg['jsp.shoplist.refreshCurrency']}</label>
                <select name="refreshcurrency" class="span2" style="height: 26px; padding: 0">
                    <option value="1">金元宝</option>
                    <option value="9">声望</option>
                    <option value="11">仙盟贡献</option>
                    <option value="13">福地积分</option>
                </select>
            </div>
            <div class="span6">
                <label class="label label-info">${msg['jsp.shoplist.refreshNum']}</label>
                <input type="text" name="refreshnum" class="span2 validate[required]" value="0"/>
            </div>
        </div>
        <br/>

        <div class="row">
            <div class="span3">
                <label class="label label-info" for="isdiscount">${msg['jsp.shoplist.isDiscount']}</label>
                <select id="isdiscount" name="isdiscount" class="span1" style="height: 26px; padding: 0">
                    <option value="0">${msg['jsp.shoplist.no']}</option>
                    <option value="1">${msg['jsp.shoplist.yes']}</option>
                </select>
            </div>
        </div>
        <hr/>

        <input type="button" id="update" class="btn btn-primary" value="提交" onclick="updateShopForm()"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" class="btn" value="返回" onclick="toShopList()"/>
    </form>
</div>
</body>
</html>
