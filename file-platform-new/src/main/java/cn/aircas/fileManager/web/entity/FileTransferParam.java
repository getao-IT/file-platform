package cn.aircas.fileManager.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@TableName(value = "file_transfer_progress")
public class FileTransferParam {

    //当前为第几块分片
    private int chunk;

    //总分片数量
    private int chunks;

    // MD5
    private String md5;

    //块大小
    private long chunkSize;


    //创建时间
    private Date createTime;

    //分片对象
    private MultipartFile file;

    private int fileTransferId;



}
