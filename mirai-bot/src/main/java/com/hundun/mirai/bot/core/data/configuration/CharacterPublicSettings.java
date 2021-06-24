package com.hundun.mirai.bot.core.data.configuration;
/**
 * @author hundun
 * Created on 2021/06/23
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CharacterPublicSettings {
    List<String> listenWeiboUids = new ArrayList<>(0);
    Map<String, String> hourlyChats = new HashMap<>(0);
}
