package club.qqtim.cache;

import lombok.Data;

/**
 * 1、调用 get(key) 方法时，要返回该 key 对应的 val。
 *
 * 2、只要用 get 或者 put 方法访问一次某个 key，该 key 的 freq 就要加一。
 *
 * 3、如果在容量满了的时候进行插入，则需要将 freq 最小的 key 删除，如果最小的 freq 对应多个 key，则删除其中最旧的那一个。
 *
 *  O(1) 的时间内解决这些需求
 */
public class LfuCache <K, V> {

    @Data
    static class Node<K, V> {

        private K k;

        private V v;

        private Node<K, V>  next;

    }



    private final int CACHE_SIZE;



    // 构造容量为 capacity 的缓存
    public LfuCache(int capacity) {
        this.CACHE_SIZE = capacity;
    }

    // 在缓存中查询 key
    public V get(K key) {
        // if cacheMap[key] == null return null

        // freqMap[key] += 1;

        // return cacheMap.get[key]
        return null;
    }

    // 将 key 和 val 存入缓存
    public void put(K key, V val) {
        // freqMap[key] += 1;

        // cacheMap[key] = val;

        // if cacheMap.size > CACHE_SIZE
            // then remove findMinFreqKey


    }

    public K findMinFreqKey(){
        // minFreqKeyList = findMinFreqKeyList

        // minFreqKeyList.oldestKey

        return null;
    }


    public static void main(String[] args) {
        // 构造一个容量为 2 的 LFU 缓存
        LfuCache cache = new LfuCache(2);

        // 插入两对 (key, val)，对应的 freq 为 1
        cache.put(1, 10);
        cache.put(2, 20);

        // 查询 key 为 1 对应的 val
        // 返回 10，同时键 1 对应的 freq 变为 2
        cache.get(1);

        // 容量已满，淘汰 freq 最小的键 2
        // 插入键值对 (3, 30)，对应的 freq 为 1
        cache.put(3, 30);

        // 键 2 已经被淘汰删除，返回 -1
        cache.get(2);
    }

}
