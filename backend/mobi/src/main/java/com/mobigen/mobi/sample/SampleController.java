package com.mobigen.mobi.sample;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
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

    @GetMapping("/api/column")
    public Map gList() throws URISyntaxException {

        System.out.println("차트 컨트롤러 동작");
        return sampleService.getList();
    }

    @GetMapping("/api/pie")
    public Map pList() throws URISyntaxException {
        System.out.println("pie 리소스 동작");

        return sampleService.getPieList();
    }

    @GetMapping("/api/line")
    public Map lList() throws URISyntaxException {
        System.out.println("line 리소스 동작");

        return sampleService.getLlist();
    }

    @GetMapping("/api/onLabels")
    public ArrayList onLabelList() throws URISyntaxException {
        System.out.println("라벨 리소스 동작");

        return sampleService.getOnLabelList();
    }

    @PostMapping("/api/searchIssue")
    public void sList(@RequestBody Map<String, String> requestData) throws URISyntaxException {
        String stateValue = requestData.get("stateValue");
        String labelValue = requestData.get("labelValue");
        String startDate = requestData.get("startDate");
        String endDate = requestData.get("endDate");

        System.out.println("state: " + stateValue + " label: " + labelValue + " startDate: "
                + startDate + " endDate: " + endDate);

        sampleService.searchList(stateValue, labelValue, startDate, endDate);



    }





}