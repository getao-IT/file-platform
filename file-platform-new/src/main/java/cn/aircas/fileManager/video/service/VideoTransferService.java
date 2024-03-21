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
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
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
    public VideoInfo transferFromWeb(String fileRelativePath, FileTransferInfo fileTransferInfo) {
        String filePath = FileUtils.getStringPath(this.rootPath, fileRelativePath);
        VideoInfo videoInfo = parseFileInfo(filePath);
        BeanUtils.copyProperties(fileTransferInfo,videoInfo);
        this.videoMapper.insert(videoInfo);
        return videoInfo;
    }

    @Override
    public List<VideoInfo> transferFromBackend(String srcDir, String destDir, FileTransferInfo fileTransferInfo) {
        List<VideoInfo> videoInfoList = traverseFile(srcDir,destDir,fileTransferInfo);
        this.videoMapper.batchInsertVideoInfo(videoInfoList);
//        if (imageUploadParam.isCreateDataset()){
//            List<Integer> imageIdList = imageList.stream().map(Image::getId).collect(Collectors.toList());
//            labelPlatFormService.createDataset(new File(absolutePath),imageIdList,token);
//        }
        return videoInfoList;
    }


    @Override
    public String[] getSupportFileType() {
        return new String[]{"mp4"};
    }

    @Override
    public VideoInfo parseFileInfo(String filePath) {
        VideoInfo videoInfo = new VideoInfo();
        File source = new File(filePath);
        try {
            Movie movie = MovieCreator.build(filePath);
            IsoFile isoFile = new IsoFile(filePath);
            MovieHeaderBox movieHeaderBox= isoFile.getMovieBox().getMovieHeaderBox();
            TrackHeaderBox trackHeaderBox = isoFile.getMovieBox().getBoxes(TrackBox.class).get(0).getTrackHeaderBox();
            float lengthInSeconds = (float)  movieHeaderBox.getDuration()/movieHeaderBox.getTimescale();
            String videoDuration = String.format("%d分%d秒",(int)lengthInSeconds/60,(int)lengthInSeconds%60);
            float frameRate = movie.getTracks().get(0).getSamples().size() / lengthInSeconds;
            videoInfo.setDurationStr(videoDuration);
            videoInfo.setFormat(movieHeaderBox.getType());
            videoInfo.setDuration(movieHeaderBox.getDuration());
            videoInfo.setFrameRate((float) Math.ceil(frameRate));
            videoInfo.setWidth((int)trackHeaderBox.getWidth());
            videoInfo.setHeight((int)trackHeaderBox.getHeight());
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
