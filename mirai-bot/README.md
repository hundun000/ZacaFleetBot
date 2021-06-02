# 开发说明

## 程序架构

mirai-core + mongoDB + gradle

### bot

本项目只登录唯一的QQ账号，即创建唯一的bot，由BotService负责。

### Character 角色

为了支持该bot可以在不同Q群表现不同行为，使用【Character】区分。

例如，在明日方舟群，配置的角色为Amiya；在舰C群，配置的角色为PrinzEugen。

进一步的，可以为一个Q群配置多个角色。如果一个群里既有明日方舟玩家和舰C玩家，可以为该群配置Amiya+PrinzEugen。此时两个角色对输入的响应存在优先级甚至更复杂的调度策略（TODO）。

单例。Character无上下文。

### cp包

每一个第三方服务封装成为一个CpService。CpService与mirai无关，可脱离mirai运行。

### Function 功能

> 自己造了一遍轮子，才发现mirai-console里包含相同思路的实现。下文的Statement对应了net.mamoe.mirai.console.command。

连接CpService和Character。是Character总体功能的组成单元，即每个Character实际是在使用若干Function。

鉴于Function需要能被任意Character使用，所以只应有Character依赖Function，而Function不应依赖某个具体Character。

单例。若有需要，在内部自行实现区分不同session的上下文。

### SubFunction 功能

枚举。用于区分一个Function下个最小粒度的功能。例如“创建事项提醒”和“查询事项提醒”、“删除事项提醒”分别是RemiderFunction下的3个SubFunction。

### Statement 指令

原始输入MessageChain与Function解耦。原始输入MessageChain将被Character处理为一个Statement，正确地填充Statement的成员变量，Statement作为Function的输入。

Function只使用Statement（的成员变量），不关心原始输入。进一步的，甚至Function不关心原始输入是否来自Mira框架。

目的是实现如下功能：假设Amiya和PrinzEugen都使用查微博功能WeiboFuction，但是希望调用功能的语法不同，例如分别是“阿米娅 看看饼”和“欧根 查看镇守府情报”。

所以Amiya和PrinzEugen需要把上述两种原始输入处理成同样的Statement(subFunction=SubFunction.WEIBO_SHOW_LATEST)，再将其输入WeiboFuction。

### Parser 语法分析器

把MessageChain处理成Statement使用的是Parser。参考编译原理。但目前的Parser并没有预期的那么有效。

开发者可以自行实现任意一种把MessageChain处理成Statement的方式，替换parser。
