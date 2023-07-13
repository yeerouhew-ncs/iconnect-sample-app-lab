package com.ncs.iconnect.sample.lab.generated.approval.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;

public interface ApprovalRequestEntityRepository extends JpaRepository<ApprovalRequestEntity, Long>, JpaSpecificationExecutor<ApprovalRequestEntity> {
	
    @Query("select r from ApprovalRequestEntity r where r.key like CONCAT(:typeKey,'-%')")
    Page<ApprovalRequestEntity> findByTypeKey(@Param("typeKey") String typeKey, Pageable pageable);
    
    
    @Query("select distinct r from ApprovalRequestEntity r"
	+ " join r.approvers ap"
	+ " where ((r.initiator = :userId) "
	+ " or ((ap.approverId = :userId) and (ap.approvalStatus!='DRAFT'))) order by r.id desc"
	)
    Page<ApprovalRequestEntity> findRequestForUser(@Param("userId") String userId, Pageable pageable);
    
    @Query("select distinct r from ApprovalRequestEntity r"
	+ " join r.approvers ap"
	+ " where (r.key like CONCAT(:typeKey,'-%')) "
	+ " and ((r.initiator = :userId) "
	+ " or ((ap.approverId = :userId) and (ap.approvalStatus!= 'DRAFT'))) order by r.id desc"
	)
    Page<ApprovalRequestEntity> findRequestForUser(@Param("typeKey") String typeKey, @Param("userId") String userId, Pageable pageable);
}

