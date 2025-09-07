# Java引用类型演示

本包包含Java四种引用类型的演示代码和ThreadLocal内存泄露演示：

## 1. ReferenceTypesDemo.java
Java四种引用类型的基础演示：
- **强引用 (Strong Reference)**：默认引用类型，不会被垃圾回收
- **软引用 (Soft Reference)**：内存不足时会被回收，适合做缓存
- **弱引用 (Weak Reference)**：下次垃圾回收时会被回收
- **虚引用 (Phantom Reference)**：随时可能被回收，主要用于跟踪对象被回收的状态

## 2. ReferenceCacheDemo.java
引用类型在缓存中的应用演示：
- 软引用缓存实现
- 弱引用缓存实现
- 不同引用类型的缓存行为对比

## 3. ThreadLocalMemoryLeakDemo.java
ThreadLocal内存泄露问题演示：
- 不正确的ThreadLocal使用方法导致的内存泄露
- 正确的ThreadLocal使用方法（try-finally模式）
- ThreadLocal内存泄露的检测方法
- ThreadLocal使用最佳实践

## 运行方式
```bash
# 编译
mvn compile

# 运行引用类型基础演示
java -cp target/classes com.github.opensharing.javabase.reference.ReferenceTypesDemo

# 运行缓存应用演示
java -cp target/classes com.github.opensharing.javabase.reference.ReferenceCacheDemo

# 运行ThreadLocal内存泄露演示
java -cp target/classes com.github.opensharing.javabase.reference.ThreadLocalMemoryLeakDemo
```

## 引用类型特点总结

| 引用类型 | 回收时机 | 用途 | 是否必须配合ReferenceQueue |
|---------|---------|------|----------------------|
| 强引用 | 从不 | 普通对象引用 | 否 |
| 软引用 | 内存不足时 | 内存敏感缓存 | 否 |
| 弱引用 | GC时 | 临时缓存、规范化映射 | 否 |
| 虚引用 | 随时 | 对象回收跟踪、资源清理 | 是 |

## ThreadLocal内存泄露注意事项
- ThreadLocal变量在线程池环境中可能导致内存泄露
- 必须在线程结束前调用`remove()`方法清理ThreadLocal
- 推荐使用try-finally模式确保清理
- 避免在ThreadLocal中存储大对象
- 使用内存分析工具定期检查泄露

## 最佳实践
### 引用类型
- 虚引用的get()方法始终返回null
- 使用引用队列(ReferenceQueue)可以跟踪对象的回收状态
- 软引用适合实现内存敏感的缓存
- 弱引用适合实现临时缓存或规范化映射

### ThreadLocal使用
- 始终在finally块中调用remove()方法
- 使用ThreadLocal.withInitial()提供初始值
- 避免存储大对象，考虑使用弱引用
- 在线程池环境中特别小心内存泄露
- 定期使用内存分析工具检查泄露