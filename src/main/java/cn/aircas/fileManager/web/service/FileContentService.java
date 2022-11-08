package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.elec.entity.ElecInfo;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface FileContentService {

    PageResult<JSONObject> getContent(int pageSize, long pageNo, int fileId);

    void parseTextContent(ElecInfo elecInfo);
}
