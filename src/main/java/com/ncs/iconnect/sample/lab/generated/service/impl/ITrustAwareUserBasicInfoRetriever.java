package com.ncs.iconnect.sample.lab.generated.service.impl;

import com.ncs.itrust5.ss5.domain.Subject;
import com.ncs.itrust5.ss5.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import com.ncs.iconnect.sample.lab.generated.service.UserBasicInfoRetriever;
import com.ncs.iframe5.service.dto.UserBasicInfoDTO;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class use iTrust API to retrieve user information, and it need iTrust Dependency
 * In Microservices where iTrust dependency is not available, please use RESTful based servcies to get User info
 */
@Service("iTrustAwareUserBasicInfoRetriever")
public class ITrustAwareUserBasicInfoRetriever implements UserBasicInfoRetriever {

    private SubjectRepository subjectRepository;

    public ITrustAwareUserBasicInfoRetriever(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public UserBasicInfoDTO getUserBasicInfo(String subjectId) {
        Subject subject = this.subjectRepository.findBySubjectId(subjectId);
        return toUserBasicInfoDTO(subject);
    }

    private UserBasicInfoDTO toUserBasicInfoDTO(Subject subject) {
        UserBasicInfoDTO userBasicInfoDTO = new UserBasicInfoDTO();
        userBasicInfoDTO.setFirstName(subject.getFirstName());
        userBasicInfoDTO.setLastName(subject.getLastName());
        userBasicInfoDTO.setSubjectId(subject.getSubjectId());
        userBasicInfoDTO.setStatus(subject.getStatus());
        userBasicInfoDTO.setEmail(subject.getEmail());
        return userBasicInfoDTO;
    }

    public Map<String, UserBasicInfoDTO> getUsersBasicInfos(List<String> subjectIds) {
        List<Subject> subjects = new ArrayList<>();
        if(subjectIds.size()>0) {
            subjects = this.subjectRepository.findbySubjectIdIn(subjectIds);
        }
        return toMap(subjects);
    }

    private Map<String, UserBasicInfoDTO> toMap(List<Subject> subjects) {
        Map<String, UserBasicInfoDTO> dtoMap = new HashMap<>();
        subjects.stream().forEach(s -> dtoMap.put(s.getSubjectId(), toUserBasicInfoDTO(s)));
        return dtoMap;
    }
}
