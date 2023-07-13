package com.ncs.iconnect.sample.lab.generated.web.rest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ncs.iconnect.sample.lab.generated.service.UserBasicInfoRetriever;
import com.ncs.iframe5.service.dto.UserBasicInfoDTO;
import com.ncs.iconnect.sample.lab.generated.service.impl.ITrustAwareUserBasicInfoRetriever;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserBasicInfoResource {

    private final UserBasicInfoRetriever userInfoRetriever;

    public UserBasicInfoResource(@Qualifier("iTrustAwareUserBasicInfoRetriever") UserBasicInfoRetriever userInfoRetriever) {
        this.userInfoRetriever = userInfoRetriever;
    }

    @PostMapping("/user-basic-infos")
    public ResponseEntity<Collection<UserBasicInfoDTO>> retrieveUsersBasicInfo(@RequestBody List<String> subjectIds, HttpServletResponse response) {
        Map<String, UserBasicInfoDTO> userDtoMap = userInfoRetriever.getUsersBasicInfos(subjectIds);
        return new ResponseEntity<>(userDtoMap.values(), newHttpHeaders(), HttpStatus.OK);
    }

    private HttpHeaders newHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        return headers;
    }
}
