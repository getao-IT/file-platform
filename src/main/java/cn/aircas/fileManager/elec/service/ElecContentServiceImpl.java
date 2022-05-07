package cn.aircas.fileManager.elec.service;

import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.elec.dao.ElecMapper;
import cn.aircas.fileManager.elec.entity.ElecInfo;
import cn.aircas.fileManager.image.dao.ImageMapper;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.web.service.FileContentService;
import cn.aircas.utils.file.FileUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service("ELEC-CONTENT")
public class ElecContentServiceImpl extends ServiceImpl<ElecMapper, ElecInfo>  implements FileContentService {

    @Value("${sys.rootPath}")
    private String rootPath;

    @Override
    public PageResult<JSONObject> getContent(int pageSize, long pageNo, int fileId) {
        List<JSONObject> result = new ArrayList<>();

        ElecInfo elecInfo = this.getById(fileId);
        int lineCount = elecInfo.getCount();
        long beginLine = (pageNo - 1) * pageSize + 1;
        long endLineNum = Math.min(beginLine + pageSize, lineCount);
        if (pageNo<=0 || pageSize<=0)
            return null;

        File sampleSetFile = FileUtils.getFile(this.rootPath,elecInfo.getPath());
        if (!sampleSetFile.exists())
            return new PageResult<JSONObject>(pageNo, result, lineCount);


        try(FileInputStream fileInputStream = new FileInputStream(sampleSetFile);
            InputStreamReader fileReader = new InputStreamReader(fileInputStream,"GB2312");
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader)){
            String header = lineNumberReader.readLine().replace(",,","");

            long readCount = 0;
            while(readCount < beginLine){
                lineNumberReader.readLine();
                readCount++;
            }

            readCount = endLineNum - beginLine;
            while(readCount-- > 0) {
                JSONObject content = new JSONObject();
                content.put("header",header);
                content.put("content",lineNumberReader.readLine().replace(",,",", , ,"));
                result.add(content);
            }
        }catch (IOException e){
            //log.error("获取样本集{}:数据内容出错",sampleSetId);
        }

        return new PageResult<JSONObject>(pageNo, result, lineCount);
    }
}
