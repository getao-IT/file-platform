package cn.aircas.fileManager.text.dao;

import cn.aircas.fileManager.text.entity.TextContentInfo;
import cn.aircas.fileManager.text.entity.TextInfo;
import cn.aircas.fileManager.text.entity.TextSearchParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TextContentMapper extends BaseMapper<TextContentInfo> {
    IPage<TextContentInfo> listTextContents(Page<TextContentInfo> pageInfo, @Param(value = "textSearchParam") TextSearchParam textSearchParam);
}
