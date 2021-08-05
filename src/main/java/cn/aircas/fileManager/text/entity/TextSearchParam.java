package cn.aircas.fileManager.text.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class TextSearchParam {
    /**
     * 查询用户id
     */
    private int userId;



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
     * 来源
     */
    private String source;

    /**
     * 查询排序字段
     */
    private String orderBy;


    /**
     * 查询开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 标签
     */
    private String keywords;


    /**
     * 查询批次号
     */
    private int batchNumber;

    /**
     * 查询影像名称
     */
    private String textName;

    /**
     * 查询排序顺序
     */
    private String sortOrder;

    /**
     * 查询页数量
     */
    private int pageSize = 10;

    /**
     * 模糊查询输入字段，可通过影像名称，来源，关键字，用户名进行模糊查询
     */
    private String searchParam;

    private List<String> searchParamList;

}
