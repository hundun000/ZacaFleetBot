package hundun.zacafleetbot.mirai.botlogic.core.function;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.ProcessResult;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.QuickSearchStatement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.KancolleWikiService;
import hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.domain.dto.KcwikiShipDetail;
import hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.domain.dto.KcwikiShipStats;
import hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.domain.model.ShipInfo;
import hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.domain.model.ShipUpgradeLink;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/05/19
 */
@Component
public class KancolleWikiQuickSearchFunction extends BaseFunction {

        
    
    @Autowired
    KancolleWikiService kancolleWikiService;

    
    @Override
    public ProcessResult process(BlackBoard blackBoard) {
        SessionId sessionId = blackBoard.getSessionId(); 
        EventInfo event = blackBoard.getEvent(); 
        Statement statement = blackBoard.getStatement();
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
                        console.getLogger().warning("Urlencolde fail: " + name);
                        urlEncodedArg = name;
                    }
                    String wikiUrl = "https://zh.kcwiki.cn/wiki/" + urlEncodedArg;
                    urls.append(name).append(" ").append(wikiUrl).append("\n");
                }
                chainBuilder.add(new PlainText(urls.toString()));
                

                StringBuilder nameLink = new StringBuilder();
                for (int i = 0; i < upgradeLink.getUpgradeLinkIds().size(); i++) {
                    int id = upgradeLink.getUpgradeLinkIds().get(i);
                    ShipInfo detail = upgradeLink.getShipDetails().get(id);
                    nameLink.append(detail.toSimpleText()).append("\n");
                    boolean hasNext = detail.getAfterLv() > 0 && i != upgradeLink.getUpgradeLinkIds().size() - 1;
                    if (hasNext) {
                        nameLink.append("-").append(detail.getAfterLv()).append("???->");
                    }
                }
                nameLink.append("\n");
                chainBuilder.add(new PlainText(nameLink.toString()));
                
                int firstId = upgradeLink.getUpgradeLinkIds().get(0);
                ShipInfo firstDetail = upgradeLink.getShipDetails().get(firstId);
                String fileId = String.valueOf(firstDetail.getId());
                File cacheFolder = console.resolveDataFileOfFileCache();
                File rawDataFolder = console.resolveDataFile(KancolleWikiService.kancolleGameDataSubFolder);
                File imageFile = kancolleWikiService.fromCacheOrDownloadOrFromLocal(fileId, cacheFolder, rawDataFolder);
                if (imageFile != null) {
                    ExternalResource externalResource = ExternalResource.create(imageFile);
                    Image image = console.uploadImage(event.getBot(), event.getGroupId(), externalResource);
                    chainBuilder.add(image);
                } else {
                    console.getLogger().info("shipDetail no imageFile");
                }
                
            } else {
                console.getLogger().info("no shipDetail");
            }
            
            if (!chainBuilder.isEmpty()) {
                console.sendToGroup(event.getBot(), event.getGroupId(), chainBuilder.build());
                return new ProcessResult(this, true);
            } 

        }
        return new ProcessResult(this, false);
    }
    
    
    private void detailToText(KcwikiShipDetail detail) {
        StringBuilder builder = new StringBuilder();
        builder.append(detail.getChinese_name()).append('\t').append(detail.getStype_name_chinese()).append('\n');
        KcwikiShipStats stats = detail.getStats();
//        builder.append("??????(").append(stats.getTaik()[0]).append("~").append(stats.getTaik()[0]).append("), ");
//        builder.append("??????(").append(stats.getSouk()[0]).append("~").append(stats.getTaik()[0]).append("), ");
//        builder.append("??????(").append(stats.getHoug()[0]).append("~").append(stats.getHoug()[0]).append("), ");
//        builder.append("??????(").append(stats.getRaig()[0]).append("~").append(stats.getRaig()[0]).append("), ");
//        builder.append("??????(").append(stats.getTyku()[0]).append("~").append(stats.getTyku()[0]).append("), ");
//        builder.append("???(").append(stats.getLuck()[0]).append("~").append(stats.getLuck()[0]).append("), ");
//        builder.append("??????(").append(stats.getKaih()).append("), ");
//        builder.append("??????(").append(stats.getTais()).append("), ");
//        builder.append("??????(").append(sokuText(stats.getSoku())).append("), ");
//        builder.append("??????(").append(lengText(stats.getLeng())).append("), ");
//        builder.append("?????????(").append(stats.getSlot_num()).append("), ");
//        builder.append("???????????????(");
//        for (int num : stats.getMax_eq()) {
//            builder.append(num).append(",");
//        }
//        builder.setLength(builder.length() - 1);
//        builder.append(")\n");
        builder.append("????????????(").append(detail.getAfter_lv()).append("), ");
    }
    
    public static String sokuText(int soku) {
        switch (soku) {
            case 0:
                return "????????????";
            case 5:
                return "??????";
            case 10:
                return "??????";
            default:
                return String.valueOf(soku);
        }
    }
    
    public static String lengText(int leng) {
        switch (leng) {
            case 0:
                return "???";
            case 1:
                return "???";
            case 2:
                return "???";
            case 3:
                return "???";
            case 4:
                return "??????";
            default:
                return String.valueOf(leng);
        }
    }

    @Override
    public List<SubFunction> getSubFunctions() {
        return null;
    }

}
