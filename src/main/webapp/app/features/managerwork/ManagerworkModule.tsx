import React from 'react';
import { Navigate, Route, Routes } from 'react-router';

import { ManagerworkDashboard } from './dashboard/ManagerworkDashboard';
import { ManagerworkConduiteEvaluation } from './evaluation/conduite/ConduiteEvaluation';
import { ManagerworkEvaluationWizard } from './evaluation/wizard/EvaluationWizard';

export const ManagerworkModuleRoutes = () => (
  <Routes>
    <Route index element={<Navigate to="dashboard" replace />} />
    <Route path="dashboard" element={<ManagerworkDashboard />} />
    <Route path="evaluation/wizard" element={<ManagerworkEvaluationWizard />} />
    <Route path="evaluation/conduite/:evaluationId" element={<ManagerworkConduiteEvaluation />} />
    <Route path="*" element={<Navigate to="dashboard" replace />} />
  </Routes>
);
