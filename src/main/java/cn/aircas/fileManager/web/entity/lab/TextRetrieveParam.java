package cn.aircas.fileManager.web.entity.lab;

import lombok.Data;

import java.util.List;

@Data
public class TextRetrieveParam {

    private int pageNo;
    private String text;
    private int pageSize;
    private int userId;
    private List<Integer> imageIdList;

}
