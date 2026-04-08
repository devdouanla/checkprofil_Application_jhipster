import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Resultat from './resultat';
import ResultatDeleteDialog from './resultat-delete-dialog';
import ResultatDetail from './resultat-detail';
import ResultatUpdate from './resultat-update';

const ResultatRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Resultat />} />
    <Route path="new" element={<ResultatUpdate />} />
    <Route path=":id">
      <Route index element={<ResultatDetail />} />
      <Route path="edit" element={<ResultatUpdate />} />
      <Route path="delete" element={<ResultatDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ResultatRoutes;
