package com.example.godogbe.etc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryTransformer {
    private static Map<String, String> categoryMap = new HashMap<>();
    static {
        categoryMap.put("소매", "도매 및 소매업");
        categoryMap.put("음식", "숙박 및 음식점업");
        categoryMap.put("숙박", "숙박 및 음식점업");
        categoryMap.put("수리·개인", "협회 및 단체, 수리 및 기타 개인 서비스업");
        categoryMap.put("예술·스포츠", "예술, 스포츠 및 여가관련 서비스업");
        categoryMap.put("교육", "교육 서비스업");
        categoryMap.put("부동산", "부동산업");
        categoryMap.put("과학·기술", "전문,과학 및 기술 서비스업");
        categoryMap.put("보건의료", "보건업 및 사회복지 서비스업");
        categoryMap.put("시설관리·임대", "사업시설 관리, 사업 지원 및 임대 서비스업");
    }

    public static List<String> sortMatching(List<String> categories) {
        List<String> resultList = new ArrayList<>();
        for (String category : categories) {
            String matchedCategory = categoryMap.getOrDefault(category, category);
            resultList.add(matchedCategory);
        }
        return resultList;
    }
}
