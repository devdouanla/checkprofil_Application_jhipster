import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RH from './rh';
import RHDeleteDialog from './rh-delete-dialog';
import RHDetail from './rh-detail';
import RHUpdate from './rh-update';

const RHRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RH />} />
    <Route path="new" element={<RHUpdate />} />
    <Route path=":id">
      <Route index element={<RHDetail />} />
      <Route path="edit" element={<RHUpdate />} />
      <Route path="delete" element={<RHDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RHRoutes;
