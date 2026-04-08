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
 * A Epreuve.
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
    @Column(name = "genere_par_ia", nullable = false)
    private Boolean genereParIA;

    @Column(name = "nb_int")
    private Integer nbInt;

    @NotNull
    @Column(name = "publie", nullable = false)
    private Boolean publie;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "epreuve")
    @JsonIgnoreProperties(value = { "epreuve" }, allowSetters = true)
    private Set<Question> questionses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "epreuves")
    @JsonIgnoreProperties(value = { "resultat", "reponseses", "evaluation", "epreuves" }, allowSetters = true)
    private Set<SessionTest> sessionTests = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "epreuveses", "expertses" }, allowSetters = true)
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

    public Integer getNbInt() {
        return this.nbInt;
    }

    public Epreuve nbInt(Integer nbInt) {
        this.setNbInt(nbInt);
        return this;
    }

    public void setNbInt(Integer nbInt) {
        this.nbInt = nbInt;
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

    public Set<Question> getQuestionses() {
        return this.questionses;
    }

    public void setQuestionses(Set<Question> questions) {
        if (this.questionses != null) {
            this.questionses.forEach(i -> i.setEpreuve(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setEpreuve(this));
        }
        this.questionses = questions;
    }

    public Epreuve questionses(Set<Question> questions) {
        this.setQuestionses(questions);
        return this;
    }

    public Epreuve addQuestions(Question question) {
        this.questionses.add(question);
        question.setEpreuve(this);
        return this;
    }

    public Epreuve removeQuestions(Question question) {
        this.questionses.remove(question);
        question.setEpreuve(null);
        return this;
    }

    public Set<SessionTest> getSessionTests() {
        return this.sessionTests;
    }

    public void setSessionTests(Set<SessionTest> sessionTests) {
        if (this.sessionTests != null) {
            this.sessionTests.forEach(i -> i.setEpreuves(null));
        }
        if (sessionTests != null) {
            sessionTests.forEach(i -> i.setEpreuves(this));
        }
        this.sessionTests = sessionTests;
    }

    public Epreuve sessionTests(Set<SessionTest> sessionTests) {
        this.setSessionTests(sessionTests);
        return this;
    }

    public Epreuve addSessionTest(SessionTest sessionTest) {
        this.sessionTests.add(sessionTest);
        sessionTest.setEpreuves(this);
        return this;
    }

    public Epreuve removeSessionTest(SessionTest sessionTest) {
        this.sessionTests.remove(sessionTest);
        sessionTest.setEpreuves(null);
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
            ", genereParIA='" + getGenereParIA() + "'" +
            ", nbInt=" + getNbInt() +
            ", publie='" + getPublie() + "'" +
            "}";
    }
}
