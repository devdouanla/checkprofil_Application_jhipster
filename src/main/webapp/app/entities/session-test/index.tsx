import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SessionTest from './session-test';
import SessionTestDeleteDialog from './session-test-delete-dialog';
import SessionTestDetail from './session-test-detail';
import SessionTestUpdate from './session-test-update';

const SessionTestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SessionTest />} />
    <Route path="new" element={<SessionTestUpdate />} />
    <Route path=":id">
      <Route index element={<SessionTestDetail />} />
      <Route path="edit" element={<SessionTestUpdate />} />
      <Route path="delete" element={<SessionTestDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SessionTestRoutes;
