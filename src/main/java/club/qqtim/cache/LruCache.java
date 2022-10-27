package club.qqtim.cache;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version: 1.0
 * @author: jie.li13@hand-china.com
 * @date: 2020/7/19
 * @description:
 */
public class LruCache <K, V> {

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

    static class DualLinkList <K, V> {
        private final Node<K, V> head;
        private final Node<K, V> tail;

        public DualLinkList() {
            this.head = new Node<>(null, null);
            this.tail = new Node<>(null, null);
            head.next = tail;
            tail.prev = head;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            Node <K,V> p = head.next;
            while(p != tail) {
                s.append(p.toString());
                p = p.next;
            }
            return "DualLinkList{" + s + '}';
        }

        public void put(Node<K, V> node) {
            // add to head
            // A <-> tail
            // A-> node
            tail.prev.next = node;
            // A <-> node
            node.prev = tail.prev;
            // A <-> node <- tail
            tail.prev = node;
            // A <-> node <-> tail
            node.next = tail;
        }



        public K removeOldest() {
            Node<K, V> lastNode = tail.prev;
            if (lastNode == head) {
                return null;
            }
            lastNode.prev.next = tail;
            tail.prev = lastNode.prev;
            lastNode.prev = null;
            lastNode.next = null;
            return lastNode.k;
        }

        public void moveToHead(Node<K, V> node) {
            // 已经是头节点，不操作
            if (head.next == node) {
                return;
            }
            // node 左右节点连起来
            node.next.prev = node.prev;
            node.prev.next = node.next;

            // 头节点的prev 指向node
            head.next.prev = node;
            //node 的next 指向头节点
            node.next = head.next;

            // node 的 prev 指向首
            node.prev = head;
            // 首 的 next 指向 node
            head.next = node;
        }


    }


    private final int CACHE_SIZE;

    private final DualLinkList<K, V> linkList;
    private final Map<K, Node<K, V>> lruCache;


    public LruCache(int cacheSize) {
        linkList = new DualLinkList<>();
        this.CACHE_SIZE = cacheSize;
        this.lruCache = new ConcurrentHashMap<>(cacheSize);
    }

    @Override
    public String toString() {
        return "LruCache{" +
                "linkList=" + linkList +
                '}';
    }

    public Map<K, Node<K, V>> getLruCache() {
        return lruCache;
    }


    public V get(K k) {
        Node<K, V> node = lruCache.get(k);
        if (node == null) {
            return null;
        } else {
            // move to head
            linkList.moveToHead(node);
        }
        return node.v;
    }

    public void put(K k, V v) {
        Node<K, V> existNode = lruCache.get(k);
        if (existNode != null) {
            existNode.v = v;
            linkList.moveToHead(existNode);
        } else {
            Node<K, V> node = new Node<>(k, v);
            linkList.put(node);
            // add to hashmap
            lruCache.put(node.k, node);
            // move to head
            linkList.moveToHead(node);
        }


        if (lruCache.size() > CACHE_SIZE) {
            // remove the oldest = tail node
            K oldestKey = linkList.removeOldest();
            lruCache.remove(oldestKey);
        }
    }

    public static void main(String[] args) {
        LruCache<Integer, Integer> cache = new LruCache<>(2);
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
