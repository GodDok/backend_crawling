package com.example.godogbe.Service;

import com.example.godogbe.DTO.PolicySupportInformationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CrawlingSearchIdServiceTest {

    @Autowired
    private CrawlingSearchIdService crawlingSearchIdService;


    @Test
    @DisplayName("입력하는 분류에 해당하는 정책정보를 정상적으로 반환하는지 확인")
    void getPolicySupportWhichQualifiesAll() {
        //given
        List<String> input = new ArrayList<>();
        input.add("창원시");
        //when
        List<String> result = crawlingSearchIdService.getPolicySupportWhichQualifiesAll(input);
        //then
        assertThat(result).isNotNull();
    }


    @Test
    @DisplayName("정책정보가 정상적으로 조회되는지 확인")
    void pagingPolicySupportInfo() {
        //given
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<PolicySupportInformationDto> result = crawlingSearchIdService.pagingPolicySupportInfo(pageable);

        //then
        assertThat(result.getNumberOfElements()).isEqualTo(10);

    }

}