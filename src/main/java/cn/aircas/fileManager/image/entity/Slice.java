package cn.aircas.fileManager.image.entity;

import cn.aircas.fileManager.web.entity.enums.FileType;
import lombok.Data;

import java.util.Map;


@Data
public class Slice {
    /**
     * 样本id
     */
    private int id;
    /**
     * 存储地址
     */
    private String sliceInsertPath;
    /**
     * 宽
     */
    private int width;
    /**
     * 高
     */
    private int height;
    /**
     * 步长
     */
    private int step;
    /**
     * 是否入库
     */
    private boolean isInsert;
    /**
     * 样本类型
     */
    private FileType fileType;
    /**
     * 自定义坐标 宽 高
     */
    private Map<Integer , Integer> params;
    /**
     * 开始经度
     */
    private double minLon;
    /**
     * 开始纬度
     */
    private double minLat;
    /**
     * 结束经度
     */
    private double maxLon;
    /**
     * 结束纬度
     */
    private double maxLat;



}


