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
    var serverId = 0;
    var groupName = "";
    $(function () {
        group_reload();
        loadAllItem();
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

    function showShopModel() {
        $("#updateShopModel").modal('toggle');
        $("#updateShopFrom")[0].reset();
    }

    function showShopList() {
        $.ajax({
            type : "POST",
            url : base + "/shop/list",
            data : {
                serverId : $("#select_server").val(),
                shopId : $("#select_shop").val()
            },
            dataType : "json",
            success : function(data) {
                if (data.ok) {
                    groupName = $("#select_group").val();
                    serverId = $("#select_server").val();
                    createShopListTable(data.data);
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    function createShopListTable(data) {
        var table = $("<table>").attr("class", "table table-bordered");
        var thead = $("<thead>");
        var htr = $("<tr>");
        var fields = [ "${msg['jsp.shoplist.id']}", "${msg['jsp.shoplist.itemID']}",
            "${msg['jsp.shoplist.shopID']}", "${msg['jsp.shoplist.shopType']}",
            "${msg['jsp.shoplist.labelID']}", "${msg['jsp.shoplist.level']}",
            "${msg['jsp.shoplist.occupation']}","${msg['jsp.shoplist.limitType']}",
            "${msg['jsp.shoplist.buyNum']}","${msg['jsp.shoplist.currencyID']}",
            "${msg['jsp.shoplist.price']}","${msg['jsp.shoplist.discountPrice']}",
            "${msg['jsp.shoplist.discount']}","${msg['jsp.shoplist.promotion']}",
            "${msg['jsp.shoplist.sort']}","${msg['jsp.shoplist.upTime']}",
            "${msg['jsp.shoplist.downTime']}","${msg['jsp.shoplist.bind']}",
            "${msg['jsp.shoplist.refreshCurrency']}","${msg['jsp.shoplist.refreshNum']}",
            "${msg['jsp.shoplist.isDiscount']}","${msg['jsp.shoplist.operate']}"];
        for (var field in fields) {
            var th = $("<th>").text(fields[field]);
            htr.append(th);
        }
        thead.append(htr);
        table.append(thead);
        var list_html = "<tbody>";
        for (var i = 0; i < data.length; i++) {
            var shop = data[i];
            var tmp = "<tr><td>" + shop.id + "</td>"
                + "<td>" + getItemName(shop.itemid)  + "</td>"
                + "<td>" + getShopType(shop.shopid) + "</td>" + "<td>" + shop.shoptype + "</td>"
                + "<td>" + shop.labelid + "</td>" + "<td>" + shop.level + "</td>"
                + "<td>" + getCareer(shop.occupation) + "</td>" + "<td>" + getLimit(shop.limittype) + "</td>"
                + "<td>" + shop.buynum + "</td>" + "<td>" + getItemName(shop.currencyid) + "</td>"
                + "<td>" + shop.price + "</td>" + "<td>" + shop.discountprice + "</td>"
                + "<td>" + shop.discount + "</td>" + "<td>" + getPromotion(shop.promotion) + "</td>"
                + "<td>" + shop.sort + "</td>" + "<td>" + shop.uptime + "</td>"
                + "<td>" + shop.downtime + "</td>" + "<td>" + getBoolean(shop.bind) + "</td>"
                + "<td>" + shop.refreshcurrency + "</td>" + "<td>" + shop.refreshnum + "</td>"
                + "<td>" + getBoolean(shop.isdiscount) + "</td>"
                + "<td><input type='button' value='查看详情/修改' class='btn btn-warning' onclick='updateShop(\"" + shop.id + "\")'>"
                + "<input type='button' value='删除' class='btn btn-danger' onclick='deleteShop(\"" + shop.id +"\")'>"
                + "</td></tr>";
            list_html += tmp;
        }
        list_html += "</tbody>";
        table.append(list_html);
        $("#shoplist").html(table);
    }

    function getShopType(shopId) {
        switch (shopId) {
            case 1:
                return "${msg['jsp.shoplist.shopId1']}";
            case 2:
                return "${msg['jsp.shoplist.shopId2']}";
            case 3:
                return "${msg['jsp.shoplist.shopId3']}";
            case 4:
                return "${msg['jsp.shoplist.shopId4']}";
        }
        return "${msg['jsp.shoplist.unknow']}" + shopId;
    }

    function getCareer(career) {
        switch (career) {
            case -1:
                return "${msg['jsp.shoplist.career-1']}";
            case 0:
                return "${msg['jsp.shoplist.career0']}";
            case 1:
                return "${msg['jsp.shoplist.career1']}";
            case 2:
                return "${msg['jsp.shoplist.career2']}";
            case 3:
                return "${msg['jsp.shoplist.career3']}";
        }
        return "${msg['jsp.shoplist.unknow']}" + career;
    }

    function getItemName(itemId) {
        var itemName = items.get(itemId + "");
        if (itemName == undefined) {
            itemName = "${msg['jsp.shoplist.unknow']}";
        }
        return itemName + "[" + itemId + "]";
    }

    function getLimit(limit) {
        switch (limit) {
            case 0:
                return "${msg['jsp.shoplist.limit0']}";
            case 1:
                return "${msg['jsp.shoplist.limit1']}";
            case 2:
                return "${msg['jsp.shoplist.limit2']}";
            case 3:
                return "${msg['jsp.shoplist.limit3']}";
            case 4:
                return "${msg['jsp.shoplist.limit4']}";
            case 5:
                return "${msg['jsp.shoplist.limit5']}";
        }
        return "${msg['jsp.shoplist.unknow']}" + limit;
    }

    function getPromotion(promotion) {
        switch (promotion) {
            case 0:
                return "${msg['jsp.shoplist.promotion0']}";
            case 1:
                return "${msg['jsp.shoplist.promotion1']}";
            case 2:
                return "${msg['jsp.shoplist.promotion2']}";
            case 3:
                return "${msg['jsp.shoplist.promotion3']}";
            case 4:
                return "${msg['jsp.shoplist.promotion4']}";
        }
        return "${msg['jsp.shoplist.unknow']}" + promotion;
    }

    function getBoolean(num) {
        if (num == 0) {
            return "${msg['jsp.shoplist.no']}";
        } else {
            return "${msg['jsp.shoplist.yes']}";
        }
    }

    function addShop() {
        window.location.href = base + "/shop/shopForm?id=0";
    }

    function updateShop(id) {
        window.location.href = base + "/shop/shopForm?id=" + id +"&groupName=" + groupName + "&serverId=" + serverId;
    }

    function deleteShop(id) {
        var flag = confirm("是否删除？");
        if (!flag) {
            return;
        }
        $.ajax({
            type : "POST",
            url : base + "/shop/deleteShop",
            data : {
                serverId : $("#select_server").val(),
                shopId : id
            },
            dataType : "json",
            success : function(data) {
                alert(data.msg);
                if (data.ok) {
                    showShopList();
                }
            }
        });
    }

    function refreshShop() {
        $.post(base + "/shop/refreshShop", {serverId: $("#select_server").val()}, function(data) {
            alert(data.msg);
        });
    }

</script>
</head>
<body>
<div class="container-fluid">
    <form action="#" id="query_form" class="well form-inline">
        <label class="label">${msg['jsp.shoplist.plat']}</label>
        <select id="select_group" onchange="queryServerByGroup(this.value)" class="span2"></select>
        <label class="label">${msg['jsp.shoplist.server']}</label>
        <select id="select_server" name="serverId" class="span2"></select>
        <label class="label">${msg['jsp.shoplist.chooseShopType']}</label>
        <select id="select_shop" name="shopId" class="span2">
            <option value="0">${msg['jsp.shoplist.shopId0']}</option>
            <option value="1">${msg['jsp.shoplist.shopId1']}</option>
            <option value="2">${msg['jsp.shoplist.shopId2']}</option>
            <option value="3">${msg['jsp.shoplist.shopId3']}</option>
            <option value="4">${msg['jsp.shoplist.shopId4']}</option>
        </select>
        <input type="button" class="btn btn-info" value="查看" onclick="showShopList()"/>

        <br/><br/>
        <input type="button" class="btn btn-primary" value="新增商品" onclick="addShop()"/>
    </form>
</div>
<div class="container-fluid">
    <div id="shoplist"></div>
</div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>