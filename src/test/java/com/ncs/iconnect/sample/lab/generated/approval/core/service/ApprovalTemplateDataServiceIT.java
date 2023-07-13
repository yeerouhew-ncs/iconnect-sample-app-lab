package com.ncs.iconnect.sample.lab.generated.approval.core.service;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplate;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplateData;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateDataRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateDataDTO;
import com.ncs.iconnect.sample.lab.generated.approval.testdata.ApprovalTemplateTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ApprovalTemplateDataServiceIT {
    @Autowired
    ApprovalTemplateRepository approvalTemplateRepository;

    @Autowired
    ApprovalTemplateDataRepository approvalTemplateDataRepository;

    @Autowired
    ApprovalTemplateDataService approvalTemplateDataService;


    public ApprovalTemplate prepareData() {
        ApprovalTemplate savedApprovalTemplate = approvalTemplateRepository.save(ApprovalTemplateTestData.newSequentialApprovalTemplate());
        ApprovalTemplateData approver1 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "appadmin", 1);
        ApprovalTemplateData approver2 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "useradmin", 2);
        approvalTemplateDataRepository.save(approver1);
        approvalTemplateDataRepository.save(approver2);
        return savedApprovalTemplate;
    }

    @Test
    @Transactional
    public void findByTemplateIdExpectSuccess() {
        ApprovalTemplate approvalTemplate = prepareData();
        List<ApprovalTemplateDataDTO> dtos = approvalTemplateDataService.findByTemplateId(approvalTemplate.getId());
        assertEquals(2, dtos.size());
    }

    @Test
    @Transactional
    public void findByNonExistTemplateIdExpectEmptyResult() {
        ApprovalTemplate approvalTemplate = prepareData();
        List<ApprovalTemplateDataDTO> dtos = approvalTemplateDataService.findByTemplateId("12456");
        assertEquals(0, dtos.size());
    }
}
