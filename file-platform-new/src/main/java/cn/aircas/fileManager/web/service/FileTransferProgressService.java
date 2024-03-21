package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.web.dao.FileTransferProgressMapper;
import cn.aircas.fileManager.web.entity.FileTransferParam;
import cn.aircas.fileManager.web.entity.FileTransferProgressInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface FileTransferProgressService extends IService<FileTransferProgressInfo>{
    void updateTransferProgress(int fileTransferInfoId, String md5, int chuncks);
    boolean checkAndSetUploadProgress(FileTransferParam fileTransferParam);
    FileTransferProgressInfo getFileTransferProgress(String fileMD5, int fileTransferId);
}
