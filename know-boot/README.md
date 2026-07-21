know-boot
===============

当前最新版本： 0.0.1

##架构设想

```
know-boot
├── accumulate      -- 积累模块
├── base            -- 基础模块
    └── config          -- 基础配置
    └── annotation      -- 自定义注解
    └── utils           -- 公共工具类
├── common          -- 公共模块
    └── constant        -- 公共常量
    └── feign           -- 公共远程调用
    └── dto             -- 各个业务dto，根据业务建立子文件夹（feign通用）
    └── vo              -- 各个业务vo，根据业务建立子文件夹（feign通用）
    └── enum            -- 公共枚举
├── starter         -- 自定义starter模块，封装中间件
├── gateway         -- 网关模块
    └── security    -- 鉴权模块
├── api             -- 公共api模块（与业务耦合度不高，通用的服务）
└── biz             -- 业务模块
```

##目录结构

```
know-boot
├── accumulate      -- 学习积累模块
├── base            -- 基础模块
├── api             -- 通用Api模块
├── start           -- 自定义starter模块
├── plan            -- 计划模块
└── system          -- 系统模块
```

```
system
├── algorithm       -- 算法（冒泡排序、桶排序、堆排序、插入排序、归并排序、快速排序、基数排序、选择排序、希尔排序、朴素算法、字符串相似度比较、KEP算法、Rabin-Karp算法）
├── es              -- es（ElasticsearchRestTemplate、EsService、EsInfoRepository）
├── applet          -- 小程序
├── rabbitmq        -- rabbitmq
├── async           -- 异步（CompletableFuture）
├── cache           -- 缓存（ehcache、jetcache、rediscache）
├── jwt             -- jwt（Jwt、JJwt）
├── redis           -- redis、redission
├── bigdecimal      -- bigdecimal
├── tree            -- tree
├── job             -- job
├── string          -- string（获取字符串某个字符的位置（多个重复字符）、格式化）
├── stream          -- stream（String... param、lambda、listToMap、mapToList、排序、用逗号隔开的字符串、一个对象转换成另一个对象、两个list的交集、差集、List 转 List<Map<String,Object>>）
├── optional        -- optional（创建 Optional 对象、判断值是否存在、非空表达式、设置（获取）默认值、过滤值、转换值）
├── collections     -- collections（空集合、一个元素的集合、不可变集合、线程安全集合、批量增加、填充、交换位置、排序）
├── thread          -- thread
└── other           -- other（AtomicReference、CommonUtisInfo、config、http、hutool、IoInfo、对象反序列化、魔法值、接口占用时间计算、令牌桶、自动生成数据字典文档、mapStruct、upload）
```

##技术栈

- spring

```
spring
├── aop              -- 自定义注解
├── config           -- 配置文件读取
├── ehcache          -- 缓存
├── Jwt              -- JSON Web Token
├── Mybatis-Plus     -- MyBatis的增强工具
├── Scheduled        -- 定时任务
├── ThreadPool       -- 线程池
├── Jta+Atomikos     -- 分布式事务
└── ...              -- ...
```

- 中间件

```
中间件
├── Easypoi          -- poi处理
├── Elasticsearch    -- 分布式全文检索引擎
├── Flyway           -- 数据库版本管理
├── Rabbitmq         -- 消息队列
├── Redis            -- 非关系型数据库
├── Redission        -- 分布式锁
├── Security         -- 认证授权
├── Swagger          -- 接口文档
├── TokenBucket      -- 令牌桶
├── WebSocket        -- 网络通信
└── ...              -- ...
```

- 微服务

```
微服务
├── nacos            -- 注册中心、分布式配置管理
├── gateway          -- 网关
├── Feign            -- 声明式WebService客户端
├── Ribbon           -- 负载均衡
├── Seata            -- 分布式事务
├── Hystrix          -- 断路器
├── Sentinel         -- 分布式系统的流量防卫兵
├── jetcache         -- 分布式缓存
├── xxx-job          -- 分布式任务调度平台
├── ELK              -- 日志系统
├── GPE              -- 监控系统
├── Skywalking       -- 链路追踪
├── Zipin            -- 链路追踪
├── Pinpoint         -- 链路追踪
└── ...              -- ...
```

- 自动化部署

```

k8s+Jenkins+GitLab

```




##服务启动

- nacos

```

1、centos
    sh startup.sh -m standalone

```

- seata

```
1、centos
    ./seata-server.sh -h 39.98.108.121 -p 10007

```

- sentinel

```

1、centos
    nohup java -Dserver.port=10008 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.6.jar >/dev/null 2>&1 &
2、windows
    java -Dserver.port=10008 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.6.jar
3、docker
    docker run -d -p 10008:8080 --name sentinel sentinel

```

- es

```

1、docker
    docker run --name elasticsearch -p 10009:9200  -p 10010:9300 -e "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms84m -Xmx512m" -v /opt/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /opt/elasticsearch/data:/usr/share/elasticsearch/data -v /opt/elasticsearch/plugins:/usr/share/elasticsearch/plugins -d elasticsearch:7.6.2

```

- kibana

```

1、docker
    docker run -d --network es-net --privileged=true \
    --name kibana -p 10011:5601 \
    -v /opt/kibana/config/kibana.yml:/usr/share/kibana/config/kibana.yml \
    -v /opt/kibana/data:/usr/share/kibana/data \
    -v /opt/kibana/plugins:/usr/share/kibana/plugins \
    kibana:7.6.2    

```

- logstash

```

1、docker
    docker run -it -d \
         -p 10012:10012 \
         -p 10013:5044 \
         --name logstash \
         -e TZ=Asia/Shanghai \
         -v /opt/logstash/config/logstash.yml:/usr/share/logstash/config/logstash.yml \
         -v /opt/logstash/conf.d/:/usr/share/logstash/config/conf.d \
         -v /opt/logstash/logs:/usr/share/logstash/logs  \
         -v /opt/logstash/conf.d/template/:/usr/share/logstash/config/template/ \
         --restart=always  \
         -m 1024m \
         logstash:7.6.2  

```

- 启动

```

# admin

http://192.168.1.103:5173/login?redirect=/
admin/123456

# front
http://192.168.45.1:3000/pc/

# 微信小程序
首先npm install
然后hbuilder运行
 

```

## 常见问题及解决

- base

```

1、Object转成JSONObject
    JSONObject json = JSONObject.parseObject(o.toString()); 报错，强制转换也报错，把object转成JSONObject 这种要怎么用的?
    JSONObject json = (JSONObject) JSON.toJSON(o);

    然后通过JSONObject.toJavaObject(json,UploadFileVo.class)转对象

2、	SrpingBoot文件上传报错 the request was rejected because its size (16030696) exceeds the configured maximum (10485760)
  	错误原因：
  		上传文件的大小超过了应用默认文件大小的限制。springboot 默认 multipart.max-file-size大小是1M，max-request-size默认大小是10M

  	解决方法
  		如果是springboot1.x，使用如下配置：
  		spring.http.multipart.max-file-size=100MB
  		spring.http.multipart.max-request-size=100MB

  		如果是springboot2.x, 使用如下配置：
  		spring.servlet.multipart.max-file-size=100MB
  		spring.servlet.multipart.max-request-size=100MB

3、请求：
  		Resolved [org.springframework.web.HttpMediaTypeNotSupportedException:
  		Content type 'multipart/form-data;boundary=651351952406143342340005;charset=UTF-8' not supported]
  	解决方法：
  		请求不对，去掉@RequestBody或者换前端请求类型

4、获取文件Md5（MultipartFile）
     /**
         * 获取上传文件的md5
         * @param file
         * @return
         */
        public static String getMd5(MultipartFile file) {
            try {
                //获取文件的byte信息
                byte[] uploadBytes = file.getBytes();
                // 拿到一个MD5转换器
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] digest = md5.digest(uploadBytes);
                //转换为16进制
                return new BigInteger(1, digest).toString(16);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return null;
        }

        /**
         * 获取上传文件的md5
         * @param file
         * @return
         */
        public static String getMd5Info(MultipartFile file) {
            try {
                byte[] uploadBytes = file.getBytes();
                //file->byte[],生成md5
                String md5Hex = DigestUtils.md5Hex(uploadBytes);
                //file->InputStream,生成md5
                String md5Hex1 = DigestUtils.md5Hex(file.getInputStream());
                //对字符串生成md5
                String s = DigestUtils.md5Hex("字符串");
                return md5Hex ;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return null;
        }

5、HttpServletRequest转MultipartHttpServletRequest
     MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request;

     // 获取上传文件对象
     MultipartFile mf = mulReq.getFile("file");
     // 获取文件名
     String orgName = mf.getOriginalFilename();

6、InputStream转MultipartFile
    MultipartFile multipartFile = new MockMultipartFile(
    					"File", attaName,
    					"text/plain",
    					responseGet.body().asInputStream());

7、InputStream转MultipartFile
     //调用放
        MultipartFile multipartFile = getMultipartFile(inputStream, originalFilename);

     /**
         * 获取封装得MultipartFile
         *
         * @param inputStream inputStream
         * @param fileName    fileName
         * @return MultipartFile
         */
        public MultipartFile getMultipartFile(InputStream inputStream, String fileName) {
            FileItem fileItem = createFileItem(inputStream, fileName);
            //CommonsMultipartFile是feign对multipartFile的封装，但是要FileItem类对象
            return new CommonsMultipartFile(fileItem);
        }


        /**
         * FileItem类对象创建
         *
         * @param inputStream inputStream
         * @param fileName    fileName
         * @return FileItem
         */
        public FileItem createFileItem(InputStream inputStream, String fileName) {
            FileItemFactory factory = new DiskFileItemFactory(16, null);
            String textFieldName = "file";
            FileItem item = factory.createItem(textFieldName, MediaType.MULTIPART_FORM_DATA_VALUE, true, fileName);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            OutputStream os = null;
            //使用输出流输出输入流的字节
            try {
                os = item.getOutputStream();
                while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                inputStream.close();
            } catch (IOException e) {
                log.error("Stream copy exception", e);
                throw new IllegalArgumentException("文件上传失败");
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        log.error("Stream close exception", e);

                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error("Stream close exception", e);
                    }
                }
            }

            return item;
        }

8、下载本地文件
      // 下载本地文件
                String fileName = "286d0db2-3fc1-4897-a602-987b88e1d859.jpeg".toString(); // 文件的默认保存名
                // 读到流中
                InputStream inStream = new FileInputStream("D:/xypj/attach/20220620/286d0db2-3fc1-4897-a602-987b88e1d859.jpeg");// 文件的存放路径
                // 设置输出的格式
                response.reset();
                response.setContentType("bin");
                response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                // 循环取出流中的数据
                byte[] b = new byte[100];
                int len;
                try {
                    while ((len = inStream.read(b)) > 0)
                        response.getOutputStream().write(b, 0, len);
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

9、maven执行clean报错；Non-resolvable parent POM ['parent.relativePath],由于pom.xm中<relativePath/> 导致
    

```

- aop

```
1、java自定义注解的使用范围
  一般我们可以通过注解来实现一些重复的逻辑，就像封装了的一个方法，可以用在一些权限校验、字段校验、字段属性注入、保存日志、缓存
2、自定义注解@Around必须有返回值，不能返回null，不然controller接口调用不通（调用不到）
  @Around(value = "token_point_cut()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("自定义注解参数 -> {}","doAroundAdvice");
        Object obj = proceedingJoinPoint.proceed();
       return obj;
    }

3、自定义校验，必须使用下面maven，否则无效
    <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-validation</artifactId>
     </dependency>

4、范围
   权限校验注解（校验token）
   角色校验注解（springsecurity中的角色校验）
   字段属性注入注解
   对象的属性校验注解
   接口的操作日志注解
   缓存注解
   防刷新注解
   动态切换数据源注解
        原理：Spring提供了AbstractRoutingDataSource用于动态路由数据源，继承AbstractRoutingDataSource类并覆写其protected abstract Object determineCurrentLookupKey()即可

5、防止重复提交
   进入方法，判断锁是否存，存在即重复，不存在，加锁继续执行方法，方法执行结束后释放锁。

```

- config

```

1、同文件读取：application.properties
    know.boot.info=config
    know.boot.document=application
    know.boot.all=${know.boot.info}-${know.boot.document}

2、使用Environment类获取配置文件,可以读取任何文件
  @Autowired
  Environment env;

   // 获取author.properties
   log.info("author -> {}",env.getProperty("author.name"));
   //获取application.properties
   log.info("application -> {}",env.getProperty("know.boot.all"));

   //获取application.yml配置
   log.info("端口 - server.port ->{}",env.getProperty("server.port"));

```

- easypoi

```

Hutool是一个小而全的Java工具类库
    图片工具：
        https://www.bookstack.cn/read/hutool/7ea903bc069ed4e0.md

```

- ehcache

```

1、springboot 整合ehcache，注入CacheManager时提示 required a bean of type 'org.springframework.cache.CacheManager' that could not be found
    <dependency>
         <groupId>net.sf.ehcache</groupId>
         <artifactId>ehcache</artifactId>
         <version>2.10.6</version>
    </dependency>
    还需依赖：
    <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-cache</artifactId>
    </dependency>

```

- es

```

参考：
    https://www.cnblogs.com/xxxh/p/15800887.html
    https://www.cnblogs.com/fantongxue/p/13471049.html

1、注解
    几个用到的注解：

    @Document：声明索引库配置
        indexName：索引库名称
        type：映射类型。如果未设置，则使用小写的类的简单名称。（从版本4.0开始不推荐使用）
        shards：分片数量，默认5
        replicas：副本数量，默认1
    @Id：声明实体类的id
    @Field：声明字段属性
        type：字段的数据类型
        analyzer：指定分词器类型
        index：是否创建索引

    关系数据库（mysql） ⇒ 数据库 ⇒ 表 ⇒ 行 ⇒ 列(Columns)
    Elasticsearch      ⇒ 索引 ⇒ 类型 ⇒ 文档 ⇒ 字段(Fields)

2、查询
    精确查询（必须完全匹配上）
    思考： 有些精确匹配，需要加上 .keyword
    单个匹配termQuery

    //不分词查询 参数1： 字段名，参数2：字段查询值，因为不分词，所以汉字只能查询一个字，英语是一个单词.
    QueryBuilder queryBuilder=QueryBuilders.termQuery("fieldName", "fieldlValue");
    //分词查询，采用默认的分词器
    QueryBuilder queryBuilder2 = QueryBuilders.matchQuery("fieldName", "fieldlValue");

    多个匹配
    //不分词查询，参数1： 字段名，参数2：多个字段查询值,因为不分词，所以汉字只能查询一个字，英语是一个单词.
    QueryBuilder queryBuilder=QueryBuilders.termsQuery("fieldName", "fieldlValue1","fieldlValue2...");
    //分词查询，采用默认的分词器
    QueryBuilder queryBuilder= QueryBuilders.multiMatchQuery("fieldlValue", "fieldName1", "fieldName2", "fieldName3");
    //匹配所有文件，相当于就没有设置查询条件
    QueryBuilder queryBuilder=QueryBuilders.matchAllQuery();


   模糊查询（只要包含即可）
   //模糊查询常见的5个方法如下
   //1.常用的字符串查询
   QueryBuilders.queryStringQuery("fieldValue").field("fieldName");//左右模糊
   //2.常用的用于推荐相似内容的查询
   QueryBuilders.moreLikeThisQuery(new String[] {"fieldName"}).addLikeText("pipeidhua");//如果不指定filedName，则默认全部，常用在相似内容的推荐上
   //3.前缀查询  如果字段没分词，就匹配整个字段前缀
   QueryBuilders.prefixQuery("fieldName","fieldValue");
   //4.fuzzy query:分词模糊查询，通过增加fuzziness模糊属性来查询,如能够匹配hotelName为tel前或后加一个字母的文档，fuzziness 的含义是检索的term 前后增加或减少n个单词的匹配查询
   QueryBuilders.fuzzyQuery("hotelName", "tel").fuzziness(Fuzziness.ONE);
   //5.wildcard query:通配符查询，支持* 任意字符串；？任意一个字符
   QueryBuilders.wildcardQuery("fieldName","ctr*");//前面是fieldname，后面是带匹配字符的字符串
   QueryBuilders.wildcardQuery("fieldName","c?r?");

   范围查询
   //闭区间查询
   QueryBuilder queryBuilder0 = QueryBuilders.rangeQuery("fieldName").from("fieldValue1").to("fieldValue2");
   //开区间查询
   QueryBuilder queryBuilder1 = QueryBuilders.rangeQuery("fieldName").from("fieldValue1").to("fieldValue2").includeUpper(false).includeLower(false);//默认是true，也就是包含
   //大于
   QueryBuilder queryBuilder2 = QueryBuilders.rangeQuery("fieldName").gt("fieldValue");
   //大于等于
   QueryBuilder queryBuilder3 = QueryBuilders.rangeQuery("fieldName").gte("fieldValue");
   //小于
   QueryBuilder queryBuilder4 = QueryBuilders.rangeQuery("fieldName").lt("fieldValue");
   //小于等于
   QueryBuilder queryBuilder5 = QueryBuilders.rangeQuery("fieldName").lte("fieldValue");

   组合查询/多条件查询/布尔查询
   QueryBuilders.boolQuery()
   QueryBuilders.boolQuery().must();//文档必须完全匹配条件，相当于and
   QueryBuilders.boolQuery().mustNot();//文档必须不匹配条件，相当于not
   QueryBuilders.boolQuery().should();//至少满足一个条件，这个文档就符合should，相当于or

3、测试地址
   http://localhost:9001/system/es/indexInfo
   http://localhost:9001/system/es/createDocument
   http://localhost:9001/system/es/updateDocument
   http://localhost:9001/system/es/updateDocumentNoRealTime
   http://localhost:9001/system/es/deleteDataById
   http://localhost:9001/system/es/isExists
   http://localhost:9001/system/es/findDocumentById

4、Document mapping type name can't start with '_', found: [_update]"
    es7 默认"_type": "_doc"
    这个可能是客户端和springboot引用不一致，springboot是7，es环境也必须是7


```

- flyway

```

Flyway是一款开源的数据库版本管理工具，它更倾向于规约优于配置的方式。
用通俗的话来讲，Flyway 可以实现开发者 Git 管理不同人的代码那样，管理不同人的SQL脚本，从而做到数据库同步。

1、设置脚本
    首先需要在classpath目录下，创建一个名为db.migration的文件夹。
    然后向该文件夹内，增加SQL脚本。编写SQL脚本需要注意以下几点：
    1、仅需要执行一次的脚本，以V开头，后面跟上0~9的数字组合，数字之间可以使用.或者_进行分割。然后再以两个下划线 __进行分割，其后跟上文件名称，最后以.sql结尾。
        如： V1__create_user_ddl.sql、V2__create_user.sql。
    2、需要重复执行的SQL，则需要以R开头。后面再以两个下划线分割，其后跟文件名称，最后以.sql结尾。
        如：R__truncate_user_dml.sql
        这里的 R 重复执行脚本，并不是说是启动项目后不断执行！而是，每次启动项目，都会重新校验对应的 R__add_user_info.sql 内容是否变更，如果变更则重新执行。

2、启动项目生成的表
    flyway_schema_history是一个flyway管理各个版本关系的表，主要用于维护和管理开发者本地的脚本版本信息。

3、one of the following migration scripts locations could be found
    没有找到脚本文件，可能原始是脚本命名不规范问题，应和配置文件规定一定

```

- jwt

```

1、Jwt添加过滤器，两种方式实现
2、Jwt过滤器校验
3、完整JWT包括三部分：Header - 头部 、Payload - 负载 、Signature（签名）
   Header.Payload.Signature

   Header:
    JWT的Header中存储了所使用的加密算法和Token类型
    {
       "alg" : "HS256",
       "typ" : "JWT"
    }

   Payload:
    Payload表示负载，也是一个JSON对象，JWT规定了7个官方字段供选用
    iss (issuer) : 签发人
    exp (expiration time) : 过期时间
    sub (subject) : 主题
    aud (audience) : 受众
    nbf (Not Before) : 生效时间
    iat (Issued At) : 签发时间
    jti (JWT ID) : 编号

   Signature:
    Signature部分是对前两部分的签名，防止数据篡改。
    首先，需要指定一一个密钥(secret) 。 这个密钥只有服务器才知道，不能泄露给用户。然后，使用Header里面指定的签名算法(默认是HMAC SHA256)，按照下面的公式产生签名
    HMAXSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload),secret)


```

- mybatisplus

```

1、JsonFormat与DateTimeFormat区别
2、Mybaits
   注解与xml可以同时存在，xml有限与注解

3、校验框架（javax.validation.Valid）
    如果传参是list
    加@Validated注解
    @Slf4j
    @RestController
    @Api(tags = "框架协议信息公共接口")
    @Validated
    @RequestMapping("/api/frameContract")
    public class FrameContractApiController {

        @Autowired
        private BaseApiService baseApiService;

        @ApiOperation(value = "初始化框架协议信息")
        @PostMapping(value = "/initFrameContract")
        public ResultMessage<Object> initFrameContract(@RequestBody @Valid FrameContractApiDTO contractApiDTO) {

            return baseApiService.initFrameContract(contractApiDTO);

        }

        @ApiOperation(value = "框架协议退出")
        @PostMapping(value = "/frameContractSignout")
        public ResultMessage<Object> frameContractSignout(@RequestBody @Valid List<FrameContractSignoutApiDTO> signoutApiDTOS) {

            return baseApiService.frameContractSignout(signoutApiDTOS);

        }

    }

    如果不是list，可以不加@Validated注解

```

- rabbitmq

```

参考：
    https://blog.csdn.net/qq_38630810/article/details/107335385

    https://www.5axxw.com/wiki/content/vn3nkg

    https://www.aquestian.cn/article/30

    https://wangsong.blog.csdn.net/article/details/121356523?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-2.pc_relevant_aa&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-2.pc_relevant_aa&utm_relevant_index=4

    RocketMQ:
    https://www.cnblogs.com/weifeng1463/p/12889300.html

1、org.springframework.amqp.rabbit.listener.BlockingQueueConsumer$DeclarationException: Failed to declare queue(s)
    在程序启动后，如果该队列不存在

2、rabbitTemplate

    发送消息
    send （自定义消息 Message）
    Message message = new Message("hello".getBytes(),new MessageProperties());
    // 发送消息到默认的交换器，默认的路由键
    rabbitTemplate.send(message);
    // 发送消息到指定的交换器，指定的路由键
    rabbitTemplate.send("direct.exchange","key.1",message);
    // 发送消息到指定的交换器，指定的路由键
    rabbitTemplate.send("direct.exchange","key.1",message,new CorrelationData(UUID.randomUUID().toString()));
    convertAndSend（自动 Java 对象包装成 Message 对象，Java 对象需要实现 Serializable 序列化接口）
    User user = new User("linyuan");
    // 发送消息到默认的交换器，默认的路由键
    rabbitTemplate.convertAndSend(user);
    // 发送消息到指定的交换器，指定的路由键，设置消息 ID
    rabbitTemplate.convertAndSend("direct.exchange","key.1",user,new CorrelationData(UUID.randomUUID().toString()));
    // 发送消息到指定的交换器，指定的路由键，在消息转换完成后，通过 MessagePostProcessor 来添加属性
    rabbitTemplate.convertAndSend("direct.exchange","key.1",user,mes -> {
        mes.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
            return mes;
    });

    接收消息
    receive（返回 Message 对象）
    // 接收来自指定队列的消息，并设置超时时间
    Message msg = rabbitTemplate.receive("debug",2000l);

    receiveAndConvert（将返回 Message 转换成 Java 对象）
    User user = (User) rabbitTemplate.receiveAndConvert();


```

- redis

```

1、Factory method 'redisConnectionFactory' threw exception
   java.lang.ClassNotFoundException: org.apache.commons.pool2.impl.GenericObjectPoolConfig
   添加：maven
   <dependency>
               <groupId>org.apache.commons</groupId>
               <artifactId>commons-pool2</artifactId>
   </dependency>

2、required a bean of type 'org.springframework.data.redis.core.RedisTemplate' that could not be found
   解决RedisTemplate找不到
   注解使用Resource

3、解决RedisTemplate找不到
   两种方式
    @Resource
   private RedisTemplate<Object, Object> redisTemplate;

   @Autowired
    private RedisTemplate redisTemplate;

4、Redis使用基于注解的缓存
    @CacheConfig   提供了一种在类级别共享公共缓存相关设置的机制
        cacheNames	        使用在类上的默认缓存名称
        keyGenerator	    用于类的默认KeyGenerator的bean名称
        cacheManager	    自定义CacheManager的bean名称，如果尚未设置，则可以用于创建默认CacheResolver
        cacheResolver	    要使用的自定义CacheResolver的bean名称
    @Cacheable  可以标记在一个方法上，也可以标记在类上，当标记在类上时，当前类的所有方法都支持缓存，当注解的方法被调用时，如果缓存中有值，则直接返回缓存中的数据
        cacheNames / value	缓存的空间名称，这两个配置只能二选一
        key / keyGenerator	缓存的key，同一个空间名称value下的key唯一，可以通过SpEL 表达式编写指定这个key的值，或者通过keyGenerator生成，这两个配置只能二选一
        cacheManager	    自定义CacheManager的bean名称，如果尚未设置，则可以用于创建默认CacheResolver
        cacheResolver	    要使用的自定义CacheResolver的bean名称
        condition	        缓存的条件，默认为true，使用 SpEL 编写，返回true或者false，只有为true才进行缓存。
                            为true时：如果缓存有值，则不执行方法；如果缓存没值，则执行方法并将结果保存到缓存。为false时：不执行缓存，每次都执行方法。
        unless	            函数返回值符合条件的不缓存、只缓存其余不符合条件的。可以使用 SpEL 编写，方法参数可以通过索引访问，例如：第二个参数可以通过#root.args[1]、#p1或#a1访问
        sync	            是否使用异步模式。默认是方法执行完，以同步的方式将方法返回的结果存在缓存中
    @CachePut   可以标记在一个方法上，也可以标记在类上，当标记在类上时，当前类的所有方法都支持缓存，当注解的方法被调用时，如果缓存中有值，则直接返回缓存中的数据
        cacheNames / value	缓存的空间名称，这两个配置只能二选一
        key / keyGenerator	缓存的key，同一个空间名称value下的key唯一，可以通过SpEL 表达式编写指定这个key的值，或者通过keyGenerator生成，这两个配置只能二选一
        cacheManager	    自定义CacheManager的bean名称，如果尚未设置，则可以用于创建默认CacheResolver
        cacheResolver	    要使用的自定义CacheResolver的bean名称
        condition	        缓存的条件，默认为true，使用 SpEL 编写，返回true或者false，只有为true才进行缓存。
                            为true时：如果缓存有值，则不执行方法；如果缓存没值，则执行方法并将结果保存到缓存。为false时：不执行缓存，每次都执行方法。
        unless	            函数返回值符合条件的不缓存、只缓存其余不符合条件的。可以使用 SpEL 编写，方法参数可以通过索引访问，例如：第二个参数可以通过#root.args[1]、#p1或#a1访问
    @CacheEvict    可以标记在一个方法上，也可以标记在类上，用来清除缓存元素的。当标记在一个类上时表示其中所有的方法的执行都会触发缓存的清除操作
        cacheNames / value	缓存的空间名称，这两个配置只能二选一
        key / keyGenerator	缓存的key，同一个空间名称value下的key唯一，可以通过SpEL 表达式编写指定这个key的值，或者通过keyGenerator生成，这两个配置只能二选一
        cacheManager	    自定义CacheManager的bean名称，如果尚未设置，则可以用于创建默认CacheResolver
        cacheResolver	    要使用的自定义CacheResolver的bean名称
        condition	        缓存的条件，默认为true，使用 SpEL 编写，返回true或者false，只有为true才进行缓存。
                            为true时：如果缓存有值，则不执行方法；如果缓存没值，则执行方法并将结果保存到缓存。为false时：不执行缓存，每次都执行方法。
        allEntries	        为true时表示清除（cacheNames或value）空间名里的所有的数据
        beforeInvocation	为false时，缓存的清除是否再方法之前执行,默认代表缓存清除操作是在方法执行后执行，如果出现异常缓存就不会清除;为true时，代表清除缓存操作是在方法运行之前执行，无论方法是否出现异常，缓存都清除
    @Caching    多个缓存注解（不同或相同类型）的组注解
        @Cacheable	        同上面的@Cacheable
        @CachePut	        同上面的@CachePut
        @CacheEvict	        同上面的@CacheEvict
    spEL 编写 key
        method	        root object	                当前被调用的方法	                                                                        #root.method.name
        methodName	    root object	                当前被调用的方法名	                                                                    #root.methodName
        target	        root object	                当前被调用的目标对象	                                                                    #root.target
        targetClass	    root object	                当前被调用的目标对象类	                                                                #root.targetClass
        caches	        root object	                当前被调用的缓存列表(比如@Cacheable(value={“key1”,“key2”})，有两个cache)	                #root.caches[0].name
        arg	            root object	                当前被调用的方法参数列表	                                                                #root.args[0]
        argument name	evaluation context	        方法参数的名字，可以使用 #参数名，也可以使用#a0 或者 #p0的形式，这里的0代表索引，从0开始	    #a0 
        result	        evaluation context	        方法执行后的返回值，执行后的判断有效	                                                    #result

```

- redission

```

1、限流框架
    1、spring cloud gateway集成redis限流,但属于网关层限流
    2、阿里Sentinel,功能强大、带监控平台
    3、srping cloud hystrix，属于接口层限流，提供线程池与信号量两种方式
    4、其他：redission

2、redis连接错误：ERR Client sent AUTH, but no password is set解决方案２个
    使用jedis或redisson连接redis时，如果redis没有密码，但在配置文件中写为
    spring:
    	redis:
    	    database: 0
    	    host: 127.0.0.1
    	    password:
    	    port: 6379
    	    timeout: 10000
    通常会报错：　ERR Client sent AUTH, but no password is set
    原因分析：把上面的文字翻译其实就知道了，客户端设置了auth认证，但没设置密码。
    解决方案－：
    　　在redis配置文件中redis.conf加入：
        requirePass: 你的密码
    解决方案二：
    　　把上面的配置中password一行去掉，既然没密码，就不要写。
    spring:
    	redis:
    	    database: 0
    	    host: 127.0.0.1
    	    port: 6379
    	    timeout: 10000

3、Redission令牌桶

```

- scheduled

```

1、详解
    corn表达式在linux使用广泛，具体可以参考cron表达式详解以及在线Cron表达式生成器
    initialDelay：启动后多久开始执行，单位时毫秒
    fixedRate：下次执行时间，任务开始运行的时候就计时。
    fixedDelay：下次执行时间，fixedDelay等任务进行完了才开始计时

2、如果定时任务不执行，在application中开启定时任务

```

- security

```

1、Spring Boot configuration annotation processor not found in classpath
      <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-configuration-processor</artifactId>
           <optional>true</optional>
      </dependency>

2、相关配置
     http.headers().frameOptions().disable();//开启运行iframe嵌套页面

            http//1、配置权限认证
                    .authorizeRequests()
                    //配置不拦截路由
                    .antMatchers("/500").permitAll()
                    .antMatchers("/403").permitAll()
                    .antMatchers("/404").permitAll()
                    .antMatchers("/login").permitAll()
                    .anyRequest() //任何其它请求
                    .authenticated() //都需要身份认证
                    .and()
                    //2、登录配置表单认证方式
                    .formLogin()
                    .loginPage("/login")//自定义登录页面的url
                    .usernameParameter("username")//设置登录账号参数，与表单参数一致
                    .passwordParameter("password")//设置登录密码参数，与表单参数一致
                    // 告诉Spring Security在发送指定路径时处理提交的凭证，默认情况下，将用户重定向回用户来自的页面。登录表单form中action的地址，也就是处理认证请求的路径，
                    // 只要保持表单中action和HttpSecurity里配置的loginProcessingUrl一致就可以了，也不用自己去处理，它不会将请求传递给Spring MVC和您的控制器，所以我们就不需要自己再去写一个/user/login的控制器接口了
                    .loginProcessingUrl("/user/login")//配置默认登录入口
                    .defaultSuccessUrl("/index")//登录成功后默认的跳转页面路径
                    .failureUrl("/login?error=true")
                    .successHandler(loginSuccessHandler)//使用自定义的成功结果处理器
                    .failureHandler(loginFailureHandler)//使用自定义失败的结果处理器
                    .and()
                    //3、注销
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                    .permitAll()
                    .and()
                    //4、session管理
                    .sessionManagement()
                    .invalidSessionUrl("/login") //失效后跳转到登陆页面
                    //单用户登录，如果有一个登录了，同一个用户在其他地方登录将前一个剔除下线
                    //.maximumSessions(1).expiredSessionStrategy(expiredSessionStrategy())
                    //单用户登录，如果有一个登录了，同一个用户在其他地方不能登录
                    //.maximumSessions(1).maxSessionsPreventsLogin(true) ;
                    .and()
                    //5、禁用跨站csrf攻击防御
                    .csrf()
                    .disable();

```

- swagger

```

1、Failed to start bean 'documentationPluginsBootstrapper'; nested exception is java.lang.NullPointerException
   因为Springfox 使用的路径匹配是基于AntPathMatcher的，而Spring Boot 2.6.X使用的是PathPatternMatcher。不兼容

   解决方法：
        1、 修复方案一：降低Spring Boot 版本到2.6.x以下版本
        2、 修复方案二: SpringBoot版本不降级解决方案
            第一步:
            application.yml 或applicaiton.properties 中添加如下配置

            spring:
              mvc:
                pathmatch:
                  matching-strategy: ant_path_matcher

            第二步:
            项目中新建一个Java Config 类，内容如下：
             /**
                * 增加如下配置可解决Spring Boot 6.x 与Swagger 3.0.0 不兼容问题
                **/
                @Bean
                public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
                    List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
                    Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
                    allEndpoints.addAll(webEndpoints);
                    allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
                    allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
                    String basePath = webEndpointProperties.getBasePath();
                    EndpointMapping endpointMapping = new EndpointMapping(basePath);
                    boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
                    return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
                }
                private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
                    return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
                }

2、No validator could be found for constraint ‘javax.validation.constraints.NotBlank‘ validating type
   引用依赖的原因，比如下面的：
          <dependency>
              <groupId>javax.validation</groupId>
              <artifactId>validation-api</artifactId>
              <version>${validation-api.version}</version>
          </dependency>
          <dependency>
              <groupId>org.hibernate</groupId>
              <artifactId>hibernate-validator</artifactId>
              <version>${hibernate-validator.version}</version>
          </dependency>

   换成：
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-validation</artifactId>
           </dependency>

```

- threadpool

```

```

- tokenbucket

```

1、guava：缓存、令牌桶
2、自定义拦截器必须注册，否则不起作用。另外，注册需拦截到请求，不然也不起作用。
3、bucket4j
  <!-- 令牌桶 -->
        <!--        <dependency>-->
        <!--            <groupId>com.github.vladimir-bukhtoyarov</groupId>-->
        <!--            <artifactId>bucket4j-core</artifactId>-->
        <!--            <version>4.0.0</version>-->
        <!--        </dependency>-->

```

- websocket

```

1、tio-websocket-server

        <dependency>
                <groupId>org.t-io</groupId>
                <artifactId>tio-websocket-server</artifactId>
                <version>3.7.0.v20201010-RELEASE</version>
        </dependency>


   启动后测试：
   在线测试地址：http://coolaf.com/tool/chattest
                ws://127.0.0.1:6789/?id=1

```

- 拦截器与过滤器

```

过滤器 (Filter) 和 拦截器 (Interceptor)

```

- nacos

```

1、Error creating bean with name ‘configurationPropertiesBeans‘ defined in class path resource
    原因是boot版本高于了cloud的引入包的版本或pom.xml发现是spring-boot的版本与nacos版本不一致的导致

2、Nacos配置中心,拉取配置报NACOS SocketTimeoutException httpGet] currentServerAddr:http://localhost:8848，error超时
    bootstrap.yml（bootstrap.properties）用来在程序引导时执行，应用于更加早期配置信息读取，如可以使用来配置application.yml中使用到参数等；
    application.yml（application.properties) 应用程序特有配置信息，可以用来配置后续各个模块中需使用的公共参数等。
    bootstrap.yml 先于 application.yml 加载
    处理方案:
    application.yml 更改为bootstrap.yml即可

3、Nacos注册中心
    java.net.ConnectException: [NACOS HTTP-POST] The maximum number of tolerable server reconnection errors has been reached
    配置：
    config:
       enabled: false
    如下：
    spring:
      cloud:
        nacos:
          # 注册中心
          discovery:
            # 指定nacos server的地址
            server-addr: 182.92.116.199:9999
          # 单独启动注册中心，没有配置中心，需要配置下面
          config:
            enabled: false
4、Nacos配置
    server:
      port: 9001
      servlet:
        context-path: /nacos
    
    spring:
      application:
        name: nacos
      cloud:
        nacos:
          #      # 注册中心
          #      discovery:
          #        # 指定nacos server的地址
          #        server-addr: 182.92.116.199:9999
          #      # 单独启动注册中心，没有配置中心，需要配置下面
          #      config:
          #        enabled: false
    
          # 配置中心
          # 1、通过server-addr+namespace找到相应的配置列表
          # 2、对应的配置列表，根据${prefix}-${spring.profile.active}.${file-extension}找到配置文件
          # ${prefix}，默认是spring.application.name,如果nacos配置prefix就取prefix的值
          # ${spring.profile.active}表示profile.active配置，没有则配置文件没有此字段
          # 单独启动配置中心，没有注册中心，需要配置下面
          discovery:
            enabled: false
          config:
            # 配置中心的地址(配置nacos服务地址)
            server-addr: 182.92.116.199:9999
            # 配置命名空间（命名空间ID）
            # 常用场景之一是不同环境的配置的区分隔离，例如开发测试环境和生产环境的资源（如配置、服务）隔离等
            namespace: 23423955-ad45-4b41-8ff2-036d9c01709c
            # 配置文件的环境（分配组）
            group: DEFAULT_GROUP
            # 配置文件后缀，用于拼接配置文件名称
            file-extension: yaml
            # 配置文件prefix前缀
            prefix: ${spring.application.name}
            # 配置自动刷新
            refresh: true
    
            # 多个配置文件，扩展配置参数
            # extension-configs配置是一个数组List类型，每个配置中包含三个参数：data-id、group，refresh。
            # 其中refresh参数用于控制这个配置文件中的内容时候是否支持自动刷新
            # 默认情况下，只有默认加载的配置才会自动刷新，对于这些扩展的配置加载内容需要配置该设置时候才会实现自动刷新。
            extension-configs:
              - dataId: datasource-${spring.profiles.active}.yaml
                group: DEFAULT_GROUP
                #是否刷新
                refresh: true
              - dataId: mybatis-plus-${spring.profiles.active}.yaml
                group: DEFAULT_GROUP
                refresh: true
    
            # 配置共享
            # 读取公用配置优先读取数组靠后的配置文件
            shared-configs:
              - data-id: common.yaml
                group: DEFAULT_GROUP
                refresh: true

```

- gateway

```

1、Parameter 0 of method modifyRequestBodyGatewayFilterFactory in org.springframework.cloud.gateway.config.GatewayAutoConfiguration
   required a bean of type 'org.springframework.http.codec.ServerCodecConfigurer' that could not be found.
   将pom.xml中关于spring-boot-start-web模块的jar依赖去掉。
   出现该错误是因为Spring Cloud Gateway依赖了spring-boot-starter-web包
   因为spring cloud gateway是基于webflux的，如果非要web支持的话需要导入spring-boot-starter-webflux而不是spring-boot-start-web。
   <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-gateway</artifactId>
               <version>2.2.3.RELEASE</version>
               <exclusions>
                   <exclusion>
                       <groupId>org.springframework.boot</groupId>
                       <artifactId>spring-boot-starter-web</artifactId>
                   </exclusion>
               </exclusions>
           </dependency>

   因为这里spring-boot-starter-web是依赖的parent，上面没有效果，所有用下面方式：
     <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
                <scope>test</scope>
            </dependency>

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-gateway</artifactId>
                <version>2.2.3.RELEASE</version>
    <!--            <exclusions>-->
    <!--                <exclusion>-->
    <!--                    <groupId>org.springframework.boot</groupId>-->
    <!--                    <artifactId>spring-boot-starter-web</artifactId>-->
    <!--                </exclusion>-->
    <!--            </exclusions>-->
            </dependency>

4、springcloud gateway出现 413Request Entity Too Large
    springboot项目在yml中添加
    server:
    	max-http-header-size: 2MB

    springcloud项目在gateway里添加
    @Component
    public class NettyConfiguration implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

        @Override
        public void customize(NettyReactiveWebServerFactory container) {
            container.addServerCustomizers(
                    httpServer -> httpServer.httpRequestDecoder(
                            httpRequestDecoderSpec -> httpRequestDecoderSpec.maxHeaderSize(1000485760)
                    )
            );
        }

    }

5、Caused by: org.yaml.snakeyaml.scanner.ScannerException: mapping values are not allowed here
    yml配置文件对其方式不对

6、关于重试
   1、如果远程服务在启动，但是报错，比如mybatis服务，可以在mybatis的日志看到除了第一次错误之外，还有重试的几次
   2、如果远程服务没有启动，gateway直接报错，连接拒绝
   3、如果mybatis只是报错，不走断路（Hystrix处理服务不可用），可以重试

7、sentinel没有请求链路、API管理
 <!--sentinel整合gatway,依赖必须在gateway，这样gateway会出现请求链路、API管理-->
 <dependency>
     <groupId>com.alibaba.cloud</groupId>
     <artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
 </dependency>



```

- feign

```

1、如果Feign没有返回数据，可能是返回值vo没有setget方法

2、com.fasterxml.jackson.core.exc.InputCoercionException: Numeric value (269175333755141) out of range of int (-2147483648 - 2147483647)
   feign定义的vo的字符类型不对

3、记录日志的两种方式
    1、配置文件
    2、FeignClient注解

4、The bean 'mybatis.FeignClientSpecification' could not be registered. A bean with that name has already been defined and overriding is disabled.
   FeignClient注解name有相同的

5、No fallback instance of type class com.know.knowboot.feign.RealFallbackFeign found for feign client mybatis1
   熔断类加@Component

6、Feign开启HyStrix，不管是远程服务未启动还是服务报错，都会走熔断

7、Feign设置超时时间
   如果openFeign没有设置对应得超时时间，那么将会采用Ribbon的默认超时时间。
   1、设置Ribbon的超时时间
       全局
       ribbon:
         # 值的是建立链接所用的时间，适用于网络状况正常的情况下， 两端链接所用的时间
         ReadTimeout: 5000
         # 指的是建立链接后从服务器读取可用资源所用的时间
         ConectTimeout: 5000

       单个服务
       serviceC:
         ribbon:
           ReadTimeout: 30000 #单位毫秒
           ConnectTimeout: 30000 #单位毫秒

   2、设置openFeign的超时时间
       全局
       feign:
         client:
           config:
             ## default 设置的全局超时时间，指定服务名称可以设置单个服务的超时时间
             default:
               connectTimeout: 5000
               readTimeout: 5000

       单个服务
       feign:
         client:
           config:
             ## 为serviceC这个服务单独配置超时时间
             serviceC:
               connectTimeout: 30000
               readTimeout: 30000

8、Feign设置超时时间，但是无效
    1、Hystrix默认超时1s，如果不设置，会影响

9、思考：feign是不是有点类似网关，可以集成很多内容

10、feign调用，参数传不过去，可能请求方式的原因
    设置下consumes
    @PostMapping(value = "/filetransfer/uploadfile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

11、feign注解
    name：指定 Feign Client 的名称，如果项目使用了 Ribbon，name 属性会作为微服务的名称，用于服务发现。
    url：url 一般用于调试，可以手动指定 @FeignClient 调用的地址。
    decode404：当发生404错误时，如果该字段为 true，会调用 decoder 进行解码，否则抛出 FeignException。
    configuration：Feign 配置类，可以自定义 Feign 的 Encoder、Decoder、LogLevel、Contract。
    fallback：定义容错的处理类，当调用远程接口失败或超时时，会调用对应接口的容错逻辑，fallback 指定的类必须实现 @FeignClient 标记的接口。
    fallbackFactory：工厂类，用于生成 fallback 类示例，通过这个属性我们可以实现每个接口通用的容错逻辑，减少重复的代码。
    path：定义当前 FeignClient 的统一前缀。

12、org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata
    springboot与springcloud版本不对应

    引入对应版本：
    <spring-cloud.version>2020.0.5</spring-cloud.version>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

13、Field ypCommonFeign in cn.lili.yunpan.utils.YpCommonUtils required a bean of type 'cn.lili.yunpan.feign.YpCommonFeign' that could not be found.
    feign找不到
    添加@EnableFeignClients注解

14、Feign远程下载

     /**
      * 校验Token是否有效
      * @param token
      * @return
      */
     @GetMapping(value = "/filetransfer/downloadfile")
     Response downloadfile(@RequestHeader("Token") String token, @RequestParam("userFileId") long userFileId);

         //可以使用这个方法
             @GetMapping(value = "/downYpFile")
             @AutoLogin(sysName = "KJXY_SYSTEM")
             public void downYpFile(FileSearchDTO fileSearchDTO, HttpServletResponse response) {

                 // 下载
                 InputStream inputStream = null;
                 try {
                     Response feignResponse = ypCommonService.downloadfile(fileDTO);
                     Response.Body body = feignResponse .body();
                     inputStream = body.asInputStream();
                     BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                     response.setContentType("multipart.form-data");
                     response.setHeader("Content-Disposition", feignResponse.headers().get("Content-Disposition").toString().replace("[","").replace("]",""));
                     BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
                     int length = 0;
                     byte[] temp = new byte[1024 * 10];
                     while ((length = bufferedInputStream.read(temp)) != -1) {
                         bufferedOutputStream.write(temp, 0, length);
                     }
                     bufferedOutputStream.flush();
                     bufferedOutputStream.close();
                     bufferedInputStream.close();
                     inputStream.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 } finally {
                     if(inputStream != null){
                         try {
                             inputStream.close();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     }
                 }

         //        return result;
             }


15、Feing get传递对象

    将对象一个个进行传递即可

    /**
     * 校验Token是否有效
     * @param token
     * @return
     */
    @GetMapping(value = "/filetransfer/downloadfile")
    Response downloadfile(@RequestHeader("Token") String token, @RequestParam("userFileId") long userFileId);

    @Operation(summary = "下载文件", description = "下载文件接口", tags = {"filetransfer"})
    @MyLog(operation = "下载文件", module = CURRENT_MODULE)
    @RequestMapping(value = "/downloadfile", method = RequestMethod.GET)
    public void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, DownloadFileDTO downloadFileDTO) throws IOException {}

16、Feign配置
    server:
      port: 9002
      servlet:
        context-path: /feign
    
    feign:
      client:
        config:
          ## default 设置的全局超时时间，指定服务名称可以设置单个服务的超时时间
          default:
            connectTimeout: 1000
            readTimeout: 1000
      httpclient:
        # 开启 Http Client
        enabled: true
    #  # 开始OkHttp
    #  okhttp:
    #    enabled: true
    #  # 开始hystrix
      hystrix:
        enabled: true
      # 开启压缩
      compression:
        request:
          enabled: true
          ## 开启压缩的阈值，单位字节，默认2048，即是2k，这里为了演示效果设置成10字节
          min-request-size: 10
          mime-types: text/xml,application/xml,application/json
        response:
          enabled: true
    
    # feign日志
    logging:
      level:
        # openFeign接口所在的包名
        com.know.knowboot.feign: debug
    
    hystrix:
      command:
        default:
          execution:
            isolation:
              thread:
                timeoutInMilliseconds: 17000
    
    # Ribbon 配置
    # 全局配置，也可以添加名称前缀，指定客户端
    # MaxAutoRetries+（MaxAutoRetries+1）*（MaxAutoRetriesNextServer）
    # 第一次请求时异常，重试2次，查询下一个节点，发送一次请求失败，进入重试，再重试2次失败，继续查询三次节点重试，所以以上配置会重试（2+3*3=11）次
    #ribbon:
    #  # 单节点最大重试次数，达到最大值时，切换到下一个示例
    #  MaxAutoRetries: 2
    #  # 更换下一个重试节点的最大次数，可以设置为服务提供者副本数，也是就每个副本都查询一次
    #  MaxAutoRetriesNextServer: 3
    #  # 是否对所有请求进行重试，默认fasle，则只会对GET请求进行重试，建议配置为false，不然添加数据接口，会造成多条重复，也就是幂等性问题
    #  OkToRetryOnAllOperations: false
    
    #服务名称
    #provider:
    #  ribbon:
    #    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
    #NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule #配置规则 随机
    #NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RoundRobinRule #配置规则 轮询
    #NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RetryRule #配置规则 重试
    #NFLoadBalancerRuleClassName: com.netflix.loadbalancer.WeightedResponseTimeRule #配置规则 响应时间权重
    #NFLoadBalancerRuleClassName: com.netflix.loadbalancer.BestAvailableRule #配置规则 最空闲连接策略
    

17、文件上传
     /**
         * 文件上传
         * StandardMultipartHttpServletRequest request1 = (StandardMultipartHttpServletRequest)request;
         *                 Iterator iter = request1.getFileNames();
         *                 MultipartFile multipartFile = null;
         *                 while(iter.hasNext()) {
         *                     multipartFile = request1.getFile((String)iter.next());
         *                 }
         * 使用下面的写法，
         * 因为feign不能传递HttpServletRequest,用下面的方法，可以传递MultipartFile，
         * 调用端用HttpServletRequest request接受就行，可以不在使用@RequestPart("multipartFile") MultipartFile multipartFile
         * 例如：
         * public RestResult<UploadFileVo> feignUploadfile(HttpServletRequest request,  @RequestParam("object") String object)
         *
         * 封装MultipartFile
         *   MultipartFile multipartFile = null;
         *             // 通过InputStream获取Multipart
         *             multipartFile = new MockMultipartFile(
         *                     "File", fileName,
         *                     "text/plain",
         *                     inputStream);
         * @param token
         * @param multipartFile
         * @param object
         * @return
         */
        @PostMapping(value = "/filetransfer/feignUploadfile",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        FileResultVO uploadfile(@RequestHeader String token, @RequestPart("multipartFile") MultipartFile multipartFile, @RequestParam("object") String object);    

```

- Ribbon

```

1、java.lang.IllegalStateException：No instances available for localhost
    既然是负载均衡，那必然是多台服务器的负载均衡，用IP访问就没有意义了，因此用负载均衡的注解时，不能用IP或者localhost，而应该用服务名。
   注销@LoadBalanced
       @Bean
   //    @LoadBalanced
       RestTemplate restTemplate() {
           return new RestTemplate();
       }
    或者在配置类配置RestTemplate时，分别配置负载均衡与默认优先选择，即写两个RestTemplate方法
    @LoadBalanced
    @Bean
    public RestTemplate loadBalanced1() {
        return new RestTemplate();
    }

    @Primary
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

2、用法
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("http://localhost/getAll", List.class);
        HttpHeaders headers = responseEntity.getHeaders();
        HttpStatus statusCode = responseEntity.getStatusCode();
        int code = statusCode.value();
        List<UserEntity> list = responseEntity.getBody();

        ResponseEntity<UserEntity> responseEntity = restTemplate.getForEntity("http://localhost/get/{id}", UserEntity.class, id);
        UserEntity userEntity = responseEntity.getBody();

        HashMap<String, String> map = new HashMap<>();
        map.put("id",id);
        ResponseEntity<UserEntity> responseEntity = restTemplate.getForEntity("http://localhost/get/{id}", UserEntity.class, map);
        UserEntity userEntity = responseEntity.getBody();

        List<UserEntity> list = restTemplate.getForObject("http://localhost/getAll", List.class);

        UserEntity userEntity = restTemplate.getForObject("http://localhost/get/{id}", UserEntity.class, id);

        HashMap<String, String> map = new HashMap<>();
        map.put("id",id);
        UserEntity userEntity = restTemplate.getForObject("http://localhost/get/{id}", UserEntity.class, map);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost/save", userEntity, String.class);
        String body = responseEntity.getBody();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost/saveByType/{type}", userEntity, String.class, type);
        String body = responseEntity.getBody();

        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost/saveByType/{type}", userEntity, String.class,map);
        String body = responseEntity.getBody();

```

- hystrix

```

1、com.sun.jersey.api.client.ClientHandlerException: java.net.ConnectException
    #是否将自己注册到Eureka Server上，默认为true
    eureka:
      client:
        register-with-eureka: false
    #是否从Eureka Server上获取注册信息，默认为true
        fetch-registry: false

    原因：在默认设置下，Eureka服务注册中心也会将自己作为客户端来尝试注册它自己，所以我们需要禁用它的客户端注册行为。

2、fallback method wasn't found: hystrixFail([])

    @HystrixCommand(fallbackMethod = "fallbackHi")
    public String getHi(String x) {
        String msg = restTemplate.getForObject("http://jack/hi", String.class);
        return msg;
    }

    public String fallbackHi(){
        return "can't say hi";
    }
   这是因为指定的 备用方法 和 原方法 的参数个数，类型不同造成的；
   即：参数与返回值一样
   @HystrixCommand(fallbackMethod = "fallbackHi")
   public String getHi(String x) {
       String msg = restTemplate.getForObject("http://jack/hi", String.class);
       return msg;
   }

   public String fallbackHi(String x){
       return "can't say hi, and get: " + x;
   }

3、http://localhost:9002/hystrix
    404，dashboard版本的原因
    <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
                <version>2.2.2.RELEASE</version>
            </dependency>

4、http://localhost:9002/actuator/hystrix.stream
    Hystrix Dashboard界面一直loading的解决
    可能没有访问数据，调用一次接口就可以了

5、http://localhost:9002/turbine.stream
  汇总监控turbine
  能够使用turbine的前提条件时，
  所有的服务必须在同一个服务注册中心（同一个eureka），
  每个待监控的服务都必须有监控路径（/actuator/hystrix.stream）

```

- seata

```

1、spring cloud alibaba +seata 实战中Error processing condition on io.seata.spring.boot.autoconfigure
    seata 版本问题， spring-cloud-alibaba 依赖下的seata-spring-boot-starter 版本是1.1.0，而我用的seata是 1.3.0 版本 。
    1.3.0 版本SeataAutoConfiguration.class 没有seataDataSourceBeanPostProcessor bean ,而是用SeataAutoDataSourceProxyCreator代替。

    解决方案：
    去掉spring-cloud-starter-alibaba-seata 下seata-spring-boot-starter 依赖

    <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
            <exclusions>
                <exclusion>
                    <artifactId>seata-all</artifactId>
                    <groupId>io.seata</groupId>
                </exclusion>
                <exclusion>
                    <groupId>io.seata</groupId>
                    <artifactId>seata-spring-boot-starter</artifactId>
                </exclusion>
                <exclusion>
                    <artifactId>spring-boot-starter</artifactId>
                    <groupId>org.springframework.boot</groupId>
                </exclusion>
            </exclusions>
        </dependency>
           <dependency>
            <groupId>io.seata</groupId>
            <artifactId>seata-all</artifactId>
            <version>${spring-alibaba-seata.version}</version>
           <exclusions>
               <exclusion>
                   <artifactId>fastjson</artifactId>
                   <groupId>com.alibaba</groupId>
               </exclusion>
           </exclusions>
    </dependency>

2、java.lang.ClassNotFoundException: io.seata.spring.annotation.datasource.SeataAutoDataSourceProxyCreator
    导包集成完毕以后启动报错
    发现没有指定版本使用到了 最高版本
    Maven中引入spring-cloud-starter-alibaba-seata的时候不要引入2.1.1.RELEASE以上的就可以
    使用的版本过高 建议使用2.1.1.RELEASE以下

3、io.seata.common.exception.NotSupportYetException: not support register type: null
    spring-cloud-starter-alibaba-seata版本不兼容

4、 applicationId: null, txServiceGroup: my_test_group
    解决：增加配置 sping.application.name=XXX，yml配置为：
    spring:
      application:
        name: XXX

5、can not get cluster name in registry config 'service.vgroupMapping.my_test_group', please make sure registry config correct
    可能是seata版本的问题，seata导入的配置有下划线
    报错的是：
    service.vgroupMapping.my_test_group
    导入的是：
    service.vgroup_mapping.my_test_group=default

6、no available service found in cluster 'default', please make sure registry config correct and keep your seata server running
   yml的配置文件与seata下的register.conf配置不一致，可以参考下面内容

   registry:
     type: nacos  # 注册中心类型，需与registry.conf配置文件中一致
     nacos:
       application: serverAddr # 服务名，需与registry.conf配置文件中一致
       server-addr: 182.92.116.199:9999 # 注册中心的地址，需与registry.conf配置文件中一致
       group: DEFAULT_GROUP # 服务的分组名，需与registry.conf配置文件中一致
       namespace: 5e22d16b-da6c-4f3f-8f3a-41cb501f18e5 # nacos的命名空间ID，需与registry.conf配置文件中一致


   type = "nacos"
   nacos {
     serverAddr = "182.92.116.199:9999"
     namespace = "5e22d16b-da6c-4f3f-8f3a-41cb501f18e5"
     cluster = "default"
   }

7、no available service ‘null‘ found, please make sure registry config correct
   1、seata配置文件不对
   2、yml配置文件与seata registry.conf不对应
   3、springboot、seata版本不一致

8、事务回滚方式
   1、手动回滚
       GlobalTransactionContext.reload(RootContext.getXID()).rollback();
   2、FallBack+Aspect回滚

9、异常不回滚
   1、全局异常，统一返回Result（需要手动回滚）
   2、FallBack服务降级，统一返回Result （需要手动回滚）

```

- sentinel

```

1、配置规则
    @Configuration
    public class SentinelConfig {
    
        @Bean
        public SentinelResourceAspect sentinelResourceAspect() {
            return new SentinelResourceAspect();
        }
    
        @PostConstruct
        private void initRules() {
    
            //=============================规则1=========================
            FlowRule rule1 = new FlowRule();
            rule1.setResource("rule1");//规则名称
            rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);//如果设置0则按照线程数限流，如果设置1则按照QPS（每秒查询率）限流
            rule1.setCount(3);   // 每秒调用最大次数为 10 次
            rule1.setControlBehavior(2); //0快速失败，1预警，2排队等候
            rule1.setMaxQueueingTimeMs(1000);//排队超时阈值
    
            //=============================规则2=========================
            FlowRule rule2 = new FlowRule();
            rule2.setResource("rule2");
            rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
            rule2.setCount(10);   // 每秒调用最大次数为 10 次
    
            List<FlowRule> rules = new ArrayList<>();
            rules.add(rule1);
            rules.add(rule2);
    
            // 将控制规则载入到 Sentinel
            FlowRuleManager.loadRules(rules);
    
        }
    
    }

2、接口配置服务降级等逻辑
    @Slf4j
    @RestController
    @RequestMapping("/sentinel")
    public class SentinelController {
    
        private AtomicInteger count = new AtomicInteger();
    
        /**
         * blockHandler: 定义了流控触发后的处理逻辑
         * fallback: 当接口发生错误异常后会触发的逻辑
         * @param name
         * @return
         */
        @GetMapping("/sayHello")
        @SentinelResource(value = "rule1",blockHandler = "blockHandler",fallback = "fallback")
        public String sayHello(String name ){
    
            log.info("count -> {}",count);
    
            if(count.incrementAndGet() % 2 == 0 ){
                throw new RuntimeException("error");
            }
            return "Hello World "+name;
        }
    
        public String blockHandler(String name, BlockException e){
            e.printStackTrace();
            return "Blocked "+name;
        }
    
        public String fallback(String name,Throwable e){
            e.printStackTrace();
            return "Fallback "+name;
        }
    
    }

```

## 算法

```

排序算法可以分为内部排序和外部排序，内部排序是数据记录在内存中进行排序，而外部排序是因排序的数据很大，一次不能容纳全部的排序记录，在排序过程中需要访问外存。
常见的内部排序算法有：插入排序、希尔排序、选择排序、冒泡排序、归并排序、快速排序、堆排序、基数排序等。

稳定的排序算法：冒泡排序、插入排序、归并排序和基数排序。
不是稳定的排序算法：选择排序、快速排序、希尔排序、堆排序。

https://github.com/hustcc/JS-Sorting-Algorithm

```

- 冒泡排序

```

冒泡排序（Bubble Sort）也是一种简单直观的排序算法。它重复地走访过要排序的数列，一次比较两个元素，如果他们的顺序错误就把他们交换过来。走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。这个算法的名字由来是因为越小的元素会经由交换慢慢“浮”到数列的顶端。

作为最简单的排序算法之一，冒泡排序给我的感觉就像 Abandon 在单词书里出现的感觉一样，每次都在第一页第一位，所以最熟悉。冒泡排序还有一种优化算法，就是立一个 flag，当在一趟序列遍历中元素没有发生交换，则证明该序列已经有序。但这种改进对于提升性能来说并没有什么太大作用。

1. 算法步骤
比较相邻的元素。如果第一个比第二个大，就交换他们两个。

对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。这步做完后，最后的元素会是最大的数。

针对所有的元素重复以上的步骤，除了最后一个。

持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。

```

- 选择排序

```

选择排序是一种简单直观的排序算法，无论什么数据进去都是 O(n²) 的时间复杂度。所以用到它的时候，数据规模越小越好。唯一的好处可能就是不占用额外的内存空间了吧。

1. 算法步骤
首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置

再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。

重复第二步，直到所有元素均排序完毕。

```

- 插入排序

```

插入排序的代码实现虽然没有冒泡排序和选择排序那么简单粗暴，但它的原理应该是最容易理解的了，因为只要打过扑克牌的人都应该能够秒懂。插入排序是一种最简单直观的排序算法，它的工作原理是通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。

插入排序和冒泡排序一样，也有一种优化算法，叫做拆半插入。

1. 算法步骤
将第一待排序序列第一个元素看做一个有序序列，把第二个元素到最后一个元素当成是未排序序列。

从头到尾依次扫描未排序序列，将扫描到的每个元素插入有序序列的适当位置。（如果待插入的元素与有序序列中的某个元素相等，则将待插入元素插入到相等元素的后面。）

```

- 希尔排序

```

希尔排序，也称递减增量排序算法，是插入排序的一种更高效的改进版本。但希尔排序是非稳定排序算法。

希尔排序是基于插入排序的以下两点性质而提出改进方法的：

插入排序在对几乎已经排好序的数据操作时，效率高，即可以达到线性排序的效率；
但插入排序一般来说是低效的，因为插入排序每次只能将数据移动一位；
希尔排序的基本思想是：先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，待整个序列中的记录“基本有序”时，再对全体记录进行依次直接插入排序。

1. 算法步骤
选择一个增量序列 t1，t2，……，tk，其中 ti > tj, tk = 1；

按增量序列个数 k，对序列进行 k 趟排序；

每趟排序，根据对应的增量 ti，将待排序列分割成若干长度为 m 的子序列，分别对各子表进行直接插入排序。仅增量因子为 1 时，整个序列作为一个表来处理，表长度即为整个序列的长度。

```

- 归并排序

```

归并排序（Merge sort）是建立在归并操作上的一种有效的排序算法。该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。

作为一种典型的分而治之思想的算法应用，归并排序的实现由两种方法：

自上而下的递归（所有递归的方法都可以用迭代重写，所以就有了第 2 种方法）；
自下而上的迭代；
在《数据结构与算法 JavaScript 描述》中，作者给出了自下而上的迭代方法。但是对于递归法，作者却认为：

However, it is not possible to do so in JavaScript, as the recursion goes too deep for the language to handle.

然而，在 JavaScript 中这种方式不太可行，因为这个算法的递归深度对它来讲太深了。

说实话，我不太理解这句话。意思是 JavaScript 编译器内存太小，递归太深容易造成内存溢出吗？还望有大神能够指教。

和选择排序一样，归并排序的性能不受输入数据的影响，但表现比选择排序好的多，因为始终都是 O(nlogn) 的时间复杂度。代价是需要额外的内存空间。

2. 算法步骤
申请空间，使其大小为两个已经排序序列之和，该空间用来存放合并后的序列；

设定两个指针，最初位置分别为两个已经排序序列的起始位置；

比较两个指针所指向的元素，选择相对小的元素放入到合并空间，并移动指针到下一位置；

重复步骤 3 直到某一指针达到序列尾；

将另一序列剩下的所有元素直接复制到合并序列尾。

```

- 快速排序

```

快速排序是由东尼·霍尔所发展的一种排序算法。在平均状况下，排序 n 个项目要 Ο(nlogn) 次比较。在最坏状况下则需要 Ο(n2) 次比较，但这种状况并不常见。事实上，快速排序通常明显比其他 Ο(nlogn) 算法更快，因为它的内部循环（inner loop）可以在大部分的架构上很有效率地被实现出来。

快速排序使用分治法（Divide and conquer）策略来把一个串行（list）分为两个子串行（sub-lists）。

快速排序又是一种分而治之思想在排序算法上的典型应用。本质上来看，快速排序应该算是在冒泡排序基础上的递归分治法。

快速排序的名字起的是简单粗暴，因为一听到这个名字你就知道它存在的意义，就是快，而且效率高！它是处理大数据最快的排序算法之一了。虽然 Worst Case 的时间复杂度达到了 O(n²)，但是人家就是优秀，在大多数情况下都比平均时间复杂度为 O(n logn) 的排序算法表现要更好，可是这是为什么呢，我也不知道。好在我的强迫症又犯了，查了 N 多资料终于在《算法艺术与信息学竞赛》上找到了满意的答案：

快速排序的最坏运行情况是 O(n²)，比如说顺序数列的快排。但它的平摊期望时间是 O(nlogn)，且 O(nlogn) 记号中隐含的常数因子很小，比复杂度稳定等于 O(nlogn) 的归并排序要小很多。所以，对绝大多数顺序性较弱的随机数列而言，快速排序总是优于归并排序。

1. 算法步骤
从数列中挑出一个元素，称为 “基准”（pivot）;

重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；

递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序；

递归的最底部情形，是数列的大小是零或一，也就是永远都已经被排序好了。虽然一直递归下去，但是这个算法总会退出，因为在每次的迭代（iteration）中，它至少会把一个元素摆到它最后的位置去。

```

- 堆排序

```

堆排序（Heapsort）是指利用堆这种数据结构所设计的一种排序算法。堆积是一个近似完全二叉树的结构，并同时满足堆积的性质：即子结点的键值或索引总是小于（或者大于）它的父节点。堆排序可以说是一种利用堆的概念来排序的选择排序。分为两种方法：

大顶堆：每个节点的值都大于或等于其子节点的值，在堆排序算法中用于升序排列；
小顶堆：每个节点的值都小于或等于其子节点的值，在堆排序算法中用于降序排列；
堆排序的平均时间复杂度为 Ο(nlogn)。

1. 算法步骤
将待排序序列构建成一个堆 H[0……n-1]，根据（升序降序需求）选择大顶堆或小顶堆；

把堆首（最大值）和堆尾互换；

把堆的尺寸缩小 1，并调用 shift_down(0)，目的是把新的数组顶端数据调整到相应位置；

重复步骤 2，直到堆的尺寸为 1。

```

- 计数排序

```

计数排序的核心在于将输入的数据值转化为键存储在额外开辟的数组空间中。作为一种线性时间复杂度的排序，计数排序要求输入的数据必须是有确定范围的整数。

```

- 桶排序

```

桶排序是计数排序的升级版。它利用了函数的映射关系，高效与否的关键就在于这个映射函数的确定。为了使桶排序更加高效，我们需要做到这两点：

在额外空间充足的情况下，尽量增大桶的数量
使用的映射函数能够将输入的 N 个数据均匀的分配到 K 个桶中
同时，对于桶中元素的排序，选择何种比较排序算法对于性能的影响至关重要。

1. 什么时候最快
当输入的数据可以均匀的分配到每一个桶中。

2. 什么时候最慢
当输入的数据被分配到了同一个桶中。

```

- 基数排序

```

基数排序是一种非比较型整数排序算法，其原理是将整数按位数切割成不同的数字，然后按每个位数分别比较。由于整数也可以表达字符串（比如名字或日期）和特定格式的浮点数，所以基数排序也不是只能使用于整数。

1. 基数排序 vs 计数排序 vs 桶排序
基数排序有两种方法：

这三种排序算法都利用了桶的概念，但对桶的使用方法上有明显差异案例看大家发的：

基数排序：根据键值的每位数字来分配桶；
计数排序：每个桶只存储单一键值；
桶排序：每个桶存储一定范围的数值；

```

## example

- example

```

```
