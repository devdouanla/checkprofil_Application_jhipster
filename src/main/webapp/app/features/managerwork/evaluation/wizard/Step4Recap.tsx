import React from 'react';
import { Badge, Card, ListGroup } from 'react-bootstrap';

import { ICompetenceRequise } from 'app/shared/model/competence-requise.model';
import { IEmploye } from 'app/shared/model/employe.model';
import { IEpreuve } from 'app/shared/model/epreuve.model';

import { managerworkGetEmployeLabel, managerworkGetPosteLabel } from '../../types/managerwork.types';

interface ManagerworkStep4RecapProps {
  employe?: IEmploye;
  competences: ICompetenceRequise[];
  epreuves: IEpreuve[];
}

export const ManagerworkStep4Recap = ({ employe, competences, epreuves }: ManagerworkStep4RecapProps) => (
  <Card className="mw-card">
    <Card.Body className="p-4">
      <h2 className="h5 fw-semibold mb-4">Recapitulatif de lancement</h2>
      <ListGroup variant="flush">
        <ListGroup.Item className="px-0">
          <div className="fw-semibold">{managerworkGetEmployeLabel(employe)}</div>
          <div className="text-muted small">{managerworkGetPosteLabel(employe)}</div>
        </ListGroup.Item>
        <ListGroup.Item className="px-0">
          <div className="fw-semibold mb-2">Competences selectionnees</div>
          <div className="d-flex flex-wrap gap-2">
            {competences.map(item => (
              <Badge key={item.id} bg={item.obligatoire ? 'danger' : 'info'}>
                {/* {item.competence?.nom}  */}
              </Badge>
            ))}
          </div>
        </ListGroup.Item>
        <ListGroup.Item className="px-0">
          <div className="fw-semibold mb-2">Epreuves planifiees</div>
          <div className="d-flex flex-column gap-2">
            {epreuves.map(item => (
              <div key={item.id} className="mw-surface rounded-3 p-3">
                <div className="fw-semibold">{item.titre}</div>
                <div className="text-muted small">
                  {item.competence?.nom} · {item.difficulte ?? 'N/A'} · {item.duree ?? 0} min
                </div>
              </div>
            ))}
          </div>
        </ListGroup.Item>
      </ListGroup>
    </Card.Body>
  </Card>
);
