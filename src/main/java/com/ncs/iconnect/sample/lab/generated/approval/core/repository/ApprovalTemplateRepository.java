package com.ncs.iconnect.sample.lab.generated.approval.core.repository;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the ApprovalTemplate entity.
 */
@Repository
public interface ApprovalTemplateRepository extends JpaRepository<ApprovalTemplate, String> {
    @Query("select t from ApprovalTemplate t where t.requestTypeKey= :requestTypeKey and t.templateKey= :templateKey")
    Optional<ApprovalTemplate> findByRequestTypeAndTemplate(@Param("requestTypeKey") String requestTypeKey, @Param("templateKey") String templateKey);
}

