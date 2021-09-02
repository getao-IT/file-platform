package cn.aircas.fileManager.image.service;

import cn.aircas.fileManager.audio.dao.AudioMapper;
import cn.aircas.fileManager.audio.entity.AudioInfo;
import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.commons.service.FileTypeService;
import cn.aircas.fileManager.image.dao.ImageMapper;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.image.entity.ImageSearchParam;
import cn.aircas.utils.file.FileUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
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
import java.util.Random;
import java.util.stream.Collectors;

@Service("IMAGE-SERVICE")
public class ImageFileServiceImpl extends ServiceImpl<ImageMapper, Image>  implements FileTypeService {

    @Value("${sys.rootPath}")
    String rootPath;


    @Autowired
    private ImageMapper imageMapper;

    @Override
    public String downloadFileById(int fileId) {
        Image image = this.imageMapper.selectById(fileId);
        return FileUtils.getStringPath(this.rootPath,image.getPath());
    }

    /**
     * 根据id批量删除影像文件
     * @param idList
     */
    @Override
    public void deleteFileByIds(List<Integer> idList) {
        List<Image> imageInfoList = this.imageMapper.selectBatchIds(idList);
        Assert.notEmpty(imageInfoList, String.format("影像的路径列表:%s为空", imageInfoList.toString()));
        for (Image imageInfo : imageInfoList) {
            String filePath = FileUtils.getStringPath(this.rootPath, imageInfo.getPath());
            FileUtils.forceDelete(filePath);
        }
        List<Integer> imageIdList = imageInfoList.stream().map(Image::getId).collect(Collectors.toList());
        this.imageMapper.deleteBatchIds(imageIdList);
    }

    /**
     * 根据id批量获取文件信息
     * @param fileIdList
     * @return
     */
    @Override
    public List<JSONObject> listFileInfosByIds(List<Integer> fileIdList) {
        List<Image> imageList = this.imageMapper.selectBatchIds(fileIdList);
        return imageList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
    }

    /**
     * 更新文件信息
     * @param fileIdList
     * @param fileInfo
     */
    @Override
    public void updateFileInfoByIds(List<Integer> fileIdList, FileInfo fileInfo) {
        UpdateWrapper<Image> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(fileInfo.getDescription()!=null,"description",fileInfo.getDescription())
                .set(fileInfo.getKeywords()!=null,"keywords",fileInfo.getKeywords())
                .set(fileInfo.getSource()!=null,"source",fileInfo.getSource()).in("id",fileIdList);
        this.update(updateWrapper);

    }

    @Override
    public List<Integer> listFileIdBySearchParam(FileSearchParam fileSearchParam) {
        ImageSearchParam imageSearchParam = convertSearchParam(fileSearchParam);
        return this.imageMapper.listImageIdBySearchParam(imageSearchParam);
    }

    /**
     * 分页查询影像信息
     * @param fileSearchParam
     * @return
     */
    @Override
    public PageResult<JSONObject> listFileInfoByPage(FileSearchParam fileSearchParam) {
        ImageSearchParam imageSearchParam = convertSearchParam(fileSearchParam);
        Page<Image> page = new Page<>(fileSearchParam.getPageNo(), fileSearchParam.getPageSize());
        IPage<Image> imageIPage = this.imageMapper.listImageInfosByPage(page, imageSearchParam);
        List<Image> imageList = imageIPage.getRecords();
        List<JSONObject> result = imageList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
        return new PageResult<>(imageIPage.getCurrent(), result, imageIPage.getTotal());
    }

    /**
     * 转换查询参数
     * @param fileSearchParam
     * @return
     */
    private ImageSearchParam convertSearchParam(FileSearchParam fileSearchParam){
        ImageSearchParam imageSearchParam = new ImageSearchParam();
        BeanUtils.copyProperties(fileSearchParam,imageSearchParam);
        imageSearchParam.setImageIdList(fileSearchParam.getFileIdList());
        String searchParam = fileSearchParam.getSearchParam();
        if (StringUtils.isNotBlank(searchParam)){
            List<String> params = Arrays.asList(searchParam.split(" "));
            imageSearchParam.setSearchParamList(params);
        }

        imageSearchParam.setImageName(fileSearchParam.getFileName());
        return imageSearchParam;
    }

    /**
     * 列出所有影像的中心坐标点
     * @return
     */
    public JSONArray listImageCoordinate() {
        JSONArray result = new JSONArray();
        QueryWrapper<Image> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("min_lon", "min_lat", "max_lon", "max_lat", "id", "image_name");
        List<Image> imageList = this.list(queryWrapper);

        for (Image image : imageList) {
            JSONObject imageInfo = new JSONObject();
            double centerLat = (image.getMinLat() + image.getMaxLat()) / 2;
            double centerLon = (image.getMinLon() + image.getMaxLon()) / 2;

            imageInfo.put("id", image.getId());
            imageInfo.put("centerLat", centerLat);
            imageInfo.put("centerLon", centerLon);
            imageInfo.put("name", image.getImageName());
            result.add(imageInfo);
        }

        return result;
    }

}
