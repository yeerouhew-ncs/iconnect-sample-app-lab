package com.ncs.iconnect.sample.lab.generated.web.rest;

import com.ncs.iconnect.sample.lab.generated.security.jwt.JWTFilter;
import com.ncs.itrust5.ss5.jwt.TokenProvider;
import com.ncs.iconnect.sample.lab.generated.web.rest.vm.LoginVM;
import com.ncs.iconnect.sample.lab.generated.web.rest.vm.TokenVM;
import com.ncs.itrust5.ss5.tools.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ncs.itrust5.ss5.service.UserTokenService;
import com.ncs.itrust5.ss5.tools.AESUtil;
import com.ncs.itrust5.ss5.exception.FirstLoginException;
import com.ncs.itrust5.ss5.exception.PasswordChangeExpiredException;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private static final String ERROR = "error";

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final UserTokenService userTokenService;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager,
                             UserTokenService userTokenService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userTokenService = userTokenService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) throws UnsupportedEncodingException, GeneralSecurityException {
        ESAPI.initialize("org.owasp.esapi.reference.DefaultSecurityConfiguration");
        Authentication authentication = null;
        try {
            authentication = createAuthentication(loginVM, response);
        }  catch (FirstLoginException | PasswordChangeExpiredException e) {
            log.debug("FirstLoginException exception trace: {}", e);
            String authLoggedName = null;
            try {
                authLoggedName = decryptor(loginVM.getKey(), loginVM.getUsername(), loginVM.getIv());
                authLoggedName = ESAPI.validator().getValidInput("Error in Username", authLoggedName,	"SafeString", 255, true, true);
            } catch (IntrusionException | ValidationException e1) {
                log.error("Error in ESAPI Valiadtons or Error in Decrypting Username");
            }
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("errorCode", "901");
            httpHeaders.add(ERROR, e.getLocalizedMessage());
            httpHeaders.add("username",authLoggedName);
            return new ResponseEntity<>(httpHeaders, HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            log.error("Decrypt loginVM exception trace: {}", e);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(ERROR, "Decrypt loginVM failed");
            return new ResponseEntity<>(httpHeaders, HttpStatus.UNAUTHORIZED);
        }
        //Failed to authenticate
        if (authentication == null) {
            log.debug("Failed to login, authentication is null");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(ERROR, "Authentication is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.UNAUTHORIZED);
        } else {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //User already logged in in another browser
            if (userTokenService.existsUserSession(getLoginMethodAndId(loginVM))) {
                return buildUserSessionAlreadyExistResponse(authentication, loginVM);
            } else {
                return buildNewUserSessionResponse(authentication, loginVM);
            }
        }
    }

    private String decryptor(String encryptedKey, String encryptedValue, String iv) throws UnsupportedEncodingException, GeneralSecurityException {
        String decryptedValue = null;
        if (encryptedValue != null && encryptedValue.length() > 0) {
            decryptedValue = AESUtil.decrypt(encryptedValue, encryptedKey, iv);
        }

        return decryptedValue;
    }

    private Authentication createAuthentication(LoginVM loginVM, HttpServletResponse response) throws UnsupportedEncodingException, GeneralSecurityException {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(decryptor(loginVM.getKey(), loginVM.getUsername(), loginVM.getIv()), decryptor(loginVM.getKey(), new String(loginVM.getTempAuthData()), loginVM.getIv()));
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        //Zeroize password to satisfy Security Scan
        loginVM.setPassword(new char[] {});
        return authentication;
    }

    private ResponseEntity<JWTToken> buildUserSessionAlreadyExistResponse(Authentication authentication, LoginVM loginVM) {
        log.info("SecurityUtils.getCurrentUserLogin().get(): " + SecurityUtils.getCurrentUserLoginMethodAndUserId());
        userTokenService.deleteUserSessionToken(SecurityUtils.getCurrentUserLoginMethodAndUserId());
        return buildNewUserSessionResponse(authentication, loginVM);
    }

    private ResponseEntity<JWTToken> buildNewUserSessionResponse(Authentication authentication, LoginVM loginVM) {
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        log.debug("SecurityUtils.getCurrentUserLogin().get(): " + SecurityUtils.getCurrentUserLoginMethodAndUserId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    private String getLoginMethodAndId(@RequestBody @Valid LoginVM loginVM) throws UnsupportedEncodingException, GeneralSecurityException {
        return SecurityUtils.getCurrentUserAuthMethod() + "/" + decryptor(loginVM.getKey(), loginVM.getUsername(), loginVM.getIv());
    }

    /**
     * Post  /createAccessToken : create the access token.
     *
     * @param tokenVM the token information
     * @return the ResponseEntity with status 200 (OK) and the new created access token in body, or status 500 (Internal Server Error) if the token couldn't be created
     */
    @PostMapping("/accessToken")
    public ResponseEntity<Object> createAccessToken(@Validated @RequestBody TokenVM tokenVM) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(authentication.isAuthenticated()){
            String jwt = tokenProvider.createAccessToken(authentication, tokenVM.getExpireDateAsStr());
            return ResponseEntity.ok(new JWTToken(jwt));
        }
        log.error("User did not login yet");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields(new String[]{});
    }
}