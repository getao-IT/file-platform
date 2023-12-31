package cn.aircas.fileManager.text.service;

import cn.aircas.fileManager.text.dao.TextContentMapper;
import cn.aircas.fileManager.text.dao.TextMapper;
import cn.aircas.fileManager.text.entity.TextContentInfo;
import cn.aircas.fileManager.text.entity.TextInfo;
import cn.aircas.fileManager.web.entity.FileTransferInfo;
import cn.aircas.fileManager.web.service.impl.AbstractFileTypeTransferService;
import cn.aircas.utils.date.DateUtils;
import cn.aircas.utils.file.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("TEXT-TRANSFER-SERVICE")
public class TextTransferServiceImpl extends AbstractFileTypeTransferService<TextInfo> {
    @Autowired
    private TextMapper textMapper;

    @Autowired
    private TextFileContentServiceImpl textFileContentService;

    @Override
    public TextInfo transferFromWeb(String fileRelativePath, FileTransferInfo fileTransferInfo) {
        String filePath = FileUtils.getStringPath(this.rootPath,fileRelativePath);
        TextInfo text = parseFileInfo(filePath);
        BeanUtils.copyProperties(fileTransferInfo,text,"id");
        this.textMapper.insert(text);
        this.textFileContentService.parseTextContent(text);
        return text;
    }

    @Override
    public List<TextInfo> transferFromBackend(String srcDir, String destDir, FileTransferInfo fileTransferInfo) {
        List<TextInfo> textInfoList = traverseFile(srcDir,destDir,fileTransferInfo);
        this.textMapper.batchInsertTextInfo(textInfoList);
        this.textFileContentService.parseTextContent(textInfoList);
        return textInfoList;


//        if (imageUploadParam.isCreateDataset()){
//            List<Integer> imageIdList = imageList.stream().map(Image::getId).collect(Collectors.toList());
//            labelPlatFormService.createDataset(new File(absolutePath),imageIdList,token);
//        }
    }


    @Override
    public String[] getSupportFileType() {
        return new String[]{"txt"};
    }

    @Override
    public TextInfo parseFileInfo(String filePath) {
        TextInfo text = new TextInfo();
        File textFile = new File(filePath);
        if (!textFile.exists())
            return text;
        int linenumber = 0;
        try(FileReader fileReader = new FileReader(filePath);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader)) {
            while ( lineNumberReader.readLine()!= null){
                linenumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("统计文件：{} 行数出错",filePath);
        }

        text.setTextName(textFile.getName());
        text.setLineCount(linenumber);
        text.setCreateTime(DateUtils.nowDate());
        text.setFileLength(textFile.length());
        text.setPath(filePath.substring(this.rootPath.length()));
        text.setSize(FileUtils.fileSizeToString(textFile.length()));
        return text;
    }
}
