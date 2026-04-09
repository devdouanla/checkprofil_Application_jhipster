import competence from 'app/entities/competence/competence.reducer';
import competenceRequise from 'app/entities/competence-requise/competence-requise.reducer';
import employe from 'app/entities/employe/employe.reducer';
import epreuve from 'app/entities/epreuve/epreuve.reducer';
import evaluation from 'app/entities/evaluation/evaluation.reducer';
import expert from 'app/entities/expert/expert.reducer';
import manager from 'app/entities/manager/manager.reducer';
import poste from 'app/entities/poste/poste.reducer';
import question from 'app/entities/question/question.reducer';
import questionAsk from 'app/entities/question-ask/question-ask.reducer';
import reponseCandidat from 'app/entities/reponse-candidat/reponse-candidat.reducer';
import resultat from 'app/entities/resultat/resultat.reducer';
import rH from 'app/entities/rh/rh.reducer';
import sessionTest from 'app/entities/session-test/session-test.reducer';
import managerwork from 'app/features/managerwork/managerwork.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  manager,
  expert,
  rH,
  employe,
  poste,
  competence,
  competenceRequise,
  epreuve,
  question,
  evaluation,
  sessionTest,
  resultat,
  reponseCandidat,
  managerwork,
  questionAsk,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
