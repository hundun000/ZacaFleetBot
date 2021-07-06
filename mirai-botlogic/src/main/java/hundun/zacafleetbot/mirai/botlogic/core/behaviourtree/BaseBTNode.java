package hundun.zacafleetbot.mirai.botlogic.core.behaviourtree;
/**
 * @author hundun
 * Created on 2021/07/06
 */

import java.util.LinkedList;
import java.util.List;

public abstract class BaseBTNode {
    protected List<BaseBTNode> children = new LinkedList<>();
    public void addChild(BaseBTNode child) {
        children.add(child);
    }
    public boolean selfEnable(BlackBoard blackBoard) { 
        // default
        return true;
    }
    public abstract ProcessResult process(BlackBoard blackBoard); 
}
