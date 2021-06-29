package com.hundun.mirai.bot.core.function;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundun.mirai.bot.core.CharacterRouter;
import com.hundun.mirai.bot.core.CustomBeanFactory;

import com.hundun.mirai.bot.core.SettingManager;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.parser.statement.LiteralValueStatement;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.core.parser.statement.SubFunctionCallStatement;
import com.hundun.mirai.bot.cp.quiz.QuizService;
import com.hundun.mirai.bot.cp.quiz.QuizService.MatchType;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.bot.helper.file.FileOperationDelegate;
import com.zaca.stillstanding.dto.buff.BuffDTO;
import com.zaca.stillstanding.dto.event.AnswerResultEvent;
import com.zaca.stillstanding.dto.event.EventType;
import com.zaca.stillstanding.dto.event.FinishEvent;
import com.zaca.stillstanding.dto.event.SkillResultEvent;
import com.zaca.stillstanding.dto.event.StartMatchEvent;
import com.zaca.stillstanding.dto.event.SwitchQuestionEvent;
import com.zaca.stillstanding.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.dto.match.AnswerType;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;
import com.zaca.stillstanding.dto.match.MatchState;
import com.zaca.stillstanding.dto.question.QuestionDTO;
import com.zaca.stillstanding.dto.question.ResourceType;
import com.zaca.stillstanding.dto.role.RoleConstInfoDTO;
import com.zaca.stillstanding.dto.team.TeamConstInfoDTO;
import com.zaca.stillstanding.dto.team.TeamRuntimeInfoDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Component
public class QuizHandler extends BaseFunction {


    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList(
                SubFunction.QUIZ_EXIT, 
                SubFunction.QUIZ_UPDATE_TEAM, 
                SubFunction.QUIZ_NEXT_QUEST, 
                SubFunction.QUIZ_USE_SKILL, 
                SubFunction.QUIZ_START_MATCH
                );
    }
    
    @Autowired
    QuizService quizService;
    
    FileOperationDelegate fileOperationDelegate;
        
    @Autowired
    SettingManager settingManager;
    
    Map<String, SessionData> sessionDataMap = new HashMap<>();

    
    @Data
    private class SessionData {
        
        MatchSituationDTO matchSituationDTO;
        File resource;
        long createTime;
        boolean showCompletedSituation;
    }
    


    

    @Override
    public boolean acceptStatement(SessionId sessionId, EventInfo event, Statement statement) {
        
        synchronized (this) {
            
            SessionData sessionData = sessionDataMap.get(sessionId.id());
            if (sessionData == null) {
                sessionData = new SessionData();
                sessionDataMap.put(sessionId.id(), sessionData);
            }
    
            boolean result = false;
            
            if (statement instanceof SubFunctionCallStatement) {
                SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
                switch (subFunctionCallStatement.getSubFunction()) {
                    case QUIZ_NEXT_QUEST:
                        if (sessionData.matchSituationDTO == null) {
                            console.sendToGroup(event.getBot(), event.getGroupId(), "没有进行中的比赛");
                            result = true;
                        } else if (sessionData.matchSituationDTO.getState() == MatchState.WAIT_GENERATE_QUESTION) {
                            result = handleNextQustion(sessionData, event);
                        } else if (sessionData.matchSituationDTO.getState() == MatchState.WAIT_ANSWER) {
                            console.sendToGroup(event.getBot(), event.getGroupId(), "上一个问题还没回答哦~");
                            result = true;
                        }
                        break;
                    case QUIZ_USE_SKILL:
                        if (sessionData.matchSituationDTO == null) {
                            console.sendToGroup(event.getBot(), event.getGroupId(), "没有进行中的比赛");
                            result = true;
                        } else if (sessionData.matchSituationDTO.getState() == MatchState.WAIT_ANSWER) {
                            String skillName = subFunctionCallStatement.getArgs().get(0);
                            result = handleUseSkill(sessionData, event, skillName);
                        } else {
                            console.sendToGroup(event.getBot(), event.getGroupId(), "当前不能使用技能");
                            result = true;
                        }
                        break;
                    case QUIZ_EXIT:
                        if (event.getSenderId() == settingManager.getAdminAccount(event.getBot().getId())) {
                            sessionData.matchSituationDTO = null;
                            console.sendToGroup(event.getBot(), event.getGroupId(), (new At(event.getSenderId())).plus("比赛已退出！"));
                            return true;
                        } else {
                            console.sendToGroup(event.getBot(), event.getGroupId(), (new At(event.getSenderId())).plus("你没有该操作的权限！"));
                            return true;
                        }
                    case QUIZ_START_MATCH:   
                        if (sessionData.matchSituationDTO == null) {
                            String matchMode = subFunctionCallStatement.getArgs().get(0);
                            String questionPackageName = subFunctionCallStatement.getArgs().get(1);
                            String teamName = subFunctionCallStatement.getArgs().get(2);
                            boolean showCompletedSituation = false;
                            if (subFunctionCallStatement.getArgs().size() >= 4) {
                                String show = subFunctionCallStatement.getArgs().get(3);
                                if (show.contains("完整")) {
                                    showCompletedSituation = true;
                                }
                            }
                            result = handleCreateAndStartMatch(sessionData, event, matchMode, questionPackageName, teamName, showCompletedSituation);
                        } else {
                            console.sendToGroup(event.getBot(), event.getGroupId(), "目前已在比赛中");
                            result = true;
                        }
                        break;
                    case QUIZ_UPDATE_TEAM:
                        if (event.getSenderId() == settingManager.getAdminAccount(event.getBot().getId())) {
                            String teamName = subFunctionCallStatement.getArgs().get(0);
                            List<String> pickTags = Arrays.asList(subFunctionCallStatement.getArgs().get(1).split("&"));
                            List<String> banTags = Arrays.asList(subFunctionCallStatement.getArgs().get(2).split("&"));
                            String roleName = subFunctionCallStatement.getArgs().get(3);
                            TeamConstInfoDTO teamConstInfoDTO = new TeamConstInfoDTO();
                            teamConstInfoDTO.setName(teamName);
                            teamConstInfoDTO.setPickTags(pickTags);
                            teamConstInfoDTO.setBanTags(banTags);
                            teamConstInfoDTO.setRoleName(roleName);
                            result = handleUpdateTeam(sessionData, event, teamConstInfoDTO);
                        } else {
                            console.sendToGroup(event.getBot(), event.getGroupId(), (new At(event.getSenderId())).plus("你没有该操作的权限！"));
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            } else if (statement instanceof LiteralValueStatement) {
                
                if (sessionData.matchSituationDTO != null && sessionData.matchSituationDTO.getState() == MatchState.WAIT_ANSWER) {
                    String newMessage = ((LiteralValueStatement)statement).getValue();
                    if (newMessage.equals("A") || newMessage.equals("B") || newMessage.equals("C") || newMessage.equals("D")) {
                        result = handleAnswer(sessionData, event, newMessage);
                    }
                }
            }
            return result;
        }
        
    }

    private boolean handleUpdateTeam(SessionData sessionData, EventInfo event, TeamConstInfoDTO teamConstInfoDTO) {
        List<TeamConstInfoDTO> payload = quizService.updateTeam(sessionData.getMatchSituationDTO().getId(), teamConstInfoDTO);
        if (payload != null)  {
            console.sendToGroup(event.getBot(), event.getGroupId(), "配置队伍成功。");
            return true;
        } else {
            console.sendToGroup(event.getBot(), event.getGroupId(), "配置队伍失败。");
            return true;
        }
    }


    private boolean handleUseSkill(SessionData sessionData, EventInfo event, String skillName) {
        MatchSituationDTO newSituationDTO = quizService.useSkill(sessionData.getMatchSituationDTO().getId(), skillName);
        if (newSituationDTO != null)  {
            sessionData.matchSituationDTO = newSituationDTO;
        } else {
            console.sendToGroup(event.getBot(), event.getGroupId(), "使用技能失败。");
            return true;
        }
        
        SkillResultEvent skillResultEvent = sessionData.matchSituationDTO.getSkillResultEvent();
        if (skillResultEvent != null) {
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            messageChainBuilder.add(new At(event.getSenderId()));
            
            if (skillResultEvent.getType() == EventType.SKILL_SUCCESS) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("使用技能成功，效果:").append(skillResultEvent.getSkillDesc()).append("\n");
                try {
                    switch (skillResultEvent.getSkillName()) {
                        case "跳过":
                            String fakeAnswerChar = skillResultEvent.getArgs().get(0);
                            return handleAnswer(sessionData, event, fakeAnswerChar);
                        case "5050":
                            Random random = new Random();
                            QuestionDTO questionDTO = sessionData.matchSituationDTO.getQuestion();
                            int rand = random.nextInt(3);
                            if (questionDTO.getAnswer() == 0) { 
                                if (rand == 0) {
                                    stringBuilder.append("揭示的错误选项：B、C");
                                } else if (rand == 1) {
                                    stringBuilder.append("揭示的错误选项：B、D");
                                } else {
                                    stringBuilder.append("揭示的错误选项：C、D");
                                } 
                            } else if (questionDTO.getAnswer() == 1) {
                                if (rand == 0) {
                                    stringBuilder.append("揭示的错误选项：A、C");
                                } else if (rand == 1) {
                                    stringBuilder.append("揭示的错误选项：A、D");
                                } else {
                                    stringBuilder.append("揭示的错误选项：C、D");
                                } 
                            } else if (questionDTO.getAnswer() == 2) {
                                if (rand == 0) {
                                    stringBuilder.append("揭示的错误选项：A、B");
                                } else if (rand == 1) {
                                    stringBuilder.append("揭示的错误选项：A、D");
                                } else {
                                    stringBuilder.append("揭示的错误选项：B、D");
                                } 
                            } else if (questionDTO.getAnswer() == 3) {
                                if (rand == 0) {
                                    stringBuilder.append("揭示的错误选项：A、B");
                                } else if (rand == 1) {
                                    stringBuilder.append("揭示的错误选项：A、C");
                                } else {
                                    stringBuilder.append("揭示的错误选项：B、C");
                                } 
                            }
                            break;
                        default:
                            console.getLogger().info("do nothing for " + skillResultEvent.getSkillName() + " as default");
                            break;
                    }
                } catch (Exception e) {
                    stringBuilder.append("エラー発生。处理这个技能的结果时出错：" + e.getMessage());
                }
                
                
                messageChainBuilder.add(new PlainText(stringBuilder.toString()));
            } else {
                messageChainBuilder.add(new PlainText("使用技能失败，技能点已耗尽。"));
            }
            console.sendToGroup(event.getBot(), event.getGroupId(), messageChainBuilder.build());
            return true;
        }
        
        return false;
    }

    private boolean handleCreateAndStartMatch(SessionData sessionData, EventInfo event, String matchMode, String questionPackageName, String teamName, boolean showTeamSituation) {
        
        
        MatchSituationDTO newSituationDTO;
        if (matchMode.equals("无尽模式")) {
            newSituationDTO = quizService.createAndStartMatch(questionPackageName, Arrays.asList(teamName), MatchType.ENDLESS);
        } else if (matchMode.equals("预赛模式")) {
            newSituationDTO = quizService.createAndStartMatch(questionPackageName, Arrays.asList(teamName), MatchType.PRE);
        } else if (matchMode.equals("复赛模式")) {
            newSituationDTO = quizService.createAndStartMatch(questionPackageName, Arrays.asList(teamName.split("&")), MatchType.MAIN);
        } else {
            console.getLogger().warning("unkown functionName: " + matchMode);
            newSituationDTO = null;
        }
        
        
        if (newSituationDTO != null)  {
            sessionData.matchSituationDTO = newSituationDTO;
            sessionData.showCompletedSituation = showTeamSituation;
            
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("开始比赛成功");
            if (sessionData.showCompletedSituation) {
                
                StartMatchEvent startMatchEvent = newSituationDTO.getStartMatchEvent();
                
                String teamDetailText = teamsDetailText(newSituationDTO.getTeamRuntimeInfos(), startMatchEvent.getTeamConstInfos(), startMatchEvent.getRoleConstInfos());
                stringBuilder.append("\n\n").append(teamDetailText);
            }
            
            
            console.sendToGroup(event.getBot(), event.getGroupId(), stringBuilder.toString());
            return true;
        } else {
            console.sendToGroup(event.getBot(), event.getGroupId(), "开始比赛失败");
            return true;
        }
        
        
    }
    
    private boolean handleNextQustion(SessionData sessionData, EventInfo event) {
        MatchSituationDTO newSituationDTO = quizService.nextQustion(sessionData.matchSituationDTO.getId());
        if (newSituationDTO != null)  {
            sessionData.matchSituationDTO = newSituationDTO;
        } else {
            console.sendToGroup(event.getBot(), event.getGroupId(), "出题失败");
            return true;
        }
        
        QuestionDTO questionDTO = sessionData.matchSituationDTO.getQuestion();
        if (questionDTO.getResource().getType() == ResourceType.IMAGE) {
            String imageResourceId = questionDTO.getResource().getData();
            File cacheFolder = console.resolveDataFileOfFileCache();
            sessionData.resource = quizService.downloadOrFromCache(imageResourceId, cacheFolder, null);
        } else {
            sessionData.resource = null;
        }
        sessionData.createTime = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        
        if (sessionData.showCompletedSituation) {
            SwitchQuestionEvent switchQuestionEvent = newSituationDTO.getSwitchQuestionEvent();
            TeamRuntimeInfoDTO currentTeam = newSituationDTO.getTeamRuntimeInfos().get(newSituationDTO.getCurrentTeamIndex());
            builder.append("当前队伍:").append(currentTeam.getName()).append(" ");
            builder.append("时间:").append(switchQuestionEvent.getTime()).append("秒\n\n");
        }
        
        builder.append(questionDTO.getStem()).append("\n")
        .append("A. ").append(questionDTO.getOptions().get(0)).append("\n")
        .append("B. ").append(questionDTO.getOptions().get(1)).append("\n")
        .append("C. ").append(questionDTO.getOptions().get(2)).append("\n")
        .append("D. ").append(questionDTO.getOptions().get(3)).append("\n")
        .append("\n")
        .append("发送选项字母来回答");
        
        
        
        MessageChain messageChain = new PlainText(builder.toString()).plus(new PlainText(""));
        if (sessionData.resource != null) {
            ExternalResource externalResource = ExternalResource.create(sessionData.resource);
            Image image = console.uploadImage(event.getBot(), event.getGroupId(), externalResource);
            messageChain = messageChain.plus(image);
        }
        console.sendToGroup(event.getBot(), event.getGroupId(), messageChain);
        return true;
    }
    
    
    
    private boolean handleAnswer(SessionData sessionData, EventInfo event, String answerChar) {
        String correctAnser = QuestionDTO.intToAnswerText(sessionData.matchSituationDTO.getQuestion().getAnswer());
        MatchSituationDTO newSituationDTO = quizService.answer(sessionData.matchSituationDTO.getId(), answerChar);
        if (newSituationDTO != null)  {
            sessionData.matchSituationDTO = newSituationDTO;
        } else {
            return false;
        }
        
        
        AnswerResultEvent answerResultEvent = sessionData.matchSituationDTO.getAnswerResultEvent();
        if (answerResultEvent != null) {
            
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            messageChainBuilder.add(new At(event.getSenderId()));
            
            {
                StringBuilder stringBuilder = new StringBuilder();
                if (answerResultEvent.getResult() == AnswerType.CORRECT) {
                    stringBuilder.append("回答正确\n正确答案是" + correctAnser);
                } else if (answerResultEvent.getResult() == AnswerType.WRONG) {
                    stringBuilder.append("回答错误QAQ\n正确答案是" + correctAnser);
                } else if (answerResultEvent.getResult() == AnswerType.SKIPPED) {
                    stringBuilder.append("本题已跳过\n正确答案是" + correctAnser);
                }
                stringBuilder.append("\n").append(answerResultEvent.getAddScoreTeamName()).append(" +").append(answerResultEvent.getAddScore()).append("分\n");
                messageChainBuilder.add(new PlainText(stringBuilder.toString()));
            }
            
            
            if (sessionData.showCompletedSituation) {
                String text = teamsNormalText(sessionData.matchSituationDTO.getTeamRuntimeInfos());
                messageChainBuilder.add(new PlainText(text));
            }
            
            if (newSituationDTO.getSwitchTeamEvent() != null) {
                SwitchTeamEvent matchEvent = newSituationDTO.getSwitchTeamEvent();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\n队伍变更: ").append(matchEvent.getFromTeamName()).append(" -> ").append(matchEvent.getToTeamName());
                messageChainBuilder.add(new PlainText(stringBuilder.toString()));
            }
            
            if (newSituationDTO.getFinishEvent() != null) {
                FinishEvent matchEvent = newSituationDTO.getFinishEvent();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\n比赛结束!");
                messageChainBuilder.add(new PlainText(stringBuilder.toString()));
                
                sessionData.matchSituationDTO = null;
            }
            
            console.sendToGroup(event.getBot(), event.getGroupId(), messageChainBuilder.build());
            return true;
        } else {
            return false;
        }
    }
    
    
    
    
    private String teamsNormalText(List<TeamRuntimeInfoDTO> dtos) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("队伍状态:\n");
        for (TeamRuntimeInfoDTO dto : dtos) {
            stringBuilder.append(dto.getName()).append(" ");
            stringBuilder.append("得分:").append(dto.getMatchScore()).append(" ");
            stringBuilder.append("生命:").append(dto.getHealth()).append(" ");
            if (dto.getBuffs().size() > 0) {
                stringBuilder.append("Buff:\n");
                for (BuffDTO buffDTO : dto.getBuffs()) {
                    stringBuilder.append(buffDTO.getName()).append("x").append(buffDTO.getDuration()).append(" ").append(buffDTO.getDescription()).append("\n");
                }
            }
            stringBuilder.append("英雄:").append(dto.getRoleName()).append(" 技能:\n");
            for (Entry<String, Integer> entry : dto.getSkillRemainTimes().entrySet()) {
                stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    
    
    private String teamsDetailText(List<TeamRuntimeInfoDTO> teamRuntimeDTOs, List<TeamConstInfoDTO> teamDTOs, List<RoleConstInfoDTO> roleDTOs) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("队伍详情:\n");
        for (int i = 0; i < teamRuntimeDTOs.size(); i++) {
            TeamRuntimeInfoDTO teamRuntimeInfoDTO = teamRuntimeDTOs.get(i);
            TeamConstInfoDTO team = teamDTOs.get(i);
            RoleConstInfoDTO role = roleDTOs.get(i);
            
            stringBuilder.append(team.getName()).append(" 生命:").append(teamRuntimeInfoDTO.getHealth()).append("\n");
            if (team.getPickTags().size() > 0) {
                stringBuilder.append("Pick:");
                team.getPickTags().forEach(tag -> stringBuilder.append(tag).append("、"));
                stringBuilder.setLength(stringBuilder.length() - 1);
                stringBuilder.append("\n");
            }
            if (team.getBanTags().size() > 0) {
                stringBuilder.append("Ban:");
                team.getBanTags().forEach(tag -> stringBuilder.append(tag).append("、"));
                stringBuilder.setLength(stringBuilder.length() - 1);
                stringBuilder.append("\n");
            }
            stringBuilder.append("英雄:").append(role.getName()).append(" 介绍:").append(role.getDescription()).append("\n");
            for (int j = 0; j < role.getSkillNames().size(); j++) {
                stringBuilder.append("技能").append(j + 1).append(":").append(role.getSkillNames().get(j)).append(" ");
                stringBuilder.append("次数:").append(role.getSkillFullCounts().get(j)).append(" ");
                stringBuilder.append("效果:").append(role.getSkillDescriptions().get(j)).append("\n");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    
    
}
