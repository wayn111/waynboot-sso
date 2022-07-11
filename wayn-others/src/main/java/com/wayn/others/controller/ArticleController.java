package com.wayn.others.controller;

import com.wayn.others.config.HttpsClientRequestFactory;
import com.wayn.others.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequestMapping("others")
public class ArticleController {
    private static final String PREFIX = "others/article";

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("accept", "*/*");
        httpHeaders.set("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        httpHeaders.set("connection", "Keep-Alive");
        HttpEntity<String> httpEntity = new HttpEntity(null, httpHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange("https://interface.meiriyiwen.com/article/day", HttpMethod.GET, httpEntity, String.class);
        log.info(exchange.getBody());
    }

    @RequestMapping("article")
    public String index() {
        return "others/article";
    }

    @RequestMapping("article/query")
    @ResponseBody
    public R queryArticle(String date) {
        String param;
        String data = "";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("accept", "*/*");
        httpHeaders.set("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        httpHeaders.set("connection", "Keep-Alive");
        HttpEntity<String> httpEntity = new HttpEntity(null, httpHeaders);
        try {
            if (StringUtils.isNotBlank(date)) {
                param = "dev=1&date=" + date;
                ResponseEntity<String> exchange = restTemplate.exchange("https://interface.meiriyiwen.com/article/day?" + param, HttpMethod.GET, httpEntity, String.class);
                data = exchange.getBody();
            } else {
                param = "dev=1";
                ResponseEntity<String> exchange = restTemplate.exchange("https://interface.meiriyiwen.com/article/today?" + param, HttpMethod.GET, httpEntity, String.class);
                data = exchange.getBody();
            }
            return R.success().add("data", data);
        } catch (Exception e) {
            log.error("获取文章失败", e);
            return R.error("获取文章失败，请联系网站管理员！");
        }
    }
}
