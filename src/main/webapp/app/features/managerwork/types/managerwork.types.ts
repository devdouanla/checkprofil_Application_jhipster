import dayjs from 'dayjs';

import { ICompetenceRequise } from 'app/shared/model/competence-requise.model';
import { IEmploye } from 'app/shared/model/employe.model';
import { IEpreuve } from 'app/shared/model/epreuve.model';
import { IEvaluation } from 'app/shared/model/evaluation.model';
import { IQuestion } from 'app/shared/model/question.model';
import { ISessionTest } from 'app/shared/model/session-test.model';

export type ManagerworkWizardStep = 1 | 2 | 3 | 4;

export interface ManagerworkKpiItem {
  id: string;
  label: string;
  value: number;
  icon: string;
  accentClass: string;
  helper: string;
}

export interface ManagerworkAlertItem {
  id: string;
  title: string;
  description: string;
  variant: 'warning' | 'danger' | 'info' | 'success';
}

export interface ManagerworkQuestionAnswerMap {
  [questionId: number]: number;
}

export interface ManagerworkEvaluationPayload {
  employeId: number;
  epreuveIds: number[];
}

export interface ManagerworkFinalizePayload {
  evaluationId: number;
  conforme: boolean;
}

export interface ManagerworkSessionScorePayload {
  sessionId: number;
  scoreObtenu: number;
}

export interface RandomDrawnQuestions {
  sessionId: number;
  questions: IQuestion[];
  drawnCount: number;
  poolSize: number;
}

export interface ManagerworkState {
  managerId?: number;
  employes: IEmploye[];
  evaluations: IEvaluation[];
  currentEvaluation?: IEvaluation;
  sessionTests: ISessionTest[];
  competences: ICompetenceRequise[];
  epreuves: IEpreuve[];
  questionsByCompetenceAndDifficulty: Record<number, IQuestion[]>;
  randomQuestionsBySession: Record<number, RandomDrawnQuestions>;
  wizardStep: ManagerworkWizardStep;
  selectedEmployeId?: number;
  selectedCompetenceIds: number[];
  selectedEpreuveIds: number[];
  dashboardLoading: boolean;
  wizardLoading: boolean;
  conduiteLoading: boolean;
  actionLoading: boolean;
  progressAnimating: boolean;
  errorMessage?: string;
  successMessage?: string;
}

export const managerworkGetEmployeLabel = (employe?: IEmploye | null): string => {
  const nom = employe?.nom?.trim();
  if (nom) {
    return nom;
  }
  if (employe?.id != null) {
    return `Employe #${employe.id}`;
  }
  return 'Employe non rattache';
};

/** Affiche le nom du poste si l’API le renvoie, sinon un fallback lisible. */
export const managerworkGetPosteLabel = (employe?: IEmploye | null): string => {
  const posteNom = employe?.poste?.nom?.trim();
  if (posteNom) {
    return posteNom;
  }
  if (employe?.poste?.id != null) {
    return `Poste #${employe.poste.id}`;
  }
  return 'Poste non renseignew';
};

export const managerworkGetEvaluationStatus = (evaluation?: IEvaluation | null): 'Planifiee' | 'En cours' | 'Terminee' => {
  if (!evaluation?.dateDebut) {
    return 'Planifiee';
  }

  if (!evaluation.dateFin) {
    return 'En cours';
  }

  return 'Terminee';
};

export const managerworkFormatDate = (value?: string | Date | dayjs.Dayjs | null, format = 'DD MMM YYYY'): string =>
  value ? dayjs(value).format(format) : 'Non renseignee';

export const managerworkGetMentionVariant = (ratio: number): 'success' | 'warning' | 'danger' => {
  if (ratio >= 75) {
    return 'success';
  }
  if (ratio >= 50) {
    return 'warning';
  }
  return 'danger';
};

export const managerworkComputeSessionProgress = (sessionTests: ISessionTest[]): number => {
  if (!sessionTests.length) {
    return 0;
  }

  const completed = sessionTests.filter(session => session.scoreObtenu !== null && session.scoreObtenu !== undefined).length;
  return Math.round((completed / sessionTests.length) * 100);
};

export const managerworkComputeAverageScore = (sessionTests: ISessionTest[]): number => {
  const scored = sessionTests.filter(session => session.scoreObtenu !== null && session.scoreObtenu !== undefined);
  if (!scored.length) {
    return 0;
  }

  const total = scored.reduce((sum, session) => sum + (session.scoreObtenu ?? 0), 0);
  return Math.round(total / scored.length);
};
