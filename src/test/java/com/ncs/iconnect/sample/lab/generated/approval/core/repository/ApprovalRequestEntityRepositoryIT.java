package com.ncs.iconnect.sample.lab.generated.approval.core.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalStatus;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.*;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApprovalBehavior;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.ApproverSelection;
import com.ncs.iconnect.sample.lab.generated.approval.testdata.ApprovalTemplateTestData;
import com.ncs.iconnect.sample.lab.generated.security.LoginContextHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ApprovalRequestEntityRepositoryIT {
    @Autowired
    private ApprovalRequestEntityRepository approvalRequestEntityRepository;

    @Autowired
    private ApprovalTemplateRepository approvalTemplateRepository;

    @Autowired
    private ApprovalTemplateDataRepository approvalTemplateDataRepository;

    private LoginContextHelper loginContextHelper;

    private String requestTypeKey = "GeneralApproval";

    private ApprovalTemplate sequentialApprovalTemplate = null;
    private ApprovalTemplate parallelApprovalTemplate = null;


    @BeforeEach
    public void setup(){
        sequentialApprovalTemplate = prepareSequentialApprovalTemplate();
        parallelApprovalTemplate = prepareParallelApprovalTemplate();
    }

    public ApprovalTemplate prepareSequentialApprovalTemplate() {
        ApprovalTemplate savedApprovalTemplate = approvalTemplateRepository.save(ApprovalTemplateTestData.newSequentialApprovalTemplate());
        ApprovalTemplateData approver1 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "appadmin", 1);
        ApprovalTemplateData approver2 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "useradmin", 2);
        approvalTemplateDataRepository.save(approver1);
        approvalTemplateDataRepository.save(approver2);
        return savedApprovalTemplate;
    }

    public ApprovalTemplate prepareParallelApprovalTemplate() {
        ApprovalTemplate savedApprovalTemplate = approvalTemplateRepository.save(ApprovalTemplateTestData.newParallelApprovalTemplate());
        ApprovalTemplateData approver1 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "appadmin", 1);
        ApprovalTemplateData approver2 = ApprovalTemplateTestData.newApprovalTemplateData(savedApprovalTemplate, "useradmin", 2);
        approvalTemplateDataRepository.save(approver1);
        approvalTemplateDataRepository.save(approver2);
        return savedApprovalTemplate;
    }

    @Test
    @Transactional
    public void findRequestByTypeKey() {
        Pageable page = PageRequest.of(0,  10);
        ApprovalRequestEntity entity1 = newApprovalRequestEntity();
        ApprovalRequestEntity entity2 = newApprovalRequestEntity();
        approvalRequestEntityRepository.save(entity1);
        approvalRequestEntityRepository.save(entity2);
        Page<ApprovalRequestEntity> result = approvalRequestEntityRepository.findByTypeKey("GeneralApproval",
            page);
        assertTrue(result.getContent().size()>1);

    }

    @Test
    @Transactional
    public void findRequestForDummyUser() {
        Pageable page = PageRequest.of(0,  10);
        ApprovalRequestEntity entity1 = newApprovalRequestEntity();
        ApprovalRequestEntity entity2 = newApprovalRequestEntity();
        approvalRequestEntityRepository.save(entity1);
        approvalRequestEntityRepository.save(entity2);
        Page<ApprovalRequestEntity> result = approvalRequestEntityRepository.findRequestForUser("test123",
            page);
        assertEquals(0, result.getContent().size());

    }

    @Test
    @Transactional
    public void findRequestForInitiator() {
        Pageable page = PageRequest.of(0,  10);
        ApprovalRequestEntity entity1 = newApprovalRequestEntity();
        ApprovalRequestEntity entity2 = newApprovalRequestEntity();
        approvalRequestEntityRepository.save(entity1);
        approvalRequestEntityRepository.save(entity2);
        Page<ApprovalRequestEntity> result = approvalRequestEntityRepository.findRequestForUser(entity1.getInitiator(),
            page);
        assertEquals(2, result.getContent().size());

    }

    @Test
    @Transactional
    public void findRequestForApprover() {
        Pageable page = PageRequest.of(0,  10);
        ApprovalRequestEntity entity1 = newApprovalRequestEntity();
        entity1.setTemplateId(this.sequentialApprovalTemplate.getId());
        entity1.getApprovers().first().setApprovalStatus(ApprovalStatus.PENDING_APPROVAL);

        ApprovalRequestEntity entity2 = newApprovalRequestEntity();
        entity2.setTemplateId(this.sequentialApprovalTemplate.getId());
        Iterator<Approver> approverIt = entity2.getApprovers().iterator();

        approvalRequestEntityRepository.save(entity1);
        approvalRequestEntityRepository.save(entity2);

        List<ApprovalRequestEntity> allResult = approvalRequestEntityRepository.findAll();

        //Assigned user Approver1
        Page<ApprovalRequestEntity> result1 = approvalRequestEntityRepository.findRequestForUser("approver1",
            page);
        assertEquals(1, result1.getContent().size());

        //Approver2
        Page<ApprovalRequestEntity> result2 = approvalRequestEntityRepository.findRequestForUser("approver2",
            page);
        assertEquals(0, result2.getContent().size());

        //Not exist user Approver
        Page<ApprovalRequestEntity> result3 = approvalRequestEntityRepository.findRequestForUser("approver3",
            page);
        assertEquals(0, result3.getContent().size());

    }

    @Test
    @Transactional
    public void findRequestForApproverWithDraftStatusExpectEmpty() {
        Pageable page = PageRequest.of(0,  10);
        ApprovalRequestEntity entity1 = newApprovalRequestEntity();
        entity1.setTemplateId(this.sequentialApprovalTemplate.getId());
        entity1.getApprovers().first().setApprovalStatus(ApprovalStatus.DRAFT);
        approvalRequestEntityRepository.save(entity1);
        //Assigned user Approver1
        Page<ApprovalRequestEntity> result1 = approvalRequestEntityRepository.findRequestForUser("approver1",
            page);
        assertEquals(0, result1.getContent().size());
    }

    @Test
    @Transactional
    public void findRequestForApproverWithUnassignStatusExpectNonEmpty() {
        Pageable page = PageRequest.of(0,  10);
        ApprovalRequestEntity entity1 = newApprovalRequestEntity();
        entity1.setTemplateId(this.sequentialApprovalTemplate.getId());
        entity1.getApprovers().first().setApprovalStatus(ApprovalStatus.UNASSIGNED);
        approvalRequestEntityRepository.save(entity1);
        //Assigned user Approver1
        Page<ApprovalRequestEntity> result1 = approvalRequestEntityRepository.findRequestForUser("approver1",
            page);
        assertEquals(1, result1.getContent().size());
    }

    private ApprovalRequestEntity newApprovalRequestEntity() {
        ApprovalRequestEntity request = new ApprovalRequestEntity();
        request.setKey("GeneralApproval-1");
        request.setSummary("test sales promo dao");
        request.setApprovalBehavior(ApprovalBehavior.UNANIMOUS_APPROVAL);
        request.setEnableRejectAll(Boolean.TRUE);
        request.setEnableRejectStep(Boolean.FALSE);
        request.setEnableRejectToApplicant(Boolean.FALSE);
        request.setApproverSelection(ApproverSelection.FIXED_STEP);
        request.setMultiInstanceType(MultiInstanceType.SEQUENTIAL);
        request.setCreatedDate(LocalDate.now());
        request.setInitiator("user123");

        Integer seq = 0;
        request.getApprovers().add(newApprover("approver1", ++seq));
        request.getApprovers().add(newApprover("approver2", ++seq));
        return request;
    }

    private Approver newApprover(String approverId, Integer seq) {
        Approver approver = new Approver();
        approver.setApproverId(approverId);
        approver.setApproverTitle("Manager " + seq);
        approver.setApproverSeq(seq);
        approver.setApprovalStatus(ApprovalStatus.DRAFT);
        return approver;
    }
}
