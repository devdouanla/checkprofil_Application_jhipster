package com.devdouanla.domain;

import com.devdouanla.domain.enumeration.Difficulte;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Une Epreuve définit les CRITÈRES de tirage aléatoire :
 * - competence  → le domaine du pool de questions
 * - difficulte  → le niveau de difficulté à piocher
 * - nbQuestions → combien de questions tirer aléatoirement
 * Elle ne possède PLUS de collection de questions fixées.
 */
@Entity
@Table(name = "epreuve")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Epreuve implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @NotNull
    @Column(name = "enonce", nullable = false)
    private String enonce;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulte", nullable = false)
    private Difficulte difficulte;

    @NotNull
    @Column(name = "duree", nullable = false)
    private Integer duree;

    @NotNull
    @Column(name = "nb_questions", nullable = false)
    private Integer nbQuestions;

    @NotNull
    @Column(name = "genere_par_ia", nullable = false)
    private Boolean genereParIA;

    @NotNull
    @Column(name = "publie", nullable = false)
    private Boolean publie;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "epreuve")
    @JsonIgnoreProperties(value = { "resultat", "questionsAsks", "reponseses", "evaluation", "epreuve" }, allowSetters = true)
    private Set<SessionTest> sessionses = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "questionses", "expertses", "epreuveses" }, allowSetters = true)
    private Competence competence;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Epreuve id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Epreuve titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getEnonce() {
        return this.enonce;
    }

    public Epreuve enonce(String enonce) {
        this.setEnonce(enonce);
        return this;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public Difficulte getDifficulte() {
        return this.difficulte;
    }

    public Epreuve difficulte(Difficulte difficulte) {
        this.setDifficulte(difficulte);
        return this;
    }

    public void setDifficulte(Difficulte difficulte) {
        this.difficulte = difficulte;
    }

    public Integer getDuree() {
        return this.duree;
    }

    public Epreuve duree(Integer duree) {
        this.setDuree(duree);
        return this;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public Integer getNbQuestions() {
        return this.nbQuestions;
    }

    public Epreuve nbQuestions(Integer nbQuestions) {
        this.setNbQuestions(nbQuestions);
        return this;
    }

    public void setNbQuestions(Integer nbQuestions) {
        this.nbQuestions = nbQuestions;
    }

    public Boolean getGenereParIA() {
        return this.genereParIA;
    }

    public Epreuve genereParIA(Boolean genereParIA) {
        this.setGenereParIA(genereParIA);
        return this;
    }

    public void setGenereParIA(Boolean genereParIA) {
        this.genereParIA = genereParIA;
    }

    public Boolean getPublie() {
        return this.publie;
    }

    public Epreuve publie(Boolean publie) {
        this.setPublie(publie);
        return this;
    }

    public void setPublie(Boolean publie) {
        this.publie = publie;
    }

    public Set<SessionTest> getSessionses() {
        return this.sessionses;
    }

    public void setSessionses(Set<SessionTest> sessionTests) {
        if (this.sessionses != null) {
            this.sessionses.forEach(i -> i.setEpreuve(null));
        }
        if (sessionTests != null) {
            sessionTests.forEach(i -> i.setEpreuve(this));
        }
        this.sessionses = sessionTests;
    }

    public Epreuve sessionses(Set<SessionTest> sessionTests) {
        this.setSessionses(sessionTests);
        return this;
    }

    public Epreuve addSessions(SessionTest sessionTest) {
        this.sessionses.add(sessionTest);
        sessionTest.setEpreuve(this);
        return this;
    }

    public Epreuve removeSessions(SessionTest sessionTest) {
        this.sessionses.remove(sessionTest);
        sessionTest.setEpreuve(null);
        return this;
    }

    public Competence getCompetence() {
        return this.competence;
    }

    public void setCompetence(Competence competence) {
        this.competence = competence;
    }

    public Epreuve competence(Competence competence) {
        this.setCompetence(competence);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Epreuve)) {
            return false;
        }
        return getId() != null && getId().equals(((Epreuve) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Epreuve{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", enonce='" + getEnonce() + "'" +
            ", difficulte='" + getDifficulte() + "'" +
            ", duree=" + getDuree() +
            ", nbQuestions=" + getNbQuestions() +
            ", genereParIA='" + getGenereParIA() + "'" +
            ", publie='" + getPublie() + "'" +
            "}";
    }
}
