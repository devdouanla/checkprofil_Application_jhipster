import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';

import { IRootState } from 'app/config/store';
import { IQuestion } from 'app/shared/model/question.model';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ISessionTest } from 'app/shared/model/session-test.model';

import { fetchManagerworkCompetencesByPoste } from '../api/competence.api';
import { fetchManagerworkEmployesByManager } from '../api/employe.api';
import {
  createManagerworkEvaluation,
  fetchManagerworkEvaluation,
  fetchManagerworkEvaluationsByManager,
  fetchManagerworkManagerByUserId,
  updateManagerworkEvaluation,
} from '../api/evaluation.api';
import { fetchManagerworkEpreuvesByCompetence } from '../api/epreuve.api';
import { fetchManagerworkQuestionsByEpreuve } from '../api/question.api';
import {
  createManagerworkSessionTest,
  fetchManagerworkSessionTestsByEvaluation,
  updateManagerworkSessionTest,
} from '../api/session-test.api';
import {
  ManagerworkEvaluationPayload,
  ManagerworkFinalizePayload,
  ManagerworkSessionScorePayload,
  ManagerworkState,
} from '../types/managerwork.types';

const initialState: ManagerworkState = {
  employes: [],
  evaluations: [],
  sessionTests: [],
  competences: [],
  epreuves: [],
  questionsByEpreuve: {},
  wizardStep: 1,
  selectedCompetenceIds: [],
  selectedEpreuveIds: [],
  dashboardLoading: false,
  wizardLoading: false,
  conduiteLoading: false,
  actionLoading: false,
  progressAnimating: false,
};

export const fetchManagerworkBootstrap = createAsyncThunk(
  'managerwork/bootstrap',
  async (userId: number, thunkAPI) => {
    const managerResult = await fetchManagerworkManagerByUserId(userId);
    const manager = managerResult.data[0];

    if (!manager?.id) {
      return thunkAPI.rejectWithValue('Aucun profil managerwork n est associe a cet utilisateur.');
    }

    const [employesResult, evaluationsResult] = await Promise.all([
      fetchManagerworkEmployesByManager(manager.id),
      fetchManagerworkEvaluationsByManager(manager.id),
    ]);

    return {
      managerId: manager.id,
      employes: employesResult.data,
      evaluations: evaluationsResult.data,
    };
  },
  { serializeError: serializeAxiosError },
);

export const fetchManagerworkCompetenceFlow = createAsyncThunk(
  'managerwork/fetch_competence_flow',
  async (employeId: number, thunkAPI) => {
    const state = thunkAPI.getState() as IRootState;
    const employe = state.managerwork.employes.find(item => item.id === employeId);
    const posteId = employe?.poste?.id;

    if (!posteId) {
      return thunkAPI.rejectWithValue('Le poste de cet employe est introuvable.');
    }

    const competencesResult = await fetchManagerworkCompetencesByPoste(posteId);
    return { employeId, competences: competencesResult.data };
  },
  { serializeError: serializeAxiosError },
);

export const fetchManagerworkEpreuveFlow = createAsyncThunk(
  'managerwork/fetch_epreuve_flow',
  async (competenceIds: number[]) => {
    const responses = await Promise.all(competenceIds.map(id => fetchManagerworkEpreuvesByCompetence(id)));
    const epreuves = responses.flatMap(response => response.data);
    const unique = Array.from(new Map(epreuves.filter(item => item.id).map(item => [item.id, item])).values());
    return unique;
  },
  { serializeError: serializeAxiosError },
);

export const fetchManagerworkConduite = createAsyncThunk(
  'managerwork/fetch_conduite',
  async (evaluationId: number) => {
    const [evaluationResult, sessionResult] = await Promise.all([
      fetchManagerworkEvaluation(evaluationId),
      fetchManagerworkSessionTestsByEvaluation(evaluationId),
    ]);

    const questionResponses = await Promise.all(
      sessionResult.data
        .map(session => session.epreuves?.id)
        .filter((id): id is number => Boolean(id))
        .map(async epreuveId => ({ epreuveId, result: await fetchManagerworkQuestionsByEpreuve(epreuveId) })),
    );

    const questionsByEpreuve = questionResponses.reduce<Record<number, IQuestion[]>>((accumulator, item) => {
      accumulator[item.epreuveId] = item.result.data;
      return accumulator;
    }, {});

    return {
      evaluation: evaluationResult.data,
      sessionTests: sessionResult.data,
      questionsByEpreuve,
    };
  },
  { serializeError: serializeAxiosError },
);

export const createManagerworkEvaluationFlow = createAsyncThunk(
  'managerwork/create_evaluation_flow',
  async (payload: ManagerworkEvaluationPayload, thunkAPI) => {
    const evaluationResult = await createManagerworkEvaluation(payload.employeId);
    const evaluation = evaluationResult.data;

    if (!evaluation.id) {
      return thunkAPI.rejectWithValue('Creation de l evaluation impossible.');
    }

    await Promise.all(payload.epreuveIds.map(epreuveId => createManagerworkSessionTest(evaluation.id, epreuveId)));

    const bootstrapState = thunkAPI.getState() as IRootState;
    if (bootstrapState.managerwork.managerId) {
      thunkAPI.dispatch(fetchManagerworkBootstrap(bootstrapState.managerwork.managerId));
    }

    const conduite = await thunkAPI.dispatch(fetchManagerworkConduite(evaluation.id)).unwrap();
    return {
      createdEvaluationId: evaluation.id,
      evaluation: conduite.evaluation,
      sessionTests: conduite.sessionTests,
      questionsByEpreuve: conduite.questionsByEpreuve,
    };
  },
  { serializeError: serializeAxiosError },
);

export const saveManagerworkSessionScore = createAsyncThunk(
  'managerwork/save_session_score',
  async (payload: ManagerworkSessionScorePayload, thunkAPI) => {
    const state = thunkAPI.getState() as IRootState;
    const session = state.managerwork.sessionTests.find(item => item.id === payload.sessionId);

    if (!session?.id) {
      return thunkAPI.rejectWithValue('Session introuvable.');
    }

    const updatedSession: ISessionTest = {
      ...session,
      scoreObtenu: payload.scoreObtenu,
    };

    const result = await updateManagerworkSessionTest(updatedSession);
    const evaluationId = state.managerwork.currentEvaluation?.id ?? updatedSession.evaluation?.id;
    if (evaluationId) {
      thunkAPI.dispatch(fetchManagerworkConduite(evaluationId));
    }

    return result.data;
  },
  { serializeError: serializeAxiosError },
);

export const finalizeManagerworkEvaluation = createAsyncThunk(
  'managerwork/finalize_evaluation',
  async (payload: ManagerworkFinalizePayload, thunkAPI) => {
    const state = thunkAPI.getState() as IRootState;
    const currentEvaluation = state.managerwork.currentEvaluation;

    if (!currentEvaluation?.id) {
      return thunkAPI.rejectWithValue('Evaluation introuvable.');
    }

    const result = await updateManagerworkEvaluation({
      ...currentEvaluation,
      dateFin: new Date() as never,
      conforme: payload.conforme,
    });

    thunkAPI.dispatch(fetchManagerworkConduite(currentEvaluation.id));

    if (state.managerwork.managerId) {
      thunkAPI.dispatch(fetchManagerworkBootstrap(state.managerwork.managerId));
    }

    return result.data;
  },
  { serializeError: serializeAxiosError },
);

const managerworkSlice = createSlice({
  name: 'managerwork',
  initialState,
  reducers: {
    clearManagerworkFeedback(state) {
      state.errorMessage = undefined;
      state.successMessage = undefined;
    },
    resetManagerworkWizard(state) {
      state.wizardStep = 1;
      state.selectedEmployeId = undefined;
      state.selectedCompetenceIds = [];
      state.selectedEpreuveIds = [];
      state.competences = [];
      state.epreuves = [];
    },
    setManagerworkWizardStep(state, action) {
      state.wizardStep = action.payload;
    },
    setManagerworkSelectedEmploye(state, action) {
      state.selectedEmployeId = action.payload;
      state.selectedCompetenceIds = [];
      state.selectedEpreuveIds = [];
      state.competences = [];
      state.epreuves = [];
    },
    toggleManagerworkCompetence(state, action) {
      const competenceId = action.payload as number;
      state.selectedCompetenceIds = state.selectedCompetenceIds.includes(competenceId)
        ? state.selectedCompetenceIds.filter(id => id !== competenceId)
        : [...state.selectedCompetenceIds, competenceId];
      state.selectedEpreuveIds = [];
    },
    toggleManagerworkEpreuve(state, action) {
      const epreuveId = action.payload as number;
      state.selectedEpreuveIds = state.selectedEpreuveIds.includes(epreuveId)
        ? state.selectedEpreuveIds.filter(id => id !== epreuveId)
        : [...state.selectedEpreuveIds, epreuveId];
    },
  },
  extraReducers(builder) {
    builder
      .addCase(fetchManagerworkBootstrap.pending, state => {
        state.dashboardLoading = true;
        state.errorMessage = undefined;
      })
      .addCase(fetchManagerworkBootstrap.fulfilled, (state, action) => {
        state.dashboardLoading = false;
        state.managerId = action.payload.managerId;
        state.employes = action.payload.employes;
        state.evaluations = action.payload.evaluations;
      })
      .addCase(fetchManagerworkBootstrap.rejected, (state, action) => {
        state.dashboardLoading = false;
        state.errorMessage = (action.payload as string) ?? action.error.message ?? 'Chargement managerwork impossible.';
      })
      .addCase(fetchManagerworkCompetenceFlow.pending, state => {
        state.wizardLoading = true;
        state.errorMessage = undefined;
      })
      .addCase(fetchManagerworkCompetenceFlow.fulfilled, (state, action) => {
        state.wizardLoading = false;
        state.selectedEmployeId = action.payload.employeId;
        state.competences = action.payload.competences;
      })
      .addCase(fetchManagerworkCompetenceFlow.rejected, (state, action) => {
        state.wizardLoading = false;
        state.errorMessage = (action.payload as string) ?? action.error.message ?? 'Chargement des competences impossible.';
      })
      .addCase(fetchManagerworkEpreuveFlow.pending, state => {
        state.wizardLoading = true;
      })
      .addCase(fetchManagerworkEpreuveFlow.fulfilled, (state, action) => {
        state.wizardLoading = false;
        state.epreuves = action.payload;
      })
      .addCase(fetchManagerworkEpreuveFlow.rejected, (state, action) => {
        state.wizardLoading = false;
        state.errorMessage = (action.payload as string) ?? action.error.message ?? 'Chargement des epreuves impossible.';
      })
      .addCase(createManagerworkEvaluationFlow.pending, state => {
        state.actionLoading = true;
        state.progressAnimating = true;
        state.errorMessage = undefined;
        state.successMessage = undefined;
      })
      .addCase(createManagerworkEvaluationFlow.fulfilled, (state, action) => {
        state.actionLoading = false;
        state.progressAnimating = false;
        state.currentEvaluation = action.payload.evaluation;
        state.sessionTests = action.payload.sessionTests;
        state.questionsByEpreuve = action.payload.questionsByEpreuve;
        state.successMessage = 'Evaluation creee et sessions initialisees avec succes.';
      })
      .addCase(createManagerworkEvaluationFlow.rejected, (state, action) => {
        state.actionLoading = false;
        state.progressAnimating = false;
        state.errorMessage = (action.payload as string) ?? action.error.message ?? 'Creation de l evaluation impossible.';
      })
      .addCase(fetchManagerworkConduite.pending, state => {
        state.conduiteLoading = true;
        state.errorMessage = undefined;
      })
      .addCase(fetchManagerworkConduite.fulfilled, (state, action) => {
        state.conduiteLoading = false;
        state.currentEvaluation = action.payload.evaluation;
        state.sessionTests = action.payload.sessionTests;
        state.questionsByEpreuve = action.payload.questionsByEpreuve;
      })
      .addCase(fetchManagerworkConduite.rejected, (state, action) => {
        state.conduiteLoading = false;
        state.errorMessage = (action.payload as string) ?? action.error.message ?? 'Chargement de la conduite impossible.';
      })
      .addCase(saveManagerworkSessionScore.pending, state => {
        state.actionLoading = true;
        state.progressAnimating = true;
      })
      .addCase(saveManagerworkSessionScore.fulfilled, state => {
        state.actionLoading = false;
        state.progressAnimating = false;
        state.successMessage = 'Score de session enregistre.';
      })
      .addCase(saveManagerworkSessionScore.rejected, (state, action) => {
        state.actionLoading = false;
        state.progressAnimating = false;
        state.errorMessage = (action.payload as string) ?? action.error.message ?? 'Enregistrement du score impossible.';
      })
      .addCase(finalizeManagerworkEvaluation.pending, state => {
        state.actionLoading = true;
        state.progressAnimating = true;
      })
      .addCase(finalizeManagerworkEvaluation.fulfilled, state => {
        state.actionLoading = false;
        state.progressAnimating = false;
        state.successMessage = 'Evaluation finalisee.';
      })
      .addCase(finalizeManagerworkEvaluation.rejected, (state, action) => {
        state.actionLoading = false;
        state.progressAnimating = false;
        state.errorMessage = (action.payload as string) ?? action.error.message ?? 'Finalisation impossible.';
      });
  },
});

export const {
  clearManagerworkFeedback,
  resetManagerworkWizard,
  setManagerworkSelectedEmploye,
  setManagerworkWizardStep,
  toggleManagerworkCompetence,
  toggleManagerworkEpreuve,
} = managerworkSlice.actions;

export default managerworkSlice.reducer;
