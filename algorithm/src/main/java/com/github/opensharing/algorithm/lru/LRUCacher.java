package com.github.opensharing.algorithm.lru;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于LRU算法的 O(1) 时间复杂度的实现
 * <p>
 * 不直接基于LinkedHashMap，自主使用基本数据结构实现
 *
 * @author jwen
 * Date 2021-01-30
 */
public class LRUCacher {

    /**
     * LRU缓存大小
     */
    private int size;

    /**
     * 链表节点
     */
    static class Node {
        private String key;
        private Node prev;
        private Object data;
        private Node next;

        public Node(String key, Object data) {
            this.key = key;
            this.data = data;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[")
                    //.append("prev=").append((prev != null) ? prev.getKey() : "null")
                    .append("key=").append(key)
                    //.append("next=").append((next != null) ? next.getKey() : "null")
                    .append("]");

            return builder.toString();
        }
    }

    /**
     * 链表头
     */
    private Node head;

    /**
     * 链表尾
     */
    private Node tail;

    /**
     * 节点hash
     */
    private Map<String, Node> nodeMap = new HashMap<>();

    public LRUCacher(int size) {
        this.size = size;
    }

    /**
     * 缓存数据
     *
     * @param key
     * @param data
     */
    public void put(String key, Object data) {
        //1.存在即刷新
        if (nodeMap.containsKey(key)) {
            resfesh(nodeMap.get(key));
        }
        //2.不存在即新增
        else {
            //缓存node
            Node node = new Node(key, data);
            nodeMap.put(key, node);
            //lru 检查
            resfesh(node);
        }
    }

    /**
     * 根据KEY获取缓存数据
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        Node node = nodeMap.get(key);

        if (node != null) {
            //更新位置
            resfesh(node);
        }

        return node;
    }

    /**
     * 更像链表及HASH缓存
     *
     * @param latestNode
     */
    private void resfesh(Node latestNode) {
        //首次访问
        boolean first = false;
        if (head == null) {
            first = true;
            head = latestNode;
        }

        //让命中的节点的前后节点首尾相连
        Node prev = latestNode.getPrev();
        Node next = latestNode.getNext();

        if (prev != null) {
            prev.setNext(next);
        }
        if (next != null) {
            next.setPrev(prev);
        }

        //如果非首次，命中的节点就是head，设置新的head
        if (!first && (head == latestNode)) {
            head = next;
        }

        //在链表尾部追加命中的节点
        if (tail != null) {
            tail.setNext(latestNode);
            latestNode.setPrev(tail);
            latestNode.setNext(null);
        }
        //设置新的tail
        tail = latestNode;

        //移除链表最不常用的头部node
        removOlder();
    }

    private void removOlder() {
        if (nodeMap.size() > size) {
            //hash移除head
            nodeMap.remove(head.getKey());
            //设置新head
            Node older = head;
            head = head.getNext();
            head.setPrev(null);
            //for gc
            older.setPrev(null);
            older.setNext(null);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HEAD=" + head);
        builder.append(", link---->");

        if (head != null) {
            Node start = head;
            do {
                builder.append(start).append(",");
                start = start.getNext();
            } while (start != null);
        }

        builder.append(" TAIL=" + tail);
        return builder.toString();
    }
}
