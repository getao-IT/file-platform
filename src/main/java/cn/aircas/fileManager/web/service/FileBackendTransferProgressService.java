package cn.aircas.fileManager.web.service;

import cn.aircas.fileManager.web.entity.FileBackendTransferProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileBackendTransferProgressService {

    private static Map<String, FileBackendTransferProgress> imageTransferProgressMap = new HashMap<>();

    public static void finishOneTransfer(String transferToken, String imageName){
        FileBackendTransferProgress fileBackendTransferProgress = imageTransferProgressMap.getOrDefault(transferToken,new FileBackendTransferProgress());
        int currentFinishedCount = fileBackendTransferProgress.getCurrentFinishedCount()+1;
        fileBackendTransferProgress.getFinishedFileNameList().add(imageName);
        fileBackendTransferProgress.setTransferringFileName(imageName);
        fileBackendTransferProgress.setCurrentFinishedCount(currentFinishedCount);
    }

    public static void beginOneTransfer(String transferToken, int totalCount){
        FileBackendTransferProgress fileBackendTransferProgress = new FileBackendTransferProgress();
        fileBackendTransferProgress.setTotalCount(totalCount);
        fileBackendTransferProgress.setFinishedFileNameList(new ArrayList<>());
        imageTransferProgressMap.put(transferToken,fileBackendTransferProgress);
    }

    public static FileBackendTransferProgress getTransferProgress(String transferToken){
        return imageTransferProgressMap.getOrDefault(transferToken,new FileBackendTransferProgress());
    }
}
