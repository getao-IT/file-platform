package cn.aircas.fileManager.web.entity;

import cn.aircas.fileManager.web.entity.enums.FileTransferStatus;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FileBackendTransferProgress {
    private int totalCount;
    private int currentFinishedCount;
    private String transferringFileName;
    private Map<String, FileTransferStatus> finishedFileNameMap;
}
