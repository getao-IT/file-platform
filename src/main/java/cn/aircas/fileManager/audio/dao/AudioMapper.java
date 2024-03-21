package cn.aircas.fileManager.audio.dao;

import cn.aircas.fileManager.audio.entity.AudioInfo;
import cn.aircas.fileManager.audio.entity.AudioSearchParam;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.image.entity.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioMapper extends BaseMapper<AudioInfo> {
    void batchInsertAudioInfo(@Param("audioInfoList") List<AudioInfo> audioInfoList);
    List<Integer> listAudioIdBySearchParam(@Param(value = "audioSearchParam") AudioSearchParam audioSearchParam);
    IPage<AudioInfo> listAudioInfos(Page<AudioInfo> pageInfo, @Param(value = "audioSearchParam") AudioSearchParam audioSearchParam);

    int selectUserId(@Param("fileId") int fileId);
}
