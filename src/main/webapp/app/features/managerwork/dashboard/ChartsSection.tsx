import React from 'react';
import { Badge, Card, Col, ProgressBar, Row } from 'react-bootstrap';

import { ICompetenceRequise } from 'app/shared/model/competence-requise.model';
import { IEvaluation } from 'app/shared/model/evaluation.model';

import { managerworkGetEvaluationStatus } from '../types/managerwork.types';

interface ManagerworkChartsSectionProps {
  evaluations: IEvaluation[];
  competences: ICompetenceRequise[];
}

export const ManagerworkChartsSection = ({ evaluations, competences }: ManagerworkChartsSectionProps) => {
  const totalEvaluations = evaluations.length || 1;
  const statusRows = [
    {
      label: 'En cours',
      value: evaluations.filter(item => managerworkGetEvaluationStatus(item) === 'En cours').length,
      variant: 'primary',
    },
    {
      label: 'Terminees',
      value: evaluations.filter(item => managerworkGetEvaluationStatus(item) === 'Terminee').length,
      variant: 'success',
    },
    {
      label: 'Planifiees',
      value: evaluations.filter(item => managerworkGetEvaluationStatus(item) === 'Planifiee').length,
      variant: 'warning',
    },
  ];

  const categoryRows = Array.from(
    new Map(
      competences.filter(item => item.competence?.id).map(item => [item.competence?.nom ?? `Competence #${item.competence?.id}`, item]),
    ).values(),
  )
    .slice(0, 5)
    .map(item => ({
      label: item.competence?.nom ?? 'Competence',
      obligatoire: Boolean(item.obligatoire),
    }));

  return (
    <Row className="g-3">
      <Col lg={7}>
        <Card className="mw-card">
          <Card.Body className="p-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
              <div>
                <h2 className="h5 fw-semibold mb-1">Pilotage des evaluations</h2>
                <p className="text-muted mb-0">Repartition du portefeuille en temps reel.</p>
              </div>
              <Badge bg="dark">Vue synthese</Badge>
            </div>
            <div className="d-flex flex-column gap-4">
              {statusRows.map(row => {
                const percent = Math.round((row.value / totalEvaluations) * 100);
                return (
                  <div key={row.label}>
                    <div className="d-flex justify-content-between mb-2">
                      <span className="fw-semibold">{row.label}</span>
                      <span className="text-muted">{percent}%</span>
                    </div>
                    <ProgressBar now={percent} variant={row.variant} />
                  </div>
                );
              })}
            </div>
          </Card.Body>
        </Card>
      </Col>
      <Col lg={5}>
        <Card className="mw-card h-100">
          <Card.Body className="p-4">
            <h2 className="h5 fw-semibold mb-1">Couverture competences</h2>
            <p className="text-muted mb-4">Priorites poste et competences requises.</p>
            <div className="d-flex flex-column gap-3">
              {categoryRows.length ? (
                categoryRows.map(item => (
                  <div key={item.label} className="mw-surface rounded-4 p-3">
                    <div className="d-flex justify-content-between align-items-center">
                      <span className="fw-semibold">{item.label}</span>
                      <Badge bg={item.obligatoire ? 'danger' : 'info'}>{item.obligatoire ? 'Obligatoire' : 'Optionnelle'}</Badge>
                    </div>
                  </div>
                ))
              ) : (
                <div className="text-muted">Les competences apparaitront ici apres la selection d un collaborateur.</div>
              )}
            </div>
          </Card.Body>
        </Card>
      </Col>
    </Row>
  );
};
