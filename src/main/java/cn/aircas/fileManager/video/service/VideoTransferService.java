package cn.aircas.fileManager.video.service;

import cn.aircas.fileManager.video.dao.VideoMapper;
import cn.aircas.fileManager.video.entity.VideoInfo;
import cn.aircas.fileManager.web.entity.FileTransferInfo;
import cn.aircas.fileManager.web.service.FileBackendTransferProgressService;
import cn.aircas.fileManager.web.service.FileTypeTransferService;
import cn.aircas.fileManager.web.service.impl.AbstractFileTypeTransferService;
import cn.aircas.utils.date.DateUtils;
import cn.aircas.utils.file.FileUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 视频文件传输服务
 */
@Slf4j
@Service("VIDEO-TRANSFER-SERVICE")
public class VideoTransferService extends AbstractFileTypeTransferService<VideoInfo> {
    @Autowired
    private VideoMapper videoMapper;

    @Override
    public void transferFromWeb(String fileRelativePath, FileTransferInfo fileTransferInfo) {
        String filePath = FileUtils.getStringPath(this.rootPath, fileRelativePath);
        VideoInfo videoInfo = parseFileInfo(filePath);
        BeanUtils.copyProperties(fileTransferInfo,videoInfo);
        this.videoMapper.insert(videoInfo);
    }

    @Override
    public void transferFromBackend(String srcDir, String destDir, FileTransferInfo fileTransferInfo) {
        List<VideoInfo> videoInfoList = traverseFile(srcDir,destDir,fileTransferInfo);
        this.videoMapper.batchInsertVideoInfo(videoInfoList);
//        if (imageUploadParam.isCreateDataset()){
//            List<Integer> imageIdList = imageList.stream().map(Image::getId).collect(Collectors.toList());
//            labelPlatFormService.createDataset(new File(absolutePath),imageIdList,token);
//        }
    }


    @Override
    public String[] getSupportFileType() {
        return new String[]{"mp4"};
    }

    @Override
    public VideoInfo parseFileInfo(String filePath) {
        VideoInfo videoInfo = new VideoInfo();
        File source = new File(filePath);
        Encoder encoder = new Encoder();
        try {
            MultimediaInfo multimediaInfo = encoder.getInfo(source);
            String videoDuration = String.format("%d分%d秒",(multimediaInfo.getDuration())/60000,(multimediaInfo.getDuration()%60000)/1000);
            videoInfo.setDurationStr(videoDuration);
            videoInfo.setFormat(multimediaInfo.getFormat());
            videoInfo.setDuration(multimediaInfo.getDuration());
            videoInfo.setFrameRate(multimediaInfo.getVideo().getFrameRate());
            videoInfo.setWidth(multimediaInfo.getVideo().getSize().getWidth());
            videoInfo.setHeight(multimediaInfo.getVideo().getSize().getHeight());
        }catch (Exception e) {
            e.printStackTrace();
            log.error("解析视频文件信息错误:{}",filePath);
        }

        videoInfo.setCreateTime(DateUtils.nowDate());
        videoInfo.setFileLength(source.length());
        videoInfo.setVideoName(source.getName());
        videoInfo.setPath(filePath.substring(this.rootPath.length()));
        videoInfo.setSize(FileUtils.fileSizeToString(source.length()));
        return videoInfo;
    }
}
