package cn.aircas.fileManager.audio.service;

import cn.aircas.fileManager.audio.dao.AudioMapper;
import cn.aircas.fileManager.audio.entity.AudioInfo;
import cn.aircas.fileManager.web.entity.FileTransferInfo;
import cn.aircas.fileManager.web.service.impl.AbstractFileTypeTransferService;
import cn.aircas.utils.date.DateUtils;
import cn.aircas.utils.file.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service("AUDIO-TRANSFER-SERVICE")
public class AudioTransferServiceImpl extends AbstractFileTypeTransferService<AudioInfo> {

    @Autowired
    private AudioMapper audioMapper;


    @Override
    public AudioInfo transferFromWeb(String fileRelativePath, FileTransferInfo fileTransferInfo) {
        String filePath = FileUtils.getStringPath(this.rootPath,fileRelativePath);
        AudioInfo audioInfo = parseFileInfo(filePath);
        BeanUtils.copyProperties(fileTransferInfo,audioInfo,"id");
        this.audioMapper.insert(audioInfo);
        return audioInfo;
    }

    @Override
    public List<AudioInfo> transferFromBackend(String srcDir, String destDir, FileTransferInfo fileTransferInfo) {
        List<AudioInfo> audioInfoList = traverseFile(srcDir,destDir,fileTransferInfo);
        this.audioMapper.batchInsertAudioInfo(audioInfoList);
//        if (imageUploadParam.isCreateDataset()){
//            List<Integer> imageIdList = imageList.stream().map(Image::getId).collect(Collectors.toList());
//            labelPlatFormService.createDataset(new File(absolutePath),imageIdList,token);
//        }
        return audioInfoList;
    }


    @Override
    public String[] getSupportFileType() {
        return new String[]{"mp3"};
    }

    @Override
    public AudioInfo parseFileInfo(String filePath) {
        AudioInfo audioInfo = new AudioInfo();
        File source = new File(filePath);
        try {
            MP3File f = (MP3File) AudioFileIO.read(source);
            MP3AudioHeader mp3AudioHeader = f.getMP3AudioHeader();
            String audioDuration = String.format("%d分%d秒",(mp3AudioHeader.getTrackLength())/60,(mp3AudioHeader.getTrackLength()));
            audioInfo.setFileLength(source.length());
            audioInfo.setAudioName(source.getName());
            audioInfo.setDurationStr(audioDuration);
            audioInfo.setCreateTime(DateUtils.nowDate());
            audioInfo.setFormat(mp3AudioHeader.getFormat());
            audioInfo.setDuration(mp3AudioHeader.getTrackLength());
            audioInfo.setPath(filePath.substring(this.rootPath.length()));
            audioInfo.setSize(FileUtils.fileSizeToString(source.length()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return audioInfo;
    }
}
