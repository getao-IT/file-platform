package cn.aircas.fileManager.video.dao;

import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.text.entity.TextInfo;
import cn.aircas.fileManager.video.entity.VideoInfo;
import cn.aircas.fileManager.video.entity.VideoSearchParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoMapper extends BaseMapper<VideoInfo> {
    void batchInsertVideoInfo(@Param("videoInfoList") List<VideoInfo> videoInfoList);
    List<Integer> listVideoIdBySearchParam(@Param(value = "videoSearchParam") VideoSearchParam videoSearchParam);
    IPage<VideoInfo> listVideoInfos(Page<VideoInfo> pageInfo, @Param(value = "videoSearchParam") VideoSearchParam videoSearchParam);
}
