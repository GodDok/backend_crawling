package com.example.godogbe.Service;


import com.example.godogbe.DTO.CheckBoxIdDto;
import com.example.godogbe.DTO.PolicySupportInformationDto;
import com.example.godogbe.Entity.PolicySearch;
import com.example.godogbe.Repository.PolicySearchRepository;
import com.example.godogbe.Repository.StartupSupportInformationRepository;
import lombok.RequiredArgsConstructor;

import org.json.JSONException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import org.json.JSONObject;
import java.time.Duration;


@Service
@RequiredArgsConstructor
public class CrawlingSearchIdService {
    private final PolicySearchRepository policySearchRepository;
    private final StartupSupportInformationRepository startupSupportInformationRepository;
    private WebDriver webDriver;

    public List<PolicySearch> getpolicySearchInfo(CheckBoxIdDto selectionInfo) {
        List<PolicySearch> policySearchDTOList = new ArrayList<>();
        webDriver = getWebDriver("https://finsupport.naver.com/subvention/search");

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        String categoryxPath = selectionInfo.getBigCategory();
        String[] secondCheck = selectionInfo.getCheckBoxId();

        // 체크박스가 나타나길 기다린 후 클릭
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(categoryxPath)));
        webDriver.findElement(By.xpath(categoryxPath)).click();
        sleep(3000);
        String sortingName = "";
        for(int i =0;i<secondCheck.length;i++){
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(secondCheck[i])));
            WebElement sortingbutton = webDriver.findElement(By.xpath(secondCheck[i]));
            if (i == secondCheck.length - 1){
                String sortingInfo = sortingbutton.getAttribute("data-au-custom-map");
                JSONObject sortingInfoJSON = new JSONObject(sortingInfo);
                sortingName = sortingInfoJSON.getString("name");
            }
            sleep(3000);
            clickProcedure(webDriver,sortingbutton);
            sleep(3000);
        }
        sleep(3000);
        //요소들 다 보일때까지 대기
        List<WebElement> links = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("/html/body/div/div[3]/div[3]/div[4]/div[2]/ul/li")));

        for (WebElement link : links) {
            try {
                WebElement inner = link.findElement(By.cssSelector("a[data-au-custom-map]"));
                String dataAuCustomMap = inner.getAttribute("data-au-custom-map");
                if (dataAuCustomMap != null) {
                    JSONObject json = new JSONObject(dataAuCustomMap);
                    String id = json.getString("id");
                    PolicySearch policySearch = PolicySearch.builder()
                            .policyId(id)
                            .searchCondition(sortingName)
                            .build();
                    if(startupSupportInformationRepository.findById(id).isEmpty()){
                        //정책정보DB에 해당 정책 id가 없는 경우에는 정책id내용 크롤링 시작함
                        System.out.println("id에 해당하는 값이 존재하지 않습니다.정보찾기를 수행합니다.");
                        getPolicySupportInfoFromId(id);
                    }
                    //DB에 검색조건과 정책id가 겹치는게 있는지 검색수행
                    if(!policySearchRepository.findByPolicyIdAndSearchCondition(id,sortingName).isEmpty()){
                        //DB에 이미 똑같은 항목이 존재하는걸 발견하는 즉시 for문 종료시켜서 해당 검색조건의 검색수행 중단
                        System.out.println(" "+sortingName+"에 이미 저장된 항목이 존재합니다. procedure 종료");
                        break;
                    }else{
                        policySearchRepository.save(policySearch);
                        policySearchDTOList.add(policySearch);
                    }
                } else {
                    System.out.println("data-au-custom-map attribute is null.");
                }

            } catch (Exception e) {
                System.out.println("Error parsing element data: " + e.getMessage());
            }
        }

        webDriver.quit();
        return policySearchDTOList;
    }

    public String[] minorDistribution(String category){
        if(category.equals("businessType")){
            return new String[]{"개인사업자","법인사업자","창업(예비사업자 포함)","재창업","기존사업자"};
        }else if(category.equals("businessField")){
            return new String[]{"도매 및 소매업","숙박 및 음식점업","예술,스포츠 및 여가관련 서비스업","부동산업","교육서비스업"};
        }else{
            return new String[]{"창원시","창원시 의창구","창원시 성산구","창원시 마산합포구","창원시 마산회원구","창원시 진해구","진주시","통영시","사천시","김해시","밀양시","거제시","양산시","의령군","함안군","창녕군","고성군","남해군","하동군","산청군","함양군","거창군"};
        }

    }

    public String[] getPolicySupportInfo(String[] categories){
        //조건에 해당하는 모든 정책정보 반환
        HashSet<String> resultIdSet = new HashSet<>();
        for(String category : categories){
            List<PolicySearch> bySearchCondition = policySearchRepository.findBySearchCondition(category);
            for(PolicySearch policySearch : bySearchCondition){
                resultIdSet.add(policySearch.getPolicyId());
            }
        }
        return resultIdSet.toArray(new String[0]);
    }

    //주어진 검색조건을 모두 해당하는 정책id를 반환
    public List<String> getPolicySupportWhichQualifiesAll(List<String> categories){
        HashMap<String,Integer> hashMap = new HashMap<>();
        for(String category : categories){
            List<PolicySearch> bySearchCondition = policySearchRepository.findBySearchCondition(category);
            for(PolicySearch policySearch : bySearchCondition){
                hashMap.put(policySearch.getPolicyId(),hashMap.getOrDefault(policySearch.getPolicyId(),0)+1);
            }
        }
        Set<Map.Entry<String, Integer>> entries = hashMap.entrySet();
        List<String> resultList = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : entries){
            if(entry.getValue() == categories.size()){
                resultList.add(entry.getKey());
            }
        }

        return resultList;
    }

    public ArrayList<PolicySupportInformationDto> getPolicySupportInfoFromId(String[] policyIds){
        ArrayList<PolicySupportInformationDto> policySupportInfoList = new ArrayList<>();
        for(String policyId : policyIds){
            policySupportInfoList.add(getPolicySupportInfoFromId(policyId));
        }
        return policySupportInfoList;
    }

    public Page<PolicySupportInformationDto> pagingPolicySupportInfo(Pageable pageable){
        // 페이지네이션 설정
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));

        // 엔티티 페이지 조회
        return startupSupportInformationRepository.findAll(pageRequest)
                .map(entity -> PolicySupportInformationDto.builder()
                        .id(entity.getId())
                        .deadlineForApplication(entity.getDeadlineForApplication())
                        .title(entity.getTitle())
                        .businessOverview(entity.getBusinessOverview())
                        .amount(entity.getAmount())
                        .url(entity.getUrl())
                        .build());

    }

    public Page<PolicySupportInformationDto> pagingSearchPolicyInfoByPolicyId(List<String> policyIds, Pageable pageable){

        // 페이지네이션 설정
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));

        // 엔티티 페이지 조회
        return startupSupportInformationRepository.findByIdIn(policyIds,pageRequest)
                .map(entity -> PolicySupportInformationDto.builder()
                        .id(entity.getId())
                        .deadlineForApplication(entity.getDeadlineForApplication())
                        .title(entity.getTitle())
                        .businessOverview(entity.getBusinessOverview())
                        .amount(entity.getAmount())
                        .url(entity.getUrl())
                        .build());

    }


    /**
     * 정책id를 통해 해당 정책정보에 접근하여 필요한 정보들을 크롤링하는 함수.
     * @param policyId 정책정보id
     * @return PolicySupportInformationDto 정책정보DTO
     */
    private PolicySupportInformationDto getPolicySupportInfoFromId(String policyId){

        WebDriver drive = getWebDriver("https://finsupport.naver.com/subvention/detail/"+policyId);
        WebDriverWait wait = new WebDriverWait(drive, Duration.ofSeconds(10)); // 10초 동안 기다림

        String title = getStringValueFromElement(wait, "//*[@id=\"root\"]/div[3]/div[3]/div[1]/div/p");
        String date = getStringValueFromElement(wait, "//*[@id=\"root\"]/div[3]/div[3]/div[1]/div/div[3]/dl/dd[1]/span[1]");
        String businessOverview = getStringValueFromElement(wait, "//*[@id=\"root\"]/div[3]/div[3]/div[2]/div[1]/div/div[3]/p");
        String amount = getStringValueFromElement(wait, "//*[@id=\"root\"]/div[3]/div[3]/div[1]/div/ul/li[2]/div[3]/span");

        WebElement button = drive.findElement(By.xpath("//*[@id=\"root\"]/div[3]/div[3]/div[1]/div/div[4]/button[2]"));
        // data-au-custom-map 속성 값 가져오기
        String dataValue = button.getAttribute("data-au-custom-map");
        //정책정보 원본 사이트 주소
        String redirectUrl = "";
        try {
            // JSON 파싱
            JSONObject jsonObj = new JSONObject(dataValue);
            redirectUrl = jsonObj.getString("redirect_url");
            System.out.println("Redirect URL: " + redirectUrl);
        } catch (JSONException e) {
            System.out.println("Error parsing JSON");
            e.printStackTrace();
        }
        PolicySupportInformationDto policySupportInformationDTO = new PolicySupportInformationDto(policyId,date,title,businessOverview,amount,redirectUrl);
        startupSupportInformationRepository.save(policySupportInformationDTO.toEntity(policySupportInformationDTO));
        drive.quit();
        return policySupportInformationDTO;
    }

    /**
     * XPath에 해당하는 요소가 나타날때까지 기다렸다가
     * @param wait driver와 몇초기다릴지가 설정되어 있는 WebDriverWait 인스턴스
     * @param XPath 값을 추출해낼 XPath 경로
     * @return 추출해낸 문자열값
     */
    private static String getStringValueFromElement(WebDriverWait wait, String XPath) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPath)));
        return element.getText();
    }

    private static void sleep(long sleepTime) {
        try {
            Thread.sleep(sleepTime); // Delay added here before the first interaction
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
        }
    }

    /**
     * 웹드라이버에서 클릭을 수행하려고 하는데 화면에 나타나지 않은 경우에 자바스크립트를 사용해서 강제로 버튼 클릭 수행
     * @param webDriver 클릭을 수행하는 웹드라이버
     * @param button 선택한 버튼요소
     */
    private static void  clickProcedure(WebDriver webDriver, WebElement button) {
        try{
            button.click();
        } catch (ElementClickInterceptedException e){
            //클릭 안되는 에러 발생한 경우 자바스크립트를 사용해서 강제로 버튼 클릭
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", button);
        }
    }

    private static WebDriver getWebDriver(String url) {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");        // 헤드리스 모드 활성화
        options.addArguments("--disable-gpu"); // GPU 하드웨어 가속 비활성화, 불필요한 리소스 사용 감소
        options.addArguments("--no-sandbox"); // 샌드박스 비활성화
        options.addArguments("--window-size=1440,788"); // 브라우저 창 크기 설정

        WebDriver webDriver = new ChromeDriver(options);
        webDriver.get(url);
        return webDriver;
    }

    private static Optional<LocalDate> convertStringToDate(String dateString) {
        //  날짜 문자열을 LocalDate객체로 저장해서 돌려주는 함수
        if (dateString == null) {
            return Optional.empty();  // dateString가 null인 경우 빈 Optional 반환
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            return Optional.of(date);  // LocalDate.parse()가 null을 반환하지 않으므로 Optional.of() 사용
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format");
            return Optional.empty();  // 날짜 형식이 잘못된 경우 빈 Optional 반환
        }
    }


}
