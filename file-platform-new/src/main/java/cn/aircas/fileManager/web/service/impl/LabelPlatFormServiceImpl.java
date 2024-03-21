package cn.aircas.fileManager.web.service.impl;

import cn.aircas.utils.file.FileUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@Slf4j
@Service
public class LabelPlatFormServiceImpl {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${value.api.create-dataset-url}")
    private String datasetCreationUrl;

    @Autowired
    private HttpServletRequest httpServletRequest;



    @Async
    public void createDataset(File datasetFile, List<Integer> imageIdList, String token){
        String imageIdListStr = imageIdList.toString().replace("[","")
                .replace("]","").replace(" ","");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("datasetName", datasetFile.getParentFile().getName());
        jsonObject.put("fileIdList", imageIdListStr);
        jsonObject.put("datasetType","IMAGE");
        jsonObject.put("labelPath", FileUtils.getStringPath(datasetFile.getParentFile().getAbsolutePath(),"xmls"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token",token);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(jsonObject,httpHeaders);
        JSONObject object = restTemplate.postForEntity(datasetCreationUrl, httpEntity, JSONObject.class).getBody();
    }
}
