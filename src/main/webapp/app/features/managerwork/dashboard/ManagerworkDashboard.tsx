import React, { useEffect, useMemo } from 'react';
import { Alert, Badge, Button, Card, Col, Row, Spinner } from 'react-bootstrap';
import { Link } from 'react-router';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ManagerworkAlertesPanel } from './AlertesPanel';
import { ManagerworkChartsSection } from './ChartsSection';
import { ManagerworkEmployeTable } from './EmployeTable';
import { ManagerworkKpiCards } from './KpiCards';
import { buildManagerworkRoute } from '../managerwork.config';
import { fetchManagerworkBootstrap } from '../store';
import {
  managerworkComputeAverageScore,
  managerworkComputeSessionProgress,
  managerworkGetEvaluationStatus,
} from '../types/managerwork.types';

export const ManagerworkDashboard = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const { dashboardLoading, errorMessage, employes, evaluations, sessionTests, competences } = useAppSelector(state => state.managerwork);

  useEffect(() => {
    if (account?.id) {
      dispatch(fetchManagerworkBootstrap(account.id));
    }
  }, [account?.id, dispatch]);

  const kpis = useMemo(() => {
    const inProgress = evaluations.filter(item => managerworkGetEvaluationStatus(item) === 'En cours').length;
    return [
      {
        id: 'employes',
        label: 'Employes suivis',
        value: employes.length,
        icon: '👥',
        accentClass: 'mw-kpi-card-primary',
        helper: 'Collaborateurs actifs rattaches au perimetre managerwork.',
      },
      {
        id: 'evaluations',
        label: 'Evaluations en cours',
        value: inProgress,
        icon: '🧭',
        accentClass: 'mw-kpi-card-info',
        helper: 'Campagnes actuellement ouvertes ou a piloter.',
      },
      {
        id: 'progress',
        label: 'Progression sessions',
        value: managerworkComputeSessionProgress(sessionTests),
        icon: '📈',
        accentClass: 'mw-kpi-card-warning',
        helper: 'Pourcentage des sessions renseignees pour l evaluation active.',
      },
      {
        id: 'score',
        label: 'Score moyen',
        value: managerworkComputeAverageScore(sessionTests),
        icon: '✅',
        accentClass: 'mw-kpi-card-success',
        helper: 'Moyenne des scores deja consolides.',
      },
    ];
  }, [employes.length, evaluations, sessionTests]);

  const alerts = useMemo(() => {
    const openEvaluations = evaluations.filter(item => managerworkGetEvaluationStatus(item) === 'En cours').length;
    const noEvaluations = employes.length - new Set(evaluations.map(item => item.employe?.id).filter(Boolean)).size;
    const missingCompetences = employes.filter(item => !item.poste?.id).length;

    return [
      openEvaluations > 2
        ? {
            id: 'open',
            title: 'Flux d evaluations dense',
            description: `${openEvaluations} evaluations sont encore ouvertes. Priorise les clotures les plus anciennes.`,
            variant: 'warning' as const,
          }
        : null,
      noEvaluations > 0
        ? {
            id: 'coverage',
            title: 'Couverture incomplet e',
            description: `${noEvaluations} employes n ont pas encore de campagne active ou terminee.`,
            variant: 'info' as const,
          }
        : null,
      missingCompetences > 0
        ? {
            id: 'poste',
            title: 'Dossiers poste incomplets',
            description: `${missingCompetences} collaborateurs n ont pas de poste complet pour initialiser le wizard.`,
            variant: 'danger' as const,
          }
        : null,
    ].filter(item => item !== null);
  }, [employes, evaluations]);

  return (
    <div className="managerwork-module p-3 p-lg-4">
      <Card className="mw-card mw-hero mb-4">
        <Card.Body className="p-4 p-lg-5">
          <Row className="align-items-center g-4">
            <Col lg={8}>
              <Badge bg="light" text="dark" className="mb-3">
                Managerwork cockpit
              </Badge>
              <h1 className="display-6 fw-bold mb-3">Pilote les evaluations collaborateurs depuis un tableau de bord unique</h1>
              <p className="mb-0 fs-5 lh-sm opacity-75">
                Supervision, lancement des campagnes, conduite des epreuves et cloture des evaluations dans un parcours fluide.
              </p>
            </Col>
            <Col lg={4}>
              <div className="d-grid gap-2">
                <Button as={Link as any} to={buildManagerworkRoute('evaluation/wizard')} variant="light">
                  Demarrer une evaluation
                </Button>
                <Button as={Link as any} to={buildManagerworkRoute('dashboard')} variant="outline-light">
                  Actualiser la vue
                </Button>
              </div>
            </Col>
          </Row>
        </Card.Body>
      </Card>

      {dashboardLoading ? (
        <div className="d-flex align-items-center gap-3 text-muted mb-4">
          <Spinner animation="border" size="sm" />
          Chargement du dashboard managerwork...
        </div>
      ) : null}

      {errorMessage ? (
        <Alert variant="danger" className="mb-4">
          {errorMessage}
        </Alert>
      ) : null}

      <div className="mb-4">
        <ManagerworkKpiCards items={kpis} />
      </div>

      <div className="mb-4">
        <ManagerworkChartsSection evaluations={evaluations} competences={competences} />
      </div>

      <Row className="g-4">
        <Col xl={8}>
          <ManagerworkEmployeTable employes={employes} />
        </Col>
        <Col xl={4}>
          <ManagerworkAlertesPanel alerts={alerts} />
        </Col>
      </Row>
    </div>
  );
};

export default ManagerworkDashboard;
