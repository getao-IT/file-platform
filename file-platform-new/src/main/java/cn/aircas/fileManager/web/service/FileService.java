package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.text.entity.TextInfo;
import cn.aircas.fileManager.web.entity.database.FileTextInfo;
import cn.aircas.fileManager.web.entity.enums.FileType;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;
import org.aspectj.lang.reflect.NoSuchPointcutException;

import javax.security.auth.message.AuthException;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FileService {
    List<String> getFileType();
    List<JSONObject> listFolderFiles(String path);
    void deleteFilesByIds(List<Integer> idList, FileType fileType) throws Exception;
    List<Integer> listFileIdBySearchParam(FileSearchParam fileSearchParam);
    PageResult<JSONObject> listFileByPage(FileSearchParam fileSearchParam);
    PageResult<JSONObject> getContent(int pageSize, int pageNo,FileType fileType, int fileId);
    void updateFileInfo(List<Integer> fileIdList, FileType fileType, FileInfo fileInfo) throws AuthException, NoSuchPointcutException;
    List<JSONObject> getFileInfoByIds(List<Integer> idList,FileType fileType, boolean content);
    int getFileLineCount(File source);
    Map<Integer, TextInfo> getFileByContentId(FileType fileType, Set<Integer> contentIds);
    void makeImageSlice(FileType fileType, int id, double minLon, double minLat, int width, int height, String sliceInsertPath, Boolean storage);
    void makeImageAllGeoSlice(FileType fileType, int id, int width, int height, String sliceInsertPath, int step, Boolean storage);
    int getFileUserId(int fileId , FileType fileType);
}
