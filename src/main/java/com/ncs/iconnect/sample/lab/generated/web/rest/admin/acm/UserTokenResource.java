package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import com.ncs.iconnect.sample.lab.generated.web.rest.util.HeaderUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.PaginationUtil;

import com.ncs.itrust5.ss5.domain.UserToken;
import com.ncs.itrust5.ss5.service.UserTokenService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing userToken Resource.
 */
@RestController
@RequestMapping("/api")
public class UserTokenResource {

    private final Logger log = LoggerFactory.getLogger(UserTokenResource.class);

    @Autowired
    private UserTokenService userTokenService;
    
	 /**
     *GET  /userToken : get all the userToken.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/userToken")
    public ResponseEntity<List<UserToken>> getAllLoggedUserToken(@ApiParam Pageable pageable,@ApiParam String loginId) {
        log.debug("REST request to get all logged UserToken");
        if(StringUtils.isNotBlank(loginId)){
            loginId = loginId.trim();
        }
        Page<UserToken> page = userTokenService.findAllLoggedUserToken(pageable,loginId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userToken");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * DELETE  /userToken/:id : delete the "id" userToken.
     *
     * @param id the id of the UserToken to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/userToken/{id}")
    public ResponseEntity<Void> deleteUserToken(@PathVariable String id) {
        log.debug("REST request to delete UserToken : {}", id);
        userTokenService.deleteUserTokenById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("acmAdmin.userToken.deleted", id)).body(null);
    }

    /**
     * GET  /userToken/:id : get the "id" userToken.
     *
     * @param id the id of the userToken to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userToken, or with status 404 (Not Found)
     * @throws GeneralSecurityException 
     */
    @GetMapping("/userToken/{id}")
    public ResponseEntity<UserToken> getUserToken(@PathVariable String id)  {
        log.debug("REST request to get UserToken : {}", id);
        UserToken userToken = userTokenService.findUserTokenById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userToken));
    }

}
