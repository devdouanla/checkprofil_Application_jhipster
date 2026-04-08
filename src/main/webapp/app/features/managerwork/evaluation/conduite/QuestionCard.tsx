import React from 'react';
import { IQuestion } from 'app/shared/model/question.model';

interface ManagerworkQuestionCardProps {
  question: IQuestion;
  value?: number;
  onChange: (questionId: number, value: number) => void;
}

export const ManagerworkQuestionCard = ({ question, value, onChange }: ManagerworkQuestionCardProps) => {
  const maxScore = question.points ?? 20;
  const isFound = value === maxScore;
  const isNotFound = value === 0;
  const hasAnswer = value !== undefined;

  const handleSelect = (found: boolean) => {
    if (question.id) onChange(question.id, found ? maxScore : 0);
  };

  const scorePercent = hasAnswer ? (value / maxScore) * 100 : null;

  return (
    <>
      <style>{`
        .mwq-card {
          border-radius: 16px;
          border: 1.5px solid #e2e8f0;
          background: #fff;
          overflow: hidden;
          transition: box-shadow 0.2s ease, border-color 0.2s ease;
          margin-bottom: 1rem;
        }
        .mwq-card:hover {
          box-shadow: 0 4px 24px rgba(0,0,0,0.07);
        }
        .mwq-card.is-found {
          border-color: #22c55e;
          box-shadow: 0 0 0 3px rgba(34,197,94,0.12), 0 4px 20px rgba(34,197,94,0.1);
        }
        .mwq-card.is-not-found {
          border-color: #ef4444;
          box-shadow: 0 0 0 3px rgba(239,68,68,0.12), 0 4px 20px rgba(239,68,68,0.1);
        }

        .mwq-header {
          display: flex;
          align-items: flex-start;
          gap: 14px;
          padding: 20px 22px 16px;
          border-bottom: 1px solid #f1f5f9;
        }
        .mwq-id-badge {
          flex-shrink: 0;
          width: 34px;
          height: 34px;
          border-radius: 10px;
          background: linear-gradient(135deg, #6366f1, #8b5cf6);
          color: white;
          font-size: 0.7rem;
          font-weight: 700;
          display: flex;
          align-items: center;
          justify-content: center;
          letter-spacing: 0.03em;
        }
        .mwq-enonce {
          font-size: 0.95rem;
          font-weight: 700;
          color: #0f172a;
          line-height: 1.5;
          flex: 1;
        }

        .mwq-body {
          padding: 16px 22px 20px;
          display: flex;
          flex-direction: column;
          gap: 14px;
        }

        .mwq-section-label {
          font-size: 0.68rem;
          font-weight: 700;
          letter-spacing: 0.08em;
          text-transform: uppercase;
          color: #94a3b8;
          margin-bottom: 4px;
        }
        .mwq-section-text {
          font-size: 0.84rem;
          color: #475569;
          line-height: 1.6;
        }

        .mwq-reponse-box {
          background: #f8fafc;
          border: 1px dashed #cbd5e1;
          border-radius: 10px;
          padding: 10px 14px;
        }
        .mwq-reponse-box .mwq-section-text {
          color: #334155;
          font-style: italic;
        }

        /* Buttons */
        .mwq-actions {
          display: flex;
          gap: 10px;
        }
        .mwq-btn {
          flex: 1;
          padding: 10px 16px;
          border-radius: 10px;
          font-size: 0.84rem;
          font-weight: 600;
          border: 2px solid transparent;
          cursor: pointer;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 7px;
          transition: all 0.18s ease;
          background: transparent;
        }
        .mwq-btn-found {
          border-color: #22c55e;
          color: #16a34a;
          background: #f0fdf4;
        }
        .mwq-btn-found:hover, .mwq-btn-found.active {
          background: #22c55e;
          color: white;
          box-shadow: 0 4px 12px rgba(34,197,94,0.3);
          transform: translateY(-1px);
        }
        .mwq-btn-found.active {
          background: #16a34a;
          border-color: #16a34a;
          box-shadow: 0 4px 14px rgba(22,163,74,0.35);
        }
        .mwq-btn-notfound {
          border-color: #ef4444;
          color: #dc2626;
          background: #fef2f2;
        }
        .mwq-btn-notfound:hover, .mwq-btn-notfound.active {
          background: #ef4444;
          color: white;
          box-shadow: 0 4px 12px rgba(239,68,68,0.3);
          transform: translateY(-1px);
        }
        .mwq-btn-notfound.active {
          background: #dc2626;
          border-color: #dc2626;
          box-shadow: 0 4px 14px rgba(220,38,38,0.35);
        }

        /* Score bar */
        .mwq-score-row {
          display: flex;
          align-items: center;
          gap: 12px;
        }
        .mwq-score-bar-wrap {
          flex: 1;
          height: 6px;
          background: #f1f5f9;
          border-radius: 99px;
          overflow: hidden;
        }
        .mwq-score-bar-fill {
          height: 100%;
          border-radius: 99px;
          transition: width 0.3s ease, background 0.3s ease;
        }
        .mwq-score-label {
          font-size: 0.8rem;
          font-weight: 700;
          white-space: nowrap;
          min-width: 60px;
          text-align: right;
        }
        .score-found   { color: #16a34a; }
        .score-notfound{ color: #dc2626; }
        .score-neutral { color: #94a3b8; }
      `}</style>

      <div className={`mwq-card ${isFound ? 'is-found' : ''} ${isNotFound && hasAnswer ? 'is-not-found' : ''}`}>
        {/* Header */}
        <div className="mwq-header">
          <div className="mwq-id-badge">Q{question.id}</div>
          <div className="mwq-enonce">{question.enonce}</div>
        </div>

        {/* Body */}
        <div className="mwq-body">
          {/* Explication */}
          {question.explication && (
            <div>
              <div className="mwq-section-label"> Explication</div>
              <div className="mwq-section-text">{question.explication}</div>
            </div>
          )}

          {/* Réponse attendue */}
          <div>
            <div className="mwq-section-label">Réponse attendue</div>
            <div className="mwq-reponse-box">
              <div className="mwq-section-text">{question.reponseTexte ?? 'Aucune réponse attendue renseignée'}</div>
            </div>
          </div>

          {/* Actions */}
          <div className="mwq-actions">
            <button className={`mwq-btn mwq-btn-found ${isFound ? 'active' : ''}`} onClick={() => handleSelect(true)}>
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2.5"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <polyline points="20 6 9 17 4 12" />
              </svg>
              Trouvée
            </button>
            <button className={`mwq-btn mwq-btn-notfound ${isNotFound && hasAnswer ? 'active' : ''}`} onClick={() => handleSelect(false)}>
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2.5"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
              Non trouvée
            </button>
          </div>

          {/* Score bar */}
          <div className="mwq-score-row">
            <div className="mwq-score-bar-wrap">
              <div
                className="mwq-score-bar-fill"
                style={{
                  width: scorePercent !== null ? `${scorePercent}%` : '0%',
                  background: isFound
                    ? 'linear-gradient(90deg,#22c55e,#16a34a)'
                    : hasAnswer
                      ? 'linear-gradient(90deg,#f87171,#ef4444)'
                      : '#e2e8f0',
                }}
              />
            </div>
            <div className={`mwq-score-label ${isFound ? 'score-found' : hasAnswer ? 'score-notfound' : 'score-neutral'}`}>
              {hasAnswer ? value : '—'} / {maxScore} pts
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
