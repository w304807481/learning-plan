package com.github.opensharing.archdesign.pattern.iterator;

/**
 * 模拟实现链表容器
 */
public class LinkedList implements Collection {
    private Node first = null;
    private Node last = null;
    private int size = 0;

    @Override
    public void add(Object o) {
        Node newNode = new Node(o, null);
        if (first == null) {
            first = newNode;
            last = newNode;
        }

        last.setNextNode(newNode);
        last = newNode;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator iterator() {
        return new LinkedIterator();
    }

    private class LinkedIterator implements Iterator {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public Object next() {
            Node n = first;
            Object currrentObject = null;
            int i = 0;
            while (i < currentIndex) {
                n = n.getNextNode();
                i++;
            }
            currentIndex++;
            currrentObject = n.getData();
            return currrentObject;
        }

    }
}

