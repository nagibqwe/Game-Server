package com.gm.project.gmtool.backenddataload;

import com.gm.common.utils.StringUtils;
import com.gm.common.utils.text.Convert;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.project.gmtool.activityBossType.domain.ActivityBossType;
import com.gm.project.gmtool.activityBossType.service.IActivityBossTypeService;
import com.gm.project.gmtool.activityFestivalRelation.domain.ActivityFestivalRelation;
import com.gm.project.gmtool.activityFestivalRelation.service.IActivityFestivalRelationService;
import com.gm.project.gmtool.activityFestivalType.domain.ActivityFestivalType;
import com.gm.project.gmtool.activityFestivalType.service.IActivityFestivalTypeService;
import com.gm.project.gmtool.changereason.domain.TChangereason;
import com.gm.project.gmtool.changereason.service.ITChangereasonService;
import com.gm.project.gmtool.function.domain.Function;
import com.gm.project.gmtool.function.service.IFunctionService;
import com.gm.project.gmtool.item.domain.Item;
import com.gm.project.gmtool.item.service.IItemService;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.gmtool.manager.CyAnnounceManager;
import com.gm.project.gmtool.manager.ItemManager;
import com.gm.project.gmtool.utils.GMLogUtil;
import com.gm.project.monitor.server.domain.Sys;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/gmtool/backenddataload")
public class BackendDataLoadController extends BaseController {

    private String prefix = "gmtool/backenddataload";

    @Autowired
    private IItemService itemService;

    @Autowired
    private IFunctionService functionService;

    @Autowired
    private IActivityBossTypeService activityBossTypeService;

    @Autowired
    private IActivityFestivalTypeService activityFestivalTypeService;

    @Autowired
    private IActivityFestivalRelationService activityFestivalRelationService;


    @Autowired
    private ITChangereasonService iTChangereasonService;

    @RequiresPermissions("gmtool:backenddataload:view")
    @GetMapping()
    public String dataload()
    {
        return prefix + "/backenddataload";
    }

    /**
     * 公告数据加载
     * @param request
     * @return
     */
    @PostMapping("/reloadAnnouce")
    @ResponseBody
    public Object reloadAnnouce(HttpServletRequest request) {
        CyAnnounceManager.getInstance().load();
        GMLogUtil.log("reloadAnnouce");
        return AjaxResult.info("").put("ok",true);
    }

    /**
     * 黑名单数据加载
     * @param request
     * @return
     */
    @PostMapping("/reloadBlackList")
    @ResponseBody
    public Object reloadBlackList(HttpServletRequest request) {
        BlackListManager.getInstance().loadData();
        GMLogUtil.log("reloadBlackList");
        return AjaxResult.info("").put("ok",true);
    }

    /**
     * 获取所有物品装备
     * @return
     */
    @PostMapping("/getAllItem")
    @ResponseBody
    public Object getAllItem() {
        List<Item> list = new ArrayList<>(ItemManager.getInstance().getItemList().values());
        return AjaxResult.success("",list).put("ok",true);
    }

    /**
     *物品装备重新加载
     */
    @PostMapping("/loadItem")
    @ResponseBody
    public Object loadItem(MultipartFile itemFile){
        if (itemFile == null) {
            return AjaxResult.info("file is null!").put("ok",false);
        }
        String fileName = itemFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx")) {
            return AjaxResult.info("file type error!").put("ok",false);
        }
        List<Item> items = new ArrayList<>();
        int[] itemInfoPos = new int[4];
        try {
            InputStream in = itemFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            boolean isEquipItem = sheet.getSheetName().equalsIgnoreCase("equip");
            if (!sheet.getSheetName().equalsIgnoreCase("item") && !isEquipItem) {
                return AjaxResult.info("not item or equip config file").put("ok",false);
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
                    itemService.clearItem("t_item");
                }

                List<Item> result = new ArrayList<>();
                for (Item item:items){
                    itemService.insertItem(item);
                    result.add(item);
                }
                ItemManager.getInstance().loadData();
                return AjaxResult.success("Reload item file, saved" + result.size() + " record!").put("ok",true);
            }
            return AjaxResult.info("Reload item file failed!").put("ok",false);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
    }

    /**
     * 功能开关重新加载
     */
    @PostMapping("/loadFunction")
    @ResponseBody
    public Object loadFunction(MultipartFile functionFile){
        if (functionFile == null) {
            return AjaxResult.info("file is null!").put("ok",false);
        }

        String fileName = functionFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx")) {
            return AjaxResult.info("file type error!").put("ok",false);
        }
        List<Function> functions = new ArrayList<>();
        int[] functionInfoPos = new int[3];
        try {
            InputStream in = functionFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            boolean isEquipItem = sheet.getSheetName().equalsIgnoreCase("equip");
            if (!sheet.getSheetName().equalsIgnoreCase("functionStart") && !isEquipItem) {
                return AjaxResult.info("not functionStart config file").put("ok",false);
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
                if (dataRow.getCell(functionInfoPos[2]) != null && !StringUtils.isBlank(dataRow.getCell(functionInfoPos[2]).toString())) {
                    function.setParentId((int) Float.parseFloat(dataRow.getCell(functionInfoPos[2]).toString()));
                }
                functions.add(function);
            }
            if (functions.size() >0) {
                functionService.deleteAllFunctions();
                List<Function> result = new ArrayList<>();
                for (Function function:functions){
                    functionService.insertFunction(function);
                    result.add(function);
                }
                return AjaxResult.success("Reload functionStar file, saved " + result.size() + " record!").put("ok", true);
            }
            return AjaxResult.info("Reload functionStar file failed!");
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
    }

    /**
     * 运营活动boss类型重新加载
     * @param activityBossTypeFile
     * @return
     */
    @PostMapping("/loadActivityBossType")
    @ResponseBody
    public Object loadActivityBossType(MultipartFile activityBossTypeFile){
        if (activityBossTypeFile == null) {
            return AjaxResult.info("file is null!").put("ok",false);
        }
        String fileName = activityBossTypeFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx")) {
            return AjaxResult.info("file type error!").put("ok",false);
        }
        List<ActivityBossType> activitys = new ArrayList<>();
        int[] activityBossTypePos = new int[3];
        try {
            InputStream in = activityBossTypeFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("activity_boss_type")) {
                return AjaxResult.info("not activity_boss_type config file").put("ok",false);
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
                activityBossType.setBossId(dataRow.getCell(activityBossTypePos[2]).toString());
                activitys.add(activityBossType);
            }
            if (activitys.size() >0) {

                activityBossTypeService.deleteAllActBossType();
                List<ActivityBossType> result = new ArrayList<>();
                for (ActivityBossType activityBossType:activitys){
                    activityBossTypeService.insertActivityBossType(activityBossType);
                    result.add(activityBossType);
                }

                return AjaxResult.success("Reload activity_boss_type file OK, saved " + result.size() + " record!").put("ok",true);
            }
            return AjaxResult.info("Reload activity_boss_type file failed!").put("ok",false);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
    }

    /**
     * 运营活动节日类型重新加载
     * @param activityFestivalTypeFile
     * @return
     */
    @PostMapping("/loadActivityFestivalType")
    @ResponseBody
    public Object loadActivityFestivalType(MultipartFile activityFestivalTypeFile){
        if (activityFestivalTypeFile == null) {
            return AjaxResult.info("file is null!").put("ok",false);
        }
        String fileName = activityFestivalTypeFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx")) {
            return AjaxResult.info("file type error!").put("ok",false);
        }
        List<ActivityFestivalType> activitys = new ArrayList<>();
        int[] activityFestivalTypePos = new int[3];
        try {
            InputStream in = activityFestivalTypeFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("activity_festival")) {
                return AjaxResult.info("not activity_festival config file").put("ok",false);
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
                activityFestivalTypeService.deleteAllActFestivalType();
                List<ActivityFestivalType> result = new ArrayList<>();
                for (ActivityFestivalType activityFestivalType:activitys){
                    activityFestivalTypeService.insertActivityFestivalType(activityFestivalType);
                    result.add(activityFestivalType);
                }

                return AjaxResult.success("Reload activity_festival file OK, saved " + result.size() + " record!").put("ok",true);
            }
            return AjaxResult.info("Reload activity_festival file failed!").put("ok",false);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
    }

    /**
     * 运营活动节日关系加载
     * @param activityFestivalRelationFile
     * @return
     */
    @PostMapping("/loadActivityFestivalRelation")
    @ResponseBody
    public Object loadActivityFestivalRelation(MultipartFile activityFestivalRelationFile){
        if (activityFestivalRelationFile == null) {
            return AjaxResult.info("file is null!").put("ok",false);
        }
        String fileName = activityFestivalRelationFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx")) {
            return AjaxResult.info("file type error!").put("ok",false);
        }
        List<ActivityFestivalRelation> activitys = new ArrayList<>();
        int[] activityFestivalRelationPos = new int[3];
        try {
            InputStream in = activityFestivalRelationFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("activity_yunying")) {
                return AjaxResult.info("not activity_yunying config file").put("ok",false);
            }
            XSSFRow row = sheet.getRow(1);
            int cellNum = row.getLastCellNum();
            for (int i =0; i < cellNum; i++) {
                if (row.getCell(i).toString().equalsIgnoreCase("logic_id")) {
                    activityFestivalRelationPos[0] = i;
                }
                if (row.getCell(i).toString().equalsIgnoreCase("festival_id")) {
                    activityFestivalRelationPos[1] = i;
                }
            }
            for (int j = 5; j <= sheet.getLastRowNum(); j++) {
                XSSFRow dataRow = sheet.getRow(j);
                if (dataRow.getCell(activityFestivalRelationPos[0]) == null || dataRow.getCell(activityFestivalRelationPos[0]).toString().equals("")) {
                    break;
                }
                ActivityFestivalRelation activityFestivalRelation = new ActivityFestivalRelation();
                activityFestivalRelation.setLogicId((int) Float.parseFloat(dataRow.getCell(activityFestivalRelationPos[0]).toString()));
                activityFestivalRelation.setFestivalId((int) Float.parseFloat(dataRow.getCell(activityFestivalRelationPos[1]).toString()));
                activitys.add(activityFestivalRelation);
            }
            if (activitys.size()>0) {
                activityFestivalRelationService.deleteAllActFestivalRelation();
                List<ActivityFestivalRelation> result = new ArrayList<>();
                for (ActivityFestivalRelation activityFestivalRelation:activitys){
                    activityFestivalRelationService.insertActivityFestivalRelation(activityFestivalRelation);
                    result.add(activityFestivalRelation);
                }

                return AjaxResult.success("Reload activity_yunying file OK, saved " + result.size() + " record!").put("ok",true);
            }
            return AjaxResult.info("Reload activity_yunying file failed!").put("ok",false);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
    }

    /**
     * 获取单元格值
     *
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column)
    {
        if (row == null)
        {
            return row;
        }
        Object val = "";
        try
        {
            Cell cell = row.getCell(column);
            if (StringUtils.isNotNull(cell))
            {
                if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA)
                {
                    val = cell.getNumericCellValue();
                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
                    }
                    else
                    {
                        if ((Double) val % 1 != 0)
                        {
                            val = new BigDecimal(val.toString());
                        }
                        else
                        {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                }
                else if (cell.getCellType() == CellType.STRING)
                {
                    val = cell.getStringCellValue();
                }
                else if (cell.getCellType() == CellType.BOOLEAN)
                {
                    val = cell.getBooleanCellValue();
                }
                else if (cell.getCellType() == CellType.ERROR)
                {
                    val = cell.getErrorCellValue();
                }

            }
        }
        catch (Exception e)
        {
            return val;
        }
        return val;
    }
    /**
     * 运营活动节日关系加载
     * @return
     */
    @PostMapping("/loadChangeReason")
    @ResponseBody
    public Object loadChangeReason(MultipartFile changeReasonFile){
        if (changeReasonFile == null) {
            return AjaxResult.info("file is null!").put("ok",false);
        }
        String fileName = changeReasonFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx")) {
            return AjaxResult.info("file type error!").put("ok",false);
        }
         List<TChangereason> reasons = new ArrayList<>();
         int[] changeReasonInfoPos = new int[2];
        try {

            InputStream in = changeReasonFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(in);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equalsIgnoreCase("ItemChangeReason")) {
                return AjaxResult.info("not ItemChangeReason config file").put("ok",false);
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
                TChangereason reason = new TChangereason();
//                if(dataRow.getCell(changeReasonInfoPos[0]).toString().equals("100900002")){
//                    System.out.println("xxxxxxxxxxxxx");
//                }
               Object obj = getCellValue(dataRow,changeReasonInfoPos[0]);
                String s = Convert.toStr(obj);
                String val = "";
                if (StringUtils.endsWith(s, ".0"))
                {
                    val = StringUtils.substringBefore(s, ".0");
                }else {
                    val = s;
                }
//                System.out.println(val);
//
                reason.setId(Long.parseLong(val));
                reason.setName(dataRow.getCell(changeReasonInfoPos[1]).toString().split(";")[1]);
                reasons.add(reason);
            }
            if (reasons.size() > 0) {
                iTChangereasonService.deleteAllTChangereason();
                List<TChangereason> result = new ArrayList<>();
                for (TChangereason tChangereason:reasons){
                    iTChangereasonService.insertTChangereason(tChangereason);
                    result.add(tChangereason);
                }
                iTChangereasonService.loadData();

                return AjaxResult.success("Reload ItemChangeReason file OK, saved " + result.size() + " record!").put("ok",true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.info(e.getMessage()).put("ok",false);
        }
        return null;
    }


}
