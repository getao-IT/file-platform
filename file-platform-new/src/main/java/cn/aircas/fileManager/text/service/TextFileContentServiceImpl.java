package cn.aircas.fileManager.text.service;

import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.text.dao.TextContentMapper;
import cn.aircas.fileManager.text.entity.TextContentInfo;
import cn.aircas.fileManager.text.entity.TextInfo;
import cn.aircas.fileManager.text.entity.TextSearchParam;
import cn.aircas.fileManager.web.service.FileContentService;
import cn.aircas.utils.file.FileUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("TEXT-CONTENT")
public class TextFileContentServiceImpl extends ServiceImpl<TextContentMapper, TextContentInfo> implements FileContentService<TextInfo> {
    @Value("${sys.rootPath}")
    public String rootPath;

    public PageResult<TextContentInfo> getContentByPage(TextSearchParam textSearchParam){
        List<Integer> fileIdList = textSearchParam.getTextIdList();
        PageResult<TextContentInfo> pageResult = new PageResult<>(textSearchParam.getPageNo(),new ArrayList<>(),0);
        if (fileIdList.isEmpty())
            return pageResult;

        Page<TextContentInfo> page = new Page<>(textSearchParam.getPageNo(),textSearchParam.getPageSize());
        IPage<TextContentInfo> iPage =  this.baseMapper.listTextContents(page,textSearchParam);
        pageResult.setPageNo(iPage.getCurrent());
        pageResult.setTotalCount(iPage.getTotal());
        pageResult.setResult(iPage.getRecords());

        return pageResult;
    }

    /**
     * 保存文件内容
     * @param textInfoList
     */
    public void parseTextContent(List<TextInfo> textInfoList){
        for (TextInfo textInfo : textInfoList) {
            parseTextContent(textInfo);
        }
    }

    /**
     * 保存文件内容
     * @param textInfo
     */
    @Override
    public void parseTextContent(TextInfo textInfo){
        List<TextContentInfo> textContentInfoList = new ArrayList<>();
        File textFile = FileUtils.getFile(this.rootPath,textInfo.getPath());

        int count = 1;
        try(LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(textFile))){
            String content ;
            while((content=lineNumberReader.readLine())!=null){
                TextContentInfo textContentInfo = new TextContentInfo();
                textContentInfo.setContent(content);
                textContentInfo.setLineNumber(count);
                textContentInfo.setTextFileId(textInfo.getId());
                textContentInfoList.add(textContentInfo);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.saveBatch(textContentInfoList);
    }

    @Override
    public PageResult<JSONObject> getContent(int pageSize, long pageNo, int fileId) {
        return null;
    }

    /**
     * 批量查询文件内容所属文件信息成功
     * @param contentIds
     * @return
     */
    @Override
    public Map<Integer, TextInfo> getFileByContentId(Set<Integer> contentIds) {
        return this.baseMapper.getFileByContentId(contentIds);
    }
}
