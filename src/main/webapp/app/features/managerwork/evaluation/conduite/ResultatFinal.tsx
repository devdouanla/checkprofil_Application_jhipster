import React from 'react';
import { Alert, Badge, Button, Card } from 'react-bootstrap';

import { IEvaluation } from 'app/shared/model/evaluation.model';
import { ISessionTest } from 'app/shared/model/session-test.model';

import {
  managerworkComputeAverageScore,
  managerworkGetEvaluationStatus,
  managerworkGetMentionVariant,
} from '../../types/managerwork.types';

interface ManagerworkResultatFinalProps {
  evaluation?: IEvaluation;
  sessionTests: ISessionTest[];
  completion: number;
  allComplete: boolean;
  onFinalize: (conforme: boolean) => void;
  loading: boolean;
}

export const ManagerworkResultatFinal = ({
  evaluation,
  sessionTests,
  completion,
  allComplete,
  onFinalize,
  loading,
}: ManagerworkResultatFinalProps) => {
  const average = managerworkComputeAverageScore(sessionTests);
  const status = managerworkGetEvaluationStatus(evaluation);
  const variant = managerworkGetMentionVariant(average);

  return (
    <Card className="mw-card">
      <Card.Body className="p-4">
        <div className="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
          <div>
            <h2 className="h5 fw-semibold mb-1">Resultat final</h2>
            <p className="text-muted mb-0">Consolide les sessions puis cloture l evaluation.</p>
          </div>
          <Badge bg={variant}>{average}%</Badge>
        </div>

        <div className="d-flex flex-column gap-3 mb-4">
          <Alert variant={allComplete ? 'success' : 'info'} className="mb-0">
            {allComplete
              ? 'Toutes les sessions sont entièrement évaluées. La clôture peut être déclenchée.'
              : 'Certaines sessions sont encore en attente d évaluation complète.'}
          </Alert>
          <div className="small text-muted">
            Statut courant: <span className="fw-semibold text-dark">{status}</span> · Progression globale: {completion}%
          </div>
        </div>

        <div className="d-flex gap-2 flex-wrap">
          <Button variant="success" onClick={() => onFinalize(true)} disabled={!allComplete || loading}>
            Valider conforme
          </Button>
          <Button variant="warning" onClick={() => onFinalize(false)} disabled={!allComplete || loading}>
            Cloturer non conforme
          </Button>
        </div>
      </Card.Body>
    </Card>
  );
};
