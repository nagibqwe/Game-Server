<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script type="text/javascript" src="${base}/js/dateFormat.js"></script>

<div id="addTemplateModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3>添加模板</h3>
            </div>
            <div class="modal-body">
                <div class="row-fluid">
                    <div class="input-group">
                        <label for="templateName">模板名称：</label><input type="text" id="templateName"/>
                        <label for="description">模板说明：</label><input type="text" id="description"/>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
                <input type="button" value="添加" onclick="submitTemplate()" class="btn btn-primary"/>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

