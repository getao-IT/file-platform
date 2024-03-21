package cn.aircas.fileManager.elec.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@TableName(value = "file_elec_content")
public class ElecContent {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 文本文件id
     */
    private int elecFileId;

    /**
     * 内容
     */
    private String content;

    /**
     * 行号
     */
    private int lineNumber;
}
