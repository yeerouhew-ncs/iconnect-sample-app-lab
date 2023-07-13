package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;
import com.ncs.itrust5.acm.app.service.AcmApplicationService;
import com.ncs.itrust5.acm.function.service.ResourceService;
import com.ncs.itrust5.acm.group.service.GroupService;
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
import java.util.*;

/**
 * REST controller for managing Function.
 */
@RestController
@RequestMapping("/api")
public class FunctionResource {

    private final Logger log = LoggerFactory.getLogger(FunctionResource.class);

    private static final String ENTITY_NAME = "function";

    private final ResourceService functionService;

    private final GroupService groupService;

    private final UserService userService;

    private AcmApplicationService acmAppService;

    public FunctionResource(ResourceService functionService, GroupService groupService, UserService userService, AcmApplicationService acmAppService) {
        this.functionService = functionService;
        this.groupService = groupService;
        this.userService = userService;
        this.acmAppService = acmAppService;
    }

    /**
     * POST  /functions : Create a new function.
     *
     * @param function the function to create
     * @return the ResponseEntity with status 201 (Created) and with body the new function, or with status 400 (Bad Request) if the function has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/functions")
    public ResponseEntity<Resource> createFunction(@Valid @RequestBody Resource function) throws URISyntaxException {
        log.debug("REST request to save Function : {}", function);
        if (StringUtils.isNotBlank(function.getAppId()) && StringUtils.isNotBlank(function.getResourceName())) {
            List<Resource> resourceList = functionService.findResourcesByResourceTypeAndResourceName(ITrustConstants.RES_TYPE_FUNCTION, function.getResourceName(), function.getAppId());
            if (!resourceList.isEmpty()) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Function already exists with name [ " + function.getResourceName() + " ]")).body(null);
            }
        }
        function.setResourceType(ITrustConstants.RES_TYPE_FUNCTION);
        Resource result = functionService.save(function);
        if (result != null) {
            result = prepareResource(result);
            return ResponseEntity.created(new URI("/api/functions/" + result.getResourceId()))
            .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".created", result.getResourceName().toString()))
            .body(result);
        }
        return null;
    }

    /**
     * PUT  /functions : Updates an existing function.
     *
     * @param function the function to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated function,
     * or with status 400 (Bad Request) if the function is not valid,
     * or with status 500 (Internal Server Error) if the function couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/functions")
    public ResponseEntity<Resource> updateFunction(@Valid @RequestBody Resource function) throws URISyntaxException {
        log.debug("REST request to update Function : {}", function);
        if (StringUtils.isNotBlank(function.getAppId()) && StringUtils.isNotBlank(function.getResourceName())) {
            List<Resource> resourceList = functionService.findResourcesByResourceTypeAndResourceName(ITrustConstants.RES_TYPE_FUNCTION, function.getResourceName(), function.getAppId());
            if (!resourceList.isEmpty()) {
                for (Resource r : resourceList) {
                    if (!r.getResourceId().equalsIgnoreCase(function.getResourceId())) {
                        return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Function already exists with name [ " + function.getResourceName() + " ]")).body(null);
                    }
                }
            }
        }

        Resource result = functionService.save(function);
        if (result != null) {
            result = prepareResource(result);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".updated", function.getResourceName().toString()))
            .body(result);
    }

    /**
     * GET  /functions/applications : get all the application.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of functions in body
     */
    @GetMapping("/functions/applications")
    public ResponseEntity<List<Application>> findAllApplication() {
        log.debug("REST request to get all applications");
        List<Application> result = acmAppService.getAllApplications();
        for (Application r : result){
            r.setResources(null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET  /functions : get all the functions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of functions in body
     */
    @GetMapping("/functions")
    public ResponseEntity<List<Resource>> findFunctions(@ApiParam String applicationId, @ApiParam String functionName, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Functions");
        Page<Resource> resultPage = functionService.findResourcesByType(applicationId, ITrustConstants.RES_TYPE_FUNCTION, functionName, null, null, pageable);
        if (resultPage != null && resultPage.getContent() != null && resultPage.getNumberOfElements() > 0){
            for (Resource c : resultPage.getContent()){
                prepareResource(c);
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/functions");
        return new ResponseEntity<>(resultPage != null?resultPage.getContent():new ArrayList<Resource>(), headers, HttpStatus.OK);
    }

    /**
     * GET  /functions/:id : get the "id" function.
     *
     * @param id the id of the function to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the function, or with status 404 (Not Found)
     */
    @GetMapping("/functions/{id}")
    public ResponseEntity<Resource> getFunction(@PathVariable String id) {
        log.debug("REST request to get Function : {}", id);
        Resource result = functionService.find(id);
        if (result != null) {
            result = functionService.prepareResource(result);

            //set null for lazy loading fields
            Application app= new Application();
            app.setAppId(result.getApplication().getAppId());
            app.setAppCode(result.getApplication().getAppCode());
            app.setAppName(result.getApplication().getAppName());
            app.setAppDesc(result.getApplication().getAppDesc());
            result.setApplication(app);
            result.setGroup2Ress(null);
            result.setSubject2Ress(null);

            if (result.getRes2RessForParentResId()!=null && result.getRes2RessForParentResId().size()>0){
                for (Res2Res r2r : result.getRes2RessForParentResId()){
                    Resource res = r2r.getResourceByResourceId();
                    r2r.setResourceByParentResId(prepareResource(res));
                    r2r.setResourceByResourceId(prepareResource(res));
                }
            }
            if (result.getRes2RessForResourceId()!=null && result.getRes2RessForResourceId().size()>0){
                for (Res2Res r2r : result.getRes2RessForResourceId()){
                    Resource res = r2r.getResourceByParentResId();
                    r2r.setResourceByParentResId(prepareResource(res));
                    r2r.setResourceByResourceId(null);
                }
            }
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * DELETE  /functions/:id : delete the "id" function.
     *
     * @param id the id of the function to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/functions/{id}")
    public ResponseEntity<Void> deleteFunction(@PathVariable String id) {
        log.debug("REST request to delete Function : {}", id);
        Resource function = functionService.find(id);
        functionService.delete(function);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".deleted", function.getResourceName().toString())).build();
    }

    /**
     * PUT  /functions/unAssignResources/:id : UnAssign selected Resources.
     *
     * @param resourceId the resource
     * @param resources the Resources to be unassign
     * @return the ResponseEntity with status 200 (OK) and with body the updated resource,
     * or with status 400 (Bad Request) if the resource is not valid,
     * or with status 500 (Internal Server Error) if the resource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/functions/unAssignResources/{resourceId}")
    public ResponseEntity<Void> unAssignResources(@PathVariable String resourceId, @RequestBody Resource[] resources) {
        log.debug("REST request to unassign resources : {}", resourceId);
        this.functionService.updateFunctionRemoveResources(resourceId, Arrays.asList(resources));
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".unAssigned", "")).build();
    }

    /**
     * GET  /functions/search/unAssignResources : get all resources unassigned to functions.
     *
     * @param page the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of functions in body
     */
    @GetMapping("/functions/search/unAssignResources")
    public ResponseEntity<List<Resource>> findUnAssignResources(@ApiParam String applicationId, @ApiParam String resourceId, @ApiParam String resourceName, Pageable page) {
        Page<Resource> resultPage = functionService.findUnassignedResources(resourceId, applicationId, "", resourceName, "", "", page);
        if (resultPage != null && resultPage.getContent() != null && resultPage.getNumberOfElements() > 0) {
            for (Resource c:resultPage.getContent()){
                prepareResource(c);
            }
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/functions/search/unAssignResources");
        return new ResponseEntity<>(resultPage!=null?resultPage.getContent():new ArrayList<Resource>(), headers, HttpStatus.OK);
    }

    /**
     * PUT  /functions/assignResources/:id : assign selected users.
     *
     * @param resourceId the resource
     * @param resources the resources to be assign
     * @return the ResponseEntity with status 200 (OK) and with body the updated resource,
     * or with status 400 (Bad Request) if the resource is not valid,
     * or with status 500 (Internal Server Error) if the resource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/functions/assignResources/{resourceId}")
    public ResponseEntity<Void> assignResources(@PathVariable String resourceId, @RequestBody Resource[] resources) {
        log.debug("REST request to assign resources : {}", resourceId);
        functionService.updateFunctionAddResources(resourceId, Arrays.asList(resources));
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".assigned", "")).build();
    }

    /**
     * PUT  /functions/unAssignRoles/:id : UnAssign selected roles.
     *
     * @param resourceId the resource
     * @param resources the roles to be unassign
     * @return the ResponseEntity with status 200 (OK) and with body the updated resource,
     * or with status 400 (Bad Request) if the resource is not valid,
     * or with status 500 (Internal Server Error) if the resource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/functions/unAssignRoles/{resourceId}")
    public ResponseEntity<Void> unAssignRoles(@PathVariable String resourceId, @RequestBody Resource[] resources) {
        log.debug("REST request to unassign roles : {}", resourceId);
        functionService.updateFunctionRemoveRoles(resourceId, Arrays.asList(resources));
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".unAssigned", "")).build();
    }

    /**
     * GET  /functions/search/unAssignRoles : get all roles unassigned to functions.
     *
     * @param page the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of functions in body
     */
    @GetMapping("/functions/search/unAssignRoles")
    public ResponseEntity<List<Resource>> findUnAssignRoles(@ApiParam String applicationId, @ApiParam String resourceId, @ApiParam String roleName, Pageable page) {
        Page<Resource> resultPage = functionService.findUnassignedRoles(resourceId, applicationId, roleName, "", page);
        if (resultPage != null && resultPage.getContent() != null && resultPage.getNumberOfElements() > 0) {
            for (Resource c:resultPage.getContent()){
                prepareResource(c);
            }
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/functions/search/unAssignRoles");
        return new ResponseEntity<>(resultPage!=null?resultPage.getContent():new ArrayList<Resource>(), headers, HttpStatus.OK);
    }

    /**
     * PUT  /functions/assignRoles/:id : assign selected users.
     *
     * @param resourceId the resource
     * @param resources the resources to be assign
     * @return the ResponseEntity with status 200 (OK) and with body the updated resource,
     * or with status 400 (Bad Request) if the resource is not valid,
     * or with status 500 (Internal Server Error) if the resource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/functions/assignRoles/{resourceId}")
    public ResponseEntity<Void> assignRoles(@PathVariable String resourceId, @RequestBody Resource[] resources) {
        log.debug("REST request to assign resources : {}", resourceId);
        functionService.updateFunctionAddRoles(resourceId, Arrays.asList(resources));
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".assigned", "")).build();
    }


    protected Resource prepareResource(Resource origin) {
        Resource result = functionService.prepareResource(origin);
        if (result != null) {
            //set null for lazy loading fields
            Application app = new Application();
            if (result.getApplication() != null) {
                app.setAppId(result.getApplication().getAppId());
                app.setAppCode(result.getApplication().getAppCode());
                app.setAppName(result.getApplication().getAppName());
                app.setAppDesc(result.getApplication().getAppDesc());
            }
            result.setApplication(app);
            result.setGroup2Ress(null);
            result.setRes2RessForResourceId(null);
            result.setRes2ResTOsForParentResId(null);
            result.setSubject2Ress(null);
        }
        return result;
    }

    @InitBinder 
	public void initBinder(WebDataBinder binder) { 
	    binder.setDisallowedFields(new String[]{}); 
	}
}
