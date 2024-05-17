package com.example.godogbe.Controller;


import com.example.godogbe.DTO.CheckBoxIdDto;
import com.example.godogbe.DTO.PolicySupportInformationDto;
import com.example.godogbe.DTO.searchQualifiesAllConditionRequestDto;
import com.example.godogbe.Entity.PolicySearch;
import com.example.godogbe.Service.CrawlingSearchIdService;
import com.example.godogbe.etc.CategoryTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StartupSupportInformationController {

    private final CrawlingSearchIdService crawlingSearchIdService;

    //중분류Xpath와 소분류Xpath배열을 가지고 있는 CheckBoxId를 입력하면 해당하는 결과를 반환함
    @GetMapping(value = "/search")
    public ResponseEntity<List<PolicySearch>> searchInfo(@RequestBody CheckBoxIdDto checkBoxIdDto) {
        return ResponseEntity.ok(crawlingSearchIdService.getpolicySearchInfo(checkBoxIdDto));
    }

    //사업 종류 업데이트 필요
    //중분류 정보를 입력하면 소분류 정보를 반환함
    @GetMapping(value = "/searchOption")
    public ResponseEntity<String[]> searchOption(@RequestParam String searchOptionName) {
        String[] result = crawlingSearchIdService.minorDistribution(searchOptionName);
        if (result == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        return ResponseEntity.ok(result);
    }

    //소분류 목록을 보내면 해당하는 정책정보id 반환함
    @GetMapping(value = "/searchByCategory")
    public ResponseEntity<String[]> searchByCategory(@RequestParam String category) {
        String[] categoryinArr = new String[]{category};
        String[] result = crawlingSearchIdService.getPolicySupportInfo(categoryinArr);
        if (result == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
       return ResponseEntity.ok(result);
    }

    //모든 조건을 만족하는 정책정보반환
    @GetMapping(value = "/searchQualifiesAllCondition")
    public Page<PolicySupportInformationDto> searchByCateogires(searchQualifiesAllConditionRequestDto categories, Pageable pageable){
        List<String> convertedCategories = CategoryTransformer.sortMatching(categories.getCategories());
        List<String> resultPolicyId = crawlingSearchIdService.getPolicySupportWhichQualifiesAll(convertedCategories);
        return crawlingSearchIdService.pagingSearchPolicyInfoByPolicyId(resultPolicyId,pageable);
    }

    //localhost:8080/getAllPolicies?page=1&size=20&sort=id,desc 이런식으로  페이지 호출하면됨
    //전체 정책정보 반환
    @GetMapping(value ="/getAllPolicies")
    public Page<PolicySupportInformationDto> getAllPolicySupportInformation(Pageable pageable) {
        return crawlingSearchIdService.pagingPolicySupportInfo(pageable);
    }

    //localhost:8080/getAllPolicies?page=1&size=20&sort=id,desc 이런식으로  페이지 호출하면됨
    //정책정보id들 보내면 해당하는 정책정보반환함
    @GetMapping (value="searchPolicyByPolicyId")
    public Page<PolicySupportInformationDto> getPolicySupportInformationByPolicyId(@RequestBody List<String> policyIds, Pageable pageable) {
        return crawlingSearchIdService.pagingSearchPolicyInfoByPolicyId(policyIds,pageable);
    }

}
