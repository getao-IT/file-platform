package cn.aircas.fileManager.audio.entity;

import cn.aircas.fileManager.image.entity.enums.CoordinateSystemType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("file_audio_info")
public class AudioInfo {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 保存路径
     */
    private String path;

    /**
     * 音频来源
     */
    private String source;

    /**
     * 标签
     */
    private String keywords;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 上传批次
     */
    private int batchNumber;


    /**
     * 用户名称
     */
    private String userName;

    /**
     * 音频名称
     */
    private String audioName;

    private String format;


    /**
     * 是否公开
     */
    private boolean isPublic;


    /**
     * 音频大小
     */
    private String size;


    /**
     * 文件长度
     */
    private long fileLength;

    /**
     * 音频时长
     */
    private long duration;

    /**
     * 字符串音频时长
     */
    private String durationStr;


}
