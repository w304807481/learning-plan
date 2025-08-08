# Project Reactor框架

## 简介
> 对响应式流规范的一种实现,
> Spring WebFlux 默认的响应式框架,
> 完全异步非阻塞，对背压的支持,
> 提供两个异步序列API:Flux[N] 和Mono [0|1],
> 提供对响应式流的操作

## 基本使用

### 1. 创建Flux序列
> ReactorTester.testFlux

### 2. 创建Mono序列
> ReactorTester.testMono

### 3. 使用from工厂
> ReactorTester.testFluxFrom

### 4. 订阅响应式流
> ReactorTester.testFluxSubscribe

### 5. 映射响应式流元素
> ReactorTester.testFluxMap

### 6. 过滤响应式流， 裁剪流中元素
> ReactorTester.testFluxFilter

### 7. 收集响应式流
> ReactorTester.testFluxCollect
> ReactorTester.testFluxOthers

### 8. 组合响应式流
> ReactorTester.testFluxStreamMerge

### 9. 流的批处理
> ReactorTester.testFluxStreamBatch

### 10. 流的FlatMap
> ReactorTester.testFluxStreamFlatMap

### 11. 元素采样
> ReactorTester.testFluxStreamSample

### 12. 流转化为阻塞结构
> ReactorTester.testFluxStreamBlock
 
### 14. 物化和非物化信号
> ReactorTester.testFluxStreamMaterialize
