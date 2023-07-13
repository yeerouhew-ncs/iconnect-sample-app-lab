package com.ncs.iconnect.sample.lab.generated.web.rest.admin.code;

import com.ncs.iforge5.codeadmin.domain.CodeInt;
import com.ncs.iforge5.codeadmin.dto.CodeIntDTO;
import com.ncs.iforge5.codeadmin.dto.CodeTypeDTO;
import com.ncs.iforge5.codeadmin.dto.CodeTypeSearchCriteria;
import com.ncs.iforge5.codeadmin.dto.Locale2DescMapDTO;
import com.ncs.iforge5.codeadmin.service.CodeAdminService;
import com.ncs.iframe5.commons.AppConfig;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;
import com.ncs.itrust5.sss.dto.AppDTO;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Code admin.
 */
@RestController
@RequestMapping("/api")
public class CodeAdminResource {

    private final Logger log = LoggerFactory.getLogger(CodeAdminResource.class);

    private static final String ENTITY_NAME = "codeType";
    private static final String ENTITY_NAME_CODE = "code";

    private CodeAdminService codeAdminService;

    private MessageSource msgSource;

    private AppConfig appConfig;

    public CodeAdminResource(CodeAdminService codeAdminService, MessageSource msgSource, AppConfig appConfig) {
        this.codeAdminService = codeAdminService;
        this.msgSource = msgSource;
        this.appConfig = appConfig;
    }

    @GetMapping(value = "/codeAdmin/appList")
    @ResponseBody
    public List<AppDTO> getAppList() {
        return codeAdminService.getAppList();
    }

    /**
     * POST  /codeAdmin/codeType : Create a new codeType.
     *
     * @param codeType the codeType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new codeType, or with status 400 (Bad Request) if the codeType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/codeAdmin/codeType")
    public ResponseEntity<CodeTypeDTO> createCodeType(@Valid @RequestBody CodeTypeDTO codeType) throws URISyntaxException {
        log.debug("REST request to save CodeType : {}", codeType);
        try {
            CodeTypeDTO result = this.codeAdminService.createCodeType(codeType);
            return ResponseEntity.created(new URI("/api/codeAdmin/codeType/" + result.getCodeTypePk())).headers(HeaderUtil.createAlert("codeAdmin" + "." + ENTITY_NAME + ".created", result.getCodeTypeId())).body(result);
        } catch (InvalidDataAccessResourceUsageException var4) {
            this.log.error(var4.getMessage(), var4);
            String exceptionStr = this.getRootCauseMessageDetail(var4);
            return StringUtils.isNotEmpty(exceptionStr) ? ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", exceptionStr)).body(null) : ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "")).body(null);
        } catch (Exception var5) {
            this.log.error(var5.getMessage(), var5);
            return var5.getCause().getCause() instanceof SQLException ? ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", var5.getCause().getCause().getMessage())).body(null) : ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", var5.getMessage())).body(null);
        }

    }

    /**
     * PUT  /codeAdmin/codeType : Updates an existing codeType.
     *
     * @param codeType the codeType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated codeType,
     * or with status 400 (Bad Request) if the codeType is not valid,
     * or with status 500 (Internal Server Error) if the codeType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/codeAdmin/codeType")
    public ResponseEntity<CodeTypeDTO> updateCodeType(@Valid @RequestBody CodeTypeDTO codeType) throws URISyntaxException {
        log.debug("REST request to update CodeType : {}", codeType);
        try {
            CodeTypeDTO result = this.codeAdminService.editCodeType(codeType);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("codeAdmin" + "." + ENTITY_NAME + ".updated", codeType.getCodeTypePk().toString()))
                .body(result);
        } catch (InvalidDataAccessResourceUsageException var4) {
            String exceptionStr = this.getRootCauseMessageDetail(var4);
            if(StringUtils.isNotEmpty(exceptionStr)) {
                return StringUtils.isNotEmpty(exceptionStr) ? ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", exceptionStr)).body(null) : ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "")).body(null);
            } else {
                return StringUtils.isNotEmpty(exceptionStr) ? ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", this.msgSource.getMessage("error.create.codetype.code.table.name.does.not.exist", (Object[])null, LocaleContextHolder.getLocale()))).body(null) : ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "")).body(null);
            }
        }
    }

    /**
     * GET  /codeAdmin/codeType : get all the codeTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of codeTypes in body
     */
    @GetMapping("/codeAdmin/codeType")
    public ResponseEntity<List<CodeTypeDTO>> findCodeTypes(@ApiParam String appId, @ApiParam String codeType, @ApiParam String codeTypeDesc, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CodeTypes");
        CodeTypeSearchCriteria searchCriteria = new CodeTypeSearchCriteria(appId, codeType, codeTypeDesc);
        Page<CodeTypeDTO> page =  this.codeAdminService.searchCodeType(searchCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/codeAdmin/codeType");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /codeAdmin/codeType/:id : get the "id" codeType.
     *
     * @param id the id of the codeType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the codeType, or with status 404 (Not Found)
     */
    @GetMapping("/codeAdmin/codeType/{id}")
    public ResponseEntity<CodeTypeDTO> getCodeType(@PathVariable String id) {
        log.debug("REST request to get CodeType : {}", id);
        CodeTypeDTO codeType = this.codeAdminService.findCodeTypeById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(codeType));
    }

    @DeleteMapping("/codeAdmin/codeType/{codeTypePk}")
    public ResponseEntity<Void> deleteCodeType(@PathVariable String codeTypePk) {
        log.debug("REST request to delete CodeType : {}", codeTypePk);
        this.codeAdminService.deleteCodeType(codeTypePk);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("codeAdmin" + "." + ENTITY_NAME + ".deleted", codeTypePk)).build();
    }

    /**
     * GET  /codeAdmin/codesByCodeTypePk
     *
     * @param codeTypePk the pk of the codeType
     * @param isExternal
     * @return the ResponseEntity with status 200 (OK) and with body the codeType, or with status 404 (Not Found)
     */
    @GetMapping("/codeAdmin/codesByCodeTypePk")
    public ResponseEntity<List<CodeIntDTO>> getCodesByCodeTypePk(@ApiParam String codeTypePk, @ApiParam boolean isExternal) {
        log.debug("REST request to get codes by pk of codetype : {}", codeTypePk);
        List<CodeIntDTO> codes = isExternal ? this.codeAdminService.getCodesByExternalCodeType(codeTypePk) : this.codeAdminService.getCodesByInternalCodeTypeGroupByCodeId(codeTypePk);
        return new ResponseEntity<>(codes, HttpStatus.OK);
    }

    /**
     * GET  /codeAdmin/codesByCodeTypePkAndCodeId
     *
     * @param codeTypePk
     * @param codeId
     * @return the ResponseEntity with status 200 (OK) and with body the codeType, or with status 404 (Not Found)
     */
    @GetMapping("/codeAdmin/codesByCodeTypePkAndCodeId")
    public ResponseEntity<List<CodeIntDTO>> getCodesByCodeTypePkAndCodeId(@ApiParam String codeTypePk, @ApiParam String codeId) {
        log.debug("REST request to get codes by pk of codetype : {}", codeTypePk);
        List<CodeIntDTO> codes = this.codeAdminService.findByCodeTypeIdAndCodeId(codeTypePk, codeId);
        return new ResponseEntity<>(codes, HttpStatus.OK);
    }

    /**
     * GET  /codeAdmin/internalParentCodeTypes
     *
     * @param appId
     * @param codeTypePk the pk of the codeType
     * @return the ResponseEntity with status 200 (OK) and with body the codeType, or with status 404 (Not Found)
     */
    @GetMapping("/codeAdmin/codeTypesByAppIdAndCodeTypePkNot")
    public ResponseEntity<List<CodeTypeDTO>> getCodeTypesByAppIdAndCodeTypePkNot(@ApiParam String appId, @ApiParam String codeTypePk) {
        log.debug("REST request to get parent codetypes : {}", codeTypePk);
        List<CodeTypeDTO> codeTypes = this.codeAdminService.getInternalCodeTypesByAppIdAndCodeTypePkNot(appId, codeTypePk);
        return new ResponseEntity<>(codeTypes, HttpStatus.OK);
    }

    /**
     * GET  /codeAdmin/code
     *
     * @param id
     * @return the ResponseEntity with status 200 (OK) and with body the codeType, or with status 404 (Not Found)
     */
    @GetMapping("/codeAdmin/code/{id}")
    public ResponseEntity<CodeIntDTO> getCode(@PathVariable String id) {
        log.debug("REST request to get Code : {}", id);
        CodeIntDTO code = this.codeAdminService.findCodeById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(code));
    }

    /**
     * POST  /codeAdmin/code : Create a new code.
     *
     * @param code the code to create
     * @return the ResponseEntity with status 201 (Created) and with body the new codeType, or with status 400 (Bad Request) if the codeType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/codeAdmin/code")
    public ResponseEntity<CodeIntDTO> createCode(@Valid @RequestBody CodeIntDTO code) throws URISyntaxException {
        log.debug("REST request to save Code : {}", code);
        List<Locale2DescMapDTO> locale2DescMapDTOList = code.getCodeDescs();
        Iterator var3 = locale2DescMapDTOList.iterator();

        while(var3.hasNext()) {
            Locale2DescMapDTO locale2DescMapDTO = (Locale2DescMapDTO)var3.next();
            if(StringUtils.isNotBlank(locale2DescMapDTO.getLocale()) && StringUtils.isNotBlank(code.getCodeTypePK()) && StringUtils.isNotBlank(code.getCodeId())) {
                List<CodeInt> codeIntList = this.codeAdminService.findByCodeTypeIdAndCodeIdAndLocale(code.getCodeTypePK(), code.getCodeId(), locale2DescMapDTO.getLocale());
                if(!codeIntList.isEmpty()) {
                    return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Code already exists with code [ " + code.getCodeId() + " ]")).body(null);
                }
            }
        }
        return createCodeResponse(code);
    }

    private ResponseEntity<CodeIntDTO> createCodeResponse(CodeIntDTO code) throws URISyntaxException{
    	if(code.getEffectiveDt() != null && code.getExpiryDt() != null && code.getEffectiveDt().after(code.getExpiryDt())) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Effect date must greater than expiry date!")).body(null);
        }
        else if(StringUtils.isNotBlank(code.getCodeTypePK()) && StringUtils.isNotBlank(code.getParentCodeTypePk()) && code.getCodeTypePK().equalsIgnoreCase(code.getParentCodeTypePk())) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Parent code type can't equal to code type!")).body(null);
        }
        else {
            CodeIntDTO result = this.codeAdminService.createCode(code);
            return ResponseEntity.created(new URI("/api/codeAdmin/codeTypes/" + result.getCodeId())).headers(HeaderUtil.createAlert("codeAdmin" + "." + ENTITY_NAME_CODE + ".created", result.getCodeId())).body(result);
        }
    }
    
    /**
     * PUT  /codeAdmin/code : Updates an existing code.
     *
     * @param code the code to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated code,
     * or with status 400 (Bad Request) if the code is not valid,
     * or with status 500 (Internal Server Error) if the code couldn't be updated
     */
    @PutMapping("/codeAdmin/code")
    public ResponseEntity<CodeIntDTO> updateCode(@Valid @RequestBody CodeIntDTO code) {
        log.debug("REST request to update Code : {}", code);
        if(code.getEffectiveDt() != null && code.getExpiryDt() != null && code.getEffectiveDt().after(code.getExpiryDt())) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Effect date must greater than expiry date")).body(null);
        } else if (!codeSeqValid(code)) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("", "Code Seq is ready existed")).body(null);
        } else {
            CodeIntDTO result = this.codeAdminService.editCodes(code);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("codeAdmin" + "." + ENTITY_NAME_CODE + ".updated", result.getCodeId().toString()))
                .body(result);
        }
    }

    /**
     * DELETE  /codeAdmin/code : delete code.
     *
     * @param codeTypePk
     * @param codeId
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/codeAdmin/code")
    public ResponseEntity<Void> deleteCode(@ApiParam String codeTypePk, @ApiParam String codeId) {
        log.debug("REST request to delete Codes : {}");
        List<CodeIntDTO> codes = this.codeAdminService.findByCodeTypeIdAndCodeId(codeTypePk, codeId);
        if (codes != null && !codes.isEmpty()) {
            this.codeAdminService.deleteCodes(codes);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("codeAdmin" + "." + ENTITY_NAME_CODE + ".deleted", codes != null ? codes.get(0).getCodeId() : "")).build();
    }

    /**
     * PUT /codeAdmin/changeCodeSeq : change code seq
     * @param codeInts
     * @return
     */
    @PutMapping("/codeAdmin/changeCodeSeq")
    public ResponseEntity<Void> changeCodeSeq(@Valid @RequestBody List<CodeIntDTO> codeInts){
        log.debug("REST request to update Codes Seq : {}");
        codeAdminService.updateCodeSeq(codeInts);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("codeAdmin" + "." + ENTITY_NAME_CODE + ".changeSeq","")).build();
    }

    private boolean codeSeqValid(CodeIntDTO codeIntDTO) {
        if (codeIntDTO.getCodeSeq() != null) {
            List<CodeIntDTO> list = codeAdminService.getCodesByInternalCodeType(codeIntDTO.getCodeTypePK());
            for (CodeIntDTO code : list) {
                if (!codeIntDTO.getCodeId().equals(code.getCodeId()) && code.getCodeSeq() != null && codeIntDTO.getCodeSeq() != null && code.getCodeSeq().intValue() == codeIntDTO.getCodeSeq().intValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getRootCauseMessageDetail(Exception e) {
        if(e != null) {
            String exceptionStr = ExceptionUtils.getRootCauseMessage(e.getCause());
            exceptionStr = exceptionStr.substring(exceptionStr.indexOf("Exception:") + 11);
            exceptionStr = "Database Mapping Error: " + exceptionStr;
            return exceptionStr;
        } else {
            return "";
        }
    }
       
	@InitBinder 
	public void initBinder(WebDataBinder binder) { 
	    binder.setDisallowedFields(new String[]{}); 
	}
}
