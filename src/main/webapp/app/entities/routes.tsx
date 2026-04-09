import React from 'react';
import { Route } from 'react-router';

import { ManagerworkModuleRoutes } from 'app/features/managerwork/ManagerworkModule';
import { getManagerworkRoutePath } from 'app/features/managerwork/managerwork.config';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Competence from './competence';
import CompetenceRequise from './competence-requise';
import Employe from './employe';
import Epreuve from './epreuve';
import Evaluation from './evaluation';
import Expert from './expert';
import Manager from './manager';
import Poste from './poste';
import Question from './question';
import QuestionAsk from './question-ask';
import ReponseCandidat from './reponse-candidat';
import Resultat from './resultat';
import RH from './rh';
import SessionTest from './session-test';

/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="/manager/*" element={<Manager />} />
        <Route path={getManagerworkRoutePath()} element={<ManagerworkModuleRoutes />} />
        <Route path="/expert/*" element={<Expert />} />
        <Route path="/rh/*" element={<RH />} />
        <Route path="/employe/*" element={<Employe />} />
        <Route path="/poste/*" element={<Poste />} />
        <Route path="/competence/*" element={<Competence />} />
        <Route path="/competence-requise/*" element={<CompetenceRequise />} />
        <Route path="/epreuve/*" element={<Epreuve />} />
        <Route path="/question/*" element={<Question />} />
        <Route path="/evaluation/*" element={<Evaluation />} />
        <Route path="/session-test/*" element={<SessionTest />} />
        <Route path="/resultat/*" element={<Resultat />} />
        <Route path="/reponse-candidat/*" element={<ReponseCandidat />} />
        <Route path="/question-ask/*" element={<QuestionAsk />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
