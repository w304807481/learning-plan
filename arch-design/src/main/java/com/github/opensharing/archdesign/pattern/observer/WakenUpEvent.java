package com.github.opensharing.archdesign.pattern.observer;

/**
 * 请编写类注释
 *
 * @author wenzongwei
 * Date 2020-05-10
 */
class WakenUpEvent extends Event {
    /**
     * 醒的时间
     */
    private long time;
    /**
     * 醒的位置
     */
    private String loc;
    /**
     * 事件源对象（小孩本身）
     */
    private Object source;

    public WakenUpEvent(long time, String loc, Object source) {
        super();
        this.time = time;
        this.loc = loc;
        this.source = source;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }


}
