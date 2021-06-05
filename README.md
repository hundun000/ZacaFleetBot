# 一、简介

bot功能：该bot在可在不同Q群扮演不同角色，只提供指定范围的功能（包含该角色的特色功能）。目标同时为多个群提供差异化服务，易配置，易裁剪或拓展。

有两种使用方式，选这其中一种即可使用：

- 方式一：作为独立应用使用
- 方式二：作为mirai-console-plugin使用【开发中】

外部服务：微博api、企鹅物流api、一站到底答题项目

# 二、bot功能详细介绍

## 1. 现有角色

### 阿米娅

《明日方舟》游戏角色

- 推送微博： 明日方舟、朝陇山
- 企鹅物流数据查询
- 事项提醒： 预设的整点报时、用户新增定时提醒
- 闲聊： 戳一戳、提醒还不能下班……
- 快速查询： PRTS网页链接、一图流网页链接……
- 一站到底答题（暂不开放）

### 欧根

《舰队收藏》游戏角色

- 推送微博： 海色镇守府
- 事项提醒： 预设的整点报时、用户新增定时提醒
- 快速查询： kcwiki网页链接

### ZACA娘

原创角色

- 推送微博： 华工ZACA动漫协会
- 日文小工具： 日文汉字注音
- 一站到底答题（暂不开放）

### 猫猫

原创角色

- 一站到底答题（暂不开放）

## 2. 指令

如前文所述，每个角色有特定的可用指令范围。下面列出的所有指令，并非任意角色都能使用。

本章的指令示例将以如下格式给出：
> 这是一句指令示例

### 2.1 第一类指令——调用某个子指令

语法：  
<角色名><子指令昵称> <指令参数列表>
或  
<角色名> <子指令昵称> <指令参数列表>

为了加强角色扮演感，对于同一个子指令，不同的角色可能使用不同的“子指令昵称”（可能随本项目更新而改变）。

### 2.1.1 微博相关指令

* 查看最近一条微博的发布时间

对于 *<角色名>: 阿米娅* ,有 *<子指令昵称>: 看看饼*

> 阿米娅看看饼

对于 *<角色名>: 欧根* ,有 *<子指令昵称>: 查看镇守府情报*

> 欧根查看镇守府情报

### 2.1.2 企鹅物流数据相关指令

### 查询物品掉率

查看某个物品的在所有地图里掉落率最高的几项。

*<子指令昵称>: 查掉率*  
*<指令参数列表>: 物品名*

> 阿米娅查掉率 固源岩

### 查询作战

查看某个作战的理智消耗，掉落等信息。

*<子指令昵称>: 查作战*  
*<指令参数列表>: 作战id*

> 阿米娅查作战 1-7

### 刷新缓存

让bot拉取最新的企鹅物流信息数据。

*<子指令昵称>: 更新企鹅物流*  

> 阿米娅更新企鹅物流

### 2.1.3 事项提醒相关指令

### 创建提醒

提醒任务指的是在满足时间条件的时候发送指定消息。

*<子指令昵称>: 创建提醒*  
*<指令参数列表>[0]: 时间条件-月。-1表示不限制。*  
*<指令参数列表>[1]: 时间条件-日。-1表示不限制。*  
*<指令参数列表>[2]: 时间条件-星期数。-1表示不限制。*  
*<指令参数列表>[3]: 时间条件-时。-1表示不限制。*  
*<指令参数列表>[4]: 时间条件-分。-1表示不限制。*  
*<指令参数列表>[5]: 时间条件-执行次数。-1表示不限制。*  
*<指令参数列表>[6]: 消息内容*  

> 阿米娅创建提醒 5 1 -1 9 0 1 这条消息将在5月1日（不论是星期几）9点0分时被bot自动发送。执行一次后失效。
> 阿米娅创建提醒 -1 -1 7 22 30 -1 这条消息将在周日（不论是几月几日）22点30分时被bot自动发送。可执行无限次。

### 查看提醒

查看已创建的提醒任务。

*<子指令昵称>: 查看提醒*   

> 阿米娅查看提醒

### 移除提醒

移除指定id的提醒任务。

*<子指令昵称>: 移除提醒*   
*<指令参数列表>: 提醒id。由查看提醒得到。*  

> 阿米娅移除提醒 29b88b93-89e2-4a3f-8ff2-b35ab13672d3

### 2.2 第二类指令——快速查询

语法：  
<参数>.

语法尽量简短，用于每个群里最常用到的查询。

### 2.2.1 阿米娅的快速搜索

| <参数> | 回复 |
|---------|--------|
| PRTS 或 prts | PRTS首页 |
| 一图流 | 一图流网站 |
| 绿票一图流 |绿票一图流网站 |
| 企鹅物流 | 企鹅物流首页 |
| 其他情况 | 在PRTS里搜索该关键词 |

> prts.
> 海猫络合物.

### 2.2.2 欧根的快速搜索

| <参数> | 回复 |
|---------|--------|
| 任意 | 在kcwiki里搜索该关键词 |

> 吹雪.

## 3. 闲聊

当群员的发言不满足该角色的任意一个指令语法，即为一句闲聊。角色也会在满足条件的情况下做出回复。

### 3.1 复读机

当群里连续3句发言相同时（包括发送相同表情/图片），角色也会回复一次该发言。

### 3.2 阿米娅的闲聊

- 当群员发言里包含“下班”，根据是否是工作时间（周一至周五9点至17点），阿米娅会做不同回复。
- 当群员发言里包含“damedane”，阿米娅会播放音频。
- 戳一戳阿米娅或特定群员，阿米娅会发送特定的图片。

### 3.3 欧根的闲聊

- 当群员发言里包含“噗噗”，欧根会发送特定的图片。

## 4. 非主动功能

指的是不由用户发言触发的功能。

- 微博推送
- 预设的整点报时

# 三、使用说明

## 方式一：作为独立应用使用

### 1. 环境准备

- java 11
- mongoDB (以默认配置在本地启动)

### 2. 启动外部项目（可选）

一站到底答题项目需要在本地启动，暂不开源。即一站到底答题功能无法使用。

### 3. 配置账号和群

1. 在\src\main\resources里把private-settings-template.yml重命名为private-settings.yml（其会被gitignore）。
2. 修改private-settings.yml。包括：bot账号密码，每个群的群号和使用的角色，其他私密数据。
3. private-settings会在启动后打印。注意日志的保密或修改代码。
4. （可选）修改public-settings.yml。包括：每个角色订阅的微博账号。

### 4. 启动和登录

1. 启动项目。启动后可以以离线模式测试功能。
2. 使用登录接口/api/login，登录qq账号。

### 5. 离线模式

用于调试。项目已启动但未登录qq账号，即为离线模式状态。此时可以调用测试接口，模拟qq消息发送给了角色，然后角色的回复会打印在日志里。

## 方式二：作为mirai-console-plugin使用【开发中】

// TODO

# 四、开发说明

## 架构简介

子项目mirai-bot: 下称“bot核心包”，提供上述bot功能。

> 通过IConsole来对接他的使用者，不关心IConsole的实现。

子项目mirai-server: 使用“bot核心包”，使用mirai-core，输出SpringBootApplication。启动后可连接QQ上线bot。

> 包含IConsole的第一种实现。

子项目mirai-plugin: 使用“bot核心包”，输出mirai-console-plugin。需通过mirai-console启动。

> 包含IConsole的第二种实现。

[![](https://mermaid.ink/img/eyJjb2RlIjoiZmxvd2NoYXJ0IExSXG4gICAgY2xhc3NEZWYgY29yZWhpZ2hsaWdodCBmaWxsOiNmOTYsc3Ryb2tlOiMzMzMsc3Ryb2tlLXdpZHRoOjNweDtcbiAgICBjbGFzc0RlZiBoaWdobGlnaHQgZmlsbDojZjg4LHN0cm9rZTojMzMzLHN0cm9rZS13aWR0aDozcHhcbiAgICBzdWJncmFwaCBtaXJhaWNvbnNvbGUgW1wiTWlyYWkgQ29uc29sZVwiXVxuICAgICAgICBtaXJhaWNvbnNvbGViYWNrZW5kKFwiQmFja0VuZFwiKVxuICAgIGVuZFxuXG4gICAgc3ViZ3JhcGggaHVuZHVubWlyYWlhbGwgW1wiaHVuZHVuLW1pcmFpLXByb2plY3RzXCJdXG4gICAgICAgIHN1YmdyYXBoIHNwcmluZ0FwcCBbXCJtaXJhaS1zZXJ2ZXLlrZDpobnnm65cIl1cbiAgICAgICAgICAgIGh1bmR1bnNlcnZlcihcIlNwcmluZ0FwcGxpY2F0aW9uXCIpOjo6Y29yZWhpZ2hsaWdodFxuICAgICAgICAgICAgbWlyYWljb3JlamFyKFwiaW1wbGVtZW50YXRpb24obWlyYWktY29yZS1qdm0pXCIpXG4gICAgICAgIGVuZFxuICAgICAgICBzdWJncmFwaCBodW5kdW5ib3QgW1wibWlyYWktYm905a2Q6aG555uuXCJdXG4gICAgICAgICAgICBodW5kdW5ib3Rjb3JlKFwiYm905qC45b-D5YyFXCIpOjo6Y29yZWhpZ2hsaWdodFxuICAgICAgICAgICAgbWlyYWljb3JlYXBpKFwiaW1wbGVtZW50YXRpb24obWlyYWktY29yZS1hcGktanZtKVwiKVxuICAgICAgICBlbmRcbiAgICAgICAgXG4gICAgICAgIFxuICAgICAgICBzdWJncmFwaCBjb25zb2xlcGx1Z2lucyBbXCJtaXJhaS1wbHVnaW7lrZDpobnnm65cIl1cbiAgICAgICAgICAgIGh1bmR1bnBsdWdpbihcIm1pcmFpLWNvbnNvbGUtcGx1Z2luXCIpOjo6Y29yZWhpZ2hsaWdodCAtLT4gbWlyYWljb25zb2xlYmFja2VuZFxuICAgICAgICAgICAgbWlyYWljb25zb2xlamFyKFwiY29tcGlsZU9ubHkobWlyYWktY29uc29sZSlcIilcbiAgICAgICAgZW5kXG4gICAgICAgIG1pcmFpY29yZWFwaSAtLT4gfOaPkOS-m-WfuuehgOWKn-iDvXxodW5kdW5ib3Rjb3JlXG4gICAgICAgIGh1bmR1bmJvdGNvcmUgLS0-IHzmj5Dkvptib3Tlip_og718aHVuZHVucGx1Z2luXG4gICAgICAgIGh1bmR1bmJvdGNvcmUgLS0-IHzmj5Dkvptib3Tlip_og718aHVuZHVuc2VydmVyXG5cbiAgICAgICAgXG4gICAgZW5kXG4gICAgY29tbWVudChcIuihqOekuuacrOmhueebruWItuWTgVwiKTo6OmNvcmVoaWdobGlnaHRcbiAgICBjb21tZW50MihcIuihqOekuuWklumDqOS-nei1llwiKVxuIiwibWVybWFpZCI6IntcbiAgXCJ0aGVtZVwiOiBcImRlZmF1bHRcIlxufSIsInVwZGF0ZUVkaXRvciI6ZmFsc2UsImF1dG9TeW5jIjp0cnVlLCJ1cGRhdGVEaWFncmFtIjpmYWxzZX0)](https://mermaid-js.github.io/mermaid-live-editor/edit/edit#eyJjb2RlIjoiZmxvd2NoYXJ0IExSXG4gICAgY2xhc3NEZWYgY29yZWhpZ2hsaWdodCBmaWxsOiNmOTYsc3Ryb2tlOiMzMzMsc3Ryb2tlLXdpZHRoOjNweDtcbiAgICBjbGFzc0RlZiBoaWdobGlnaHQgZmlsbDojZjg4LHN0cm9rZTojMzMzLHN0cm9rZS13aWR0aDozcHhcbiAgICBzdWJncmFwaCBtaXJhaWNvbnNvbGUgW1wiTWlyYWkgQ29uc29sZVwiXVxuICAgICAgICBtaXJhaWNvbnNvbGViYWNrZW5kKFwiQmFja0VuZFwiKVxuICAgIGVuZFxuXG4gICAgc3ViZ3JhcGggaHVuZHVubWlyYWlhbGwgW1wiaHVuZHVuLW1pcmFpLXByb2plY3RzXCJdXG4gICAgICAgIHN1YmdyYXBoIHNwcmluZ0FwcCBbXCJtaXJhaS1zZXJ2ZXLlrZDpobnnm65cIl1cbiAgICAgICAgICAgIGh1bmR1bnNlcnZlcihcIlNwcmluZ0FwcGxpY2F0aW9uXCIpOjo6Y29yZWhpZ2hsaWdodFxuICAgICAgICAgICAgbWlyYWljb3JlamFyKFwiaW1wbGVtZW50YXRpb24obWlyYWktY29yZS1qdm0pXCIpXG4gICAgICAgIGVuZFxuICAgICAgICBzdWJncmFwaCBodW5kdW5ib3QgW1wibWlyYWktYm905a2Q6aG555uuXCJdXG4gICAgICAgICAgICBodW5kdW5ib3Rjb3JlKFwiYm905qC45b-D5YyFXCIpOjo6Y29yZWhpZ2hsaWdodFxuICAgICAgICAgICAgbWlyYWljb3JlYXBpKFwiaW1wbGVtZW50YXRpb24obWlyYWktY29yZS1hcGktanZtKVwiKVxuICAgICAgICBlbmRcbiAgICAgICAgXG4gICAgICAgIFxuICAgICAgICBzdWJncmFwaCBjb25zb2xlcGx1Z2lucyBbXCJtaXJhaS1wbHVnaW7lrZDpobnnm65cIl1cbiAgICAgICAgICAgIGh1bmR1bnBsdWdpbihcIm1pcmFpLWNvbnNvbGUtcGx1Z2luXCIpOjo6Y29yZWhpZ2hsaWdodCAtLT4gbWlyYWljb25zb2xlYmFja2VuZFxuICAgICAgICAgICAgbWlyYWljb25zb2xlamFyKFwiY29tcGlsZU9ubHkobWlyYWktY29uc29sZSlcIilcbiAgICAgICAgZW5kXG4gICAgICAgIG1pcmFpY29yZWFwaSAtLT4gfOaPkOS-m-WfuuehgOWKn-iDvXxodW5kdW5ib3Rjb3JlXG4gICAgICAgIGh1bmR1bmJvdGNvcmUgLS0-IHzmj5Dkvptib3Tlip_og718aHVuZHVucGx1Z2luXG4gICAgICAgIGh1bmR1bmJvdGNvcmUgLS0-IHzmj5Dkvptib3Tlip_og718aHVuZHVuc2VydmVyXG5cbiAgICAgICAgXG4gICAgZW5kXG4gICAgY29tbWVudChcIuihqOekuuacrOmhueebruWItuWTgVwiKTo6OmNvcmVoaWdobGlnaHRcbiAgICBjb21tZW50MihcIuihqOekuuWklumDqOS-nei1llwiKVxuIiwibWVybWFpZCI6IntcbiAgXCJ0aGVtZVwiOiBcImRlZmF1bHRcIlxufSIsInVwZGF0ZUVkaXRvciI6ZmFsc2UsImF1dG9TeW5jIjp0cnVlLCJ1cGRhdGVEaWFncmFtIjpmYWxzZX0)


## 子项目mirai-bot

主要使用的库：mirai-core + mongoDB + openfeign

### java包/java基类说明

#### Character 角色

为了支持该bot可以在不同Q群表现不同行为，使用【Character】区分。

例如，在明日方舟群，配置的角色为Amiya；在舰C群，配置的角色为PrinzEugen。

进一步的，可以为一个Q群配置多个角色。如果一个群里既有明日方舟玩家和舰C玩家，可以为该群配置Amiya+PrinzEugen。此时两个角色对输入的响应存在优先级甚至更复杂的调度策略（TODO）。

单例。Character无上下文。

#### cp包

每一个第三方服务封装成为一个CpService。CpService与mirai无关，可脱离mirai运行。

#### Function 功能

> 自己造了一遍轮子，才发现mirai-console里包含相同思路的实现。下文的Statement对应了net.mamoe.mirai.console.command。

连接CpService和Character。是Character总体功能的组成单元，即每个Character实际是在使用若干Function。

鉴于Function需要能被任意Character使用，所以只应有Character依赖Function，而Function不应依赖某个具体Character。

单例。若有需要，在内部自行实现区分不同session的上下文。

#### SubFunction 功能

枚举。用于区分一个Function下个最小粒度的功能。例如“创建事项提醒”和“查询事项提醒”、“删除事项提醒”分别是RemiderFunction下的3个SubFunction。

#### Statement 指令

原始输入MessageChain与Function解耦。原始输入MessageChain将被Character处理为一个Statement，正确地填充Statement的成员变量，Statement作为Function的输入。

Function只使用Statement（的成员变量），不关心原始输入。进一步的，甚至Function不关心原始输入是否来自Mira框架。

目的是实现如下功能：假设Amiya和PrinzEugen都使用查微博功能WeiboFuction，但是希望调用功能的语法不同，例如分别是“阿米娅 看看饼”和“欧根 查看镇守府情报”。

所以Amiya和PrinzEugen需要把上述两种原始输入处理成同样的Statement(subFunction=SubFunction.WEIBO_SHOW_LATEST)，再将其输入WeiboFuction。

#### Parser 语法分析器

把MessageChain处理成Statement使用的是Parser。参考编译原理。但目前的Parser并没有预期的那么有效。

开发者可以自行实现任意一种把MessageChain处理成Statement的方式，替换parser。


## 子项目mirai-server

主要使用的库：SpringBoot

> SpringConsole是IConsole的一种实现。该子项目从yml加载“bot核心包”启动所需的配置，提供了一些能直接调用Character或CpService的方法的http-api用于调试。

## 子项目mirai-plugin

> ConsoleAdapter是IConsole的一种实现，在“bot核心包”与mirai-console之间转发输入/输出。以mirai-console-plugin推荐的方式加载“bot核心包”启动所需的配置。