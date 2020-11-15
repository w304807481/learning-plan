## ELK最佳实践

ELK Stack是最受欢迎的开源日志平台

1. #### 为什么要ELK

   ##### 为什么要ELK

   - 日志(log) = 数据 + 时间戳;

   - cat/tail/grep/less/awk/cut+正则表达式不能满足业务的统计需要;

   - 查找某天某个服务某个文件某个时间点的日志;

   - 快速只查看警告或者更严重的信息;

   - 有些日志问题在几天前就是已知的,如何区分与忽略;

   - 某个错误信息可能出现在集群的每个服务器上,怎么确定是哪台出了问题呢;

   - 如果涉及到日志回溯,现在凌晨3点运维能支持吗;

   ##### 基本需求:

   - 具有索引,检索,排序,分类,可视化,分析日志的功能

   - 可根据数据量的增加横向扩展

   - 有对外的API,对接已有的其他系统

   ##### 扩展需求:

   - 提高系统可维护性

   - 提高安全性,保护用户信息

   - 取样或者全部记录

   ##### 我们需要搭建一套如下图的结构系统:

   ![ELK](/Users/chenwenying/Downloads/ELK.png)

2. #### 什么是ELK

   ELK 通常 由 Elasticsearch、Logstash、Kibana 和 Beats 四个组件组成

   - [**Beats**]，是轻量型采集器的平台，从边缘机器向 Logstash 和 Elasticsearch 发送数据。

   - [**Logstash**]，集中、转换和存储数据，是动态数据收集管道，拥有可扩展的插件生态系统，能够与 Elasticsearch 产生强大的协同作用。

   - [**Elasticsearch**]，搜索、分析和存储您的数据，是基于 JSON 的分布式搜索和分析引擎，专为实现水平扩展、高可靠性和管理便捷性而设计。

   - [**Kibana**]，实现数据可视化，导览 Elastic Stack。能够以图表的形式呈现数据，并且具有可扩展的用户界面，供您全方位配置和管理 Elastic Stack

3. #### ELK原理有哪些

   **Elasticsearch**主要原理:

   - Lucene基本概念,TODO未完待续

   - Elasticsearch基本概念,TODO未完待续

   - Elasticsearch的启动过程,TODO未完待续

   - 索引基本使用,TODO未完待续
   - 索引生命周期的管理,TODO未完待续
   - 数据在Elasticsearch中是如何存储的,,TODO未完待续

   - 文档CRUD,TODO未完待续

   - 分词器,TODO未完待续

   - Elasticsearch中的Mapping,TODO未完待续

   - Elasticsearch的搜索过程,TODO未完待续

   **Logstash**主要功能原理:

   - Logstash Grok数据结构化,TODO未完待续

   - Logstash自定义正则表达式ETL,TODO未完待续

   - Kafka Group实现Logstash的高可用,TODO未完待续

   - logstash grok filter插件的使用,TODO未完待续

   - Filebeat与Logstash的关系,TODO未完待续

4. #### 快速部署一个ELK
   TODO,补充
5. #### 常见问题

   - `Elasticsearch` 启动的时候，如果报错"max virtual memory areas vm.maxmapcount [65530] is too low"

   ```
   ##修改最大虚拟内存数
   $ sudo sysctl -w vm.max_map_count=262144
   ```

   - 默认情况下，`Elasticsearch` 只允许本机访问,外网访问不了

   ```
   ##将 network.host 前面的注释去掉，同时将值改成 0.0.0.0，表示所有机器都可以访问，然后重启
   $ vim config/elasticsearch.yml
   ```