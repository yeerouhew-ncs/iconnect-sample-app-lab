package com.ncs.iconnect.sample.lab.generated.approval.customerrequest;

import javax.validation.constraints.*;
import java.util.Objects;
import com.ncs.iconnect.sample.lab.generated.domain.enumeration.AccountStatus;

import com.ncs.iconnect.sample.lab.generated.approval.core.service.dto.ApprovalAwareFormDTO;
/**
 * A DTO for the CustomerRequest entity.
 */
public class CustomerRequestDTO extends ApprovalAwareFormDTO {

    private static final long serialVersionUID = 1L;
    
    private String typeKey = "CustomerRequest";

	public String getTypeKey() {
		return this.typeKey;
    }
    

    private Long id;

    @NotNull
    @Size(min = 3)
    private String name;

    private AccountStatus accountStatus;

    @NotNull
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")
    private String email;

    @NotNull
    private String telMain;

    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelMain() {
        return telMain;
    }

    public void setTelMain(String telMain) {
        this.telMain = telMain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CustomerRequestDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", accountStatus='" + getAccountStatus() + "'" +
            ", email='" + getEmail() + "'" +
            ", telMain='" + getTelMain() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
