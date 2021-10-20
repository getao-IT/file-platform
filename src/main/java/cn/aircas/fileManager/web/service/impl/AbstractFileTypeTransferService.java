package cn.aircas.fileManager.web.service.impl;

import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.web.entity.FileTransferInfo;
import cn.aircas.fileManager.web.service.FileBackendTransferProgressService;
import cn.aircas.fileManager.web.service.FileTypeTransferService;
import cn.aircas.utils.date.DateUtils;
import cn.aircas.utils.file.FileUtils;
import cn.aircas.utils.image.ImageInfo;
import cn.aircas.utils.image.ParseImageInfo;
import cn.aircas.utils.image.slice.CreateThumbnail;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class AbstractFileTypeTransferService<T> implements FileTypeTransferService {
    @Value("${sys.rootPath}")
    public String rootPath;

    @Value("${sys.uploadRootPath}")
    public String uploadRootPath;

    public abstract String[] getSupportFileType();

    public abstract T parseFileInfo(String filePath);

    /**
     * 遍历并解析文件
     * @param srcDir
     * @param destDir
     * @param fileTransferInfo
     * @return
     */
    public List<T> traverseFile(String srcDir, String destDir, FileTransferInfo fileTransferInfo){
        List<T> fileInfoList = new ArrayList<>();
        File srcDirFile = FileUtils.getFile(this.rootPath,srcDir);
        List<File> fileList = Arrays.asList(Objects.requireNonNull(srcDirFile.listFiles((dir, name) -> FilenameUtils.isExtension(name, getSupportFileType()))));
        Assert.notEmpty(fileList, String.format("上传文件夹：%s 为空", fileList));
        String transferToken = fileTransferInfo.getTransferToken();
        FileBackendTransferProgressService.beginOneTransfer(transferToken, fileList.size());

        int count = 0;
        for (File file : fileList) {
            String fileName = file.getName();
            String fileRelativeSavePath = FileUtils.getStringPath(destDir, fileName);
            String fileSavePath = FileUtils.getStringPath(this.rootPath, fileRelativeSavePath);

            if (fileTransferInfo.isCopy()){
                try {
                    FileUtils.copyFile(file, FileUtils.getFile(this.rootPath, fileRelativeSavePath));
                } catch (IOException e) {
                    log.error("拷贝文件：{} 出错", file.getAbsolutePath());
                    continue;
                }
            }else {
                fileSavePath = file.getAbsolutePath();
            }

            T fileInfo = parseFileInfo(fileSavePath);
            BeanUtils.copyProperties(fileTransferInfo,fileInfo,"createTime");
            fileInfoList.add(fileInfo);
            count++;

            FileBackendTransferProgressService.finishOneTransfer(transferToken, file.getName());
            log.info("上传进度：{}/{}", count, fileList.size());

        }
        return fileInfoList;
    }
}
