package com.ncs.iconnect.sample.lab.generated.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;


@Configuration
public class TxAdviceInterceptor {
	
	private static final String AOP_POINTCUT_EXPRESSION = "execution(* *..service.*Service+.*(..))";
    
	@Autowired
	private JpaTransactionManager transactionManager;
    
    @Bean
    public TransactionInterceptor txAdvice(){
    	NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
    	
    	RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
    	readOnlyTx.setReadOnly(true);
    	
    	RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
    	requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    	
    	
    	Map<String, TransactionAttribute> txMap = new HashMap<>();
    	txMap.put("get*", readOnlyTx);
        txMap.put("find*", readOnlyTx);
        txMap.put("prepare**", readOnlyTx);
        txMap.put("*", requiredTx);
        source.setNameMap(txMap);
        
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
        
        return txAdvice;
      
    }
    
    @Bean
    public Advisor txAdviceAdvisor(){
    	AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    	pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
    	return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}