### 1. 环境准备

- java 11
- mongoDB (以默认配置在本地启动)
- [mirai-console](https://github.com/mamoe/mirai/blob/dev/docs/UserManual.md)

### 2. 下载本项目制品

解压后得到：
```
+-- hundun.zacafleetbot.router-XXX.mirai.jar
+-- config
|   +-- hundun.zacafleetbot.router
|   |   +-- private-settings-template.json
|   |   +-- public-settings.json
+-- data
|   +-- hundun.zacafleetbot.router
|   |   +-- images
|   |   +-- voices
|   |   +-- quiz
|   |   |   +-- 略
```

config、data合并至mirai-console的同名文件夹。hundun.zacafleetbot.router-XXX.mirai.jar是插件本体，放入plugins。

### 3. 配置账号和群

1. 位于`config文件夹`：把`private-settings-template.json`重命名为`private-settings.json`。修改`private-settings.json`。包括：bot账号密码，每个群的群号和使用的角色，其他私密数据。[详细说明](settings详细说明.md)
2. （可选）位于`config文件夹`：修改`public-settings.json`。包括：每个角色订阅的微博账号。[详细说明](settings详细说明.md)
3. （可选）位于`data文件夹`：补充kancolle_game_data数据；修改一站到底题库（[一站到底详细说明](一站到底详细说明.md)）；

### 4. 启动和登录

启动mirai-console，在mirai-console里登录。