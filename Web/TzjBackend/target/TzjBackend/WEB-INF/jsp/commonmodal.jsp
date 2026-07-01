<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--这是配套使用bootstrap版本对应的 --%>
<div class="modal hide fade in" id="loadingmodal">
    <div class="modal-body">
		<div style="margin-left: 20px; margin-top:18px;" id="spintarget"></div>
		<div style="padding-left: 50px;"><p style="font-size: 14px;">${msg['jsp.loading']}</p></div>
    </div>
</div>

<div class="modal hide fade in" id="progressmodal">
	<div class="modal-header">
    	<h3 id="progressdetailhead">${msg['jsp.dealing']}</h3>
    </div>
	<div class="modal-body">
		<div class="progress">
			<div class="bar" id="bar" style="width: 1%;"></div>
		</div>
		<div id="progressdetail"></div>
	</div>
</div>