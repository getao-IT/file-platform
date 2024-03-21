package cn.aircas.fileManager.video.entity;

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
@TableName("file_video_info")
public class VideoInfo {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 用户id
     */
    private int userId;

    private String format;

    private float frameRate;

    /**
     * 保存路径
     */
    private String path;

    /**
     * 视频来源
     */
    private String source;

    /**
     * 标签
     */
    private String keywords;


    /**
     * 视频创建时间
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
     * 视频名称
     */
    private String videoName;


    /**
     * 是否公开
     */
    private boolean isPublic;


    /**
     * 视频大小
     */
    private String size;


    /**
     * 文件长度
     */
    private long fileLength;

    /**
     * 视频时长
     */
    private long duration;

    /**
     * 视频高
     */
    private int height;

    /**
     * 视频宽
     */
    private int width;

    /**
     * 视频分辨率
     */
    private float resolution;

    /**
     * 字符串格式视频时长
     */
    private String durationStr;

}
