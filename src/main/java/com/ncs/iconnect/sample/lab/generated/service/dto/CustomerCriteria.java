package com.ncs.iconnect.sample.lab.generated.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.ncs.iconnect.sample.lab.generated.domain.enumeration.AccountStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ncs.iconnect.sample.lab.generated.domain.Customer} entity. This class is used
 * in {@link com.ncs.iconnect.sample.lab.generated.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerCriteria implements Serializable, Criteria {
    /**
     * Class for filtering AccountStatus
     */
    public static class AccountStatusFilter extends Filter<AccountStatus> {

        public AccountStatusFilter() {
        }

        public AccountStatusFilter(AccountStatusFilter filter) {
            super(filter);
        }

        @Override
        public AccountStatusFilter copy() {
            return new AccountStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private AccountStatusFilter accountStatus;

    private StringFilter email;

    private StringFilter telMain;

    private StringFilter description;

    private LongFilter addressId;

    public CustomerCriteria() {
    }

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.accountStatus = other.accountStatus == null ? null : other.accountStatus.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.telMain = other.telMain == null ? null : other.telMain.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public AccountStatusFilter getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatusFilter accountStatus) {
        this.accountStatus = accountStatus;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelMain() {
        return telMain;
    }

    public void setTelMain(StringFilter telMain) {
        this.telMain = telMain;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerCriteria that = (CustomerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(accountStatus, that.accountStatus) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telMain, that.telMain) &&
            Objects.equals(description, that.description) &&
            Objects.equals(addressId, that.addressId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        accountStatus,
        email,
        telMain,
        description,
        addressId
        );
    }

    @Override
    public String toString() {
        return "CustomerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (accountStatus != null ? "accountStatus=" + accountStatus + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (telMain != null ? "telMain=" + telMain + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (addressId != null ? "addressId=" + addressId + ", " : "") +
            "}";
    }

}
