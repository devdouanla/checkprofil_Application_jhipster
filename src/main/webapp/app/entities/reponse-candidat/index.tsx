import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ReponseCandidat from './reponse-candidat';
import ReponseCandidatDeleteDialog from './reponse-candidat-delete-dialog';
import ReponseCandidatDetail from './reponse-candidat-detail';
import ReponseCandidatUpdate from './reponse-candidat-update';

const ReponseCandidatRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ReponseCandidat />} />
    <Route path="new" element={<ReponseCandidatUpdate />} />
    <Route path=":id">
      <Route index element={<ReponseCandidatDetail />} />
      <Route path="edit" element={<ReponseCandidatUpdate />} />
      <Route path="delete" element={<ReponseCandidatDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReponseCandidatRoutes;
