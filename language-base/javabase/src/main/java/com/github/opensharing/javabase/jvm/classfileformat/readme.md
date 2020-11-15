## 深入理解 Class 文件

#### 1. 理论知识

- 1.1 Class文件本质是二进制字节流

- 1.2 包含数据类型: u1,u2,u4,u8 和 xx_info（表类型）
  -  xx_info的来源是hotsport源码中的写法

- 1.3 ClassFile构成
  ```
    ClassFile {
       u4 magic;
       u2 minor_version;
       u2 major_version;
       u2 constant_pool_count;
       cp_info constant_pool[constant_pool_count - 1]; //保留 0号位置用于扩展
       u2 access_flags;
       u1 this_calss;
       u2 super_class;
       u2 interfaces_count;
       interfaces_info interfaces[interfaces_count];
       u2 fields_count;
       fields_info fields[fields_count];
       u2 methods_count;
       methods_info methods[methods_count];
       u2 attributes_count;
       attributes_info attributes[methods_count]; // 实际就是存储每行代码      
    }
  ```
  
- 1.4 查看16进制格式的ClassFile
  - UltraEdit / notepad++ (文本编辑器)
  - IDEA插件 -BindEd

- 1.5 有很多可以观察ByteCode的方法：
  - javap （jdk自己命令）
  - JBE 可以直接修改二进制码
  - JClasslib IDEA插件之一 
    
#### 2. 案例实践
  - 2.1 通过安装IDEA插件：BindEd查看
    > 找到class文件选中，如下图操作
    > 参见：language-base/docs/image/查看Class文件内容-BinEd.png

  - 2.2 通过安装IDEA插件：JClasslib查看
    > 找到Java文件，光标放在类文件中，如下图操作
    > 参见：language-base/docs/image/查看Class文件内容-jclasslib.png
  
  - 2.3 可以通过给Hello.java 不断增加属性，方法，观察class文件的内容变化