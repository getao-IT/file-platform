package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.web.entity.FileTransferInfo;

public interface FileTypeTransferService {
    void transferFromWeb(String fileRelativePath, FileTransferInfo fileTransferInfo);
    void transferFromBackend(String srcDir, String destDir, FileTransferInfo fileTransferInfo);
}
