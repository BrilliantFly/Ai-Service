自定义starter
===============

当前最新版本： 0.0.1

## generator-know-boot-starter

- 数据库文档生成器（screw）

```
 # 1.添加maven依赖
  <dependency>
     <groupId>com.know</groupId>
     <artifactId>generator-know-boot-starter</artifactId>
     <version>0.0.1</version>
  </dependency>

 # 2.添加配置
  know:
    generator:
      driver-class-name: 1
      jdbc-url: 1
      user-name: 1
      password: 1
      ignore-table: 1
      ignore-table-prefix: 1
      ignore-table-suffix: 1
      save-path: 1
      version: 1
      description: 1

  # 3.引入bean
   @Autowired
   private SqlDocAutoGenerate generate;

  # 4、生成
   generate.autoGenerate();

```

- 代码生成器

## utils-know-boot-starter

- 雪花算法（SnowFlake）
- 雪花漂移（yitter-idgenerator）

    1、雪花漂移
    yitter-idgenerator 是基于雪花算法进行改进的分布式ID生成算法，解决了返回给前端的id过长问题 + 时钟回拨问题
    集成时需要为每个服务设置唯一的机器号，才能保证生成的Id不会重复(集群情况下设置每个服务对应的机器号唯一，单体不需考虑)

    2、CommandLineRunner
    需要在容器启动的时候执行一些内容，比如：读取配置文件信息，数据库连接，删除临时文件，清除缓存信息，
    在Spring框架下是通过ApplicationListener监听器来实现的。
    在Spring Boot中给我们提供了两个接口来帮助我们实现这样的需求。
    这两个接口就是CommandLineRunner和ApplicationRunner，他们的执行时机为容器启动完成的时候。
    
```    
  // 初始化以后，即可在任何需要生成ID的地方，调用以下方法：
   long newId = YitIdHelper.nextId();
```