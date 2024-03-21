package cn.aircas.fileManager.text.entity;

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
@TableName("file_text_info")
public class TextInfo {
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
     * 来源
     */
    private String source;

    /**
     * 标签
     */
    private String keywords;

    /**
     * 影像上传批次
     */
    private int batchNumber;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 名称
     */
    private String textName;


    /**
     * 是否公开
     */
    private boolean isPublic;

    /**
     * 大小
     */
    private String size;

    /**
     * 文件中行数
     */
    private int lineCount;

    /**
     * 文件长度
     */
    private long fileLength;
}
