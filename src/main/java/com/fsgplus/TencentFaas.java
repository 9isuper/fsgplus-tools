package com.fsgplus;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.services.scf.runtime.events.APIGatewayProxyRequestEvent;
import com.qcloud.services.scf.runtime.events.APIGatewayProxyResponseEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/** @ClassName TencentFaas @Description @Author admin @Date 8/25/2021 10:43 AM @Version 1.0 */
public class TencentFaas {

  private static boolean cold_launch = true;

  public String mainHandler(APIGatewayProxyRequestEvent req) {
    System.out.println("start main handler");
    if (cold_launch) {
      System.out.println("start spring");
      FsgplusApplication.main(new String[] {""});
      System.out.println("stop spring");
      cold_launch = false;
    }

    String path = req.getPath();
    System.out.println("request path: " + path);

    String method = req.getHttpMethod();
    System.out.println("request method: " + method);

    String body = req.getBody();
    System.out.println("Body: " + body);

    Map<String, String> hdrs = req.getHeaders();

    HttpMethod m = HttpMethod.resolve(method);
    HttpHeaders headers = new HttpHeaders();
    headers.setAll(hdrs);
    RestTemplate client = new RestTemplate();
    HttpEntity<String> entity = new HttpEntity<String>(body, headers);

    String url = "http://127.0.0.1:8080" + path;

    System.out.println("send request");
    ResponseEntity<String> response = client.exchange(url, m, entity, String.class, new Object[0]);

    APIGatewayProxyResponseEvent resp = new APIGatewayProxyResponseEvent();
    resp.setStatusCode(Integer.valueOf(response.getStatusCodeValue()));
    HttpHeaders headers1 = response.getHeaders();
    resp.setHeaders(JSONObject.parseObject(JSONObject.toJSONString(headers1)));
    resp.setBody((String) response.getBody());
    System.out.println("response body: " + (String) response.getBody());
    return resp.toString();
  }
}
