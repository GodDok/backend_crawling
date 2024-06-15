package com.example.godogbe.Controller;

import com.example.godogbe.DTO.PolicySupportInformationDto;
import com.example.godogbe.Service.CrawlingSearchIdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StartupSupportInformationControllerTest {

    @InjectMocks
    private StartupSupportInformationController startupSupportInformationController;

    @Mock
    private CrawlingSearchIdService crawlingSearchIdService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders
                .standaloneSetup(startupSupportInformationController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("카테고리들을 모두 만족하는 검색결과 api 작동테스트")
    void searchByCateogires() throws Exception {
    //given
        List<String> categories = new ArrayList<>();
        categories.add("창원시");
        Pageable pageable = PageRequest.of(0,1);

        Page<PolicySupportInformationDto> response = response();
        List<String> mockPolicyIds = List.of("policyId1","policyId2");
        doReturn(mockPolicyIds).when(crawlingSearchIdService).getPolicySupportWhichQualifiesAll(ArgumentMatchers.<List<String>>any());
        doReturn(response).when(crawlingSearchIdService).pagingSearchPolicyInfoByPolicyId(ArgumentMatchers.<List<String>>any(),any(Pageable.class));
    //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/searchQualifiesAllCondition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("categories","창원시")
                        .param("page","0")
                        .param("size","1")
        );

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.content.length()").value(1))
                .andReturn();

    }
    private Page<PolicySupportInformationDto> response(){
        List<PolicySupportInformationDto> dtoList = List.of(
                new PolicySupportInformationDto("1", "2023-12-31", "Title1", "Business Overview 1", "1000", "http://example.com/1", "Institution1", "Location1") );
        Pageable pageable = PageRequest.of(0,1);
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }
}