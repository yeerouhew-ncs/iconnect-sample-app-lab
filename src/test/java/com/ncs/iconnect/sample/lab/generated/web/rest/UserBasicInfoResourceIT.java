package com.ncs.iconnect.sample.lab.generated.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;
import com.ncs.iframe5.service.dto.UserBasicInfoDTO;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class UserBasicInfoResourceIT {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;
    private MediaType mediaType = MediaType.APPLICATION_JSON;

    @Autowired
    private TestTokenProvider testTokenProvider;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilter(springSecurityFilterChain).build();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void shouldRetrieveSubjectId() throws Exception {
        ResultActions result = mockMvc
            .perform(post("/api/user-basic-infos").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(Arrays.asList("DEF-user-appadmin"))).header("Authorization",
                    "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        String content = result.andReturn().getResponse().getContentAsString();


        List<UserBasicInfoDTO> userDtos = (List<UserBasicInfoDTO>) objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, UserBasicInfoDTO.class));


        result.andDo(print()).andExpect(status().is2xxSuccessful());
        assertEquals(1, userDtos.size());

        UserBasicInfoDTO userDto = userDtos.get(0);
        assertEquals("DEF-user-appadmin", userDto.getSubjectId());
        assertEquals("Application", userDto.getFirstName());
        assertEquals("Admin", userDto.getLastName());
        assertEquals("A", userDto.getStatus());
        assertEquals("appadmin@ncs.com.sg", userDto.getEmail());
    }

    @Test
    public void shouldRetrieveSubjectIds() throws Exception {
        ResultActions result = mockMvc
            .perform(post("/api/user-basic-infos").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(Arrays.asList("DEF-user-appadmin", "DEF-user-useradmin"))).header("Authorization",
                    "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        List<UserBasicInfoDTO> userDtos = (List<UserBasicInfoDTO>) objectMapper
            .readValue(result.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, UserBasicInfoDTO.class));

        result.andDo(print()).andExpect(status().is2xxSuccessful());
        assertEquals(2, userDtos.size());

        UserBasicInfoDTO userDto1 = userDtos.stream().filter(s -> s.getSubjectId().equalsIgnoreCase("DEF-user-appadmin")).findFirst().get();
        UserBasicInfoDTO userDto2 = userDtos.stream().filter(s -> s.getSubjectId().equalsIgnoreCase("DEF-user-useradmin")).findFirst().get();

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

    private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }
}
