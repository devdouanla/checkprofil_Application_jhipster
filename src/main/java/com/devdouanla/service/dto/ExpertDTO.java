package com.devdouanla.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.devdouanla.domain.Expert} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpertDTO implements Serializable {

    private Long id;

    @NotNull
    private UserDTO user;

    private Set<CompetenceDTO> competenceses = new HashSet<>();

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

    public Set<CompetenceDTO> getCompetenceses() {
        return competenceses;
    }

    public void setCompetenceses(Set<CompetenceDTO> competenceses) {
        this.competenceses = competenceses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpertDTO)) {
            return false;
        }

        ExpertDTO expertDTO = (ExpertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, expertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpertDTO{" +
            "id=" + getId() +
            ", user=" + getUser() +
            ", competenceses=" + getCompetenceses() +
            "}";
    }
}
