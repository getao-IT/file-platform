package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.commons.entity.common.PageResult;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface FileContentService {


    PageResult<JSONObject> getContent(int pageSize, int pageNo, int fileId);
}
