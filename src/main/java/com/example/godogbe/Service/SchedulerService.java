package com.example.godogbe.Service;


import com.example.godogbe.DTO.CheckBoxIdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    String[] bigSortCriteria = new String[]{"/html/body/div/div[3]/div[3]/div[3]/div/div[2]/div/ul/li[1]/button","/html/body/div/div[3]/div[3]/div[3]/div/div[2]/div/ul/li[2]/button","/html/body/div/div[3]/div[3]/div[3]/div/div[2]/div/ul/li[3]/button"};
    String[] businessType = new String[]{"/html/body/div/div[3]/div[3]/div[3]/div/div[2]/div[2]/ul[1]/li[1]/button","/html/body/div/div[3]/div[3]/div[3]/div/div[2]/div[2]/ul[1]/li[2]/button","/html/body/div/div[3]/div[3]/div[3]/div/div[2]/div[2]/ul[2]/li[1]/button","/html/body/div/div[3]/div[3]/div[3]/div/div[2]/div[2]/ul[2]/li[2]/button","/html/body/div/div[3]/div[3]/div[3]/div/div[2]/div[2]/ul[2]/li[3]/button"};
    int[] businessField = new int[]{1,2,4,5,6,7,8,13,14};
    String location1 = "/html/body/div/div[3]/div[3]/div[3]/div/div[2]/div[2]/div/ul/li[14]/button";
    String location2 = "/html/body/div/div[3]/div[3]/div[3]/div/div[2]/div[2]/div/ul[2]/li[2]/button";
    //마지막 li[2]~li[24]까지

    @Autowired
    CrawlingSearchIdService crawlingSearchIdService;
    //3600000
    @Scheduled(fixedDelay = 3600000)
    public void crawling() {

        //사업유형 크롤링
        for (int i = 0; i < businessType.length; i++) {
            String[] arr = new String[]{businessType[i]};
            CheckBoxIdDto transfer = new CheckBoxIdDto(bigSortCriteria[0],arr);
            crawlingSearchIdService.getpolicySearchInfo(transfer);
        }

        for (int i = 0; i < businessField.length; i++) {
            String businessloc = "//*[@id=\"root\"]/div[3]/div[3]/div[3]/div/div[2]/div[2]/div[3]/ul/li["+businessField[i]+"]/div";
            String[] arr = new String[]{businessloc};
            CheckBoxIdDto transfer = new CheckBoxIdDto(bigSortCriteria[1],arr);
            crawlingSearchIdService.getpolicySearchInfo(transfer);
        }

        for (int i =2 ; i<=24;i++){
            String loc2 = "/html/body/div/div[3]/div[3]/div[3]/div/div[2]/div[2]/div/ul[2]/li[" + i + "]/button";
            String[] arr = new String[]{location1,loc2};
            CheckBoxIdDto transfer = new CheckBoxIdDto(bigSortCriteria[2],arr);
            crawlingSearchIdService.getpolicySearchInfo(transfer);
        }
    }


}
