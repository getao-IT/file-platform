package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.web.entity.FileBackendTransferProgress;
import cn.aircas.fileManager.web.entity.FileTransferInfo;
import cn.aircas.fileManager.web.entity.FileTransferParam;
import cn.aircas.fileManager.web.entity.FileTransferProgressInfo;
import cn.aircas.fileManager.web.entity.enums.FileType;
import org.aspectj.lang.reflect.NoSuchPointcutException;

import javax.security.auth.message.AuthException;

public interface FileTransferService {
    String download(int fileId, FileType fileType) throws AuthException, NoSuchPointcutException;
    void checkFileMd5(String fileMD5, int fileTransferId);
    void backendTransfer(FileTransferInfo fileTransferParam);
    int commitTransferInfo( FileTransferInfo fileTransferInfo);
    void transferFromWeb(FileTransferParam fileTransferParam) throws Exception;
    FileBackendTransferProgress getBackendTransferProgress(String transferToken);

    Boolean checkUserUploadAuth(int userId);
}
