package com.backend.module.admin;

import com.backend.bean.*;
import com.backend.filter.MenuFilter;
import com.backend.manager.*;
import com.backend.struct.Questionnaire;
import com.backend.struct.backEndShowGrid;
import com.backend.utils.*;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@At("/admin")
@Ok("json")
@Fail("http:500")
public class AdminModule {

    private static final Logger log = Logger.getLogger(AdminModule.class);
    private DateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Inject
    protected Dao dao;

    @At
    @Ok("jsp:jsp.admin.user.backendlog")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void backendlog(HttpServletRequest request) {
        request.setAttribute("nowDate", ymdhms.format(new Date()));
    }

    @At
    @Ok("jsp:jsp.admin.data.dataload")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void data() {
    }

    @At
    @Ok("json")
    public JSON getBackendLog(@Param("id") int id, @Param("content") String content, @Param("page") int page,@Param("rows") int rows,
                                @Param("startDate") String startDate, @Param("endDate") String endDate) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        if ((null == startDate || "".equals(startDate))&&(null == endDate || "".equals(endDate))){
            return null;
        }
        try {
            start.setTime(ymdhms.parse(startDate));
            end.setTime(ymdhms.parse(endDate));
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        QueryResult qr = new QueryResult();
        List<BackendLog> backendLogs = null;
        Cnd cnd = null;
        int count = 0;
        cnd = Cnd.where("time", ">", start.getTimeInMillis()).and("time", "<", end.getTimeInMillis());
        if (id == 0 && Strings.isBlank(content)){
            count = dao.count(BackendLog.class, cnd);
            backendLogs = dao.query(BackendLog.class,cnd);
        }else if (id != 0 && Strings.isBlank(content)){
            cnd.and("userId", "=", id);
            backendLogs = dao.query(BackendLog.class, cnd);
            if (null != backendLogs){
                count = backendLogs.size();
            }
        }else if (id == 0 && !Strings.isBlank(content)){
            cnd.and("content", "like", "%" + content + "%");
            backendLogs = dao.query(BackendLog.class, cnd);
            if (null != backendLogs){
                count = backendLogs.size();
            }
        }else if (id != 0 && !Strings.isBlank(content)) {
            cnd.and("userId", "=", id).and("content", "like", "%" + content + "%");
            backendLogs = dao.query(BackendLog.class, cnd);
            if (null != backendLogs){
                count = backendLogs.size();
            }
        }
        HashMap<String, Object> rs = new HashMap<String, Object>();
        rs.put("total", count);
        List<backEndShowGrid> gridList = new ArrayList<>(count);
        if (null != backendLogs){
            for (BackendLog backendLog:backendLogs){
                backEndShowGrid grid = new backEndShowGrid();
                grid.setId(backendLog.getId());
                grid.setUserId(backendLog.getUserId());
                grid.setUserName(backendLog.getUserName());
                grid.setContent(backendLog.getContent());
                grid.setTime(TimeUtils.format2string(backendLog.getTime()));
                grid.setIp(backendLog.getIp());
                gridList.add(grid);

            }
            int fromIndex = rows*(page - 1);
            int toIndex = rows*page >= gridList.size() ? gridList.size() : rows*page;
            rs.put("total", gridList.size());
            rs.put("rows", gridList.subList(fromIndex,toIndex));
        }else {
            rs.put("rows", null);
        }

        JSONObject json = JSONObject.fromObject(rs);
        return (JSON)json;
    }

    @At
    public Object reloadAllConfigFile(HttpServletRequest request) {
        reloadCurrencyRateConfig(request);
        reloadServerLanguageConfig(request);
        return Toolkit.outResult(true);
    }


    @At
    public Object reloadCurrencyRateConfig(HttpServletRequest request) {
        CurrencyRateManager.getInstance().load();
        BackendLogUtil.getInstance().log(request, "reloadCurrencyRateConfig");
        return Toolkit.outResult(true);
    }

    @At
    public Object reloadServerLanguageConfig(HttpServletRequest request) {
//        ItemManager.getInstance().loadServerLanguageConfig();
        BackendLogUtil.getInstance().log(request, "reloadServerLanguageConfig");
        return Toolkit.outResult(true);
    }

    @At
    @Filters()
    public Object reloadAllServer(HttpServletRequest request) {
        LoginServerManager.getInstance().loadAll();
        DbLogListManager.getInstance().reloadAll();
        ServerListManager.getInstance().load();
        CrossManager.getInstance().reloadAll();
        BackendLogUtil.getInstance().log(request, "reloadAllServer");
        return Toolkit.outResult(true);
    }

    @At
    public Object reloadLoginServer(HttpServletRequest request) {
        LoginServerManager.getInstance().loadAll();
        BackendLogUtil.getInstance().log(request, "reloadLoginServer");
        return Toolkit.outResult(true);
    }

    @At
    public Object reloadPlatFormServerInfo(HttpServletRequest request) {
        DbLogListManager.getInstance().reloadAll();
        BackendLogUtil.getInstance().log(request, "reloadGameLog");
        return Toolkit.outResult(true);
    }

    @At
    public Object reloadServerList(HttpServletRequest request) {
        ServerListManager.getInstance().load();
        BackendLogUtil.getInstance().log(request, "reloadGameWorld");
        return Toolkit.outResult(true);
    }

    @At
    public Object reloadPublic(HttpServletRequest request) {
        CrossManager.getInstance().reloadAll();
        BackendLogUtil.getInstance().log(request, "reloadPublic");
        return Toolkit.outResult(true);
    }

    @At
    public Object reloadAnnouce(HttpServletRequest request) {
        CyAnnounceManager.getInstance().load();
        BackendLogUtil.getInstance().log(request, "reloadAnnouce");
        return Toolkit.outResult(true);
    }

    @At
    public Object reloadBlackList(HttpServletRequest request) {
        BlackListManager.getInstance().loadData();
        BackendLogUtil.getInstance().log(request, "reloadBlackList");
        return Toolkit.outResult(true);
    }

    @At
    @POST
    public Object getAllItem() {
        List<Item> list = new ArrayList<>(ItemManager.getInstance().getItemList().values());
        return Toolkit.outResult(true, list);
    }

    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"${app.root}/WEB-INF/tmp","8192", "UTF-8", "2000", "10240000"})
    public Object loadItem(@Param("itemFile") TempFile itemFile){
        if (itemFile == null) {
            return Toolkit.outResult(false, "file is null!");
        }
        String fileName = itemFile.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Toolkit.outResult(false, "file type error!");
        }
        List<Item> items = new ArrayList<>();
        int[] itemInfoPos = new int[4];
        try {
            FileInputStream in = new FileInputStream(itemFile.getFile());
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            boolean isEquipItem = sheet.getSheetName().equalsIgnoreCase("equip");
            if (!sheet.getSheetName().equalsIgnoreCase("item") && !isEquipItem) {
                return Toolkit.outResult(false, "not item or equip config file");
            }
            XSSFRow row = sheet.getRow(1);
            int cellNum = row.getLastCellNum();
            for (int i =0; i < cellNum; i++) {
                if (row.getCell(i).toString().equalsIgnoreCase("id")) {
                    itemInfoPos[0] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("name")) {
                    itemInfoPos[1] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("type")) {
                    itemInfoPos[2] = i;
                }
                if (isEquipItem) {
                    if (row.getCell(i).toString().equalsIgnoreCase("quality")) {
                        itemInfoPos[3] = i;
                    }
                } else {
                    if (row.getCell(i).toString().equalsIgnoreCase("color")) {
                        itemInfoPos[3] = i;
                    }
                }
            }
            for (int j = 5; j <= sheet.getLastRowNum(); j++) {
                XSSFRow dataRow = sheet.getRow(j);
                if (dataRow.getCell(itemInfoPos[0]) == null || dataRow.getCell(itemInfoPos[0]).toString().equals("")) {
                    break;
                }
                Item item = new Item();
                item.setItemId((int) Float.parseFloat(dataRow.getCell(itemInfoPos[0]).toString()));
                item.setItemName(dataRow.getCell(itemInfoPos[1]).toString());
                if (!isEquipItem) {
                    item.setItemType((int) Float.parseFloat(dataRow.getCell(itemInfoPos[2]).toString()));
                } else {
                    item.setItemType(0);
                }
                item.setColor((int) Float.parseFloat(dataRow.getCell(itemInfoPos[3]).toString()));
                items.add(item);
            }
            if (items.size() >0) {
                if (!isEquipItem) {
                    dao.clear(Item.class);
                }
                List<Item> result = dao.insert(items);
                ItemManager.getInstance().loadData();
                return Toolkit.outResult(true, "Reload item file, saved " + result.size() + " record!");
            }
            return Toolkit.outResult(false, "Reload item file failed!");
        } catch (IOException e) {
            e.printStackTrace();
            return Toolkit.outResult(false, e.getMessage());
        }
    }

    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:upload"})
    public Object loadFunction(@Param("functionFile") TempFile functionFile){
        if (functionFile == null) {
            return Toolkit.outResult(false, "file is null!");
        }
        String fileName = functionFile.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Toolkit.outResult(false, "file type error!");
        }
        List<Function> functions = new ArrayList<>();
        int[] functionInfoPos = new int[3];
        try {
            FileInputStream in = new FileInputStream(functionFile.getFile());
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("functionStart")) {
                return Toolkit.outResult(false, "not functionStart config file");
            }
            XSSFRow row = sheet.getRow(1);
            int cellNum = row.getLastCellNum();
            for (int i =0; i < cellNum; i++) {
                if (row.getCell(i).toString().equalsIgnoreCase("function_id")) {
                    functionInfoPos[0] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("id_code")) {
                    functionInfoPos[1] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("parent_id")) {
                    functionInfoPos[2] = i;
                }
            }
            for (int j = 5; j <= sheet.getLastRowNum(); j++) {
                XSSFRow dataRow = sheet.getRow(j);
                if (dataRow.getCell(functionInfoPos[0]) == null || dataRow.getCell(functionInfoPos[0]).toString().equals("")) {
                    break;
                }
                Function function = new Function();
                function.setFuncId((int) Float.parseFloat(dataRow.getCell(functionInfoPos[0]).toString()));
                function.setFuncName(dataRow.getCell(functionInfoPos[1]).toString().split(";")[1]);
                if (dataRow.getCell(functionInfoPos[2]) != null && !Strings.isBlank(dataRow.getCell(functionInfoPos[2]).toString())) {
                    function.setParentId((int) Float.parseFloat(dataRow.getCell(functionInfoPos[2]).toString()));
                }
                functions.add(function);
            }
            if (functions.size() >0) {
                dao.clear(Function.class);
                List<Function> result = dao.insert(functions);
                return Toolkit.outResult(true, "Reload functionStar file, saved " + result.size() + " record!");
            }
            return Toolkit.outResult(false, "Reload functionStar file failed!");
        } catch (IOException e) {
            e.printStackTrace();
            return Toolkit.outResult(false, e.getMessage());
        }
    }

    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:upload"})
    public Object loadChangeReason(@Param("reasonFile") TempFile reasonFile){
        if (reasonFile == null) {
            return Toolkit.outResult(false, "file is null!");
        }
        String fileName = reasonFile.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Toolkit.outResult(false, "file type error!");
        }
        List<ChangeReason> reasons = new ArrayList<>();
        int[] changeReasonInfoPos = new int[2];
        try {
            FileInputStream in = new FileInputStream(reasonFile.getFile());
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("itemChangeReason")) {
                return Toolkit.outResult(false, "not itemChangeReason config file");
            }
            XSSFRow row = sheet.getRow(1);
            int cellNum = row.getLastCellNum();
            for (int i =0; i < cellNum; i++) {
                if (row.getCell(i).toString().equalsIgnoreCase("id")) {
                    changeReasonInfoPos[0] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("name")) {
                    changeReasonInfoPos[1] = i;
                }
            }
            for (int j = 5; j <= sheet.getLastRowNum(); j++) {
                XSSFRow dataRow = sheet.getRow(j);
                if (dataRow.getCell(changeReasonInfoPos[0]) == null || dataRow.getCell(changeReasonInfoPos[0]).toString().equals("")) {
                    break;
                }
                ChangeReason reason = new ChangeReason();
                reason.setId((int) Float.parseFloat(dataRow.getCell(changeReasonInfoPos[0]).toString()));
                reason.setName(dataRow.getCell(changeReasonInfoPos[1]).toString().split(";")[1]);
                reasons.add(reason);
            }
            if (reasons.size() > 0) {
                dao.clear(Function.class);
                List<ChangeReason> result = dao.insert(reasons);
                ReasonManager.getInstance().loadReasons();
                return Toolkit.outResult(true, "Reload itemChangeReason File, saved " + result.size() + " record!");
            }
            return Toolkit.outResult(false, "Reload itemChangeReason file failed!");
        } catch (IOException e) {
            e.printStackTrace();
            return Toolkit.outResult(false, e.getMessage());
        }
    }


    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:upload"})
    public Object loadQuestionnaire(@Param("questionnaireFile") TempFile questionnaireFile){
        if (questionnaireFile == null) {
            return Toolkit.outResult(false, "file is null!");
        }
        String fileName = questionnaireFile.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Toolkit.outResult(false, "file type error!");
        }
        int[] questionnairePos = new int[4];
        try {
            FileInputStream in = new FileInputStream(questionnaireFile.getFile());
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("question")) {
                return Toolkit.outResult(false, "not question config file");
            }
            XSSFRow row = sheet.getRow(1);
            int cellNum = row.getLastCellNum();
            for (int i =0; i < cellNum; i++) {
                if (row.getCell(i).toString().equalsIgnoreCase("id")) {
                    questionnairePos[0] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("questContent")) {
                    questionnairePos[1] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("questType")) {
                    questionnairePos[2] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("answerOption")) {
                    questionnairePos[3] = i;
                }
            }
            OtherDataManager.getInstance().getQuestionnaireMap().clear();
            for (int j = 5; j <= sheet.getLastRowNum(); j++) {
                XSSFRow dataRow = sheet.getRow(j);
                Questionnaire questionnaire = new Questionnaire();
                questionnaire.setId((int) Float.parseFloat(dataRow.getCell(questionnairePos[0]).toString()));
                questionnaire.setQuestion(dataRow.getCell(questionnairePos[1]).toString());
                questionnaire.setType((int) Float.parseFloat(dataRow.getCell(questionnairePos[2]).toString()));
                String answerStr = dataRow.getCell(questionnairePos[3]).toString();
                String[] answerArray = answerStr.split("_");
                for (String value : answerArray) {
                    questionnaire.getAnswers().add(value);
                }
                OtherDataManager.getInstance().getQuestionnaireMap().put(questionnaire.getId(), questionnaire);
            }
            return Toolkit.outResult(true, "Reload questionnaire file! size" + OtherDataManager.getInstance().getQuestionnaireMap().size());
        } catch (IOException e) {
            e.printStackTrace();
            return Toolkit.outResult(false, e.getMessage());
        }
    }

    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:upload"})
    public Object loadActivityBossType(@Param("activityBossTypeFile") TempFile activityBossTypeFile){
        if (activityBossTypeFile == null) {
            return Toolkit.outResult(false, "file is null!");
        }
        String fileName = activityBossTypeFile.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Toolkit.outResult(false, "file type error!");
        }
        List<ActivityBossType> activitys = new ArrayList<>();
        int[] activityBossTypePos = new int[3];
        try {
            FileInputStream in = new FileInputStream(activityBossTypeFile.getFile());
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("activity_boss_type")) {
                return Toolkit.outResult(false, "not activity_boss_type config file");
            }
            XSSFRow row = sheet.getRow(1);
            int cellNum = row.getLastCellNum();
            for (int i =0; i < cellNum; i++) {
                if (row.getCell(i).toString().equalsIgnoreCase("id")) {
                    activityBossTypePos[0] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("name")) {
                    activityBossTypePos[1] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("boss_id")) {
                    activityBossTypePos[2] = i;
                }
            }
            for (int j = 5; j <= sheet.getLastRowNum(); j++) {
                XSSFRow dataRow = sheet.getRow(j);
                if (dataRow.getCell(activityBossTypePos[0]) == null || dataRow.getCell(activityBossTypePos[0]).toString().equals("")) {
                    break;
                }
                ActivityBossType activityBossType = new ActivityBossType();
                activityBossType.setId((int) Float.parseFloat(dataRow.getCell(activityBossTypePos[0]).toString()));
                activityBossType.setName(dataRow.getCell(activityBossTypePos[1]).toString());
                activityBossType.setBoss_id(dataRow.getCell(activityBossTypePos[2]).toString());
                activitys.add(activityBossType);
            }
            if (activitys.size() >0) {
                dao.clear(ActivityBossType.class);
                List<ActivityBossType> result = dao.insert(activitys);

                return Toolkit.outResult(true, "Reload activity_boss_type file OK, saved " + result.size() + " record!");
            }
            return Toolkit.outResult(false, "Reload activity_boss_type file failed!");
        } catch (IOException e) {
            e.printStackTrace();
            return Toolkit.outResult(false, e.getMessage());
        }
    }

    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:upload"})
    public Object loadActivityFestivalType(@Param("activityFestivalTypeFile") TempFile activityFestivalTypeFile){
        if (activityFestivalTypeFile == null) {
            return Toolkit.outResult(false, "file is null!");
        }
        String fileName = activityFestivalTypeFile.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return Toolkit.outResult(false, "file type error!");
        }
        List<ActivityFestivalType> activitys = new ArrayList<>();
        int[] activityFestivalTypePos = new int[3];
        try {
            FileInputStream in = new FileInputStream(activityFestivalTypeFile.getFile());
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("activity_festival")) {
                return Toolkit.outResult(false, "not activity_festival config file");
            }
            XSSFRow row = sheet.getRow(1);
            int cellNum = row.getLastCellNum();
            for (int i =0; i < cellNum; i++) {
                if (row.getCell(i).toString().equalsIgnoreCase("id")) {
                    activityFestivalTypePos[0] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("f_name")) {
                    activityFestivalTypePos[1] = i;
                }
            }
            for (int j = 5; j <= sheet.getLastRowNum(); j++) {
                XSSFRow dataRow = sheet.getRow(j);
                if (dataRow.getCell(activityFestivalTypePos[0]) == null || dataRow.getCell(activityFestivalTypePos[0]).toString().equals("")) {
                    break;
                }
                ActivityFestivalType activityFestivalType = new ActivityFestivalType();
                activityFestivalType.setId((int) Float.parseFloat(dataRow.getCell(activityFestivalTypePos[0]).toString()));
                activityFestivalType.setName(dataRow.getCell(activityFestivalTypePos[1]).toString());
                activitys.add(activityFestivalType);
            }
            if (activitys.size()>0) {
                dao.clear(ActivityFestivalType.class);
                List<ActivityFestivalType> result = dao.insert(activitys);

                return Toolkit.outResult(true, "Reload activity_festival file OK, saved " + result.size() + " record!");
            }
            return Toolkit.outResult(false, "Reload activity_festival file failed!");
        } catch (IOException e) {
            e.printStackTrace();
            return Toolkit.outResult(false, e.getMessage());
        }
    }

}
