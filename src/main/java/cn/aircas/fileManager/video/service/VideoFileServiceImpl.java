package cn.aircas.fileManager.video.service;

import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.commons.service.FileTypeService;
import cn.aircas.fileManager.video.dao.VideoMapper;
import cn.aircas.fileManager.video.entity.VideoInfo;
import cn.aircas.fileManager.video.entity.VideoSearchParam;
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

@Service("VIDEO-SERVICE")
public class VideoFileServiceImpl extends ServiceImpl<VideoMapper, VideoInfo> implements FileTypeService {

    @Value("${sys.rootPath}")
    String rootPath;

    @Autowired
    VideoMapper videoMapper;

    @Override
    public String downloadFileById(int fileId) {
        VideoInfo videoInfo = this.videoMapper.selectById(fileId);
        return FileUtils.getStringPath(this.rootPath, videoInfo.getPath());
    }

    /**
     * 根据id批量删除文件
     * @param idList
     */
    @Override
    public void deleteFileByIds(List<Integer> idList) {
        List<VideoInfo> videoInfoInfoList = this.videoMapper.selectBatchIds(idList);
        Assert.notEmpty(videoInfoInfoList, String.format("文本的路径列表:%s为空", videoInfoInfoList.toString()));
        for (VideoInfo videoInfo : videoInfoInfoList) {
            String filePath = FileUtils.getStringPath(this.rootPath, videoInfo.getPath());
            FileUtils.forceDelete(filePath);
        }
        List<Integer> videoIdList = videoInfoInfoList.stream().map(VideoInfo::getId).collect(Collectors.toList());
        this.videoMapper.deleteBatchIds(videoIdList);
    }

    /**
     * 根据id批量获取文件信息
     * @param fileIdList
     * @return
     */
    @Override
    public List<JSONObject> listFileInfosByIds(List<Integer> fileIdList) {
        List<VideoInfo> videoInfoList = this.videoMapper.selectBatchIds(fileIdList);
        return videoInfoList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
    }

    /**
     * 更新文件信息
     * @param fileIdList
     * @param fileInfo
     */
    @Override
    public void updateFileInfoByIds(List<Integer> fileIdList, FileInfo fileInfo) {
        UpdateWrapper<VideoInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(fileInfo.getKeywords()!=null,"keywords",fileInfo.getKeywords())
                .set(fileInfo.getSource()!=null,"source",fileInfo.getSource()).in("id",fileIdList);
        this.update(updateWrapper);
    }

    @Override
    public List<Integer> listFileIdBySearchParam(FileSearchParam fileSearchParam) {
        VideoSearchParam videoSearchParam = convertSearchParam(fileSearchParam);
        return this.videoMapper.listVideoIdBySearchParam(videoSearchParam);
    }

    /**
     * 分页查询文件信息
     * @param fileSearchParam
     * @return
     */
    @Override
    public PageResult<JSONObject> listFileInfoByPage(FileSearchParam fileSearchParam) {
        VideoSearchParam videoSearchParam = convertSearchParam(fileSearchParam);
        Page<VideoInfo> page = new Page<>(fileSearchParam.getPageNo(), fileSearchParam.getPageSize());
        IPage<VideoInfo> videoIPage = this.videoMapper.listVideoInfos(page, videoSearchParam);
        List<VideoInfo> videoInfoList = videoIPage.getRecords();
        List<JSONObject> result = videoInfoList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
        return new PageResult<>(videoIPage.getCurrent(), result, videoIPage.getTotal());
    }

    /**
     * 转换查询参数
     * @param fileSearchParam
     * @return
     */
    private VideoSearchParam convertSearchParam(FileSearchParam fileSearchParam){
        VideoSearchParam videoSearchParam = new VideoSearchParam();
        BeanUtils.copyProperties(fileSearchParam,videoSearchParam);
        videoSearchParam.setVideoIdList(fileSearchParam.getFileIdList());

        String searchParam = fileSearchParam.getSearchParam();
        if (StringUtils.isNotBlank(searchParam)){
            List<String> params = Arrays.asList(searchParam.split(" "));
            videoSearchParam.setSearchParamList(params);
        }

        videoSearchParam.setVideoName(fileSearchParam.getFileName());
        return videoSearchParam;
    }
}
