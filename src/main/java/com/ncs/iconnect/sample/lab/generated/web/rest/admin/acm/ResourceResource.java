package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;
import com.ncs.itrust5.acm.app.service.AcmApplicationService;
import com.ncs.itrust5.acm.function.service.ResourceService;
import com.ncs.itrust5.ss5.domain.Application;
import com.ncs.itrust5.ss5.domain.Res2Res;
import com.ncs.itrust5.ss5.domain.Resource;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Resource.
 */
@RestController
@RequestMapping("/api")
public class ResourceResource {

    private final Logger log = LoggerFactory.getLogger(ResourceResource.class);

    private static final String ENTITY_NAME = "resource";

    private final ResourceService resourceService;

    private AcmApplicationService acmAppService;

    public ResourceResource(ResourceService resourceService, AcmApplicationService acmAppService) {
        this.resourceService = resourceService;
        this.acmAppService = acmAppService;
    }

    /**
     * POST  /resources : Create a new resource.
     *
     * @param resource the resource to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resource, or with status 400 (Bad Request) if the resource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/resources")
    public ResponseEntity<Resource> createResource(@Valid @RequestBody Resource resource) throws URISyntaxException {
        log.debug("REST request to save Resource : {}", resource);
        if (StringUtils.isNotBlank(resource.getAppId()) && StringUtils.isNotBlank(resource.getResourceName()) && StringUtils.isNotBlank(resource.getResourceType())) {
            List<Resource> resourceList = resourceService.findResourcesByResourceTypeAndResourceName(resource.getResourceType(), resource.getResourceName(), resource.getAppId());
            if (!resourceList.isEmpty()) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Resource already exists with name [ " + resource.getResourceName() + " ]")).body(null);
            }
        }
        Resource result = resourceService.save(resource);
        if (result != null) {
            result = prepareResource(result);
            return ResponseEntity.created(new URI("/api/resources/" + result.getResourceId()))
            .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".created", result.getResourceName().toString()))
            .body(result);
        }
        return null;
    }

    /**
     * PUT  /resources : Updates an existing resource.
     *
     * @param resource the resource to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resource,
     * or with status 400 (Bad Request) if the resource is not valid,
     * or with status 500 (Internal Server Error) if the resource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resources")
    public ResponseEntity<Resource> updateResource(@Valid @RequestBody Resource resource) throws URISyntaxException {
        log.debug("REST request to update Resource : {}", resource);
        if (StringUtils.isNotBlank(resource.getAppId()) && StringUtils.isNotBlank(resource.getResourceName()) && StringUtils.isNotBlank(resource.getResourceType())) {
            List<Resource> resourceList = resourceService.findResourcesByResourceTypeAndResourceName(resource.getResourceType(), resource.getResourceName(), resource.getAppId());
            if (!resourceList.isEmpty()) {
                for (Resource r : resourceList) {
                    if (!r.getResourceId().equalsIgnoreCase(resource.getResourceId())) {
                        return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Resource already exists with name [ " + resource.getResourceName() + " ]")).body(null);
                    }
                }
            }
        }

        Resource result = resourceService.save(resource);
        if (result != null) {
            result = prepareResource(result);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".updated", resource.getResourceName().toString()))
            .body(result);
    }

    /**
     * GET  /resources/applications : get all the application.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of resources in body
     */
    @GetMapping("/resources/applications")
    public ResponseEntity<List<Application>> findAllApplication() {
        log.debug("REST request to get all applications");
        List<Application> result = acmAppService.getAllApplications();
        for (Application r : result){
            r.setResources(null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET  /resources : get all the resources.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resources in body
     */
    @GetMapping("/resources")
    public ResponseEntity<List<Resource>> findResources(@ApiParam String applicationId, @ApiParam String resourceType, @ApiParam String resourceName, @ApiParam String resourcePath, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Resources");
        Page<Resource> resultPage = resourceService.findResources(applicationId, resourceType, resourceName, "", resourcePath, pageable);
        if (resultPage != null && resultPage.getContent() != null && resultPage.getNumberOfElements() > 0){
            for (Resource c : resultPage.getContent()){
                prepareResource(c);
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/resources");
        return new ResponseEntity<>(resultPage!=null?resultPage.getContent():new ArrayList<Resource>(), headers, HttpStatus.OK);
    }

    /**
     * GET  /resources/:id : get the "id" resource.
     *
     * @param id the id of the resource to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resource, or with status 404 (Not Found)
     */
    @GetMapping("/resources/{id}")
    public ResponseEntity<Resource> getResource(@PathVariable String id) {
        log.debug("REST request to get Resource : {}", id);
        Resource result = resourceService.find(id);
        if (result != null) {
            result = resourceService.prepareResource(result);

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
     * DELETE  /resources/:id : delete the "id" resource.
     *
     * @param id the id of the resource to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/resources/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable String id) {
        log.debug("REST request to delete Resource : {}", id);
        Resource resource = resourceService.find(id);
        resourceService.delete(resource);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".deleted", resource.getResourceName().toString())).build();
    }

    /**
     * PUT  /resources/unAssignFunctions/:id : UnAssign selected Resources.
     *
     * @param resourceId the resource
     * @param functions the Resources to be unassign
     * @return the ResponseEntity with status 200 (OK) and with body the updated resource,
     * or with status 400 (Bad Request) if the resource is not valid,
     * or with status 500 (Internal Server Error) if the resource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resources/unAssignFunctions/{resourceId}")
    public ResponseEntity<Void> unAssignFunctions(@PathVariable String resourceId, @RequestBody Resource[] functions) {
        log.debug("REST request to unassign resources : {}", resourceId);
        this.resourceService.updateResourceRemoveFunctions(resourceId, Arrays.asList(functions));
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".unAssigned", "")).build();
    }

    /**
     * GET  /resources/search/unAssignFunctions : get all functions unassigned to resource.
     *
     * @param page the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of functions in body
     */
    @GetMapping("/resources/search/unAssignFunctions")
    public ResponseEntity<List<Resource>> findUnAssignFunctions(@ApiParam String applicationId, @ApiParam String resourceId, @ApiParam String functionName, Pageable page) {
        Page<Resource> resultPage = resourceService.findUnassignedFunctions(resourceId, applicationId, functionName, "", page);
        if (resultPage != null && resultPage.getContent() != null && resultPage.getNumberOfElements() > 0) {
            for (Resource c:resultPage.getContent()){
                prepareResource(c);
            }

            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/resources/search/unAssignFunctions");
            return new ResponseEntity<>(resultPage.getContent(), headers, HttpStatus.OK);
        }

        return null;
    }

    /**
     * PUT  /resources/assignFunctions/:id : assign selected users.
     *
     * @param resourceId the resource
     * @param functions the functions to be assign
     * @return the ResponseEntity with status 200 (OK) and with body the updated resource,
     * or with status 400 (Bad Request) if the resource is not valid,
     * or with status 500 (Internal Server Error) if the resource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resources/assignFunctions/{resourceId}")
    public ResponseEntity<Void> assignFunctions(@PathVariable String resourceId, @RequestBody Resource[] functions) {
        log.debug("REST request to assign functions : {}", resourceId);
        resourceService.updateResourceAddFunctions(resourceId, Arrays.asList(functions));
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".assigned", "")).build();
    }


    protected Resource prepareResource(Resource origin) {
        Resource result = resourceService.prepareResource(origin);

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
