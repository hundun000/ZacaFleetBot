package com.mirai.hundun.function;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.SessionId;
import com.mirai.hundun.cp.kcwiki.KancolleWikiService;
import com.mirai.hundun.cp.kcwiki.domain.dto.KcwikiShipDetail;
import com.mirai.hundun.cp.kcwiki.domain.dto.KcwikiShipStats;
import com.mirai.hundun.cp.kcwiki.domain.model.ShipInfo;
import com.mirai.hundun.cp.kcwiki.domain.model.ShipUpgradeLink;
import com.mirai.hundun.function.QuickSearchFunction.QuickSearchNode;
import com.mirai.hundun.parser.statement.QuickSearchStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;
import com.mirai.hundun.service.file.FileService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/05/19
 */
@Slf4j
@Component
public class KancolleWikiQuickSearchFunction implements IFunction {

    @Autowired
    BotService botService;
    
    @Autowired
    KancolleWikiService kancolleWikiService;
    
    @Autowired
    FileService fileService;
    
    @Override
    public boolean acceptStatement(SessionId sessionId, EventInfo event, Statement statement) {
        if (statement instanceof QuickSearchStatement) {
            QuickSearchStatement quickSearchStatement = (QuickSearchStatement)statement;
            MessageChainBuilder chainBuilder = new MessageChainBuilder();

            String shipNameArg = quickSearchStatement.getMainArg();

            ShipUpgradeLink upgradeLink = kancolleWikiService.getShipUpgradeLine(shipNameArg);
            if (upgradeLink != null && !upgradeLink.getUpgradeLinkIds().isEmpty()) {
                chainBuilder.add(new PlainText("\n\n"));
                
                StringBuilder urls = new StringBuilder();
                {
                    ShipInfo detail = upgradeLink.getShipDetails().get(upgradeLink.getUpgradeLinkIds().get(0));
                    String name = detail.getChineseName();
                    String urlEncodedArg;
                    try {
                        urlEncodedArg = URLEncoder.encode(
                                name,
                                java.nio.charset.StandardCharsets.UTF_8.toString()
                              );
                    } catch (UnsupportedEncodingException e) {
                        log.warn("Urlencolde fail: {}", name);
                        urlEncodedArg = name;
                    }
                    String wikiUrl = "https://zh.kcwiki.cn/wiki/" + urlEncodedArg;
                    urls.append(name).append(" ").append(wikiUrl).append("\n");
                }
                chainBuilder.add(new PlainText(urls.toString()));
                

                StringBuilder nameLink = new StringBuilder();
                for (int id : upgradeLink.getUpgradeLinkIds()) {
                    ShipInfo detail = upgradeLink.getShipDetails().get(id);
                    if (detail.getAfterLv() > 0) {
                        nameLink.append(detail.toSimpleText()).append("\n");
                        nameLink.append("-").append(detail.getAfterLv()).append("级->");
                    } else {
                        nameLink.append(detail.toSimpleText());
                    }
                }
                nameLink.append("\n");
                chainBuilder.add(new PlainText(nameLink.toString()));
                
                int firstId = upgradeLink.getUpgradeLinkIds().get(0);
                ShipInfo firstDetail = upgradeLink.getShipDetails().get(firstId);
                String fileId = String.valueOf(firstDetail.getId());
                File imageFile = fileService.downloadOrFromCache(fileId, kancolleWikiService);
                if (imageFile != null) {
                    ExternalResource externalResource = ExternalResource.create(imageFile);
                    Image image = botService.uploadImage(event.getGroupId(), externalResource);
                    chainBuilder.add(image);
                } else {
                    log.info("shipDetail no imageFile");
                }
                
            } else {
                log.info("no shipDetail");
            }
            
            botService.sendToGroup(event.getGroupId(), chainBuilder.build());
            return true;
  
        }
        return false;
    }
    
    
    private void detailToText(KcwikiShipDetail detail) {
        StringBuilder builder = new StringBuilder();
        builder.append(detail.getChinese_name()).append('\t').append(detail.getStype_name_chinese()).append('\n');
        KcwikiShipStats stats = detail.getStats();
//        builder.append("耐久(").append(stats.getTaik()[0]).append("~").append(stats.getTaik()[0]).append("), ");
//        builder.append("装甲(").append(stats.getSouk()[0]).append("~").append(stats.getTaik()[0]).append("), ");
//        builder.append("火力(").append(stats.getHoug()[0]).append("~").append(stats.getHoug()[0]).append("), ");
//        builder.append("雷装(").append(stats.getRaig()[0]).append("~").append(stats.getRaig()[0]).append("), ");
//        builder.append("对空(").append(stats.getTyku()[0]).append("~").append(stats.getTyku()[0]).append("), ");
//        builder.append("运(").append(stats.getLuck()[0]).append("~").append(stats.getLuck()[0]).append("), ");
//        builder.append("回避(").append(stats.getKaih()).append("), ");
//        builder.append("对潜(").append(stats.getTais()).append("), ");
//        builder.append("速度(").append(sokuText(stats.getSoku())).append("), ");
//        builder.append("射程(").append(lengText(stats.getLeng())).append("), ");
//        builder.append("装备格(").append(stats.getSlot_num()).append("), ");
//        builder.append("舰载机搭载(");
//        for (int num : stats.getMax_eq()) {
//            builder.append(num).append(",");
//        }
//        builder.setLength(builder.length() - 1);
//        builder.append(")\n");
        builder.append("改造等级(").append(detail.getAfter_lv()).append("), ");
    }
    
    public static String sokuText(int soku) {
        switch (soku) {
            case 0:
                return "陆上基地";
            case 5:
                return "低速";
            case 10:
                return "高速";
            default:
                return String.valueOf(soku);
        }
    }
    
    public static String lengText(int leng) {
        switch (leng) {
            case 0:
                return "无";
            case 1:
                return "短";
            case 2:
                return "中";
            case 3:
                return "长";
            case 4:
                return "超长";
            default:
                return String.valueOf(leng);
        }
    }

    @Override
    public List<SubFunction> getSubFunctions() {
        return null;
    }

}
