package cn.aircas.fileManager.commons.service;

import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import com.alibaba.fastjson.JSONObject;

import javax.security.auth.message.AuthException;
import java.util.List;

public interface FileTypeService {
    String downloadFileById(int fileId);
    void deleteFileByIds(List<Integer> idList) throws AuthException;
    void updateFileInfoByIds(List<Integer> fileIdList, FileInfo fileInfo);
    List<Integer> listFileIdBySearchParam(FileSearchParam fileSearchParam);
    PageResult<JSONObject> listFileInfoByPage(FileSearchParam searchParam);
    List<JSONObject> listFileInfosByIds(List<Integer> fileIdList,boolean content);

    int getFileUserId(int fileId);
}
