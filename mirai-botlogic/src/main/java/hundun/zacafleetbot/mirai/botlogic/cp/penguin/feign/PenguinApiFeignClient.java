package hundun.zacafleetbot.mirai.botlogic.cp.penguin.feign;



import java.util.List;

import feign.Param;
import feign.RequestLine;
import hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.Item;
import hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.ResultMatrixResponse;
import hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.Stage;





//@FeignClient(
//        name = "penguinApiService",
//        url = "https://penguin-stats.io/PenguinStats/api",
//        configuration = PublicFeignConfiguration.class
//)
//@Component
public interface PenguinApiFeignClient {

    @RequestLine("GET /v2/items")
    List<Item> items();
    
    @RequestLine("GET /v2/stages?server=CN")
    List<Stage> stages();
    
    @RequestLine("GET /v2/result/matrix?is_personal=false&server=CN&show_closed_zones=false&itemFilter={itemFilter}")
    ResultMatrixResponse resultMatrix(@Param("itemFilter") String itemFilter);

}
