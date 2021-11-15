package cn.aircas.fileManager.web.service.impl;

import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.web.entity.lab.ImageRetrieveParam;
import cn.aircas.fileManager.web.entity.lab.TextRetrieveParam;
import cn.aircas.fileManager.web.service.LabService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LabServiceImpl implements LabService {

    @Value("${sys.rootPath}")
    String rootPath;

    @Value("${lab.lab-service-url}")
    String labServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void encodeImage(Image image) {
        List<Image> imageList = new ArrayList<>();
        imageList.add(image);
        this.encodeImage(imageList);
    }

    @Override
    public void encodeImage(List<Image> imageList) {
        JSONArray imageArray = new JSONArray();
        JSONObject imageObject = new JSONObject();
        for(int i=0; i<imageList.size(); i++ ){
            imageObject.put("image_id",imageList.get(i).getId());
            imageObject.put("image_path",imageList.get(i).getPath());
            imageObject.put("user_id",imageList.get(i).getUserId());
            imageObject.put("privilege",imageList.get(i).isPublic());
            imageArray.add(imageObject);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<JSONArray> httpEntity = new HttpEntity<>(imageArray,httpHeaders);
        JSONObject object = restTemplate.postForEntity(this.labServiceUrl +"/image_encode/", httpEntity, JSONObject.class).getBody();
        log.info("影像编码完成");
        //System.out.println(object);
    }

    @Override
    public void decodeImage(List<Integer> imageList) {
        JSONObject imageObject = new JSONObject();
        StringBuilder stringBuilder = new StringBuilder();
        String str = "";
        for(int i=0;i<imageList.size();i++){
            if(i<imageList.size()-1){
                stringBuilder.append(imageList.get(i));
                stringBuilder.append("'");
            }else{
                stringBuilder.append(imageList.get(i));
            }
        }
        str = stringBuilder.toString();
        imageObject.put("deleteID",str);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(imageObject,httpHeaders);
        JSONObject object = restTemplate.postForEntity(this.labServiceUrl +"/delete_encode/", httpEntity, JSONObject.class).getBody();
        log.info("取消影像编码完成");
        //System.out.println(object);
    }

    @Override
    public void retrieveImage(TextRetrieveParam textRetrieveParam) {
        JSONObject imageObject = new JSONObject();
        imageObject.put("text",textRetrieveParam.getText());
        imageObject.put("user_id",textRetrieveParam.getUserId());
        imageObject.put("page_no",textRetrieveParam.getPageNo());
        imageObject.put("page_size",textRetrieveParam.getPageSize());
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(imageObject,httpHeaders);
        JSONObject object = restTemplate.postForEntity(this.labServiceUrl +"/text_search/", httpEntity, JSONObject.class).getBody();
        log.info("以文搜图完成");
        //System.out.println(object);
    }

    @Override
    public void retrieveImageByImage(ImageRetrieveParam imageRetrieveParam) throws IOException {
        File imagePath = FileUtils.getFile(this.rootPath,"file-data","lab","tmp");
        if (!imagePath.exists()){
            imagePath.mkdirs();
        }
        FileUtils.copyFileToDirectory((File) imageRetrieveParam.getFile(),imagePath);
        String path = "file-data"+"lab"+"tmp"+ imageRetrieveParam.getFile().getOriginalFilename();

        JSONObject imageObject = new JSONObject();
        imageObject.put("image_path",path);
        imageObject.put("user_id",imageRetrieveParam.getUserId());
        imageObject.put("page_no",imageRetrieveParam.getPageNo());
        imageObject.put("page_size",imageRetrieveParam.getPageSize());
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(imageObject,httpHeaders);
        JSONObject object = restTemplate.postForEntity(this.labServiceUrl +"/image_search/", httpEntity, JSONObject.class).getBody();
        log.info("以图搜图完成");
        //System.out.println(object);
    }
}
