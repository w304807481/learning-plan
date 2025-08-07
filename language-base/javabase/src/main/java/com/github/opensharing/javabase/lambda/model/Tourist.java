package com.github.opensharing.javabase.lambda.model;

import com.github.opensharing.javabase.lambda.Moveable;
import com.github.opensharing.javabase.lambda.Stayable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Tourist extends Person {
    private String name;

    @Override
    public String toString() {
        return name;
    }

    /**
     * 出行
     * @param moveable
     */
    private void move(Moveable moveable) {
        moveable.move();
    }

    /**
     * 住宿
     * @param stayable
     */
    private void stay(Stayable stayable) {
        stayable.stay();
    }

    /**
     * 旅行
     */
    public void travel() {
        //出行
        move();
        //住宿
        stay();
    }

    /**
     * 出行
     */
    public void move() {
        //一般写法
        this.move(() -> {
            System.out.println("坐飞机");
        });

        //一般写法
        this.move(() -> {
            //徒步
            this.run();
        });

        //简化写法
        this.move(super :: run);
    }

    public void stay() {
        this.stay(() -> {
            System.out.println("住酒店");
        });

        //一般写法
        this.stay(() -> {
            this.camping();
        });

        //简化写法
        this.stay(this :: camping);
    }

    private void camping() {
        System.out.println("搭帐篷露营");
    }
}
