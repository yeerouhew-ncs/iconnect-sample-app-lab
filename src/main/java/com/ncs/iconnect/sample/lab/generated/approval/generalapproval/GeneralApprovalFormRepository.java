package com.ncs.iconnect.sample.lab.generated.approval.generalapproval;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GeneralApprovalRequestEntity.
 */
@Repository
public interface GeneralApprovalFormRepository extends JpaRepository<GeneralApprovalRequestForm, Long> {

    @Modifying
    @Query(value = "delete from IFLOW_REQ_GENERAL_APPROVAL where REQUEST_ID = :requestId", nativeQuery = true)
    void deleteByRequestId(@Param("requestId") Long requestId);
}
