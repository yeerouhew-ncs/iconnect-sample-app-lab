package com.ncs.iconnect.sample.lab.generated.web.rest;

import java.util.List;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.github.jhipster.web.util.PaginationUtil;
import com.ncs.itrust5.ss5.service.AuditEventService;
import com.ncs.itrust5.ss5.to.SearchAuditEventCriteria;
import io.github.jhipster.web.util.ResponseUtil;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * REST controller for getting the audit events.
 */
@RestController
@RequestMapping("/management/audits")
public class AuditResource {

    private final AuditEventService auditEventService;

    public AuditResource(AuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    /**
     * GET /audits : get a page of AuditEvents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of AuditEvents in body
     */
    @GetMapping
    public ResponseEntity<List<AuditEvent>> getAll(Pageable pageable) {
        Page<AuditEvent> page = auditEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(params = {"fromDate", "toDate","user","event"})
    public ResponseEntity<List<AuditEvent>> searchRevision(
        @RequestParam(value = "fromDate") String fromDate,
        @RequestParam(value = "toDate") String toDate,
        @RequestParam(value = "user") String user,
        @RequestParam(value = "event") String event,
        Pageable pageable) {
    	
    	SearchAuditEventCriteria searchAuditEventCriteria = new SearchAuditEventCriteria(fromDate,toDate,user,event);
    	
    	 Page<AuditEvent> page = auditEventService.searchRevision(
    			 searchAuditEventCriteria,
    	          pageable);
    	        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    	        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    	
    } 

    /**
     * {@code GET  /audits} : get a page of {@link AuditEvent} between the {@code fromDate} and {@code toDate}.
     *
     * @param fromDate the start of the time period of {@link AuditEvent} to get.
     * @param toDate the end of the time period of {@link AuditEvent} to get.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of {@link AuditEvent} in body.
     */
    @GetMapping(params = {"fromDate", "toDate"})
    public ResponseEntity<List<AuditEvent>> getByDates(
        @RequestParam(value = "fromDate") LocalDate fromDate,
        @RequestParam(value = "toDate") LocalDate toDate,
        Pageable pageable) {

        Instant from = fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant to = toDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        Page<AuditEvent> page = auditEventService.findByDates(from, to, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /audits/:id : get an AuditEvent by id.
     *
     * @param id the id of the entity to get
     * @return the ResponseEntity with status 200 (OK) and the AuditEvent in body, or status 404 (Not Found)
     */
    @GetMapping("/{id:.+}")
    public ResponseEntity<AuditEvent> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(auditEventService.find(id));
    }
}
