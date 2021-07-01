package hundun.zacafleetbot.mirai.botlogic.cp.quiz;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import hundun.stillstanding.api.StillstandingApiFeignClient;
import hundun.stillstanding.dto.ApiResult;
import hundun.stillstanding.dto.match.MatchConfigDTO;
import hundun.stillstanding.dto.match.MatchSituationDTO;
import hundun.stillstanding.dto.team.TeamConstInfoDTO;
import hundun.zacafleetbot.mirai.botlogic.helper.file.FileOperationDelegate;
import hundun.zacafleetbot.mirai.botlogic.helper.file.IFileOperationDelegator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Component
public class QuizService implements IFileOperationDelegator {
    ObjectMapper mapper = new ObjectMapper();
    
    
    FileOperationDelegate fileOperationDelegate;
    @Autowired
    StillstandingApiFeignClient stillstandingApiService;
    

    public QuizService() {
        this.fileOperationDelegate = new FileOperationDelegate(this);
    }
    
    public enum MatchType {
        ENDLESS,
        PRE, MAIN,
        ;
    }
    
    public MatchSituationDTO createAndStartMatch(String questionPackageName, List<String> teamNames, MatchType type) {
        MatchConfigDTO matchConfigDTO = new MatchConfigDTO();
        matchConfigDTO.setTeamNames(teamNames);
        matchConfigDTO.setQuestionPackageName(questionPackageName);
        ApiResult<MatchSituationDTO> apiResult;
        if (type == MatchType.ENDLESS) {
            apiResult = stillstandingApiService.createEndlessMatch(matchConfigDTO);
        } else if (type == MatchType.PRE) {
            apiResult = stillstandingApiService.createPreMatch(matchConfigDTO);
        } else if (type == MatchType.MAIN) {
            apiResult = stillstandingApiService.createMainMatch(matchConfigDTO);
        } else {
            log.warn("cannot handle type = {}", type);
            return null;
        }
        
        String sessionId;
        try {
            MatchSituationDTO matchSituationDTO = apiResult.getPayload();
            sessionId = matchSituationDTO.getId();
        } catch (Exception e) {
            log.warn("createEndlessMatch error, apiResult = {}", apiResult);
            log.warn("createEndlessMatch error, e = ", e);
            return null;
        }
        
        apiResult = stillstandingApiService.start(sessionId);
        try {
            MatchSituationDTO matchSituationDTO = apiResult.getPayload();
            return matchSituationDTO;
        } catch (Exception e) {
            log.warn("createEndlessMatch error, apiResult = {}", apiResult);
            log.warn("createEndlessMatch error, e = ", e);
            return null;
        }
    }
    
    public MatchSituationDTO answer(String sessionId, String answerChar) {
        ApiResult<MatchSituationDTO> apiResult = stillstandingApiService.teamAnswer(sessionId, answerChar);
        try {
            MatchSituationDTO matchSituationDTO = apiResult.getPayload();
            return matchSituationDTO;
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", apiResult);
            return null;
        }
    }
    
    public MatchSituationDTO useSkill(String sessionId, String skillName) {
        ApiResult<MatchSituationDTO> apiResult = stillstandingApiService.teamUseSkill(sessionId, skillName);
        try {
            MatchSituationDTO matchSituationDTO = apiResult.getPayload();
            return matchSituationDTO;
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", apiResult);
            return null;
        }
    }
    
    public MatchSituationDTO nextQustion(String sessionId) {
        ApiResult<MatchSituationDTO> apiResult = stillstandingApiService.nextQustion(sessionId);
        try {
            MatchSituationDTO matchSituationDTO = apiResult.getPayload();
            return matchSituationDTO;
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", apiResult);
            return null;
        }
    }
    
    
    
    


    @Override
    public InputStream download(String fileId, File cacheFolder) {
        try {
            final Response response = stillstandingApiService.pictures(fileId);
            final Response.Body body = response.body();
            final InputStream inputStream = body.asInputStream();
            return inputStream;
        } catch (Exception e) {
            log.info("download image faild {} {}", fileId, e);
            return null;
        }
    }

    @Override
    public String getCacheSubFolderName() {
        return "quiz";
    }

    public List<TeamConstInfoDTO> updateTeam(String sessionId, TeamConstInfoDTO teamConstInfoDTO) {
        ApiResult<List<TeamConstInfoDTO>> apiResult = stillstandingApiService.updateTeam(teamConstInfoDTO);
        try {
            List<TeamConstInfoDTO> payload = apiResult.getPayload();
            return payload;
        } catch (Exception e) {
            log.warn("updateTeam error: {}", apiResult);
            return null;
        }
    }


    @Override
    public File downloadOrFromCache(String fileId, File cacheFolder, File rawDataFolder) {
        return fileOperationDelegate.downloadOrFromCache(fileId, cacheFolder, rawDataFolder);
    }
    
}
