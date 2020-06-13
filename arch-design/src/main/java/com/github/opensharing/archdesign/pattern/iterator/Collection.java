package com.github.opensharing.archdesign.pattern.iterator;

/**
 * 模拟Collection
 *
 * @author Administrator
 */
public interface Collection {

    void add(Object o);

    int size();

    Iterator iterator();
}
