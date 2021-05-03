## 程序架构

mirai-core + springboot + mongoDB + gradle

### bot

本项目只登录唯一的QQ账号，创建唯一的bot，由BotService负责。

### Character 角色

为了支持该bot可以在不同Q群表现不同行为，使用【Character】区分。拟人化地理解，该bot在可在不同Q群扮演不同角色，只提供该角色的功能，使用在该角色的台词。

例如，在明日方舟群，配置的角色为Amiya；在舰C群，配置的角色为PrinzEugen。

进一步的，可以为一个Q群配置多个角色。如果一个群里既有明日方舟玩家和舰C玩家，可以为该群配置Amiya+PrinzEugen。此时两个角色对输入的响应存在优先级甚至更复杂的调度策略（待优化）。

### cp包

每一个第三方服务封装成为一个CpService。CpService与mirai无关，可脱离mirai运行。

### Function 功能

连接CpService和Character。是Character总体功能的组成单元，即每个Character实际是在使用若干Function。

鉴于Function需要能被任意Character使用，所以只应有Character依赖Function，而Function不应依赖某个具体Character。

### Statement 指令

原始输入MessageChain将被Character处理为一个Statement，Statement作为Function的输入。

目的是实现如下功能：假设Amiya和PrinzEugen都使用查微博功能WeiboFuction，但是希望调用功能的语法不同，例如分别是“阿米娅 看看饼”和“欧根 查看镇守府情报”。

所以Amiya和PrinzEugen需要把原始输入处理成同样的Statement(type=WEIBO_QUERY)，再将其输入WeiboFuction。

### Parser 语法分析器

把MessageChain处理成Statement使用的是Parser。参考编译原理。