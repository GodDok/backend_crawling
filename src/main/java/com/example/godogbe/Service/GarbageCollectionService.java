package com.example.godogbe.Service;


import com.example.godogbe.Entity.PolicySupportInformation;
import com.example.godogbe.Repository.PolicySearchRepository;
import com.example.godogbe.Repository.StartupSupportInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GarbageCollectionService {

    private final PolicySearchRepository policySearchRepository;
    private final StartupSupportInformationRepository startupSupportInformationRepository;

    public List<String> getOutdatedPolicySupportInformation() {
        List<String> result = new LinkedList<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        List<PolicySupportInformation> policySupportInformations = startupSupportInformationRepository.findAll();

        for(PolicySupportInformation policySupportInformation : policySupportInformations) {
            String dateString = policySupportInformation.getDeadlineForApplication();

            try {
                LocalDate date = LocalDate.parse(dateString, formatter);

                if (date.isBefore(today)||date.isEqual(today)) {
                    result.add(policySupportInformation.getId());
                }
            } catch (DateTimeParseException e) {

            }

        }

        return result;
    }

    public void removeOutdatedPolicies(List<String> ids){
        policySearchRepository.deleteAllByIds(ids);
        startupSupportInformationRepository.deleteAllByIds(ids);
    }
}
