<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${msg['jsp.login.changepwd']}</title>
<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
<script type="text/javascript">
    var base = '${base}';
    $(function () {
        $(document).keyup(function (event) {
            if (event.keyCode == 13) {
                $("#update_btn").trigger("click");
            }
        });
    });

    var passwordCheck = true;
    var cpasswordCheck = true;

    //验证输入的密码是否为空，而且密码必须要12位以上，必须包含数字、大小写字母及特殊符号。
    function checkPassword() {
        var numasc = 0;  //数字的个数九零 一起 玩 www.901 75.c om
        var charasc = 0;  //字母的个数
        var otherasc = 0; //特殊符号的个数
        var password = $("input[name='password']").val();
        if (password == "" || password.length == 0) {
            $("#checkPassword").html("${msg['jsp.login.nullpassword']}");
            passwordCheck = false;
        } else if (password.length < 12) {
            $("#checkPassword").html("${msg['jsp.login.minpasswordlength']}");
            passwordCheck = false;
        } else {
            for (var i = 0; i < password.length; i++) {
                var asciiNumber = password.substr(i, 1).charCodeAt();
                if (asciiNumber >= 48 && asciiNumber <= 57) {
                    numasc += 1;
                }
                if ((asciiNumber >= 65 && asciiNumber <= 90) || (asciiNumber >= 97 && asciiNumber <= 122)) {
                    charasc += 1;
                }
                if ((asciiNumber >= 33 && asciiNumber <= 47) || (asciiNumber >= 58 && asciiNumber <= 64) || (asciiNumber >= 91 && asciiNumber <= 96) || (asciiNumber >= 123 && asciiNumber <= 126)) {
                    otherasc += 1;
                }
            }
            if (0 == numasc) {
                $("#checkPassword").html("${msg['jsp.login.passwordnofigure']}");
                passwordCheck = false;
            } else if (0 == charasc) {
                $("#checkPassword").html("${msg['jsp.login.passwordnoletter']}");
                passwordCheck = false;
            } else if (0 == otherasc) {
                $("#checkPassword").html("${msg['jsp.login.passwordnocharacter']}");
                passwordCheck = false;
            } else {
                $("#checkPassword").html("");
                passwordCheck = true;
            }
        }
    }

    function checkCPassword() {
        var password = $("input[name='password']").val();
        var cpassword = $("input[name='cpassword']").val();
        if (cpassword != password) {
            $("#checkCPassword").html("${msg['jsp.login.cpwdnoeqpwd']}");
            cpasswordCheck = false;
        } else {
            $("#checkCPassword").html("");
            cpasswordCheck = true;
        }
    }

    function updatePWD() {
        if (passwordCheck && cpasswordCheck) {
            if ($("#update_form").validationEngine('validate')) {
                $.ajax({
                    url: base + "/user/changepwd",
                    type: "POST",
                    data: $('#update_form').serialize(),
                    dataType: "json",
                    success: function (data) {
                        alert(data.msg);
                        if (data.ok) {
                            top.location.href = base + "/user/logout";
                        }
                    }
                });
            }
        }
    }
</script>
</head>
<body>
<form id="update_form" method="post">
    <table class="table table-bordered span6">
        <tr>
            <td>${msg['jsp.login.user']}</td>
            <td>${USER.name }</td>
        </tr>
        <tr>
            <td>${msg['jsp.login.newpass']}</td>
            <td>
                <input type="password" id="pwd" name="password" class="input validate[required],minSize[0]"/>
                <span style="color:#b94a48" id="checkPassword"></span>
            </td>
        </tr>
        <tr>
            <td>${msg['jsp.login.cpass']}</td>
            <td>
                <input type="password" name="cpassword" class="input validate[required],equals[pwd],minSize[0]"/>
                <span style="color:#b94a48" id="checkCPassword"></span>
            </td>
        </tr>

        <tr>
            <td colspan="2">
                <input type="button" id="update_btn" value="${msg['jsp.login.changepwd']}" class="btn btn-primary" onclick="updatePWD();"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>