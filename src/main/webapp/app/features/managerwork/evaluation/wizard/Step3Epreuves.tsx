import React from 'react';
import { Col, Row } from 'react-bootstrap';
import { IEpreuve } from 'app/shared/model/epreuve.model';

interface ManagerworkStep3EpreuvesProps {
  epreuves: IEpreuve[];
  selectedEpreuveIds: number[];
  onToggle: (epreuveId: number) => void;
}

const difficulteColor: Record<string, string> = {
  FACILE: '#22c55e',
  MOYEN: '#f59e0b',
  DIFFICILE: '#ef4444',
};

export const ManagerworkStep3Epreuves = ({ epreuves, selectedEpreuveIds, onToggle }: ManagerworkStep3EpreuvesProps) => (
  <>
    <style>{`
      .mw-epreuve-card {
        border-radius: 14px;
        border: 2.5px solid #e2e8f0;
        background: #ffffff;
        cursor: pointer;
        transition:
          border-color 0.22s ease,
          box-shadow 0.22s ease,
          transform 0.18s ease;
        position: relative;
        overflow: hidden;
      }

      .mw-epreuve-card::before {
        content: '';
        position: absolute;
        inset: 0;
        border-radius: 12px;
        opacity: 0;
        background: linear-gradient(135deg, #6366f115 0%, #8b5cf615 100%);
        transition: opacity 0.22s ease;
        pointer-events: none;
      }

      .mw-epreuve-card:hover {
        border-color: #a5b4fc;
        box-shadow: 0 4px 20px rgba(99, 102, 241, 0.12);
        transform: translateY(-2px);
      }

      .mw-epreuve-card:hover::before {
        opacity: 1;
      }

      .mw-epreuve-card.is-selected {
        border-color: #6366f1;
        box-shadow:
          0 0 0 4px rgba(99, 102, 241, 0.15),
          0 6px 24px rgba(99, 102, 241, 0.2);
        transform: translateY(-3px);
        background: linear-gradient(160deg, #fafafe 0%, #f0f0ff 100%);
      }

      .mw-epreuve-card.is-selected::before {
        opacity: 1;
      }

      /* Accent bar top */
      .mw-epreuve-accent {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 3px;
        background: linear-gradient(90deg, #6366f1, #8b5cf6);
        border-radius: 14px 14px 0 0;
        opacity: 0;
        transition: opacity 0.22s ease;
      }

      .mw-epreuve-card.is-selected .mw-epreuve-accent {
        opacity: 1;
      }

      /* Checkmark icon */
      .mw-check-icon {
        width: 22px;
        height: 22px;
        border-radius: 50%;
        background: #6366f1;
        display: flex;
        align-items: center;
        justify-content: center;
        opacity: 0;
        transform: scale(0.6);
        transition: opacity 0.2s ease, transform 0.2s ease;
        flex-shrink: 0;
      }

      .mw-epreuve-card.is-selected .mw-check-icon {
        opacity: 1;
        transform: scale(1);
      }

      .mw-badge-selected {
        background: linear-gradient(135deg, #6366f1, #8b5cf6) !important;
        color: white !important;
        font-size: 0.72rem;
        padding: 4px 10px;
        border-radius: 20px;
        font-weight: 600;
        letter-spacing: 0.02em;
      }

      .mw-badge-option {
        background: #f1f5f9 !important;
        color: #94a3b8 !important;
        font-size: 0.72rem;
        padding: 4px 10px;
        border-radius: 20px;
        font-weight: 500;
        letter-spacing: 0.02em;
      }

      .mw-titre {
        font-size: 0.95rem;
        font-weight: 700;
        color: #1e293b;
        line-height: 1.4;
      }

      .mw-competence {
        font-size: 0.78rem;
        color: #64748b;
        font-weight: 500;
        margin-bottom: 10px;
      }

      .mw-meta {
        display: flex;
        gap: 10px;
        flex-wrap: wrap;
      }

      .mw-meta-pill {
        font-size: 0.73rem;
        background: #f8fafc;
        border: 1px solid #e2e8f0;
        border-radius: 20px;
        padding: 2px 10px;
        color: #475569;
        font-weight: 500;
        display: flex;
        align-items: center;
        gap: 4px;
      }

      .mw-meta-pill .dot {
        width: 6px;
        height: 6px;
        border-radius: 50%;
        display: inline-block;
        flex-shrink: 0;
      }
    `}</style>

    <Row className="g-3">
      {epreuves.map(epreuve => {
        const selected = epreuve.id ? selectedEpreuveIds.includes(epreuve.id) : false;
        const diff = (epreuve.difficulte ?? '').toUpperCase();
        const dotColor = difficulteColor[diff] ?? '#94a3b8';

        return (
          <Col key={epreuve.id} md={6} xl={4}>
            <div
              className={`mw-epreuve-card ${selected ? 'is-selected' : ''}`}
              onClick={() => epreuve.id && onToggle(epreuve.id)}
              role="checkbox"
              aria-checked={selected}
              tabIndex={0}
              onKeyDown={e => e.key === ' ' && epreuve.id && onToggle(epreuve.id)}
            >
              {/* Accent bar */}
              <div className="mw-epreuve-accent" />

              <div className="p-3 pt-4">
                {/* Header */}
                <div className="d-flex justify-content-between align-items-start mb-2 gap-2">
                  <div className="d-flex align-items-center gap-2 flex-grow-1 min-w-0">
                    <div className="mw-check-icon">
                      <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
                        <path d="M2 6l3 3 5-5" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                      </svg>
                    </div>
                    <div className="mw-titre text-truncate">{epreuve.titre}</div>
                  </div>
                  <span className={selected ? 'mw-badge-selected' : 'mw-badge-option'}>{selected ? '✓ Retenue' : 'Option'}</span>
                </div>

                {/* Compétence */}
                <div className="mw-competence">{epreuve.competence?.nom ?? 'Compétence non renseignée'}</div>

                {/* Meta pills */}
                <div className="mw-meta">
                  <span className="mw-meta-pill">
                    <span className="dot" style={{ background: dotColor }} />
                    {epreuve.difficulte ?? 'N/A'}
                  </span>
                  <span className="mw-meta-pill">
                    <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" strokeWidth="2" strokeLinecap="round">
                      <circle cx="12" cy="12" r="10" />
                      <polyline points="12 6 12 12 16 14" />
                    </svg>
                    {epreuve.duree ?? 0} min
                  </span>
                </div>
              </div>
            </div>
          </Col>
        );
      })}
    </Row>
  </>
);
