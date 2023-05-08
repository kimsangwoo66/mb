package com.mobigen.mobi.sample;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class SampleService {


    @Value("${git.tokenvalue}")
    private String token;
    String owner = "mobigen";
    String repo = "IRIS-Analyzer";
    String apiUrl = "https://api.github.com/repos/" + owner + "/" + repo + "/issues";



    public Map getList() throws URISyntaxException {

        ArrayList allData = new ArrayList();
        int dataCount = 0;
        int page = 1;

        Map<String, Integer> registerCount = new HashMap<>();


        //git api를 사용하여 필요 데이터 정재 후 allData 리스트에 삽입
        //{number=1583, state="closed", title="제목", closeAt=2023-03-08, content="내용",
        // createAt=2023-03-08, register="veldise", labels=["개선","진행중"]} -> allData
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


                            String createdAt = String.valueOf(node.get("created_at"))
                                    .substring(1, node.get("created_at").asText().length())
                                    .trim();

                            String closedAt;
                            String trans_closedAt;


                            LocalDateTime createDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME);
                            String trans_createdAt = createDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


                            JsonNode closedAtNullCheck = node.get("closed_at");
                            if("null".equals(closedAtNullCheck.toString())){

                                trans_closedAt = "";

                            }
                            else{
                                        closedAt = String.valueOf(node.get("closed_at"))
                                        .substring(1, node.get("closed_at").asText().length())
                                        .trim();
                                LocalDateTime closeDateTime = LocalDateTime.parse(closedAt, DateTimeFormatter.ISO_DATE_TIME);
                                trans_closedAt = closeDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            }



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
//        System.out.println("dataCount: " + dataCount);




        for(Object jObject : allData){
            String register = ((TextNode) ((Map<String, JsonNode>) jObject).get("register")).asText();
            // 맵에 해당 register가 있는지 확인하고, 있으면 값을 1 증가
            if (registerCount.containsKey(register)) {
                registerCount.put(register, registerCount.get(register) + 1);
            } else {
                registerCount.put(register, 1);
            }
        }

        // 결과를 출력
        for (String register : registerCount.keySet()) {
            int count = registerCount.get(register);
            //System.out.println(register + ": " + count);

        }
        System.out.println("======================");
        System.out.println(registerCount.toString());

        return registerCount;
    }


    public Map getPieList() throws URISyntaxException {

        ArrayList allData = new ArrayList();
        int dataCount = 0;
        int page = 1;

        Map<String, Integer> registerCount = new HashMap<>();

        //git api를 사용하여 필요 데이터 정재 후 allData 리스트에 삽입
        //{number=1583, state="closed", title="제목", closeAt=2023-03-08, content="내용",
        // createAt=2023-03-08, register="veldise", labels=["개선","진행중"]} -> allData
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


                            String createdAt = String.valueOf(node.get("created_at"))
                                    .substring(1, node.get("created_at").asText().length())
                                    .trim();

                            String closedAt;
                            String trans_closedAt;


                            LocalDateTime createDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME);
                            String trans_createdAt = createDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


                            JsonNode closedAtNullCheck = node.get("closed_at");
                            if("null".equals(closedAtNullCheck.toString())){
                                trans_closedAt = "";

                            }
                            else{
                                closedAt = String.valueOf(node.get("closed_at"))
                                        .substring(1, node.get("closed_at").asText().length())
                                        .trim();
                                LocalDateTime closeDateTime = LocalDateTime.parse(closedAt, DateTimeFormatter.ISO_DATE_TIME);
                                trans_closedAt = closeDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            }




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

//        System.out.println(allData.toString());
//        System.out.println("dataCount: " + dataCount);

        for(Object jObject : allData){
            String register = ((TextNode) ((Map<String, JsonNode>) jObject).get("register")).asText();
            // 맵에 해당 register가 있는지 확인하고, 있으면 값을 1 증가
            if (registerCount.containsKey(register)) {
                registerCount.put(register, registerCount.get(register) + 1);
            } else {
                registerCount.put(register, 1);
            }
        }

        // 결과를 출력
        for (String register : registerCount.keySet()) {
            int count = registerCount.get(register);
//            System.out.println(register + ": " + count);

        }
//        System.out.println("======================");
//        System.out.println(registerCount.toString());



        return registerCount;
    }

    public Map getLlist() throws URISyntaxException {
        ArrayList allData = new ArrayList();
        int dataCount = 0;
        int page = 1;

        Map<String, Integer> createAtCount = new HashMap<>();


        //git api를 사용하여 필요 데이터 정재 후 allData 리스트에 삽입
        //{number=1583, state="closed", title="제목", closeAt=2023-03-08, content="내용",
        // createAt=2023-03-08, register="veldise", labels=["개선","진행중"]} -> allData
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


                            String createdAt = String.valueOf(node.get("created_at"))
                                    .substring(1, node.get("created_at").asText().length())
                                    .trim();

                            String closedAt;
                            String trans_closedAt;


                            LocalDateTime createDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME);
                            String trans_createdAt = createDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


                            JsonNode closedAtNullCheck = node.get("closed_at");
                            if("null".equals(closedAtNullCheck.toString())){

                                trans_closedAt = "";

                            }
                            else{
                                closedAt = String.valueOf(node.get("closed_at"))
                                        .substring(1, node.get("closed_at").asText().length())
                                        .trim();
                                LocalDateTime closeDateTime = LocalDateTime.parse(closedAt, DateTimeFormatter.ISO_DATE_TIME);
                                trans_closedAt = closeDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            }




                            JsonNode labels = node.get("labels");
                            List labelList = new ArrayList();
                            for (JsonNode label : labels) {

                                labelList.add(label.get("name"));

                            }


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

        //System.out.println(allData.toString());
        System.out.println("dataCount: " + dataCount);


        for(Object jObject : allData){

            String createTime = (String) ((Map<String, Object>) jObject).get("createAt");
            // 맵에 해당 createAt가 있는지 확인하고, 있으면 값을 1 증가
            if (createAtCount.containsKey(createTime)) {
                createAtCount.put(createTime, createAtCount.get(createTime) + 1);
            } else {
                createAtCount.put(createTime, 1);
            }
        }

        // 결과를 출력
        for (String createTime : createAtCount.keySet()) {
            int count = createAtCount.get(createTime);
//            System.out.println(createTime + ": " + count);

        }
//        System.out.println("======================");
//        System.out.println(createAtCount.toString());

        return createAtCount;
    }



    public void searchList(String stateValue, String labelValue, String startDate, String endDate) throws URISyntaxException {
        ArrayList allData = new ArrayList();
        int dataCount = 0;
        int page = 1;

        Map<String, Integer> searchResult = new HashMap<>();


        //git api를 사용하여 필요 데이터 정재 후 allData 리스트에 삽입
        //{number=1583, state="closed", title="제목", closeAt=2023-03-08, content="내용",
        // createAt=2023-03-08, register="veldise", labels=["개선","진행중"]} -> allData
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


                            String createdAt = String.valueOf(node.get("created_at"))
                                    .substring(1, node.get("created_at").asText().length())
                                    .trim();

                            String closedAt;
                            String trans_closedAt;


                            LocalDateTime createDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME);
                            String trans_createdAt = createDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


                            JsonNode closedAtNullCheck = node.get("closed_at");
                            if("null".equals(closedAtNullCheck.toString())){

                                trans_closedAt = "";

                            }
                            else{
                                closedAt = String.valueOf(node.get("closed_at"))
                                        .substring(1, node.get("closed_at").asText().length())
                                        .trim();
                                LocalDateTime closeDateTime = LocalDateTime.parse(closedAt, DateTimeFormatter.ISO_DATE_TIME);
                                trans_closedAt = closeDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            }



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

        //System.out.println(allData.toString());
        System.out.println("==========서치===============");
        for(Object jObject : allData){

            String jObject_state = ((TextNode) ((Map<String, JsonNode>) jObject).get("state")).asText();
            List<JsonNode> labels = (List<JsonNode>) ((Map<?, ?>) jObject).get("labels");


            // open or closed 일경우
            if (stateValue.equals(jObject_state)){
                // labels에 labelValue가 포함되어 있을 경우
                if (labels.stream().anyMatch(label -> label.asText().equalsIgnoreCase(labelValue))) {
                    System.out.println(jObject.toString());
                }



            }
            //all 일 경우
            else if(stateValue.equals("all")){

                if (labels.stream().anyMatch(label -> label.asText().equalsIgnoreCase(labelValue))) {
                    System.out.println(jObject.toString());
                }
            }


        }



    }


}
