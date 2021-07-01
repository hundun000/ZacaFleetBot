### 1. 环境准备

- java 11
- mongoDB (以默认配置在本地启动)
- [mirai-consle](https://github.com/mamoe/mirai/blob/dev/docs/UserManual.md)
- 启动外部项目（可选）：一站到底答题项目需要在本地启动，暂不开源。即一站到底答题功能无法使用。

### 2. 下载本项目制品

解压后得到：
```
+-- plugins
|   +-- ZacaFleetBot.Router.mirai.jar
+-- config
|   +-- ZacaFleetBot.Router
|   |   +-- private-settings-template.json
|   |   +-- public-settings.json
+-- data
|   +-- ZacaFleetBot.Router
|   |   +-- images
|   |   +-- voices
```

config、data合并至mirai-console的同名文件夹。

### 3. 配置账号和群

1. 位于`config文件夹`：把`private-settings-template.json`重命名为`private-settings.json`。修改`private-settings.json`。包括：bot账号密码，每个群的群号和使用的角色，其他私密数据。[详细说明](settings详细说明.md)
2. （可选）位于`config文件夹`：修改`public-settings.json`。包括：每个角色订阅的微博账号。[详细说明](settings详细说明.md)
3. （可选）位于`data文件夹`：补充的数据（例如`kancolle_game_data`）

### 4. 启动和登录

启动mirai-console，在mirai-console里登录。