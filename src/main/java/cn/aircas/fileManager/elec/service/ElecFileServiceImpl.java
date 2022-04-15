package cn.aircas.fileManager.elec.service;

import cn.aircas.fileManager.audio.entity.AudioSearchParam;
import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.commons.service.FileTypeService;
import cn.aircas.fileManager.elec.dao.ElecMapper;
import cn.aircas.fileManager.elec.entity.ElecInfo;
import cn.aircas.fileManager.elec.entity.ElecSearchParam;
import cn.aircas.fileManager.text.entity.TextContentInfo;
import cn.aircas.fileManager.text.entity.TextInfo;
import cn.aircas.fileManager.text.entity.TextSearchParam;
import cn.aircas.fileManager.web.service.FileContentService;
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

@Service("ELEC-SERVICE")
public class ElecFileServiceImpl extends ServiceImpl<ElecMapper, ElecInfo> implements FileTypeService{

    @Value("${sys.rootPath}")
    String rootPath;

    @Autowired
    ElecMapper elecMapper;

    @Override
    public String downloadFileById(int fileId) {
        ElecInfo elecInfo = this.elecMapper.selectById(fileId);
        return FileUtils.getStringPath(this.rootPath,elecInfo.getPath());
    }

    /**
     * 根据id批量删除文件
     * @param idList
     */
    @Override
    public void deleteFileByIds(List<Integer> idList) {
        List<ElecInfo> elecInfoList = this.elecMapper.selectBatchIds(idList);
        Assert.notEmpty(elecInfoList,String.format("文本的路径列表:%s为空",elecInfoList.toString()));
        for (ElecInfo elecInfo : elecInfoList) {
            String elecPath = FileUtils.getStringPath(this.rootPath, elecInfo.getPath());
            FileUtils.forceDelete(elecPath);
        }
        List<Integer> elecIdList = elecInfoList.stream().map(ElecInfo::getId).collect(Collectors.toList());
        this.elecMapper.deleteBatchIds(elecIdList);

    }

    /**
     * 更新文件信息
     * @param fileIdList
     * @param fileInfo
     */
    @Override
    public void updateFileInfoByIds(List<Integer> fileIdList, FileInfo fileInfo) {
        UpdateWrapper<ElecInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(fileInfo.getKeywords()!=null,"keywords",fileInfo.getKeywords())
                .set(fileInfo.getSource()!=null,"source",fileInfo.getSource()).in("id",fileIdList);
        this.update(updateWrapper);
    }

    /**
     * 根据id批量获取文件信息
     * @param fileSearchParam
     * @return
     */
    @Override
    public List<Integer> listFileIdBySearchParam(FileSearchParam fileSearchParam) {
        ElecSearchParam elecSearchParam = convertSearchParam(fileSearchParam);
        return this.elecMapper.listElecIdBySearchParam(elecSearchParam);
    }

    /***
     *分页查询文件信息 TODO
     * @param fileSearchParam
     * @return
     */
    @Override
    public PageResult<JSONObject> listFileInfoByPage(FileSearchParam fileSearchParam) {
        long totalPage = 0;
        long currentPage = 0;

        List<JSONObject> result = null;
        if (fileSearchParam.isContent()) {
            FileContentService fileContentService = fileSearchParam.getFileType().getContentService();
            PageResult<JSONObject> content = fileContentService.getContent(fileSearchParam.getPageSize(), fileSearchParam.getPageNo(), fileSearchParam.getFileIdList().get(0));
            totalPage = content.getTotalCount();
            currentPage = content.getPageNo();
            result = content.getResult();
        } else {
            ElecSearchParam elecSearchParam = convertSearchParam(fileSearchParam);
            Page<ElecInfo> page = new Page<>(fileSearchParam.getPageNo(), fileSearchParam.getPageSize());
            IPage<ElecInfo> elecIPage = this.elecMapper.listElecInfos(page, elecSearchParam);
            List<ElecInfo> elecInfoList = elecIPage.getRecords();
            totalPage = elecIPage.getTotal();
            currentPage = elecIPage.getCurrent();
            result = elecInfoList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
        }

        return new PageResult<>(currentPage,result,totalPage);
    }

    /***
     * 根据id批量获取文件信息
     * @param fileIdList
     * @param content
     * @return
     */
    @Override
    public List<JSONObject> listFileInfosByIds(List<Integer> fileIdList, boolean content) {
        List<ElecInfo> elecInfoList = this.elecMapper.selectBatchIds(fileIdList);
        return elecInfoList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
    }

    /**
     * 转换查询参数
     * @param fileSearchParam
     * @return
     */
    private ElecSearchParam convertSearchParam(FileSearchParam fileSearchParam){
        ElecSearchParam elecSearchParam = new ElecSearchParam();
        System.out.println(elecSearchParam);
        BeanUtils.copyProperties(fileSearchParam,elecSearchParam);
        System.out.println(elecSearchParam);
        elecSearchParam.setElecIdList(fileSearchParam.getFileIdList());
        String searchParam = fileSearchParam.getSearchParam();
        if (StringUtils.isNotBlank(searchParam)){
            List<String> params = Arrays.asList(searchParam.split(" "));
            elecSearchParam.setSearchParamList(params);
        }

        elecSearchParam.setElecName(fileSearchParam.getFileName());
        return elecSearchParam;
    }
}
