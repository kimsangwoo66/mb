package com.mobigen.mobi.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class SampleService {

    @Value("${git.tokenvalue}")
    private String token;

    private String apiUrl = "https://api.github.com/repos/mobigen/IRIS-Analyzer/issues";
    // https://api.github.com/repos/mobigen/IRIS-Analyzer/issues

    private ArrayList allData;

    //service 클래스 초ㅓ기화 시점에 데이터를 한번만 가져와서 캐시
    @PostConstruct
    public void init() throws URISyntaxException {
        allData = getNeedList(); // 초기화 중에 데이터를 한 번 가져옴
    }

    public ArrayList getNeedList() throws URISyntaxException {

        if(allData != null){
            return allData;
        }

        ArrayList findAllData = new ArrayList();

        int page = 1;
        int dataCount = 0;


//

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
                System.out.println("uri: " + uri);
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
                            findAllData.add(data);

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

        return findAllData;

    }

    public Map getColumnList() throws URISyntaxException {


        Map<String, Integer> registerCount = new HashMap<>();

        for(Object jObject : allData){
            String register = ((TextNode) ((Map<String, JsonNode>) jObject).get("register")).asText();
            // 맵에 해당 register가 있는지 확인하고, 있으면 값을 1 증가
            if (registerCount.containsKey(register)) {
                registerCount.put(register, registerCount.get(register) + 1);
            } else {
                registerCount.put(register, 1);
            }
        }

        return registerCount;
    }



    public Map getPieList() throws URISyntaxException {


        Map<String, Integer> assignCount = new HashMap<>();

        for(Object jObject : allData){
            String register = ((TextNode) ((Map<String, JsonNode>) jObject).get("register")).asText();
            // 맵에 해당 register가 있는지 확인하고, 있으면 값을 1 증가
            if (assignCount.containsKey(register)) {
                assignCount.put(register, assignCount.get(register) + 1);
            } else {
                assignCount.put(register, 1);
            }
        }

        return assignCount;
    }


    public Map getLinelist() throws URISyntaxException {


        Map<String, Integer> createAtCount = new HashMap<>();

        for(Object jObject : allData){

            String createTime = (String) ((Map<String, Object>) jObject).get("createAt");
            // 맵에 해당 createAt가 있는지 확인하고, 있으면 값을 1 증가
            if (createAtCount.containsKey(createTime)) {
                createAtCount.put(createTime, createAtCount.get(createTime) + 1);
            } else {
                createAtCount.put(createTime, 1);
            }
        }

        return createAtCount;
    }


    public ArrayList getOnLabelList() throws URISyntaxException {


        ArrayList noDuplicateLabels = new ArrayList();
        ObjectMapper objectMapper = new ObjectMapper();

        String getLabelUri = "https://api.github.com/repos/mobigen/IRIS-Analyzer/labels";
        URIBuilder builder = new URIBuilder(getLabelUri);

        try {
            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.addHeader("Authorization", "Bearer " + token);

            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if(entity !=null){
                String result = EntityUtils.toString(entity);
                JsonNode jsonNode = objectMapper.readTree(result);

                for(JsonNode node: jsonNode){
                    noDuplicateLabels.add(node.get("name"));
                }
            }

        }catch (Exception e){
            e.printStackTrace();

        }


        return noDuplicateLabels;

    }


    public ArrayList searchList(String stateValue, String labelValue, String startDate, String endDate, String currentPage) throws URISyntaxException {

        ArrayList searchResult = new ArrayList();

        String searchUrl = "https://api.github.com/search/issues?q=repo:mobigen/IRIS-Analyzer+type:issue,pr";

        /*
         * 필터 조건
         * */
        // open 이거나 closed 일 경우
        if(!"all".equals(stateValue))
        {
            searchUrl = searchUrl + "+state:"+stateValue;
        }

        //라벨이 존재할 경우
        if(labelValue.isEmpty() == false){
            searchUrl = searchUrl + "+label:"+labelValue;
        }

        // startDate와 endDate 둘다 지정했을 경우
        if(startDate.isEmpty() == false && endDate.isEmpty() == false)
        {
            searchUrl = searchUrl + "+created:" + startDate + ".." + endDate;
        }
        // endDate만 지정했을 경우
        else if(startDate.isEmpty() == true && endDate.isEmpty() == false){
            // url 인코딩 -> %3C%3D == "<="
            searchUrl = searchUrl + "+created:%3C%3D" + endDate;
        }
        // startDate만 지정 했을 경우
        else if(startDate.isEmpty() == false && endDate.isEmpty() == true){
            // url 인코딩 -> %3E%3D == ">="
            searchUrl = searchUrl + "+created:%3E%3D" + startDate;
        }

        searchUrl = searchUrl + "&per_page=10&page="+currentPage;
        System.out.println("searchUrl: " + searchUrl);

        /**
         * searchUrl 최종 참고
         * https://api.github.com/search/issues?q=repo:mobigen/IRIS-Analyzer+type:issue,pr
         * +state:open+label:개선+created:2016-07-01..2018-07-11&per_page=10&page=1
         *
         */


        URIBuilder builder = new URIBuilder(searchUrl);

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
                JsonNode items = jsonNode.get("items");


                for(JsonNode node:items){
                    Map searchObj = new HashMap();


                    JsonNode sNumber = node.get("number");
                    JsonNode sTitle = node.get("title");
                    JsonNode sRegister = node.get("user").get("login");
                    JsonNode sCreatedAt = node.get("created_at");
                    JsonNode sClosedAt = node.get("closed_at");
                    JsonNode sState = node.get("state");
                    JsonNode sLabels = node.get("labels");


                    String YMD_createdAt = sCreatedAt.asText().split("T")[0];

                    if(sClosedAt.isNull() == false){
                        String YMD_closedAt = sClosedAt.asText().split("T")[0];
                        searchObj.put("closeAt", YMD_closedAt);
                    }
                    else{
                        searchObj.put("closeAt", sClosedAt);
                    }



                    List labelList = new ArrayList();
                    for (JsonNode label : sLabels) {

                        labelList.add(label.get("name"));

                    }

                    searchObj.put("number", sNumber);
                    searchObj.put("title", sTitle);
                    searchObj.put("register", sRegister);
                    searchObj.put("createAt", YMD_createdAt);
                    searchObj.put("state", sState);
                    searchObj.put("labels", labelList);


                    searchResult.add(searchObj);
                }
                Map totalCnt = new HashMap();
                totalCnt.put("totalCnt", jsonNode.get("total_count"));
                searchResult.add(totalCnt);



            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return searchResult;
    }

}

