package com.ncs.iconnect.sample.lab.generated.web.rest.admin.param;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ncs.iforge5.param.exception.ParamServiceException;
import com.ncs.iforge5.param.service.ParamService;
import com.ncs.iforge5.param.to.ParamDTO;
import com.ncs.iforge5.param.to.ParamTO;
import com.ncs.iforge5.param.to.ParamTOId;
import com.ncs.itrust5.sss.dto.AppDTO;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;

/**
 * Created by xinwu on 17/11/2016.
 */
@RestController
public class ParamAdminResource {
	private Logger log = LoggerFactory.getLogger(ParamAdminResource.class);

	private static final String ENTITY_NAME = "param";

	@Autowired
	private ParamService paramService;

	@Autowired
	private MessageSource msgSource;

	@GetMapping(value = "/api/applicationList")
	@ResponseBody
	public List<AppDTO> getAppList() {
		return paramService.getAppList();
	}

	@RequestMapping(value = "/api/paramadmin", params = { "appId", "paramKey",
			"paramDesc", "effectiveDateAsStr", "expireDateAsStr" }, method = RequestMethod.GET)
	public ResponseEntity<List<ParamDTO>> findParameters(Pageable pageable,
			String appId, String paramKey, String paramDesc,
			String effectiveDateAsStr, String expireDateAsStr) {
		log.debug("invoke the rest controller for findParameters");
        if(StringUtils.isNotBlank(paramKey)) {
			paramKey = paramKey.trim();
		}
		if(StringUtils.isNotEmpty(paramDesc)) {
			paramDesc = paramDesc.trim();
		}
		Page<ParamDTO> page = paramService.findParameters(appId, paramKey,
				paramDesc, effectiveDateAsStr, expireDateAsStr, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
				page, "/api/paramadmin");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/paramadmin", method = RequestMethod.POST)
	public ResponseEntity<ParamDTO> createParameter(
			@Validated @RequestBody ParamDTO paramDTO) {
		log.debug("invoke the createParameter api");
		boolean duplicateKey = paramService.validateParameter(paramDTO);
		if (duplicateKey) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil
									.createFailureAlert(ENTITY_NAME,
											"duplicateKey",
											"Key cannot be duplicated"))
					.body(null);
		}
		ParamTO existParamTO = paramService.findByParamKey(paramDTO
				.getParamKey());
		if (null != existParamTO) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil
									.createFailureAlert(ENTITY_NAME,
											"alreadyExist",
											"The Parameter Key already exist"))
					.body(null);
		}
		if (null != paramDTO.getExpireDate()
				&& null != paramDTO.getEffectiveDate()
				&& paramDTO.getEffectiveDate().after(paramDTO.getExpireDate())) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil
									.createFailureAlert(ENTITY_NAME,
											"invalidDateRange",
											"Effective Date cannot be later than Expire Date"))
					.body(null);
		}
		try {
			ParamDTO result = this.paramService.create(paramDTO);
			return ResponseEntity
					.created(new URI("/api/paramadmin/" + result.getParamKey()))
					.headers(HeaderUtil.createAlert("paramAdmin" + "." + ENTITY_NAME + ".created", result.getParamKey()))
					.body(result);
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		}
		return null;

	}

	/**
	 * @param paramTO
	 *            the parameter object to udpate
	 * @throws ParamServiceException
	 */
	@RequestMapping(value = "/api/paramadmin", method = RequestMethod.PUT)
	public ResponseEntity<ParamDTO> updateParameter(
			@Validated @RequestBody ParamDTO paramDTO)
			throws ParamServiceException {
		log.debug("invoke the updateParameter api");
		boolean duplicateKey = paramService.validateParameter(paramDTO);
		if (duplicateKey) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil
									.createFailureAlert(ENTITY_NAME,
											"duplicateKey",
											"Key cannot be duplicated"))
					.body(null);
		}
		if (null != paramDTO.getExpireDate()
				&& null != paramDTO.getEffectiveDate()
				&& paramDTO.getEffectiveDate().after(paramDTO.getExpireDate())) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil
									.createFailureAlert(ENTITY_NAME,
											"invalidDateRange",
											"Effective Date cannot be later than Expire Date"))
					.body(null);
		}
		ParamTOId id = new ParamTOId();
		id.setAppId(paramDTO.getAppId());
		id.setParamKey(paramDTO.getParamKey());
		paramDTO.setId(id);
		ParamDTO updatedDto = paramService.update(paramDTO);
		return ResponseEntity
				.ok()
				.headers(HeaderUtil.createAlert("paramAdmin" + "." + ENTITY_NAME + ".updated", updatedDto.getParamKey()))
				.body(updatedDto);
	}

	@RequestMapping(value = "/api/paramadmin/{appId}/{paramKey}", method = RequestMethod.GET)
	public ParamDTO findParameter(@PathVariable String appId,
			@PathVariable String paramKey) throws ParamServiceException {
		log.debug("invoke the findParameter api");
		ParamTOId id = new ParamTOId();
		id.setAppId(appId);
		id.setParamKey(paramKey);
		ParamDTO result = paramService.getParamById(id);
		if (null != result) {
			result.setHasId(true);
		}
		return result;
	}

	@RequestMapping(value = "/api/paramadmin", method = RequestMethod.DELETE)
	public void deleteParameters(@RequestBody List<ParamTO> paramTOs) {
		log.debug("invoke the deleteParameters api");
		paramService.delete(paramTOs);
	}

	@RequestMapping(value = "/api/paramadmin/{appId}/{paramKey}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteParameter(
			@NotNull @PathVariable String appId,
			@NotNull @PathVariable String paramKey)
			throws ParamServiceException {
		log.debug("invoke the deleteParameter api");
		ParamTOId id = new ParamTOId();
		id.setAppId(appId);
		id.setParamKey(paramKey);
		ParamDTO paramDTO = paramService.getParamById(id);
		paramService.delete(paramDTO);
		return ResponseEntity
				.ok()
				.headers(HeaderUtil.createAlert("paramAdmin" + "." + ENTITY_NAME + ".deleted", id.getAppId() + '-' + id.getParamKey()))
				.build();
	}

	@RequestMapping(value = "/api/paramadmin/paramkeys", params = { "appId" }, method = RequestMethod.GET)
	public List<String> getAllParamKey(String appId) {
		log.debug("invoke the getAllParamKey api by " + appId);
		if (StringUtils.isNotEmpty(appId)) {
			return paramService.getAllParamKeyByAppId(appId);
		} else {
			return paramService.getAllParamKey();
		}
	}

	public void setParamService(ParamService paramService) {
		this.paramService = paramService;
	}

	public void setMsgSource(MessageSource msgSource) {
		this.msgSource = msgSource;
	}

	@InitBinder 
	public void initBinder(WebDataBinder binder) { 
	    binder.setDisallowedFields(new String[]{}); 
	}
}
