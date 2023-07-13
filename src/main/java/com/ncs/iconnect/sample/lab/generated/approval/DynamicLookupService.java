package com.ncs.iconnect.sample.lab.generated.approval;

import com.ncs.iconnect.sample.lab.generated.approval.generalapproval.GeneralApprovalFormRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class DynamicLookupService {

    private final GeneralApprovalFormRepository generalApprovalRepository;

    public DynamicLookupService(GeneralApprovalFormRepository generalApprovalRepository) {
        this.generalApprovalRepository = generalApprovalRepository;
    }

    public JpaRepository getJpaRepository(String requestTypeKey) {
        return this.generalApprovalRepository;
    }
}
