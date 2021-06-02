package com.hundun.mirai.bot.core;

import java.util.List;

import lombok.Data;

/**
 * 在配置里为账号打上标签，bot通过检查标签，走特殊流程
 * @author hundun
 * Created on 2021/05/13
 */
@Data
public class UserTagConfig {
    Long id;
    List<UserTag> tags;
}
