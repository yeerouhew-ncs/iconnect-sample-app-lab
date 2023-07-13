package com.ncs.iconnect.sample.lab.generated.aop.logging;

import io.github.jhipster.config.JHipsterConstants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class LoggingAspectIT {
	
	@InjectMocks
	private LoggingAspect aspect;

    @Mock
	private Environment env;

    private ProceedingJoinPoint proceedingJoinPoint;

	@BeforeEach
    public void initTest() {
        MockitoAnnotations.initMocks(this);
		aspect = new LoggingAspect(env);
        proceedingJoinPoint = mock(ProceedingJoinPoint.class);
	}

	@Test
	public void springBeanPointcut() {
		aspect.springBeanPointcut();
	}

	@Test
	public void applicationPackagePointcut() {
		aspect.applicationPackagePointcut();
    }

    @Test
    public void logAfterThrowing() {
        Throwable e =mock(Throwable.class);
        JoinPoint joinPoint = mock(JoinPoint.class);
        assertThrows(NullPointerException.class,()->{
            aspect.logAfterThrowing(joinPoint,e);
        });
    }

    @Test
    public void logAfterThrowingWithException() {
        Throwable e =mock(Throwable.class);
        JoinPoint joinPoint = mock(JoinPoint.class);
        when(env.acceptsProfiles(Profiles.of(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT))).thenReturn(true);
        assertThrows(NullPointerException.class,()-> aspect.logAfterThrowing(joinPoint,e));
    }

    @Test
    public void logAround() throws Throwable {
        // aspect.logAround(proceedingJoinPoint);
    }

    @Test
    public void logAroundWithException() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenThrow(IllegalArgumentException.class);
        assertThrows(NullPointerException.class,()-> aspect.logAround(proceedingJoinPoint));
    }
}
