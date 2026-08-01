
/******************************************/
/*   表名称 = config_info   */
/******************************************/

drop DATABASE if EXISTS `ry-config`;
creaTE DATABASE `ry-config` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
use `ry-config`;

CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) DEFAULT NULL COMMENT 'group_id',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) DEFAULT NULL COMMENT 'configuration description',
  `c_use` varchar(64) DEFAULT NULL COMMENT 'configuration usage',
  `effect` varchar(64) DEFAULT NULL COMMENT '配置生效的描述',
  `type` varchar(64) DEFAULT NULL COMMENT '配置的类型',
  `c_schema` text COMMENT '配置的模式',
  `encrypted_data_key` text NOT NULL COMMENT '密钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

insert into config_info(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) values
(1, 'application-common.yml', 'DEFAULT_GROUP', ' # 内嵌服务器
server:
  undertow:
    # HTTP post内容的最大大小。当值为-1时，默认值为大小是无限的
    max-http-post-size: 1GB
    # buffer会用于服务器连接的IO操作,有点类似netty的池化内存管理
    # 每块buffer的空间大小,越小的空间被利用越充分
    buffer-size: 512
    # 是否分配的直接内存
    direct-buffers: true
    threads:
      # 设置IO线程数, 它主要执行非阻塞的任务,它们会负责多个连接, 默认设置每个CPU核心一个线程
      io: 8
      # 阻塞任务线程池, 当执行类似servlet请求阻塞操作, undertow会从这个线程池中取得线程,它的值设置取决于系统的负载
      worker: 256

# RPC 框架
dubbo:
  application:
    # 关闭qos端口避免单机多生产者端口冲突 如需使用自行开启
    qos-enable: false
  protocol:
    # 如需使用 Triple 3.0 新协议 可查看官方文档
    # 使用 dubbo 协议通信
    name: dubbo
    # dubbo 协议端口(-1表示自增端口,从20880开始)
    port: -1
    # 指定dubbo协议注册ip
    # host: 192.168.0.100
    # 开启虚拟线程
    # threadpool: virtual
  # 消费者相关配置
  consumer:
    # 超时时间
    timeout: 3000
  # 自定义配置
  custom:
    # 全局请求log
    request-log: true
    # info 基础信息 param 参数信息 full 全部
    log-level: info

spring:
  threads:
    # 开启虚拟线程 仅jdk21可用
    virtual:
      enabled: false
  task:
    execution:
      # 从 springboot 3.5 开始 spring自带线程池
      # 不再需要 AsyncConfig与ThreadPoolConfig 可直接注入线程池使用
      thread-name-prefix: async-
      # 由spring自己初始化线程池
      mode: force
  # 资源信息
  messages:
    # 国际化资源文件路径
    basename: i18n/messages
  servlet:
    multipart:
      # 整个请求大小限制
      max-request-size: 20MB
      # 上传单个文件大小限制
      max-file-size: 10MB
  mvc:
    # 设置静态资源路径 防止所有请求都去查静态资源
    static-path-pattern: /static/**
    format:
      date-time: yyyy-MM-dd HH:mm:ss
  #jackson配置
  jackson:
    # 日期格式化
    date-format: yyyy-MM-dd HH:mm:ss
    serialization:
      # 格式化输出
      INDENT_OUTPUT: false
      # 忽略无法转换的对象
      fail_on_empty_beans: false
    deserialization:
      # 允许对象忽略json中不存在的属性
      fail_on_unknown_properties: false
  # 云分布式
  cloud:
    nacos:
      discovery:
        metadata:
          # admin 监控账号密码
          username: ruoyi
          userpassword: 123456
    bus:
      id: ${spring.application.name}
      base-packages: org.dromara.**.event
  # 消息总线 也可以使用 kafka 参考 spring-cloud-bus 用法
  rabbitmq:
    host: guli-rabbitmq
    port: 5672
    username: ruoyi
    password: ruoyi123

  # redis(存储层) 通用配置: 子服务可以自行配置进行覆盖
  data:
    redis:
      host: guli-redis
      port: 6379
      # redis 密码必须配置
      password: ruoyi123
      database: 0
      # 需要使用数字
      timeout: 10000
      ssl.enabled: false

# redis分布式锁(引擎层)
redisson:
  # redis key前缀
  keyPrefix:
  # 线程池数量
  threads: 4
  # Netty线程池数量
  nettyThreads: 8
  # 单节点配置
  singleServerConfig:
    # 客户端名称
    clientName: ${spring.application.name}
    # 最小空闲连接数
    connectionMinimumIdleSize: 8
    # 连接池大小
    connectionPoolSize: 32
    # 连接空闲超时，单位：毫秒
    idleConnectionTimeout: 10000
    # 命令等待超时，单位：毫秒
    timeout: 3000
    # 发布和订阅连接池大小
    subscriptionConnectionPoolSize: 50

# 分布式锁 lock4j 全局配置 (注解层)
lock4j:
  # 获取分布式锁超时时间，默认为 3000 毫秒
  acquire-timeout: 3000
  # 分布式锁的超时时间，默认为 30 秒
  expire: 30000

# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: ''*''
  endpoint:
    health:
      show-details: ALWAYS
    logfile:
      external-file: ./logs/${spring.application.name}/console.log

# 日志配置
logging:
  level:
    org.springframework: warn
    org.apache.dubbo: error
    com.alibaba.nacos: error
    org.mybatis.spring.mapper: error
    org.apache.fury: warn
    io.micrometer: error
    # 临时处理 spring 调整日志级别导致启动警告问题 不影响使用等待 alibaba 适配
    org.springframework.context.support.PostProcessorRegistrationDelegate: error
  config: classpath:logback-plus.xml

# Sa-Token配置
sa-token:
  # token名称 (同时也是cookie名称)
  token-name: Authorization
  # 开启内网服务调用鉴权(不允许越过gateway访问内网服务 保障服务安全)
  check-same-token: true
  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
  is-share: false
  # jwt秘钥
  jwt-secret-key: abcdefghijklmnopqrstuvwxyz

# MyBatisPlus配置 https://baomidou.com/config/
mybatis-plus:
  # 自定义配置 是否全局开启逻辑删除 关闭后 所有逻辑删除功能将失效
  enableLogicDelete: true
  # Mapper 扫描包路径，保证 @MapperScan 能扫到所有模块的 Mapper 接口
  # 多包名用逗号分隔：org.dromara.**.mapper（Ruoyi 平台）, com.atlearn.guli.**.mapper（Guli 业务）
  mapperPackage: org.dromara.**.mapper,com.atlearn.guli.**.mapper
  # 对应的 XML 文件位置
  mapperLocations: classpath*:mapper/**/*Mapper.xml
  # 实体扫描，多个package用逗号或者分号分隔
  typeAliasesPackage: org.dromara.**.domain
  global-config:
    dbConfig:
      # 主键类型
      # AUTO 自增 NONE 空 INPUT 用户输入 ASSIGN_ID 雪花 ASSIGN_UUID 唯一 UUID
      # 如需改为自增 需要将数据库表全部设置为自增
      idType: ASSIGN_ID

# 数据加密
mybatis-encryptor:
  # 是否开启加密
  enable: false
  # 默认加密算法
  algorithm: BASE64
  # 编码方式 BASE64/HEX。默认BASE64
  encode: BASE64
  # 安全秘钥 对称算法的秘钥 如：AES，SM4
  password:
  # 公私钥 非对称算法的公私钥 如：SM2，RSA
  publicKey:
  privateKey:

# api接口加密
api-decrypt:
  # 是否开启全局接口加密
  enabled: true
  # AES 加密头标识
  headerFlag: encrypt-key
  # 响应加密公钥 非对称算法的公私钥 如：SM2，RSA 使用者请自行更换
  # 对应前端解密私钥 MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAmc3CuPiGL/LcIIm7zryCEIbl1SPzBkr75E2VMtxegyZ1lYRD+7TZGAPkvIsBcaMs6Nsy0L78n2qh+lIZMpLH8wIDAQABAkEAk82Mhz0tlv6IVCyIcw/s3f0E+WLmtPFyR9/WtV3Y5aaejUkU60JpX4m5xNR2VaqOLTZAYjW8Wy0aXr3zYIhhQQIhAMfqR9oFdYw1J9SsNc+CrhugAvKTi0+BF6VoL6psWhvbAiEAxPPNTmrkmrXwdm/pQQu3UOQmc2vCZ5tiKpW10CgJi8kCIFGkL6utxw93Ncj4exE/gPLvKcT+1Emnoox+O9kRXss5AiAMtYLJDaLEzPrAWcZeeSgSIzbL+ecokmFKSDDcRske6QIgSMkHedwND1olF8vlKsJUGK3BcdtM8w4Xq7BpSBwsloE=
  publicKey: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJnNwrj4hi/y3CCJu868ghCG5dUj8wZK++RNlTLcXoMmdZWEQ/u02RgD5LyLAXGjLOjbMtC+/J9qofpSGTKSx/MCAwEAAQ==
  # 请求解密私钥 非对称算法的公私钥 如：SM2，RSA 使用者请自行更换
  # 对应前端加密公钥 MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKoR8mX0rGKLqzcWmOzbfj64K8ZIgOdHnzkXSOVOZbFu/TJhZ7rFAN+eaGkl3C4buccQd/EjEsj9ir7ijT7h96MCAwEAAQ==
  privateKey: MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAqhHyZfSsYourNxaY7Nt+PrgrxkiA50efORdI5U5lsW79MmFnusUA355oaSXcLhu5xxB38SMSyP2KvuKNPuH3owIDAQABAkAfoiLyL+Z4lf4Myxk6xUDgLaWGximj20CUf+5BKKnlrK+Ed8gAkM0HqoTt2UZwA5E2MzS4EI2gjfQhz5X28uqxAiEA3wNFxfrCZlSZHb0gn2zDpWowcSxQAgiCstxGUoOqlW8CIQDDOerGKH5OmCJ4Z21v+F25WaHYPxCFMvwxpcw99EcvDQIgIdhDTIqD2jfYjPTY8Jj3EDGPbH2HHuffvflECt3Ek60CIQCFRlCkHpi7hthhYhovyloRYsM+IS9h/0BzlEAuO0ktMQIgSPT3aFAgJYwKpqRYKlLDVcflZFCKY7u3UP8iWi1Qw0Y=

# 防止XSS攻击
xss:
  enabled: true
  excludeUrls:
    - /system/notice
    - /warm-flow/save-json

# 接口文档配置
springdoc:
  api-docs:
    # 是否开启接口文档
    enabled: true
  info:
    # 标题
    title: ''标题：RuoYi-Cloud-Plus微服务权限管理系统_接口文档''
    # 描述
    description: ''描述：微服务权限管理系统, 具体包括XXX,XXX模块...''
    # 版本
    version: ''版本号：系统版本...''
    # 作者信息
    contact:
      name: Lion Li
      email: crazylionli@163.com
      url: https://gitee.com/dromara/RuoYi-Cloud-Plus

# seata配置
seata:
  # 是否启用
  enabled: false
  # Seata 应用编号，默认为应用名
  application-id: ${spring.application.name}
  # Seata 事务组编号，用于 TC 集群名
  tx-service-group: ${spring.application.name}-group
  config:
    type: nacos
    nacos:
      server-addr: ${spring.cloud.nacos.server-addr}
      group: ${spring.cloud.nacos.config.group}
      namespace: ${spring.profiles.active}
      username: ${spring.cloud.nacos.username}
      password: ${spring.cloud.nacos.password}
      data-id: seata-server.properties
  registry:
    type: nacos
    nacos:
      application: ruoyi-seata-server
      server-addr: ${spring.cloud.nacos.server-addr}
      group: ${spring.cloud.nacos.discovery.group}
      username: ${spring.cloud.nacos.username}
      password: ${spring.cloud.nacos.password}
      namespace: ${spring.profiles.active}
  # 关闭自动代理
  enable-auto-data-source-proxy: false

# 多租户配置
tenant:
  # 是否开启
  enable: true
  # 排除表
  excludes:
    - sys_menu
    - sys_tenant
    - sys_tenant_package
    - sys_role_dept
    - sys_role_menu
    - sys_user_post
    - sys_user_role
    - sys_client
    - sys_oss_config
    - flow_spel', '20487bb9b891e70e0e772c510a284631', '2022-01-09 15:18:55', '2022-01-09 15:18:55', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '通用配置基础配置', NULL, NULL, 'yaml', NULL, ''),
(2, 'datasource.yml', 'DEFAULT_GROUP', 'datasource:
  system-master:
    # jdbc 所有参数配置参考 https://lionli.blog.csdn.net/article/details/122018562
    # rewriteBatchedStatements=true 批处理优化 大幅提升批量插入更新删除性能
    url: jdbc:mysql://guli-mysql:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
    username: root
    password: password
  gen:
    url: jdbc:mysql://guli-mysql:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
    username: root
    password: password
  job:
    url: jdbc:mysql://guli-mysql:3306/ry-job?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
    username: root
    password: password
  workflow:
    url: jdbc:mysql://guli-mysql:3306/ry-workflow?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true
    username: root
    password: password


spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    # 动态数据源文档 https://www.kancloud.cn/tracy5546/dynamic-datasource/content
    dynamic:
      # 性能分析插件(有性能损耗 不建议生产环境使用)
      p6spy: true
      # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭
      seata: ${seata.enabled}
      # 严格模式 匹配不到数据源则报错
      strict: true
      hikari:
        # 最大连接池数量
        maxPoolSize: 20
        # 最小空闲线程数量
        minIdle: 10
        # 配置获取连接等待超时的时间
        connectionTimeout: 30000
        # 校验超时时间
        validationTimeout: 5000
        # 空闲连接存活最大时间，默认10分钟
        idleTimeout: 600000
        # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认30分钟
        maxLifetime: 1800000
        # 多久检查一次连接的活性
        keepaliveTime: 30000', '39519abdbf09a81cfd13351a002367da', '2022-01-09 15:19:07', '2022-01-09 15:19:07', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '数据源配置', NULL, NULL, 'yaml', NULL, ''),
(3, 'ruoyi-gateway.yml', 'DEFAULT_GROUP', '# 安全配置
security:
  # 不校验白名单
  ignore:
    whites:
      - /auth/code
      - /auth/logout
      - /auth/login
      - /auth/binding/*
      - /auth/register
      - /auth/tenant/list
      - /resource/sms/code
      - /resource/sse/close
      - /*/v3/api-docs
      - /*/error
      - /csrf
      - /warm-flow-ui/**

spring:
  cloud:
    # 网关配置
    gateway:
      # 打印请求日志(自定义)
      requestLog: true
      server:
        webflux:
          # Gateway WebFlux 转发使用的 Reactor Netty 连接池配置
          httpclient:
            pool:
              # 使用固定连接池，避免 SSE 长连接占满默认连接池后快速拒绝普通请求
              type: fixed
              # 总最大连接数
              max-connections: 1000
              # 连接池满时等待可用连接的时间，单位毫秒
              acquire-timeout: 3000
              # 空闲连接最大保留时间
              max-idle-time: 30s
              # 后台空闲连接回收间隔
              eviction-interval: 30s
            # SSE 场景通常不设置响应超时，避免长连接被网关主动断开
            # response-timeout: 60s
          discovery:
            locator:
              lowerCaseServiceId: true
              enabled: true
          routes:
            # 认证中心
            - id: ruoyi-auth
              uri: lb://ruoyi-auth
              predicates:
                - Path=/auth/**
              filters:
                - StripPrefix=1
            # 代码生成
            - id: ruoyi-gen
              uri: lb://ruoyi-gen
              predicates:
                - Path=/tool/**
              filters:
                - StripPrefix=1
            # 系统模块
            - id: ruoyi-system
              uri: lb://ruoyi-system
              predicates:
                - Path=/system/**,/monitor/**
              filters:
                - StripPrefix=1
            # 资源服务
            - id: ruoyi-resource
              uri: lb://ruoyi-resource
              predicates:
                - Path=/resource/**
              filters:
                - StripPrefix=1
            # workflow服务
            - id: ruoyi-workflow
              uri: lb://ruoyi-workflow
              predicates:
                - Path=/workflow/**
              filters:
                - StripPrefix=1
            # warm-flow服务
            - id: warm-flow
              uri: lb://ruoyi-workflow
              predicates:
                - Path=/warm-flow-ui/**,/warm-flow/**
            # 演示服务
            - id: ruoyi-demo
              uri: lb://ruoyi-demo
              predicates:
                - Path=/demo/**
              filters:
                - StripPrefix=1
            # MQ演示服务
            - id: ruoyi-test-mq
              uri: lb://ruoyi-test-mq
              predicates:
                - Path=/test-mq/**
              filters:
                - StripPrefix=1
            # guli-mall 商品服务
            - id: guli-product
              uri: lb://guli-product
              predicates:
                - Path=/guli/**
              filters:
                - StripPrefix=1', 'a53d4df83393dbac512231b30288e97f', '2022-01-09 15:19:43', '2022-01-09 15:22:42', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '网关模块', NULL, NULL, 'yaml', NULL, ''),
(4, 'ruoyi-auth.yml', 'DEFAULT_GROUP', '# 安全配置
security:
  # 验证码
  captcha:
    # 是否开启验证码
    enabled: true
    # 验证码类型 math 数组计算 char 字符验证
    type: math
    # 数字验证码位数
    numberLength: 1
    # 字符验证码长度
    charLength: 4

# 用户配置
user:
  password:
    # 密码最大错误次数
    maxRetryCount: 5
    # 密码锁定时间（默认10分钟）
    lockTime: 10

# 三方授权
justauth:
  # 前端外网访问地址
  address: http://guli-nginx:80
  type:
    qq:
      client-id: 10**********6
      client-secret: 1f7d08**********5b7**********29e
      redirect-uri: ${justauth.address}/social-callback?source=qq
      union-id: false
    wechat_open:
      client-id: 10**********6
      client-secret: 1f7d08**********5b7**********29e
      redirect-uri: ${justauth.address}/social-callback?source=wechat_open', 'c0a9eb1b7cfd759e299a10519e6a8dcf', '2022-01-09 15:19:43', '2022-01-09 15:22:29', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '认证中心', NULL, NULL, 'yaml', NULL, ''),
(5, 'ruoyi-monitor.yml', 'DEFAULT_GROUP', '# 监控中心配置
spring:
  security:
    user:
      name: ruoyi
      password: 123456
  boot:
    admin:
      ui:
        title: RuoYi-Cloud-Plus服务监控中心
      discovery:
        # seata 不具有健康检查的能力 防止报错排除掉
        ignored-services: ruoyi-seata-server
  # 忽略无用警告
  thymeleaf:
    check-template-location: false', '43e539f88a7e704477dac189f80a2bce', '2022-01-09 15:20:18', '2022-01-09 15:22:15', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '监控中心', NULL, NULL, 'yaml', NULL, ''),
(6, 'ruoyi-system.yml', 'DEFAULT_GROUP', 'spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}', '71f39b8986d41f4e692e83cd9694e1bc', '2022-01-09 15:20:18', '2022-01-09 15:22:03', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '系统模块', NULL, NULL, 'yaml', NULL, ''),
(7, 'ruoyi-gen.yml', 'DEFAULT_GROUP', 'spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      seata: false
      datasource:
        # 主库数据源 (主数据源内包含代码生成数据表 增加其他数据库不要修改主数据源 否则数据无法存储)
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}

# 代码生成
gen:
  # 作者
  author: mayao
  # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool
  packageName: com.atmayao.guli
  # 自动去除表前缀，默认是false
  autoRemovePre: true
  # 表前缀（生成类名不会包含表前缀，多个用逗号分隔）
  tablePrefix: sys_', 'b363cfa42b31dde07cee887d73aad947', '2022-01-09 15:20:18', '2022-01-09 15:21:51', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '代码生成', NULL, NULL, 'yaml', NULL, ''),
(8, 'ruoyi-job.yml', 'DEFAULT_GROUP', 'spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      seata: false
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.job.url}
          username: ${datasource.job.username}
          password: ${datasource.job.password}

snail-job:
  enabled: true
  # 需要在 SnailJob 后台组管理创建对应名称的组,然后创建任务的时候选择对应的组,才能正确分派任务
  group: "ruoyi_group"
  #  SnailJob 接入验证令牌
  token: "SJ_cKqBTPzCsWA3VyuCfFoccmuIEGXjr5KT"
  server:
    # 从 nacos 获取服务
    server-name: ruoyi-snailjob-server
    # 服务名优先 ip垫底
    host: guli-snailjob
    port: 17888
  # 详见 sql/ry_job.sql `sj_namespace` 表 `unique_id`
  namespace: ${spring.profiles.active}
  # 随主应用端口飘逸
  port: 2${server.port}
  # 客户端ip指定
  host:', 'b7571ec5dc7841a16dae08fcb3b84aa0', '2022-01-09 15:20:18', '2022-01-09 15:21:36', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '定时任务', NULL, NULL, 'yaml', NULL, ''),
(9, 'ruoyi-resource.yml', 'DEFAULT_GROUP', 'spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}

# 默认/推荐使用sse推送
sse:
  enabled: true
  path: /sse

websocket:
  # 如果关闭 需要和前端开关一起关闭
  enabled: false
  # 路径
  path: /websocket
  # 设置访问源地址
  allowedOrigins: ''*''

mail:
  enabled: false
  host: smtp.163.com
  port: 465
  # 是否需要用户名密码验证
  auth: true
  # 发送方，遵循RFC-822标准
  from: xxx@163.com
  # 用户名（注意：如果使用foxmail邮箱，此处user为qq号）
  user: xxx@163.com
  # 密码（注意，某些邮箱需要为SMTP服务单独设置密码，详情查看相关帮助）
  pass: xxxxxxxxxx
  # 使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。
  starttlsEnable: true
  # 使用SSL安全连接
  sslEnable: true
  # SMTP超时时长，单位毫秒，缺省值不超时
  timeout: 0
  # Socket连接超时值，单位毫秒，缺省值不超时
  connectionTimeout: 0

# sms 短信 支持 阿里云 腾讯云 云片 等等各式各样的短信服务商
# https://sms4j.com/doc3/ 差异配置文档地址 支持单厂商多配置，可以配置多个同时使用
sms:
  # 配置源类型用于标定配置来源(interface,yaml)
  config-type: yaml
  # 用于标定yml中的配置是否开启短信拦截，接口配置不受此限制
  restricted: true
  # 短信拦截限制单手机号每分钟最大发送，只对开启了拦截的配置有效
  minute-max: 1
  # 短信拦截限制单手机号每日最大发送量，只对开启了拦截的配置有效
  account-max: 30
  # 以下配置来自于 org.dromara.sms4j.provider.config.BaseConfig类中
  blends:
    # 唯一ID 用于发送短信寻找具体配置 随便定义别用中文即可
    # 可以同时存在两个相同厂商 例如: ali1 ali2 两个不同的阿里短信账号 也可用于区分租户
    config1:
      # 框架定义的厂商名称标识，标定此配置是哪个厂商，详细请看厂商标识介绍部分
      supplier: alibaba
      # 有些称为accessKey有些称之为apiKey，也有称为sdkKey或者appId。
      access-key-id: 您的accessKey
      # 称为accessSecret有些称之为apiSecret
      access-key-secret: 您的accessKeySecret
      signature: 您的短信签名
      sdk-app-id: 您的sdkAppId
    config2:
      # 厂商标识，标定此配置是哪个厂商，详细请看厂商标识介绍部分
      supplier: tencent
      access-key-id: 您的accessKey
      access-key-secret: 您的accessKeySecret
      signature: 您的短信签名
      sdk-app-id: 您的sdkAppId', '16aef84a00fe39c8cdad702f4f47575e', '2022-01-09 15:20:35', '2022-01-09 15:21:21', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '文件服务', NULL, NULL, 'yaml', NULL, ''),
(10, 'ruoyi-workflow.yml', 'DEFAULT_GROUP', 'spring:
  datasource:
    dynamic:
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.workflow.url}
          username: ${datasource.workflow.username}
          password: ${datasource.workflow.password}
#        oracle:
#          type: ${spring.datasource.type}
#          driverClassName: oracle.jdbc.OracleDriver
#          url: ${datasource.system-oracle.url}
#          username: ${datasource.system-oracle.username}
#          password: ${datasource.system-oracle.password}
#        postgres:
#          type: ${spring.datasource.type}
#          driverClassName: org.postgresql.Driver
#          url: ${datasource.system-postgres.url}
#          username: ${datasource.system-postgres.username}
#          password: ${datasource.system-postgres.password}

# warm-flow工作流配置
warm-flow:
  # 是否开启工作流，默认true
  enabled: true
  # 是否开启设计器ui
  ui: true
  # 是否显示流程图顶部文字
  top-text-show: true
  # 是否渲染节点悬浮提示，默认true
  node-tooltip: true
  # 默认Authorization，如果有多个token，用逗号分隔
  token-name: ${sa-token.token-name},clientid', '775626cf8ba73641814932206f502330', '2022-01-09 15:20:35', '2022-01-09 15:21:21', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '工作流服务', NULL, NULL, 'yaml', NULL, ''),
(12, 'seata-server.properties', 'DEFAULT_GROUP', 'service.vgroupMapping.ruoyi-auth-group=default
service.vgroupMapping.ruoyi-system-group=default
service.vgroupMapping.ruoyi-resource-group=default
service.vgroupMapping.ruoyi-workflow-group=default

service.enableDegrade=false
service.disableGlobalTransaction=false

#Transaction storage configuration, only for the server. The file, DB, and redis configuration values are optional.
store.mode=db
store.lock.mode=db
store.session.mode=db
#Used for password encryption
#store.publicKey=

#These configurations are required if the `store mode` is `db`. If `store.mode,store.lock.mode,store.session.mode` are not equal to `db`, you can remove the configuration block.
store.db.datasource=hikari
store.db.dbType=mysql
store.db.driverClassName=com.mysql.cj.jdbc.Driver
store.db.url=jdbc:mysql://guli-mysql:3306/ry-seata?useUnicode=true&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
store.db.user=root
store.db.password=password
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.distributedLockTable=distributed_lock
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000

# redis 模式 store.mode=redis 开启 (控制台查询功能有限,不影响实际执行功能)
# store.redis.host=127.0.0.1
# store.redis.port=6379
# 最大连接数
# store.redis.maxConn=10
# 最小连接数
# store.redis.minConn=1
# store.redis.database=0
# store.redis.password=
# store.redis.queryLimit=100

#Transaction rule configuration, only for the server
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
server.distributedLockExpireTime=10000
server.xaerNotaRetryTimeout=60000
server.session.branchAsyncQueueSize=5000
server.session.enableBranchAsyncRemove=false

#Transaction rule configuration, only for the client
client.rm.asyncCommitBufferLimit=10000
client.rm.lock.retryInterval=10
client.rm.lock.retryTimes=30
client.rm.lock.retryPolicyBranchRollbackOnConflict=true
client.rm.reportRetryCount=5
client.rm.tableMetaCheckEnable=true
client.rm.tableMetaCheckerInterval=60000
client.rm.sqlParserType=druid
client.rm.reportSuccessEnable=false
client.rm.sagaBranchRegisterEnable=false
client.rm.sagaJsonParser=fastjson
client.rm.tccActionInterceptorOrder=-2147482648
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
client.tm.defaultGlobalTransactionTimeout=60000
client.tm.degradeCheck=false
client.tm.degradeCheckAllowTimes=10
client.tm.degradeCheckPeriod=2000
client.tm.interceptorOrder=-2147482648
client.undo.dataValidation=true
client.undo.logSerialization=jackson
client.undo.onlyCareUpdateColumns=true
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
client.undo.logTable=undo_log
client.undo.compress.enable=true
client.undo.compress.type=zip
client.undo.compress.threshold=64k

#For TCC transaction mode
tcc.fence.logTableName=tcc_fence_log
tcc.fence.cleanPeriod=1h

#Log rule configuration, for client and server
log.exceptionRate=100

#Metrics configuration, only for the server
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898

#For details about configuration items, see https://seata.io/zh-cn/docs/user/configurations.html
#Transport configuration, for client and server
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableTmClientBatchSendRequest=false
transport.enableRmClientBatchSendRequest=true
transport.enableTcServerBatchSendResponse=false
transport.rpcRmRequestTimeout=30000
transport.rpcTmRequestTimeout=30000
transport.rpcTcRequestTimeout=30000
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
transport.serialization=seata
transport.compressor=none', '7073da8cdac5e0c35c4474f32e00bd70', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', 'dev', 'seata配置文件', NULL, NULL, 'properties', NULL, ''),
(14, 'ruoyi-snailjob-server.yml', 'DEFAULT_GROUP', 'spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${datasource.job.url}
    username: ${datasource.job.username}
    password: ${datasource.job.password}
    hikari:
      connection-timeout: 30000
      validation-timeout: 5000
      minimum-idle: 10
      maximum-pool-size: 20
      idle-timeout: 600000
      max-lifetime: 900000
      keepaliveTime: 30000
  cloud:
    nacos:
      discovery:
        metadata:
          # 解决 er 服务有 context-path 无法监控问题
          management.context-path: ${server.servlet.context-path}/actuator
          # 监控账号密码
          username: ruoyi
          userpassword: 123456

# snail-job 服务端配置
snail-job:
  # 服务端节点IP(默认按照`NetUtil.getLocalIpStr()`)
  server-host:
  # 服务端netty的端口号
  server-port: 17888
  # 合并日志默认保存天数
  merge-Log-days: 1
  # 合并日志默认的条数
  merge-Log-num: 500
  # 配置每批次拉取重试数据的大小
  retry-pull-page-size: 100
  # 配置日志保存时间（单位：天）
  log-storage: 7
  # bucket的总数量
  bucket-total: 128
  # Dashboard 任务容错天数
  summary-day: 7
  # 配置负载均衡周期时间
  load-balance-cycle-time: 10
  # 重试任务拉取的并行度
  retry-max-pull-parallel: 2', 'e017acb47e00bca42c922b9216f3272c', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', 'dev', 'SJ定时任务控制台', NULL, NULL, 'yaml', NULL, ''),

(101, 'application-common.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '通用配置基础配置', NULL, NULL, 'yaml', NULL, ''),
(102, 'datasource.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '数据源配置', NULL, NULL, 'yaml', NULL, ''),
(103, 'ruoyi-gateway.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '网关模块', NULL, NULL, 'yaml', NULL, ''),
(104, 'ruoyi-auth.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '认证中心', NULL, NULL, 'yaml', NULL, ''),
(105, 'ruoyi-monitor.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '监控中心', NULL, NULL, 'yaml', NULL, ''),
(106, 'ruoyi-system.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '系统模块', NULL, NULL, 'yaml', NULL, ''),
(107, 'ruoyi-gen.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '代码生成', NULL, NULL, 'yaml', NULL, ''),
(108, 'ruoyi-job.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '定时任务', NULL, NULL, 'yaml', NULL, ''),
(109, 'ruoyi-resource.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '文件服务', NULL, NULL, 'yaml', NULL, ''),
(110, 'ruoyi-workflow.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '工作流服务', NULL, NULL, 'yaml', NULL, ''),
(112, 'seata-server.properties', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', 'prod', 'seata配置文件', NULL, NULL, 'properties', NULL, ''),
(114, 'ruoyi-snailjob-server.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', 'prod', 'SJ定时任务控制台', NULL, NULL, 'yaml', NULL, '');
-- ============================================
-- 此处向后是业务
-- ============================================
insert into config_info(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) values
(15, 'guli-product.yml', 'DEFAULT_GROUP', 'spring:
  datasource:
    dynamic:
      # 主数据源
      primary: master
      datasource:
        master:
          type: ${spring.datasource.type}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: ${datasource.system-master.url}
          username: ${datasource.system-master.username}
          password: ${datasource.system-master.password}', 'e5a7b3c9d1f8a2b4e6c8d0f2a4b6c8d0', '2026-08-01 09:40:00', '2026-08-01 09:40:00', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '商城-商品模块', NULL, NULL, 'yaml', NULL, ''),
(115, 'guli-product.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2026-08-01 09:40:00', '2026-08-01 09:40:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '商城-商品模块', NULL, NULL, 'yaml', NULL, '');



/******************************************/
/*   表名称 = config_info  since 2.5.0                */
/******************************************/
CREATE TABLE `config_info_gray` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id` varchar(255) NOT NULL COMMENT 'data_id',
    `group_id` varchar(128) NOT NULL COMMENT 'group_id',
    `content` longtext NOT NULL COMMENT 'content',
    `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
    `src_user` text COMMENT 'src_user',
    `src_ip` varchar(100) DEFAULT NULL COMMENT 'src_ip',
    `gmt_create` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_create',
    `gmt_modified` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_modified',
    `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
    `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
    `gray_name` varchar(128) NOT NULL COMMENT 'gray_name',
    `gray_rule` text NOT NULL COMMENT 'gray_rule',
    `encrypted_data_key` varchar(256) NOT NULL DEFAULT '' COMMENT 'encrypted_data_key',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfogray_datagrouptenantgray` (`data_id`,`group_id`,`tenant_id`,`gray_name`),
    KEY `idx_dataid_gmt_modified` (`data_id`,`gmt_modified`),
    KEY `idx_gmt_modified` (`gmt_modified`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='config_info_gray';

/******************************************/
/*   表名称 = config_tags_relation         */
/******************************************/
CREATE TABLE `config_tags_relation` (
    `id` bigint(20) NOT NULL COMMENT 'id',
    `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
    `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
    `data_id` varchar(255) NOT NULL COMMENT 'data_id',
    `group_id` varchar(128) NOT NULL COMMENT 'group_id',
    `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
    `nid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
    PRIMARY KEY (`nid`),
    UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   表名称 = group_capacity               */
/******************************************/
CREATE TABLE `group_capacity` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
    `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
    `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
    `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
    `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   表名称 = his_config_info              */
/******************************************/
CREATE TABLE `his_config_info` (
    `id` bigint(20) unsigned NOT NULL COMMENT 'id',
    `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
    `data_id` varchar(255) NOT NULL COMMENT 'data_id',
    `group_id` varchar(128) NOT NULL COMMENT 'group_id',
    `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
    `content` longtext NOT NULL COMMENT 'content',
    `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user` text COMMENT 'source user',
    `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
    `op_type` char(10) DEFAULT NULL COMMENT 'operation type',
    `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
    `encrypted_data_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '密钥',
    `publish_type` varchar(50)  DEFAULT 'formal' COMMENT 'publish type gray or formal',
    `gray_name` varchar(50)  DEFAULT NULL COMMENT 'gray name',
    `ext_info`  longtext DEFAULT NULL COMMENT 'ext info',
    PRIMARY KEY (`nid`),
    KEY `idx_gmt_create` (`gmt_create`),
    KEY `idx_gmt_modified` (`gmt_modified`),
    KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';


/******************************************/
/*   表名称 = tenant_capacity              */
/******************************************/
CREATE TABLE `tenant_capacity` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
    `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
    `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
    `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
    `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';


CREATE TABLE `tenant_info` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `kp` varchar(128) NOT NULL COMMENT 'kp',
    `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
    `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
    `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
    `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
    `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

insert into tenant_info(id, kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) values
(1, '1', 'dev', 'dev', '开发环境', NULL, 1641741261189, 1641741261189),
(2, '1', 'prod', 'prod', '生产环境', NULL, 1641741270448, 1641741287236);

CREATE TABLE `users` (
    `username` varchar(50) NOT NULL PRIMARY KEY COMMENT 'username',
    `password` varchar(500) NOT NULL COMMENT 'password',
    `enabled` boolean NOT NULL COMMENT 'enabled'
);

CREATE TABLE `roles` (
    `username` varchar(50) NOT NULL COMMENT 'username',
    `role` varchar(50) NOT NULL COMMENT 'role',
    UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE `permissions` (
    `role` varchar(50) NOT NULL COMMENT 'role',
    `resource` varchar(128) NOT NULL COMMENT 'resource',
    `action` varchar(8) NOT NULL COMMENT 'action',
    UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);

INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);

INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
