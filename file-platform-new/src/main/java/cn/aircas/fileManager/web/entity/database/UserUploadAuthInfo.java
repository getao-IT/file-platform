package cn.aircas.fileManager.web.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_upload_auth_info")
@TableName(value = "user_upload_auth_info")
public class UserUploadAuthInfo {

    @Id
    @Column(name = "id",unique = true,columnDefinition = "serial")
    @TableId(type = IdType.AUTO,value = "id")
    private int id;


    /**
     * 用户id
     */
    @Column(name = "user_id", nullable = false, columnDefinition = "int4")
    private int userId;

    /**
     * 用户权限
     */
    @Column(name = "admin_level", columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String adminLevel;

    /**
     * 已上传的文件总大小
     */
    @Column(name = "file_size", columnDefinition = "float4")
    private double fileSize;

    /**
     * 已上传文件个数
     */
    @Column(name = "file_count", columnDefinition = "int4")
    private int fileCount;

    /**
     * 已上传样本集合数
     */
    @Column(name = "sample_set_count", columnDefinition = "int4")
    private int sampleSetCount;

    /**
     * 已上传样本集总大小
     */
    @Column(name = "sample_set_size", columnDefinition = "float4")
    private double sampleSetSize;
}
