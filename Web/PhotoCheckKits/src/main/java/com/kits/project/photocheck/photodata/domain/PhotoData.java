package com.kits.project.photocheck.photodata.domain;

import org.springframework.web.multipart.MultipartFile;

public class PhotoData {
    private String desc1;
    private String desc2;
    private String desc3;
    private String photoId;
    private MultipartFile photoData;
    private String extName;

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public MultipartFile getPhotoData() {
        return photoData;
    }

    public void setPhotoData(MultipartFile photoData) {
        this.photoData = photoData;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }
}
