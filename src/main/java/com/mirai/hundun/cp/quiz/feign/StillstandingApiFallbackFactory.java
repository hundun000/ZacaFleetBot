package com.mirai.hundun.cp.quiz.feign;


import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;




/**
 *
 * @author yiklam
 * @date 2021/1/28
 */
@Slf4j
@Component
public class StillstandingApiFallbackFactory implements FallbackFactory<StillstandingApiService> {
    @Override
    public StillstandingApiService create(Throwable cause) {
        return new StillstandingApiService() {

            @Override
            public String createEndlessMatch() {
                log.warn("createEndlessMatch fail: ", cause);
                return null;
            }

            @Override
            public String start(int id) {
                log.warn("start fail: ", cause);
                return null;
            }

            @Override
            public String answer(int id, String answer) {
                log.warn("answer fail: ", cause);
                return null;
            }





        };
    }
}
