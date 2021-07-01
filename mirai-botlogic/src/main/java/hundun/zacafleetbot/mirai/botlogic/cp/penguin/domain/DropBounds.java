package hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/29
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DropBounds {
    int lower;
    int upper;
}
