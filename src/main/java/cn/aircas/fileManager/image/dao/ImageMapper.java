package cn.aircas.fileManager.image.dao;

import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.image.entity.ImageSearchParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageMapper extends BaseMapper<Image> {
    void batchInsertImageInfo(@Param("imageInfoList") List<Image> imageInfoList);
    List<Integer> listImageIdBySearchParam(@Param(value = "imageSearchParam") ImageSearchParam imageSearchParam);
    IPage<Image> listImageInfosByPage(Page<Image> pageInfo, @Param(value = "imageSearchParam") ImageSearchParam imageSearchParam , @Param("adminLevel") int adminLevel);
}
