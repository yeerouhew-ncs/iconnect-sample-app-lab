package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;
import com.ncs.itrust5.acm.app.service.AcmApplicationService;
import com.ncs.itrust5.ss5.domain.Application;
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

/**
 * REST controller for managing Application.
 */
@RestController
@RequestMapping("/api")
public class ApplicationResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationResource.class);

    private static final String ENTITY_NAME = "application";

    private AcmApplicationService amcApplicationService;

    public ApplicationResource(AcmApplicationService amcApplicationService) {
        this.amcApplicationService = amcApplicationService;
    }

    /**
     * POST  /applications : Create a new application.
     *
     * @param application the application to create
     * @return the ResponseEntity with status 201 (Created) and with body the new application, or with status 400 (Bad Request) if the application has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/applications")
    public ResponseEntity<Application> createApplication(@Valid @RequestBody Application application) throws URISyntaxException {
        log.debug("REST request to save Application : {}", application);
        if (StringUtils.isNotBlank(application.getAppCode())) {
            List<Application> applicationList = amcApplicationService.findByAppCode(application.getAppCode());
            if (applicationList != null && !applicationList.isEmpty()) {
              return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createAlert("", "Application already exists with code [ " + application.getAppCode() + " ]"))
                    .body(null);
            }
        }

        Application result = amcApplicationService.save(application);
        amcApplicationService.prepareApplication(result);
        return ResponseEntity.created(new URI("/api/applications/" + result.getAppId()))
            .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".created", result.getAppCode().toString()))
            .body(result);
    }

    /**
     * PUT  /applications : Updates an existing application.
     *
     * @param application the application to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated application,
     * or with status 400 (Bad Request) if the application is not valid,
     * or with status 500 (Internal Server Error) if the application couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/applications")
    public ResponseEntity<Application> updateApplication(@Valid @RequestBody Application application) throws URISyntaxException {
        log.debug("REST request to update Application : {}", application);
        Application result = amcApplicationService.save(application);
        amcApplicationService.prepareApplication(application);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("acmAdmin" + "." + ENTITY_NAME + ".updated", application.getAppCode().toString()))
            .body(result);
    }

    /**
     * GET  /applications : get all the applications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of applications in body
     */
    @GetMapping("/applications")
    public ResponseEntity<List<Application>> findApplications(@ApiParam String appCode, @ApiParam String appName, @ApiParam String appDesc, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Applications");
        Page<Application> page = amcApplicationService.findApplication(appCode, appName, appDesc, pageable);
        if (page != null && page.getContent() != null && page.getNumberOfElements() > 0) {
            for (Application c : page.getContent()){
                amcApplicationService.prepareApplication(c);
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/applications");
        return new ResponseEntity<>(page!=null?page.getContent():new ArrayList<Application>(), headers, HttpStatus.OK);
    }

    /**
     * GET  /applications/:id : get the "id" application.
     *
     * @param id the id of the application to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the application, or with status 404 (Not Found)
     */
    @GetMapping("/applications/{id}")
    public ResponseEntity<Application> getApplication(@PathVariable String id) {
        log.debug("REST request to get Application : {}", id);
        Application application = amcApplicationService.find(id);
        amcApplicationService.prepareApplication(application);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(application));
    }

    @InitBinder 
	public void initBinder(WebDataBinder binder) { 
	    binder.setDisallowedFields(new String[]{}); 
	}
}
