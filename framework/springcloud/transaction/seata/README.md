# Seata 分布式事务研究项目

## 项目定位

用于系统学习 Seata 分布式事务框架，包含：
- Seata Server（TC）部署配置
- 多种事务模式演示（AT、TCC、Saga、XA）
- 常见业务场景实践

## 版本匹配

### 环境信息

| 基础设施 | 版本 | 说明 |
|---------|------|------|
| Nacos | r-nacos 0.8.6 | Rust 版 Nacos，已在 Docker 中部署 |
| MySQL | 8.0 | 运行在 3306 端口 |
| Docker | 本地 Docker 环境 | 用于部署 Seata Server |

### 技术栈版本

| 组件 | 版本 | 说明 |
|------|------|------|
| JDK | 17 | |
| Spring Boot | 3.2.10 | |
| Spring Cloud | 2023.0.4 | |
| Spring Cloud Alibaba | 2023.0.3.2 | 稳定版，支持 Nacos 2.x |
| Nacos Client | 2.1.4 | grpc 协议，与 r-nacos 0.8.6 兼容 |
| Seata | 2.3.0 | 客户端与服务端统一版本 |

> **Nacos Client 版本说明**：r-nacos 官方推荐使用 nacos-client 2.1.x（grpc 协议），与 0.8.6 版本兼容良好。

## Seata 工作架构与原理

### 1. 核心角色

Seata 分布式事务架构包含三个核心角色：

```
┌─────────────────────────────────────────────────────────────────┐
│                          Seata Server (TC)                       │
│                     Transaction Coordinator                      │
│                     事务协调器，维护全局事务状态                    │
└─────────────────────────────────────────────────────────────────┘
                    ▲                           │
                    │                           │
         全局事务发起                          分支事务注册
         全局事务提交/回滚                     分支事务执行结果
                    │                           │
                    ▼                           │
┌─────────────────────────────────────────────────────────────────┐
│                      Application (TM)                          │
│                      事务管理器，标注全局事务                      │
│                      @GlobalTransactional                        │
└─────────────────────────────────────────────────────────────────┘
                    ▲                           │
                    │                           │
                    │                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Databases (RM)                             │
│                      资源管理器，管理分支事务使用的资源              │
│                      每个数据库实例为一个 RM                       │
└─────────────────────────────────────────────────────────────────┘
```

| 角色 | 全称 | 说明 |
|------|------|------|
| **TC** | Transaction Coordinator | 事务协调器，独立部署，维护全局事务状态 |
| **TM** | Transaction Manager | 事务管理器，应用端组件，发起和管理全局事务 |
| **RM** | Resource Manager | 资源管理器，管理分支事务操作的资源 |

### 2. 事务处理流程

#### 2.1 正常提交流程

```
TM                                    TC                                      RM
 │                                     │                                       │
 │── 开启全局事务 ─────────────────────>│                                       │
 │                                     │                                       │
 │<─ 返回 XID (全局事务ID) ─────────────│                                       │
 │                                     │                                       │
 │                                     │<── 注册分支事务 ──────────────────────│
 │                                     │                                       │
 │                                     │<── 注册分支事务 ──────────────────────│
 │                                     │                                       │
 │── 提交/回滚全局事务 ────────────────>│                                       │
 │                                     │                                       │
 │                                     │── 通知分支提交/回滚 ─────────────────>│
 │                                     │                                       │
 │                                     │<── 分支执行结果 ──────────────────────│
 │                                     │                                       │
 │<─ 全局事务结果 ─────────────────────│                                       │
```

#### 2.2 分支事务执行

1. **开启全局事务**：TM 向 TC 申请开启一个全局事务，获得全局事务 ID（XID）
2. **执行分支事务**：各 RM 执行本地分支事务
3. **注册分支事务**：每个分支事务向 TC 注册，关联到全局事务
4. **提交/回滚**：TM 向 TC 发起全局提交或回滚
5. **分支处理**：TC 向各 RM 发送分支提交或回滚指令
6. **结果汇总**：TC 汇总所有分支结果，返回给 TM

### 3. 四种事务模式

#### 3.1 AT 模式（自动回滚）

**原理**：
- 基于本地 ACID 事务特性
- 自动生成反向 SQL 进行回滚
- 无需手动编写回滚逻辑

**工作流程**：
```
        全局事务开启
              │
              ▼
    ┌─────────────────┐
    │   执行 SQL1      │
    │   执行 SQL2      │
    │   执行 SQL3      │
    └────────┬────────┘
             │
             ▼
    ┌─────────────────┐
    │ 生成 Undo Log    │  <-- 记录数据前后镜像
    │ (前置镜像 +      │
    │  后置镜像)        │
    └────────┬────────┘
             │
             ▼
    ┌─────────────────┐
    │   提交本地事务    │
    └────────┬────────┘
             │
             ▼
    ┌─────────────────┐
    │   注册分支到 TC   │
    └────────┬────────┘
             │
             ▼
      全局提交/回滚
             │
       ┌─────┴─────┐
       │           │
       ▼           ▼
    提交分支    生成反向SQL
                执行回滚
```

**优点**：
- 对业务零侵入
- 自动生成回滚 SQL
- 性能较高

**缺点**：
- 只能用于支持本地事务的存储引擎（如 InnoDB）
- 全局锁导致性能略有下降

#### 3.2 TCC 模式（两阶段提交）

**原理**：
- Try：预留资源（冻结）
- Confirm：确认使用资源
- Cancel：释放预留资源

**工作流程**：
```
        全局事务开启
              │
              ▼
    ┌─────────────────┐
    │      Try        │  <-- 预留资源，冻结金额/库存
    │  预留/冻结资源   │
    └────────┬────────┘
             │
             ▼
    ┌─────────────────┐
    │   注册分支到 TC   │
    └────────┬────────┘
             │
             ▼
      ┌──────┴──────┐
      │             │
      ▼             ▼
    全局提交       全局回滚
      │             │
      ▼             ▼
    ┌────┐      ┌───────┐
    │Confirm│     │ Cancel│
    │确认扣减│     │释放冻结│
    └────┘      └───────┘
```

**代码示例**：
```java
@LocalTCC
public interface AccountService {

    @TwoPhaseBusinessAction(
        name = "debitAction",
        commitMethod = "commit",
        rollbackMethod = "rollback"
    )
    boolean debit(@BusinessActionContextParameter(paramName = "userId") String userId,
                  @BusinessActionContextParameter(paramName = "amount") BigDecimal amount);

    boolean commit(BusinessActionContext context);
    boolean rollback(BusinessActionContext context);
}
```

**优点**：
- 性能高，不锁定资源
- 可以自定义补偿逻辑
- 适用于各类存储引擎

**缺点**：
- 对业务有侵入
- 需要处理空回滚、悬挂、幂等问题

#### 3.3 Saga 模式（长事务编排）

**原理**：
- 将长事务拆分为多个子事务
- 每个子事务都有对应的补偿操作
- 正向操作失败则执行补偿操作

**工作流程**：
```
    ┌────────────────────────────────────────────────────┐
    │                    Saga 编排器                      │
    └────────────────────────────────────────────────────┘
                          │
         ┌────────────────┼────────────────┐
         │                │                │
         ▼                ▼                ▼
    ┌─────────┐     ┌─────────┐     ┌─────────┐
    │步骤1: A │     │步骤2: B │     │步骤3: C │
    │创建订单 │     │扣减库存 │     │扣减余额 │
    └───┬─────┘     └───┬─────┘     └───┬─────┘
        │                │                │
        ▼                ▼                ▼
    ┌─────────┐     ┌─────────┐     ┌─────────┐
    │补偿1:   │     │补偿2:   │     │补偿3:   │
    │取消订单 │     │恢复库存 │     │恢复余额 │
    └─────────┘     └─────────┘     └─────────┘
```

**优点**：
- 适用于长事务场景
- 无锁执行，性能高
- 可以异步执行

**缺点**：
- 不保证隔离性
- 需要为每个子事务编写补偿逻辑
- 补偿逻辑可能失败

#### 3.4 XA 模式（强一致）

**原理**：
- 基于 XA 协议的两阶段提交
- 事务 coordinator 与 RM 直接通信
- Phase 1：Prepare（所有 RM 锁定资源）
- Phase 2：Commit/Rollback

**工作流程**：
```
    ┌──────────────────────────────────────┐
    │              TC (Coordinator)         │
    └──────────────────────────────────────┘
                      │
         ┌────────────┼────────────┐
         │            │            │
         ▼            ▼            ▼
    ┌────────┐   ┌────────┐   ┌────────┐
    │ Prepare│   │ Prepare│   │ Prepare│
    │ XA RM1 │   │ XA RM2 │   │ XA RM3 │
    │锁定资源 │   │锁定资源 │   │锁定资源 │
    └────────┘   └────────┘   └────────┘
         │            │            │
         └────────────┼────────────┘
                      │
                      ▼
    ┌──────────────────────────────────────┐
    │     Phase 2: Commit / Rollback       │
    └──────────────────────────────────────┘
                      │
         ┌────────────┼────────────┐
         ▼            ▼            ▼
      Commit        Commit        Commit
      释放锁        释放锁        释放锁
```

**优点**：
- 强一致性
- 基于数据库原生 XA 协议

**缺点**：
- 性能较低
- 需要数据库支持 XA 协议
- 锁定资源时间长

### 4. 模式对比

| 特性 | AT | TCC | Saga | XA |
|------|-----|-----|------|-----|
| **一致性** | 强一致 | 最终一致 | 最终一致 | 强一致 |
| **性能** | 高 | 高 | 高 | 低 |
| **侵入性** | 无 | 高 | 中 | 低 |
| **锁等待** | 全局锁 | 无 | 无 | 数据库锁 |
| **回滚方式** | 自动 | 自定义 | 自定义 | 自动 |
| **适用场景** | 普通业务 | 性能要求高 | 长事务 | 强一致要求 |

### 5. 全局锁机制

Seata AT 模式通过全局锁保证隔离性：

```
┌─────────────────────────────────────────────────────────────┐
│                         TC (Seata Server)                   │
│  ┌───────────────────────────────────────────────────────┐  │
│  │                    全局锁管理器                         │  │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────┐               │  │
│  │  │Lock:    │  │Lock:    │  │Lock:    │               │  │
│  │  │user:100 │  │order:5  │  │stock:20 │               │  │
│  │  └─────────┘  └─────────┘  └─────────┘               │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                           ▲
                           │ 获取/释放锁
                           │
┌─────────────────────────────────────────────────────────────┐
│                         RM (数据库)                          │
│  ┌───────────────────────────────────────────────────────┐  │
│  │                   Branch Transaction                    │  │
│  │                                                       │  │
│  │  1. 检查全局锁                                         │  │
│  │  2. 执行 SQL                                          │  │
│  │  3. 生成 Undo Log                                     │  │
│  │  4. 释放全局锁                                        │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

**锁的粒度**：行级锁（通过主键索引）

**等待机制**：
- 默认等待 30 次，每次间隔 30ms
- 超时后抛出 LockWaitTimeoutException

## 项目结构

```
transaction/seata/
├── seata-server/               # Seata Server 独立模块（TC）
│   └── docker-compose.yaml     # Docker Compose 部署配置
├── seata-at-sample/            # AT 模式示例（默认、隔离级别）
├── seata-tcc-sample/           # TCC 模式示例
├── seata-saga-sample/          # Saga 模式示例
├── seata-xa-sample/            # XA 模式示例
├── seata-business-demo/        # 组合业务场景（订单+库存+账户）
└── pom.xml                   # Maven 父 POM
```

## 模块详细设计

### 1. seata-server

- 部署方式：Docker Compose
- 运行模式：file（学习用）/ db（生产用）
- 配置说明：registry.conf、file.conf
- 对接 Nacos 2.x 作为注册/配置中心

### 2. seata-at-sample（AT 模式）

- 单服务单库场景
- 全局锁机制演示
- 回滚验证
- 隔离级别测试

### 3. seata-tcc-sample（TCC 模式）

- Try-Confirm-Cancel 三阶段设计
- 空回滚、悬挂问题处理
- 幂等性保障

### 4. seata-saga-sample（Saga 模式）

- 长事务编排
- 状态机配置
- 补偿机制

### 5. seata-xa-sample（XA 模式）

- 两阶段提交
- 与 AT 模式对比

### 6. seata-business-demo（综合示例）

- 订单服务：创建订单
- 库存服务：扣减库存
- 账户服务：扣减余额
- 模拟超时、分库等复杂场景

## 学习路径

```
第1步：环境准备（Seata Server + Nacos）
    ↓
第2步：AT 模式（最简单，自动回滚）
    ↓
第3步：TCC 模式（灵活，需要手动编写补偿）
    ↓
第4步：Saga 模式（长事务、服务编排）
    ↓
第5步：XA 模式（强一致，性能较低）
    ↓
第6步：综合实战（多服务协同）
```

## 数据库设计

### 1. 创建数据库

```sql
CREATE DATABASE IF NOT EXISTS seata_sample DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE seata_sample;
```

### 2. 账户表（业务表）

```sql
CREATE TABLE IF NOT EXISTS account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(64) NOT NULL UNIQUE COMMENT '用户ID',
    balance DECIMAL(10,2) DEFAULT 0 COMMENT '账户余额',
    frozen_amount DECIMAL(10,2) DEFAULT 0 COMMENT '冻结金额(TCC模式使用)',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账户表';

-- 插入测试数据
INSERT INTO account (user_id, balance, frozen_amount) VALUES
('user001', 1000.00, 0.00),
('user002',  500.00, 0.00);
```

### 3. Undo Log 表（Seata AT 模式 **必须**，字段严格与官方一致）

> **⚠️ 注意：该表是 AT 模式工作的前提条件，字段名、类型、索引必须严格按照以下 DDL 创建，否则会出现 `JDBC commit failed` / `PhaseOne_Failed` 错误。**

| 关键字段 | 说明 |
|---------|------|
| `branch_id` | 分支事务ID，主键 |
| `xid` | 全局事务ID，长度必须 128 |
| `log_created` / `log_modified` | 时间字段（**不是** log_created_time / log_modified_by） |
| `ext` | Seata 2.x 扩展字段，缺失会导致写入失败 |
| `ux_undo_log` | 唯一索引 (xid, branch_id) |

```sql
CREATE TABLE IF NOT EXISTS undo_log (
    `branch_id`     BIGINT       NOT NULL COMMENT '分支事务ID',
    `xid`           VARCHAR(128) NOT NULL COMMENT '全局事务ID(注意长度128)',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log上下文，如序列化配置',
    `rollback_info` LONGBLOB     NOT NULL COMMENT '回滚信息(before/after镜像)',
    `log_status`    INT          NOT NULL COMMENT '0:正常 1:防御状态',
    `log_created`   DATETIME     NOT NULL COMMENT '创建时间',
    `log_modified`  DATETIME     NOT NULL COMMENT '修改时间',
    `ext`           VARCHAR(100) DEFAULT NULL COMMENT 'Seata 2.x 扩展字段(必须存在)',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Seata AT模式回滚日志表';
```

## 交付清单

| 内容 | 说明 |
|------|------|
| 可运行代码 | 每个模块独立运行 |
| Docker Compose | 一键启动 Seata Server |
| 测试脚本 | 接口测试/异常场景演示 |
| README | 快速启动指南 |

## 快速开始

### 环境要求

| 组件 | 版本 | 检查命令 |
|------|------|----------|
| JDK | 17+ | `java -version` |
| Maven | 3.6+ | `mvn -version` |
| Docker | 20+ | `docker -v` |
| MySQL | 8.0 | 已在 3306 端口运行 |
| Nacos | 0.8.6 | 已在 8848 端口运行 |

### 步骤 1：初始化数据库（AT 模式前置条件）

Seata AT 模式要求业务库中存在 **undo_log 表**，且字段必须严格匹配官方 DDL。

```bash
# 通过 TCP 连接 MySQL（Docker 中运行需使用 -h 127.0.0.1，不能用 socket）
# 无密码：
MYSQL_CMD="mysql -h 127.0.0.1 -P 3306 -u root"
# 有密码（请替换 YOUR_PASSWORD）：
# MYSQL_CMD="mysql -h 127.0.0.1 -P 3306 -u root -pYOUR_PASSWORD"

# 执行建库 + 建表 + 初始化数据（DDL 见上文「数据库设计」章节）
$MYSQL_CMD -e "
CREATE DATABASE IF NOT EXISTS seata_sample DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE seata_sample;

-- 业务表：账户
CREATE TABLE IF NOT EXISTS account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(64) NOT NULL UNIQUE,
    balance DECIMAL(10,2) DEFAULT 0,
    frozen_amount DECIMAL(10,2) DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- AT 模式必须：undo_log 表（字段严格匹配官方 DDL）
CREATE TABLE IF NOT EXISTS undo_log (
    branch_id     BIGINT       NOT NULL,
    xid           VARCHAR(128) NOT NULL,
    context       VARCHAR(128) NOT NULL,
    rollback_info LONGBLOB     NOT NULL,
    log_status    INT          NOT NULL,
    log_created   DATETIME     NOT NULL,
    log_modified  DATETIME     NOT NULL,
    ext           VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (branch_id),
    UNIQUE KEY ux_undo_log (xid, branch_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入测试数据（仅首次需要）
INSERT IGNORE INTO account (user_id, balance, frozen_amount) VALUES
('user001', 1000.00, 0.00),
('user002',  500.00, 0.00);
"
# 验证：应看到 seata_sample + 两张表 (account + undo_log)
$MYSQL_CMD -e "USE seata_sample; SHOW TABLES;"
```

> ⚠️ **常见坑（已踩）**：如果 undo_log 表使用了错误的字段名（如 `log_created_time`、`log_created_by`）或缺少 `ext` 字段、`xid` 长度不够 128，会导致一阶段提交静默失败 `JDBC commit failed`（TC 日志显示 `PhaseOne_Failed`，但业务 SQL 本身能手动执行成功）。

### 步骤 2：检查环境

```bash
# 检查 Java 版本
java -version
# 应显示 17 或更高版本

# 检查 Maven 版本
mvn -version

# 检查 Docker
docker -v

# 检查 MySQL 连通性 + 验证表结构
$MYSQL_CMD -e "USE seata_sample; SELECT 'account' AS tbl, COUNT(*) AS n FROM account UNION ALL SELECT 'undo_log', COUNT(*) FROM undo_log;"
```

### 步骤 3：验证 Nacos

访问 Nacos 控制台确认服务正常运行：

```
http://localhost:8848/nacos
用户名: nacos
密码: nacos
```

### 步骤 4：启动 Seata Server

```bash
# 进入 seata-server 目录
cd /Users/jwen/Documents/workspace/learning-plan/framework/springcloud/transaction/seata/seata-server

# 启动 Seata Server（后台运行）
docker-compose up -d

# 查看日志确认启动成功
docker-compose logs -f seata-server
# 看到 "Server started..." 表示启动成功

# 停止服务
# docker-compose down
```

**验证 Seata Server 启动**：

```bash
# 检查容器状态
docker ps | grep seata

# 检查端口
lsof -i :8091
lsof -i :7091
```

### 步骤 5：启动应用

```bash
# 进入 seata-at-sample 模块目录
cd /Users/jwen/Documents/workspace/learning-plan/framework/springcloud/transaction/seata/seata-at-sample

# 编译打包（首次运行需要）
mvn clean package -DskipTests

# 启动应用
mvn spring-boot:run

# 或者运行打包后的 jar
# java -jar target/seata-at-sample-1.0.0-SNAPSHOT.jar
```

**验证应用启动**：

```bash
# 检查健康状态
curl http://localhost:8080/actuator/health

# 应返回 {"status":"UP"}
```

### 步骤 6：测试分布式事务

#### 6.1 查询账户余额

```bash
# 查询 user001 的账户信息
curl http://localhost:8080/account/user001

# 预期响应：{"userId":"user001","balance":1000.00}
```

#### 6.2 正常扣款（事务提交）

```bash
# 扣减 user001 账户 100 元
curl -X POST "http://localhost:8080/account/debit?userId=user001&amount=100"

# 预期：
# 1. 余额从 1000.00 变为 900.00
# 2. 返回 {"success":true,"message":"扣款成功"}
```

**验证余额变化**：

```bash
curl http://localhost:8080/account/user001
# 应返回 {"userId":"user001","balance":900.00}
```

#### 6.3 异常扣款（事务回滚）

```bash
# 模拟业务异常，触发回滚
curl -X POST "http://localhost:8080/account/debit?userId=user001&amount=100&error=true"

# 预期：
# 1. 返回 {"success":false,"message":"扣款失败: 模拟业务异常"}
# 2. 余额保持 900.00 不变（已扣减的 100 元被回滚）
```

**验证余额未变化**：

```bash
curl http://localhost:8080/account/user001
# 应返回 {"userId":"user001","balance":900.00}
```

### 步骤 7：查看日志

应用日志中可以看到 Seata 的执行过程：

```bash
# 查看 Seata 相关日志
grep -A5 "Seata" target/logs/spring.log

# 或者在启动时观察控制台输出
```

**关键日志**：

```
========== AT 模式扣减账户余额 ==========
userId: user001, amount: 100
扣减成功，当前余额: 900.00
```

### 快速测试脚本

```bash
#!/bin/bash

echo "===== Seata AT 模式测试 ====="

echo "1. 查询初始余额..."
curl -s http://localhost:8080/account/user001
echo ""

echo "2. 扣减 100 元（正常提交）..."
curl -X POST "http://localhost:8080/account/debit?userId=user001&amount=100"
echo ""

echo "3. 查询扣减后余额..."
curl -s http://localhost:8080/account/user001
echo ""

echo "4. 模拟异常扣款（触发回滚）..."
curl -X POST "http://localhost:8080/account/debit?userId=user001&amount=100&error=true"
echo ""

echo "5. 查询余额（应该与步骤3相同，未回滚）..."
curl -s http://localhost:8080/account/user001
echo ""

echo "===== 测试完成 ====="
```

## 常见问题

### Q1: Seata Server 启动失败

**检查项**：
- Docker 是否正常运行：`docker ps`
- 端口是否被占用：`lsof -i :8091`
- Nacos 是否可连接

**解决方案**：
```bash
# 重启 Seata Server
docker-compose down
docker-compose up -d
```

### Q2: 应用启动失败

**检查项**：
- MySQL 数据库是否创建
- Nacos 是否正常运行
- application.yaml 配置是否正确

**解决方案**：
```bash
# 确保数据库存在
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS seata_sample;"

# 使用 debug 模式启动
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

### Q3: 事务未生效

**检查项**：
- Seata Server 是否正常运行
- 是否正确引入 seata-spring-boot-starter
- @GlobalTransactional 注解是否添加
- **undo_log 表结构是否严格匹配官方 DDL**（参见 Q5）

### Q4: 全局锁等待超时

**原因**：并发更新同一行数据
**解决方案**：
- 增加重试次数
- 优化业务逻辑，减少并发冲突

### Q5: 扣减接口返回「JDBC commit failed」，TC 日志显示「PhaseOne_Failed」

**典型特征**：
- 业务 SQL 手动执行能成功（如 `UPDATE account SET balance = balance - 50 WHERE ...` 能正常扣减）
- 应用日志显示「注册分支成功、拿到全局锁」，但最终上报失败
- `undo_log` 表中无任何记录

**根因（100% 情况）**：**业务库中 undo_log 表字段名/字段长度/索引与 Seata 官方 DDL 不一致**，导致一阶段 UndoLog 写入 JDBC 失败，但业务 SQL 执行成功后本地事务提交被 ConnectionProxy 拦截并回滚。

**快速修复**：
```sql
-- 直接删掉老表，使用官方 DDL 重建（Seata 2.3.0 验证通过）
USE seata_sample;
DROP TABLE IF EXISTS undo_log;
CREATE TABLE undo_log (
    branch_id     BIGINT       NOT NULL COMMENT '分支事务ID',
    xid           VARCHAR(128) NOT NULL COMMENT '全局事务ID(必须128字符)',
    context       VARCHAR(128) NOT NULL,
    rollback_info LONGBLOB     NOT NULL,
    log_status    INT          NOT NULL,
    log_created   DATETIME     NOT NULL,
    log_modified  DATETIME     NOT NULL,
    ext           VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (branch_id),
    UNIQUE KEY ux_undo_log (xid, branch_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**历史上错误的写法（请自查）**：
- ❌ 使用了 `log_created_time / log_modified_time`（多了 `_time`）
- ❌ 使用了 `log_created_by / log_modified_by`（多了 `_by`）
- ❌ 漏掉了 `ext` 扩展列（Seata 2.x 新增，必须存在，可为 NULL）
- ❌ `xid` 长度是 100（必须是 128）
- ❌ 主键是 `id AUTO_INCREMENT`，但官方要求主键是 `branch_id`（虽然影响不大，但建议严格对齐）

## 进阶学习

### 下一步

1. **TCC 模式**：参考 `seata-tcc-sample` 模块
2. **Saga 模式**：参考 `seata-saga-sample` 模块
3. **多服务协同**：参考 `seata-business-demo` 模块

### 相关资源

- [Seata 官方文档](https://seata.io/zh-cn/)
- [Spring Cloud Alibaba 官方文档](https://spring-cloud-alibaba-group.github.io/github-pages/2023.0.3.2/zh-cn/)
- [r-nacos GitHub](https://github.com/nacos-group/r-nacos)
