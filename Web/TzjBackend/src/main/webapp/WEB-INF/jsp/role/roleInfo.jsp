<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
    /**
     * 加载角色信息
     */
    function role_reload() {
        if (!$("#role_query_form").validationEngine('validate')) {
            return;
        }
        if (($("#select_queryType").val() == "3")) {
            if (isNaN($("#queryString").val())) {
                alert("${msg['jsp.role.pleasetypenum']}");
                return;
            }
        }
        $("#loadingmodal").modal({
            backdrop: 'static',
            keyboard: false
        });
        $.ajax({
            type: "POST",
            url: base + "/role/query",
            data: $("#role_query_form").serialize(),
            dataType: "json",
            success: function (data) {
                $("#msg").empty();
                $("#role_list").empty();
                $("#account_list").empty();

                $("#loadingmodal").modal('hide');
                if (!data.ok) {
                    $("#msg").html(data.msg);
                    return;
                }
                if (!data.hasOwnProperty("roles")) {
                    $("#role_list").html(data.msg);
                    createAccountTable(data.accounts);
                } else if (!data.hasOwnProperty("accounts")) {
                    $("#account_list").html(data.msg);
                    createRoleTable(data.roles);
                } else {
                    createAccountTable(data.accounts);
                    createRoleTable(data.roles);
                }
            }
        });
    }

    /**
     * 角色信息表格
     */
    function createRoleTable(roles) {
        var table = $("<table>").attr("class", "table table-bordered");
        var caption = $("<caption>").text("${msg['jsp.role.roleinfo']}");
        table.append(caption);
        var thead = $("<thead>");
        var htr = $("<tr>");
        var fields = ["${msg['jsp.role.userid']}", "${msg['jsp.role.roleid']}",
            "${msg['jsp.role.rolename']}", "${msg['jsp.role.level']}",
            "${msg['jsp.role.sex']}", "${msg['jsp.role.career']}",
            "${msg['jsp.role.money']}", "${msg['jsp.role.gold']}",
            "${msg['jsp.role.server']}",
            "${msg['jsp.role.rechargegold']}", "${msg['jsp.role.ct']}",
            "${msg['jsp.role.ip']}", "${msg['jsp.role.isdelete']}",
            "${msg['jsp.role.fcaccount']}", "${msg['jsp.role.channel']}",
            "${msg['jsp.role.machineCode']}"];
        for (var field in fields) {
            var th = $("<th>").text(fields[field]);
            htr.append(th);
        }
        thead.append(htr);
        table.append(thead);

        var list_html = "<tbody>";
        for (var i = 0; i < roles.length; i++) {
            var role = roles[i];
            console.log(role);
            var tmp = "<tr><td>" + role.userId + "</td><td>" + role.roleId + "</td><td>" + role.roleName
                + "</td><td>" + role.level + "</td><td>" + getSex(role.sex)
                + "</td><td>" + getCareer(role.career) + "</td><td>"
                + role.money + "</td><td>" + role.gold + "</td><td>"
                + role.createsid + "</td><td>"
                + role.rechargeGold + "</td><td>" + role.createTime
                + "</td><td>" + role.ip + "</td><td>"
                + isDeleted(role.isDelete) + "</td><td>" + role.funcellUUid
                + "</td><td>" + role.platformName
                + "</td><td>" + role.machineCode + "</tr>";
            list_html += tmp;
        }
        list_html += "</tbody>";
        table.append(list_html);
        $("#role_list").html(table);
    }

    /**
     * 账号信息表格
     */
    function createAccountTable(accounts) {
        var table = $("<table>").attr("class", "table table-bordered");
        var caption = $("<caption>").text("${msg['jsp.role.acinfo']}");
        table.append(caption);
        var thead = $("<thead>");
        var htr = $("<tr>");
        var fields = ["${msg['jsp.role.acname']}", "${msg['jsp.role.acid']}",
            "${msg['jsp.role.acpfaccount']}", "${msg['jsp.role.acplatform']}",
            "${msg['jsp.role.acct']}", "${msg['jsp.role.aclt']}",
            "${msg['jsp.role.acip']}", "${msg['jsp.role.mac']}",
            "${msg['jsp.role.imei']}", "${msg['jsp.role.machineCode']}"];
        for (var field in fields) {
            var th = $("<th>").text(fields[field]);
            htr.append(th);
        }
        thead.append(htr);
        table.append(thead);

        var list_html = "<tbody>";
        for (var i = 0; i < accounts.length; i++) {
            var account = accounts[i];
            var tmp = "<tr><td>" + account.userName + "</td><td>"
                + account.userid + "</td><td>"
                + account.platformAccount + "</td><td>"
                + account.platformName + "</td><td>"
                + TimeObjectUtil.UnixToDate(account.createTime) + "</td><td>"
                + TimeObjectUtil.UnixToDate(account.time) + "</td><td>"
                + account.lastLoginIp + "</td><td>"
                + account.mac + "</td><td>"
                + account.imei + "</td><td>"
                + account.machineCode + "</td></tr>";
            list_html += tmp;
        }
        list_html += "</tbody>";
        table.append(list_html);
        $("#account_list").html(table);
    }

    function isDeleted(value) {
        return value == 0 ? "${msg['jsp.role.noout']}" :
            "${msg['jsp.role.logouttime']}：" + TimeObjectUtil.UnixToDate(value);
    }

    function getSex(sex) {
        return sex == 1 ? "男" : "女";
    }

    function getCareer(career) {
        switch (career) {
            case 0:
                return "${msg['jsp.career.career0']}";
            case 1:
                return "${msg['jsp.career.career1']}";
            case 2:
                return "${msg['jsp.career.career2']}";
            case 3:
                return "${msg['jsp.career.career3']}";
            default:
                return "${msg['jsp.career.unknown']}[" + career + "]";
        }
    }
</script>
