import React, { useEffect, useMemo, useState } from 'react';
import { Alert, Button, Card, Col, Nav, ProgressBar, Row, Spinner } from 'react-bootstrap';
import { Link, useNavigate, useParams } from 'react-router';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { finalizeManagerworkEvaluation, fetchManagerworkConduite, saveManagerworkSessionScore } from '../../store';
import {
  ManagerworkQuestionAnswerMap,
  managerworkComputeAverageScore,
  managerworkGetEmployeLabel,
  managerworkGetPosteLabel,
} from '../../types/managerwork.types';
import { buildManagerworkRoute } from '../../managerwork.config';
import { ManagerworkResultatFinal } from './ResultatFinal';
import { ManagerworkSessionTab } from './SessionTab';

export const ManagerworkConduiteEvaluation = () => {
  const dispatch = useAppDispatch();
  const { evaluationId } = useParams();
  const navigate = useNavigate();
  const parsedEvaluationId = Number(evaluationId);
  const [activeSessionId, setActiveSessionId] = useState<number | undefined>(undefined);
  const [answersBySession, setAnswersBySession] = useState<Record<number, ManagerworkQuestionAnswerMap>>({});
  const {
    currentEvaluation,
    sessionTests,
    questionsByEpreuve,
    conduiteLoading,
    actionLoading,
    progressAnimating,
    errorMessage,
    successMessage,
  } = useAppSelector(state => state.managerwork);

  useEffect(() => {
    if (Number.isFinite(parsedEvaluationId)) {
      dispatch(fetchManagerworkConduite(parsedEvaluationId));
    }
  }, [dispatch, parsedEvaluationId]);

  useEffect(() => {
    if (!activeSessionId && sessionTests[0]?.id) {
      setActiveSessionId(sessionTests[0].id);
    }
  }, [activeSessionId, sessionTests]);

  const activeSession = sessionTests.find(item => item.id === activeSessionId) ?? sessionTests[0];
  const epreuveId = activeSession?.epreuves?.id || activeSession?.epreuve?.id;
  const questions = epreuveId ? (questionsByEpreuve[epreuveId] ?? []) : [];

  // TEMP DEBUG (remove after testing)
  console.warn('ConduiteEvaluation DEBUG:');
  console.warn('  questionsByEpreuve keys:', Object.keys(questionsByEpreuve));
  console.warn('  epreuveId:', epreuveId);
  console.warn('  questions.length:', questions.length);

  useEffect(() => {
    if (!activeSession?.id) {
      return;
    }

    setAnswersBySession(previous => {
      if (previous[activeSession.id]) {
        return previous;
      }

      return {
        ...previous,
        [activeSession.id]: {},
      };
    });
  }, [activeSession?.id]);

  const average = managerworkComputeAverageScore(sessionTests);

  const completedSessionCount = sessionTests.reduce((count, session) => {
    if (!session.id || !session.epreuves?.id) {
      return count;
    }

    const sessionQuestions = questionsByEpreuve[session.epreuves.id] ?? [];
    if (!sessionQuestions.length) {
      return count;
    }

    const answers = answersBySession[session.id] ?? {};
    const allAnswered = sessionQuestions.every(question => question.id !== undefined && answers[question.id] !== undefined);
    return allAnswered ? count + 1 : count;
  }, 0);

  const completion = sessionTests.length ? Math.round((completedSessionCount / sessionTests.length) * 100) : 0;
  const allSessionsComplete = sessionTests.length > 0 && completedSessionCount === sessionTests.length;

  const handleAnswerChange = (questionId: number, value: number) => {
    if (!activeSession?.id) {
      return;
    }

    const currentAnswers = {
      ...(answersBySession[activeSession.id] ?? {}),
      [questionId]: value,
    };

    setAnswersBySession(previous => ({
      ...previous,
      [activeSession.id]: currentAnswers,
    }));

    const total = Object.values(currentAnswers).reduce((sum, score) => sum + (score ?? 0), 0);
    dispatch(saveManagerworkSessionScore({ sessionId: activeSession.id, scoreObtenu: total }));
  };

  const handleFinalize = async (conforme: boolean) => {
    if (!currentEvaluation?.id) {
      return;
    }

    await dispatch(finalizeManagerworkEvaluation({ evaluationId: currentEvaluation.id, conforme }));
  };

  useEffect(() => {
    if (successMessage === 'Evaluation finalisee.' && currentEvaluation?.id) {
      navigate(buildManagerworkRoute('dashboard'), { replace: true });
    }
  }, [successMessage, currentEvaluation?.id, navigate]);

  const summaryCards = useMemo(
    () => [
      { label: 'Employe', value: managerworkGetEmployeLabel(currentEvaluation?.employe) },
      { label: 'Poste', value: managerworkGetPosteLabel(currentEvaluation?.employe) },
      { label: 'Progression', value: `${completion}%` },
      { label: 'Moyenne', value: `${average}%` },
    ],
    [average, completion, currentEvaluation?.employe],
  );

  return (
    <div className="managerwork-module p-3 p-lg-4">
      <Row className="g-4">
        <Col xl={4}>
          <Card className="mw-card">
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start flex-wrap gap-3 mb-4">
                <div>
                  <h1 className="h3 fw-bold mb-2">Conduite d evaluation</h1>
                  <p className="text-muted mb-0">Anime les sessions et consolide le score final.</p>
                </div>
                <Button as={Link as any} to={buildManagerworkRoute('dashboard')} variant="outline-secondary" size="sm">
                  Dashboard
                </Button>
              </div>

              {summaryCards.map(item => (
                <div key={item.label} className="mw-surface rounded-4 p-3 mb-3">
                  <div className="text-muted small">{item.label}</div>
                  <div className="fw-semibold">{item.value}</div>
                </div>
              ))}

              <div className="mt-4">
                <div className="d-flex justify-content-between small mb-2">
                  <span className="text-muted">Progression globale</span>
                  <span className="fw-semibold">{completion}%</span>
                </div>
                <ProgressBar now={completion} striped animated={progressAnimating && actionLoading} variant="info" />
              </div>
            </Card.Body>
          </Card>

          <Card className="mw-card mt-4">
            <Card.Body className="p-4">
              <h2 className="h5 fw-semibold mb-3">Sessions</h2>
              <Nav variant="pills" className="flex-column gap-2">
                {sessionTests.map(session => (
                  <Nav.Item key={session.id}>
                    <Button
                      variant={activeSession?.id === session.id ? 'primary' : 'outline-secondary'}
                      className="w-100 text-start"
                      onClick={() => setActiveSessionId(session.id)}
                    >
                      {session.epreuve?.titre ?? `Session #${session.id}`}
                    </Button>
                  </Nav.Item>
                ))}
              </Nav>
            </Card.Body>
          </Card>
        </Col>

        <Col xl={8}>
          {conduiteLoading ? (
            <div className="d-flex align-items-center gap-3 text-muted mb-4">
              <Spinner animation="border" size="sm" />
              Chargement de la conduite managerwork...
            </div>
          ) : null}

          {errorMessage ? (
            <Alert variant="danger" className="mb-4">
              {errorMessage}
            </Alert>
          ) : null}

          {successMessage ? (
            <Alert variant="success" className="mb-4">
              {successMessage}
            </Alert>
          ) : null}

          {activeSession ? (
            <ManagerworkSessionTab
              sessionTest={activeSession}
              questions={questions}
              answers={answersBySession[activeSession.id] ?? {}}
              onAnswerChange={handleAnswerChange}
              saving={actionLoading}
              progressAnimating={progressAnimating}
            />
          ) : (
            <Card className="mw-card mb-4">
              <Card.Body className="p-4 text-muted">Aucune session disponible pour cette evaluation.</Card.Body>
            </Card>
          )}

          <div className="mt-4">
            <ManagerworkResultatFinal
              evaluation={currentEvaluation}
              sessionTests={sessionTests}
              completion={completion}
              allComplete={allSessionsComplete}
              onFinalize={handleFinalize}
              loading={actionLoading}
            />
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default ManagerworkConduiteEvaluation;
