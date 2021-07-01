### public-settings详细说明

```
{
    // 以角色为单位进行配置
    "characterIdToPublicSettings": {
        // 阿米娅的配置
        "CHARACTER_AMIYA": {
            // 订阅推送的微博uid
            "listenWeiboUids": ["6279793937","6441489862","7499841383"],
            // 报时时间和文本
            "hourlyChats": {
                "0": "呜哇！？正好0点！今天是，由阿米娅来担当助理的工作呢。我不会辜负大家的。",
                ……
                "23": "二十三点到了。有什么想喝的吗，博士？"
            }
        },
        // 欧根的配置
        "CHARACTER_PRINZ_EUGEN": {
            ……
        }
    }
}
```

### private-settings详细说明

```
{
    // 以bot为单位进行配置
    "botPrivateSettingsList":[
        {
            // 管理员账号
            "adminAccount": 11111111,
            // bot账号
            "botAccount": 22222222,
            // 实验性功能，目前保留原样即可
            "botPwd": null,
            
            // 实验性功能，目前保留原样即可
            "userTagConfigs":{
            },
            // 配置该bot对在群里的角色
            "groupConfigs":[
                // 在群号为33333333的群里，该bot的角色为阿米娅
                {
                    "groupId": 33333333,
                    // 仅是注释
                    "groupDescription": "这是群A",
                    // 对应阿米娅
                    "enableCharacters":[
                        "CHARACTER_AMIYA"
                    ]
                },
                // 在群号为44444444的群里，该bot的角色为欧根
                {
                    "groupId": 44444444,
                    "groupDescription": "这是群B",
                    // 对应欧根
                    "enableCharacters":[
                        "CHARACTER_PRINZ_EUGEN"  
                    ]
                }
            ]
        }
    ]
}

```