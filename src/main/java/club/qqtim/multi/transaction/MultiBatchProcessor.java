package club.qqtim.multi.transaction;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Stopwatch;
import com.hand.hcf.core.component.ApplicationContextProvider;
import com.hand.hcf.core.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @version: 1.0
 * @author:  1726542850@qq.com
 * @date: 2020/5/31
 * @description: 建议根据实际场景调整
 * @param <TEMP> DTO class
 * @param <DOMAIN> Domain class
 */
@Slf4j
public final class MultiBatchProcessor<TEMP, DOMAIN> {

    private static final int BATCH_SIZE = 500;

    private static final int AT_MOST_TASKS = 8;

    /**
     * 基础配置类
     */
    private final class PreparedProcessor {
        private final int totalSize;
        private List<Page<TEMP>> pages;
        private CountDownLatch transactionLock;
        private CountDownLatch startCountDownLatch;
        private CountDownLatch endCountDownLatch;
        private List<TransactionInfo> transactionInfos;

        public PreparedProcessor(int totalSize) {
            this.totalSize = totalSize;
        }

        public List<Page<TEMP>> getPages() {
            return pages;
        }

        public CountDownLatch getTransactionLock() {
            return transactionLock;
        }

        public CountDownLatch getStartCountDownLatch() {
            return startCountDownLatch;
        }

        public CountDownLatch getEndCountDownLatch() {
            return endCountDownLatch;
        }

        public List<TransactionInfo> getTransactionInfos() {
            return transactionInfos;
        }

        public PreparedProcessor invoke() {
            // 分 1-9 页, 一是为了防线程占用太多而是为了防连接数占用太多
            setPages();
            // 事务收集锁，收集所有事务状态后释放
            transactionLock = new CountDownLatch(1);
            // 单个事务开始锁
            startCountDownLatch = new CountDownLatch(pages.size());
            // 单个事务结束锁
            endCountDownLatch = new CountDownLatch(pages.size());
            // 保存所有线程的事务信息
            transactionInfos = Collections.synchronizedList(new ArrayList<>(pages.size()));
            return this;
        }

        private void setPages() {
            pages = new ArrayList<>();
            int currentPage = 0;
            int pageSize = getSizePerPage(totalSize);
            int total = totalSize;
            while (total > 0) {
                total -= pageSize;
                pages.add(PageUtil.getPage(currentPage++, pageSize));
            }
        }
    }

    private int getSizePerPage(int totalSize) {
        boolean moreThanDefaultTasks = ((totalSize / BATCH_SIZE) > AT_MOST_TASKS)
                || (((totalSize / BATCH_SIZE) == AT_MOST_TASKS) && ((totalSize % AT_MOST_TASKS) != 0));
        if (moreThanDefaultTasks) {
            return totalSize / AT_MOST_TASKS;
        }
        return BATCH_SIZE;
    }

    /**
     * @param asyncTaskExecutor 建议使用官方提供：(HcfAsyncTaskExecutor) @Qualifier("taskExecutor")
     * @param processMultiDataHandler 数据配置 handler
     * @param transactionAction 子线程需要执行的具体事务
     * @param callbackAfterInserted 子线程需要执行的回调函数，上述事务的执行结果可见对该方法可见
     */
    public List<DOMAIN> insertBatch(int totalSize, AsyncTaskExecutor asyncTaskExecutor,
                               ProcessMultiDataHandler<TEMP, DOMAIN> processMultiDataHandler,
                               Consumer<List<DOMAIN>> transactionAction,
                               Consumer<List<DOMAIN>> callbackAfterInserted) {
        PreparedProcessor preparedProcessor = new PreparedProcessor(totalSize).invoke();
        List<Page<TEMP>> pages = preparedProcessor.getPages();
        CountDownLatch transactionLock = preparedProcessor.getTransactionLock();
        CountDownLatch startCountDownLatch = preparedProcessor.getStartCountDownLatch();
        CountDownLatch endCountDownLatch = preparedProcessor.getEndCountDownLatch();
        List<TransactionInfo> transactionInfos = preparedProcessor.getTransactionInfos();

        log.info("====================== START INSERT ======================");
        // 计时
        Stopwatch stopwatch = Stopwatch.createStarted();

        // 最终收集到的完整数据，同步抛出给外层使用
        List<DOMAIN> fullResultList = new ArrayList<>();

        pages.forEach(page -> {
            List<TEMP> dataGroup = processMultiDataHandler.queryDataByPage(page);
            List<DOMAIN> subDataList = dataGroup.stream()
                    .map(processMultiDataHandler::toDomain).collect(Collectors.toList());
            asyncTaskExecutor.submit(
                    () -> {
                        PlatformTransactionManager txManager = ApplicationContextProvider
                                .getApplicationContext().getBean(PlatformTransactionManager.class);
                        // 创建事务定义
                        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
                        // 开新事务防一手
                        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                        // 获取事务状态
                        TransactionStatus status = txManager.getTransaction(transactionDefinition);

                        TransactionInfo transactionInfo = new TransactionInfo();
                        transactionInfo.setShouldRollBack(false);

                        try {
                            transactionAction.accept(subDataList);
                        } catch (Exception e) {
                            // 设置事务 共享态，可以是容器保存每个事务的状态或者是布尔做等幂，只决定是否需要全部线程都回滚
                            transactionInfo.setShouldRollBack(true);
                        } finally {
                            transactionInfos.add(transactionInfo);
                            // 子线程回馈主线程当前事务状态
                            startCountDownLatch.countDown();
                            // 等待主线程收集所有事务通知
                            try {
                                transactionLock.await();
                                // 检查子线程共享事务状态是否需要回滚，回滚则不走缓存，主线程也直接抛异常不发消息
                                boolean existRollBack = transactionInfos
                                        .stream().anyMatch(TransactionInfo::getShouldRollBack);
                                if (existRollBack) {
                                    status.setRollbackOnly();; // 回滚事务
                                }
                                // 提交，由 rollBackOnly决定是否回滚，不要手动回滚
                                txManager.commit(status);
                                if (!existRollBack) {
                                    // 回调函数, 完成所有线程插入后触发
                                    callbackAfterInserted.accept(subDataList);
                                    fullResultList.addAll(subDataList);
                                }
                            } catch (InterruptedException e) {
                                log.warn(e.getMessage());
                            } finally {
                                endCountDownLatch.countDown();
                            }
                        }
                    }
            );
        });

        // todo：死锁期限预防与异常的优雅处理
        try {
            startCountDownLatch.await();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        } finally {
            transactionLock.countDown();
        }
        try {
            endCountDownLatch.await();
        } catch (InterruptedException e) {
            log.warn(e.getMessage(), e);
        }

        long timeConsume = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        log.info("=========== END INSERT: " + timeConsume + " MS ===========" );
        return fullResultList;
    }



}
