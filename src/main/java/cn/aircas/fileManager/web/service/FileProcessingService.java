package cn.aircas.fileManager.web.service;



public interface FileProcessingService {
    void formatConverter(int fileId, String format,String source,String keywords,boolean isPublic);
}
