package com.ncs.iconnect.sample.lab.generated.approval.customerrequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.util.Objects;

import com.ncs.iconnect.sample.lab.generated.domain.enumeration.AccountStatus;

import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalAwareForm;
import com.ncs.iconnect.sample.lab.generated.approval.core.domain.ApprovalRequestEntity;

/**
 * A CustomerRequest.
 */
@Entity
@Table(name = "iflow_req_customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomerRequest  implements ApprovalAwareForm {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus accountStatus;

    @NotNull
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "tel_main", nullable = false)
    private String telMain;

    @Column(name = "description")
    private String description;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="request_id")
    private ApprovalRequestEntity approvalRequest = new ApprovalRequestEntity();
    
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public CustomerRequest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public CustomerRequest accountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
        return this;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getEmail() {
        return email;
    }

    public CustomerRequest email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelMain() {
        return telMain;
    }

    public CustomerRequest telMain(String telMain) {
        this.telMain = telMain;
        return this;
    }

    public void setTelMain(String telMain) {
        this.telMain = telMain;
    }

    public String getDescription() {
        return description;
    }

    public CustomerRequest description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public ApprovalRequestEntity getApprovalRequest() {
		return approvalRequest;
	}

	public void setApprovalRequest(ApprovalRequestEntity approvalRequest) {
		this.approvalRequest = approvalRequest;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        CustomerRequest customerRequest = (CustomerRequest) o;
        if (customerRequest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerRequest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", accountStatus='" + getAccountStatus() + "'" +
            ", email='" + getEmail() + "'" +
            ", telMain='" + getTelMain() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
