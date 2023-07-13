package com.ncs.iconnect.sample.lab.generated.approval.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestAttachment;

@Repository
public interface ApprovalRequestAttachmentRepository extends JpaRepository<ApprovalRequestAttachment, Long>, JpaSpecificationExecutor<ApprovalRequestAttachment> {
    @Query("select u from ApprovalRequestAttachment u where u.request.id = :requestId")
    List<ApprovalRequestAttachment> findByApprovalRequestId(@Param("requestId") Long requestId);
}
 