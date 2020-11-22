## 1. rabbitmq 安装步骤

### 一 安装依赖环境

1.在 http://www.rabbitmq.com/which-erlang.html 页面查看安装rabbitmq需要安装erlang对应的版本。

2.在 https://github.com/rabbitmq/erlang-rpm/releases 页面找到需要下载的erlang版本.

3.复制地址后，使用 wget 命令下载

4.安装 erlang

```
下载 : wget http://www.rabbitmq.com/releases/erlang/erlang-18.3-1.el7.centos.x86_64.rpm
安装 : rpm -ivh erlang-18.3-1.el7.centos.x86_64.rpm
```

5.安装 socat

```
下载: wget http://repo.iotti.biz/CentOS/7/x86_64/socat-1.7.3.2-5.el7.lux.x86_64.rpm
安装: rpm -ivh socat-1.7.3.2-5.el7.lux.x86_64.rpm
```

6 安装rabbitmq

```
下载: wget http://www.rabbitmq.com/releases/rabbitmq-server/v3.6.5/rabbitmq-server-3.6.5-1.noarch.rpm
安装: rpm -ivh rabbitmq-server-3.6.5-1.noarch.rpm
```

安装成功后的输出

```
-rw-r--r--. 1 root root 18345424 Apr  6  2016 erlang-18.3-1.el7.centos.x86_64.rpm
-rw-r--r--. 1 root root  5520417 Aug  6  2016 rabbitmq-server-3.6.5-1.noarch.rpm
-rw-r--r--. 1 root root   284676 Jun 23  2017 socat-1.7.3.2-5.el7.lux.x86_64.rpm
[root@rabbitmq home]# rpm -ivh erlang-18.3-1.el7.centos.x86_64.rpm
Preparing...                          ################################# [100%]
Updating / installing...
   1:erlang-18.3-1.el7.centos         ################################# [100%]
[root@rabbitmq home]# rpm -ivh socat-1.7.3.2-5.el7.lux.x86_64.rpm
warning: socat-1.7.3.2-5.el7.lux.x86_64.rpm: Header V4 DSA/SHA1 Signature, key ID 53e4e7a9: NOKEY
Preparing...                          ################################# [100%]
Updating / installing...
   1:socat-1.7.3.2-5.el7.lux          ################################# [100%]
[root@rabbitmq home]# rpm -ivh rabbitmq-server-3.6.5-1.noarch.rpm
warning: rabbitmq-server-3.6.5-1.noarch.rpm: Header V4 RSA/SHA1 Signature, key ID 6026dfca: NOKEY
Preparing...                          ################################# [100%]
Updating / installing...
   1:rabbitmq-server-3.6.5-1          ################################# [100%]
```



### 二 启动rabbitmq

安装完成后, 还没启动服务, 先查看rabbitmq服务状态， 命令: `service rabbitmq-server status

```
[root@rabbitmq ~]# service rabbitmq-server status
Status of node rabbit@rabbitmq ...
Error: unable to connect to node rabbit@rabbitmq: nodedown
DIAGNOSTICS
===========
attempted to contact: [rabbit@rabbitmq]
rabbit@rabbitmq:
  * connected to epmd (port 4369) on rabbitmq
  * epmd reports: node 'rabbit' not running at all
                  no other nodes on rabbitmq
  * suggestion: start the node
current node details:
- node name: 'rabbitmq-cli-66@rabbitmq'
- home dir: /var/lib/rabbitmq
- cookie hash: Syc9KCEzBF1lpHR35HW08Q==
```

提示 ： Error: unable to connect to node rabbit@rabbitmq: nodedown。

ok 启动rabbitmq 服务，命令 `service rabbitmq-server start`, 检查是否启动成功: `ps -ef | grep rabbitmq`



### 三 启用后台管理插件

此时, 服务已经启动, 但通过浏览器访问`http://localhost:15672`或`ip:15672`还是不能访问管理界面, 因为还没启用插件。

启用命令: `rabbitmq-plugins enable rabbitmq_management`

```
[root@rabbitmq ~]# rabbitmq-plugins enable rabbitmq_management
The following plugins have been enabled:
  mochiweb
  webmachine
  rabbitmq_web_dispatch
  amqp_client
  rabbitmq_management_agent
  rabbitmq_management
Applying plugin configuration to rabbit@rabbitmq... started 6 plugins.
```



提示`Applying plugin configuration to rabbit@wangzaiplus... started 6 plugins.`即表示启用成功

浏览器远程访问`http://192.168.1.123:15672`, 出现登录界面.

**注意:**
如果服务器开启了防火墙, 则访问不了, 可以先关闭防火墙或者暴露端口
centos7查看防火墙状态: `firewall-cmd --state`, 关闭后显示`not running`，开启后显示`running`
关闭防火墙: `systemctl stop firewalld.service`

### 四 开启用户远程访问

rabbitmq从3.3.0开始, 默认用户`guest`只允许本机访问, 即: `http://localhost:15672`, 如果通过`ip:port`访问, 会发现`Login failed`, 为了让`guest`用户能够远程访问, 只需新建配置文件配置`loopback_users`即可(`rabbitmq.config`配置文件需手动创建), 步骤如下:

`cd /etc/rabbitmq`
 新建配置文件: `touch rabbitmq.config`
 `vim rabbitmq.config`
 写入并保存: `[{rabbit, [{loopback_users, []}]}].`
 重启服务: `service rabbitmq-server restart`
 重新以`guest`登录, OK

目前为止, rabbitmq服务已安装完毕.



## 2.rabbitmq 集群总结

### rabbitmq部署模式

**1.单一模式**：即单机情况不做集群，就单独运行一个rabbitmq而已。

**2.普通模式**：默认模式。以两个节点（rabbit01、rabbit02）为例来进行说明。对于Queue来说，消息实体只存在于其中一个节点rabbit01（或者rabbit02），rabbit01和rabbit02两个节点仅有相同的元数据，即队列的结构。当消息进入rabbit01节点的Queue后，consumer从rabbit02节点消费时，RabbitMQ会临时在rabbit01、rabbit02间进行消息传输，把A中的消息实体取出并经过B发送给consumer。所以consumer应尽量连接每一个节点，从中取消息。即对于同一个逻辑队列，要在多个节点建立物理Queue。否则无论consumer连rabbit01或rabbit02，出口总在rabbit01，会产生瓶颈。当rabbit01节点故障后，rabbit02节点无法取到rabbit01节点中还未消费的消息实体。如果做了消息持久化，那么得等rabbit01节点恢复，然后才可被消费；如果没有持久化的话，就会产生消息丢失的现象。

**3.镜像模式：** 把需要的队列做成镜像队列，存在与多个节点属于RabbitMQ的HA方案 及高可用方案。该模式解决了普通模式中的问题，其实质和普通模式不同之处在于，消息实体会主动在镜像节点间同步，而不是在客户端取数据时临时拉取。该模式带来的副作用也很明显，除了降低系统性能外，如果镜像队列数量过多，加之大量的消息进入，集群内部的网络带宽将会被这种同步通讯大大消耗掉。所以在对可靠性要求较高的场合中适用。



#### 普通模式搭建

1.因为RabbitMQ的集群是依赖erlang集群，而erlang集群是通过这个cookie进行通信认证的。**所以第一步是要保证每个rabbitmq 节点的 erlang cookie值 是一致的，且权限为owner只读**。而这个cookie值是保存在安装机器的 .erlang.cookie 文件中。 文件存放地址有两个： 1. /var/lib/rabbitmq; 2.当前用户目录下，如当前用户是root  则存放在 /root 目录下。

2.cookie 设置好了后查看一下集群状态, 输入命令  ./rabbitmqctl cluster_status ， 信息如下

```
[root@rabbitmq1 bin]# ./rabbitmqctl cluster_status
Cluster status of node rabbit@rabbitmq1 ...
[{nodes,[{disc,[rabbit@rabbitmq1]},{ram,[rabbit@rabbitmq]}]},
 {running_nodes,[rabbit@rabbitmq,rabbit@rabbitmq1]},
 {cluster_name,<<"rabbit@rabbitmq1">>},
 {partitions,[]},
 {alarms,[{rabbit@rabbitmq,[]},{rabbit@rabbitmq1,[]}]}]
```

3.停止当前机器中rabbitmq的服务

```
[root@rabbitmq bin]# ./rabbitmqctl stop_app
```

4.把 rabbitmq1 中的rabbitmq加入到集群中来

```
[root@rabbitmq bin]# ./rabbitmqctl join_cluster --ram rabbit@rabbitmq1
```

5.开启当前机器的rabbitmq服务

```
[root@rabbitmq bin]# ./rabbitmqctl start_app
```

6.打开rabbitmq web 管理界面查看节点情况,  如下  则普通模式的集群 部署完成了

![](D:\MyNutCloud\截图\rabbitmq.jpg)



#### 镜像模式搭建

镜像模式是基于普通模式的，所以要实现高可用方案，我们就需要在普通集群上进行升级，升级成镜像集群的 rabbitmq 高可用方案。

并且镜像模式要依赖policy模块，这个模块是做什么用的呢？

policy中文来说是政策，策略的意思，那么他就是要设置，那些Exchanges或者queue的数据需要复制，同步，如何复制同步？policy模块就是做这些数据复制同步的。 如下为设置命令

```
./rabbitmqctl set_policy <name> [-p <vhost>] <pattern> <definition> [--apply-to <apply-to>]
    name: 策略名称
    vhost: 指定vhost, 默认值 /
    pattern: 需要镜像的正则
    definition: 
        ha-mode: 指明镜像队列的模式，有效值为 all/exactly/nodes 
            all     表示在集群所有的节点上进行镜像，无需设置ha-params
            exactly 表示在指定个数的节点上进行镜像，节点的个数由ha-params指定 
            nodes   表示在指定的节点上进行镜像，节点名称通过ha-params指定 
        ha-params: ha-mode 模式需要用到的参数 
        ha-sync-mode: 镜像队列中消息的同步方式，有效值为automatic，manually
    apply-to: 可选值3个，默认all
        exchanges 表示镜像 exchange (并不知道意义所在)
        queues    表示镜像 queue
        all       表示镜像 exchange和queue
eg:
./rabbitmqctl set_policy test "test" '{"ha-mode":"all","ha-sync-mode":"automatic"}' 

```



本机设置 policy策略为 test， 添加的exchange test ， queue test

```
[root@rabbitmq ~]# cd /usr/lib/rabbitmq/bin/
[root@rabbitmq bin]#  ./rabbitmqctl set_policy test "test" '{"ha-mode":"all","ha-sync-mode":"automatic"}'
Setting policy "test" for pattern "test" to "{\"ha-mode\":\"all\",\"ha-sync-mode\":\"automatic\"}" with priority "0" ...
[root@rabbitmq bin]# rabbitmqctl list_policies
Listing policies ...
/       test    all     test    {"ha-mode":"all","ha-sync-mode":"automatic"}    0
[root@rabbitmq bin]# rabbitmqctl list_queues name policy pid slave_pids
Listing queues ...
test    test    <rabbit@rabbitmq1.3.21066.1>    [<rabbit@rabbitmq.2.29328.1>]
[root@rabbitmq bin]# 
```

![](D:\MyNutCloud\截图\rabbitmqHA.jpg)

如上图 在 Features 展示了使用的策略 test ，表示该队列是镜像队列，没有的就是普通队列。

```
测试: exchange = test, queue = test
    case1: pattern=test, apply-to=exchanges -> 结果 exchange被镜像
    case2: pattern=test, apply-to=queues    -> 结果    queue被镜像
    case3: pattern=test, apply-to=all       -> 结果    queue被镜像
结论: 不知道exchange被镜像的意义所在，镜像queue才是关键

ps
	保证集群的高可用，至少要有1个disc节点
    RabbitMQ Cluster 全部挂掉，RAM节点无法先启动，必须先启动disc节点
```



#### 负载均衡

使用haproxy做负载均衡。

#### 主要配置

```
haproxy配置文件   linux路径：/usr/local/haproxy/conf/haproxy.cfg
global
   log 127.0.0.1 local0
   log 127.0.0.1 local1 notice
   daemon
   nbproc 8
   maxconn 4096
   user haproxy
   group haproxy
   chroot /usr/local/haproxy

defaults
    log global
    mode tcp
    option tcplog
    option dontlognull
    retries 3
    option httpclose
    option abortonclose
    maxconn 4096
    timeout connect 5000ms
    timeout client 10000ms
    timeout server 8000ms
    timeout check 3000
    option  httpclose
    balance roundrobin
	
listen stats
    bind 0.0.0.0:1080			#登录监控台的端口和地址
    mode http
    option httplog
    log 127.0.0.1 local0 err warning
    maxconn 10
    stats refresh 10s
    stats uri /stats				#监控台地址：http://haproxy_ip:1080/stats
    stats realm Haproxy Manager
    stats auth user1:password #登录监控台的账户和密码
    stats auth user2:password #登录监控台的账户和密码
    stats hide-version
    stats admin if TRUE
	
frontend Rabbitmq_frontend  
    bind 0.0.0.0:5673     #rabbitmq服务调用端口配置
    mode tcp
    option    clitcpka
    timeout client  100m
    maxconn 10000
    default_backend  Rabbitmq_Backend

backend Rabbitmq_Backend
    mode tcp
    timeout queue   100m
    timeout server  300m
    option srvtcpka
    option redispatch
    fullconn 10000
    balance roundrobin
    option tcp-check     #配置rabbitmq 服务调用ip 和 端口
    server server242 192.168.146.131:5672 weight 3 maxconn 10000 check port 5672 inter 5000 rise 1 fall 2 backup
    server server243 192.168.146.132:5672 weight 3 maxconn 10000 check port 5672 inter 5000 rise 1 fall 2
	
listen rabbitmq_admin 
    bind 0.0.0.0:8004
    server node1 192.168.146.131:15672  # 配置监听的rabbitmq服务
    server node2 192.168.146.132:15672
```



## 3. 问题汇总

#### 问题一  启动报错

```
[root@192 ~]# service rabbitmq-server start
Starting rabbitmq-server (via systemctl):  Job for rabbitmq-server.service failed because the control process exited with error code. See "systemctl status rabbitmq-server.service" and "journalctl -xe" for details.                               [FAILED]
[root@192 ~]# ^C
[root@192 ~]# cat /var/log/rabbitmq/startup_log
ERROR: epmd error for host 192: badarg (unknown POSIX error)
```

解决办法：节点名称解析失败，设置节点名称

```
[root@192 ~]# vi /etc/rabbitmq/rabbitmq-env.conf
NODENAME=rabbit@rabbitmq
[root@192 ~]# service rabbitmq-server start
Starting rabbitmq-server (via systemctl):                  [  OK  ]
```



#### 问题二   各个节点之间不能相互连接，集群失败

```
[root@rabbitmq bin]#  ./rabbitmqctl cluster_status
Cluster status of node rabbit@rabbitmq ...
Error: unable to connect to node rabbit@rabbitmq: nodedown
DIAGNOSTICS
===========
attempted to contact: [rabbit@rabbitmq]
rabbit@rabbitmq:
  * connected to epmd (port 4369) on rabbitmq
  * epmd reports node 'rabbit' running on port 25672
  * TCP connection succeeded but Erlang distribution failed
  * suggestion: hostname mismatch?
  * suggestion: is the cookie set correctly?
  * suggestion: is the Erlang distribution using TLS?
current node details:
- node name: 'rabbitmq-cli-84@rabbitmq'
- home dir: /root
- cookie hash: rNIeXAA1FEuL2LKxY0cN5A==
```

产生原因：rabbitmq的运行依赖 erlang 环境，各个节点之间若要做集群，则必须保持  .erlang.cookie 文件内容一致。而安装rabbitmq 后 会在两个路径下生成 .erlang.cookie 文件（/var/lib/rabbitmq  和 当前用户路径下，eg： 我是root用户 就在 /root 路径下）。在本次测试中造成节点连接不上原因是 两个路径下的 cookie 值不同。

解决办法：将 当前用户路径下的 .erlang.cookie 文件 拷贝 到 /var/lib/rabbitmq 路径下，使两者内容一致。

#### 问题三  添加集群节点时 连接不到添加节点

```
[root@rabbitmq bin]# ./rabbitmqctl join_cluster --ram rabbit@rabbitmq1
Clustering node rabbit@rabbitmq with rabbit@rabbitmq1 ...
Error: unable to connect to nodes [rabbit@rabbitmq1]: nodedown
DIAGNOSTICS
===========
attempted to contact: [rabbit@rabbitmq1]
rabbit@rabbitmq1:
  * unable to connect to epmd (port 4369) on rabbitmq1: nxdomain (non-existing domain)
current node details:
- node name: 'rabbitmq-cli-64@rabbitmq'
- home dir: /root
- cookie hash: rNIeXAA1FEuL2LKxY0cN5A==
```

解决办法  /etc/hosts 中把各个主机节点的hostname 加上



```
[root@rabbitmq1 bin]# ./rabbitmqctl join_cluster --ram rabbit@rabbitmq
Clustering node rabbit@rabbitmq1 with rabbit@rabbitmq ...
Error: unable to connect to nodes [rabbit@rabbitmq]: nodedown
DIAGNOSTICS
===========
attempted to contact: [rabbit@rabbitmq]
rabbit@rabbitmq:
  * unable to connect to epmd (port 4369) on rabbitmq: address (cannot connect to host/port)
current node details:
- node name: 'rabbitmq-cli-11@rabbitmq1'
- home dir: /root
- cookie hash: rNIeXAA1FEuL2LKxY0cN5A==
```

产生原因 无法解析 rabbitmq 映射路径，因为防火墙阻止了。

解决办法： 清理一下防火墙， 命令：**sudo iptables -F**



#### 问题四  使用ngnix做负载均衡 提示 not found

产生原因：因为在RabbitMQ的请求路径中，存在特殊的字符"%2F"，Nginx在反向代理时转换失败。因此，需要把"%2F"正常传给RabbitMQ

解决办法：

```
location / {
    port_in_redirect on;
    proxy_redirect off;
    proxy_pass http://rabbitmqserver;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header User-Agent $http_user_agent;
    proxy_set_header Host $http_host;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}

location ~* /rabbitmq/api/ {
    rewrite ^ $request_uri;
    rewrite ^/rabbitmq/api/(.*) /api/$1 break;
    return 400;
    proxy_pass http://rabbitmqserver$uri;
    proxy_buffering                    off;
    proxy_set_header Host              $http_host;
    proxy_set_header X-Real-IP         $remote_addr;
    proxy_set_header X-Forwarded-For   $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}
```



## 4.rabbitmq 配置的备份与恢复

链接官网：https://www.rabbitmq.com/management-cli.html

​       **注意：此处的备份只是备份RabbitMQ用户、vhost、队列、交换和绑定，备份文件是RabbitMQ元数据的JSON表示，我们将使用rabbitmqadmin命令行工具进行备份。**

1.下载rabbitmqadmin

​	启用管理插件后，下载与HTTP API交互的rabbitmqadmin Python命令行工具，它可以从任何启用了管理插件的RabbitMQ节点下载：**http://{node-hostname}:15672/cli/**

   下载后，使文件可执行并将其移动到/usr/local/bin目录：

```
chmod +x rabbitmqadmin
sudo mv rabbitmqadmin /usr/local/bin
```

2.备份配置 与 恢复mq配置

备份配置命令：

```
rabbitmqadmin export <backup-file-name>
比如：
$ rabbitmqadmin export rabbitmq-backup-config.json
Exported definitions for localhost to "rabbitmq-backup-config.json"
导出写入文件filerabbitmq-backup-config.json。
```

恢复配置命令

```
rabbitmqadmin import <JSON backup file >
比如：
$ rabbitmqadmin import rabbitmq-backup.json 
Imported definitions for localhost from "rabbitmq-backup.json"
```





```shell
# 将rabbitmqadmin 复制到 /usr/local/bin 目录下
cp -r /var/lib/rabbitmq/mnesia/rabbit-plugins-expand/rabbitmq_management-3.6.5/priv/www/cli/rabbitmqadmin /usr/local/bin/rabbitmqadmin
# 授权rabbitmqadmin可执行
chmod +x /usr/local/bin/rabbitmqadmin
# 执行导入命令 文件存在路径
rabbitmqadmin import rabbitmq-backup.json 
```

