package com.hundun.mirai.plugin.export;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundun.mirai.bot.core.BaseBotLogic;
import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.AppPublicSettings;
import com.hundun.mirai.bot.export.BotLogicOfCharacterRouterAsEventHandler;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.bot.helper.Utils;
import com.hundun.mirai.plugin.ConsoleAdapter;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.yamlkt.Yaml;
import net.mamoe.yamlkt.YamlMap;


/**
 * @author hundun
 * Created on 2021/06/02
 */
public class RouterPlugin extends MyPlugin {
    public static final RouterPlugin INSTANCE = new RouterPlugin(); // 可以像 Kotlin 一样静态初始化单例
    
    
    public RouterPlugin() {
        super(new JvmPluginDescriptionBuilder(
                "org.example.RouterPlugin", // name
                "1.0.0" // version
            )
            // .author("...")
            // .info("...")
            .build());
    }


    @Override
    protected BaseBotLogic getBotLogicImpl(
            IConsole console) throws Exception {
        return new BotLogicOfCharacterRouterAsEventHandler(console);
    }




}
