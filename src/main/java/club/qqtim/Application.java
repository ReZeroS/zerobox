package club.qqtim;

import club.qqtim.dimension.InputUnit;
import club.qqtim.util.KvObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * @version: 1.0
 * @author: rezeros.github.io
 * @date: 2020/4/3
 * @description:
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {

    final ConcurrentLinkedQueue<Object> objects = new ConcurrentLinkedQueue<>();


    public void init(){
        objects.offer(new InputUnit(1L));
        objects.offer(new InputUnit(2L));
        objects.offer(new InputUnit(3L));
    }

    public void sss(){
        int a[][] = new int[3][2];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                System.out.println(a[i][j]);
            }
        }
    }


    public static <T> Optional<T> ofEmpty(T value) {
        if (value == null) {
            return Optional.empty();
        } else {
            if (value instanceof Collection) {
                final Collection collection = (Collection) value;
                if (collection.isEmpty()) {
                    return Optional.empty();
                }
            }
        }

        return Optional.of(value);
    }

    static class Solution {
        public int longestValidParentheses(String s) {
            // dp[i] 是指以 s[i] 结尾的最长有效
            int dp[] = new int[Math.max(s.length(), 2)];
            dp[0] = 0;
            dp[1] = s.length() > 1 && s.charAt(0) == '(' && s.charAt(1) == ')' ? 2: 0;
            int max = Math.max(dp[0], dp[1]);
            for(int i = 2; i < s.length(); ++i) {
                char b = s.charAt(i - 1);
                char c = s.charAt(i);
                if(b == '(') {
                    if (c == ')') {
                        dp[i] = dp[i - 2] + 2;
                    } else {
                        dp[i] = 0;
                    }
                } else {
                    if(c == '(') {
                        dp[i] = 0;
                    } else {
                        int preIdx = i - 1 - dp[i - 1];
                        if (preIdx >= 0 && s.charAt(preIdx) == '(') {
                            dp[i] = 2 + dp[i - 1];

                        }
                        if(preIdx > 0) {
                            dp[i] = dp[i] + dp[preIdx - 1];
                        }

                        int prexIdx = i - 1 - dp[i - 1];
                        int t = prexIdx >= 0 && s.charAt(prexIdx) == '('? 1: 0;
                        dp[i] = t == 0? 0: 2 + dp[i - 1] ;
                        if(prexIdx > 0) {
                            dp[i] = dp[i] + dp[prexIdx - 1];
                        }
                    }
                }
                max = Math.max(dp[i], max);
            }
            return max;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Solution solution = new Solution();
        int i = solution.longestValidParentheses("()()))))()()(");


//
//
//        final List<byte[]> list = new ArrayList<>();
//
//        int i = 0;
//        int memory_size = 3;
//        while(true) {
//            Thread.sleep(500);
////            log.info("分配了 {} m", ++i * memory_size  );
//            byte[] bytes = new byte[memory_size * 1024];
//            for (int i1 = 0; i1 < bytes.length; i1++) {
////                bytes[i] = (Math.random() == 0.0 ? 1b : 0b);
//            }
//            list.add(bytes);
//        }
//        ofEmpty(list).map(ids -> {
//            System.out.println(ids);
//            return Arrays.asList("111");
//        }).orElse(Collections.emptyList());
//        System.out.println(UUID.randomUUID().toString().replace("-", "").length());

//        Map<String, String> say = new HashMap<>();
//        say.put("A", "1.3");
//        say.put("B", "22");
//        say.put("C", "521");
//        say.put("D", "");
//        say.put("E", "55");
//
//        List<KvObject> bigDecimal = say.entrySet().stream().sorted((o1, o2) -> {
//            String passPercentage1 = o1.getValue();
//            String passPercentage2 = o2.getValue();
//            if (StringUtils.isBlank(passPercentage1)) {
//                return 1;
//            }
//            if (StringUtils.isBlank(passPercentage2)) {
//                return -1;
//            }
//            BigDecimal bigDecimal1 = new BigDecimal(passPercentage1);
//            BigDecimal bigDecimal2 = new BigDecimal(passPercentage2);
//            return bigDecimal2.compareTo(bigDecimal1);
//        }).map(entry -> new KvObject(entry.getKey(), entry.getValue())).collect(Collectors.toList());
//        System.out.println(bigDecimal);
//
//
//        List<KvObject> kvObjectList = say.entrySet().stream()
//                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.nullsLast(Comparator.reverseOrder())
//                ))
//                .map(entry -> {
//                    final String departmentId = entry.getKey();
//                    return new KvObject(departmentId,
//                            entry.getValue() == null ? null : String.valueOf(entry.getValue()));
//                }).collect(Collectors.toList());
//
//        System.out.println(kvObjectList);
//        final Application application = new Application();
//        application.init();
//        application.sss();
//        ManagerConfig config = new ManagerConfig();
//        config.setReader(new LiquibaseXmlReader());
//        config.setAbstractFactory(new LiquibaseFactory());
//        config.setResource(new ClassPathResource("xml/master.xml"));
//        config.setExecutor(new LiquibaseValidExecutor("valid.json"));
//        Manager liquibaseManager = new LiquibaseManager(config);
//        liquibaseManager.manage();
//        liquibaseManager.execute();
//
//        while (true) {
//            String maxPrefix = "";
//            maxPrefix = "".charAt(0) + "";
//
//        }
//        new SpringApplication(Application.class).run(args);
    }

}
