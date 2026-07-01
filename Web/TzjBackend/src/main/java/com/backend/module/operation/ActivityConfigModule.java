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
 * 运营活动
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
     * 查询幸运值数据
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
     * 修改/添加幸运值配置
     */
    @At
    @POST
    public Object addLuckyValue(int idCopy, int totalLuckyValue, String tips, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        ActivityLuckyValue luckyValue = new ActivityLuckyValue();
        luckyValue.setTotalLuckyValue(totalLuckyValue);
        luckyValue.setTips(tips);
        luckyValue.setState(0);//设置状态
        if (idCopy > 0){
            //修改
            luckyValue.setId(idCopy);
            int num = dao.update(luckyValue);
            if (num <1 ){
                BackendLogUtil.getInstance().log(request, "活动ID:" + idCopy + "数据库更新失败");
                return Toolkit.outResult(false, ",数据库更新失败");
            }
        }else {
            //添加
            ActivityLuckyValue activityLuckyValue = dao.insert(luckyValue);
            if (activityLuckyValue == null) {
                BackendLogUtil.getInstance().log(request, "数据库添加失败");
                return Toolkit.outResult(false, ",数据库添加失败");
            }
        }
        return Toolkit.outResult(true, "");
    }

    /**
     * 删除抽奖幸运值配置
     * @param id
     * @return
     */
    @At
    @POST
    public Object deleteLuckyValue(int id,HttpServletRequest request) {
        ActivityLuckyValue luckyValue = dao.fetch(ActivityLuckyValue.class, Cnd.where("id", "=", id));
//        int num = dao.delete(ActivityLuckyValue.class, id);
//        boolean b = num > 0;
        luckyValue.setIsDeleted(Byte.parseByte(String.valueOf(1)));
        boolean b = dao.update(luckyValue) > 0;
        BackendLogUtil.getInstance().log(request, "删除抽奖幸运值活动id:" + id+",结果：" + b);
        return Toolkit.outResult(b);
    }

    /**
     * 发布抽奖幸运值配置到游戏服
     * @param groupName
     * @param serverids
     * @param id
     * @param totalLuckyValue
     * @param cover
     * @param request
     * @return
     */
    @At
    @POST
    public Object publishActivity(String groupName, String serverids, int id, int totalLuckyValue,int cover, HttpServletRequest request) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            return Toolkit.outResult(false, "");
        }
        if (StringUtils.isEmpty(groupName) || StringUtils.isEmpty(serverids)) {
            return Toolkit.outResult(false, "发送到服务器数据错误!");
        }
        List<Integer> okServerIdList = new ArrayList<>();//成功列表
        List<Integer> failServerIdList = new ArrayList<>();//失败列表
        ActivityLuckyValue luckyValue = dao.fetch(ActivityLuckyValue.class, Cnd.where("id", "=", id));
        luckyValue.setPlatform(groupName);
        luckyValue.setState(3);
        String serverIdStr = "";
        if (serverids.contains("[")){
            serverIdStr = serverIdStr.replace("[","").replace("]","");
        }else {
            serverIdStr = serverids;
        }
//        String[] serverIdArr = serverIdStr.split(",");
        NutMap result = new NutMap();
        List<Integer> serverIdList = JsonUtils.parseArray("[" + serverIdStr + "]", Integer.class);
        luckyValue.setToSidList(serverIdList.toString());//设置活动要发布到的区服列表
        for (Integer sid:serverIdList){
            Server server = ServerListManager.getInstance().getServer(sid);
            result = GameServerRequestUtil.gmPublishLuckyValue(server, totalLuckyValue);//向游戏服务器发送命令
            if (result.getBoolean("ok")) {
                okServerIdList.add(sid);
            } else {
                failServerIdList.add(sid);
            }
        }
        luckyValue.setOkSidList(okServerIdList.toString());
        luckyValue.setCover(cover);
        dao.update(luckyValue);
        BackendLogUtil.getInstance().log(request, "活动id:"+id+"要发布到的区服列表:" + serverIdList.toString()+",成功的区服列表："+okServerIdList.toString()+",失败的区服列表："+failServerIdList.toString());
        return Toolkit.outResult(true,"活动id:"+id+"要发布到的区服列表:" + serverIdList.toString()+",成功的区服列表："+okServerIdList.toString()+",失败的区服列表："+failServerIdList.toString());
    }

    /**
     * 查询模型库数据
     * @param page
     * @param rows
     * @return
     */
    @At
    @POST
    public Object queryModelConfig(@Param("page") int page, @Param("rows") int rows) {
        List<Model> list = dao.query(Model.class,null);
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
            //修改
            model.setId(idCopy);
            addModelData(idCopy,model);
            int num = dao.update(model);
            if (num <1 ){
                BackendLogUtil.getInstance().log(request, "ID:" + idCopy + "数据库更新失败");
                return Toolkit.outResult(false, ",数据库更新失败");
            }
        }else {
            //添加
            Model model1 = dao.insert(model);
            addModelData(model1.getId(),model1);
            dao.update(model1);
            if (model1 == null) {
                BackendLogUtil.getInstance().log(request, "数据库添加失败");
                return Toolkit.outResult(false, ",数据库添加失败");
            }
        }
        return Toolkit.outResult(true, "");
    }
    //设置ModelData(需要发送给服务器的数据)
    private void addModelData(int id, Model model) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        String[] careers = model.getCareer().split(",");
        String[] modelId = model.getModelId().split(",");
        String[] scale = model.getScale().split(",");
        String[] rotX = model.getRotX().split(",");
        String[] rotY = model.getRotY().split(",");
        String[] rotZ = model.getRotZ().split(",");
        String[] posX = model.getPosX().split(",");
        String[] posY = model.getPosY().split(",");
        List<HashMap<String, Object>> modelDataList = new ArrayList<>();
//        List<Integer> careerList = JsonUtils.parseArray("[" + model.getCareer() + "]", Integer.class);
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
        model.setModelData(JsonUtils.toJSONString(map));
    }

    /**
     * 删除模型库
     * @param id
     * @param request
     * @return
     */
    @At
    @POST
    public Object deleteModel(int id,HttpServletRequest request) {
        Model model = dao.fetch(Model.class, Cnd.where("id", "=", id));
        int num = dao.delete(Model.class, id);
        boolean b = num > 0;
        BackendLogUtil.getInstance().log(request, "删除模型库配置id:" + id+",模型库备注:"+model.getTips()+",结果：" + b);
        return Toolkit.outResult(b);
    }

    /**
     * 获取所有模型库类型
     * @return
     */
    @At
    public Object getAllModel() {
        List<Model> list = dao.query(Model.class, Cnd.NEW());
        return Toolkit.outResult(true, list);
    }

    /**
     * 查询标签库数据
     * @param page
     * @param rows
     * @return
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
     * 删除标签库
     * @param id
     * @param request
     * @return
     */
    @At
    @POST
    public Object deleteTag(int id,HttpServletRequest request) {
        Sql sql = Sqls.create("DELETE from tag where id=" + id + ";");
        sql.setCallback(Sqls.callback.integer());
        loginDao.execute(sql);
        int num = sql.getUpdateCount();
        boolean b = num > 0;
        String result = "";
        if (b){
            result = sendServerUpdate();
        }
        BackendLogUtil.getInstance().log(request, "删除标签库配置id:" + id+",结果：" + result);
        return Toolkit.outResult(b,result);
    }

    /**
     * 根据标签id进行检测
     * @param tagGrid
     * @param request
     * @return
     */
    @At
    @POST
    public Object checkTag(@Param("..") TagGrid tagGrid, HttpServletRequest request) {
        String sqlStr = "SELECT * FROM tag where id="+tagGrid.getId();
        List<Map<String, Object>> resultMap = QueryUtil.getInstance().query(loginDao, sqlStr);
        if (null != resultMap && resultMap.size() > 0){
            return Toolkit.outResult(false);
        }

        return Toolkit.outResult(true);
    }

    /**
     * 新增标签配置
     * @param tagGrid
     * @param request
     * @return
     */
    @At
    @POST
    public Object addTag(@Param("..") TagGrid tagGrid, HttpServletRequest request) {
        Sql sql = Sqls.create("insert into tag(id,name,icon,style) values ("+tagGrid.getId()+",'"+tagGrid.getName()+"','"+tagGrid.getIcon()+"',"+tagGrid.getStyle()+");");
        loginDao.execute(sql);
        int exeNum = sql.getUpdateCount();
        boolean b = exeNum > 0;
        String result = "";
        if (b){
            result = sendServerUpdate();
        }
        BackendLogUtil.getInstance().log(request, "添加标签库配置id:" + tagGrid.getId()+",结果：" + result);
        return Toolkit.outResult(b,result);
    }

    /**
     * 修改标签配置
     * @param tagGrid
     * @param request
     * @return
     */
    @At
    @POST
    public Object updateTag(@Param("..") TagGrid tagGrid, HttpServletRequest request) {
        Sql sql = Sqls.create("update tag set name='"+tagGrid.getName()+"',icon='"+tagGrid.getIcon()+"',style="+tagGrid.getStyle()+" where id="+tagGrid.getId()+";");
        loginDao.execute(sql);
        int exeNum = sql.getUpdateCount();
        boolean b = exeNum > 0;
        String result = "";
        if (b){
            result = sendServerUpdate();
        }
        BackendLogUtil.getInstance().log(request, "修改标签库配置id:" + tagGrid.getId()+",结果：" + result);
        return Toolkit.outResult(b,result);
    }

    /**
     * 获取所有标签库数据
     * @return
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

    //标签库有改动之后通知服务器更新
    private String sendServerUpdate(){
        StringBuilder sb = new StringBuilder();
        Cnd cnd = Cnd.where("isDeleted", "=", 0).
                and("isHeFu","=", 0).
                and("serverType", "in", "0,1");
        List<Server> servers = dao.query(Server.class, cnd);
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();
        for(Server server : servers){
            int serverId = server.getServerId();
            try {
                NutMap resultMap = GameServerRequestUtil.gmUpdateTagInfo(server);
                if (!resultMap.getBoolean("ok")) {
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
}