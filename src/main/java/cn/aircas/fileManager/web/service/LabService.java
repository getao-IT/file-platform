package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.web.entity.lab.ImageRetrieveParam;
import cn.aircas.fileManager.web.entity.lab.TextRetrieveParam;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface LabService {

    /**
     * 对单张影像进行编码
     * @param image
     */
    void encodeImage(Image image);

    /**
     * 对多张影像进行编码
     * @param imageList
     */
    void encodeImage(List<Image> imageList);

    /**
     * 取消影像编码
     * @param imageList
     */
    void decodeImage(List<Integer> imageList);

    /**
     * 以文搜图
     * @param textRetrieveParam
     */
    void retrieveImage(TextRetrieveParam textRetrieveParam);

    /**
     * 以图搜图
     * @param imageRetrieveParam
     */
    void retrieveImageByImage(ImageRetrieveParam imageRetrieveParam) throws IOException;

}
