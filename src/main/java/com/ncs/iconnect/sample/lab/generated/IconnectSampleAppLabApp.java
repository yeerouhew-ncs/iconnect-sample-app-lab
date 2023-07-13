package com.ncs.iconnect.sample.lab.generated;

import java.net.InetAddress;
import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import com.ncs.iconnect.sample.lab.generated.web.rest.util.ValidationUtils;
import com.ncs.iconnect.sample.lab.generated.config.ApplicationProperties;
import io.github.jhipster.config.DefaultProfileUtil;

import io.github.jhipster.config.JHipsterConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;


@EnableAutoConfiguration(exclude = {LiquibaseAutoConfiguration.class,})
@EnableConfigurationProperties({ ApplicationProperties.class,LiquibaseProperties.class })
@ImportResource({"classpath:system-infrastructure-config.xml"})
@ComponentScan("com.ncs.iconnect.sample.lab,com.ncs.iframe5,com.ncs.itrust5,com.ncs.iforge5")
public class IconnectSampleAppLabApp {

    private static final Logger log = LoggerFactory.getLogger(IconnectSampleAppLabApp.class);

    private final Environment env;

    public IconnectSampleAppLabApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes IconnectSampleAppLab.
     * <p>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="http://www.jhipster.tech/profiles/">http://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }


    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(IconnectSampleAppLabApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }

        String hostAddress = null;
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error("Get host address error");
		}

        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}\n\t" +
                "External: \t{}://{}:{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            ValidationUtils.getInstance().getValidInput(env.getProperty("spring.application.name")),
            protocol,
            env.getProperty("server.port"),
            protocol,
            hostAddress,
            env.getProperty("server.port"),
            env.getActiveProfiles());
    }
}
