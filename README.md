# 程序员之家后端项目

 本项目为程序员之家平台的后端项目，[具体的项目说明地址](https://github.com/CXYZJ408/CXYZJ)

## 项目使用说明

1. 安装JDK、IDEA、MySql、redis、navcat等开发工具（如果没安装的话）
2. 下载本项目，如果要进行协同开发，则将本项目Fork到自己的git仓库中，不了解的[参考这里](https://www.cnblogs.com/schaepher/p/4933873.html)
3. 使用IDEA的推荐从IDEA中[导入项目](https://my.oschina.net/zjllovecode/blog/1591823)
4. 运行build.gradle安装相关依赖
5. 在mysql中导入[数据库SQL](https://github.com/CXYZJ408/CXYZJ/blob/master/cxyzj.sql)（也可以不导入，spring data 会自动建表，但建议导入，以防出现未知错误）
6. 配置`\src\main\resources\application-dev.yml`中的数据库地址，数据库用户名和密码
7. 如果前端项目运行的话，配置`/src/main/resources/config/Config.properties`中的绝对路径信息，将`E:/workspace/cxyzj_nuxt/`内容改为你前端项目存放的地址
8. [IDEA配置lombok](https://blog.csdn.net/qq_31496897/article/details/77970043)
9. 运行项目

## 项目进度

因为后端均为API的编写，所以此处进展可以参见[API文档](https://www.eolinker.com/#/share/index?shareCode=d9dbhT)，开发中的API有两种，一种是标星的为开发中，另一种未标星的为即将开发，其他的参见API前面的API状态信息。

## 项目目录结构及主要文件说明（从上至下）

**注：只说明`src\main\java\com\cxyzj\cxyzjback`下的目录文件**

- Bean：与数据库相对应的Bean配置文件还有些资源配置文件
- Catch：缓存文件（这个后期需要移除，缓存全部放到redis中）
- Config：spring security的配置文件目录
- Controller：RESTful API请求入口文件目录，此处的文件不要有任何的业务逻辑代码，仅需要提供一个API的入口即可，所有的业务逻辑代码放在Service目录文件中
- Data：用来存放返回给前端的数据结构，注：**不要将数据从数据库拿出后直接用Bean返回**，Data里面的字段，也就是变量名要和api接口的名字一样的，这里因为为了和数据库命名统一，所以使用的是下划线命名法，同时里面的所有类均需要实现Data接口中的getName方法，此处是为了配合Utils中的response使用，后面会有说明。
- Filter：过滤器，此处对前端传来的Token进行读取解析操作
- Repository：Spring Data JPA的配置文件目录，用于对数据库的操作
- Service：业务逻辑代码，里面的impl目录是具体的实现方法，interface为接口文件
- Utils：工具类
  - JWT：jwt的工具包
  - Constant：常量类，存放数据库的一些信息和一些全局常量信息
  - ListToMap：将List数据转为Map数据，一个工具类，因为jpa在批量查询数据的时候，对于id重复的数据会自动合并，所以需要做一个映射处理
  - Response：后端返回给前端的数据统一是`json`格式的string类型，转换使用`response`，如果请求成功，使用sendSuccess方法，如果失败则使用sendFailure方法，具体的使用方法，参见项目中已完成的API
  - SnowflakeIdGenerator：全局唯一性ID生成器，在Bean的配置文件中使用
  - status：返回给前端的状态码
  - Utils：工具包

## 项目开发要求

此处可以参见[接口开发文档](https://github.com/CXYZJ408/CXYZJ/blob/master/%E6%8E%A5%E5%8F%A3%E7%BC%96%E5%86%99%E8%AF%B4%E6%98%8E.md)

## 项目所使用的主要工具与框架一览(暂)

1. [Spring Boot](http://spring.io/projects/spring-boot)
2. [Spring Security](https://vincentmi.gitbooks.io/spring-security-reference-zh/content/1_introduction.html)
3. [Spring Data JPA](https://segmentfault.com/a/1190000015047290)
4. [JWT](http://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html)
5. [Gradle](https://juejin.im/post/5afe85d551882542ba080264)