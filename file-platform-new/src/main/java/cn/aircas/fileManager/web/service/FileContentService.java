package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.elec.entity.ElecContent;
import cn.aircas.fileManager.elec.entity.ElecInfo;
import cn.aircas.fileManager.elec.entity.ElecSearchParam;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FileContentService<T> {

    PageResult<JSONObject> getContent(int pageSize, long pageNo, int fileId);

    void parseTextContent(T fileInfo);

    Map<Integer, T> getFileByContentId(Set<Integer> contentIds);
}
