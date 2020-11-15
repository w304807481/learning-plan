## 深入理解JMM(Java内存模型)

#### 1. 理论知识

- 1.1 Java 内存模型

     1.7: 
     
     1.8: 
    
#### 2. 案例实践

    2.1 配置JVMDemoApplication启动的VM options：-Xms50M -Xmx50M
        
        2.2 启动JVMDemoApplication
        
        2.3 启动jvisualvm，安装Visual GC 
        
        2.4 监控JVM 初始状态
        
            2.4.1 通过 Visual GC 直接图形化查看
            
            2.4.2 通过 jps -l , 和jstat -gc  pid  intervalTime  times  命令窗口来查看，原理相似。
    
        2.5 模拟对象创建，直到发生OOM
         
            2.5.1 模拟对象创建：浏览器访问 http://localhost:8080/jvm/object/new/{loop}
                loop=8 OOM发生
            
            2.5.2 模拟对象释放：浏览器访问 http://localhost:8080/jvm/object/gc/{loop}
            
        2.6 观察JVM 各代内存变化，GC次数，时间变化情况
            
            可以通过jvisualvm中主动发起垃圾回收（实际调用System.gc()， 触发full gc)