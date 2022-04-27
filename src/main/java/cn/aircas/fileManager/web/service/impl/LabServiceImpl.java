package cn.aircas.fileManager.web.service.impl;

import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.image.dao.ImageMapper;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.web.entity.lab.AudioRetrieveParam;
import cn.aircas.fileManager.web.entity.lab.ImageRetrieveParam;
import cn.aircas.fileManager.web.entity.lab.TextRetrieveParam;
import cn.aircas.fileManager.web.service.LabService;
import cn.aircas.utils.file.FileUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class LabServiceImpl implements LabService {

    @Value("${lab.prefix}")
    String rootPath;

    @Value("${lab.lab-service-url}")
    String labServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ImageMapper imageMapper;


    @Autowired
    LabService labService;

    @Override
    public void encodeImage(Image image) {
        List<Image> imageList = new ArrayList<>();
        imageList.add(image);
        this.encodeImage(imageList);
    }

    /**
     * 对上传完成的影像进行编码
     * @param imageList
     */
    @Override
    public void encodeImage(List<Image> imageList) {
        JSONArray imageArray = new JSONArray();
        JSONObject imageObject = new JSONObject();
        for(int i=0; i<imageList.size(); i++ ){
            imageObject.put("image_id",imageList.get(i).getId());
            imageObject.put("image_path",imageList.get(i).getPath().substring(1));
            imageObject.put("user_id",imageList.get(i).getUserId());
            imageObject.put("privilege",(imageList.get(i).isPublic()) ? 1:0);
            imageArray.add(imageObject);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<JSONArray> httpEntity = new HttpEntity<>(imageArray,httpHeaders);
        JSONObject object = restTemplate.postForEntity(this.labServiceUrl +"/image_encode/", httpEntity, JSONObject.class).getBody();
        log.info("影像编码完成");
        //System.out.println(object);
    }

    /**
     * 对上传完成的影像进行解码，删除原有影像
     * @param imageList
     */
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

    /**
     * 以文搜图
     * @param textRetrieveParam
     * @return
     */
    @Override
    public PageResult<JSONObject> retrieveImage(TextRetrieveParam textRetrieveParam) {
        JSONObject imageObject = new JSONObject();
        imageObject.put("text",textRetrieveParam.getText());
        imageObject.put("user_id",textRetrieveParam.getUserId());
        imageObject.put("page_no",textRetrieveParam.getPageNo());
        imageObject.put("page_size",textRetrieveParam.getPageSize());
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(imageObject,httpHeaders);
        JSONObject object = restTemplate.postForEntity(this.labServiceUrl +"/text_search/", httpEntity, JSONObject.class).getBody();

        List<Integer> messageList =JSONArray.parseObject(String.valueOf(object.getJSONObject("message").getJSONArray("results")), List.class) ;
        List<Image> imageList = this.imageMapper.selectBatchIds(messageList);
        List<JSONObject> result =  new ArrayList<>();
        for (Integer id : messageList) {
            for (Image ob :imageList) {
                if (ob.getId() == id){
                    result.add(JSONObject.parseObject(JSONObject.toJSONString(ob)));
                }
            }
        }
//        List<Image> imageList = this.imageMapper.listImageByMessage(messageList);
//        List<JSONObject> result = imageList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
        Integer totalCount = object.getJSONObject("message").getInteger("total");
        return new PageResult<>(textRetrieveParam.getPageNo(), result, totalCount);

    }

    /**
     * 以图搜图
     * @param imageRetrieveParam
     * @throws IOException
     * @return
     */
    @Override
    public PageResult<JSONObject> retrieveImageByImage(ImageRetrieveParam imageRetrieveParam) throws IOException {

        String originalFilename = imageRetrieveParam.getFile().getOriginalFilename();
        String path = FileUtils.getStringPath("file-data", "lab", "tmp",System.currentTimeMillis(),originalFilename);
        if (!FileUtils.getFile(this.rootPath,path).getParentFile().exists()){
            FileUtils.getFile(this.rootPath,path).getParentFile().mkdirs();
        }
        File file = FileUtils.getFile(this.rootPath,path);
        imageRetrieveParam.getFile().transferTo(file);
        file.deleteOnExit();
        FileUtils.copyFileToDirectory(file, FileUtils.getFile(path));

        JSONObject imageObject = new JSONObject();
        imageObject.put("image_path",path);
        imageObject.put("user_id",imageRetrieveParam.getUserId());
        imageObject.put("page_no",imageRetrieveParam.getPageNo());
        imageObject.put("page_size",imageRetrieveParam.getPageSize());
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(imageObject,httpHeaders);
        JSONObject object = restTemplate.postForEntity(this.labServiceUrl +"/image_search/", httpEntity, JSONObject.class).getBody();

        List<Integer> messageList =JSONArray.parseObject(String.valueOf(object.getJSONObject("message").getJSONArray("results")), List.class) ;
        List<Image> imageList = this.imageMapper.selectBatchIds(messageList);
        List<JSONObject> result =  new ArrayList<>();
        for (Integer id : messageList) {
            for (Image ob :imageList) {
                if (ob.getId() == id){
                    result.add(JSONObject.parseObject(JSONObject.toJSONString(ob)));
                }
            }
        }
        //List<JSONObject> result  = this.imageFileService.listFileInfosByIds(messageList,false);
//        List<Image> imageList = this.imageMapper.listImageByMessage(messageList);
//        List<JSONObject> result = imageList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
        Integer totalCount = object.getJSONObject("message").getInteger("total");
        return new PageResult<>(imageRetrieveParam.getPageNo(), result, totalCount);

    }

    @Override
    public PageResult<JSONObject> retrieveImageByAudio(AudioRetrieveParam audioRetrieveParam) throws IOException {
        String originalFilename = audioRetrieveParam.getFile().getOriginalFilename();
        String path = FileUtils.getStringPath("file-data", "lab", "tmp",System.currentTimeMillis(),originalFilename);
        if (!FileUtils.getFile(this.rootPath,path).getParentFile().exists()){
            FileUtils.getFile(this.rootPath,path).getParentFile().mkdirs();
        }
        File file = FileUtils.getFile(this.rootPath,path);
        audioRetrieveParam.getFile().transferTo(file);
        file.deleteOnExit();
        FileUtils.copyFileToDirectory(file, FileUtils.getFile(path));

        JSONObject imageObject = new JSONObject();
        imageObject.put("audio_path",path);
        imageObject.put("user_id",audioRetrieveParam.getUserId());
        imageObject.put("page_no",audioRetrieveParam.getPageNo());
        imageObject.put("page_size",audioRetrieveParam.getPageSize());
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(imageObject,httpHeaders);
        JSONObject object = restTemplate.postForEntity(this.labServiceUrl +"/audio_search/", httpEntity, JSONObject.class).getBody();

        List<Integer> messageList =JSONArray.parseObject(String.valueOf(object.getJSONObject("message").getJSONArray("results")), List.class) ;
        List<Image> imageList = this.imageMapper.selectBatchIds(messageList);
        List<JSONObject> result =  new ArrayList<>();
        for (Integer id : messageList) {
            for (Image ob :imageList) {
                if (ob.getId() == id){
                    result.add(JSONObject.parseObject(JSONObject.toJSONString(ob)));
                }
            }
        }
        Integer totalCount = object.getJSONObject("message").getInteger("total");
        return new PageResult<>(audioRetrieveParam.getPageNo(), result, totalCount);
    }


}
