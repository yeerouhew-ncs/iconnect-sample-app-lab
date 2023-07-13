package com.ncs.iconnect.sample.lab.generated.security;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class PermissionValidationExceptionIT {

    @Test
    public void permissionValidationException(){
        new PermissionValidationException();
        new PermissionValidationException("test permissionValidationException");
        new PermissionValidationException("test permissionValidationException",new Throwable());
        new PermissionValidationException(new Throwable());
    }
    
    @Test
    public void UserNotActivatedException(){
        new UserNotActivatedException("test UserNotActivatedException");
        new UserNotActivatedException("test UserNotActivatedException",new Throwable());
    }

    @Test
    public void SpringSecurityAuditorAware(){
        new SpringSecurityAuditorAware().getCurrentAuditor();
    }

}
