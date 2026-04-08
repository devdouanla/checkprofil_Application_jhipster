import React, { useEffect } from 'react';
import { Alert, Badge, Card, ProgressBar } from 'react-bootstrap';

import { IQuestion } from 'app/shared/model/question.model';
import { ISessionTest } from 'app/shared/model/session-test.model';

import { ManagerworkQuestionAnswerMap, managerworkGetMentionVariant } from '../../types/managerwork.types';
import { ManagerworkQuestionCard } from './QuestionCard';

interface ManagerworkSessionTabProps {
  sessionTest: ISessionTest;
  questions: IQuestion[];
  answers: ManagerworkQuestionAnswerMap;
  onAnswerChange: (questionId: number, value: number) => void;
  saving: boolean;
  progressAnimating: boolean;
}

export const ManagerworkSessionTab = ({
  sessionTest,
  questions,
  answers,
  onAnswerChange,
  saving,
  progressAnimating,
}: ManagerworkSessionTabProps) => {
  // TEMP DEBUG (remove after testing)
  useEffect(() => {
    console.warn('SessionTab DEBUG - Session:', sessionTest.epreuves?.titre, 'Epreuve ID:', sessionTest.epreuves?.id);
    console.warn('SessionTab DEBUG - Questions length:', questions.length, 'Questions:', questions);
    console.warn('SessionTab DEBUG - questionsByEpreuve expected for epreuve', sessionTest.epreuves?.id);
  }, [questions, sessionTest]);

  const maxScore = questions.reduce((sum, question) => sum + (question.points ?? 20), 0);
  const currentScore = questions.reduce((sum, question) => sum + (question.id ? (answers[question.id] ?? 0) : 0), 0);
  const answeredCount = questions.filter(question => question.id !== undefined && answers[question.id] !== undefined).length;
  const totalQuestions = questions.length;
  const ratio = maxScore ? Math.round((currentScore / maxScore) * 100) : 0;
  const mentionVariant = managerworkGetMentionVariant(ratio);
  const mentionLabel = ratio >= 75 ? 'Très bien' : ratio >= 50 ? 'Satisfaisant' : 'Insuffisant';

  return (
    <Card className="mw-card">
      <Card.Body className="p-4">
        <div className="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
          <div>
            <h2 className="h5 fw-semibold mb-1">{sessionTest.epreuves?.titre ?? 'Session'}</h2>
            <div className="text-muted small">{sessionTest.epreuves?.competence?.nom ?? 'Competence non renseignee'}</div>
          </div>
          <Badge
            bg={sessionTest.scoreObtenu !== null && sessionTest.scoreObtenu !== undefined ? 'success' : 'warning'}
            className="mw-score-pill"
          >
            {sessionTest.scoreObtenu !== null && sessionTest.scoreObtenu !== undefined ? 'Enregistree' : 'A noter'}
          </Badge>
        </div>

        <div className="mb-4">
          <div className="d-flex justify-content-between small mb-2">
            <span className="text-muted">Progression de notation</span>
            <span className="fw-semibold">
              {answeredCount}/{totalQuestions} questions
            </span>
          </div>
          <ProgressBar
            now={totalQuestions ? (answeredCount / totalQuestions) * 100 : 0}
            striped
            animated={progressAnimating && saving}
            variant="primary"
          />
        </div>

        {questions.length === 0 ? (
          <Alert variant="warning" className="mb-3">
            <div className="fw-semibold mb-2">Aucune question trouvée</div>
            <div className="mb-2">
              Épreuve: <strong>{sessionTest.epreuves?.titre ?? 'Inconnue'}</strong> (ID: {sessionTest.epreuves?.id ?? 'N/A'})
            </div>
            <div>
              Vérifiez questionsByEpreuve[{sessionTest.epreuves?.id}] et l&apos;API /api/questions/epreuve/{sessionTest.epreuves?.id}
            </div>
          </Alert>
        ) : (
          questions.map(question => (
            <ManagerworkQuestionCard
              key={question.id}
              question={question}
              value={question.id ? answers[question.id] : undefined}
              onChange={onAnswerChange}
            />
          ))
        )}

        <div className="d-flex justify-content-between align-items-center mt-4 flex-wrap gap-3">
          <div>
            <div className="text-muted small">Score calcule</div>
            <div className="fw-semibold">
              {currentScore}/{maxScore}
            </div>
          </div>
          <div className="text-end">
            <Badge bg={mentionVariant}>{mentionLabel}</Badge>
            <div className="small text-muted mt-1">Sauvegarde automatique à chaque réponse</div>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
};
