package club.qqtim.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @version: 1.0
 * @author: jie.li13@hand-china.com
 * @date: 2020/7/19
 * @description:
 */
public class LruCache <K, V> {

    private final Map<K, V> lruCache;

    private final int CACHE_SIZE;

    public LruCache(Map<K, V> lruCache, int cacheSize) {
        this.CACHE_SIZE = cacheSize;
        this.lruCache = Collections.synchronizedMap(
                new LinkedHashMap<K, V>(100, .75F, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size () > CACHE_SIZE;
            }
        });
    }

    public Map<K, V> getLruCache() {
        return lruCache;
    }

}
