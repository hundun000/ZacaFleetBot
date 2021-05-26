package com.hundun.mirai.plugin.cp.penguin.feign;



import java.util.List;


import com.hundun.mirai.plugin.cp.penguin.domain.Item;
import com.hundun.mirai.plugin.cp.penguin.domain.ResultMatrixResponse;
import com.hundun.mirai.plugin.cp.penguin.domain.Stage;

import feign.Param;
import feign.RequestLine;





public interface PenguinApiService {

    @RequestLine("GET /v2/items")
    List<Item> items();
    
    @RequestLine("GET /v2/stages?server=CN")
    List<Stage> stages();
    
    @RequestLine("GET /v2/result/matrix?is_personal=false&server=CN&show_closed_zones=false")
    ResultMatrixResponse resultMatrix(@Param("itemFilter") String itemFilter);

}
