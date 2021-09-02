package cn.aircas.fileManager.text.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("file_text_content")
public class TextContentInfo {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 文本文件id
     */
    private int textFileId;

    /**
     * 内容
     */
    private String content;

    /**
     * 行号
     */
    private int lineNumber;
}
