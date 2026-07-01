package com.kits.project.serverListConfig.channel.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kits.common.utils.MessageUtils;
import com.kits.common.utils.StringUtils;
import com.kits.common.utils.text.Convert;
import com.kits.framework.aspectj.lang.annotation.DataSource;
import com.kits.framework.aspectj.lang.enums.DataSourceType;
import com.kits.project.serverListConfig.channelids.domain.SdkChannelidChannelids;
import com.kits.project.serverListConfig.channelids.service.ISdkChannelidChannelidsService;
import com.kits.project.serverListConfig.serverList.domain.SdkServerList;
import com.kits.project.serverListConfig.serverList.service.ISdkServerListService;
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
import com.kits.project.serverListConfig.channel.domain.SdkChannel;
import com.kits.project.serverListConfig.channel.service.ISdkChannelService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 渠道信息Controller
 * 
 * @author gm
 * @date 2021-04-28
 */
@Controller
@RequestMapping("/serverListConfig/channel")
public class SdkChannelController extends BaseController
{
    private String prefix = "serverListConfig/channel";

    @Autowired
    private ISdkChannelService sdkChannelService;

    @Autowired
    private ISdkServerListService sdkServerListService;

    @Autowired
    private ISdkChannelidChannelidsService sdkChannelidChannelidsService;

    @RequiresPermissions("serverListConfig:channel:view")
    @GetMapping()
    public String channel()
    {
        return prefix + "/channel";
    }

    /**
     * 查询渠道信息列表
     */
    @RequiresPermissions("serverListConfig:channel:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SdkChannel sdkChannel)
    {
        startPage();
        List<SdkChannel> list = sdkChannelService.selectSdkChannelList(sdkChannel);
        return getDataTable(list);
    }

    /**
     * 导出渠道信息列表
     */
    @RequiresPermissions("serverListConfig:channel:export")
    @Log(title = "渠道信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SdkChannel sdkChannel)
    {
        List<SdkChannel> list = sdkChannelService.selectSdkChannelList(sdkChannel);
        ExcelUtil<SdkChannel> util = new ExcelUtil<SdkChannel>(SdkChannel.class);
        return util.exportExcel(list, "渠道信息数据");
    }

    /**
     * 新增渠道信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存渠道信息
     */
    @RequiresPermissions("serverListConfig:channel:add")
    @Log(title = "渠道信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SdkChannel sdkChannel)
    {
        return toAjax(sdkChannelService.insertSdkChannel(sdkChannel));
    }

    /**
     * 修改渠道信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SdkChannel sdkChannel = sdkChannelService.selectSdkChannelById(id);
        mmap.put("sdkChannel", sdkChannel);
        return prefix + "/edit";
    }

    /**
     * 修改保存渠道信息
     */
    @RequiresPermissions("serverListConfig:channel:edit")
    @Log(title = "渠道信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SdkChannel sdkChannel)
    {
        return toAjax(sdkChannelService.updateSdkChannel(sdkChannel));
    }

    /**
     * 删除渠道信息
     */
    @RequiresPermissions("serverListConfig:channel:remove")
    @Log(title = "渠道信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        //根据页面传的ids查询需要删除的channelId列表
        List<Long> channelIdLongList = sdkChannelService.selectChannelIdsByIds(ids);
        String channelIdsDel = StringUtils.longListToString(channelIdLongList);
        //删除服务器列表中的渠道ID和渠道ID列表关系对应
        sdkChannelidChannelidsService.deleteSdkChannelidChannelidsByIds(channelIdsDel);
        List<String> channelIdStrListDel = Arrays.asList(Convert.toStrArray(channelIdsDel));
        //查询全部渠道ID和渠道ID列表关系对应数据
        SdkChannelidChannelids sdkChannelidChannelids = new SdkChannelidChannelids();
        List<SdkChannelidChannelids> sdkChannelidChannelidsList = sdkChannelidChannelidsService.selectSdkChannelidChannelidsList(sdkChannelidChannelids);
        if (sdkChannelidChannelidsList != null && sdkChannelidChannelidsList.size() > 0){
            for (int i = 0; i < sdkChannelidChannelidsList.size(); i++){
                Long channelId = sdkChannelidChannelidsList.get(i).getChannelId();
                String channelIds = sdkChannelidChannelidsList.get(i).getChannelIds();
                List<String> channelIdsList = Arrays.asList(Convert.toStrArray(channelIds));
                List<String> channelIdsList2 = new ArrayList(channelIdsList);
                for (int j = 0; j < channelIdStrListDel.size(); j++){
                    if (channelIdsList2.contains(channelIdStrListDel.get(j))){
                        channelIdsList2.remove(channelIdStrListDel.get(j));//修改渠道ID和渠道ID列表关系对应数据
                    }
                }
                SdkChannelidChannelids channelids = new SdkChannelidChannelids();
                channelids.setChannelId(channelId);
                channelids.setChannelIds(StringUtils.stringListToString(channelIdsList2));
                sdkChannelidChannelidsService.replaceInsertSdkChannelidChannelids(channelids);
            }
        }
        //查询全部服务器列表数据
        SdkServerList sdkServerList = new SdkServerList();
        List<SdkServerList> sdkServerLists = sdkServerListService.selectSdkServerListList(sdkServerList);
        if (sdkServerLists != null && sdkServerLists.size() > 0){
            for (int i = 0; i < sdkServerLists.size(); i++){
                Long id = sdkServerLists.get(i).getId();
                String channelIds = sdkServerLists.get(i).getChannelIds();
                if (channelIds != null){
                    List<String> channelIdsList = Arrays.asList(Convert.toStrArray(channelIds));
                    List<String> channelIdsList2 = new ArrayList(channelIdsList);
                    for (int j = 0; j < channelIdStrListDel.size(); j++){
                        if (channelIdsList2.contains(channelIdStrListDel.get(j))){
                            channelIdsList2.remove(channelIdStrListDel.get(j));//修改服务器列表中渠道ID列表 channel_ids
                        }
                    }
                    SdkServerList sdkServerList1 = new SdkServerList();
                    sdkServerList1.setId(id);
                    sdkServerList1.setChannelIds(StringUtils.stringListToString(channelIdsList2));
                    sdkServerListService.updateSdkServerList(sdkServerList1,1);
                }
            }
        }
        //删除渠道信息
        sdkChannelService.deleteSdkChannelByIds(ids);
        return AjaxResult.success();
    }

    /**
     * 获取可用渠道(包括已有的渠道)
     * @param serverListId
     * @return
     */
    @PostMapping("/selectChannels")
    @ResponseBody
    public List<SdkChannel> selectChannels(long serverListId)
    {
        String channelIds = sdkServerListService.selectChannelIdsById(serverListId);
        List<SdkChannel> sdkChannelList = sdkChannelService.selectChannels(StringUtils.nvl(channelIds,""));
        return sdkChannelList;
    }

    /**
     * 获取页面展示的渠道(服务器列表已有的渠道数据)
     * @param serverListId
     * @return
     */
    @PostMapping("/selectShowChannels")
    @ResponseBody
    public List<SdkChannel> selectShowChannels(long serverListId)
    {
        String channelIds = sdkServerListService.selectChannelIdsById(serverListId);
        List<SdkChannel> sdkShowChannelList = sdkChannelService.selectShowChannels(StringUtils.nvl(channelIds,""));
        return sdkShowChannelList;
    }

    /**
     * 加载所有渠道信息
     * @return
     */
    @PostMapping("/selectChannelsInfo")
    @ResponseBody
    public List<SdkChannel> selectChannelsInfo()
    {
        SdkChannel sdkChannel = new SdkChannel();
        List<SdkChannel> list = sdkChannelService.selectSdkChannelList(sdkChannel);
        return list;
    }

    /**
     * 检测渠道ID是否已经存在
     * @param channelId
     * @return
     */
    @PostMapping("/checkChannelId")
    @ResponseBody
    public AjaxResult checkChannelId(String channelId)
    {
        int count = 0;
        SdkChannel sdkChannel = new SdkChannel();
        sdkChannel.setChannelId(Long.valueOf(channelId));
        List<SdkChannel> list = sdkChannelService.selectSdkChannelList(sdkChannel);
        AjaxResult ajaxResult = new AjaxResult();
        if (null != list && list.size() > 0){
            count = list.size();
            ajaxResult.put("count",count);
            ajaxResult.put("msg", MessageUtils.message("serverList.channelIdHasExist"));
        }else {
            ajaxResult.put("count",count);
            ajaxResult.put("msg", MessageUtils.message("serverList.channelIdEnable"));
        }
        return ajaxResult;
    }
}
