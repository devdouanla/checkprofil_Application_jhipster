import React from 'react';
import { Badge, Card, Col, Row } from 'react-bootstrap';

import { IEmploye } from 'app/shared/model/employe.model';

import { managerworkFormatDate, managerworkGetEmployeLabel, managerworkGetPosteLabel } from '../../types/managerwork.types';

interface ManagerworkStep1EmployeProps {
  employes: IEmploye[];
  selectedEmployeId?: number;
  onSelect: (employeId: number) => void;
}

export const ManagerworkStep1Employe = ({ employes, selectedEmployeId, onSelect }: ManagerworkStep1EmployeProps) => (
  <Row className="g-3">
    {employes.map(employe => (
      <Col key={employe.id} md={6} xl={4}>
        <Card
          className={`mw-card mw-choice-card ${selectedEmployeId === employe.id ? 'is-selected' : ''}`}
          onClick={() => employe.id && onSelect(employe.id)}
        >
          <Card.Body className="p-4">
            <div className="d-flex justify-content-between align-items-start mb-3">
              <div>
                <div className="fw-semibold">{managerworkGetEmployeLabel(employe)}</div>
                <div className="text-muted small">{managerworkGetPosteLabel(employe)}</div>
              </div>
              <Badge bg={selectedEmployeId === employe.id ? 'primary' : 'secondary'}>
                {selectedEmployeId === employe.id ? 'Selectionne' : 'Disponible'}
              </Badge>
            </div>
            <div className="text-muted small">Date de recrutement: {managerworkFormatDate(employe.dateRecrutement)}</div>
          </Card.Body>
        </Card>
      </Col>
    ))}
  </Row>
);
