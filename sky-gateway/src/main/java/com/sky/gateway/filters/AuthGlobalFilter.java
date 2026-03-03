package com.sky.gateway.filters;

import com.sky.constant.JwtClaimsConstant;
import com.sky.gateway.properties.AuthProperties;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private final JwtProperties jwtProperties;

    private final AuthProperties authProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 判断当前请求路径是否在排除路径列表中
        ServerHttpRequest request = exchange.getRequest();
        if (isAdminExcludePath(request.getPath().toString())) {
            return chain.filter(exchange);
        }
        // 解析JWT
        String token = request.getHeaders().getFirst(jwtProperties.getAdminTokenName());
        try {
            Claims claims = JwtUtils.parseJwt(token);
            Long id = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            // 将ID添加到请求头中
            ServerWebExchange webExchange = exchange.mutate().request(builder -> builder.header(JwtClaimsConstant.EMP_ID, String.valueOf(id))).build();
            log.info("解析JWT成功：{}", claims);
            return chain.filter(webExchange);
        } catch (Exception e) {
            log.error("解析JWT失败：{}", e.getMessage());
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private boolean isAdminExcludePath(String path) {
        if (!antPathMatcher.match(authProperties.getAdminPath(), path)) {
            return true;
        }
        for (String excludePath : authProperties.getAdminExcludePaths()) {
            if (antPathMatcher.match(excludePath, path)) {
                return true;
            }
        }
        return false;
    }
}