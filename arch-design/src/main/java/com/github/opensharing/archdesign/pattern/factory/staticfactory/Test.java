package com.github.opensharing.archdesign.pattern.factory.staticfactory;

public class Test {
    public static void main(String[] args) {
		/*Movable m = Car.getInstance();//����ģʽ
		Movable m1 = Car.getInstance();
		if(m==m1)System.out.println("same car");*/


        //Movable m = new Train();

        VehicleFactory vf = new CarFactory();
        Movable m = vf.create();
        m.move();
    }
}
