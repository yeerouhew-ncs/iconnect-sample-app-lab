package com.ncs.iconnect.sample.lab.generated.approval.core.service.impl;

import com.ncs.iconnect.sample.lab.generated.approval.DisplayNameUpdater;
import com.ncs.iconnect.sample.lab.generated.approval.DynamicLookupService;
import com.ncs.iconnect.sample.lab.generated.approval.core.ApprovalAction;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.*;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.*;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEvent;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEventDispatcher;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEventType;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.impl.ApprovalRequetEvent;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.impl.ApprovalTaskAwareEvent;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalRequestEntityRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalTemplateRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApproverRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalRequestPermissionService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.ApprovalRequestService;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.TaskActionDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.mapper.ApprovalRequestMapper;
import com.ncs.iconnect.sample.lab.generated.approval.generalapproval.GeneralApprovalFormRepository;
import com.ncs.iconnect.sample.lab.generated.security.LoginContextHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing ApprovalRequest.
 */
@Service
@Transactional
public class ApprovalRequestServiceImpl implements ApprovalRequestService {

    private final Logger log = LoggerFactory.getLogger(ApprovalRequestServiceImpl.class);

    private final DynamicLookupService dynamicLookupService;

    private final ApprovalRequestEntityRepository approvalRequestEntityRepository;

    private final GeneralApprovalFormRepository generalApprovalFormRepository;

    private final ApproverRepository approverRepository;
    private final ApprovalRequestMapper approvalRequestMapper;
    private final ApprovalRequestPermissionService approvalRequestPermissionService;
    private final DisplayNameUpdater displayNameUpdater;
    @Autowired
    private ApprovalEventDispatcher approvalEventDispatcher;
    private ApprovalTemplateRepository approvalTemplateRepository;
    @Autowired
    private LoginContextHelper loginContextHelper;
    
    public ApprovalRequestServiceImpl(DynamicLookupService dynamicLookupService,
    ApprovalRequestEntityRepository approvalRequestEntityRepository,                                 
                                      ApproverRepository approverRepository,
                                      GeneralApprovalFormRepository generalApprovalFormRepository,
                                      ApprovalRequestMapper approvalRequestMapper,
                                      ApprovalTemplateRepository approvalTemplateRepository,
                                      ApprovalRequestPermissionService approvalRequestPermissionService, DisplayNameUpdater displayNameUpdater) {
        this.dynamicLookupService = dynamicLookupService;
        this.approvalRequestEntityRepository = approvalRequestEntityRepository;
        this.generalApprovalFormRepository = generalApprovalFormRepository;
        this.approverRepository = approverRepository;
        this.approvalRequestMapper = approvalRequestMapper;
        this.approvalTemplateRepository = approvalTemplateRepository;
        this.approvalRequestPermissionService = approvalRequestPermissionService;
        this.displayNameUpdater = displayNameUpdater;
    }

    @Override
    public Page<ApprovalRequestDTO> findApprovalRequests(String requestTypeKey, Pageable pageable) {
        Page<ApprovalRequestDTO> page = new PageImpl<>(Collections.emptyList());

        // find all type
        if (StringUtils.isEmpty(requestTypeKey) || "All".equalsIgnoreCase(requestTypeKey)) {
            if (this.approvalRequestPermissionService.isAdministrator()) {
                page = approvalRequestEntityRepository.findAll(pageable).map(approvalRequestMapper::toDto);
            } else {
                page = approvalRequestEntityRepository.findRequestForUser(getCurrentUserUUID(), pageable)
                    .map(approvalRequestMapper::toDto);
            }
        } else {
            if (this.approvalRequestPermissionService.isAdministrator()) {
                page = approvalRequestEntityRepository.findByTypeKey(requestTypeKey, pageable)
                    .map(approvalRequestMapper::toDto);
            } else {
                page = approvalRequestEntityRepository
                    .findRequestForUser(requestTypeKey, getCurrentUserUUID(), pageable)
                    .map(approvalRequestMapper::toDto);
            }
        }
        displayNameUpdater.updateApprovalRequestUserDisplayName(page.getContent());
        return page;
    }

    @Override
    public ApprovalRequestEntity findOne(Long id) {
        ApprovalRequestEntity approvalRequestEntity = this.approvalRequestEntityRepository.findById(id).get();
        approvalRequestPermissionService.validatePermission(getCurrentUserUUID(),
            approvalRequestEntity, RequestPermission.UPDATE);
        return approvalRequestEntity;
    }

    @Override
    public void deleteOne(Long id) {
         this.generalApprovalFormRepository.deleteByRequestId(id);
         this.approvalRequestEntityRepository.deleteById(id);
    }

    private String getCurrentUserUUID() {
        return loginContextHelper.getCurrentUserUUID();
    }

    /**
     * Save a generalApproval.
     *
     * @param approvalAwareRequest the entity to save
     * @param requestTypeKey       Type key of request
     * @return the persisted entity
     */
    @Override
    public ApprovalAwareForm save(ApprovalAwareForm approvalAwareRequest, String requestTypeKey) {

        updateAuditInfo(approvalAwareRequest.getApprovalRequest());
        approvalRequestPermissionService.validatePermission(getCurrentUserUUID(),
            approvalAwareRequest.getApprovalRequest(), RequestPermission.UPDATE);

        initApprovers(approvalAwareRequest.getApprovalRequest());

        JpaRepository approvalAwareRequestRepository = this.getApprovalRequestRepository(requestTypeKey);

        ApprovalAwareForm approvalAwareRequestWithKey = approvalAwareRequest;
        if (approvalAwareRequest.getApprovalRequest().getKey() == null) {
            ApprovalRequestEntity approvalRequest = approvalAwareRequest.getApprovalRequest();
            approvalAwareRequest.setApprovalRequest(null);

            // First save extended request to get id
            ApprovalAwareForm savedApprovalAwareRequest = (ApprovalAwareForm) approvalAwareRequestRepository
                .save(approvalAwareRequest);

            // Then set requestkey and save it
            approvalRequest.setKey(requestTypeKey + "-" + savedApprovalAwareRequest.getId());
            savedApprovalAwareRequest.setApprovalRequest(approvalRequest);
            approvalAwareRequestWithKey = savedApprovalAwareRequest;
        }

        initTemplateSetting(approvalAwareRequestWithKey.getApprovalRequest());
        return (ApprovalAwareForm) approvalAwareRequestRepository.save(approvalAwareRequestWithKey);
    }

    private void initApprovers(ApprovalRequestEntity approvalRequest) {
        approvalRequest.getApprovers().forEach(approver -> {
            approver.setApprovalStatus(ApprovalStatus.DRAFT);
            //Set Approver id to null if approval request is not created
            if (approvalRequest.getId() == null) {
                approver.setId(null);
            }
        });
    }

    private void initTemplateSetting(ApprovalRequestEntity approvalRequest) {
        String templateId = approvalRequest.getTemplateId();
        if (!StringUtils.isEmpty(templateId)) {
            log.debug("using template with id {}", templateId);
            ApprovalTemplate approvalTemplate = this.approvalTemplateRepository.findById(templateId).get();
            approvalRequest.setMultiInstanceType(approvalTemplate.getMultiInstanceType());
            approvalRequest.setApprovalBehavior(approvalTemplate.getApprovalBehavior());
            approvalRequest.setEnableRejectAll(approvalTemplate.isEnableRejectAll());
            approvalRequest.setEnableRejectStep(approvalTemplate.isEnableRejectStep());
            approvalRequest.setEnableRejectToApplicant(approvalTemplate.isEnableRejectToApplicant());
            approvalRequest.setApproverSelection(approvalTemplate.getApproverSelection());
        } else {
            approvalRequest.setMultiInstanceType(MultiInstanceType.SEQUENTIAL);
            approvalRequest.setApprovalBehavior(ApprovalBehavior.UNANIMOUS_APPROVAL);
            approvalRequest.setEnableRejectAll(Boolean.TRUE);
            approvalRequest.setEnableRejectStep(Boolean.FALSE);
            approvalRequest.setEnableRejectToApplicant(Boolean.TRUE);
            approvalRequest.setApproverSelection(ApproverSelection.FLEXIBLE);
        }
    }

    private void updateAuditInfo(ApprovalRequestEntity approvalRequest) {

        if (approvalRequest.getId() == null) {
            approvalRequest.setInitiator(getCurrentUserUUID());
            approvalRequest.setCreatedDate(LocalDate.now());
        } else {
            approvalRequest.setUpdatedBy(getCurrentUserUUID());
            approvalRequest.setUpdatedDate(LocalDate.now());
        }

        if (approvalRequest.getStatus() == null) {
            approvalRequest.setStatus(ApprovalStatus.DRAFT);
        }
    }

    @Override
    public ApprovalRequestDTO submit(Long approvalRequestId, TaskActionDTO taskActionDTO) {

        List<ApprovalEvent> approvalEvents = new ArrayList<>();
        ApprovalRequestEntity approvalRequestEntity = this.approvalRequestEntityRepository.findById(approvalRequestId).get();

        approvalRequestPermissionService.validatePermission(getCurrentUserUUID(),
            approvalRequestEntity, RequestPermission.UPDATE);

        updateAuditInfo(approvalRequestEntity);
        initTemplateSetting(approvalRequestEntity);

        // Update approvalRequest Process Status
        approvalRequestEntity.setStatus(ApprovalStatus.PENDING_APPROVAL);
        approvalRequestEntity.setSubmittedDate(LocalDate.now());

        // Update Approve List Status
        if (approvalRequestEntity.getMultiInstanceType() == MultiInstanceType.PARALLEL) {
            approvalRequestEntity.getApprovers().forEach(approver -> {
                approver.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL);
                approver.setTaskAssignedDate(LocalDate.now());
                approvalEvents.add(new ApprovalTaskAwareEvent(ApprovalEventType.APPROVER_ASSIGNED, approvalRequestEntity.getId(), approver.getId()));
            });
        } else {
            Approver approver = approvalRequestEntity.getApprovers().first();
            approver.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL);
            approver.setTaskAssignedDate(LocalDate.now());
            approvalEvents.add(new ApprovalTaskAwareEvent(ApprovalEventType.APPROVER_ASSIGNED, approvalRequestEntity.getId(), approver.getId()));
        }

        approvalRequestEntity.getApprovalHistoryItems().add(newApprovalHistory(approvalRequestEntity, ApprovalAction.SUBMIT, taskActionDTO.getComment(), ApprovalStatus.DRAFT));
        ApprovalRequestEntity updatedApprovalRequestEntity = this.approvalRequestEntityRepository
            .save(approvalRequestEntity);

        updatedApprovalRequestEntity = this.approvalRequestEntityRepository.findById(updatedApprovalRequestEntity.getId()).get();
        approvalEvents.add(new ApprovalRequetEvent(ApprovalEventType.REQUEST_SUBMITTED, approvalRequestId));
        approvalEventDispatcher.dispatchEvents(approvalEvents);
        return this.approvalRequestMapper.toDto(updatedApprovalRequestEntity);
    }

    private ApprovalHistoryItem newApprovalHistory(ApprovalRequestEntity approvalRequestEntity, String actionName, String comment, ApprovalStatus oldStatus) {
        ApprovalHistoryItem approvalHistory = new ApprovalHistoryItem();
        approvalHistory.setRequest(approvalRequestEntity);
        approvalHistory.setActUserId(this.getCurrentUserUUID());
        approvalHistory.setActionName(actionName);
        approvalHistory.setComment(comment);
        approvalHistory.setActionDate(LocalDate.now());
        approvalHistory.setOldRequestStatus(oldStatus);
        approvalHistory.setNewRequestStatus(approvalRequestEntity.getStatus());
        return approvalHistory;
    }

    @Override
    public ApprovalRequestDTO approve(TaskActionDTO taskAction) {
        List<ApprovalEvent> approvalEvents = new ArrayList<>();
        Approver approver = this.approverRepository.findById(taskAction.getApproverInstanceId()).get();
        ApprovalRequestEntity approvalRequestEntity = approver.getRequest();

        approvalRequestPermissionService.validatePermission(getCurrentUserUUID(),
            approvalRequestEntity, RequestPermission.REVIEW);

        updateApproverStatus(approver, ApprovalStatus.APPROVED);

        // If all approvers have approved, update ApprovalRequest status to 'APPROVED'
        // Else Next approver status to "PENDING_APPROVAL"
        ApprovalEvent requestApprovedEvent = null;
        if (this.isRequestApproved(approvalRequestEntity)) {
            approvalRequestEntity.setStatus(ApprovalStatus.APPROVED);
            requestApprovedEvent = new ApprovalRequetEvent(ApprovalEventType.APPROVER_APPROVED,
                approvalRequestEntity.getId());
        } else {
            //Only assign next approver for Sequential approvers, as parallel approvers already assigned at submit time
            if (approvalRequestEntity.getMultiInstanceType() == MultiInstanceType.SEQUENTIAL) {
                Approver assignedApprover = assignNextApprover(approvalRequestEntity);
                approvalEvents.add(new ApprovalTaskAwareEvent(ApprovalEventType.APPROVER_ASSIGNED,
                    approvalRequestEntity.getId(), assignedApprover.getId()));
            }
        }

        ApprovalHistoryItem history = newApprovalHistory(approvalRequestEntity, ApprovalAction.APPROVE, taskAction.getComment(), ApprovalStatus.PENDING_APPROVAL);
        approvalRequestEntity.getApprovalHistoryItems().add(history);
        ApprovalRequestEntity updatedApprovalRequestEntity = this.approvalRequestEntityRepository
            .save(approvalRequestEntity);

        approvalEvents.add(new ApprovalTaskAwareEvent(ApprovalEventType.APPROVER_APPROVED,
            approvalRequestEntity.getId(), taskAction.getApproverInstanceId()));
        if (requestApprovedEvent != null) {
            approvalEvents.add(requestApprovedEvent);
        }
        this.approvalEventDispatcher.dispatchEvents(approvalEvents);
        return this.approvalRequestMapper.toDto(updatedApprovalRequestEntity);
    }

    @Override
    public ApprovalRequestDTO complete(TaskActionDTO taskAction) {
        //Approver approver = this.approverRepository.findById(taskAction.getApproverInstanceId()).get();
        Approver approver = this.approverRepository.findByApprovalRequestEntity(taskAction.getApproverInstanceId()).get(0);
        ApprovalRequestEntity approvalRequestEntity = approver.getRequest();
        ApprovalStatus oldStatus = approvalRequestEntity.getStatus();
        setApprovalRequestCompletionInfo(approvalRequestEntity, ApprovalStatus.COMPLETED);

        approvalRequestEntity.getApprovalHistoryItems().add(newApprovalHistory(approvalRequestEntity, ApprovalAction.COMPLETED, taskAction.getComment(), oldStatus));
        ApprovalRequestEntity updatedApprovalRequestEntity = this.approvalRequestEntityRepository
            .save(approvalRequestEntity);

        return this.approvalRequestMapper.toDto(updatedApprovalRequestEntity);

    }

    private Approver assignNextApprover(ApprovalRequestEntity approvalRequestEntity) {
        Optional<Approver> firstUnassignedApproverOption = approvalRequestEntity.getApprovers().stream().filter(approver -> (approver.getApprovalStatus() == null) || approver.getApprovalStatus() == ApprovalStatus.DRAFT || approver.getApprovalStatus() == ApprovalStatus.UNASSIGNED).findFirst();
        Approver firstUnassignedApprover=firstUnassignedApproverOption.isPresent()?firstUnassignedApproverOption.get():new Approver();
        firstUnassignedApprover.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL);
        firstUnassignedApprover.setTaskAssignedDate(LocalDate.now());
        return firstUnassignedApprover;
    }

    private void updateApproverStatus(Approver currentApprover, ApprovalStatus approvalStatus) {
        currentApprover.setApprovalStatus(approvalStatus);
        currentApprover.setActualApprover(getCurrentUserUUID());
        currentApprover.setTaskCompletionDate(LocalDate.now());
    }

    private boolean isRequestApproved(ApprovalRequestEntity approvalRequestEntity) {

        if (approvalRequestEntity.getApprovalBehavior() == ApprovalBehavior.FIRST_APPROVAL) {
            return approvalRequestEntity.getApprovers().stream().anyMatch(approver -> ApprovalStatus.APPROVED == approver.getApprovalStatus());
        } else if (approvalRequestEntity.getApprovalBehavior() == ApprovalBehavior.UNANIMOUS_APPROVAL) {
            return approvalRequestEntity.getApprovers().stream().allMatch(approver -> ApprovalStatus.APPROVED == approver.getApprovalStatus());
        } else {
            throw new UnsupportedOperationException("ApprovalBehavior " + approvalRequestEntity.getApprovalBehavior() + " is not supporated.");
        }
    }

    @Override
    public ApprovalRequestDTO reject(TaskActionDTO taskAction) {
        Approver approver = this.approverRepository.findById(taskAction.getApproverInstanceId()).get();
        ApprovalRequestEntity approvalRequestEntity = approver.getRequest();

        approvalRequestPermissionService.validatePermission(getCurrentUserUUID(),
            approvalRequestEntity, RequestPermission.REVIEW);

        // Update Current Approver Status to Rejected
        updateApproverStatus(approver, ApprovalStatus.REJECTED);

        // Updated General ApprovalRequest Form to Rejected
        approvalRequestEntity.setStatus(ApprovalStatus.REJECTED);

        approvalRequestEntity.getApprovalHistoryItems().add(newApprovalHistory(approvalRequestEntity, ApprovalAction.REJECT, taskAction.getComment(), ApprovalStatus.PENDING_APPROVAL));
        ApprovalRequestEntity updatedApprovalRequestEntity = this.approvalRequestEntityRepository
            .save(approvalRequestEntity);

        updatedApprovalRequestEntity = this.approvalRequestEntityRepository.findById(updatedApprovalRequestEntity.getId()).get();
        
        approvalEventDispatcher.dispatchEvent(new ApprovalTaskAwareEvent(ApprovalEventType.APPROVER_REJECTED,
            approvalRequestEntity.getId(), taskAction.getApproverInstanceId()));
        return this.approvalRequestMapper.toDto(updatedApprovalRequestEntity);

    }

    @Override
    public ApprovalRequestDTO rejectStep(TaskActionDTO taskAction) {
        throw new UnsupportedOperationException("rejectStep is not implmented!");
    }

    private void setApprovalRequestCompletionInfo(ApprovalRequestEntity approvalRequestEntity,
                                                  ApprovalStatus approvalStatus) {
        approvalRequestEntity.setStatus(approvalStatus);
        approvalRequestEntity.setCompletedDate(LocalDate.now());
    }

    @Override
    public ApprovalRequestDTO cancel(Long approvalRequestEntityId, TaskActionDTO taskActionDTO) {
        ApprovalRequestEntity approvalRequestEntity = this.approvalRequestEntityRepository
            .findById(approvalRequestEntityId).get();

        approvalRequestPermissionService.validatePermission(getCurrentUserUUID(),
            approvalRequestEntity, RequestPermission.CANCEL);

        // Update approvalRequest status to 'Cancelled'
        setApprovalRequestCompletionInfo(approvalRequestEntity, ApprovalStatus.CANCELLED);

        approvalRequestEntity.getApprovalHistoryItems().add(newApprovalHistory(approvalRequestEntity, "Cancel", taskActionDTO.getComment(), ApprovalStatus.PENDING_APPROVAL));
        
        ApprovalRequestEntity updatedApprovalRequestEntity = this.approvalRequestEntityRepository
            .save(approvalRequestEntity);

        updatedApprovalRequestEntity = this.approvalRequestEntityRepository.findById(updatedApprovalRequestEntity.getId()).get();
        
        approvalEventDispatcher.dispatchEvent(
            new ApprovalRequetEvent(ApprovalEventType.REQUEST_CANCELLED, approvalRequestEntity.getId()));
        return this.approvalRequestMapper.toDto(updatedApprovalRequestEntity);
    }

    @Override
    public ApprovalRequestDTO rollbackToApplicant(TaskActionDTO taskAction) {
        Approver approver = this.approverRepository.findById(taskAction.getApproverInstanceId()).get();
        ApprovalRequestEntity approvalRequestEntity = approver.getRequest();

        approvalRequestPermissionService.validatePermission(getCurrentUserUUID(),
            approvalRequestEntity, RequestPermission.REVIEW);

        resetApprovers(approvalRequestEntity);

        // Updated General ApprovalRequest Form to Rejected
        approvalRequestEntity.setStatus(ApprovalStatus.REQUEST_FOR_CHANGE);
        approvalRequestEntity.getApprovalHistoryItems().add(newApprovalHistory(approvalRequestEntity, ApprovalAction.ROLLBACKTOAPPLICANT, taskAction.getComment(), ApprovalStatus.PENDING_APPROVAL));
        ApprovalRequestEntity updatedApprovalRequestEntity = this.approvalRequestEntityRepository
            .save(approvalRequestEntity);

        approvalEventDispatcher.dispatchEvent(new ApprovalTaskAwareEvent(ApprovalEventType.APPROVER_REQUEST_CHANGE,
            approvalRequestEntity.getId(), taskAction.getApproverInstanceId()));
        return this.approvalRequestMapper.toDto(updatedApprovalRequestEntity);
    }

    private void resetApprovers(ApprovalRequestEntity approvalRequestEntity) {
        approvalRequestEntity.getApprovers().forEach(approver -> {
            approver.setApprovalStatus(ApprovalStatus.UNASSIGNED);
            approver.setTaskAssignedDate(null);
        });
    }


    private JpaRepository getApprovalRequestRepository(String requestTypeKey) {
        return this.dynamicLookupService.getJpaRepository(requestTypeKey);
    }

    @Override
    public void setLoginContxtHelper(LoginContextHelper loginContextHelper) {
        this.loginContextHelper = loginContextHelper;
    }

    @Override
    public void setApprovalEventDispatcher(ApprovalEventDispatcher approvalEventDispatcher) {
        this.approvalEventDispatcher = approvalEventDispatcher;
    }
   
}
