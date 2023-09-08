package com.example.apigateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        //Global PRE FILTER
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

//            log.info("config bassMessage : {}", config.getBassMessageA());
//            log.info("config preLogger : {}", config.isPreLoggerA());
//            log.info("config postLogger : {}", config.isPostLoggerA());



            log.info("Global filter baseMessage : {}",config.getBassMessage());

            if(config.isPreLogger()){
                log.info("Global filter Start : request Id -> {}",request.getId());
            }

            //Global POST Filter
            return chain.filter(exchange).then(Mono.fromRunnable(()  ->{
                if(config.isPostLogger()){
                    log.info("Global Filter End : request code -> {}",response.getStatusCode());
                }
            }));
        };
    }

    @Data
    public static class Config{
        //설정 정보 추가 위치
        private String bassMessage;
        private boolean preLogger;
        private boolean postLogger;

    }

}
