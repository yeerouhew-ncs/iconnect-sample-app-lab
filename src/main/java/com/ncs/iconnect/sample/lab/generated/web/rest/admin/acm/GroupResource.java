package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;
import com.ncs.itrust5.acm.group.service.GroupService;
import com.ncs.itrust5.acm.role.service.RoleService;
import com.ncs.itrust5.acm.user.service.UserService;
import com.ncs.itrust5.ss5.domain.*;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing Group.
 */
@RestController
@RequestMapping("/api")
public class GroupResource {

    private final Logger log = LoggerFactory.getLogger(GroupResource.class);

    private static final String ENTITY_NAME = "group";

    private final GroupService groupService;

    private final UserService userService;

    private final RoleService roleService;

    public GroupResource(GroupService groupService, UserService userService, RoleService roleService) {
        this.groupService = groupService;
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * POST  /groups : Create a new group.
     *
     * @param group the group to create
     * @return the ResponseEntity with status 201 (Created) and with body the new group, or with status 400 (Bad Request) if the group has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/groups")
    public ResponseEntity<Group> createGroup(@Valid @RequestBody Group group) throws URISyntaxException {
        log.debug("REST request to save Group : {}", group);
        if (StringUtils.isNotBlank(group.getGroupName())) {
            List<Group> groupList = groupService.findByGroupName(group.getGroupName());
            if (!groupList.isEmpty()) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Group already exists with name [ " + group.getGroupName() + " ]")).body(null);
            }
        }
        Group result = groupService.save(group);
        if (result != null) {
            result = prepareCreateGroup(result);
            return ResponseEntity.created(new URI("/api/groups/" + result.getGroupId()))
	            .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".created", result.getGroupName().toString()))
	            .body(result);
        }
        return null;
    }

    /**
     * PUT  /groups : Updates an existing group.
     *
     * @param group the group to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated group,
     * or with status 400 (Bad Request) if the group is not valid,
     * or with status 500 (Internal Server Error) if the group couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups")
    public ResponseEntity<Group> updateGroup(@Valid @RequestBody Group group) throws URISyntaxException {
        log.debug("REST request to update Group : {}", group);
        if (StringUtils.isNotBlank(group.getGroupName())) {
            List<Group> groupList = groupService.findByGroupName(group.getGroupName());
            if (!groupList.isEmpty()) {
                for (Group g : groupList) {
                    if (!g.getGroupId().equalsIgnoreCase(group.getGroupId())) {
                        return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Group already exists with name [ " + group.getGroupName() + " ]")).body(null);
                    }
                }
            }
        }
        if(group.getGroup() != null && group.getGroup().getGroupId() == null) {
            group.setGroup(null);
        }
        Group result = groupService.save(group);
        if(result!=null){
        	result = prepareCreateGroup(result);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".updated", group.getGroupName().toString()))
            .body(result);
    }

    /**
     * GET  /groups : get all the groups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of groups in body
     */
    @GetMapping("/groups")
    public ResponseEntity<List<Group>> findGroups(@ApiParam String groupId, @ApiParam String groupName, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Groups");
        Page<Group> page = groupService.findGroups(groupId, groupName, pageable);
        if (page != null && page.getContent() != null && page.getNumberOfElements() > 0){
            for (Group s : page.getContent()) {
                groupService.prepareGroup(s);
                List<Group> groupList = groupService.findByLeftIndexAndRightIndex(s.getLeftIndex(), s.getRightIndex());
                if (groupList != null) {
                    Integer i = groupList.size();
                    s.setNoOfSubGroup(i.toString());
                }
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/groups");
        return new ResponseEntity<>(page!=null?page.getContent():new ArrayList<Group>(), headers, HttpStatus.OK);
    }

    /**
     * GET  /groups/:id : get the "id" group.
     *
     * @param id the id of the group to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the group, or with status 404 (Not Found)
     */
    @GetMapping("/groups/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable String id) {
        log.debug("REST request to get Group : {}", id);
        Group group = groupService.find(id);
        group = groupService.prepareGroup(group);
        group.setReParentId(group.getGroupParentId());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(group));
    }

    /**
     * DELETE  /groups/:id : delete the "id" group.
     *
     * @param id the id of the group to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String id) {
        log.debug("REST request to delete Group : {}", id);
        Group group = groupService.find(id);
        group = groupService.prepareGroup(group);
        groupService.delete(group);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".deleted", group.getGroupName().toString())).build();
    }

    /**
     * GET  /groups/assignedUsers/:id : get the "id" group.
     *
     * @param id the id of the group to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the group, or with status 404 (Not Found)
     */
    @GetMapping("/groups/assignedUsers/{id}")
    public ResponseEntity<List<Subject>> findAllAssignedUsers(@PathVariable String id) {
        Set<? extends Subject2Group> subject2GroupList = ((Group)this.groupService.find(id)).getSubject2Groups();
        List<Subject> result = new ArrayList();
        if(subject2GroupList != null && !subject2GroupList.isEmpty()) {
            Iterator var4 = subject2GroupList.iterator();
            while (var4.hasNext()) {
                Subject2Group s = (Subject2Group) var4.next();
                Subject subject = s.getSubject();
                subject = this.userService.prepareSubject(subject);
                result.add(subject);
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET  /groups/assignedRoles/:id : get the "id" group.
     *
     * @param id the id of the group to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the group, or with status 404 (Not Found)
     */
    @GetMapping("/groups/assignedRoles/{id}")
    public ResponseEntity<List<Resource>> findAllAssignedRoles(@PathVariable String id) {
        Set<? extends Group2Res> group2Reses = ((Group)this.groupService.find(id)).getGroup2Ress();
        List<Resource> result = new ArrayList();
        if(group2Reses != null && !group2Reses.isEmpty()) {
            Iterator var4 = group2Reses.iterator();
            while(var4.hasNext()) {
                Group2Res g = (Group2Res)var4.next();
                Resource resource = g.getResource();
                if(resource.getResourceType().equals("LOG_ROLE")) {
                    resource = this.roleService.prepareRole(resource);
                    result.add(resource);
                }
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * PUT  /groups/unAssignUsers/:id : UnAssign selected users.
     *
     * @param groupId the group
     * @param subjects the users to be unassign
     * @return the ResponseEntity with status 200 (OK) and with body the updated group,
     * or with status 400 (Bad Request) if the group is not valid,
     * or with status 500 (Internal Server Error) if the group couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups/unAssignUsers/{groupId}")
    public ResponseEntity<Void> unAssignUsers(@PathVariable String groupId, @RequestBody Subject[] subjects) {
        log.debug("REST request to unassign users : {}", groupId);
        this.groupService.unAssignUsers(groupId, subjects);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".unAssigned", "")).build();
    }


    /**
     * PUT  /groups/unAssignRoles/:id : UnAssign selected roles.
     *
     * @param groupId the group
     * @param resources the roles to be unassign
     * @return the ResponseEntity with status 200 (OK) and with body the updated group,
     * or with status 400 (Bad Request) if the group is not valid,
     * or with status 500 (Internal Server Error) if the group couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups/unAssignRoles/{groupId}")
    public ResponseEntity<Void> unAssignRoles(@PathVariable String groupId, @RequestBody Resource[] resources) {
        log.debug("REST request to unassign roles : {}", groupId);
        this.groupService.unAssignRoles(groupId, resources);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".unAssigned", "")).build();
    }

    /**
     * GET  /groups : get all the groups.
     *
     * @param page the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of groups in body
     */
    @GetMapping("/groups/search/unAssignedUsers")
    public ResponseEntity<List<Subject>> findUnassignedUsers(@ApiParam String groupId, @ApiParam String firstName, Pageable page) {
        Page<Subject> resultPage = groupService.findUnassignedSubjects(groupId, firstName, "", "", "", page);
        for (Subject subject : resultPage) {
            userService.prepareSubject(subject);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/groups/search/unAssignedUsers");
        return new ResponseEntity<>(resultPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * PUT  /groups/assignUsers/:id : assign selected users.
     *
     * @param groupId the group
     * @param subjects the users to be assign
     * @return the ResponseEntity with status 200 (OK) and with body the updated group,
     * or with status 400 (Bad Request) if the group is not valid,
     * or with status 500 (Internal Server Error) if the group couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups/assignUsers/{groupId}")
    public ResponseEntity<Void> assignUsers(@PathVariable String groupId, @RequestBody Subject[] subjects) {
        log.debug("REST request to assign users : {}", groupId);
        groupService.assignSubjects(groupId, subjects);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".assigned", "")).build();
    }

    /**
     * GET  /groups/search/unAssignedRoles : get all unassign roles.
     *
     * @param page the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of groups in body
     */
    @GetMapping("/groups/search/unAssignedRoles")
    public ResponseEntity<List<Resource>> findUnassignedRoles(@ApiParam String groupId, @ApiParam String roleName, Pageable page) {
        Page<Resource> resultPage = groupService.findUnassignedRoles(groupId, "", roleName, page);
        for (Resource resource : resultPage){
            roleService.prepareRole(resource);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/groups/search/unAssignedUsers");
        return new ResponseEntity<>(resultPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * PUT  /groups/assignUsers/:id : assign selected users.
     *
     * @param groupId the group
     * @param roles the roles to be assign
     * @return the ResponseEntity with status 200 (OK) and with body the updated group,
     * or with status 400 (Bad Request) if the group is not valid,
     * or with status 500 (Internal Server Error) if the group couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups/assignRoles/{groupId}")
    public ResponseEntity<Void> assignRoles(@PathVariable String groupId, @RequestBody Resource[] roles) {
        log.debug("REST request to assign roles : {}", groupId);
        groupService.assignRoles(groupId, roles);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".assigned", "")).build();
    }

    private Group prepareCreateGroup(Group origin) {
        Group result = origin;
        result.setGroup2Ress(null);
        result.setSubject2Groups(null);
        result.setGroups(null);
        result.setGroup(null);
        return result;
    }
        
	@InitBinder 
	public void initBinder(WebDataBinder binder) { 
	    binder.setDisallowedFields(new String[]{}); 
	}
}
