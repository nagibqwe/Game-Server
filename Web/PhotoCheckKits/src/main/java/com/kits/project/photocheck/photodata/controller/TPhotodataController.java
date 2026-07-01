package com.kits.project.photocheck.photodata.controller;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kits.common.utils.DateUtils;
import com.kits.common.utils.LogUtil;
import com.kits.common.utils.StringUtils;
import com.kits.common.utils.text.Convert;
import com.kits.framework.config.ProjectConfig;
import com.kits.project.photocheck.photodata.domain.PhotoData;
import com.kits.project.photocheck.photodata.utils.PhotoFileDownloadUtils;
import com.kits.project.photocheck.photodata.utils.PhotoFileUploadUtils;
import com.kits.project.photocheck.photodata.utils.PhotoFileUtils;
import com.kits.project.photocheck.photoglobal.domain.TPhotoglobal;
import com.kits.project.photocheck.photoglobal.service.ITPhotoglobalService;
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
import com.kits.project.photocheck.photodata.domain.TPhotodata;
import com.kits.project.photocheck.photodata.service.ITPhotodataService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.MimeTypeUtils.APPLICATION_OCTET_STREAM;


/**
 * 图片信息Controller
 * 
 * @author gm
 * @date 2021-07-19
 */
@Controller
@RequestMapping("/photocheck/photodata")
public class TPhotodataController extends BaseController
{
    private String prefix = "photocheck/photodata";
    private static final int notDelete = 0;//未删除
    private static final int delete = 1;//删除
    public static final int dayLimit = 20;//默认删除时间为20天
    public static final int status_notCheck = 0;//未审核
    public static final int status_pass = 2;//审核通过
    public static final int status_notPass = 3;//审核未通过

    @Autowired
    private ITPhotodataService tPhotodataService;

    @Autowired
    private ITPhotoglobalService tPhotoglobalService;

    //查询页面
    @RequiresPermissions("photocheck:photodata:view")
    @GetMapping()
    public String photodata()
    {
        return prefix + "/photodata";
    }
    /**
     * 根据不同页面条件查询图片信息列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPhotodata tPhotodata)
    {
        startPage();
        List<TPhotodata> list = getOldList(tPhotodata,notDelete);
        return getDataTable(getNewList(list));
    }
    //回收站页面
    @RequiresPermissions("photocheck:photodata:backPage")
    @GetMapping("/backPage")
    public String backPage()
    {
        return prefix + "/backPage";
    }
    /**
     * 根据不同页面条件查询图片信息列表
     */
    @PostMapping("/backPageList")
    @ResponseBody
    public TableDataInfo backPageList(TPhotodata tPhotodata)
    {
        startPage();
        List<TPhotodata> list = getOldList(tPhotodata,delete);
        return getDataTable(getNewList(list));
    }

    private List<TPhotodata> getOldList(TPhotodata tPhotodata,int isDelete) {
        List<TPhotodata> list = new ArrayList<>();
        tPhotodata.setIsDelete(isDelete);
        list = tPhotodataService.selectTPhotodataList(tPhotodata);
        return list;
    }

    //未审核页面
    @RequiresPermissions("photocheck:photodata:notCheckPage")
    @GetMapping("/notCheckPage")
    public String notCheckPage()
    {
        return prefix + "/notCheckPage";
    }
    /**
     * 根据不同页面条件查询图片信息列表
     */
    @PostMapping("/notCheckPageList")
    @ResponseBody
    public TableDataInfo notCheckPageList(TPhotodata tPhotodata)
    {
        startPage();
        List<TPhotodata> list = new ArrayList<>();
        tPhotodata.setIsDelete(notDelete);
        tPhotodata.setCheckStatus(0);//未审核
        list = tPhotodataService.selectTPhotodataList(tPhotodata);
        return getDataTable(getNewList(list));
    }

    //审核通过页面
    @RequiresPermissions("photocheck:photodata:checkPassPage")
    @GetMapping("/checkPassPage")
    public String checkPass()
    {
        return prefix + "/checkPassPage";
    }
    /**
     * 根据不同页面条件查询图片信息列表
     */
    @PostMapping("/checkPassList")
    @ResponseBody
    public TableDataInfo checkPassList(TPhotodata tPhotodata)
    {
        startPage();
        List<TPhotodata> list = new ArrayList<>();
        tPhotodata.setIsDelete(notDelete);
        tPhotodata.setCheckStatus(2);//审核通过
        list = tPhotodataService.selectTPhotodataList(tPhotodata);
        return getDataTable(getNewList(list));
    }

    //审核未通过页面
    @RequiresPermissions("photocheck:photodata:checkNotPassPage")
    @GetMapping("/checkNotPassPage")
    public String checkNotPass()
    {
        return prefix + "/checkNotPassPage";
    }
    /**
     * 根据不同页面条件查询图片信息列表
     */
    @PostMapping("/checkNotPassList")
    @ResponseBody
    public TableDataInfo checkNotPassList(TPhotodata tPhotodata)
    {
        startPage();
        List<TPhotodata> list = new ArrayList<>();
        tPhotodata.setIsDelete(notDelete);
        tPhotodata.setCheckStatus(3);//审核未通过
        list = tPhotodataService.selectTPhotodataList(tPhotodata);

        return getDataTable(getNewList(list));
    }

    /**
     * 每条数据中放入真实图片地址方便显示
     * @param tPhotodataList
     * @return
     */
    private List<TPhotodata> getNewList(List<TPhotodata> tPhotodataList){
        for (TPhotodata tPhotodata1:tPhotodataList){
            tPhotodata1.setDesc6(getPhotoPath(tPhotodata1.getSmallPhotoPath()));//将图片地址放入备用字段中
        }
        return tPhotodataList;
    }

    /**
     * 获取图片最终的真实地址
     * @param smallPhotoPath
     * @return
     */
    private String getPhotoPath(String smallPhotoPath) {
        String photoRootPath = ProjectConfig.getPhotoURL();
        String absolutePath = photoRootPath + "/" + smallPhotoPath;

        return absolutePath;
    }

    /**
     * 导出图片信息列表
     */
    @Log(title = "图片信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPhotodata tPhotodata)
    {
        List<TPhotodata> list = tPhotodataService.selectTPhotodataList(tPhotodata);
        ExcelUtil<TPhotodata> util = new ExcelUtil<TPhotodata>(TPhotodata.class);
        return util.exportExcel(list, "图片信息数据");
    }

    /**
     * 新增图片信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

//    /**
//     * 新增保存图片信息
//     */
//    @RequiresPermissions("photocheck:photodata:add")
//    @Log(title = "图片信息", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    @ResponseBody
//    public AjaxResult addSave(TPhotodata tPhotodata)
//    {
//        return toAjax(tPhotodataService.insertTPhotodata(tPhotodata));
//    }

    /**
     * 修改图片信息
     */
    @GetMapping("/edit")
    public String edit(Long id, ModelMap mmap)
    {
        TPhotodata tPhotodata = tPhotodataService.selectTPhotodataById(id);
        mmap.put("tPhotodata", getNewTPhotodata(tPhotodata));
        return "photocheck/common/photoDetail";
    }

    /**
     * 详情中的图片最终的真实地址
     * @param tPhotodata
     * @return
     */
    private TPhotodata getNewTPhotodata(TPhotodata tPhotodata){
        String photoRootPath = ProjectConfig.getPhotoURL();
        String absolutePath = photoRootPath + "/" + tPhotodata.getBigPhotoPath();
        tPhotodata.setDesc6(absolutePath);
        return tPhotodata;
    }

    /**
     * 修改保存图片信息
     */
    @RequiresPermissions("photocheck:photodata:edit")
    @Log(title = "图片信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPhotodata tPhotodata)
    {
        return toAjax(tPhotodataService.updateTPhotodata(tPhotodata));
    }

    /**
     * 图片信息放入回收站
     */
    @Log(title = "图片信息放入回收站", businessType = BusinessType.OTHER)
    @PostMapping( "/putBack")
    @ResponseBody
    public AjaxResult putBack(String ids)
    {

        return toAjax(tPhotodataService.putBackTPhotodataByIds(ids));
    }

    /**
     * 回收站中的信息批量销毁
     */
    @RequiresPermissions("photocheck:photodata:remove")
    @Log(title = "回收站中的信息批量销毁", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        List<TPhotodata> photodataList = tPhotodataService.selectTPhotodataByIds(ids);
        deleteFile(photodataList);
        return toAjax(tPhotodataService.deleteTPhotodataByIds(ids));
    }

    /**
     * 回收站中的信息全部清空
     */
    @Log(title = "回收站中的信息全部清空", businessType = BusinessType.OTHER)
    @PostMapping( "/deleteAll")
    @ResponseBody
    public AjaxResult deleteAll()
    {
        TPhotodata tPhotodata = new TPhotodata();
        tPhotodata.setIsDelete(delete);
        List<TPhotodata> list = tPhotodataService.selectTPhotodataList(tPhotodata);
        deleteFile(list);
        return toAjax(tPhotodataService.deleteTPhotodataByIsDelete());
    }

    /**
     * 批量删除文件
     * @param list
     */
    private void deleteFile(List<TPhotodata> list){
        for (TPhotodata tPhotodata:list){
            String bigPhotoPath = tPhotodata.getBigPhotoPath();
            String smallPhotoPath = tPhotodata.getSmallPhotoPath();
            PhotoFileUtils.delete(bigPhotoPath,smallPhotoPath);
        }
    }

    /**
     * 回收站中的信息批量恢复
     */
    @Log(title = "回收站中的信息批量恢复", businessType = BusinessType.OTHER)
    @PostMapping( "/recoverMore")
    @ResponseBody
    public AjaxResult recoverMore(String ids)
    {
        return toAjax(tPhotodataService.recoverTPhotodataByIds(ids));
    }

    /**
     * 回收站中的信息全部恢复
     */
    @Log(title = "回收站中的信息全部恢复", businessType = BusinessType.OTHER)
    @PostMapping( "/recoverAll")
    @ResponseBody
    public AjaxResult recoverAll()
    {
        return toAjax(tPhotodataService.recoverTPhotodata());
    }

    /**
     * 回收站中的信息获取删除时间
     */
    @Log(title = "回收站中的信息获取删除时间", businessType = BusinessType.OTHER)
    @PostMapping( "/getDayLimit")
    @ResponseBody
    public AjaxResult getDayLimit()
    {
        TPhotoglobal tPhotoglobal = new TPhotoglobal();
        int dayLimit1 = dayLimit;
        List<TPhotoglobal> tPhotoglobals = tPhotoglobalService.selectTPhotoglobalList(tPhotoglobal);
        if (null != tPhotoglobals && tPhotoglobals.size() > 0){
            for (TPhotoglobal tPhotoglobal1:tPhotoglobals){
                if (tPhotoglobal1.getKeyStr().equals("dayLimit")){
                    dayLimit1 = Integer.parseInt(tPhotoglobal1.getValueStr());
                }
            }
        }
        return AjaxResult.success(dayLimit1);
    }

    /**
     * 回收站中的信息设置删除时间
     */
    @Log(title = "回收站中的信息设置删除时间", businessType = BusinessType.OTHER)
    @PostMapping( "/editDayLimit")
    @ResponseBody
    public AjaxResult editDayLimit(int dayLimit)
    {
        TPhotoglobal tPhotoglobal = new TPhotoglobal();
        tPhotoglobal.setKeyStr("dayLimit");
        tPhotoglobal.setValueStr(String.valueOf(dayLimit));
        return toAjax(tPhotoglobalService.updateTPhotoglobal(tPhotoglobal));
    }

    /**
     * 未审核中的信息批量通过
     */
    @Log(title = "未审核中的信息批量通过", businessType = BusinessType.OTHER)
    @PostMapping( "/passMore")
    @ResponseBody
    public AjaxResult passMore(String ids)
    {
        HashMap map = new HashMap();
        map.put("checkStatus",status_pass);
        map.put("ids", Convert.toStrArray(ids));
        return toAjax(tPhotodataService.updateTPhotodataCheckStatus(map));
    }

    /**
     * 未审核中的信息批量拒绝
     */
    @Log(title = "未审核中的信息批量拒绝", businessType = BusinessType.OTHER)
    @PostMapping( "/refuseMore")
    @ResponseBody
    public AjaxResult refuseMore(String ids)
    {
        HashMap map = new HashMap();
        map.put("checkStatus",status_notPass);
        map.put("ids", Convert.toStrArray(ids));
        return toAjax(tPhotodataService.updateTPhotodataCheckStatus(map));
    }

    /**
     * 审核通过中的信息批量退回未审核状态
     */
    @Log(title = "审核通过中的信息批量退回未审核状态", businessType = BusinessType.OTHER)
    @PostMapping( "/backNotCheckMore")
    @ResponseBody
    public AjaxResult backNotCheckMore(String ids)
    {
        HashMap map = new HashMap();
        map.put("checkStatus",status_notCheck);
        map.put("ids", Convert.toStrArray(ids));
        return toAjax(tPhotodataService.updateTPhotodataCheckStatus(map));
    }

    /**
     * 审核未通过中的信息批量移到回收站
     */
    @Log(title = "审核未通过中的信息批量移到回收站", businessType = BusinessType.OTHER)
    @PostMapping( "/putBackMore")
    @ResponseBody
    public AjaxResult putBackMore(String ids)
    {
        HashMap map = new HashMap();
        map.put("isDelete",delete);
        map.put("deleteTime", DateUtils.dateTimeNow());
        map.put("ids", Convert.toStrArray(ids));
        return toAjax(tPhotodataService.updateTPhotodataCheckStatus(map));
    }

    //-------- 图片审核系统后台开始 ---------
    /**
     * 上传图片到本地服务器
     * @param response
     * @param request
     */
    @PostMapping("/upload")
    public void upload(HttpServletResponse response, HttpServletRequest request, PhotoData model) throws NoSuchAlgorithmException, IOException {
        long currentTimeMillis = System.currentTimeMillis();
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String photofile = ProjectConfig.getPhotoFile();//上传图片的地址
        String serverId = model.getDesc1();//服务器ID
        String roleId = model.getDesc2();//角色ID
        String roleName = model.getDesc3();//角色名称
        String photoId = model.getPhotoId();//图片ID

        MultipartFile photoData = model.getPhotoData();//二进制图片数据
        String extName = model.getExtName();//扩展名

        //判断参数
        if (StringUtils.isNotNull(serverId) && !StringUtils.isNum(serverId)){
            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getResult(""))));
            LogUtil.error("服务器ID有误!");
            return;
        }

//        if (StringUtils.isNotNull(roleId) && !StringUtils.isNum(roleId)){
//            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getResult(""))));
//            LogUtil.error("角色ID有误!");
//            return;
//        }

        if ((null == photoData || photoData.isEmpty()) || (null == extName || "".equals(extName))){
            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getResult(""))));
            LogUtil.error("二进制图片数据or扩展名为空");
            return;
        }

        //图片ID:不为空表示替换,如果为空表示新增
        Date dateNow = new Date();
        String photoID = "";
        if (null != photoId && !"".equals(photoId)){
            TPhotodata tPhotodata = tPhotodataService.selectTPhotodataByPhotoID(photoId);
            if (null == tPhotodata){
                LogUtil.error("数据库中没有该图片ID相关数据");
            }else {
                //删除文件
                String bigPhotoPath = tPhotodata.getBigPhotoPath();
                String smallPhotoPath = tPhotodata.getSmallPhotoPath();
                PhotoFileUtils.delete(bigPhotoPath,smallPhotoPath);

                //删除数据库数据
                tPhotodataService.deleteTPhotodataByPhotoID(photoId);

            }
        }

        photoID = updatePhotoData(serverId,roleId,roleName,photoData,photofile,extName,dateNow);

        //返回图片ID
        response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getResult(photoID))));

        LogUtil.info("客户端上传图片耗时:" +(System.currentTimeMillis() - currentTimeMillis)+ "ms");
    }

    private JSON getResult(Object photoID) {

        HashMap<String, Object> dataMap = new HashMap<String, Object>();

        dataMap.put("photoID",photoID);
        return (JSON) JSONObject.toJSON(dataMap);
    }

    /**
     * PhotoID:原始图片的MD5+数据自增ID
     * @param serverId
     * @param roleId
     * @param roleName
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private Long getId(String serverId,String roleId,String roleName) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        TPhotodata tPhotodata = new TPhotodata();
        tPhotodata.setDesc1(serverId);
        tPhotodata.setDesc2(roleId);
        tPhotodata.setDesc3(roleName);
        Long id = tPhotodataService.insertTPhotodata(tPhotodata);
        return id;
    }

    /**
     * 上传原图并且生成小图记录数据到数据库
     * @param serverId
     * @param roleId
     * @param roleName
     * @param photoData
     * @param photofile
     * @param extName
     * @param dateNow
     */
    private String updatePhotoData(String serverId,String roleId,String roleName,MultipartFile photoData,String photofile,String extName,Date dateNow){
        Long id = 0L;
        try {
            id = getId(serverId,roleId,roleName);
        } catch (Exception e) {
            LogUtil.error("存入数据库出错",e);
        }
        try {
            HashMap<String,String> dataResultMap = PhotoFileUploadUtils.uploadPhoto(photoData,photofile,extName,dateNow,id);
            String photoID = dataResultMap.get("photoID");
            String bigAbsolutePath = dataResultMap.get("bigAbsolutePath");
            String smallAbsolutePath = dataResultMap.get("smallAbsolutePath");
            String bigPhotoSize = dataResultMap.get("bigPhotoSize");
            String smallPhotoSize = dataResultMap.get("smallPhotoSize");
            String bigPhotoMD5 = PhotoFileUtils.getMD5(new File(bigAbsolutePath));
            String smallPhotoMD5 = PhotoFileUtils.getMD5(new File(smallAbsolutePath));
            String bigRelativePath = PhotoFileUtils.getRelativePath(bigAbsolutePath,photofile);
            String smallRelativePath = PhotoFileUtils.getRelativePath(smallAbsolutePath,photofile);
            TPhotodata tPhotodata = new TPhotodata();
            tPhotodata.setId(id);
            tPhotodata.setPhotoID(photoID);
            tPhotodata.setPhotoMD5(bigPhotoMD5);
            tPhotodata.setPhotoSize(bigPhotoSize);
            tPhotodata.setSmallPhotoSize(smallPhotoSize);
            tPhotodata.setSmallMD5(smallPhotoMD5);
            tPhotodata.setBigPhotoPath(bigRelativePath);
            tPhotodata.setSmallPhotoPath(smallRelativePath);
            tPhotodata.setPhotoExtName(extName);
            tPhotodata.setUploadTime(DateUtils.dateToString(dateNow));

            tPhotodataService.updateTPhotodata(tPhotodata);
            return photoID;
        } catch (IOException e) {
            LogUtil.error("上传文件失败",e);
        }
        return "";
    }


    /**
     * 查询图片状态
     * @param response
     * @param request
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/query")
    public void query(HttpServletResponse response, HttpServletRequest request) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String photoId = request.getParameter("photoId");//图片ID
        String type = request.getParameter("type");//图片类型 0:原图 1:小图

        //判断参数
        if (null == photoId || "".equals(photoId)){
            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getResult(0,""))));
            LogUtil.error("图片ID为空");
            return;
        }

        if (StringUtils.isNotNull(type) && !StringUtils.isNum(type)){
            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getResult(0,""))));
            LogUtil.error("图片类型有误");
            return;
        }

        TPhotodata tPhotodata = tPhotodataService.selectTPhotodataByPhotoID(photoId);
        if (null == tPhotodata){
            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getResult(0,""))));
            LogUtil.error("数据库中没有该图片ID相关数据");
            return;
        }
        int checkStatus = tPhotodata.getCheckStatus();//0未审核 1:审核中 2:审核通过 3:审核未通过
        int requestNum = tPhotodata.getRequestNum();
        int isDelete = tPhotodata.getIsDelete();
        tPhotodata.setRequestNum(requestNum + 1);//每次请求次数加1
        tPhotodata.setLastRquestTime(DateUtils.dateToString(new Date()));
        if (checkStatus == 3){
            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getResult(1,""))));
            LogUtil.error("审核未通过");
            return;
        }
        if (checkStatus == 0 || checkStatus == 1){
            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getResult(2,""))));
            LogUtil.error("正在审核中");
            return;
        }
//        String photoMD5 = "";//数据库中存储的图片md5
        if (checkStatus == 2 && isDelete == notDelete){//审核通过 并且未删除
            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getResult(3,getPhotoAbsolutePath(type,tPhotodata)))));
            tPhotodata.setLastDownloadTime(DateUtils.dateToString(new Date()));
            tPhotodataService.updateTPhotodata(tPhotodata);
            LogUtil.error("可以下载");
        }

        LogUtil.info("客户端查询图片耗时:" +(System.currentTimeMillis() - currentTimeMillis)+ "ms");
    }
    private JSON getResult(Object error,Object photoRelativePath) {

        HashMap<String, Object> errorMap = new HashMap<String, Object>();

        errorMap.put("stateCode",error);
        errorMap.put("photoAbsolutePath",photoRelativePath);
        return (JSON) JSONObject.toJSON(errorMap);
    }

    /**
     * 根据请求图片类型获取对应图片的绝对地址
     * @param type
     * @param tPhotodata
     * @return
     */
    private String getPhotoAbsolutePath(String type,TPhotodata tPhotodata){
        String photoAbsolutePath = "";//数据库中存储的图片md5
        String photoRootPath = ProjectConfig.getPhotoURL();//图片资源地址
        if (Integer.parseInt(type) == 0){
            photoAbsolutePath = photoRootPath + "/" + tPhotodata.getBigPhotoPath();
        }else {
            photoAbsolutePath = photoRootPath + "/" + tPhotodata.getSmallPhotoPath();
        }
        return photoAbsolutePath;
    }

//    /**
//     * 请求图片
//     * @param response
//     * @param request
//     * @throws NoSuchAlgorithmException
//     * @throws IOException
//     */
//    @GetMapping("/download")
//    public void download(HttpServletResponse response, HttpServletRequest request) throws NoSuchAlgorithmException, IOException {
//        long currentTimeMillis = System.currentTimeMillis();
//        response.setContentType("application/json;charset=UTF-8");
//        request.setCharacterEncoding("UTF-8");
//
//        String relativePath = request.getParameter("relativePath");//图片相对路径
//
//        //判断参数
//        if (null == relativePath || "".equals(relativePath)){
//            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getDataResult(""))));
//            LogUtil.error("图片相对路径为空");
//            return;
//        }
//        String photoID = getPhotoID(relativePath);
//        TPhotodata tPhotodata = tPhotodataService.selectTPhotodataByPhotoID(photoID);
//        if (null == tPhotodata){
//            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getDataResult(""))));
//            LogUtil.error("数据库中没有该图片ID相关数据");
//            return;
//        }
//        int checkStatus = tPhotodata.getCheckStatus();
//        int requestNum = tPhotodata.getRequestNum();
//        int isDelete = tPhotodata.getIsDelete();
//        tPhotodata.setRequestNum(requestNum + 1);//每次请求次数加1
//        tPhotodata.setLastRquestTime(DateUtils.dateToString(new Date()));
//        tPhotodata.setLastDownloadTime(DateUtils.dateToString(new Date()));
//        tPhotodataService.updateTPhotodata(tPhotodata);
//        String absolutePath = "";
//        if ((checkStatus == 2) && (isDelete == notDelete)){//审核通过 并且未删除
//            absolutePath = getAbsolutePath(relativePath);
//            try {
//                response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getDataResult(absolutePath))));
//            } catch (Exception e) {
//                LogUtil.error("下载图片数据失败");
//            }
//        }else {
//            try {
//                absolutePath = TPhotodataController.class.getClassLoader().getResource("default.png").getPath();
//                response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(getDataResult(absolutePath))));
//            } catch (Exception e) {
//                LogUtil.error("下载默认图片数据失败");
//            }
//        }
//        LogUtil.info("客户端下载图片耗时:" +(System.currentTimeMillis() - currentTimeMillis)+ "ms");
//    }

    /**
     * 根据发过来的相对地址获取图片ID
     * @param relativePath
     * @return
     */
    private String getPhotoID(String relativePath){
        String[] pathArr = relativePath.split("/");
        String photoName = pathArr[2];//图片ID.扩展名
        return photoName.split("\\.")[0];
    }

    private String getAbsolutePath(String relativePath){
        String photoRootPath = ProjectConfig.getPhotoURL();//图片资源地址
        return photoRootPath + "/" + relativePath;
    }

    private JSON getDataResult(String photoData) {

        HashMap<String, Object> dataMap = new HashMap<String, Object>();

        dataMap.put("photoData",photoData);
        return (JSON) JSONObject.toJSON(dataMap);
    }

    //-------- 图片审核系统后台结束 ---------
}
