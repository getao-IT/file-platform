package cn.aircas.fileManager.image.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class ImageSearchParam {
    /**
     * 查询用户id
     */
    private int userId;

    /**
     * 來源
     */
    private String source;

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
     * 关键字
     */
    private String keywords;

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
     * 查询页数量
     */
    private int pageSize = 10;


    /**
     * 影像文件名称
     */
    private String imageName;

    /**
     * 影像id列表
     */
    private List<Integer> imageIdList;


    /**
     * 模糊查询输入字段，可通过影像名称，来源，关键字，用户名进行模糊查询
     */
    private List<String> searchParamList;

}
