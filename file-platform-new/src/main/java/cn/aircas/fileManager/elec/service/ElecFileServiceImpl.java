package cn.aircas.fileManager.elec.service;

import cn.aircas.fileManager.audio.entity.AudioSearchParam;
import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.commons.service.FileTypeService;
import cn.aircas.fileManager.elec.dao.ElecMapper;
import cn.aircas.fileManager.elec.entity.ElecContent;
import cn.aircas.fileManager.elec.entity.ElecInfo;
import cn.aircas.fileManager.elec.entity.ElecSearchParam;
import cn.aircas.fileManager.text.entity.TextContentInfo;
import cn.aircas.fileManager.text.entity.TextInfo;
import cn.aircas.fileManager.text.entity.TextSearchParam;
import cn.aircas.fileManager.web.service.FileContentService;
import cn.aircas.fileManager.web.service.UserInfoService;
import cn.aircas.utils.file.FileUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("ELEC-SERVICE")
public class ElecFileServiceImpl extends ServiceImpl<ElecMapper, ElecInfo> implements FileTypeService{

    @Value("${sys.rootPath}")
    String rootPath;

    @Autowired
    private ElecContentServiceImpl elecContentService;

    @Autowired
    ElecMapper elecMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private HttpServletRequest request;

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
                .set(fileInfo.getSource()!=null,"source",fileInfo.getSource())
                .set(fileInfo.getIsPublic() != null, "is_public", fileInfo.getIsPublic()).in("id",fileIdList);
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

        List<JSONObject> result = new ArrayList<>();
        if (fileSearchParam.isContent()) {
            Assert.isTrue(fileSearchParam.getFileIdList().size() == 1, "查看单个文件内容，但传入了多个文件ID："+fileSearchParam.getFileIdList());
            FileContentService fileContentService = fileSearchParam.getFileType().getContentService();
            if (FilenameUtils.isExtension(fileSearchParam.getFilePath(), "dat")) {
                List<ElecContent> collect = elecContentService.getElecContentFromDat(fileSearchParam);
                totalPage = collect.size();
                currentPage = 1;
                List<String> data = collect.stream().map(ElecContent::getContent).collect(Collectors.toList());
                double[] fourierTransInitData = this.getFourierTransInitData(collect);
                List<Double> fourierTransformData = this.getFourierTransformData(fourierTransInitData);
                JSONObject dataJsonObject = new JSONObject();
                dataJsonObject.put("contentData", data);
                result.add(dataJsonObject);
                JSONObject fourierJsonObject = new JSONObject();
                fourierJsonObject.put("fourierTransformData", fourierTransformData);
                result.add(fourierJsonObject);
            } else {
                PageResult<JSONObject> content = fileContentService.getContent(fileSearchParam.getPageSize(), fileSearchParam.getPageNo(), fileSearchParam.getFileIdList().get(0));
                totalPage = content.getTotalCount();
                currentPage = content.getPageNo();
                result = content.getResult();
            }
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

    @Override
    public int getFileUserId(int fileId) {
        ElecInfo elecInfo = this.elecMapper.selectById(fileId);
        return elecInfo.getUserId();
    }

    /**
     * 转换查询参数
     * @param fileSearchParam
     * @return
     */
    private ElecSearchParam convertSearchParam(FileSearchParam fileSearchParam){
        ElecSearchParam elecSearchParam = new ElecSearchParam();
        BeanUtils.copyProperties(fileSearchParam,elecSearchParam);
        elecSearchParam.setElecIdList(fileSearchParam.getFileIdList());
        String searchParam = fileSearchParam.getSearchParam();
        if (StringUtils.isNotBlank(searchParam)){
            List<String> params = Arrays.asList(searchParam.split(" "));
            elecSearchParam.setSearchParamList(params);
        }
        JSONObject userInfo = this.userInfoService.getUserInfoByToken(request.getHeader("token")).getData();
        elecSearchParam.setAdminLevel(userInfo.getString("admin_level"));
        elecSearchParam.setUserId(userInfo.getInteger("id"));
        elecSearchParam.setElecName(fileSearchParam.getFileName());
        return elecSearchParam;
    }


    /**
     * 获取傅立叶转换数据
     * @param initData
     * @return
     */
    public List<Double> getFourierTransformData(double[] initData) {
        List<Double> result = new ArrayList<>();
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] complexes = transformer.transform(initData, TransformType.FORWARD);
        for (int i = 0; i < complexes.length; i++) {
            double sqrt = Math.sqrt(Math.pow(complexes[i].getReal(), 2) + Math.pow(complexes[i].getImaginary(), 2));
            result.add(sqrt);
        }
        return result;
    }

    /**
     * 初始化傅里叶转换所用数据
     * @param collect
     */
    private double[] getFourierTransInitData(List<ElecContent> collect) {
        int size = collect.size();
        double[] doubleData = null;
        int powerOfTwo = 1<<(int)(Math.log(size)/Math.log(2))+1;
        if (size == 0) {
            throw new IllegalArgumentException("傅里叶转换数据为空");
        }
        List<Double> list = collect.stream().map(ElecContent::getContent).map(Double::parseDouble).collect(Collectors.toList());
        for (int i = size; i < powerOfTwo; i++) {
            list.add((double) 0);
        }
        doubleData = list.stream().mapToDouble(Double::doubleValue).toArray();
        return doubleData;
    }
}
