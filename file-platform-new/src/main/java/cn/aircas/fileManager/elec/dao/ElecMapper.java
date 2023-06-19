package cn.aircas.fileManager.elec.dao;

import cn.aircas.fileManager.elec.entity.ElecInfo;
import cn.aircas.fileManager.elec.entity.ElecSearchParam;
import cn.aircas.fileManager.video.entity.VideoInfo;
import cn.aircas.fileManager.video.entity.VideoSearchParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElecMapper extends BaseMapper<ElecInfo> {
    void batchInsertElecInfo(@Param("elecInfoList") List<ElecInfo> elecInfoList);
    List<Integer> listElecIdBySearchParam(@Param(value = "elecSearchParam") ElecSearchParam elecSearchParam);
    IPage<ElecInfo> listElecInfos(Page<ElecInfo> pageInfo, @Param(value = "elecSearchParam") ElecSearchParam elecSearchParam);
}
