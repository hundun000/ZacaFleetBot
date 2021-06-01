# 使用说明

## 环境准备

- java 11
- mongoDB (以默认配置在本地启动)

## 启动外部项目（可选）

一站到底答题项目需要在本地启动，暂不开源。即一站到底答题功能无法使用。

## 配置账号和群

1. 在\src\main\resources里把private-settings-template.yml重命名为private-settings.yml（其会被gitignore）。
2. 修改private-settings.yml。包括：bot账号密码，每个群的群号和使用的角色，其他私密数据。
3. private-settings会在启动后打印。注意日志的保密或修改代码。

## 启动和登录

1. 启动项目。启动后可以以离线模式测试功能。
2. 使用登录接口/api/login，登录qq账号。

### 离线模式

用于调试。项目已启动但未登录qq账号，即为离线模式状态。此时可以调用测试接口，模拟qq消息发送给了角色，然后角色的回复会打印在日志里。