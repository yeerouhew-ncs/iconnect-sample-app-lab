package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;
import com.ncs.itrust5.acm.group.service.GroupService;
import com.ncs.itrust5.acm.role.service.RoleService;
import com.ncs.itrust5.acm.user.service.UserService;
import com.ncs.itrust5.dto.UserSearchCriteria;
import com.ncs.itrust5.ss5.ITrustConstants;
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
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Resource.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "user";

    private final UserService userService;

    private final RoleService roleService;

    private final GroupService groupService;

    public UserResource(UserService userService, RoleService roleService, GroupService groupService) {
        this.userService = userService;
        this.roleService = roleService;
        this.groupService = groupService;
    }

    /**
     * POST  /users : Create a new user.
     *
     * @param user the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the user has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/users")
    public ResponseEntity<Subject> createUser(@Valid @RequestBody Subject user) throws URISyntaxException {
        log.debug("REST request to save User : {}", user);
        String loginName="";
        if (user.getSubjectLogins() != null && !user.getSubjectLogins().isEmpty()) {
            for (SubjectLogin subjectLogin : user.getSubjectLogins()) {
                loginName = subjectLogin.getLoginName();
                if (StringUtils.isNotBlank(subjectLogin.getLoginMechanism()) && StringUtils.isNotBlank(subjectLogin.getLoginName())) {
                    SubjectLogin login = userService.getSubjectLoginByUsernameAndLoginMethod(subjectLogin.getLoginName(), subjectLogin.getLoginMechanism());
                    if (login != null) {
                        return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "User already exists with name [ " + login.getLoginName() + " ]")).body(null);
                    }
                }
            }
        }
        Subject result = userService.save(user);
        if (result != null) {
            result = userService.prepareSubject(result);
            return ResponseEntity.created(new URI("/api/users/" + result.getSubjectId()))
            .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".created", loginName))
            .body(result);
        }
        return null;

    }   

    /**
     * PUT  /users : Updates an existing user.
     *
     * @param user the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the user is not valid,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/users")
    public ResponseEntity<Subject> updateUser(@Valid @RequestBody Subject user) throws URISyntaxException {
        log.debug("REST request to update User : {}", user);
        String loginName="";
        if (user.getSubjectLogins() != null && !user.getSubjectLogins().isEmpty()) {
            for (SubjectLogin subjectLogin : user.getSubjectLogins()) {
                loginName = subjectLogin.getLoginName();
                /*if (StringUtils.isNotBlank(subjectLogin.getLoginMechanism()) && StringUtils.isNotBlank(subjectLogin.getLoginName())) {
                    SubjectLogin login = userService.getSubjectLoginByUsernameAndLoginMethod(subjectLogin.getLoginName(), subjectLogin.getLoginMechanism());
                    if (login != null && !login.getSubjectId().equalsIgnoreCase(user.getSubjectId())) {
                        return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "User already exists with name [ " + login.getLoginName() + " ]")).body(null);
                    } else {
                        if(ITrustConstants.USER_STATUS_ACTIVE.equals(user.getStatus()) && subjectLogin.getAttemptsMade()!=null && subjectLogin.getAttemptsMade()>0){
                            subjectLogin.setAttemptsMade(0);
                        }
                    }
                }*/
                ResponseEntity<Subject> badResponse = updateUserBadResponse(user,subjectLogin);
                if (badResponse!=null){
                    return badResponse;
                }
            }
        }

        Subject result = userService.save(user);
        if (result != null) {
            result = userService.prepareSubject(result);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".updated", loginName))
            .body(result);
    }

    private ResponseEntity<Subject> updateUserBadResponse(Subject user,SubjectLogin subjectLogin) throws URISyntaxException{
    	ResponseEntity<Subject> updateUserBadResponse = null;
    	if (StringUtils.isNotBlank(subjectLogin.getLoginMechanism()) && StringUtils.isNotBlank(subjectLogin.getLoginName())) {
            SubjectLogin login = userService.getSubjectLoginByUsernameAndLoginMethod(subjectLogin.getLoginName(), subjectLogin.getLoginMechanism());
            if (login != null && !login.getSubjectId().equalsIgnoreCase(user.getSubjectId())) {
            	updateUserBadResponse =  ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "User already exists with name [ " + login.getLoginName() + " ]")).body(null);
            } else {
                if(ITrustConstants.USER_STATUS_ACTIVE.equals(user.getStatus()) && subjectLogin.getAttemptsMade()!=null && subjectLogin.getAttemptsMade()>0){
                    subjectLogin.setAttemptsMade(0);
                }
            }
        }
        return updateUserBadResponse;
    }

    /**
     * GET  /users : get all the users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of users in body
     */
    @GetMapping("/users")
    public ResponseEntity<List<Subject>> findUsers(@ApiParam String firstName, @ApiParam String lastName, @ApiParam String loginName, @ApiParam String email, @ApiParam String effectiveDt, @ApiParam String expiryDt, @ApiParam String status, @ApiParam String loginMechanism, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Users");
        Page<Subject> resultPage = userService.findUsers(new UserSearchCriteria(firstName, lastName, loginName, email, effectiveDt, expiryDt, status, loginMechanism), pageable);
        if (resultPage != null && resultPage.getContent() != null && resultPage.getNumberOfElements() > 0){
            for (Subject c:resultPage.getContent()){
                userService.prepareSubject(c);
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/users");
        return new ResponseEntity<>(resultPage!=null?resultPage.getContent():new ArrayList<Subject>(), headers, HttpStatus.OK);
    }

    /**
     * GET  /roles/:id : get the "id" user.
     *
     * @param id the id of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<Subject> getUser(@PathVariable String id) {
        log.debug("REST request to get User : {}", id);
        Subject result = userService.find(id);
        if (result != null) {
            result = userService.prepareSubject(result);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * GET  /users/assignedRoles/:id : get the "id" role.
     *
     * @param id the id of the role to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the role, or with status 404 (Not Found)
     */
    @GetMapping("/users/assignedRoles/{id}")
    public ResponseEntity<List<Resource>> findAllAssignedRoles(@PathVariable String id) {
        Set<Subject2Res> subject2GroupList = userService.find(id).getSubject2Ress();

        List<Resource> result = new ArrayList();
        if(subject2GroupList != null){
            for(Subject2Res s : subject2GroupList){
                if(ITrustConstants.RES_TYPE_ROLE.equals(s.getResource().getResourceType())){
                    result.add(s.getResource());
                }
            }
        }
        for(Resource r : result){
            roleService.prepareRole(r);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET  /users/assignedGroups/:id : get the "id" role.
     *
     * @param id the id of the role to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the role, or with status 404 (Not Found)
     */
    @GetMapping("/users/assignedGroups/{id}")
    public ResponseEntity<List<Group>> findAllAssignedGroups(@PathVariable String id) {
        Set<Subject2Group> subject2GroupList = userService.find(id).getSubject2Groups();
        List<Group> result = new ArrayList();
        if (subject2GroupList != null) {
            for (Subject2Group s : subject2GroupList) {
                result.add(s.getGroup());
            }
        }
        for(Group group : result) {
            groupService.prepareGroup(group);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * PUT  /users/unAssignRoles/:id : UnAssign selected roles.
     *
     * @param subjectId the role
     * @param roles the roles to be unassign
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/users/unAssignRoles/{subjectId}")
    public ResponseEntity<Void> unAssignRoles(@PathVariable String subjectId, @RequestBody Resource[] roles) {
        log.debug("REST request to unassign roles : {}", subjectId);
        userService.unAssignRoles(subjectId, roles);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".unAssigned", "")).build();
    }

    /**
     * PUT  /users/unAssignGroups/:id : UnAssign selected groups.
     *
     * @param subjectId the role
     * @param groups the groups to be unassign
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/users/unAssignGroups/{subjectId}")
    public ResponseEntity<Void> unAssignGroups(@PathVariable String subjectId, @RequestBody Group[] groups) {
        log.debug("REST request to unassign groups : {}", subjectId);
        userService.unAssignGroups(subjectId, groups);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".unAssigned", "")).build();
    }

    /**
     * GET  /users/search/unAssignedRoles : get all unassign roles.
     *
     * @param page the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/users/search/unAssignedRoles")
    public ResponseEntity<List<Resource>> findUnAssignedRoles(@ApiParam String subjectId, @ApiParam String roleName, Pageable page) {
        Page<Resource> resultPage = userService.findUnassignedRoles(subjectId, null, roleName, page);
        for (Resource resource : resultPage){
            roleService.prepareRole(resource);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/users/search/unAssignedRoles");
        return new ResponseEntity<>(resultPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /users/search/unAssignedGroups : get all unassign groups.
     *
     * @param page the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of users in body
     */
    @GetMapping("/users/search/unAssignedGroups")
    public ResponseEntity<List<Group>> findUnAssignedGroups(@ApiParam String subjectId, @ApiParam String groupName, Pageable page) {
        Page<Group> resultPage = userService.findUnassignedGroups(subjectId, null, groupName, page);
        for (Group group : resultPage){
            groupService.prepareGroup(group);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/users/search/unAssignedGroups");
        return new ResponseEntity<>(resultPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * PUT  /users/assignRoles/:id : assign selected users.
     *
     * @param subjectId the role
     * @param roles the roles to be assign
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/users/assignRoles/{subjectId}")
    public ResponseEntity<Void> assignRoles(@PathVariable String subjectId, @RequestBody Resource[] roles) {
        log.debug("REST request to assign roles : {}", subjectId);
        userService.assignRoles(subjectId, roles);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".assigned", "")).build();
    }

    /**
     * PUT  /users/assignGroups/:id : assign selected users.
     *
     * @param subjectId the role
     * @param groups the users to be assign
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/users/assignGroups/{subjectId}")
    public ResponseEntity<Void> assignGroups(@PathVariable String subjectId, @RequestBody Group[] groups) {
        log.debug("REST request to assign groups : {}", subjectId);
        userService.assignGroups(subjectId, groups);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".assigned", "")).build();
    }

    @PostMapping("/users/resetPassword")
    public ResponseEntity<Void> resetPassword(@NotNull @RequestBody String username) {
        userService.resetPassword(username);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".resetPassword", "")).build();
    }

    @InitBinder 
	public void initBinder(WebDataBinder binder) { 
	    binder.setDisallowedFields(new String[]{}); 
	}
}
