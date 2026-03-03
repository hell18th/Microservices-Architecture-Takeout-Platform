package com.sky.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
    // 连接池配置
    private static final PoolingHttpClientConnectionManager connectionManager;
    private static final RequestConfig requestConfig;

    // 共享的HTTP客户端实例
    private static final CloseableHttpClient httpClient;

    static {
        // 创建连接池管理器
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(40); // 最大连接数
        connectionManager.setDefaultMaxPerRoute(20); // 每个路由的最大连接数

        // 请求配置
        requestConfig = RequestConfig.custom().setConnectTimeout(10000) // 连接超时时间
                .setSocketTimeout(20000)  // 读取超时时间
                .setConnectionRequestTimeout(10000) // 从连接池获取连接的超时时间
                .build();

        // 创建HTTP客户端
        httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * 执行HTTP GET请求
     *
     * @param url      请求的URL地址
     * @param paramMap 请求参数映射表，键值对形式的参数
     * @return 返回HTTP响应的字符串内容，如果请求失败则返回空字符串
     */
    public static String doGET(String url, Map<String, String> paramMap) {
        String result = "";

        try {
            // 构建请求URI，添加参数到URL中
            URIBuilder uriBuilder = new URIBuilder(url);
            if (paramMap != null) {
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    uriBuilder.addParameter(param.getKey(), param.getValue());
                }
            }
            URI uri = uriBuilder.build();
            HttpGet httpGet = new HttpGet(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                // 检查响应状态码，如果为200则读取响应内容
                if (response.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * 执行HTTP POST请求
     *
     * @param url      请求的目标URL
     * @param paramMap 请求参数映射表，键值对形式的参数
     * @return 返回服务器响应的字符串内容，如果请求失败则返回空字符串
     */
    public static String doPOST(String url, Map<String, String> paramMap) {
        String result = "";

        try {
            HttpPost httpPost = new HttpPost(url);

            // 构建请求参数实体
            if (paramMap != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // 检查响应状态码，如果为200则读取响应内容
                if (response.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * 关闭HTTP客户端连接池
     * 在应用关闭时调用此方法
     */
    public static void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
            connectionManager.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}