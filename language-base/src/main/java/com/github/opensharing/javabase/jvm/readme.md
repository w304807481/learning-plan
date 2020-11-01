## 深入理解JVM

#### 1. 理论知识

- 1.1 知识图谱参考：language-base/docs/pdf/深入理解JVM.pdf
    
- 1.2 JVM是什么：language-base/docs/image/JVM是什么.png (TODO：图解)
  > Java是跨平台的语言[JLS.15](https://docs.oracle.com/javase/specs/jls/se15/jls15.pdf)，JVM是跨语言的平台，只要一种语言能最终按照JVMS规范编译成class文件，那么就能运行在JVM上
  >
  > JVM是一种规范[JVMS.15](https://docs.oracle.com/javase/specs/jvms/se15/jvms15.pdf) 任何厂商只要按照标准实现，即可拥有属于自己的JVM平台
    
- 1.3 [深入理解类加载全过程](./classloading/readme.md)

- 1.4 [深入理解Class文件格式](./classfileformat/readme.md)
   
#### 2. 实践

- 2.1 [利用JDK Tools观察内存情况](./jmm/readme.md)
    
