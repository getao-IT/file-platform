package cn.aircas.fileManager.web.service.impl;

import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.text.entity.TextInfo;
import cn.aircas.fileManager.web.entity.database.FileTextInfo;
import cn.aircas.fileManager.web.entity.enums.FileType;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.web.service.FileContentService;
import cn.aircas.fileManager.web.service.FileService;
import cn.aircas.fileManager.commons.service.FileTypeService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Vanishrain
 * 文件管理服务实现类
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Value("${sys.uploadRootPath}")
    String uploadRootPath;

    @Override
    public List<String> getFileType() {
        List<String> fileTypeList = new ArrayList<>();
        for (FileType fileType : FileType.values()) {
            fileTypeList.add(fileType.getValue().toUpperCase());
        }
        return fileTypeList;
    }

    @Override
    public PageResult<JSONObject> getContent(int pageSize, int pageNo,FileType fileType, int fileId) {
        FileContentService fileContentService = fileType.getContentService();
        return fileContentService.getContent(pageSize, pageNo,fileId);
    }

    /**
     * 列出文件夹下的文件夹
     * @param path
     * @return
     */
    @Override
    public List<String> listFolderFiles(String path) {
        path = StringUtils.isBlank(path) ? File.separator : path;
        path = FilenameUtils.normalizeNoEndSeparator(this.uploadRootPath + File.separator + path);
        File[] files = new File(path).listFiles();

        if (files == null){
            log.error("路径：{} 不存在",path);
            return new ArrayList<>();
        }
        List<String> filePathList = Arrays.stream(files)
                .filter(file -> file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());

        return filePathList;
    }


    /**
     * 根据id批量删除文件
     * @param idList
     * @param fileType
     */
    @Override
    public void deleteFilesByIds(List<Integer> idList, FileType fileType) {
        FileTypeService fileTypeService = fileType.getService();
        fileTypeService.deleteFileByIds(idList);
    }

    @Override
    public List<Integer> listFileIdBySearchParam(FileSearchParam fileSearchParam) {
        String searchParam = fileSearchParam.getSearchParam();
        if (searchParam!=null){
            String[] params = searchParam.split(" ");
            List<String> paramList = Arrays.asList(params);
            fileSearchParam.setSearchParamList(paramList);
        }
        FileType fileType = fileSearchParam.getFileType();
        return fileType.getService().listFileIdBySearchParam(fileSearchParam);
    }

    @Override
    public void updateFileInfo(List<Integer> fileIdList, FileType fileType, FileInfo fileInfo) {
        FileTypeService fileTypeService = fileType.getService();
        fileTypeService.updateFileInfoByIds(fileIdList,fileInfo);
    }

    /**
     * 分页查询文件信息
     * @param fileSearchParam
     * @return
     */
    @Override
    public PageResult<JSONObject> listFileByPage(FileSearchParam fileSearchParam) {
        addEndTimeOneDay(fileSearchParam);
        FileTypeService fileTypeService = fileSearchParam.getFileType().getService();
        return fileTypeService.listFileInfoByPage(fileSearchParam);
    }

    /**
     * 根据id批量获取文件信息
     * @param idList
     * @param fileType
     * @return
     */
    @Override
    public List<JSONObject> getFileInfoByIds(List<Integer> idList, FileType fileType, boolean content) {
        FileTypeService fileTypeService = fileType.getService();
        return fileTypeService.listFileInfosByIds(idList,content);
    }

    /**
     * 获取文件条数
     * @param source
     * @return
     */
    @Override
    public int getFileLineCount(File source) {
        int count = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream(source);
            InputStreamReader streamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            while ((line=reader.readLine()) != null) {
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 批量查询文件内容所属文件信息成功
     * @param fileType
     * @param contentIds
     * @return
     */
    @Override
    public Map<Integer, TextInfo> getFileByContentId(FileType fileType, Set<Integer> contentIds) {
        FileContentService contentService = fileType.getContentService();
        Map<Integer, TextInfo> fileByContentId = contentService.getFileByContentId(contentIds);
        return fileByContentId;
    }

    public void addEndTimeOneDay(FileSearchParam fileSearchParam){
        Date endTime = fileSearchParam.getEndTime();
        if (endTime == null)
            return;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(endTime);
        calendar.add(Calendar.DATE,1);
        fileSearchParam.setEndTime(calendar.getTime());
    }



}
