package club.qqtim.interview;

import lombok.Data;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version: 1.0
 * @author: jie.li13@hand-china.com
 * @date: 2020/7/19
 * @description:
 */
public class LruCache<K, V> {
    @Override
    public String toString() {
        return "LruCache{" +
                "linkList=" + linkList +
                '}';
    }

    @Data
    static class Node <K, V> {
        private K k;
        private V v;
        private Node<K, V> prev;
        private Node<K, V> next;

        public Node(K k, V v) {
            this.k = k;
            this.v = v;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "k=" + k +
                    ", v=" + v +
                    '}';
        }
    }

    private final int CACHE_SIZE;
    private final Map<K, Node<K, V>> lruCache;
    private final LinkedList<Node<K, V>> linkList;

    public Map<K, Node<K, V>> getLruCache() {
        return lruCache;
    }

    public LruCache(int cacheSize) {
        linkList = new LinkedList<>();
        this.CACHE_SIZE = cacheSize;
        this.lruCache = new ConcurrentHashMap<>(cacheSize);
    }


    public V get(K k) {
        Node<K, V> node = lruCache.get(k);
        if (node == null) {
            return null;
        } else {
            // move to head
            linkList.remove(node);
            linkList.offerFirst(node);
        }
        return node.v;
    }

    public void put(K k, V v) {
        Node<K, V> existNode = lruCache.get(k);
        if (existNode != null) {
            existNode.v = v;
            // move to head
            linkList.remove(existNode);
            linkList.offerFirst(existNode);
        } else {
            Node<K, V> node = new Node<>(k, v);
            // add to hashmap
            lruCache.put(node.k, node);
            // move to head
            linkList.offerFirst(node);
        }

        if (lruCache.size() > CACHE_SIZE) {
            // remove the oldest = tail node
            Node<K, V> kvNode = linkList.removeLast();
            lruCache.remove(kvNode.k);
        }
    }

    public static void main(String[] args) {
        int i  = 1;
        LruCache<Integer, Integer> cache = new LruCache<>(i += 2);

        cache.put(1, 1);
// cache = [(1, 1)]
        cache.put(2, 2);
// cache = [(2, 2), (1, 1)]
        cache.get(1);       // 返回 1
// cache = [(1, 1), (2, 2)]
// 解释：因为最近访问了键 1，所以提前至队头
// 返回键 1 对应的值 1
        cache.put(3, 3);
// cache = [(3, 3), (1, 1)]
// 解释：缓存容量已满，需要删除内容空出位置
// 优先删除久未使用的数据，也就是队尾的数据
// 然后把新的数据插入队头
        cache.get(2);       // 返回 -1 (未找到)
// cache = [(3, 3), (1, 1)]
// 解释：cache 中不存在键为 2 的数据
        cache.put(1, 4);
// cache = [(1, 4), (3, 3)]
// 解释：键 1 已存在，把原始值 1 覆盖为 4
// 不要忘了也要将键值对提前到队头


    }






}
