package com.ncs.iconnect.sample.lab.generated.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.ncs.iframe5.data.hibernate.audit.listener.IframeRevListener;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackages={"com.ncs.iframe5","com.ncs.itrust5","com.ncs.iforge5","com.ncs.iconnect.sample.lab"},transactionManagerRef="transactionManager",entityManagerFactoryRef="entityManagerFactory")
@EnableTransactionManagement
public class DataBaseConfiguration {
    
    @Autowired
	private JpaProperties jpaProperties;

	@Autowired
    private HibernateProperties hibernateProperties;

	@Autowired
    private DataSource IconnectSampleAppLabDS;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder){
        return builder.dataSource(IconnectSampleAppLabDS)
    			           .packages("com.ncs.iframe5","com.ncs.itrust5","com.ncs.iforge5","com.ncs.iconnect.sample.lab")
    			              .properties(getJpaProperties())
    			                  .build();
    }
    
    private Map<String, Object> getJpaProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactoryBuilder builder){
    	return new JpaTransactionManager(entityManagerFactory(builder).getObject());
    }
    
    @Bean(initMethod="initialize")
	public IframeRevListener iframeRevListener(){
		return new IframeRevListener();
    }
 }