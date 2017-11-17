**项目说明** 
- hxyFrame-base-boot是一个Spring boot快速开发脚手架，是另一个开源项目hxyFrame的Spring boot基础框架版本。
技术使用shiro、Redis、MyBatis、quartz、jwt、swagger2、Vue2.x等，
完善的权限管理（菜单权限、按钮权限、数据权限）、用户管理、角色管理、API接口、日志管理、定时任务管理、云存储等模块。
感兴趣可以Watch、Start持续关注项目最新状态，加入QQ群：210315502 解决各种疑难杂证，丰富的学习资源。


**项目地址** 
**hxyFrame-base-boot地址** 
- oschina仓库：https://git.oschina.net/huangxianyuan/hxyFrame.git
- github仓库：https://github.com/huangxianyuan/hxyFrame-base-boot.git

**hxyFrame地址** 
- oschina仓库：https://git.oschina.net/huangxianyuan/hxyFrame.git
- github仓库：https://github.com/huangxianyuan/hxyFrame.git

 **演示环境** 
- 由于服务器资源有限，没有单独部署，下面是hxyFrame演示地址，页面效果一样。
- 项目demo地址(测试系统性能有限,如访问速度较慢,还请耐心等待)：http://47.95.234.81:8090/frame-admin 帐户/密码:hxy/a 

 **软件环境** 
- JDK1.8
- MySQL5.7.17
- Maven3.0
- Tomcat7.0
- redis 3.07

 **本地部署**
- 创建数据库hxyframe，数据库编码为UTF-8,导入doc/sql/hxyframe.sql脚本
- 修改conf/jdbc.properties文件，更改MySQL账号和密码
- redis服务,可以使用单机redis也可以配置哨兵集群模式，如果不会部署可以加群咨询：210315502
- solr服务器,可自行下载配置,也可以到官方qq群下载配置完善的
- 项目访问路径：http://localhost:8080/frame-admin/
