import React from 'react';
import { Alert, Card } from 'react-bootstrap';

import { ManagerworkAlertItem } from '../types/managerwork.types';

interface ManagerworkAlertesPanelProps {
  alerts: ManagerworkAlertItem[];
}

export const ManagerworkAlertesPanel = ({ alerts }: ManagerworkAlertesPanelProps) => (
  <Card className="mw-card h-100">
    <Card.Body className="p-4">
      <h2 className="h5 fw-semibold mb-1">Alertes prioritaires</h2>
      <p className="text-muted mb-4">Points d attention a traiter avant la prochaine campagne.</p>
      <div className="d-flex flex-column gap-3">
        {alerts.length ? (
          alerts.map(alert => (
            <Alert key={alert.id} variant={alert.variant} className="mb-0">
              <div className="fw-semibold mb-1">{alert.title}</div>
              <div className="small">{alert.description}</div>
            </Alert>
          ))
        ) : (
          <Alert variant="success" className="mb-0">
            Aucun signal bloquant. Le portefeuille est sous controle.
          </Alert>
        )}
      </div>
    </Card.Body>
  </Card>
);
