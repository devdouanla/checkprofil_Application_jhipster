import React, { useEffect } from 'react';
import { Alert, Button, Card, Col, ProgressBar, Row, Spinner } from 'react-bootstrap';
import { Link, useLocation, useNavigate } from 'react-router';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ManagerworkStep1Employe } from './Step1Employe';
import { ManagerworkStep2Competences } from './Step2Competences';
import { ManagerworkStep3Epreuves } from './Step3Epreuves';
import { ManagerworkStep4Recap } from './Step4Recap';
import { ManagerworkWizardStepper } from './WizardStepper';
import {
  clearManagerworkFeedback,
  createManagerworkEvaluationFlow,
  fetchManagerworkBootstrap,
  fetchManagerworkCompetenceFlow,
  fetchManagerworkEpreuveFlow,
  resetManagerworkWizard,
  setManagerworkSelectedEmploye,
  setManagerworkWizardStep,
  toggleManagerworkCompetence,
  toggleManagerworkEpreuve,
} from '../../store';
import { generateManagerworkSessionTest } from '../../api/session-test.api';
import { buildManagerworkRoute } from '../../managerwork.config';

export const ManagerworkEvaluationWizard = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const account = useAppSelector(state => state.authentication.account);
  const {
    actionLoading,
    dashboardLoading,
    wizardLoading,
    progressAnimating,
    errorMessage,
    successMessage,
    employes,
    selectedEmployeId,
    selectedCompetenceIds,
    selectedEpreuveIds,
    competences,
    epreuves,
    currentEvaluation,
    wizardStep,
  } = useAppSelector(state => state.managerwork);

  useEffect(() => {
    if (account?.id) {
      dispatch(fetchManagerworkBootstrap(account.id));
    }
    dispatch(clearManagerworkFeedback());
    return () => {
      dispatch(clearManagerworkFeedback());
    };
  }, [account?.id, dispatch]);

  useEffect(() => {
    const employeIdParam = new URLSearchParams(location.search).get('employeId');
    if (!employeIdParam || !employes.length || selectedEmployeId) {
      return;
    }

    const parsed = Number(employeIdParam);
    if (Number.isFinite(parsed) && employes.some(item => item.id === parsed)) {
      dispatch(setManagerworkSelectedEmploye(parsed));
      dispatch(fetchManagerworkCompetenceFlow(parsed));
      dispatch(setManagerworkWizardStep(2));
    }
  }, [dispatch, employes, location.search, selectedEmployeId]);

  useEffect(() => {
    if (successMessage && currentEvaluation?.id) {
      navigate(buildManagerworkRoute(`evaluation/conduite/${currentEvaluation.id}`));
    }
  }, [currentEvaluation?.id, navigate, successMessage]);

  const selectedEmploye = employes.find(item => item.id === selectedEmployeId);
  const selectedCompetences = competences.filter(item => item.competence?.id && selectedCompetenceIds.includes(item.competence.id));
  const selectedEpreuves = epreuves.filter(item => item.id && selectedEpreuveIds.includes(item.id));

  const handleEmployeSelect = (employeId: number) => {
    dispatch(setManagerworkSelectedEmploye(employeId));
    dispatch(fetchManagerworkCompetenceFlow(employeId));
    dispatch(setManagerworkWizardStep(2));
  };

  const handleNext = () => {
    if (wizardStep === 1 && selectedEmployeId) {
      dispatch(setManagerworkWizardStep(2));
    } else if (wizardStep === 2 && selectedCompetenceIds.length) {
      dispatch(fetchManagerworkEpreuveFlow(selectedCompetenceIds));
      dispatch(setManagerworkWizardStep(3));
    } else if (wizardStep === 3 && selectedEpreuveIds.length) {
      dispatch(setManagerworkWizardStep(4));
    }
  };

  const handleBack = () => {
    if (wizardStep > 1) {
      dispatch(setManagerworkWizardStep((wizardStep - 1) as 1 | 2 | 3 | 4));
    }
  };

  const handleLaunch = async () => {
    if (!selectedEmployeId || !selectedEpreuveIds.length) {
      return;
    }

    // First create evaluation
    const evaluationResult = await dispatch(
      createManagerworkEvaluationFlow({
        employeId: selectedEmployeId,
        epreuveIds: selectedEpreuveIds,
      }),
    );

    if (evaluationResult.payload && (evaluationResult.payload as any).id) {
      const evaluationId = (evaluationResult.payload as any).id;

      // Generate SessionTests for each selected epreuve
      for (const epreuveId of selectedEpreuveIds) {
        try {
          await generateManagerworkSessionTest(evaluationId, epreuveId);
        } catch (error) {
          console.error('Failed to generate session test for epreuve', epreuveId, error);
        }
      }
    }
  };

  return (
    <div className="managerwork-module p-3 p-lg-4">
      <Row className="justify-content-center">
        <Col xxl={11}>
          <Card className="mw-card mb-4">
            <Card.Body className="p-4 p-lg-5">
              <div className="d-flex justify-content-between align-items-start flex-wrap gap-3 mb-4">
                <div>
                  <p className="text-muted mb-0">Un parcours en 4 etapes pour cadrer, planifier et lancer la campagne.</p>
                </div>
                <Button variant="outline-secondary" onClick={() => dispatch(resetManagerworkWizard())}>
                  Reinitialiser
                </Button>
              </div>

              <ManagerworkWizardStepper activeStep={wizardStep} />

              {(dashboardLoading || wizardLoading) && (
                <div className="d-flex align-items-center gap-3 text-muted mb-4">
                  <Spinner animation="border" size="sm" />
                  Chargement des donnees managerwork...
                </div>
              )}

              {errorMessage ? (
                <Alert variant="danger" className="mb-4">
                  {errorMessage}
                </Alert>
              ) : null}

              {successMessage ? (
                <Alert variant="success" className="mb-4">
                  {successMessage}
                </Alert>
              ) : null}

              {actionLoading ? (
                <div className="mb-4">
                  <ProgressBar now={100} striped animated={progressAnimating} variant="primary" />
                </div>
              ) : null}

              <div className="mb-4">
                {wizardStep === 1 ? (
                  <ManagerworkStep1Employe employes={employes} selectedEmployeId={selectedEmployeId} onSelect={handleEmployeSelect} />
                ) : null}
                {wizardStep === 2 ? (
                  <ManagerworkStep2Competences
                    competences={competences}
                    selectedCompetenceIds={selectedCompetenceIds}
                    onToggle={competenceId => dispatch(toggleManagerworkCompetence(competenceId))}
                  />
                ) : null}
                {wizardStep === 3 ? (
                  <ManagerworkStep3Epreuves
                    epreuves={epreuves}
                    selectedEpreuveIds={selectedEpreuveIds}
                    onToggle={epreuveId => dispatch(toggleManagerworkEpreuve(epreuveId))}
                  />
                ) : null}
                {wizardStep === 4 ? (
                  <ManagerworkStep4Recap employe={selectedEmploye} competences={selectedCompetences} epreuves={selectedEpreuves} />
                ) : null}
              </div>

              <div className="d-flex justify-content-between align-items-center flex-wrap gap-3">
                <Button as={Link as any} to={buildManagerworkRoute('dashboard')} variant="outline-dark">
                  Retour dashboard
                </Button>
                <div className="d-flex gap-2">
                  <Button variant="outline-secondary" onClick={handleBack} disabled={wizardStep === 1}>
                    Precedent
                  </Button>
                  {wizardStep < 4 ? (
                    <Button
                      variant="primary"
                      onClick={handleNext}
                      disabled={
                        (wizardStep === 1 && !selectedEmployeId) ||
                        (wizardStep === 2 && !selectedCompetenceIds.length) ||
                        (wizardStep === 3 && !selectedEpreuveIds.length)
                      }
                    >
                      Continuer
                    </Button>
                  ) : (
                    <Button variant="success" onClick={handleLaunch} disabled={actionLoading || !selectedEpreuveIds.length}>
                      Lancer l evaluation
                    </Button>
                  )}
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default ManagerworkEvaluationWizard;
