package com.ncs.iconnect.sample.lab.generated.web.rest;
import com.ncs.iframe5.service.dto.GroupBasicInfoDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.ncs.iconnect.sample.lab.generated.service.GroupBasicInfoRetriever;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupBasicInfoResource {

    private final GroupBasicInfoRetriever groupBasicInfoRetriever;

    public GroupBasicInfoResource(@Qualifier("iTrustAwareGroupBasicInfoRetriever") GroupBasicInfoRetriever groupBasicInfoRetriever) {
        this.groupBasicInfoRetriever = groupBasicInfoRetriever;
    }

    @PostMapping("/group-by-condition")
    public ResponseEntity<Collection<GroupBasicInfoDTO>> retrieveUsersBasicInfo(@RequestBody String condition, HttpServletResponse response) {
        List<GroupBasicInfoDTO> groupDtoMap = groupBasicInfoRetriever.findGroupsByCondition(condition);
        return new ResponseEntity<>(groupDtoMap, newHttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/group-basic-infos")
    public ResponseEntity<Collection<GroupBasicInfoDTO>> retriveGroupToApprove(@RequestBody String username, HttpServletResponse response){
        List<GroupBasicInfoDTO> groupDtoMap = groupBasicInfoRetriever.getGroupIdsToApprove(username);
        return new ResponseEntity<>(groupDtoMap, newHttpHeaders(), HttpStatus.OK);
    }

    private HttpHeaders newHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        return headers;
    }
}
