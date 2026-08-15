package com.backend.module.operation;

import com.backend.bean.*;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.ServerListManager;
import com.backend.utils.*;
import net.sf.json.JSON;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Операционные активности
 */
@IocBean
@Ok("json")
@At("/activityConfig")
@Fail("http:500")
public class ActivityConfigModule {
    private static final Log logger = Logs.getLog(ActivityConfigModule.class);
    @Inject
    protected Dao dao;

    @Inject
    private Dao loginDao;

    @At
    @Ok("jsp:jsp.activity.op.luckyValue")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void luckyValue() {
        HttpServletRequest request = Mvcs.getReq();
        HttpSession session = request.getSession();
        List<String> groupList = ServerListManager.getInstance().getGroupList();
        JSON groupServer = ServerListManager.getInstance().getGroupServer(groupList,0);
        session.setAttribute("groupServer",groupServer);
    }
    @At
    @Ok("jsp:jsp.activity.op.model")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void model() {

    }
    @At
    @Ok("jsp:jsp.activity.op.tag")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void tag() {

    }

    /**
     * Запрос данных значения удачи
     * @param page
     * @param rows
     * @return
     */
    @At
    @POST
    public Object queryActivityConfig(@Param("page") int page, @Param("rows") int rows) {
        Cnd cnd = Cnd.where("isDeleted", "=", 0);
        List<ActivityLuckyValue> list = dao.query(ActivityLuckyValue.class, cnd);
        if (list == null){
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= list.size() ? list.size() : rows * page;

        return Toolkit.outResult(true).setv("total", list.size()).setv("rows", list.subList(fromIndex, toIndex));
    }
    /**
     * Изменение/добавление конфигурации значения удачи
     */
    @At
    @POST
    public Object addLuckyValue(int idCopy, int totalLuckyValue, String tips, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        ActivityLuckyValue luckyValue = new ActivityLuckyValue();
        luckyValue.setTotalLuckyValue(totalLuckyValue);
        luckyValue.setTips(tips);
        luckyValue.setState(0); // Установка статуса
        if (idCopy > 0){
            // Изменение
            luckyValue.setId(idCopy);
            int num = dao.update(luckyValue);
            if (num < 1){
                BackendLogUtil.getInstance().log(request, "ID активности: " + idCopy + " — ошибка обновления в БД");
                return Toolkit.outResult(false, ", ошибка обновления в БД");
            }
        } else {
            // Добавление
            ActivityLuckyValue activityLuckyValue = dao.insert(luckyValue);
            if (activityLuckyValue == null) {
                BackendLogUtil.getInstance().log(request, "Ошибка добавления в БД");
                return Toolkit.outResult(false, ", ошибка добавления в БД");
            }
        }
        return Toolkit.outResult(true, "");
    }

    /**
     * Удаление конфигурации значения удачи
     * @param id
     * @return
     */
    @At
    @POST
    public Object deleteLuckyValue(int id, HttpServletRequest request) {
        ActivityLuckyValue luckyValue = dao.fetch(ActivityLuckyValue.class, Cnd.where("id", "=", id));
        luckyValue.setIsDeleted(Byte.parseByte(String.valueOf(1)));
        boolean b = dao.update(luckyValue) > 0;
        BackendLogUtil.getInstance().log(request, "Удаление активности значения удачи ID: " + id + ", результат: " + b);
        return Toolkit.outResult(b);
    }

    /**
     * Публикация конфигурации значения удачи на игровые сервера
     */
    @At
    @POST
    public Object publishActivity(String groupName, String serverids, int id, int totalLuckyValue, int cover, HttpServletRequest request) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            return Toolkit.outResult(false, "");
        }
        if (StringUtils.isEmpty(groupName) || StringUtils.isEmpty(serverids)) {
            return Toolkit.outResult(false, "Ошибка данных для отправки на сервер!");
        }
        List<Integer> okServerIdList = new ArrayList<>(); // Список успехов
        List<Integer> failServerIdList = new ArrayList<>(); // Список неудач
        ActivityLuckyValue luckyValue = dao.fetch(ActivityLuckyValue.class, Cnd.where("id", "=", id));
        luckyValue.setPlatform(groupName);
        luckyValue.setState(3);
        String serverIdStr = "";
        if (serverids.contains("[")){
            serverIdStr = serverIdStr.replace("[","").replace("]","");
        } else {
            serverIdStr = serverids;
        }
        NutMap result = new NutMap();
        List<Integer> serverIdList = JsonUtils.parseArray("[" + serverIdStr + "]", Integer.class);
        luckyValue.setToSidList(serverIdList.toString()); // Установка списка серверов для публикации
        for (Integer sid : serverIdList){
            Server server = ServerListManager.getInstance().getServer(sid);
            result = GameServerRequestUtil.gmPublishLuckyValue(server, totalLuckyValue); // Отправка команды на игровой сервер
            if (result.getBoolean("ok")) {
                okServerIdList.add(sid);
            } else {
                failServerIdList.add(sid);
            }
        }
        luckyValue.setOkSidList(okServerIdList.toString());
        luckyValue.setCover(cover);
        dao.update(luckyValue);
        BackendLogUtil.getInstance().log(request, "Активность ID: " + id + ", серверы для публикации: " + serverIdList.toString() + ", успешные серверы: " + okServerIdList.toString() + ", неудачные серверы: " + failServerIdList.toString());
        return Toolkit.outResult(true, "Активность ID: " + id + ", серверы для публикации: " + serverIdList.toString() + ", успешные серверы: " + okServerIdList.toString() + ", неудачные серверы: " + failServerIdList.toString());
    }

    /**
     * Запрос данных библиотеки моделей
     */
    @At
    @POST
    public Object queryModelConfig(@Param("page") int page, @Param("rows") int rows) {
        List<Model> list = dao.query(Model.class, null);
        if (list == null){
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= list.size() ? list.size() : rows * page;

        return Toolkit.outResult(true).setv("total", list.size()).setv("rows", list.subList(fromIndex, toIndex));
    }

    @At
    @POST
    public Object addOrEditModel(@Param("..") Model model, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        Map<String, String[]> paramMap = request.getParameterMap();
        String[] idCopys = paramMap.get("idCopy");
        int idCopy = Integer.parseInt(idCopys[0]);
        if (idCopy > 0){
            // Изменение
            model.setId(idCopy);
            addModelData(idCopy, model);
            int num = dao.update(model);
            if (num < 1){
                BackendLogUtil.getInstance().log(request, "ID: " + idCopy + " — ошибка обновления в БД");
                return Toolkit.outResult(false, ", ошибка обновления в БД");
            }
        } else {
            // Добавление
            Model model1 = dao.insert(model);
            addModelData(model1.getId(), model1);
            dao.update(model1);
            if (model1 == null) {
                BackendLogUtil.getInstance().log(request, "Ошибка добавления в БД");
                return Toolkit.outResult(false, ", ошибка добавления в БД");
            }
        }
        return Toolkit.outResult(true, "");
    }
    // Установка ModelData (данные для отправки на сервер)
    private void addModelData(int id, Model model) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        String[] careers = model.getCareer().split(",");
        String[] modelId = model.getModelId().split(",");
        String[] scale = model.getScale().split(",");
        String[] rotX = model.getRotX().split(",");
        String[] rotY = model.getRotY().split(",");
        String[] rotZ = model.getRotZ().split(",");
        String[] posX = model.getPosX().split(",");
        String[] posY = model.getPosY().split(",");
        List<HashMap<String, Object>> modelDataList = new ArrayList<>();
        for (int i = 0; i < careers.length; i++){
            HashMap<String, Object> modelMap = new HashMap<>();
            modelMap.put("career", careers[i]);
            modelMap.put("modelId", modelId[i]);
            modelMap.put("scale", scale[i]);
            modelMap.put("rotX", rotX[i]);
            modelMap.put("rotY", rotY[i]);
            modelMap.put("rotZ", rotZ[i]);
            modelMap.put("posX", posX[i]);
            modelMap.put("posY", posY[i]);

            modelDataList.add(modelMap);
        }
        map.put("modelDataList", modelDataList);
        model.setModelData(JsonUtils.toJSONString(map));
    }

    /**
     * Удаление библиотеки моделей
     */
    @At
    @POST
    public Object deleteModel(int id, HttpServletRequest request) {
        Model model = dao.fetch(Model.class, Cnd.where("id", "=", id));
        int num = dao.delete(Model.class, id);
        boolean b = num > 0;
        BackendLogUtil.getInstance().log(request, "Удаление конфигурации библиотеки моделей ID: " + id + ", примечание: " + model.getTips() + ", результат: " + b);
        return Toolkit.outResult(b);
    }

    /**
     * Получение всех типов библиотеки моделей
     */
    @At
    public Object getAllModel() {
        List<Model> list = dao.query(Model.class, Cnd.NEW());
        return Toolkit.outResult(true, list);
    }

    /**
     * Запрос данных библиотеки тегов
     */
    @At
    @POST
    public Object queryTagConfig(@Param("page") int page, @Param("rows") int rows) {
        List<TagGrid> list = getTagList();
        if (list == null || list.size() < 1){
            return Toolkit.outResult(true).setv("total", 0).setv("rows", 0);
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = rows * (page - 1);
        toIndex = rows * page >= list.size() ? list.size() : rows * page;

        return Toolkit.outResult(true).setv("total", list.size()).setv("rows", list.subList(fromIndex, toIndex));
    }

    /**
     * Удаление библиотеки тегов
     */
    @At
    @POST
    public Object deleteTag(int id, HttpServletRequest request) {
        Sql sql = Sqls.create("DELETE from tag where id=" + id + ";");
        sql.setCallback(Sqls.callback.integer());
        loginDao.execute(sql);
        int num = sql.getUpdateCount();
        boolean b = num > 0;
        String result = "";
        if (b){
            result = sendServerUpdate();
        }
        BackendLogUtil.getInstance().log(request, "Удаление конфигурации библиотеки тегов ID: " + id + ", результат: " + result);
        return Toolkit.outResult(b, result);
    }

    /**
     * Проверка по ID тега
     */
    @At
    @POST
    public Object checkTag(@Param("..") TagGrid tagGrid, HttpServletRequest request) {
        String sqlStr = "SELECT * FROM tag where id=" + tagGrid.getId();
        List<Map<String, Object>> resultMap = QueryUtil.getInstance().query(loginDao, sqlStr);
        if (null != resultMap && resultMap.size() > 0){
            return Toolkit.outResult(false);
        }

        return Toolkit.outResult(true);
    }

    /**
     * Добавление конфигурации тега
     */
    @At
    @POST
    public Object addTag(@Param("..") TagGrid tagGrid, HttpServletRequest request) {
        Sql sql = Sqls.create("insert into tag(id,name,icon,style) values (" + tagGrid.getId() + ",'" + tagGrid.getName() + "','" + tagGrid.getIcon() + "'," + tagGrid.getStyle() + ");");
        loginDao.execute(sql);
        int exeNum = sql.getUpdateCount();
        boolean b = exeNum > 0;
        String result = "";
        if (b){
            result = sendServerUpdate();
        }
        BackendLogUtil.getInstance().log(request, "Добавление конфигурации библиотеки тегов ID: " + tagGrid.getId() + ", результат: " + result);
        return Toolkit.outResult(b, result);
    }

    /**
     * Изменение конфигурации тега
     */
    @At
    @POST
    public Object updateTag(@Param("..") TagGrid tagGrid, HttpServletRequest request) {
        Sql sql = Sqls.create("update tag set name='" + tagGrid.getName() + "',icon='" + tagGrid.getIcon() + "',style=" + tagGrid.getStyle() + " where id=" + tagGrid.getId() + ";");
        loginDao.execute(sql);
        int exeNum = sql.getUpdateCount();
        boolean b = exeNum > 0;
        String result = "";
        if (b){
            result = sendServerUpdate();
        }
        BackendLogUtil.getInstance().log(request, "Изменение конфигурации библиотеки тегов ID: " + tagGrid.getId() + ", результат: " + result);
        return Toolkit.outResult(b, result);
    }

    /**
     * Получение всех данных библиотеки тегов
     */
    @At
    public Object getAllTag() {
        List<TagGrid> list = getTagList();
        return Toolkit.outResult(true, list);
    }

    private List<TagGrid> getTagList(){
        String sqlStr = "SELECT * FROM tag";
        List<Map<String, Object>> resultMap = QueryUtil.getInstance().query(loginDao, sqlStr);
        List<TagGrid> list = new ArrayList<>();
        for (Map<String, Object> result : resultMap){
            TagGrid tagGrid = new TagGrid();
            tagGrid.setId(Integer.parseInt(String.valueOf(result.get("id"))));
            tagGrid.setName(String.valueOf(result.get("name")));
            tagGrid.setIcon(String.valueOf(result.get("icon")));
            tagGrid.setStyle(Integer.parseInt(String.valueOf(result.get("style"))));
            list.add(tagGrid);
        }

        return list;
    }

    // Уведомление сервера об изменениях в библиотеке тегов
    private String sendServerUpdate(){
        StringBuilder sb = new StringBuilder();
        Cnd cnd = Cnd.where("isDeleted", "=", 0)
                .and("isHeFu", "=", 0)
                .and("serverType", "in", "0,1");
        List<Server> servers = dao.query(Server.class, cnd);
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();
        for (Server server : servers){
            int serverId = server.getServerId();
            try {
                NutMap resultMap = GameServerRequestUtil.gmUpdateTagInfo(server);
                if (!resultMap.getBoolean("ok")) {
                    serverFailedList.add(serverId);
                    logger.error("Сервер " + serverId + ", ошибка обновления библиотеки тегов! msg: " + resultMap.get("msg"));
                } else {
                    serverSuccessList.add(serverId);
                }
            } catch (Exception e){
                logger.error("Сервер " + serverId + ", ошибка синхронизации библиотеки тегов! Ошибка: " + e.getMessage());
                serverFailedList.add(serverId);
            }
        }
        sb.append("Список успешно синхронизированных игровых серверов: ").append(serverSuccessList).append("\n");
        sb.append("Список серверов с ошибкой синхронизации: ").append(serverFailedList).append("\n");
        return sb.toString();
    }
}