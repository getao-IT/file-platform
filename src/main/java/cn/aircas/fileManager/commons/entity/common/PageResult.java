package cn.aircas.fileManager.commons.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResult<T> {
    private long pageNo;
    private List<T> result;
    private long totalCount;


}
