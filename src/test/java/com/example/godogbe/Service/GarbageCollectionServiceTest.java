package com.example.godogbe.Service;

import com.example.godogbe.Entity.PolicySupportInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GarbageCollectionServiceTest {

    @Autowired
    GarbageCollectionService garbageCollectionService;

    @Test
    void getOutdatedPolicySupportInformation() {
        List<String> outdatedPolicySupportInformation = garbageCollectionService.getOutdatedPolicySupportInformation();
        for(String policySupportInformation : outdatedPolicySupportInformation){
            System.out.println("policySupportInformation = " + policySupportInformation);
        }
    }
}