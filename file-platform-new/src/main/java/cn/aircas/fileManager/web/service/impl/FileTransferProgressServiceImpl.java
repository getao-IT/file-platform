package cn.aircas.fileManager.web.service.impl;

import cn.aircas.fileManager.web.dao.FileTransferProgressMapper;
import cn.aircas.fileManager.web.entity.FileTransferParam;
import cn.aircas.fileManager.web.entity.FileTransferProgressInfo;
import cn.aircas.fileManager.web.service.FileTransferProgressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FileTransferProgressServiceImpl extends ServiceImpl<FileTransferProgressMapper, FileTransferProgressInfo> implements FileTransferProgressService {


    /**
     * 更新文件分块上传进度
     * @param fileTransferInfoId
     * @param md5
     */
    @Override
    public void updateTransferProgress(int fileTransferInfoId, String md5, int chunks) {
        QueryWrapper<FileTransferProgressInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5).eq("file_transfer_id",fileTransferInfoId);
        FileTransferProgressInfo fileTransferProgressInfo = this.getOne(queryWrapper);
        fileTransferProgressInfo.setChunks(chunks);
        fileTransferProgressInfo.setTransferredChunk(fileTransferProgressInfo.getTransferredChunk()+1);
        this.updateById(fileTransferProgressInfo);
    }

    /**
     * 检查并修改文件上传进度,判断是否完成
     * @param fileTransferParam
     * @return
     */
    @Override
    public boolean checkAndSetUploadProgress( FileTransferParam fileTransferParam) {
        String md5 = fileTransferParam.getMd5();
        int imageTileUploadInfoId = fileTransferParam.getFileTransferId();
        FileTransferProgressInfo fileTransferProgressInfo = this.getFileTransferProgress(md5,imageTileUploadInfoId);

        int uploadedChunck = fileTransferProgressInfo.getTransferredChunk();
        int chuncks = fileTransferProgressInfo.getChunks();
        return chuncks == uploadedChunck;

    }

    /**
     * 获取文件分块上传进度
     * @param fileMD5
     * @param fileTransferId
     * @return
     */
    @Override
    public FileTransferProgressInfo getFileTransferProgress(String fileMD5, int fileTransferId) {
        QueryWrapper<FileTransferProgressInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",fileMD5).eq("file_transfer_id",fileTransferId);
        return  this.getOne(queryWrapper);
    }
}
