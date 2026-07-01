package com.kits.project.serverListConfig.serverList.controller;

import java.io.UnsupportedEncodingException;
import java.util.*;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kits.common.utils.DateUtils;
import com.kits.common.utils.JsonUtils;
import com.kits.common.utils.StringUtils;
import com.kits.common.utils.text.Convert;
import com.kits.framework.aspectj.lang.annotation.DataSource;
import com.kits.framework.aspectj.lang.enums.DataSourceType;
import com.kits.project.serverListConfig.channel.service.ISdkChannelService;
import com.kits.project.serverListConfig.channelids.domain.SdkChannelidChannelids;
import com.kits.project.serverListConfig.channelids.service.ISdkChannelidChannelidsService;
import com.kits.project.serverListConfig.serverExtra.domain.SdkServerExtra;
import com.kits.project.serverListConfig.serverExtra.service.ISdkServerExtraService;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.kits.framework.aspectj.lang.annotation.Log;
import com.kits.framework.aspectj.lang.enums.BusinessType;
import com.kits.project.serverListConfig.serverList.domain.SdkServerList;
import com.kits.project.serverListConfig.serverList.service.ISdkServerListService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务器列表Controller
 * 
 * @author gm
 * @date 2021-04-26
 */
@Controller
@RequestMapping("/serverListConfig/serverList")
public class SdkServerListController extends BaseController
{
    private String prefix = "serverListConfig/serverList";
    public final static int NotUsed = 0;
    public final static int IsUsed = 1;

    @Autowired
    private ISdkServerListService sdkServerListService;

    @Autowired
    private ISdkChannelService sdkChannelService;

    @Autowired
    private ISdkServerExtraService sdkServerExtraService;

    @Autowired
    private ISdkChannelidChannelidsService sdkChannelidChannelidsService;

    @RequiresPermissions("serverListConfig:serverList:view")
    @GetMapping()
    public String serverList()
    {
        return prefix + "/serverList";
    }
    @GetMapping("/toServerListEditPage")
    public String toServerListEditPage()
    {
        return prefix + "/serverListEdit";
    }

    /**
     * 查询服务器列表列表
     */
    @RequiresPermissions("serverListConfig:serverList:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SdkServerList sdkServerList)
    {
        startPage();
        List<SdkServerList> list = sdkServerListService.selectSdkServerListList(sdkServerList);
        return getDataTable(list);
    }

    /**
     * 导出服务器列表列表
     */
    @RequiresPermissions("serverListConfig:serverList:export")
    @Log(title = "服务器列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SdkServerList sdkServerList)
    {
        List<SdkServerList> list = sdkServerListService.selectSdkServerListList(sdkServerList);
        ExcelUtil<SdkServerList> util = new ExcelUtil<SdkServerList>(SdkServerList.class);
        return util.exportExcel(list, "服务器列表数据");
    }

    /**
     * 新增服务器列表
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存服务器列表
     */
    @RequiresPermissions("serverListConfig:serverList:add")
    @Log(title = "服务器列表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SdkServerList sdkServerList)
    {
        return toAjax(sdkServerListService.insertSdkServerList(sdkServerList));
    }

    /**
     * 修改服务器列表
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SdkServerList sdkServerList = sdkServerListService.selectSdkServerListById(id);
        mmap.put("sdkServerList", sdkServerList);
        return prefix + "/edit";
    }

//    /**
//     * 修改保存服务器列表
//     */
//    @RequiresPermissions("serverListConfig:serverList:edit")
//    @Log(title = "服务器列表", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(SdkServerList sdkServerList)
//    {
//
//        return toAjax(sdkServerListService.updateSdkServerList(sdkServerList));
//    }

    /**
     * 删除服务器列表
     */
    @RequiresPermissions("serverListConfig:serverList:remove")
    @Log(title = "服务器列表", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        //删除服务器额外信息
        sdkServerExtraService.deleteSdkServerExtraByIds(ids);

        String[] serverListIds = Convert.toStrArray(ids);
        HashMap map = new HashMap();
        for (String serverListId:serverListIds){
            SdkServerList sdkServerListOld = sdkServerListService.selectSdkServerListById(Long.valueOf(serverListId));
            String channelIdsOld = sdkServerListOld.getChannelIds();
            if (null != channelIdsOld && !channelIdsOld.equals("")){
                //删除服务器列表中的渠道ID和渠道ID列表关系对应(sdk_channelid_channelids)
                sdkChannelidChannelidsService.deleteSdkChannelidChannelidsByIds(channelIdsOld);
                //修改渠道使用状态为未使用 0
                map.put("isUse",NotUsed);
                map.put("channelIds",Convert.toStrArray(channelIdsOld));
                sdkChannelService.updateSdkChannelIsUse(map);
            }
        }
        //删除服务器列表信息
        sdkServerListService.deleteSdkServerListByIds(ids);
        return AjaxResult.success();
    }

    @Log(title = "服务器列表编辑服务器额外信息", businessType = BusinessType.UPDATE)
    @PostMapping("/editAll")
    @ResponseBody
        public AjaxResult addSave(String serverListData,String serverExtraData,HttpServletRequest request) {
        JSONArray serverListArray = JSONArray.parseArray(serverListData);

        String serverListId ="";
        String name ="";
        String channelIds ="";
        String serverIds ="";
        String loginServerGroup = "";
        String status ="";
        for (int i = 0; i < serverListArray.size(); i++){//serverList的信息只有一条
            if ("id".equals(getName(serverListArray,i))){
                serverListId = getValue(serverListArray,i);//服务器列表id
            }else if ("name".equals(getName(serverListArray,i))){
                name = getValue(serverListArray,i);
            }else if ("channelIds".equals(getName(serverListArray,i))){
                channelIds = getValue(serverListArray,i);
            }else if ("serverIds".equals(getName(serverListArray,i))){
                serverIds = getValue(serverListArray,i);
            }else if ("loginServers".equals(getName(serverListArray,i))){
                loginServerGroup = getValue(serverListArray,i);
            }else if ("status".equals(getName(serverListArray,i))){
                status = getValue(serverListArray,i);
            }
        }
        SdkServerList sdkServerListOld = sdkServerListService.selectSdkServerListById(Long.valueOf(serverListId));
        String channelIdsOld = sdkServerListOld.getChannelIds();

        saveServerExtra(request,serverListId,serverIds,serverExtraData);
        saveChannelIdChannelIds(channelIdsOld,channelIds);
        editChannelIsUse(channelIdsOld,channelIds);

        //修改服务器列表SdkServerList表格数据
        SdkServerList sdkServerList = new SdkServerList();
        sdkServerList.setId(Long.valueOf(serverListId));
        sdkServerList.setName(name);
        sdkServerList.setLoginServerGroup(loginServerGroup);
        sdkServerList.setChannelIds(channelIds);
        sdkServerList.setServerIds(serverIds);
        sdkServerList.setStatus(Long.valueOf(status));
        sdkServerList.setUpdateTime(DateUtils.getNowDate());
        sdkServerListService.updateSdkServerList(sdkServerList,2);
        return AjaxResult.success();
    }

    private String getName(JSONArray array,int i){
        return array.getJSONObject(i).getString("name");
    }

    private String getValue(JSONArray array,int i){
        return array.getJSONObject(i).getString("value");
    }
    /**
     * 保存渠道ID和渠道ID列表的对应关系
     * @param channelIds
     */
    private void saveChannelIdChannelIds(String channelIdsOld,String channelIds) {
        if (channelIdsOld == null){
            channelIdsOld = "";
        }
        List<String> deleteList=new ArrayList<String>();
        //初始化情况(没有旧数据，直接新加)
        if ((null == channelIdsOld || channelIdsOld.equals("")) && !channelIds.equals("")){
            saveChannelId(channelIds);
        }
        //有旧数据,修改后没有选择对应渠道
        if ((null != channelIdsOld || !"".equals(channelIdsOld)) && channelIds.equals("")){
            sdkChannelidChannelidsService.deleteSdkChannelidChannelidsByIds(channelIdsOld);
        }

        //有旧数据,修改后变动
        if (!channelIdsOld.equals("") && !channelIds.equals("")){
            String[] channelIdsOldArr = Convert.toStrArray(channelIdsOld);
            String[] channelIdsArr = Convert.toStrArray(channelIds);
            List<String> channelIdsOldList= Arrays.asList(channelIdsOldArr);
            List<String> channelIdsList= Arrays.asList(channelIdsArr);
            saveChannelId(channelIds);
            for (int i=0; i < channelIdsOldList.size();i++){
                int count = 0;
                for (int j=0;j < channelIdsList.size();j++){
                    if (!channelIdsOldList.get(i).equals(channelIdsList.get(j))){
                        count++;
                        if (count == channelIdsList.size()){//新改的列表需要全部对比完才能判定
                            deleteList.add(channelIdsOldList.get(i));
                        }
                    }else {
                        continue;
                    }
                }
            }
            HashMap map = new HashMap();
            if (deleteList.size() > 0){
                map.put("channelIds",deleteList);
                sdkChannelidChannelidsService.deleteSdkChannelidChannelidsByChannelIds(map);
            }
        }
    }

    private void saveChannelId(String channelIds) {
        String[] channelIdsArr = Convert.toStrArray(channelIds);
        for (int i = 0; i < channelIdsArr.length; i++){
            SdkChannelidChannelids channelids = new SdkChannelidChannelids();
            channelids.setChannelId(Long.valueOf(channelIdsArr[i]));
            channelids.setChannelIds(channelIds);
            sdkChannelidChannelidsService.replaceInsertSdkChannelidChannelids(channelids);
        }
    }

    /**
     * 保存服务器列表下对应的服务器额外信息
     * @param request
     * @param serverListId
     * @param serverIds
     */
    private void saveServerExtra(HttpServletRequest request, String serverListId, String serverIds,String serverExtraData) {
        if (!serverIds.equals("")){
            String[] serverIdsArr = Convert.toStrArray(serverIds);

            JSONArray serverExtraArray = JSONArray.parseArray(serverExtraData);
            List<Long> serverIdsList = sdkServerExtraService.selectServerIdsByServerListId(Long.valueOf(serverListId));//查询数据库原有数据
            //需要保存的服务器额外信息(新增或者更新)
            String id = "";
            String serverId ="";
            String sortId ="";
            String serverStatus ="";
            String groupType ="";
            String serverLabel ="";
            String sceId ="";
            String appVersion ="";
            JSONObject serverExtraArrayIndex = new JSONObject();
            for (int i=0; i < serverIdsArr.length; i++){
                serverExtraArrayIndex = serverExtraArray.getJSONObject(i);
                id = serverExtraArrayIndex.getString("id");
                if (id == null){
                    List<Long> sdkServerExtraIdList = sdkServerExtraService.selectSdkServerExtraId();
                    if (null == sdkServerExtraIdList || sdkServerExtraIdList.size() < 1){
                        id = "1";
                    }else {
                        Long lastId = sdkServerExtraIdList.get(sdkServerExtraIdList.size() - 1);
                        id = String.valueOf(lastId + 1);
                    }
                }
                serverId = serverExtraArrayIndex.getString("serverId");
                sortId = serverExtraArrayIndex.getString("sortId");
                serverStatus = serverExtraArrayIndex.getString("serverStatus");
                groupType = serverExtraArrayIndex.getString("groupType");
                serverLabel = serverExtraArrayIndex.getString("serverLabel");
                sceId = serverExtraArrayIndex.getString("sceId");
                appVersion = serverExtraArrayIndex.getString("appVersion");

                SdkServerExtra sdkServerExtra = new SdkServerExtra();
                sdkServerExtra.setId(Long.valueOf(id));
                sdkServerExtra.setServerListId(Long.valueOf(serverListId));
                sdkServerExtra.setServerId(Long.valueOf(serverId));
                sdkServerExtra.setSortId(StringUtils.parseLong(sortId,0));
                sdkServerExtra.setServerStatus(Long.valueOf(serverStatus));
                sdkServerExtra.setGroupType(Long.valueOf(groupType));
                if ("".equals(serverLabel)){
                    sdkServerExtra.setServerLabel(serverLabel);
                }else {
                    String[] serverLabelArr = serverLabel.split(",");
                    //转换存储数据格式
                    sdkServerExtra.setServerLabel(StringUtils.stringArrToIntStr(serverLabelArr));
                }
                if (!sceId.equals("") && !sceId.endsWith(",")){
                    sceId+=",";
                    sdkServerExtra.setSceId(sceId);
                }
                if (!sceId.equals("") && sceId.endsWith(",")){
                    sdkServerExtra.setSceId(sceId);
                }
                if (sceId.equals("")){
                    sdkServerExtra.setSceId(sceId);
                }
                sdkServerExtra.setAppVersion(appVersion);
                sdkServerExtra.setCreateTime(DateUtils.getNowDate());
                sdkServerExtra.setUpdateTime(DateUtils.getNowDate());
                sdkServerExtraService.saveServerExtra(sdkServerExtra);
            }
            //页面操作后需要删除的服务器额外信息
            Long[] serverIdsNew = StringUtils.stringArrToLongArr(serverIdsArr);
            HashMap map = new HashMap();
            if (null != serverIdsList && serverIdsList.size() > 0){
                for (int i=0; i < serverIdsList.size(); i++){
                    int count = 0;
                    for (int j=0; j < serverIdsNew.length; j++){
                        if (serverIdsList.get(i).longValue() != serverIdsNew[j].longValue()){
                            count++;
                            if (count == serverIdsNew.length){
                                map.put("serverListId",Long.valueOf(serverListId));
                                map.put("serverId",serverIdsList.get(i));
                                sdkServerExtraService.deleteServerExtraByServerListIdAndServerId(map);
                            }
                        }else {
                            continue;
                        }
                    }
                }
            }
        }
    }

    /**
     * 改变渠道是否使用的状态
     * @param channelIdsOld
     * @param channelIdsNew
     */
    private void editChannelIsUse(String channelIdsOld,String channelIdsNew){
        String[] channelIdsOldArr = null;//数据中的老数据
        String[] channelIdsNewArr = null;//修改后的新数据
        List<String> notUsedlist=new ArrayList<String>();
        if (channelIdsOld != null && !channelIdsOld.equals("")){
            channelIdsOldArr = Convert.toStrArray(channelIdsOld);
        }
        if (channelIdsNew != null && !channelIdsNew.equals("")){
            channelIdsNewArr = Convert.toStrArray(channelIdsNew);
        }
        //初始化的情况(没有旧数据，直接新加)
        HashMap map = new HashMap();
        if (channelIdsOldArr == null && channelIdsNewArr != null){
            map.put("isUse",IsUsed);
            map.put("channelIds",channelIdsNewArr);
            sdkChannelService.updateSdkChannelIsUse(map);
        }
        //有旧数据,修改后没有选择对应渠道
        if (channelIdsOldArr != null && channelIdsNewArr == null){
            map.put("isUse",NotUsed);
            map.put("channelIds",channelIdsOldArr);
            sdkChannelService.updateSdkChannelIsUse(map);
        }
        if (channelIdsOldArr != null && channelIdsNewArr != null){
            map.put("isUse",IsUsed);
            map.put("channelIds",channelIdsNewArr);
            sdkChannelService.updateSdkChannelIsUse(map);

            List<String> channelIdsOldList= Arrays.asList(channelIdsOldArr);
            List<String> channelIdsNewList= Arrays.asList(channelIdsNewArr);

            for (int i=0; i < channelIdsOldList.size();i++){
                int count = 0;
                for (int j=0;j < channelIdsNewList.size();j++){
                    if (!channelIdsOldList.get(i).equals(channelIdsNewList.get(j))){
                        count++;
                        if (count == channelIdsNewList.size()){//新改的列表需要全部对比完才能判定
                            notUsedlist.add(channelIdsOldList.get(i));
                        }
                    }else {
                        continue;
                    }
                }
            }
            if (notUsedlist.size()>0){
                map.put("isUse",NotUsed);
                map.put("channelIds",notUsedlist);
                sdkChannelService.updateSdkChannelIsUse(map);
            }
        }
    }

    private String getLoginServerGroup(String[] loginServerIpArr,String[] loginServerNameArr,String[] loginServerPortArr){
        Map<String, String[]> loginServerMap = new LinkedHashMap<>();
        loginServerMap.put("loginServerIp",loginServerIpArr);
        loginServerMap.put("loginServerName",loginServerNameArr);
        loginServerMap.put("loginServerPort",loginServerPortArr);

        return JsonUtils.toJSONString(loginServerMap);
    }
}
