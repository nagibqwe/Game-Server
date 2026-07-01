package com.gm.project.gmtool.blackuser.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.project.gmtool.blackuser.domain.Blackuser;
import com.gm.project.gmtool.blackuser.service.IBlackuserService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;


/**
 * 黑名单Controller
 * 
 * @author gm
 * @date 2021-11-04
 */
@Controller
@RequestMapping("/gmtool/blackuser")
public class BlackuserController extends BaseController
{
    private String prefix = "gmtool/blackuser";

    @Autowired
    private IBlackuserService blackuserService;
    @Autowired
    private ITServerService tServerService;

    @RequiresPermissions("gmtool:blackuser:view")
    @GetMapping()
    public String blackuser()
    {
        return prefix + "/blackuser";
    }

    /**
     * 查询黑名单列表
     */
//    @RequiresPermissions("gmtool:blackuser:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Blackuser blackuser)
    {
        startPage();
        List<Blackuser> list = blackuserService.selectBlackuserList(blackuser);
        return getDataTable(list);
    }

    /**
     * 导出黑名单列表
     */
    @RequiresPermissions("gmtool:blackuser:export")
    @Log(title = "黑名单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Blackuser blackuser)
    {
        List<Blackuser> list = blackuserService.selectBlackuserList(blackuser);
        ExcelUtil<Blackuser> util = new ExcelUtil<Blackuser>(Blackuser.class);
        return util.exportExcel(list, "黑名单数据");
    }

    /**
     * 新增黑名单
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存黑名单
     */
    @RequiresPermissions("gmtool:blackuser:add")
    @Log(title = "黑名单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Blackuser blackuser)
    {
        return toAjax(blackuserService.insertBlackuser(blackuser));
    }

    /**
     * 修改黑名单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        Blackuser blackuser = blackuserService.selectBlackuserById(id);
        mmap.put("blackuser", blackuser);
        return prefix + "/edit";
    }

    /**
     * 修改保存黑名单
     */
    @RequiresPermissions("gmtool:blackuser:edit")
    @Log(title = "黑名单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Blackuser blackuser)
    {
        return toAjax(blackuserService.updateBlackuser(blackuser));
    }

    /**
     * 删除黑名单
     */
    @RequiresPermissions("gmtool:blackuser:remove")
    @Log(title = "黑名单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        int row = blackuserService.deleteBlackuserByIds(ids);
        BlackListManager.getInstance().loadData();
        return toAjax(row);
    }

    /**
     * 黑名单转换
     * @param convertBlackListFile
     * @param serverId
     * @param groupName
     * @return
     * @throws IOException
     */
    @PostMapping( "/blackListConvert")
    @ResponseBody
    public Object blackListConvert(MultipartFile convertBlackListFile, String serverId, String groupName) throws IOException {
        InputStream is = convertBlackListFile.getInputStream();
        XSSFWorkbook wb = new XSSFWorkbook(is);
        XSSFSheet sheet = wb.getSheetAt(0);
        StringBuilder userIdStr = new StringBuilder();
        List<Map<String, Object>> blackList = BlackListManager.getInstance().getBlackList(groupName);
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            XSSFCell userNameCell = row.getCell(0);
            if (userNameCell == null) {
                continue;
            }
            userIdStr.append("'").append(userNameCell.getStringCellValue()).append("',");
            nameList.add(userNameCell.toString());
        }
        userIdStr = new StringBuilder(userIdStr.substring(0, userIdStr.length() - 1));
        TServer tServer = tServerService.selectTServerByServerId(Integer.parseInt(serverId));

        if (tServer == null) {
            return null;
        }

        DBClient logDBClient = DBServerMgr.getInstance().getLogDBClient(Integer.parseInt(serverId));
        List<Map<String, Object>> dataMap = logDBClient.selectList("SELECT rs.userId,rs.rolename FROM rolestate rs WHERE rs.rolename IN (" + userIdStr + ")");
        List<Blackuser> buList = new ArrayList<>();
        List<String> roleNameList = new ArrayList<>();
        for (Map<String, Object> map: dataMap) {
            String userId0 = map.get("userId").toString();
            String roleName = map.get("roleName").toString();
            roleNameList.add(roleName);
            int flag = 0;
            for (Map<String, Object> blackMap : blackList) {
                String userId1 = blackMap.get("userId").toString();
                if (userId1.equals(userId0)) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                continue;
            }
            Blackuser blackUser = new Blackuser();
            blackUser.setUserId(Long.parseLong(userId0));
            blackUser.setPlatform(groupName);
            buList.add(blackUser);
            map.put("platform", groupName);
            blackList.add(map);
        }
        if (buList.size() > 0) {
            BlackListManager.getInstance().setDataList(blackList);
            for (Blackuser bUser:buList){
                blackuserService.insertBlackuser(bUser);
                BlackListManager.getInstance().loadData();
            }
        }
        nameList.removeAll(roleNameList);
        String resultStr;
        if (nameList.size() > 0) {
            resultStr = "黑名单中存在角色名找不到账号，以下角色名为：" + nameList;
        } else {
            resultStr = "黑名单导入完成！";
        }
        return AjaxResult.info(resultStr).put("ok",true);
    }

    /**
     * 导入黑名单
     * @param importBlackListFile
     * @return
     * @throws IOException
     */
    @PostMapping( "/upLoadBlackExcl")
    @ResponseBody
    public AjaxResult upLoadBlackExcl(MultipartFile importBlackListFile) throws IOException {
        InputStream is = importBlackListFile.getInputStream();
        XSSFWorkbook wb = new XSSFWorkbook(is);
        XSSFSheet sheet = wb.getSheetAt(0);
        long userId;
        String platform;
        List<Blackuser> buList = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            XSSFCell userIdCell = row.getCell(0);
            XSSFCell platFormCell = row.getCell(1);
            if (userIdCell == null) {
                continue;
            }
            if (platFormCell == null) {
                continue;
            }
            userId = Long.parseLong(userIdCell.getStringCellValue());
            platform = platFormCell.getStringCellValue();
            Blackuser blackuser = new Blackuser();
            blackuser.setUserId(userId);
            List<Blackuser> blackusers = blackuserService.selectBlackuserList(blackuser);
            Blackuser bu = null;
            if (blackusers.size() > 0){
                bu = blackuserService.selectBlackuserList(blackuser).get(0);
            }
            if (bu != null) {
                continue;
            } else {
                bu = new Blackuser();
            }
            bu.setPlatform(platform);
            bu.setUserId(userId);
            buList.add(bu);
        }
        for (Blackuser blackuser:buList){
            blackuserService.insertBlackuser(blackuser);
            BlackListManager.getInstance().loadData();
        }
        return AjaxResult.info("Reload success!").put("ok",true);
    }
}
