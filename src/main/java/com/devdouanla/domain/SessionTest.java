package com.devdouanla.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A SessionTest.
 */
@Entity
@Table(name = "session_test")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionTest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "score_obtenu")
    private Float scoreObtenu;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @JsonIgnoreProperties(value = { "session" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Resultat resultat;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "session")
    @JsonIgnoreProperties(value = { "question", "session" }, allowSetters = true)
    private Set<ReponseCandidat> reponseses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sessionses", "employe" }, allowSetters = true)
    private Evaluation evaluation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "questionses", "sessionTests", "competence" }, allowSetters = true)
    private Epreuve epreuves;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SessionTest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getScoreObtenu() {
        return this.scoreObtenu;
    }

    public SessionTest scoreObtenu(Float scoreObtenu) {
        this.setScoreObtenu(scoreObtenu);
        return this;
    }

    public void setScoreObtenu(Float scoreObtenu) {
        this.scoreObtenu = scoreObtenu;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public SessionTest dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Resultat getResultat() {
        return this.resultat;
    }

    public void setResultat(Resultat resultat) {
        this.resultat = resultat;
    }

    public SessionTest resultat(Resultat resultat) {
        this.setResultat(resultat);
        return this;
    }

    public Set<ReponseCandidat> getReponseses() {
        return this.reponseses;
    }

    public void setReponseses(Set<ReponseCandidat> reponseCandidats) {
        if (this.reponseses != null) {
            this.reponseses.forEach(i -> i.setSession(null));
        }
        if (reponseCandidats != null) {
            reponseCandidats.forEach(i -> i.setSession(this));
        }
        this.reponseses = reponseCandidats;
    }

    public SessionTest reponseses(Set<ReponseCandidat> reponseCandidats) {
        this.setReponseses(reponseCandidats);
        return this;
    }

    public SessionTest addReponses(ReponseCandidat reponseCandidat) {
        this.reponseses.add(reponseCandidat);
        reponseCandidat.setSession(this);
        return this;
    }

    public SessionTest removeReponses(ReponseCandidat reponseCandidat) {
        this.reponseses.remove(reponseCandidat);
        reponseCandidat.setSession(null);
        return this;
    }

    public Evaluation getEvaluation() {
        return this.evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public SessionTest evaluation(Evaluation evaluation) {
        this.setEvaluation(evaluation);
        return this;
    }

    public Epreuve getEpreuves() {
        return this.epreuves;
    }

    public void setEpreuves(Epreuve epreuve) {
        this.epreuves = epreuve;
    }

    public SessionTest epreuves(Epreuve epreuve) {
        this.setEpreuves(epreuve);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionTest)) {
            return false;
        }
        return getId() != null && getId().equals(((SessionTest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionTest{" +
            "id=" + getId() +
            ", scoreObtenu=" + getScoreObtenu() +
            ", dateDebut='" + getDateDebut() + "'" +
            "}";
    }
}
