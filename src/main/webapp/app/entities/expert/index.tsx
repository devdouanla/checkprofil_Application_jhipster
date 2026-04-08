import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Expert from './expert';
import ExpertDeleteDialog from './expert-delete-dialog';
import ExpertDetail from './expert-detail';
import ExpertUpdate from './expert-update';

const ExpertRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Expert />} />
    <Route path="new" element={<ExpertUpdate />} />
    <Route path=":id">
      <Route index element={<ExpertDetail />} />
      <Route path="edit" element={<ExpertUpdate />} />
      <Route path="delete" element={<ExpertDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExpertRoutes;
