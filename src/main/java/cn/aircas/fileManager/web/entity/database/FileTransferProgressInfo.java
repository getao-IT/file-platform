package cn.aircas.fileManager.web.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "file_transfer_progress_info")
public class FileTransferProgressInfo {

    @Id
    @Column(name = "id", unique = true, columnDefinition = "serial2")
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    /**
     * MD5
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String md5;

    /**
     * 总分块数
     */
    @Column(nullable = true, columnDefinition = "int4")
    private int chunks;

    /**
     * 上传分片数
     */
    @Column(nullable = true, columnDefinition = "int4")
    private int transferred_chunk;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "timestamp(6)")
    private Date create_time;

    /**
     * 文件传输信息id
     */
    @Column(nullable = true, columnDefinition = "int4")
    private int file_transfer_id;
}
