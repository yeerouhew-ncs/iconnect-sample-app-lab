package com.ncs.iconnect.sample.lab.generated.approval.core.service;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalAwareForm;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalTemplate;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalBehavior;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateDataRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalTemplateRTDTO;
import com.ncs.iconnect.sample.lab.generated.approval.testdata.ApprovalRequestTestData;
import com.ncs.iconnect.sample.lab.generated.approval.testdata.ApprovalTemplateTestData;
import com.ncs.iconnect.sample.lab.generated.security.LoginContextHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ApprovalTemplateServiceIT {
    @Autowired
    private ApprovalTemplateRepository approvalTemplateRepository;

    @Autowired
    private ApprovalTemplateDataRepository approvalTemplateDataRepository;

    @Autowired
    private ApprovalTemplateService approvalTemplateService;

    @BeforeEach
    public void setup(){
        approvalTemplateDataRepository.deleteAll();
        approvalTemplateRepository.deleteAll();
    }

    @Test
    public void saveSequentialRequestWithFirstApprovalBehaviorExpectException() {
        assertThrows(UnsupportedOperationException.class,()->{
            ApprovalTemplate approvalTemplate = ApprovalTemplateTestData.newSequentialApprovalTemplate();
            approvalTemplate.setApprovalBehavior(ApprovalBehavior.FIRST_APPROVAL);
            approvalTemplateService.save(approvalTemplate);
        });
    }


    @Test
    public void saveParallelRequestWithRejectStepExpectException() {
        assertThrows(UnsupportedOperationException.class,()->{
            ApprovalTemplate approvalTemplate = ApprovalTemplateTestData.newParallelApprovalTemplate();
            approvalTemplate.setEnableRejectStep(true);
            approvalTemplateService.save(approvalTemplate);
        });
    }

    @Test
    public void saveApprovalTemplateWithExistRequestKeyAndSelectorExpectException() {
        ApprovalTemplate approvalTemplate1 = ApprovalTemplateTestData.newSequentialApprovalTemplate();
        ApprovalTemplate approvalTemplate2 = ApprovalTemplateTestData.newSequentialApprovalTemplate();
        approvalTemplateService.save(approvalTemplate1);
        approvalTemplateService.save(approvalTemplate2);
    }

    @Test
    @Transactional
    public void findByRequestTypeAndTemplateKeyExpectSuccess() {
        ApprovalTemplate approvalTemplate = approvalTemplateRepository.save(ApprovalTemplateTestData.newSequentialApprovalTemplate());
        Optional<ApprovalTemplateRTDTO> dto = approvalTemplateService.findByRequestTypeAndTemplateKey(approvalTemplate.getRequestTypeKey(), approvalTemplate.getTemplateKey());
        assertTrue(dto.isPresent());
    }

    @Test
    @Transactional
    public void findByRequestTypeWithNullSelectorExpectSuccess() {
        ApprovalTemplate approvalTemplate = approvalTemplateRepository.save(ApprovalTemplateTestData.newSequentialApprovalTemplate());
        Optional<ApprovalTemplateRTDTO> dto = approvalTemplateService.findByRequestTypeAndTemplateKey(approvalTemplate.getRequestTypeKey(), null);
        assertTrue(dto.isPresent());
    }

    @Test
    @Transactional
    public void findByNonExistRequestTypeExpectEmptyResult() {
        ApprovalTemplate approvalTemplate = approvalTemplateRepository.save(ApprovalTemplateTestData.newSequentialApprovalTemplate());
        Optional<ApprovalTemplateRTDTO> dto = approvalTemplateService.findByRequestTypeAndTemplateKey("non exist type key", approvalTemplate.getTemplateKey());
        assertFalse(dto.isPresent());
    }

    @Test
    @Transactional
    public void findByNonExistTemplateKeyExpectEmptyResult() {
        ApprovalTemplate approvalTemplate = approvalTemplateRepository.save(ApprovalTemplateTestData.newSequentialApprovalTemplate());
        Optional<ApprovalTemplateRTDTO> dto = approvalTemplateService.findByRequestTypeAndTemplateKey(approvalTemplate.getRequestTypeKey(), "non exist selector");
        assertFalse(dto.isPresent());
    }
}
