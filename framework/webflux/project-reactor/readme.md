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

### 4. Mono延迟执行
> ReactorTester.testMonoDefer

### 5. 订阅响应式流
> ReactorTester.testFluxSubscribe

### 6. 映射响应式流元素
> ReactorTester.testFluxMap

### 7. 过滤响应式流， 裁剪流中元素
> ReactorTester.testFluxFilter

### 8. 收集响应式流
> ReactorTester.testFluxCollect
> ReactorTester.testFluxOthers

### 9. 组合响应式流
> ReactorTester.testFluxStreamMerge

### 10. 流的批处理
> ReactorTester.testFluxStreamBatch

### 11. 流的FlatMap
> ReactorTester.testFluxStreamFlatMap

### 12. 元素采样
> ReactorTester.testFluxStreamSample

### 13. 流转化为阻塞结构
> ReactorTester.testFluxStreamBlock

### 14. 物化和非物化信号
> ReactorTester.testFluxStreamMaterialize

### 15. 错误处理
> ReactorTester.testErrorHandling

### 16. 背压处理
> ReactorTester.testBackpressureHandling

### 17. 热数据流和冷数据流
> ReactorTester.testHotAndColdStreams

### 18. 处理时间
> ReactorTester.testTimeHandling

### 19. 组合和转化响应式流
> ReactorTester.testStreamCombination

## 以编程方式创建流

### push
> ReactorTester2.testPush

### create
> ReactorTester2.testCreate

### create
> ReactorTester2.testGenerate

### 包装disposable资源
