package com.github.opensharing.archdesign.pattern.observer;

import java.io.IOException;
import java.util.Properties;

/**
 * @author jwen
 */
public class ChildSleepObserverViaConfigTest {
    public static void main(String[] args) {
        Child child = new Child();
        Properties props = new Properties();
        try {
            props.load(ChildSleepObserverViaConfigTest.class.getClassLoader().getResourceAsStream("observer.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] observers = props.getProperty("observers").split(",");


        for (String observer : observers) {
            try {
                child.addWakenUpListener((WakenUpListener) (Class.forName(observer).newInstance()));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        new Thread(child).start();
    }
}
