package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;
import com.ncs.itrust5.acm.app.service.AcmApplicationService;
import com.ncs.itrust5.acm.group.service.GroupService;
import com.ncs.itrust5.acm.role.service.RoleService;
import com.ncs.itrust5.acm.user.service.UserService;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Role.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    private static final String ENTITY_NAME = "role";

    private final RoleService roleService;

    private final GroupService groupService;

    private final UserService userService;

    private AcmApplicationService acmAppService;

    public RoleResource(RoleService roleService,  GroupService groupService, UserService userService, AcmApplicationService acmAppService) {
        this.roleService = roleService;
        this.groupService = groupService;
        this.userService = userService;
        this.acmAppService = acmAppService;
    }

    /**
     * POST  /roles : Create a new role.
     *
     * @param role the role to create
     * @return the ResponseEntity with status 201 (Created) and with body the new role, or with status 400 (Bad Request) if the role has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/roles")
    public ResponseEntity<Resource> createRole(@Valid @RequestBody Resource role) throws URISyntaxException {
        log.debug("REST request to save Role : {}", role);
        if (StringUtils.isNotBlank(role.getAppId()) && StringUtils.isNotBlank(role.getResourceName())) {
            List<Resource> resourceList = roleService.findResourcesByResourceTypeAndResourceName(ITrustConstants.RES_TYPE_ROLE, role.getResourceName(), role.getAppId());
            if (!resourceList.isEmpty()) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Role already exists with name [ " + role.getResourceName() + " ]")).body(null);
            }
        }
        role.setResourceType(ITrustConstants.RES_TYPE_ROLE);
        Resource result = roleService.save(role);
        if (result != null) {
            result = roleService.prepareRole(result);
            return ResponseEntity.created(new URI("/api/roles/" + result.getResourceId()))
                    .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".created", result.getResourceName().toString()))
                    .body(result);
        }
        return null;
    }

    /**
     * PUT  /roles : Updates an existing role.
     *
     * @param role the role to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles")
    public ResponseEntity<Resource> updateRole(@Valid @RequestBody Resource role) throws URISyntaxException {
        log.debug("REST request to update Role : {}", role);
        if (StringUtils.isNotBlank(role.getAppId()) && StringUtils.isNotBlank(role.getResourceName())) {
            List<Resource> resourceList = roleService.findResourcesByResourceTypeAndResourceName(ITrustConstants.RES_TYPE_ROLE, role.getResourceName(), role.getAppId());
            if (!resourceList.isEmpty()) {
                for (Resource r : resourceList) {
                    if (!r.getResourceId().equalsIgnoreCase(role.getResourceId())) {
                        return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Role already exists with name [ " + role.getResourceName() + " ]")).body(null);
                    }
                }
            }
        }

        Resource result = roleService.save(role);
        if (result!=null) {
            result = roleService.prepareRole(result);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".updated", role.getResourceName().toString()))
            .body(result);
    }

    /**
     * GET  /roles/applications : get all the application.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/roles/applications")
    public ResponseEntity<List<Application>> findAllApplication() {
        log.debug("REST request to get all applications");
        List<Application> result = acmAppService.getAllApplications();
        for (Application r : result){
            r.setResources(null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET  /roles : get all the roles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/roles")
    public ResponseEntity<List<Resource>> findRoles(@ApiParam String applicationId, @ApiParam String roleName, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Roles");
        Page<Resource> resultPage = roleService.findRoles(applicationId, ITrustConstants.RES_TYPE_ROLE, roleName, pageable);
        if (resultPage != null && resultPage.getContent() != null && resultPage.getNumberOfElements() > 0){
            for (Resource c:resultPage.getContent()){
                roleService.prepareRole(c);
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/roles");
        return new ResponseEntity<>(resultPage!=null?resultPage.getContent():new ArrayList<Resource>(), headers, HttpStatus.OK);
    }

    /**
     * GET  /roles/:id : get the "id" role.
     *
     * @param id the id of the role to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the role, or with status 404 (Not Found)
     */
    @GetMapping("/roles/{id}")
    public ResponseEntity<Resource> getRole(@PathVariable String id) {
        log.debug("REST request to get Role : {}", id);
        Resource result = roleService.find(id);
        if (result != null) {
            result = roleService.prepareRole(result);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * DELETE  /roles/:id : delete the "id" role.
     *
     * @param id the id of the role to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        log.debug("REST request to delete Role : {}", id);
        Resource role = roleService.find(id);
        roleService.delete(role);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".deleted", role.getResourceName().toString())).build();
    }

    /**
     * GET  /roles/assignedUsers/:id : get the "id" role.
     *
     * @param id the id of the role to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the role, or with status 404 (Not Found)
     */
    @GetMapping("/roles/assignedUsers/{id}")
    public ResponseEntity<List<Subject>> findAllAssignedUsers(@PathVariable String id) {
        Set<? extends Subject2Res> subject2ResSet = roleService.find(id).getSubject2Res();
        List<Subject> result = new ArrayList();
        if (subject2ResSet != null) {
            for (Subject2Res s : subject2ResSet) {
                Subject subject = s.getSubject();
                subject = userService.prepareSubject(subject);
                result.add(subject);
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET  /roles/assignedFuncs/:id : get the "id" role.
     *
     * @param id the id of the role to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the role, or with status 404 (Not Found)
     */
    @GetMapping("/roles/assignedFuncs/{id}")
    public ResponseEntity<List<Resource>> findAllAssignedFuncs(@PathVariable String id) {
        Set<? extends Res2Res> res2ResSet = roleService.find(id).getRes2RessForParentResId();
        List<Resource> result = new ArrayList();
        if (res2ResSet != null) {
            for (Res2Res r : res2ResSet) {
                Resource resource = r.getResourceByResourceId();
                resource = roleService.prepareRole(resource);
                result.add(resource);
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET  /roles/assignedGroups/:id : get the "id" role.
     *
     * @param id the id of the role to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the role, or with status 404 (Not Found)
     */
    @GetMapping("/roles/assignedGroups/{id}")
    public ResponseEntity<List<Group>> findAllAssignedGroups(@PathVariable String id) {
        Set<? extends Group2Res> group2Reses = roleService.find(id).getGroup2Ress();
        List<Group> result = new ArrayList();
        if(group2Reses != null){
            for(Group2Res g : group2Reses){
                Group group = g.getGroup();
                group = groupService.prepareGroup(group);
                result.add(group);
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * PUT  /roles/unAssignUsers/:id : UnAssign selected users.
     *
     * @param resourceId the role
     * @param subjects the users to be unassign
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles/unAssignUsers/{resourceId}")
    public ResponseEntity<Void> unAssignUsers(@PathVariable String resourceId, @RequestBody Subject[] subjects) {
        log.debug("REST request to unassign users : {}", resourceId);
        roleService.unAssignUsers(resourceId, subjects);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".unAssigned", "")).build();
    }

    /**
     * PUT  /roles/unAssignFuncs/:id : UnAssign selected roles.
     *
     * @param resourceId the role
     * @param functions the roles to be unassign
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles/unAssignFuncs/{resourceId}")
    public ResponseEntity<Void> unAssignFuncs(@PathVariable String resourceId, @RequestBody Resource[] functions) {
        log.debug("REST request to unassign functions : {}", resourceId);
        roleService.unAssignFuncs(resourceId, functions);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".unAssigned", "")).build();
    }

    /**
     * PUT  /roles/unAssignGroups/:id : UnAssign selected roles.
     *
     * @param resourceId the role
     * @param groups the roles to be unassign
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles/unAssignGroups/{resourceId}")
    public ResponseEntity<Void> unAssignGroups(@PathVariable String resourceId, @RequestBody Group[] groups) {
        log.debug("REST request to unassign functions : {}", resourceId);
        roleService.unAssignGroups(resourceId, groups);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".unAssigned", "")).build();
    }

    /**
     * GET  /roles/search/unAssignedUsers : get all the unassign users.
     *
     * @param page the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/roles/search/unAssignedUsers")
    public ResponseEntity<List<Subject>> findUnassignedUsers(@ApiParam String resourceId, @ApiParam String firstName, Pageable page) {
        Page<Subject> resultPage = roleService.findUnassignedSubjects(resourceId, firstName, "", "", page);
        for (Subject subject : resultPage){
            userService.prepareSubject(subject);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/roles/search/unAssignedUsers");
        return new ResponseEntity<>(resultPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /roles/search/unAssignedFuncs : get all unassign roles.
     *
     * @param page the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/roles/search/unAssignedFuncs")
    public ResponseEntity<List<Resource>> findUnAssignedFuncs(@ApiParam String resourceId, @ApiParam String roleName, Pageable page) {
        Page<Resource> resultPage = roleService.findUnassignedFunctions(resourceId, roleName, page);
        for (Resource resource : resultPage){
            roleService.prepareRole(resource);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/roles/search/unAssignedFuncs");
        return new ResponseEntity<>(resultPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /roles/search/unAssignedGroups : get all unassign roles.
     *
     * @param page the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/roles/search/unAssignedGroups")
    public ResponseEntity<List<Group>> findUnAssignedGroups(@ApiParam String resourceId, @ApiParam String groupName, Pageable page) {
        Page<Group> resultPage = roleService.findUnassignedGroups(resourceId, "", groupName, page);
        for (Group group : resultPage){
            groupService.prepareGroup(group);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/roles/search/unAssignedGroups");
        return new ResponseEntity<>(resultPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * PUT  /roles/assignUsers/:id : assign selected users.
     *
     * @param resourceId the role
     * @param subjects the users to be assign
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles/assignUsers/{resourceId}")
    public ResponseEntity<Void> assignUsers(@PathVariable String resourceId, @RequestBody Subject[] subjects) {
        log.debug("REST request to assign users : {}", resourceId);
        roleService.assignSubjects(resourceId, subjects);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".assigned", "")).build();
    }

    /**
     * PUT  /roles/assignFuncs/:id : assign selected users.
     *
     * @param resourceId the role
     * @param functions the roles to be assign
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles/assignFuncs/{resourceId}")
    public ResponseEntity<Void> assignRoles(@PathVariable String resourceId, @RequestBody Resource[] functions) {
        log.debug("REST request to assign functions : {}", resourceId);
        roleService.assignFuncs(resourceId, functions);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".assigned", "")).build();
    }

    /**
     * PUT  /roles/assignGroups/:id : assign selected users.
     *
     * @param resourceId the role
     * @param groups the roles to be assign
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles/assignGroups/{resourceId}")
    public ResponseEntity<Void> assignGroups(@PathVariable String resourceId, @RequestBody Group[] groups) {
        log.debug("REST request to assign groups : {}", resourceId);
        roleService.assignGroups(resourceId, groups);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".assigned", "")).build();
    }
    
	@InitBinder 
	public void initBinder(WebDataBinder binder) { 
	    binder.setDisallowedFields(new String[]{}); 
	}
}
