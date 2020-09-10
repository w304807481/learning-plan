package com.github.opensharing.archdesign.pattern.observer;

/**
 * WakenUp监听者
 *
 * @author jwen
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
