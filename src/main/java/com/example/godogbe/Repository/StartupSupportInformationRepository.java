package com.example.godogbe.Repository;

import com.example.godogbe.Entity.PolicySearch;
import com.example.godogbe.Entity.PolicySupportInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StartupSupportInformationRepository extends JpaRepository<PolicySupportInformation,String> {
    @Query("SELECT p FROM PolicySupportInformation p WHERE p.id IN :ids")
    Page<PolicySupportInformation> findByIdIn(List<String> ids, Pageable pageable);

    @Modifying
    @Query("delete from PolicySupportInformation c where c.id in :ids")
    void deleteAllByIds(@Param("ids") List<String> ids);

}
