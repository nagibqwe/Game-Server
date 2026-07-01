package com.kits.project.photocheck.photodata.utils;

import com.kits.common.utils.DateUtils;
import com.kits.common.utils.LogUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.HashMap;

public class PhotoFileUploadUtils {
    private static String DEFAULT_PREVFIX = "tmp";
    public static HashMap<String,String> uploadPhoto(MultipartFile photoData, String photofile, String extName, Date dateNow, Long id) throws IOException {

        String dateTime = DateUtils.dateTime(dateNow);
        //如果保存上传文件的根目录不存在,创建根目录
        File imageFile = new File(photofile);
        if (!imageFile.exists()){
            imageFile.mkdirs();
        }

        //如果上传时间YYYYMMDD的目录不存在，创建上传时间的目录
        photofile = photofile + "/" + dateTime;
        String smallfile = photofile;
        imageFile = new File(photofile);
        if(!imageFile.exists())
        {
            imageFile.mkdirs();
        }

        //原图的目录不存在，创建该目录
        photofile = photofile + "/" + "big";
        imageFile = new File(photofile);
        if(!imageFile.exists())
        {
            imageFile.mkdirs();
        }

        //小图的目录不存在，创建该目录

        String smallPath = smallfile + "/" + "small";
        File smallFile = new File(smallPath);
        if(!smallFile.exists())
        {
            smallFile.mkdirs();
        }
        String tempFileName = DEFAULT_PREVFIX + String.valueOf(id) + extName;
        String tempAbsolutePath = photofile + tempFileName;
        //将图片文件写入临时地址
        upload(tempAbsolutePath,photoData);
        //获取原图的MD5
        String photoMD5 = PhotoFileUtils.getMD5(new File(tempAbsolutePath));
        String photoID = photoMD5 + String.valueOf(id);

        //将临时地址修改为最终的地址
        File file=new File(tempAbsolutePath);
        String bigResultPath = photofile + "/" + photoID + extName;
        String smallResultPath = smallPath + "/" + photoID + extName;

        file.renameTo(new File(bigResultPath));//更改文件名操作


//        //创建输出流
//        FileOutputStream outStream = new FileOutputStream(bigResultPath);
//        //写入数据
//        outStream.write(photoDataBytes);
//        //关闭输出流
//        outStream.close();

        //生成小图128*128
        PhotoFileUtils.thumbnailImage(bigResultPath,128,128,smallResultPath,true);

        //需要存储的数据集合
        HashMap<String,String> resultMap = new HashMap<>();
        String bigPhotoSize = PhotoFileUtils.getImgSize(bigResultPath);
        String smallPhotoSize = PhotoFileUtils.getImgSize(smallResultPath);
        resultMap.put("photoID",photoID);
        resultMap.put("bigAbsolutePath",bigResultPath);
        resultMap.put("smallAbsolutePath",smallResultPath);
        resultMap.put("bigPhotoSize",bigPhotoSize);
        resultMap.put("smallPhotoSize",smallPhotoSize);
        return resultMap;
    }

    /**
     * absolutePath 上传的文件
     * @param absolutePath
     * @param file
     * @throws IOException
     */
    public static void upload(String absolutePath, MultipartFile file){

        File desc = new File(absolutePath);
        try {
            file.transferTo(desc.getAbsoluteFile());//获取最终的绝对路径 Windows下会找到最终盘符,linux则为最终路径
        } catch (IOException e) {
            LogUtil.error("写入临时图片文件出错");
        }
    }

public static void main(String[] args) {
    String ss = PhotoFileUtils.bytesToHexString(PhotoFileUtils.image2byte("C:/Users/Administrator/Desktop/veer-305358648.jpg"));
    System.out.println(ss);
}
}
