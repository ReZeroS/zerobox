package club.qqtim;

import club.qqtim.executor.Executor;
import club.qqtim.executor.support.ValidExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @version: 1.0
 * @author: jie.li13@hand-china.com
 * @date: 2020/4/3
 * @description:
 */
public class Application {


    public static void main(String[] args) {
        List<Executor> executors = loadExecutors();
        executors.forEach(Executor::execute);
    }

    private static List<Executor> loadExecutors() {
        List<Executor> executors = new ArrayList<>();
        executors.add(new ValidExecutor());
        return executors;
    }
}
