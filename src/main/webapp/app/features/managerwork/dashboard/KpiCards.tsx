import React from 'react';
import { Card, Col, Row } from 'react-bootstrap';

import { ManagerworkKpiItem } from '../types/managerwork.types';

interface ManagerworkKpiCardsProps {
  items: ManagerworkKpiItem[];
}

export const ManagerworkKpiCards = ({ items }: ManagerworkKpiCardsProps) => (
  <Row className="g-3">
    {items.map(item => (
      <Col key={item.id} xs={12} md={6} xl={3}>
        <Card className={`mw-card mw-kpi-card ${item.accentClass}`}>
          <Card.Body className="p-4 d-flex flex-column gap-3">
            <div className="d-flex justify-content-between align-items-start">
              <div>
                <div className="text-muted text-uppercase small fw-semibold">{item.label}</div>
                <div className="display-6 fw-bold lh-sm">{item.value}</div>
              </div>
              <span className="mw-kpi-icon" aria-hidden="true">
                {item.icon}
              </span>
            </div>
            <div className="text-muted small">{item.helper}</div>
          </Card.Body>
        </Card>
      </Col>
    ))}
  </Row>
);
