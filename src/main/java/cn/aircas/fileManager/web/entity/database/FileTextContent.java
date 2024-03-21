package cn.aircas.fileManager.web.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "file_text_content")
public class FileTextContent {

    @Id
    @Column(name = "id", unique = true, columnDefinition = "serial4")
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    /**
     * 文本文件id
     */
    @Column(nullable = true, columnDefinition = "int4")
    private int textFileId;

    /**
     * 内容
     */
    @Column(columnDefinition = "text COLLATE \"pg_catalog\".\"default\"")
    private String content;

    /**
     * 行号
     */
    @Column(nullable = true, columnDefinition = "int4")
    private int lineNumber;
}
