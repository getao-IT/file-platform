package cn.aircas.fileManager.web.entity;

import lombok.Data;

import java.util.List;

@Data
public class FileBackendTransferProgress {
    private int totalCount;
    private int currentFinishedCount;
    private String transferringFileName;
    private List<String> finishedFileNameList;
}
