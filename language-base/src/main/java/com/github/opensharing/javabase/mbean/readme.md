1. 开发MBean
   参见：https://blog.csdn.net/hobby_rain/article/details/80289804

2. 配置JMX参数
   在vm arguments中添加“-Dcom.sun.management.jmxremote"
   idea直接钩上 ----》 Enable JMX Agent

3. 运行springboot 启动方法

4. 模拟通过JMX 机制远程更改JVM中的MBean
      1) 浏览器访问 http://localhost:8080/mbean/user/get 返回 "jwen"
      2) 启动jconsole，到java home目录的bin下，运行jconsole, 通过updateName接口 更新name为 "张三"
      3) 浏览器访问 http://localhost:8080/mbean/user/get 返回 "张三"