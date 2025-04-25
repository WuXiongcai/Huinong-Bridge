package com.example.test;

import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;

public class test1 {

    // 请替换为您自己的高德API密钥
    private static final String KEY = "8820b301cbb7e426018db32287d394a0";

    public static void main(String[] args) {
        String url = "https://restapi.amap.com/v3/geocode/geo?key={key}&address={address}";

        // 创建参数集合
        Map<String, String> params = new HashMap<>();
        params.put("key", KEY);
        params.put("address", "广东省惠州市龙门县南昆山国家森林公园");

        // 创建RestTemplate实例
        RestTemplate restTemplate = new RestTemplate();

        try {
            // 发送GET请求并获取响应
            String result = restTemplate.getForObject(url, String.class, params);

            // 解析JSON响应
            JSONObject jsonObject = new JSONObject(result);
            JSONArray geocodes = jsonObject.getJSONArray("geocodes");

            if (geocodes.length() > 0) {
                JSONObject firstGeocode = geocodes.getJSONObject(0);
                String location = firstGeocode.getString("location");
                System.out.println("地理编码结果：");
                System.out.println("location: " + location);
            } else {
                System.out.println("未找到地理编码结果。");
            }
        } catch (Exception e) {
            System.err.println("请求发生异常：");
            e.printStackTrace();
            // 如果需要自定义异常处理，可以在这里添加
            // throw new RuntimeException("地理编码请求失败", e);
        }
    }
}