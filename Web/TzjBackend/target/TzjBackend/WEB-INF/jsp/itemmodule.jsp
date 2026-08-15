<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="addItemModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>Добавить предмет</h3>
    </div>
    <div class="modal-body">
        <form id="addItemForm" class="row-fluid">
            <div class="offset1 span10">
                <input id="curObj" type="hidden"/>
                <label for="itemId">ID предмета</label><input type="text" id="itemId" list="itemList" class="span8"/>
                <datalist id="itemList"></datalist>
                <label for="itemNum">Количество</label><input id="itemNum" type="text" class="span8"/>
                <label for="isBind">Привязка</label>
                <select id="isBind" class="span8">
                    <option value="0" selected>Не привязан</option>
                    <option value="1">Привязан</option>
                </select>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <input type="button" value="Закрыть" class="btn" data-dismiss="modal" aria-hidden="true"/>
        <input type="button" value="Добавить" onclick="addItem()" class="btn btn-primary"/>
    </div>
</div>