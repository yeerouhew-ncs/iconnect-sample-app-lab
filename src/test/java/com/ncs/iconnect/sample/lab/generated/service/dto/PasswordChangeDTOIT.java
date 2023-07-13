package com.ncs.iconnect.sample.lab.generated.service.dto;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.service.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class PasswordChangeDTOIT {

    @Test
    public void PasswordChangeDTO(){
        PasswordChangeDTO pass = new PasswordChangeDTO();
        pass.setCurrentPassword("password1");
        pass.setNewPassword("password2");

        assertNotNull(pass.getCurrentPassword());
        assertNotNull(pass.getNewPassword());
        new PasswordChangeDTO("pwd1","pwd2");
    }

    @Test
    public void userDTO(){
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setLogin("admin");
        user.setFirstName("admin");
        user.setLastName("app");
        user.setEmail("abc@ncs.com.sg");
        user.setImageUrl("root/home/admin.png");
        user.setActivated(true);
        user.setActivationKey("active");
        user.setResetKey("reset");
        user.setLangKey("en");
        user.setCreatedBy("system");
        user.setCreatedDate(Instant.now());
        user.setLastModifiedBy("user");
        user.setLastModifiedDate(Instant.now());
        Set<String> authorities = new HashSet<>();
        authorities.add("authorities");
        user.setAuthorities(authorities);

        assertNotNull(user.getId());
        assertNotNull(user.getLogin());
        assertNotNull(user.getFirstName());
        assertNotNull(user.getLastName());
        assertNotNull(user.getEmail());
        assertNotNull(user.getImageUrl());
        assertNotNull(user.getActivationKey());
        assertNotNull(user.isActivated());
        assertNotNull(user.getResetKey());
        assertNotNull(user.getLangKey());
        assertNotNull(user.getCreatedBy());
        assertNotNull(user.getCreatedDate());
        assertNotNull(user.getLastModifiedBy());
        assertNotNull(user.getLastModifiedDate());
        assertNotNull(user.getAuthorities());

        user.toString();

        new UserDTO(2L,"admin","admin","app","abc@ncs.com.sg",
            true,"root/home/admin.png","en","system",Instant.now(),
            "user",Instant.now(),null);
    }

    @Test
    public void RandomUtil(){
        RandomUtil.generateActivationKey();
        RandomUtil.generatePassword();
        RandomUtil.generateResetKey();
    }
}
