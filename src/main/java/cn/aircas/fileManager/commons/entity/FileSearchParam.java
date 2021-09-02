package cn.aircas.fileManager.commons.entity;

import cn.aircas.fileManager.web.entity.enums.FileType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class FileSearchParam {
    /**
     * 查询用户id
     */
    private int userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 查询结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 查询页码
     */
    private int pageNo =1;

    /**
     * 查询开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;


    /**
     * 查询批次号
     */
    private int batchNumber;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 是否获取文件内容
     */
    private boolean content;


    /**
     * 查询页数量
     */
    private int pageSize = 10;

    /**
     * 文件类型
     */
    private FileType fileType;

    /**
     * 模糊查询输入字段，可通过影像名称，来源，关键字，用户名进行模糊查询
     */
    private String searchParam;

    private List<String> searchParamList;

    private List<Integer> fileIdList = new ArrayList<>();



    public void setFileIdList(String fileIdListStr){
        List<String> fileList = Arrays.asList(fileIdListStr.split(","));
        fileList.forEach(str->fileIdList.add(Integer.valueOf(str.trim())));
    }


}
