package hundun.zacafleetbot.mirai.botlogic.core.behaviourtree;
/**
 * 等价于：<br>
 * Sequence<br>
 * +—— prehandle-Action(ModifyBlackBoard)<br>
 * +—— PrioritySelector<br>
 * @author hundun
 * Created on 2021/07/06
 */
public abstract class PreHandleablePrioritySelectorBTNode extends BaseBTNode {
    
    /**
     * @param blackBoard
     * @return modify sucess
     */
    protected boolean modifyBlackBoardForChildren(BlackBoard blackBoard) {
        // default
        return true;
    }
    
    @Override
    public ProcessResult process(BlackBoard blackBoard) {
        boolean selfEnable = selfEnable(blackBoard);
        if (!selfEnable) {
            return new ProcessResult(this, false);
        }
        boolean modifySuccess = modifyBlackBoardForChildren(blackBoard);
        if (!modifySuccess) {
            return new ProcessResult(this, false);
        }
        
        for (BaseBTNode child : children) {
            ProcessResult childResult = child.process(blackBoard);
            if (childResult.isDone()) {
                return childResult;
            }
        }

        return new ProcessResult(this, false);
    }
}
