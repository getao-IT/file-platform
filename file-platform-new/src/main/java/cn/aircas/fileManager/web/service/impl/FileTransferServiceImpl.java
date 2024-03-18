package cn.aircas.fileManager.web.service.impl;

import cn.aircas.fileManager.commons.service.FileTypeService;
import cn.aircas.fileManager.web.dao.FileTransferInfoMapper;
import cn.aircas.fileManager.web.entity.FileBackendTransferProgress;
import cn.aircas.fileManager.web.entity.FileTransferInfo;
import cn.aircas.fileManager.web.entity.FileTransferParam;
import cn.aircas.fileManager.web.entity.FileTransferProgressInfo;
import cn.aircas.fileManager.web.entity.enums.FileTransferStatus;
import cn.aircas.fileManager.web.entity.enums.FileType;
import cn.aircas.fileManager.web.service.FileBackendTransferProgressService;
import cn.aircas.fileManager.web.service.FileTransferProgressService;
import cn.aircas.fileManager.web.service.FileTransferService;
import cn.aircas.fileManager.web.service.FileTypeTransferService;
import cn.aircas.utils.date.DateUtils;
import cn.aircas.utils.file.FileUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.reflect.NoSuchPointcutException;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.security.auth.message.AuthException;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.Map;



/**
 * @author Vanishrain
 * 文件传输服务
 */
@Slf4j
@Service
public class FileTransferServiceImpl extends ServiceImpl<FileTransferInfoMapper,FileTransferInfo> implements FileTransferService {

    @Value("${sys.rootPath}")
    String rootPath;

    @Value("${sys.uploadRootPath}")
    String uploadRootPath;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private FileTransferProgressService fileTransferProgressService;


    /**
     * 下载文件
     * @param fileId
     * @param fileType
     * @return
     */
    @Override
    public String download(int fileId, FileType fileType) throws AuthException, NoSuchPointcutException {
        authService.checkDownloadAuth(fileId , fileType);
        FileTypeService fileTypeService = fileType.getService();
        return fileTypeService.downloadFileById(fileId);
    }


    /**
     * 从后台上传文件
     * @param fileTransferInfo
     */
    @Async
    @Override
    public void backendTransfer(FileTransferInfo fileTransferInfo) {
        int userId = fileTransferInfo.getUserId();
        FileType fileType = fileTransferInfo.getFileType();
        String relativeSaveDir = fileTransferInfo.isPublic() ? FileUtils.getStringPath("file-data",fileType.getValue().toLowerCase(), System.currentTimeMillis()) :
                FileUtils.getStringPath("user", userId, "file-data", fileType.getValue().toLowerCase(),System.currentTimeMillis());

        FileTypeTransferService fileTypeTransferService = fileType.getTransferService();
        fileTypeTransferService.transferFromBackend(fileTransferInfo.getFileSaveDir(),relativeSaveDir,fileTransferInfo);
    }


    /**
     * 记录文件上传提交的信息
     * @param fileTransferInfo
     * @return
     */
    @Override
    public int commitTransferInfo(FileTransferInfo fileTransferInfo) {
        int userId = fileTransferInfo.getUserId();
        FileType fileType = fileTransferInfo.getFileType();

        //int batchNumber = getMaxBatchNumber(userId);
        String relativeSaveDir = fileTransferInfo.isPublic() ? FileUtils.getStringPath("file-data",fileType.getValue().toLowerCase(), System.currentTimeMillis()) :
                FileUtils.getStringPath("user", userId, "file-data", fileType.getValue().toLowerCase(),System.currentTimeMillis());

        fileTransferInfo.setFileSaveDir(relativeSaveDir);
        fileTransferInfo.setCreateTime(DateUtils.nowDate());
        this.save(fileTransferInfo);
        return fileTransferInfo.getId();
    }


    @Override
    public void transferFromWeb(FileTransferParam fileTransferParam) throws Exception {
        boolean isComplete = true;
        String fileMD5 = fileTransferParam.getMd5();
        int fileTransferId = fileTransferParam.getFileTransferId();
        FileTransferInfo fileTransferInfo = this.getById(fileTransferId);
        FileType fileType = fileTransferInfo.getFileType();
        String relativeDir = fileTransferInfo.getFileSaveDir();


        FileTransferProgressInfo fileTransferProgressInfo = this.fileTransferProgressService.getFileTransferProgress(fileMD5,fileTransferId);

        if ((fileTransferProgressInfo.getChunks() != fileTransferProgressInfo.getTransferredChunk())
                || (fileTransferProgressInfo.getTransferredChunk() == 0)) {
            this.transferTiles(fileTransferParam,relativeDir);
            isComplete = this.fileTransferProgressService.checkAndSetUploadProgress(fileTransferParam);
        }
        if (isComplete){
            String fileRelativePath = FileUtils.getStringPath(relativeDir,fileTransferParam.getFile().getOriginalFilename());
            FileTypeTransferService fileTypeTransferService = fileType.getTransferService();
            fileTypeTransferService.transferFromWeb(fileRelativePath,fileTransferInfo);
            // 如果上传的文件为IMAGE，若不存在ORV文件，则构建
            if (fileType == FileType.IMAGE) {
                String filePath = FileUtils.getStringPath(this.rootPath, fileRelativePath);
                File ovrFile = new File(filePath + ".ovr");
                if (!ovrFile.exists()) {
                    this.buildOverviews(filePath, new int[]{2,4,8});
                    log.info("{} 金字塔文件生成中...", filePath);
                }
            }
            authService.saveOrUpdateUserFileAuth(fileTransferParam);
        }
        fileTransferParam.setFile(null);
    }


    @Override
    public FileBackendTransferProgress getBackendTransferProgress(String transferToken) {
        FileBackendTransferProgress progress = FileBackendTransferProgressService.getTransferProgress(transferToken);
        Map<String, FileTransferStatus> finishedFileNameMap = progress.getFinishedFileNameMap();
        return FileBackendTransferProgressService.getTransferProgress(transferToken);
    }


    @Override
    public Boolean checkUserUploadAuth(int userId) {
        return null;
    }


    /**
     * 检查文件上传md5，如果没有则创建一条md5信息
     * @param fileMD5
     * @param fileTransferId
     */
    @Override
    public void checkFileMd5(String fileMD5, int fileTransferId) {
       FileTransferProgressInfo fileTransferProgressInfo = this.fileTransferProgressService.getFileTransferProgress(fileMD5,fileTransferId);
        if (null == fileTransferProgressInfo){
            fileTransferProgressInfo = FileTransferProgressInfo.builder().md5(fileMD5).fileTransferId(fileTransferId).createTime(new Date()).build();
            fileTransferProgressService.save(fileTransferProgressInfo);
        }
    }


    /*
     * 上传文件分片
     * */
    public void transferTiles(FileTransferParam fileTransferParam, String relativeSaveDir) throws Exception {

        String md5 = fileTransferParam.getMd5();
        int fileTransferId = fileTransferParam.getFileTransferId();
        String imageUploadPath = FileUtils.getStringPath(this.rootPath, relativeSaveDir,fileTransferParam.getFile().getOriginalFilename());
        File uploadFile = new File(imageUploadPath);
        if (!uploadFile.getParentFile().exists())
            uploadFile.getParentFile().mkdirs();

        RandomAccessFile randomAccessFile = new RandomAccessFile(imageUploadPath, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        long offset = fileTransferParam.getChunkSize() * fileTransferParam.getChunk();
        byte[] fileData = fileTransferParam.getFile().getBytes();   //文件分片转为字节数组
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        mappedByteBuffer.put(fileData);
        // 释放
        //mappedByteBuffer.clear();
        freedMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();

        log.info("开始更新上传进度");

        this.fileTransferProgressService.updateTransferProgress(fileTransferId,md5,fileTransferParam.getChunks());
        fileTransferParam.setChunk(fileTransferParam.getChunk() + 1);
    }


    /**
     * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
     * 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检查是否还有线程在读或写
     *
     * @param mappedByteBuffer
     */
    public static void freedMappedByteBuffer(final MappedByteBuffer mappedByteBuffer) {
        try {
            if (mappedByteBuffer == null) {
                return;
            }

            mappedByteBuffer.force();
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner", new Class[0]);
                        getCleanerMethod.setAccessible(true);
                        sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(mappedByteBuffer,
                                new Object[0]);
                        // Java8之后，好像将Cleaner归类到了jdk.internal.ref下
                        // CLASSPATH .;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar
                       /* Cleaner cleaner = (Cleaner) getCleanerMethod.invoke(mappedByteBuffer,
                                new Object[0]);*/
                        cleaner.clean();
                    } catch (Exception e) {
                        log.error("clean MappedByteBuffer error!!!", e);
                    }
                    log.info("clean MappedByteBuffer completed!!!");
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 构建文件金字塔
     * @param imagePath
     * @param overviewLists
     */
    @Async
    public void buildOverviews(String imagePath, int[] overviewLists) {
        gdal.SetConfigOption("GDAL_PAM_ENABLED", "FALSE");
        Dataset dataset = gdal.Open(imagePath);
        dataset.BuildOverviews(overviewLists);
        dataset.delete();
    }
}
