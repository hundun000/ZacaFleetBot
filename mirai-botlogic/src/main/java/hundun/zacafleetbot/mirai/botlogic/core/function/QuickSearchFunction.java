package hundun.zacafleetbot.mirai.botlogic.core.function;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.ProcessResult;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.QuickSearchStatement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;

/**
 * @author hundun
 * Created on 2021/05/18
 */
@Component
public class QuickSearchFunction extends BaseFunction {

    
    class QuickSearchNode {
        List<String> conditions;
        String answerTemplate;
        boolean argUrlencolde;
        List<String> defaultArgs = new ArrayList<>(0);
    }
    
    List<QuickSearchNode> nodes = new ArrayList<>();

    
    public QuickSearchFunction() {
        initNodes();
    }
    
    static final String ARG_PLACEHOLDER_START = "{ARG_";
    static final String ARG_PLACEHOLDER_END = "}";
    public void initNodes() {
        
        
        QuickSearchNode node;
        
        node = new QuickSearchNode();
        node.conditions = Arrays.asList("PRTS", "prts");
        node.answerTemplate = "http://prts.wiki/w/{ARG_0}";
        node.argUrlencolde = true;
        node.defaultArgs = Arrays.asList("首页");
        nodes.add(node);
        
        node = new QuickSearchNode();
        node.conditions = Arrays.asList("aog", "AOG","一图流");
        node.answerTemplate = "https://aog.wiki/";
        nodes.add(node);
        
        node = new QuickSearchNode();
        node.conditions = Arrays.asList("企鹅物流");
        node.answerTemplate = "https://penguin-stats.cn/";
        nodes.add(node);
        
        node = new QuickSearchNode();
        node.conditions = Arrays.asList("绿票一图流", "一图流绿票");
        node.answerTemplate = "https://hguandl.com/yituliu/yituliu.jsp";
        nodes.add(node);
        
        
        
    }
    
    
    
    
    
    
    @Override
    public ProcessResult process(BlackBoard blackBoard) {
        SessionId sessionId = blackBoard.getSessionId(); 
        EventInfo event = blackBoard.getEvent(); 
        Statement statement = blackBoard.getStatement();
        if (statement instanceof QuickSearchStatement) {
            QuickSearchStatement quickSearchStatement = (QuickSearchStatement)statement;
            for (QuickSearchNode node : nodes) {
                if (node.conditions.contains(quickSearchStatement.getMainArg())) {
                    String answer = node.answerTemplate;
                    
                    int placeholderStartIndex = answer.indexOf(ARG_PLACEHOLDER_START);
                    int placeholderEndIndex = answer.indexOf(ARG_PLACEHOLDER_END);
                    while (placeholderStartIndex > -1 && placeholderEndIndex > -1) {
                        String indexPart = node.answerTemplate.substring(placeholderStartIndex + ARG_PLACEHOLDER_START.length(), placeholderEndIndex);
                        String placeholderPart = ARG_PLACEHOLDER_START + indexPart + ARG_PLACEHOLDER_END;
                        int index = Integer.valueOf(indexPart);
                        String arg;
                        if (quickSearchStatement.getArgs().size() > index) {
                            arg = quickSearchStatement.getArgs().get(index);
                        } else if (node.defaultArgs.size() > index) {
                            arg = node.defaultArgs.get(index);
                        } else {
                            arg = "";
                        }
                        if (node.argUrlencolde) {
                            try {
                                arg = URLEncoder.encode(
                                        arg,
                                        java.nio.charset.StandardCharsets.UTF_8.toString()
                                      );
                            } catch (UnsupportedEncodingException e) {
                                console.getLogger().warning("Urlencolde fail: " + arg);
                            }
                        }
                        
                        answer = answer.replace(placeholderPart, arg);
                        
                        placeholderStartIndex = answer.indexOf(ARG_PLACEHOLDER_START);
                        placeholderEndIndex = answer.indexOf(ARG_PLACEHOLDER_END);
                    }
                    
                    
                    console.sendToGroup(event.getBot(), event.getGroupId(), answer);
                    return new ProcessResult(this, true);
                }
            }   
        }
        return new ProcessResult(this, false);
    }

    @Override
    public List<SubFunction> getSubFunctions() {
        return null;
    }

}
