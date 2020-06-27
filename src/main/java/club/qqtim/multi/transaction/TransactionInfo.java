package club.qqtim.multi.transaction;

import lombok.Data;

/**
 * @version: 1.0
 * @author:  1726542850@qq.com
 * @date: 2020/5/30
 * @description: 保存所有线程的事务信息，可自定义扩展
 */
@Data
public class TransactionInfo {

    /**
     * true 将被回滚
     */
    private Boolean shouldRollBack;


}
