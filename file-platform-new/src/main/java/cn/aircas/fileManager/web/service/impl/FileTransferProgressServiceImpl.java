package cn.aircas.fileManager.web.service.impl;

import cn.aircas.fileManager.image.dao.ImageMapper;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.web.dao.FileTransferProgressMapper;
import cn.aircas.fileManager.web.entity.FileTransferParam;
import cn.aircas.fileManager.web.entity.FileTransferProgressInfo;
import cn.aircas.fileManager.web.service.FileTransferProgressService;
import cn.aircas.utils.date.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class FileTransferProgressServiceImpl extends ServiceImpl<FileTransferProgressMapper, FileTransferProgressInfo> implements FileTransferProgressService {

    @Autowired
    private ImageMapper imageMapper;

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

    @Override
    public void updateTransferProgressDemo(int fileTransferInfoId, String md5, int chunks, String fullName) {
        QueryWrapper<FileTransferProgressInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5).eq("file_transfer_id",fileTransferInfoId);
        FileTransferProgressInfo fileTransferProgressInfo = this.getOne(queryWrapper);
        fileTransferProgressInfo.setChunks(chunks);
        fileTransferProgressInfo.setTransferredChunk(fileTransferProgressInfo.getTransferredChunk()+1);
        this.updateById(fileTransferProgressInfo);
        log.info("文件 {} 分块 {} 完成", fullName, fileTransferProgressInfo.getTransferredChunk());

        if (fileTransferProgressInfo.getTransferredChunk() >= chunks) {
            int fileId = 0;
            if (fullName.equalsIgnoreCase("nigulayefujichang.tif")) {
                fileId = 1977;
            }
            if (fullName.equalsIgnoreCase("JL1KF02B02_200383366_001_L5D_PSH.tif")) {
                fileId = 1997;
            }
            if (fullName.equalsIgnoreCase("hengxuhe_sar.tiff")) {
                fileId = 1968;
            }
            if (fullName.equalsIgnoreCase("songshan_kjg_1.tif")) {
                fileId = 1963;
            }
            if (fullName.equalsIgnoreCase("GF01_WF4_062299_20241116_MY8M2_01_022_L1A_01.browse.tif")) {
                fileId = 1939;
            }

            Image image = imageMapper.selectById(fileId);
            image.setDelete(false);
            image.setCreateTime(DateUtils.nowDate());
            this.imageMapper.updateById(image);
            log.info("文件 {} 自主上传完成", fullName);
        }
    }

    public void test() {
        Map<String, List<Image>> parentSubDirs = new HashMap<>();
        List<String> stscNames = new ArrayList<>();
        List<Image> statistics = new ArrayList<>();
        for (Image dirName : statistics) {
            Map<String, Object> stsc = new HashMap<>();
            if (stscNames.contains(dirName)) {
                Image statistic = statistics.get(stscNames.indexOf(dirName));
                double count = 0;
                double total = 0;
                for (Deque<Integer> que = new ArrayDeque<>(Arrays.asList(statistic.getId()));!que.isEmpty();) {
                    for (Image image : parentSubDirs.getOrDefault(que.poll(), new ArrayList<>())) {
                        que.add(image.getId());
                        count++;
                        total += count;
                    }
                }
                log.info("Image {} 的总子级个数 {}", dirName, total);
            }
        }
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
        return chuncks <= uploadedChunck;

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
