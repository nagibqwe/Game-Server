package com.kits.project.clientlog.clientlogdata.controller;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.security.MessageDigest;

import com.kits.common.constant.Constants;
import com.kits.common.utils.StringUtils;
import com.kits.common.utils.file.FileUtils;
import com.kits.framework.config.ProjectConfig;
import com.kits.project.clientlog.uploader.controller.ClientLogUploadController;
import com.kits.project.clientlog.utils.LogFileDownloadUtils;
import com.kits.project.clientlog.utils.LogFileUtils;
import com.kits.project.clientlog.utils.LogFileZipUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.apache.commons.io.IOUtils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
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
import com.kits.project.clientlog.clientlogdata.domain.Clientlog;
import com.kits.project.clientlog.clientlogdata.service.IClientlogService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 客户端日志Controller
 * 
 * @author gzg
 * @date 2021-06-18
 */
@Controller
@RequestMapping("/clientlog/clientlogdata")
public class ClientlogController extends BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(ClientlogController.class);
    private String prefix = "clientlog/clientlogdata";

    @Autowired
    private IClientlogService clientlogService;

    @RequiresPermissions("clientlog:clientlogdata:view")
    @GetMapping()
    public String clientlogdata()
    {
        return prefix + "/clientlogdata";
    }

    /**
     * 查询客户端日志列表
     */
    @RequiresPermissions("clientlog:clientlogdata:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Clientlog clientlog)
    {
        startPage();
        List<Clientlog> list = clientlogService.selectClientlogList(clientlog);
        return getDataTable(list);
    }

    /**
     * 导出客户端日志列表
     */
    @RequiresPermissions("clientlog:clientlogdata:export")
    @Log(title = "客户端日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Clientlog clientlog)
    {
        List<Clientlog> list = clientlogService.selectClientlogList(clientlog);
        ExcelUtil<Clientlog> util = new ExcelUtil<Clientlog>(Clientlog.class);
        return util.exportExcel(list, "客户端日志数据");
    }
    /**
     * 删除客户端日志
     */
    @RequiresPermissions("clientlog:clientlogdata:remove")
    @Log(title = "客户端日志", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(clientlogService.deleteClientlogByIds(ids));
    }

    /**
     * 客户端日志下载操作
     * @param response
     * @param request
     */
    @RequiresPermissions("clientlog:clientlogdata:downlod")
    @GetMapping("/download")
    public void download( HttpServletResponse response, HttpServletRequest request){
        //0.从请求中获取参数信息
        String strID = request.getParameter("id");
        if(strID == null || strID == ""){
            logger.error("请求下载没有获得对应的参数信息.");
            showAlert("没有序号参数,下载日志文件失败!",response);
        }
        strID = strID.trim();

        try{
            //1.根据id查询数据库,获得日志目录,以及zip文件的路径
            Long id = Long.valueOf(strID);
            Clientlog model = clientlogService.selectClientlogById(id);
            String dir = LogFileUtils.getLogDirPath(model.getUuid(),model.getGame());
            String zipFile = LogFileUtils.getZipFilePath(id);
            File file = new File(zipFile);
            boolean needGenZip = false;

            //2.判断zip文件是否存在,
            if(file.exists()){
                //2.1 如果存在,就对比md5.当md5不相同,就说明要重新生成zip文件,否则直接下载
                String md5 = LogFileUtils.getMD5(file);
                logger.info("比对MD5:"+model.getFilemd5()+":::"+md5);
                if(model.getFilemd5().equals(md5)){
                    needGenZip = true;
                    file.delete();
                }
            }else{
                //2.2 如果zip文件不存在就说明当前没有zip文件,就需要重新生成zip文件
                needGenZip = true;
            }

            //3.生成zip文件
            if (needGenZip){
                logger.info("需要重新生成zip文件:"+id);
                //3.1 压缩指定目录
                LogFileUtils.mkParentDirs(file);
                LogFileZipUtils.zipDir(dir,zipFile);
                //3.2 对新生成的文件,进行生成md5处理
                String md5 = LogFileUtils.getMD5(file);
                //3.3 把生成的md5码保存到数据库
                model.setFilemd5(md5);
                clientlogService.updateClientlog(model);
            }

            //4.把文件推向远端
            LogFileDownloadUtils.download(file.getAbsolutePath(),response);
        }catch (Exception e){
            logger.error("下载日志文件失败.参数:" + strID, e);
            showAlert("下载日志文件失败!"+e.getMessage(),response);
        }
    }

    /**
     * 展示提示
     * @param msg
     * @param response
     */
    private void showAlert(String msg,HttpServletResponse response){
        try
        {
            response.setContentType("text/html; charset=UTF-8");//注意text/html，和application/html
            response.getWriter().print("<html><body><script type='text/javascript'>alert('" + msg + "');history.back();</script></body></html>");
            response.getWriter().close();
        }catch (Exception ex){
            logger.error("showAlert异常." + msg,ex);
        }
    }






}
