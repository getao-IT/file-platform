package cn.aircas.fileManager.image.service;

import cn.aircas.fileManager.image.dao.ImageMapper;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.web.entity.FileTransferInfo;
import cn.aircas.fileManager.web.service.impl.AbstractFileTypeTransferService;
import cn.aircas.fileManager.web.service.impl.LabelPlatFormServiceImpl;
import cn.aircas.utils.date.DateUtils;
import cn.aircas.utils.file.FileUtils;
import cn.aircas.utils.image.ImageInfo;
import cn.aircas.utils.image.ParseImageInfo;
import cn.aircas.utils.image.slice.CreateThumbnail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author vanishrain
 * 影像文件传输服务
 */
@Slf4j
@Service("IMAGE-TRANSFER-SERVICE")
public class ImageTransferService extends AbstractFileTypeTransferService<Image>{
    @Autowired
    ImageMapper imageMapper;

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Value("${value.createDataset}")
    private boolean createDataset;

    @Autowired
    LabelPlatFormServiceImpl labelPlatFormService;

    @Override
    public Image transferFromWeb(String fileRelativePath, FileTransferInfo fileTransferInfo) {
        String filePath = FileUtils.getStringPath(this.rootPath, fileRelativePath);

        Image image = parseFileInfo(filePath);
        BeanUtils.copyProperties(fileTransferInfo,image,"id");
        this.imageMapper.insert(image);
        return image;
    }

    @Override
    public List<Image> transferFromBackend(String srcDir, String destDir, FileTransferInfo fileTransferInfo){
        List<Image> imageInfoList = traverseFile(srcDir,destDir,fileTransferInfo);

        int beginIndex = 0;
        int batchInsertSize = 1000;
        int imageCount = imageInfoList.size();

        while (beginIndex<imageCount){
            int endIndex = imageCount < (beginIndex+batchInsertSize) ? imageCount : (beginIndex+batchInsertSize);
            this.imageMapper.batchInsertImageInfo(imageInfoList.subList(beginIndex,endIndex));
            beginIndex = endIndex;
        }

        if (createDataset){
            List<Integer> imageIdList = imageInfoList.stream().map(Image::getId).collect(Collectors.toList());
            labelPlatFormService.createDataset(FileUtils.getFile(this.rootPath,srcDir),imageIdList,fileTransferInfo.getToken());
        }
        return imageInfoList;
    }

    @Override
    public String[] getSupportFileType() {
        return new String[]{"jpg", "tiff", "tif", "png","TIF","TIFF","jpeg"};
    }

    @Override
    public Image parseFileInfo(String filePath) {
        File imageFile = new File(filePath);

        String thumbnailPath = FileUtils.getStringPath(imageFile.getParentFile().getAbsolutePath(),
                "thumbnail_" + FilenameUtils.removeExtension(imageFile.getName()) + ".jpg");
        String fileSize = FileUtils.fileSizeToString(imageFile.length());
        String thumbnail = CreateThumbnail.createBase64Thumbnail(filePath, thumbnailPath, 256);

        String relativeFilePath = filePath.substring(this.rootPath.length());
        if (relativeFilePath.startsWith("/"))
            relativeFilePath = relativeFilePath.substring(1);
        ImageInfo imageInfo = ParseImageInfo.parseInfo(filePath);
        Image image = Image.builder().imageName(imageFile.getName()).createTime(DateUtils.nowDate()).path(relativeFilePath)
                .thumb(thumbnail).size(fileSize).fileLength(imageFile.length()).minProjectionX(imageInfo.getProjectionRange()[0])
                .minProjectionY(imageInfo.getProjectionRange()[1]).maxProjectionX(imageInfo.getProjectionRange()[2])
                .maxProjectionY(imageInfo.getProjectionRange()[3]).delete(false).build();
        BeanUtils.copyProperties(imageInfo,image);

        return image;
    }

}
