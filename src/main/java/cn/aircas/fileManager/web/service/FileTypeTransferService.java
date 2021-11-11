package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.web.entity.FileTransferInfo;

import java.util.List;

public interface FileTypeTransferService<T> {
    T transferFromWeb(String fileRelativePath, FileTransferInfo fileTransferInfo);
    List<T> transferFromBackend(String srcDir, String destDir, FileTransferInfo fileTransferInfo);
}
