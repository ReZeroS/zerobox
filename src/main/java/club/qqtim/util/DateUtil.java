package club.qqtim.util;

import club.qqtim.util.item.IntervalTime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @version: 1.0
 * @author: rezeros.github.io
 * @date: 2020/4/17
 * @description:
 */
public final class DateUtil {

    /**
     * 当天开始时间
     */
    public static ZonedDateTime getStartOfTheDay(ZonedDateTime zonedDateTime) {
        if (null == zonedDateTime) {
            return null;
        }
        return zonedDateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault());
    }

    /**
     * 当天结束时间，只精确到秒
     */
    public static ZonedDateTime getEndOfTheDay(ZonedDateTime zonedDateTime) {
        if (null == zonedDateTime) {
            return null;
        }
        return zonedDateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusSeconds(1);
    }


    /**
     * 验证是否存在重叠日期
     * @param from1 开始日期
     * @param to1 结束日期
     * @param from2 开始日期
     * @param to2 结束日期
     */
    public static boolean overlapTime(
            ZonedDateTime from1, ZonedDateTime to1,
            ZonedDateTime from2, ZonedDateTime to2){
        return (to2 == null || from1 == null || from1.isBefore(to2))
                && (to1 == null || from2 == null || to1.isAfter(from2));
    }

    /**
     * 验证是否存在重叠日期  值为 null 则对应方向的无限日期
     * @param entity 待校验的项
     * @param list 进行叫校验的数组
     * @param startDateFunction 从项里取出开始时间的方法
     * @param endDateFunction 从项里取出结束时间的方法
     * @param exclude 将 list 与 entity 比对决定是否排除 entity 的校验方法
     * @param action 如果校验出重叠日期，那么对 entity 采取的操作
     */
    public static <T> void invalidOverlapDate(
            T entity,
            List<T> list,
            Function<? super T, ZonedDateTime> startDateFunction,
            Function<? super T, ZonedDateTime> endDateFunction,
            BiPredicate<? super T, ? super T> exclude,
            Consumer<? super T> action){
        Objects.requireNonNull(startDateFunction);
        Objects.requireNonNull(endDateFunction);
        Objects.requireNonNull(action);
        for(T item : list){
            if (Objects.nonNull(exclude) && exclude.test(item, entity)) {
                continue;
            }
            ZonedDateTime from1 = startDateFunction.apply(entity);
            ZonedDateTime from2 = startDateFunction.apply(item);
            ZonedDateTime to1 = endDateFunction.apply(entity);
            ZonedDateTime to2 = endDateFunction.apply(item);
            if (overlapTime(from1, to1, from2, to2)) {
                action.accept(entity);
            }
        }
    }


    /**
     * merged time
     * 合并时间区域
     */
    public static List<IntervalTime> mergeIntervalTime(List<IntervalTime> intervals) {
        intervals.sort(Comparator.comparing(IntervalTime::getStartDateTime));
        LinkedList<IntervalTime> merged = new LinkedList<>();
        for (IntervalTime interval : intervals){
            if (merged.isEmpty() || (merged.getLast().getEndDateTime().isBefore(interval.getStartDateTime()))) {
                merged.add(interval);
            } else {
                merged.getLast().setEndDateTime(
                        merged.getLast().getEndDateTime().compareTo(interval.getEndDateTime()) < 0
                                ? interval.getEndDateTime() : merged.getLast().getEndDateTime()
                );
            }
        }
        return merged;
    }



}
