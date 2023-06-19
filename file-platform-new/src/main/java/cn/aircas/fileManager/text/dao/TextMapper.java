package cn.aircas.fileManager.text.dao;

import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.text.entity.TextInfo;
import cn.aircas.fileManager.text.entity.TextSearchParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextMapper extends BaseMapper<TextInfo> {
    void batchInsertTextInfo(@Param("textInfoList") List<TextInfo> textInfoList);
    List<Integer> listTextIdBySearchParam(@Param(value = "textSearchParam") TextSearchParam textSearchParam);
    IPage<TextInfo> listTextInfos(Page<TextInfo> pageInfo, @Param(value = "textSearchParam") TextSearchParam textSearchParam);
}
