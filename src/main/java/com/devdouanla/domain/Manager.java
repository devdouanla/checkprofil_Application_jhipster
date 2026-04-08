package com.devdouanla.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Manager.
 */
@Entity
@Table(name = "manager")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Manager implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manager")
    @JsonIgnoreProperties(value = { "employeses", "competencesRequiseses", "manager" }, allowSetters = true)
    private Set<Poste> posteses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Manager id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Manager user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Poste> getPosteses() {
        return this.posteses;
    }

    public void setPosteses(Set<Poste> postes) {
        if (this.posteses != null) {
            this.posteses.forEach(i -> i.setManager(null));
        }
        if (postes != null) {
            postes.forEach(i -> i.setManager(this));
        }
        this.posteses = postes;
    }

    public Manager posteses(Set<Poste> postes) {
        this.setPosteses(postes);
        return this;
    }

    public Manager addPostes(Poste poste) {
        this.posteses.add(poste);
        poste.setManager(this);
        return this;
    }

    public Manager removePostes(Poste poste) {
        this.posteses.remove(poste);
        poste.setManager(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Manager)) {
            return false;
        }
        return getId() != null && getId().equals(((Manager) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Manager{" +
            "id=" + getId() +
            "}";
    }
}
