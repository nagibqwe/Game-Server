package com.gm.project.gmtool.activity.controller;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gmtool.activityLuckyValue.domain.ActivityLuckyValue;
import com.gm.project.gmtool.activityLuckyValue.service.IActivityLuckyValueService;
import com.gm.project.gmtool.activityModel.domain.ActivityModel;
import com.gm.project.gmtool.activityModel.service.IActivityModelService;
import com.gm.project.gmtool.activity.domain.TagGrid;
import com.gm.project.gmtool.selectgroup.service.ISelectGroupService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import com.gm.project.gmtool.utils.JsonUtils;
import com.gm.project.gmtool.utils.StringUtils;
import com.gm.project.system.user.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/gmtool/activityConfig")
public class ActivityConfigController extends BaseController {

    private String prefix = "gmtool/activityConfig";

    @Autowired
    private IActivityModelService modelService;

    @Autowired
    private IActivityLuckyValueService activityLuckyValueService;

    @Autowired
    private ITServerService tServerService;

    @Resource
    private ISelectGroupService selectGroupService;

    @RequiresPermissions("gmtool:activityConfig:luckyValue")
    @GetMapping("/luckyValue")
    public String luckyValue()
    {
        return prefix + "/luckyValue";
    }

    /**
     * Получение списка значений удачи для розыгрыша
     */
    @PostMapping("/luckyValueList")
    @ResponseBody
    public TableDataInfo luckyValueList(ActivityLuckyValue activityLuckyValue)
    {
        startPage();
        List<ActivityLuckyValue> list = activityLuckyValueService.selectActivityLuckyValueList(activityLuckyValue);
        return getDataTable(list);
    }

    @RequiresPermissions("gmtool:activityConfig:model")
    @GetMapping("/model")
    public String model()
    {
        return prefix + "/model";
    }

    /**
     * Список библиотеки моделей
     */
    @PostMapping("/modelList")
    @ResponseBody
    public TableDataInfo modelList(ActivityModel activityModel)
    {
        startPage();
        List<ActivityModel> list = modelService.selectModelList(activityModel);
        return getDataTable(list);
    }

    @RequiresPermissions("gmtool:activityConfig:tag")
    @GetMapping("/tag")
    public String tag()
    {
        return prefix + "/tag";
    }

    /**
     * Список библиотеки тегов
     */
    @PostMapping("/tagList")
    @ResponseBody
    public TableDataInfo tagList()
    {
        startPage();
        List<TagGrid> list = getTagList();
        return getDataTable(list);
    }

    /**
     * Изменение/добавление конфигурации значения удачи
     * @param idCopy
     * @param totalLuckyValue
     * @param tips
     * @return
     */
    @PostMapping("/addLuckyValue")
    @ResponseBody
    public Object addLuckyValue(int idCopy, int totalLuckyValue, String tips) {
        ActivityLuckyValue luckyValue = new ActivityLuckyValue();
        luckyValue.setTotalLuckyValue(totalLuckyValue);
        luckyValue.setTips(tips);
        luckyValue.setState(0);//Установка статуса
        if (idCopy > 0){
            //Изменение
            luckyValue.setId(idCopy);
            int num = activityLuckyValueService.updateActivityLuckyValue(luckyValue);
            if (num <1 ){
                GMLogUtil.log("ID активности:" + idCopy + " ошибка обновления БД");
                return AjaxResult.info("Не удалось обновить базу данных").put("ok",false);
            }
        }else {
            //Добавление
            int updateNum = activityLuckyValueService.insertActivityLuckyValue(luckyValue);
            if (updateNum < 1) {
                GMLogUtil.log("Ошибка добавления в БД");
                return AjaxResult.info("Не удалось добавить запись в базу данных").put("ok",false);
            }
        }
        return AjaxResult.info("").put("ok",true);
    }

    /**
     * Публикация конфигурации значения удачи на игровые серверы
     * @param groupName
     * @param serverids
     * @param id
     * @param totalLuckyValue
     * @param cover
     * @return
     */
    @PostMapping("/publishActivity")
    @ResponseBody
    public Object publishActivity(String groupName, String serverids, int id, int totalLuckyValue,int cover) {
        User user = ShiroUtils.getSysUser();
        if (user == null) {
            return AjaxResult.info("").put("ok",false);
        }
        if (StringUtils.isEmpty(groupName) || StringUtils.isEmpty(serverids)) {
            return AjaxResult.info("Ошибка данных для отправки на сервер!").put("ok",false);
        }
        List<Integer> okServerIdList = new ArrayList<>();//Список успешных
        List<Integer> failServerIdList = new ArrayList<>();//Список неудачных
        ActivityLuckyValue luckyValue = activityLuckyValueService.selectActivityLuckyValueById(id);
        luckyValue.setPlatform(groupName);
        luckyValue.setState(3);
        String serverIdStr = "";
        if (serverids.contains("[")){
            serverIdStr = serverIdStr.replace("[","").replace("]","");
        }else {
            serverIdStr = serverids;
        }
//        String[] serverIdArr = serverIdStr.split(",");
        HashMap result = new HashMap();
        List<Integer> serverIdList = JsonUtils.parseArray("[" + serverIdStr + "]", Integer.class);
        luckyValue.setToSidList(serverIdList.toString());//Установка списка серверов для публикации
        for (Integer sid:serverIdList){
            TServer server = tServerService.selectTServerByServerId(sid);
            result = GameServerRequestUtil.gmPublishLuckyValue(server, totalLuckyValue);//Отправка команды на игровой сервер
            if (Boolean.valueOf(result.get("ok").toString())) {
                okServerIdList.add(sid);
            } else {
                failServerIdList.add(sid);
            }
        }
        luckyValue.setOkSidList(okServerIdList.toString());
        luckyValue.setCover(cover);
        activityLuckyValueService.updateActivityLuckyValue(luckyValue);
        GMLogUtil.log("ID активности:"+id+", список серверов для публикации:" + serverIdList.toString()+", успешные серверы:"+okServerIdList.toString()+", неудачные серверы:"+failServerIdList.toString());
        return AjaxResult.info("ID активности:"+id+", список серверов для публикации:" + serverIdList.toString()+", успешные серверы:"+okServerIdList.toString()+", неудачные серверы:"+failServerIdList.toString()).put("ok",true);
    }

    /**
     * Удаление конфигурации значения удачи
     * @param id
     * @return
     */
    @PostMapping("/deleteLuckyValue")
    @ResponseBody
    public Object deleteLuckyValue(int id) {
        ActivityLuckyValue luckyValue = activityLuckyValueService.selectActivityLuckyValueById(id);
//        int num = dao.delete(ActivityLuckyValue.class, id);
//        boolean b = num > 0;
        luckyValue.setIsDeleted(1);
        boolean b = activityLuckyValueService.updateActivityLuckyValue(luckyValue) > 0;
        GMLogUtil.log("Удаление активности удачи ID:" + id+", результат：" + b);
        return AjaxResult.info("").put("ok",b);
    }

    /**
     * Добавление/изменение библиотеки моделей
     * @param activityModel
     * @param request
     * @return
     */
    @PostMapping("/addOrEditModel")
    @ResponseBody
    public Object addOrEditModel(ActivityModel activityModel, HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String[] idCopys = paramMap.get("idCopy");
        int idCopy = Integer.parseInt(idCopys[0]);
        if (idCopy > 0){
            //Изменение
            activityModel.setId(idCopy);
            addModelData(idCopy, activityModel);
            int num = modelService.updateModel(activityModel);
            if (num <1 ){
                GMLogUtil.log("ID:" + idCopy + " ошибка обновления БД");
                return AjaxResult.info("Не удалось обновить базу данных").put("ok",false);
            }
        }else {
            //Добавление
            int addNum = modelService.insertModel(activityModel);
            addModelData(activityModel.getId(), activityModel);
            modelService.updateModel(activityModel);
            if (addNum < 1) {
                GMLogUtil.log("Ошибка добавления в БД");
                return AjaxResult.info("Не удалось добавить запись в базу данных").put("ok",false);
            }
        }
        return AjaxResult.info("").put("ok",true);
    }
    //Установка ModelData (данные для отправки на сервер)
    private void addModelData(int id, ActivityModel activityModel) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        String[] careers = activityModel.getCareer().split(",");
        String[] modelId = activityModel.getModelId().split(",");
        String[] scale = activityModel.getScale().split(",");
        String[] rotX = activityModel.getRotX().split(",");
        String[] rotY = activityModel.getRotY().split(",");
        String[] rotZ = activityModel.getRotZ().split(",");
        String[] posX = activityModel.getPosX().split(",");
        String[] posY = activityModel.getPosY().split(",");
        List<HashMap<String, Object>> modelDataList = new ArrayList<>();
//        List<Integer> careerList = JsonUtils.parseArray("[" + activityModel.getCareer() + "]", Integer.class);
        for (int i = 0; i < careers.length; i++){
            HashMap<String, Object> modelMap = new HashMap<>();
            modelMap.put("career",careers[i]);
            modelMap.put("modelId",modelId[i]);
            modelMap.put("scale",scale[i]);
            modelMap.put("rotX",rotX[i]);
            modelMap.put("rotY",rotY[i]);
            modelMap.put("rotZ",rotZ[i]);
            modelMap.put("posX",posX[i]);
            modelMap.put("posY",posY[i]);

            modelDataList.add(modelMap);
        }
        map.put("modelDataList",modelDataList);
        activityModel.setModelData(JsonUtils.toJSONString(map));
    }

    /**
     * Удаление модели
     * @param id
     * @return
     */
    @PostMapping("/deleteModel")
    @ResponseBody
    public Object deleteModel(int id) {
        ActivityModel activityModel = modelService.selectModelById(id);
        boolean b = modelService.deleteModelById(id) > 0;
        GMLogUtil.log("Удаление модели ID:" + id+", примечание:"+ activityModel.getTips()+", результат：" + b);
        return AjaxResult.info("").put("ok",b);
    }


    /**
     * Получение всех тегов операционных активностей
     * @return
     */
    @PostMapping( "/getAllTag")
    @ResponseBody
    public Object getAllTag() {
        List<TagGrid> list = getTagList();
        return AjaxResult.info("",list).put("ok",true);
    }

    private List<TagGrid> getTagList(){
        String sqlStr = "SELECT * FROM tag";
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        List<Map<String, Object>> resultMap = loginDao.selectList(sqlStr);
        List<TagGrid> list = new ArrayList<>();
        for (Map<String, Object> result:resultMap){
            TagGrid tagGrid = new TagGrid();
            tagGrid.setId(Integer.parseInt(String.valueOf(result.get("id"))));
            tagGrid.setName(String.valueOf(result.get("name")));
            tagGrid.setIcon(String.valueOf(result.get("icon")));
            tagGrid.setStyle(Integer.parseInt(String.valueOf(result.get("style"))));
            list.add(tagGrid);
        }

        return list;
    }

    /**
     * Проверка по ID тега
     * @param tagGrid
     * @return
     */
    @PostMapping( "/checkTag")
    @ResponseBody
    public Object checkTag(TagGrid tagGrid) {
        String sqlStr = "SELECT * FROM tag where id="+tagGrid.getId();
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        List<Map<String, Object>> resultMap = loginDao.selectList(sqlStr);
        if (null != resultMap && resultMap.size() > 0){
            return AjaxResult.info("").put("ok",false);
        }

        return AjaxResult.info("").put("ok",true);
    }

    /**
     * Добавление тега
     * @param tagGrid
     * @return
     * @throws SQLException
     */
    @PostMapping( "/addTag")
    @ResponseBody
    public Object addTag(TagGrid tagGrid) throws SQLException {
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        String sqlStr = "insert into tag(id,name,icon,style) values ("+tagGrid.getId()+",'"+tagGrid.getName()+"','"+tagGrid.getIcon()+"',"+tagGrid.getStyle()+");";
        int exeNum = loginDao.executeUpdate(sqlStr);
        boolean b = exeNum > 0;
        String result = "";
        if (b){
            result = sendServerUpdate();
        }
        GMLogUtil.log("Добавление тега ID:" + tagGrid.getId()+", результат：" + result);
        return AjaxResult.info(result).put("ok",b);
    }

    /**
     * Изменение тега
     * @param tagGrid
     * @return
     * @throws SQLException
     */
    @PostMapping( "/updateTag")
    @ResponseBody
    public Object updateTag(TagGrid tagGrid) throws SQLException {
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        String sqlStr = "update tag set name='"+tagGrid.getName()+"',icon='"+tagGrid.getIcon()+"',style="+tagGrid.getStyle()+" where id="+tagGrid.getId()+";";
        int exeNum = loginDao.executeUpdate(sqlStr);
        boolean b = exeNum > 0;
        String result = "";
        if (b){
            result = sendServerUpdate();
        }
        GMLogUtil.log("Изменение тега ID:" + tagGrid.getId()+", результат：" + result);
        return AjaxResult.info(result).put("ok",b);
    }

    //Уведомление серверов об обновлении библиотеки тегов
    private String sendServerUpdate(){
        StringBuilder sb = new StringBuilder();
        TServer serverSearch = new TServer();
        serverSearch.setIsHeFu(0);
        serverSearch.setServerType(0);
        List<TServer> list = tServerService.selectTServerList(serverSearch);
        serverSearch.setServerType(1);
        List<TServer> servers = tServerService.selectTServerList(serverSearch);
        servers.addAll(list);
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();
        for(TServer server : servers){
            int serverId = server.getServerId();
            try {
                HashMap resultMap = GameServerRequestUtil.gmUpdateTagInfo(server);
                if (!Boolean.valueOf(resultMap.get("ok").toString())) {
                    serverFailedList.add(serverId);
                    logger.error(serverId + " сервер, ошибка обновления библиотеки тегов! msg:"+resultMap.get("msg"));
                } else {
                    serverSuccessList.add(serverId);
                }
            }catch (Exception e){
                logger.error(serverId + " сервер, ошибка синхронизации библиотеки тегов! error："+e.getMessage());
                serverFailedList.add(serverId);
            }
        }
        sb.append("Синхронизация на игровые серверы успешно：").append(serverSuccessList).append("\n");
        sb.append("Синхронизация на игровые серверы неудачно：").append(serverFailedList).append("\n");
        return sb.toString();
    }

    /**
     * Удаление тега
     * @param id
     * @return
     * @throws SQLException
     */
    @PostMapping( "/deleteTag")
    @ResponseBody
    public Object deleteTag(int id) throws SQLException {
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        String sqlStr = "DELETE from tag where id=" + id + ";";
        int exeNum = loginDao.executeUpdate(sqlStr);
        boolean b = exeNum > 0;
        String result = "";
        if (b){
            result = sendServerUpdate();
        }
        GMLogUtil.log("Удаление тега ID:" + id+", результат：" + result);
        return AjaxResult.info(result).put("ok",b);
    }

    /**
     * Получение всех типов моделей
     * @return
     */
    @PostMapping( "/getAllModel")
    @ResponseBody
    public Object getAllModel() {
        List<ActivityModel> list = modelService.selectModelList(new ActivityModel());
        return AjaxResult.info("",list).put("ok",true);
    }
}