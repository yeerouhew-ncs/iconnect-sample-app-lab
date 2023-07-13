package com.ncs.iconnect.sample.lab.generated.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.ncs.iframe5.data.hibernate.audit.func.AuditFuncNameInterceptor;

@Configuration
@EnableAspectJAutoProxy
@Aspect
public class AuditAdviceInteceptor {

	private static final String AUDIT_AOP_POINTCUT_EXPRESSION = "execution(* *..*Resource+.*(..)) || execution(* *..*Controller+.*(..))";
	
	@Bean
    public AuditFuncNameInterceptor auditFuncInterceptor() {
    	return new AuditFuncNameInterceptor();
    }
    
    @Bean
    public Advisor auditFuncInterceptorAdvisor(){
    	AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    	pointcut.setExpression(AUDIT_AOP_POINTCUT_EXPRESSION);
    	return new DefaultPointcutAdvisor(pointcut, auditFuncInterceptor());
    }
}
