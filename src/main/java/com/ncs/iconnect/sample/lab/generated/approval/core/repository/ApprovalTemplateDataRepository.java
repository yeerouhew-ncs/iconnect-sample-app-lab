package com.ncs.iconnect.sample.lab.generated.approval.core.repository;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplate;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplateData;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the ApprovalTemplateData entity.
 */
@Repository
public interface ApprovalTemplateDataRepository extends JpaRepository<ApprovalTemplateData, String> {
    @Query("select t from ApprovalTemplateData t where t.approvalTemplate.id= :templateId order by t.approverSeq asc")
    List<ApprovalTemplateData> findByTemplateId(@Param("templateId") String templateId);
}
