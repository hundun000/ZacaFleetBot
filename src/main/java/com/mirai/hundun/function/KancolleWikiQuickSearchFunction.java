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

            String arg = quickSearchStatement.getMainArg();
            String urlEncodedArg;
            try {
                urlEncodedArg = URLEncoder.encode(
                        arg,
                        java.nio.charset.StandardCharsets.UTF_8.toString()
                      );
            } catch (UnsupportedEncodingException e) {
                log.warn("Urlencolde fail: {}", arg);
                urlEncodedArg = arg;
            }
            String wikiUrl = "https://zh.kcwiki.cn/wiki/" + urlEncodedArg;
            chainBuilder.add(new PlainText(wikiUrl));
            
            String shipName = arg;
            KcwikiShipDetail shipDetail = kancolleWikiService.getShipDetail(shipName);
            if (shipDetail != null) {
                String fileId = String.valueOf(shipDetail.getId());
                File imageFile = fileService.downloadOrFromCache(fileId, kancolleWikiService);
                if (imageFile != null) {
                    ExternalResource externalResource = ExternalResource.create(imageFile);
                    Image image = botService.uploadImage(event.getGroupId(), externalResource);
                    chainBuilder.add(new PlainText("\n\n"));
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

    @Override
    public List<SubFunction> getSubFunctions() {
        return null;
    }

}
