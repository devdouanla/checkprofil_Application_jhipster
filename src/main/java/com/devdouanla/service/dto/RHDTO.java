package com.devdouanla.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.RH} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RHDTO implements Serializable {

    private Long id;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RHDTO)) {
            return false;
        }

        RHDTO rHDTO = (RHDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rHDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RHDTO{" +
            "id=" + getId() +
            ", user=" + getUser() +
            "}";
    }
}
