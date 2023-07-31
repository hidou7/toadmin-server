package com.hidou7.toadmin.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class HttpUtil {

    public static Result doGet(String url, Map<String, Object> params, Map<String, String> headers){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        if(params != null && params.size() > 0){
            url += "?" + handleUrlParam(params);
        }
        HttpGet httpGet = new HttpGet(url);
        if(headers != null){
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpGet.setHeader(header.getKey(), header.getValue());
            }
        }
        httpGet.setConfig(getRequestConfig());
        CloseableHttpResponse response = null;
        Header[] allHeaders = null;
        String content = null;
        Integer statusCode = null;
        try{
            // 发请求
            log.info("[Get] " + url + ", " +
                     "header: " + headers);
            response = httpClient.execute(httpGet);
            // 解析
            HttpEntity entity = response.getEntity();
            if(entity != null){
                content = EntityUtils.toString(entity, "utf8");
            }
            allHeaders = response.getAllHeaders();
            // 请求状态码
            statusCode = response.getStatusLine().getStatusCode();
        }catch (IOException e){
            log.error("httpClient异常", e);
        }finally{
            log.info("status: " + statusCode + ", " +
                     "return data: " + substringContent(content));
            // 释放资源
            release(response, httpClient);
        }
        return new Result(statusCode, content, allHeaders);
    }

    public static Result doPost(String url, String params, Map<String, String> headers){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        Integer statusCode = null;
        String content = null;
        httpPost = new HttpPost(url);
        //设置post请求头
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Accept", "*/*");// accept 这样才能接收所有返回的数据
        if(params != null && !params.equals("")) {
            StringEntity stringEntity = new StringEntity(params, "utf-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
        }
        if(headers != null){
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
            }
        }
        httpPost.setConfig(getRequestConfig());
        try{
            log.info("[Post] " + url + ", " +
                     "params: " + params +
                     "header: " + headers);
            response = httpClient.execute(httpPost);
            // 请求状态码
            statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if(entity != null){
                content = EntityUtils.toString(entity, "utf8");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            log.info("status: " + statusCode + ", " +
                     "return data: " + substringContent(content));
            release(response, httpClient);
        }
        return new Result(statusCode, content, response == null ? null : response.getAllHeaders());
    }

    private static String handleUrlParam(Map<String, Object> params){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String s = sb.toString();
        return s.substring(0, s.length() - 1);
    }

    private static String handleJsonParam(Map<String, Object> params){
        return JsonUtil.toJsonString(params);
    }

    // 释放资源
    private static void release(CloseableHttpResponse response, CloseableHttpClient httpClient){
        // 释放资源
        if(response != null){
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String substringContent(String content){
        return (content == null || content.length() < 500? content : content.substring(0, 500) + "...");
    }

    private static RequestConfig getRequestConfig(){
        return RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .setConnectionRequestTimeout(3000) // 指从连接池获取到连接的超时时间
                .build();
    }

    public static class Result{

        private final Integer code;
        private final String content;
        private final Header[] headers;

        public Result(Integer code, String content, Header[] headers) {
            this.code = code;
            this.content = content;
            this.headers = headers;
        }

        public Integer getCode() {
            return code;
        }

        public String getContent() {
            return content;
        }

        public Header[] getHeaders() {
            return headers;
        }
    }
}
