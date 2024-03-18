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

    private String adminLevel;


    private String userName;

    /**
     * 查询结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 查询页码
     */
    private long pageNo =1;


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
     * 是否是获取文件内容信息
     */
    private boolean content;


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
     * 是否公开
     */
    private boolean isPublic;

    /**
     * 是否公开查询用
     */
    private String ispub = "";

    /**
     * 模糊查询输入字段，可通过影像名称，来源，关键字，用户名进行模糊查询
     */
    private String searchParam;

    /**
     * 是否重数据集查看
     * @param fileIdListStr
     */
    private boolean isFromDataset;



    private List<Integer> textIdList;

    private List<String> searchParamList;

    public void setIspub(String ispub) {
        this.ispub = ispub;
        if (ispub.toLowerCase().equals("t")) {
            this.isPublic = true;
        }
        if (ispub.toLowerCase().equals("f"))  {
            this.isPublic = false;
        }
    }


}
