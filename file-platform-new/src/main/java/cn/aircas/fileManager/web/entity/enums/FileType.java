package cn.aircas.fileManager.web.entity.enums;

import cn.aircas.fileManager.commons.service.FileTypeService;
import cn.aircas.fileManager.web.service.FileContentService;
import cn.aircas.fileManager.web.service.FileTypeTransferService;
import cn.aircas.fileManager.web.utils.SpringContextUtil;

public enum FileType {
    IMAGE("IMAGE"),VIDEO("VIDEO"),TEXT("TEXT"), AUDIO("AUDIO"),ELEC("ELEC");

    private String value;

    FileType(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }


    public void setValue(String value){
        this.value = value;
    }

    public FileTypeService getService(){
        String serviceName = String.format("%s-SERVICE",this.value.toUpperCase());
        return (FileTypeService) SpringContextUtil.getBean(serviceName);
    }

    public FileTypeTransferService getTransferService(){
        String serviceName = String.format("%s-TRANSFER-SERVICE",this.value.toUpperCase());
        return (FileTypeTransferService) SpringContextUtil.getBean(serviceName);
    }

    public FileContentService getContentService(){
        String serviceName = String.format("%s-CONTENT",this.value.toUpperCase());
        return (FileContentService) SpringContextUtil.getBean(serviceName);
    }





}
