package com.ncs.iconnect.sample.lab.generated.web.rest.admin.audit;

import com.ncs.iforge5.logadmin.domain.ViolationLogTO;
import com.ncs.iforge5.logadmin.to.DownloadFile;
import com.ncs.iforge5.logadmin.to.RevisionPageItemTO;
import com.ncs.iforge5.logadmin.to.SearchAuditLogCriteria;
import com.ncs.iforge5.logadmin.service.AuditLogService;
import com.ncs.iforge5.logadmin.service.ViolationLogService;
import com.ncs.iforge5.logadmin.to.SearchViolationLogCriteria;
import com.ncs.iframe5.component.validator.exception.BusinessValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.List;

@RestController
public class LogAdminResource {

    private Logger log = LoggerFactory.getLogger(LogAdminResource.class);

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private ViolationLogService violationLogService;

    @RequestMapping(
            value = "/api/log/auditLog",
            params = {
                    "fromDateAsStr",
                    "toDateAsStr",
                    "revision",
                    "userId",
                    "revisionType",
                    "funcName",
                    "tableName",
                    "businessKey"},
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<RevisionPageItemTO>> searchRevision(
            String fromDateAsStr,
            String toDateAsStr,
            Integer revision,
            String userId,
            String revisionType,
            String funcName,
            String tableName,
            String businessKey,
            Pageable pageable) {
        SearchAuditLogCriteria searchAuditLogCriteria = new SearchAuditLogCriteria(
                fromDateAsStr, toDateAsStr, revision, userId, revisionType, funcName, tableName, businessKey
        );
        Page<RevisionPageItemTO> page =  auditLogService.searchRevision(searchAuditLogCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/log/auditLog");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/log/downloadFile",
            params = {
                    "fromDateAsStr",
                    "toDateAsStr",
                    "revision",
                    "userId",
                    "revisionType",
                    "funcName",
                    "tableName",
                    "businessKey"},
            method = RequestMethod.GET)
    @ResponseBody
    public DownloadFile getDownloadFile(
            String fromDateAsStr,
            String toDateAsStr,
            Integer revision,
            String userId,
            String revisionType,
            String funcName,
            String tableName,
            String businessKey) {
        SearchAuditLogCriteria searchAuditLogCriteria = new SearchAuditLogCriteria(
                fromDateAsStr, toDateAsStr, revision, userId, revisionType, funcName, tableName, businessKey
        );
        DownloadFile downloadFile = auditLogService.getDownloadFile(searchAuditLogCriteria);
        return downloadFile;
    }

    @RequestMapping(
            value = "/api/log/exportCSV",
            params = {
                    "fromDateAsStr",
                    "toDateAsStr",
                    "revision",
                    "userId",
                    "revisionType",
                    "funcName",
                    "tableName",
                    "businessKey"},
            method = RequestMethod.GET)
    @ResponseBody
    public void exportToCSV(
            String fromDateAsStr,
            String toDateAsStr,
            Integer revision,
            String userId,
            String revisionType,
            String funcName,
            String tableName,
            String businessKey,
            HttpServletRequest request,
            HttpServletResponse response) {

        SearchAuditLogCriteria searchAuditLogCriteria = new SearchAuditLogCriteria(
                fromDateAsStr, toDateAsStr, revision, userId, revisionType, funcName, tableName, businessKey
        );
        File file = auditLogService.exportToCSV(searchAuditLogCriteria);
        if(null != file){
            try {
                response.setContentType("application/download");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "");
                try (InputStream is = new FileInputStream(file)) {
                    try (OutputStream out = response.getOutputStream()) {
                        byte[] bytes = new byte[1024];
                        int b;
                        while ((b = is.read(bytes)) > 0) {
                            out.write(bytes, 0, b);
                        }
                        out.flush();
                    }
                }
                response.getOutputStream().flush();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new BusinessValidationException(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/api/log/violationAccess", params = {"fromDateAsStr", "toDateAsStr", "loginName"}, method = RequestMethod.GET)
    @ResponseBody
    public Page<ViolationLogTO> searchViolationLog(String fromDateAsStr, String toDateAsStr, String loginName, Pageable pageable) {
        SearchViolationLogCriteria searchViolationLogCriteria = new SearchViolationLogCriteria(fromDateAsStr, toDateAsStr, loginName);
        return violationLogService.searchViolationLog(searchViolationLogCriteria, pageable);
    }

    @RequestMapping(value = "/api/log/funcNames", params = {"funcName"}, method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchFunctionNames(String funcName){
        return auditLogService.searchFuncNames(funcName);
    }
    
    public void setAuditLogService(AuditLogService auditLogService) {
		this.auditLogService = auditLogService;
	}

	public void setViolationLogService(ViolationLogService violationLogService) {
		this.violationLogService = violationLogService;
	}
}
