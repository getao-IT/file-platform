package cn.aircas.fileManager.web.entity.lab;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AudioRetrieveParam {

    private int pageNo;
    private MultipartFile file;
    private int pageSize;
    private int userId;

}
