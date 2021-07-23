package hundun.zacafleetbot.mirai.botlogic.core.function;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.ProcessResult;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.SubFunctionCallStatement;
import hundun.zacafleetbot.mirai.botlogic.cp.pixiv.PixivService;
import lombok.Data;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/07/21
 */
@Component
public class PixivFunction extends BaseFunction {

    @Autowired
    PixivService pixivService;
    
    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList(SubFunction.PIXIV_RANDOM_IMAGE);
    }
    
    @Data
    private class SessionData {
        long lastUpdateTime;
        
        public SessionData() {
            this.lastUpdateTime = 0;
        }
    }
    
    Map<String, SessionData> sessionDataMap = new HashMap<>();

    @Override
    public ProcessResult process(BlackBoard blackBoard) {
        SessionId sessionId = blackBoard.getSessionId(); 
        EventInfo event = blackBoard.getEvent(); 
        Statement statement = blackBoard.getStatement();
        
        if (statement instanceof SubFunctionCallStatement) {
            SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
            if (subFunctionCallStatement.getSubFunction() == SubFunction.PIXIV_RANDOM_IMAGE) {
                MessageChainBuilder chainBuilder = new MessageChainBuilder();
                
                if (!sessionDataMap.containsKey(sessionId.id())) {
                    sessionDataMap.put(sessionId.id(), new SessionData());
                }
                SessionData sessionData = sessionDataMap.get(sessionId.id());
                
                boolean coldDown = System.currentTimeMillis() - sessionData.lastUpdateTime > 5 * 1000;
                if (coldDown) {
                    sessionData.lastUpdateTime = System.currentTimeMillis();
                    String fileId = UUID.randomUUID().toString();
                    File cacheFolder = console.resolveDataFileOfFileCache();
                    File imageFile = pixivService.fromCacheOrDownloadOrFromLocal(fileId, cacheFolder, null);
                    if (imageFile != null) {
                        ExternalResource externalResource = ExternalResource.create(imageFile);
                        Image image = console.uploadImage(event.getBot(), event.getGroupId(), externalResource);
                        chainBuilder.add(image);
                    } else {
                        chainBuilder.add("没找到图QAQ");
                    }
                    
                    console.sendToGroup(event.getBot(), event.getGroupId(), chainBuilder.build());
                    return new ProcessResult(this, true);
                }
            }
        }
        
        return new ProcessResult(this, false);
    }

}
