package com.gm.project.gmtool.operation.controller;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.project.gmtool.activeCodebatch.domain.CodeBatch;
import com.gm.project.gmtool.activeCodebatch.service.ICodeBatchService;
import com.gm.project.gmtool.item.domain.Item;
import com.gm.project.gmtool.manager.ItemManager;
import com.gm.project.gmtool.operation.domain.ActiveCode;
import com.gm.project.gmtool.operation.domain.ActiveCodeLog;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.gmtool.utils.RandomUtil;
import com.gm.project.gmtool.utils.TimeUtils;
import com.gm.project.system.user.domain.User;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 操作类型的Controller
 *
 * @author gm
 * @date 2021-09-18
 */
@Controller
@RequestMapping("/gmtool/operation")
public class OperationController extends BaseController {
    private String prefix = "gmtool/operation";

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger log = LoggerFactory.getLogger(OperationController.class);
    @Autowired
    private ICodeBatchService codeBatchService;
    @Autowired
    private ITServerService tServerService;

    /**
     * 激活码相关跳转
     * @param type
     * @param mmap
     * @return
     */
    @RequiresPermissions("gmtool:operation:view")
    @GetMapping("/getPage")
    public String activity(int type, ModelMap mmap)
    {
        switch (type) {
            case 1:
                return prefix + "/activecode";
            case 2:
                return prefix + "/activecodesearch";
            case 3:
                return prefix + "/activecodeUse";
        }
        return "";
    }

    /**
     * 查询激活码列表
     * @param pageSize
     * @param pageNum
     * @return
     */
    @PostMapping( "/queryActiveCode")
    @ResponseBody
    public Object queryActiveCode(int pageSize,int pageNum) {
        List<Map<String, String>> result = new ArrayList<>();
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        int start = 0;
        start = (pageNum - 1) * pageSize;
        String sql = "select code, activeName, batch, itemList, param, valide_time_begin, valide_time_end, " +
                "plateform_name_big, plateform_name_small, valide_server_id_list, create_time, deleteTime from activecode order by create_time desc limit "+start+","+pageSize+"";

        String sql2 = "select code, activeName, batch, itemList, param, valide_time_begin, valide_time_end, " +
                "plateform_name_big, plateform_name_small, valide_server_id_list, create_time, deleteTime from activecode order by create_time desc";
        List<Map<String, String>> dataList = null;
        List<Map<String, String>> dataListCount = null;
        dataList = loginDao.selectList(sql);
        dataListCount = loginDao.selectList(sql2);
        if (!dataList.isEmpty()) {
            dataList = getNewList(dataList);
            result.addAll(dataList);
        }
        List<ActiveCode> grids = new ArrayList<>();
        for (Map<String, String> map:dataList){
            ActiveCode grid = new ActiveCode();
            grid.setCode(map.get("code"));
            grid.setActiveName(map.get("activeName"));
            grid.setBatch(map.get("batch"));
            grid.setItemList(map.get("itemList"));
            grid.setParam(Integer.parseInt(String.valueOf(map.get("param"))));
            grid.setValide_time_begin(String.valueOf(map.get("valide_time_begin")));
            grid.setValide_time_end(String.valueOf(map.get("valide_time_end")));
            grid.setPlateform_name_big(map.get("plateform_name_big"));
            grid.setValide_server_id_list(map.get("valide_server_id_list"));
            grid.setCreate_time(TimeUtils.format2string(Long.parseLong(String.valueOf(map.get("create_time"))) * 1000L));
            if (String.valueOf(map.get("deleteTime")).equals("0")){
                grid.setDeleteTime(String.valueOf(0));
            }else {
                grid.setDeleteTime(TimeUtils.format2string(Long.parseLong(String.valueOf(map.get("deleteTime"))) * 1000L));
            }
            grids.add(grid);
        }
        return AjaxResult.success("").put("ok",true).put("total",dataListCount.size()).put("rows",grids);
    }

    /**
     * 根据批次号ID查询激活码列表
     * @param pageSize
     * @param pageNum
     * @param searchParam
     * @return
     */
    @PostMapping( "/queryActiveCodeByBatchId")
    @ResponseBody
    public Object queryActiveCodeByBatchId(int pageSize,int pageNum,Integer searchType,String searchParam) {
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        int start = 0;
        start = (pageNum - 1) * pageSize;
        if (searchType == 1){
            if (!com.gm.project.gmtool.utils.StringUtils.isNumber(searchParam)){
                return AjaxResult.success("").put("ok",true).put("total",0).put("rows",new ArrayList<>());
            }
        }
        if (searchType == 0){
            if (com.gm.project.gmtool.utils.StringUtils.isNumber(searchParam)){
                return AjaxResult.success("").put("ok",true).put("total",0).put("rows",new ArrayList<>());
            }
        }
        StringBuilder common = new StringBuilder("select code, activeName, batch, itemList, param, valide_time_begin, valide_time_end, plateform_name_big, plateform_name_small, valide_server_id_list, create_time, get_time, get_player_id, get_account_id, get_plateform_aid, get_plateform_name,get_server_id from activecode");
        String sqlStr = "";
        String sqlStr2 = "";
        List<Map<String, String>> result = new ArrayList<>();
        List<Map<String, String>> resultCount = new ArrayList<>();

        if (com.gm.project.gmtool.utils.StringUtils.isNumber(searchParam)){
            //根据批号获取平台名
            CodeBatch codeBatch = new CodeBatch();
            codeBatch.setBatchId(Integer.parseInt(searchParam));
            List<CodeBatch> codeBatches = codeBatchService.selectCodeBatchList(codeBatch);
            codeBatch = codeBatches.get(0);
            if (codeBatch == null) {
                return AjaxResult.info("没有找到该批号相关激活码信息").put("ok",false);
            }
            common.append(" where batch=\""+searchParam+"\"");
        }else if (!"".equals(searchParam) && !com.gm.project.gmtool.utils.StringUtils.isNumber(searchParam)){
            common.append(" where code=\""+searchParam+"\"");
        }

        common.append(" order by create_time desc");
        sqlStr = common.toString();
        common.append(" limit "+start+","+pageSize+"");
        sqlStr2 = common.toString();
        result = loginDao.selectList(sqlStr);
        resultCount = loginDao.selectList(sqlStr2);

        if (result.isEmpty()) {
            return AjaxResult.info("没有数据").put("ok",false);
        }
        result = getNewList(result);
        List<ActiveCode> grids = new ArrayList<>();
        for (Map<String, String> map:result){
            ActiveCode grid = new ActiveCode();
            grid.setCode(map.get("code"));
            grid.setActiveName(map.get("activeName"));
            grid.setBatch(map.get("batch"));
            grid.setItemList(map.get("itemList"));
            grid.setParam(Integer.parseInt(String.valueOf(map.get("param"))));
            grid.setValide_time_begin(String.valueOf(map.get("valide_time_begin")));
            grid.setValide_time_end(String.valueOf(map.get("valide_time_end")));
            grid.setPlateform_name_big(map.get("plateform_name_big"));
            grid.setValide_server_id_list(map.get("valide_server_id_list"));
            grid.setCreate_time(TimeUtils.format2string(Long.parseLong(String.valueOf(map.get("create_time"))) * 1000L));
            grid.setGetTime(TimeUtils.format2string(Long.parseLong(String.valueOf(map.get("get_time"))) * 1000L));
            grid.setGetPlayerId(String.valueOf(map.get("get_player_id")));
            grid.setGetPlateformAid(map.get("get_plateform_aid"));
            grid.setGetAccountId(String.valueOf(map.get("get_account_id")));
            grid.setGetPlateformName(map.get("get_plateform_name"));
            grid.setGetServerId(String.valueOf(map.get("get_server_id")));
            grids.add(grid);
        }

        return AjaxResult.success("").put("ok",true).put("total",result.size()).put("rows",grids);
    }

    /**
     * 根据激活码查询激活码使用日志列表
     * @param pageSize
     * @param pageNum
     * @param serverId
     * @param activeCode
     * @param roleId
     * @return
     */
    @PostMapping( "/queryActiveCodeByCode")
    @ResponseBody
    public Object queryActiveCodeByCode(int pageSize,int pageNum,Integer serverId,String activeCode,String roleId) {
        TServer dblog = tServerService.selectTServerByServerId(serverId);
        if (dblog == null) {
            return AjaxResult.info("请求查询的服务器并不存在").put("ok",false);
        }

        DBClient dbClient = DBServerMgr.getInstance().getLogDBClient(dblog);
        int start = 0;
        start = (pageNum - 1) * pageSize;
        if (null == serverId || serverId < 0){
            return AjaxResult.info("服务器ID错误").put("ok",false);
        }
//        if (!StringUtils.isBlank(activeCode) && activeCode.length() < 6) {
//            return AjaxResult.info("输入的激活码不正确,长度应大于等于6").put("ok",false);
//        }
//        if (StringUtils.isBlank(activeCode)){
//            return AjaxResult.success("").put("ok",true).put("total",0).put("rows",new ArrayList<>());
//        }
        StringBuilder common = new StringBuilder("select activeCode,platformName,sid,roleid,userId,itemList,actionId from activecodelog");
        String sqlStr = "";
        String sqlStr2 = "";
        List<Map<String, String>> result = new ArrayList<>();
        List<Map<String, String>> resultCount = new ArrayList<>();
        if ("".equals(activeCode) && "".equals(roleId)){

        }else if ("".equals(activeCode) && !"".equals(roleId)){
            Long roleID = Long.parseLong(roleId);
            common.append(" where roleid="+roleID+"");
        }else if (!"".equals(activeCode) && "".equals(roleId)){
            common.append(" where activeCode=\""+activeCode+"\"");
        }else if (!"".equals(activeCode) && !"".equals(roleId)){
            Long roleID = Long.parseLong(roleId);
            common.append(" where roleid="+roleID+" and activeCode=\""+activeCode+"\"");
        }
        sqlStr = common.toString();
        common.append(" limit "+start+","+pageSize+"");
        sqlStr2 = common.toString();

        result = dbClient.selectList(sqlStr);
        resultCount = dbClient.selectList(sqlStr2);
        if (null != result && !result.isEmpty()) {
            result = getNewList(result);
        }
        if (null == result){
            return AjaxResult.info("数据库查询出错").put("ok",false);
        }
        if (result.isEmpty()) {
            return AjaxResult.info("没有使用激活码相关记录").put("ok",false);
        }
        List<ActiveCodeLog> grids = new ArrayList<>();
        for (Map<String,String> map:result){
            ActiveCodeLog grid = new ActiveCodeLog();
            grid.setActiveCode(map.get("activeCode"));
            grid.setPlatformName(map.get("platformName"));
            grid.setSid(String.valueOf(map.get("sid")));
            grid.setRoleid(String.valueOf(map.get("roleid")));
            grid.setUserId(String.valueOf(map.get("userId")));
            grid.setItemList(map.get("itemList"));
            grid.setActionId(String.valueOf(map.get("actionId")));
            grids.add(grid);
        }
        return AjaxResult.success("").put("ok",true).put("total",result.size()).put("rows",grids);
    }

    private List<Map<String, String>> getNewList(List<Map<String, String>> list) {
        for (Map<String, String> map : list) {
            if (map.containsKey("itemList")) {
                String itemList = map.get("itemList");
                String itemStr = "";
                String[] items = itemList.split(";");
                for (int i = 0; i < items.length; i++) {
                    String[] item = items[i].split(",");
                    if (item.length > 0) {
                        if (StringUtils.isBlank(item[0]) || "null".equalsIgnoreCase(item[0])) {
                            continue;
                        }
                        Item item0 = ItemManager.getInstance().getItemList().get(Integer.parseInt(item[0]));
                        itemStr += "[" + item[0] + "]" + (item0 == null ? "[未知]" : item0.getItemName());
                        if (item.length > 1) {
                            itemStr += "_" + "数量:" + item[1];
                            itemStr += "_" + "是否绑定:";
                            if (item.length > 2) {
                                if (item[2].equals("1")) {
                                    itemStr += "绑定";
                                } else {
                                    itemStr += "不绑定";
                                }
                            }
                        }
                        if (i < (items.length - 1)) {
                            itemStr += ";";
                        }
                    }
                }
                map.put("itemList", itemStr);
            }
        }
        return list;
    }

    /**
     * 禁用激活码
     * @param code
     * @return
     * @throws SQLException
     */
    @PostMapping( "/deleteCode")
    @ResponseBody
    public Object deleteCode(String code) throws SQLException {
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        if (StringUtils.isBlank(code)) {
            return AjaxResult.info("激活码错误").put("ok",false);
        }
        String deleteTime = System.currentTimeMillis() / 1000 + "";
        String sqlStr = "update activecode set deleteTime="+deleteTime+" where code=\""+code+"\" and deleteTime = 0";

        int count = loginDao.executeUpdate(sqlStr);
        if (count < 0) {
            return AjaxResult.info("操作失败").put("ok",false);
        }
        GMLogUtil.log("禁用激活码:"+code);
        return AjaxResult.info("操作完成").put("ok",true);
    }

    /**
     * 生成激活码操作
     * @param codeNum
     * @param activeCode
     * @param selectServerIdList
     * @param request
     * @param response
     * @throws SQLException
     */
    @PostMapping( "/activecode")
    @ResponseBody
    public void activecode(int codeNum, ActiveCode activeCode, String[] selectServerIdList,
                           HttpServletRequest request, HttpServletResponse response) throws SQLException {
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        String activeName = activeCode.getActiveName();
        String items = activeCode.getItemList();
        if (StringUtils.isBlank(items)) {
            return;
        }
        int param = activeCode.getParam();
        Date valide_time_begin = null;
        Date valide_time_end = null;
        try {
            valide_time_begin = sdfhm.parse(activeCode.getValide_time_begin());
            valide_time_end = sdfhm.parse(activeCode.getValide_time_end());
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        if (!ItemManager.getInstance().checkItems(activeCode.getItemList())) {
            return;
        }

        // 为“0”表示所有渠道
        String channel = activeCode.getPlateform_name_big();
        String valide_server_id_list = "[]";
        if (selectServerIdList != null) {
            valide_server_id_list = Arrays.toString(selectServerIdList);
        }

        int batchId = 0;
        List<String> codes;

        if (param == 1) {
            String codeSql = "select code from activecode where param = 1";
            List<String> list = loginDao.selectList(codeSql, (Class<String>) null);
            if (list != null && list.contains(activeCode.getCode())) {
                return;
            }
            codes = Collections.singletonList(activeCode.getCode());
        } else {
            //生成激活码
            try {
                batchId = codeBatchService.selectMaxId() + 1;
            }catch (Exception e){
                batchId = batchId + 1;
            }
            // 判断是否已经拥有批号
            if (codeBatchService.selectBatchId(batchId) > 0) {
                batchId += 1;
            }
            // 使用set集合去除重复激活码
            Set<String> codeSet = new HashSet<>();
            while (codeSet.size() < codeNum) {
                codeSet.add(RandomUtil.sg(5).next() + batchId);
            }
            codes = new ArrayList<>(codeSet);
        }

        //激活码存入各自平台的LS库中
        long createTime = System.currentTimeMillis() / 1000;
        // 分批存入数据库
        int batchSize = 1000;//每一批上限，因为国内app(rds云数据库)插入有问题，减少成1000
        String sqlStr = "INSERT INTO activecode (code, activeName, batch, itemList,param,valide_time_begin," +
                "valide_time_end,plateform_name_big,valide_server_id_list,create_time,deleteTime) " +
                "VALUES (?, ?, ?, ?,?,?,?,?,?,?,?);";
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try{
            if (codeNum > batchSize) {
                int k = 0;// 用于判断插入剩余数据
                connection = loginDao.getConnection();
                preparedStatement = connection.prepareStatement(sqlStr);
                for (int i = 0; i < codeNum; i++) {
                    preparedStatement.setString(1,codes.get(i));
                    preparedStatement.setString(2,activeName);
                    preparedStatement.setInt(3,batchId);
                    preparedStatement.setObject(4,items);
                    preparedStatement.setInt(5,param);
                    preparedStatement.setObject(6,valide_time_begin);
                    preparedStatement.setObject(7,valide_time_end);
                    preparedStatement.setString(8,channel);
                    preparedStatement.setString(9,valide_server_id_list);
                    preparedStatement.setObject(10,createTime);
                    preparedStatement.setInt(11,0);
                    preparedStatement.addBatch();

                    // batchSize条插入一次
                    if (i % batchSize == 0) {
                        preparedStatement.executeBatch();
                        if (preparedStatement.getUpdateCount() < 1) {
                            log.error("数据库操作失败");
                        }
                        preparedStatement.clearBatch();// 清理已操作批次
                        k = i;
                    }
                }
                if (k < codeNum - 1) {
                    // 最后插入不足batchSize条的数据
                    preparedStatement.executeBatch();
                    if (preparedStatement.getUpdateCount() < 1) {
                        log.error("数据库操作失败");
                    }
                }
            } else {
                connection = loginDao.getConnection();
                preparedStatement = connection.prepareStatement(sqlStr);
                for (int i = 0; i < codeNum; i++) {
                    preparedStatement.setString(1,codes.get(i));
                    preparedStatement.setString(2,activeName);
                    preparedStatement.setInt(3,batchId);
                    preparedStatement.setObject(4,items);
                    preparedStatement.setInt(5,param);
                    preparedStatement.setObject(6,valide_time_begin);
                    preparedStatement.setObject(7,valide_time_end);
                    preparedStatement.setString(8,channel);
                    preparedStatement.setString(9,valide_server_id_list);
                    preparedStatement.setObject(10,createTime);
                    preparedStatement.setInt(11,0);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                if (preparedStatement.getUpdateCount() < 1) {
                    log.error("数据库操作失败");
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
        }
        log.error("激活码存入登录服数据库成功，平台标识 :" + activeCode.getPlateform_name_big() + ",批次号:" + batchId + ",数量:" + codeNum);

        // 保存批号记录
        if (batchId != 0) {
            User user = ShiroUtils.getSysUser();
            CodeBatch codeBatch = new CodeBatch();
            codeBatch.setBatchId(batchId);
            codeBatch.setUserId(user.getUserId());
            codeBatch.setTime(System.currentTimeMillis());
//            codeBatch.setPlatform(groupName);
            codeBatch.setIsUniversal(param);
            int insert = codeBatchService.insertCodeBatch(codeBatch);
            if (insert < 1) {
                log.error("批次号为" + batchId + "的激活码信息录入GM后台失败！");
            } else {
                log.info("批次号为" + batchId + "的激活码信息录入GM后台成功！");
            }
            GMLogUtil.log("生成激活码[平台:" + activeCode.getPlateform_name_big() + "\t批号:" + batchId + "\t数量:" + codeNum + "\t万能码：" + (param == 1) + "]");
        }

        try (OutputStream out = response.getOutputStream()) {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String excelName = "激活码";
            // 转码防止乱码
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes(UTF_8), "ISO8859-1") + ".xlsx");
            genExcel(codes, batchId, out);
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void genExcel(List<String> rows, int batchId, OutputStream out) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("批次号" + "_" + batchId);
        String[] header = {"激活码"};
        // 创建表头
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell;
        for (int ci = 0; ci < header.length; ci++) {
            cell = row.createCell(ci);
            cell.setCellValue(header[ci]);
        }
        // 创建数据
        for (int ri = 0; ri < rows.size(); ri++) {
            row = sheet.createRow(ri + 1);
            cell = row.createCell(0);
            cell.setCellValue(rows.get(ri));
        }
        workbook.write(out);
    }

    /**
     * 检测礼包码可用性
     * @param code
     * @return
     */
    @PostMapping( "/checkActiveCode")
    @ResponseBody
    public Object checkActiveCode(String code) {
        DBClient loginDao = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.LOGIN);
        String codeSql = "select code from activecode where param = 1";
        List<String> list = loginDao.selectList(codeSql, (Class<String>) null);
        boolean canUse = true;
        if (list != null && list.contains(code)) {
            canUse = false;
        }
        return AjaxResult.info("").put("ok",canUse);
    }
}
