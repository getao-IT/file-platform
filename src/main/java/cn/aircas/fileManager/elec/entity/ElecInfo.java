package cn.aircas.fileManager.elec.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "file_elec_info")
public class ElecInfo {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private int id;


    private int count;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 是否公开
     */
    private boolean isPublic = true;

    /**
     * 名称
     */
    private String elecName;

    /**
     * 保存路径
     */
    private String path;

    /**
     * 视频来源
     */
    private String source;

    /**
     * 用户名称
     */
    private String userName;

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
     * 视频大小
     */
    private String size;

    /**
     * 文件长度
     */
    @TableField(exist = false)
    private long fileLength;


}
