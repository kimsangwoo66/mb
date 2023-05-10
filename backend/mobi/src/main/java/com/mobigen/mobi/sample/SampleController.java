package com.mobigen.mobi.sample;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor // autowired 대신 처리
@RestController
public class SampleController {

    private final SampleService sampleService;
//    @GetMapping("/sample")
//    public void gList() throws URISyntaxException {
//
//        System.out.println("왜 안나옴?");
//        sampleService.getList();
//    }


    // 등록자별 이슈 개수 api
    @GetMapping("/api/column")
    public Map gList() throws URISyntaxException {

        System.out.println("차트 컨트롤러 동작");
        return sampleService.getList();
    }

    // 담당자별 이슈 개수 api
    @GetMapping("/api/pie")
    public Map pList() throws URISyntaxException {
        System.out.println("pie 리소스 동작");

        return sampleService.getPieList();
    }

    // 일별 이슈 등록 개수 api
    @GetMapping("/api/line")
    public Map lList() throws URISyntaxException {
        System.out.println("line 리소스 동작");

        return sampleService.getLlist();
    }

    // 라벨 필터링 가져오기 api
    @GetMapping("/api/onLabels")
    public ArrayList onLabelList() throws URISyntaxException {
        System.out.println("라벨 리소스 동작");

        return sampleService.getOnLabelList();
    }

    // 이슈 검색 api
    @PostMapping("/api/searchIssue")
    public ArrayList sList(@RequestBody Map<String, String> requestData) throws URISyntaxException, ParseException, IOException {
        String stateValue = requestData.get("stateValue");
        String labelValue = requestData.get("labelValue");
        String startDate = requestData.get("startDate");
        String endDate = requestData.get("endDate");
        String currentPage = requestData.get("currentPage");

        System.out.println("state: " + stateValue + " label: " + labelValue + " startDate: "
                + startDate + " endDate: " + endDate + " currentPage: " + currentPage);

        return sampleService.searchList(stateValue, labelValue, startDate, endDate, currentPage);



    }





}