package com.ncs.iconnect.sample.lab.generated.service.impl;

import com.ncs.iconnect.sample.lab.generated.service.GroupBasicInfoRetriever;
import com.ncs.iframe5.service.dto.GroupBasicInfoDTO;
import com.ncs.itrust5.acm.group.service.GroupService;
import com.ncs.itrust5.ss5.domain.Group;
import com.ncs.itrust5.ss5.domain.Subject2Group;
import com.ncs.itrust5.ss5.domain.SubjectLogin;
import com.ncs.itrust5.ss5.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("iTrustAwareGroupBasicInfoRetriever")
public class ITrustAwareGroupBasicInfoRetriever implements GroupBasicInfoRetriever {

    UserService userService;

    GroupService groupService;

    public ITrustAwareGroupBasicInfoRetriever(UserService userService, GroupService groupService){
        this.userService = userService;
        this.groupService = groupService;
    }

    @Override
    public List<GroupBasicInfoDTO> getGroupIdsToApprove(String username) {

        List<GroupBasicInfoDTO> dtos = new ArrayList<GroupBasicInfoDTO>();
        List<String> groupIds = new ArrayList<String>();

        SubjectLogin subjectLogin = userService.getSubjectLoginByUsernameAndLoginMethod(username, "PASSWORD");
        String subjectId = subjectLogin.getSubjectId();
        List<Subject2Group> subject2Groups = userService.findSubject2GroupBySubjectID(subjectId);

        for (Subject2Group subject2Group : subject2Groups) {
            GroupBasicInfoDTO dto = new GroupBasicInfoDTO();
            dto.setGroupId(subject2Group.getGroup().getGroupId());
            dtos.add(dto);
        }
         return  dtos;
    }

    @Override
    public List<GroupBasicInfoDTO> findGroupsByCondition(String condition) {
        List<GroupBasicInfoDTO> dtos = new ArrayList<GroupBasicInfoDTO>();
        //GroupBasicInfoDTO dto = null;

        List<Group> groups =  groupService.findByCondition(condition);
        for(Group group: groups){
            GroupBasicInfoDTO dto = new GroupBasicInfoDTO();
            dto.setGroupId(group.getGroupId());
            dto.setGroupName(group.getGroupName());
            dtos.add(dto);
        }
       return dtos;
    }
}
