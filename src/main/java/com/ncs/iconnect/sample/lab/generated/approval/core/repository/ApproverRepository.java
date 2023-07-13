package com.ncs.iconnect.sample.lab.generated.approval.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.Approver;

@Repository
public interface ApproverRepository extends JpaRepository<Approver, Long>, JpaSpecificationExecutor<Approver> {
    
    @Query("select u from Approver u where u.request.id = :requestId")
    List<Approver> findByApprovalRequestEntity(@Param("requestId") Long requestId);
}
