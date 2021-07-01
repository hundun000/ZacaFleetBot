package hundun.stillstanding.dto.event;

import java.util.List;

import hundun.stillstanding.dto.role.RoleConstInfoDTO;
import hundun.stillstanding.dto.team.TeamConstInfoDTO;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class StartMatchEvent extends MatchEvent {
    List<TeamConstInfoDTO> teamConstInfos;
    List<RoleConstInfoDTO> roleConstInfos;
}
