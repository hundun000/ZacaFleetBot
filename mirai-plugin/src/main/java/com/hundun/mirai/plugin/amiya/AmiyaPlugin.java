package com.hundun.mirai.plugin.amiya;

import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.plugin.router.RouterPlugin;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

/**
 * @author hundun
 * Created on 2021/06/16
 */
@Slf4j
public class AmiyaPlugin extends JavaPlugin {
    public static final AmiyaPlugin INSTANCE = new AmiyaPlugin();
    public AmiyaPlugin() {
        super(new JvmPluginDescriptionBuilder(
                "com.hundun.AmiyaPlugin", // name
                "0.0.1" // version
            )
            // .author("...")
            // .info("...")
            .build());
    }
    
    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        log.info("Amiya onLoad!");
    }
    
    
    
    @Override
    public void onEnable() {
        log.info("Amiya onEnable!");
    }
}
