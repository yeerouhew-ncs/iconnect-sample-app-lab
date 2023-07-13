package com.ncs.iconnect.sample.lab.generated.service;

import java.util.List;
import java.util.Map;

import com.ncs.iframe5.service.dto.UserBasicInfoDTO;

/**
 * Class to Retrieve Basic User Display Information
 */
public interface UserBasicInfoRetriever {

    UserBasicInfoDTO getUserBasicInfo(String subjectId);

    Map<String, UserBasicInfoDTO> getUsersBasicInfos(List<String> subjectId);
}
