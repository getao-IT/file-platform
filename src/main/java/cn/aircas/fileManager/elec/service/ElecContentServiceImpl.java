package cn.aircas.fileManager.elec.service;

import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.elec.dao.ElecContextMapper;
import cn.aircas.fileManager.elec.dao.ElecMapper;
import cn.aircas.fileManager.elec.entity.ElecContent;
import cn.aircas.fileManager.elec.entity.ElecInfo;
import cn.aircas.fileManager.elec.entity.ElecSearchParam;
import cn.aircas.fileManager.image.dao.ImageMapper;
import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.web.service.FileContentService;
import cn.aircas.utils.file.FileUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service("ELEC-CONTENT")
public class ElecContentServiceImpl extends ServiceImpl<ElecMapper, ElecInfo>  implements FileContentService<ElecInfo> {

    @Value("${sys.rootPath}")
    private String rootPath;

    @Autowired
    private ElecContextMapper elecContextMapper;

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
            log.error("电子文件获取数据内容出错 {} ", new Exception(e));
        }

        return new PageResult<JSONObject>(pageNo, result, lineCount);
    }

    public List<ElecInfo> parseElecContent(List<ElecInfo> elecInfoList) {
        for (ElecInfo elecInfo : elecInfoList) {
            parseTextContent(elecInfo);
        }
        return null;
    }

    /**
     * 保存电子文件内容
     * @param elecInfo
     */
    public void parseTextContent(ElecInfo elecInfo) {
        File elecFile = FileUtils.getFile(this.rootPath, elecInfo.getPath());
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(elecFile), "GB2312");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            int lineNumber = 1;
            String context = "";
            while ((context=bufferedReader.readLine()) != null) {
                ElecContent elecContent = new ElecContent();
                elecContent.setLineNumber(lineNumber);
                elecContent.setElecFileId(elecInfo.getId());
                elecContent.setContent(context);
                this.elecContextMapper.insert(elecContent);
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存dat电子文件内容
     * @param elecInfo
     */
    public void parseElecContentFromDat(ElecInfo elecInfo) {
        try {
            RandomAccessFile raf = new RandomAccessFile(FileUtils.getStringPath(this.rootPath, elecInfo.getPath()), "rw");
            short len = 0;
            int lineNumber = 1;
            while ((len=raf.readShort()) != -1) {
                if (lineNumber > 1000) {
                    break;
                }
                ElecContent elecContent = new ElecContent();
                elecContent.setLineNumber(lineNumber);
                elecContent.setElecFileId(elecInfo.getId());
                elecContent.setContent(String.valueOf(len));
                this.elecContextMapper.insert(elecContent);
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取dat电子文件内容
     * @param fileSearchParam
     * @return
     */
    public List<ElecContent> getElecContentFromDat(FileSearchParam fileSearchParam) {
        Page<ElecContent> page = new Page<>(fileSearchParam.getPageNo(), fileSearchParam.getPageSize());
        List<ElecContent> result = this.elecContextMapper.selectList(
                new QueryWrapper<ElecContent>().eq("elec_file_id", fileSearchParam.getFileIdList().get(0)));
        return result;
    }

    /**
     * 批量查询文件内容所属文件信息成功
     * @param contentIds
     * @return
     */
    @Override
    public Map<Integer, ElecInfo> getFileByContentId(Set<Integer> contentIds) {
        return null;
    }
}
