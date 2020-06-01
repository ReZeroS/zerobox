package club.qqtim.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 1726542850@qq.com
 * rezeros.github.io
 * description:
 */
@Data
public class IndexConstraint {

    @ExcelProperty("idxNo")
    private Integer idxNo;

    @ExcelProperty("tableName")
    private String tableName;

    @ExcelProperty("tableNameStr")
    private String tableNameStr;

    @ExcelProperty("firstIndex")
    private String firstIndex;

    @ExcelProperty("secondIndex")
    private String secondIndex;

    @ExcelProperty("thirdIndex")
    private String thirdIndex;

    @ExcelProperty("fourthIndex")
    private String fourthIndex;

    @ExcelProperty("fifthIndex")
    private String fifthIndex;

}
