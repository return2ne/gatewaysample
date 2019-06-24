package com.example.gatewaysample.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class GatewaysampleFilterFactory extends AbstractGatewayFilterFactory<GatewaysampleFilterFactory.GatewaysampleConfig> {

    public GatewaysampleFilterFactory() {
        super(GatewaysampleConfig.class);
    }

    private boolean isValidServerIP(String ip) {
        return ip.equals("");
    }

    private boolean isAuthorizationValid(String authorizationHeader) {
        boolean isValid = true;

        return isValid;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

    @Override
    public GatewayFilter apply(GatewaysampleConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String clientIP = request.getRemoteAddress().getAddress().getHostAddress();

//            if (isValidServerIP(clientIP)) {
//                return chain.filter(exchange);
//            }

            if (!request.getHeaders().containsKey("myheader")) {
                return this.onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String header = request.getHeaders().getFirst("myheader");

            if (!this.isAuthorizationValid(header)) {
                return this.onError(exchange, "Invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("key", header)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    public static class GatewaysampleConfig {

        private List<String> serverIPs;

        public List<String> getServerIPs() {
            return this.serverIPs;
        }

        public void setServerIPs(List serverIPs) {
            this.serverIPs = serverIPs;
        }

    }
}
