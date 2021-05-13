package com.zaca.stillstanding.dto.question;

import java.util.List;
import java.util.Set;



import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/08
 */
@Data
public class QuestionDTO {
    String id;
    String stem;
    List<String> options;
    int answer;
    Resource resource;
    Set<String> tags;
    
    
    public static int answerTextToInt(String text) {
        switch (text) {
            case "A":
            case "a":
                return 0;
            case "B":
            case "b":
                return 1;
            case "C":
            case "c":
                return 2;
            case "D":
            case "d":
                return 3;
            default:
                return -1;
        }
        
    }
    
    public static String intToAnswerText(int value) {
        switch (value) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
            default:
                return "?";
        }
        
    }
}
