<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="item form-group">
    <label class="control-label col-md-3 col-sm-3 col-xs-12">货币类型</label>
    <div class="inline">
        <div class="col-md-3 col-sm-3 col-xs-3">
            <select id="coinType" name="coinType" onchange="" class="form-control">
                <option value="1">金元宝</option>
                <option value="3">铜钱</option>
                <option value="12">银元宝</option>
            </select>
        </div>
        <div class="col-md-3 col-sm-3 col-xs-3">
            <input type="text" class="form-control" id="coinCount" name="coinCount"
                   placeholder="800" onkeyup="value=value.replace(/\D/g,'')" class="input-xlarge" required/>
        </div>
    </div>
</div>