package cn.aircas.fileManager.text.service;

import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.commons.service.FileTypeService;
import cn.aircas.fileManager.text.dao.TextMapper;
import cn.aircas.fileManager.text.entity.TextContentInfo;
import cn.aircas.fileManager.text.entity.TextInfo;
import cn.aircas.fileManager.text.entity.TextSearchParam;
import cn.aircas.fileManager.web.service.UserInfoService;
import cn.aircas.utils.file.FileUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service("TEXT-SERVICE")
public class TextFileServiceImpl extends ServiceImpl<TextMapper, TextInfo> implements FileTypeService {


    @Value("${sys.rootPath}")
    String rootPath;

    @Autowired
    TextMapper textMapper;

    @Autowired
    private TextFileContentServiceImpl textFileContentService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public String downloadFileById(int fileId) {
        TextInfo text = this.textMapper.selectById(fileId);
        return FileUtils.getStringPath(this.rootPath,text.getPath());
    }

    /**
     * 根据id批量删除影像文件
     * @param idList
     */
    @Override
    public void deleteFileByIds(List<Integer> idList) {
        List<TextInfo> textInfoList = this.textMapper.selectBatchIds(idList);
        Assert.notEmpty(textInfoList, String.format("文本的路径列表:%s为空", textInfoList.toString()));
        for (TextInfo textInfo : textInfoList) {
            String filePath = FileUtils.getStringPath(this.rootPath, textInfo.getPath());
            FileUtils.forceDelete(filePath);
        }
        List<Integer> textIdList = textInfoList.stream().map(TextInfo::getId).collect(Collectors.toList());
        this.textMapper.deleteBatchIds(textIdList);
    }

    /**
     * 根据id批量获取文件信息
     * @param fileIdList
     * @return
     */
    @Override
    public List<JSONObject> listFileInfosByIds(List<Integer> fileIdList, boolean content) {
        if (content){
            Set<Integer> idList = new HashSet<>();
            List<TextContentInfo> textContentInfoList = (List<TextContentInfo>) this.textFileContentService.listByIds(fileIdList);
            for (TextContentInfo textContentInfo : textContentInfoList) {
                int fileId = textContentInfo.getTextFileId();
                idList.add(fileId);
            }
            fileIdList = new ArrayList<>(idList);
        }

        List<TextInfo> textList = this.textMapper.selectBatchIds(fileIdList);
        return textList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
    }

    /**
     * 更新文件信息
     * @param fileIdList
     * @param fileInfo
     */
    @Override
    public int getFileUserId(int fileId) {
        TextInfo textInfo = getById(fileId);
        return textInfo.getUserId();
    }

    /**
     * 更新文件信息
     * @param fileIdList
     * @param fileInfo
     */
    @Override
    public void updateFileInfoByIds(List<Integer> fileIdList, FileInfo fileInfo) {
        UpdateWrapper<TextInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(fileInfo.getKeywords()!=null,"keywords",fileInfo.getKeywords())
                .set(fileInfo.getSource()!=null,"source",fileInfo.getSource())
                .set(fileInfo.getIsPublic() != null, "is_public", fileInfo.getIsPublic()).in("id",fileIdList);
        this.update(updateWrapper);
    }

    @Override
    public List<Integer> listFileIdBySearchParam(FileSearchParam fileSearchParam) {
        TextSearchParam textSearchParam = convertSearchParam(fileSearchParam);
        if (textSearchParam.isContent()) {
            List<Integer> textContentIdList = new ArrayList<>();
            List<Integer> fileIdList = fileSearchParam.getFileIdList();
            if (fileIdList.isEmpty())
                return textContentIdList;
            QueryWrapper<TextContentInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id").eq("text_file_id",fileIdList.get(0));
            List<TextContentInfo> textContentInfoList = this.textFileContentService.list(queryWrapper);
            textContentIdList = textContentInfoList.stream().map(TextContentInfo::getId).collect(Collectors.toList());
            return textContentIdList;
        }
        return this.textMapper.listTextIdBySearchParam(textSearchParam);
    }

    /**
     * 分页查询文件信息
     * @param fileSearchParam
     * @return
     */
    @Override
    public PageResult<JSONObject> listFileInfoByPage(FileSearchParam fileSearchParam) {
        long totalPage;
        long currentPage;

        List<JSONObject> result;
        TextSearchParam textSearchParam = convertSearchParam(fileSearchParam);
        Page<TextInfo> page = new Page<>(fileSearchParam.getPageNo(), fileSearchParam.getPageSize());
        if (fileSearchParam.isContent()){
            PageResult<TextContentInfo> textContentInfoPageResult = this.textFileContentService.getContentByPage(textSearchParam);
            List<TextContentInfo> textContentInfoList = textContentInfoPageResult.getResult();
            currentPage = textContentInfoPageResult.getPageNo();
            totalPage = textContentInfoPageResult.getTotalCount();
            result = textContentInfoList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
        }else {
            IPage<TextInfo> textIPage = this.textMapper.listTextInfos(page, textSearchParam);
            List<TextInfo> imageList = textIPage.getRecords();
            currentPage = textIPage.getCurrent();
            totalPage = textIPage.getTotal();
            result = imageList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
        }

        return new PageResult<>(currentPage, result, totalPage);
    }

    /**
     * 分页获取文件内容
     * @param fileSearchParam
     * @return
     */
    private PageResult<JSONObject> getFileContentByPage(FileSearchParam fileSearchParam){
        List<JSONObject> result = new ArrayList<>();
        PageResult<JSONObject> pageResult = new PageResult<>(fileSearchParam.getPageNo(),new ArrayList<>(),0);
        List<Integer> fileIdList = fileSearchParam.getFileIdList();
        if (fileIdList.isEmpty())
            return pageResult;

        int textFileId = fileSearchParam.getFileIdList().get(0);
        TextInfo textInfo = this.getById(textFileId);
        File textFile = FileUtils.getFile(this.rootPath,textInfo.getPath());

        int totalCount = textInfo.getLineCount();
        long beginLine = Math.min(fileSearchParam.getPageSize() * (fileSearchParam.getPageNo() - 1),totalCount);
        long endLine = Math.min(beginLine + fileSearchParam.getPageSize(), totalCount);
        if ( !textFile.exists() || (beginLine == endLine))
            return pageResult;

        try {
            int skipLineCount = 0;
            LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(textFile));
            while (skipLineCount < beginLine){
                lineNumberReader.readLine();
                skipLineCount++;
            }

            while(beginLine < endLine){
                JSONObject content = new JSONObject();
                String lineContent = lineNumberReader.readLine();
                content.put("data",lineContent);
                result.add(content);
                beginLine++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        pageResult.setResult(result);
        pageResult.setTotalCount(textInfo.getLineCount());
        return pageResult;
    }

    /**
     * 转换查询参数
     * @param fileSearchParam
     * @return
     */
    private TextSearchParam convertSearchParam(FileSearchParam fileSearchParam){
        TextSearchParam textSearchParam = new TextSearchParam();
        BeanUtils.copyProperties(fileSearchParam,textSearchParam);
        textSearchParam.setTextIdList(fileSearchParam.getFileIdList());
        String searchParam = fileSearchParam.getSearchParam();
        if (StringUtils.isNotBlank(searchParam)){
            List<String> params = Arrays.asList(searchParam.split(" "));
            textSearchParam.setSearchParamList(params);
        }
        JSONObject userInfo = this.userInfoService.getUserInfoByToken(request.getHeader("token")).getData();
        textSearchParam.setAdminLevel(userInfo.getString("admin_level"));
        textSearchParam.setUserId(userInfo.getInteger("id"));
        textSearchParam.setTextName(fileSearchParam.getFileName());
        return textSearchParam;
    }
}
