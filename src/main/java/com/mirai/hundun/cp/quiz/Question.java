package com.mirai.hundun.cp.quiz;

import java.io.File;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@AllArgsConstructor
@Data
public class Question {
    String stem;
    List<String> options;
    String answerChar;
    File resourceImage;
    
    public Question() {
        
    }
}
