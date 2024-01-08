package cn.aircas.fileManager.web.dao;

import cn.aircas.fileManager.web.entity.database.UserUploadAuthInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserUploadAuthMapper extends BaseMapper<UserUploadAuthInfo> {

}
