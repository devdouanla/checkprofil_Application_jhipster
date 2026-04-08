import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Epreuve from './epreuve';
import EpreuveDeleteDialog from './epreuve-delete-dialog';
import EpreuveDetail from './epreuve-detail';
import EpreuveUpdate from './epreuve-update';

const EpreuveRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Epreuve />} />
    <Route path="new" element={<EpreuveUpdate />} />
    <Route path=":id">
      <Route index element={<EpreuveDetail />} />
      <Route path="edit" element={<EpreuveUpdate />} />
      <Route path="delete" element={<EpreuveDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EpreuveRoutes;
