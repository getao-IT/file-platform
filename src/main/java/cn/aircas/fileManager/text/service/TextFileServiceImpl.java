package cn.aircas.fileManager.text.service;

import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.commons.service.FileTypeService;
import cn.aircas.fileManager.text.dao.TextMapper;
import cn.aircas.fileManager.text.entity.TextInfo;
import cn.aircas.fileManager.text.entity.TextSearchParam;
import cn.aircas.utils.file.FileUtils;
import com.alibaba.fastjson.JSONObject;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("TEXT-SERVICE")
public class TextFileServiceImpl extends ServiceImpl<TextMapper, TextInfo> implements FileTypeService {


    @Value("${sys.rootPath}")
    String rootPath;

    @Autowired
    TextMapper textMapper;

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
    public List<JSONObject> listFileInfosByIds(List<Integer> fileIdList) {
        List<TextInfo> textList = this.textMapper.selectBatchIds(fileIdList);
        return textList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
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
                .set(fileInfo.getSource()!=null,"source",fileInfo.getSource()).in("id",fileIdList);
        this.update(updateWrapper);
    }

    @Override
    public List<Integer> listFileIdBySearchParam(FileSearchParam fileSearchParam) {
        TextSearchParam textSearchParam = convertSearchParam(fileSearchParam);
        return this.textMapper.listTextIdBySearchParam(textSearchParam);
    }

    /**
     * 分页查询文件信息
     * @param fileSearchParam
     * @return
     */
    @Override
    public PageResult<JSONObject> listFileInfoByPage(FileSearchParam fileSearchParam) {
        TextSearchParam textSearchParam = convertSearchParam(fileSearchParam);
        Page<TextInfo> page = new Page<>(fileSearchParam.getPageNo(), fileSearchParam.getPageSize());
        IPage<TextInfo> textIPage = this.textMapper.listTextInfos(page, textSearchParam);
        List<TextInfo> imageList = textIPage.getRecords();
        List<JSONObject> result = imageList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
        return new PageResult<>(textIPage.getCurrent(), result, textIPage.getTotal());
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

        textSearchParam.setTextName(fileSearchParam.getFileName());
        return textSearchParam;
    }
}
