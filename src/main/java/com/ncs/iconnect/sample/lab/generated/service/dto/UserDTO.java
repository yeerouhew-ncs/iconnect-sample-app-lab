package com.ncs.iconnect.sample.lab.generated.service.dto;

import java.time.Instant;
import java.util.Set;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.ncs.iconnect.sample.lab.generated.config.Constants;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    @Size(max = 20)
    private String activationKey;

    @Size(max = 20)
    private String resetKey;
    
    @Size(min = 2, max = 5)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }


    public UserDTO(Long id, String login, String firstName, String lastName,
        String email, boolean activated, String imageUrl, String langKey,
        String createdBy, Instant createdDate, String lastModifiedBy, Instant lastModifiedDate,
        Set<String> authorities) {

        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.imageUrl = imageUrl;
        this.langKey = langKey;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.authorities = authorities;
    }

    public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public boolean isActivated() {
		return activated;
	}


	public void setActivated(boolean activated) {
		this.activated = activated;
	}


	public String getLangKey() {
		return langKey;
	}


	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Instant getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}


	public String getLastModifiedBy() {
		return lastModifiedBy;
	}


	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}


	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}


	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


	public Set<String> getAuthorities() {
		return authorities;
	}


	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}


	public String getActivationKey() {
		return activationKey;
	}


	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}


	public String getResetKey() {
		return resetKey;
	}


	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}


	@Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", authorities=" + authorities +
            "}";
    }
}
