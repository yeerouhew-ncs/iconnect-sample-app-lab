package com.ncs.iconnect.sample.lab.generated.approval;

import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalHistoryItemDTO;
import org.springframework.stereotype.Service;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalRequestDTO;
import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApproverDTO;
import com.ncs.iconnect.sample.lab.generated.service.UserBasicInfoRetriever;
import com.ncs.iframe5.service.dto.UserBasicInfoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

@Service
public class DisplayNameUpdater {
    private final UserBasicInfoRetriever userInfoRetriever;

    public DisplayNameUpdater(UserBasicInfoRetriever userInfoRetriever){
        this.userInfoRetriever = userInfoRetriever;
    }

    private void updateApproversDisplayName(Set<ApproverDTO> approvers){
        List<String> initiatorUUids = new ArrayList<>();
        approvers.forEach(s->initiatorUUids.add(s.getApproverId()));
        Map<String, UserBasicInfoDTO> userBasicInfoMap = this.userInfoRetriever.getUsersBasicInfos(initiatorUUids);
        approvers.forEach(s->s.setApproverDisplayName(userBasicInfoMap.get(s.getApproverId()).getDisplayNameWithEmail()));
    }

    private void updateHistoryItemUserDisplayName(SortedSet<ApprovalHistoryItemDTO> historyItems){
        List<String> actUserids = new ArrayList<>();
        historyItems.forEach(item->actUserids.add(item.getActUserId()));
        Map<String, UserBasicInfoDTO> userBasicInfoMap = this.userInfoRetriever.getUsersBasicInfos(actUserids);
        historyItems.forEach(item->item.setActUserDisplayName(userBasicInfoMap.get(item.getActUserId()).getDisplayNameWithEmail()));
    }

    private void updateHistoryItemUserDisplayName(List<ApprovalHistoryItemDTO> historyItems){
        List<String> actUserids = new ArrayList<>();
        historyItems.forEach(item->actUserids.add(item.getActUserId()));
        Map<String, UserBasicInfoDTO> userBasicInfoMap = this.userInfoRetriever.getUsersBasicInfos(actUserids);
        historyItems.forEach(item->item.setActUserDisplayName(userBasicInfoMap.get(item.getActUserId()).getDisplayNameWithEmail()));
    }

    private String getDisplayNameWithEmail(UserBasicInfoDTO userBasicInfo) {
        return userBasicInfo.getFirstName() + " " + userBasicInfo.getLastName() + "(" + userBasicInfo.getEmail() +")";
    }

    public void updateApprovalRequestUserDisplayName(ApprovalRequestDTO approvalRequestDTO) {
        approvalRequestDTO.setInitiatorDisplayName(this.userInfoRetriever.getUserBasicInfo(approvalRequestDTO.getInitiator()).getDisplayNameWithEmail());
        this.updateApproversDisplayName(approvalRequestDTO.getApprovers());
        this.updateHistoryItemUserDisplayName(approvalRequestDTO.getApprovalHistoryItems());
    }

    public void updateApprovalRequestUserDisplayName(List<ApprovalRequestDTO> approvalRequestDTOs) {
        List<String> initiatorUUids = new ArrayList<>();
        approvalRequestDTOs.forEach(s->initiatorUUids.add(s.getInitiator()));
        Map<String, UserBasicInfoDTO> userBasicInfoMap = this.userInfoRetriever.getUsersBasicInfos(initiatorUUids);
        approvalRequestDTOs.forEach(s->s.setInitiatorDisplayName(userBasicInfoMap.get(s.getInitiator()).getDisplayNameWithEmail()));
    }
}
