import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CompetenceRequise from './competence-requise';
import CompetenceRequiseDeleteDialog from './competence-requise-delete-dialog';
import CompetenceRequiseDetail from './competence-requise-detail';
import CompetenceRequiseUpdate from './competence-requise-update';

const CompetenceRequiseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CompetenceRequise />} />
    <Route path="new" element={<CompetenceRequiseUpdate />} />
    <Route path=":id">
      <Route index element={<CompetenceRequiseDetail />} />
      <Route path="edit" element={<CompetenceRequiseUpdate />} />
      <Route path="delete" element={<CompetenceRequiseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompetenceRequiseRoutes;
