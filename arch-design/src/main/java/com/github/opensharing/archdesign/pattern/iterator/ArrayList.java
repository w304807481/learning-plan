package com.github.opensharing.archdesign.pattern.iterator;

/**
 * 模拟实现数组容器
 */
public class ArrayList implements Collection {

    private Object[] objects = new Object[10];
    /**
     * 记录容器中装有多少个对象
     */
    private int index = 0;

    @Override
    public void add(Object o) {
        if (index == objects.length) {
            Object[] newObjects = new Object[objects.length + 10];
            System.arraycopy(objects, 0, newObjects, 0, objects.length);
            objects = newObjects;
        }
        objects[index] = o;
        index++;
    }

    @Override
    public int size() {
        return index;
    }

    @Override
    public Iterator iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < index;
        }

        @Override
        public Object next() {
            Object currentObject = objects[currentIndex];
            currentIndex++;
            return currentObject;
        }
    }
}

