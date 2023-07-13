package com.ncs.iconnect.sample.lab.generated.service;

import com.ncs.iframe5.service.dto.GroupBasicInfoDTO;

import java.util.List;


public interface GroupBasicInfoRetriever {
    
    List<GroupBasicInfoDTO> getGroupIdsToApprove(String username);
 
    List<GroupBasicInfoDTO> findGroupsByCondition(String condition);
}