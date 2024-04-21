package cn.aircas.fileManager.audio.service;


import cn.aircas.fileManager.audio.dao.AudioMapper;
import cn.aircas.fileManager.audio.entity.AudioInfo;
import cn.aircas.fileManager.audio.entity.AudioSearchParam;
import cn.aircas.fileManager.commons.entity.FileInfo;
import cn.aircas.fileManager.commons.entity.FileSearchParam;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.commons.service.FileTypeService;
import cn.aircas.fileManager.web.service.UserInfoService;
import cn.aircas.utils.file.FileUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



@Service("AUDIO-SERVICE")
public class AudioFileServiceImpl extends ServiceImpl<AudioMapper, AudioInfo> implements FileTypeService {

    @Value("${sys.rootPath}")
    String rootPath;

    @Autowired
    private AudioMapper audioMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public String downloadFileById(int fileId) {
        AudioInfo audioInfo = this.audioMapper.selectById(fileId);
        return FileUtils.getStringPath(this.rootPath, audioInfo.getPath());
    }

    /**
     * 根据id批量删除影像文件
     * @param idList
     */
    @Override
    public void deleteFileByIds(List<Integer> idList) {
        List<AudioInfo> audioInfoList = this.audioMapper.selectBatchIds(idList);
        Assert.notEmpty(audioInfoList, String.format("文本的路径列表:%s为空", audioInfoList.toString()));
        for (AudioInfo audioInfo : audioInfoList) {
            String filePath = FileUtils.getStringPath(this.rootPath, audioInfo.getPath());
            FileUtils.forceDelete(filePath);
        }
        List<Integer> audioIdList = audioInfoList.stream().map(AudioInfo::getId).collect(Collectors.toList());
        this.audioMapper.deleteBatchIds(audioIdList);
    }

    /**
     * 根据id批量获取文件信息
     * @param fileIdList
     * @return
     */
    @Override
    public List<JSONObject> listFileInfosByIds(List<Integer> fileIdList, boolean content) {
        List<AudioInfo> textList = this.audioMapper.selectBatchIds(fileIdList);
        return textList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
    }

    /**
     * 更新文件信息
     * @param fileIdList
     * @param fileInfo
     */
    @Override
    public int getFileUserId(int fileId) {
        return this.audioMapper.selectUserId(fileId);
    }

    /**
     * 更新文件信息
     * @param fileIdList
     * @param fileInfo
     */
    @Override
    public void updateFileInfoByIds(List<Integer> fileIdList, FileInfo fileInfo) {
        UpdateWrapper<AudioInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(fileInfo.getKeywords()!=null,"keywords",fileInfo.getKeywords())
                .set(fileInfo.getSource()!=null,"source",fileInfo.getSource())
                .set(fileInfo.getIsPublic() != null, "is_public", fileInfo.getIsPublic()).in("id",fileIdList);
        this.update(updateWrapper);
    }

    @Override
    public List<Integer> listFileIdBySearchParam(FileSearchParam fileSearchParam) {
        AudioSearchParam audioSearchParam = convertSearchParam(fileSearchParam);
        return this.audioMapper.listAudioIdBySearchParam(audioSearchParam);
    }

    /**
     * 分页查询文件信息
     * @param fileSearchParam
     * @return
     */
    @Override
    public PageResult<JSONObject> listFileInfoByPage(FileSearchParam fileSearchParam) {
        AudioSearchParam audioSearchParam = convertSearchParam(fileSearchParam);
        Page<AudioInfo> page = new Page<>(fileSearchParam.getPageNo(), fileSearchParam.getPageSize());
        IPage<AudioInfo> audioIPage = this.audioMapper.listAudioInfos(page, audioSearchParam);
        List<AudioInfo> audioList = audioIPage.getRecords();
        List<JSONObject> result = audioList.stream().map(JSONObject::toJSONString).map(JSONObject::parseObject).collect(Collectors.toList());
        return new PageResult<>(audioIPage.getCurrent(), result, audioIPage.getTotal());
    }

    /**
     * 转换查询参数
     * @param fileSearchParam
     * @return
     */
    private AudioSearchParam convertSearchParam(FileSearchParam fileSearchParam){
        AudioSearchParam audioSearchParam = new AudioSearchParam();
        BeanUtils.copyProperties(fileSearchParam,audioSearchParam);
        JSONObject userInfo = this.userInfoService.getUserInfoByToken(request.getHeader("token")).getData();
        audioSearchParam.setAdminLevel(userInfo.getString("admin_level"));
        audioSearchParam.setUserId(userInfo.getInteger("id"));
        audioSearchParam.setAudioIdList(fileSearchParam.getFileIdList());
        String searchParam = fileSearchParam.getSearchParam();
        if (StringUtils.isNotBlank(searchParam)){
            List<String> params = Arrays.asList(searchParam.split(" "));
            audioSearchParam.setSearchParamList(params);
        }

        audioSearchParam.setAudioName(fileSearchParam.getFileName());
        return audioSearchParam;
    }
}
