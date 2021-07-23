package hundun.zacafleetbot.mirai.botlogic.cp.pixiv.feign;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

/**
 * @author hundun
 * Created on 2021/07/21
 */
public interface RainchanPixivFeignClient {
    @RequestLine("GET /img")
    Response randomSmallPictures();
}
