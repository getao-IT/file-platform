package cn.aircas.fileManager.elec.service;

import cn.aircas.fileManager.elec.dao.ElecMapper;
import cn.aircas.fileManager.elec.entity.ElecInfo;
import cn.aircas.fileManager.web.entity.FileTransferInfo;
import cn.aircas.fileManager.web.service.FileService;
import cn.aircas.fileManager.web.service.impl.AbstractFileTypeTransferService;
import cn.aircas.utils.date.DateUtils;
import cn.aircas.utils.file.FileUtils;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.File;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("ELEC-TRANSFER-SERVICE")
public class ElecTransferService extends AbstractFileTypeTransferService<ElecInfo> {
    @Autowired
    private ElecMapper elecMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private ElecContentServiceImpl elecContentService;

    @Override
    public ElecInfo transferFromWeb(String fileRelativePath, FileTransferInfo fileTransferInfo) {
        String filePath = FileUtils.getStringPath(this.rootPath, fileRelativePath);
        ElecInfo elecInfo = parseFileInfo(filePath);
        BeanUtils.copyProperties(fileTransferInfo,elecInfo);
        elecInfo.setElecName(new File(fileRelativePath).getName());
        elecInfo.setPath(fileRelativePath);
        try {
            this.elecMapper.insert(elecInfo);
            this.elecContentService.parseTextContent(elecInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elecInfo;
    }

    @Override
    public List<ElecInfo> transferFromBackend(String srcDir, String destDir, FileTransferInfo fileTransferInfo) {
        List<ElecInfo> elecInfoList = traverseFile(srcDir, destDir, fileTransferInfo);
        this.elecMapper.batchInsertElecInfo(elecInfoList);
//        if (imageUploadParam.isCreateDataset()){
//            List<Integer> imageIdList = imageList.stream().map(Image::getId).collect(Collectors.toList());
//            labelPlatFormService.createDataset(new File(absolutePath),imageIdList,token);
//        }
        //this.elecContentService.parseElecContent(elecInfoList);
        return elecInfoList;
    }

    @Override
    public String[] getSupportFileType() {
        return new String[]{"csv"};
    }


    //todo 解析文件入库信息
    @Override
    public ElecInfo parseFileInfo(String filePath) {
        ElecInfo elecInfo = new ElecInfo();
        File file = new File(filePath);
        try{
            elecInfo.setCount(fileService.getFileLineCount(file));
            elecInfo.setSize(FileUtils.fileSizeToString(file.length()));
        }catch (Exception e){
            e.printStackTrace();
            log.info("解析电子文件信息错误:{}",filePath);
        }
        return elecInfo;
    }
}
