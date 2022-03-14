package cn.aircas.fileManager.web.service.impl;

import cn.aircas.fileManager.image.dao.ImageMapper;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.image.service.ImageTransferService;
import cn.aircas.fileManager.web.service.FileProcessingService;
import cn.aircas.utils.file.FileUtils;
import cn.aircas.utils.image.ImageFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
public class FileProcessingServiceImpl implements FileProcessingService {
    @Value("${sys.rootPath}")
    public String rootPath;

    @Autowired
    ImageMapper imageMapper;
    @Autowired
    ImageTransferService imageTransferService;

    @Override
    public void formatConverter(int fileId, String format) {
        Image srcimage = this.imageMapper.selectById(fileId);
        String inputPath = FileUtils.getStringPath(this.rootPath,srcimage.getPath());
        File outputParentPath = srcimage.isPublic() ? FileUtils.getFile(this.rootPath, "file-data","image", System.currentTimeMillis()) :
                FileUtils.getFile(this.rootPath, "user", srcimage.getUserId(), "file-data", "image",System.currentTimeMillis());
        if (!outputParentPath.exists()){
            outputParentPath.mkdirs();
        }
        String path = ImageFormat.formatConvertor(inputPath, outputParentPath.getPath(), format);
        String filePath = FileUtils.getStringPath(path);
        Image image = this.imageTransferService.parseFileInfo(filePath);
        this.imageMapper.insert(image);
        log.info("影像格式转换成功");

    }
}
