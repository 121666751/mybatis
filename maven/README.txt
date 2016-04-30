===============
关于settings.xml
===============

1. 此文件中的设置会指引Maven到公司内部的Maven镜像服务器上获取依赖包。

2. 将此文件复制到用户目录下的.m2目录中。

3. 不同系统的用户目录:

Windows:
    c:\Users\<用户名>\

Unix/Linux:
    /home/<用户名>/

Mac OS X:
    /Users/<用户名>/

=========
repo文件夹
=========

1. 此文件夹用来存放不受Maven中心仓库（https://repo.maven.apache.org/maven2）管理的第三方依赖包，例如PE、Oracle数据库JDBC驱动等。

2. 在powercore-parent中将此目录配置为Maven的本地库。

3. 在powercore-local-deps使用命令mvn install将依赖包安装到此文件夹中。

