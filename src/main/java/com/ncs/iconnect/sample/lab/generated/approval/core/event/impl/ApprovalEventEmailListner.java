package com.ncs.iconnect.sample.lab.generated.approval.core.event.impl;

import java.util.Iterator;
import java.util.Locale;
import java.util.SortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.Approver;
import com.ncs.iconnect.sample.lab.generated.approval.core.enumeration.MultiInstanceType;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEvent;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEventType;
import com.ncs.iconnect.sample.lab.generated.approval.core.event.ApprovalEventListener;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApproverRepository;
import com.ncs.iconnect.sample.lab.generated.approval.core.repository.ApprovalRequestEntityRepository;
import com.ncs.iconnect.sample.lab.generated.service.impl.ITrustAwareUserBasicInfoRetriever;
import com.ncs.iframe5.service.MailService;
import com.ncs.iframe5.service.dto.UserBasicInfoDTO;
    
@Service ("approvalEventEmailListner")
public class ApprovalEventEmailListner implements ApprovalEventListener {
    private final Logger log = LoggerFactory.getLogger(ApprovalEventEmailListner.class);
    private final MailService mailService;
    private final ApprovalRequestEntityRepository approvalRequestEntityRepository;
    private final ApproverRepository approverRepository;
    private final ITrustAwareUserBasicInfoRetriever userInfoRetriever;
    
    public ApprovalEventEmailListner(MailService mailService, ApprovalRequestEntityRepository approvalRequestEntityRepository, ApproverRepository approverRepository, ITrustAwareUserBasicInfoRetriever userInfoRetriever){
        this.mailService = mailService;
        this.approvalRequestEntityRepository = approvalRequestEntityRepository;
		this.approverRepository = approverRepository;
        this.userInfoRetriever = userInfoRetriever;
    }

@Override
public void onEvent(ApprovalEvent event) {
    log.warn("Received event: {0} ", event);
    
    ApprovalRequestEntity approvalRequestEntity = this.approvalRequestEntityRepository.findById(event.getRequestId()).get();
    Approver approver;

    if(event.getEventType() == ApprovalEventType.APPROVER_ASSIGNED
        && approvalRequestEntity.getMultiInstanceType() == MultiInstanceType.PARALLEL){
        approver = approverRepository.findById(
            ((ApprovalTaskAwareEvent)event).getApproverTaskId())
            .get();
    } else {
        approver = approvalRequestEntity.getApprovers().first();
    }

    UserBasicInfoDTO userBasicInfoDTO = this.userInfoRetriever.getUserBasicInfo(approver.getApproverId());
    userBasicInfoDTO.setLogin(userBasicInfoDTO.getLogin());
    userBasicInfoDTO.setEmail(userBasicInfoDTO.getEmail());
    
    UserBasicInfoDTO initiator = userInfoRetriever.getUserBasicInfo(approvalRequestEntity.getInitiator());
    String initiatorEmail = initiator.getEmail();
    String initiatorLogin = initiatorEmail.substring(0, initiatorEmail.indexOf("@"));
    initiator.setLogin(initiatorLogin);
    initiator.setEmail(initiatorEmail);
    
    Locale locale = Locale.forLanguageTag("en");
    Context context = new Context(locale);
    context.setVariable("approvalRequestId", approvalRequestEntity.getId());
    context.setVariable("summary", approvalRequestEntity.getSummary());
    context.setVariable("initiator",approvalRequestEntity.getInitiator());
    context.setVariable("date", approvalRequestEntity.getSubmittedDate());
    context.setVariable("status", approvalRequestEntity.getStatus());
    SortedSet<Approver> approvers = approvalRequestEntity.getApprovers();
    String approveNames = "";
    Iterator<Approver> iterator = approvers.iterator();
    while(iterator.hasNext()){
        Approver approverSendMail = (Approver)iterator.next();
        approveNames = approveNames +" "+approverSendMail.getApproverDisplayName();
    }
    context.setVariable("currentassignee", approveNames);
    
    switch (event.getEventType()) {
        case APPROVER_ASSIGNED:
            log.warn("APPROVER_ASSIGNED");
            mailService.sendEmailFromTemplate(userBasicInfoDTO,"mail/task-assgined","email.task.assgined.title",context);
            break;

        case APPROVER_APPROVED:
            log.warn("APPROVER_APPROVED");
            mailService.sendEmailFromTemplate(initiator,"mail/task-approved","email.task.approved.title",context);
            break;

        case APPROVER_REJECTED:
            log.warn("APPROVER_REJECTED");
            mailService.sendEmailFromTemplate(initiator,"mail/request-rejected","email.request.rejected.title",context);
            break;

        case APPROVER_REQUEST_CHANGE:
            log.warn("APPROVER_REQUEST_CHANGE");
            mailService.sendEmailFromTemplate(initiator,"mail/request-rollbacked","email.request.rollbacked.title",context);
            break;

        case REQUEST_SUBMITTED:
            log.warn("REQUEST_SUBMITTED");
            mailService.sendEmailFromTemplate(initiator,"mail/request-submitted","email.request.submitted.title",context);
            break;

        case REQUEST_CANCELLED:
            log.warn("REQUEST_CANCELLED");
            mailService.sendEmailFromTemplate(initiator,"mail/request-cancelled","email.request.cancelled.title",context);
            mailService.sendEmailFromTemplate(userBasicInfoDTO,"mail/request-cancelled","email.request.cancelled.title",context);
            break;

        case REQUEST_APPROVED:
            log.warn("REQUEST_APPROVED");
            mailService.sendEmailFromTemplate(initiator,"mail/request-approved","email.request.approved.title",context);
            break;
    }
  }
}
