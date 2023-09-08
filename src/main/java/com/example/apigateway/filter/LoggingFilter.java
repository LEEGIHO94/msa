package com.example.apigateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
//        //Logging PRE FILTER
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Logging filter baseMessage : {}",config.getBassMessage());
//
//            if(config.isPreLogger()){
//                log.info("Logging filter Start : request Id -> {}",request.getId());
//            }
//
//            //Logging POST Filter
//            return chain.filter(exchange).then(Mono.fromRunnable(()  ->{
//                if(config.isPostLogger()){
//                    log.info("Logging Filter End : request code -> {}",response.getStatusCode());
//                }
//            }));
//        };
        GatewayFilter filter = new OrderedGatewayFilter((exchange,chain)->{
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging filter baseMessage : {}",config.getBassMessage());

            if(config.isPreLogger()){
                log.info("Logging filter Start : request Id -> {}",request.getId());
            }

            //Logging POST Filter
            return chain.filter(exchange).then(Mono.fromRunnable(()  ->{
                if(config.isPostLogger()){
                    log.info("Logging Filter End : request code -> {}",response.getStatusCode());
                }
            }));
        }, Ordered.HIGHEST_PRECEDENCE);
        return filter;
    }

    @Data
    public static class Config{
        //설정 정보 추가 위치
        private String bassMessage;
        private boolean preLogger;
        private boolean postLogger;

    }

}
