package com.ncs.iconnect.sample.lab.generated.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class JWTSignKeyResourceTest {
	
	@Autowired
	private WebApplicationContext wac;
	@Autowired
	private Filter springSecurityFilterChain;
    @Autowired
    private TestTokenProvider testTokenProvider;
	
	private MockMvc revokeTokenMockMvc;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		revokeTokenMockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
	}
	
    private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }
    
	@Test
	public void jwtsignkey() throws Exception {
		 ResultActions rs = revokeTokenMockMvc.perform(get("/api/itrust/jwtsignkey")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				//.header("Authorization","Bearer " + obtainAccessToken("PASSWORD/appadmin", "DEF-user-appadmin", "password1"))
				);
		 
		 rs.andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.key").isNotEmpty());
	}
}