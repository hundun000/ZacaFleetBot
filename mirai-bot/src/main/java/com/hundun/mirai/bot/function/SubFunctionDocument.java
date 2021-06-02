package com.hundun.mirai.bot.function;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FunctionCallStatement里有实参list。本类用于管理每个实参被SubFunction使用时的形参名和形参说明。
 * @author hundun
 * Created on 2021/05/17
 */
@AllArgsConstructor
@Data
public class SubFunctionDocument {
    String subFunctionIdentifier;
    String subFunctionDescription;
    List<String> parameterDescriptions = new ArrayList<>();
    //List<String> formalParameterNames;
    
    public SubFunctionDocument() {
        
    }
    
    public String toSimpleText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("指令id: ").append(subFunctionIdentifier).append("\n");
        stringBuilder.append("功能描述: ").append(subFunctionDescription).append("\n");
        return stringBuilder.toString();
    }
    
    public String toDetailText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("指令id: ").append(subFunctionIdentifier).append("\n");
        stringBuilder.append("功能描述: ").append(subFunctionDescription).append("\n");
        if (parameterDescriptions.size() == 0) {
            stringBuilder.append("参数: 无\n");
        } else {
            stringBuilder.append("参数:\n");
            for (int i = 0; i < parameterDescriptions.size(); i++) {
                String parameterDescription = parameterDescriptions.get(i);
                stringBuilder.append("  ").append(parameterDescription).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}   
