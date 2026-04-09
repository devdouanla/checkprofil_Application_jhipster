import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import QuestionAsk from './question-ask';
import QuestionAskDeleteDialog from './question-ask-delete-dialog';
import QuestionAskDetail from './question-ask-detail';
import QuestionAskUpdate from './question-ask-update';

const QuestionAskRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QuestionAsk />} />
    <Route path="new" element={<QuestionAskUpdate />} />
    <Route path=":id">
      <Route index element={<QuestionAskDetail />} />
      <Route path="edit" element={<QuestionAskUpdate />} />
      <Route path="delete" element={<QuestionAskDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QuestionAskRoutes;
