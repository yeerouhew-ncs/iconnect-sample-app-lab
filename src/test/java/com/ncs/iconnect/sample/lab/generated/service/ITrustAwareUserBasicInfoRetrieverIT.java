package com.ncs.iconnect.sample.lab.generated.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.service.impl.ITrustAwareUserBasicInfoRetriever;
import com.ncs.iframe5.service.dto.UserBasicInfoDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ITrustAwareUserBasicInfoRetrieverIT {
    @Autowired
    ITrustAwareUserBasicInfoRetriever userInfoRetriever;

    @Test
    public void shouldRetrieveSubjectId(){
        UserBasicInfoDTO userDto = userInfoRetriever.getUserBasicInfo("DEF-user-appadmin");
        assertEquals("DEF-user-appadmin", userDto.getSubjectId());
        assertEquals("Application", userDto.getFirstName());
        assertEquals("Admin", userDto.getLastName());
        assertEquals("A", userDto.getStatus());
        assertEquals("appadmin@ncs.com.sg", userDto.getEmail());

    }

    @Test
    public void shouldRetrieveSubjectIds(){
        Map<String, UserBasicInfoDTO> userDtoMap  = userInfoRetriever.getUsersBasicInfos(Arrays.asList("DEF-user-appadmin", "DEF-user-useradmin"));
        UserBasicInfoDTO userDto1 = userDtoMap.get("DEF-user-appadmin");
        UserBasicInfoDTO userDto2 = userDtoMap.get("DEF-user-useradmin");

        assertEquals("DEF-user-appadmin", userDto1.getSubjectId());
        assertEquals("Application", userDto1.getFirstName());
        assertEquals("Admin", userDto1.getLastName());
        assertEquals("A", userDto1.getStatus());
        assertEquals("appadmin@ncs.com.sg", userDto1.getEmail());

        assertEquals("DEF-user-useradmin", userDto2.getSubjectId());
        assertEquals("User", userDto2.getFirstName());
        assertEquals("Admin", userDto2.getLastName());
        assertEquals("A", userDto2.getStatus());
        assertEquals("useradmin@ncs.com.sg", userDto2.getEmail());
    }
}
