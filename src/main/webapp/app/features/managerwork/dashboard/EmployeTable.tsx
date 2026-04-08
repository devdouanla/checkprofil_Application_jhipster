import React from 'react';
import { Badge, Button, ButtonGroup, Card, Table } from 'react-bootstrap';
import { Link } from 'react-router';

import { IEmploye } from 'app/shared/model/employe.model';

import { buildManagerworkRoute } from '../managerwork.config';
import { managerworkFormatDate, managerworkGetEmployeLabel, managerworkGetPosteLabel } from '../types/managerwork.types';

interface ManagerworkEmployeTableProps {
  employes: IEmploye[];
}

export const ManagerworkEmployeTable = ({ employes }: ManagerworkEmployeTableProps) => (
  <Card className="mw-card">
    <Card.Body className="p-4">
      <div className="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
        <div>
          <h2 className="h5 fw-semibold mb-1">Collaborateurs suivis</h2>
          <p className="text-muted mb-0">Vue operationnelle pour lancer une evaluation ou consulter les sessions.</p>
        </div>
        <Badge bg="primary">{employes.length} employes actifs</Badge>
      </div>
      <Table responsive hover className="align-middle mb-0">
        <thead>
          <tr>
            <th>Employe</th>
            <th>Poste</th>
            <th>Recrutement</th>
            <th>Statut</th>
            <th className="text-end">Actions</th>
          </tr>
        </thead>
        <tbody>
          {employes.length ? (
            employes.map(employe => (
              <tr key={employe.id}>
                <td>
                  <div className="fw-semibold">{managerworkGetEmployeLabel(employe)}</div>
                  <div className="text-muted small">Reference #{employe.id}</div>
                </td>
                <td>{managerworkGetPosteLabel(employe)}</td>
                <td>{managerworkFormatDate(employe.dateRecrutement)}</td>

                <td className="text-end">
                  <ButtonGroup size="sm">
                    <Button as={Link as any} to={`${buildManagerworkRoute('evaluation/wizard')}?employeId=${employe.id}`} variant="primary">
                      Evaluer
                    </Button>
                    <Button as={Link as any} to={buildManagerworkRoute('dashboard')} variant="outline-secondary">
                      Profil
                    </Button>
                  </ButtonGroup>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={5} className="text-center text-muted py-4">
                Aucun employe disponible pour ce managerwork.
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Card.Body>
  </Card>
);
