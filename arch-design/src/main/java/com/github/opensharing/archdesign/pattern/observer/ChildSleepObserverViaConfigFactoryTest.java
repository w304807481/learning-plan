package com.github.opensharing.archdesign.pattern.observer;

import java.io.IOException;
import java.util.Properties;

/**
 * 通过配置文件定义观察者
 *
 * @author jwen
 */
public class ChildSleepObserverViaConfigFactoryTest {
    public static void main(String[] args) {
        Child c = new Child();

        String[] observers = ProprietyMgr.getProperty("observers").split(",");


        for (String s : observers) {
            try {
                c.wakenUpListeners.add((WakenUpListener) (Class.forName(s).newInstance()));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        new Thread(c).start();
    }
}


class ProprietyMgr {

    private static Properties props = new Properties();

    static {
        try {
            props.load(ClassLoader.getSystemResourceAsStream("observer.properties"));
        } catch (IOException e1) {
            System.out.println("配置文件加载失败！！");
            e1.printStackTrace();
        }

    }

    private ProprietyMgr() {
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

}