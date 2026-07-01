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
     * 查询抽奖幸运值列表
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
     * 模型库列表
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
     * 标签库列表
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
     * 修改/添加幸运值配置
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
        luckyValue.setState(0);//设置状态
        if (idCopy > 0){
            //修改
            luckyValue.setId(idCopy);
            int num = activityLuckyValueService.updateActivityLuckyValue(luckyValue);
            if (num <1 ){
                GMLogUtil.log("活动ID:" + idCopy + "数据库更新失败");
                return AjaxResult.info("数据库更新失败").put("ok",false);
            }
        }else {
            //添加
            int updateNum = activityLuckyValueService.insertActivityLuckyValue(luckyValue);
            if (updateNum < 1) {
                GMLogUtil.log("数据库添加失败");
                return AjaxResult.info("数据库添加失败").put("ok",false);
            }
        }
        return AjaxResult.info("").put("ok",true);
    }

    /**
     * 发布抽奖幸运值配置到游戏服
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
            return AjaxResult.info("发送到服务器数据错误!").put("ok",false);
        }
        List<Integer> okServerIdList = new ArrayList<>();//成功列表
        List<Integer> failServerIdList = new ArrayList<>();//失败列表
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
        luckyValue.setToSidList(serverIdList.toString());//设置活动要发布到的区服列表
        for (Integer sid:serverIdList){
            TServer server = tServerService.selectTServerByServerId(sid);
            result = GameServerRequestUtil.gmPublishLuckyValue(server, totalLuckyValue);//向游戏服务器发送命令
            if (Boolean.valueOf(result.get("ok").toString())) {
                okServerIdList.add(sid);
            } else {
                failServerIdList.add(sid);
            }
        }
        luckyValue.setOkSidList(okServerIdList.toString());
        luckyValue.setCover(cover);
        activityLuckyValueService.updateActivityLuckyValue(luckyValue);
        GMLogUtil.log("活动id:"+id+"要发布到的区服列表:" + serverIdList.toString()+",成功的区服列表："+okServerIdList.toString()+",失败的区服列表："+failServerIdList.toString());
        return AjaxResult.info("活动id:"+id+"要发布到的区服列表:" + serverIdList.toString()+",成功的区服列表："+okServerIdList.toString()+",失败的区服列表："+failServerIdList.toString()).put("ok",true);
    }

    /**
     * 删除抽奖幸运值配置
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
        GMLogUtil.log("删除抽奖幸运值活动id:" + id+",结果：" + b);
        return AjaxResult.info("").put("ok",b);
    }

    /**
     * 添加/修改模型库
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
            //修改
            activityModel.setId(idCopy);
            addModelData(idCopy, activityModel);
            int num = modelService.updateModel(activityModel);
            if (num <1 ){
                GMLogUtil.log("ID:" + idCopy + "数据库更新失败");
                return AjaxResult.info("数据库更新失败").put("ok",false);
            }
        }else {
            //添加
            int addNum = modelService.insertModel(activityModel);
            addModelData(activityModel.getId(), activityModel);
            modelService.updateModel(activityModel);
            if (addNum < 1) {
                GMLogUtil.log("数据库添加失败");
                return AjaxResult.info("数据库添加失败").put("ok",false);
            }
        }
        return AjaxResult.info("").put("ok",true);
    }
    //设置ModelData(需要发送给服务器的数据)
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
     * 删除模型
     * @param id
     * @return
     */
    @PostMapping("/deleteModel")
    @ResponseBody
    public Object deleteModel(int id) {
        ActivityModel activityModel = modelService.selectModelById(id);
        boolean b = modelService.deleteModelById(id) > 0;
        GMLogUtil.log("删除模型库配置id:" + id+",模型库备注:"+ activityModel.getTips()+",结果：" + b);
        return AjaxResult.info("").put("ok",b);
    }


    /**
     * 获取全部运营活动标签
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
     * 根据标签id进行检测
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
     * 新增标签配置
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
        GMLogUtil.log("添加标签库配置id:" + tagGrid.getId()+",结果：" + result);
        return AjaxResult.info(result).put("ok",b);
    }

    /**
     * 修改标签配置
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
        GMLogUtil.log("修改标签库配置id:" + tagGrid.getId()+",结果：" + result);
        return AjaxResult.info(result).put("ok",b);
    }

    //标签库有改动之后通知服务器更新
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
                    logger.error(serverId + "服,标签库配置刷新失败！msg:"+resultMap.get("msg"));
                } else {
                    serverSuccessList.add(serverId);
                }
            }catch (Exception e){
                logger.error(serverId + "服标签库配置同步失败！error："+e.getMessage());
                serverFailedList.add(serverId);
            }
        }
        sb.append("游戏服同步成功列表：").append(serverSuccessList).append("\n");
        sb.append("游戏服同步失败列表：").append(serverFailedList).append("\n");
        return sb.toString();
    }

    /**
     * 删除标签库
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
        GMLogUtil.log("删除标签库配置id:" + id+",结果：" + result);
        return AjaxResult.info(result).put("ok",b);
    }

    /**
     * 获取所有模型库类型
     * @return
     */
    @PostMapping( "/getAllModel")
    @ResponseBody
    public Object getAllModel() {
        List<ActivityModel> list = modelService.selectModelList(new ActivityModel());
        return AjaxResult.info("",list).put("ok",true);
    }
}
