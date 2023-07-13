package com.ncs.iconnect.sample.lab.generated.security.jwt;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.itrust5.ss5.jwt.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class JWTConfigurerIT {

    @Autowired
    private TokenProvider tokenProvider;

    @Test
    public void JWTConfigurer(){
        assertThrows(NullPointerException.class,()->{
            new JWTConfigurer(tokenProvider).configure(null);
        });
    }
}
