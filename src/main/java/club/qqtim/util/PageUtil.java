package club.qqtim.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.stream.Collectors;

public final class PageUtil {

    private PageUtil() {}



    public static <ENTITY> Page<ENTITY> getPage(long page, long size){
        final Page<ENTITY> entityPage = new Page<>();
        entityPage.setCurrent(page);
        entityPage.setSize(size);
        return entityPage;
    }

    public static <ENTITY> List<ENTITY> manualPage(List<ENTITY> totalList, int page, int size) {
        return totalList.stream().skip((long) (page - 1) * size).limit(size).collect(Collectors.toList());
    }






}
