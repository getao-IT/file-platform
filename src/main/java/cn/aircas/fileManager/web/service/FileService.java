package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.web.entity.enums.FileType;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;

import java.util.List;

public interface FileService {
    List<String> getFileType();
    List<String> listFolderFiles(String path);
    void deleteFilesByIds(List<Integer> idList, FileType fileType);
    List<Integer> listFileIdBySearchParam(FileSearchParam fileSearchParam);
    PageResult<JSONObject> listFileByPage(FileSearchParam fileSearchParam);
    List<JSONObject> getFileInfoByIds(List<Integer> idList,FileType fileType);
    void updateFileInfo(List<Integer> fileIdList, FileType fileType, FileInfo fileInfo);

}
