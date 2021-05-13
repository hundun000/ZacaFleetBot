package com.zaca.stillstanding.dto.match;
/**
 * @author hundun
 * Created on 2019/10/08
 */
public enum AnswerType {
    CORRECT,
    WRONG,
    SKIPPED;
    
    public static AnswerType value(Boolean isCorrect) {
        if (isCorrect == null) {
            return SKIPPED;
        } else {
            return isCorrect ? CORRECT : WRONG;
        }
    }
}
