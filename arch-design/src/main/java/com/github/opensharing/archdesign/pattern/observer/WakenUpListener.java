package com.github.opensharing.archdesign.pattern.observer;

/**
 * 请编写类注释
 *
 * @author wenzongwei
 * Date 2020-05-10
 */
interface WakenUpListener {
    /**
     * 处理醒来事件
     *
     * @param wakenUpEvent
     */
    void actionToWakenUP(WakenUpEvent wakenUpEvent);
}
