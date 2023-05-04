package com.mobigen.mobi.sample;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class SampleService {


    @Value("${git.tokenvalue}")
    private String token;

    public Map getList() throws URISyntaxException {
        String owner = "mobigen";
        String repo = "IRIS-Analyzer";
        String apiUrl = "https://api.github.com/repos/" + owner + "/" + repo + "/issues";
        ArrayList allData = new ArrayList();
        int dataCount = 0;
        int page = 1;

        Map<String, Integer> registerCount = new HashMap<>();



        while (true) {

            URIBuilder builder = new URIBuilder(apiUrl);
            builder.setParameter("state", "all");
            builder.setParameter("per_page", "100");
            builder.setParameter("page", Integer.toString(page));


            try {
                URI uri = builder.build();
                HttpGet request = new HttpGet(uri);
                request.addHeader("Authorization", "Bearer " + token);

                HttpClient httpClient = HttpClients.createDefault();
                HttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    ObjectMapper objectMapper = new ObjectMapper();

                    JsonNode jsonNode = objectMapper.readTree(result);



                    if (jsonNode.size() > 0) {
                        for (JsonNode node : jsonNode) {
//                            System.out.println(node.toString());
//                            System.out.println(node.get("number"));
//                            System.out.println(node.get("user").get("login"));
//                            System.out.println("=============");

                            String createdAt = String.valueOf(node.get("created_at"))
                                    .substring(1, node.get("created_at").asText().length())
                                    .trim();

                            String closedAt;
                            String trans_closedAt;


                            LocalDateTime createDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME);
                            String trans_createdAt = createDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


                            JsonNode closedAtNullCheck = node.get("closed_at");
                            if("null".equals(closedAtNullCheck.toString())){
                                //System.out.println("yes null");
                                //trans_closedAt = String.valueOf(node.get("closed_at"));
                                trans_closedAt = "";

                            }
                            else{
                                        closedAt = String.valueOf(node.get("closed_at"))
                                        .substring(1, node.get("closed_at").asText().length())
                                        .trim();
                                LocalDateTime closeDateTime = LocalDateTime.parse(closedAt, DateTimeFormatter.ISO_DATE_TIME);
                                trans_closedAt = closeDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            }

                            //System.out.println(trans_closedAt);


                            JsonNode labels = node.get("labels");
                            List labelList = new ArrayList();
                            for (JsonNode label : labels) {

                                labelList.add(label.get("name"));

                            }
                            //System.out.println("labelList확인: " + labelList.toString());


                            Map<String, Object> data = new HashMap<>();
                            data.put("number", String.valueOf(node.get("number")));
                            data.put("title", node.get("title"));
                            data.put("register", node.get("user").get("login"));
                            data.put("content", node.get("body"));

                            data.put("createAt", trans_createdAt);
                            data.put("closeAt", trans_closedAt);

                            data.put("state", node.get("state"));
                            data.put("labels", labelList);
                            allData.add(data);

                        }
                    } else {
                        break;
                    }

                    page += 1;
                    dataCount += jsonNode.size();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        System.out.println(allData.toString());
        System.out.println("dataCount: " + dataCount);




        for(Object jObject : allData){
            String register = ((TextNode) ((Map<String, JsonNode>) jObject).get("register")).asText();
            // 맵에 해당 register가 있는지 확인하고, 있으면 값을 1 증가
            if (registerCount.containsKey(register)) {
                registerCount.put(register, registerCount.get(register) + 1);
            } else {
                registerCount.put(register, 1);
            }
        }

        // 결과를 출력합니다.
        for (String register : registerCount.keySet()) {
            int count = registerCount.get(register);
            System.out.println(register + ": " + count);

        }
        System.out.println("======================");
        System.out.println(registerCount.toString());
        /*
        * 등록자 별 이슈 개수 카운트 후 리스트에 삽입
        *
        *
        *
        * */



        //return allData;
        return registerCount;
    }


}
