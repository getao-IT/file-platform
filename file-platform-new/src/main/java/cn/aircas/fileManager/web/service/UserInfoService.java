package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.commons.entity.common.CommonResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;
import java.util.Set;

public interface UserInfoService {
    Map<Integer,String> getAllUserInfo();
    Map<Integer,String> getUserInfoById(Set<Integer> userIdSet);
    CommonResult<JSONObject> getUserInfoByToken(String token) throws ResourceAccessException;
}
