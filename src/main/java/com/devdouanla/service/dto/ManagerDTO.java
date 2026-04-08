package com.devdouanla.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devdouanla.domain.Manager} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManagerDTO implements Serializable {

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
        if (!(o instanceof ManagerDTO)) {
            return false;
        }

        ManagerDTO managerDTO = (ManagerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, managerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagerDTO{" +
            "id=" + getId() +
            ", user=" + getUser() +
            "}";
    }
}
