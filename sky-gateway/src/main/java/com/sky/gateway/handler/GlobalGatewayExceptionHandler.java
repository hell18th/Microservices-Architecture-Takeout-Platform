package com.sky.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.result.Result;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

/**
 * Spring Cloud Gateway 全局异常处理器
 */
@Component
@Order(-1)
public class GlobalGatewayExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    public GlobalGatewayExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // 参数1：exchange = 网关的「请求/响应上下文」，能拿到请求、响应等信息
    // 参数2：ex = 异常
    @Override
    @NonNull
    public Mono<Void> handle(ServerWebExchange exchange, @NonNull Throwable ex) {
        // 第一步：从上下文里拿到「响应对象」
        ServerHttpResponse response = exchange.getResponse();

        // 第二步：设置响应头JSON格式，编码UTF-8
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        // 第三步：根据不同的异常类型，封装不同的返回结果
        Result<Void> result; // 统一返回类
        if (ex instanceof ConnectException) {
            // 情况1：后端服务连不上
            result = Result.error("后端服务暂不可用，请稍后重试");
        } else if (ex instanceof ResponseStatusException e) {
            // 情况2：404/403等状态码异常
            result = Result.error(e.getReason());
        } else {
            // 情况3：其他所有没预料到的异常
            result = Result.error("网关服务异常：" + ex.getMessage());
        }

        // 第四步：把封装好的结果转成JSON字节数组
        byte[] jsonBytes;
        try {
            // 把对象转成JSON字符串，再转成字节
            jsonBytes = objectMapper.writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
            // 极端情况：JSON转换失败，返回兜底的异常信息
            jsonBytes = "{\"code\":500,\"msg\":\"网关异常处理失败\",\"data\":null}".getBytes();
        }

        // 第五步：把JSON字节写入响应，返回给前端
        // Mono.just：Reactor响应式编程的写法
        // response.bufferFactory().wrap：把字节数组包装成响应能识别的格式
        return response.writeWith(Mono.just(response.bufferFactory().wrap(jsonBytes)));
    }
}