package com.ncs.iconnect.sample.lab.generated.config.sysschedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ncs.itrust5.ss5.service.UserService;
import com.ncs.itrust5.ss5.service.UserTokenService;

@Component
public class SystemScheduledTaskBean {

	private final Logger log = LoggerFactory.getLogger(SystemScheduledTaskBean.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserTokenService userTokenService;
	
	@Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiriedUserTokens() {
		log.info("[Remove Expired User Tokens]");
        userTokenService.housekeepingRemoveExpiredUserTokens();
    }
	
	@Scheduled(cron = "0 0 1 * * *")
	public void updateExpiriedUserAccountsStatus() {
		log.info("[Update Expired Subject Status]");
		userService.housekeepMonitorSubjectStatus();
	}
}
