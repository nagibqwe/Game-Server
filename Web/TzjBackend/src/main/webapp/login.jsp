<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>${msg['jsp.main.title']}</title>
	<link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
	<script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
	<!-- 引入封装了failback的接口--initGeetest -->
	<%--<script src="http://static.geetest.com/static/tools/gt.js"></script>--%>
	<script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
	<script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
	<script type="text/javascript">
		var me = '<%=session.getAttribute("USER")%>';
		var base = '${base}';

		$(function() {
			if (me != "null") {
				location.href = base + "/home/";
			}
			$(document).keyup(function(event){
				if(event.keyCode ==13){
					$("#login_button").trigger("click");
				}
			});
			//正常登录
			$("#login_button").click(function () {
				login();
			});

			//SDK登录
			//initValidate();
		});
		var userNameCheck=false;
		var passwordCheck=false;
		var checkCodeCheck=false;
		//验证输入的用户名是否为空
		function checkUserName(){
			var username = $("input[name='username']").val();
			if(username == ""){
				$("#checkUserName").html("${msg['jsp.login.nullusername']}");
				userNameCheck=false;
			}else{
				$("#checkUserName").html("");
				userNameCheck=true;
			}
		}
		//验证输入的密码是否为空，而且密码必须要12位以上，必须包含数字、大小写字母及特殊符号。
		function checkPassword(){
			var numasc = 0;  //数字的个数
			var charasc = 0;  //字母的个数
			var otherasc = 0; //特殊符号的个数
			var password = $("input[name='password']").val();
			if(password == "" || password.length == 0){
				$("#checkPassword").html("${msg['jsp.login.nullpassword']}");
				passwordCheck=false;
			}else{
				for (var i = 0; i < password.length; i++) {
					var asciiNumber = password.substr(i, 1).charCodeAt();
					if (asciiNumber >= 48 && asciiNumber <= 57) {
						numasc += 1;
					}
					if ((asciiNumber >= 65 && asciiNumber <= 90)||(asciiNumber >= 97 && asciiNumber <= 122)) {
						charasc += 1;
					}
					if ((asciiNumber >= 33 && asciiNumber <= 47)||(asciiNumber >= 58 && asciiNumber <= 64)||(asciiNumber >= 91 && asciiNumber <= 96)||(asciiNumber >= 123 && asciiNumber <= 126)) {
						otherasc += 1;
					}
				}
				if(0==numasc)  {
					$("#checkPassword").html("${msg['jsp.login.passwordnofigure']}");
					passwordCheck=false;
				}else if(0==charasc){
					$("#checkPassword").html("${msg['jsp.login.passwordnoletter']}");
					passwordCheck=false;
				}else if(0==otherasc){
					$("#checkPassword").html("${msg['jsp.login.passwordnocharacter']}");
					passwordCheck=false;
				}else{
					$("#checkPassword").html("");
					passwordCheck=true;
				}
			}
		}


		//验证验证码是否为空
		function checkCheckCode(){
			var checkCodeStr = $("input[name='checkCode']").val();
			//console.log("===="+checkCodeStr);
			if(checkCodeStr == "" || checkCodeStr.length == 0){
				$("#checkCheckCode").html("${msg['jsp.login.nullcheckcode']}");
				checkCodeCheck=false;
			}else{
				$.ajax({
					url : base + "/picture/getCheckCode",
					type : "POST",
					dataType : "json",
					success : function(data) {
						if(checkCodeStr.toLowerCase() == data.toLowerCase()){
							$("#checkCheckCode").html("");
							checkCodeCheck=true;
						}else{
							$("#checkCheckCode").html("${msg['jsp.login.checkcodewrong']}");
							checkCodeCheck=false;
						}
					}
				});
			}
		}

		/* function initValidate(){
            var handlerPopup = function (captchaObj) {
                // 成功的回调
                captchaObj.onSuccess(function () {
                    //alert("回调成功");
                    var validate = captchaObj.getValidate();
                    $.ajax({
                        url: base + "/user/validate", // 进行二次验证
                        type: "post",
                        dataType: "json",
                        data: {
                            username: $('#username').val(),
                            password: $('#password').val(),
                            geetest_challenge: validate.geetest_challenge,
                            geetest_validate: validate.geetest_validate,
                            geetest_seccode: validate.geetest_seccode
                        },
                        success: function (data) {
                            //console.log(data);
                            //if (data && (data.status == "success")) {
                            if(data.ok){
                                   // $(document.body).html('<h1>登录成功</h1>');
                                   // login();
                                    location.href = base + "/home/";
                            }else {
                                $(document.body).html('<h1>登录失败</h1>');
                                $("#msg").text(data.msg);
                            }
                        }
                    });
                });
                //SDK登录
               $("#login_button").click(function () {
                    if(check==false){
                        return;
                    }
                   //alert("点击登录");
                    if(!$("#loginForm").validationEngine('validate')){
                        //alert("验证失败");
                        return;
                    }
                    //var result = captchaObj.getValidate();
                    //captchaObj.show();
                });
                // 将验证码加到id为captcha的元素里
                captchaObj.appendTo("#popup-captcha");
                // 更多接口参考：http://www.geetest.com/install/sections/idx-client-sdk.html
            };
            // 验证开始需要向网站主后台获取id，challenge，success（是否启用failback）
            $.ajax({
                url: base + "/user/initValidate?t=" + (new Date()).getTime(), // 加随机数防止缓存
                type: "get",
                dataType: "json",
                success: function (data) {
                    // 使用initGeetest接口
                    // 参数1：配置参数
                    // 参数2：回调，回调的第一个参数验证码对象，之后可以使用它做appendTo之类的事件
                    initGeetest({
                        gt: data.gt,
                        challenge: data.challenge,
                        new_captcha: data.new_captcha, // 用于宕机时表示是新验证码的宕机
                        product: "popup", // 产品形式，包括：float，embed，popup。注意只对PC版验证码有效
                        offline: !data.success // 表示用户后台检测极验服务器是否宕机，一般不需要关注
                        // 更多配置参数请参见：http://www.geetest.com/install/sections/idx-client-sdk.html#config
                    }, handlerPopup);
                }
            });
        } */
		function login() {
			// if(!$("#loginForm").validationEngine('validate')){
			// 	return;
			// }
			//
			// if(userNameCheck == false|| passwordCheck == false ||checkCodeCheck == false){
			// 	return;
			// }
			//
			$.ajax({
				url : base + "/user/login",
				type : "POST",
				data : $('#loginForm').serialize(),
				error : function(request) {
					console.log(request);
				},
				dataType : "json",
				success : function(data) {
					if (data.ok) {
						location.reload();
						location.href = base + "/home/";
					} else {
						$("#msg").text(data.msg);
					}
				}
			});
		}
		//更换验证码
		function myReload() {
			document.getElementById("CreateCheckCode").src = document
							.getElementById("CreateCheckCode").src
					+ "?nocache=" + new Date().getTime();
		}
	</script>

	<style>
		body {
			width: 100%;
			height: 100%;
			background: url('${base}/image/bg.jpg') no-repeat;
			background-attachment: fixed;
			background-size: contain;
			-o-background-size: cover;
			-moz-background-size: cover;
			-webkit-background-size: cover;
		}
		.mydiv {
			opacity: 0.9
		}
	</style>
</head>
<body>
<div class="mydiv" style="position: absolute;top: 25%;left: 25%;">
	<div style="width: 400px">
		<div class="hero-unit" style="margin-bottom: 2px">
			<h3>
				<%-- <img src="/TzjBackend/image/logo.png" /> --%>${msg['jsp.login.logo']}
			</h3>
			<form id="loginForm" method="POST" style="height:220px;">
				<div class="form-group" style="margin-top: 10px;">
					<label class="control-label">${msg['jsp.login.user']}</label>
					<input type="text" id="username" name="username" value="admin" class="form-control input validate[required,minSize[4]]" onblur="checkUserName();">
					<span style="color:#b94a48" id="checkUserName"></span>
				</div>
				<div class="form-group" style="margin-top: 10px;">
					<label class="control-label">${msg['jsp.login.pass']}</label>
					<input type="password" id="password" name="password" value="admin" class="form-control input validate[required,minSize[1]]"<%-- onblur="checkPassword();"--%>>
					<span style="color:#b94a48" id="checkPassword"></span>
				</div>
				<%--						<div class="form-group" style="margin-top: 10px;">--%>
				<%--							<label class="control-label">${msg['jsp.login.checkCode']}</label>--%>
				<%--							<input name="checkCode" type="text" id="checkCode" class="form-control input validate[required,minSize[4]]" onblur="checkCheckCode();" />--%>
				<%--            				<span style="color:#b94a48" id="checkCheckCode"></span>--%>
				<%--            				<br/><br/>--%>
				<%--            				<img src="${base}/picture/setPictureCheckCode" id="CreateCheckCode" />--%>
				<%--            				<a href="#" onclick="myReload()"> ${msg['jsp.login.changepicture']}</a>--%>
				<%--						</div>--%>
				<br/>
				<input type="button" id="login_button" class="btn btn-primary" value="${msg['jsp.login']}">
				<div id="msg" style="color:#b94a48"></div>
			</form>
		</div>
		<div class="text-error">${msg['jsp.login.tips']}</div>
	</div>
</div>
</body>
</html>